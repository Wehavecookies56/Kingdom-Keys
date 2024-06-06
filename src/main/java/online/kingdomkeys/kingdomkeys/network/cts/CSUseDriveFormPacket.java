package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class CSUseDriveFormPacket {

	String form;

	public CSUseDriveFormPacket() {
	}

	public CSUseDriveFormPacket(String form) {
		this.form = form;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.form.length());
		buffer.writeUtf(this.form);
	}

	public static CSUseDriveFormPacket decode(FriendlyByteBuf buffer) {
		CSUseDriveFormPacket msg = new CSUseDriveFormPacket();
		int length = buffer.readInt();
		msg.form = buffer.readUtf(length);
		return msg;
	}

	public static void handle(CSUseDriveFormPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			if (message.form.equals(Strings.Form_Anti)) { //If target is antiform
				playerData.setActiveDriveForm(Strings.Form_Anti);
				if(!playerData.isAbilityEquipped(Strings.darkDomination))
					playerData.setDP(0);
				else {
					int cost = ModDriveForms.registry.get().getValue(new ResourceLocation(Strings.Form_Anti)).getDriveCost();
					playerData.remDP(cost);
				}
				playerData.setFP(1000);
				playerData.setAntiPoints(playerData.getAntiPoints() -4);
				PacketHandler.syncToAllAround(player, playerData);
			} else { //if target is a normal form or revert
				if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && message.form.equals(DriveForm.NONE.toString())) { // If is in a drive form and the target is "" (player)
					DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
					form.endDrive(player);
					if (!form.getBaseGrowthAbilities()) {
						MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get().getValue(new ResourceLocation(form.getDFAbilityForLevel(playerData.getDriveFormLevel(form.getName())))), playerData.getDriveFormLevel(form.getName()), player, false));
					}
					for (String abilityLoc : form.getDriveFormData().getAbilities()) {
						Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityLoc));
						MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(ability, 0, player, false));
					}
				} else if (!message.form.equals(DriveForm.NONE.toString())) { // If is not in a form and wants to drive
					DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(message.form));
					form.initDrive(player);
					if (!form.getBaseGrowthAbilities()) {
						MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get().getValue(new ResourceLocation(form.getDFAbilityForLevel(playerData.getDriveFormLevel(form.getName())))), playerData.getDriveFormLevel(form.getName()), player, false));
					}
					for (String abilityLoc : form.getDriveFormData().getAbilities()) {
						Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityLoc));
						MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
					}
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
