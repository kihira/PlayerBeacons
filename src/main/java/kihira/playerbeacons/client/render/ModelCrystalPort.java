package kihira.playerbeacons.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystalPort extends ModelBase {

	private final ModelRenderer crystalport1;
	private final ModelRenderer crystalport2;
	private final ModelRenderer crystalport3;
	private final ModelRenderer crystalport4;

	public ModelCrystalPort() {

		textureWidth = 64;
		textureHeight = 32;

		crystalport1 = new ModelRenderer(this, 0, 0);
		crystalport1.addBox(-2F, 0F, -0.5F, 4, 5, 1);
		crystalport1.setRotationPoint(0F, 13F, 4.5F);
		crystalport1.mirror = true;
		setRotation(crystalport1, 0F, 0F, 0F);
		crystalport2 = new ModelRenderer(this, 0, 0);
		crystalport2.addBox(-2F, 0F, -0.5F, 4, 5, 1);
		crystalport2.setRotationPoint(4.5F, 13F, 0F);
		crystalport2.mirror = true;
		setRotation(crystalport2, 0F, 1.570796F, 0F);
		crystalport3 = new ModelRenderer(this, 0, 0);
		crystalport3.addBox(-2F, 0F, -0.5F, 4, 5, 1);
		crystalport3.setRotationPoint(0F, 13F, -4.5F);
		crystalport3.mirror = true;
		setRotation(crystalport3, 0F, 3.141593F, 0F);
		crystalport4 = new ModelRenderer(this, 0, 0);
		crystalport4.addBox(-2F, 0F, -0.5F, 4, 5, 1);
		crystalport4.setRotationPoint(-4.5F, 13F, 0F);
		crystalport4.mirror = true;
		setRotation(crystalport4, 0F, -1.570796F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		crystalport1.render(f5);
		crystalport2.render(f5);
		crystalport3.render(f5);
		crystalport4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	private void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}
}
