/*
package online.kingdomkeys.kingdomkeys.capability;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;


public class SynthesisMaterialCapability {

    public Map<String, Integer> knownMaterialsMap = new HashMap<String, Integer>();


    public interface ISynthesisMaterial {
        TreeMap<String, Integer> getKnownMaterialsMap();
        //TreeMap<String, Integer> getBuyableMaterialsMap();

        int getMaterialAmount(Material material);

        void addMaterial (Material material, int amount);
        void removeMaterial (Material material, int amount);
        void setMaterial (Material material, int amount);
    }

    public static class Storage implements IStorage<ISynthesisMaterial> {

        @Override
        public NBTBase writeNBT(Capability<ISynthesisMaterial> capability, ISynthesisMaterial instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();

            Iterator<Map.Entry<String, Integer>> it = instance.getKnownMaterialsMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
                properties.setInteger(pair.getKey().toString(), pair.getValue());
            }
            return properties;
        }

        @Override
        public void readNBT(Capability<ISynthesisMaterial> capability, ISynthesisMaterial instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;

            Iterator<String> it = properties.getKeySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                instance.getKnownMaterialsMap().put(key.toString(), properties.getInteger(key));
                if (properties.getInteger(key) == 0 && key.toString() != null) 
                	instance.getKnownMaterialsMap().remove(key.toString());
            }

        }

    }

    public static class Default implements ISynthesisMaterial {
        private TreeMap<String, Integer> materials = new TreeMap<String, Integer>();

        @Override
        public TreeMap<String, Integer> getKnownMaterialsMap() {
            return materials;
        }

        @Override
        public int getMaterialAmount(Material material) {
            if (materials.containsKey(material.getName())) {
                int currAmount = materials.get(material.getName());
                return currAmount;
            }
            return 0;
        }

        @Override
        public void addMaterial(Material material, int amount) {
            if (materials.containsKey(material.getName())) {
                int currAmount = materials.get(material.getName());
                if (amount <= 0) {
                    materials.remove(material.getName());
                } else {
                    materials.replace(material.getName(), currAmount + amount);
                }
            } else {
                if (amount <= 0) {
                    materials.remove(material.getName());
                } else {
                    materials.put(material.getName(), amount);
                }
            }
        }

        @Override
        public void setMaterial(Material material, int amount) {
            if (materials.containsKey(material.getName())) {
                if (amount <= 0)
                    materials.remove(material.getName());
                else
                materials.replace(material.getName(), amount);
            } else {
                if (amount <= 0)
                    materials.remove(material.getName());
                else
                    materials.put(material.getName(), amount);
            }
        }

        @Override
        public void removeMaterial(Material material, int amount) {
            if (materials.containsKey(material.getName())) {
                int currAmount = materials.get(material.getName());
                if (amount > currAmount) 
                	amount = currAmount;
                materials.replace(material.getName(), currAmount - amount);
            } else
                return;
        }
    }
}
*/