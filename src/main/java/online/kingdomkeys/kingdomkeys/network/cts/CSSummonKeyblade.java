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
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSSummonKeyblade {

	ResourceLocation formToSummonFrom;
	boolean hasForm;
	boolean forceDesummon;

	public CSSummonKeyblade() {
		hasForm = false;
		forceDesummon = false;
	}

	public CSSummonKeyblade(boolean forceDesummon) {
		this.forceDesummon = forceDesummon;
	}

	//Don't pass none please
	public CSSummonKeyblade(ResourceLocation formToSummonFrom) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
		forceDesummon = false;
	}

	public CSSummonKeyblade(ResourceLocation formToSummonFrom, boolean forceDesummon) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
		this.forceDesummon = forceDesummon;
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(forceDesummon);
		buffer.writeBoolean(hasForm);
		if (formToSummonFrom != null)
			buffer.writeResourceLocation(formToSummonFrom);
	}

	public static CSSummonKeyblade decode(PacketBuffer buffer) {
		CSSummonKeyblade msg = new CSSummonKeyblade();
		msg.forceDesummon = buffer.readBoolean();
		msg.hasForm = buffer.readBoolean();
		if (msg.hasForm)
			msg.formToSummonFrom = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSummonKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

			ItemStack orgWeapon = null;
			if (playerData.getAlignment() != Utils.OrgMember.NONE) {
				orgWeapon = playerData.getEquippedWeapon().getWeapon();
			}

			ItemStack heldStack = player.getHeldItemMainhand();
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
			int slotSummoned = Utils.findSummoned(player.inventory, chain);
			if (orgWeapon != null) {
				slotSummoned = Utils.findSummoned(player.inventory, orgWeapon);
			}
			ItemStack summonedStack = slotSummoned > -1 ? player.inventory.getStackInSlot(slotSummoned) : ItemStack.EMPTY;
			if (message.forceDesummon)
				heldStack = summonedStack;
			if ((message.forceDesummon) || (!ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY) && (Utils.hasID(heldStack) || (orgWeapon != null && heldStack.getItem() instanceof OrgWeaponItem)))) {
				//DESUMMON
				if (heldStack.getItem() instanceof KeybladeItem) {
					if (heldStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))) {
						chain.setTag(heldStack.getTag());
						playerData.equipKeychain(DriveForm.NONE, chain);
						player.inventory.setInventorySlotContents(slotSummoned, ItemStack.EMPTY);
						player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}
				} else if (heldStack.getItem() instanceof OrgWeaponItem) {
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
					if (slot.inventory != player.inventory) {
						if ((Utils.hasID(stack) && stack.getItem() instanceof KeybladeItem) || stack.getItem() instanceof OrgWeaponItem) {
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
			if ((Utils.hasID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem) || droppedItem.getItem() instanceof OrgWeaponItem) {
				PlayerEntity player = event.getPlayer();
				player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				event.setCanceled(true);
			}
		}
	}

}
