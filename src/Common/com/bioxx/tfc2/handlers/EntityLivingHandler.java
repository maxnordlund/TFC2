package com.bioxx.tfc2.handlers;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.bioxx.tfc2.Core;
import com.bioxx.tfc2.core.FoodStatsTFC;

public class EntityLivingHandler
{
	@SubscribeEvent
	public void onEntityLivingUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			//Set Max Health
			float newMaxHealth = FoodStatsTFC.getMaxHealth(player);
			float oldMaxHealth = (float)player.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
			if(oldMaxHealth != newMaxHealth)
			{
				player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(newMaxHealth);
			}

			if(!player.worldObj.isRemote)
			{
				//Tick Decay
				//Core.handleItemTicking(player.inventory.mainInventory, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);//Removed on port
				//Handle Food


				//Nullify the Old Food
				player.getFoodStats().addStats(20 - player.getFoodStats().getFoodLevel(), 0.0F);
				//Handle Food
				FoodStatsTFC foodstats = Core.getPlayerFoodStats(player);
				foodstats.onUpdate(player);
				Core.setPlayerFoodStats(player, foodstats);
				//Send update packet from Server to Client
				//Removed on port
				/*if(foodstats.shouldSendUpdate())
				{
					AbstractPacket pkt = new PlayerUpdatePacket(player, 0);
					TerraFirmaCraft.PACKET_PIPELINE.sendTo(pkt, (EntityPlayerMP) player);
				}*/
				if(foodstats.waterLevel / foodstats.getMaxWater(player) <= 0.25f)
				{
					setThirsty(player, true);
				}
				else if(foodstats.waterLevel / foodstats.getMaxWater(player) <= 0.5f)
				{
					if(player.isSprinting())
						player.setSprinting(false);
				}
				else
				{
					setThirsty(player, false);
				}
				if (foodstats.stomachLevel / foodstats.getMaxStomach(player) <= 0.25f)
				{
					player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 20, 1));
					player.addPotionEffect(new PotionEffect(Potion.weakness.id, 20, 1));
				}

				//Handle Spawn Protection
				//Removed on port
				/*NBTTagCompound nbt = player.getEntityData();
				long spawnProtectionTimer = nbt.hasKey("spawnProtectionTimer") ? nbt.getLong("spawnProtectionTimer") : TFC_Time.getTotalTicks() + TFC_Time.HOUR_LENGTH;
				if(spawnProtectionTimer < TFC_Time.getTotalTicks())
				{
					//Add protection time to the chunks
					for(int i = -2; i < 3; i++)
					{
						for(int k = -2; k < 3; k++)
						{
							int lastChunkX = ((int) Math.floor(player.posX)) >> 4;
						int lastChunkZ = ((int) Math.floor(player.posZ)) >> 4;
				TFC_Core.getCDM(player.worldObj).addProtection(lastChunkX + i, lastChunkZ + k, TFCOptions.protectionGain);
						}
					}

					spawnProtectionTimer += TFC_Time.HOUR_LENGTH;
					nbt.setLong("spawnProtectionTimer", spawnProtectionTimer);
				}*/
			}
			else
			{

			}
		}
	}

	public void setThirsty(EntityPlayer player, boolean b)
	{
		//Removed on port
		/*IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (iattributeinstance.getModifier(TFCAttributes.THIRSTY_UUID) != null)
		{
			iattributeinstance.removeModifier(TFCAttributes.THIRSTY);
		}

		if (b)
		{
			iattributeinstance.applyModifier(TFCAttributes.THIRSTY);
		}*/
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void handleFOV(FOVUpdateEvent event)
	{
		EntityPlayer player = event.entity;

		// Calculate FOV based on the variable draw speed of the bow depending on player armor.
		//Removed on port
		/*if (player.isUsingItem() && player.getItemInUse().getItem() instanceof ItemCustomBow)
		{
			float fov = 1.0F;
			int duration = player.getItemInUseDuration();
			float speed = ItemCustomBow.getUseSpeed(player);
			float force = duration / speed;

			if (force > 1.0F)
			{
				force = 1.0F;
			}
			else
			{
				force *= force;
			}

			fov *= 1.0F - force * 0.15F;
			event.newfov = fov;
		}*/
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		EntityLivingBase entity = event.entityLiving;

		if (entity instanceof EntityPlayer)
		{
			//Removed on port
			/*EntityPlayer player = (EntityPlayer) entity;
			SkillStats skills = TFC_Core.getSkillStats(player);
			PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
			pi.tempSkills = skills;

			// Save the item in the back slot if keepInventory is set to true.
			if (entity.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory") && player.inventory instanceof InventoryPlayerTFC)
			{
				pi.tempEquipment = ((InventoryPlayerTFC) player.inventory).extraEquipInventory.clone();
			}*/
		}

		if (event.entity.dimension == 1)
			event.entity.travelToDimension(0);
	}

	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event)
	{
		boolean processed = false;
		if (!event.entity.worldObj.isRemote && event.recentlyHit && !(event.entity instanceof EntityPlayer) && !(event.entity instanceof EntityZombie))
		{
			if(event.source.getSourceOfDamage() instanceof EntityPlayer || event.source.isProjectile())
			{
				boolean foundFood = false;
				processed = true;
				ArrayList<EntityItem> drop = new ArrayList<EntityItem>();
				EntityPlayer p = null;
				if(event.source.getSourceOfDamage() instanceof EntityPlayer)
					p = (EntityPlayer)event.source.getSourceOfDamage();
				//Removed on port
				/*else if(event.source.getSourceOfDamage() instanceof EntityProjectileTFC)
				{
					EntityProjectileTFC proj = (EntityProjectileTFC)event.source.getSourceOfDamage();
					if(proj.shootingEntity instanceof EntityPlayer)
						p = (EntityPlayer)proj.shootingEntity;
				}*/
				//Removed on port
				/*for(EntityItem ei : event.drops)
				{
					ItemStack is = ei.getEntityItem();
					if (is.getItem() instanceof IFood)
					{
						if(p == null)
							continue;
						foundFood = true;

						int sweetMod = ((ItemFoodTFC) is.getItem()).getTasteSweetMod(is);
						int sourMod = ((ItemFoodTFC) is.getItem()).getTasteSourMod(is);
						int saltyMod = ((ItemFoodTFC) is.getItem()).getTasteSaltyMod(is);
						int bitterMod = ((ItemFoodTFC) is.getItem()).getTasteBitterMod(is);
						int umamiMod = ((ItemFoodTFC) is.getItem()).getTasteSavoryMod(is);

						float oldWeight = Food.getWeight(is);
						Food.setWeight(is, 0);
						float newWeight = oldWeight * (TFC_Core.getSkillStats(p).getSkillMultiplier(Global.SKILL_BUTCHERING)+0.01f);
						while (newWeight >= Global.FOOD_MIN_DROP_WEIGHT)
						{
							float fw = Helper.roundNumber(Math.min(Global.FOOD_MAX_WEIGHT, newWeight), 10);
							if (fw < Global.FOOD_MAX_WEIGHT)
								newWeight = 0;
							newWeight -= fw;

							ItemStack result = ItemFoodTFC.createTag(new ItemStack(is.getItem(), 1), fw);

							if(sweetMod != 0) result.getTagCompound().setInteger("tasteSweetMod", sweetMod);
							if(sourMod != 0) result.getTagCompound().setInteger("tasteSourMod", sourMod);
							if(saltyMod != 0) result.getTagCompound().setInteger("tasteSaltyMod", saltyMod);
							if(bitterMod != 0) result.getTagCompound().setInteger("tasteBitterMod", bitterMod);
							if(umamiMod != 0) result.getTagCompound().setInteger("tasteUmamiMod", umamiMod);	

							drop.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, result));
						}
					}
					else
					{
						drop.add(ei);
					}
				}
				event.drops.clear();*/
				event.drops.addAll(drop);
				if(foundFood && p != null)
				{
					//Removed on port
					//TFC_Core.getSkillStats(p).increaseSkill(Global.SKILL_BUTCHERING, 1);
				}
			}
		}
		//Removed on port
		/*if (!processed && !(event.entity instanceof EntityPlayer) && !(event.entity instanceof EntityZombie))
		{
			ArrayList<EntityItem> drop = new ArrayList<EntityItem>();
			for(EntityItem ei : event.drops)
			{
				if (!(ei.getEntityItem().getItem() instanceof IFood))
				{
					drop.add(ei);
				}
			}
			event.drops.clear();
			event.drops.addAll(drop);
		}*/
	}
}
