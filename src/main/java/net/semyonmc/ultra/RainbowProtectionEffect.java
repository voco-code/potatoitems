package net.semyonmc.ultra;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/** «Защита радуги»: единственное, что спасает от меча «Остановка сердца». */
public class RainbowProtectionEffect extends MobEffect {
	public RainbowProtectionEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x00FFF7);
	}
}
