package online.kingdomkeys.kingdomkeys.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import javax.annotation.Nullable;
import java.util.Map;


// Worlds music system, which allows for fading in and out.
@OnlyIn(Dist.CLIENT)
public final class DimensionMusic {
	private DimensionMusic() {}

	//Field and nullable battle sound
	public record Theme(Holder<SoundEvent> calm, @Nullable Holder<SoundEvent> battle) {}

	private static final Map<ResourceKey<Level>, Theme> THEMES = Map.of(
			ModDimensions.WORLDMAP, new Theme(ModSounds.Music_World_Map, null),
			ModDimensions.DAYBREAK_TOWN, new Theme(ModSounds.Music_Daybreak_Town, ModSounds.Music_Daybreak_Town_Battle),
			ModDimensions.DESTINY_ISLANDS, new Theme(ModSounds.Music_Destiny_Islands, ModSounds.Music_Destiny_Islands_Battle)
	);

	// Ticks the battle music will keep playing after battle ends, in case you latch onto another battle right after so it doesn't cut.
	private static final int COMBAT_GRACE = 30;


	private static final int FADE_TICKS = 40;

	@Nullable
	private static FadingMusicInstance playing;
	@Nullable
	private static Holder<SoundEvent> playingTrack;
	private static int combatTicks;

	public static boolean hasMusic(ResourceKey<Level> dimension) {
		return THEMES.containsKey(dimension);
	}

	public static void tick(Minecraft mc) {
		Theme theme = mc.level == null ? null : THEMES.get(mc.level.dimension());

		if (theme == null) {
			end();
			return;
		}

		// The battle theme lasts as long as the threat plus the grace
		if (EntityEvents.threatLevel != EntityEvents.ThreatLevel.NONE) {
			combatTicks = COMBAT_GRACE;
		} else if (combatTicks > 0) {
			combatTicks--;
		}

		Holder<SoundEvent> wanted = combatTicks > 0 && theme.battle() != null ? theme.battle() : theme.calm();

		if (wanted == playingTrack && playing != null && !playing.isStopped()) {
			return;
		}

		// If it shouldn't still be playing
		if (playing != null && wanted != playingTrack) {
			playing.fadeOut();
		}

		playing = new FadingMusicInstance(wanted.value(), FADE_TICKS);
		playingTrack = wanted;
		mc.getSoundManager().play(playing);
	}

	private static void end() {
		if (playing != null) {
			playing.fadeOut();
			playing = null;
			playingTrack = null;
		}

		combatTicks = 0;
	}
}
