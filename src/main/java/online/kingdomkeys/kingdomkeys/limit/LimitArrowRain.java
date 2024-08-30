package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowRainCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class LimitArrowRain extends Limit {

	public LimitArrowRain(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		ItemStack stack = player.getMainHandItem();
		player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.portal.get(), SoundSource.PLAYERS, 1F, 1F);
		IPlayerData playerData = ModData.getPlayer(player);
		playerData.setLimitCooldownTicks(getCooldown());
		PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer)player);

		float damage;
		if(stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) / 2;
		} else {
			damage = (playerData.getStrength(true) + playerData.getMagic(true)) / 2;
		}
		
		damage *= getLimitData().getDmgMult();

		ArrowRainCoreEntity core = new ArrowRainCoreEntity(player.level(), player, target, damage);
		core.setPos(target.getX(), target.getY(), target.getZ());
		player.level().addFreshEntity(core);
	}
}