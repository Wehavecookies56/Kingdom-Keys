package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record SCShowWarning(Component body, SoundEvent soundEvent) implements Packet {
	public static final Type<SCShowWarning> TYPE = new Type<>(KingdomKeys.rl("sc_show_warning"));

	public static final StreamCodec<RegistryFriendlyByteBuf, SCShowWarning> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC, SCShowWarning::body,
			ByteBufCodecs.registry(BuiltInRegistries.SOUND_EVENT.key()), SCShowWarning::soundEvent,
			SCShowWarning::new
	);

	public static void send(Player player, Component body, SoundEvent soundEvent) {
		if (player instanceof ServerPlayer serverPlayer) {
			PacketHandler.sendTo(new SCShowWarning(body, soundEvent), serverPlayer);
		}
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.showWarning(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
