package com.befuzzle.antiquity.block.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableInt;

import com.befuzzle.antiquity.core.EntityRegistry;

public class AncientBellBlockEntity extends BlockEntity {
   private static final int DURATION = 50;
   private static final int GLOW_DURATION = 60;
   private static final int MIN_TICKS_BETWEEN_SEARCHES = 120;
   private static final int MAX_RESONATION_TICKS = 40;
   private static final int TICKS_BEFORE_RESONATION = 5;
   private static final int SEARCH_RADIUS = 48;
   private static final int HEAR_BELL_RADIUS = 32;
   private static final int HIGHLIGHT_RAIDERS_RADIUS = 48;
   private long lastRingTimestamp;
   /** How many ticks the bell has been ringing. */
   public int ticks;
   public boolean shaking;
   public Direction clickDirection;
   private List<LivingEntity> nearbyEntities;
   private boolean resonating;
   /**
    * A tick counter before raiders are revealed. At {@link #TICKS_BEFORE_RESONATION} ticks, the resonation sound is
    * played, and at {@link #MAX_RESONATION_TICKS}, nearby raiders are revealed.
    */
   private int resonationTicks;

   public AncientBellBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(EntityRegistry.ANCIENT_BELL.get(), pPos, pBlockState);
   }

   public boolean triggerEvent(int pId, int pType) {
      if (pId == 1) {
         this.updateEntities();
         this.resonationTicks = 0;
         this.clickDirection = Direction.from3DDataValue(pType);
         this.ticks = 0;
         this.shaking = true;
         return true;
      } else {
         return super.triggerEvent(pId, pType);
      }
   }

   private static void tick(Level pLevel, BlockPos pPos, BlockState pState, AncientBellBlockEntity pBlockEntity, AncientBellBlockEntity.ResonationEndAction pResonationEndAction) {
      if (pBlockEntity.shaking) {
         ++pBlockEntity.ticks;
      }

      if (pBlockEntity.ticks >= DURATION) {
         pBlockEntity.shaking = false;
         pBlockEntity.ticks = 0;
      }

      if (pBlockEntity.ticks >= TICKS_BEFORE_RESONATION && pBlockEntity.resonationTicks == 0 && areTargetsNearby(pPos, pBlockEntity.nearbyEntities)) {
         pBlockEntity.resonating = true;
         pLevel.playSound((Player)null, pPos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
      }

      if (pBlockEntity.resonating) {
         if (pBlockEntity.resonationTicks < MAX_RESONATION_TICKS) {
            ++pBlockEntity.resonationTicks;
         } else {
            pResonationEndAction.run(pLevel, pPos, pBlockEntity.nearbyEntities);
            pBlockEntity.resonating = false;
         }
      }

   }

   public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, AncientBellBlockEntity pBlockEntity) {
      tick(pLevel, pPos, pState, pBlockEntity, AncientBellBlockEntity::showBellParticles);
   }

   public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AncientBellBlockEntity pBlockEntity) {
      tick(pLevel, pPos, pState, pBlockEntity, AncientBellBlockEntity::makeRaidersGlow);
   }

   public void onHit(Direction pDirection) {
      BlockPos blockpos = this.getBlockPos();
      this.clickDirection = pDirection;
      if (this.shaking) {
         this.ticks = 0;
      } else {
         this.shaking = true;
      }

      this.level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, pDirection.get3DDataValue());
   }

   private void updateEntities() {
      BlockPos blockpos = this.getBlockPos();
      if (this.level.getGameTime() > this.lastRingTimestamp + MIN_TICKS_BETWEEN_SEARCHES || this.nearbyEntities == null) {
         this.lastRingTimestamp = this.level.getGameTime();
         AABB aabb = (new AABB(blockpos)).inflate(HIGHLIGHT_RAIDERS_RADIUS);
         this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
      }

      if (!this.level.isClientSide) {
         for(LivingEntity livingentity : this.nearbyEntities) {
            if (livingentity.isAlive() && !livingentity.isRemoved() && blockpos.closerToCenterThan(livingentity.position(), HEAR_BELL_RADIUS)) {
               livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
            }
         }
      }

   }

   private static boolean areTargetsNearby(BlockPos pPos, List<LivingEntity> pTargets) {
      for(LivingEntity livingentity : pTargets) {
         if (livingentity.isAlive() && !livingentity.isRemoved() && pPos.closerToCenterThan(livingentity.position(), HEAR_BELL_RADIUS) && livingentity.isInvertedHealAndHarm()) {
        	 
        	 System.out.println("Bam!");
        	 
        	 return true;
         }
      }

      return false;
   }

   private static void makeRaidersGlow(Level p_155187_, BlockPos p_155188_, List<LivingEntity> p_155189_) {
      p_155189_.stream().filter((p_155219_) -> {
         return isTargetWithinRange(p_155188_, p_155219_);
      }).forEach(AncientBellBlockEntity::glow);
   }

   private static void showBellParticles(Level p_155208_, BlockPos p_155209_, List<LivingEntity> p_155210_) {
      MutableInt mutableint = new MutableInt(16700985);
      int i = (int)p_155210_.stream().filter((p_289508_) -> {
         return p_155209_.closerToCenterThan(p_289508_.position(), HIGHLIGHT_RAIDERS_RADIUS);
      }).count();
      p_155210_.stream().filter((p_155213_) -> {
         return isTargetWithinRange(p_155209_, p_155213_);
      }).forEach((p_155195_) -> {
         double d0 = Math.sqrt((p_155195_.getX() - (double)p_155209_.getX()) * (p_155195_.getX() - (double)p_155209_.getX()) + (p_155195_.getZ() - (double)p_155209_.getZ()) * (p_155195_.getZ() - (double)p_155209_.getZ()));
         double d1 = (double)((float)p_155209_.getX() + 0.5F) + 1.0D / d0 * (p_155195_.getX() - (double)p_155209_.getX());
         double d2 = (double)((float)p_155209_.getZ() + 0.5F) + 1.0D / d0 * (p_155195_.getZ() - (double)p_155209_.getZ());
         int j = Mth.clamp((i - 21) / -2, 3, 15);

         for(int k = 0; k < j; ++k) {
            int l = mutableint.addAndGet(5);
            double d3 = (double)FastColor.ARGB32.red(l) / 255.0D;
            double d4 = (double)FastColor.ARGB32.green(l) / 255.0D;
            double d5 = (double)FastColor.ARGB32.blue(l) / 255.0D;
            p_155208_.addParticle(ParticleTypes.ENTITY_EFFECT, d1, (double)((float)p_155209_.getY() + 0.5F), d2, d3, d4, d5);
         }

      });
   }

   private static boolean isTargetWithinRange(BlockPos pPos, LivingEntity pEntity) {
      return pEntity.isAlive() && !pEntity.isRemoved() && pPos.closerToCenterThan(pEntity.position(), SEARCH_RADIUS) && pEntity.isInvertedHealAndHarm();
   }

   private static void glow(LivingEntity pEntity) {
	   pEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION));
   }

   @FunctionalInterface
   interface ResonationEndAction {
      void run(Level pLevel, BlockPos pPos, List<LivingEntity> pRaiders);
   }
}