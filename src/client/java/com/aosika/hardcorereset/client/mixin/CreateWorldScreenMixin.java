package com.aosika.hardcorereset.client.mixin;

import com.aosika.hardcorereset.HardcoreResetClient;
import com.aosika.hardcorereset.HardcoreResetMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {

    @Shadow
    private void onCreate() {}

    @Inject(method = "init", at = @At("TAIL"))
    private void hardcore_reset$autoCreateWorld(CallbackInfo ci) {
        if (HardcoreResetClient.isAutoCreatePending()) {
            HardcoreResetClient.setAutoCreatePending(false);
            CreateWorldScreen screen = (CreateWorldScreen) (Object) this;

            HardcoreResetMod.LOGGER.info("[Hardcore Reset] Disparando creación automática de mundo Hardcore (0 clics)...");

            screen.getUiState().setGameMode(WorldCreationUiState.SelectedGameMode.HARDCORE);
            screen.getUiState().setName(net.minecraft.client.resources.language.I18n.get("hardcore_reset.world.name", com.aosika.hardcorereset.HardcoreResetConfig.getCurrentAttempt()));

            Minecraft.getInstance().execute(this::onCreate);
        }
    }
}
