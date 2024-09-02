package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSUpgradeSynthesisBagPacket() implements Packet {

	public static final Type<CSUpgradeSynthesisBagPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_upgrade_synthesis_bag"));

	public static final StreamCodec<FriendlyByteBuf, CSUpgradeSynthesisBagPacket> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSUpgradeSynthesisBagPacket());

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();

		PlayerData playerData = PlayerData.get(player);
		ItemStack stack = Utils.getItemInAnyHand(player, ModItems.synthesisBag.get());

		if(stack != null) {
			CompoundTag nbt = stack.getOrCreateTag();

			int cost = Utils.getBagCosts(nbt.getInt("level"));
			if (playerData.getMunny() >= cost) {
				playerData.setMunny(playerData.getMunny() - cost);
				nbt.putInt("level", nbt.getInt("level") + 1);
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
