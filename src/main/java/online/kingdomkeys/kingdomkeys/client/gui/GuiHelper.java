package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class GuiHelper {
	@OnlyIn(Dist.CLIENT)
	public static void openMenu() {
		Minecraft mc = Minecraft.getInstance();
		mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		mc.displayGuiScreen(new GUIMenuO());
	}
}
