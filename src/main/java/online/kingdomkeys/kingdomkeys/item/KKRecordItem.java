package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class KKRecordItem extends Item implements IItemCategory {
    public KKRecordItem(ResourceKey<JukeboxSong> song) {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(song));
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.kingdomkeys."+Utils.getItemRegistryName(this).getPath()+".desc"));

        JukeboxSong song = JukeboxSong.fromStack(context.registries(), stack).get().value();
        if(song != null) {
            int length = (int) song.lengthInSeconds();
            int minutes = length / 60;
            int seconds = length % 60;
            tooltipComponents.add( Component.literal(Utils.translateToLocal("disc.duration.desc")+": %d:%02d".formatted(minutes,seconds)).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
