package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

public abstract class RoomModifier {

    public abstract void onEnter();

    public abstract void onGenerate();

    public abstract void onExit();

    public abstract void tick();

    public String modifierName;

    public RoomModifier(String modifierName) {
        this.modifierName = modifierName;
    }

}
