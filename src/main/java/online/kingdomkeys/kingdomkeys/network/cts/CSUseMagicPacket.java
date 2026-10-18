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
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUseMagicPacket(String name, int allyTarget, int lockedTarget) implements Packet {

	public static final Type<CSUseMagicPacket> TYPE = new Type<>(KingdomKeys.rl("cs_use_magic"));

	public static final StreamCodec<FriendlyByteBuf, CSUseMagicPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSUseMagicPacket::name,
			ByteBufCodecs.INT,
			CSUseMagicPacket::allyTarget,
			ByteBufCodecs.INT,
			CSUseMagicPacket::lockedTarget,
			CSUseMagicPacket::new
	);

	public CSUseMagicPacket(String name, LivingEntity lockedTarget) {
		this(name, -1, lockedTarget == null ? -1 : lockedTarget.getId());
	}
	
	public CSUseMagicPacket(String name, int targetID) {
		this(name, targetID, -1);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		ResourceLocation magicName = KingdomKeys.rl(name);

		// Same rules as the shortcuts, so the command menu can't cast what they wouldn't
		if (!Utils.canCastMagic(player, playerData, magicName)) {
			if (playerData != null) {
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}
			return;
		}

		if (NeoForge.EVENT_BUS.post(new MagicSpellCastEvent(player, magicName)).isCanceled())
			return;

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);

		Magic magic = ModMagic.registry.get(magicName);
		if(allyTarget == -1) { // Direct magic
			magic.onUse(player, player, player.level().getEntity(lockedTarget) instanceof LivingEntity target ? target : null);
		} else if (player.level().getEntity(allyTarget) instanceof LivingEntity allyTargetEntity) { // On party member
			magic.onUse(allyTargetEntity, player, null);
		}

		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
