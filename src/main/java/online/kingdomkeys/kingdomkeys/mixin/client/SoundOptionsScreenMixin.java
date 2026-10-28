package online.kingdomkeys.kingdomkeys.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin {

	// Vanilla leaves the last row of small sliders with an empty slot, ours takes it
	@ModifyReturnValue(method = "getAllSoundOptionsExceptMaster", at = @At("RETURN"))
	private OptionInstance<?>[] kkSoundVolume(OptionInstance<?>[] options) {
		OptionInstance<?>[] withKK = Arrays.copyOf(options, options.length + 1);
		withKK[options.length] = new OptionInstance<>("soundCategory." + KingdomKeys.MODID, OptionInstance.noTooltip(), (caption, value) -> value == 0.0 ? Options.genericValueLabel(caption, CommonComponents.OPTION_OFF) : Component.translatable("options.percent_value", caption, (int) (value * 100.0)), OptionInstance.UnitDouble.INSTANCE, ModConfigs.soundVolume, value -> {
			ModConfigs.setSoundVolume(value);
			Minecraft.getInstance().getSoundManager().updateSourceVolume(SoundSource.PLAYERS, Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.PLAYERS));
		});
		return withKK;
	}
}
