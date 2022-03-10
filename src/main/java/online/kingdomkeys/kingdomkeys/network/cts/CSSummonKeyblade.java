package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
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
			} else {
				if(playerData.isAbilityEquipped(Strings.synchBlade)) {
					if(playerData.getAlignment() == OrgMember.NONE || playerData.getEquippedWeapon() != null && playerData.getEquippedWeapon().getItem() instanceof KeybladeItem) {
						extraChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
					} else {
						extraChain = orgWeapon;
					}
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
							ItemStack keyblade;
							if(extraChain.getItem() instanceof IKeychain) {
								keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							} else {
								keyblade = new ItemStack(extraChain.getItem());
							}
							keyblade.setTag(extraChain.getTag());
							player.inventory.setInventorySlotContents(40, keyblade);
							player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
							spawnKeybladeParticles(player, Hand.OFF_HAND);

						} else if (player.inventory.getFirstEmptyStack() > -1) {
							ItemStack keyblade;
							if(extraChain.getItem() instanceof IKeychain) {
								keyblade = new ItemStack(((IKeychain) extraChain.getItem()).toSummon());
							} else {
								keyblade = new ItemStack(extraChain.getItem());
							}
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
					if (heldStack.getTag().getUniqueId("keybladeID").equals(chain.getTag().getUniqueId("keybladeID"))) { //Keyblade user
						chain.setTag(heldStack.getTag());
						playerData.equipKeychain(DriveForm.NONE, chain);
						player.inventory.setInventorySlotContents(slotSummoned, ItemStack.EMPTY);
						player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					}
				} else if (heldStack.getItem() instanceof IOrgWeapon || heldStack.getItem() instanceof KeybladeItem) { //Org user
					Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
					for(ItemStack weapon : weapons) {
						if(ItemStack.areItemsEqual(weapon, heldStack)) {
							weapon.setTag(heldStack.getTag());
							break;
						}
					}
					playerData.setWeaponsUnlocked(weapons);
					player.inventory.setInventorySlotContents(slotSummoned, ItemStack.EMPTY);
					if(playerData.isAbilityEquipped(Strings.synchBlade)) {
						player.inventory.setInventorySlotContents(40, ItemStack.EMPTY);
					}
					player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				}
			} else if (slotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.inventory, player.inventory.currentItem, slotSummoned);
				player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				spawnKeybladeParticles(player, Hand.MAIN_HAND);

			} else {
				if (!ItemStack.areItemStacksEqual(chain, ItemStack.EMPTY) || orgWeapon != null) {
					if (ItemStack.areItemStacksEqual(heldStack, ItemStack.EMPTY)) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.areItemsEqual(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}
						}
						//Summon when keyblade is unsummoned
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						spawnKeybladeParticles(player, Hand.MAIN_HAND);

					} else if (player.inventory.getFirstEmptyStack() > -1) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.areItemsEqual(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}
						}
						//When does it happen?
						Utils.swapStack(player.inventory, player.inventory.getFirstEmptyStack(), player.inventory.currentItem);
						player.inventory.setInventorySlotContents(player.inventory.currentItem, keyblade);
						player.world.playSound(null, player.getPosition(), ModSounds.summon.get(), SoundCategory.MASTER, 1.0f, 1.0f);

					}
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	private static void spawnKeybladeParticles(PlayerEntity summoner, Hand hand) {
		Vector3d userPos = new Vector3d(summoner.getPosX(), summoner.getPosY(), summoner.getPosZ());
		Vector3d lHandCenter = new Vector3d(-0.4, -1.3D, -0.38D);
		lHandCenter = lHandCenter.rotateYaw((float) Math.toRadians(-summoner.renderYawOffset));

		Vector3d rHandCenter = new Vector3d(0.4, -1.3D, -0.38D);
		rHandCenter = rHandCenter.rotateYaw((float) Math.toRadians(-summoner.renderYawOffset));
        Vector3d v = null;
        if(hand == Hand.MAIN_HAND) {
        	v = userPos.add(-rHandCenter.x, rHandCenter.y, -rHandCenter.z);
        } else {
        	v = userPos.add(-lHandCenter.x,lHandCenter.y, -lHandCenter.z);
        }
        ((ServerWorld)summoner.world).spawnParticle(ParticleTypes.FIREWORK, v.x, summoner.getPosY() + 1, v.z, 80, 0,0,0, 0.2);
        
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
						if ((Utils.hasID(stack) && stack.getItem() instanceof KeybladeItem) || (playerData.getAlignment() != Utils.OrgMember.NONE) && ((stack.getItem() instanceof IOrgWeapon || (playerData.getEquippedWeapon().getItem() == stack.getItem())))) {
							slot.putStack(ItemStack.EMPTY);
							if(stack.getItem() instanceof IOrgWeapon || (playerData.getAlignment() != Utils.OrgMember.NONE)) {
								Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
								for(ItemStack weapon : weapons) {
									if(ItemStack.areItemsEqual(weapon, stack)) {
										weapon.setTag(stack.getTag());
										break;
									}
								}
								playerData.setWeaponsUnlocked(weapons);
							}
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
			if (droppedItem != null) {
				if (playerData.getEquippedWeapon() != null) {
					if ((droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE) || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem()))) {
						if(droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE)) { //If weapon is org weapon || a keyblade && org member
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.areItemsEqual(weapon, droppedItem)) {
									weapon.setTag(droppedItem.getTag());
									break;
								}
							}
							playerData.setWeaponsUnlocked(weapons);
						}
						player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
						event.setCanceled(true);
						return;
					}
				}
				if ((Utils.hasID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem)) {
					player.world.playSound(null, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
					event.setCanceled(true);
				}
			}
		}
	}

}
