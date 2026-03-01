package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import yesman.epicfight.EpicFight;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.player.SkillCastEvent;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;

public class ComboExtender extends Skill {
	private final DeferredHolder<SkillDataKey<?>, SkillDataKey<Integer>> combo = EpicFightSkillDataKeys.COMBO_COUNTER;
	public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(EpicFight.MODID, "skill_data_keys"), KingdomKeys.MODID);
	public static final DeferredHolder<SkillDataKey<?>, SkillDataKey<Integer>> FINISHER_DATA = DATA_KEYS.register("finisher_data", () -> SkillDataKey.createSkillDataKey(ByteBufCodecs.INT, 0, false, ComboExtender.class));
	//private final SkillDataKey<Integer> finisherData = SkillDataKey.createDataKey(ValueType.INTEGER);
	public int numberOfNegativeCombo = 0;
	public int numberOfComboPlus = 0;
	public int numberOfFinishingPlus = 0;
	public int totalComboOffset = 0;
	public int finisherPlacement = 0;
	public final int lastBasicAttackFromEnd = 4;

	public ComboExtender(SkillBuilder<?> builder) {
		super(builder);
	}

    public static void skillCastEvent(SkillCastEvent event) {
        SkillContainer skillContainer = event.getSkillContainer();
        if (skillContainer.getSkill() instanceof ComboExtender skill) {
        PlayerPatch<?> spp = skillContainer.getExecutor();
        Player player = spp.getOriginal();
        if (player.onGround() && !player.isSprinting() && event.getSkillContainer().getSkill() == EpicFightSkills.COMBO_ATTACKS.get()) {
            if (!skill.isExecutableState(spp))
                return;
            PlayerData playerCapabilities = PlayerData.get(player);
            event.cancel();
            AnimationManager.AnimationAccessor<? extends AttackAnimation> attackMotion;
            skill.numberOfComboPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.comboPlus);
            skill.numberOfNegativeCombo = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.negativeCombo);
            skill.numberOfFinishingPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.finishingPlus);
            skill.totalComboOffset = skill.numberOfComboPlus - skill.numberOfNegativeCombo;

            CapabilityItem cap = spp.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> combo = cap.getAutoAttackMotion(spp);
            SkillDataManager dataManager = spp.getSkill(EpicFightSkills.COMBO_ATTACKS.get()).getDataManager();
            int comboCounter = dataManager.getDataValue(skill.combo);

            int comboSize = combo.size();
            if ((comboSize - skill.lastBasicAttackFromEnd) + skill.totalComboOffset < 0)
                skill.totalComboOffset -= (comboSize - skill.lastBasicAttackFromEnd) + skill.totalComboOffset;

            if (comboCounter >= (comboSize - skill.lastBasicAttackFromEnd) + skill.totalComboOffset) {
                SkillDataManager finishDataManager = spp.getSkill(skill).getDataManager();
                if (finishDataManager.getDataValue(FINISHER_DATA) == null) {
                    skillContainer.getDataManager().registerData(FINISHER_DATA);
                    skillContainer.getDataManager().setData(FINISHER_DATA, 0);
                }
                skill.finisherPlacement = finishDataManager.getDataValue(FINISHER_DATA);
                int finisher = (skill.finisherPlacement % 2) + (comboSize - skill.lastBasicAttackFromEnd);
                if (comboCounter >= (comboSize - skill.lastBasicAttackFromEnd) + skill.totalComboOffset + skill.numberOfFinishingPlus) {
                    comboCounter = 0;
                    skill.finisherPlacement = 0;
                } else {
                    comboCounter++;
                    skill.finisherPlacement++;
                }
                attackMotion = combo.get(finisher);
                finishDataManager.setData(FINISHER_DATA, skill.finisherPlacement);
            } else {
                attackMotion = combo.get(comboCounter % (comboSize - 4));
                comboCounter++;
            }

            if (attackMotion != null) {
                spp.playAnimationSynchronized(attackMotion, 0);
            }
            dataManager.setData(skill.combo, comboCounter);
            spp.updateEntityState();
        }
        }
    }

	@Override
	public boolean isExecutableState(PlayerPatch<?> executor) {
		EntityState playerState = executor.getEntityState();
		Player player = executor.getOriginal();

		return !(player.isSpectator() || executor.isInAir() || !playerState.canBasicAttack());
	}
}
