package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class TutorialsCapability {

	List<String> messages = new ArrayList<String>();

	public interface ITutorials {
		boolean getKnownTutorial(int id);

		void setKnownTutorial(int id, boolean watched);

		ArrayList<Integer> getKnownTutorials();
		
		void setKnownTutorials(ArrayList<Integer> list);
	}

	public static class Storage implements IStorage<ITutorials> {

		@Override
		public NBTBase writeNBT(Capability<ITutorials> capability, ITutorials instance, EnumFacing side) {
			NBTTagCompound properties = new NBTTagCompound();

			NBTTagList tagList = new NBTTagList();
			for (int i = 0; i < instance.getKnownTutorials().size(); i++) {
				int s = instance.getKnownTutorials().get(i);
				NBTTagCompound tutorials = new NBTTagCompound();
				tutorials.setInteger("Tutorials" + i, s);
				tagList.appendTag(tutorials);
			}
			properties.setTag("TutorialList", tagList);

			return properties;
		}

		@Override
		public void readNBT(Capability<ITutorials> capability, ITutorials instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound properties = (NBTTagCompound) nbt;

			NBTTagList tagList = properties.getTagList("TutorialList", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tutorials = tagList.getCompoundTagAt(i);
				instance.getKnownTutorials().add(i, tutorials.getInteger("Tutorials" + i));
				KingdomKeys.logger.info("Loaded known tutorial: " + tutorials.getInteger("Tutorials" + i) + " " + i);
			}
		}
	}

	public static class Default implements ITutorials {
		ArrayList<Integer> list = new ArrayList<Integer>();

		@Override
		public boolean getKnownTutorial(int id) {
			return list.contains(id);
		}

		@Override
		public void setKnownTutorial(int id, boolean watched) {
			if (watched) {
				if (!list.contains(id)) {
					list.add(id);
				}/* else {
					System.out.println("Tutorial already watched");
				}*/
			} else {
				if (list.contains(id)) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i) == id) {
							list.remove(i);
						}
					}
				}/* else {
					System.out.println("Tutorial was not watched");
				}*/
			}
		}

		@Override
		public ArrayList<Integer> getKnownTutorials() {
			return list;
		}

		@Override
		public void setKnownTutorials(ArrayList<Integer> list) {
			//System.out.println(this.list);
			//System.out.println(list);
			this.list = list;
		}
	}
}