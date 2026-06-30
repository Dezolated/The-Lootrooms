package com.sp;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SPBRevamped.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SPBRevampedClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // TODO(Stage 4): Veil hooks, renderers, key binds, block render layers
    }
}
