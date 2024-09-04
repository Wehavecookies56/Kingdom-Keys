//package online.kingdomkeys.kingdomkeys.integration.epicfight.capabilities;
//
//import java.util.Map;
//
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.item.UseAnim;
//import yesman.epicfight.api.animation.AnimationProvider;
//import yesman.epicfight.api.animation.LivingMotion;
//import yesman.epicfight.api.animation.LivingMotions;
//import yesman.epicfight.gameasset.Animations;
//import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
//import yesman.epicfight.world.capabilities.item.CapabilityItem;
//import yesman.epicfight.world.capabilities.item.WeaponCapability;
//
//public class ShieldCapabilities extends WeaponCapability {
//
//    public ShieldCapabilities(CapabilityItem.Builder builder) {
//        super(builder);
//    }
//
//    @Override
//    public UseAnim getUseAnimation(LivingEntityPatch<?> playerpatch) {
//        return UseAnim.NONE;
//    }
//
//    @Override
//    public boolean canHoldInOffhandAlone() {
//        return true;
//    }
//
//    @Override
//    public Map<LivingMotion, AnimationProvider<?>> getLivingMotionModifier(LivingEntityPatch<?> player, InteractionHand hand) {
//        if (this.livingMotionModifiers != null) {
//            if (hand != InteractionHand.OFF_HAND)
//                return this.livingMotionModifiers.get(this.getStyle(player));
//            else
//                return Map.of(LivingMotions.BLOCK_SHIELD, Animations.BIPED_BLOCK);
//        } else
//            return super.getLivingMotionModifier(player, hand);
//    }
//}
