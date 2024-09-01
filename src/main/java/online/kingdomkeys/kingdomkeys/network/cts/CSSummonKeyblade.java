package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Set;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public record CSSummonKeyblade(ResourceLocation formToSummonFrom, boolean forceDesummon) implements Packet {

	public static final Type<CSSummonKeyblade> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_summon_keyblade"));

	public static final StreamCodec<FriendlyByteBuf, CSSummonKeyblade> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			CSSummonKeyblade::formToSummonFrom,
			ByteBufCodecs.BOOL,
			CSSummonKeyblade::forceDesummon,
			CSSummonKeyblade::new
	);

	public CSSummonKeyblade() {
		this(DriveForm.NONE, false);
	}

	public CSSummonKeyblade(boolean forceDesummon) {
		this(DriveForm.NONE, forceDesummon);
	}

	//Don't pass none please
	public CSSummonKeyblade(ResourceLocation formToSummonFrom) {
		this(formToSummonFrom, false);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Utils.summonKeyblade(player, forceDesummon, formToSummonFrom);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void containerClose(PlayerContainerEvent.Close event) {
			ServerPlayer player = (ServerPlayer) event.getEntity();
			AbstractContainerMenu openContainer = event.getContainer();
			AbstractContainerMenu playerContainer = player.inventoryMenu;
			PlayerData playerData = PlayerData.get(player);
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
			PlayerData playerData = PlayerData.get(player);
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
