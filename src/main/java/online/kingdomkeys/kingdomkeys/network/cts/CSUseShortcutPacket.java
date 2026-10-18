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
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUseShortcutPacket(int index, int lockOnTarget) implements Packet {

	public static final Type<CSUseShortcutPacket> TYPE = new Type<>(KingdomKeys.rl("cs_use_shortcut"));

	public static final StreamCodec<FriendlyByteBuf, CSUseShortcutPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, CSUseShortcutPacket::index,
			ByteBufCodecs.INT, CSUseShortcutPacket::lockOnTarget,
			CSUseShortcutPacket::new
	);

	public CSUseShortcutPacket(int index, LivingEntity lockOnTarget) {
		this(index, lockOnTarget == null ? -1 : lockOnTarget.getId());
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		if(playerData == null)
			return;

		ResourceLocation magicName = Utils.getShortcutMagic(playerData, index);
		if (magicName == null)
			return;

		if (Utils.canCastMagic(player, playerData, magicName)) {
			Magic magic = ModMagic.registry.get(magicName);
			double cost = magic.getCost(player);

			// MP Safety only looks after the shortcuts
			boolean mpSafety = cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(ModAbilities.MP_SAFETY);
			if (!mpSafety) {
				if (NeoForge.EVENT_BUS.post(new MagicSpellCastEvent(player, magicName)).isCanceled())
					return;
				magic.onUse(player, player, player.level().getEntity(lockOnTarget) instanceof LivingEntity target ? target : null);
			}
		}

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
