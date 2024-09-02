package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSUnlockEquipOrgWeapon(ItemStack weapon, int cost, boolean unlock) implements Packet {

    public static final Type<CSUnlockEquipOrgWeapon> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_unlock_equip_org_weapon"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CSUnlockEquipOrgWeapon> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CSUnlockEquipOrgWeapon::weapon,
            ByteBufCodecs.INT,
            CSUnlockEquipOrgWeapon::cost,
            ByteBufCodecs.BOOL,
            CSUnlockEquipOrgWeapon::unlock,
            CSUnlockEquipOrgWeapon::new
    );

    //Unlock
    public CSUnlockEquipOrgWeapon(ItemStack weapon, int cost) {
        this(weapon, cost, true);
    }

    //Equip
    public CSUnlockEquipOrgWeapon(ItemStack weapon) {
        this(weapon, 0, false);
    }

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        if (unlock) {
            if (playerData.getHearts() >= cost) {
                weapon.set(ModComponents.KEYBLADE_ID, new KeybladeItem.KeybladeID(UUID.randomUUID()));
                playerData.unlockWeapon(weapon);
                playerData.removeHearts(cost);
            }
        } else {
            if (playerData.isWeaponUnlocked(weapon.getItem())) {
                playerData.getWeaponsUnlocked().forEach(itemStack -> {
                    if (itemStack.is(weapon.getItem())) {
                        if (!NeoForge.EVENT_BUS.post(new EquipmentEvent.OrgWeapon(player, playerData.getEquippedWeapon(), itemStack)).isCanceled()) {
                            playerData.equipWeapon(itemStack);
                        }
                    }
                });
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
