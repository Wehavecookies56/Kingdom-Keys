package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;

public class MiniCO {
	public static final ResourceKey<Structure> STRUCTURE = ResourceKey.create(Registries.STRUCTURE, KingdomKeys.rl("mini_co"));

	// Where falling out of Castle Oblivion's exterior leaves you, in front of the entrance
	private static final int EXIT_X = 16, EXIT_Z = 25, EXIT_ABOVE_BASE = 5;

	@SubscribeEvent
	public void playerTick(PlayerTickEvent.Pre event) {
		if (!event.getEntity().isCreative() && !event.getEntity().level().isClientSide()) {
			if (CastleOblivionHandler.inExterior(event.getEntity())){
				if (event.getEntity().getY() < 0) {
					ServerLevel serverlevel = ((ServerLevel) event.getEntity().level()).getServer().overworld();
                    ServerPlayer sPlayer = (ServerPlayer) event.getEntity();

                    BlockPos pos = exitPos(serverlevel);
                    sPlayer.changeDimension(new DimensionTransition(serverlevel, new Vec3(pos.getX()+0.5F, pos.getY(), pos.getZ()+0.5F), Vec3.ZERO, event.getEntity().getYRot(), event.getEntity().getXRot(), entity -> {}));
                    sPlayer.fallDistance = 0;
                }
			}
		}
	}

	private static BlockPos exitPos(ServerLevel overworld) {
		int x = (ModConfigs.coEntranceChunkX * 16) + EXIT_X;
		int z = (ModConfigs.coEntranceChunkZ * 16) + EXIT_Z;

		Structure structure = overworld.registryAccess().registryOrThrow(Registries.STRUCTURE).get(STRUCTURE);
		if (structure != null) {
			StructureStart start = overworld.getChunk(ModConfigs.coEntranceChunkX, ModConfigs.coEntranceChunkZ, ChunkStatus.STRUCTURE_STARTS).getStartForStructure(structure);
			// The start's own box is grown 12 blocks each way for the terrain adaptation, the piece's is the real one
			if (start != null && start.isValid() && !start.getPieces().isEmpty()) {
				return new BlockPos(x, start.getPieces().getFirst().getBoundingBox().minY() + EXIT_ABOVE_BASE, z);
			}
		}

		// Worlds from before it was a structure had it placed by hand, and kept its height
		WorldData worldData = WorldData.get(overworld.getServer());
		if (worldData.isMiniCOGenerated()) {
			return new BlockPos(x, worldData.getMiniCOY() + 3, z);
		}

		return new BlockPos(x, overworld.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);
	}
}
