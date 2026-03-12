package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

import java.text.DecimalFormat;

public class GummiHUD extends OverlayBase {

	public static boolean handledCamera = false;
	public static CameraType prevCamera = CameraType.FIRST_PERSON;
	public static final GummiHUD INSTANCE = new GummiHUD();

	private GummiHUD() {
		super();
	}

	private static final DecimalFormat df = new DecimalFormat("0.00");

    private float displayedGummiHP, realGummiHP;

    private long gummiDelayEnd = 0;

    int barWidth = 908;
    int barHeight = 244;
    int barX = 0;
    int barY = 0;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);
        Player player = minecraft.player;
        if (player == null)
            return;

        if(player.getVehicle() instanceof GummiShipEntity ship){
            if(!handledCamera && ModConfigs.auto3rdPersonShip){
                // Store and swap camera if needed
                prevCamera = minecraft.options.getCameraType();
                minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                handledCamera = true;
            }
            int screenWidth = minecraft.getWindow().getGuiScaledWidth() - 10;

            int x = screenWidth, y = 1;
            GummiShipEntity.ShipStats stats = ship.shipStats;
            if(stats != null) {
                float deltaX = (float) (ship.getX() - ship.xOld);
                float deltaY = (float) (ship.getY() - ship.yOld);
                float deltaZ = (float) (ship.getZ() - ship.zOld);
                float speed = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

                String text = "";
                if(ModConfigs.SERVER.gummiShipFuelSystem.get()) {
                    text = Utils.translateToLocal("container.gummi_ship.fuel") + ": " + ship.getFuel() + " / " + ship.getMaxFuel();
                    drawString(guiGraphics, minecraft.font, text, x - font.width(text), 10 * y++, 0xFFFFFF);
                }
                text = Utils.translateToLocal("container.gummi_ship.speed")+": "+ df.format(speed * 20)+"m/s";
                drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
                text = Utils.translateToLocal("container.gummi_ship.eng_power")+": "+(int) Math.abs(ship.currentSpeed * 100)+" / "+ (int) (stats.speed() * 100);
                drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
                text = Utils.translateToLocal("container.gummi_ship.armor")+": " + stats.armour();
                drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
                text = Utils.translateToLocal("container.gummi_ship.numofweapons")+": " + stats.firepower().size();
                drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);

                //if radar is present
                x = screenWidth/2;
                y = 1;
                text = ship.structure.getName();
                drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xAA0000);
                text = Utils.translateToLocal("container.gummi_ship.coords")+": " + (int) ship.getX()+", "+(int) ship.getY()+", "+(int) ship.getZ();
                drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xFFFFFF);
                text = Utils.translateToLocal("container.gummi_ship.facing")+": " + ship.getDirection();
                drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xFFFFFF);

                drawHP(ship, deltaTracker);
            }

            x = 10;
            y = 1;

            drawString(guiGraphics, minecraft.font, minecraft.options.keyUp.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.forward"), x, 10*y++, ship.inputForward ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, minecraft.options.keyDown.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.backwards"), x, 10*y++, ship.inputBackward ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, minecraft.options.keyLeft.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.left"), x, 10*y++, ship.inputLeft ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, minecraft.options.keyRight.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.right"), x, 10*y++, ship.inputRight ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, minecraft.options.keyJump.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.up"), x, 10*y++, ship.inputUp ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, minecraft.options.keySprint.getKey().getDisplayName().getString()+": "+Utils.translateToLocal("container.gummi_ship.down"), x, 10*y++, ship.inputDown ? 0xAA0000 : 0xFFFFFF);
            drawString(guiGraphics, minecraft.font, InputHandler.Keybinds.ACTION.getKeybind().getKey().getDisplayName().getString() +": "+Utils.translateToLocal("container.gummi_ship.boost")+" ["+ (ClientEvents.gummiBoostCD == 0 ? Utils.translateToLocal("container.gummi_ship.ready") :  Utils.translateToLocal("container.gummi_ship.not_ready"))+"]", x, 10*y++, InputHandler.Keybinds.ACTION.getKeybind().isDown() || ClientEvents.gummiBoostCD > 0 ? 0xAA0000 : 0xFFFFFF);
        } else {
            //Restore camera if needed
            if(handledCamera && ModConfigs.auto3rdPersonShip){
                minecraft.options.setCameraType(prevCamera);
                handledCamera = false;
            }
        }

    }

    private void drawHP(GummiShipEntity ship, DeltaTracker deltaTracker) {
        PoseStack poseStack = guiGraphics.pose();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        float maxHealth = ship.shipStats.armour();
        float health = maxHealth - ship.getDamage();

        //Based on the max hp per ship tier
        float maxMaxHealth = 2000 * ship.getShipLevel(); //maybe config value or something?

        float healthPercentage = health / maxMaxHealth;
        float maxHealthPercentage = maxHealth / maxMaxHealth;

        /*
        float minExtra = 0.01F;
        float maxExtra = 0.004F;
        float exponent = 2.5F;

        float extra = minExtra + (maxExtra - minExtra) * (1 - (float) Math.pow(healthPercentage, exponent));
        float maxHealthPercentageOutline = maxHealthPercentage + extra;
         */

        if (realGummiHP == 0) {
            realGummiHP = health;
            displayedGummiHP = health;
        }

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        long now = net.minecraft.Util.getMillis();

        if (health < realGummiHP) {
            gummiDelayEnd = now + 1000;
        }
        realGummiHP = health;

        if (now > gummiDelayEnd) {
            displayedGummiHP = Mth.lerp(0.05F * partialTick, displayedGummiHP, realGummiHP);
        }

        float displayedPercentage = displayedGummiHP / maxMaxHealth;

        ClientUtils.HP_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        //drawHPOutline(poseStack, maxHealthPercentageOutline);
        drawHPBackground(poseStack, maxHealthPercentage);
        drawHPBar(poseStack, healthPercentage);
        drawRedHP(poseStack, healthPercentage, displayedPercentage);

        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        ClientUtils.HP_ELEMENT.endTransform(guiGraphics);
    }

    private void drawHPBackground(PoseStack poseStack, float maxHealthPercentage) {
        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_background.png"));
        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_mask.png"));

        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);
        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(maxHealthPercentage);
        ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 1F, 1F, 1F);

        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

        poseStack.translate(0.5F, 6F, 0);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void drawHPBar(PoseStack poseStack, float healthPercentage) {
        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        if (minecraft.player.level().getLevelData().isHardcore())
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_fill_h.png"));
        else
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_fill.png"));

        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_mask.png"));

        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);

        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(healthPercentage);
        ClientSetup.gummiHPShader.safeGetUniform("RedStart").set(0f);
        ClientSetup.gummiHPShader.safeGetUniform("RedEnd").set(0f);

        ClientSetup.gummiHPShader.safeGetUniform("Colour").set(0F, 0.5F, 0F, 1F);

        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }

    private void drawRedHP(PoseStack poseStack, float healthPercentage, float displayedPercentage) {
        float damagedPercentage = displayedPercentage - healthPercentage;

        if (damagedPercentage < 0)
            damagedPercentage = 0;

        if (damagedPercentage > 0.001F) {

            float redEnd = displayedPercentage;

            Matrix4f matrix = poseStack.last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_fill.png"));
            RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_mask.png"));

            ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
            ClientSetup.gummiHPShader.setSampler("Sampler1", 1);

            ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(0F);
            ClientSetup.gummiHPShader.safeGetUniform("RedStart").set(healthPercentage);
            ClientSetup.gummiHPShader.safeGetUniform("RedEnd").set(redEnd);

            ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 0F, 0F, 1F);

            ClientSetup.gummiHPShader.apply();
            RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

            buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
            buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
            buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
            buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

            BufferUploader.drawWithShader(buffer.buildOrThrow());
        }
    }

    private void drawHPOutline(PoseStack poseStack, float maxHealthPercentage) {

        poseStack.pushPose();

        int barWidth = 916;
        int barHeight = 254;

        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_outline.png"));
        RenderSystem.setShaderTexture(1, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hp_gummi_outline_mask.png"));

        ClientSetup.gummiHPShader.setSampler("Sampler0", 0);
        ClientSetup.gummiHPShader.setSampler("Sampler1", 1);

        float outlinePercentage = maxHealthPercentage;

        if (maxHealthPercentage <= 0.45F || maxHealthPercentage >= 0.67F)
            outlinePercentage += 0.005F;

        ClientSetup.gummiHPShader.safeGetUniform("HealthPercentage").set(outlinePercentage);
        ClientSetup.gummiHPShader.safeGetUniform("Colour").set(1F, 1F, 1F, 1F);

        ClientSetup.gummiHPShader.apply();
        RenderSystem.setShader(() -> ClientSetup.gummiHPShader);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // poseStack.translate(-6F, -9.8F, 0);

        buffer.addVertex(matrix, barX, barY + barHeight, 0).setUv(0, 1);
        buffer.addVertex(matrix, barX + barWidth, barY + barHeight, 0).setUv(1, 1);
        buffer.addVertex(matrix, barX + barWidth, barY, 0).setUv(1, 0);
        buffer.addVertex(matrix, barX, barY, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        poseStack.popPose();
    }
}
