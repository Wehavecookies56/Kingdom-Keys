package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetOrgPortalName;

public class OrgPortalGui extends Screen {

	TextFieldWidget nameBox;
	Button set;
	
	BlockPos pos;

	public OrgPortalGui(BlockPos pos) {
		super(new TranslationTextComponent("Org Portal"));
		this.pos = pos;
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		int tfWidth = minecraft.fontRenderer.getStringWidth("####################");
		addButton(nameBox = new TextFieldWidget(minecraft.fontRenderer, width / 2 - tfWidth / 2, height / 2 - 10, tfWidth, 16, new TranslationTextComponent("")));
		addButton(set = new Button(width / 2 - tfWidth / 2, height / 2 +10, tfWidth, 20, new TranslationTextComponent("Set name"), (e) -> {
			action();
		}));
		
        if(minecraft.player.world.getTileEntity(pos) != null && minecraft.player.world.getTileEntity(pos) instanceof OrgPortalTileEntity) {
        	OrgPortalTileEntity te = (OrgPortalTileEntity) minecraft.player.world.getTileEntity(pos);
        	UUID portalUUID = te.getUUID();
        	Map<UUID, PortalData> portals = ModCapabilities.getWorld(minecraft.player.world).getPortals();
        	String text = ModCapabilities.getWorld(minecraft.player.world).getPortalFromUUID(portalUUID).getName();

        	nameBox.setText(text);
        }

		super.init();
	}

	private void action() {
		PacketHandler.sendToServer(new CSSetOrgPortalName(pos, nameBox.getText()));
		closeScreen();
	}

	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {

		super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	}
}