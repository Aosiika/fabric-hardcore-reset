package com.aosika.hardcorereset;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class HardcoreResetMod implements ModInitializer {
    public static final String MOD_ID = "hardcore_reset";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final AtomicBoolean RESET_IN_PROGRESS = new AtomicBoolean(false);
    private static Consumer<Path> clientResetHandler = null;

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "HardcoreReset-Scheduler");
        thread.setDaemon(true);
        return thread;
    });

    @Override
    public void onInitialize() {
        HardcoreResetConfig.load();
        LOGGER.info("[Hardcore Reset] Mod inicializado. Intento actual: #{}", HardcoreResetConfig.getCurrentAttempt());

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (server.isSingleplayer()) {
                HardcoreResetConfig.startNewRun();
                LOGGER.info("[Hardcore Reset] Nueva run iniciada para el Intento #{}", HardcoreResetConfig.getCurrentAttempt());
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("hardcore")
                .requires(source -> true)
                .then(Commands.literal("reset")
                    .requires(source -> true)
                    .executes(context -> {
                        HardcoreResetConfig.resetCounter();
                        context.getSource().sendSuccess(() -> Component.translatable("hardcore_reset.command.reset_success"), true);
                        return 1;
                    })
                )
            );
        });
    }

    public static void setClientResetHandler(Consumer<Path> handler) {
        clientResetHandler = handler;
    }

    public static void triggerHardcoreReset(ServerPlayer player) {
        if (!RESET_IN_PROGRESS.compareAndSet(false, true)) {
            return;
        }

        MinecraftServer server = player.level().getServer();
        if (server == null || !server.isSingleplayer()) {
            RESET_IN_PROGRESS.set(false);
            return;
        }

        int finishedAttempt = HardcoreResetConfig.getCurrentAttempt();
        String runDurationFormatted = HardcoreResetConfig.getFormattedElapsedTime();

        HardcoreResetConfig.recordDeathAndAdvanceAttempt();
        int nextAttempt = HardcoreResetConfig.getCurrentAttempt();

        LOGGER.info("[Hardcore Reset] ¡Muerte en Hardcore para '{}'! Intento #{} finalizado en {}. Iniciando Intento #{}...",
                player.getScoreboardName(), finishedAttempt, runDurationFormatted, nextAttempt);

        try {
            player.setHealth(player.getMaxHealth());
            player.removeAllEffects();
            player.setInvulnerable(true);
            player.setGameMode(GameType.SPECTATOR);

            Path worldDirectory = server.getWorldPath(LevelResource.ROOT).toAbsolutePath();
            LOGGER.info("[Hardcore Reset] Directorio del mundo a resetear: {}", worldDirectory);

            AtomicInteger secondsLeft = new AtomicInteger(10);
            ScheduledFuture<?>[] countdownTask = new ScheduledFuture<?>[1];

            countdownTask[0] = SCHEDULER.scheduleAtFixedRate(() -> {
                int remaining = secondsLeft.getAndDecrement();
                HardcoreResetConfig.setDeathCountdownSeconds(remaining);

                if (remaining > 0) {
                    player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 25, 5));
                    player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("hardcore_reset.death.title")));
                    player.connection.send(new ClientboundSetSubtitleTextPacket(Component.translatable(
                            "hardcore_reset.death.countdown", remaining, finishedAttempt, runDurationFormatted, nextAttempt
                    )));
                } else {
                    if (countdownTask[0] != null) {
                        countdownTask[0].cancel(false);
                    }

                    player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 30, 10));
                    player.connection.send(new ClientboundSetTitleTextPacket(Component.translatable("hardcore_reset.death.generating")));
                    player.connection.send(new ClientboundSetSubtitleTextPacket(Component.translatable("hardcore_reset.death.loading", nextAttempt)));

                    if (clientResetHandler != null) {
                        clientResetHandler.accept(worldDirectory);
                    } else {
                        LOGGER.warn("[Hardcore Reset] No hay ClientResetHandler registrado. No se puede proceder con la desconexión del cliente.");
                        RESET_IN_PROGRESS.set(false);
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);

        } catch (Exception e) {
            LOGGER.error("[Hardcore Reset] Error durante la ejecución del reseteo Hardcore", e);
            RESET_IN_PROGRESS.set(false);
        }
    }
}
