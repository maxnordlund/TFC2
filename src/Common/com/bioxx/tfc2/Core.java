package com.bioxx.tfc2;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.apache.commons.lang3.text.WordUtils;

import com.bioxx.tfc2.core.FoodStatsTFC;
import com.bioxx.tfc2.core.InventoryPlayerTFC;

public class Core 
{
	public static Block getGroundAboveSeaLevel(World world, BlockPos pos)
	{
		BlockPos blockpos1;

		for (blockpos1 = new BlockPos(pos.getX(), 63, pos.getZ()); !world.isAirBlock(blockpos1.up()); blockpos1 = blockpos1.up())
		{
			;
		}

		return world.getBlockState(blockpos1).getBlock();
	}

	public static int getHeight(World world, int worldX, int worldZ)
	{
		Chunk c = world.getChunkFromChunkCoords(worldX >> 4, worldZ >> 4); 
		return c.getHeightValue(worldX & 15, worldZ & 15);
	}

	/**
	 * Sets the block using setActualState for the given block. Helper method to reduce repeated code usage
	 * @return Returns if the block is successfully set
	 */
	public static boolean setBlock(World world, Block b, BlockPos bp)
	{
		return world.setBlockState(bp, b.getActualState(b.getDefaultState(), world, bp));
	}

	/**
	 * Sets the block using setActualState for the given block. Helper method to reduce repeated code usage
	 * @return Returns if the block is successfully set
	 */
	public static boolean setBlock(World world, IBlockState b, BlockPos bp)
	{
		return world.setBlockState(bp, b.getBlock().getActualState(b, world, bp));
	}

	public static String translate(String s)
	{
		return StatCollector.translateToLocal(s);
	}

	public static String textConvert(String s)
	{
		return WordUtils.capitalize(s, '_').replaceAll("_", " ");
	}

	/**
	 * Creates a new ResourceLocation from an input string. Shortens the code slightly.
	 */
	public static ResourceLocation CreateRes(String s)
	{
		return new ResourceLocation(s);
	}

	public static boolean isGrass(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Grass)
			return true;

		return false;
	}

	public static boolean isDirt(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Dirt)
			return true;

		return false;
	}

	public static boolean isSoil(IBlockState state)
	{
		return isGrass(state) || isDirt(state);
	}

	public static boolean isSand(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Sand)
			return true;

		return false;
	}

	public static boolean isStone(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Stone)
			return true;

		return false;
	}

	public static boolean isGravel(IBlockState state)
	{
		if(state.getBlock() == TFCBlocks.Gravel)
			return true;

		return false;
	}

	public static boolean isTerrain(IBlockState state)
	{
		return isSoil(state) || isSand(state) || isStone(state) || isGravel(state);
	}

	/**
	 * 
	 * @param w
	 * @param pos
	 * @return
	 */
	public static float getMoistureFromChunk(ChunkCache w, BlockPos pos)
	{
		Chunk c = w.worldObj.getChunkFromBlockCoords(pos);
		byte[] moistureArray = c.getBiomeArray();
		byte b = moistureArray[(pos.getZ() & 0xF) << 4 | (pos.getX() & 0xF)];
		int s = (b & 0xFF);
		return (float)s / 255F;
	}

	public static void bindTexture(ResourceLocation texture)
	{
		net.minecraft.client.Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}

	public static int getExtraEquipInventorySize() 
	{
		return 0;
	}

	public static InventoryPlayer getNewInventory(EntityPlayer player)
	{
		InventoryPlayer ip = player.inventory;
		NBTTagList nbt = new NBTTagList();
		nbt = player.inventory.writeToNBT(nbt);
		ip = new InventoryPlayerTFC(player);
		ip.readFromNBT(nbt);
		return ip;
	}

	public static boolean isPlayerInDebugMode(EntityPlayer player)
	{
		return true;
	}

	/**
	 * If this is modified in any way, setPlayerFoodStats should be called to save the data back 
	 * to players nbt or the changes will be lost.
	 * @return Returns the FoodStatsTFC object that is associated with this player. 
	 */
	public static FoodStatsTFC getPlayerFoodStats(EntityPlayer player)
	{
		FoodStatsTFC foodstats = new FoodStatsTFC(player);
		foodstats.readNBT(player.getEntityData());
		return foodstats;
	}

	public static void setPlayerFoodStats(EntityPlayer player, FoodStatsTFC foodstats)
	{
		foodstats.writeNBT(player.getEntityData());
	}
}
