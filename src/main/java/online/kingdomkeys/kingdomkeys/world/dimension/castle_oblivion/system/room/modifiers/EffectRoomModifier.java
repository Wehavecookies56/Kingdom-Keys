package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public class EffectRoomModifier extends RoomModifier {

    Holder<MobEffect> effect;

    public EffectRoomModifier(ResourceLocation modifierName, Holder<MobEffect> effect) {
        super(modifierName);
        this.effect = effect;
    }


    @Override
    public void onEnter(Room room, Player player) {
        player.addEffect(new MobEffectInstance(effect, -1, 0, false, true, true));
    }

    @Override
    public void onGenerate(Room room) {

    }

    @Override
    public void onExit(Room room, Player player) {
        player.removeEffect(effect);
    }

    @Override
    public void tick(Room room, List<Player> players) {

    }
}
