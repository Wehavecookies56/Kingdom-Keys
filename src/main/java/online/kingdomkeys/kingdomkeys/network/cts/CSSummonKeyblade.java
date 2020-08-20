package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class CSSummonKeyblade {


	public CSSummonKeyblade() {
	}
	
	public void encode(PacketBuffer buffer) {
	}

	public static CSSummonKeyblade decode(PacketBuffer buffer) {
		CSSummonKeyblade msg = new CSSummonKeyblade();
		return msg;
	}

	public static void handle(CSSummonKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ItemStack itemStack = player.getHeldItemMainhand();
			
			ItemStack result = null;
			if(itemStack.getItem() instanceof KeychainItem) {
				//Summon Keyblade
				KeychainItem kcItem = (KeychainItem) itemStack.getItem();
				result = new ItemStack(kcItem.getKeyblade());
				player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				
			} else if(itemStack.getItem() instanceof KeybladeItem) {
				//Unsummon Keyblade
				KeybladeItem kcItem = (KeybladeItem) itemStack.getItem();
				result = new ItemStack(kcItem.data.getKeychain());
				player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			}
			
			if(result != null) {
				result.setTag(itemStack.getTag());
				player.inventory.setInventorySlotContents(player.inventory.currentItem, result);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
