package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUseItemPacket(int slot, String target) implements Packet {

	public static final Type<CSUseItemPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_item"));

	public static final StreamCodec<FriendlyByteBuf, CSUseItemPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSUseItemPacket::slot,
			ByteBufCodecs.STRING_UTF8,
			CSUseItemPacket::target,
			CSUseItemPacket::new
	);

	public CSUseItemPacket(int slot) {
		this(slot, "");
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		KKPotionItem potion = (KKPotionItem) playerData.getEquippedItem(slot).getItem();

		if (target.equals("")) {
			potion.potionEffect(player);
		} else {
			Player targetEntity = Utils.getPlayerByName(player.level(), target.toLowerCase());
			potion.potionEffect(targetEntity);
		}
		playerData.equipItem(slot, ItemStack.EMPTY);

		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
