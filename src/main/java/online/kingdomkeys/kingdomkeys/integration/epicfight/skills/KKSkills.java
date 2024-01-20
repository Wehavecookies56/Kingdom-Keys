package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

public class KKSkills {
    public static Skill comboExtender;

    public static void register()
    {
        SkillManager.register(ComboExtender::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE), KingdomKeys.MODID, "combo_extender");
    }

    @SubscribeEvent
    public void buildSkillsEvent(SkillBuildEvent event)
    {
        comboExtender = event.build(KingdomKeys.MODID, "combo_extender");
    }
}
