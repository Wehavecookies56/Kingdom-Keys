package online.kingdomkeys.kingdomkeys.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKArmorItem extends Item implements IItemCategory {
    private final int defence;
    private final ImmutableMap<KKResistanceType, Integer> resList;
    public KKArmorItem(Properties properties, int defense, ImmutableMap<KKResistanceType, Integer> resList ) {
        super(properties);
        this.defence = defense;
        this.resList =  resList;
    }

    public int getDefence() {
        return defence;
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(getDefence() != 0) {
            tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Status_Defense)+": "+getDefence()));
        }
        for(Map.Entry<KKResistanceType, Integer> resistanceType : resList.entrySet()) {
            switch (resistanceType.getKey()) {
                case fire -> tooltip.add(new TranslatableComponent(Utils.translateToLocal(
                        Strings.Gui_Menu_Status_FireRes + ": " + resistanceType.getValue())));
                case ice -> tooltip.add(new TranslatableComponent(Utils.translateToLocal(
                        Strings.Gui_Menu_Status_BlizzardRes + ": " + resistanceType.getValue())));
                case lightning -> tooltip.add(new TranslatableComponent(Utils.translateToLocal(
                        Strings.Gui_Menu_Status_ThunderRes + ": " + resistanceType.getValue())));
                case darkness -> tooltip.add(new TranslatableComponent(Utils.translateToLocal(
                        Strings.Gui_Menu_Status_DarkRes + ": " + resistanceType.getValue())));
            }
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public boolean CheckKey(KKResistanceType kkResistanceType) {
        return resList.containsKey(kkResistanceType);
    }

    public int GetResValue(KKResistanceType kkResistanceType){
        return resList.get(kkResistanceType);
    }

    public ImmutableMap<KKResistanceType, Integer> getResList() {
        return resList;
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.EQUIPMENT;
    }
}
