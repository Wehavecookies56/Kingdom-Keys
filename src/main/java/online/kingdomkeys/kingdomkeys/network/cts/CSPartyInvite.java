package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class CSPartyInvite {
	
	String name;
	UUID playerUUID;
	
	public CSPartyInvite() {}

	public CSPartyInvite(Party party, UUID playerUUID) {
		this.name = party.getName();
		this.playerUUID = playerUUID;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
				
		buffer.writeUniqueId(this.playerUUID);
	}

	public static CSPartyInvite decode(PacketBuffer buffer) {
		CSPartyInvite msg = new CSPartyInvite();
		int length = buffer.readInt();
		msg.name = buffer.readString(length);
				
		msg.playerUUID = buffer.readUniqueId();
		return msg;
	}

	public static void handle(CSPartyInvite message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			
			PlayerEntity target = player.world.getPlayerByUuid(message.playerUUID);
			IPlayerCapabilities tProps = ModCapabilities.get(target);
			if(!tProps.getPartiesInvited().contains(message.name)) {
				tProps.addPartiesInvited(message.name);
				
				IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);
				Party p = worldData.getPartyFromName(message.name);
				target.sendMessage(new TranslationTextComponent(TextFormatting.YELLOW+p.getLeader().getUsername()+" has invited you to "+p.getName()));
			}
			
			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(tProps), (ServerPlayerEntity)target);
		});
		ctx.get().setPacketHandled(true);
	}

}
