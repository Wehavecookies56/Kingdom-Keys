package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetOrgPortalName;

import java.util.UUID;

public class OrgPortalGui extends Screen {

	EditBox nameBox;

	BlockPos pos;

	public OrgPortalGui(BlockPos pos) {
		super(Component.translatable("Org Portal"));
		this.pos = pos;
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {
		int tfWidth = minecraft.font.width("####################");
		addRenderableWidget(nameBox = new EditBox(minecraft.font, width / 2 - tfWidth / 2, height / 2 - 10, tfWidth, 16, Component.translatable("")));

		addRenderableWidget(Button.builder(Component.translatable("Set name"), (e) -> {
			action();
		}).bounds(width / 2 - tfWidth / 2, height / 2 + 10, tfWidth, 20).build());

		if (minecraft.player.level().getBlockEntity(pos) != null && minecraft.player.level().getBlockEntity(pos) instanceof OrgPortalTileEntity) {
			OrgPortalTileEntity te = (OrgPortalTileEntity) minecraft.player.level().getBlockEntity(pos);
			UUID portalUUID = te.getUUID();
			if(portalUUID != null) {
				String text = WorldData.getClient().getPortalFromUUID(portalUUID).getName();
				nameBox.setValue(text);
			}
		}
		super.init();
	}

	private void action() {
		PacketHandler.sendToServer(new CSSetOrgPortalName(pos, nameBox.getValue()));
		onClose();
	}

}