package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import yesman.epicfight.world.capabilities.item.Style;

public enum KKStyles implements Style {
    VALOR(true), WISDOM(false), MASTER(true), FINAL(true);
    private final boolean canUseOffhand;
    private final int id;

    KKStyles(boolean canUseOffhand) {
        this.id = Style.ENUM_MANAGER.assign(this);
        this.canUseOffhand = canUseOffhand;
    }

    @Override
    public boolean canUseOffhand() {
        return canUseOffhand;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}
