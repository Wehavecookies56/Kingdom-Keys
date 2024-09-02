package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSPartyAddMember(String name, UUID memberUUID, String memberName) implements Packet {

	public static final Type<CSPartyAddMember> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_party_add_member"));

	public static final StreamCodec<FriendlyByteBuf, CSPartyAddMember> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSPartyAddMember::name,
			UUIDUtil.STREAM_CODEC,
			CSPartyAddMember::memberUUID,
			ByteBufCodecs.STRING_UTF8,
			CSPartyAddMember::memberName,
			CSPartyAddMember::new
	);

	public CSPartyAddMember(Party party, Player member) {
		this(party.getName(), member.getUUID(), member.getDisplayName().getString());
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		for(Party p : worldData.getParties()) {
			if(p.getName().equals(name))
				p.addMember(memberUUID, memberName);
			Player target = player.level().getPlayerByUUID(memberUUID);
			PlayerData.get(target).removePartiesInvited(name);
			PacketHandler.sendTo(new SCSyncPlayerData(target), (ServerPlayer)target);
		}
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
