package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightUtils;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public record CSExtendedReach(int entityId) implements Packet {

	public static final Type<CSExtendedReach> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_extended_reach"));

	public static final StreamCodec<FriendlyByteBuf, CSExtendedReach> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSExtendedReach::entityId,
			CSExtendedReach::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Entity theEntity = player.level().getEntity(entityId);
		if(EpicFightUtils.isBattleMode(player))
			return;
		if (ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY)) {
			return;
		}
		if (player.getMainHandItem().getItem() instanceof IExtendedReach) {
			IExtendedReach theExtendedReachWeapon = (IExtendedReach) player.getMainHandItem().getItem();
			double distanceSq = player.distanceToSqr(theEntity);
			double reachSq = theExtendedReachWeapon.getReach() * theExtendedReachWeapon.getReach();
			if (reachSq >= distanceSq) {
				player.attack(theEntity);
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
