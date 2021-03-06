package com.bioxx.tfc2.blocks;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.bioxx.tfc2.TFCBlocks;
import com.bioxx.tfc2.api.types.WoodType;

public class BlockLogHorizontal3 extends BlockLogHorizontal
{
	public static PropertyEnum META_PROPERTY = PropertyEnum.create("wood", WoodType.class, Arrays.copyOfRange(WoodType.values(), 16, 19));
	public static PropertyInteger ROT_PROPERTY =  PropertyInteger.create("rotation", 0, 1);

	public BlockLogHorizontal3()
	{
		super(Material.ground, META_PROPERTY);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setShowInCreative(false);
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]{META_PROPERTY, ROT_PROPERTY});
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(META_PROPERTY, WoodType.getTypeFromMeta((meta & 7) + 16)).withProperty(ROT_PROPERTY, (meta & 8) >> 3);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int wood = ((((WoodType)state.getValue(META_PROPERTY)).getMeta()) & 7);
		int rot = (((Integer)state.getValue(ROT_PROPERTY)) << 3);
		return wood + rot;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(TFCBlocks.LogVertical);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((WoodType)state.getValue(META_PROPERTY)).getMeta();
	}
}
