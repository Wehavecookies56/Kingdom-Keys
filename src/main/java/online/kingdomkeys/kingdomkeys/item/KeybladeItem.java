package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeRenderer;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;

public class KeybladeItem extends SwordItem {

    //Level 0 = no upgrades, will use base stats in the data file
    private int level = 0;
    private KeybladeData data;

    //TODO remove attack damage
    public KeybladeItem(String name, Item.Properties properties) {
        super(new KeybladeItemTier(0), 0, 1, properties);
        setRegistryName(KingdomKeys.MODID, name);
    }

    //Get strength from the data based on level
    public int getStrength(int level) {
        return data.getStrength(level);
    }

    //Get magic from the data based on level
    public int getMagic(int level) {
        return data.getMagic(level);
    }

    public String getDescription() {
        return data.getDescription();
    }

    public void setKeybladeData(KeybladeData data) {
        this.data = data;
    }

    public int getKeybladeLevel() {
        return level;
    }

    public void setKeybladeLevel(int level) {
        this.level = level;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //TODO make better tooltip (translations and looks)
        if (data != null) {
            tooltip.add(new TranslationTextComponent("Level %s", getKeybladeLevel()));
            tooltip.add(new TranslationTextComponent("Strength %s", getStrength(getKeybladeLevel())));
            tooltip.add(new TranslationTextComponent("Magic %s", getMagic(getKeybladeLevel())));
            tooltip.add(new TranslationTextComponent(TextFormatting.ITALIC + getDescription()));
        }
    }


}
