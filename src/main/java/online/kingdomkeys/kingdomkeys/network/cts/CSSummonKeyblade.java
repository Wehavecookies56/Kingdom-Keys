package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

public class CSSummonKeyblade {

	ResourceLocation formToSummonFrom;
	boolean hasForm;

	public CSSummonKeyblade() {
		hasForm = false;
	}

	//Don't pass none please
	public CSSummonKeyblade(ResourceLocation formToSummonFrom) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(hasForm);
		if (formToSummonFrom != null)
			buffer.writeResourceLocation(formToSummonFrom);
	}

	public static CSSummonKeyblade decode(PacketBuffer buffer) {
		CSSummonKeyblade msg = new CSSummonKeyblade();
		msg.hasForm = buffer.readBoolean();
		if (msg.hasForm)
			msg.formToSummonFrom = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSummonKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ItemStack heldStack = player.getHeldItemMainhand();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			ItemStack chain = playerData.getEquippedKeychain(DriveForm.NONE);
			ItemStack extraChain = null;
			if (message.formToSummonFrom != null) {
				if (!message.formToSummonFrom.equals(DriveForm.NONE)) {
					if (playerData.getEquippedKeychains().containsKey(message.formToSummonFrom)) {
						extraChain = playerData.getEquippedKeychain(DriveForm.NONE);
					}
				} else {
					KingdomKeys.LOGGER.fatal(".-.");
					//.-. but why tho
				}
			}
			//TODO handle extraChain
			int slotSummoned = findSummoned(player.inventory, chain);
			if (!ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY) && hasID(heldStack) && heldStack.getItem() instanceof KeybladeItem) {
				//DESUMMON
				if (heldStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))){
					chain.setTag(heldStack.getTag());
					playerData.equipKeychain(DriveForm.NONE, chain);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
					player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				}
			} else if (slotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				swapStack(player.inventory, player.inventory.currentItem, slotSummoned);
				player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			} else {
				if (!ItemStack.areItemStacksEqual(chain, ItemStack.EMPTY)) {
					if (ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY)) {
						ItemStack keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
						keyblade.setTag(chain.getTag());
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					} else if (player.inventory.getFirstEmptyStack() > -1) {
						ItemStack keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
						keyblade.setTag(chain.getTag());
						swapStack(player.inventory, player.inventory.getFirstEmptyStack(), player.inventory.currentItem);
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}
				}
			}


		});
		ctx.get().setPacketHandled(true);
	}

	static boolean hasID(ItemStack stack) {
		if (stack.getTag() != null) {
			if (stack.getTag().hasUniqueId("keybladeID")) {
				return true;
			}
		}
		return false;
	}

	//Returns the inv slot if summoned keychain is found
	static int findSummoned(PlayerInventory inv, ItemStack chain) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack slotStack = inv.getStackInSlot(i);
			//Only check the tag for keyblades
			if (slotStack.getItem() instanceof KeybladeItem) {
				//Make sure it has a tag
				if (hasID(slotStack)) {
					//Compare the ID with the chain's
					if (slotStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	static void swapStack(PlayerInventory inv, int stack1, int stack2) {
		ItemStack tempStack = inv.getStackInSlot(stack2);
		inv.setInventorySlotContents(stack2, inv.getStackInSlot(stack1));
		inv.setInventorySlotContents(stack1, tempStack);
	}

}
