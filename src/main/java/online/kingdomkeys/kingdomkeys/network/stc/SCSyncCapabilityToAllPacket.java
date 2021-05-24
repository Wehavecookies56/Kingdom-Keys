package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncCapabilityToAllPacket {

	private String name, driveForm;
	private int level = 0,
			exp = 0,
			expGiven = 0,
			strength = 0,
			magic = 0,
			defense = 0,
			reflectTicks = 0,
			reflectLevel = 0,
			aeroTicks = 0,
			aeroLevel = 0,
			antipoints = 0,
			maxHP = 20;
	
	double mp = 0, maxMP = 0;
	
	LinkedHashMap<String,int[]> driveFormMap = new LinkedHashMap<String,int[]>();
	LinkedHashMap<String,int[]> magicsMap = new LinkedHashMap<String,int[]>();

	private double dp = 0, fp = 0;

	private int aerialDodgeTicks = 0;
	private boolean isGliding = false, hasJumpedAD = false;
		
	public SCSyncCapabilityToAllPacket() {
	}

	public SCSyncCapabilityToAllPacket(String name, IPlayerCapabilities capability) {
		this.name = name;
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
		this.driveForm = capability.getActiveDriveForm();
		this.aeroTicks = capability.getAeroTicks();
		this.aeroLevel = capability.getAeroLevel();
		this.reflectTicks = capability.getReflectTicks();
		this.reflectLevel = capability.getReflectLevel();
		this.fp = capability.getFP();
		this.dp = capability.getDP();
		this.antipoints = capability.getAntiPoints();
		this.maxHP = capability.getMaxHP();
		this.mp = capability.getMP();
		this.maxMP = capability.getMaxMP();
		
        this.magicsMap = capability.getMagicsMap();
		this.driveFormMap = capability.getDriveFormMap();

		this.isGliding = capability.getIsGliding();
		this.aerialDodgeTicks = capability.getAerialDodgeTicks();
		this.hasJumpedAD = capability.hasJumpedAerialDodge();
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
		buffer.writeInt(this.aeroTicks);
		buffer.writeInt(this.aeroLevel);
		buffer.writeInt(this.reflectTicks);
		buffer.writeInt(this.reflectLevel);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.maxHP);
		buffer.writeDouble(this.mp);
		buffer.writeDouble(this.maxMP);
		
		CompoundNBT magics = new CompoundNBT();
		Iterator<Map.Entry<String, int[]>> magicsIt = magicsMap.entrySet().iterator();
		while (magicsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) magicsIt.next();
			magics.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeCompoundTag(magics);
		
		CompoundNBT forms = new CompoundNBT();
		Iterator<Map.Entry<String, int[]>> driveFormsIt = driveFormMap.entrySet().iterator();
		while (driveFormsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
			forms.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeCompoundTag(forms);
		
		buffer.writeBoolean(this.isGliding);
		buffer.writeInt(this.aerialDodgeTicks);
		buffer.writeBoolean(this.hasJumpedAD);
	}

	public static SCSyncCapabilityToAllPacket decode(PacketBuffer buffer) {
		SCSyncCapabilityToAllPacket msg = new SCSyncCapabilityToAllPacket();
		msg.name = buffer.readString();
		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		msg.driveForm = buffer.readString();
		msg.aeroTicks = buffer.readInt();
		msg.aeroLevel = buffer.readInt();
		msg.reflectTicks = buffer.readInt();
		msg.reflectLevel = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.maxHP = buffer.readInt();
		msg.mp = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		
		CompoundNBT magicsTag = buffer.readCompoundTag();
		Iterator<String> magicsIt = magicsTag.keySet().iterator();
		while (magicsIt.hasNext()) {
			String magicName = (String) magicsIt.next();
			msg.magicsMap.put(magicName, magicsTag.getIntArray(magicName));
		}
		
		CompoundNBT driveFormsTag = buffer.readCompoundTag();
		Iterator<String> driveFormsIt = driveFormsTag.keySet().iterator();
		while (driveFormsIt.hasNext()) {
			String driveFormName = (String) driveFormsIt.next();
			msg.driveFormMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
		}
		
		msg.isGliding = buffer.readBoolean();
		msg.aerialDodgeTicks = buffer.readInt();
		msg.hasJumpedAD = buffer.readBoolean();
		return msg;
	}

	public static void handle(final SCSyncCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			List<AbstractClientPlayerEntity> list = Minecraft.getInstance().world.getPlayers();
			PlayerEntity player = null;
			for (int i = 0; i < list.size(); i++) { //Loop through the players
				String name = list.get(i).getName().getString();
				if (name.equals(message.name)) {
					player = list.get(i);
				}
			}
			if (player != null) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				playerData.setLevel(message.level);
				playerData.setExperience(message.exp);
				playerData.setExperienceGiven(message.expGiven);
				playerData.setStrength(message.strength);
				playerData.setMagic(message.magic);
				playerData.setDefense(message.defense);
				playerData.setActiveDriveForm(message.driveForm);
				playerData.setAeroTicks(message.aeroTicks, message.aeroLevel);
				playerData.setReflectTicks(message.reflectTicks, message.reflectLevel);
				playerData.setDP(message.dp);
				playerData.setFP(message.fp);
				playerData.setAntiPoints(message.antipoints);
				playerData.setMaxHP(message.maxHP);
				playerData.setMP(message.mp);
				playerData.setMaxMP(message.maxMP);

				playerData.setMagicsMap(message.magicsMap);
				playerData.setDriveFormMap(message.driveFormMap);

				playerData.setIsGliding(message.isGliding);
				playerData.setAerialDodgeTicks(message.aerialDodgeTicks);
				playerData.setHasJumpedAerialDodge(message.hasJumpedAD);
				
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
