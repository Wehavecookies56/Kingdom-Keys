package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.organization.PetalLauncherCoreEntity;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;


public class LimitPetalLaunchers extends Limit {
	private static final float DISTANCE_FROM_PLAYER = 7F; // how far out (front/back/left/right) each disc sits
	private static final float CIRCLE_RADIUS = 3.0F; // how big each disc is - both what you see and what actually triggers it

	public LimitPetalLaunchers(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);

		Vec3 forward = player.getLookAngle();
		Vec3 flatForward = new Vec3(forward.x, 0, forward.z).normalize();
		Vec3 right = new Vec3(-flatForward.z, 0, flatForward.x);

		Vec3[] offsets = {flatForward, flatForward.scale(-1), right, right.scale(-1)};

		for (Vec3 offset : offsets) {
			Vec3 pos = player.position().add(offset.scale(DISTANCE_FROM_PLAYER));
			PetalLauncherCoreEntity launcher = new PetalLauncherCoreEntity(player.level(), player, getLimitData().getDmgMult());
			launcher.setRadius(CIRCLE_RADIUS);
			launcher.setPos(pos.x, player.getY(), pos.z);
			player.level().addFreshEntity(launcher);
		}

		player.level().playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1F, 0.7F);
	}
}
