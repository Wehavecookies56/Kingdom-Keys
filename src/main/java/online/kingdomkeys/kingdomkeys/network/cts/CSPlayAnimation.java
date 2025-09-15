package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class CSPlayAnimation {
    protected AnimationManager.AnimationAccessor<?> animation;
    public CSPlayAnimation()
    {

    }
    public CSPlayAnimation(AnimationManager.AnimationAccessor<?> animation)
    {
        this.animation = animation;
    }
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(animation.id());
    }
    public static CSPlayAnimation decode(FriendlyByteBuf buffer) {
        CSPlayAnimation msg = new CSPlayAnimation();
        msg.animation = AnimationManager.byId(buffer.readInt());
        //(String)l.get(2)
        return msg;
    }

    public static void handle(CSPlayAnimation message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()->{
            ServerPlayerPatch spp = EpicFightCapabilities.getEntityPatch(ctx.get().getSender(), ServerPlayerPatch.class);
            spp.playAnimationSynchronized((AssetAccessor<? extends StaticAnimation>) message.animation, 0);
        });
    }

}
