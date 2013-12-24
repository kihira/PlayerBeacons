package playerbeacons.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSantaHat extends ModelBase {

	ModelRenderer base;
	ModelRenderer one;
	ModelRenderer two;
	ModelRenderer three;
	ModelRenderer four;
	ModelRenderer five;
	ModelRenderer six;
	ModelRenderer bobble;

	public ModelSantaHat() {
		textureWidth = 64;
		textureHeight = 32;

		base = new ModelRenderer(this, 0, 9);
		base.addBox(0F, 0F, 0F, 9, 2, 9);
		base.setRotationPoint(-4.5F, -2F, -4.5F);
		base.setTextureSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		one = new ModelRenderer(this, 0, 0);
		one.addBox(0F, 0F, 0F, 7, 2, 7);
		one.setRotationPoint(-3.5F, -3.5F, -3.5F);
		one.setTextureSize(64, 32);
		one.mirror = true;
		setRotation(one, -0.1047198F, 0F, 0F);
		two = new ModelRenderer(this, 0, 0);
		two.addBox(0F, 0F, 0F, 6, 2, 6);
		two.setRotationPoint(-3F, -4.6F, -2.5F);
		two.setTextureSize(64, 32);
		two.mirror = true;
		setRotation(two, -0.2617994F, 0F, 0F);
		three = new ModelRenderer(this, 0, 0);
		three.addBox(0F, 0F, 0F, 5, 2, 5);
		three.setRotationPoint(-2.5F, -5.666667F, -1.2F);
		three.setTextureSize(64, 32);
		three.mirror = true;
		setRotation(three, -0.4537856F, 0F, 0F);
		four = new ModelRenderer(this, 0, 0);
		four.addBox(0F, 0F, 0F, 4, 2, 4);
		four.setRotationPoint(-2F, -6.2F, -0.1F);
		four.setTextureSize(64, 32);
		four.mirror = true;
		setRotation(four, -0.6283185F, 0F, 0F);
		five = new ModelRenderer(this, 0, 0);
		five.addBox(0F, 0F, 0F, 3, 2, 3);
		five.setRotationPoint(-1.5F, -6.566667F, 1.333333F);
		five.setTextureSize(64, 32);
		five.mirror = true;
		setRotation(five, -0.8726646F, 0F, 0F);
		six = new ModelRenderer(this, 0, 0);
		six.addBox(0F, 0F, 0F, 2, 2, 2);
		six.setRotationPoint(-1F, -4.2F, 2F);
		six.setTextureSize(64, 32);
		six.mirror = true;
		setRotation(six, 2.03389F, 0F, 0F);
		bobble = new ModelRenderer(this, 0, 11);
		bobble.addBox(0F, 0F, 0F, 3, 3, 3);
		bobble.setRotationPoint(-1.5F, -4.466667F, 3.6F);
		bobble.setTextureSize(64, 32);
		bobble.mirror = true;
		setRotation(bobble, 2.033885F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		base.render(f5);
		one.render(f5);
		two.render(f5);
		three.render(f5);
		four.render(f5);
		five.render(f5);
		six.render(f5);
		bobble.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
	}

}
