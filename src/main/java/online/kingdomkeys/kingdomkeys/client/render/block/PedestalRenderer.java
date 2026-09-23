package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

import java.util.Map;
import java.util.WeakHashMap;

public class PedestalRenderer implements BlockEntityRenderer<PedestalTileEntity> {

	private final ItemRenderer renderItem;

	private static final class CachedModel {
		final Item item;
		final BakedModel model;
		final ItemStack shown;
		CachedModel(Item item, BakedModel model, ItemStack shown) {
			this.item = item;
			this.model = model;
			this.shown = shown;
		}
	}
	private final Map<PedestalTileEntity, CachedModel> modelCache = new WeakHashMap<>();

	public PedestalRenderer(BlockEntityRendererProvider.Context context) {
		this.renderItem = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(PedestalTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (!tileEntityIn.isStationOfAwakeningMarker()) {
			IItemHandler itemHandler = tileEntityIn.inventory.get();
			if (itemHandler != null) {
				ItemStack held = itemHandler.getStackInSlot(0);
				if (!held.isEmpty()) {
					renderItem(tileEntityIn, matrixStackIn, bufferIn, partialTicks, held, combinedLightIn);
				}
			}
		} else {
			if (!tileEntityIn.hide) {
				renderItem(tileEntityIn, matrixStackIn, bufferIn, partialTicks, tileEntityIn.getDisplayStack(), combinedLightIn);
			}
		}
	}

	private void renderItem(PedestalTileEntity tileEntity, PoseStack matrixStack, MultiBufferSource buffer, float partialTicks, ItemStack toRender, int combinedLightIn) {
		matrixStack.pushPose();
		{
			float height, rotation;
			if (!tileEntity.isPaused()) {
				float lerpedTicks = tileEntity.previousTicks + (tileEntity.ticksExisted() - tileEntity.previousTicks) * partialTicks;
				height = tileEntity.getBaseHeight() + (0.1F * (float) Math.sin(tileEntity.getBobSpeed() * lerpedTicks));
				rotation = lerpedTicks * tileEntity.getRotationSpeed() % 360F;
				tileEntity.setCurrentTransforms(rotation, height);
			} else {
				height = tileEntity.getSavedHeight();
				rotation = tileEntity.getSavedRotation();
			}

			matrixStack.translate(0.5F, height, 0.5F);
			matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
			matrixStack.scale(tileEntity.getScale(), tileEntity.getScale(), tileEntity.getScale());
			if(tileEntity.isFlipped()) {
				matrixStack.mulPose(Axis.ZP.rotationDegrees(180F));
				matrixStack.translate(0, -0.6F, 0);
			}
			CachedModel cached = getOrResolveModel(tileEntity, toRender);
			ItemStack shown = toRender.getItem() instanceof KeychainItem ? cached.shown : toRender;
			renderItem.render(shown, ItemDisplayContext.FIXED, false, matrixStack, buffer, combinedLightIn, OverlayTexture.NO_OVERLAY, cached.model);
		}
		matrixStack.popPose();
	}

	private CachedModel getOrResolveModel(PedestalTileEntity tileEntity, ItemStack toRender) {
		Item item = toRender.getItem();
		CachedModel cached = modelCache.get(tileEntity);
		if (cached != null && cached.item == item) {
			return cached;
		}

		// A keychain on display shows the keyblade it belongs to
		ItemStack shown = item instanceof KeychainItem keychain ? new ItemStack(keychain.getKeyblade()) : toRender;
		BakedModel model = renderItem.getModel(shown, tileEntity.getLevel(), null, 1);
		cached = new CachedModel(item, model, shown);
		modelCache.put(tileEntity, cached);
		return cached;
	}

	@Override
	public AABB getRenderBoundingBox(PedestalTileEntity blockEntity) {
		return new AABB(blockEntity.getBlockPos()).expandTowards(0, 5, 0);
	}

	@Override
	public int getViewDistance() {
		return 32;
	}
}