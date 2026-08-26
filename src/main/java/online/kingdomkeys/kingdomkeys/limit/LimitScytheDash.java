package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.organization.ScytheDashCoreEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.ScytheItem;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitScytheDash extends Limit {

	private static final int EFFECT_TICKS = 100; // ~2s, matches ScytheDashCoreEntity's own duration
	private static final int SPEED_AMPLIFIER = 3; // Speed IV

	public LimitScytheDash(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);

		ItemStack stack = player.getMainHandItem();
		float damage;
		if (stack.getItem() instanceof IOrgWeapon) {
			damage = DamageCalculation.getOrgStrengthDamage(player, stack);
		} else {
			damage = PlayerData.get(player).getStrength(true);
		}
		damage *= getLimitData().getDmgMult();

		// Their own scythe if they're holding one, Graceful Dahlia otherwise.
		ItemStack visualStack = stack.getItem() instanceof ScytheItem ? stack.copy() : new ItemStack(ModItems.gracefulDahlia.get());

		player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, EFFECT_TICKS, 0, false, false, false));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_TICKS, SPEED_AMPLIFIER, false, false, false));
		player.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1.2F);

		ScytheDashCoreEntity dash = new ScytheDashCoreEntity(ModEntities.TYPE_SCYTHE_DASH.get(), player.level(), player, damage, visualStack);
		player.level().addFreshEntity(dash);
	}
}
