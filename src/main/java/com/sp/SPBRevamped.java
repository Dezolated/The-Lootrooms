package com.sp;

import com.sp.cca_stuff.InitializeComponents;
import com.sp.cca_stuff.PlayerComponent;
import com.sp.command.EventCommand;
import com.sp.command.GimmeMyInventoryBack;
import com.sp.command.LevelCommand;
import com.sp.compat.modmenu.ConfigStuff;
import com.sp.init.*;
import com.sp.item.ModItemGroups;
import com.sp.mixininterfaces.NewServerProperties;
import com.sp.networking.InitializePackets;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

import java.util.UUID;

public class SPBRevamped implements ModInitializer {
	public static final String MOD_ID = "spb-revamped";
    public static final Logger LOGGER = LoggerFactory.getLogger("spb-revamped");
	public static final int FINAL_MAZE_SIZE = 5;

	private static final UUID SLOW_SPEED_MODIFIER_ID = UUID.fromString("6a11099c-c3b8-4eba-9dad-f0c0bb997d35");
	public static final EntityAttributeModifier SLOW_SPEED_MODIFIER = new EntityAttributeModifier(SLOW_SPEED_MODIFIER_ID, "SPBRevamped slow walk speed", -0.2f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

	@Override
	public void onInitialize() {
		BackroomsLevels.init();

		ModItems.registerModItems();
		ModSounds.registerSounds();
		InitializePackets.registerC2SPackets();
		ModItemGroups.registerItemGroups();
		ModBlocks.init();
		ModBlockEntities.registerAllBlockEntities();
		MidnightConfig.init(MOD_ID, ConfigStuff.class);
		ModGamerules.registerGamerules();

		CommandRegistrationCallback.EVENT.register(EventCommand::register);
		CommandRegistrationCallback.EVENT.register(LevelCommand::register);
		CommandRegistrationCallback.EVENT.register(GimmeMyInventoryBack::register);

		GeckoLib.initialize();

		LOGGER.info("\"WOOOOOOOOOOOOOOOOOOOOOOOooooooooooooooooooooooooo..........\" -He said as he fell into the backrooms, never to be seen again.");

		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((player, origin, destination) -> {
			PacketByteBuf buffer = PacketByteBufs.create();
			ServerPlayNetworking.send(player, InitializePackets.RELOAD_LIGHTS, buffer);
		}));
	}

	public static void sendCameraShakePacket(ServerPlayerEntity player, double speed, double trauma){
		PacketByteBuf buffer = PacketByteBufs.create();
		buffer.writeDouble(speed);
		buffer.writeDouble(trauma);
		ServerPlayNetworking.send(player, InitializePackets.SCREEN_SHAKE, buffer);
	}

	public static void sendBlackScreenPacket(ServerPlayerEntity player, int duration, boolean shouldPauseSounds, boolean noEscape){
		PacketByteBuf buffer = PacketByteBufs.create();
		buffer.writeInt(duration);
		buffer.writeBoolean(shouldPauseSounds);
		buffer.writeBoolean(noEscape);
		ServerPlayNetworking.send(player, InitializePackets.BLACK_SCREEN, buffer);
	}

	public static void sendPersonalPlaySoundPacket(ServerPlayerEntity player, SoundEvent sound, float volume, float pitch){
		PacketByteBuf buffer = PacketByteBufs.create();
		buffer.writeRegistryEntry(Registries.SOUND_EVENT.getIndexedEntries(), RegistryEntry.of(sound), (packetByteBuf, soundEvent) -> soundEvent.writeBuf(packetByteBuf));
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
		ServerPlayNetworking.send(player, InitializePackets.SOUND, buffer);
	}

	public static void sendLevelTransitionLightsOutPacket(ServerPlayerEntity player, int time) {
		PlayerComponent component = InitializeComponents.PLAYER.get(player);
		component.setTeleporting(true);
		PacketByteBuf buffer = PacketByteBufs.create();
		buffer.writeInt(time);
		ServerPlayNetworking.send(player, InitializePackets.LEVEL_TRANSITION_LIGHTSOUT, buffer);
	}

    public static int getExitSpawnRadius(World world) {
        int exitRadius = ConfigStuff.exitSpawnRadius;

        if (world.getServer() != null) {
            if (world.getServer().isDedicated()) {
                exitRadius = ((NewServerProperties) ((MinecraftDedicatedServer) world.getServer()).getProperties()).getExitSpawnRadius();
            }
        }

        return exitRadius;
    }
}