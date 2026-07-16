package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
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
import java.util.ArrayList;
import java.util.List;

public class TreasureChestTileEntity extends BlockEntity {

    EntityType<?> trapEntity;
    List<ItemStack> treasure = new ArrayList<>();

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
            te.setTrapEntity(toSpawn.get(Utils.randomWithRange(0, toSpawn.size()-1)));
        } else {
            LootTable table = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, treasure.lootTable()));
            if (table.equals(LootTable.EMPTY)) {
                throw new IllegalArgumentException("Invalid loot table for treasure");
            }
            LootParams params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).create(LootContextParamSets.CHEST);

            te.setTreasure(table.getRandomItems(params));
        }
        level.setBlockEntity(te);
    }

    private boolean isTrapped() {
        return trapEntity != null;
    }

    public void setTrapEntity(EntityType<?> trapEntity) {
        this.trapEntity = trapEntity;
    }

    public void setTreasure(List<ItemStack> treasure) {
        this.treasure = new ArrayList<>(treasure);
    }

    public boolean open(Player player) {
        if (isTrapped()) {
            LivingEntity toSpawn = (LivingEntity) trapEntity.create(level);
            if (toSpawn != null) {
                GlobalData globalData = GlobalData.get(toSpawn);
                if (CastleOblivionHandler.isInterior(level.dimension())) {
                    CastleOblivionData.InteriorData.get((ServerLevel) level).ifPresent(interiorData -> {
                        Room room = interiorData.getRoomAtPos(getBlockPos());
                        room.addEntityToCache(toSpawn);
                        globalData.setLevel(((room.parentFloor + 1) * 10) + 5 + Utils.randomWithRange(-3, 3));
                        room.modifierOnSpawn(toSpawn);
                        globalData.setCastleOblivionMarker(true);
                    });
                }
                toSpawn.moveTo((double) getBlockPos().getX() + 0.5, getBlockPos().getY(), (double) getBlockPos().getZ() + 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
                level.playSound(null, getBlockPos(), ModSounds.portal.get(), SoundSource.HOSTILE, 2,2F);
                ((ServerLevel) level).addFreshEntityWithPassengers(toSpawn);
                if (toSpawn instanceof Mob spawnedMob) {
                    EventHooks.finalizeMobSpawn(spawnedMob, (ServerLevel) level, level.getCurrentDifficultyAt(getBlockPos()), MobSpawnType.TRIAL_SPAWNER, null);
                }
            }
            trapEntity = null;
            level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        } else if (!treasure.isEmpty()) {
            Utils.giveItems((ServerPlayer) player, treasure);
            treasure = new ArrayList<>();
            level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);

        this.treasure = ItemStack.CODEC.listOf().parse(NbtOps.INSTANCE, pTag.get("treasure")).getPartialOrThrow();
        if (pTag.contains("trap_entity")) {
            this.trapEntity = BuiltInRegistries.ENTITY_TYPE.byNameCodec().parse(NbtOps.INSTANCE, pTag.get("trap_entity")).getPartialOrThrow();
        }

    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        pTag.put("treasure", ItemStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, treasure).getPartialOrThrow());
        if (isTrapped()) {
            pTag.put("trap_entity", BuiltInRegistries.ENTITY_TYPE.byNameCodec().encodeStart(NbtOps.INSTANCE, trapEntity).getPartialOrThrow());
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveCustomOnly(pRegistries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

}
