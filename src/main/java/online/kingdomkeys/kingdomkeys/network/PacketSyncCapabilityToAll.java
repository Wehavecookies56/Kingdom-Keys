package online.kingdomkeys.kingdomkeys.network;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSyncCapabilityToAll {

	private String name, driveForm;
	private int level = 0,
			exp = 0,
			expGiven = 0,
			strength = 0,
			magic = 0,
			defense = 0,
			reflectTicks = 0,
			antipoints = 0;
	
	private double dp = 0, fp = 0;

	private boolean isGliding = false;
	
	public PacketSyncCapabilityToAll() {
	}

	public PacketSyncCapabilityToAll(String name, IPlayerCapabilities capability) {
		this.name = name;
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
		this.driveForm = capability.getActiveDriveForm();
		this.reflectTicks = capability.getReflectTicks();
		this.fp = capability.getFP();
		this.dp = capability.getDP();
		this.antipoints = capability.getAntiPoints();
		this.isGliding = capability.getIsGliding();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeString(name);
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeInt(this.strength);
		buffer.writeInt(this.magic);
		buffer.writeInt(this.defense);
		buffer.writeString(this.driveForm);
		buffer.writeInt(this.reflectTicks);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		
		buffer.writeBoolean(this.isGliding);
	}

	public static PacketSyncCapabilityToAll decode(PacketBuffer buffer) {
		PacketSyncCapabilityToAll msg = new PacketSyncCapabilityToAll();
		msg.name = buffer.readString();
		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		msg.driveForm = buffer.readString();
		msg.reflectTicks = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		
		msg.isGliding = buffer.readBoolean();
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
				LazyOptional<IPlayerCapabilities> props = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES);
				props.ifPresent(cap -> cap.setLevel(message.level));
				props.ifPresent(cap -> cap.setExperience(message.exp));
				props.ifPresent(cap -> cap.setExperienceGiven(message.expGiven));
				props.ifPresent(cap -> cap.setStrength(message.strength));
				props.ifPresent(cap -> cap.setMagic(message.magic));
				props.ifPresent(cap -> cap.setDefense(message.defense));
				props.ifPresent(cap -> cap.setActiveDriveForm(message.driveForm));
				props.ifPresent(cap -> cap.setReflectTicks(message.reflectTicks));
				props.ifPresent(cap -> cap.setDP(message.dp));
				props.ifPresent(cap -> cap.setFP(message.fp));
				props.ifPresent(cap -> cap.setAntiPoints(message.antipoints));
				
				props.ifPresent(cap -> cap.setIsGliding(message.isGliding));
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
