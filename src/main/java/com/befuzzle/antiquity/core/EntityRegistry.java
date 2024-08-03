package com.befuzzle.antiquity.core;

import com.befuzzle.antiquity.Antiquity;
import com.befuzzle.antiquity.block.entity.GypsumRoseBlockEntity;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Antiquity.MOD_ID);

	public static final RegistryObject<BlockEntityType<GypsumRoseBlockEntity>> GYPSUM_ROSE = BLOCK_ENTITY_TYPES
			.register("gypsum_rose",
					() -> BlockEntityType.Builder.of(GypsumRoseBlockEntity::new, BlockRegistry.GYPSUM_ROSE.get())
							.build(Util.fetchChoiceType(References.BLOCK_ENTITY,
									new ResourceLocation(Antiquity.MOD_ID, "gypsum_rose").toString())));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITY_TYPES.register(eventBus);
	}
}
