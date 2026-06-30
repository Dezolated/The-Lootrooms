package com.sp.init;

import com.sp.SPBRevamped;
import com.sp.block.entity.CeilingLightBlockEntity;
import com.sp.block.entity.DrawingMarkerBlockEntity;
import com.sp.block.entity.EmergencyLightBlockEntity;
import com.sp.block.entity.FluorescentLightBlockEntity;
import com.sp.block.entity.ThinFluorescentLightBlockEntity;
import com.sp.block.entity.TinyFluorescentLightBlockEntity;
import com.sp.block.entity.WoodenCrateBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SPBRevamped.NAMESPACE);

    public static final RegistryObject<BlockEntityType<FluorescentLightBlockEntity>> FLUORESCENT_LIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fluorescent_light_block_entity",
                    () -> BlockEntityType.Builder.of(FluorescentLightBlockEntity::new, ModBlocks.FLUORESCENT_LIGHT.get()).build(null));

    // Registers under id "poolrooms_window_block_entity" (preserved verbatim from master, NOT "drawing_marker...").
    public static final RegistryObject<BlockEntityType<DrawingMarkerBlockEntity>> DRAWING_MARKER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("poolrooms_window_block_entity",
                    () -> BlockEntityType.Builder.of(DrawingMarkerBlockEntity::new, ModBlocks.drawingMarker.get()).build(null));

    public static final RegistryObject<BlockEntityType<ThinFluorescentLightBlockEntity>> THIN_FLUORESCENT_LIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("thin_fluorescent_light_block_entity",
                    () -> BlockEntityType.Builder.of(ThinFluorescentLightBlockEntity::new, ModBlocks.THIN_FLUORESCENT_LIGHT.get()).build(null));

    public static final RegistryObject<BlockEntityType<TinyFluorescentLightBlockEntity>> TINY_FLUORESCENT_LIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("tiny_fluorescent_light_block_entity",
                    () -> BlockEntityType.Builder.of(TinyFluorescentLightBlockEntity::new, ModBlocks.TINY_FLUORESCENT_LIGHT.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodenCrateBlockEntity>> WOODEN_CRATE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wooden_crate_block_entity",
                    () -> BlockEntityType.Builder.of(WoodenCrateBlockEntity::new, ModBlocks.WOODEN_CRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<CeilingLightBlockEntity>> CEILING_LIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ceiling_light_block_entity",
                    () -> BlockEntityType.Builder.of(CeilingLightBlockEntity::new, ModBlocks.CEILINGLIGHT.get()).build(null));

    public static final RegistryObject<BlockEntityType<EmergencyLightBlockEntity>> EMERGENCY_LIGHT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("emergency_light_block_entity",
                    () -> BlockEntityType.Builder.of(EmergencyLightBlockEntity::new, ModBlocks.EMERGENCY_LIGHT.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
