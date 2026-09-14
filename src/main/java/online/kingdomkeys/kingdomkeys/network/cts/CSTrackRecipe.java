package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

public record CSTrackRecipe(ResourceLocation name) implements Packet {
	public static final Type<CSTrackRecipe> TYPE = new Type<>(KingdomKeys.rl("cs_track_recipe"));

	public static final StreamCodec<FriendlyByteBuf, CSTrackRecipe> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, CSTrackRecipe::name,
			CSTrackRecipe::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		if (playerData == null) {
			return;
		}

		if (!playerData.hasKnownRecipe(name) || !RecipeRegistry.getInstance().containsKey(name)) {
			return;
		}

		playerData.setTrackedRecipe(name.equals(playerData.getTrackedRecipe()) ? null : name);
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
