package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class KKSkills {
    public static Skill comboExtender;

    private KKSkills() {}

    public static void register()
    {
        SkillManager.register(ComboExtender::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE), KingdomKeys.MODID, "combo_extender");
    }

    @SubscribeEvent
    public static void buildSkillsEvent(SkillBuildEvent event)
    {
        comboExtender = event.build(KingdomKeys.MODID, "combo_extender");
    }
}
