package com.sp.item;

import com.sp.SPBRevamped;
import com.sp.init.ModBlocks;
import com.sp.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SPBRevamped.NAMESPACE);

    public static final RegistryObject<CreativeModeTab> BACKROOMS = TABS.register("spbrevamped", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.spbrevamped"))
                    .icon(() -> new ItemStack(ModBlocks.WALL_BLOCK.get()))
                    .displayItems((params, output) ->
                            ModItems.ITEMS.getEntries().forEach(e -> output.accept(e.get())))
                    .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
