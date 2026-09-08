package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.TrainingHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.util.List;

public record CSStartEncounter(boolean duel, ResourceLocation encounter) implements Packet {
	public static final Type<CSStartEncounter> TYPE = new Type<>(KingdomKeys.rl("cs_start_encounter"));

	public static final StreamCodec<FriendlyByteBuf, CSStartEncounter> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, CSStartEncounter::duel,
			ResourceLocation.STREAM_CODEC, CSStartEncounter::encounter,
			CSStartEncounter::new
	);

	private static final double REACH = 8.0D;

	@Override
	public void handle(IPayloadContext context) {
		if (!(context.player() instanceof ServerPlayer pupil)) {
			return;
		}

		PlayerData playerData = PlayerData.get(pupil);
		if (playerData == null || !playerData.hasUnion()) {
			return;
		}

		RoomEncounter lesson = (duel ? ModJsonRegistries.DUEL_ENCOUNTER : ModJsonRegistries.TRAINING_ENCOUNTER).get().getValue(encounter);
		if (lesson == null) {
			return;
		}

		// The master has to actually be here to set anyone on anything
		List<ForetellerEntity> nearby = pupil.level().getEntitiesOfClass(ForetellerEntity.class, pupil.getBoundingBox().inflate(REACH), master -> master.getUnion() == playerData.getUnion() && master.isAlive());

		if (nearby.isEmpty()) {
			return;
		}

		TrainingHandler.start(pupil, nearby.getFirst(), lesson);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
