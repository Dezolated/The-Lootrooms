package com.sp.block.entity;

import com.sp.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Stage 1 STUB: registration-only. Registers under id "poolrooms_window_block_entity"
// (preserved from master). Drawing/render logic is TODO(Stage 3/4).
public class DrawingMarkerBlockEntity extends BlockEntity {
    public DrawingMarkerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRAWING_MARKER_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        // TODO(Stage 3/4): port drawing-marker behaviour
    }
}
