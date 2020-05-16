package online.kingdomkeys.kingdomkeys.network.magic.old;
/*
package online.kingdomkeys.kingdomkeys.packets.magic;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import uk.co.wehavecookies56.kk.common.capability.ModCapabilities;
import uk.co.wehavecookies56.kk.common.container.inventory.InventorySpells;
import uk.co.wehavecookies56.kk.common.lib.Constants;
import uk.co.wehavecookies56.kk.common.lib.Strings;
import uk.co.wehavecookies56.kk.common.network.packet.AbstractMessage.AbstractServerMessage;
import uk.co.wehavecookies56.kk.common.network.packet.PacketDispatcher;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicData;
import uk.co.wehavecookies56.kk.common.network.packet.client.SyncMagicInventory;

public class LevelUpMagic extends AbstractServerMessage<LevelUpMagic> {

    String magic;

    public LevelUpMagic () {}

    public LevelUpMagic (String magic) {
        this.magic = magic;
    }

    @Override
    protected void read (PacketBuffer buffer) throws IOException {
        magic = buffer.readString(40);
    }

    @Override
    protected void write (PacketBuffer buffer) throws IOException {
        buffer.writeString(magic);
    }

    @Override
    public void process (EntityPlayer player, Side side) {

        int firstEmptySlot = -1;

        boolean hasMagicInSlot = false;

        for (int i = 0; i < InventorySpells.INV_SIZE; i++) {
            if (!ItemStack.areItemStacksEqual(player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i), ItemStack.EMPTY)) {
                if (player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().getStackInSlot(i).getItem() == player.getHeldItem(EnumHand.MAIN_HAND).getItem()) {
                    hasMagicInSlot = true;
                }
            } else {
                firstEmptySlot = i;
                break;
            }
        }

        //Check if has magic
        if (!hasMagicInSlot) { //Magic not found in the inventory
            setInFreeSlot(player,firstEmptySlot);
            TextComponentTranslation learnMessage = new TextComponentTranslation(Strings.Chat_Magic_Learn, new TextComponentTranslation(Constants.getMagicName(magic, player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic))));
            learnMessage.getStyle().setColor(TextFormatting.YELLOW);
            player.sendMessage(learnMessage);
        } else { //Magic found, level up
            levelUpMagic(player);
        }
        PacketDispatcher.sendTo(new SyncMagicData(player.getCapability(ModCapabilities.MAGIC_STATE, null), player.getCapability(ModCapabilities.PLAYER_STATS, null)), (EntityPlayerMP) player);
        PacketDispatcher.sendTo(new SyncMagicInventory(player.getCapability(ModCapabilities.MAGIC_STATE, null)), (EntityPlayerMP) player);
    }

	private void setInFreeSlot(EntityPlayer player, int firstEmptySlot) {
		player.getCapability(ModCapabilities.MAGIC_STATE, null).getInventorySpells().setStackInSlot(firstEmptySlot, player.getHeldItem(EnumHand.MAIN_HAND));
        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);		
	}
	
	private void levelUpMagic(EntityPlayer player) {
		if (player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic) < Constants.MAX_MAGIC_LEVEL) { //If the magic level is below the limit, increase it
            player.getCapability(ModCapabilities.MAGIC_STATE, null).setMagicLevel(magic, player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic) + 1);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            TextComponentTranslation levelupMessage = new TextComponentTranslation(Strings.Chat_Magic_Levelup, new TextComponentTranslation(Constants.getMagicName(magic, player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic) - 1)), new TextComponentTranslation(Constants.getMagicName(magic, player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic))));
            levelupMessage.getStyle().setColor(TextFormatting.YELLOW);
            player.sendMessage(levelupMessage);
        } else { //Max magic level
            TextComponentTranslation errorMessage = new TextComponentTranslation(Strings.Chat_Magic_Error, new TextComponentTranslation(Constants.getMagicName(magic, player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic))));
            errorMessage.getStyle().setColor(TextFormatting.YELLOW);
            player.sendMessage(errorMessage);
        }		
	}
}
 */
