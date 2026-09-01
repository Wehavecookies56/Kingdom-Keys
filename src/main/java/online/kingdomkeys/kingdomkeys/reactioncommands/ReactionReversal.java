package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCStartReversal;

import javax.annotation.Nullable;

public class ReactionReversal extends ReactionCommand {
	private static final double REACH = 5;
	private static final int TIMEOUT = 30;
	private static final int DISORIENTED = 40;

	public ReactionReversal(ResourceLocation registryName) {
		super(registryName, false, TIMEOUT, 0x33CCFF);
	}

	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		DuskEntity dusk = findDusk(player, lockedOnEntity);

		if (dusk == null) {
			return;
		}

		PacketHandler.sendTo(new SCStartReversal(dusk.getId()), serverPlayer);
		dusk.disorient(DISORIENTED);
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return findDusk(player, null) != null;
	}

	@Override
	public SoundEvent getUseSound(Player player, LivingEntity target) {
		return ModSounds.air_slide.get();
	}

	@Nullable
	private static DuskEntity findDusk(Player player, @Nullable LivingEntity lockedOnEntity) {
		if (lockedOnEntity instanceof DuskEntity locked && inReach(player, locked)) {
			return locked;
		}

		DuskEntity closest = null;
		double best = Double.MAX_VALUE;

		AABB around = player.getBoundingBox().inflate(REACH);

		for (DuskEntity dusk : player.level().getEntitiesOfClass(DuskEntity.class, around, dusk -> inReach(player, dusk))) {
			double distance = dusk.distanceToSqr(player);

			if (distance < best) {
				best = distance;
				closest = dusk;
			}
		}

		return closest;
	}

	private static boolean inReach(Player player, DuskEntity dusk) {
		return dusk.isAlive() && dusk.distanceToSqr(player) <= REACH * REACH;
	}

}
