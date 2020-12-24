package online.kingdomkeys.kingdomkeys.lib;

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
}
