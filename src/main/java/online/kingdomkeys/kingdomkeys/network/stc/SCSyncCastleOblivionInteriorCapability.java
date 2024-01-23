package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class SCSyncCastleOblivionInteriorCapability {

	public CompoundTag data;

	public SCSyncCastleOblivionInteriorCapability() {
	}

	public SCSyncCastleOblivionInteriorCapability(CastleOblivionCapabilities.ICastleOblivionInteriorCapability worldData) {
		this.data = worldData.serializeNBT();
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeNbt(this.data);
	}

	public static SCSyncCastleOblivionInteriorCapability decode(FriendlyByteBuf buffer) {
		SCSyncCastleOblivionInteriorCapability msg = new SCSyncCastleOblivionInteriorCapability();
		msg.data = buffer.readNbt();
		return msg;	
	}

	public static void handle(final SCSyncCastleOblivionInteriorCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncCastleOblivionInterior(message)));
		ctx.get().setPacketHandled(true);
	}

	public static void syncClients(Level level) {
		PacketHandler.sendToAllPlayers(new SCSyncCastleOblivionInteriorCapability(ModCapabilities.getCastleOblivionInterior(level)));
	}

}
