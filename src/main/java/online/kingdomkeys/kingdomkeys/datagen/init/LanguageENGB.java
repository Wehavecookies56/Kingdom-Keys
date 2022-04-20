package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.datagen.provider.KKLanguageProvider;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class LanguageENGB extends KKLanguageProvider {


    public LanguageENGB(DataGenerator gen) {
        super(gen, "en_gb");
    }

    @Override
    protected void addTranslations() {
        add(Strings.Gui_Menu_Status_Defense, "Defence");
        add("stats.levelUpDef", "Defence Increased!");
        addItem(ModItems.defenseBoost, "Defence Boost");

        add("gui.org.line1", "By donning the Dark Robe you are now a member of Organisation XIII.");
        add("gui.org.line2", "Choose a member of Organisation XIII you align with.");

        add("gui.proofofheart.desc", "Use this to leave Organisation XIII");
        add("gui.proofofheart.desc2", "You won't be able to use it if you're wearing the Organisation XIII robes");
        add("gui.proofofheart.notinorg", "You are not in Organisation XIII");
        add("gui.proofofheart.leftorg", "You have left Organisation XIII");
        add("gui.proofofheart.unequip", "First unequip your Organisation XIII armour");

        addItem(ModItems.organizationRobe_Helmet, "Organisation Hood");
        addItem(ModItems.organizationRobe_Chestplate, "Organisation Coat");
        addItem(ModItems.organizationRobe_Leggings, "Organisation Leggings");
        addItem(ModItems.organizationRobe_Boots, "Organisation Boots");

        addBlock(ModBlocks.orgPortal, "Organisation Portal");

        add("jei.info.kingdomkeys.organization_weapons", "As an Organisation member you can unlock weapons within the equipment menu by spending hearts gained from kills, you will earn 2x hearts from using a weapon from your chosen member. Summon the weapons using the summon key.");
        add("jei.info.kingdomkeys.organization_robes", "Wear the full Organisation set to join and select a member to start with, no matter who you choose you can unlock every member's weapons however it requires unlocking the adjacent member's weapon first.");
        add("jei.info.kingdomkeys.proof_of_heart", "Obtained from defeating the Ender Dragon, use this to leave the Organisation.");
    }
}
