package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.EpicFight;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

public class KKSkills {

    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(KingdomKeys.rl(EpicFight.MODID, "skill"), KingdomKeys.MODID);
    public static final DeferredHolder<Skill, ComboExtender> comboExtender = SKILLS.register("combo_extender", key ->
            Skill.createBuilder(ComboExtender::new)
                    .setCategory(SkillCategories.WEAPON_PASSIVE)
                    .setResource(Skill.Resource.NONE)
                    .build(key));
}
