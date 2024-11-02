package com.befuzzle.antiquity.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class GypsumRoseParticle extends TextureSheetParticle {

	protected GypsumRoseParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet spriteSheet, double pXSpeed, double pYSpeed, double pZSpeed) {
		super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
		
		this.friction = 0.8F;
		// Velocity
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		
		this.quadSize *= 0.5F;
		this.lifetime = 20;
		this.setSpriteFromAge(spriteSheet);
		
		this.rCol = 1F;
		this.gCol = 1F;
		this.bCol = 1F;
		
	}
	
	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Provider implements ParticleProvider<SimpleParticleType>{
		private final SpriteSet sprites;

		public Provider(SpriteSet spriteSet) {
			this.sprites = spriteSet;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ,
				double pXSpeed, double pYSpeed, double pZSpeed) {
			return new GypsumRoseParticle(pLevel, pX, pY, pZ, this.sprites, pXSpeed, pYSpeed, pZSpeed);
		}
	}

}
