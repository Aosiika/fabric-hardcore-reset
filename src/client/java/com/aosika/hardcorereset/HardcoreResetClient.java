package com.aosika.hardcorereset;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.CompletableFuture;

public class HardcoreResetClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HardcoreResetMod.LOGGER.info("[Hardcore Reset] Cliente inicializado. Registrando manejador de reseteo...");
        HardcoreResetMod.setClientResetHandler(HardcoreResetClient::handleWorldReset);
    }

    private static final java.util.concurrent.atomic.AtomicBoolean AUTO_CREATE_PENDING = new java.util.concurrent.atomic.AtomicBoolean(false);

    public static boolean isAutoCreatePending() {
        return AUTO_CREATE_PENDING.get();
    }

    public static void setAutoCreatePending(boolean pending) {
        AUTO_CREATE_PENDING.set(pending);
    }

    public static void handleWorldReset(Path worldDirectory) {
        Minecraft client = Minecraft.getInstance();

        client.execute(() -> {
            HardcoreResetMod.LOGGER.info("[Hardcore Reset] Desconectando del servidor local para liberar bloqueos de archivo...");

            client.disconnect(new GenericMessageScreen(Component.translatable("hardcore_reset.disconnect.releasing_files")), false);

            CompletableFuture.runAsync(() -> {
                HardcoreResetMod.LOGGER.info("[Hardcore Reset] Iniciando eliminación asíncrona de: {}", worldDirectory);
                boolean deleted = deleteDirectoryWithRetry(worldDirectory, 10, 250);

                if (deleted) {
                    HardcoreResetMod.LOGGER.info("[Hardcore Reset] Carpeta del mundo eliminada correctamente.");
                } else {
                    HardcoreResetMod.LOGGER.error("[Hardcore Reset] No se pudo eliminar la carpeta por completo: {}", worldDirectory);
                }

                client.execute(() -> {
                    HardcoreResetMod.RESET_IN_PROGRESS.set(false);
                    HardcoreResetMod.LOGGER.info("[Hardcore Reset] Disparando creación automática de nuevo mundo Hardcore...");
                    AUTO_CREATE_PENDING.set(true);
                    CreateWorldScreen.openFresh(client, () -> client.gui.setScreen(new TitleScreen()));
                });
            });
        });
    }

    private static boolean deleteDirectoryWithRetry(Path directory, int maxAttempts, long delayMillis) {
        if (!Files.exists(directory)) {
            return true;
        }

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                deleteDirectoryRecursively(directory);
                if (!Files.exists(directory)) {
                    return true;
                }
            } catch (Exception e) {
                HardcoreResetMod.LOGGER.warn("[Hardcore Reset] Intento {}/{} de eliminar '{}' falló: {}. Reintentando...",
                        attempt, maxAttempts, directory.getFileName(), e.getMessage());
            }

            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return !Files.exists(directory);
    }

    private static void deleteDirectoryRecursively(Path rootPath) throws IOException {
        if (!Files.exists(rootPath)) {
            return;
        }

        Files.walkFileTree(rootPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc != null) {
                    throw exc;
                }
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
