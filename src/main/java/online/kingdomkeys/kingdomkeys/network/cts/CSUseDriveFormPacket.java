package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.DriveFormCastEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSUseDriveFormPacket(ResourceLocation form) implements Packet {

	public static final Type<CSUseDriveFormPacket> TYPE = new Type<>(KingdomKeys.rl("cs_use_drive_form"));

	public static final StreamCodec<FriendlyByteBuf, CSUseDriveFormPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			CSUseDriveFormPacket::form,
			CSUseDriveFormPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

        if (NeoForge.EVENT_BUS.post(new DriveFormCastEvent(player, form)).isCanceled())
            return;

		if (form.equals(ModDriveForms.ANTI.location())) { //If target is antiform
			DriveForm form = ModDriveForms.ANTI.get();
			form.initDrive(player);
		} else { //if target is a normal form or revert
			if (!playerData.noFormActive() && form.equals(DriveForm.NONE)) { // If is in a drive form and the target is "" (player)
				DriveForm form = ModDriveForms.registry.get(playerData.getActiveDriveForm());
				form.endDrive(player);
			} else if (!form.equals(DriveForm.NONE)) { // If is not in a form and wants to drive
				DriveForm form = ModDriveForms.registry.get(this.form);
				form.initDrive(player);
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
