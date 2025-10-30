package online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.guard.GuardSkill;

public class GuardObject {
    private final AnimationManager.AnimationAccessor<? extends StaticAnimation> guardHit;
    private final AnimationManager.AnimationAccessor<? extends StaticAnimation> guardBreak;
    private final AnimationManager.AnimationAccessor<? extends StaticAnimation> advancedGuard;

    public GuardObject(AnimationManager.AnimationAccessor<? extends StaticAnimation> guardHit, AnimationManager.AnimationAccessor<? extends StaticAnimation> guardBreak, AnimationManager.AnimationAccessor<? extends StaticAnimation> advancedGuard) {
        this.guardHit = guardHit;
        this.guardBreak = guardBreak;
        this.advancedGuard = advancedGuard;
    }

    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getGuardAnimation(GuardSkill.BlockType blockType) {
        return switch (blockType) {
            case GUARD -> guardHit;
            case GUARD_BREAK -> guardBreak;
            case ADVANCED_GUARD -> advancedGuard;
        };
    }
}
