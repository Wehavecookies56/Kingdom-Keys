package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CheckboxButton;
import online.kingdomkeys.kingdomkeys.container.PedestalContainer;
import online.kingdomkeys.kingdomkeys.entity.block.PedestalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPedestalConfig;

public class PedestalScreen extends ContainerScreen<PedestalContainer> {

	private static final String texture = KingdomKeys.MODID+":textures/gui/pedestal.png";

    public PedestalScreen(PedestalContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
		this.ySize = 186;

    }

    final float rotationSpeedMax = 5.0F;
    final float bobSpeedMax = 0.5F;
    final float scaleMax = 2.0F;
    final float heightMax = 2.0F;
    Slider rotationSpeedSlider, bobSpeedSlider, scaleSlider, heightSlider;
    CheckboxButton pauseCheckbox;
    ExtendedButton reset;


    @Override
    protected void init() {
        super.init();
        float current = (rotationSpeedMax / 100.0F) * (container.TE.getRotationSpeed() * (100.0F/(PedestalTileEntity.DEFAULT_ROTATION_SPEED * rotationSpeedMax)));
        addButton(scaleSlider = new Slider(guiLeft + 8, guiTop + 30, 50, 10, "", "", 0.2D, scaleMax, container.TE.getScale(), true, false, h -> {}));
        addButton(heightSlider = new Slider(guiLeft + 8, guiTop + 42, 50, 10, "", "", 0, heightMax, container.TE.getBaseHeight(), true, false, h -> {}));
        addButton(rotationSpeedSlider = new Slider(guiLeft + 8, guiTop + 54, 50, 10, "", "", -rotationSpeedMax, rotationSpeedMax, container.TE.getRotationSpeed(), true, false, h -> { }));
        addButton(bobSpeedSlider = new Slider(guiLeft + 8, guiTop + 66, 50, 10, "", "", 0, bobSpeedMax, container.TE.getBobSpeed(), true, false, h -> {}));
        addButton(pauseCheckbox = new CheckboxButton(guiLeft + 8, guiTop + 18, "Pause", container.TE.isPaused()));
        addButton(reset = new ExtendedButton(guiLeft + xSize - 53, guiTop + 80, 45, 15, "Reset", p -> {
            container.TE.setPause(false);
            container.TE.setCurrentTransforms(PedestalTileEntity.DEFAULT_ROTATION, PedestalTileEntity.DEFAULT_HEIGHT);
            container.TE.setSpeed(PedestalTileEntity.DEFAULT_ROTATION_SPEED, PedestalTileEntity.DEFAULT_BOB_SPEED);
            container.TE.setScale(PedestalTileEntity.DEFAULT_SCALE);
            container.TE.setBaseHeight(PedestalTileEntity.DEFAULT_HEIGHT);
            rotationSpeedSlider.setValue(PedestalTileEntity.DEFAULT_ROTATION_SPEED);
            bobSpeedSlider.setValue(PedestalTileEntity.DEFAULT_BOB_SPEED);
            scaleSlider.setValue(PedestalTileEntity.DEFAULT_SCALE);
            heightSlider.setValue(PedestalTileEntity.DEFAULT_HEIGHT);
            pauseCheckbox.setChecked(false);
            PacketHandler.sendToServer(new CSPedestalConfig(container.TE.getPos(), container.TE.getRotationSpeed(), container.TE.getBobSpeed(), container.TE.getSavedRotation(), container.TE.getSavedHeight(), container.TE.getBaseHeight(), container.TE.getScale(), container.TE.isPaused()));
        }));
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256 || p_keyPressed_1_ == Minecraft.getInstance().gameSettings.keyBindInventory.getKey().getKeyCode()) { //256 = Esc
            PacketHandler.sendToServer(new CSPedestalConfig(container.TE.getPos(), container.TE.getRotationSpeed(), container.TE.getBobSpeed(), container.TE.getSavedRotation(), container.TE.getSavedHeight(), container.TE.getBaseHeight(), container.TE.getScale(), container.TE.isPaused()));
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        container.TE.setSpeed((float) rotationSpeedSlider.getValue(), (float) bobSpeedSlider.getValue());
        container.TE.setPause(pauseCheckbox.isChecked());
        container.TE.setScale((float) scaleSlider.getValue());
        container.TE.setBaseHeight((float) heightSlider.getValue());
        if (pauseCheckbox.isChecked())
            container.TE.saveTransforms(container.TE.getCurrentRotation(), container.TE.getCurrentHeight());
        super.tick();
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString("Scale " + String.format("%.2f", scaleSlider.getValue()) + "x", 60.0F, 31.0F, 4210752);
        this.font.drawString("Height " + String.format("%.2f", heightSlider.getValue()), 60.0F, 43.0F, 4210752);
        this.font.drawString("Rotation Speed " + String.format("%.2f", rotationSpeedSlider.getValue()), 60.0F, 55.0F, 4210752);
        this.font.drawString("Bob Speed " + String.format("%.2f", bobSpeedSlider.getValue()), 60.0F, 67.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(texture));

        int xPos = (width - xSize) / 2;
		int yPos = (height / 2) - (ySize / 2);
		blit(xPos, yPos, 0, 0, xSize, ySize);

    }


    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        rotationSpeedSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        bobSpeedSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        scaleSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        heightSlider.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }
}
