package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

public class KKSkills {

    public static final DeferredRegister<Skill> SKILLS = DeferredRegister.create(new ResourceLocation(EpicFightMod.MODID, "skill"), KingdomKeys.MODID);
    public static final RegistryObject<Skill> comboExtender = SKILLS.register("combo_extender", () -> new ComboExtender(Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE).setRegistryName(new ResourceLocation(KingdomKeys.MODID, "combo_extender"))));
}
