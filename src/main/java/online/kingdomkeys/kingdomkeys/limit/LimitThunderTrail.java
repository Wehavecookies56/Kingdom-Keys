package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowRainCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.ThunderTrailCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class LimitThunderTrail extends Limit {
	float speed;
	
	public LimitThunderTrail(String registryName, int order, OrgMember owner, float speed) {
		super(registryName, order, owner);
		this.speed=speed;
	}
	
	@Override
	public void onUse(Player player, LivingEntity target) {
		ItemStack stack = player.getMainHandItem();
		player.level.playSound(null, player.blockPosition(), ModSounds.portal.get(), SoundSource.PLAYERS, 1F, 1F);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setLimitCooldownTicks(getCooldown());
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);

		float damage;
		if(stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) * 0.4F;
		} else {
			damage = (playerData.getStrength(true) + playerData.getMagic(true)) * 0.4F;
		}
		
		damage *= getLimitData().getDmgMult();

		ThunderTrailCoreEntity thunder = new ThunderTrailCoreEntity(player.level, player, target, damage);
		player.level.addFreshEntity(thunder);
		thunder.shootFromRotation(player, 0, player.getYRot(), 0, speed, 0);
	}
}