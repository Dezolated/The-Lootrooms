package com.sp.mixin;

import com.sp.cca_stuff.InitializeComponents;
import com.sp.cca_stuff.PlayerComponent;
import com.sp.init.BackroomsLevels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z", ordinal = 0, shift = At.Shift.AFTER))
    private void enableNoclip(CallbackInfo ci) {
        PlayerComponent playerComponent = InitializeComponents.PLAYER.get(this);

        if (playerComponent.shouldNoClip()) {
            this.noClip = playerComponent.shouldNoClip();
        }
    }

    // Potential fix for: https://github.com/SpacePotatoee/MinecraftFoundFootage/issues/85
    // IDK tho. I am just throwing shit at the wall to see what sticks.
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (damageSource.isOf(DamageTypes.OUT_OF_WORLD)) {
            if (BackroomsLevels.getLevel(player.getWorld()).isPresent()) {
                player.setPosition(0, 100, 0);
            } else {
                player.setPosition(BackroomsLevels.getLevel(player.getWorld()).get().getSpawnPos());
            }
        }
    }
}
