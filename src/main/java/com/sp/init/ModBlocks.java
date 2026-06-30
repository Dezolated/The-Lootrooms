package com.sp.init;

import com.sp.SPBRevamped;
import com.sp.block.SprintBlockSoundGroup;
import com.sp.block.custom.*;
import com.sp.block.custom.pipes.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    // NOTE: master applied Fabric's noBlockBreakParticles() to every block; Forge 1.20.1 has no
    // BlockBehaviour.Properties equivalent (would need a LevelRenderer mixin) — dropped for now. TODO(later stage).
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SPBRevamped.NAMESPACE);

    public static final RegistryObject<Block> VOID_BLOCK = reg("void_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    //////Level 1 Stuff//////
    public static final RegistryObject<WallBlock> WALL_BLOCK = reg("wall_block",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f).sound(SprintBlockSoundGroup.WALL)));

    public static final RegistryObject<WallBlock> WALL_BLOCK_2 = reg("wall_block_2",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f).sound(SprintBlockSoundGroup.WALL)));

    public static final RegistryObject<CarpetBlock> CARPET_BLOCK = reg("carpet_block",
            () -> new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).destroyTime(-1f).sound(SprintBlockSoundGroup.CARPET)));

    public static final RegistryObject<Block> CEILING_TILE = reg("ceiling_tile",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f).sound(SprintBlockSoundGroup.CEILING_TILE)));

    public static final RegistryObject<Block> GHOST_CEILING_TILE = reg("ghost_ceiling_tile",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f).noCollission()));

    public static final RegistryObject<FluorescentLightBlock> FLUORESCENT_LIGHT = reg("fluorescent_light",
            () -> new FluorescentLightBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f)));

    public static final RegistryObject<WallText> WALL_ARROW_1 = reg("arrow1",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_ARROW_2 = reg("arrow2",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_ARROW_3 = reg("arrow3",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_ARROW_4 = reg("arrow4",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_SMALL_1 = reg("wall_small_1",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_SMALL_2 = reg("wall_small_2",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_DRAWING_DOOR = reg("wall_drawing_door",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_DRAWING_WINDOW = reg("wall_drawing_window",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<DrawingMarker> drawingMarker = reg("drawing_marker",
            () -> new DrawingMarker(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Rug> RUG_1 = reg("rug1",
            () -> new Rug(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Rug> RUG_2 = reg("rug2",
            () -> new Rug(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<ChainFence> CHAINFENCE = reg("chain_fence",
            () -> new ChainFence(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).destroyTime(-1f)));

    public static final RegistryObject<NewStairs> NEWSTAIRS = reg("new_stairs",
            () -> new NewStairs(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<Block> BRICKS = reg("bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    //////Level 2 and 3 Stuff//////
    public static final RegistryObject<ThinFluorescentLightBlock> THIN_FLUORESCENT_LIGHT = reg("thin_fluorescent_light",
            () -> new ThinFluorescentLightBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f)));

    public static final RegistryObject<EmergencyLightBlock> EMERGENCY_LIGHT = reg("emergency_light",
            () -> new EmergencyLightBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f)));

    public static final RegistryObject<BottomTrim> BOTTOM_TRIM = reg("bottom_trim",
            () -> new BottomTrim(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noCollission().noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_1 = reg("concrete1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_2 = reg("concrete2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_5 = reg("concrete5",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_6 = reg("concrete6",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_7 = reg("concrete7",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_9 = reg("concrete9",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<SlabBlock> CONCRETE_BLOCK_9_SLAB = reg("concrete9slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_10 = reg("concrete10",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_11 = reg("concrete11",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<Block> CONCRETE_BLOCK_12 = reg("concrete12",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<ThinPipe> THIN_PIPE = reg("thin_pipe",
            () -> new ThinPipe(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<ThinPipeCorner> THIN_PIPE_CORNER = reg("thin_pipe_corner",
            () -> new ThinPipeCorner(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Pipe> PIPE = reg("pipe",
            () -> new Pipe(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Pipe> PIPE_MIDDLE = reg("pipe_middle",
            () -> new Pipe(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<PipeCorner> PIPE_CORNER = reg("pipe_corner",
            () -> new PipeCorner(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Pipe> BIG_PIPE = reg("big_pipe",
            () -> new Pipe(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Pipe> BIG_PIPE_MIDDLE = reg("big_pipe_middle",
            () -> new Pipe(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<SmallPipeSet> SMALL_PIPE_SET = reg("small_pipe_set",
            () -> new SmallPipeSet(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_1 = reg("wall_text_1",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_2 = reg("wall_text_2",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_3 = reg("wall_text_3",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_4 = reg("wall_text_4",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_5 = reg("wall_text_5",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_6 = reg("wall_text_6",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_7 = reg("wall_text_7",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_8 = reg("wall_text_8",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WallText> WALL_TEXT_99 = reg("wall_text_99",
            () -> new WallText(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().noCollission().sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<WoodenCrate> WOODEN_CRATE = reg("wooden_crate",
            () -> new WoodenCrate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f)));

    //////Poolrooms Stuff//////
    public static final RegistryObject<GlassBlock> POOLROOMS_SKY_BLOCK = reg("pool_sky",
            () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f).sound(SprintBlockSoundGroup.SILENT)));

    public static final RegistryObject<Block> POOL_TILES = reg("pool_tiles",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<PoolTileWall> POOL_TILE_WALL = reg("pool_tile_wall",
            () -> new PoolTileWall(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f)));

    public static final RegistryObject<PoolTileSlopeBlock> POOL_TILE_SLOPE = reg("slope",
            () -> new PoolTileSlopeBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).noOcclusion().sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<CeilingLight> CEILINGLIGHT = reg("ceiling_light",
            () -> new CeilingLight(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f)));

    ///////Infinite Fields Stuff//////
    public static final RegistryObject<UtilityPole> POWER_POLE_TOP = reg("power_pole_top",
            () -> new UtilityPole(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f), 5));

    public static final RegistryObject<UtilityPole> POWER_POLE = reg("power_pole",
            () -> new UtilityPole(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).destroyTime(-1f), 5));

    public static final RegistryObject<Block> DIRT = reg("dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).destroyTime(-1f).sound(SprintBlockSoundGroup.GRASS2)));

    ///////Level 324 Stuff//////
    public static final RegistryObject<Block> ROAD = reg("road",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<Block> RED_DIRT = reg("red_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).destroyTime(-1f).sound(SprintBlockSoundGroup.GRASS2)));

    public static final RegistryObject<Block> PLASTIC = reg("plastic",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.SHROOMLIGHT)));

    public static final RegistryObject<Block> NONE_REFLECTIVE_PLASTIC = reg("none_reflective_plastic",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.SHROOMLIGHT)));

    public static final RegistryObject<RedMetalCasingBlock> RED_METAL_CASING = reg("red_metal_casing",
            () -> new RedMetalCasingBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.METAL)));

    public static final RegistryObject<PillarBlock> PILLAR = reg("pillar",
            () -> new PillarBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.SHROOMLIGHT)));

    public static final RegistryObject<UtilityPole> POLE = reg("pole",
            () -> new UtilityPole(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.SHROOMLIGHT), 6));

    public static final RegistryObject<LampBlock> LAMP = reg("lamp",
            () -> new LampBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.METAL)));

    public static final RegistryObject<WindowBlock> WINDOW = reg("window",
            () -> new WindowBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.GLASS)));

    public static final RegistryObject<TinyFluorescentLightBlock> TINY_FLUORESCENT_LIGHT = reg("tiny_fluorescent_light",
            () -> new TinyFluorescentLightBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).destroyTime(-1f)));

    public static final RegistryObject<Block> FLOOR_TILING = reg("floor_tiling",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<DoubleSidedShelfBlock> DOUBLE_SIDED_SHELF = reg("double_sided_shelf",
            () -> new DoubleSidedShelfBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    public static final RegistryObject<DoubleSidedShelfBlock> ONE_SIDED_SHELF = reg("one_sided_shelf",
            () -> new DoubleSidedShelfBlock(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(net.minecraft.world.level.block.SoundType.WOOD)));

    public static final RegistryObject<Block> PAVEMENT = reg("pavement",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).destroyTime(-1f).sound(SprintBlockSoundGroup.CONCRETE)));

    private static <T extends Block> RegistryObject<T> reg(String name, Supplier<T> sup) {
        return BLOCKS.register(name, sup);
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    public static void init() {

    }
}
