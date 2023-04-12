package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import yesman.epicfight.api.animation.LivingMotion;

public enum KKLivingMotionsEnum implements LivingMotion {
    SPELL;
    private int id;
    KKLivingMotionsEnum() {
        		this.id = LivingMotion.ENUM_MANAGER.assign(this);

    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}
