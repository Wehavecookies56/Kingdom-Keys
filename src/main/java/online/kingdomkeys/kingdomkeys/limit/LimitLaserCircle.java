package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserCircleCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class LimitLaserCircle extends Limit {

	public LimitLaserCircle(String registryName, int order, int cost, int cooldown, OrgMember owner) {
		super(registryName, order, cost, cooldown, owner);
	}
	
	@Override
	public int getCost() {
		return ModConfigs.limitLaserCircleCost;
	}

	@Override
	public void onUse(PlayerEntity player, LivingEntity target) {
		ItemStack stack = player.getHeldItemMainhand();
		player.world.playSound(null, player.getPosition(), ModSounds.portal.get(), SoundCategory.PLAYERS, 1F, 1F);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setLimitCooldownTicks(cooldown);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);

		float damage;
		if(stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) / 2;
		} else {
			damage = (playerData.getStrength() + playerData.getMagic()) / 2;
		}

		LaserCircleCoreEntity dome = new LaserCircleCoreEntity(player.world, player, target, damage);
		dome.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
		player.world.addEntity(dome);
	}
}