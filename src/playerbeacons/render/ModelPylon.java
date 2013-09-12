package playerbeacons.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPylon extends ModelBase {
	private final ModelRenderer Main;
	private final ModelRenderer acc1;
	private final ModelRenderer acc2;
	private final ModelRenderer acc3;
	private final ModelRenderer acc4;
	private final ModelRenderer acc11;
	private final ModelRenderer acc21;
	private final ModelRenderer acc31;
	private final ModelRenderer acc41;
	private final ModelRenderer acc42;
	private final ModelRenderer acc32;
	private final ModelRenderer acc12;
	private final ModelRenderer acc22;
	private final ModelRenderer support1;
	private final ModelRenderer support2;
	private final ModelRenderer support3;
	private final ModelRenderer support4;
	private final ModelRenderer Addon1;
	private final ModelRenderer Addon2;
	private final ModelRenderer topbase;
	private final ModelRenderer toptop;

	public ModelPylon() {
		textureWidth = 64;
		textureHeight = 64;

		Main = new ModelRenderer(this, 0, 0);
		Main.addBox(-4F, 0F, -4F, 8, 16, 8);
		Main.setRotationPoint(0F, 8F, 0F);
		Main.mirror = true;
		setRotation(Main, 0F, 0F, 0F);
		acc1 = new ModelRenderer(this, 24, 0);
		acc1.addBox(-0.5F, 0F, -2F, 1, 1, 4);
		acc1.setRotationPoint(-4.5F, 8F, 0F);
		acc1.mirror = true;
		setRotation(acc1, 0F, 0F, 0F);
		acc2 = new ModelRenderer(this, 24, 0);
		acc2.addBox(-0.5F, 0F, -2F, 1, 1, 4);
		acc2.setRotationPoint(4.5F, 8F, 0F);
		acc2.mirror = true;
		setRotation(acc2, 0F, 3.141593F, 0F);
		acc3 = new ModelRenderer(this, 24, 0);
		acc3.addBox(-0.5F, 0F, -2F, 1, 1, 4);
		acc3.setRotationPoint(0F, 8F, 4.5F);
		acc3.mirror = true;
		setRotation(acc3, 0F, 1.570796F, 0F);
		acc4 = new ModelRenderer(this, 24, 0);
		acc4.addBox(-0.5F, 0F, -2F, 1, 1, 4);
		acc4.setRotationPoint(0F, 8F, -4.5F);
		acc4.mirror = true;
		setRotation(acc4, 0F, -1.570796F, 0F);
		acc11 = new ModelRenderer(this, 24, 0);
		acc11.addBox(-0.5F, 0F, -2F, 1, 2, 4);
		acc11.setRotationPoint(-4.4F, 21.9F, 0F);
		acc11.mirror = true;
		setRotation(acc11, 0F, 0F, 0F);
		acc21 = new ModelRenderer(this, 24, 0);
		acc21.addBox(-0.5F, 0F, -2F, 1, 2, 4);
		acc21.setRotationPoint(4.4F, 21.9F, 0F);
		acc21.mirror = true;
		setRotation(acc21, 0F, 3.141593F, 0F);
		acc31 = new ModelRenderer(this, 24, 0);
		acc31.addBox(-0.5F, 0F, -2F, 1, 2, 4);
		acc31.setRotationPoint(0F, 21.9F, 4.4F);
		acc31.mirror = true;
		setRotation(acc31, 0F, 1.570796F, 0F);
		acc41 = new ModelRenderer(this, 24, 0);
		acc41.addBox(-0.5F, 0F, -2F, 1, 2, 4);
		acc41.setRotationPoint(0F, 21.9F, -4.4F);
		acc41.mirror = true;
		setRotation(acc41, 0F, -1.570796F, 0F);
		acc42 = new ModelRenderer(this, 34, 0);
		acc42.addBox(-5F, 0F, -0.5F, 10, 1, 1);
		acc42.setRotationPoint(0F, 23F, -4.5F);
		acc42.mirror = true;
		setRotation(acc42, 0F, 0F, 0F);
		acc32 = new ModelRenderer(this, 34, 0);
		acc32.addBox(-5F, 0F, -0.5F, 10, 1, 1);
		acc32.setRotationPoint(0F, 23F, 4.5F);
		acc32.mirror = true;
		setRotation(acc32, 0F, 3.141593F, 0F);
		acc12 = new ModelRenderer(this, 34, 0);
		acc12.addBox(-5F, 0F, -0.5F, 10, 1, 1);
		acc12.setRotationPoint(-4.5F, 23F, 0F);
		acc12.mirror = true;
		setRotation(acc12, 0F, 1.570796F, 0F);
		acc22 = new ModelRenderer(this, 34, 0);
		acc22.addBox(-5F, 0F, -0.5F, 10, 1, 1);
		acc22.setRotationPoint(4.5F, 23F, 0F);
		acc22.mirror = true;
		setRotation(acc22, 0F, -1.570796F, 0F);
		support1 = new ModelRenderer(this, 0, 24);
		support1.addBox(-0.5F, 0F, -1F, 1, 13, 2);
		support1.setRotationPoint(4.4F, 9F, 0F);
		support1.mirror = true;
		setRotation(support1, 0F, 0F, 0F);
		support2 = new ModelRenderer(this, 0, 24);
		support2.addBox(-0.5F, 0F, -1F, 1, 13, 2);
		support2.setRotationPoint(0F, 9F, 4.4F);
		support2.mirror = true;
		setRotation(support2, 0F, -1.570796F, 0F);
		support3 = new ModelRenderer(this, 0, 24);
		support3.addBox(-0.5F, 0F, -1F, 1, 13, 2);
		support3.setRotationPoint(0F, 9F, -4.4F);
		support3.mirror = true;
		setRotation(support3, 0F, 1.570796F, 0F);
		support4 = new ModelRenderer(this, 0, 24);
		support4.addBox(-0.5F, 0F, -1F, 1, 13, 2);
		support4.setRotationPoint(-4.4F, 9F, 0F);
		support4.mirror = true;
		setRotation(support4, 0F, 3.141593F, 0F);
		Addon1 = new ModelRenderer(this, 14, 30);
		Addon1.addBox(-4F, 0F, -1F, 8, 1, 2);
		Addon1.setRotationPoint(0F, 7.5F, 0F);
		Addon1.mirror = true;
		setRotation(Addon1, 0F, 0F, 0F);
		Addon2 = new ModelRenderer(this, 14, 30);
		Addon2.addBox(-4F, 0F, -1F, 8, 1, 2);
		Addon2.setRotationPoint(0F, 7.5F, 0F);
		Addon2.mirror = true;
		setRotation(Addon2, 0F, 1.570796F, 0F);
		topbase = new ModelRenderer(this, 6, 24);
		topbase.addBox(-2F, 0F, -2F, 4, 2, 4);
		topbase.setRotationPoint(0F, 6F, 0F);
		topbase.mirror = true;
		setRotation(topbase, 0F, 0F, 0F);
		toptop = new ModelRenderer(this, 6, 30);
		toptop.addBox(-1F, 0F, -1F, 2, 6, 2);
		toptop.setRotationPoint(0F, 0F, 0F);
		toptop.mirror = true;
		setRotation(toptop, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		Main.render(f5);
		acc1.render(f5);
		acc2.render(f5);
		acc3.render(f5);
		acc4.render(f5);
		acc11.render(f5);
		acc21.render(f5);
		acc31.render(f5);
		acc41.render(f5);
		acc42.render(f5);
		acc32.render(f5);
		acc12.render(f5);
		acc22.render(f5);
		support1.render(f5);
		support2.render(f5);
		support3.render(f5);
		support4.render(f5);
		Addon1.render(f5);
		Addon2.render(f5);
		topbase.render(f5);
		toptop.render(f5);
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
