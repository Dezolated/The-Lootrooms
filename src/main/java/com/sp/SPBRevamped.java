package com.sp;

import com.sp.init.ModBlockEntities;
import com.sp.init.ModBlocks;
import com.sp.init.ModItems;
import com.sp.init.ModSounds;
import com.sp.item.ModCreativeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(SPBRevamped.MOD_ID)
public class SPBRevamped {
    // Forge mod IDs must match ^[a-z][a-z0-9_]{1,63}$ — hyphens are forbidden, so the
    // Fabric mod id "spb-revamped" became "spb_revamped" here. Used ONLY for @Mod,
    // mods.toml, and the mod event bus.
    public static final String MOD_ID = "spb_revamped";

    // ResourceLocation namespaces DO allow hyphens, so every asset/data path keeps the
    // original "spb-revamped" namespace — assets copy 1:1 with zero rename. Use this
    // (NOT MOD_ID) for every DeferredRegister.create(registry, NAMESPACE) and
    // ResourceLocation(NAMESPACE, ...) in the registration layer (Tasks 6–9).
    public static final String NAMESPACE = "spb-revamped";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SPBRevamped() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // DeferredRegister registrations attached here in later tasks:
        ModSounds.register(modBus);
        ModBlocks.register(modBus);
        ModBlockEntities.register(modBus);
        ModItems.register(modBus);
        ModCreativeTab.register(modBus);
        LOGGER.info("SP-Backrooms Revamped (Forge) initializing");
    }
}
