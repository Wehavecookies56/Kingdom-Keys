package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenMaterialsScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSTakeMaterials {
	
	ItemStack stack;
	
	public CSTakeMaterials() {}
	
	public CSTakeMaterials(Item item, int amount) {
		this.stack = new ItemStack(item,amount);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeItem(stack);
	}

	public static CSTakeMaterials decode(FriendlyByteBuf buffer) {
		CSTakeMaterials msg = new CSTakeMaterials();
		msg.stack = buffer.readItem();
		return msg;
	}

	public static void handle(CSTakeMaterials message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(!ItemStack.isSame(message.stack, ItemStack.EMPTY)) {
				Material mat = ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+ Utils.getItemRegistryName(message.stack.getItem()).getPath()));
				
				if(playerData.getMaterialAmount(mat)<message.stack.getCount()) {
					
				} else {
					playerData.removeMaterial(mat, message.stack.getCount());
					player.getInventory().add(message.stack);
				}
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
            PacketHandler.sendTo(new SCOpenMaterialsScreen(), (ServerPlayer) player);
		});
		ctx.get().setPacketHandled(true);
	}

}
