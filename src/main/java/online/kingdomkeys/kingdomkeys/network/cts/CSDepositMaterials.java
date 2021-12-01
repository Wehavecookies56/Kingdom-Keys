package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
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

public class CSDepositMaterials {
	
	
	public CSDepositMaterials() {}
	
	public void encode(FriendlyByteBuf buffer) {
	}

	public static CSDepositMaterials decode(FriendlyByteBuf buffer) {
		CSDepositMaterials msg = new CSDepositMaterials();
		return msg;
	}

	public static void handle(CSDepositMaterials message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				for(int i = 0; i < player.getInventory().getContainerSize();i++) {
					ItemStack stack = player.getInventory().getItem(i);					
					if(!ItemStack.matches(stack, ItemStack.EMPTY)) {
						if(ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath())) != null) {
							Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath()));
							playerData.addMaterial(mat, stack.getCount());
							player.getInventory().setItem(i, ItemStack.EMPTY);
						}
						
						//Bag
						if (stack != null && stack.getItem() instanceof SynthesisBagItem) {
		                    IItemHandler bag = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
		                    removeMaterial(bag, player, i);
		    				//PacketHandler.sendTo(new SCSyncSynthBagToClientPacket(bag), (ServerPlayerEntity) player);
						}
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
	            PacketHandler.sendTo(new SCOpenMaterialsScreen(), (ServerPlayer) player);
		});
		ctx.get().setPacketHandled(true);
	}

	private static void removeMaterial(IItemHandler bag, Player player, int i) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        for (int j = 0; j < bag.getSlots(); j++) { //Check bag slots
            ItemStack bagItem = bag.getStackInSlot(j);
        	Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+bagItem.getItem().getRegistryName().getPath()));
            if (!ItemStack.matches(bagItem, ItemStack.EMPTY)) { //If current bag slot is filled
            	if(mat != null) {
            		playerData.addMaterial(mat, bag.getStackInSlot(j).getCount());
            		bag.extractItem(j, bag.getStackInSlot(j).getCount(), false);
            	}
            }
        }
    }
}
