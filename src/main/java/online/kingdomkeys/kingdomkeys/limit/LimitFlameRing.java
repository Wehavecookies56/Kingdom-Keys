package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.FlameRingCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitFlameRing extends Limit {

	public LimitFlameRing(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		ItemStack stack = player.getMainHandItem();
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 0.8F);

		PlayerData playerData = PlayerData.get(player);
		playerData.setLimitCooldownTicks(getCooldown());
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);

		float damage;
		if (stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) * 0.35F;
		} else {
			damage = (playerData.getStrength(true) + playerData.getMagic(true)) * 0.35F;
		}

		damage *= getLimitData().getDmgMult();

		FlameRingCoreEntity ring = new FlameRingCoreEntity(player.level(), player, damage);
		ring.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(ring);
	}
}
