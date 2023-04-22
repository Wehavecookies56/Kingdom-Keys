package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.util.function.Supplier;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

public abstract class KKLanguageProvider extends LanguageProvider {

    public KKLanguageProvider(DataGenerator gen, String locale) {
        super(gen, KingdomKeys.MODID, locale);
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

    public void addGrowthAbility(Supplier<Ability> key, String... value) {
        for (int i = 0; i < value.length; i++) {
            add(i+1, key.get(), value[i]);
        }
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

    public void add(int level, Magic key, String value) {
        add(key.getTranslationKey(level), value);
    }

    public void addMagic(Supplier<Magic> key, String... value) {
        for (int i = 0; i < value.length; i++) {
            add(i, key.get(), value[i]);
        }
    }

    public void add(InputHandler.Keybinds key, String value) {
        add(key.translationKey, value);
    }

    public void addMusicDisc(Supplier<Item> key, String value) {
        addItem(key, "Music Disc");
        add(key.get().getDescriptionId() + ".desc", value);
    }

}
