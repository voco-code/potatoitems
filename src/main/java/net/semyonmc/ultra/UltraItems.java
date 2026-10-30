package net.semyonmc.ultra;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public final class UltraItems {
	// ---------- материал брони «Пожиратель душ» ----------
	public static final int BASE_DURABILITY = 120;
	public static final ResourceKey<EquipmentAsset> SOUL_EATER_ASSET =
			ResourceKey.create(EquipmentAssets.ROOT_ID, UltraMod.id("soul_eater"));
	public static final TagKey<Item> REPAIRS_SOUL_ARMOR =
			TagKey.create(Registries.ITEM, UltraMod.id("repairs_soul_armor"));
	public static final ArmorMaterial SOUL_EATER = new ArmorMaterial(
			BASE_DURABILITY,
			Map.of(
					ArmorType.HELMET, 6,
					ArmorType.CHESTPLATE, 12,
					ArmorType.LEGGINGS, 9,
					ArmorType.BOOTS, 6
			),
			30,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			8.0F,
			0.2F,
			REPAIRS_SOUL_ARMOR,
			SOUL_EATER_ASSET
	);

	// ---------- предметы ----------
	public static final Item ULTRA_DIAMOND = register("ultra_diamond", Item::new,
			new Item.Properties().rarity(Rarity.EPIC).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

	public static final Item ULTRA_SWORD = register("ultra_sword", Item::new,
			new Item.Properties()
					.sword(ToolMaterial.NETHERITE, 3.0F, -2.4F)
					.rarity(Rarity.EPIC)
					.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
					.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
							.add(Attributes.ATTACK_DAMAGE,
									new AttributeModifier(UltraMod.id("sword_damage"), 100.0, AttributeModifier.Operation.ADD_VALUE),
									EquipmentSlotGroup.MAINHAND)
							.add(Attributes.ATTACK_SPEED,
									new AttributeModifier(UltraMod.id("sword_speed"), -1.8, AttributeModifier.Operation.ADD_VALUE),
									EquipmentSlotGroup.MAINHAND)
							.build()));

	public static final Item ULTRA_PICKAXE = register("ultra_pickaxe", Item::new,
			new Item.Properties()
					.pickaxe(ToolMaterial.NETHERITE, 1.0F, -2.8F)
					.rarity(Rarity.EPIC)
					.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

	public static final Item ULTRA_AXE = register("ultra_axe",
			props -> new AxeItem(ToolMaterial.NETHERITE, 5.0F, -3.0F, props),
			new Item.Properties()
					.rarity(Rarity.EPIC)
					.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

	public static final Item ULTRA_SHOVEL = register("ultra_shovel",
			props -> new ShovelItem(ToolMaterial.NETHERITE, 1.5F, -3.0F, props),
			new Item.Properties()
					.rarity(Rarity.EPIC)
					.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

	public static final Item ULTRA_HOE = register("ultra_hoe",
			props -> new HoeItem(ToolMaterial.NETHERITE, -4.0F, 0.0F, props),
			new Item.Properties()
					.rarity(Rarity.EPIC)
					.component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));

	public static final Item ULTRA_HELMET = register("ultra_helmet", Item::new,
			new Item.Properties().humanoidArmor(SOUL_EATER, ArmorType.HELMET)
					.durability(ArmorType.HELMET.getDurability(BASE_DURABILITY))
					.rarity(Rarity.EPIC));
	public static final Item ULTRA_CHESTPLATE = register("ultra_chestplate", Item::new,
			new Item.Properties().humanoidArmor(SOUL_EATER, ArmorType.CHESTPLATE)
					.durability(ArmorType.CHESTPLATE.getDurability(BASE_DURABILITY))
					.rarity(Rarity.EPIC));
	public static final Item ULTRA_LEGGINGS = register("ultra_leggings", Item::new,
			new Item.Properties().humanoidArmor(SOUL_EATER, ArmorType.LEGGINGS)
					.durability(ArmorType.LEGGINGS.getDurability(BASE_DURABILITY))
					.rarity(Rarity.EPIC));
	public static final Item ULTRA_BOOTS = register("ultra_boots", Item::new,
			new Item.Properties().humanoidArmor(SOUL_EATER, ArmorType.BOOTS)
					.durability(ArmorType.BOOTS.getDurability(BASE_DURABILITY))
					.rarity(Rarity.EPIC));

	// ---------- вкладка креатива ----------
	public static final ResourceKey<CreativeModeTab> TAB_KEY =
			ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), UltraMod.id("ultra"));
	public static final CreativeModeTab TAB = FabricItemGroup.builder()
			.icon(() -> new ItemStack(ULTRA_DIAMOND))
			.title(Component.translatable("itemGroup.ultra"))
			.displayItems((params, output) -> {
				output.accept(ULTRA_DIAMOND);
				output.accept(UltraBlocks.RAINBOW_ORE);
				output.accept(ULTRA_SWORD);
				output.accept(ULTRA_PICKAXE);
				output.accept(ULTRA_AXE);
				output.accept(ULTRA_SHOVEL);
				output.accept(ULTRA_HOE);
				output.accept(ULTRA_HELMET);
				output.accept(ULTRA_CHESTPLATE);
				output.accept(ULTRA_LEGGINGS);
				output.accept(ULTRA_BOOTS);
			})
			.build();

	private UltraItems() {
	}

	private static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties props) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, UltraMod.id(name));
		T item = factory.apply(props.setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static boolean isSoulArmor(ItemStack stack) {
		return stack.is(ULTRA_HELMET) || stack.is(ULTRA_CHESTPLATE) || stack.is(ULTRA_LEGGINGS) || stack.is(ULTRA_BOOTS);
	}

	public static boolean isUltraTool(ItemStack stack) {
		return stack.is(ULTRA_PICKAXE) || stack.is(ULTRA_AXE) || stack.is(ULTRA_SHOVEL) || stack.is(ULTRA_HOE);
	}

	public static void init() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB);

		// Ещё и в обычные вкладки каталога
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.accept(ULTRA_SWORD);
			entries.accept(ULTRA_AXE);
			entries.accept(ULTRA_HELMET);
			entries.accept(ULTRA_CHESTPLATE);
			entries.accept(ULTRA_LEGGINGS);
			entries.accept(ULTRA_BOOTS);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
			entries.accept(ULTRA_PICKAXE);
			entries.accept(ULTRA_AXE);
			entries.accept(ULTRA_SHOVEL);
			entries.accept(ULTRA_HOE);
		});
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
				.register(entries -> entries.accept(ULTRA_DIAMOND));
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS)
				.register(entries -> entries.accept(UltraBlocks.RAINBOW_ORE));
	}
}
