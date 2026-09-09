package online.kingdomkeys.kingdomkeys.world.dimension.daybreak_town;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ApprenticeEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class DaybreakTownDimension {
    public record Post(double x, double y, double z, float yaw) {
        public Vec3 pos() {
            return new Vec3(x + 0.5D, y, z + 0.5D);
        }
    }

    private static final Map<Union, Post> POSTS = new EnumMap<>(Union.class);

    static {
        POSTS.put(Union.LEOPARDOS, new Post(130, 66, 119, 0.0F));
        POSTS.put(Union.VULPES, new Post(274, 78, 176, 0.0F));
        POSTS.put(Union.URSUS, new Post(292, 66, 304, 0.0F));
        POSTS.put(Union.UNICORNIS, new Post(176, 66, 404, 0.0F));
        POSTS.put(Union.ANGUIS, new Post(87, 77, 260, 0.0F));
    }

    private static final double NEAR_PLAYER = 48.0D;

    private static final double POST_RADIUS = 8.0D;

    /** The built city, in blocks: 21 by 40 chunks laid from the world origin. */
    private static final int TOWN_X = 21 * 16, TOWN_Z = 40 * 16;
    private static final int APPRENTICES = 400;

    /** How far from their own post an apprentice may have wandered and still count as posted. */
    private static final double BEAT = 32.0D;

    /** Max height where NPC will spawn */
    private static final int CEILING = 100;

    private static List<Post> apprenticePosts;

    private static long postsSeed;

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide())
            return;
        if (!player.level().dimension().equals(ModDimensions.DAYBREAK_TOWN))
            return;
        if (player.tickCount % 40 != 0)
            return;

        ensureForetellers((ServerLevel) player.level(), player);
        ensureApprentices((ServerLevel) player.level(), player);
    }

    private static List<Post> apprenticePosts(ServerLevel level) {
        long seed = level.getSeed();

        if (apprenticePosts != null && postsSeed == seed) {
            return apprenticePosts;
        }

        RandomSource random = RandomSource.create(seed ^ 0x0DA7B4EAL);
        List<Post> found = new ArrayList<>(APPRENTICES);

        for (int i = 0; i < APPRENTICES; i++) {
            found.add(new Post(random.nextInt(TOWN_X), 0, random.nextInt(TOWN_Z), random.nextInt(4) * 90.0F));
        }

        apprenticePosts = found;
        postsSeed = seed;
        return found;
    }

    public static void ensureApprentices(ServerLevel level, Player near) {
        for (Post post : apprenticePosts(level)) {
            if (near.position().distanceToSqr(post.x() + 0.5D, near.getY(), post.z() + 0.5D) > NEAR_PLAYER * NEAR_PLAYER)
                continue;

            BlockPos home = new BlockPos((int) post.x(), 0, (int) post.z());

            if (!level.isLoaded(home))
                continue;

            home = ground(level, home);

            if (home == null)
                continue;

            AABB around = AABB.ofSize(Vec3.atCenterOf(home), BEAT * 2, BEAT * 2, BEAT * 2);
            BlockPos posted = home;

            if (!level.getEntitiesOfClass(ApprenticeEntity.class, around, a -> a.getHome().equals(posted)).isEmpty())
                continue;

            ApprenticeEntity apprentice = ModEntities.TYPE_APPRENTICE.get().create(level);

            if (apprentice == null)
                continue;

            apprentice.moveTo(home.getX() + 0.5D, home.getY(), home.getZ() + 0.5D, post.yaw(), 0.0F);
            apprentice.setHome(home);

            // Drawn from the post itself, so the same street always holds the same people
            RandomSource seeded = RandomSource.create(home.asLong());

            apprentice.setApprenticeLevel(ApprenticeEntity.rollLevel(seeded));
            apprentice.setUnion(ApprenticeEntity.rollUnion(seeded));
            apprentice.dress();

            level.addFreshEntity(apprentice);
        }
    }

    private static BlockPos ground(ServerLevel level, BlockPos at) {
        BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, at);

        if (surface.getY() <= level.getSeaLevel() || surface.getY() > CEILING)
            return null; // the sea around the island, or something absurd

        if (!level.getFluidState(surface.below()).isEmpty() || invalidSpawnPoint(level.getBlockState(surface.below())))
            return null;

        // Room to stand up in
        if (!level.getBlockState(surface).isAir() || !level.getBlockState(surface.above()).isAir())
            return null;

        return surface;
    }

    private static boolean invalidSpawnPoint(BlockState state) {
        return state.is(Blocks.PURPLE_CONCRETE) || state.is(BlockTags.WOOL) || state.is(BlockTags.WOOL_CARPETS) || state.is(BlockTags.LEAVES);
    }

    public static void ensureForetellers(ServerLevel level, Player near) {
        for (Map.Entry<Union, Post> entry : POSTS.entrySet()) {
            Union union = entry.getKey();
            Post post = entry.getValue();
            Vec3 pos = post.pos();

            if (near.position().distanceToSqr(pos) > NEAR_PLAYER * NEAR_PLAYER)
                continue;

            AABB around = AABB.ofSize(pos, POST_RADIUS * 2, POST_RADIUS * 2, POST_RADIUS * 2);
            List<ForetellerEntity> found = level.getEntitiesOfClass(ForetellerEntity.class, around, f -> f.getUnion() == union);

            if (!found.isEmpty()) {
                for (int i = 1; i < found.size(); i++) {
                    found.get(i).discard();
                }
                continue;
            }

            ForetellerEntity foreteller = ModEntities.TYPE_FORETELLER.get().create(level);
            if (foreteller == null)
                continue;

            foreteller.setUnion(union);
            foreteller.wearUnionRobes();
            foreteller.moveTo(pos.x, pos.y, pos.z, post.yaw(), 0.0F);
            foreteller.setYHeadRot(post.yaw());
            foreteller.setYBodyRot(post.yaw());
            level.addFreshEntity(foreteller);
        }
    }
}
