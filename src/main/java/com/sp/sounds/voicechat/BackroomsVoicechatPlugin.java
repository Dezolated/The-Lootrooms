package com.sp.sounds.voicechat;

import com.sp.SPBRevamped;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;

/**
 * Minimal Simple Voice Chat plugin. Skinwalker logic removed in the Phase 1
 * strip-down; this is the base for a future party-talk feature.
 */
public class BackroomsVoicechatPlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return SPBRevamped.MOD_ID;
    }

    @Override
    public void initialize(VoicechatApi api) {
    }
}
