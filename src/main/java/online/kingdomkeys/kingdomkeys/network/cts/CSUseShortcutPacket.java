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
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSUseShortcutPacket(int index, int lockOnTarget) implements Packet {

	public static final Type<CSUseShortcutPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_shortcut"));

	public static final StreamCodec<FriendlyByteBuf, CSUseShortcutPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSUseShortcutPacket::index,
			ByteBufCodecs.INT,
			CSUseShortcutPacket::lockOnTarget,
			CSUseShortcutPacket::new
	);

	public CSUseShortcutPacket(int index, LivingEntity lockOnTarget) {
		this(index, lockOnTarget == null ? -1 : lockOnTarget.getId());
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		GlobalData globalData = GlobalData.get(player);
		if(playerData == null || globalData == null)
			return;

		if(playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor) && !globalData.isKO()) {
			if (playerData.getShortcutsMap().containsKey(index)) {
				String[] data = playerData.getShortcutsMap().get(index).split(",");
				String magicName = data[0];
				int level = Integer.parseInt(data[1]);
				Magic magic = ModMagic.registry.get(ResourceLocation.parse(magicName));
				double cost = magic.getCost(level, player);

				if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300 || cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety) || playerData.getMagicCooldownTicks() > 0) {

				} else {
					magic.onUse(player, player, level, (LivingEntity) player.level().getEntity(lockOnTarget));
				}

				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}