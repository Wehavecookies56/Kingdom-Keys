package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.encounter.Encounter;

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

    public RoomEncounterBuilder payout(int experience, int lux) {
        root.addProperty("experience", experience);
        root.addProperty("lux", lux);
        return this;
    }

    public RoomEncounterBuilder level(int level) {
        root.addProperty("level", level);
        return this;
    }

    public RoomEncounterBuilder requires(ResourceLocation... flags) {
        JsonArray needed = new JsonArray();
        Arrays.stream(flags).forEach(flag -> needed.add(flag.toString()));
        root.add("requires", needed);
        return this;
    }

    public RoomEncounterBuilder grants(ResourceLocation flag) {
        root.addProperty("grants", flag.toString());
        return this;
    }

    public RoomEncounterBuilder arena(int radius, int spawnPoints) {
        root.addProperty("arena_radius", radius);
        root.addProperty("spawn_points", spawnPoints);
        return this;
    }

    /** What the information plaque says when it starts. */
    public RoomEncounterBuilder info(String translationKey) {
        root.addProperty("info", translationKey);
        return this;
    }

    public RoomEncounterBuilder music(SoundEvent music) {
        root.addProperty("music", music.getLocation().toString());
        return this;
    }
}
