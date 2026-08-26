package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class KKLanguageProvider extends LanguageProvider {

    String locale;

    public KKLanguageProvider(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), KingdomKeys.MODID, locale);
        this.locale = locale;
    }

    public void add(int level, Ability key, String value) {
        add(key.getTranslationKey(level), value);
    }

    public void addAbility(Supplier<Ability> key, String value) {
        add(0, key.get(), value);
    }

    public void addAbilityWithDesc(Supplier<Ability> key, String value, String value2) {
        add(0, key.get(), value);
        addAbilityDesc(key, value2);
    }

    public void addGrowthAbility(Supplier<Ability> key, String description, String... value) {
        for (int i = 0; i < value.length; i++) {
            add(i+1, key.get(), value[i]);
        }
        add(key.get().getDescTranslationKey(), description);
    }

    public void addItem(Supplier<? extends Item> item, String name, String description) {
        addItem(item, name);
        add(Utils.createDescriptionKey(new ItemStack(item.get())), description);
    }

    public void addAbilityDesc(Supplier<Ability> key, String value) {
        add(key.get().getDescTranslationKey(), value);
    }

    public void add(DriveForm key, String value) {
        add(key.getTranslationKey(), value);
    }

    public void addDriveForm(Supplier<DriveForm> key, String value) {
        add(key.get(), value);
    }

    public void add(ReactionCommand key, String value) {
        add(key.getTranslationKey(), value);
    }

    public void addReactionCommand(Supplier<ReactionCommand> key, String value) {
        add(key.get(), value);
    }

    public void add(Shotlock key, String value) {
        add(key.getTranslationKey(), value);
    }

    public void addShotlock(Supplier<Shotlock> key, String value) {
        add(key.get(), value);
    }

    public void add(Limit key, String value) {
        add(key.getTranslationKey(), value);
    }

    public void addLimit(Supplier<Limit> key, String value) {
        add(key.get(), value);
    }

    public void add(Magic key, String value) {
        add(key.getTranslationKey(), value);
    }

    public void add(InputHandler.Keybinds key, String value) {
        add(key.translationKey, value);
    }

    public void addMusicDisc(Supplier<Item> key, String value, String comp) {
        addItem(key, "Music Disc");
        add(key.get().getDescriptionId() + ".desc", value);
        add(key.get().getDescriptionId() + ".comp", comp);
    }
    
    public void addAdvancement(String key, String name, String desc) {
        add("advancements.kingdomkeys."+key, name);
        add("advancements.kingdomkeys."+key+".desc", desc);
    }

    public void addKKEffect(Holder<MobEffect> key, String name, String desc) {
        add(key.value(), name);
        add(key.value().getDescriptionId()+".desc", desc);
    }

    public void addConfigKey(ModConfigSpec.ConfigValue configOption, String translation) {
        add(configOption.getSpec().getTranslationKey(), translation);
    }

    Map<String, Map<String, String>> colours = Map.ofEntries(
            Map.entry("es_es", Map.ofEntries(
            Map.entry("color.minecraft.black", "Negro"),
            Map.entry("color.minecraft.blue", "Azul"),
            Map.entry("color.minecraft.brown", "Marrón"),
            Map.entry("color.minecraft.cyan", "Cian"),
            Map.entry("color.minecraft.gray", "Gris"),
            Map.entry("color.minecraft.green", "Verde"),
            Map.entry("color.minecraft.light_blue", "Azul claro"),
            Map.entry("color.minecraft.light_gray", "Gris claro"),
            Map.entry("color.minecraft.lime", "Verde lima"),
            Map.entry("color.minecraft.magenta", "Magenta"),
            Map.entry("color.minecraft.orange", "Naranja"),
            Map.entry("color.minecraft.pink", "Rosa"),
            Map.entry("color.minecraft.purple", "Morado"),
            Map.entry("color.minecraft.red", "Rojo"),
            Map.entry("color.minecraft.white", "Blanco"),
            Map.entry("color.minecraft.yellow", "Amarillo"))
            ));

    public void addTintedBlock(List<Supplier<Block>> blocks, String name) {
        for (int i = 0; i < DyeColor.values().length; i++) {
            String colour;
            if (colours.containsKey(locale)) {
                colour = colours.get(locale).get("color.minecraft." + DyeColor.values()[i].getName());
            } else {
                colour = Component.translatable("color.minecraft." + DyeColor.values()[i].getName()).getString();

            }
            addBlock(blocks.get(i), String.format(name, colour));
        }
    }

    public void addRoom(String roomName, String name) {
        add("room." + roomName, name);
    }

}
