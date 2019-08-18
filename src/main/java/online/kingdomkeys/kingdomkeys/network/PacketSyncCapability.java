package online.kingdomkeys.kingdomkeys.network;

import java.util.ArrayList;
import java.util.List;
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
	private int hp, ap, maxAP;

	private double MP, maxMP;

	List<String> messages;

	public PacketSyncCapability() {
	}

	public PacketSyncCapability(ILevelCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
		
		this.MP = capability.getMP();
		this.maxMP = capability.getMaxMP();
		this.hp = capability.getHP();
		// this.choice1 = capability.getChoice1();
		this.ap = capability.getConsumedAP();
		this.maxAP = capability.getMaxAP();
		this.messages = capability.getMessages();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeInt(this.strength);
		buffer.writeInt(this.magic);
		buffer.writeInt(this.defense);

		buffer.writeDouble(this.MP);
		buffer.writeDouble(this.maxMP);
		buffer.writeInt(this.hp);
		// buffer.writeString(this.choice1);
		buffer.writeInt(this.ap);
		buffer.writeInt(this.maxAP);
		for (int i = 0; i < this.messages.size(); i++) {
			buffer.writeString(this.messages.get(i));
		}
	}

	public static PacketSyncCapability decode(PacketBuffer buffer) {
		PacketSyncCapability msg = new PacketSyncCapability();

		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();

		msg.MP = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		msg.hp = buffer.readInt();
		// msg.choice1 = buffer.readString(40);
		msg.ap = buffer.readInt();
		msg.maxAP = buffer.readInt();
		msg.messages = new ArrayList<String>();
		while (buffer.isReadable()) {
			msg.messages.add(buffer.readString(100));
		}
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
			props.ifPresent(cap -> cap.setMP(message.MP));
			props.ifPresent(cap -> cap.setMaxMP(message.maxMP));
			props.ifPresent(cap -> cap.setHP(message.hp));
			props.ifPresent(cap -> cap.setConsumedAP(message.ap));
			props.ifPresent(cap -> cap.setMaxAP(message.maxAP));
			props.ifPresent(cap -> cap.setMessages(message.messages));
		});
		ctx.get().setPacketHandled(true);
	}

}
