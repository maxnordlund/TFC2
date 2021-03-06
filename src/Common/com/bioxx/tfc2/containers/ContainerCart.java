package com.bioxx.tfc2.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc2.core.PlayerInventory;
import com.bioxx.tfc2.entity.InventoryCart;

public class ContainerCart extends ContainerTFC
{
	private World world;
	private InventoryCart cart;
	private EntityPlayer player;

	public ContainerCart(InventoryPlayer playerinv, InventoryCart cart, World world, int x, int y, int z)
	{
		this.player = playerinv.player;

		this.world = world;
		this.cart = cart;
		cart.openInventory(player);
		layoutContainer(playerinv, cart, 0, 0);
		PlayerInventory.buildInventoryLayout(this, playerinv, 8, 93, false, true);
	}

	/**
	 * Callback for when the crafting gui is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if(!world.isRemote)
			cart.closeInventory(par1EntityPlayer);
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift clicking.
	 */
	@Override
	public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum)
	{
		ItemStack origStack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			origStack = slotStack.copy();

			// From pile to inventory
			if (slotNum < 27)
			{
				if (!this.mergeItemStack(slotStack, 27, inventorySlots.size(), true))
					return null;
			}
			else
			{
				if (!this.mergeItemStack(slotStack, 0, 27, false))
					return null;
			}

			if (slotStack.stackSize <= 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();

			if (slotStack.stackSize == origStack.stackSize)
				return null;

			slot.onPickupFromSlot(player, slotStack);
		}

		return origStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}

	protected void layoutContainer(IInventory playerInventory, IInventory chestInventory, int xSize, int ySize)
	{
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(chestInventory, x+y*9, 8+x*18, 18+y*18));
			}
		}
	}

	public EntityPlayer getPlayer()
	{
		return player;
	}
}
