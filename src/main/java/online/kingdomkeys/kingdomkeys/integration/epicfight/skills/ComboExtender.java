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
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.neoevent.playerpatch.SkillCastEvent;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.registry.entries.EpicFightSkillDataKeys;
import yesman.epicfight.registry.entries.EpicFightSkills;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;

public class ComboExtender extends Skill {
	private final DeferredHolder<SkillDataKey<?>, SkillDataKey<Integer>> combo = EpicFightSkillDataKeys.COMBO_COUNTER;
	public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "skill_data_keys"), KingdomKeys.MODID);
	public static final DeferredHolder<SkillDataKey<?>, SkillDataKey<Integer>> FINISHER_DATA = DATA_KEYS.register("finisher_data", () -> SkillDataKey.createSkillDataKey(ByteBufCodecs.INT, 0, false, ComboExtender.class));
	//private final SkillDataKey<Integer> finisherData = SkillDataKey.createDataKey(ValueType.INTEGER);
	private int numberOfNegativeCombo = 0;
	private int numberOfComboPlus = 0;
	private int numberOfFinishingPlus = 0;
	private int totalComboOffset = 0;
	private int finisherPlacement = 0;
	private final int lastBasicAttackFromEnd = 4;

	public ComboExtender(SkillBuilder<?> builder) {
		super(builder);
	}

    @SkillEvent(caller = KingdomKeys.MODID, side = SkillEvent.Side.SERVER)
    public void skillCastEvent(SkillCastEvent event, SkillContainer skillContainer) {
        PlayerPatch<?> spp = skillContainer.getExecutor();
        Player player = spp.getOriginal();
        if (player.onGround() && !player.isSprinting() && event.getSkillContainer().getSkill() == EpicFightSkills.COMBO_ATTACKS.get()) {
            if (!this.isExecutableState(spp))
                return;
            PlayerData playerCapabilities = PlayerData.get(player);
            event.setCanceled(true);
            AnimationManager.AnimationAccessor<? extends AttackAnimation> attackMotion;
            this.numberOfComboPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.comboPlus);
            this.numberOfNegativeCombo = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.negativeCombo);
            this.numberOfFinishingPlus = playerCapabilities.getNumberOfAbilitiesEquipped(Strings.finishingPlus);
            this.totalComboOffset = this.numberOfComboPlus - this.numberOfNegativeCombo;

            CapabilityItem cap = spp.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> combo = cap.getAutoAttackMotion(spp);
            SkillDataManager dataManager = spp.getSkill(EpicFightSkills.COMBO_ATTACKS.get()).getDataManager();
            int comboCounter = dataManager.getDataValue(this.combo);

            int comboSize = combo.size();
            if ((comboSize - lastBasicAttackFromEnd) + this.totalComboOffset < 0)
                this.totalComboOffset -= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset;

            if (comboCounter >= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset) {
                SkillDataManager finishDataManager = spp.getSkill(this).getDataManager();
                if (finishDataManager.getDataValue(FINISHER_DATA) == null) {
                    skillContainer.getDataManager().registerData(FINISHER_DATA);
                    skillContainer.getDataManager().setData(FINISHER_DATA, 0);
                }
                finisherPlacement = finishDataManager.getDataValue(FINISHER_DATA);
                int finisher = (finisherPlacement % 2) + (comboSize - lastBasicAttackFromEnd);
                if (comboCounter >= (comboSize - lastBasicAttackFromEnd) + this.totalComboOffset + numberOfFinishingPlus) {
                    comboCounter = 0;
                    finisherPlacement = 0;
                } else {
                    comboCounter++;
                    finisherPlacement++;
                }
                attackMotion = combo.get(finisher);
                finishDataManager.setData(FINISHER_DATA, finisherPlacement);
            } else {
                attackMotion = combo.get(comboCounter % (comboSize - 4));
                comboCounter++;
            }

            if (attackMotion != null) {
                spp.playAnimationSynchronized(attackMotion, 0);
            }
            dataManager.setData(this.combo, comboCounter);
            spp.updateEntityState();

        }
    }

	@Override
	public boolean isExecutableState(PlayerPatch<?> executor) {
		EntityState playerState = executor.getEntityState();
		Player player = executor.getOriginal();

		return !(player.isSpectator() || executor.isInAir() || !playerState.canBasicAttack());
	}
}
