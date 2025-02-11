package online.kingdomkeys.kingdomkeys.integration.epicfight.init;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;

public class KKCollider {
    private KKCollider(){}
    public static final Collider KEYBLADE = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
    public static final Collider NO = new MultiOBBCollider(3, 0.3, 0.3, 0.8, 0.0, 0.0, -0.9);
}
