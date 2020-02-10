package online.kingdomkeys.kingdomkeys.integration.corsair.functions;

import net.minecraft.client.resources.I18n;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public enum KeyFunction {
	NO(KingdomKeys.MODID + ".keyfunc.off", (k, m, w, p) -> new int[] {0,0,0}),

	SLOT_DAMAGE_1(KingdomKeys.MODID + ".keyfunc.slot1", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 0)),
	SLOT_DAMAGE_2(KingdomKeys.MODID + ".keyfunc.slot2", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 1)),
	SLOT_DAMAGE_3(KingdomKeys.MODID + ".keyfunc.slot3", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 2)),
	SLOT_DAMAGE_4(KingdomKeys.MODID + ".keyfunc.slot4", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 3)),
	SLOT_DAMAGE_5(KingdomKeys.MODID + ".keyfunc.slot5", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 4)),
	SLOT_DAMAGE_6(KingdomKeys.MODID + ".keyfunc.slot6", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 5)),
	SLOT_DAMAGE_7(KingdomKeys.MODID + ".keyfunc.slot7", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 6)),
	SLOT_DAMAGE_8(KingdomKeys.MODID + ".keyfunc.slot8", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 7)),
	SLOT_DAMAGE_9(KingdomKeys.MODID + ".keyfunc.slot9", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColor(p, 8)),
	
	SLOT_DAMAGE_OFF(KingdomKeys.MODID + ".keyfunc.slotoff", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColorOffhand(p)),

	SLOT_ARMOR_0(KingdomKeys.MODID + ".keyfunc.armor0", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColorArmor(p, 0)),
	SLOT_ARMOR_1(KingdomKeys.MODID + ".keyfunc.armor1", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColorArmor(p, 1)),
	SLOT_ARMOR_2(KingdomKeys.MODID + ".keyfunc.armor2", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColorArmor(p, 2)),
	SLOT_ARMOR_3(KingdomKeys.MODID + ".keyfunc.armor3", (k, m, w, p) -> FunctionItemDamage.getRGBKeyColorArmor(p, 3)),

	HEALTH(KingdomKeys.MODID + ".keyfunc.health", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p)),
	HEALTH_0(KingdomKeys.MODID + ".keyfunc.health0", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 0)),
	HEALTH_1(KingdomKeys.MODID + ".keyfunc.health1", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 1)),
	HEALTH_2(KingdomKeys.MODID + ".keyfunc.health2", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 2)),
	HEALTH_3(KingdomKeys.MODID + ".keyfunc.health3", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 3)),
	HEALTH_4(KingdomKeys.MODID + ".keyfunc.health4", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 4)),
	HEALTH_5(KingdomKeys.MODID + ".keyfunc.health5", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 5)),
	HEALTH_6(KingdomKeys.MODID + ".keyfunc.health6", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 6)),
	HEALTH_7(KingdomKeys.MODID + ".keyfunc.health7", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 7)),
	HEALTH_8(KingdomKeys.MODID + ".keyfunc.health8", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 8)),
	HEALTH_9(KingdomKeys.MODID + ".keyfunc.health9", (k, m, w, p) -> FunctionHealth.getRGBKeyColor(p, 9)),

	FOOD(KingdomKeys.MODID + ".keyfunc.food", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p)),
	FOOD_0(KingdomKeys.MODID + ".keyfunc.food0", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 0)),
	FOOD_1(KingdomKeys.MODID + ".keyfunc.food1", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 1)),
	FOOD_2(KingdomKeys.MODID + ".keyfunc.food2", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 2)),
	FOOD_3(KingdomKeys.MODID + ".keyfunc.food3", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 3)),
	FOOD_4(KingdomKeys.MODID + ".keyfunc.food4", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 4)),
	FOOD_5(KingdomKeys.MODID + ".keyfunc.food5", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 5)),
	FOOD_6(KingdomKeys.MODID + ".keyfunc.food6", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 6)),
	FOOD_7(KingdomKeys.MODID + ".keyfunc.food7", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 7)),
	FOOD_8(KingdomKeys.MODID + ".keyfunc.food8", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 8)),
	FOOD_9(KingdomKeys.MODID + ".keyfunc.food9", (k, m, w, p) -> FunctionFood.getRGBKeyColor(p, 9)),

	AIR(KingdomKeys.MODID + ".keyfunc.air", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p)),
	AIR_0(KingdomKeys.MODID + ".keyfunc.air0", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 0)),
	AIR_1(KingdomKeys.MODID + ".keyfunc.air1", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 1)),
	AIR_2(KingdomKeys.MODID + ".keyfunc.air2", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 2)),
	AIR_3(KingdomKeys.MODID + ".keyfunc.air3", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 3)),
	AIR_4(KingdomKeys.MODID + ".keyfunc.air4", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 4)),
	AIR_5(KingdomKeys.MODID + ".keyfunc.air5", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 5)),
	AIR_6(KingdomKeys.MODID + ".keyfunc.air6", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 6)),
	AIR_7(KingdomKeys.MODID + ".keyfunc.air7", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 7)),
	AIR_8(KingdomKeys.MODID + ".keyfunc.air8", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 8)),
	AIR_9(KingdomKeys.MODID + ".keyfunc.air9", (k, m, w, p) -> FunctionAir.getRGBKeyColor(p, 9)),
	
	RESET(KingdomKeys.MODID + ".keyfunc.reset", (k, m, w, p) -> FunctionReset.getRGBKeyColor(p));

	private final IFunctionCallback callback;
	private final String label;

	private KeyFunction(String label, IFunctionCallback callback) {
		this.label = label;
		this.callback = callback;
	}

	public String getLabel() {
		return this.label;
	}

	public IFunctionCallback getCallback() {
		return this.callback;
	}

	public String toString() {
		return I18n.format((String) this.getLabel(), new Object[0]);
	}
}