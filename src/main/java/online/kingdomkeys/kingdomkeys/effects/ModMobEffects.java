package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, KingdomKeys.MODID);

    public static final Holder<MobEffect>
            FREEZE = MOB_EFFECTS.register("freeze", () -> new FreezeEffect(MobEffectCategory.HARMFUL, 0xFFFFFF)),
            STOP = MOB_EFFECTS.register("stop", () -> new StopEffect(MobEffectCategory.HARMFUL, 0xB3408D)),
            GRAVITY = MOB_EFFECTS.register("gravity", () -> new GravityEffect(MobEffectCategory.HARMFUL, 0x63337F)),
            AERO = MOB_EFFECTS.register("aero", () -> new AeroEffect(MobEffectCategory.BENEFICIAL, 0x60B86E)),
            KO = MOB_EFFECTS.register("ko", () -> new KOEffect(MobEffectCategory.BENEFICIAL, 0xFF6666)),
            UNDERWORLD_CURSE = MOB_EFFECTS.register("underworld_curse", () -> new NoDriveEffect(MobEffectCategory.HARMFUL, 0x612bad)),
            ZERO_GRAVITY = MOB_EFFECTS.register("zero_gravity", () -> new ZeroGravityEffect(MobEffectCategory.HARMFUL, 0x612bad)),
            CONFUSE = MOB_EFFECTS.register("confuse", () -> new ConfuseEffect(MobEffectCategory.HARMFUL, 0xCCCCFF));
}
