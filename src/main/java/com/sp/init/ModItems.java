package com.sp.init;

import com.sp.SPBRevamped;
import com.sp.item.custom.Backshroom;
import com.sp.item.custom.CannedFood;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SPBRevamped.NAMESPACE);

    // ── Level 1 ─────────────────────────────────────────────────────────────
    public static final RegistryObject<Item> VOID_BLOCK = blockItem("void_block", ModBlocks.VOID_BLOCK);
    public static final RegistryObject<Item> WALL_BLOCK = blockItem("wall_block", ModBlocks.WALL_BLOCK);
    public static final RegistryObject<Item> WALL_BLOCK_2 = blockItem("wall_block_2", ModBlocks.WALL_BLOCK_2);
    public static final RegistryObject<Item> CARPET_BLOCK = blockItem("carpet_block", ModBlocks.CARPET_BLOCK);
    public static final RegistryObject<Item> CEILING_TILE = blockItem("ceiling_tile", ModBlocks.CEILING_TILE);
    public static final RegistryObject<Item> GHOST_CEILING_TILE = blockItem("ghost_ceiling_tile", ModBlocks.GHOST_CEILING_TILE);
    public static final RegistryObject<Item> FLUORESCENT_LIGHT = blockItem("fluorescent_light", ModBlocks.FLUORESCENT_LIGHT);
    public static final RegistryObject<Item> WALL_ARROW_1 = blockItem("arrow1", ModBlocks.WALL_ARROW_1);
    public static final RegistryObject<Item> WALL_ARROW_2 = blockItem("arrow2", ModBlocks.WALL_ARROW_2);
    public static final RegistryObject<Item> WALL_ARROW_3 = blockItem("arrow3", ModBlocks.WALL_ARROW_3);
    public static final RegistryObject<Item> WALL_ARROW_4 = blockItem("arrow4", ModBlocks.WALL_ARROW_4);
    public static final RegistryObject<Item> WALL_SMALL_1 = blockItem("wall_small_1", ModBlocks.WALL_SMALL_1);
    public static final RegistryObject<Item> WALL_SMALL_2 = blockItem("wall_small_2", ModBlocks.WALL_SMALL_2);
    public static final RegistryObject<Item> WALL_DRAWING_DOOR = blockItem("wall_drawing_door", ModBlocks.WALL_DRAWING_DOOR);
    public static final RegistryObject<Item> WALL_DRAWING_WINDOW = blockItem("wall_drawing_window", ModBlocks.WALL_DRAWING_WINDOW);
    public static final RegistryObject<Item> DRAWING_MARKER = blockItem("drawing_marker", ModBlocks.drawingMarker);
    public static final RegistryObject<Item> RUG_1 = blockItem("rug1", ModBlocks.RUG_1);
    public static final RegistryObject<Item> RUG_2 = blockItem("rug2", ModBlocks.RUG_2);
    public static final RegistryObject<Item> CHAINFENCE = blockItem("chain_fence", ModBlocks.CHAINFENCE);
    public static final RegistryObject<Item> NEWSTAIRS = blockItem("new_stairs", ModBlocks.NEWSTAIRS);
    public static final RegistryObject<Item> BRICKS = blockItem("bricks", ModBlocks.BRICKS);

    // ── Level 2 / 3 ─────────────────────────────────────────────────────────
    public static final RegistryObject<Item> THIN_FLUORESCENT_LIGHT = blockItem("thin_fluorescent_light", ModBlocks.THIN_FLUORESCENT_LIGHT);
    public static final RegistryObject<Item> EMERGENCY_LIGHT = blockItem("emergency_light", ModBlocks.EMERGENCY_LIGHT);
    public static final RegistryObject<Item> BOTTOM_TRIM = blockItem("bottom_trim", ModBlocks.BOTTOM_TRIM);
    public static final RegistryObject<Item> CONCRETE_BLOCK_1 = blockItem("concrete1", ModBlocks.CONCRETE_BLOCK_1);
    public static final RegistryObject<Item> CONCRETE_BLOCK_2 = blockItem("concrete2", ModBlocks.CONCRETE_BLOCK_2);
    public static final RegistryObject<Item> CONCRETE_BLOCK_5 = blockItem("concrete5", ModBlocks.CONCRETE_BLOCK_5);
    public static final RegistryObject<Item> CONCRETE_BLOCK_6 = blockItem("concrete6", ModBlocks.CONCRETE_BLOCK_6);
    public static final RegistryObject<Item> CONCRETE_BLOCK_7 = blockItem("concrete7", ModBlocks.CONCRETE_BLOCK_7);
    public static final RegistryObject<Item> CONCRETE_BLOCK_9 = blockItem("concrete9", ModBlocks.CONCRETE_BLOCK_9);
    public static final RegistryObject<Item> CONCRETE_BLOCK_9_SLAB = blockItem("concrete9slab", ModBlocks.CONCRETE_BLOCK_9_SLAB);
    public static final RegistryObject<Item> CONCRETE_BLOCK_10 = blockItem("concrete10", ModBlocks.CONCRETE_BLOCK_10);
    public static final RegistryObject<Item> CONCRETE_BLOCK_11 = blockItem("concrete11", ModBlocks.CONCRETE_BLOCK_11);
    public static final RegistryObject<Item> CONCRETE_BLOCK_12 = blockItem("concrete12", ModBlocks.CONCRETE_BLOCK_12);
    public static final RegistryObject<Item> THIN_PIPE = blockItem("thin_pipe", ModBlocks.THIN_PIPE);
    public static final RegistryObject<Item> THIN_PIPE_CORNER = blockItem("thin_pipe_corner", ModBlocks.THIN_PIPE_CORNER);
    public static final RegistryObject<Item> PIPE = blockItem("pipe", ModBlocks.PIPE);
    public static final RegistryObject<Item> PIPE_MIDDLE = blockItem("pipe_middle", ModBlocks.PIPE_MIDDLE);
    public static final RegistryObject<Item> PIPE_CORNER = blockItem("pipe_corner", ModBlocks.PIPE_CORNER);
    public static final RegistryObject<Item> BIG_PIPE = blockItem("big_pipe", ModBlocks.BIG_PIPE);
    public static final RegistryObject<Item> BIG_PIPE_MIDDLE = blockItem("big_pipe_middle", ModBlocks.BIG_PIPE_MIDDLE);
    public static final RegistryObject<Item> SMALL_PIPE_SET = blockItem("small_pipe_set", ModBlocks.SMALL_PIPE_SET);
    public static final RegistryObject<Item> WALL_TEXT_1 = blockItem("wall_text_1", ModBlocks.WALL_TEXT_1);
    public static final RegistryObject<Item> WALL_TEXT_2 = blockItem("wall_text_2", ModBlocks.WALL_TEXT_2);
    public static final RegistryObject<Item> WALL_TEXT_3 = blockItem("wall_text_3", ModBlocks.WALL_TEXT_3);
    public static final RegistryObject<Item> WALL_TEXT_4 = blockItem("wall_text_4", ModBlocks.WALL_TEXT_4);
    public static final RegistryObject<Item> WALL_TEXT_5 = blockItem("wall_text_5", ModBlocks.WALL_TEXT_5);
    public static final RegistryObject<Item> WALL_TEXT_6 = blockItem("wall_text_6", ModBlocks.WALL_TEXT_6);
    public static final RegistryObject<Item> WALL_TEXT_7 = blockItem("wall_text_7", ModBlocks.WALL_TEXT_7);
    public static final RegistryObject<Item> WALL_TEXT_8 = blockItem("wall_text_8", ModBlocks.WALL_TEXT_8);
    public static final RegistryObject<Item> WALL_TEXT_99 = blockItem("wall_text_99", ModBlocks.WALL_TEXT_99);
    public static final RegistryObject<Item> WOODEN_CRATE = blockItem("wooden_crate", ModBlocks.WOODEN_CRATE);

    // ── Poolrooms ────────────────────────────────────────────────────────────
    public static final RegistryObject<Item> POOLROOMS_SKY_BLOCK = blockItem("pool_sky", ModBlocks.POOLROOMS_SKY_BLOCK);
    public static final RegistryObject<Item> POOL_TILES = blockItem("pool_tiles", ModBlocks.POOL_TILES);
    public static final RegistryObject<Item> POOL_TILE_WALL = blockItem("pool_tile_wall", ModBlocks.POOL_TILE_WALL);
    public static final RegistryObject<Item> POOL_TILE_SLOPE = blockItem("slope", ModBlocks.POOL_TILE_SLOPE);
    public static final RegistryObject<Item> CEILINGLIGHT = blockItem("ceiling_light", ModBlocks.CEILINGLIGHT);

    // ── Infinite Fields ──────────────────────────────────────────────────────
    public static final RegistryObject<Item> POWER_POLE_TOP = blockItem("power_pole_top", ModBlocks.POWER_POLE_TOP);
    public static final RegistryObject<Item> POWER_POLE = blockItem("power_pole", ModBlocks.POWER_POLE);
    public static final RegistryObject<Item> DIRT = blockItem("dirt", ModBlocks.DIRT);

    // ── Level 324 ────────────────────────────────────────────────────────────
    public static final RegistryObject<Item> ROAD = blockItem("road", ModBlocks.ROAD);
    public static final RegistryObject<Item> RED_DIRT = blockItem("red_dirt", ModBlocks.RED_DIRT);
    public static final RegistryObject<Item> PLASTIC = blockItem("plastic", ModBlocks.PLASTIC);
    public static final RegistryObject<Item> NONE_REFLECTIVE_PLASTIC = blockItem("none_reflective_plastic", ModBlocks.NONE_REFLECTIVE_PLASTIC);
    public static final RegistryObject<Item> RED_METAL_CASING = blockItem("red_metal_casing", ModBlocks.RED_METAL_CASING);
    public static final RegistryObject<Item> PILLAR = blockItem("pillar", ModBlocks.PILLAR);
    public static final RegistryObject<Item> POLE = blockItem("pole", ModBlocks.POLE);
    public static final RegistryObject<Item> LAMP = blockItem("lamp", ModBlocks.LAMP);
    public static final RegistryObject<Item> WINDOW = blockItem("window", ModBlocks.WINDOW);
    public static final RegistryObject<Item> TINY_FLUORESCENT_LIGHT = blockItem("tiny_fluorescent_light", ModBlocks.TINY_FLUORESCENT_LIGHT);
    public static final RegistryObject<Item> FLOOR_TILING = blockItem("floor_tiling", ModBlocks.FLOOR_TILING);
    public static final RegistryObject<Item> DOUBLE_SIDED_SHELF = blockItem("double_sided_shelf", ModBlocks.DOUBLE_SIDED_SHELF);
    public static final RegistryObject<Item> ONE_SIDED_SHELF = blockItem("one_sided_shelf", ModBlocks.ONE_SIDED_SHELF);
    public static final RegistryObject<Item> PAVEMENT = blockItem("pavement", ModBlocks.PAVEMENT);

    // ── Standalone food items ────────────────────────────────────────────────
    public static final RegistryObject<Item> BACKSHROOM = ITEMS.register("backshroom",
            () -> new Backshroom(new Item.Properties().food(ModFoodComponents.BACKSHROOM)));

    public static final RegistryObject<Item> CANNED_FOOD = ITEMS.register("canned_food",
            () -> new CannedFood(new Item.Properties().food(ModFoodComponents.CANNED_FOOD)));

    // ── Helper ───────────────────────────────────────────────────────────────
    private static RegistryObject<Item> blockItem(String name, RegistryObject<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
