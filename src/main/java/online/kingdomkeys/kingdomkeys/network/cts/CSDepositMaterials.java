package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
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
						//System.out.println(stack.getItem().getRegistryName().getPath());
						if(ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath())) != null) {
							Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath()));
							playerData.addMaterial(mat, stack.getCount());
							player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						}
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		});
		ctx.get().setPacketHandled(true);
	}

}
