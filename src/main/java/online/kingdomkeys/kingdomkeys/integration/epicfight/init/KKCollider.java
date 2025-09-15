package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;

public class KKCollider {
    private KKCollider(){}
    public static final Collider KEYBLADE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider ETHEREAL_BLADE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider ARROWGUN = new MultiOBBCollider(1, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider LANCE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider AXE_SWORD = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider LEXICON = new MultiOBBCollider(1, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider CLAYMORE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider SITAR = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider CARD = new MultiOBBCollider(1, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider SCYTHE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider KNIVES = new MultiOBBCollider(2, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);


    public static final Collider NO = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);


    
}
