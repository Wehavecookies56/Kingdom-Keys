package online.kingdomkeys.kingdomkeys.packets;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSyncCapabilityToAll {

	private String name;
	private int level = 0,
			exp = 0,
			expGiven = 0,
			strength = 0,
			magic = 0,
			defense = 0;

	public PacketSyncCapabilityToAll() {
	}

	public PacketSyncCapabilityToAll(String name, ILevelCapabilities capability) {
		this.name = name;
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

	public static PacketSyncCapabilityToAll decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAll msg = new PacketSyncCapabilityToAll();
		
		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		
		return msg;
	}

	public static void handle(final PacketSyncCapabilityToAll message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			List<AbstractClientPlayerEntity> list = Minecraft.getInstance().world.getPlayers();
			PlayerEntity player = null;
			for (int i = 0; i < list.size(); i++) { //Loop through the players
				String name = list.get(i).getName().getFormattedText();
				if (name.equals(message.name)) {
					player = list.get(i);
				}
			}
			if (player != null) {
				LazyOptional<ILevelCapabilities> props = player.getCapability(ModCapabilities.LEVEL_CAPABILITIES);
				props.ifPresent(cap -> cap.setLevel(message.level));
				props.ifPresent(cap -> cap.setExperience(message.exp));
				props.ifPresent(cap -> cap.setExperienceGiven(message.expGiven));
				props.ifPresent(cap -> cap.setStrength(message.strength));
				props.ifPresent(cap -> cap.setMagic(message.magic));
				props.ifPresent(cap -> cap.setDefense(message.defense));

			}
		});
		ctx.get().setPacketHandled(true);
	}

}
