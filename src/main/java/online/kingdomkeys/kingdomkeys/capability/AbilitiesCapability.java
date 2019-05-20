package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.Ability;

public class AbilitiesCapability {

	public interface IAbilities {
		boolean getUnlockedAbility(Ability ability);

		void unlockAbility(Ability ability);

		ArrayList<Ability> getUnlockedAbilities();

		void clearUnlockedAbilities();

		void setUnlockedAbilities(ArrayList<Ability> ability);

		boolean getEquippedAbility(Ability ability);

		void equipAbility(Ability ability, boolean unlocked);

		ArrayList<Ability> getEquippedAbilities();

		void clearEquippedAbilities();

		void setEquippedAbilities(ArrayList<Ability> equippedAbilities);

		boolean getUseSonicBlade();

		void setUseSonicBlade(boolean use);

		boolean getInvincible();

		void setInvincible(boolean inv);
		
		int getInvTicks();
		
		void setInvTicks(int ticks);
	}

	public static class Storage implements IStorage<IAbilities> {

		@Override
		public INBTBase writeNBT(Capability<IAbilities> capability, IAbilities instance, EnumFacing side) {
			NBTTagCompound properties = new NBTTagCompound();

			NBTTagList uTagList = new NBTTagList();
			for (int i = 0; i < instance.getUnlockedAbilities().size(); i++) {
				Ability a = instance.getUnlockedAbilities().get(i);
				String s = a.getName();
				NBTTagCompound abilities = new NBTTagCompound();
				abilities.setString("UAbilities" + i, s);
				uTagList.appendTag(abilities);
			}
			properties.setTag("UAbilitiesList", uTagList);

			NBTTagList eTagList = new NBTTagList();
			for (int i = 0; i < instance.getEquippedAbilities().size(); i++) {
				Ability a = instance.getEquippedAbilities().get(i);
				String s = a.getName();
				NBTTagCompound abilities = new NBTTagCompound();
				abilities.setString("EAbilities" + i, s);
				eTagList.appendTag(abilities);
			}
			properties.setTag("EAbilitiesList", eTagList);
			return properties;

		}

		@Override
		public void readNBT(Capability<IAbilities> capability, IAbilities instance, EnumFacing side, INBTBase nbt) {
			NBTTagCompound properties = (NBTTagCompound) nbt;

			NBTTagList uTagList = properties.getList("UAbilitiesList", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < uTagList.size(); i++) {
				NBTTagCompound abilities = uTagList.getCompound(i);
				Ability ability = GameRegistry.findRegistry(Ability.class).getValue(new ResourceLocation(Reference.MODID + ":" + abilities.getString("UAbilities" + i)));
				instance.getUnlockedAbilities().add(i, ability);
				KingdomKeys.LOGGER.info("Loaded unlocked ability: " + abilities.getString("UAbilities" + i) + " " + i);
			}

			NBTTagList eTagList = properties.getTagList("EAbilitiesList", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < eTagList.tagCount(); i++) {
				NBTTagCompound abilities = eTagList.getCompoundTagAt(i);
				Ability ability = GameRegistry.findRegistry(Ability.class).getValue(new ResourceLocation(Reference.MODID + ":" + abilities.getString("EAbilities" + i)));
				instance.getEquippedAbilities().add(i, ability);
				KingdomKeys.logger.info("Loaded equipped ability: " + abilities.getString("EAbilities" + i) + " " + i);
			}
		}
	}

	public static class Default implements IAbilities {
		ArrayList<Ability> unlockedList = new ArrayList<Ability>();

		@Override
		public boolean getUnlockedAbility(Ability ability) {
			return unlockedList.contains(ability);
		}

		@Override
		public void unlockAbility(Ability ability) {
			unlockedList.add(ability);
		}

		@Override
		public ArrayList<Ability> getUnlockedAbilities() {
			return unlockedList;
		}

		@Override
		public void clearUnlockedAbilities() {
			unlockedList.clear();
		}

		ArrayList<Ability> equippedList = new ArrayList<Ability>();

		@Override
		public boolean getEquippedAbility(Ability ability) {
			return equippedList.contains(ability);
		}

		@Override
		public void equipAbility(Ability ability, boolean equip) {
			if (equip) {
				// System.out.println("Going to equip " + ability.getName());
				equippedList.add(ability);
			} else {
				// System.out.println("Going to unequip " + ability.getName());
				if (equippedList.contains(ability)) {
					for (int i = 0; i < equippedList.size(); i++) {
						if (equippedList.get(i) == ability) {
							equippedList.remove(i);
						}
					}
				}
			}
			// System.out.println(equippedList);
		}

		@Override
		public ArrayList<Ability> getEquippedAbilities() {
			return equippedList;
		}

		@Override
		public void clearEquippedAbilities() {
			equippedList.clear();
		}

		@Override
		public void setUnlockedAbilities(ArrayList<Ability> list) {
			this.unlockedList = list;
		}

		@Override
		public void setEquippedAbilities(ArrayList<Ability> list) {
			this.equippedList = list;
		}

		boolean useSonicBlade = false;

		@Override
		public boolean getUseSonicBlade() {
			return useSonicBlade;
		}

		@Override
		public void setUseSonicBlade(boolean use) {
			useSonicBlade = use;
		}

		boolean invincibility = false;
		int invTicks = 0;
		
		@Override
		public boolean getInvincible() {
			return this.invincibility;
		}

		@Override
		public void setInvincible(boolean inv) {
			this.invincibility = inv;
			this.invTicks = 0;
		}
		
		@Override
		public int getInvTicks() {
			return this.invTicks;
		}
		
		@Override
		public void setInvTicks(int ticks) {
			this.invTicks = ticks;
		}
	}
}