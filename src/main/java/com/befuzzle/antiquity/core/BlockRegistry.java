package com.befuzzle.antiquity.core;

import com.befuzzle.antiquity.Antiquity;
import com.befuzzle.antiquity.block.AncientBellBlock;
import com.befuzzle.antiquity.block.GypsumRoseBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Antiquity.MOD_ID);

	public static final RegistryObject<Block> ANCIENT_BELL = BLOCKS.register("ancient_bell",
			() -> new AncientBellBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)
					.requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).noOcclusion().mapColor(MapColor.METAL)));

	public static final RegistryObject<Block> GYPSUM_ROSE = BLOCKS
			.register("gypsum_rose",
					() -> new GypsumRoseBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER)
							.requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.AMETHYST_CLUSTER)
							.noOcclusion().mapColor(MapColor.SAND).lightLevel((gypsumRoseLight) -> {
								return 4;
							})));

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}
}
