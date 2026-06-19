package online.kingdomkeys.kingdomkeys.util;

import com.mojang.serialization.Codec;

import java.awt.*;
import java.util.List;

public class Codecs {

    public static final Codec<Color> COLOR_CODEC_RGB = Codec.INT.listOf(3, 3).xmap(
            integers -> new Color(integers.get(0), integers.get(1), integers.get(2)),
            color -> List.of(color.getRed(), color.getGreen(), color.getBlue()));

    public static final Codec<Color> COLOR_CODEC_HEX = Codec.STRING.xmap(
            Color::decode,
            color -> String.format("#%06X", color.getRGB() & 0xFFFFFF));

}
