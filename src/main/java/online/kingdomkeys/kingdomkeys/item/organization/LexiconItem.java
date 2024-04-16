package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class LexiconItem extends OrgSwordItem implements IOrgWeapon {
    
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.ZEXION;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        
        player.playSound(SoundEvents.BOOK_PAGE_TURN, 1F, 1F);

        return super.use(world, player, hand);
    }

}
