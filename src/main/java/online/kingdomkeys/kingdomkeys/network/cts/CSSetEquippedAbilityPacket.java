package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSSetEquippedAbilityPacket(String ability, int level) implements Packet {

	public static final Type<CSSetEquippedAbilityPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_equipped_ability"));

	public static final StreamCodec<FriendlyByteBuf, CSSetEquippedAbilityPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSSetEquippedAbilityPacket::ability,
			ByteBufCodecs.INT,
			CSSetEquippedAbilityPacket::level,
			CSSetEquippedAbilityPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		ServerPlayer player = (ServerPlayer) context.player();
		PlayerData playerData = PlayerData.get(player);
		boolean cancelled;
		if (playerData.isAbilityEquipped(ability, level)) {
			cancelled = NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get(ResourceLocation.parse(ability)), level, player, false)).isCanceled();
		} else {
			cancelled = NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get(ResourceLocation.parse(ability)), level, player, false)).isCanceled();
		}
		if (!cancelled) {
			playerData.equipAbilityToggle(ability, level);
			Utils.RefreshAbilityAttributes(player, playerData);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
