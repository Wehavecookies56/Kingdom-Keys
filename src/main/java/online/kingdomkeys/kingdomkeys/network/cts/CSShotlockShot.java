package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public record CSShotlockShot(List<Utils.ShotlockPosition> shotlockEnemies, double cost) implements Packet {
	
	public static final Type<CSShotlockShot> TYPE = new Type<>(KingdomKeys.rl("cs_shotlock_shot"));

	public static final StreamCodec<FriendlyByteBuf, CSShotlockShot> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, Utils.ShotlockPosition.STREAM_CODEC), CSShotlockShot::shotlockEnemies,
			ByteBufCodecs.DOUBLE, CSShotlockShot::cost,
			CSShotlockShot::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();

		PlayerData playerData = PlayerData.get(player);
		Shotlock shotlock = Utils.getPlayerShotlock(player);

		List<Entity> targets = new ArrayList<>();

		for(Utils.ShotlockPosition enemy : shotlockEnemies) {
			Entity target = player.level().getEntity(enemy.id());
			targets.add(target);
		}

		boolean fullShotlock = targets.size() == shotlock.getMaxLocks();
		playerData.setHasShotMaxShotlock(fullShotlock);

		shotlock.onUse(player, targets);
		playerData.remFocus(cost);
		PacketHandler.syncToAllAround(player, playerData);

		if (fullShotlock) {
			ShotlockMinigameHandler.start(player, shotlock, targets);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}