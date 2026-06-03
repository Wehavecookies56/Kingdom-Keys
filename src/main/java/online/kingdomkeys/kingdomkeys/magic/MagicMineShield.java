package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.MineEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagicMineShield extends Magic {
	float forwardOffset = 2.0F;

	public MagicMineShield(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float dmgMult = getRealDamageMult(level,caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

// Horizontal forward vector only
		Vec3 look = player.getLookAngle();
		Vec3 forward = new Vec3(look.x, 0, look.z).normalize();
		Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();

		Vec3 base = player.position().add(forward.scale(forwardOffset));
		float spacing = 1.4F;
		PlayerData playerData = PlayerData.get(caster);

		switch (level) {
			case 0 -> { // Mine shield
				int mineCount = 1 + (Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(),getRegistryName().toString(), level) * 2);

				for (int i = 0; i < mineCount; i++) {
					float offset = (i - (mineCount - 1) / 2.0F) * spacing;
					Vec3 spawnPos = base.add(right.scale(offset));
					MineEntity mine = new MineEntity(player.level(), player, 0, dmgMult);

					mine.setMaxTicks(200 + mineCount * 10);
					mine.setCaster(player.getDisplayName().getString());
					mine.setPos(spawnPos.x, player.getY(), spawnPos.z);
					player.level().addFreshEntity(mine);
				}
			}

			case 1 -> { // Mine square
				int mineCount = 2 + (Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(),getRegistryName().toString(), level) * 2);
				float radius = mineCount * 0.5F;

				base = player.position();

				for (int i = 0; i < mineCount; i++) {
					double angle = (Math.PI * 2 / mineCount) * i;

					double x = Math.cos(angle) * radius;
					double z = Math.sin(angle) * radius;

					Vec3 spawnPos = base.add(x, 0, z);

					MineEntity mine = new MineEntity(player.level(), player, 0, dmgMult);

					mine.setMaxTicks(220);
					mine.setCaster(player.getDisplayName().getString());
					mine.setPos(spawnPos.x, player.getY() + 1.2D, spawnPos.z);

					player.level().addFreshEntity(mine);
				}
			}

			case 2 -> { // Seeker mine
				int mineCount = 2 + (Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(),getRegistryName().toString(), level) * 2);
				float radius = mineCount * 0.5F;

				base = player.position();

				for (int i = 0; i < mineCount; i++) {
					double angle = (Math.PI * 2 / mineCount) * i;

					double x = Math.cos(angle) * radius;
					double z = Math.sin(angle) * radius;

					Vec3 spawnPos = base.add(x, 0, z);

					MineEntity mine = new MineEntity(player.level(), player, 0, dmgMult);

					mine.setSeeker(true);

					mine.setMaxTicks(220);
					mine.setCaster(player.getDisplayName().getString());
					mine.setPos(spawnPos.x, player.getY() + 1.2D, spawnPos.z);

					player.level().addFreshEntity(mine);
				}
			}
		}
	}

	@Override
	protected void playMagicCastSound(LivingEntity player, Player player1, int i) {

	}
}
