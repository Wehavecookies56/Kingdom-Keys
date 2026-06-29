package online.kingdomkeys.kingdomkeys.lib;

import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Map;

public class Constants {

    public static final int TUTORIAL_CO_CASTLE = 0;
    public static final int TUTORIAL_CO_LOBBY = 1;


    public static final Map<Integer, List<Utils.Title>> TUTORIALS = Map.of(
    TUTORIAL_CO_CASTLE, List.of(),

    TUTORIAL_CO_LOBBY, List.of(
            new Utils.Title("", Strings.COIntro1),
            new Utils.Title("", Strings.COIntro2),
            new Utils.Title("", Strings.COIntro3),
            new Utils.Title(Strings.COIntroTitle, "").setKHFont()
    ));

    public static final int
    // Input
    LEFT_MOUSE = 0,
    RIGHT_MOUSE = 1,
    MIDDLE_MOUSE = 2,
    WHEEL_UP = 1,
    WHEEL_DOWN = -1;

    // Drive abilities
    public static double[]
            VALOR_SPEED = {0,1.1,1.1,1.1,1.1,1.1,1.1,1.1},
            VALOR_JUMP = {0,0.020,0.020,0.025,0.025,0.030,0.030,0.035},

    WISDOM_QR = {0,2,2,3,3,4,4,4.5},

    LIMIT_DR = {0,2.5,2.5,3.5,3.5,4.5,4.5,5},

    MASTER_SPEED = {0,1.8,1.8,1.8,1.8,1.8,1.8,1.8},
            MASTER_JUMP = {0,0.015,0.015,0.0175,0.0175,0.02,0.02,0.022},
            MASTER_SECOND_JUMP = {0,1.25,1.25,1.5,1.5,1.75,1.75,2},


    FINAL_SPEED = {0,1.23,1.23,1.23,1.23,1.23,1.23,1.23},
            FINAL_GLIDE_SPEED = {0,1.23,1.23,1.23,1.23,1.23,1.23,1.23},
            FINAL_JUMP = {0,0.03,0.03,0.03,0.03,0.03,0.03,0.03},
            FINAL_GLIDE = {0,0.9,0.9,0.8,0.8,0.6,0.6,0.4}
                    ;

    public static final double
            PLAYER_WALKSPEED = 0.10000000149011612D,
            PLAYER_JUMP = 0.42D;

}
