package online.kingdomkeys.kingdomkeys.world.worldmap;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.lang.reflect.Type;

public class GummiWorldDeserializer implements JsonDeserializer<GummiWorld> {
	private static final double DEFAULT_TAKEOFF = 320;
	private static final float DEFAULT_SCALE = 24F;

	@Override
	public GummiWorld deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();

		ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, resource(obj, "dimension"));
		double takeoff = obj.has("takeoff_altitude") ? obj.get("takeoff_altitude").getAsDouble() : DEFAULT_TAKEOFF;
		Vec3 worldmapPosition = vec(obj, "worldmap_position");
		Vec3 landingSpawn = vec(obj, "landingSpawn");
		float scale = obj.has("scale") ? obj.get("scale").getAsFloat() : DEFAULT_SCALE;

		Vec3 takeOffSpawn = obj.has("takeoffSpawn") ? vec(obj, "takeoffSpawn") : worldmapPosition.add(0, 0, scale);
		double approachRange = obj.has("approach_range") ? obj.get("approach_range").getAsDouble() : scale;

		ResourceLocation texture = obj.has("texture") ? ResourceLocation.parse(obj.get("texture").getAsString()) : KingdomKeys.rl("textures/worldmap/" + dimension.location().getPath() + ".png");

		// Leaving either of these out keeps whichever way the player was already facing
		Vec2 takeOffLook = look(obj, "takeoffRotation");
		Vec2 landingLook = look(obj, "landingRotation");

		// Both default to true
		boolean build = !obj.has("build") || obj.get("build").getAsBoolean();
		boolean vanillaMobs = !obj.has("vanilla_mobs") || obj.get("vanilla_mobs").getAsBoolean();

		return new GummiWorld(dimension, takeoff, worldmapPosition, takeOffSpawn, landingSpawn, takeOffLook, landingLook, texture, scale, approachRange, build, vanillaMobs);
	}

	private static ResourceLocation resource(JsonObject obj, String key) {
		if (!obj.has(key)) {
			throw new JsonParseException("Gummi world is missing the required field '" + key + "'");
		}
		return ResourceLocation.parse(obj.get(key).getAsString());
	}

	// A pair of degrees, yaw and pitch
	private static Vec2 look(JsonObject obj, String key) {
		if (!obj.has(key)) {
			return null;
		}

		JsonArray array = obj.getAsJsonArray(key);

		if (array.size() != 2) {
			throw new JsonParseException("Field '" + key + "' must hold two numbers, a yaw and a pitch in " + obj);
		}

		return new Vec2(array.get(0).getAsFloat(), array.get(1).getAsFloat());
	}

	private static Vec3 vec(JsonObject obj, String key) {
		if (!obj.has(key)) {
			throw new JsonParseException("Gummi world is missing the required field '" + key + "'");
		}
		JsonArray array = obj.getAsJsonArray(key);
		if (array.size() != 3) {
			throw new JsonParseException("Field '" + key + "' must hold exactly three numbers");
		}
		return new Vec3(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
	}
}
