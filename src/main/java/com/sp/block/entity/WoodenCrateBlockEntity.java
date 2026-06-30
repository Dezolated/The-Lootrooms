package com.sp.block.entity;

import com.sp.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// Stage 1 STUB: registration-only. Inventory/menu (MenuProvider, Container) and NBT
// are TODO(Stage 3/4) once the item/menu systems are ported. The block's use() is
// stubbed to PASS so this BE only needs to register as a type for now.
public class WoodenCrateBlockEntity extends BlockEntity {
    public WoodenCrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOODEN_CRATE_BLOCK_ENTITY.get(), pos, state);
    }
}
