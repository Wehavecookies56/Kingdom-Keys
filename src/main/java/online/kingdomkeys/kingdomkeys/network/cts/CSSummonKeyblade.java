package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class CSSummonKeyblade {

	ResourceLocation formToSummonFrom;
	boolean hasForm;
	boolean forceDesummon;
	OrgMember alignment;

	public CSSummonKeyblade() {
		hasForm = false;
		forceDesummon = false;
		alignment = OrgMember.NONE;
	}

	public CSSummonKeyblade(boolean forceDesummon) {
		this.forceDesummon = forceDesummon;
		alignment = OrgMember.NONE;
	}

	//Don't pass none please
	public CSSummonKeyblade(ResourceLocation formToSummonFrom) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
		forceDesummon = false;
		alignment = OrgMember.NONE;
	}

	public CSSummonKeyblade(ResourceLocation formToSummonFrom, boolean forceDesummon) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
		this.forceDesummon = forceDesummon;
		alignment = OrgMember.NONE;
	}
	
	public CSSummonKeyblade(boolean forceDesummon, OrgMember alignment) {
		hasForm = false;
		this.forceDesummon = forceDesummon;
		this.alignment = alignment;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(forceDesummon);
		buffer.writeBoolean(hasForm);
		buffer.writeInt(alignment.ordinal());
		if (formToSummonFrom != null)
			buffer.writeResourceLocation(formToSummonFrom);
	}

	public static CSSummonKeyblade decode(PacketBuffer buffer) {
		CSSummonKeyblade msg = new CSSummonKeyblade();
		msg.forceDesummon = buffer.readBoolean();
		msg.hasForm = buffer.readBoolean();
		msg.alignment = OrgMember.values()[buffer.readInt()];
		if (msg.hasForm)
			msg.formToSummonFrom = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSummonKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			if(playerData.getActiveDriveForm().equals(Strings.Form_Anti))
				return;
			ItemStack orgWeapon = null;

			if (playerData.getAlignment() != Utils.OrgMember.NONE) {
				orgWeapon = playerData.getEquippedWeapon().copy();
			}
			
			if(orgWeapon == null) {
				if(message.alignment != OrgMember.NONE) {
					orgWeapon = playerData.getEquippedWeapon().copy();
				}
			}

			ItemStack heldStack = player.getHeldItemMainhand();
			ItemStack offHeldStack = player.getHeldItemOffhand();
			ItemStack chain = playerData.getEquippedKeychain(DriveForm.NONE);
			ItemStack extraChain = null;
			if (message.formToSummonFrom != null) {
				if (!message.formToSummonFrom.equals(DriveForm.NONE)) {
					if (playerData.getEquippedKeychains().containsKey(message.formToSummonFrom)) {
						extraChain = playerData.getEquippedKeychain(message.formToSummonFrom);
					}
				} else {
					KingdomKeys.LOGGER.fatal(".-.");
					//.-. but why tho
				}
			}
			//TODO dual wield org weapons
			int slotSummoned = Utils.findSummoned(player.inventory, chain, false);
			int extraSlotSummoned = -1;
			if (extraChain != null)
				extraSlotSummoned = Utils.findSummoned(player.inventory, extraChain, false);
			if (orgWeapon != null) {
				slotSummoned = Utils.findSummoned(player.inventory, orgWeapon, true);
			}
			ItemStack summonedStack = slotSummoned > -1 ? player.inventory.getStackInSlot(slotSummoned) : ItemStack.EMPTY;
			ItemStack summonedExtraStack = extraSlotSummoned > -1 ? player.inventory.getStackInSlot(extraSlotSummoned) : ItemStack.EMPTY;
			if (message.forceDesummon) {
				heldStack = summonedStack;
				if (!ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY)) {
					offHeldStack = summonedExtraStack;
				}
			}
			if ((message.forceDesummon) || (!ItemStack.areItemStacksEqual(offHeldStack, ItemStack.EMPTY) && (Utils.hasID(offHeldStack)))) {
				if (message.forceDesummon || (!ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY) && (ItemStack.areItemStacksEqual(heldStack, summonedStack)))) {
					if (offHeldStack.getItem() instanceof KeybladeItem) {
						if (offHeldStack.getTag().getUniqueId("keybladeID").equals(extraChain.getTag().getUniqueId("keybladeID"))) {
							extraChain.setTag(offHeldStack.getTag());
							playerData.equipKeychain(message.formToSummonFrom, extraChain);
							player.inventory.setInventorySlotContents(extraSlotSummoned, ItemStack.EMPTY);
							player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						}
					}
				}
			} else if (extraSlotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.inventory, 40, extraSlotSummoned);
				player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			} else {
				if (extraChain != null) {
					if (!ItemStack.areItemStacksEqual(extraChain, ItemStack.EMPTY)) {
						if (ItemStack.areItemStacksEqual(offHeldStack, ItemStack.EMPTY)) {
							ItemStack keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							keyblade.setTag(extraChain.getTag());
							player.inventory.setInventorySlotContents(40, keyblade);
							player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						} else if (player.inventory.getFirstEmptyStack() > -1) {
							ItemStack keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							keyblade.setTag(extraChain.getTag());
							Utils.swapStack(player.inventory, player.inventory.getFirstEmptyStack(), 40);
							player.inventory.setInventorySlotContents(40, keyblade);
							player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						}
					}
				}
			}
			if ((message.forceDesummon) || (!ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY) && (Utils.hasID(heldStack) || (orgWeapon != null && (heldStack.getItem() instanceof IOrgWeapon || heldStack.getItem() instanceof KeybladeItem))))) {
				//DESUMMON
				if (heldStack.getItem() instanceof KeybladeItem && orgWeapon == null) {
					if (heldStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))) {
						chain.setTag(heldStack.getTag());
						playerData.equipKeychain(DriveForm.NONE, chain);
						player.inventory.setInventorySlotContents(slotSummoned, ItemStack.EMPTY);
						player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}
				} else if (heldStack.getItem() instanceof OrgWeaponItem || heldStack.getItem() instanceof KeybladeItem) {
					player.inventory.setInventorySlotContents(slotSummoned, ItemStack.EMPTY);
					player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				}
			} else if (slotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.inventory, player.inventory.currentItem, slotSummoned);
				player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			} else {
				if (!ItemStack.areItemStacksEqual(chain, ItemStack.EMPTY) || orgWeapon != null) {
					if (ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY)) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
						}
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					} else if (player.inventory.getFirstEmptyStack() > -1) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
						}
						Utils.swapStack(player.inventory, player.inventory.getFirstEmptyStack(), player.inventory.currentItem);
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void containerClose(PlayerContainerEvent.Close event) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			Container openContainer = event.getContainer();
			Container playerContainer = player.container;

			if (!openContainer.equals(playerContainer)) {
				openContainer.inventorySlots.forEach(slot -> {
					ItemStack stack = slot.getStack();
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (slot.inventory != player.inventory) {
						if ((Utils.hasID(stack) && stack.getItem() instanceof KeybladeItem) || (playerData.getAlignment() != Utils.OrgMember.NONE) && ((stack.getItem() instanceof OrgWeaponItem || (playerData.getEquippedWeapon().getItem() == stack.getItem())))) {
							slot.putStack(ItemStack.EMPTY);
							openContainer.detectAndSendChanges();
							player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						}
					}
				});
			}
		}

		@SubscribeEvent
		public static void dropItem(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntityItem().getItem();
			//If it doesn't have an ID it was not summoned unless it's an org weapon
			PlayerEntity player = event.getPlayer();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if ((Utils.hasID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem) || ((droppedItem.getItem() instanceof OrgWeaponItem || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem())))) {
				player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				event.setCanceled(true);
			}
		}
	}

}
