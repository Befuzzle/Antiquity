package com.befuzzle.antiquity.event;

import com.befuzzle.antiquity.Antiquity;
import com.befuzzle.antiquity.core.ParticleRegistry;
import com.befuzzle.antiquity.particle.GypsumRoseParticle;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Antiquity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BusEvents {
	@SubscribeEvent
	public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
		Minecraft.getInstance().particleEngine.register(ParticleRegistry.GYPSUM_ROSE_PARTICLE.get(), GypsumRoseParticle.Provider::new);
	}
}
