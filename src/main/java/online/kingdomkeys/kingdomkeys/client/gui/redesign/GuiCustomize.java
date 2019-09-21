package uk.co.wehavecookies56.kk.client.gui.redesign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import uk.co.wehavecookies56.kk.client.core.helper.GuiHelper;
import uk.co.wehavecookies56.kk.client.gui.GuiMenu_Bars;
import uk.co.wehavecookies56.kk.common.capability.DriveStateCapability.IDriveState;
import uk.co.wehavecookies56.kk.common.capability.MagicStateCapability.IMagicState;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.capability.PlayerStatsCapability;
import uk.co.wehavecookies56.kk.common.item.base.ItemDriveForm;
import uk.co.wehavecookies56.kk.common.item.base.ItemSpellOrb;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.server.SwapMagic;
import uk.co.wehavecookies56.kk.common.util.Utils;

public class GuiCustomize extends GuiScreen {

	GuiMenu_Bars background;
	public static List<String> driveForms;
	EntityPlayer player = Minecraft.getMinecraft().player;
	IDriveState DS = player.getCapability(ModCapabilities.DRIVE_STATE, null);
	IMagicState MS = player.getCapability(ModCapabilities.MAGIC_STATE, null);
	PlayerStatsCapability.IPlayerStats STATS = player.getCapability(ModCapabilities.PLAYER_STATS, null);

	public GuiCustomize() {
		this.background = new GuiMenu_Bars("Customize");
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		background.drawBars();
		background.drawBiomeDim();
		background.drawMunnyTime();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		background.width = width;
		background.height = height;
		background.init();

		//this.spells = new ArrayList<String>();
		this.driveForms = new ArrayList<String>();

		int mID = 0;
		//this.spells.clear();
		for (int i = 0; i < player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getSlots(); i++)
			if (!ItemStack.areItemStacksEqual(Minecraft.getMinecraft().player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i), ItemStack.EMPTY)) {
				String magicName = ((ItemSpellOrb) Minecraft.getMinecraft().player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i).getItem()).getMagicName();
				System.out.println(magicName);
				buttonList.add(new GuiMenuButton(mID++, 0, mID * 23 / 3, 100, Utils.translateToLocal(magicName)));
				buttonList.add(new GuiMenuButtonMove(mID++, 125, mID * 23 / 3 - 8, "up"));
				buttonList.add(new GuiMenuButtonMove(mID++, 125, mID * 23 / 3 - 6, "down"));
				System.out.println(((ItemSpellOrb) Minecraft.getMinecraft().player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i).getItem()).getMagicName());
			}

		this.driveForms.clear();
		for (int i = 0; i < player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getSlots(); i++)
			if (!ItemStack.areItemStacksEqual(Minecraft.getMinecraft().player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getStackInSlot(i), ItemStack.EMPTY))
				this.driveForms.add(((ItemDriveForm) Minecraft.getMinecraft().player.getCapability(ModCapabilities.DRIVE_STATE, null).getInventoryDriveForms().getStackInSlot(i).getItem()).getDriveFormName());


		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id % 3) {
		case 0:
			// Magic Button
			//System.out.println(spells.get(button.id / 3));
			break;
		case 1:
			// move up
			// System.out.println("Moving
			// "+Utils.translateToLocal(spells.get(button.id/3))+" above");
			PacketDispatcher.sendToServer(new SwapMagic(button.id / 3, "up"));
			GuiHelper.openCustomize();
			break;
		case 2:
			// move down
			// System.out.println("Moving
			// "+Utils.translateToLocal(spells.get(button.id/3))+" below");
			PacketDispatcher.sendToServer(new SwapMagic(button.id / 3, "down"));
			GuiHelper.openCustomize();
			break;
		}
		super.actionPerformed(button);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
