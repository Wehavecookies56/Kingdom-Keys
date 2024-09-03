package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SitarItem extends OrgSwordItem implements IOrgWeapon {

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.DEMYX;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        if(playerData != null && !playerData.getRecharge()) {
            int cost = 10;
            cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
            playerData.remMP(Math.max(1, cost));

            WatergaEntity shot = new WatergaEntity(player.level(), player, 0.75f);
            shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
            world.addFreshEntity(shot);
            player.level().playSound(null, player.blockPosition(), SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 1F, 1F);
            player.getCooldowns().addCooldown(this,25);
        }
        return super.use(world, player, hand);
    }

}
