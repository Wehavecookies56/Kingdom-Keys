package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.FaithEntityController;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFaith extends Magic {

    public MagicFaith(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
        super(registryName, hasToSelect, maxLevel, gmAbility);
    }

    @Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity){
        float dmgMult = getDamageMult(level) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.thunderBoost) * 0.25F;
        dmgMult *= fullMPBlastMult;

        FaithEntityController faith = new FaithEntityController(player.level(), player, dmgMult, lockOnEntity);
        faith.setPos(player.getX(), player.getY() + 1.8F, player.getZ());
        player.level().addFreshEntity(faith);
    }

    @Override
    protected void playMagicCastSound(LivingEntity player, Player player1, int i) {
    }
}
