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
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSUseDriveFormPacket(String form) implements Packet {

	public static final Type<CSUseDriveFormPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_drive_form"));

	public static final StreamCodec<FriendlyByteBuf, CSUseDriveFormPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CSUseDriveFormPacket::form,
			CSUseDriveFormPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		if (form.equals(Strings.Form_Anti)) { //If target is antiform
			playerData.setActiveDriveForm(Strings.Form_Anti);
			if(!playerData.isAbilityEquipped(Strings.darkDomination))
				playerData.setDP(0);
			else {
				int cost = ModDriveForms.registry.get(ResourceLocation.parse(Strings.Form_Anti)).getDriveCost();
				playerData.remDP(cost);
			}
			playerData.setFP(1000);
			playerData.setAntiPoints(playerData.getAntiPoints() -4);
			PacketHandler.syncToAllAround(player, playerData);
		} else { //if target is a normal form or revert
			if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && form.equals(DriveForm.NONE.toString())) { // If is in a drive form and the target is "" (player)
				DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
				form.endDrive(player);
				if (!form.getBaseGrowthAbilities()) {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get(ResourceLocation.parse(form.getDFAbilityForLevel(playerData.getDriveFormLevel(form.getName())))), playerData.getDriveFormLevel(form.getName()), player, false));
				}
				for (String abilityLoc : form.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get(ResourceLocation.parse(abilityLoc));
					NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ability, 0, player, false));
				}
			} else if (!form.equals(DriveForm.NONE.toString())) { // If is not in a form and wants to drive
				DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(this.form));
				form.initDrive(player);
				if (!form.getBaseGrowthAbilities()) {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get(ResourceLocation.parse(form.getDFAbilityForLevel(playerData.getDriveFormLevel(form.getName())))), playerData.getDriveFormLevel(form.getName()), player, false));
				}
				for (String abilityLoc : form.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get(ResourceLocation.parse(abilityLoc));
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
				}
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
