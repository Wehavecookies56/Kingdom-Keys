package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.util.StreamCodecs;

import java.util.ConcurrentModificationException;

public record CSDepositMaterials(String inv, String name, int moogle) implements Packet {

	public static final Type<CSDepositMaterials> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_deposit_materials"));

	public static final StreamCodec<FriendlyByteBuf, CSDepositMaterials> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSDepositMaterials::inv,
			StreamCodecs.STRING_UTF8,
			CSDepositMaterials::name,
			ByteBufCodecs.INT,
			CSDepositMaterials::moogle,
			CSDepositMaterials::new
	);

	private static void removeMaterial(IItemHandler bag, Player player, int i) {
		PlayerData playerData = PlayerData.get(player);
        for (int j = 0; j < bag.getSlots(); j++) { //Check bag slots
            ItemStack synthBag = bag.getStackInSlot(j);
            if (!ItemStack.matches(synthBag, ItemStack.EMPTY)) { //If current bag slot is filled
				if(synthBag.is(ModTags.MATERIALS)) {
            		playerData.addMaterial(synthBag.getItem(), bag.getStackInSlot(j).getCount());
            		bag.extractItem(j, bag.getStackInSlot(j).getCount(), false);
            	}
            }
        }
    }

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		try {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (!ItemStack.matches(stack, ItemStack.EMPTY)) {
					if (stack.is(ModTags.MATERIALS)) {
						playerData.addMaterial(stack.getItem(), stack.getCount());
						player.getInventory().setItem(i, ItemStack.EMPTY);
					}

					//Bag
					if (stack != null && stack.getItem() == ModItems.synthesisBag.get()) {
						IItemHandler bag = stack.getCapability(Capabilities.ItemHandler.ITEM);
						if (bag != null) {
							removeMaterial(bag, player, i);
						}
					}
				}
			}
			PacketHandler.sendTo(new SCOpenMaterialsScreen(playerData, player, inv, name, moogle), (ServerPlayer) player);
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
