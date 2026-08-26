package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.organization.*;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

import java.util.function.Supplier;

public class OrganizationWeaponItemBuilder extends ItemBuilder {

    Supplier<Item> itemSupplier;

    public static final ResourceLocation[] ORG_WEAPONS = {
            ModTags.ORG.location(), ItemTags.SWORD_ENCHANTABLE.location(), ItemTags.SHARP_WEAPON_ENCHANTABLE.location()
    };

    public OrganizationWeaponItemBuilder(ResourceLocation i, Supplier<Item> item) {
        super(i);
        tag(ORG_WEAPONS);
    }

    @Override
    public Item createObject() {
        return itemSupplier.get();
    }

    public static class Arrowgun extends OrganizationWeaponItemBuilder {
        public Arrowgun(ResourceLocation i) {
            super(i, ArrowgunItem::new);
        }
    }

    public static class AxeSword extends OrganizationWeaponItemBuilder {
        public AxeSword(ResourceLocation i) {
            super(i, AxeSwordItem::new);
        }
    }

    public static class Card extends OrganizationWeaponItemBuilder {
        public Card(ResourceLocation i) {
            super(i, CardItem::new);
        }
    }

    public static class Chakram extends OrganizationWeaponItemBuilder {
        public Chakram(ResourceLocation i) {
            super(i, ChakramItem::new);
        }
    }

    public static class Claymore extends OrganizationWeaponItemBuilder {
        public Claymore(ResourceLocation i) {
            super(i, ClaymoreItem::new);
        }
    }

    public static class EtherealBlade extends OrganizationWeaponItemBuilder {
        public EtherealBlade(ResourceLocation i) {
            super(i, EtherealBladeItem::new);
        }
    }

    public static class Knife extends OrganizationWeaponItemBuilder {
        public Knife(ResourceLocation i) {
            super(i, KnifeItem::new);
        }
    }

    public static class Lance extends OrganizationWeaponItemBuilder {
        public Lance(ResourceLocation i) {
            super(i, LanceItem::new);
        }
    }

    public static class Lexicon extends OrganizationWeaponItemBuilder {
        public Lexicon(ResourceLocation i) {
            super(i, LexiconItem::new);
        }
    }

    public static class Shield extends OrganizationWeaponItemBuilder {
        public Shield(ResourceLocation i) {
            super(i, OrgShieldItem::new);
        }
    }

    public static class Scythe extends OrganizationWeaponItemBuilder {
        public Scythe(ResourceLocation i) {
            super(i, ScytheItem::new);
        }
    }

    public static class Sitar extends OrganizationWeaponItemBuilder {
        public Sitar(ResourceLocation i) {
            super(i, SitarItem::new);
        }
    }

}
