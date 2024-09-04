package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Function;

public class EpicFightUtils {
    private EpicFightUtils() {}

    private static Function<Player, Boolean> battleMode = player -> false;

    public static final EpicFightUtils INSTANCE = new EpicFightUtils();

    public static void setBattleMode(Function<Player, Boolean> battleMode) {
        EpicFightUtils.battleMode = battleMode;
    }

    public static boolean isBattleMode(Player player) {
        return battleMode.apply(player);
    }
}