package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;

public class SCAeroSoundPacket {

	int entID;
	public SCAeroSoundPacket() {
		
	}

	public SCAeroSoundPacket(LivingEntity ent) {
		this.entID = ent.getId();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(entID);
	}

	public static SCAeroSoundPacket decode(FriendlyByteBuf buffer) {
		SCAeroSoundPacket msg = new SCAeroSoundPacket();
		msg.entID = buffer.readInt();
		return msg;
	}

	public static void handle(final SCAeroSoundPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.aeroSoundInstance(message.entID)));
		ctx.get().setPacketHandled(true);
	}

}
