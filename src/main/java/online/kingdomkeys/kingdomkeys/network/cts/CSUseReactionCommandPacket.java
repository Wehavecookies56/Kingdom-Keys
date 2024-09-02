package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public record CSUseReactionCommandPacket(int index, int lockedOnEntity) implements Packet {

	public static final Type<CSUseReactionCommandPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_reaction_command"));

	public static final StreamCodec<FriendlyByteBuf, CSUseReactionCommandPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSUseReactionCommandPacket::index,
			ByteBufCodecs.INT,
			CSUseReactionCommandPacket::lockedOnEntity,
			CSUseReactionCommandPacket::new
	);

	public CSUseReactionCommandPacket(int index, LivingEntity lockedOnEntity) {
		this(index, lockedOnEntity == null ? -1 : lockedOnEntity.getId());
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		String reactionName = playerData.getReactionCommands().get(index);
		ReactionCommand reaction = ModReactionCommands.registry.get(ResourceLocation.parse(reactionName));
		reaction.onUse(player, player, (LivingEntity) player.level().getEntity(lockedOnEntity));

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}