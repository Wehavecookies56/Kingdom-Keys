package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.*;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.DualChoices;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.SingleChoices;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SCSyncCapabilityPacket {

	public int level = 0;
	public int exp = 0;
	public int expGiven = 0;
	public Stat strength, magic, defense, maxAP;
	public int maxHp;
	public int munny = 0;
	public int antipoints = 0;

	public double MP, maxMP, dp, maxDP, fp, focus, maxFocus;

	public boolean recharge;

	public List<String> messages, bfMessages, dfMessages;
	public String driveForm;

	public SingleChoices singleStyle = SingleChoices.SORA;
	public DualChoices dualStyle = DualChoices.KH2_ROXAS_DUAL;

	public List<ResourceLocation> recipeList = new ArrayList<>();
	public LinkedHashMap<String,int[]> magicsMap = new LinkedHashMap<>();
	public List<String> shotlockList = new ArrayList<>();
	public List<String> reactionList = new ArrayList<>();
	public String equippedShotlock;
	public LinkedHashMap<String,int[]> driveFormMap = new LinkedHashMap<>();
	public LinkedHashSet<String> visibleDriveForms = new LinkedHashSet<String>();
	public LinkedHashMap<String,int[]> abilityMap = new LinkedHashMap<>();
	public List<String> partyList = new ArrayList<>(10);
	public TreeMap<ResourceLocation, Integer> materialMap = new TreeMap<>();
	public Map<ResourceLocation, ItemStack> keychains = new HashMap<>();
	public Map<Integer, ItemStack> items = new HashMap<>();
	public Map<Integer, ItemStack> accessories = new HashMap<>();
	public Map<Integer, ItemStack> kbArmors = new HashMap<>();
	public Map<Integer, ItemStack> armors = new HashMap<>();
	public int maxAccessories, maxArmors;
	public SoAState soAstate, choice, sacrifice;
	public BlockPos choicePedestal, sacrificePedestal;
	public Vec3 returnPos;
	public ResourceKey<Level> returnDim;

	public int hearts;
	public Utils.OrgMember alignment;
	public ItemStack equippedWeapon;
	public Set<ItemStack> unlocks;
	public int limitCooldownTicks;
	public int magicCasttimeTicks, magicCooldownTicks;

	public LinkedHashMap<Integer,String> shortcutsMap = new LinkedHashMap<>();
	
	public int synthLevel,synthExp;
	private int armorColor = 0;
	private boolean armorGlint = true;
	public boolean respawnROD;

	public Set<String> synthesisedRecipes = new HashSet<>();
	
	public SCSyncCapabilityPacket() {
	}

	public SCSyncCapabilityPacket(IPlayerCapabilities capability) {
		this.level = capability.getLevel();
		this.exp = capability.getExperience();
		this.expGiven = capability.getExperienceGiven();
		this.strength = capability.getStrengthStat();
		this.magic = capability.getMagicStat();
		this.defense = capability.getDefenseStat();
		this.maxAP = capability.getMaxAPStat();

		this.MP = capability.getMP();
		this.maxMP = capability.getMaxMP();
		this.recharge = capability.getRecharge();
		this.maxHp = capability.getMaxHP();
		this.dp = capability.getDP();
		this.maxDP = capability.getMaxDP();
		this.fp = capability.getFP();
		this.antipoints = capability.getAntiPoints();
		this.munny = capability.getMunny();
		this.focus = capability.getFocus();
		this.maxFocus = capability.getMaxFocus();
		
		this.recipeList = new ArrayList<>(capability.getKnownRecipeList());
		this.magicsMap = new LinkedHashMap<>(capability.getMagicsMap());
		this.shotlockList = new ArrayList<>(capability.getShotlockList());
		this.equippedShotlock = capability.getEquippedShotlock();
		this.driveFormMap = new LinkedHashMap<>(capability.getDriveFormMap());
		this.abilityMap = new LinkedHashMap<>(capability.getAbilityMap());
		this.partyList = new ArrayList<>(capability.getPartiesInvited());
		this.materialMap = new TreeMap<>(capability.getMaterialMap());
		this.keychains = capability.getEquippedKeychains();
		this.items = new HashMap<>(capability.getEquippedItems());
		this.accessories = new HashMap<>(capability.getEquippedAccessories());
		this.kbArmors = new HashMap<>(capability.getEquippedKBArmors());
		this.armors = new HashMap<>(capability.getEquippedArmors());
		this.maxAccessories = capability.getMaxAccessories();
		this.maxArmors = capability.getMaxArmors();
		
		this.messages = capability.getMessages();
		this.bfMessages = capability.getBFMessages();
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

		this.magicCasttimeTicks = capability.getMagicCasttimeTicks();
		this.magicCooldownTicks = capability.getMagicCooldownTicks();
		
		this.reactionList = capability.getReactionCommands();
		
		this.shortcutsMap = capability.getShortcutsMap();
		
		this.synthLevel = capability.getSynthLevel();
		this.synthExp = capability.getSynthExperience();
		
		this.respawnROD = capability.getRespawnROD();

		this.singleStyle = capability.getSingleStyle();
		this.dualStyle = capability.getDualStyle();

		this.synthesisedRecipes = capability.getSynthesisedRecipes();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.level);
		buffer.writeInt(this.exp);
		buffer.writeInt(this.expGiven);
		buffer.writeNbt(this.strength.serialize(new CompoundTag()));
		buffer.writeNbt(this.magic.serialize(new CompoundTag()));
		buffer.writeNbt(this.defense.serialize(new CompoundTag()));
		buffer.writeNbt(this.maxAP.serialize(new CompoundTag()));
		
		buffer.writeDouble(this.MP);
		buffer.writeDouble(this.maxMP);
		buffer.writeBoolean(this.recharge);
		buffer.writeInt(this.maxHp);
		buffer.writeDouble(this.dp);
		buffer.writeDouble(this.maxDP);
		buffer.writeDouble(this.fp);
		buffer.writeInt(this.antipoints);
		buffer.writeInt(this.munny);
		buffer.writeDouble(this.focus);
		buffer.writeDouble(this.maxFocus);
		
		CompoundTag recipes = new CompoundTag();
        for (ResourceLocation r : recipeList) {
            recipes.putString(r.toString(), r.toString());
        }
		buffer.writeNbt(recipes);

		CompoundTag magics = new CompoundTag();
        for (Map.Entry<String, int[]> pair : magicsMap.entrySet()) {
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
        for (Map.Entry<String, int[]> pair : driveFormMap.entrySet()) {
            forms.putIntArray(pair.getKey().toString(), pair.getValue());
        }
		buffer.writeNbt(forms);
		
		//TODO Here
		CompoundTag visibleForms = new CompoundTag();
		Iterator<String> visibleDriveFormsIt = visibleDriveForms.iterator();
		while (visibleDriveFormsIt.hasNext()) {
			String formName = visibleDriveFormsIt.next();
			visibleForms.putString(formName, "");
		}
		buffer.writeNbt(visibleForms);
		
		CompoundTag abilities = new CompoundTag();
        for (Map.Entry<String, int[]> pair : abilityMap.entrySet()) {
            abilities.putIntArray(pair.getKey().toString(), pair.getValue());
        }
		buffer.writeNbt(abilities);

		CompoundTag keychains = new CompoundTag();
		this.keychains.forEach((key, value) -> keychains.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(keychains);
		
		CompoundTag items = new CompoundTag();
		this.items.forEach((key, value) -> items.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(items);
		
		CompoundTag accessories = new CompoundTag();
		this.accessories.forEach((key, value) -> accessories.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(accessories);
		
		CompoundTag kbArmors = new CompoundTag();
		this.kbArmors.forEach((key, value) -> kbArmors.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(kbArmors);

		CompoundTag armors = new CompoundTag();
		this.armors.forEach((key, value) -> armors.put(key.toString(), value.serializeNBT()));
		buffer.writeNbt(armors);

		buffer.writeInt(maxAccessories);
		buffer.writeInt(maxArmors);
		
		buffer.writeInt(partyList.size());
        for (String string : partyList) {
            buffer.writeInt(string.length());
            buffer.writeUtf(string);
        }
		
		CompoundTag materials = new CompoundTag();
        for (Map.Entry<ResourceLocation, Integer> pair : materialMap.entrySet()) {
            materials.putInt(pair.getKey().toString(), pair.getValue());
            if (materials.getInt(pair.getKey().toString()) == 0)
                materials.remove(pair.getKey().toString());
        }
		buffer.writeNbt(materials);
		
		buffer.writeInt(messages.size());
		buffer.writeInt(bfMessages.size());
		buffer.writeInt(dfMessages.size());

        for (String message : this.messages) {
            buffer.writeUtf(message);
        }

        for (String bfMessage : this.bfMessages) {
            buffer.writeUtf(bfMessage);
        }

        for (String dfMessage : this.dfMessages) {
            buffer.writeUtf(dfMessage);
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
		
		buffer.writeInt(this.magicCasttimeTicks);
		buffer.writeInt(this.magicCooldownTicks);
		
		buffer.writeInt(reactionList.size());
        for (String s : reactionList) {
            buffer.writeUtf(s, 100);
        }
		
		CompoundTag shortcuts = new CompoundTag();

        for (Map.Entry<Integer, String> pair : shortcutsMap.entrySet()) {
            shortcuts.putString(pair.getKey().toString(), pair.getValue());
        }
		buffer.writeNbt(shortcuts);
		
		buffer.writeInt(this.synthLevel);
		buffer.writeInt(this.synthExp);
		
		buffer.writeBoolean(this.respawnROD);

		buffer.writeUtf(singleStyle.toString(), 20);
		buffer.writeUtf(dualStyle.toString(), 20);

		buffer.writeInt(synthesisedRecipes.size());
		for (String s : synthesisedRecipes) {
			buffer.writeUtf(s, 100);
		}
	}

	public static SCSyncCapabilityPacket decode(FriendlyByteBuf buffer) {
		SCSyncCapabilityPacket msg = new SCSyncCapabilityPacket();

		msg.level = buffer.readInt();
		msg.exp = buffer.readInt();
		msg.expGiven = buffer.readInt();
		msg.strength = Stat.deserializeNBT("strength", buffer.readNbt());
		msg.magic = Stat.deserializeNBT("magic", buffer.readNbt());
		msg.defense = Stat.deserializeNBT("defense", buffer.readNbt());
		msg.maxAP = Stat.deserializeNBT("max_ap", buffer.readNbt());

		msg.MP = buffer.readDouble();
		msg.maxMP = buffer.readDouble();
		msg.recharge = buffer.readBoolean();
		msg.maxHp = buffer.readInt();
		msg.dp = buffer.readDouble();
		msg.maxDP = buffer.readDouble();
		msg.fp = buffer.readDouble();
		msg.antipoints = buffer.readInt();
		msg.munny = buffer.readInt();
		msg.focus = buffer.readDouble();
		msg.maxFocus = buffer.readDouble();

		CompoundTag recipesTag = buffer.readNbt();
        for (String key : recipesTag.getAllKeys()) {
            msg.recipeList.add(new ResourceLocation(key));
        }
		
		CompoundTag magicsTag = buffer.readNbt();
        for (String magicName : magicsTag.getAllKeys()) {
            msg.magicsMap.put(magicName, magicsTag.getIntArray(magicName));
        }
		
		CompoundTag shotlocksTag = buffer.readNbt();
        msg.shotlockList.addAll(shotlocksTag.getAllKeys());
		
		msg.equippedShotlock = buffer.readUtf(100);
		
		CompoundTag driveFormsTag = buffer.readNbt();
        for (String driveFormName : driveFormsTag.getAllKeys()) {
            msg.driveFormMap.put(driveFormName, driveFormsTag.getIntArray(driveFormName));
        }
		
		//TODO here
		CompoundTag visibleDriveForms = buffer.readNbt();
        msg.visibleDriveForms.addAll(visibleDriveForms.getAllKeys());
		
		CompoundTag abilitiesTag = buffer.readNbt();
        for (String abilityName : abilitiesTag.getAllKeys()) {
            msg.abilityMap.put(abilityName, abilitiesTag.getIntArray(abilityName));
        }

		CompoundTag keychainsNBT = buffer.readNbt();
		keychainsNBT.getAllKeys().forEach(key -> msg.keychains.put(new ResourceLocation(key), ItemStack.of((CompoundTag) keychainsNBT.get(key))));
		
		CompoundTag itemsNBT = buffer.readNbt();
		itemsNBT.getAllKeys().forEach(key -> msg.items.put(Integer.parseInt(key), ItemStack.of((CompoundTag) itemsNBT.get(key))));
		
		CompoundTag accessoriesNBT = buffer.readNbt();
		accessoriesNBT.getAllKeys().forEach(key -> msg.accessories.put(Integer.parseInt(key), ItemStack.of((CompoundTag) accessoriesNBT.get(key))));
		
		CompoundTag kbArmorsNBT = buffer.readNbt();
		kbArmorsNBT.getAllKeys().forEach(key -> msg.kbArmors.put(Integer.parseInt(key), ItemStack.of((CompoundTag) kbArmorsNBT.get(key))));

		CompoundTag armorsNBT = buffer.readNbt();
		armorsNBT.getAllKeys().forEach(key -> msg.armors.put(Integer.parseInt(key), ItemStack.of((CompoundTag) armorsNBT.get(key))));
		
		msg.maxAccessories = buffer.readInt();
		msg.maxArmors = buffer.readInt();
		
		int amount = buffer.readInt();
		msg.partyList = new ArrayList<String>();

		for(int i=0;i<amount;i++) {
			int length = buffer.readInt();
			msg.partyList.add(buffer.readUtf(length));
		}
		
		CompoundTag materialsTag = buffer.readNbt();
        for (String matName : materialsTag.getAllKeys()) {
            msg.materialMap.put(new ResourceLocation(matName), materialsTag.getInt(matName));
        }
		
		int msgSize = buffer.readInt();
		int bfMsgSize = buffer.readInt();
		int dfMsgSize = buffer.readInt();
		
		msg.messages = new ArrayList<>();
		for(int i = 0;i<msgSize;i++) {
			msg.messages.add(buffer.readUtf(100));
		}
		
		msg.bfMessages = new ArrayList<>();
		for(int i = 0;i<bfMsgSize;i++) {
			msg.bfMessages.add(buffer.readUtf(100));
		}
		
		msg.dfMessages = new ArrayList<>();
		for(int i = 0;i<dfMsgSize;i++) {
			msg.dfMessages.add(buffer.readUtf(100));
		}
		
		int length = buffer.readInt();
		msg.driveForm = buffer.readUtf(length);
		
		msg.returnDim = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
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
		
		msg.magicCasttimeTicks = buffer.readInt();
		msg.magicCooldownTicks = buffer.readInt();
		
		int rSize = buffer.readInt();
		for(int i = 0; i < rSize;i++) {
			msg.reactionList.add(buffer.readUtf(100));
		}
		
		CompoundTag shortcutsTag = buffer.readNbt();
        for (String s : shortcutsTag.getAllKeys()) {
            int shortcutPos = Integer.parseInt(s);
            msg.shortcutsMap.put(shortcutPos, shortcutsTag.getString(shortcutPos + ""));
        }
		
		msg.synthLevel = buffer.readInt();
		msg.synthExp = buffer.readInt();
		
		msg.respawnROD = buffer.readBoolean();

		msg.singleStyle = SingleChoices.valueOf(buffer.readUtf(20));
		msg.dualStyle = DualChoices.valueOf(buffer.readUtf(20));

		int recipeSize = buffer.readInt();
		for(int i = 0; i < recipeSize;i++) {
			msg.synthesisedRecipes.add(buffer.readUtf(100));
		}
		return msg;
	}

	public static void handle(final SCSyncCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncCapability(message)));
		ctx.get().setPacketHandled(true);
	}

}
