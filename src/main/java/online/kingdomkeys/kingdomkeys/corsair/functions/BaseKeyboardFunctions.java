package online.kingdomkeys.kingdomkeys.corsair.functions;

import java.awt.Color;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;

public class BaseKeyboardFunctions {

	static float maxAir = 300.0F;
	static float maxFood = 20F;
	static float maxAbsorption = 20;

	protected static int getRGB(float value, float max) {
		return MathHelper.hsvToRGB(Math.max(0.0F, Math.min(max, value)) / max / 3.0F, 1.0F, 1.0F);
	}

	protected static int[] decimalToRGB(int decimal) {
		Color color = new Color(decimal);
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		return new int[] { red, green, blue };
	}

	protected static float getAir(EntityPlayerSP player) {
		return player.getAir();
	}

	protected static float getMaxAir() {
		return maxAir;
	}

	protected static float getFood(EntityPlayerSP player) {
		return player.getFoodStats().getFoodLevel();
	}

	protected static float getMaxFood() {
		return maxFood;
	}

	protected static float getHealth(EntityPlayerSP player) {
		return player.getHealth();
	}

	protected static float getMaxHealth(EntityPlayerSP player) {
		return player.getMaxHealth();
	}
	protected static float getAbsorption(EntityPlayerSP player) {
		return player.getAbsorptionAmount();
	}

	protected static float getMaxAbsorption() {
		return maxAbsorption;
	}
}
