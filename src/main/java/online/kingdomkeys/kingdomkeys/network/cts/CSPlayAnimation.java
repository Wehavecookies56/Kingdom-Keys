package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.integration.epicfight.init.KKAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.function.Supplier;

public class CSPlayAnimation {
    protected StaticAnimation animation;
    public CSPlayAnimation()
    {

    }
    public CSPlayAnimation(StaticAnimation animation)
    {
        this.animation = animation;
    }
    public void encode(FriendlyByteBuf buffer) {
        if(animation != null)
            buffer.writeUtf(animation.toString());
    }
    public static CSPlayAnimation decode(FriendlyByteBuf buffer) {
        CSPlayAnimation msg = new CSPlayAnimation();
        return msg;
    }

    public static void handle(CSPlayAnimation message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()->{
            ServerPlayerPatch spp = EpicFightCapabilities.getEntityPatch(ctx.get().getSender(), ServerPlayerPatch.class);
            spp.playAnimationSynchronized(KKAnimations.Summon_Test, 0);
        });
    }

}
