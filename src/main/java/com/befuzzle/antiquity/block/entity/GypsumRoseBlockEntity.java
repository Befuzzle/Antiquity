package com.befuzzle.antiquity.block.entity;

import com.befuzzle.antiquity.core.EntityRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class GypsumRoseBlockEntity extends BlockEntity {
   private static final int AMPLIFICATION_MODIFIER = 3;
   public static final int MAX_RADIUS = 2;
   private int levels;
   //private BlockState blockBeneath;

   public GypsumRoseBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(EntityRegistry.GYPSUM_ROSE.get(), pPos, pBlockState);
   }

   public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GypsumRoseBlockEntity pBlockEntity) {

      if (pLevel.getGameTime() % 80L == 0L) {
    	 // how much sand is around the gypsum rose?
         pBlockEntity.levels = updateBase(pLevel, pPos, pBlockEntity);
         
         // if activation conditions are met:
         if (pBlockEntity.levels > 0) {
            applyEffects(pLevel, pPos, pBlockEntity.levels);
            //playSound(pLevel, pPos, SoundEvents.BEACON_AMBIENT);
         }
      }
   }
   
   //public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, GypsumRoseBlockEntity pBlockEntity) {
   //   
   //}

   private static int updateBase(Level level, BlockPos pos, GypsumRoseBlockEntity blockEntity) {

		BlockState block = level.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
		
		if (!block.is(BlockTags.SAND))
			return 0;

		//blockEntity.blockBeneath = block;
		int radius = 1;

		for (int j = 1; j <= MAX_RADIUS; j++) {
			int k = pos.getY() - j;
			if (k < level.getMinBuildHeight()) {
				break;
			}

			boolean flag = true;

			for (int l = pos.getX() - j; l <= pos.getX() + j && flag; ++l) {
				for (int i = pos.getZ() - j; i <= pos.getZ() + j; ++i) {
					if (!level.getBlockState(new BlockPos(l, pos.getY() - 1, i)).is(BlockTags.SAND)) {
						flag = false;
						break;
					}
				}
			}

			if (!flag) {
				break;
			}
			radius++;
		}

		return radius;
	}

   /**
    * Marks this {@code BlockEntity} as no longer valid (removed from the level).
    */
   public void setRemoved() {
      super.setRemoved();
   }

   private static void applyEffects(Level pLevel, BlockPos pPos, int pLevels) {
      if (!pLevel.isClientSide) {
         double effectRange = (double)(pLevels * AMPLIFICATION_MODIFIER);
         int effectAmplifier = 0;
         int effectDuration = (pLevels * 2) * 20;
         
         AABB aabb = (new AABB(pPos)).inflate(effectRange).expandTowards(0.0D, 0.0D, 0.0D);
         
         List<LivingEntity> list = pLevel.getEntitiesOfClass(LivingEntity.class, aabb, (mob) -> {
             return mob instanceof Enemy && mob.isInvertedHealAndHarm();
         });
         
         for(LivingEntity entity : list) {
        	 entity.addEffect(new MobEffectInstance(MobEffects.WITHER, effectDuration, effectAmplifier, true, true));
         }
      }
   }

   public static void playSound(Level pLevel, BlockPos pPos, SoundEvent pSound) {
      pLevel.playSound((Player)null, pPos, pSound, SoundSource.BLOCKS, 1.0F, 1.0F);
   }

   public ClientboundBlockEntityDataPacket getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   /**
    * Get an NBT compound to sync to the client with SPacketChunkData, used for initial loading of the chunk or when
    * many blocks change at once. This compound comes back to you clientside in {@link handleUpdateTag}
    */
   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }
}
