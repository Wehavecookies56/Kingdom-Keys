package online.kingdomkeys.kingdomkeys.item;

public interface ICreativeTab {
    enum TABS {
        KEYBLADES, KEYCHAINS, ORGANIZATION, EQUIPABLES, GUMMI, MISC
    }

    TABS getTab();
}
