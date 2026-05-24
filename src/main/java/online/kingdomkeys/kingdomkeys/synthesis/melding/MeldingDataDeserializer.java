package online.kingdomkeys.kingdomkeys.synthesis.melding;

import com.google.gson.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class MeldingDataDeserializer implements JsonDeserializer<Melding>{
	@Override
	public Melding deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Melding out = new Melding();
		JsonObject jsonObject = json.getAsJsonObject();

		jsonObject.entrySet().forEach(entry -> {
			JsonElement element = entry.getValue();
			switch (entry.getKey()) {//Check for the first level key
				case "ingredient1" -> out.setIngredient1(getIngredient(element));
				case "ingredient2" -> out.setIngredient2(getIngredient(element));
				case "cost" -> out.setCost(element.getAsInt());
				case "exp" -> out.setExp(element.getAsInt());
				case "output" -> {
					JsonObject outputObject = element.getAsJsonObject();
					if (!outputObject.has("item") || !outputObject.has("quantity")) {
						throw new JsonParseException("Output missing item/quantity: " + json);
					}

					Item keychain = BuiltInRegistries.ITEM.get(ResourceLocation.parse(outputObject.get("item").getAsString()));
					out.setResult(keychain, outputObject.get("quantity").getAsInt());
					out.setType(outputObject.get("type").getAsString());
				}
				case "tier" -> out.setTier(element.getAsInt());
			}
		});
		KingdomKeys.LOGGER.info("OUTPUT: {}, TYPE {}, QUANTITY: {}, INGREDIENT 1: {}, INGREDIENT 2: {}", out.result, out.type, out.amount, out.ingredient1, out.ingredient2);
		return out;
	}

	private static @NotNull Item getIngredient(JsonElement element) {
		ResourceLocation id = ResourceLocation.parse(element.getAsString());
		Item item = BuiltInRegistries.ITEM.get(id);
		if (item == Items.AIR) {
			throw new JsonParseException("Material supplied in recipe cannot be found in registry: " + id);
		}

		return item;
	}
}
