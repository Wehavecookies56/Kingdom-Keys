package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.Encounter;

import java.util.Arrays;

public class RoomEncounterBuilder extends BuilderBase {

    public RoomEncounterBuilder(ResourceLocation location, Encounter encounter, ItemStack... rewards) {
        super(location);
        root.add("encounter", Encounter.CODEC.encodeStart(JsonOps.INSTANCE, encounter).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        JsonArray rewardsArray = new JsonArray();
        Arrays.stream(rewards).forEach(stack -> {
            rewardsArray.add(ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
        });
        root.add("rewards", rewardsArray);
    }

    public RoomEncounterBuilder music(SoundEvent music) {
        root.addProperty("music", music.getLocation().toString());
        return this;
    }
}
