package com.sp;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(SPBRevamped.MOD_ID)
public class SPBRevamped {
    // NOTE: Forge mod IDs must match ^[a-z][a-z0-9_]{1,63}$ — hyphens are forbidden.
    // The Fabric mod used "spb-revamped"; the Forge port uses "spb_revamped".
    // All resource paths (data/, assets/) that referenced spb-revamped are unchanged —
    // they are updated in a follow-up task when resources are ported.
    public static final String MOD_ID = "spb_revamped";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SPBRevamped() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // DeferredRegister registrations attached here in later tasks:
        // ModSounds.register(modBus); ModBlocks.register(modBus); ModItems.register(modBus);
        // ModBlockEntities.register(modBus); ModCreativeTab.register(modBus);
        LOGGER.info("SP-Backrooms Revamped (Forge) initializing");
    }
}
