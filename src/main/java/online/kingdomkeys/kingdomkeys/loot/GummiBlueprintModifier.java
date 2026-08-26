package online.kingdomkeys.kingdomkeys.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiShipLoader;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GummiBlueprintModifier extends LootModifier {

	public static final Supplier<MapCodec<GummiBlueprintModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
			.and(ResourceLocation.CODEC.fieldOf("ship").forGetter(modifier -> modifier.ship))
			.apply(inst, GummiBlueprintModifier::new)));

	private final ResourceLocation ship;

	public GummiBlueprintModifier(LootItemCondition[] conditions, ResourceLocation ship) {
		super(conditions);
		this.ship = ship;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		GummiStructure structure = GummiShipLoader.get(ship, context.getLevel().registryAccess());

		if (structure == null) {
			KingdomKeys.LOGGER.warn("A loot modifier asked for the gummi ship {}, which no data pack provides", ship);
			return generatedLoot;
		}

		ItemStack blueprint = new ItemStack(ModItems.gummiShipBlueprint.get());

		blueprint.set(ModComponents.GUMMI_STRUCTURE, structure.withoutBlockEntities());
		blueprint.set(ModComponents.BLUEPRINT_NAME, structure.getName());

		generatedLoot.add(blueprint);
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
