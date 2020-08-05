package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.PortalData;

public class SCSyncCapabilityPacket {

	int level = 0;
	private int exp = 0;
	private int expGiven = 0;
	private int strength = 0;
	private int magic = 0;
	private int defense = 0;
	private int maxHp, ap, maxAP;
	private int munny = 0;
	private int antipoints = 0;

	private double MP, maxMP, dp, maxDP, fp;
	
	private boolean recharge;

	List<String> messages, dfMessages;
	
    PortalData[] orgPortalCoords = {new PortalData((byte)0,0,0,0,0),new PortalData((byte)0,0,0,0,0),new PortalData((byte)0,0,0,0,0)};

    List<String> recipesList = new ArrayList<String>();
    List<String> magicList = new ArrayList<String>();
	LinkedHashMap<String,int[]> driveFormsMap = new LinkedHashMap<String,int[]>();
	LinkedHashMap<String,int[]> abilitiesMap = new LinkedHashMap<String,int[]>();
	List<String> partiesList = new ArrayList<String>(10);

	public SCSyncCapabilityPacket() {
	}

	public SCSyncCapabilityPacket(IPlayerCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength();
		this.magic = capability.getMagic();
		this.defense = capability.getDefense();
		
		this.MP = capability.getMP();
		this.maxMP = capability.getMaxMP();
		this.recharge = capability.getRecharge();
		this.maxHp = capability.getMaxHP();
		// this.choice1 = capability.getChoice1();
		this.ap = capability.getConsumedAP();
		this.maxAP = capability.getMaxAP();
		this.dp = capability.getDP();
		this.maxDP = capability.getMaxDP();
		this.fp = capability.getFP();
		this.antipoints=capability.getAntiPoints();
		this.munny = capability.getMunny();
		
		for(byte i=0;i<3;i++) {
        	this.orgPortalCoords[i] = capability.getPortalCoords((byte)i);
        }
		
		this.recipesList = capability.getKnownRecipesList();
		this.magicList = capability.getMagicsList();
		this.driveFormsMap = capability.getDriveFormsMap();
		this.abilitiesMap = capability.getAbilitiesMap();
		this.partiesList = capability.getPartiesInvited();
		
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
		buffer.writeInt(this.maxHp);
		// buffer.writeString(this.choice1);
		buffer.writeInt(this.ap);
		buffer.writeInt(this.maxAP);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.maxDP);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.munny);
		
		for(byte i=0;i<3;i++) {
        	buffer.writeByte(this.orgPortalCoords[i].getPID());
        	buffer.writeDouble(this.orgPortalCoords[i].getX());
        	buffer.writeDouble(this.orgPortalCoords[i].getY());
        	buffer.writeDouble(this.orgPortalCoords[i].getZ());
        	buffer.writeInt(this.orgPortalCoords[i].getDimID());
        }
		
		CompoundNBT recipes = new CompoundNBT();
		Iterator<String> recipesIt = recipesList.iterator();
		while (recipesIt.hasNext()) {
			String r = recipesIt.next();
			recipes.putInt(r, 1);
		}
		buffer.writeCompoundTag(recipes);

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
		
		CompoundNBT abilities = new CompoundNBT();
		Iterator<Map.Entry<String, int[]>> abilitiesIt = abilitiesMap.entrySet().iterator();
		while (abilitiesIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) abilitiesIt.next();
			abilities.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeCompoundTag(abilities);
		
		buffer.writeInt(partiesList.size());
		for(int i=0;i<partiesList.size();i++) {
			buffer.writeInt(this.partiesList.get(i).length());
			buffer.writeString(this.partiesList.get(i));
		}
		
		buffer.writeInt(messages.size());
		buffer.writeInt(dfMessages.size());

		for (int i = 0; i < this.messages.size(); i++) {
			buffer.writeString(this.messages.get(i));
		}
		
		for (int i = 0; i < this.dfMessages.size(); i++) {
			buffer.writeString(this.dfMessages.get(i));
		}
	}

	public static SCSyncCapabilityPacket decode(PacketBuffer buffer) {
		SCSyncCapabilityPacket msg = new SCSyncCapabilityPacket();

		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();

		msg.MP = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		msg.recharge = buffer.readBoolean();
		msg.maxHp = buffer.readInt();
		// msg.choice1 = buffer.readString(40);
		msg.ap = buffer.readInt();
		msg.maxAP = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.maxDP = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.munny = buffer.readInt();

		for(byte i=0;i<3;i++) {
    		msg.orgPortalCoords[i].setPID(buffer.readByte());
    		msg.orgPortalCoords[i].setX(buffer.readDouble());
    		msg.orgPortalCoords[i].setY(buffer.readDouble());
    		msg.orgPortalCoords[i].setZ(buffer.readDouble());
    		msg.orgPortalCoords[i].setDimID(buffer.readInt());
        }
		
		CompoundNBT recipesTag = buffer.readCompoundTag();
		Iterator<String> recipesIt = recipesTag.keySet().iterator();
		while (recipesIt.hasNext()) {
			String key = (String) recipesIt.next();
			msg.recipesList.add(key);
		}
		
		CompoundNBT magicsTag = buffer.readCompoundTag();
		Iterator<String> magicsIt = magicsTag.keySet().iterator();
		while (magicsIt.hasNext()) {
			String key = (String) magicsIt.next();
			msg.magicList.add(key);
		}
		
		CompoundNBT driveFormsTag = buffer.readCompoundTag();
		Iterator<String> driveFormsIt = driveFormsTag.keySet().iterator();
		while (driveFormsIt.hasNext()) {
			String driveFormName = (String) driveFormsIt.next();
			msg.driveFormsMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
		}
		
		CompoundNBT abilitiesTag = buffer.readCompoundTag();
		Iterator<String> abilitiesIt = abilitiesTag.keySet().iterator();
		while (abilitiesIt.hasNext()) {
			String abilityName = (String) abilitiesIt.next();
			msg.abilitiesMap.put(abilityName, abilitiesTag.getIntArray(abilityName));
		}
		
		int amount = buffer.readInt();
		msg.partiesList = new ArrayList<String>();

		for(int i=0;i<amount;i++) {
			int length = buffer.readInt();
			msg.partiesList.add(buffer.readString(length));
		}
		
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

	public static void handle(final SCSyncCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			IPlayerCapabilities props = ModCapabilities.get(KingdomKeys.proxy.getClientPlayer());
			
			props.setLevel(message.level);
			props.setExperience(message.exp);
			props.setExperienceGiven(message.expGiven);
			props.setStrength(message.strength);
			props.setMagic(message.magic);
			props.setDefense(message.defense);
			props.setMP(message.MP);
			props.setMaxMP(message.maxMP);
			props.setRecharge(message.recharge);
			props.setMaxHP(message.maxHp);
			props.setConsumedAP(message.ap);
			props.setMaxAP(message.maxAP);
			props.setDP(message.dp);
			props.setFP(message.fp);
			props.setMaxDP(message.maxDP);
			props.setMunny(message.munny);
			props.setMessages(message.messages);
			props.setDFMessages(message.dfMessages);
			
			props.setPortalCoords((byte)0, message.orgPortalCoords[0]);
			props.setPortalCoords((byte)1, message.orgPortalCoords[1]);
			props.setPortalCoords((byte)2, message.orgPortalCoords[2]);

			props.setKnownRecipesList(message.recipesList);
			props.setMagicsList(message.magicList);
			props.setDriveFormsMap(message.driveFormsMap);
			props.setAbilitiesMap(message.abilitiesMap);
			props.setAntiPoints(message.antipoints);
			props.setPartiesInvited(message.partiesList);
		});
		ctx.get().setPacketHandled(true);
	}

}
