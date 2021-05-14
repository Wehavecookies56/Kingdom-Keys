package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicBlizzard extends Magic {

	public MagicBlizzard(String registryName, int cost, int maxLevel, int order) {
		super(registryName, cost, false, maxLevel, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		switch(level) {
		case 0:
			ThrowableEntity blizzard = new BlizzardEntity(player.world, player);
			player.world.addEntity(blizzard);
			blizzard.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			player.swingArm(Hand.MAIN_HAND);
			break;
		case 1:
			for(int i = -1; i < 2; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw + i*6, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			player.swingArm(Hand.MAIN_HAND);

			break;
		case 2:
			for(int i = -1; i < 2; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw + i*6, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			for(int i = -1; i < 1; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch-6, player.rotationYaw + i*6+3, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			player.swingArm(Hand.MAIN_HAND);

			break;
		}
		
		
	}

}
