package online.kingdomkeys.kingdomkeys.capability;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

public class PlayerCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IPlayerCapabilities instance = new PlayerCapabilities();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.PLAYER_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
        storage.putInt("level", instance.getLevel());
        storage.putInt("experience", instance.getExperience());
        storage.putInt("experience_given", instance.getExperienceGiven());
        storage.putInt("strength", instance.getStrength(false));
        storage.putInt("boost_strength", instance.getBoostStrength());
        storage.putInt("magic", instance.getMagic(false));
        storage.putInt("boost_magic", instance.getBoostMagic());
        storage.putInt("defense", instance.getDefense(false));
        storage.putInt("boost_defense", instance.getBoostDefense());
        storage.putInt("max_hp", instance.getMaxHP());
        storage.putInt("max_ap", instance.getMaxAP(false));
        storage.putInt("boost_max_ap", instance.getBoostMaxAP());
        storage.putDouble("mp", instance.getMP());
        storage.putDouble("max_mp", instance.getMaxMP());
        storage.putDouble("focus", instance.getFocus());
        storage.putDouble("max_focus", instance.getMaxFocus());
        storage.putBoolean("recharge", instance.getRecharge());
        storage.putDouble("dp", instance.getDP());
        storage.putDouble("max_dp", instance.getMaxDP());
        storage.putDouble("fp", instance.getFP());
        storage.putString("drive_form", instance.getActiveDriveForm());
        storage.putInt("anti_points", instance.getAntiPoints());
        storage.putInt("aero_ticks", instance.getAeroTicks());
        storage.putInt("aero_level", instance.getAeroLevel());
        storage.putInt("reflect_ticks", instance.getReflectTicks());
        storage.putInt("reflect_level", instance.getReflectLevel());
        storage.putBoolean("reflect_active", instance.getReflectActive());
        storage.putInt("munny", instance.getMunny());
        storage.putByte("soa_state", instance.getSoAState().get());
        storage.putByte("soa_choice", instance.getChosen().get());
        storage.putByte("soa_sacrifice", instance.getSacrificed().get());
        CompoundTag returnCompound = new CompoundTag();
        Vec3 pos = instance.getReturnLocation();
        returnCompound.putDouble("x", pos.x);
        returnCompound.putDouble("y", pos.y);
        returnCompound.putDouble("z", pos.z);
        storage.put("soa_return_pos", returnCompound);
        storage.putString("soa_return_dim", instance.getReturnDimension().location().toString());
        CompoundTag choicePedestalCompound = new CompoundTag();
        BlockPos choicePos = instance.getChoicePedestal();
        choicePedestalCompound.putInt("x", choicePos.getX());
        choicePedestalCompound.putInt("y", choicePos.getY());
        choicePedestalCompound.putInt("z", choicePos.getZ());
        storage.put("soa_choice_pedestal", choicePedestalCompound);
        CompoundTag sacrificePedestalCompound = new CompoundTag();
        BlockPos sacrificePos = instance.getSacrificePedestal();
        sacrificePedestalCompound.putInt("x", sacrificePos.getX());
        sacrificePedestalCompound.putInt("y", sacrificePos.getY());
        sacrificePedestalCompound.putInt("z", sacrificePos.getZ());
        storage.put("soa_sacrifice_pedestal", sacrificePedestalCompound);

        CompoundTag recipes = new CompoundTag();
        for (ResourceLocation recipe : instance.getKnownRecipeList()) {
            recipes.putString(recipe.toString(), recipe.toString());
        }
        storage.put("recipes", recipes);

        CompoundTag magics = new CompoundTag();
        Iterator<Map.Entry<String, int[]>> magicsIt = instance.getMagicsMap().entrySet().iterator();
        while (magicsIt.hasNext()) {
            Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) magicsIt.next();
            magics.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        storage.put("magics", magics);
        
        CompoundTag shotlocks = new CompoundTag();
        for (String shotlock : instance.getShotlockList()) {
            shotlocks.putInt(shotlock, 0);
        }
        storage.put("shotlocks", shotlocks);
        
        storage.putString("equipped_shotlock", instance.getEquippedShotlock());

        CompoundTag forms = new CompoundTag();
        Iterator<Map.Entry<String, int[]>> driveFormsIt = instance.getDriveFormMap().entrySet().iterator();
        while (driveFormsIt.hasNext()) {
            Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
            forms.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        storage.put("drive_forms", forms);

        CompoundTag abilities = new CompoundTag();
        Iterator<Map.Entry<String, int[]>> abilitiesIt = instance.getAbilityMap().entrySet().iterator();
        while (abilitiesIt.hasNext()) {
            Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) abilitiesIt.next();
            abilities.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        storage.put("abilities", abilities);

        CompoundTag keychains = new CompoundTag();
        instance.getEquippedKeychains().forEach((form, chain) -> keychains.put(form.toString(), chain.serializeNBT()));
        storage.put("keychains", keychains);
        
        CompoundTag items = new CompoundTag();
        instance.getEquippedItems().forEach((slot, item) -> items.put(slot.toString(), item.serializeNBT()));
        storage.put("items", items);

        storage.putInt("hearts", instance.getHearts());
        storage.putInt("org_alignment", instance.getAlignmentIndex());
        storage.put("org_equipped_weapon", instance.getEquippedWeapon().serializeNBT());

        CompoundTag unlockedWeapons = new CompoundTag();
        instance.getWeaponsUnlocked().forEach(weapon -> unlockedWeapons.put(weapon.getItem().getRegistryName().toString(), weapon.serializeNBT()));
        storage.put("org_weapons_unlocked", unlockedWeapons);

        CompoundTag parties = new CompoundTag();
        for (int i=0;i<instance.getPartiesInvited().size();i++) {
            parties.putInt(instance.getPartiesInvited().get(i),i);
        }
        storage.put("parties", parties);

        CompoundTag mats = new CompoundTag();
        Iterator<Map.Entry<String, Integer>> materialsIt = instance.getMaterialMap().entrySet().iterator();
        while (materialsIt.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) materialsIt.next();
            mats.putInt(pair.getKey().toString(), pair.getValue());
            if(mats.getInt(pair.getKey()) == 0 && pair.getKey().toString() != null)
                mats.remove(pair.getKey().toString());
        }
        storage.put("materials", mats);
        storage.putInt("limitCooldownTicks", instance.getLimitCooldownTicks());

        CompoundTag shortcuts = new CompoundTag();
        Iterator<Map.Entry<Integer, String>> shortcutsIt = instance.getShortcutsMap().entrySet().iterator();
        while (shortcutsIt.hasNext()) {
            Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) shortcutsIt.next();
            shortcuts.putString(pair.getKey().toString(), pair.getValue());
        }
        storage.put("shortcuts", shortcuts);
        
        return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag storage = (CompoundTag) nbt;
        instance.setLevel(storage.getInt("level"));
        instance.setExperience(storage.getInt("experience"));
        instance.setExperienceGiven(storage.getInt("experience_given"));
        instance.setStrength(storage.getInt("strength"));
        instance.setBoostStrength(storage.getInt("boost_strength"));
        instance.setMagic(storage.getInt("magic"));
        instance.setBoostMagic(storage.getInt("boost_magic"));
        instance.setDefense(storage.getInt("defense"));
        instance.setBoostDefense(storage.getInt("boost_defense"));
        instance.setMaxHP(storage.getInt("max_hp"));
        instance.setMaxAP(storage.getInt("max_ap"));
        instance.setBoostMaxAP(storage.getInt("boost_max_ap"));
        instance.setMP(storage.getDouble("mp"));
        instance.setMaxMP(storage.getDouble("max_mp"));
        instance.setFocus(storage.getDouble("focus"));
        instance.setMaxFocus(storage.getDouble("max_focus"));
        instance.setRecharge(storage.getBoolean("recharge"));
        instance.setDP(storage.getDouble("dp"));
        instance.setMaxDP(storage.getDouble("max_dp"));
        instance.setFP(storage.getDouble("fp"));
        instance.setActiveDriveForm(storage.getString("drive_form"));
        instance.setAntiPoints(storage.getInt("anti_points"));
        instance.setAeroTicks(storage.getInt("aero_ticks"), storage.getInt("aero_level"));
        instance.setReflectTicks(storage.getInt("reflect_ticks"), storage.getInt("reflect_level"));
        instance.setReflectActive(storage.getBoolean("reflect_active"));
        instance.setMunny(storage.getInt("munny"));
        instance.setSoAState(SoAState.fromByte(storage.getByte("soa_state")));
        instance.setChoice(SoAState.fromByte(storage.getByte("soa_choice")));
        instance.setSacrifice(SoAState.fromByte(storage.getByte("soa_sacrifice")));
        CompoundTag returnCompound = storage.getCompound("soa_return_pos");
        instance.setReturnLocation(new Vec3(returnCompound.getDouble("x"), returnCompound.getDouble("y"), returnCompound.getDouble("z")));
        instance.setReturnDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(storage.getString("soa_return_dim"))));
        CompoundTag choicePedestal = storage.getCompound("soa_choice_pedestal");
        instance.setChoicePedestal(new BlockPos(choicePedestal.getInt("x"), choicePedestal.getInt("y"), choicePedestal.getInt("z")));
        CompoundTag sacrificePedestal = storage.getCompound("soa_sacrifice_pedestal");
        instance.setSacrificePedestal(new BlockPos(sacrificePedestal.getInt("x"), sacrificePedestal.getInt("y"), sacrificePedestal.getInt("z")));

        Iterator<String> recipesIt = storage.getCompound("recipes").getAllKeys().iterator();
        while (recipesIt.hasNext()) {
            String key = (String) recipesIt.next();
            instance.getKnownRecipeList().add(new ResourceLocation(key));
        }

        Iterator<String> magicsIt = storage.getCompound("magics").getAllKeys().iterator();
        while (magicsIt.hasNext()) {
            String magicName = (String) magicsIt.next();
            
            int[] array;
            if(storage.getCompound("magics").contains(magicName,99)) {
            	System.out.println("Converting "+magicName+" data");
            	array = new int[] { storage.getCompound("magics").getInt(magicName), 0 };
            } else {
            	array = storage.getCompound("magics").getIntArray(magicName);
            }
            instance.getMagicsMap().put(magicName.toString(), array);
        }
        
        Iterator<String> shotlockIt = storage.getCompound("shotlocks").getAllKeys().iterator();
        while (shotlockIt.hasNext()) {
            String key = (String) shotlockIt.next();
            
            instance.getShotlockList().add(key.toString());
        }
        
        instance.setEquippedShotlock(storage.getString("equipped_shotlock"));

        Iterator<String> driveFormsIt = storage.getCompound("drive_forms").getAllKeys().iterator();
        while (driveFormsIt.hasNext()) {
            String driveFormName = (String) driveFormsIt.next();
            
            instance.getDriveFormMap().put(driveFormName.toString(), storage.getCompound("drive_forms").getIntArray(driveFormName));
        }

        Iterator<String> abilitiesIt = storage.getCompound("abilities").getAllKeys().iterator();
        while (abilitiesIt.hasNext()) {
            String abilityName = (String) abilitiesIt.next();
            
            instance.getAbilityMap().put(abilityName.toString(), storage.getCompound("abilities").getIntArray(abilityName));
        }

        CompoundTag keychainsNBT = storage.getCompound("keychains");
        keychainsNBT.getAllKeys().forEach((chain) -> instance.setNewKeychain(new ResourceLocation(chain), ItemStack.of(keychainsNBT.getCompound(chain))));
        
        CompoundTag itemsNBT = storage.getCompound("items");
        itemsNBT.getAllKeys().forEach((slot) -> instance.setNewItem(Integer.parseInt(slot), ItemStack.of(itemsNBT.getCompound(slot))));

        instance.setHearts(storage.getInt("hearts"));
        instance.setAlignment(storage.getInt("org_alignment"));
        instance.equipWeapon(ItemStack.of(storage.getCompound("org_equipped_weapon")));
        CompoundTag unlocksCompound = storage.getCompound("org_weapons_unlocked");
        unlocksCompound.getAllKeys().forEach(key -> instance.unlockWeapon(ItemStack.of(unlocksCompound.getCompound(key))));

        Iterator<String> partyIt = storage.getCompound("parties").getAllKeys().iterator();
        while (partyIt.hasNext()) {
            String key = (String) partyIt.next();
            instance.getPartiesInvited().add(key.toString());
        }

        Iterator<String> materialsIt = storage.getCompound("materials").getAllKeys().iterator();
        while (materialsIt.hasNext()) {
            String mat = (String) materialsIt.next();
            instance.getMaterialMap().put(mat.toString(), storage.getCompound("materials").getInt(mat));
        }
        
        instance.setLimitCooldownTicks(storage.getInt("limitCooldownTicks"));
        
        Iterator<String> shortcutsIt = storage.getCompound("shortcuts").getAllKeys().iterator();
        while (shortcutsIt.hasNext()) {
            int shortcutPos = Integer.parseInt(shortcutsIt.next());
            instance.getShortcutsMap().put(shortcutPos, storage.getCompound("shortcuts").getString(shortcutPos+""));
        }

	}

}
