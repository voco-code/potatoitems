package net.semyonmc.ultra;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public final class UltraEffects {
	public static final Holder<MobEffect> RAINBOW_PROTECTION = Registry.registerForHolder(
			BuiltInRegistries.MOB_EFFECT, UltraMod.id("rainbow_protection"), new RainbowProtectionEffect());

	private UltraEffects() {
	}

	public static void init() {
	}
}
