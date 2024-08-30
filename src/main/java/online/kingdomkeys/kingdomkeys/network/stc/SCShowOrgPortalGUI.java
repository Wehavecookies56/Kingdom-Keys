package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCShowOrgPortalGUI(BlockPos pos) implements Packet {

	public static final Type<SCShowOrgPortalGUI> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_show_org_portal_gui"));

	public static final StreamCodec<FriendlyByteBuf, SCShowOrgPortalGUI> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			SCShowOrgPortalGUI::pos,
			SCShowOrgPortalGUI::new
	);

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.showOrgPortalGUI(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
