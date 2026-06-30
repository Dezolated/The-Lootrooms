package com.sp.init;

import com.sp.SPBRevamped;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SPBRevamped.NAMESPACE);

    public static final RegistryObject<SoundEvent> SILENCE = reg("silence");

    public static final RegistryObject<SoundEvent> FLASHLIGHT_CLICK = reg("flashlight_click");

    public static final RegistryObject<SoundEvent> AMBIENCE = reg("ambience");

    public static final RegistryObject<SoundEvent> FALLING = reg("falling");
    public static final RegistryObject<SoundEvent> GLITCH = reg("glitch");

    public static final RegistryObject<SoundEvent> NO_ESCAPE = reg("noescape");

    public static final RegistryObject<SoundEvent> FLUORESCENT_LIGHT_HUM = reg("fluorescent_light");
    public static final RegistryObject<SoundEvent> FLUORESCENT_LIGHT_HUM2 = reg("fluorescent_light2");

    public static final RegistryObject<SoundEvent> LIGHTS_OUT = reg("lights_out");
    public static final RegistryObject<SoundEvent> LIGHTS_ON = reg("lights_on");
    public static final RegistryObject<SoundEvent> LIGHT_BLINK = reg("light_blink");

    public static final RegistryObject<SoundEvent> INTERCOM_BASIC1 = reg("intercom_basic1");
    public static final RegistryObject<SoundEvent> INTERCOM_BASIC2 = reg("intercom_basic2");
    public static final RegistryObject<SoundEvent> INTERCOM_FRIEND = reg("intercom_friend");
    public static final RegistryObject<SoundEvent> INTERCOM_REVERSED = reg("intercom_reversed");
    public static final RegistryObject<SoundEvent> CREEPY_MUSIC1 = reg("creepy_music1");
    public static final RegistryObject<SoundEvent> CREEPY_MUSIC2 = reg("creepy_music2");
    public static final RegistryObject<SoundEvent> FAR_CROWD = reg("far_crowd");

    public static final RegistryObject<SoundEvent> LEVEL1_AMBIENCE1 = reg("level1_ambience1");
    public static final RegistryObject<SoundEvent> LEVEL1_AMBIENCE2 = reg("level1_ambience2");
    public static final RegistryObject<SoundEvent> LEVEL1_AMBIENCE3 = reg("level1_ambience3");
    public static final RegistryObject<SoundEvent> LEVEL1_AMBIENCE4 = reg("level1_ambience4");

    public static final RegistryObject<SoundEvent> WATER_PIPE = reg("water_pipe");
    public static final RegistryObject<SoundEvent> GAS_PIPE = reg("gas_pipe");
    public static final RegistryObject<SoundEvent> CREAKING1 = reg("creaking1");
    public static final RegistryObject<SoundEvent> CREAKING2 = reg("creaking2");
    public static final RegistryObject<SoundEvent> LEVEL2_AMBIENCE = reg("level2_ambience");
    public static final RegistryObject<SoundEvent> LEVEL2_WARP_CREAKING_LOOP = reg("level2_warp_creaking_loop");

    public static final RegistryObject<SoundEvent> SWIM = reg("swim");
    public static final RegistryObject<SoundEvent> POOLROOMS_AMBIENCE_NOON = reg("poolrooms_ambience_noon");
    public static final RegistryObject<SoundEvent> POOLROOMS_AMBIENCE_SUNSET = reg("poolrooms_ambience_sunset");
    public static final RegistryObject<SoundEvent> POOLROOMS_SPLASH1 = reg("poolrooms_splash1");
    public static final RegistryObject<SoundEvent> POOLROOMS_SPLASH2 = reg("poolrooms_splash2");
    public static final RegistryObject<SoundEvent> POOLROOMS_DRIP1 = reg("poolrooms_drip1");
    public static final RegistryObject<SoundEvent> POOLROOMS_DRIP2 = reg("poolrooms_drip2");
    public static final RegistryObject<SoundEvent> SUNSET_TRANSITION = reg("sunset_transition");
    public static final RegistryObject<SoundEvent> MIDNIGHT_TRANSITION = reg("midnight_transition");
    public static final RegistryObject<SoundEvent> SUNSET_TRANSITION_END = reg("sunset_transition_end");

    public static final RegistryObject<SoundEvent> INFINITE_GRASS_AMBIENCE = reg("infinite_grass_ambience");
    public static final RegistryObject<SoundEvent> INFINITE_GRASS_SOUNDEVENT = reg("infinite_grass_soundevent");
    public static final RegistryObject<SoundEvent> INFINITE_GRASS_SOUNDEVENT_FAR = reg("infinite_grass_soundevent_far");

    // WINDTUNNEL_GRASS_AMBIENCE ("wind_tunnel_ambience") intentionally excluded — no sounds.json entry (known dead).

    public static final RegistryObject<SoundEvent> SKINWALKER_AMBIENCE = reg("skinwalker_ambience");
    public static final RegistryObject<SoundEvent> SKINWALKER_CHASE = reg("skinwalker_chase");
    public static final RegistryObject<SoundEvent> SKINWALKER_NOTICE = reg("skinwalker_notice");
    public static final RegistryObject<SoundEvent> SKINWALKER_SNIFF = reg("skinwalker_sniff");
    public static final RegistryObject<SoundEvent> SKINWALKER_FOOTSTEP = reg("skinwalker_footstep");
    public static final RegistryObject<SoundEvent> SKINWALKER_BONE_CRACK = reg("skinwalker_bone_crack");
    public static final RegistryObject<SoundEvent> SKINWALKER_BONE_CRACK_LONG = reg("skinwalker_bone_crack_long");
    public static final RegistryObject<SoundEvent> SKINWALKER_REVEAL = reg("skinwalker_reveal");
    public static final RegistryObject<SoundEvent> JUMPSCARE = reg("jumpscare");
    public static final RegistryObject<SoundEvent> SKINWALKER_RELEASE = reg("skinwalker_release");

    public static final RegistryObject<SoundEvent> WALKER_FOOTSTEP = reg("walker_footstep");

    public static final RegistryObject<SoundEvent> SMILER_AMBIENCE = reg("smiler_laugh");
    public static final RegistryObject<SoundEvent> SMILER_GLITCH = reg("smiler_glitch");

    public static final RegistryObject<SoundEvent> CARPET_WALK = reg("carpet_walk");
    public static final RegistryObject<SoundEvent> CARPET_RUN = reg("carpet_run");

    public static final RegistryObject<SoundEvent> CONCRETE_WALK = reg("concrete_walk");
    public static final RegistryObject<SoundEvent> CONCRETE_RUN = reg("concrete_run");

    public static final RegistryObject<SoundEvent> GRASS_WALK = reg("grass_walk");
    public static final RegistryObject<SoundEvent> GRASS_RUN = reg("grass_run");

    public static final RegistryObject<SoundEvent> EMERGENCY_LIGHT_ALARM = reg("emergency_light_alarm");

    public static final RegistryObject<SoundEvent> SCREECH_SOUNDEVENT_FAR = reg("walker_screech");

    private static RegistryObject<SoundEvent> reg(String name) {
        return SOUNDS.register(name,
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SPBRevamped.NAMESPACE, name)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
