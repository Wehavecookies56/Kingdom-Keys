package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
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
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSTakeMaterials(ItemStack stack, int amount, String inv, String name, int moogle) implements Packet {
	
	public static final Type<CSTakeMaterials> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_take_materials"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSTakeMaterials> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CSTakeMaterials::stack,
			ByteBufCodecs.INT,
			CSTakeMaterials::amount,
			ByteBufCodecs.STRING_UTF8,
			CSTakeMaterials::inv,
			ByteBufCodecs.STRING_UTF8,
			CSTakeMaterials::name,
			ByteBufCodecs.INT,
			CSTakeMaterials::moogle,
			CSTakeMaterials::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		if(!ItemStack.isSameItem(stack, ItemStack.EMPTY)) {
			Material mat = ModMaterials.registry.get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"mat_"+ Utils.getItemRegistryName(stack.getItem()).getPath()));

			if(playerData.getMaterialAmount(mat)<stack.getCount()) {

			} else {
				playerData.removeMaterial(mat, stack.getCount());
				player.getInventory().add(stack);
			}
		}
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
		PacketHandler.sendTo(new SCOpenMaterialsScreen(inv, name, moogle), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
