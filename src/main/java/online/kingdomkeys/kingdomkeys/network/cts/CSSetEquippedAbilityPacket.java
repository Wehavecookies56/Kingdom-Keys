package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class CSSetEquippedAbilityPacket {

	String ability;
	int level;

	public CSSetEquippedAbilityPacket() {
	}

	public CSSetEquippedAbilityPacket(String ability, int level) {
		this.ability = ability;
		this.level = level;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.ability.length());
		buffer.writeString(this.ability);
		buffer.writeInt(this.level);
	}

	public static CSSetEquippedAbilityPacket decode(PacketBuffer buffer) {
		CSSetEquippedAbilityPacket msg = new CSSetEquippedAbilityPacket();
		int length = buffer.readInt();
		msg.ability = buffer.readString(length);
		msg.level = buffer.readInt();
		return msg;
	}

	public static void handle(CSSetEquippedAbilityPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			//System.out.println("adding/sub " + message.ability + " by " + message.level + " adding: " + newConsumedAP);
			playerData.addEquippedAbilityLevel(message.ability, message.level);
		});
		ctx.get().setPacketHandled(true);
	}

}
