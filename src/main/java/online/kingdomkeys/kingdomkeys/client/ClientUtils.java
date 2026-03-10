package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.CMElement;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.HPElement;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.LockOnElement;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.PartyElement;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUDElement;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.*;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ClientUtils {
    //Order is important for overlapping boxes, top to bottom
    public static final HUDElement DRIVE_ELEMENT = new HUDElement("Drive").setScale(0.8F,0.8F);
    public static final HUDElement MP_ELEMENT = new HUDElement("MP").setScale(0.7F, 0.5F);
    public static final HUDElement FOCUS_ELEMENT = new HUDElement("Focus");
    public static final HPElement HP_ELEMENT = new HPElement("HP").setScale(0.2F,0.2F);
    public static final CMElement CM_ELEMENT = new CMElement("CM");
    public static final LockOnElement LOCKON_ELEMENT = new LockOnElement("LockOn");
    public static final PartyElement PARTY_ELEMENT = new PartyElement("Party");
    public static final HUDElement PORTRAIT_ELEMENT = new HUDElement("Portrait");

    public static List<HUDElement> HUD_ELEMENTS = List.of(DRIVE_ELEMENT, MP_ELEMENT, PORTRAIT_ELEMENT, FOCUS_ELEMENT, HP_ELEMENT, CM_ELEMENT, LOCKON_ELEMENT, PARTY_ELEMENT);

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
            Minecraft.getInstance().getResourceManager().getResourceOrThrow(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, path));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static ResourceLocation getResourceExistsOrDefault(String path, String name, String defaultName){
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, String.format(path, getResourceExists(String.format(path, name)) ? name : defaultName));
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

		public void animate() {
            if(model != null) {
                if(increasing) { //animnation increase
                    actVal += 2;
                    if(actVal >= maxVal) {
                        increasing = false;
                    }
                } else { //Animation decrease
                    actVal -= 2;
                    if(actVal <= minVal) {
                        increasing = true;
                    }
                }
                switch(angle) {
                case X:
                    model.xRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.xRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                case Y:
                    model.yRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.yRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                case Z:
                    model.zRot = (float) Math.toRadians(actVal);
                    if(modelCounterpart != null) {
                        modelCounterpart.zRot = (float) Math.toRadians(defVal*2-actVal);
                    }
                	break;
                }
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
        if(entity instanceof LivingEntity livingEntity)
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
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/lockon_0.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType LOCK_ON_INNER = RenderType.create(KingdomKeys.MODID + ":lock_on_inner", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/lockon_1.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID + ":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/shotlock_indicator.png"),
                            false, false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).setWriteMaskState(RenderStateShard.COLOR_WRITE).setLightmapState(RenderStateShard.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY).createCompositeState(true));

    public static final RenderType ULTIMATE_SHOTLOCK_INDICATOR = RenderType.create(KingdomKeys.MODID+":shotlock_indicator", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"textures/gui/ultimate_shotlock_indicator.png"),
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

            float size = 1.5F + shotlock.getCooldown() * 0.2F - ClientEvents.focusingAnEntityTicks * 0.2F;
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

    // Airsteps
    public static void drawShotlockIndicator(BlockPos pos, PoseStack poseStack, MultiBufferSource buffer, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

        float x = (float) (pos.getX() + 0.5 - camPos.x);
        float y = (float) (pos.getY() + 0.5 - camPos.y);
        float z = (float) (pos.getZ() + 0.5 - camPos.z);

        Matrix4f mvMatrix = getMVMatrix(poseStack, x, y, z, 0.5F, 0.5F, 0.5F, true, partialTicks);

        mvMatrix.rotate(mc.getEntityRenderDispatcher().cameraOrientation());

        drawTexturedModalRect2DPlane(mvMatrix, buffer.getBuffer(SHOTLOCK_INDICATOR), -0.6f, -0.6f, 0.6f, 0.6f, 0, 0, 256, 256);
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
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/heart")));
            matrixStackIn.scale(0.005F, 0.005F, 0.005F);
            matrixStackIn.translate(0, 300, 0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(entitylivingbaseIn.tickCount*5));

            for (BakedQuad quad : model.getQuads(null, null, RandomSource.create(), ModelData.EMPTY, RenderType.cutout())) {
                buffer.putBulkData(matrixStackIn.last(), quad, 1, 1, 1, 1, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
            }
        }
        matrixStackIn.popPose();
    }

    /**
     * Copied from {@link net.minecraft.client.renderer.block.BlockRenderDispatcher#renderSingleBlock(BlockState, PoseStack, MultiBufferSource, int, int, ModelData, RenderType)} modified to use alpha
     */
    public static void renderSingleBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ModelData modelData, RenderType renderType, float alpha) {
        RenderShape rendershape = state.getRenderShape();
        if (rendershape != RenderShape.INVISIBLE) {
            switch (rendershape) {
                case MODEL:
                    BakedModel bakedmodel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
                    int i = Minecraft.getInstance().getBlockRenderer().blockColors.getColor(state, (BlockAndTintGetter)null, (BlockPos)null, 0);
                    float r = (float)(i >> 16 & 255) / 255.0F;
                    float g = (float)(i >> 8 & 255) / 255.0F;
                    float b = (float)(i & 255) / 255.0F;

                    for (RenderType rt : bakedmodel.getRenderTypes(state, RandomSource.create(42L), modelData)) {
                        renderModel(poseStack.last(), bufferSource.getBuffer(renderType != null ? renderType : RenderTypeHelper.getEntityRenderType(rt, false)), state, bakedmodel, r, g, b, alpha, packedLight, packedOverlay, modelData, rt);
                    }

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
        int var16 = var15.length;

        for(int var17 = 0; var17 < var16; ++var17) {
            Direction direction = var15[var17];
            randomsource.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, randomsource, modelData, renderType), packedLight, packedOverlay);
        }

        randomsource.setSeed(42L);
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
        for(Iterator var8 = quads.iterator(); var8.hasNext(); consumer.putBulkData(pose, bakedquad, f, f1, f2, alpha, packedLight, packedOverlay)) {
            bakedquad = (BakedQuad)var8.next();
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
}





























