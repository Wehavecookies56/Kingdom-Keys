package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

public class SCSyncOrgPortalPacket {

	public BlockPos pos;
	public BlockPos destPos;
	public ResourceKey<Level> dimension;

	public SCSyncOrgPortalPacket() {
	}

	public SCSyncOrgPortalPacket(BlockPos pos, BlockPos dest, ResourceKey<Level> dim) {
		this.pos = pos;
        this.destPos = dest;
        this.dimension = dim;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
        buffer.writeBlockPos(destPos);
        buffer.writeResourceLocation(dimension.location());
	}

	public static SCSyncOrgPortalPacket decode(FriendlyByteBuf buffer) {
		SCSyncOrgPortalPacket msg = new SCSyncOrgPortalPacket();
		msg.pos = buffer.readBlockPos();
        msg.destPos = buffer.readBlockPos();
        msg.dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());
		return msg;
	}

	public static void handle(final SCSyncOrgPortalPacket msg, Supplier<NetworkEvent.Context> ctx) {
		
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncOrgPortal(msg)));
		ctx.get().setPacketHandled(true);
	}

}
