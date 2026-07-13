package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomTypeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomEnemies;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType.Enemies;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.EffectRoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.EffectRoomModifier.EffectType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.LevelModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.LevelModifier.Operation;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.LevelModifier.Operator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.SpawnMobModifier;

import java.awt.*;
import java.util.List;

public class RoomTypesGen extends BaseProvider<RoomTypeBuilder> {

    public RoomTypesGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "castle_oblivion/room_type");
    }

    @Override
    protected void build() {
        createRoomType(Strings.AlchemicWaking, RoomSize.M, RoomCategory.STATUS)
                .enemies(new Enemies(RoomEnemies.S, 6, 3));
        createRoomType(Strings.AlmightyDarkness, RoomSize.M, RoomCategory.ENEMY).enemies(new Enemies(RoomEnemies.M, 7, 3))
                .modifiers(new LevelModifier(List.of(new Operation(2, Operator.ADD))));
        createRoomType(Strings.BottomlessDarkness, RoomSize.L, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.L, 13, 6, ModTags.CO_BOTTOMLESS_DARKNESS, null))
                .modifiers(new EffectRoomModifier(MobEffects.DARKNESS, EffectType.PLAYER, 0))
                .fixedRoom(KingdomKeys.rl(Strings.BottomlessDarkness))
                .colour(Color.BLACK);
        createRoomType(Strings.CalmBounty, RoomSize.S, RoomCategory.BOUNTY);
        createRoomType(Strings.ConquerorsRespite, RoomSize.SPECIAL, RoomCategory.SPECIAL)
                .fixedRoom(KingdomKeys.rl(Strings.ConquerorsRespite));
        createRoomType(Strings.EntranceHall, RoomSize.SPECIAL, RoomCategory.SPECIAL)
                .fixedRoom(KingdomKeys.rl(Strings.EntranceHall))
                .music(ModSounds.Music_The_13th_Floor.value())
                .isEntranceHall();
        createRoomType(Strings.FalseBounty, RoomSize.S, RoomCategory.BOUNTY)
                .enemies(new Enemies(RoomEnemies.S, 5, 3));
        createRoomType(Strings.FeebleDarkness, RoomSize.M, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.S, 6, 3))
                .modifiers(new LevelModifier(List.of(new Operation(2, Operator.SUBTRACT))));
        createRoomType(Strings.GuardedTrove, RoomSize.S, RoomCategory.BOUNTY)
                .enemies(new Enemies(RoomEnemies.M, 7, 3));
        createRoomType(Strings.LoomingDarkness, RoomSize.L, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.M, 10, 4))
                .modifiers(new EffectRoomModifier(MobEffects.MOVEMENT_SPEED, EffectType.MOB, 0));
        createRoomType(Strings.MartialWaking, RoomSize.M, RoomCategory.STATUS)
                .enemies(new Enemies(RoomEnemies.M, 8, 3))
                .modifiers(new EffectRoomModifier(MobEffects.DAMAGE_BOOST, EffectType.PLAYER, 1));
        createRoomType(Strings.MomentsReprieve, RoomSize.S, RoomCategory.BOUNTY)
                .fixedRoom(KingdomKeys.rl(Strings.MomentsReprieve));
        CompoundTag moogleData = new CompoundTag();
        moogleData.putString("inv", "kingdomkeys:cards");
        moogleData.putBoolean("stationary", true);
        createRoomType(Strings.MoogleRoom, RoomSize.S, RoomCategory.BOUNTY)
                .fixedRoom(KingdomKeys.rl(Strings.MoogleRoom))
                .modifiers(new SpawnMobModifier(ModEntities.TYPE_MOOGLE.getDelegate(), moogleData));
        createRoomType(Strings.ProsperousRepository, RoomSize.S, RoomCategory.BOUNTY);
        createRoomType(Strings.ReposefulGrove, RoomSize.M, RoomCategory.BOUNTY);
        createRoomType(Strings.RoomOfBeginnings, RoomSize.S, RoomCategory.ENCOUNTER)
                .encounter(KingdomKeys.rl(Strings.RoomOfBeginnings));
        createRoomType(Strings.RoomOfGuidance, RoomSize.S, RoomCategory.ENCOUNTER)
                .encounter(KingdomKeys.rl(Strings.RoomOfGuidance));
        createRoomType(Strings.RoomOfTruth, RoomSize.S, RoomCategory.ENCOUNTER)
                .encounter(KingdomKeys.rl(Strings.RoomOfTruth));        
        createRoomType(Strings.RoomOfRewards, RoomSize.S, RoomCategory.BOUNTY);
        createRoomType(Strings.SleepingDarkness, RoomSize.S, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.S, 5, 3));
        createRoomType(Strings.SorcerousWaking, RoomSize.M, RoomCategory.STATUS)
                .enemies(new Enemies(RoomEnemies.M, 8, 3));
        createRoomType(Strings.StagnantSpace, RoomSize.M, RoomCategory.STATUS)
                .enemies(new Enemies(RoomEnemies.S, 5, 3))
                .modifiers(new EffectRoomModifier(MobEffects.MOVEMENT_SLOWDOWN, EffectRoomModifier.EffectType.MOB, 0));
        createRoomType(Strings.TeemingDarkness, RoomSize.L, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.L, 16, 6));
        createRoomType(Strings.TranquilDarkness, RoomSize.M, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.S, 6, 2));
        createRoomType(Strings.TreacherousRepository, RoomSize.S, RoomCategory.BOUNTY)
                .enemies(new Enemies(RoomEnemies.M, 7, 3));
        createRoomType(Strings.UnknownRoom, RoomSize.S, RoomCategory.ENEMY)
                .enemies(new Enemies(RoomEnemies.S, 4, 2));
        createRoomType(Strings.WeightlessSpace, RoomSize.M, RoomCategory.STATUS)
                .enemies(new Enemies(RoomEnemies.S, 5, 3))
                .modifiers(new EffectRoomModifier(MobEffects.SLOW_FALLING, EffectType.BOTH, 0), new EffectRoomModifier(MobEffects.JUMP, EffectType.BOTH, 3));
    }

    @Override
    public String getName() {
        return "Kingdom Keys Castle Oblivion Room Types";
    }

    public RoomTypeBuilder createRoomType(String path, RoomSize size, RoomCategory category) {
        return addBuilder(new RoomTypeBuilder(getLocation(path), size, category));
    }
}
