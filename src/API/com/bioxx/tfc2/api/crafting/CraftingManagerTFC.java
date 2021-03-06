package com.bioxx.tfc2.api.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc2.api.interfaces.IRecipeTFC;

public class CraftingManagerTFC
{
	private static final CraftingManagerTFC INSTANCE = new CraftingManagerTFC();
	public static final CraftingManagerTFC getInstance()
	{
		return INSTANCE;
	}

	private List<IRecipeTFC> recipes;
	private List<IRecipeTFC> recipes_knapping;

	private CraftingManagerTFC()
	{
		recipes = new ArrayList<IRecipeTFC>();
		recipes_knapping = new ArrayList<IRecipeTFC>();

		Collections.sort(recipes, new RecipeSorterTFC(this));
		Collections.sort(recipes_knapping, new RecipeSorterTFC(this));
	}

	public ShapedRecipesTFC addRecipe(ItemStack itemstack, Object... aobj)
	{
		return addRecipe(RecipeType.Normal, itemstack, aobj);
	}

	public ShapedRecipesTFC addRecipe(RecipeType rt, ItemStack itemstack, Object... aobj)
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if (aobj[i] instanceof String[])
		{
			String as[] = (String[])aobj[i++];
			for (int l = 0; l < as.length; l++)
			{
				String s2 = as[l];
				k++;
				j = s2.length();
				s = new StringBuilder().append(s).append(s2).toString();
			}
		}
		else
		{
			while (aobj[i] instanceof String)
			{
				String s1 = (String)aobj[i++];
				k++;
				j = s1.length();
				s = new StringBuilder().append(s).append(s1).toString();
			}
		}
		HashMap<Character, ItemStack> hashmap = new HashMap<Character, ItemStack>();
		for (; i < aobj.length; i += 2)
		{
			Character character = (Character)aobj[i];
			ItemStack itemstack1 = null;
			if (aobj[i + 1] instanceof Item)
			{
				itemstack1 = new ItemStack((Item)aobj[i + 1]);
			}
			else if (aobj[i + 1] instanceof Block)
			{
				itemstack1 = new ItemStack((Block)aobj[i + 1], 1, -1);
			}
			else if (aobj[i + 1] instanceof ItemStack)
			{
				itemstack1 = (ItemStack)aobj[i + 1];
			}
			hashmap.put(character, itemstack1);
		}

		List<ItemStack> aitemstack = new ArrayList<ItemStack>(j * k);
		for (int i1 = 0; i1 < j * k; i1++)
		{
			char c = s.charAt(i1);
			if (hashmap.containsKey(Character.valueOf(c)))
			{
				aitemstack.add(i1, hashmap.get(Character.valueOf(c)).copy());
			}
			else
			{
				aitemstack.add(i1, null);
			}
		}

		ShapedRecipesTFC shapedRecipesTFC = new ShapedRecipesTFC(j, k, aitemstack, itemstack);
		if(rt == RecipeType.Normal)
			recipes.add(shapedRecipesTFC);
		else if(rt == RecipeType.Knapping)
			recipes_knapping.add(shapedRecipesTFC);
		return shapedRecipesTFC;
	}

	public ShapedRecipesTFC addShapelessRecipe(ItemStack itemstack, Object... aobj)
	{
		return addRecipe(RecipeType.Normal, itemstack, aobj);
	}

	public ShapelessRecipesTFC addShapelessRecipe(RecipeType rt, ItemStack itemstack, Object... aobj)
	{
		ArrayList<ItemStack> arraylist = new ArrayList<ItemStack>();
		Object aobj1[] = aobj;
		int i = aobj1.length;
		for (int j = 0; j < i; j++)
		{
			Object obj = aobj1[j];
			if (obj instanceof ItemStack)
			{
				arraylist.add(((ItemStack)obj).copy());
				continue;
			}
			if (obj instanceof Item)
			{
				arraylist.add(new ItemStack((Item)obj));
				continue;
			}
			if (obj instanceof Block)
			{
				arraylist.add(new ItemStack((Block)obj));
			}
			else
			{
				throw new RuntimeException("Invalid shapeless recipe!");
			}
		}
		ShapelessRecipesTFC recipesTFC = new ShapelessRecipesTFC(itemstack, arraylist);
		if(rt == RecipeType.Normal)
			recipes.add(recipesTFC);
		else if(rt == RecipeType.Knapping)
			recipes_knapping.add(recipesTFC);
		return recipesTFC;
	}

	public ItemStack findMatchingRecipe(InventoryCrafting inventorycrafting, World world)
	{
		return findMatchingRecipe(RecipeType.Normal, inventorycrafting, world);
	}

	public ItemStack findMatchingRecipe(RecipeType rt, InventoryCrafting inventorycrafting, World world)
	{
		List<IRecipeTFC> rec = Collections.emptyList();
		if(rt == RecipeType.Normal)
			rec = recipes;
		else if(rt == RecipeType.Knapping)
			rec = recipes_knapping;

		for (int k = 0; k < rec.size(); k++)
		{
			IRecipeTFC irecipe = rec.get(k);
			if (irecipe.matches(inventorycrafting, world))
			{
				return irecipe.getCraftingResult(inventorycrafting);
			}
		}

		return null;
	}

	public List<IRecipeTFC> getRecipeList(RecipeType rt)
	{
		if(rt == RecipeType.Normal)
			return recipes;
		else if(rt == RecipeType.Knapping)
			return recipes_knapping;

		return recipes;
	}

	public enum RecipeType
	{
		Normal, Knapping;
	}
}
