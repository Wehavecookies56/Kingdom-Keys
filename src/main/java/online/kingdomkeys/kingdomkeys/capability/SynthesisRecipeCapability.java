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
import uk.co.wehavecookies56.kk.api.recipes.FreeDevRecipeRegistry;
import uk.co.wehavecookies56.kk.api.recipes.Recipe;
import uk.co.wehavecookies56.kk.api.recipes.RecipeRegistry;
import uk.co.wehavecookies56.kk.common.KingdomKeys;

public class SynthesisRecipeCapability {

	public List<String> knownRecipes = new ArrayList<String>();
	public List<String> freeDevRecipes = new ArrayList<String>();

	public interface ISynthesisRecipe {
		List<String> getKnownRecipes();

		List<String> getFreeDevRecipes();

		void learnRecipe(Recipe recipe);

		void learnFreeDevRecipe(Recipe recipe);
	}

	public static class Storage implements IStorage<ISynthesisRecipe> {

		@Override
		public NBTBase writeNBT(Capability<ISynthesisRecipe> capability, ISynthesisRecipe instance, EnumFacing side) {
			NBTTagCompound properties = new NBTTagCompound();

			NBTTagList tagList = new NBTTagList();
			for (int i = 0; i < instance.getKnownRecipes().size(); i++) {
				String s = instance.getKnownRecipes().get(i);
				if (!s.isEmpty()) {
					NBTTagCompound recipes = new NBTTagCompound();
					recipes.setString("Recipes" + i, s);
					tagList.appendTag(recipes);
				}
			}
			properties.setTag("RecipeList", tagList);

			NBTTagList tagListFD = new NBTTagList();
			for (int i = 0; i < instance.getFreeDevRecipes().size(); i++) {
				String s2 = instance.getFreeDevRecipes().get(i);
				if (!s2.isEmpty()) {
					NBTTagCompound FDrecipes = new NBTTagCompound();
					FDrecipes.setString("FDRecipes" + i, s2);
					tagListFD.appendTag(FDrecipes);
				}
			}
			properties.setTag("FDRecipeList", tagListFD);

			return properties;
		}

		@Override
		public void readNBT(Capability<ISynthesisRecipe> capability, ISynthesisRecipe instance, EnumFacing side, NBTBase nbt) {

			NBTTagCompound properties = (NBTTagCompound) nbt;
			NBTTagList tagList = properties.getTagList("RecipeList", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound recipes = tagList.getCompoundTagAt(i);
				if (!RecipeRegistry.isRecipeKnown(instance.getKnownRecipes(), recipes.getString("Recipes" + i))) {
					instance.getKnownRecipes().add(i, recipes.getString("Recipes" + i));
					KingdomKeys.logger.info("Loaded known recipe: " + recipes.getString("Recipes" + i) + " " + i);
				}

			}

			NBTTagList tagListFD = properties.getTagList("FDRecipeList", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagListFD.tagCount(); i++) {
				NBTTagCompound FDrecipes = tagListFD.getCompoundTagAt(i);
				if (!FreeDevRecipeRegistry.isFreeDevRecipeKnown(instance.getFreeDevRecipes(), FDrecipes.getString("FDRecipes" + i))) {
					instance.getFreeDevRecipes().add(i, FDrecipes.getString("FDRecipes" + i));
					KingdomKeys.logger.info("Loaded known FD recipe: " + FDrecipes.getString("FDRecipes" + i) + " " + i);
				}

			}
		}

	}

	public static class Default implements ISynthesisRecipe {
		private List<String> knownRecipes = new ArrayList<String>();
		private List<String> freeDevRecipes = new ArrayList<String>();

		@Override
		public List<String> getKnownRecipes() {
			return knownRecipes;
		}

		@Override
		public List<String> getFreeDevRecipes() {
			return freeDevRecipes;
		}

		@Override
		public void learnRecipe(Recipe recipe) {
			if (!this.knownRecipes.contains(recipe.getName()))
				this.knownRecipes.add(recipe.getName());
			java.util.Collections.sort(knownRecipes);
		}

		@Override
		public void learnFreeDevRecipe(Recipe recipe) {
			if (!this.freeDevRecipes.contains(recipe.getName()))
				this.freeDevRecipes.add(recipe.getName());
			java.util.Collections.sort(freeDevRecipes);
		}
	}
}
