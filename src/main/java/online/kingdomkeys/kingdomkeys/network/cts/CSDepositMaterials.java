package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

public class CSDepositMaterials {
	
	
	public CSDepositMaterials() {}
	
	public void encode(PacketBuffer buffer) {
	}

	public static CSDepositMaterials decode(PacketBuffer buffer) {
		CSDepositMaterials msg = new CSDepositMaterials();
		return msg;
	}

	public static void handle(CSDepositMaterials message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				for(int i = 0; i < player.inventory.getSizeInventory();i++) {
					ItemStack stack = player.inventory.getStackInSlot(i);					
					if(!ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)) {
						if(ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath())) != null) {
							Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath()));
							playerData.addMaterial(mat, stack.getCount());
							player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						}
						
						//Bag
						if (player.inventory.mainInventory.get(i).getItem() == ModItems.synthesisBag.get()) {
		                    IItemHandler bag = player.inventory.mainInventory.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
		                    removeMaterial(bag, player, i);
		    				//PacketHandler.sendTo(new SCSyncSynthBagToClientPacket(bag), (ServerPlayerEntity) player);
						}
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
	            PacketHandler.sendTo(new SCOpenMaterialsScreen(), (ServerPlayerEntity) player);
		});
		ctx.get().setPacketHandled(true);
	}

	private static void removeMaterial(IItemHandler bag, PlayerEntity player, int i) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        for (int j = 0; j < bag.getSlots(); j++) { //Check bag slots
            ItemStack bagItem = bag.getStackInSlot(j);
        	Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+bagItem.getItem().getRegistryName().getPath()));
            if (!ItemStack.areItemStacksEqual(bagItem, ItemStack.EMPTY)) { //If current bag slot is filled
            	if(mat != null) {
            		playerData.addMaterial(mat, bag.getStackInSlot(j).getCount());
            		bag.extractItem(j, bag.getStackInSlot(j).getCount(), false);
            	}
            }
        }
    }
}
