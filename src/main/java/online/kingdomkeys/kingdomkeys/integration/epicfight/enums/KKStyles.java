//package online.kingdomkeys.kingdomkeys.integration.epicfight.enums;
//
//import yesman.epicfight.world.capabilities.item.Style;
//
//public enum KKStyles implements Style {
//    VALOR(true), WISDOM(false), MASTER(true), FINAL(true), SORA(false),
//    ROXAS(false), TERRA(false), AQUA(false), VENTUS(false), RIKU(false),
//    KH2_ROXAS_DUAL(true), DAYS_ROXAS_DUAL(true);
//    private final boolean canUseOffhand;
//    private final int id;
//
//    KKStyles(boolean canUseOffhand) {
//        this.id = Style.ENUM_MANAGER.assign(this);
//        this.canUseOffhand = canUseOffhand;
//    }
//
//    @Override
//    public boolean canUseOffhand() {
//        return canUseOffhand;
//    }
//
//    @Override
//    public int universalOrdinal() {
//        return id;
//    }
//}
