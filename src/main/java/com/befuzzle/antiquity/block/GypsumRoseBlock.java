package com.befuzzle.antiquity.block;

import javax.annotation.Nullable;

import com.befuzzle.antiquity.block.entity.GypsumRoseBlockEntity;
import com.befuzzle.antiquity.core.EntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GypsumRoseBlock extends BaseEntityBlock {
	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

	public GypsumRoseBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
	      return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GypsumRoseBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> entityType) {
		return level.isClientSide ? null : 
			createTickerHelper(entityType, EntityRegistry.GYPSUM_ROSE.get(), GypsumRoseBlockEntity::serverTick);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
	   double d0 = (double)pPos.getX() + 0.5D;
	   double d1 = (double)pPos.getY() + 0.6D;
	   double d2 = (double)pPos.getZ() + 0.5D;
	   if (pRandom.nextInt(5) == 0) {
		  pLevel.addParticle(ParticleTypes.END_ROD, d0, d1, d2, pRandom.nextGaussian() * 0.005D, pRandom.nextGaussian() * -0.005D, pRandom.nextGaussian() * 0.005D);
	   }
	}
}
