package com.bioxx.tfc2;

import java.io.File;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.bioxx.tfc2.api.Global;
import com.bioxx.tfc2.api.types.OreType;
import com.bioxx.tfc2.api.types.WoodType;
import com.bioxx.tfc2.api.util.KeyBindings;
import com.bioxx.tfc2.blocks.BlockLeaves2;
import com.bioxx.tfc2.core.RegistryItemQueue;
import com.bioxx.tfc2.entity.EntityBear;
import com.bioxx.tfc2.entity.EntityBearPanda;
import com.bioxx.tfc2.entity.EntityCart;
import com.bioxx.tfc2.entity.EntityLion;
import com.bioxx.tfc2.entity.EntityTiger;
import com.bioxx.tfc2.handlers.client.BackgroundMusicHandler;
import com.bioxx.tfc2.handlers.client.ClientRenderHandler;
import com.bioxx.tfc2.handlers.client.GuiHandler;
import com.bioxx.tfc2.handlers.client.KeyBindingHandler;
import com.bioxx.tfc2.handlers.client.RenderOverlayHandler;
import com.bioxx.tfc2.rendering.MeshDef;
import com.bioxx.tfc2.rendering.model.RenderBear;
import com.bioxx.tfc2.rendering.model.RenderBearPanda;
import com.bioxx.tfc2.rendering.model.RenderCart;
import com.bioxx.tfc2.rendering.model.RenderLion;
import com.bioxx.tfc2.rendering.model.RenderTiger;

public class ClientProxy extends CommonProxy
{
	private static ModelResourceLocation freshwaterLocation = new ModelResourceLocation(Reference.getResID() + "liquids", "freshwater");
	private static ModelResourceLocation saltwaterLocation = new ModelResourceLocation(Reference.getResID() + "liquids", "saltwater");

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);

		B3DLoader.instance.addDomain(Reference.ModID);
		OBJLoader.instance.addDomain(Reference.ModID);
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TFCBlocks.LeavesPalm), 0, new ModelResourceLocation(Reference.ModID + ":leaves_palm", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(TFCBlocks.Leaves2), 18, new ModelResourceLocation(Reference.ModID + ":leaves_palm", "inventory"));
		Item fresh = Item.getItemFromBlock(TFCBlocks.FreshWater);
		Item salt = Item.getItemFromBlock(TFCBlocks.SaltWater);
		Item saltstatic = Item.getItemFromBlock(TFCBlocks.SaltWaterStatic);
		Item freshstatic = Item.getItemFromBlock(TFCBlocks.FreshWaterStatic);
		ModelBakery.addVariantName(fresh);
		ModelBakery.addVariantName(salt);
		ModelBakery.addVariantName(saltstatic);
		ModelBakery.addVariantName(freshstatic);
		ModelLoader.setCustomMeshDefinition(fresh, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return freshwaterLocation;
			}
		});
		ModelLoader.setCustomMeshDefinition(salt, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return saltwaterLocation;
			}
		});
		ModelLoader.setCustomMeshDefinition(saltstatic, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return saltwaterLocation;
			}
		});
		ModelLoader.setCustomMeshDefinition(freshstatic, new ItemMeshDefinition()
		{
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				return freshwaterLocation;
			}
		});
		ModelLoader.setCustomStateMapper(TFCBlocks.FreshWater, new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return freshwaterLocation;
			}
		});
		ModelLoader.setCustomStateMapper(TFCBlocks.SaltWater, new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return saltwaterLocation;
			}
		});
		ModelLoader.setCustomStateMapper(TFCBlocks.SaltWaterStatic, new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return saltwaterLocation;
			}
		});
		ModelLoader.setCustomStateMapper(TFCBlocks.FreshWaterStatic, new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return freshwaterLocation;
			}
		});

		StateMapperBase ignoreState = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) 
			{
				if(iBlockState.getValue(BlockLeaves2.META_PROPERTY) == WoodType.Palm)
					return new ModelResourceLocation("tfc2:leaves_palm");
				else return new ModelResourceLocation("tfc2:leaves2");

			}
		};
		ModelLoader.setCustomStateMapper(TFCBlocks.Leaves2, ignoreState);

		/**
		 * Dirt
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Dirt), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Dirt/" + stone, "inventory");
				}
			}

		});

		/**
		 * Grass
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Grass), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Grass/" + stone, "inventory");
				}
			}

		});

		/**
		 * Stone
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Stone), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Stone/" + stone, "inventory");
				}
			}

		});

		/**
		 * Rubble
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Rubble), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Rubble/" + stone, "inventory");
				}
			}

		});

		/**
		 * Sand
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Sand), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Sand/" + stone, "inventory");
				}
			}

		});

		/**
		 * Gravel
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Gravel), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Gravel/" + stone, "inventory");
				}
			}

		});

		/**
		 * LooseRock
		 */
		ModelLoader.setCustomMeshDefinition(TFCItems.LooseRock, new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[Global.STONE_ALL.length];
				for(int l = 0; l < Global.STONE_ALL.length; l++)
				{
					String stone = Core.textConvert(Global.STONE_ALL[l]);
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":LooseRock/" + stone, "inventory");
				}
			}

		});

		/**
		 * Planks
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Planks), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 0; l < 16; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Planks/" + wood, "inventory");
				}
			}

		});

		/**
		 * Saplings
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Sapling), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 0; l < 16; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Saplings/" + wood, "inventory");
				}
			}

		});

		/**
		 * Logs
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.LogVertical), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 0; l < 16; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Logs/" + wood, "inventory");
				}
			}

		});

		/**
		 * Leaves
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Leaves), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 0; l < 16; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Leaves/" + wood, "inventory");
				}
			}

		});

		/**
		 * Planks2
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Planks2), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 16; l < WoodType.values().length; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Planks/" + wood, "inventory");
				}
			}

		});

		/**
		 * Saplings2
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Sapling2), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 16; l < WoodType.values().length; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Saplings/" + wood, "inventory");
				}
			}

		});

		/**
		 * Logs2
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.LogVertical2), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 16; l < WoodType.values().length; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Logs/" + wood, "inventory");
				}
			}

		});

		/**
		 * Leaves2
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Leaves2), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 16; l < 18; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Wood/Leaves/" + wood, "inventory");
				}
			}

		});

		/**
		 * Ore
		 */
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(TFCBlocks.Ore), new MeshDef()
		{
			@Override
			public void Setup()
			{
				this.rl = new ModelResourceLocation[WoodType.values().length];
				for(int l = 0; l < OreType.values().length; l++)
				{
					String wood = Core.textConvert(WoodType.values()[l].getName());
					this.rl[l] = new ModelResourceLocation(Reference.ModID + ":Ore/" + wood, "inventory");
				}
			}

		});

		for(int l = 0; l < Global.STONE_ALL.length; l++)
		{
			String stone = Core.textConvert(Global.STONE_ALL[l]);

			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Dirt), Reference.ModID + ":Dirt/" + stone);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Grass), Reference.ModID + ":Grass/" + stone + "/" + stone);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Stone), Reference.ModID + ":Stone/" + stone);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Rubble), Reference.ModID + ":Rubble/" + stone);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Sand), Reference.ModID + ":Sand/" + stone);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Gravel), Reference.ModID + ":Gravel/" + stone);

			ModelBakery.addVariantName(TFCItems.LooseRock, Reference.ModID + ":LooseRock/" + stone);
		}
		for(int l = 0; l < 16; l++)
		{
			String wood = Core.textConvert(WoodType.values()[l].getName());

			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Planks), Reference.ModID + ":Wood/Planks/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Sapling), Reference.ModID + ":Wood/Saplings/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogVertical), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogHorizontal), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogHorizontal2), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogNatural), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Leaves), Reference.ModID + ":Wood/Leaves/" + wood);

		}

		for(int l = 16; l < 19; l++)
		{
			String wood = Core.textConvert(WoodType.values()[l].getName());

			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Planks2), Reference.ModID + ":Wood/Planks/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Sapling2), Reference.ModID + ":Wood/Saplings/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogVertical2), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogHorizontal3), Reference.ModID + ":Wood/Logs/" + wood);
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.LogNatural2), Reference.ModID + ":Wood/Logs/" + wood);
			if(l < 18)
			{
				ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Leaves2), Reference.ModID + ":Wood/Leaves/" + wood);
			}
		}

		for(int l = 0; l < OreType.values().length; l++)
		{
			String ore = Core.textConvert(OreType.values()[l].getName());
			ModelBakery.addVariantName(Item.getItemFromBlock(TFCBlocks.Ore), Reference.ModID + ":Ore/" + ore);
		}

		RegistryItemQueue.getInstance().registerMeshes();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new RenderOverlayHandler());
		MinecraftForge.EVENT_BUS.register(new ClientRenderHandler());
		MinecraftForge.EVENT_BUS.register(new BackgroundMusicHandler());


		//Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityCart.class, new RenderCart(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBear.class, new RenderBear(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBearPanda.class, new RenderBearPanda(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityLion.class, new RenderLion(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityTiger.class, new RenderTiger(Minecraft.getMinecraft().getRenderManager()));

		//Disable vanilla UI elements
		GuiIngameForge.renderHealth = false;
		GuiIngameForge.renderArmor = false;
		GuiIngameForge.renderExperiance = false;
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);

	}

	private void registerItemMesh(Item i, ModelResourceLocation mrl)
	{
		ModelLoader.setCustomMeshDefinition(i, new MeshDef(mrl));
	}

	private void registerItemMesh(Item i, int meta, ModelResourceLocation mrl)
	{
		ModelLoader.setCustomMeshDefinition(i, new MeshDef(mrl));
	}

	@Override
	public File getMinecraftDir()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}

	@Override
	public void registerKeys()
	{
		//KeyBindings.addKeyBinding(KeyBindingHandler.Key_CombatMode);
		//KeyBindings.addIsRepeating(false);
		//ClientRegistry.registerKeyBinding(KeyBindingHandler.Key_ToolMode);
		//ClientRegistry.registerKeyBinding(KeyBindingHandler.Key_LockTool);
		ClientRegistry.registerKeyBinding(KeyBindingHandler.Key_CombatMode);
		//uploadKeyBindingsToGame();
	}

	@Override
	public void registerKeyBindingHandler()
	{
		FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
	}

	@Override
	public void uploadKeyBindingsToGame()
	{
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		KeyBinding[] tfcKeyBindings = KeyBindings.gatherKeyBindings();
		KeyBinding[] allKeys = new KeyBinding[settings.keyBindings.length + tfcKeyBindings.length];
		System.arraycopy(settings.keyBindings, 0, allKeys, 0, settings.keyBindings.length);
		System.arraycopy(tfcKeyBindings, 0, allKeys, settings.keyBindings.length, tfcKeyBindings.length);
		settings.keyBindings = allKeys;
		settings.loadOptions();
	}

	@Override
	public boolean isClientSide()
	{
		return true;
	}

	@Override
	public void registerGuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(TFC.instance, new GuiHandler());
		// Register Gui Event Handler
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
	}

}
