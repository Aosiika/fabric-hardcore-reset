package com.aosika.hardcorereset.mixin;

import com.aosika.hardcorereset.HardcoreResetMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void hardcore_reset$onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        if (player.level().getServer() == null) {
            return;
        }

        boolean isSingleplayer = player.level().getServer().isSingleplayer();
        boolean isHardcore = player.level().getLevelData().isHardcore() || player.level().getServer().isHardcore();

        if (isSingleplayer && isHardcore) {
            ci.cancel();
            HardcoreResetMod.triggerHardcoreReset(player);
        }
    }
}
