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
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

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

	/*public CSSummonKeyblade(ResourceLocation formToSummonFrom, boolean forceDesummon) {
		this.formToSummonFrom = formToSummonFrom;
		hasForm = true;
		this.forceDesummon = forceDesummon;
		alignment = OrgMember.NONE;
	}*/
	
	public CSSummonKeyblade(boolean forceDesummon, OrgMember alignment) {
		hasForm = false;
		this.forceDesummon = forceDesummon;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(forceDesummon);
		buffer.writeBoolean(hasForm);
		if (formToSummonFrom != null)
			buffer.writeResourceLocation(formToSummonFrom);
	}

	public static CSSummonKeyblade decode(FriendlyByteBuf buffer) {
		CSSummonKeyblade msg = new CSSummonKeyblade();
		msg.forceDesummon = buffer.readBoolean();
		msg.hasForm = buffer.readBoolean();
		if (msg.hasForm)
			msg.formToSummonFrom = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSummonKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			Utils.summonKeyblade(player,message.forceDesummon, message.formToSummonFrom);
		});
		ctx.get().setPacketHandled(true);
	}

	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void containerClose(PlayerContainerEvent.Close event) {
			ServerPlayer player = (ServerPlayer) event.getEntity();
			AbstractContainerMenu openContainer = event.getContainer();
			AbstractContainerMenu playerContainer = player.inventoryMenu;
			IPlayerData playerData = ModData.getPlayer(player);
			if (playerData != null) {
				if (!openContainer.equals(playerContainer)) {
					openContainer.slots.forEach(slot -> {
						ItemStack stack = slot.getItem();
						if (!(stack.getItem() instanceof IKeychain)) {
							if (slot.container != player.getInventory()) {
								if ((Utils.hasKeybladeID(stack))) {
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
			IPlayerData playerData = ModData.getPlayer(player);
			if(playerData != null) {
				if (droppedItem != null) {
					if (!(droppedItem.getItem() instanceof IKeychain)) {
						if ((Utils.hasKeybladeID(droppedItem))) {
							if (playerData.getEquippedWeapon() != null) {
								if ((droppedItem.getItem() instanceof IOrgWeapon || (droppedItem.getItem() instanceof KeybladeItem && playerData.getAlignment() != OrgMember.NONE) || (playerData.getEquippedWeapon().getItem() == droppedItem.getItem()))) {
									Set<ItemStack> weapons = playerData.getWeaponsUnlocked();
									for (ItemStack weapon : weapons) {
										if (ItemStack.isSameItem(weapon, droppedItem)) {
											weapon.setTag(droppedItem.getTag());
											break;
										}
									}
									playerData.setWeaponsUnlocked(weapons);

								}
							}
							player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

}
