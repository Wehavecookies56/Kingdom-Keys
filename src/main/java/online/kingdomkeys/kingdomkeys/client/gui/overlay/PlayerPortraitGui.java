package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerPortraitGui extends OverlayBase {
    public static ResourceLocation SKINRL = null;

	public static final PlayerPortraitGui INSTANCE = new PlayerPortraitGui();
    private static final Map<UUID, Vec3i> GUMMI_SIZE_CACHE = new HashMap<>();

    private PlayerPortraitGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
        if(minecraft != null && minecraft.options.hideGui){
            return;
        }
		PlayerData playerData = PlayerData.get(minecraft.player);
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);
        float scale = 0.4f;
        if(minecraft.options.guiScale().get() > 0){
            scale = minecraft.options.guiScale().get() * 0.08F;
        }

		if (playerData != null) {
			if (playerData.isFormActive(ModDriveForms.ANTI)) {
				RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 1F);
			}

			if(Utils.isPlayerLowHP(minecraft.player)) {
				RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1F);
			}

			PoseStack poseStack = guiGraphics.pose();

			poseStack.pushPose();
            {
                poseStack.pushPose();
                {
                    ClientUtils.PORTRAIT_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
                    RenderSystem.enableBlend();
                    float scaleX = 0.18F, scaleY = 0.18F;
                    poseStack.scale(scaleX, scaleY, 0);
                    ResourceLocation circle = KingdomKeys.rl("textures/gui/portrait.png");
                    blit(guiGraphics, circle, -39, -39, 0, 0, 256, 256);
                    RenderSystem.disableBlend();
                    ClientUtils.PORTRAIT_ELEMENT.endTransform(guiGraphics);
                }
                poseStack.popPose();
                if(minecraft.player == null)
                    return;

                Player player = Minecraft.getInstance().player;

                //3D render
				float playerHeight = 50;
                ClientUtils.PORTRAIT_ELEMENT.applyTransform(guiGraphics,screenWidth, screenHeight);
                int playerPosX = 16;
                int playerPosY = 94;

				poseStack.pushPose();
				{
					ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
					player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.AIR));

                    if(player.getVehicle() instanceof GummiShipEntity) {
                        renderShip(poseStack, scale);
                    } else {
                        if(ModConfigs.portrait3D) {
                            ClientUtils.renderEntity(poseStack, playerPosX, playerPosY, (int) playerHeight, 0,0, player);
                        } else {
                            render2D();
                        }
                    }
					player.getInventory().setItem(player.getInventory().selected, stack);
				}
				poseStack.popPose();
                ClientUtils.PORTRAIT_ELEMENT.endTransform(guiGraphics);

            }
			poseStack.popPose();
		}
	}

    private void renderShip(PoseStack poseStack, float scale) {
        poseStack.pushPose();
        {
            Player player = Minecraft.getInstance().player;

            int sizeX = 1, sizeY = 1;
            if (player.getVehicle() instanceof GummiShipEntity ship) {
                Vec3i size = getCachedGummiSize(ship);
                sizeX = size.getX();
                sizeY = size.getY();
                if (ship.structure != null) {
                    scale = 11f / Math.max(sizeX, sizeY);
                }
            }

            float baseX = -9;
            float baseY = 31;

            if(sizeX % 2 == 0){
                baseX -= 2;
            }
            float offsetX = baseX - (sizeX * scale) / 2f;
            int missingY = 13 - sizeY;
            float offsetY = baseY - missingY;
            scale *= 1.4F;

            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(scale, scale, scale);

            ClientUtils.renderEntity(poseStack, 0, 0, 2, 0,0, player.getVehicle());
        }
        poseStack.popPose();
    }

    private void render2D() {
        if(SKINRL == null || minecraft.player.tickCount < 100) { //For the first 5 seconds cache it instantly
            SKINRL = minecraft.player.getSkin().texture();
        } else if(minecraft.player.tickCount % 100 == 0) { //Every 5s cache it once
            SKINRL = minecraft.player.getSkin().texture();
        }
        RenderSystem.setShaderTexture(0, SKINRL);

        // HEAD
        int headWidth = 32;
        int headHeight = 32;

        this.blit(guiGraphics, SKINRL, 0, 0, 32, 32, headWidth, headHeight);

        // HAT
        if(minecraft.options.isModelPartEnabled(PlayerModelPart.HAT)){
            this.blit(guiGraphics, SKINRL, 0, 0, 160, 32, headWidth, headHeight);
        }
    }

    // Individual layer so it renders above HP gui
    public static final LayeredDraw.Layer CROWN_OVERLAY = (guiGraphics, deltaTracker) -> INSTANCE.renderCrownOverlay(guiGraphics);

    private void renderCrownOverlay(GuiGraphics guiGraphics) {
        if(minecraft != null && minecraft.options.hideGui){
            return;
        }
        if (ModConfigs.portrait3D || minecraft.player == null || minecraft.player.getVehicle() instanceof GummiShipEntity) {
            RenderSystem.setShaderColor(1, 1, 1, 1);
            return;
        }

        PlayerData playerData = PlayerData.get(minecraft.player);
        if (playerData == null) {
            return;
        }

        // Same tinting the portrait itself uses, so the crown keeps matching the face.
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (playerData.isFormActive(ModDriveForms.ANTI)) {
            RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 1F);
        }
        if (Utils.isPlayerLowHP(minecraft.player)) {
            RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1F);
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        {
            ClientUtils.PORTRAIT_ELEMENT.applyTransform(guiGraphics, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
            renderCrown(guiGraphics, poseStack);
            ClientUtils.PORTRAIT_ELEMENT.endTransform(guiGraphics);
        }
        poseStack.popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static final int CROWN_X = 8;
    private static final int CROWN_Y = -6;
    private static final int CROWN_WIDTH = 16;
    private static final int CROWN_HEIGHT = 12;

    private static final float CROWN_OFFSET_TO_PIXELS = 2F;

    private void renderCrown(GuiGraphics guiGraphics, PoseStack poseStack) {
        PlayerData playerData = PlayerData.get(minecraft.player);
        if (playerData == null) {
            return;
        }

        String crown = playerData.getCrown();
        if (crown == null || crown.isEmpty()) {
            return;
        }

        ResourceLocation texture = KingdomKeys.rl("textures/models/crown/" + crown + ".png");

        float x = CROWN_X + playerData.getCrownOffsetX() * CROWN_OFFSET_TO_PIXELS;
        float y = CROWN_Y + playerData.getCrownOffsetY() * CROWN_OFFSET_TO_PIXELS;

        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        poseStack.pushPose();
        {
            poseStack.translate(x + CROWN_WIDTH / 2F, y + CROWN_HEIGHT, 0);
            poseStack.mulPose(new Quaternionf().rotationZYX(Mth.DEG_TO_RAD * playerData.getCrownRotationZ(), Mth.DEG_TO_RAD * -playerData.getCrownRotationY(), Mth.DEG_TO_RAD * -playerData.getCrownRotationX()));

            poseStack.translate(-CROWN_WIDTH / 2F, -CROWN_HEIGHT, 0);
            guiGraphics.blit(texture, 0, 0, CROWN_WIDTH, CROWN_HEIGHT, 1, 1, 8, 6, 32, 32);
        }
        poseStack.popPose();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static Vec3i getCachedGummiSize(GummiShipEntity ship) {
        if (ship == null || ship.structure == null)
            return new Vec3i(1,1,1);

        return GUMMI_SIZE_CACHE.computeIfAbsent(ship.getUUID(), id ->
                Utils.getRealGummiStructureSize(ship.structure)
        );
    }
}
