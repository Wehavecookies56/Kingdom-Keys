package online.kingdomkeys.kingdomkeys.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSyncCapability {

	int level = 0;
	private int exp = 0;
	private int expGiven = 0;
	private int strength = 0;
	private int magic = 0;
	private int defense = 0;

	public PacketSyncCapability() {
	}

	public PacketSyncCapability(ILevelCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeInt(this.strength);
		buffer.writeInt(this.magic);
		buffer.writeInt(this.defense);
	}

	public static PacketSyncCapability decode(PacketBuffer buffer) {
		PacketSyncCapability msg = new PacketSyncCapability();
		
		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		
		return msg;
	}

	public static void handle(final PacketSyncCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<ILevelCapabilities> props = Minecraft.getInstance().player.getCapability(ModCapabilities.LEVEL_CAPABILITIES);
			props.ifPresent(cap -> cap.setLevel(message.level));
			props.ifPresent(cap -> cap.setExperience(message.exp));
			props.ifPresent(cap -> cap.setExperienceGiven(message.expGiven));
			props.ifPresent(cap -> cap.setStrength(message.strength));
			props.ifPresent(cap -> cap.setMagic(message.magic));
			props.ifPresent(cap -> cap.setDefense(message.defense));
		});
		ctx.get().setPacketHandled(true);
	}

}
