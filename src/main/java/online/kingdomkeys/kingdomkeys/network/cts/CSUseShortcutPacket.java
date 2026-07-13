package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.MagicSpellCastEvent;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSUseShortcutPacket(int index, int lockOnTarget) implements Packet {

	public static final Type<CSUseShortcutPacket> TYPE = new Type<>(KingdomKeys.rl("cs_use_shortcut"));

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
		if(playerData == null)
			return;

		if (playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor) && !player.hasEffect(ModMobEffects.KO)) {
			if (playerData.getShortcutsMap().containsKey(index)) {
				int slot = playerData.getShortcutsMap().get(index);
				if(slot >= playerData.getMaxMagics())
					return;

				ItemStack stack = playerData.getEquippedMagics().get(slot);
				if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof MagicSpellItem spell)) {
					return;
				}

				ResourceLocation magicName = spell.getMagic();

				Magic magic = ModMagic.registry.get(magicName);
				double cost = magic.getCost( player);

				boolean allowUseMagicIfCostIsHigher = ModConfigs.SERVER.allowCastMagicIfTooExpensive.get();
				boolean insufficientMP = cost > playerData.getMaxMP() && cost < 300;

				if (playerData.getMaxMP() == 0 || playerData.getRecharge() || ((!allowUseMagicIfCostIsHigher && insufficientMP)|| (cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(ModAbilities.MP_SAFETY))) && playerData.getMagicCooldownTicks() <= 0){

				//if (playerData.getMaxMP() == 0 || playerData.getRecharge() || (cost > playerData.getMaxMP() && cost < 300) || (cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety)) || playerData.getMagicCooldownTicks() > 0) {

				} else {
					if (NeoForge.EVENT_BUS.post(new MagicSpellCastEvent(player, magicName)).isCanceled())
						return;
					magic.onUse(player, player, (LivingEntity) player.level().getEntity(lockOnTarget));
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