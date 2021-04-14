package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class MagicFire extends Magic {

	public MagicFire(String registryName, int cost, int order) {
		super(registryName, cost, false, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		ThrowableEntity shot = new FireEntity(player.world, player);
		player.world.addEntity(shot);
		shot.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
		player.swingArm(Hand.MAIN_HAND);
	}

}
