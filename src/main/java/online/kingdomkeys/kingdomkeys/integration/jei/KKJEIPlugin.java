package online.kingdomkeys.kingdomkeys.integration.jei;


import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

@JeiPlugin
public class KKJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(KingdomKeys.MODID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SynthesisRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new KeybladeSummonRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        //TODO make category for ore drops
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        //registration.addRecipeCatalyst(new ItemStack(ModBlocks.moogleProjector.get()), SynthesisRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<Item> keychainsFromRegistry = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof KeychainItem).toList();
        List<Item> orgWeapons = ForgeRegistries.ITEMS.getValues().stream().filter(i -> i instanceof IOrgWeapon).toList();
        List<KeychainItem> keychains = new ArrayList<>();
        keychainsFromRegistry.forEach(chain -> {
            if (((KeychainItem) chain).getKeyblade() != null) {
                keychains.add((KeychainItem) chain);
            }
        });

        //Info
        InfoAdder info = new InfoAdder(registration);

        info.addInfo(ModBlocks.moogleProjector.get(), "moogle_projector");
        orgWeapons.forEach(i -> info.addInfo(i, "organization_weapons"));
        keychains.forEach(i -> info.addInfo(i, "keychains"));
        info.addInfo(ModBlocks.blastBlox.get(), "blast_blox");
        info.addInfo(ModBlocks.dangerBlox.get(), "danger_blox");
        info.addInfo(ModBlocks.magnetBlox.get(), "magnet_blox");
        info.addInfo(ModBlocks.ghostBlox.get(), "ghost_blox");
        info.addInfo(ModBlocks.bounceBlox.get(), "bounce_blox");
        info.addInfo(ModItems.recipeD.get(), "recipes");
        info.addInfo(ModItems.proofOfHeart.get(), "proof_of_heart");
        info.addInfo(ModItems.organizationRobe_Helmet.get(), "organization_robes");
        info.addInfo(ModItems.organizationRobe_Chestplate.get(), "organization_robes");
        info.addInfo(ModItems.organizationRobe_Leggings.get(), "organization_robes");
        info.addInfo(ModItems.organizationRobe_Boots.get(), "organization_robes");
        info.addInfo(ModItems.xemnas_Helmet.get(), "organization_robes");
        info.addInfo(ModItems.xemnas_Chestplate.get(), "organization_robes");
        info.addInfo(ModItems.xemnas_Leggings.get(), "organization_robes");
        info.addInfo(ModItems.xemnas_Boots.get(), "organization_robes");
        info.addInfo(ModItems.antiCoat_Helmet.get(), "organization_robes");
        info.addInfo(ModItems.antiCoat_Chestplate.get(), "organization_robes");
        info.addInfo(ModItems.antiCoat_Leggings.get(), "organization_robes");
        info.addInfo(ModItems.antiCoat_Boots.get(), "organization_robes");
        //TODO individual magic descriptions (what they do, changes between tiers, mp cost)
        info.addInfo(ModItems.fireSpell.get(), "spell_orb");
        info.addInfo(ModItems.blizzardSpell.get(), "spell_orb");
        info.addInfo(ModItems.thunderSpell.get(), "spell_orb");
        info.addInfo(ModItems.gravitySpell.get(), "spell_orb");
        info.addInfo(ModItems.aeroSpell.get(), "spell_orb");
        info.addInfo(ModItems.cureSpell.get(), "spell_orb");
        info.addInfo(ModItems.reflectSpell.get(), "spell_orb");
        info.addInfo(ModItems.stopSpell.get(), "spell_orb");
        info.addInfo(ModItems.waterSpell.get(), "spell_orb");
        info.addInfo(ModItems.magnetSpell.get(), "spell_orb");
        info.addInfo(ModItems.valorOrb.get(), "valor_orb");
        info.addInfo(ModItems.wisdomOrb.get(), "wisdom_orb");
        info.addInfo(ModItems.limitOrb.get(), "limit_orb");
        info.addInfo(ModItems.masterOrb.get(), "master_orb");
        info.addInfo(ModItems.finalOrb.get(), "final_orb");


        //Recipes
        registration.addRecipes(KeybladeSummonRecipeCategory.TYPE, keychains);
        registration.addRecipes(SynthesisRecipeCategory.TYPE, RecipeRegistry.getInstance().getValues());

    }

    public static class InfoAdder {
        IRecipeRegistration registration;

        public InfoAdder(IRecipeRegistration registration) {
            this.registration = registration;
        }

        public void addInfo(Item item, String text) {
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK, Component.translatable("jei.info.kingdomkeys." + text));
        }

        public void addInfo(Block item, String text) {
            registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK, Component.translatable("jei.info.kingdomkeys." + text));
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {

    }
}
