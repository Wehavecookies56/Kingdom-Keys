package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class EpicFightEvents {

	private EpicFightEvents() {}

	public static void registerGuardParryData(Player player) {
		PlayerPatch<?> patch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);

		if (patch == null) {
			return;
		}

		SkillContainer guardContainer = patch.getSkill(SkillSlots.GUARD);

		if (guardContainer == null || !(guardContainer.getSkill() instanceof ImpactGuardSkill)) {
			return;
		}

		SkillDataManager dataManager = guardContainer.getDataManager();

		if (!dataManager.hasData(EpicFightSkillDataKeys.PARRY_MOTION_COUNTER)) {
			dataManager.registerData(EpicFightSkillDataKeys.PARRY_MOTION_COUNTER);
		}
	}
}
