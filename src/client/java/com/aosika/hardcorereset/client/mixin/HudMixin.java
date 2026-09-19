package com.aosika.hardcorereset.client.mixin;

import com.aosika.hardcorereset.HardcoreResetConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {

    @Shadow
    public abstract boolean isHidden();

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void hardcore_reset$renderScoreboardHud(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();

        if (this.isHidden() || client.level == null || client.player == null) {
            return;
        }

        if (!client.level.getLevelData().isHardcore()) {
            return;
        }

        Font font = client.font;
        int screenWidth = client.getWindow().getGuiScaledWidth();

        int attempt = HardcoreResetConfig.getCurrentAttempt();
        String formattedTime = HardcoreResetConfig.getFormattedElapsedTime();
        long lastDuration = HardcoreResetConfig.getLastRunDurationSeconds();

        Component title = Component.translatable("hardcore_reset.hud.title");
        Component attemptText = Component.translatable("hardcore_reset.hud.attempt", attempt);
        Component timeText = Component.translatable("hardcore_reset.hud.time", formattedTime);
        Component lastRunText = lastDuration > 0
                ? Component.translatable("hardcore_reset.hud.last_run", HardcoreResetConfig.formatDuration(lastDuration))
                : null;

        int countdown = HardcoreResetConfig.getDeathCountdownSeconds();
        Component countdownText = countdown > 0
                ? Component.translatable("hardcore_reset.hud.countdown", countdown)
                : null;

        int maxTextWidth = Math.max(font.width(title), Math.max(font.width(attemptText), font.width(timeText)));
        if (lastRunText != null) {
            maxTextWidth = Math.max(maxTextWidth, font.width(lastRunText));
        }
        if (countdownText != null) {
            maxTextWidth = Math.max(maxTextWidth, font.width(countdownText));
        }

        int padding = 6;
        int boxWidth = maxTextWidth + (padding * 2);
        int lineCount = 3 + (lastRunText != null ? 1 : 0) + (countdownText != null ? 1 : 0);
        int boxHeight = (font.lineHeight * lineCount) + (padding * 2) + ((lineCount - 1) * 3);

        int x = screenWidth - boxWidth - 8;
        int y = 8;

        extractor.fill(x, y, x + boxWidth, y + boxHeight, 0x90000000);
        extractor.fill(x, y, x + boxWidth, y + 2, 0xFFFF2222);

        int textY = y + padding + 1;
        int titleX = x + (boxWidth - font.width(title)) / 2;
        extractor.text(font, title, titleX, textY, 0xFFFFFFFF, true);

        textY += font.lineHeight + 4;
        extractor.text(font, attemptText, x + padding, textY, 0xFFFFFFFF, true);

        textY += font.lineHeight + 3;
        extractor.text(font, timeText, x + padding, textY, 0xFFFFFFFF, true);

        if (countdownText != null) {
            textY += font.lineHeight + 3;
            extractor.text(font, countdownText, x + padding, textY, 0xFFFFFFFF, true);
        }

        if (lastRunText != null) {
            textY += font.lineHeight + 3;
            extractor.text(font, lastRunText, x + padding, textY, 0xFFAAAAAA, true);
        }
    }
}
