package online.kingdomkeys.kingdomkeys.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Map;

public class KKArmorItem extends Item implements IItemCategory {
    private final int defence;
    private final ImmutableMap<KKResistanceType, Integer> resList;
    public KKArmorItem(Properties properties, int defense, ImmutableMap<KKResistanceType, Integer> resList ) {
        super(properties);
        this.defence = defense;
        this.resList =  resList;
    }

    public int getDefense() {
        return defence;
    }
    
   
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        if(getDefense() != 0) {
            tooltip.add(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Status_Defense)+": "+getDefense()));
        }
		for (Map.Entry<KKResistanceType, Integer> resistanceType : resList.entrySet()) {
			switch (resistanceType.getKey()) {
			case fire -> tooltip.add(Component.translatable(ChatFormatting.RED+Utils.translateToLocal(Strings.Gui_Menu_Status_FireRes) + ": " + resistanceType.getValue()+"%"));
			case ice -> tooltip.add(Component.translatable(ChatFormatting.AQUA+Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardRes) + ": " + resistanceType.getValue()+"%"));
			case lightning -> tooltip.add(Component.translatable(ChatFormatting.YELLOW+Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderRes) + ": " + resistanceType.getValue()+"%"));
			case light -> tooltip.add(Component.translatable(ChatFormatting.WHITE+Utils.translateToLocal(Strings.Gui_Menu_Status_LightRes) + ": " + resistanceType.getValue()+"%"));
			case darkness -> tooltip.add(Component.translatable(ChatFormatting.GRAY+Utils.translateToLocal(Strings.Gui_Menu_Status_DarkRes) + ": " + resistanceType.getValue()+"%"));
			}
		}
		
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
    }

    public boolean CheckKey(KKResistanceType kkResistanceType) {
        return resList.containsKey(kkResistanceType);
    }

    public int GetResValue(KKResistanceType kkResistanceType){
        return resList.get(kkResistanceType);
    }
    
    public int GetResValue(KKResistanceType kkResistanceType, int total){
        return resList.get(kkResistanceType) * total / 100;
    }

    public ImmutableMap<KKResistanceType, Integer> getResList() {
        return resList;
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.EQUIPMENT;
    }
}
