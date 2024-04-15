package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.GlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.damagesource.DarknessDamageSource;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class AxeSwordItem extends OrgSwordItem implements IOrgWeapon {
	
	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.LEXAEUS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

		float dmg = playerData.getStrengthStat().getStat();
		dmg *= 2.5F;
		System.out.println(dmg);

		if (!player.isShiftKeyDown()){
			// Helm Split (Heavy Attack)
			if (player.onGround()) {
				//player.jumpFromGround();
				player.setDeltaMovement(player.getDeltaMovement().add(0, 0.8, 0));
				world.addParticle(ParticleTypes.ANGRY_VILLAGER,player.getX(),player.getY(),player.getZ(), 0,0,0);
			}
			if (!player.onGround()){
				List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) player, player, 3,3,3);
				for(LivingEntity e : targetList) {
					e.hurt(DarknessDamageSource.getDarknessDamage(e, player), dmg);

					world.explode(player,player.getX(), player.getY(),player.getZ(),3,false, Level.ExplosionInteraction.NONE);
					player.swing(hand);
					player.getCooldowns().addCooldown(this,20);
				}
			}


		} //else {
			// Shift + R-Click Move

		//}


		return super.use(world, player, hand);
	}

}
