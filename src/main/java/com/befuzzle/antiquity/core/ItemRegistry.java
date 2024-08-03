package com.befuzzle.antiquity.core;

import com.befuzzle.antiquity.Antiquity;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			Antiquity.MOD_ID);

	public static final RegistryObject<Item> GYPSUM_ROSE = ITEMS.register("gypsum_rose",
			() -> new BlockItem(BlockRegistry.GYPSUM_ROSE.get(),
					new Item.Properties().rarity(Rarity.UNCOMMON)));
	

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
