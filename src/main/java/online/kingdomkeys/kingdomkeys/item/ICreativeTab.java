package online.kingdomkeys.kingdomkeys.item;

public interface ICreativeTab {
    enum Tab {
        KEYBLADES, KEYCHAINS, ORGANIZATION, EQUIPABLES, GUMMI, MISC, NONE, CARDS, ARMORS, MATS
    }

    Tab getTab();
}
