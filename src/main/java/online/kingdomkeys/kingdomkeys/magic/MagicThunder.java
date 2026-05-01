package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundaraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicThunder extends Magic {

	public MagicThunder(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch(level) {
		case 0:
			ThunderEntity thunderController = new ThunderEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(thunderController);
			break;
		case 1:
			ThundaraEntity thundaraController = new ThundaraEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(thundaraController);
			break;
		case 2:
			ThundagaEntity thundagaController = new ThundagaEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(thundagaController);
			break;
		case 3:
			ThundazaEntity thundazaController = new ThundazaEntity(player.level(), player, dmgMult, lockOnEntity);
			player.level().addFreshEntity(thundazaController);
			break;
		}
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {

	}
}