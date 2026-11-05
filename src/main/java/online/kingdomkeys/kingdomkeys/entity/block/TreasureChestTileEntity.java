package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static online.kingdomkeys.kingdomkeys.lib.Strings.Message_Chest;

public class TreasureChestTileEntity extends BlockEntity {

	private static final int EDITOR_SIZE = 54;
	// Players that already took their loot from this chest
	private final Set<UUID> looted = new HashSet<>();
	EntityType<?> trapEntity;
	// Fixed contents, given as they are to every player. Set from the creative editor.
	List<ItemStack> treasure = new ArrayList<>();
	// When set, the table is rolled separately for every player that opens the chest
	@Nullable
	ResourceKey<LootTable> lootTable;

	public TreasureChestTileEntity(BlockPos pos, BlockState blockState) {
		super(ModEntities.TYPE_TREASURE_CHEST.get(), pos, blockState);
	}

	public static void create(ServerLevel level, BlockPos pos, BlockState blockState, RoomType.Treasure treasure, boolean trapped) {
		TreasureChestTileEntity te = new TreasureChestTileEntity(pos, blockState);
		if (trapped) {
			TagKey<EntityType<?>> trapEntities = treasure.trappedEntities();
			if (treasure.trappedEntities() == null) {
				trapEntities = ModTags.CO_REGULAR_ENEMIES;
			}
			List<? extends EntityType<?>> toSpawn = ModTags.getEntitiesInTag(level, trapEntities);
			te.setTrapEntity(toSpawn.get(Utils.randomWithRange(0, toSpawn.size() - 1)));
		} else {
			ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, treasure.lootTable());
			LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
			if (table.equals(LootTable.EMPTY)) {
				throw new IllegalArgumentException("Invalid loot table for treasure");
			}
			// Not rolled here anymore, every player rolls their own when they open it
			te.setLootTable(key);
		}
		level.setBlockEntity(te);
	}

	public boolean isTrapped() {
		return trapEntity != null;
	}

	public void setTrapEntity(EntityType<?> trapEntity) {
		this.trapEntity = trapEntity;
	}

	public void setTreasure(List<ItemStack> treasure) {
		this.treasure = new ArrayList<>(treasure);
	}

	public void setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
		this.lootTable = lootTable;
		setChanged();
	}

	// Creative + sneaking on a chest that has no loot table (and isn't a trap) opens the editor
	public boolean canEdit(Player player) {
		return player.isCreative() && player.isSecondaryUseActive() && !isTrapped() && lootTable == null;
	}

	public void openEditor(ServerPlayer player) {
		SimpleContainer container = new SimpleContainer(EDITOR_SIZE) {
			@Override
			public void stopOpen(Player p) {
				super.stopOpen(p);
				saveEditor(this);
			}
		};
		for (int i = 0; i < treasure.size() && i < EDITOR_SIZE; i++) {
			container.setItem(i, treasure.get(i).copy());
		}
		player.openMenu(new SimpleMenuProvider((id, inventory, p) -> ChestMenu.sixRows(id, inventory, container), getBlockState().getBlock().getName()));
	}

	private void saveEditor(SimpleContainer container) {
		List<ItemStack> newItems = container.removeAllItems();
		boolean changed = lootTable != null || !ItemStack.listMatches(treasure, newItems);
		this.treasure = newItems;
		this.lootTable = null;
		if (changed) { //So players that have already taken it can reclaim new prizes
			this.looted.clear();
		}
		setChanged();
		if (level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	public boolean open(Player player) {
		if (!(level instanceof ServerLevel serverLevel)) {
			return false;
		}
		BlockPos pos = getBlockPos();

		if (isTrapped()) {
			LivingEntity toSpawn = (LivingEntity) trapEntity.create(serverLevel);
			if (toSpawn != null) {
				GlobalData globalData = GlobalData.get(toSpawn);
				if (CastleOblivionHandler.isInterior(serverLevel.dimension())) {
					CastleOblivionData.InteriorData.get(serverLevel).ifPresent(interiorData -> {
						Room room = interiorData.getRoomAtPos(pos);
						room.addEntityToCache(toSpawn);
						globalData.setLevel(((room.parentFloor + 1) * 10) + 5 + Utils.randomWithRange(-3, 3));
						room.modifierOnSpawn(toSpawn);
						globalData.setCastleOblivionMarker(true);
					});
				}
				toSpawn.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(serverLevel.random.nextFloat() * 360.0F), 0.0F);
				serverLevel.playSound(null, pos, ModSounds.portal.get(), SoundSource.HOSTILE, 2, 2F);
				serverLevel.addFreshEntityWithPassengers(toSpawn);
				if (toSpawn instanceof Mob spawnedMob) {
					EventHooks.finalizeMobSpawn(spawnedMob, serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.TRIAL_SPAWNER, null);
				}
			}
			trapEntity = null;
			serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			return true;
		}

		if ((lootTable == null && treasure.isEmpty()) || !(player instanceof ServerPlayer serverPlayer)) {
			return false;
		}
		if (!looted.add(player.getUUID())) {
			player.displayClientMessage(Component.translatable(Message_Chest + ".already_looted"), true);
			return true;
		}
		setChanged();
		serverLevel.playSound(null, pos, ModSounds.openChest.get(), SoundSource.PLAYERS, 1F, 1F);
		List<ItemStack> items = rollLoot(serverLevel, serverPlayer);
		Utils.giveItems(serverPlayer, items.size() == 1, items);
		return true;
	}

	private List<ItemStack> rollLoot(ServerLevel serverLevel, ServerPlayer player) {
		if (lootTable == null) {
			return treasure.stream().map(ItemStack::copy).collect(Collectors.toCollection(ArrayList::new));
		}
		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(getBlockPos())).withOptionalParameter(LootContextParams.THIS_ENTITY, player).withLuck(player.getLuck()).create(LootContextParamSets.CHEST);
		return new ArrayList<>(serverLevel.getServer().reloadableRegistries().getLootTable(lootTable).getRandomItems(params));
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.loadAdditional(pTag, provider);

		if (pTag.contains("treasure")) {
			this.treasure = ItemStack.CODEC.listOf().parse(NbtOps.INSTANCE, pTag.get("treasure")).getPartialOrThrow();
		}
		this.trapEntity = pTag.contains("trap_entity") ? BuiltInRegistries.ENTITY_TYPE.byNameCodec().parse(NbtOps.INSTANCE, pTag.get("trap_entity")).getPartialOrThrow() : null;
		this.lootTable = pTag.contains("loot_table") ? ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(pTag.getString("loot_table"))) : null;
		this.looted.clear();
		for (Tag tag : pTag.getList("looted", Tag.TAG_INT_ARRAY)) {
			this.looted.add(NbtUtils.loadUUID(tag));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.saveAdditional(pTag, provider);
		pTag.put("treasure", ItemStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, treasure).getPartialOrThrow());
		if (isTrapped()) {
			pTag.put("trap_entity", BuiltInRegistries.ENTITY_TYPE.byNameCodec().encodeStart(NbtOps.INSTANCE, trapEntity).getPartialOrThrow());
		}
		if (lootTable != null) {
			pTag.putString("loot_table", lootTable.location().toString());
		}
		ListTag lootedTag = new ListTag();
		for (UUID uuid : looted) {
			lootedTag.add(NbtUtils.createUUID(uuid));
		}
		pTag.put("looted", lootedTag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	// The client only needs to know if it is a trap or has a loot table, not the items or who looted it
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = saveCustomOnly(registries);
		tag.remove("treasure");
		tag.remove("looted");
		return tag;
	}
}
