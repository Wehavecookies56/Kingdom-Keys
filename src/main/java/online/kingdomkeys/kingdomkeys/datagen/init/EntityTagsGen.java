package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;

public class EntityTagsGen extends EntityTypeTagsProvider {

	public static final TagKey<EntityType<?>> HEARTLESS = create(KingdomKeys.MODID+":heartless");
	public static final TagKey<EntityType<?>> PUREBLOOD = create(KingdomKeys.MODID+":pureblood");
	public static final TagKey<EntityType<?>> EMBLEM = create(KingdomKeys.MODID+":emblem");
	public static final TagKey<EntityType<?>> NOBODY = create(KingdomKeys.MODID+":nobody");
	public static final TagKey<EntityType<?>> NPC = create(KingdomKeys.MODID+":npc");
	public static final TagKey<EntityType<?>> BOSS = create(KingdomKeys.MODID+":boss");

	public EntityTagsGen(DataGenerator generator, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), lookupProvider, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		for (RegistryObject<EntityType<?>> itemRegistryObject : ModEntities.ENTITIES.getEntries()) {
			final @NotNull EntityType<?> entityType = itemRegistryObject.get();
			String name = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType)).getPath();
			if (entityType.getBaseClass().isAssignableFrom(BaseKHEntity.class)) {
				IKHMob khenemy = (IKHMob) entityType;
				switch (khenemy.getKHMobType()) {
				case BOSS:
					add(BOSS,entityType);
					break;
				case HEARTLESS_EMBLEM:
					add(HEARTLESS,entityType);
					add(EMBLEM,entityType);
					break;
				case HEARTLESS_PUREBLOOD:
					add(HEARTLESS,entityType);
					add(PUREBLOOD,entityType);
					break;
				case NOBODY:
					add(NOBODY,entityType);
					break;
				case NPC:
					add(NPC,entityType);
					break;
				default:
					break;

				}
			}

		}
	}

	public void add(TagKey<EntityType<?>> boss2, @NotNull EntityType<?> entity) {
		this.tag(boss2).add(entity);
	}
	
	private static TagKey<EntityType<?>> create(String pName) {
		return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(pName));
	}
}
