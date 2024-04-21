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
import online.kingdomkeys.kingdomkeys.entity.organization.SaixShockwave;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ClaymoreItem extends OrgSwordItem implements IOrgWeapon {
    
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.SAIX;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        if (playerData != null && !playerData.getRecharge()) {
            if (!player.isShiftKeyDown()) {
                // Right-Click Attack
                int cost = 5;
                cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
                playerData.remMP(Math.max(1, cost));

                for(int i = -20; i <= 20; i+=20) {
	                SaixShockwave shockwave = new SaixShockwave(world, player, DamageCalculation.getOrgStrengthDamage(player, player.getMainHandItem()) / 3);
	                shockwave.setPos(player.getX(),player.getY(),player.getZ());
	                shockwave.shootFromRotation(player, player.getXRot(), player.getYRot()+i, 0, 1.5F, 0);
	                world.addFreshEntity(shockwave);
                }
                world.playSound(null, player.blockPosition(),SoundEvents.FIRECHARGE_USE,SoundSource.PLAYERS);
                player.swing(hand);
                player.getCooldowns().addCooldown(this,30);
            }
            // Shift + R-click Attack when I get to it. - Xephiro
            // else {

        }
        return super.use(world, player, hand);

    }


}
