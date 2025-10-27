package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;

public class GummiHUD extends OverlayBase {

	public static final GummiHUD INSTANCE = new GummiHUD();

	private GummiHUD() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		if(player.getVehicle() instanceof GummiShipEntity ship){
			int x = 10, y = 1;
			GummiShipEntity.ShipStats stats = ship.shipStats;
			if(stats != null) {
				drawString(guiGraphics, minecraft.font, "Current speed: " + (int)(ship.currentSpeed*100), x, 10*y++, 0xFFFFFF);
				drawString(guiGraphics, minecraft.font, "Max speed: " + (int)(stats.speed()*100), x, 10*y++, 0xFFFFFF);

				drawString(guiGraphics, minecraft.font, "Armor: " + stats.armour(), x, 10*y++, 0xFFFFFF);
				drawString(guiGraphics, minecraft.font, "# of weapons: " + stats.firepower().size(), x, 10*y++, 0xFFFFFF);
			}

			drawString(guiGraphics, minecraft.font, minecraft.options.keyUp.getKey().getDisplayName().getString()+": FORWARD", x, 10*y++, ship.inputForward ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyDown.getKey().getDisplayName().getString()+": BACKWARDS", x, 10*y++, ship.inputBackward ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyLeft.getKey().getDisplayName().getString()+": LEFT", x, 10*y++, ship.inputLeft ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyRight.getKey().getDisplayName().getString()+": RIGHT", x, 10*y++, ship.inputRight ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyJump.getKey().getDisplayName().getString()+": UP", x, 10*y++, ship.inputUp ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keySprint.getKey().getDisplayName().getString()+": DOWN", x, 10*y++, ship.inputDown ? 0xAA0000 : 0xFFFFFF);


		}
	}
}
