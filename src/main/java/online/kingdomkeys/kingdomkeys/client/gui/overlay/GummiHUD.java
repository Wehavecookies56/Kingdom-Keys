package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.HUD.HUDElement;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    // Ticks the boost takes to come back, which is what its bar is filling up over
    private static final int BOOST_COOLDOWN = 5 * 20;

    private static final int PANEL_FILL = 0xB806060E;
    private static final int PANEL_EDGE = 0xFF3A3A52;
    private static final int TRACK_FILL = 0xFF1C1C2C;
    private static final int TRACK_EDGE = 0xFF33334A;

    private static final int LABEL = 0xFFCFD8E8;
    private static final int FAINT = 0xFF8F9BB3;
    private static final int SHIP_NAME = 0xFFFFD257;

    private static final int FUEL = 0xFFF0A52A;
    private static final int FUEL_LOW = 0xFFE24B4A;
    private static final int ENGINE = 0xFF4FC3F7;
    private static final int BOOST_READY = 0xFFE8E8F2;
    private static final int PRESSED = 0xFFFFD257;
    private static final int ON = 0xFF7DDC7D;
    private static final int OFF = 0xFFB35A5A;

    private static final int PAD = 5;
    private static final int LINE = 10;
    private static final int BAR_HEIGHT = 4;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);
        if(minecraft != null && minecraft.options.hideGui){
            return;
        }
        Player player = minecraft.player;
        if (player == null)
            return;

        if (!(player.getVehicle() instanceof GummiShipEntity ship)) {
            //Restore camera if needed
            if (handledCamera && ModConfigs.auto3rdPersonShip) {
                minecraft.options.setCameraType(prevCamera);
                handledCamera = false;
            }
            return;
        }

        if (!handledCamera && ModConfigs.auto3rdPersonShip) {
            // Store and swap camera if needed
            prevCamera = minecraft.options.getCameraType();
            minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            handledCamera = true;
        }

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        draw(ClientUtils.GUMMI_INFO_ELEMENT, screenWidth, screenHeight, () -> drawCoords(ship, ClientUtils.GUMMI_INFO_ELEMENT));

        if (ship.shipStats != null) {
            sizeStats(ship, ClientUtils.GUMMI_READOUT_ELEMENT);
            draw(ClientUtils.GUMMI_READOUT_ELEMENT, screenWidth, screenHeight, () -> drawStats(ship, ClientUtils.GUMMI_READOUT_ELEMENT));
            drawHP(ship, deltaTracker);
        }

        sizeControls(ship, ClientUtils.GUMMI_CONTROLS_ELEMENT);
        draw(ClientUtils.GUMMI_CONTROLS_ELEMENT, screenWidth, screenHeight, () -> drawControls(ship, ClientUtils.GUMMI_CONTROLS_ELEMENT));
    }

    private void draw(HUDElement element, int screenWidth, int screenHeight, Runnable contents) {
        element.applyTransform(guiGraphics, screenWidth, screenHeight);
        contents.run();
        element.endTransform(guiGraphics);
    }

    private void drawCoords(GummiShipEntity ship, HUDElement element) {
        String name = ship.structure.getName();
        String coords = (int) ship.getX() + ", " + (int) ship.getY() + ", " + (int) ship.getZ();
        String facing = Utils.translateToLocal("container.gummi_ship.facing") + ": " + ship.getDirection();

        panel(0, 0, element.width, element.height);

        // Centred within the panel, since this one sits over the middle of the screen by default
        drawCentred(name, element.width, PAD, SHIP_NAME);
        drawCentred(coords, element.width, PAD + LINE, LABEL);
        drawCentred(facing, element.width, PAD + LINE * 2, LABEL);
    }

    private void drawCentred(String text, int width, int y, int colour) {
        drawString(guiGraphics, font, text, (width - font.width(text)) / 2, y, colour);
    }

    /** One line of the readout. A bar row carries a filled fraction under it, a plain one is just label and value. */
    private record StatRow(String label, String value, boolean hasBar, float fraction, int colour) {
        static StatRow bar(String label, String value, float fraction, int colour) {
            return new StatRow(label, value, true, fraction, colour);
        }

        static StatRow plain(String label, String value) {
            return new StatRow(label, value, false, 0F, FAINT);
        }
    }

    private static final int VALUE_GAP = 8;

    private StatRow[] stats(GummiShipEntity ship) {
        GummiShipEntity.ShipStats stats = ship.shipStats;
        List<StatRow> rows = new ArrayList<>();

        if (ModConfigs.SERVER.gummiShipFuelSystem.get()) {
            float left = ship.getMaxFuel() == 0 ? 0F : (float) ship.getFuel() / ship.getMaxFuel();
            rows.add(StatRow.bar(Utils.translateToLocal("container.gummi_ship.fuel"), ship.getFuel() + " / " + ship.getMaxFuel(), left, left <= 0.2F ? FUEL_LOW : FUEL));
        }

        float ceiling = stats.weight() <= 0 ? 0F : stats.getEffectiveSpeed();
        float power = ceiling <= 0F ? 0F : Math.min(Math.abs(ship.currentSpeed) / ceiling, 1F);
        rows.add(StatRow.bar(Utils.translateToLocal("container.gummi_ship.eng_power"), Math.round(power * 100) + "%", power, ENGINE));

        boolean ready = ClientEvents.gummiBoostCD <= 0;
        float charge = ready ? 1F : 1F - (float) ClientEvents.gummiBoostCD / BOOST_COOLDOWN;
        String state = Utils.translateToLocal(ready ? "container.gummi_ship.ready" : "container.gummi_ship.not_ready");
        rows.add(StatRow.bar(Utils.translateToLocal("container.gummi_ship.boost"), state, charge, ready ? BOOST_READY : FUEL));

        float travelled = (float) Math.sqrt(Math.pow(ship.getX() - ship.xOld, 2) + Math.pow(ship.getY() - ship.yOld, 2) + Math.pow(ship.getZ() - ship.zOld, 2));
        rows.add(StatRow.plain(Utils.translateToLocal("container.gummi_ship.speed"), df.format(travelled * 20) + " m/s"));
        rows.add(StatRow.plain(Utils.translateToLocal("container.gummi_ship.armor"), String.valueOf(stats.armour())));
        rows.add(StatRow.plain(Utils.translateToLocal("container.gummi_ship.numofweapons"), String.valueOf(stats.firepower().size())));

        return rows.toArray(new StatRow[0]);
    }

    private void sizeStats(GummiShipEntity ship, HUDElement element) {
        StatRow[] rows = stats(ship);
        int width = 0;
        int bars = 0;

        for (StatRow row : rows) {
            width = Math.max(width, font.width(row.label()) + VALUE_GAP + font.width(row.value()));

            if (row.hasBar()) {
                bars++;
            }
        }

        element.width = width + PAD * 2;
        element.height = PAD * 2 + bars * (LINE + BAR_HEIGHT + 3) + (rows.length - bars) * LINE + 3;
    }

    private void drawStats(GummiShipEntity ship, HUDElement element) {
        StatRow[] rows = stats(ship);
        int width = element.width;

        panel(0, 0, width, element.height);

        int row = PAD;
        boolean dividerDrawn = false;

        for (StatRow entry : rows) {
            if (entry.hasBar()) {
                row = barRow(width, row, entry.label(), entry.value(), entry.fraction(), entry.colour());
                continue;
            }

            if (!dividerDrawn) {
                guiGraphics.fill(PAD, row, width - PAD, row + 1, PANEL_EDGE);
                row += 3;
                dividerDrawn = true;
            }

            row = statRow(width, row, entry.label(), entry.value());
        }
    }

    /** A label on the left, its value on the right, and the bar underneath. Returns where the next row starts. */
    private int barRow(int width, int y, String label, String value, float fraction, int colour) {
        drawString(guiGraphics, font, label, PAD, y, LABEL);
        drawString(guiGraphics, font, value, width - PAD - font.width(value), y, LABEL);
        bar(PAD, y + LINE - 1, width - PAD * 2, fraction, colour);
        return y + LINE + BAR_HEIGHT + 3;
    }

    private int statRow(int width, int y, String label, String value) {
        drawString(guiGraphics, font, label, PAD, y, FAINT);
        drawString(guiGraphics, font, value, width - PAD - font.width(value), y, FAINT);
        return y + LINE;
    }

    private ControlRow[] controls(GummiShipEntity ship) {
        return new ControlRow[]{
                new ControlRow(new String[]{
                        minecraft.options.keyUp.getKey().getDisplayName().getString(),
                        minecraft.options.keyLeft.getKey().getDisplayName().getString(),
                        minecraft.options.keyDown.getKey().getDisplayName().getString(),
                        minecraft.options.keyRight.getKey().getDisplayName().getString()
                }, new boolean[]{ship.inputForward, ship.inputLeft, ship.inputBackward, ship.inputRight}, "container.gummi_ship.movement", false),

                key(minecraft.options.keyJump, ship.inputUp, "container.gummi_ship.up"),
                key(minecraft.options.keySprint, ship.inputDown, "container.gummi_ship.down"),
                new ControlRow(new String[]{InputHandler.Keybinds.ACTION.getKeybind().getKey().getDisplayName().getString()},
                        new boolean[]{InputHandler.Keybinds.ACTION.getKeybind().isDown()}, "container.gummi_ship.boost", false),

                // A setting rather than a key being held, so it says which way it is set instead
                new ControlRow(new String[]{minecraft.options.keyPickItem.getKey().getDisplayName().getString()},
                        new boolean[]{ship.isFlightType3D()}, "container.gummi_ship.3d_flight", true)
        };
    }

    private ControlRow key(KeyMapping mapping, boolean held, String label) {
        return new ControlRow(new String[]{mapping.getKey().getDisplayName().getString()}, new boolean[]{held}, label, false);
    }

    private record ControlRow(String[] keys, boolean[] held, String label, boolean setting) {}

    private void sizeControls(GummiShipEntity ship, HUDElement element) {
        ControlRow[] rows = controls(ship);
        int width = 0;

        for (ControlRow row : rows) {
            width = Math.max(width, font.width(line(row)));
        }

        element.width = width + PAD * 2;
        element.height = rows.length * LINE + PAD;
    }

    private void drawControls(GummiShipEntity ship, HUDElement element) {
        ControlRow[] rows = controls(ship);
        panel(0, 0, element.width, element.height);

        for (int i = 0; i < rows.length; i++) {
            ControlRow row = rows[i];
            int y = PAD + i * LINE;
            int x = PAD;

            for (int k = 0; k < row.keys().length; k++) {
                String chip = "[" + row.keys()[k] + "]";
                int colour = row.setting() ? (row.held()[k] ? ON : OFF) : row.held()[k] ? PRESSED : FAINT;
                drawString(guiGraphics, font, chip, x, y, colour);
                x += font.width(chip + " ");
            }

            drawString(guiGraphics, font, Utils.translateToLocal(row.label()), x, y, FAINT);
        }
    }

    private String line(ControlRow row) {
        StringBuilder built = new StringBuilder();

        for (String key : row.keys()) {
            built.append('[').append(key).append("] ");
        }

        return built.append(Utils.translateToLocal(row.label())).toString();
    }

    private void panel(int x, int y, int width, int height) {
        guiGraphics.fill(x, y, x + width, y + height, PANEL_FILL);
        guiGraphics.fill(x, y, x + width, y + 1, PANEL_EDGE);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, PANEL_EDGE);
        guiGraphics.fill(x, y, x + 1, y + height, PANEL_EDGE);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, PANEL_EDGE);
    }

    private void bar(int x, int y, int width, float fraction, int colour) {
        guiGraphics.fill(x, y, x + width, y + BAR_HEIGHT, TRACK_FILL);
        guiGraphics.fill(x, y, x + width, y + 1, TRACK_EDGE);
        guiGraphics.fill(x, y + BAR_HEIGHT - 1, x + width, y + BAR_HEIGHT, TRACK_EDGE);

        int filled = Math.round(Mth.clamp(fraction, 0F, 1F) * (width - 2));

        if (filled > 0) {
            guiGraphics.fill(x + 1, y + 1, x + 1 + filled, y + BAR_HEIGHT - 1, colour);
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
        RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/gui/hp_gummi_background.png"));
        RenderSystem.setShaderTexture(1, KingdomKeys.rl("textures/gui/hp_gummi_mask.png"));

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
            RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/gui/hp_gummi_fill_h.png"));
        else
            RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/gui/hp_gummi_fill.png"));

        RenderSystem.setShaderTexture(1, KingdomKeys.rl("textures/gui/hp_gummi_mask.png"));

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

            RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/gui/hp_gummi_fill.png"));
            RenderSystem.setShaderTexture(1, KingdomKeys.rl("textures/gui/hp_gummi_mask.png"));

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

        RenderSystem.setShaderTexture(0, KingdomKeys.rl("textures/gui/hp_gummi_outline.png"));
        RenderSystem.setShaderTexture(1, KingdomKeys.rl("textures/gui/hp_gummi_outline_mask.png"));

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
