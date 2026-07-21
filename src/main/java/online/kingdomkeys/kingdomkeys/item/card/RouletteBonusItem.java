package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCardRoulette;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class RouletteBonusItem extends Item {
    public RouletteBonusItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            int rouletteSize = 20; //not sure how much it is in Re:CoM might need to have a look and count
            List<MapCardItem> mapCards = ModTags.getItemsInTag(level, ModTags.MAP_CARD).stream().filter(item -> item instanceof MapCardItem).map(item -> (MapCardItem)item).toList();
            List<ItemStack> rouletteCards = new ArrayList<>();
            for (int i = 0; i < rouletteSize; ++i) {
                ItemStack stack = new ItemStack(mapCards.get(Utils.randomWithRange(0, mapCards.size()-1)));
                MapCardItem.initialize(stack);
                rouletteCards.add(stack);
            }
            if (!rouletteCards.isEmpty()) {
                PacketHandler.sendTo(new SCOpenCardRoulette(rouletteCards), (ServerPlayer) player);
                player.getItemInHand(usedHand).shrink(1);
            }
        }
        return super.use(level, player, usedHand);
    }
}
