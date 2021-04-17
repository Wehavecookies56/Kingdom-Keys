package online.kingdomkeys.kingdomkeys.capability;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.capabilities.Capability;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

public class PlayerCapabilitiesStorage implements Capability.IStorage<IPlayerCapabilities> {
    @Override
    public INBT writeNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side) {
        CompoundNBT storage = new CompoundNBT();
        storage.putInt("level", instance.getLevel());
        storage.putInt("experience", instance.getExperience());
        storage.putInt("experience_given", instance.getExperienceGiven());
        storage.putInt("strength", instance.getStrength());
        storage.putInt("magic", instance.getMagic());
        storage.putInt("defense", instance.getDefense());
        storage.putInt("max_hp", instance.getMaxHP());
        storage.putInt("max_ap", instance.getMaxAP());
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
        storage.putInt("reflect_ticks", instance.getReflectTicks());
        storage.putBoolean("reflect_active", instance.getReflectActive());
        storage.putInt("munny", instance.getMunny());
        storage.putByte("soa_state", instance.getSoAState().get());
        storage.putByte("soa_choice", instance.getChosen().get());
        storage.putByte("soa_sacrifice", instance.getSacrificed().get());
        CompoundNBT returnCompound = new CompoundNBT();
        Vector3d pos = instance.getReturnLocation();
        returnCompound.putDouble("x", pos.x);
        returnCompound.putDouble("y", pos.y);
        returnCompound.putDouble("z", pos.z);
        storage.put("soa_return_pos", returnCompound);
        storage.putString("soa_return_dim", instance.getReturnDimension().getLocation().toString());
        CompoundNBT choicePedestalCompound = new CompoundNBT();
        BlockPos choicePos = instance.getChoicePedestal();
        choicePedestalCompound.putInt("x", choicePos.getX());
        choicePedestalCompound.putInt("y", choicePos.getY());
        choicePedestalCompound.putInt("z", choicePos.getZ());
        storage.put("soa_choice_pedestal", choicePedestalCompound);
        CompoundNBT sacrificePedestalCompound = new CompoundNBT();
        BlockPos sacrificePos = instance.getSacrificePedestal();
        sacrificePedestalCompound.putInt("x", sacrificePos.getX());
        sacrificePedestalCompound.putInt("y", sacrificePos.getY());
        sacrificePedestalCompound.putInt("z", sacrificePos.getZ());
        storage.put("soa_sacrifice_pedestal", sacrificePedestalCompound);

        CompoundNBT recipes = new CompoundNBT();
        for (ResourceLocation recipe : instance.getKnownRecipeList()) {
            recipes.putString(recipe.toString(), recipe.toString());
        }
        storage.put("recipes", recipes);

        CompoundNBT magics = new CompoundNBT();
        for (String magic : instance.getMagicList()) {
            magics.putInt(magic, 0);
        }
        storage.put("magics", magics);
        
        CompoundNBT shotlocks = new CompoundNBT();
        for (String shotlock : instance.getShotlockList()) {
            shotlocks.putInt(shotlock, 0);
        }
        storage.put("shotlocks", shotlocks);
        
        storage.putString("equipped_shotlock", instance.getEquippedShotlock());

        CompoundNBT forms = new CompoundNBT();
        Iterator<Map.Entry<String, int[]>> driveFormsIt = instance.getDriveFormMap().entrySet().iterator();
        while (driveFormsIt.hasNext()) {
            Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) driveFormsIt.next();
            forms.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        storage.put("drive_forms", forms);

        CompoundNBT abilities = new CompoundNBT();
        Iterator<Map.Entry<String, int[]>> abilitiesIt = instance.getAbilityMap().entrySet().iterator();
        while (abilitiesIt.hasNext()) {
            Map.Entry<String, int[]> pair = (Map.Entry<String, int[]>) abilitiesIt.next();
            abilities.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        storage.put("abilities", abilities);

        CompoundNBT keychains = new CompoundNBT();
        instance.getEquippedKeychains().forEach((form, chain) -> keychains.put(form.toString(), chain.serializeNBT()));
        storage.put("keychains", keychains);
        
        CompoundNBT items = new CompoundNBT();
        instance.getEquippedItems().forEach((slot, item) -> items.put(slot.toString(), item.serializeNBT()));
        storage.put("items", items);

        storage.putInt("hearts", instance.getHearts());
        storage.putInt("org_alignment", instance.getAlignmentIndex());
        storage.put("org_equipped_weapon", instance.getEquippedWeapon().serializeNBT());

        CompoundNBT unlockedWeapons = new CompoundNBT();
        instance.getWeaponsUnlocked().forEach(weapon -> unlockedWeapons.put(weapon.getItem().getRegistryName().toString(), weapon.serializeNBT()));
        storage.put("org_weapons_unlocked", unlockedWeapons);

        CompoundNBT parties = new CompoundNBT();
        for (int i=0;i<instance.getPartiesInvited().size();i++) {
            parties.putInt(instance.getPartiesInvited().get(i),i);
        }
        storage.put("parties", parties);

        CompoundNBT mats = new CompoundNBT();
        Iterator<Map.Entry<String, Integer>> materialsIt = instance.getMaterialMap().entrySet().iterator();
        while (materialsIt.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) materialsIt.next();
            mats.putInt(pair.getKey().toString(), pair.getValue());
            if(mats.getInt(pair.getKey()) == 0 && pair.getKey().toString() != null)
                mats.remove(pair.getKey().toString());
        }
        storage.put("materials", mats);
        storage.putInt("limitCooldownTicks", instance.getLimitCooldownTicks());

        return storage;
    }

    @Override
    public void readNBT(Capability<IPlayerCapabilities> capability, IPlayerCapabilities instance, Direction side, INBT nbt) {
        CompoundNBT storage = (CompoundNBT) nbt;
        instance.setLevel(storage.getInt("level"));
        instance.setExperience(storage.getInt("experience"));
        instance.setExperienceGiven(storage.getInt("experience_given"));
        instance.setStrength(storage.getInt("strength"));
        instance.setMagic(storage.getInt("magic"));
        instance.setDefense(storage.getInt("defense"));
        instance.setMaxHP(storage.getInt("max_hp"));
        instance.setMaxAP(storage.getInt("max_ap"));
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
        instance.setAeroTicks(storage.getInt("aero_ticks"));
        instance.setReflectTicks(storage.getInt("reflect_ticks"));
        instance.setReflectActive(storage.getBoolean("reflect_active"));
        instance.setMunny(storage.getInt("munny"));
        instance.setSoAState(SoAState.fromByte(storage.getByte("soa_state")));
        instance.setChoice(SoAState.fromByte(storage.getByte("soa_choice")));
        instance.setSacrifice(SoAState.fromByte(storage.getByte("soa_sacrifice")));
        CompoundNBT returnCompound = storage.getCompound("soa_return_pos");
        instance.setReturnLocation(new Vector3d(returnCompound.getDouble("x"), returnCompound.getDouble("y"), returnCompound.getDouble("z")));
        instance.setReturnDimension(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(storage.getString("soa_return_dim"))));
        CompoundNBT choicePedestal = storage.getCompound("soa_choice_pedestal");
        instance.setChoicePedestal(new BlockPos(choicePedestal.getInt("x"), choicePedestal.getInt("y"), choicePedestal.getInt("z")));
        CompoundNBT sacrificePedestal = storage.getCompound("soa_sacrifice_pedestal");
        instance.setSacrificePedestal(new BlockPos(sacrificePedestal.getInt("x"), sacrificePedestal.getInt("y"), sacrificePedestal.getInt("z")));

        Iterator<String> recipesIt = storage.getCompound("recipes").keySet().iterator();
        while (recipesIt.hasNext()) {
            String key = (String) recipesIt.next();
            instance.getKnownRecipeList().add(new ResourceLocation(key));
        }

        Iterator<String> magicIt = storage.getCompound("magics").keySet().iterator();
        while (magicIt.hasNext()) {
            String key = (String) magicIt.next();
            //System.out.println("Read: " + key);
            instance.getMagicList().add(key.toString());
        }
        
        Iterator<String> shotlockIt = storage.getCompound("shotlocks").keySet().iterator();
        while (shotlockIt.hasNext()) {
            String key = (String) shotlockIt.next();
            //System.out.println("Read: " + key);
            instance.getShotlockList().add(key.toString());
        }
        
        instance.setEquippedShotlock(storage.getString("equipped_shotlock"));

        Iterator<String> driveFormsIt = storage.getCompound("drive_forms").keySet().iterator();
        while (driveFormsIt.hasNext()) {
            String driveFormName = (String) driveFormsIt.next();
            //System.out.println("Read: " + driveFormName);
            instance.getDriveFormMap().put(driveFormName.toString(), storage.getCompound("drive_forms").getIntArray(driveFormName));
        }

        Iterator<String> abilitiesIt = storage.getCompound("abilities").keySet().iterator();
        while (abilitiesIt.hasNext()) {
            String abilityName = (String) abilitiesIt.next();
            //System.out.println("Read: " + abilityName);
            instance.getAbilityMap().put(abilityName.toString(), storage.getCompound("abilities").getIntArray(abilityName));
        }

        CompoundNBT keychainsNBT = storage.getCompound("keychains");
        keychainsNBT.keySet().forEach((chain) -> instance.setNewKeychain(new ResourceLocation(chain), ItemStack.read(keychainsNBT.getCompound(chain))));
        
        CompoundNBT itemsNBT = storage.getCompound("items");
        itemsNBT.keySet().forEach((slot) -> instance.setNewItem(Integer.parseInt(slot), ItemStack.read(itemsNBT.getCompound(slot))));

        instance.setHearts(storage.getInt("hearts"));
        instance.setAlignment(storage.getInt("org_alignment"));
        instance.equipWeapon(ItemStack.read(storage.getCompound("org_equipped_weapon")));
        CompoundNBT unlocksCompound = storage.getCompound("org_weapons_unlocked");
        unlocksCompound.keySet().forEach(key -> instance.unlockWeapon(ItemStack.read(unlocksCompound.getCompound(key))));

        Iterator<String> partyIt = storage.getCompound("parties").keySet().iterator();
        while (partyIt.hasNext()) {
            String key = (String) partyIt.next();
            instance.getPartiesInvited().add(key.toString());
        }

        Iterator<String> materialsIt = storage.getCompound("materials").keySet().iterator();
        while (materialsIt.hasNext()) {
            String mat = (String) materialsIt.next();
            instance.getMaterialMap().put(mat.toString(), storage.getCompound("materials").getInt(mat));
        }
        
        instance.setLimitCooldownTicks(storage.getInt("limitCooldownTicks"));
    }
}
