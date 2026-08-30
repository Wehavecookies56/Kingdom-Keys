package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeLivingMotion;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.ImpactGuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightEvents {

	private EpicFightEvents() {}

	public static void playCastAnimation(Player caster, boolean projectile) {
		if (caster == null || caster.level().isClientSide) {
			return;
		}

		ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(caster, ServerPlayerPatch.class);
		if (patch == null) {
			return;
		}

		patch.playAnimationSynchronized(projectile ? KKAnimations.PROJECTILE_CAST : KKAnimations.INDIRECT_CAST, 0.0F);
	}

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


	public static void refreshLivingMotions(Player player) {
	    if (!KingdomKeys.efmLoaded || player == null || player.level().isClientSide) {
	        return;
	    }

	    ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
	    if (patch != null) {
	        patch.modifyLivingMotionByCurrentItem();
	        applyAntiFormMotions(player, patch);
	    }
	}

	public static boolean applyAntiFormMotions(Player player, ServerPlayerPatch patch) {
	    PlayerData data = PlayerData.get(player);

	    if (data == null || !data.isFormActive(ModDriveForms.ANTI)) {
	        return false;
	    }

	    Animator animator = patch.getAnimator();

	    animator.addLivingAnimation(LivingMotions.IDLE, KKAnimations.ANTI_FORM_IDLE);
	    animator.addLivingAnimation(LivingMotions.WALK, KKAnimations.ANTI_FORM_WALK);
	    animator.addLivingAnimation(LivingMotions.RUN, KKAnimations.ANTI_FORM_RUN);

	    SPChangeLivingMotion message = new SPChangeLivingMotion(player.getId());
	    message.putEntries(animator.getLivingAnimations().entrySet());
	    EpicFightNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(message, (ServerPlayer) player);
	    return true;
	}

	public static boolean needsAntiFormMotions(Player player) {
	    if (!KingdomKeys.efmLoaded || player == null || player.level().isClientSide) {
	        return false;
	    }

	    PlayerData data = PlayerData.get(player);

	    if (data == null || !data.isFormActive(ModDriveForms.ANTI)) {
	        return false;
	    }

	    ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
	    if (patch == null) {
	        return false;
	    }

	    return patch.getAnimator().getLivingAnimations().get(LivingMotions.IDLE) != KKAnimations.ANTI_FORM_IDLE;
	}
}
