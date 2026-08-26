package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSPlaySoundPacket(double x, double y, double z, ResourceLocation sound, SoundSource source) implements Packet {

	public static final Type<CSPlaySoundPacket> TYPE = new Type<>(KingdomKeys.rl("cs_play_sound"));

	public static final StreamCodec<FriendlyByteBuf, CSPlaySoundPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, CSPlaySoundPacket::x,
			ByteBufCodecs.DOUBLE, CSPlaySoundPacket::y,
			ByteBufCodecs.DOUBLE, CSPlaySoundPacket::z,
			ResourceLocation.STREAM_CODEC, CSPlaySoundPacket::sound,
			NeoForgeStreamCodecs.enumCodec(SoundSource.class), CSPlaySoundPacket::source,
			CSPlaySoundPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(sound);
		if(soundEvent == null)
			return;
		player.level().playSound(null, x,y,z, soundEvent, source);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
