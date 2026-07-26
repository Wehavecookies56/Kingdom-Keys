package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.*;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.*;

public class ClientUtils {
    public static void drawCategoryIcon(GuiGraphics gui, ItemCategory category, float x, float y, float scale) {
        PoseStack matrixStack = gui.pose();
        matrixStack.pushPose();
        {
            matrixStack.translate(x, y, 0);
            matrixStack.scale(scale, scale, 1);
            gui.blit(Constants.MENU_TEXTURE, 0, 0, category.getU(), category.getV(), 20, 20);
        }
        matrixStack.popPose();
    }

    public static void drawGloveAndDot(GuiGraphics gui, float ox, float oy, float width, float partialTicks) {
        float ballScale = 0.5F;
        int u = 0;
        int v = 204;

        gui.pose().pushPose();
        {
            float radiusX = 4.5F;
            float radiusY = 6F;
            float centerX = ox + width - radiusX -3;
            float centerY = oy + 3;

            float delta = ClientEvents.ballRot - ClientEvents.prevBallRot;

            if (delta < -180F)
                delta += 360F;
            if (delta > 180F)
                delta -= 360F;

            float interpRot = ClientEvents.prevBallRot + delta * partialTicks;

            float t = (float)Math.toRadians(-interpRot);

            float x = centerX + (float)Math.cos(t * 3F + Math.PI / 2F) * radiusX;
            float y = centerY + (float)Math.sin(t * 2F) * radiusY;

            float gloveX = x - width - 10;
            gui.pose().pushPose();
            {
                gui.pose().translate(gloveX, oy + 3, 0);
                gui.blit(Constants.MENU_TEXTURE, 0, 0, 21, 204, 20, 14);
            }
            gui.pose().popPose();
            gui.pose().pushPose();
            {
                gui.pose().translate(x, y, 0);
                gui.pose().scale(ballScale, ballScale, 1F);
                gui.blit(Constants.MENU_TEXTURE, 0, 0, u, v, 18, 16);
            }
            gui.pose().popPose();
        }
        gui.pose().popPose();
    }

    public static ResourceLocation variantTexture(ResourceLocation base, Entity entity) {
        if (!(entity instanceof BaseKHEntity mob))
            return base;
        String variant = mob.getVariant();
        if (variant == null || variant.isEmpty())
            return base;

        String path = base.getPath();
        int dot = path.lastIndexOf('.');
        if (dot < 0)
            return base;

        ResourceLocation variantLoc = ResourceLocation.fromNamespaceAndPath(base.getNamespace(), path.substring(0, dot) + "_" + variant + path.substring(dot));
        return VARIANT_TEXTURE_EXISTS.computeIfAbsent(variantLoc, loc -> Minecraft.getInstance().getResourceManager().getResource(loc).isPresent()) ? variantLoc : base;
    }

    private static final java.util.Map<ResourceLocation, Boolean> VARIANT_TEXTURE_EXISTS = new java.util.concurrent.ConcurrentHashMap<>();

    public static Style KK_Font_EXP = Style.EMPTY.withFont(KingdomKeys.rl("kk_font_exp"));
    public static Style KK_Font_MENU = Style.EMPTY.withFont(KingdomKeys.rl("kk_font_menu"));
    public static Style KK_Font_TITLE = Style.EMPTY.withFont(KingdomKeys.rl("kk_font_title"));

    //Order is important for overlapping boxes, top to bottom
    public static final HUDElement DRIVE_ELEMENT = new HUDElement("Drive").setScale(0.8F,0.8F);
    public static final HUDElement MP_ELEMENT = new HUDElement("MP").setScale(0.7F, 0.5F);
    public static final HUDElement PORTRAIT_ELEMENT = new HUDElement("Portrait");
    public static final HUDElement FOCUS_ELEMENT = new HUDElement("Focus");
    public static final HPElement HP_ELEMENT = new HPElement("HP").setScale(0.2F,0.2F);
    public static final CMElement CM_ELEMENT = new CMElement("CM");
    public static final HUDElement RC_ELEMENT = new HUDElement("RC");
    public static final LockOnElement LOCKON_ELEMENT = new LockOnElement("LockOn");
    public static final PartyElement PARTY_ELEMENT = new PartyElement("Party");
    public static final HUDElement MUNNYEXP_ELEMENT = new HUDElement("MunnyExp");
    public static final HUDElement LEVELUP_ELEMENT = new HUDElement("LevelUp");
    public static final HUDElement DRIVELEVEL_ELEMENT = new HUDElement("DriveLevel");
    public static final HUDElement ROOMNAME_ELEMENT = new HUDElement("RoomName");
    public static final HUDElement MINIMAP_ELEMENT = new HUDElement("Minimap");
    public static final HUDElement ITEMGET_ELEMENT = new HUDElement("ItemGet");

    public static Entity getEntityByUUIDClient(UUID uuid) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        if (level == null)
            return null;

        for (Entity entity : level.entitiesForRendering()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public static int drawScrollingString(GuiGraphics gui, Font font, Component text, int minX, int maxX, int y, int color, boolean centered){
        int maxWidth = maxX - minX;
        int textWidth = font.width(text.getVisualOrderText());
        if (textWidth <= maxWidth) {
            if (centered) {
                int i = font.width(text);
                int i1 = Mth.clamp((minX + maxX) / 2, minX + i / 2, maxX - i / 2);
                gui.drawCenteredString(font, text, i1+1, y, color);
                return maxWidth;
            } else {
                return gui.drawString(font, text, minX, y, color);
            }
        } else {
            y-=1;
            gui.drawScrollingString(font, text, minX, maxX, y, color);
            return maxWidth;
        }
    }

    public static boolean getResourceExists(String path){
        try {
            Minecraft.getInstance().getResourceManager().getResourceOrThrow(KingdomKeys.rl(path));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static ResourceLocation getResourceExistsOrDefault(String path, String name, String defaultName){
        return KingdomKeys.rl(String.format(path, getResourceExists(String.format(path, name)) ? name : defaultName));
    }

    public static void drawXOnFace(PoseStack stack, VertexConsumer builder, double x, double y, double z, Direction face) {
        float r = 0.0F, g = 0F, b = 0F, a = 1.0F;

        switch (face) {
            case NORTH, SOUTH -> {
                drawLine(builder, stack, x, y, z, x + 1, y + 1, z, r, g, b, a);
                drawLine(builder, stack, x + 1, y, z, x, y + 1, z, r, g, b, a);
            }
            case EAST, WEST -> {
                drawLine(builder, stack, x, y, z, x, y + 1, z + 1, r, g, b, a);
                drawLine(builder, stack, x, y, z + 1, x, y + 1, z, r, g, b, a);
            }
            case UP, DOWN -> {
                drawLine(builder, stack, x, y, z, x + 1, y, z + 1, r, g, b, a);
                drawLine(builder, stack, x + 1, y, z, x, y, z + 1, r, g, b, a);
            }
        }
    }

    public static void drawPlusOnFace(PoseStack stack, VertexConsumer builder, double x, double y, double z, Direction face) {
        float r = 0.0F, g = 0F, b = 0F, a = 1.0F;

        switch (face) {
            case NORTH, SOUTH -> {
                drawLine(builder, stack, x+0.5, y, z, x + 0.5, y + 1, z, r, g, b, a);
                drawLine(builder, stack, x, y+0.5, z, x+1, y + 0.5, z, r, g, b, a);
            }
            case EAST, WEST -> {
                drawLine(builder, stack, x, y+0.5F, z, x, y+0.5F, z+1F, r, g, b, a);
                drawLine(builder, stack, x, y, z + 0.5F, x, y + 1, z+0.5F, r, g, b, a);
            }
            case UP, DOWN -> {
                drawLine(builder, stack, x+0.5F, y, z, x + 0.5, y, z + 1, r, g, b, a);
                drawLine(builder, stack, x + 1, y, z+0.5, x, y, z + 0.5, r, g, b, a);
            }
        }
    }



    public static void drawLine(VertexConsumer builder, PoseStack stack, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a) {
        Matrix4f pose = stack.last().pose();
        PoseStack.Pose lastPose = stack.last();
        float dx = (float)(x2 - x1);
        float dy = (float)(y2 - y1);
        float dz = (float)(z2 - z1);

        float len = Mth.sqrt(dx * dx + dy * dy + dz * dz);

        //Avoid /0
        if (len == 0)
            len = 1;

        float nx = dx / len;
        float ny = dy / len;
        float nz = dz / len;

        builder.addVertex(pose, (float)x1, (float)y1, (float)z1).setColor(r, g, b, a).setNormal(lastPose, nx, ny, nz);
        builder.addVertex(pose, (float)x2, (float)y2, (float)z2).setColor(r, g, b, a).setNormal(lastPose, nx, ny, nz);
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }


    public enum Angle{
    	X,Y,Z
    }
    
    public static class ModelAnimation {
        public ModelPart model;
        public ModelPart modelCounterpart;
        public float defVal;
        public float minVal;
        public float maxVal;
        public float actVal;
        public Angle angle;
        public boolean increasing;

        public ModelAnimation(ModelPart model, float defVal, float minVal, float maxVal, float actVal, boolean increasing, Angle angle, @Nullable ModelPart counterpart) {
            this.model = model;
            this.defVal = defVal;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.actVal = actVal;
            this.increasing = increasing;
            this.angle = angle;
            this.modelCounterpart = counterpart;
        }

        @Override
        public String toString() {
            return defVal + ": " + actVal + " " + increasing;
        }

		public void animate(float clock) {
			if (model == null)
                return;
			float span = maxVal - minVal;
			if (span <= 0) {
				setDefault();
				return;
			}

			float travelled = (clock * 2F) % (2F * span);
			actVal = minVal + (travelled <= span ? travelled : 2F * span - travelled);
			apply(actVal);
		}

		private void apply(float value) {
			switch(angle) {
			case X:
				model.xRot = (float) Math.toRadians(value);
				if(modelCounterpart != null) modelCounterpart.xRot = (float) Math.toRadians(defVal*2-value);
				break;
			case Y:
				model.yRot = (float) Math.toRadians(value);
				if(modelCounterpart != null) modelCounterpart.yRot = (float) Math.toRadians(defVal*2-value);
				break;
			case Z:
				model.zRot = (float) Math.toRadians(value);
				if(modelCounterpart != null) modelCounterpart.zRot = (float) Math.toRadians(defVal*2-value);
				break;
			}
		}

		public void setDefault() {
            if(model != null) {
                switch(angle) {
                case X:
                    model.xRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.xRot = (float) Math.toRadians(defVal);
                    }
                	break;
                case Y:
                    model.yRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.yRot = (float) Math.toRadians(defVal);
                    }
                	break;
                case Z:
                    model.zRot = (float) Math.toRadians(defVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.zRot = (float) Math.toRadians(defVal);
                    }
                	break;
                }
			}
		}
    }
    

    @OnlyIn(Dist.CLIENT)
    public static void blitScaled(ResourceLocation texture, GuiGraphics gui, float x, float y, int u, int v, int width, int height, float scaleX, float scaleY) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(scaleX, scaleY, 1);
        gui.blit(texture, 0, 0, u, v, width, height);
        gui.pose().popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void blitScaled(ResourceLocation texture, GuiGraphics gui, float x, float y, int u, int v, int width, int height, float scaleXY) {
        blitScaled(texture, gui, x, y, u, v, width, height, scaleXY, scaleXY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawStringScaled(GuiGraphics gui, float x, float y, String text, int colour, float scaleX, float scaleY) {
        gui.pose().pushPose();
        gui.pose().translate(x, y, 0);
        gui.pose().scale(scaleX, scaleY, 1);
        gui.drawString(Minecraft.getInstance().font, text, 0, 0, colour);
        gui.pose().popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawStringScaled(GuiGraphics gui, float x, float y, String text, int colour, float scaleXY) {
        drawStringScaled(gui, x, y, text, colour, scaleXY, scaleXY);
    }

    public static void drawSplitString(GuiGraphics gui, String text, int x, int y, int len, int color) {
        gui.drawWordWrap(Minecraft.getInstance().font, FormattedText.of(text), x, y, len, color);
    }

    public static void drawItemAsIcon(ItemStack itemStack, PoseStack poseStack, int positionX, int positionY, int size) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        //Code stolen from ItemRenderer.renderGuiItem and changed to suit scaled items instead of fixing size to 16
        BakedModel itemBakedModel = itemRenderer.getModel(itemStack, null, null, 0);

        Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.pushPose();
        poseStack.translate(positionX, positionY, 100.0F);
        poseStack.translate(8.0D, 8.0D, 0.0D);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.scale(size, size, size);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !itemBakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        itemRenderer.render(itemStack, ItemDisplayContext.GUI, false, poseStack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, itemBakedModel);
        multibuffersource$buffersource.endBatch();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

  //Copy of InventoryScreen.renderEntityInInventory to disable animations, so if it breaks in an update, use that to fix it
  	public static void renderEntity(PoseStack posestack, int pPosX, int pPosY, float pScale, float pMouseX, float pMouseY, Entity entity) {
  		float f = (float)Math.atan(pMouseX / 40.0F);
  		float f1 = (float)Math.atan(pMouseY / 40.0F);
        if(entity instanceof AbstractClientPlayer livingEntity)
  		    renderPlayerNoAnimsRaw(posestack, pPosX, pPosY, (int) pScale, f, f1, livingEntity);
        else
            renderEntityRaw(posestack, pScale, f, f1, entity);
    }

    public static boolean disableEFMAnims = false;
  	
  	//Slightly modified copy of InventoryScreen.renderEntityInInventoryRaw to disable animations, so if it breaks in an update, use that to fix it
  	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void renderPlayerNoAnimsRaw(PoseStack p_275396_, int p_275688_, int p_275245_, int p_275535_, float angleXComponent, float angleYComponent, LivingEntity p_275689_) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = p_275689_.yBodyRot;
        float f3 = p_275689_.getYRot();
        float f4 = p_275689_.getXRot();
        float f5 = p_275689_.yHeadRotO;
        float f6 = p_275689_.yHeadRot;
        p_275689_.yBodyRot = 180.0F + f * 20.0F;
        p_275689_.setYRot(180.0F + f * 40.0F);
        p_275689_.setXRot(-f1 * 20.0F);
        p_275689_.yHeadRot = p_275689_.getYRot();
        p_275689_.yHeadRotO = p_275689_.getYRot();

        Matrix4fStack posestack = RenderSystem.getModelViewStack();
        posestack.pushMatrix();
        posestack.translate(0.0F, 0.0F, 1000.0F);
        RenderSystem.applyModelViewMatrix();
        p_275396_.pushPose();
        p_275396_.translate(p_275688_, p_275245_, -950.0D);
        p_275396_.mulPose((new Matrix4f()).scaling((float) p_275535_, (float) p_275535_, (float) (-p_275535_)));
        p_275396_.mulPose(quaternionf);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (quaternionf1 != null) {
            quaternionf1.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(quaternionf1);
        }

        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) p_275689_);
            ((IDisabledAnimations) renderer).kingdom_Keys$setDisabled(true);
            disableEFMAnims = true;
            renderer.render((AbstractClientPlayer) p_275689_, 0, 1, p_275396_, multibuffersource$buffersource, 15728880);
            renderer = (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer((AbstractClientPlayer) p_275689_);
            ((IDisabledAnimations) renderer).kingdom_Keys$setDisabled(false);
            disableEFMAnims = false;
        });

        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        p_275396_.popPose();
        Lighting.setupFor3DItems();
        posestack.popMatrix();
        RenderSystem.applyModelViewMatrix();

        p_275689_.yBodyRot = f2;
        p_275689_.setYRot(f3);
        p_275689_.setXRot(f4);
        p_275689_.yHeadRotO = f5;
        p_275689_.yHeadRot = f6;
    }

    public static void renderEntityRaw(PoseStack pose, float scale, float angleXComponent, float angleYComponent, Entity entity) {
        Quaternionf qZ = new Quaternionf().rotateZ((float)Math.PI);
        Quaternionf qX = new Quaternionf().rotateX(angleYComponent * 20.0F * ((float)Math.PI / 180F));
        qZ.mul(qX);

        pose.pushPose();
        {
            pose.mulPose(qZ);
            pose.scale(-scale, scale, scale);

            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

            if (qX != null) {
                Quaternionf inv = new Quaternionf(qX).conjugate();
                dispatcher.overrideCameraOrientation(inv);
            }

            dispatcher.setRenderShadow(false);
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

            RenderSystem.runAsFancy(() -> {
                EntityRenderer<? super Entity> renderer = dispatcher.getRenderer(entity);
                pose.translate(0,0,-100);
                pose.mulPose(Axis.YP.rotationDegrees(180));
                renderer.render(entity, 0, 1, pose, buffer, 15728880);
            });

            buffer.endBatch();
            dispatcher.setRenderShadow(true);
            Lighting.setupFor3DItems();
        }
        pose.popPose();
    }
  	public static List<Component> getTooltip(List<Component> tooltip, Item.TooltipContext context, ItemStack stack) {
          if (context.level() != null) {
              float baseStr = 0, baseMag = 0;
              float totalStr = 0, totalMag = 0;
              String desc = "";
              MutableComponent ln1 = null;

              KeybladeItem kbItem = null;

              if (stack == null)
                  return tooltip;

              if (stack.getItem() instanceof KeybladeItem keyblade) {
                  kbItem = keyblade;
              } else if (stack.getItem() instanceof KeychainItem keychain) {
                  kbItem = keychain.getKeyblade();
              }

              if (kbItem != null) {
                  if (kbItem.getKeybladeLevel(stack) > 0)
                      ln1 = (Component.translatable(ChatFormatting.YELLOW + "Level %s", kbItem.getKeybladeLevel(stack)));

                  baseStr = kbItem.getStrength(kbItem.getKeybladeLevel(stack)) + DamageCalculation.getSharpnessDamage(stack, context.level().registryAccess());
                  totalStr = DamageCalculation.getKBStrengthDamage(Minecraft.getInstance().player, stack) + DamageCalculation.getSharpnessDamage(stack, context.level().registryAccess());

                  baseMag = kbItem.getMagic(kbItem.getKeybladeLevel(stack));
                  totalMag = DamageCalculation.getMagicDamage(Minecraft.getInstance().player, stack);

                  desc = kbItem.getDesc();

              } else if (stack.getItem() instanceof IOrgWeapon orgItem) {
                  ln1 = Component.translatable(ChatFormatting.YELLOW + "" + orgItem.getMember());

                  baseStr = orgItem.getStrength() + DamageCalculation.getSharpnessDamage(stack, context.level().registryAccess());
                  totalStr = DamageCalculation.getOrgStrengthDamage(Minecraft.getInstance().player, stack) + DamageCalculation.getSharpnessDamage(stack, context.level().registryAccess());

                  baseMag = orgItem.getMagic();
                  totalMag = DamageCalculation.getOrgMagicDamage(Minecraft.getInstance().player, orgItem);

                  desc = orgItem.getDesc();
              }

              if (ln1 != null)
                  tooltip.add(ln1);

              tooltip.add(Component.translatable(ChatFormatting.RED + Utils.translateToLocal(Strings.Gui_Menu_Status_Strength) + " %s", baseStr + " [" + totalStr + "]"));
              tooltip.add(Component.translatable(ChatFormatting.BLUE + Utils.translateToLocal(Strings.Gui_Menu_Status_Magic) + " %s", baseMag + " [" + totalMag + "]"));
              if (stack.getItem() instanceof IExtendedReach extendedReach) {
                tooltip.add(Component.translatable("kingdomkeys.keyblade.reach", extendedReach.getReach()).withStyle(ChatFormatting.AQUA));
              }
              tooltip.add(Component.translatable(ChatFormatting.WHITE + "" + ChatFormatting.ITALIC + desc));

          }
		return tooltip;
	}

    public static Matrix4f getMVMatrix(PoseStack poseStack, float posX, float posY, float posZ, float x, float y, float z, boolean lockRotation, float partialTicks) {
        poseStack.pushPose();
        poseStack.translate(-posX, -posY, -posZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        float screenX = posX + x;
        float screenY = posY + y;
        float screenZ = posZ + z;

        Matrix4f viewMatrix = poseStack.last().pose();
        Matrix4f finalMatrix = new Matrix4f();
        finalMatrix.translate(-screenX, screenY, -screenZ);
        poseStack.popPose();

        if (lockRotation) {
            finalMatrix.m00(viewMatrix.m00());
            finalMatrix.m01(viewMatrix.m10());
            finalMatrix.m02(viewMatrix.m20());
            finalMatrix.m10(viewMatrix.m01());
            finalMatrix.m11(viewMatrix.m11());
            finalMatrix.m12(viewMatrix.m21());
            finalMatrix.m20(viewMatrix.m02());
            finalMatrix.m21(viewMatrix.m12());
            finalMatrix.m22(viewMatrix.m22());
        }

        finalMatrix = viewMatrix.mul(finalMatrix);

        return finalMatrix;
    }

    public static Matrix4f getMVMatrix(PoseStack poseStack, LivingEntity entity, float x, float y, float z, boolean lockRotation, float partialTicks) {
        float posX = (float) Mth.lerp(partialTicks, entity.xOld, entity.getX());
        float posY = (float)Mth.lerp(partialTicks, entity.yOld, entity.getY());
        float posZ = (float)Mth.lerp(partialTicks, entity.zOld, entity.getZ());
        return getMVMatrix(poseStack,posX,posY,posZ,x,y,z,lockRotation,partialTicks);
    }

    public static final RenderType LOCK_ON_INDICATOR = RenderType.create(KingdomKeys.MODID + ":lock_on_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(KingdomKeys.rl("textures/gui/lockon_0.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType LOCK_ON_INNER = RenderType.create(KingdomKeys.MODID + ":lock_on_inner", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(KingdomKeys.rl("textures/gui/lockon_1.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID + ":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(KingdomKeys.rl("textures/gui/shotlock_indicator.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType ULTIMATE_SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID+":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(KingdomKeys.rl("textures/gui/ultimate_shotlock_indicator.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType AIRSTEP_INDICATOR = RenderType.create(KingdomKeys.MODID+":airstep_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(KingdomKeys.rl("textures/gui/airstep_indicator.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));


    //Lock on
    public static void drawLockOnIndicator(int entityID, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Entity target = mc.level.getEntity(entityID);
        if(target == null)
            return;

        double x = Mth.lerp(partialTicks, target.xOld, target.getX());
        double y = Mth.lerp(partialTicks, target.yOld, target.getY());
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ());

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        poseStack.pushPose();
        {
            poseStack.translate(x - camPos.x, y - camPos.y + target.getBbHeight() * 0.5, z - camPos.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

            float size = 0.5f;
            Matrix4f mat = poseStack.last().pose();
            ClientUtils.drawTexturedModalRect2DPlane(mat, buffer.getBuffer(LOCK_ON_INDICATOR), -size, -size, size, size, 0, 0, 256, 256);

            poseStack.pushPose();
            {
                float ticks = target.tickCount + partialTicks;
                float rotation = ticks * ModConfigs.lockOnIconRotation * -0.5f;

                poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));

                mat = poseStack.last().pose();
                ClientUtils.drawTexturedModalRect2DPlane(mat, buffer.getBuffer(LOCK_ON_INNER), -size, -size, size, size, 0, 0, 256, 256);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    // Ultimate shotlock
    public static void drawSingleShotlockIndicator(int entityID, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
        if (shotlock == null)
            return;

        Entity target = mc.level.getEntity(entityID);
        if(target == null)
            return;

        double x = Mth.lerp(partialTicks, target.xOld, target.getX());
        double y = Mth.lerp(partialTicks, target.yOld, target.getY());
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ());

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        poseStack.pushPose();
        {
            poseStack.translate(x - camPos.x, y - camPos.y + target.getBbHeight() * 0.5, z - camPos.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

            float size = 1.5F + shotlock.getRealCooldown(mc.player) * 0.2F - ClientEvents.focusingAnEntityTicks * 0.2F;
            Matrix4f mat = poseStack.last().pose();
            drawTexturedModalRect2DPlane(mat, buffer.getBuffer(ULTIMATE_SHOTLOCK_INDICATOR), -size, -size, size, size, 0, 0, 256, 256);
        }
        poseStack.popPose();
    }

    //Normal shotlock
    public static void drawShotlockIndicator(Utils.ShotlockPosition shotlockPosition, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
        if (shotlock == null)
            return;

        Entity target = mc.level.getEntity(shotlockPosition.id());
        if(target == null)
            return;

        double ex = Mth.lerp(partialTicks, target.xo, target.getX());
        double ey = Mth.lerp(partialTicks, target.yo, target.getY());
        double ez = Mth.lerp(partialTicks, target.zo, target.getZ());

        double x = ex + shotlockPosition.x();
        double y = ey + shotlockPosition.y();
        double z = ez + shotlockPosition.z();

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        poseStack.pushPose();
        {
            poseStack.translate(x - camPos.x, y - camPos.y, z - camPos.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

            float size = 0.3F;
            Matrix4f mat = poseStack.last().pose();
            drawTexturedModalRect2DPlane(mat, buffer.getBuffer(SHOTLOCK_INDICATOR), -size, -size, size, size, 0, 0, 256, 256);
        }
        poseStack.popPose();
    }

    // Airstep to block
    public static void drawShotlockIndicator(BlockPos pos, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

        float x = (float) (pos.getX() + 0.5 - camPos.x);
        float y = (float) (pos.getY() + 0.5 - camPos.y);
        float z = (float) (pos.getZ() + 0.5 - camPos.z);

        Matrix4f mvMatrix = getMVMatrix(poseStack, x, y, z, 0.5F, 0.5F, 0.5F, true, partialTicks);

        mvMatrix.rotate(mc.getEntityRenderDispatcher().cameraOrientation());

        drawTexturedModalRect2DPlane(mvMatrix, buffer.getBuffer(AIRSTEP_INDICATOR), -0.6f, -0.6f, 0.6f, 0.6f, 0, 0, 256, 256);
    }

    // Airstep shotlock
    public static void drawAirstepIndicator(int entityID, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Shotlock shotlock = Utils.getPlayerShotlock(mc.player);
        if (shotlock == null)
            return;

        Entity target = mc.level.getEntity(entityID);
        if(target == null)
            return;

        double x = Mth.lerp(partialTicks, target.xOld, target.getX());
        double y = Mth.lerp(partialTicks, target.yOld, target.getY());
        double z = Mth.lerp(partialTicks, target.zOld, target.getZ());

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        poseStack.pushPose();
        {
            poseStack.translate(x - camPos.x, y - camPos.y + target.getBbHeight() * 0.5, z - camPos.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

            float size = 1.5F + shotlock.getRealCooldown(mc.player) * 0.2F - ClientEvents.focusingAnEntityTicks * 0.2F;
            Matrix4f mat = poseStack.last().pose();
            drawTexturedModalRect2DPlane(mat, buffer.getBuffer(AIRSTEP_INDICATOR), -size, -size, size, size, 0, 0, 256, 256);
        }
        poseStack.popPose();
    }


    public static void drawTexturedModalRect2DPlane(Matrix4f matrix, VertexConsumer vertexBuilder, float minX, float minY, float maxX, float maxY, float minTexU, float minTexV, float maxTexU, float maxTexV) {
        RenderSystem.depthMask(false);
        drawTexturedModalRect3DPlane(matrix, vertexBuilder, minX, minY, 0, maxX, maxY, 0, minTexU, minTexV, maxTexU, maxTexV);
        RenderSystem.depthMask(true);
    }

    public static void drawTexturedModalRect3DPlane(Matrix4f matrix, VertexConsumer vertexBuilder, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float minTexU, float minTexV, float maxTexU, float maxTexV) {
        float cor = 0.00390625F;
        vertexBuilder.addVertex(matrix, minX, minY, maxZ).setUv((minTexU * cor), (maxTexV) * cor);
        vertexBuilder.addVertex(matrix, maxX, minY, maxZ).setUv((maxTexU * cor), (maxTexV) * cor);
        vertexBuilder.addVertex(matrix, maxX, maxY, minZ).setUv((maxTexU * cor), (minTexV) * cor);
        vertexBuilder.addVertex(matrix, minX, maxY, minZ).setUv((minTexU * cor), (minTexV) * cor);
    }

    public static PlayerData readPlayerData(CompoundTag data, int player) {
        PlayerData playerData = PlayerData.get((Player) Minecraft.getInstance().level.getEntity(player));
        playerData.deserializeNBT(Minecraft.getInstance().level.registryAccess(), data);
        return playerData;
    }

    /**
     * Used in the KO system so it doesn't rotate
     */
    public static void renderNameTag(LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, LivingEntity entity, String displayName, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float partialTick) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        double d0 = dispatcher.distanceToSqr(entity);
        if (ClientHooks.isNameplateInRenderDistance(entity, d0)) {
            Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTick));
            if (vec3 != null) {
                boolean flag = !entity.isDiscrete();
                int i = "deadmau5".equals(displayName) ? -10 : 0;
                poseStack.pushPose();
                {
                    poseStack.translate(vec3.x, vec3.y + (double) 0.5F, vec3.z);
                    poseStack.mulPose(dispatcher.cameraOrientation());
                    poseStack.scale(0.025F, -0.025F, 0.025F);
                    Matrix4f matrix4f = poseStack.last().pose();
                    float f = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
                    int j = (int) (f * 255.0F) << 24;
                    Font font = renderer.getFont();
                    float f1 = (float) (-font.width(displayName) / 2);
                    font.drawInBatch(displayName, f1, (float) i, 553648127, false, matrix4f, bufferSource, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, packedLight);
                    if (flag) {
                        font.drawInBatch(displayName, f1, (float) i, 0xFFFFFF, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, packedLight);
                    }
                }
                poseStack.popPose();
            }
        }

    }

    public static void renderHeart(PoseStack matrixStackIn, MultiBufferSource bufferIn, LivingEntity entitylivingbaseIn) {
        VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
        matrixStackIn.pushPose();
        {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(KingdomKeys.rl("entity/heart")));
            matrixStackIn.scale(0.005F, 0.005F, 0.005F);
            matrixStackIn.translate(0, 300, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(entitylivingbaseIn.tickCount*5));

            for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, RenderType.cutout())) {
                buffer.putBulkData(matrixStackIn.last(), quad, 1, 1, 1, 1, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
            }
        }
        matrixStackIn.popPose();
    }

    public static final RenderType shipHologramRenderType = RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS);

    /**
     * Copied from {@link net.minecraft.client.renderer.block.BlockRenderDispatcher#renderSingleBlock(BlockState, PoseStack, MultiBufferSource, int, int, ModelData, RenderType)} modified to use alpha
     */
    public static void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelData modelData, float alpha) {
        RenderShape rendershape = state.getRenderShape();
        if (rendershape != RenderShape.INVISIBLE) {
            switch (rendershape) {
                case MODEL:
                    BakedModel bakedmodel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
                    int i = Minecraft.getInstance().getBlockRenderer().blockColors.getColor(state, null, null, 0);
                    float r = (float)(i >> 16 & 255) / 255.0F;
                    float g = (float)(i >> 8 & 255) / 255.0F;
                    float b = (float)(i & 255) / 255.0F;

                    VertexConsumer consumer = bufferSource.getBuffer(shipHologramRenderType);
                    renderModel(poseStack.last(), consumer, state, bakedmodel, r, g, b, alpha, packedLight, packedOverlay, modelData, shipHologramRenderType);
                    return;
                case ENTITYBLOCK_ANIMATED:
                    ItemStack stack = new ItemStack(state.getBlock());
                    IClientItemExtensions.of(stack).getCustomRenderer().renderByItem(stack, ItemDisplayContext.NONE, poseStack, bufferSource, packedLight, packedOverlay);
            }
        }
    }

    /**
     * Copied from {@link net.minecraft.client.renderer.block.ModelBlockRenderer#renderModel(PoseStack.Pose, VertexConsumer, BlockState, BakedModel, float, float, float, int, int, ModelData, RenderType)} modified to use alpha
     */
    private static void renderModel(PoseStack.Pose pose, VertexConsumer consumer, @Nullable BlockState state, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;
        Direction[] var15 = Direction.values();

	    for (Direction direction : var15) {
		    randomsource.setSeed(i);
		    renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, randomsource, modelData, renderType), packedLight, packedOverlay);
	    }

        randomsource.setSeed(i);
        renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, (Direction)null, randomsource, modelData, renderType), packedLight, packedOverlay);
    }

    /**
     * Copied from {@link net.minecraft.client.renderer.block.ModelBlockRenderer#renderQuadList} modified to use alpha
     */
    private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay) {
        BakedQuad bakedquad;
        float f;
        float f1;
        float f2;
        for(Iterator<BakedQuad> var8 = quads.iterator(); var8.hasNext(); consumer.putBulkData(pose, bakedquad, f, f1, f2, alpha, packedLight, packedOverlay)) {
            bakedquad = var8.next();
            if (bakedquad.isTinted()) {
                f = Mth.clamp(red, 0.0F, 1.0F);
                f1 = Mth.clamp(green, 0.0F, 1.0F);
                f2 = Mth.clamp(blue, 0.0F, 1.0F);
            } else {
                f = 1.0F;
                f1 = 1.0F;
                f2 = 1.0F;
            }
        }

    }


    /**
     * Stuff for trails
     */

    public enum TrailType {
        FLOWMOTION, DASH
    }
    private static final Map<UUID, Deque<Vec3>> FLOW_TRAILS = new HashMap<>();
    private static final Map<UUID, Deque<Vec3>> DASH_TRAILS = new HashMap<>();

    private static Deque<Vec3> getTrail(TrailType type, Player player) {
        return switch(type){
            case FLOWMOTION -> FLOW_TRAILS.computeIfAbsent(player.getUUID(), k -> new ArrayDeque<>());
            case DASH -> DASH_TRAILS.computeIfAbsent(player.getUUID(), k -> new ArrayDeque<>());
        };
    }

    public static void updateTrail(TrailType type, Player player, float partialTick, int MAX_POINTS) {
        Deque<Vec3> trail = getTrail(type, player);
        Vec3 pos = player.getPosition(partialTick);
        trail.addLast(pos);

        if (trail.size() > MAX_POINTS) {
            trail.removeFirst();
        }
    }

    public static void fadeTrail(TrailType type, Player player) {
        Deque<Vec3> trail = getTrail(type, player);

        if (!trail.isEmpty()) {
            trail.removeFirst();
        }
    }

    private static final float TRAIL_WIDTH = 0.03F;

    public static void renderTrail(TrailType type, Player player, PoseStack poseStack, MultiBufferSource bufferSource, float offsetAmount, float verticalOffset, float r, float g, float b, boolean oscillate) {
        Deque<Vec3> trail = getTrail(type, player);

        if (trail.size() < 2)
            return;

        Vec3[] spine = new Vec3[trail.size()];
        int idx = 0;
        Iterator<Vec3> it = trail.descendingIterator();
        while (it.hasNext()) {
            spine[idx++] = it.next();
        }

        int count = spine.length;
        Vec3[] offsetPoints = new Vec3[count];

        for (int i = 0; i < count; i++) {
            Vec3 curr = spine[i];
            Vec3 prev = i < count - 1 ? spine[i + 1] : curr;
            Vec3 next = i > 0 ? spine[i - 1] : curr;

            Vec3 dirVec = next.subtract(prev);
            Vec3 dir = dirVec.lengthSqr() < 1E-6 ? new Vec3(0, 0, 1) : dirVec.normalize();

            Vec3 offset;

            if (oscillate) {
                //Spiral
                Vec3 arbitrary = Math.abs(dir.y) < 0.99 ? new Vec3(0,1,0) : new Vec3(1,0,0);

                Vec3 right = dir.cross(arbitrary).normalize();
                Vec3 up = dir.cross(right).normalize();

                float t = i / (float) count;
                float angle = t * 10f + player.tickCount * 0.2f;
                float radius = 0.05f * offsetAmount;

                offset = right.scale((double)Math.cos(angle) * radius).add(up.scale((double)Math.sin(angle) * radius));
                offset = offset.add(new Vec3(0, verticalOffset, 0));
            } else {
                //Fixed
                Vec3 worldUp = new Vec3(0, 1, 0);
                Vec3 side = dir.cross(worldUp).normalize().scale(0.05f);

                Vec3 horizontalOffset = side.scale(offsetAmount);
                Vec3 vertical = worldUp.scale(verticalOffset);

                offset = horizontalOffset.add(vertical);
            }

            offsetPoints[i] = curr.add(offset);
        }

        poseStack.pushPose();
        {
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugQuads());
            Matrix4f pose = poseStack.last().pose();
            TrailRenderer.render(offsetPoints, Vec3.ZERO, pose, buffer, r, g, b, TRAIL_WIDTH);
        }
        poseStack.popPose();
    }

    /**
     * Render magnet blox mini trails
     */
    public static void renderMiniTrails(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {
        if(MINI_TRAILS.isEmpty())
            return;

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugQuads());
        Matrix4f pose = poseStack.last().pose();
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        double maxDistanceSqr = 80 * 80;

        for (MiniTrail t : MINI_TRAILS) {
            Vec3 p1 = t.getPos(partialTick);
            if (p1.distanceToSqr(camPos) > maxDistanceSqr)
                continue;

            float length = 0.5f;

            Vec3 p2 = p1.add(t.dir.scale(length));

            if (t.side.lengthSqr() < 0.0001)
                t.side = new Vec3(0,1,0);

            float alpha = 0.2F;
            float width = 0.015f;

            Vec3 off1 = t.side.scale(width);
            Vec3 off2 = t.up.scale(width);

            //We draw 2 quads to make a cross
            drawQuad(buffer, pose, p1, p2, off1, t.r, t.g, t.b, alpha);
            drawQuad(buffer, pose, p1, p2, off2, t.r, t.g, t.b, alpha);
        }
    }

    /**
     * Helper function to draw the quads
     */
    private static void drawQuad(VertexConsumer buffer, Matrix4f pose, Vec3 p1, Vec3 p2, Vec3 offset, float r, float g, float b, float alpha) {
        Vec3 p1A = p1.add(offset);
        Vec3 p1B = p1.subtract(offset);
        Vec3 p2A = p2.add(offset);
        Vec3 p2B = p2.subtract(offset);

        buffer.addVertex(pose, (float)p1A.x, (float)p1A.y, (float)p1A.z)
                .setColor(r, g, b, alpha)
                .setNormal(0,1,0);

        buffer.addVertex(pose, (float)p2A.x, (float)p2A.y, (float)p2A.z)
                .setColor(r, g, b, alpha)
                .setNormal(0,1,0);

        buffer.addVertex(pose, (float)p2B.x, (float)p2B.y, (float)p2B.z)
                .setColor(r, g, b, alpha)
                .setNormal(0,1,0);

        buffer.addVertex(pose, (float)p1B.x, (float)p1B.y, (float)p1B.z)
                .setColor(r, g, b, alpha)
                .setNormal(0,1,0);
    }

    /**
     * Update the magnet blox mini trails
     */
    public static void updateMiniTrails() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null)
            return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
        double maxDistanceSqr = 80 * 80;

        Iterator<MiniTrail> it = MINI_TRAILS.iterator();
        while (it.hasNext()) {
            MiniTrail t = it.next();
            Vec3 pos = t.getPos(0);

            if (pos.distanceToSqr(camPos) > maxDistanceSqr) {
                it.remove();
                continue;
            }

            float delta = mc.getTimer().getGameTimeDeltaTicks();
            t.progress += t.speed * delta;

            if (t.progress >= 1f) {
                it.remove();
            }
        }
    }

    private static final List<MiniTrail> MINI_TRAILS = new ArrayList<>();

    private static class MiniTrail {
        Vec3 start;
        Vec3 target;

        float progress;
        float speed;

        float r, g, b;
        boolean attract;

        Vec3 dir;

        Vec3 arbitrary;
        Vec3 side;
        Vec3 up;


        public MiniTrail(Vec3 start, Vec3 target, float speed, boolean attract) {
            this.start = start;
            this.target = target;
            this.progress = 0f;
            this.speed = speed;
            this.attract = attract;
            this.dir = target.subtract(start).normalize();
            this.arbitrary = Math.abs(dir.y) < 0.99 ? new Vec3(0,1,0) : new Vec3(1,0,0);
            this.side = dir.cross(arbitrary).normalize();
            this.up = side.cross(dir).normalize();

            this.r = attract ? 1 : 0;
            this.g = 0;
            this.b = attract ? 0 : 1;
        }

        public Vec3 getPos(float partialTick) {
            float p = Math.min(1f, progress + partialTick * speed);
            return start.lerp(target, p);
        }
    }

    public static void spawnRandomMiniTrail(BlockPos pos, Direction facing, int range, boolean attract) {
        RandomSource rand = Minecraft.getInstance().level.getRandom();

        Vec3 base = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        Vec3 forward = new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ());
        Vec3 arbitrary = Math.abs(forward.y) < 0.99 ? new Vec3(0,1,0) : new Vec3(1,0,0);

        Vec3 right = forward.cross(arbitrary).normalize();
        Vec3 up = forward.cross(right).normalize();

        float spreadAmount = 0.4f;

        Vec3[] offsets = new Vec3[] {
                Vec3.ZERO,
                right.add(up).normalize().scale(spreadAmount),
                right.subtract(up).normalize().scale(spreadAmount),
                right.scale(-1).add(up).normalize().scale(spreadAmount),
                right.scale(-1).subtract(up).normalize().scale(spreadAmount)
        };

        Vec3 spread = offsets[rand.nextInt(offsets.length)];

        Vec3 farPoint = base.add(forward.scale(range));

        Vec3 start;
        Vec3 target;

        if (attract) {
            start = farPoint.add(spread);
            target = base.add(spread);
        } else {
            start = base.add(spread);
            target = farPoint.add(spread);
        }

        float speed = 0.02f;

        MINI_TRAILS.add(new MiniTrail(start, target, speed, attract));
    }

    //copy of GuiGraphics blit methods but modified to batch render and with innerStretch boolean to replicate the 1.21.2+ mcmeta property
    public static void blitSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int width, int height, boolean innerStretch) {
        blitSprite(guiGraphics, sprite, x, y, width, height, 0, innerStretch);
    }

    public static void blitSprite(GuiGraphics guiGraphics, ResourceLocation sprite, int x, int y, int width, int height, int blitOffset, boolean innerStretch) {
        GuiSpriteManager sprites = Minecraft.getInstance().getGuiSprites();
        TextureAtlasSprite textureatlassprite = sprites.getSprite(sprite);
        GuiSpriteScaling guispritescaling = sprites.getSpriteScaling(textureatlassprite);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        if (guispritescaling instanceof GuiSpriteScaling.Stretch) {
            blitSprite(guiGraphics, bufferbuilder, textureatlassprite, x, y, width, height, blitOffset);
        } else if (guispritescaling instanceof GuiSpriteScaling.Tile) {
            GuiSpriteScaling.Tile guispritescaling$tile = (GuiSpriteScaling.Tile)guispritescaling;
            blitTiledSprite(guiGraphics, bufferbuilder, textureatlassprite, x, y, width, height, 0, 0, guispritescaling$tile.width(), guispritescaling$tile.height(), guispritescaling$tile.width(), guispritescaling$tile.height(), blitOffset);
        } else if (guispritescaling instanceof GuiSpriteScaling.NineSlice guispritescaling$nineslice) {
            blitNineSlicedSprite(guiGraphics, bufferbuilder, textureatlassprite, guispritescaling$nineslice, x, y, width, height, blitOffset, innerStretch);
        }
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
    }

    private static void blitNineSlicedSprite(GuiGraphics guiGraphics, BufferBuilder bufferbuilder, TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int width, int height, int blitOffset, boolean innerStretch) {
        RenderSystem.setShaderTexture(0, sprite.atlasLocation());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        GuiSpriteScaling.NineSlice.Border guispritescaling$nineslice$border = nineSlice.border();
        int i = Math.min(guispritescaling$nineslice$border.left(), width / 2);
        int j = Math.min(guispritescaling$nineslice$border.right(), width / 2);
        int k = Math.min(guispritescaling$nineslice$border.top(), height / 2);
        int l = Math.min(guispritescaling$nineslice$border.bottom(), height / 2);
        if (width == nineSlice.width() && height == nineSlice.height()) {
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, height, blitOffset);
        } else if (height == nineSlice.height()) {
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, height, blitOffset);
            blitTiledSprite(guiGraphics, bufferbuilder, sprite, x + i, y, width - j - i, height, i, 0, nineSlice.width() - j - i, nineSlice.height(), nineSlice.width(), nineSlice.height(), blitOffset);
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, height, blitOffset);
        } else if (width == nineSlice.width()) {
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, k, blitOffset);
            blitTiledSprite(guiGraphics, bufferbuilder, sprite, x, y + k, width, height - l - k, 0, k, nineSlice.width(), nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), blitOffset);
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, width, l, blitOffset);
        } else {
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, i, k, blitOffset);
            blitNineSliceInnerSegment(guiGraphics, bufferbuilder, sprite, x + i, y, width - j - i, k, i, 0, nineSlice.width() - j - i, k, nineSlice.width(), nineSlice.height(), blitOffset, innerStretch);
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, 0, x + width - j, y, j, k, blitOffset);
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - l, x, y + height - l, i, l, blitOffset);
            blitNineSliceInnerSegment(guiGraphics, bufferbuilder, sprite, x + i, y + height - l, width - j - i, l, i, nineSlice.height() - l, nineSlice.width() - j - i, l, nineSlice.width(), nineSlice.height(), blitOffset, innerStretch);
            blitSprite(guiGraphics, bufferbuilder, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - j, nineSlice.height() - l, x + width - j, y + height - l, j, l, blitOffset);
            blitNineSliceInnerSegment(guiGraphics, bufferbuilder, sprite, x, y + k, i, height - l - k, 0, k, i, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), blitOffset, innerStretch);
            blitNineSliceInnerSegment(guiGraphics, bufferbuilder, sprite, x + i, y + k, width - j - i, height - l - k, i, k, nineSlice.width() - j - i, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), blitOffset, innerStretch);
            blitNineSliceInnerSegment(guiGraphics, bufferbuilder, sprite, x + width - j, y + k, i, height - l - k, nineSlice.width() - j, k, j, nineSlice.height() - l - k, nineSlice.width(), nineSlice.height(), blitOffset, innerStretch);
        }
    }

    private static void blitNineSliceInnerSegment(GuiGraphics guiGraphics, BufferBuilder bufferBuilder, TextureAtlasSprite sprite, int x, int y, int width, int height, int uPosition, int vPosition, int spriteWidth, int spriteHeight, int nineSliceWidth, int nineSliceHeight, int blitOffset, boolean innerStretch) {
        if (width > 0 && height > 0) {
            if (innerStretch) {
                innerBlit(guiGraphics, bufferBuilder, sprite.atlasLocation(), x, x + width, y, y + height, sprite.getU((float)uPosition / (float)nineSliceWidth), sprite.getU((float)(uPosition + spriteWidth) / (float)nineSliceWidth), sprite.getV((float)vPosition / (float)nineSliceHeight), sprite.getV((float)(vPosition + spriteHeight) / (float)nineSliceHeight), blitOffset);
            } else {
                blitTiledSprite(guiGraphics, bufferBuilder, sprite, x, y, width, height, uPosition, vPosition, spriteWidth, spriteHeight, nineSliceWidth, nineSliceHeight, blitOffset);
            }
        }

    }

    private static void blitTiledSprite(GuiGraphics guiGraphics, BufferBuilder bufferBuilder, TextureAtlasSprite sprite, int x, int y, int width, int height, int uPosition, int vPosition, int spriteWidth, int spriteHeight, int nineSliceWidth, int nineSliceHeight, int blitOffset) {
        if (width > 0 && height > 0) {
            if (spriteWidth <= 0 || spriteHeight <= 0) {
                throw new IllegalArgumentException("Tiled sprite texture size must be positive, got " + spriteWidth + "x" + spriteHeight);
            }

            for(int i = 0; i < width; i += spriteWidth) {
                int j = Math.min(spriteWidth, width - i);

                for(int k = 0; k < height; k += spriteHeight) {
                    int l = Math.min(spriteHeight, height - k);
                    blitSprite(guiGraphics, bufferBuilder, sprite, nineSliceWidth, nineSliceHeight, uPosition, vPosition, x + i, y + k, j, l, blitOffset);
                }
            }
        }

    }

    private static void blitSprite(GuiGraphics guiGraphics, BufferBuilder bufferBuilder, TextureAtlasSprite sprite, int x, int y, int width, int height, int blitOffset) {
        if (width != 0 && height != 0) {
            innerBlit(guiGraphics, bufferBuilder, sprite.atlasLocation(), x, x + width, y, y + height, sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(), blitOffset);
        }

    }

    private static void blitSprite(GuiGraphics guiGraphics, BufferBuilder bufferBuilder, TextureAtlasSprite sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight, int blitOffset) {
        if (uWidth != 0 && vHeight != 0) {
            innerBlit(guiGraphics, bufferBuilder, sprite.atlasLocation(), x, x + uWidth, y, y + vHeight, sprite.getU((float)uPosition / (float)textureWidth), sprite.getU((float)(uPosition + uWidth) / (float)textureWidth), sprite.getV((float)vPosition / (float)textureHeight), sprite.getV((float)(vPosition + vHeight) / (float)textureHeight), blitOffset);
        }

    }

    private static void innerBlit(GuiGraphics guiGraphics, BufferBuilder bufferBuilder, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV, int blitOffset) {
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        bufferBuilder.addVertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).setUv(minU, minV);
        bufferBuilder.addVertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).setUv(minU, maxV);
        bufferBuilder.addVertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).setUv(maxU, maxV);
        bufferBuilder.addVertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).setUv(maxU, minV);
    }

}





























