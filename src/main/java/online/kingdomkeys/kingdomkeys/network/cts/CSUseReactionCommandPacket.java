package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.ReactionCommandCastEvent;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;

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
		String reactionName = Utils.getRCNameFromIndex(player, index);
		ReactionCommand reaction = ModReactionCommands.registry.get(ResourceLocation.parse(reactionName));
        if (NeoForge.EVENT_BUS.post(new ReactionCommandCastEvent(player, ResourceLocation.parse(reactionName))).isCanceled())
            return;
        reaction.onUse(player, player, (LivingEntity) player.level().getEntity(lockedOnEntity));

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}