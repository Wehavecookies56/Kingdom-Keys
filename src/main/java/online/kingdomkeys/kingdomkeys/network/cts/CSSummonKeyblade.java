package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
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
					if(playerData.getAlignment() == OrgMember.NONE || playerData.getEquippedWeapon() != null && playerData.getEquippedWeapon().getItem() instanceof KeybladeItem) {
						extraChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
					} else {
						extraChain = orgWeapon.copy();
						for(ItemStack weapon : playerData.getWeaponsUnlocked()) {
							if(ItemStack.isSameItem(weapon, extraChain)) {
								extraChain.setTag(weapon.getTag());
								break;
							}
						}
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
			if ((message.forceDesummon) || (!ItemStack.matches(offHeldStack, ItemStack.EMPTY) && (Utils.hasKeybladeID(offHeldStack)))) {
				if (message.forceDesummon || (!ItemStack.matches(heldStack, ItemStack.EMPTY) && (ItemStack.matches(heldStack, summonedStack)))) {
					if (offHeldStack.getItem() instanceof KeybladeItem) {
						if (offHeldStack.getTag().getUUID("keybladeID").equals(extraChain.getTag().getUUID("keybladeID"))) {
							extraChain.setTag(offHeldStack.getTag());
							playerData.equipKeychain(message.formToSummonFrom, extraChain);
							player.getInventory().setItem(extraSlotSummoned, ItemStack.EMPTY);
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				}
			} else if (extraSlotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.getInventory(), 40, extraSlotSummoned);
				player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);

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
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							spawnKeybladeParticles(player, InteractionHand.OFF_HAND);

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
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				}
			}
			if ((message.forceDesummon) || (!ItemStack.matches(heldStack, ItemStack.EMPTY) && (Utils.hasKeybladeID(heldStack) || (orgWeapon != null && (heldStack.getItem() instanceof IOrgWeapon || heldStack.getItem() instanceof KeybladeItem))))) {
				//DESUMMON
				if (heldStack.getItem() instanceof KeybladeItem && orgWeapon == null) {
					if (heldStack.getTag().getUUID("keybladeID").equals(chain.getTag().getUUID("keybladeID"))) { //Keyblade user
						chain.setTag(heldStack.getTag());
						playerData.equipKeychain(DriveForm.NONE, chain);
						player.getInventory().setItem(slotSummoned, ItemStack.EMPTY);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}
				} else if (heldStack.getItem() instanceof IOrgWeapon || heldStack.getItem() instanceof KeybladeItem) { //Org user
					Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
					for(ItemStack weapon : weapons) {
						if(ItemStack.isSameItem(weapon, heldStack)) {
							weapon.setTag(heldStack.getTag());
							break;
						}
					}
					playerData.setWeaponsUnlocked(weapons);
					player.getInventory().setItem(slotSummoned, ItemStack.EMPTY);
					if(playerData.isAbilityEquipped(Strings.synchBlade)) {
						player.getInventory().setItem(40, ItemStack.EMPTY);
					}
					player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			} else if (slotSummoned > -1) {
				//SUMMON FROM ANOTHER SLOT
				Utils.swapStack(player.getInventory(), player.getInventory().selected, slotSummoned);
				player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				spawnKeybladeParticles(player, InteractionHand.MAIN_HAND);

			} else {
				if (!ItemStack.matches(chain, ItemStack.EMPTY) || orgWeapon != null) {
					if (ItemStack.matches(heldStack, ItemStack.EMPTY)) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else {
							//Summon org
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.isSameItem(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}

						}
						//Summon when keyblade is unsummoned
						player.getInventory().setItem(player.getInventory().selected, keyblade);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						spawnKeybladeParticles(player, InteractionHand.MAIN_HAND);

					} else if (player.getInventory().getFreeSlot() > -1) {
						ItemStack keyblade;
						if (orgWeapon == null) {
							keyblade = new ItemStack(((IKeychain) chain.getItem()).toSummon());
							keyblade.setTag(chain.getTag());
						} else { //Summon org weapon
							keyblade = orgWeapon;
							Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
							for(ItemStack weapon : weapons) {
								if(ItemStack.isSameItem(weapon, keyblade)) {
									keyblade.setTag(weapon.getTag());
									break;
								}
							}
						}
						//When does it happen?
						Utils.swapStack(player.getInventory(), player.getInventory().getFreeSlot(), player.getInventory().selected);
						player.getInventory().setItem(player.getInventory().selected, keyblade);
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.summon.get(), SoundSource.MASTER, 1.0f, 1.0f);

					}
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	private static void spawnKeybladeParticles(Player summoner, InteractionHand hand) {
		Vec3 userPos = new Vec3(summoner.getX(), summoner.getY(), summoner.getZ());
		Vec3 lHandCenter = new Vec3(-0.4, -1.3D, -0.38D);
		lHandCenter = lHandCenter.yRot((float) Math.toRadians(-summoner.yBodyRot));

		Vec3 rHandCenter = new Vec3(0.4, -1.3D, -0.38D);
		rHandCenter = rHandCenter.yRot((float) Math.toRadians(-summoner.yBodyRot));
        Vec3 v = null;
        if(hand == InteractionHand.MAIN_HAND) {
        	v = userPos.add(-rHandCenter.x, rHandCenter.y, -rHandCenter.z);
        } else {
        	v = userPos.add(-lHandCenter.x,lHandCenter.y, -lHandCenter.z);
        }
        ((ServerLevel)summoner.level()).sendParticles(ParticleTypes.FIREWORK, v.x, summoner.getY() + 1, v.z, 80, 0,0,0, 0.2);
        
	}

	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void containerClose(PlayerContainerEvent.Close event) {
			ServerPlayer player = (ServerPlayer) event.getEntity();
			AbstractContainerMenu openContainer = event.getContainer();
			AbstractContainerMenu playerContainer = player.inventoryMenu;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData != null) {
				if (!openContainer.equals(playerContainer)) {
					openContainer.slots.forEach(slot -> {
						ItemStack stack = slot.getItem();
						if (slot.container != player.getInventory()) {
							if ((Utils.hasKeybladeID(stack) && stack.getItem() instanceof KeybladeItem) || (playerData.getAlignment() != Utils.OrgMember.NONE) && ((stack.getItem() instanceof IOrgWeapon || (playerData.getEquippedWeapon().getItem() == stack.getItem())))) {
								slot.set(ItemStack.EMPTY);
								if (stack.getItem() instanceof IOrgWeapon || (playerData.getAlignment() != Utils.OrgMember.NONE)) {
									Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
									for (ItemStack weapon : weapons) {
										if (ItemStack.isSameItem(weapon, stack)) {
											weapon.setTag(stack.getTag());
											break;
										}
									}
									playerData.setWeaponsUnlocked(weapons);
								}
								openContainer.broadcastChanges();
								player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							}
						}
					});
				}
			}
		}

		@SubscribeEvent
		public static void dropItem(ItemTossEvent event) {
			ItemStack droppedItem = event.getEntity().getItem();
			//If it doesn't have an ID it was not summoned unless it's an org weapon
			Player player = event.getPlayer();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData != null) {
				if (droppedItem != null) {
					if (playerData.getEquippedWeapon() != null) {
						if ((droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE) || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem()))) {
							if(droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE)) { //If weapon is org weapon || a keyblade && org member
								Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
								for(ItemStack weapon : weapons) {
									if(ItemStack.isSameItem(weapon, droppedItem)) {
										weapon.setTag(droppedItem.getTag());
										break;
									}
								}
								playerData.setWeaponsUnlocked(weapons);
							}
							player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							event.setCanceled(true);
							return;
						}
					}
					if ((Utils.hasKeybladeID(droppedItem) && droppedItem.getItem() instanceof KeybladeItem)) {
						player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						event.setCanceled(true);
					}
				}
			}
		}
	}

}
