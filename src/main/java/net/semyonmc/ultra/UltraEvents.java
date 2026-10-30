package net.semyonmc.ultra;

import java.util.List;
import java.util.Locale;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public final class UltraEvents {
	private static final EquipmentSlot[] ARMOR_SLOTS = {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	// Чары, которые «Пожиратель душ» получает за убийства (по слотам)
	private static final List<ResourceKey<Enchantment>> COMMON = List.of(
			Enchantments.PROTECTION, Enchantments.FIRE_PROTECTION, Enchantments.BLAST_PROTECTION,
			Enchantments.PROJECTILE_PROTECTION, Enchantments.THORNS, Enchantments.UNBREAKING);
	private static final List<ResourceKey<Enchantment>> HEAD = List.of(
			Enchantments.RESPIRATION, Enchantments.AQUA_AFFINITY);
	private static final List<ResourceKey<Enchantment>> LEGS = List.of(
			Enchantments.SWIFT_SNEAK);
	private static final List<ResourceKey<Enchantment>> FEET = List.of(
			Enchantments.FEATHER_FALLING, Enchantments.DEPTH_STRIDER, Enchantments.SOUL_SPEED);

	private static int tickCounter = 0;

	private UltraEvents() {
	}

	public static void init() {
		// ---------- МЕЧ: убийство с одного удара, молния, контра — «Защита радуги» ----------
		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
			if (!(entity.level() instanceof ServerLevel level)) {
				return true;
			}
			if (!(source.getEntity() instanceof ServerPlayer player)) {
				return true;
			}
			if (!player.getMainHandItem().is(UltraItems.ULTRA_SWORD)) {
				return true;
			}
			if (entity.hasEffect(UltraEffects.RAINBOW_PROTECTION)) {
				return true; // под защитой радуги — обычный урон меча
			}

			double x = entity.getX();
			double y = entity.getY();
			double z = entity.getZ();

			// generic_kill обходит неуязвимость, тотемы и сон
			entity.hurtServer(level, level.damageSources().genericKill(), Float.MAX_VALUE);

			strikeLightning(level, x, y, z, player);
			feedSoulArmor(player, level);
			return false; // исходный удар отменяем
		});

		// ---------- Пожиратель душ: чары за любые убийства ----------
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((level, entity, killedEntity, damageSource) -> {
			if (entity instanceof ServerPlayer player && !player.getMainHandItem().is(UltraItems.ULTRA_SWORD)) {
				feedSoulArmor(player, level);
			}
		});

		// ---------- КИРКА и остальные инструменты: ломают любой блок мгновенно ----------
		AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> {
			if (level.isClientSide() || !(level instanceof ServerLevel)) {
				return InteractionResult.PASS;
			}
			if (player.isSpectator() || player.isCreative() || !player.getAbilities().mayBuild) {
				return InteractionResult.PASS;
			}
			ItemStack tool = player.getMainHandItem();
			if (!UltraItems.isUltraTool(tool)) {
				return InteractionResult.PASS;
			}
			BlockState state = level.getBlockState(pos);
			if (state.isAir() || state.getDestroySpeed(level, pos) < 0.0F) {
				return InteractionResult.PASS; // бедрок и прочее неразрушимое
			}
			level.destroyBlock(pos, true, player);
			return InteractionResult.SUCCESS;
		});

		// ---------- Серверный тик: автопочинка брони и «Защита радуги» от тотема ----------
		ServerTickEvents.END_SERVER_TICK.register(UltraEvents::onServerTick);
	}

	private static void onServerTick(MinecraftServer server) {
		tickCounter++;
		boolean repair = tickCounter % 20 == 0;

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (repair) {
				for (EquipmentSlot slot : ARMOR_SLOTS) {
					ItemStack stack = player.getItemBySlot(slot);
					if (UltraItems.isSoulArmor(stack) && stack.isDamaged()) {
						stack.setDamageValue(0);
					}
				}
			}

			// Тотем бессмертия даёт Регенерацию II (45 с) + Поглощение II сразу после срабатывания.
			// Ловим этот момент и выдаём «Защиту радуги» на 60 секунд.
			MobEffectInstance regen = player.getEffect(MobEffects.REGENERATION);
			MobEffectInstance absorption = player.getEffect(MobEffects.ABSORPTION);
			if (regen != null && absorption != null
					&& regen.getAmplifier() == 1 && regen.getDuration() >= 890
					&& absorption.getAmplifier() == 1 && absorption.getDuration() >= 90
					&& !player.hasEffect(UltraEffects.RAINBOW_PROTECTION)) {
				player.addEffect(new MobEffectInstance(UltraEffects.RAINBOW_PROTECTION, 1200, 0, false, true, true));
			}
		}
	}

	private static void strikeLightning(ServerLevel level, double x, double y, double z, ServerPlayer attacker) {
		MinecraftServer server = level.getServer();
		// чтобы молния не поджигала самого владельца меча
		attacker.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, false, false, false));
		String cmd = String.format(Locale.ROOT, "summon minecraft:lightning_bolt %.2f %.2f %.2f", x, y, z);
		server.getCommands().performPrefixedCommand(
				server.createCommandSourceStack().withLevel(level).withSuppressedOutput(), cmd);
	}

	private static void feedSoulArmor(ServerPlayer player, ServerLevel level) {
		for (EquipmentSlot slot : ARMOR_SLOTS) {
			ItemStack stack = player.getItemBySlot(slot);
			if (!UltraItems.isSoulArmor(stack)) {
				continue;
			}
			ResourceKey<Enchantment> key = pickEnchantment(slot, player);
			Holder<Enchantment> holder = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
			int current = EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
			int next = Math.min(255, current + 1 + player.getRandom().nextInt(2));
			stack.enchant(holder, next);
		}
	}

	private static ResourceKey<Enchantment> pickEnchantment(EquipmentSlot slot, ServerPlayer player) {
		List<ResourceKey<Enchantment>> pool = new java.util.ArrayList<>(COMMON);
		switch (slot) {
			case HEAD -> pool.addAll(HEAD);
			case LEGS -> pool.addAll(LEGS);
			case FEET -> pool.addAll(FEET);
			default -> { }
		}
		return pool.get(player.getRandom().nextInt(pool.size()));
	}
}
