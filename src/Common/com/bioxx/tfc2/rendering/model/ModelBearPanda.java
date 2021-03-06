package com.bioxx.tfc2.rendering.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBearPanda extends ModelBase
{
	ModelRenderer Head;
	ModelRenderer Body;
	ModelRenderer LegLeftFront;
	ModelRenderer LegRightFront;
	ModelRenderer LegLeftRear;
	ModelRenderer LegRightRear;

	public ModelBearPanda()
	{
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Head.EarRight", 0, 5);
		setTextureOffset("Head.EarLeft", 0, 5);
		setTextureOffset("Head.Head", 0, 22);
		setTextureOffset("Head.Snout", 50, 5);
		setTextureOffset("Head.Jaw", 50, 9);
		setTextureOffset("Body.Torso", 18, 9);
		setTextureOffset("Body.Hunch", 0, 9);
		setTextureOffset("Body.Tail", 0, 0);
		setTextureOffset("LegLeftFront.LegFrontLeftUpper", 15, 0);
		setTextureOffset("LegLeftFront.LegFrontLeftLower", 33, 0);
		setTextureOffset("LegLeftFront.LegFrontLeftPaw", 45, 0);
		setTextureOffset("LegRightFront.LegFrontRightUpper", 15, 0);
		setTextureOffset("LegRightFront.LegFrontRightLower", 33, 0);
		setTextureOffset("LegRightFront.LegFrontRightPaw", 45, 0);
		setTextureOffset("LegLeftRear.LegRearLeftUpper", 15, 0);
		setTextureOffset("LegLeftRear.LegRearLeftLower", 33, 0);
		setTextureOffset("LegLeftRear.LegRearLeftPaw", 45, 0);
		setTextureOffset("LegRightRear.LegRearRightUpper", 15, 0);
		setTextureOffset("LegRightRear.LegRearRightLower", 33, 0);
		setTextureOffset("LegRightRear.LegRearRightPaw", 45, 0);

		Head = new ModelRenderer(this, "Head");
		Head.setRotationPoint(0F, 10F, -2F);
		setRotation(Head, 0F, 0F, 0F);
		Head.mirror = true;
		Head.addBox("EarRight", 2.5F, -3F, -2F, 2, 2, 1);
		Head.addBox("EarLeft", -4.5F, -3F, -2F, 2, 2, 1);
		Head.addBox("Head", -3.5F, -2F, -4F, 7, 6, 4);
		Head.addBox("Snout", -1.5F, 0F, -6F, 3, 2, 2);
		Head.addBox("Jaw", -1.5F, 2F, -6F, 3, 1, 2);
		Body = new ModelRenderer(this, "Body");
		Body.setRotationPoint(0F, 12F, 0F);
		setRotation(Body, 0F, 0F, 0F);
		Body.mirror = true;
		Body.addBox("Torso", -4F, -4F, 0F, 8, 8, 15);
		Body.addBox("Hunch", -2.5F, -5F, -2F, 5, 8, 5);
		Body.addBox("Tail", -1F, -3F, 15F, 2, 3, 2);
		LegLeftFront = new ModelRenderer(this, "LegLeftFront");
		LegLeftFront.setRotationPoint(4F, 10F, 3F);
		setRotation(LegLeftFront, 0F, 0F, 0F);
		LegLeftFront.mirror = true;
		LegLeftFront.addBox("LegFrontLeftUpper", -2.5F, -1F, -2.5F, 4, 9, 5);
		LegLeftFront.addBox("LegFrontLeftLower", -2F, 8F, -2F, 3, 5, 3);
		LegLeftFront.addBox("LegFrontLeftPaw", -2F, 13F, -3F, 3, 1, 3);
		LegRightFront = new ModelRenderer(this, "LegRightFront");
		LegRightFront.setRotationPoint(-4F, 10F, 3F);
		setRotation(LegRightFront, 0F, 0F, 0F);
		LegRightFront.mirror = true;
		LegRightFront.addBox("LegFrontRightUpper", -1.5F, -1F, -2.5F, 4, 9, 5);
		LegRightFront.addBox("LegFrontRightLower", -1F, 8F, -2F, 3, 5, 3);
		LegRightFront.addBox("LegFrontRightPaw", -1F, 13F, -3F, 3, 1, 3);
		LegLeftRear = new ModelRenderer(this, "LegLeftRear");
		LegLeftRear.setRotationPoint(4F, 10F, 13F);
		setRotation(LegLeftRear, 0F, 0F, 0F);
		LegLeftRear.mirror = true;
		LegLeftRear.addBox("LegRearLeftUpper", -2.5F, -1F, -2.5F, 4, 9, 5);
		LegLeftRear.addBox("LegRearLeftLower", -2F, 8F, -1F, 3, 5, 3);
		LegLeftRear.addBox("LegRearLeftPaw", -2F, 13F, -2F, 3, 1, 3);
		LegRightRear = new ModelRenderer(this, "LegRightRear");
		LegRightRear.setRotationPoint(-4F, 10F, 13F);
		setRotation(LegRightRear, 0F, 0F, 0F);
		LegRightRear.mirror = true;
		LegRightRear.addBox("LegRearRightUpper", -1.5F, -1F, -2.5F, 4, 9, 5);
		LegRightRear.addBox("LegRearRightLower", -1F, 8F, -1F, 3, 5, 3);
		LegRightRear.addBox("LegRearRightPaw", -1F, 13F, -2F, 3, 1, 3);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
	{
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);

		if (this.isChild)
		{
			float f6 = 2.0F;
			GlStateManager.pushMatrix();
			//GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
			this.Head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.Body.render(scale);
			LegLeftFront.render(scale);
			LegLeftRear.render(scale);
			LegRightFront.render(scale);
			LegRightRear.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, -0.5F);
			this.Head.render(scale);
			this.Body.render(scale);
			LegLeftFront.render(scale);
			LegLeftRear.render(scale);
			LegRightFront.render(scale);
			LegRightRear.render(scale);
			GlStateManager.popMatrix();
		}
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		float f6 = (180F / (float)Math.PI);
		this.Head.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
		this.Head.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
		this.Body.rotateAngleX = 0;//((float)Math.PI / 2F);
		this.LegLeftFront.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
		this.LegLeftRear.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
		this.LegRightFront.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float)Math.PI) * 1.4F * p_78087_2_;
		this.LegRightRear.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
	}

}

