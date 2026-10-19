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
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSSetEquippedAbilityPacket(ResourceLocation ability, int level, boolean equip) implements Packet {

	public static final Type<CSSetEquippedAbilityPacket> TYPE = new Type<>(KingdomKeys.rl("cs_set_equipped_ability"));

	public static final StreamCodec<FriendlyByteBuf, CSSetEquippedAbilityPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, CSSetEquippedAbilityPacket::ability,
			ByteBufCodecs.INT, CSSetEquippedAbilityPacket::level,
			ByteBufCodecs.BOOL, CSSetEquippedAbilityPacket::equip,
			CSSetEquippedAbilityPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		ServerPlayer player = (ServerPlayer) context.player();
		PlayerData playerData = PlayerData.get(player);
		Ability instance = ModAbilities.registry.get(ability);

		if (instance == null) {
			return;
		}

		if (playerData.hasAbilityCopy(ability, level) && playerData.isAbilityEquipped(ability, level) != equip) {
			if (equip) {
				// Same check as the client
				boolean affordable = Utils.getConsumedAP(playerData) + instance.getAPCost() <= playerData.getMaxAP(true);
				if (affordable && Utils.canEquipAbility(playerData, instance) && !NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(instance, level, player, false)).isCanceled()) {
					playerData.equipAbility(ability, level);
				}
			} else if (!NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(instance, level, player, false)).isCanceled()) {
				playerData.unequipAbility(ability, level);
			}
			Utils.RefreshAbilityAttributes(player, playerData);
		}

		// The menu already shows the change, this puts it right if it did not go through
		PacketHandler.sendTo(new SCSyncPlayerData(player), player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
