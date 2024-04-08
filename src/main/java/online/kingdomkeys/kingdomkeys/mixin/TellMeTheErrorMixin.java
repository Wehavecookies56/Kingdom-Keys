package online.kingdomkeys.kingdomkeys.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ConcurrentModificationException;

@Mixin(Connection.class)
public class TellMeTheErrorMixin {

    @Inject(method = "exceptionCaught", at=@At("HEAD"))
    public void printError(ChannelHandlerContext pContext, Throwable pException, CallbackInfo ci) {
        if (pException instanceof ConcurrentModificationException) {
            pException.printStackTrace();
        }
    }

}
