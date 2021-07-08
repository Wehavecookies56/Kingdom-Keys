package online.kingdomkeys.kingdomkeys.lib;

import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;

public enum SoAState {
    NONE((byte)0), CHOICE((byte)1), SACRIFICE((byte)2), CONFIRM((byte)3), COMPLETE((byte)4), WARRIOR((byte)5), GUARDIAN((byte)6), MYSTIC((byte)7);

    private final byte b;
    SoAState(byte b) {
        this.b = b;
    }
    public byte get() {
        return b;
    }

    private boolean Compare(byte b) { return this.b == b; }

    public static SoAState fromByte(byte b) {
        SoAState[] values = SoAState.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].Compare(b)) {
                return values[i];
            }
        }
        return NONE;
    }

    //TODO make choices more substantial
    public static void applyStatsForChoices(IPlayerCapabilities playerData) {
        if (playerData.getSoAState() == COMPLETE) {
            SoAState choice = playerData.getChosen();
            SoAState sacrifice = playerData.getSacrificed();
            switch (choice) {
                case WARRIOR:
                    playerData.setStrength(playerData.getStrength(false) + 1);
                    break;
                case GUARDIAN:
                    playerData.setDefense(playerData.getDefense(false) + 1);
                    break;
                case MYSTIC:
                    playerData.setMagic(playerData.getMagic(false) + 1);
                    break;
            }

            switch (sacrifice) {
                case WARRIOR:
                    playerData.setStrength(playerData.getStrength(false) - 1);
                    break;
                case GUARDIAN:
                    playerData.setDefense(playerData.getDefense(false) - 1);
                    break;
                case MYSTIC:
                    playerData.setMagic(playerData.getMagic(false) - 1);
                    break;
            }
        }
    }
}
