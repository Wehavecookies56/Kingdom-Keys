package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SCSyncCapabilityPacket {

	int level = 0;
	private int exp = 0;
	private int expGiven = 0;
	private int strength = 0, boostStr = 0;
	private int magic = 0, boostMag = 0;
	private int defense = 0, boostDef = 0;
	private int maxHp, maxAP=0, boostMaxAP = 0;
	private int munny = 0;
	private int antipoints = 0;

	private double MP, maxMP, dp, maxDP, fp, focus, maxFocus;
	
	private boolean recharge;

	List<String> messages, dfMessages;
	String driveForm;

	List<ResourceLocation> recipeList = new ArrayList<>();
	LinkedHashMap<String,int[]> magicsMap = new LinkedHashMap<>();
    List<String> shotlockList = new ArrayList<>();
    List<String> reactionList = new ArrayList<>();
    String equippedShotlock;
	LinkedHashMap<String,int[]> driveFormMap = new LinkedHashMap<>();
	LinkedHashMap<String,int[]> abilityMap = new LinkedHashMap<>();
	List<String> partyList = new ArrayList<>(10);
	TreeMap<String, Integer> materialMap = new TreeMap<>();
	Map<ResourceLocation, ItemStack> keychains = new HashMap<>();
	Map<Integer, ItemStack> items = new HashMap<>();
	
	SoAState soAstate, choice, sacrifice;
	BlockPos choicePedestal, sacrificePedestal;
	Vec3 returnPos;
	ResourceKey<Level> returnDim;

	int hearts;
	Utils.OrgMember alignment;
	ItemStack equippedWeapon;
	Set<ItemStack> unlocks;
	int limitCooldownTicks;
	int magicCooldownTicks;
	
	LinkedHashMap<Integer,String> shortcutsMap = new LinkedHashMap<>();

	
	public SCSyncCapabilityPacket() {
	}

	public SCSyncCapabilityPacket(IPlayerCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrength(false);
		this.magic = capability.getMagic(false);
		this.defense = capability.getDefense(false);
		
		this.MP = capability.getMP();
		this.maxMP = capability.getMaxMP();
		this.recharge = capability.getRecharge();
		this.maxHp = capability.getMaxHP();
		this.maxAP = capability.getMaxAP(false);
		this.dp = capability.getDP();
		this.maxDP = capability.getMaxDP();
		this.fp = capability.getFP();
		this.antipoints=capability.getAntiPoints();
		this.munny = capability.getMunny();
		this.focus = capability.getFocus();
		this.maxFocus = capability.getMaxFocus();
		
		this.recipeList = capability.getKnownRecipeList();
		this.magicsMap = capability.getMagicsMap();
		this.shotlockList = capability.getShotlockList();
		this.equippedShotlock = capability.getEquippedShotlock();
		this.driveFormMap = capability.getDriveFormMap();
		this.abilityMap = capability.getAbilityMap();
		this.partyList = capability.getPartiesInvited();
		this.materialMap = capability.getMaterialMap();
		this.keychains = capability.getEquippedKeychains();
		this.items = capability.getEquippedItems();
		
		this.messages = capability.getMessages();
		this.dfMessages = capability.getDFMessages();
		this.soAstate = capability.getSoAState();
		this.choice = capability.getChosen();
		this.choicePedestal = capability.getChoicePedestal();
		this.sacrifice = capability.getSacrificed();
		this.sacrificePedestal = capability.getSacrificePedestal();
		this.driveForm = capability.getActiveDriveForm();
		this.returnPos = capability.getReturnLocation();
		this.returnDim = capability.getReturnDimension();

		this.hearts = capability.getHearts();
		this.alignment = capability.getAlignment();
		this.equippedWeapon = capability.getEquippedWeapon();
		this.unlocks = capability.getWeaponsUnlocked();
		this.limitCooldownTicks = capability.getLimitCooldownTicks();
		this.magicCooldownTicks = capability.getMagicCooldownTicks();
		
		this.reactionList = capability.getReactionCommands();
		
		this.shortcutsMap = capability.getShortcutsMap();
		
		this.boostStr = capability.getBoostStrength();
		this.boostMag = capability.getBoostMagic();
		this.boostDef = capability.getBoostDefense();
		this.boostMaxAP = capability.getBoostMaxAP();

	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeInt(this.strength);
		buffer.writeInt(this.magic);
		buffer.writeInt(this.defense);
		
		buffer.writeInt(this.boostStr);
		buffer.writeInt(this.boostMag);
		buffer.writeInt(this.boostDef);
		
		buffer.writeDouble(this.MP);
		buffer.writeDouble(this.maxMP);
		buffer.writeBoolean(this.recharge);
		buffer.writeInt(this.maxHp);
		buffer.writeInt(this.maxAP);
		buffer.writeInt(this.boostMaxAP);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.maxDP);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.munny);
		buffer.writeDouble(this.focus);
		buffer.writeDouble(this.maxFocus);
		
		CompoundTag recipes = new CompoundTag();
		Iterator<ResourceLocation> recipesIt = recipeList.iterator();
		while (recipesIt.hasNext()) {
			ResourceLocation r = recipesIt.next();
			recipes.putString(r.toString(), r.toString());
		}
		buffer.writeNbt(recipes);

		CompoundTag magics = new CompoundTag();
		Iterator<Map.Entry<String, int[]>> magicsIt = magicsMap.entrySet().iterator();
		while (magicsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) magicsIt.next();
			magics.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(magics);
		
		CompoundTag shotlocks = new CompoundTag();
		Iterator<String> shotlocksIt = shotlockList.iterator();
		while (shotlocksIt.hasNext()) {
			String s = shotlocksIt.next();
			shotlocks.putInt(s, 1);
		}
		buffer.writeNbt(shotlocks);
		
		buffer.writeUtf(this.equippedShotlock, 100);
		
		CompoundTag forms = new CompoundTag();
		Iterator<Map.Entry<String, int[]>> driveFormsIt = driveFormMap.entrySet().iterator();
		while (driveFormsIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
			forms.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(forms);
		
		CompoundTag abilities = new CompoundTag();
		Iterator<Map.Entry<String, int[]>> abilitiesIt = abilityMap.entrySet().iterator();
		while (abilitiesIt.hasNext()) {
			Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) abilitiesIt.next();
			abilities.putIntArray(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(abilities);

		CompoundTag keychains = new CompoundTag();
		this.keychains.forEach((key, value) -> keychains.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(keychains);
		
		CompoundTag items = new CompoundTag();
		this.items.forEach((key, value) -> items.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(items);

		buffer.writeInt(partyList.size());
		for(int i=0;i<partyList.size();i++) {
			buffer.writeInt(this.partyList.get(i).length());
			buffer.writeUtf(this.partyList.get(i));
		}
		
		CompoundTag materials = new CompoundTag();
		Iterator<Map.Entry<String, Integer>> materialsIt = materialMap.entrySet().iterator();
		while (materialsIt.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) materialsIt.next();
			materials.putInt(pair.getKey().toString(), pair.getValue());
			if (materials.getInt(pair.getKey()) == 0 && pair.getKey().toString() != null)
				materials.remove(pair.getKey().toString());
		}
		buffer.writeNbt(materials);
		
		buffer.writeInt(messages.size());
		buffer.writeInt(dfMessages.size());

		for (int i = 0; i < this.messages.size(); i++) {
			buffer.writeUtf(this.messages.get(i));
		}
		
		for (int i = 0; i < this.dfMessages.size(); i++) {
			buffer.writeUtf(this.dfMessages.get(i));
		}
		
		buffer.writeInt(this.driveForm.length());
		buffer.writeUtf(this.driveForm);
		
		buffer.writeResourceLocation(this.returnDim.location());
		buffer.writeDouble(this.returnPos.x);
		buffer.writeDouble(this.returnPos.y);
		buffer.writeDouble(this.returnPos.z);
		buffer.writeByte(this.soAstate.get());
		buffer.writeByte(this.choice.get());
		buffer.writeByte(this.sacrifice.get());
		buffer.writeBlockPos(this.choicePedestal);
		buffer.writeBlockPos(this.sacrificePedestal);

		buffer.writeInt(this.hearts);
		buffer.writeInt(this.alignment.ordinal());
		buffer.writeItem(this.equippedWeapon);
		buffer.writeInt(this.unlocks.size());
		unlocks.forEach(buffer::writeItem);
		buffer.writeInt(this.limitCooldownTicks);
		buffer.writeInt(this.magicCooldownTicks);
		
		buffer.writeInt(reactionList.size());
		for(int i = 0; i < reactionList.size();i++) {
			buffer.writeUtf(reactionList.get(i), 100);
		}
		
		CompoundTag shortcuts = new CompoundTag();
         
		Iterator<Map.Entry<Integer,String>> shortcutsIt = shortcutsMap.entrySet().iterator();
		while (shortcutsIt.hasNext()) {
			Map.Entry<Integer,String> pair = (Map.Entry<Integer,String>) shortcutsIt.next();
			shortcuts.putString(pair.getKey().toString(), pair.getValue());
		}
		buffer.writeNbt(shortcuts);
		
	}

	public static SCSyncCapabilityPacket decode(FriendlyByteBuf buffer) {
		SCSyncCapabilityPacket msg = new SCSyncCapabilityPacket();

		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = buffer.readInt();
		msg.magic = buffer.readInt();
		msg.defense = buffer.readInt();
		msg.boostStr = buffer.readInt();
		msg.boostMag = buffer.readInt();
		msg.boostDef = buffer.readInt();

		msg.MP = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		msg.recharge = buffer.readBoolean();
		msg.maxHp = buffer.readInt();
		// msg.choice1 = buffer.readString(40);
		msg.maxAP = buffer.readInt();
		msg.boostMaxAP = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.maxDP = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.munny = buffer.readInt();
		msg.focus = buffer.readDouble();
		msg.maxFocus = buffer.readDouble();

		CompoundTag recipesTag = buffer.readNbt();
		Iterator<String> recipesIt = recipesTag.getAllKeys().iterator();
		while (recipesIt.hasNext()) {
			String key = (String) recipesIt.next();
			msg.recipeList.add(new ResourceLocation(key));
		}
		
		CompoundTag magicsTag = buffer.readNbt();
		Iterator<String> magicsIt = magicsTag.getAllKeys().iterator();
		while (magicsIt.hasNext()) {
			String magicName = (String) magicsIt.next();
			msg.magicsMap.put(magicName, magicsTag.getIntArray(magicName));
		}
		
		CompoundTag shotlocksTag = buffer.readNbt();
		Iterator<String> shotlocksIt = shotlocksTag.getAllKeys().iterator();
		while (shotlocksIt.hasNext()) {
			String key = (String) shotlocksIt.next();
			msg.shotlockList.add(key);
		}
		
		msg.equippedShotlock = buffer.readUtf(100);
		
		CompoundTag driveFormsTag = buffer.readNbt();
		Iterator<String> driveFormsIt = driveFormsTag.getAllKeys().iterator();
		while (driveFormsIt.hasNext()) {
			String driveFormName = (String) driveFormsIt.next();
			msg.driveFormMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
		}
		
		CompoundTag abilitiesTag = buffer.readNbt();
		Iterator<String> abilitiesIt = abilitiesTag.getAllKeys().iterator();
		while (abilitiesIt.hasNext()) {
			String abilityName = (String) abilitiesIt.next();
			msg.abilityMap.put(abilityName, abilitiesTag.getIntArray(abilityName));
		}

		CompoundTag keychainsNBT = buffer.readNbt();
		keychainsNBT.getAllKeys().forEach(key -> msg.keychains.put(new ResourceLocation(key), ItemStack.of((CompoundTag) keychainsNBT.get(key))));
		
		CompoundTag itemsNBT = buffer.readNbt();
		itemsNBT.getAllKeys().forEach(key -> msg.items.put(Integer.parseInt(key), ItemStack.of((CompoundTag) itemsNBT.get(key))));
		
		int amount = buffer.readInt();
		msg.partyList = new ArrayList<String>();

		for(int i=0;i<amount;i++) {
			int length = buffer.readInt();
			msg.partyList.add(buffer.readUtf(length));
		}
		
		CompoundTag materialsTag = buffer.readNbt();
		Iterator<String> materialsIt = materialsTag.getAllKeys().iterator();
		while (materialsIt.hasNext()) {
			String matName = (String) materialsIt.next();
			msg.materialMap.put(matName, materialsTag.getInt(matName));
		}
		
		int msgSize = buffer.readInt();
		int dfMsgSize = buffer.readInt();
		
		msg.messages = new ArrayList<String>();
		for(int i = 0;i<msgSize;i++) {
			msg.messages.add(buffer.readUtf(100));
		}
		
		msg.dfMessages = new ArrayList<String>();
		for(int i = 0;i<dfMsgSize;i++) {
			msg.dfMessages.add(buffer.readUtf(100));
		}
		
		int length = buffer.readInt();
		msg.driveForm = buffer.readUtf(length);
		
		msg.returnDim = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
		msg.returnPos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		msg.soAstate = SoAState.fromByte(buffer.readByte());
		msg.choice = SoAState.fromByte(buffer.readByte());
		msg.sacrifice = SoAState.fromByte(buffer.readByte());
		msg.choicePedestal = buffer.readBlockPos();
		msg.sacrificePedestal = buffer.readBlockPos();

		msg.hearts = buffer.readInt();
		msg.alignment = Utils.OrgMember.values()[buffer.readInt()];
		msg.equippedWeapon = buffer.readItem();
		msg.unlocks = new HashSet<>();
		int unlockSize = buffer.readInt();
		for (int i = 0; i < unlockSize; ++i) {
			msg.unlocks.add(buffer.readItem());
		}
		msg.limitCooldownTicks = buffer.readInt();
		msg.magicCooldownTicks = buffer.readInt();
		
		int rSize = buffer.readInt();
		for(int i = 0; i < rSize;i++) {
			msg.reactionList.add(buffer.readUtf(100));
		}
		
		CompoundTag shortcutsTag = buffer.readNbt();
		Iterator<String> shortcutsIt = shortcutsTag.getAllKeys().iterator();
		while (shortcutsIt.hasNext()) {
            int shortcutPos = Integer.parseInt(shortcutsIt.next());
            msg.shortcutsMap.put(shortcutPos, shortcutsTag.getString(shortcutPos+""));
		}
		
		return msg;
	}

	public static void handle(final SCSyncCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(KingdomKeys.proxy.getClientPlayer());

			playerData.setLevel(message.level);
			playerData.setExperience(message.exp);
			playerData.setExperienceGiven(message.expGiven);
			playerData.setStrength(message.strength);
			playerData.setMagic(message.magic);
			playerData.setDefense(message.defense);
			playerData.setBoostStrength(message.boostStr);
			playerData.setBoostMagic(message.boostMag);
			playerData.setBoostDefense(message.boostDef);
			playerData.setMP(message.MP);
			playerData.setMaxMP(message.maxMP);
			playerData.setRecharge(message.recharge);
			playerData.setMaxHP(message.maxHp);
			playerData.setMaxAP(message.maxAP);
			playerData.setBoostMaxAP(message.boostMaxAP);
			playerData.setDP(message.dp);
			playerData.setFP(message.fp);
			playerData.setMaxDP(message.maxDP);
			playerData.setMunny(message.munny);
			playerData.setFocus(message.focus);
			playerData.setMaxFocus(message.maxFocus);

			playerData.setMessages(message.messages);
			playerData.setDFMessages(message.dfMessages);

			playerData.setKnownRecipeList(message.recipeList);
			playerData.setMagicsMap(message.magicsMap);
			playerData.setShotlockList(message.shotlockList);
			playerData.setEquippedShotlock(message.equippedShotlock);
			playerData.setDriveFormMap(message.driveFormMap);
			playerData.setAbilityMap(message.abilityMap);
			playerData.setAntiPoints(message.antipoints);
			playerData.setPartiesInvited(message.partyList);
			playerData.setMaterialMap(message.materialMap);
			playerData.equipAllKeychains(message.keychains, false);
			playerData.equipAllItems(message.items, false);
			playerData.setActiveDriveForm(message.driveForm);

			playerData.setReturnDimension(message.returnDim);
			playerData.setReturnLocation(message.returnPos);
			playerData.setSoAState(message.soAstate);
			playerData.setChoice(message.choice);
			playerData.setSacrifice(message.sacrifice);
			playerData.setChoicePedestal(message.choicePedestal);
			playerData.setSacrificePedestal(message.sacrificePedestal);

			playerData.setHearts(message.hearts);
			playerData.setAlignment(message.alignment);
			playerData.equipWeapon(message.equippedWeapon);
			playerData.setWeaponsUnlocked(message.unlocks);
			playerData.setLimitCooldownTicks(message.limitCooldownTicks);
			playerData.setMagicCooldownTicks(message.magicCooldownTicks);
			
			playerData.setReactionCommands(message.reactionList);
			playerData.setShortcutsMap(message.shortcutsMap);
			
			KingdomKeys.proxy.getClientPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(message.maxHp);
		});
		ctx.get().setPacketHandled(true);
	}

}
