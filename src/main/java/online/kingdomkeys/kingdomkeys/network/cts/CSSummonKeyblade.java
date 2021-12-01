package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
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

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(forceDesummon);
		buffer.writeBoolean(hasForm);
		buffer.writeInt(alignment.ordinal());
		if (formToSummonFrom != null)
			buffer.writeResourceLocation(formToSummonFrom);
	}

	public static CSSummonKeyblade decode(FriendlyByteBuf buffer) {
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
			Player player = ctx.get().getSender();
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

			ItemStack heldStack = player.getMainHandItem();
			ItemStack offHeldStack = player.getOffhandItem();
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
			} else {
				if(playerData.isAbilityEquipped(Strings.synchBlade)) {
					if(playerData.getAlignment() == OrgMember.NONE || playerData.getAlignment() == OrgMember.ROXAS) {
						extraChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
					} else {
						extraChain = orgWeapon;
					}
				}
			
			}
			//TODO dual wield org weapons
			int slotSummoned = Utils.findSummoned(player.getInventory(), chain, false);
			int extraSlotSummoned = -1;
			if (extraChain != null)
				extraSlotSummoned = Utils.findSummoned(player.getInventory(), extraChain, false);
			if (orgWeapon != null) {
				slotSummoned = Utils.findSummoned(player.getInventory(), orgWeapon, true);
			}
			ItemStack summonedStack = slotSummoned > -1 ? player.getInventory().getItem(slotSummoned) : ItemStack.EMPTY;
			ItemStack summonedExtraStack = extraSlotSummoned > -1 ? player.getInventory().getItem(extraSlotSummoned) : ItemStack.EMPTY;
			if (message.forceDesummon) {
				heldStack = summonedStack;
				if (!ItemStack.matches(heldStack, ItemStack.EMPTY)) {
					offHeldStack = summonedExtraStack;
				}
			}
			if ((message.forceDesummon) || (!ItemStack.matches(offHeldStack, ItemStack.EMPTY) && (Utils.hasID(offHeldStack)))) {
				if (message.forceDesummon || (!ItemStack.matches(heldStack, ItemStack.EMPTY) && (ItemStack.matches(heldStack, summonedStack)))) {
					if (offHeldStack.getItem() instanceof KeybladeItem) {
						if (offHeldStack.getTag().getUUID("keybladeID").equals(extraChain.getTag().getUUID("keybladeID"))) {
							extraChain.setTag(offHeldStack.getTag());
							playerData.equipKeychain(message.formToSummonFrom, extraChain);
							player.getInventory().setItem(extraSlotSummoned, ItemStack.EMPTY);
							player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				}
			} else if (extraSlotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.getInventory(), 40, extraSlotSummoned);
				player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
			} else {
				if (extraChain != null) {
					if (!ItemStack.matches(extraChain, ItemStack.EMPTY)) {
						if (ItemStack.matches(offHeldStack, ItemStack.EMPTY)) {
							ItemStack keyblade;
							if(extraChain.getItem() instanceof IKeychain) {
								keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							} else {
								keyblade = new ItemStack(extraChain.getItem());
							}
							keyblade.setTag(extraChain.getTag());
							player.getInventory().setItem(40, keyblade);
							player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						} else if (player.getInventory().getFreeSlot() > -1) {
							ItemStack keyblade;
							if(extraChain.getItem() instanceof IKeychain) {
								keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							} else {
								keyblade = new ItemStack(extraChain.getItem());
							}
							keyblade.setTag(extraChain.getTag());
							Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), 40);
							player.getInventory().setItem(40, keyblade);
							player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				}
			}
			if ((message.forceDesummon) || (!ItemStack.matches(heldStack, ItemStack.EMPTY) && (Utils.hasID(heldStack) || (orgWeapon != null && (heldStack.getItem() instanceof IOrgWeapon || heldStack.getItem() instanceof KeybladeItem))))) {
				//DESUMMON
				if (heldStack.getItem() instanceof KeybladeItem && orgWeapon == null) {
					if (heldStack.getTag().getUUID("keybladeID").equals(chain.getTag().getUUID("keybladeID"))) { //Keyblade user
						chain.setTag(heldStack.getTag());
						playerData.equipKeychain(DriveForm.NONE, chain);
						player.getInventory().setItem(slotSummoned, ItemStack.EMPTY);
						player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}
				} else if (heldStack.getItem() instanceof OrgWeaponItem || heldStack.getItem() instanceof KeybladeItem) { //Org user
					Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
					for(ItemStack weapon : weapons) {
						if(ItemStack.isSame(weapon, heldStack)) {
							weapon.setTag(heldStack.getTag());
							break;
						}
					}
					playerData.setWeaponsUnlocked(weapons);
					player.getInventory().setItem(slotSummoned, ItemStack.EMPTY);
					if(playerData.isAbilityEquipped(Strings.synchBlade)) {
						player.getInventory().setItem(40, ItemStack.EMPTY);
					}
					player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			} else if (slotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.getInventory(), player.getInventory().selected, slotSummoned);
				player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
			} else {
				if (!ItemStack.matches(chain, ItemStack.EMPTY) || orgWeapon != null) {
					if (ItemStack.matches(heldStack, ItemStack.EMPTY)) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.isSame(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}
						}
						player.getInventory().setItem(player.getInventory().selected, keyblade);
						player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					} else if (player.getInventory().getFreeSlot() > -1) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.isSame(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}
						}
						Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), player.getInventory().selected);
						player.getInventory().setItem(player.getInventory().selected, keyblade);
						player.level.playSound(null, player.blockPosition(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
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
			ServerPlayer player = (ServerPlayer) event.getPlayer();
			AbstractContainerMenu openContainer = event.getContainer();
			AbstractContainerMenu playerContainer = player.inventoryMenu;

			if (!openContainer.equals(playerContainer)) {
				openContainer.slots.forEach(slot -> {
					ItemStack stack = slot.getItem();
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (slot.container != player.getInventory()) {
						if ((Utils.hasID(stack) && stack.getItem() instanceof KeybladeItem) || (playerData.getAlignment() != Utils.OrgMember.NONE) && ((stack.getItem() instanceof OrgWeaponItem || (playerData.getEquippedWeapon().getItem() == stack.getItem())))) {
							slot.set(ItemStack.EMPTY);
							if(stack.getItem() instanceof OrgWeaponItem || (playerData.getAlignment() != Utils.OrgMember.NONE)) {
								Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
								for(ItemStack weapon : weapons) {
									if(ItemStack.isSame(weapon, stack)) {
										weapon.setTag(stack.getTag());
										break;
									}
								}
								playerData.setWeaponsUnlocked(weapons);
							}
							openContainer.broadcastChanges();
							player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				});
			}
		}

		@SubscribeEvent
		public static void dropItem(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntityItem().getItem();
			//If it doesn't have an ID it was not summoned unless it's an org weapon
			Player player = event.getPlayer();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (droppedItem != null) {
				if (playerData.getEquippedWeapon() != null) {
					if ((droppedItem.getItem() instanceof OrgWeaponItem || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE) || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem()))) {
						if(droppedItem.getItem() instanceof OrgWeaponItem || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE)) { //If weapon is org weapon || a keyblade && org member
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.isSame(weapon, droppedItem)) {
									weapon.setTag(droppedItem.getTag());
									break;
								}
							}
							playerData.setWeaponsUnlocked(weapons);
						}
						player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						event.setCanceled(true);
						return;
					}
				}
				if ((Utils.hasID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem)) {
					player.level.playSound(null, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					event.setCanceled(true);
				}
			}
		}
	}

}
