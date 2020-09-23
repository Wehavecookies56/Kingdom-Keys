package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

public class CSTakeMaterials {
	
	ItemStack stack;
	
	public CSTakeMaterials() {}
	
	public CSTakeMaterials(Item item, int amount) {
		this.stack = new ItemStack(item,amount);
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeItemStack(stack);
	}

	public static CSTakeMaterials decode(PacketBuffer buffer) {
		CSTakeMaterials msg = new CSTakeMaterials();
		msg.stack = buffer.readItemStack();
		return msg;
	}

	public static void handle(CSTakeMaterials message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(!ItemStack.areItemsEqual(message.stack, ItemStack.EMPTY)) {
				Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+message.stack.getItem().getRegistryName().getPath()));
				System.out.println(mat.getMaterialName());
				if(playerData.getMaterialAmount(mat)<message.stack.getCount()) {
					System.out.println("Client unsynced");
				} else {
					playerData.removeMaterial(mat, message.stack.getCount());
					player.inventory.addItemStackToInventory(message.stack);
				}
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
            PacketHandler.sendTo(new SCOpenMaterialsScreen(), (ServerPlayerEntity) player);
		});
		ctx.get().setPacketHandled(true);
	}

}
