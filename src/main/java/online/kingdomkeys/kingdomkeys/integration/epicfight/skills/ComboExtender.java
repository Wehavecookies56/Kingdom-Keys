package online.kingdomkeys.kingdomkeys.integration.epicfight.skills;

import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class ComboExtender extends WeaponInnateSkill {
    private final StaticAnimation[] attackAnimations;
    public ComboExtender(Builder<? extends Skill> builder, StaticAnimation[] attackAnimations) {
        super(builder);
        this.attackAnimations = attackAnimations;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        super.executeOnServer(executer, args);
    }

    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        return null;
    }

    public static class SpecialBuilder {
        private StaticAnimation[] attackAnimations;

        public SpecialBuilder setAttackAnimations(StaticAnimation... attackAnimations) {
            this.attackAnimations = attackAnimations;
            return this;
        }

        public ComboExtender build(Builder<? extends Skill> builder) {
            return new ComboExtender(builder, attackAnimations);
        }
    }
}
