package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//TODO cleanup + comments
public class PlayerPortraitGui extends OverlayBase {

	public static final PlayerPortraitGui INSTANCE = new PlayerPortraitGui();
    private static final Map<UUID, Vec3i> GUMMI_SIZE_CACHE = new HashMap<>();

    private boolean render3D = false;

    private PlayerPortraitGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		PlayerData playerData = PlayerData.get(minecraft.player);
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);
        float scale = 0.4f;
        if(minecraft.options.guiScale().get() > 0){
            scale = minecraft.options.guiScale().get() * 0.08F;
        }

		if (playerData != null) {
			if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
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
                    RenderSystem.enableBlend();
                    float scaleX = 0.13F, scaleY = 0.13F;
                    int texSize = 131;
                    poseStack.translate(screenWidth-38.8, screenHeight-29, 0);
                    poseStack.translate(-texSize * scaleX, -texSize * scaleY, 0);
                    poseStack.scale(scaleX, scaleY, 0);
                    ResourceLocation circle = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_background_circle.png");
                    blit(guiGraphics, circle, 0, 0, 0, 0, 256, 256);
                    RenderSystem.disableBlend();
                }
                poseStack.popPose();
                if(minecraft.player == null)
                    return;

                Player player = Minecraft.getInstance().player;

                //3D render
				float playerHeight = 50;
                poseStack.translate(screenWidth-0.4, screenHeight, 0);
                poseStack.translate(scale, scale, 0);

                float playerPosX = -39;
				float playerPosY = 53;

				poseStack.pushPose();
				{

					ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
					player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.AIR));

                    if(player.getVehicle() == null) {
                        render3D = false;
                        if(render3D) {
                            ClientUtils.renderEntity(poseStack, (int) playerPosX + ModConfigs.playerSkinXPos, (int) playerPosY+ ModConfigs.playerSkinYPos, (int) playerHeight, 0,0, player);
                        } else {
                            render2D(poseStack);
                        }
                    } else {
                        poseStack.pushPose();
                        {
                            int sizeX = 1, sizeY = 1;
                            if (player.getVehicle() instanceof GummiShipEntity ship) {
                                Vec3i size = getCachedGummiSize(ship);
                                sizeX = size.getX();
                                sizeY = size.getY();
                                if (ship.structure != null) {
                                    scale = 11f / Math.max(sizeX, sizeY);
                                }
                            }

                            float baseX = 46;
                            float baseY = -15;

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
					player.getInventory().setItem(player.getInventory().selected, stack);
				}
				poseStack.popPose();
			}
			poseStack.popPose();
		}
	}

    private void render2D(PoseStack poseStack) {
        ResourceLocation skin = minecraft.player.getSkin().texture();
        RenderSystem.setShaderTexture(0, skin);

        poseStack.translate(-50.5F + ModConfigs.playerSkinXPos, -41 + ModConfigs.playerSkinYPos, 0);
        float scale = 0.7F;
        // HEAD
        int headWidth = 32;
        int headHeight = 32;
        poseStack.scale(scale, scale, scale);

        this.blit(guiGraphics,skin, 0, 0, 32, 32, headWidth, headHeight);

        // HAT
        if(minecraft.options.isModelPartEnabled(PlayerModelPart.HAT)){
            this.blit(guiGraphics, skin, 0, 0, 160, 32, headWidth, headHeight);
        }
    }

    public static Vec3i getCachedGummiSize(GummiShipEntity ship) {
        if (ship == null || ship.structure == null)
            return new Vec3i(1,1,1);

        return GUMMI_SIZE_CACHE.computeIfAbsent(ship.getUUID(), id ->
                Utils.getRealGummiStructureSize(ship.structure)
        );
    }
}
