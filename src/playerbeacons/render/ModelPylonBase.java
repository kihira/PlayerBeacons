package playerbeacons.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPylonBase extends ModelBase {
	private final ModelRenderer Foot;
	private final ModelRenderer Foot2;
	private final ModelRenderer Connection;
	private final ModelRenderer Pillarstart1;
	private final ModelRenderer Pillarstart2;
	private final ModelRenderer PillarstartTop;
	private final ModelRenderer acc1;
	private final ModelRenderer acc2;
	private final ModelRenderer acc3;
	private final ModelRenderer acc4;
	private final ModelRenderer acc11;
	private final ModelRenderer acc12;
	private final ModelRenderer acc13;
	private final ModelRenderer acc14;
	private final ModelRenderer Shape1;
	private final ModelRenderer Shape2;
	private final ModelRenderer Shape3;
	private final ModelRenderer Shape4;
	private final ModelRenderer Shape5;
	private final ModelRenderer Shape6;
	private final ModelRenderer Shape7;
	private final ModelRenderer Shape8;

	public ModelPylonBase() {
		textureWidth = 64;
		textureHeight = 64;

		Foot = new ModelRenderer(this, 0, 0);
		Foot.addBox(-7.5F, 0F, -7.5F, 15, 1, 15);
		Foot.setRotationPoint(0F, 23F, 0F);
		Foot.mirror = true;
		setRotation(Foot, 0F, 0F, 0F);
		Foot2 = new ModelRenderer(this, 0, 16);
		Foot2.addBox(-7F, 0F, -7F, 14, 1, 14);
		Foot2.setRotationPoint(0F, 22F, 0F);
		Foot2.mirror = true;
		setRotation(Foot2, 0F, 0F, 0F);
		Connection = new ModelRenderer(this, 40, 56);
		Connection.addBox(-3F, 0F, -3F, 6, 2, 6);
		Connection.setRotationPoint(0F, 20F, 0F);
		Connection.mirror = true;
		setRotation(Connection, 0F, 0F, 0F);
		Pillarstart1 = new ModelRenderer(this, 0, 37);
		Pillarstart1.addBox(-4F, -6F, -4F, 8, 3, 8);
		Pillarstart1.setRotationPoint(0F, 23F, 0F);
		Pillarstart1.mirror = true;
		setRotation(Pillarstart1, 0F, 0F, 0F);
		Pillarstart2 = new ModelRenderer(this, 0, 48);
		Pillarstart2.addBox(-5F, 0F, -5F, 10, 6, 10);
		Pillarstart2.setRotationPoint(0F, 11F, 0F);
		Pillarstart2.mirror = true;
		setRotation(Pillarstart2, 0F, 0F, 0F);
		PillarstartTop = new ModelRenderer(this, 32, 37);
		PillarstartTop.addBox(-4F, 8F, -4F, 8, 3, 8);
		PillarstartTop.setRotationPoint(0F, 0F, 0F);
		PillarstartTop.mirror = true;
		setRotation(PillarstartTop, 0F, 0F, 0F);
		acc1 = new ModelRenderer(this, 0, 0);
		acc1.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
		acc1.setRotationPoint(0F, 13.5F, 5F);
		acc1.mirror = true;
		setRotation(acc1, 0F, 0F, 0F);
		acc2 = new ModelRenderer(this, 0, 0);
		acc2.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
		acc2.setRotationPoint(-5F, 13.5F, 0F);
		acc2.mirror = true;
		setRotation(acc2, 0F, -1.570796F, 0F);
		acc3 = new ModelRenderer(this, 0, 0);
		acc3.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
		acc3.setRotationPoint(0F, 13.5F, -5F);
		acc3.mirror = true;
		setRotation(acc3, 0F, -3.141593F, 0F);
		acc4 = new ModelRenderer(this, 0, 0);
		acc4.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
		acc4.setRotationPoint(5F, 13.5F, 0F);
		acc4.mirror = true;
		setRotation(acc4, 0F, 1.570796F, 0F);
		acc11 = new ModelRenderer(this, 0, 31);
		acc11.addBox(-5F, -0.5F, 0F, 10, 1, 1);
		acc11.setRotationPoint(0F, 13.5F, 4.5F);
		acc11.mirror = true;
		setRotation(acc11, 0F, 0F, 0F);
		acc12 = new ModelRenderer(this, 0, 31);
		acc12.addBox(-5F, -0.5F, -0.5F, 10, 1, 1);
		acc12.setRotationPoint(-5F, 13.5F, 0F);
		acc12.mirror = true;
		setRotation(acc12, 0F, -1.570796F, 0F);
		acc13 = new ModelRenderer(this, 0, 31);
		acc13.addBox(-5F, -0.5F, -0.5F, 10, 1, 1);
		acc13.setRotationPoint(0F, 13.5F, -5F);
		acc13.mirror = true;
		setRotation(acc13, 0F, -3.141593F, 0F);
		acc14 = new ModelRenderer(this, 0, 31);
		acc14.addBox(-5F, -0.5F, 0F, 10, 1, 1);
		acc14.setRotationPoint(4.5F, 13.5F, 0F);
		acc14.mirror = true;
		setRotation(acc14, 0F, 1.570796F, 0F);
		Shape1 = new ModelRenderer(this, 0, 4);
		Shape1.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
		Shape1.setRotationPoint(5F, 13F, 5F);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0.7853982F, 0F);
		Shape2 = new ModelRenderer(this, 0, 4);
		Shape2.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
		Shape2.setRotationPoint(5F, 13F, -5F);
		Shape2.mirror = true;
		setRotation(Shape2, 0F, 2.356194F, 0F);
		Shape3 = new ModelRenderer(this, 0, 4);
		Shape3.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
		Shape3.setRotationPoint(-5F, 13F, -5F);
		Shape3.mirror = true;
		setRotation(Shape3, 0F, -0.7853982F, 0F);
		Shape4 = new ModelRenderer(this, 0, 4);
		Shape4.addBox(-0.5F, 0F, -0.5F, 1, 1, 1);
		Shape4.setRotationPoint(-5F, 13F, 5F);
		Shape4.mirror = true;
		setRotation(Shape4, 0F, -2.356194F, 0F);
		Shape5 = new ModelRenderer(this, 9, 0);
		Shape5.addBox(-0.5F, 0F, -1F, 1, 1, 2);
		Shape5.setRotationPoint(4.4F, 8F, 0F);
		Shape5.mirror = true;
		setRotation(Shape5, 0F, 0F, 0F);
		Shape6 = new ModelRenderer(this, 9, 0);
		Shape6.addBox(-0.5F, 0F, -1F, 1, 1, 2);
		Shape6.setRotationPoint(0F, 8F, -4.4F);
		Shape6.mirror = true;
		setRotation(Shape6, 0F, 1.570796F, 0F);
		Shape7 = new ModelRenderer(this, 9, 0);
		Shape7.addBox(-0.5F, 0F, -1F, 1, 1, 2);
		Shape7.setRotationPoint(0F, 8F, 4.4F);
		Shape7.mirror = true;
		setRotation(Shape7, 0F, -1.570796F, 0F);
		Shape8 = new ModelRenderer(this, 9, 0);
		Shape8.addBox(-0.5F, 0F, -1F, 1, 1, 2);
		Shape8.setRotationPoint(-4.4F, 8F, 0F);
		Shape8.mirror = true;
		setRotation(Shape8, 0F, 3.141593F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		Foot.render(f5);
		Foot2.render(f5);
		Connection.render(f5);
		Pillarstart1.render(f5);
		Pillarstart2.render(f5);
		PillarstartTop.render(f5);
		acc1.render(f5);
		acc2.render(f5);
		acc3.render(f5);
		acc4.render(f5);
		acc11.render(f5);
		acc12.render(f5);
		acc13.render(f5);
		acc14.render(f5);
		Shape1.render(f5);
		Shape2.render(f5);
		Shape3.render(f5);
		Shape4.render(f5);
		Shape5.render(f5);
		Shape6.render(f5);
		Shape7.render(f5);
		Shape8.render(f5);
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
