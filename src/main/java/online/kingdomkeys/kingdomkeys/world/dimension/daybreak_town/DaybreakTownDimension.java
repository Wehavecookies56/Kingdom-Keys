package online.kingdomkeys.kingdomkeys.world.dimension.daybreak_town;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

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
