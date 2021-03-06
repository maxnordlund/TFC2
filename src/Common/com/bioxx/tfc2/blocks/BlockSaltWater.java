package com.bioxx.tfc2.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;

import com.bioxx.tfc2.TFCBlocks;

public class BlockSaltWater extends BlockFluidClassic {

	public BlockSaltWater(Fluid fluid, Material material) 
	{
		super(fluid, material);
		this.setTickRate(3);
	}

	@Override
	public float getFluidHeightForRender(IBlockAccess world, BlockPos pos)
	{
		IBlockState here = world.getBlockState(pos);
		IBlockState up = world.getBlockState(pos.down(densityDir));
		if (here.getBlock() instanceof BlockFluidBase)
		{
			if (up.getBlock().getMaterial().isLiquid() || up.getBlock() instanceof IFluidBlock)
			{
				return 1;
			}

			if (getMetaFromState(here) == getMaxRenderHeightMeta())
			{
				return 0.875F;
			}
		}
		return !here.getBlock().getMaterial().isSolid() && up.getBlock() == this ? 1 : this.getQuantaPercentage(world, pos) * 0.875F;
	}

	@Override
	protected boolean canFlowInto(IBlockAccess world, BlockPos pos)
	{
		if (world.isAirBlock(pos)) return true;

		Block block = world.getBlockState(pos).getBlock();
		if (block == this || block == TFCBlocks.SaltWater || block == TFCBlocks.SaltWaterStatic || block == TFCBlocks.FreshWaterStatic)
		{
			return true;
		}

		if (displacements.containsKey(block))
		{
			return displacements.get(block);
		}

		Material material = block.getMaterial();
		if (material.blocksMovement()  ||
				material == Material.water ||
				material == Material.lava  ||
				material == Material.portal)
		{
			return false;
		}

		int density = getDensity(world, pos);
		if (density == Integer.MAX_VALUE)
		{
			return true;
		}

		if (this.density > density)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isSourceBlock(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock() instanceof BlockFluidBase && ((Integer)world.getBlockState(pos).getValue(LEVEL)).intValue() == 0;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.updateTick(world, pos, state, rand);
		int myLevel = ((Integer)state.getValue(LEVEL)).intValue();
		if(state.getBlock() == this && myLevel > 0)
		{
			int count = 0;
			if(isSourceBlock(world, pos.north())) count++;
			if(isSourceBlock(world, pos.south())) count++;
			if(isSourceBlock(world, pos.east())) count++;
			if(isSourceBlock(world, pos.west())) count++;

			if(count > 1)
			{
				world.setBlockState(pos, TFCBlocks.SaltWaterStatic.getDefaultState().withProperty(LEVEL, 0));
			}
		}
		else
		{
			world.setBlockState(pos, TFCBlocks.SaltWater.getDefaultState().withProperty(LEVEL, state.getValue(LEVEL)), 2);
		}
	}
}
