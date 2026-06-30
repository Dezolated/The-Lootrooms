package com.sp.block.entity;

import com.sp.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Stage 1 STUB: registration-only. Light-state machine, NBT sync, and rendering
// hooks are TODO(Stage 3/4) once CCA/WorldEvents/networking are ported.
public class EmergencyLightBlockEntity extends BlockEntity {
    public EmergencyLightBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EMERGENCY_LIGHT_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        // TODO(Stage 3/4): port emergency-light / redstone behaviour
    }
}
