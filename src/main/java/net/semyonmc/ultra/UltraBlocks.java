package net.semyonmc.ultra;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class UltraBlocks {
	public static final Block RAINBOW_ORE = register("rainbow_ore",
			BlockBehaviour.Properties.of()
					.strength(30.0F, 1200.0F)
					.requiresCorrectToolForDrops()
					.sound(SoundType.DEEPSLATE)
					.lightLevel(state -> 7));

	private UltraBlocks() {
	}

	private static Block register(String name, BlockBehaviour.Properties props) {
		ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, UltraMod.id(name));
		Block block = new Block(props.setId(blockKey));

		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, UltraMod.id(name));
		BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
		Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
	}

	public static void init() {
	}
}
