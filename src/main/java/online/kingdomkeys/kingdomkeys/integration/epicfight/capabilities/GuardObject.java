package online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities;

import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.guard.GuardSkill;

public class GuardObject {
    private final StaticAnimation guardHit;
    private final StaticAnimation guardBreak;
    private final StaticAnimation advancedGuard;

    public GuardObject(StaticAnimation guardHit, StaticAnimation guardBreak, StaticAnimation advancedGuard)
    {
        this.guardHit = guardHit;
        this.guardBreak = guardBreak;
        this.advancedGuard = advancedGuard;
    }

    public StaticAnimation getGuardAnimation(GuardSkill.BlockType blockType)
    {
        return switch (blockType) {
            case GUARD -> guardHit;
            case GUARD_BREAK -> guardBreak;
            case ADVANCED_GUARD -> advancedGuard;
        };
    }

}
