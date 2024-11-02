package com.befuzzle.antiquity.core;

import com.befuzzle.antiquity.Antiquity;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,
			Antiquity.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> GYPSUM_ROSE_PARTICLE =
			PARTICLE_TYPES.register("gypsum_rose_particle", () -> new SimpleParticleType(true));
	
	public static void register(IEventBus eventBus) {
		PARTICLE_TYPES.register(eventBus);
	}
}
