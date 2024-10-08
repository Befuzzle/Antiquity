package com.befuzzle.antiquity.block.entity;

import com.befuzzle.antiquity.core.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
   private BlockState blockBeneath;

   public GypsumRoseBlockEntity(BlockPos pPos, BlockState pBlockState) {
      super(EntityRegistry.GYPSUM_ROSE.get(), pPos, pBlockState);
   }

   public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GypsumRoseBlockEntity pBlockEntity) {

      if (pLevel.getGameTime() % 80L == 0L) {
    	 // how much sand is around the gypsum rose?
         pBlockEntity.levels = updateBase(pLevel, pPos, pBlockEntity);
         
         // if activation conditions are met:
         if (pBlockEntity.levels > 0)
            vetrify(pLevel, pPos, pBlockEntity.levels);
      }
   }
   
   //public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, GypsumRoseBlockEntity pBlockEntity) {
   //   
   //}

   private static int updateBase(Level pLevel, BlockPos pPos, GypsumRoseBlockEntity pBlockEntity) {

		BlockState block = pLevel.getBlockState(new BlockPos(pPos.getX(), pPos.getY() - 1, pPos.getZ()));
		
		if (!block.is(BlockTags.SAND) || pPos.getY() <= pLevel.getMinBuildHeight())
			return 0;

		pBlockEntity.blockBeneath = block;
		int radius = 1;

		for (int j = 1; j <= MAX_RADIUS; j++) {

			for (int l = pPos.getX() - j; l <= pPos.getX() + j; ++l) {
				for (int i = pPos.getZ() - j; i <= pPos.getZ() + j; ++i) {
					if (!pLevel.getBlockState(new BlockPos(l, pPos.getY() - 1, i)).is(BlockTags.SAND)) {
						break;
					}
				}
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

   private static void vetrify(Level pLevel, BlockPos pPos, int pLevels) {
      if (!pLevel.isClientSide) {
         double range = (pLevels * AMPLIFICATION_MODIFIER);
         //int amplifier = 0;
         //int duration = (pLevels * 2) * 20;
         AABB aabb = (new AABB(pPos)).inflate(range);
         
         pLevel.getEntitiesOfClass(LivingEntity.class, aabb, mob -> mob instanceof Enemy && mob.isInvertedHealAndHarm())
         .stream().forEach(mob -> mob.hurt(mob.damageSources().dryOut(), 1.0F));
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
   @Override
   public CompoundTag getUpdateTag() {
      return this.saveWithoutMetadata();
   }
}
