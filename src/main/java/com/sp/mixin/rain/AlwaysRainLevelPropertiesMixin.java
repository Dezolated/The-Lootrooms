package com.sp.mixin.rain;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelProperties.class)
public class AlwaysRainLevelPropertiesMixin {
    @WrapMethod(method = "getRainTime")
    private int spbrevamped$alwaysRainGetRainTime(Operation<Integer> original) {
        return original.call();
    }

    @WrapMethod(method = "isRaining")
    private boolean spbrevamped$IsRaining(Operation<Boolean> original) {
        return original.call();
    }

    @WrapMethod(method = "getThunderTime")
    private int spbrevamped$alwaysRainGetThunderTime(Operation<Integer> original) {
        return original.call();
    }

    @WrapMethod(method = "isThundering")
    private boolean spbrevamped$IsThundering(Operation<Boolean> original) {
        return original.call();
    }
}
