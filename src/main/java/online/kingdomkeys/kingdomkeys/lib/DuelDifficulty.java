package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum DuelDifficulty implements StringRepresentable {
    EASY("easy", (byte) 0, 10),
    NORMAL("normal", (byte) 1, 25),
    HARD("hard", (byte) 2, 60);

    private final String name;
    private final byte id;
    private final int level;

    DuelDifficulty(String name, byte id, int level) {
        this.name = name;
        this.id = id;
        this.level = level;
    }

    public byte getID() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getTranslationKey() {
        return "kingdomkeys.duel.difficulty." + name;
    }

    public static DuelDifficulty fromByte(byte b) {
        for (DuelDifficulty value : values()) {
            if (value.id == b) {
                return value;
            }
        }
        return NORMAL;
    }

    public static final StreamCodec<FriendlyByteBuf, DuelDifficulty> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BYTE,
            DuelDifficulty::getID,
            DuelDifficulty::fromByte
    );

    @Override
    public String getSerializedName() {
        return name;
    }
}
