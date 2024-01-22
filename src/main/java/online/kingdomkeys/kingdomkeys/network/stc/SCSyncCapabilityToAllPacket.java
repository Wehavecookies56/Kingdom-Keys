package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;

public class SCSyncCapabilityToAllPacket {
	public Map<Integer, ItemStack> kbArmors = new HashMap<>();

	private String name, driveForm;
	private int level = 0,
			exp = 0,
			expGiven = 0,
			strength = 0,
			magic = 0,
			defense = 0,
			reflectTicks = 0,
			reflectLevel = 0,
			antipoints = 0,
			maxHP = 20;
	
	double mp = 0, maxMP = 0;
	
	LinkedHashMap<String,int[]> driveFormMap = new LinkedHashMap<String,int[]>();
	LinkedHashMap<String,int[]> magicsMap = new LinkedHashMap<String,int[]>();

	private double dp = 0, fp = 0;

	private int aerialDodgeTicks = 0;
	private boolean isGliding = false, hasJumpedAD = false;
	
	private int armorColor = 0, notifColor = 0;
	private boolean armorGlint = true;

	private SingleChoices singleStyle;
	private DualChoices dualStyle;
		
	public SCSyncCapabilityToAllPacket() {
	}

	public SCSyncCapabilityToAllPacket(String name, IPlayerCapabilities capability) {
		this.name = name;
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength(false);
		this.magic = capability.getMagic(false);
		this.defense = capability.getDefense(false);
		this.driveForm = capability.getActiveDriveForm();
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
		
		this.kbArmors = capability.getEquippedKBArmors();
		this.notifColor = capability.getNotifColor();
		this.armorColor = capability.getArmorColor();
		this.armorGlint = capability.getArmorGlint();

		this.singleStyle = capability.getSingleStyle();
		this.dualStyle = capability.getDualStyle();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(name);
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeInt(this.strength);
		buffer.writeInt(this.magic);
		buffer.writeInt(this.defense);
		buffer.writeUtf(this.driveForm);
		buffer.writeInt(this.reflectTicks);
		buffer.writeInt(this.reflectLevel);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.maxHP);
		buffer.writeDouble(this.mp);
		buffer.writeDouble(this.maxMP);
		
		CompoundTag magics = new CompoundTag();
		Iterator<Map.Entry<String, int[]>> magicsIt = magicsMap.entrySet().iterator();
		while (magicsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) magicsIt.next();
			magics.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(magics);
		
		CompoundTag forms = new CompoundTag();
		Iterator<Map.Entry<String, int[]>> driveFormsIt = driveFormMap.entrySet().iterator();
		while (driveFormsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
			forms.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(forms);
		
		buffer.writeBoolean(this.isGliding);
		buffer.writeInt(this.aerialDodgeTicks);
		buffer.writeBoolean(this.hasJumpedAD);
		
		CompoundTag kbArmors = new CompoundTag();
		this.kbArmors.forEach((key, value) -> kbArmors.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(kbArmors);
		
		buffer.writeInt(notifColor);
		buffer.writeInt(armorColor);
		buffer.writeBoolean(armorGlint);

		buffer.writeUtf(singleStyle.toString(), 20);
		buffer.writeUtf(dualStyle.toString(), 20);
	}

	public static SCSyncCapabilityToAllPacket decode(FriendlyByteBuf buffer) {
		SCSyncCapabilityToAllPacket msg = new SCSyncCapabilityToAllPacket();
		msg.name = buffer.readUtf();
		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		msg.driveForm = buffer.readUtf();
		msg.reflectTicks = buffer.readInt();
		msg.reflectLevel = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.maxHP = buffer.readInt();
		msg.mp = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		
		CompoundTag magicsTag = buffer.readNbt();
		Iterator<String> magicsIt = magicsTag.getAllKeys().iterator();
		while (magicsIt.hasNext()) {
			String magicName = (String) magicsIt.next();
			msg.magicsMap.put(magicName, magicsTag.getIntArray(magicName));
		}
		
		CompoundTag driveFormsTag = buffer.readNbt();
		Iterator<String> driveFormsIt = driveFormsTag.getAllKeys().iterator();
		while (driveFormsIt.hasNext()) {
			String driveFormName = (String) driveFormsIt.next();
			msg.driveFormMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
		}
		
		msg.isGliding = buffer.readBoolean();
		msg.aerialDodgeTicks = buffer.readInt();
		msg.hasJumpedAD = buffer.readBoolean();
		
		CompoundTag kbArmorsNBT = buffer.readNbt();
		kbArmorsNBT.getAllKeys().forEach(key -> msg.kbArmors.put(Integer.parseInt(key), ItemStack.of((CompoundTag) kbArmorsNBT.get(key))));
		
		msg.notifColor = buffer.readInt();
		msg.armorColor = buffer.readInt();
		msg.armorGlint = buffer.readBoolean();

		msg.singleStyle = SingleChoices.valueOf(buffer.readUtf(20));
		msg.dualStyle = DualChoices.valueOf(buffer.readUtf(20));
		return msg;
	}

	public static void handle(final SCSyncCapabilityToAllPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			List<AbstractClientPlayer> list = Minecraft.getInstance().level.players();
			Player player = null;
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
				
                playerData.equipAllKBArmor(message.kbArmors, false);
                playerData.setNotifColor(message.notifColor);
                playerData.setArmorColor(message.armorColor);
				playerData.setArmorGlint(message.armorGlint);

				playerData.setSingleStyle(message.singleStyle);
				playerData.setDualStyle(message.dualStyle);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
