package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.EquipmentEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenEquipmentScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSSwapKeyblade() implements Packet {

    public static final Type<CSSwapKeyblade> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_swap_keyblade"));

    public static final StreamCodec<FriendlyByteBuf, CSSwapKeyblade> STREAM_CODEC = StreamCodec.of((pBuffer, pValue) -> {}, pBuffer -> new CSSwapKeyblade());

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        Utils.summonKeyblade(player, true, DriveForm.NONE);

        //Get the prev keyblade from the NONE form
        ItemStack prevKB = playerData.getEquippedKeychain(DriveForm.NONE);

        //Swap it cycling through all the forms
        playerData.equipKeychain(DriveForm.NONE, playerData.getEquippedKeychain(DriveForm.KB2));
        playerData.equipKeychain(DriveForm.KB2, playerData.getEquippedKeychain(DriveForm.KB3));
        playerData.equipKeychain(DriveForm.KB3, prevKB);

        Utils.summonKeyblade(player, false, DriveForm.NONE);
        PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
