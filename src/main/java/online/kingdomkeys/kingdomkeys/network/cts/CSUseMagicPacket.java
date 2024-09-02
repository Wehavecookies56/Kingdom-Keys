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
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUseMagicPacket(String name, int level, String allyTarget, int lockedTarget) implements Packet {

	public static final Type<CSUseMagicPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_magic"));

	public static final StreamCodec<FriendlyByteBuf, CSUseMagicPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSUseMagicPacket::name,
			ByteBufCodecs.INT,
			CSUseMagicPacket::level,
			ByteBufCodecs.STRING_UTF8,
			CSUseMagicPacket::allyTarget,
			ByteBufCodecs.INT,
			CSUseMagicPacket::lockedTarget,
			CSUseMagicPacket::new
	);

	public CSUseMagicPacket(String name, int level, LivingEntity lockedTarget) {
		this(name, level, "", lockedTarget == null ? -1 : lockedTarget.getId());
	}
	
	public CSUseMagicPacket(String name, String target, int level) {
		this(name, level, target, -1);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		if (playerData.getMP() >= 0 && !playerData.getRecharge()) {
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);

			if(allyTarget.equals("")) { // Direct magic
				if(lockedTarget > -1) {
					ModMagic.registry.get(ResourceLocation.parse(name)).onUse(player, player, level, (LivingEntity) player.level().getEntity(lockedTarget));
				} else {
					ModMagic.registry.get(ResourceLocation.parse(name)).onUse(player, player, level, null);

				}
			} else { // On party member
				Player allyTargetEntity = Utils.getPlayerByName(player.level(), allyTarget.toLowerCase());
				ModMagic.registry.get(ResourceLocation.parse(name)).onUse(allyTargetEntity, player, level, null);
			}
		}

		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
