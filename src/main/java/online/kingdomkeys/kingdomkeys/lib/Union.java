package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum Union implements StringRepresentable {
    NONE("none", (byte) 0, 0xFFFFFF),
    UNICORNIS("unicornis", (byte) 1, 0xF2C230),
    LEOPARDOS("leopardos", (byte) 2, 0x4FA5DE),
    VULPES("vulpes", (byte) 3, 0x8E56C4),
    ANGUIS("anguis", (byte) 4, 0x54B948),
    URSUS("ursus", (byte) 5, 0xD34C3E);

    private final String name;
    private final byte b;
    private final int colour;

    Union(String name, byte b, int colour) {
        this.name = name;
        this.b = b;
        this.colour = colour;
    }

    public byte get() {
        return b;
    }

    public int getColour() {
        return colour;
    }

    private boolean Compare(byte b) {
        return this.b == b;
    }

    public static Union fromByte(byte b) {
        Union[] values = Union.values();
        for (Union value : values) {
            if (value.Compare(b)) {
                return value;
            }
        }
        return NONE;
    }

    //Order they appear in the SoA, based on the texture order.
    public static Union[] choosable() {
        return new Union[] { UNICORNIS, ANGUIS, LEOPARDOS, VULPES, URSUS };
    }

    public String getTranslationKey() {
        return "kingdomkeys.union." + name;
    }

    public String getDescriptionKey() {
        return "kingdomkeys.union." + name + ".desc";
    }

    public static final StreamCodec<FriendlyByteBuf, Union> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE,
            Union::get,
            Union::fromByte
    );

    @Override
    public String getSerializedName() {
        return name;
    }
}
