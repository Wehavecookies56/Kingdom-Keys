package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetOrgPortalName;

public class OrgPortalGui extends Screen {

	EditBox nameBox;
	Button set;

	BlockPos pos;

	public OrgPortalGui(BlockPos pos) {
		super(new TranslatableComponent("Org Portal"));
		this.pos = pos;
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		int tfWidth = minecraft.font.width("####################");
		addWidget(nameBox = new EditBox(minecraft.font, width / 2 - tfWidth / 2, height / 2 - 10, tfWidth, 16, new TranslatableComponent("")));
		addWidget(set = new Button(width / 2 - tfWidth / 2, height / 2 + 10, tfWidth, 20, new TranslatableComponent("Set name"), (e) -> {
			action();
		}));

		if (minecraft.player.level.getBlockEntity(pos) != null && minecraft.player.level.getBlockEntity(pos) instanceof OrgPortalTileEntity) {
			OrgPortalTileEntity te = (OrgPortalTileEntity) minecraft.player.level.getBlockEntity(pos);
			UUID portalUUID = te.getUUID();
			if(portalUUID != null) {
				String text = ModCapabilities.getWorld(minecraft.player.level).getPortalFromUUID(portalUUID).getName();
				nameBox.setValue(text);
			}
		}
		super.init();
	}

	private void action() {
		PacketHandler.sendToServer(new CSSetOrgPortalName(pos, nameBox.getValue()));
		onClose();
	}

	@Override
	public void render(PoseStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {

		super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	}
}