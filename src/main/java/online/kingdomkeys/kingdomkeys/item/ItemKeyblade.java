package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKeyblade extends ItemSword {

    //Level 0 = no upgrades, will use base stats in the data file
    private int level = 0;
    private KeybladeData data;

    //TODO remove attack damage
    public ItemKeyblade(String name, int attackDamageIn, float attackSpeedIn) {
        super(new ItemTierKeyblade(attackDamageIn), attackDamageIn, attackSpeedIn, new Item.Properties().group(KingdomKeys.keybladesGroup).maxStackSize(1));
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
        tooltip.add(new TextComponentTranslation("Level %s", getKeybladeLevel()));
        tooltip.add(new TextComponentTranslation("Strength %s", getStrength(getKeybladeLevel())));
        tooltip.add(new TextComponentTranslation("Magic %s", getMagic(getKeybladeLevel())));
        tooltip.add(new TextComponentTranslation(TextFormatting.ITALIC + getDescription()));
    }
}
