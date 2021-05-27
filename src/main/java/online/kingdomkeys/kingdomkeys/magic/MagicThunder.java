package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundaraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;

public class MagicThunder extends Magic {

	public MagicThunder(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		switch(level) {
		case 0:
			ThunderEntity thunderController = new ThunderEntity(player.world, player);
			thunderController.setCaster(player.getUniqueID());
			player.world.addEntity(thunderController);
			break;
		case 1:
			ThundaraEntity thundaraController = new ThundaraEntity(player.world, player);
			thundaraController.setCaster(player.getUniqueID());
			player.world.addEntity(thundaraController);
			break;
		case 2:
			ThundagaEntity thundagaController = new ThundagaEntity(player.world, player);
			thundagaController.setCaster(player.getUniqueID());
			player.world.addEntity(thundagaController);
			break;
		case 3:
			ThundazaEntity thundazaController = new ThundazaEntity(player.world, player);
			thundazaController.setCaster(player.getUniqueID());
			player.world.addEntity(thundazaController);
			break;
		}
		
	}

}
