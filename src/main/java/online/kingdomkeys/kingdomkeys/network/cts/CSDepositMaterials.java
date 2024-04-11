package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.ConcurrentModificationException;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.SynthesisBagItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSDepositMaterials {
	
	public CSDepositMaterials() {}

	String inv;
	String name;
	int moogle = -1;

	public CSDepositMaterials(String inv, String name, int moogle) {
		this.inv = inv;
		this.moogle = moogle;
		this.name = name;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(inv);
		buffer.writeInt(moogle);
		buffer.writeUtf(name);
	}

	public static CSDepositMaterials decode(FriendlyByteBuf buffer) {
		CSDepositMaterials msg = new CSDepositMaterials();
		msg.inv = buffer.readUtf();
		msg.moogle = buffer.readInt();
		msg.name = buffer.readUtf();
		return msg;
	}

	public static void handle(CSDepositMaterials message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				try {
					for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
						ItemStack stack = player.getInventory().getItem(i);
						if (!ItemStack.matches(stack, ItemStack.EMPTY)) {
							if (ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "mat_" + Utils.getItemRegistryName(stack.getItem()).getPath())) != null) {
								Material mat = ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "mat_" + Utils.getItemRegistryName(stack.getItem()).getPath()));
								playerData.addMaterial(mat, stack.getCount());
								player.getInventory().setItem(i, ItemStack.EMPTY);
							}

							//Bag
							if (stack != null && stack.getItem() instanceof SynthesisBagItem) {
								IItemHandler bag = stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
								removeMaterial(bag, player, i);
								//PacketHandler.sendTo(new SCSyncSynthBagToClientPacket(bag), (ServerPlayerEntity) player);
							}
						}
					}
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
					PacketHandler.sendTo(new SCOpenMaterialsScreen(message.inv, message.name, message.moogle), (ServerPlayer) player);
				} catch (ConcurrentModificationException e) {
					e.printStackTrace();
				}
		});
		ctx.get().setPacketHandled(true);
	}

	private static void removeMaterial(IItemHandler bag, Player player, int i) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        for (int j = 0; j < bag.getSlots(); j++) { //Check bag slots
            ItemStack bagItem = bag.getStackInSlot(j);
        	Material mat = ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+Utils.getItemRegistryName(bagItem.getItem()).getPath()));
            if (!ItemStack.matches(bagItem, ItemStack.EMPTY)) { //If current bag slot is filled
            	if(mat != null) {
            		playerData.addMaterial(mat, bag.getStackInSlot(j).getCount());
            		bag.extractItem(j, bag.getStackInSlot(j).getCount(), false);
            	}
            }
        }
    }
}
