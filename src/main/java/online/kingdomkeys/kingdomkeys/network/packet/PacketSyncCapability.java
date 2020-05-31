package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSyncCapability {

	int level = 0;
	private int exp = 0;
	private int expGiven = 0;
	private int strength = 0;
	private int magic = 0;
	private int defense = 0;
	private int hp, ap, maxAP;
	private int munny = 0;
	private int antipoints = 0;

	private double MP, maxMP, dp, maxDP, fp;
	
	private boolean recharge;

	List<String> messages, dfMessages;
	
	List<String> magicList = new ArrayList<String>();
	Map<String,int[]> driveFormsMap = new HashMap<String,int[]>();

	public PacketSyncCapability() {
	}

	public PacketSyncCapability(IPlayerCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
		
		this.MP = capability.getMP();
		this.maxMP = capability.getMaxMP();
		this.recharge = capability.getRecharge();
		this.hp = capability.getMaxHP();
		// this.choice1 = capability.getChoice1();
		this.ap = capability.getConsumedAP();
		this.maxAP = capability.getMaxAP();
		this.dp = capability.getDP();
		this.maxDP = capability.getMaxDP();
		this.fp = capability.getFP();
		this.antipoints=capability.getAntiPoints();
		this.munny = capability.getMunny();
		
		this.magicList = capability.getMagicsList();
		this.driveFormsMap = capability.getDriveFormsMap();
		
		this.messages = capability.getMessages();
		this.dfMessages = capability.getDFMessages();
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
		buffer.writeBoolean(this.recharge);
		buffer.writeInt(this.hp);
		// buffer.writeString(this.choice1);
		buffer.writeInt(this.ap);
		buffer.writeInt(this.maxAP);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.maxDP);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.munny);

		CompoundNBT magics = new CompoundNBT();
		Iterator<String> magicsIt = magicList.iterator();
		while (magicsIt.hasNext()) {
			String m = magicsIt.next();
			magics.putInt(m, 1);
		}
		buffer.writeCompoundTag(magics);
		
		CompoundNBT forms = new CompoundNBT();
		Iterator<Map.Entry<String, int[]>> driveFormsIt = driveFormsMap.entrySet().iterator();
		while (driveFormsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
			forms.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeCompoundTag(forms);

		
		buffer.writeInt(messages.size());
		buffer.writeInt(dfMessages.size());

		for (int i = 0; i < this.messages.size(); i++) {
			buffer.writeString(this.messages.get(i));
		}
		
		for (int i = 0; i < this.dfMessages.size(); i++) {
			buffer.writeString(this.dfMessages.get(i));
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
		msg.recharge = buffer.readBoolean();
		msg.hp = buffer.readInt();
		// msg.choice1 = buffer.readString(40);
		msg.ap = buffer.readInt();
		msg.maxAP = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.maxDP = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.munny = buffer.readInt();

		CompoundNBT magicsTag = buffer.readCompoundTag();
		Iterator<String> it = magicsTag.keySet().iterator();
		while (it.hasNext()) {
			//System.out.println(it);
			String key = (String) it.next();
//			System.out.println("ReadPacket: "+key+" value: "+value);
			msg.magicList.add(key);
			/*if (properties.getCompound("drive_forms").getInt(key) == 0 && key.toString() != null)
				instance.getDriveFormsMap().remove(key.toString());*/
		}
		
		CompoundNBT driveFormsTag = buffer.readCompoundTag();
		Iterator<String> driveFormsIt = driveFormsTag.keySet().iterator();
		while (driveFormsIt.hasNext()) {
			//System.out.println(it);
			String driveFormName = (String) driveFormsIt.next();
//			System.out.println("ReadPacket: "+key+" value: "+value);
			msg.driveFormsMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
			/*if (properties.getCompound("drive_forms").getInt(key) == 0 && key.toString() != null)
				instance.getDriveFormsMap().remove(key.toString());*/
		}
		//System.out.println(msg.driveFormsMap);
		
		int msgSize = buffer.readInt();
		int dfMsgSize = buffer.readInt();
		
		msg.messages = new ArrayList<String>();
		for(int i = 0;i<msgSize;i++) {
			msg.messages.add(buffer.readString(100));
		}
		
		msg.dfMessages = new ArrayList<String>();
		for(int i = 0;i<dfMsgSize;i++) {
			msg.dfMessages.add(buffer.readString(100));
		}
		
		return msg;
	}

	public static void handle(final PacketSyncCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			LazyOptional<IPlayerCapabilities> props = Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_CAPABILITIES);
			props.ifPresent(cap -> cap.setLevel(message.level));
			props.ifPresent(cap -> cap.setExperience(message.exp));
			props.ifPresent(cap -> cap.setExperienceGiven(message.expGiven));
			props.ifPresent(cap -> cap.setStrength(message.strength));
			props.ifPresent(cap -> cap.setMagic(message.magic));
			props.ifPresent(cap -> cap.setDefense(message.defense));
			props.ifPresent(cap -> cap.setMP(message.MP));
			props.ifPresent(cap -> cap.setMaxMP(message.maxMP));
			props.ifPresent(cap -> cap.setRecharge(message.recharge));
			props.ifPresent(cap -> cap.setMaxHP(message.hp));
			props.ifPresent(cap -> cap.setConsumedAP(message.ap));
			props.ifPresent(cap -> cap.setMaxAP(message.maxAP));
			props.ifPresent(cap -> cap.setDP(message.dp));
			props.ifPresent(cap -> cap.setFP(message.fp));
			props.ifPresent(cap -> cap.setMaxDP(message.maxDP));
			props.ifPresent(cap -> cap.setMunny(message.munny));
			props.ifPresent(cap -> cap.setMessages(message.messages));
			props.ifPresent(cap -> cap.setDFMessages(message.dfMessages));
			props.ifPresent(cap -> cap.setMagicsList(message.magicList));
			props.ifPresent(cap -> cap.setDriveFormsMap(message.driveFormsMap));
			props.ifPresent(cap -> cap.setAntiPoints(message.antipoints));
		});
		ctx.get().setPacketHandled(true);
	}

}
