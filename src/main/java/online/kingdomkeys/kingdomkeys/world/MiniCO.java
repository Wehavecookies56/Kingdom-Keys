package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;

public class MiniCO {
	private static int pendingTicks = -1;

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event) {
		if (ModConfigs.generateCOEntrance) {
			if (!(event.getLevel() instanceof ServerLevel level))
				return;

			if (level.dimension() != Level.OVERWORLD)
				return;

			if (event.getChunk().getPos().x != ModConfigs.coEntranceChunkX || event.getChunk().getPos().z != ModConfigs.coEntranceChunkZ)
				return;

			ChunkPos chunkPos = new ChunkPos(ModConfigs.coEntranceChunkX, ModConfigs.coEntranceChunkZ);
			if (!level.hasChunk(chunkPos.x, chunkPos.z))
				return;

			WorldData worldData = WorldData.get(level.getServer());

			if (!worldData.isMiniCOGenerated() && pendingTicks < 0) {
				KingdomKeys.LOGGER.info("Pending Mini CO");
				pendingTicks = 40;
			}
		}
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		if (ModConfigs.generateCOEntrance) {
			if (pendingTicks < 0)
				return;

			pendingTicks--;

			if (pendingTicks > 0)
				return;

			ServerLevel level = event.getServer().overworld();
			WorldData worldData = WorldData.get(level.getServer());

			if(worldData.isMiniCOGenerated())
				return;
			
			int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (ModConfigs.coEntranceChunkX * 16) + 16, (ModConfigs.coEntranceChunkZ * 16) + 16);

			if (y <= level.getMinBuildHeight()) {
				KingdomKeys.LOGGER.info("Attempted to place too early, delaying placement");
				pendingTicks = 20;
				return;
			}

			if (generateMiniCO(level)) {
				worldData.setMiniCOGenerated(true);
				worldData.setMiniCOY(y);
				pendingTicks = -1;
			}
		}
	}

	private static boolean generateMiniCO(ServerLevel level) {
		if (level.dimension() != Level.OVERWORLD)
			return false;

		StructureTemplate template = level.getStructureManager().get(KingdomKeys.rl("castle_oblivion/mini_co")).orElse(null);

		if (template == null) {
			KingdomKeys.LOGGER.error("Mini CO template is null, couldn't find a valid nbt file in castle_oblivion/mini_co");
			return false;
		}

		Vec3i size = template.getSize();

		int centerX = (ModConfigs.coEntranceChunkX * 16) + size.getX() / 2;
		int centerZ = (ModConfigs.coEntranceChunkZ * 16) + size.getZ() / 2;
		int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, centerX, centerZ) - 2;

		BlockPos origin = new BlockPos((ModConfigs.coEntranceChunkX * 16), y, (ModConfigs.coEntranceChunkZ * 16));

		KingdomKeys.LOGGER.info("About to place Mini CO");
		StructurePlaceSettings settings = new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true);
		boolean placed = template.placeInWorld(level, origin, origin, settings, level.random, Block.UPDATE_ALL);

		KingdomKeys.LOGGER.info("Placed Mini CO = {}", placed);
		return placed;
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent.Pre event) {
		if (!event.getEntity().isCreative() && !event.getEntity().level().isClientSide()) {
			if (CastleOblivionHandler.inExterior(event.getEntity())){
				if (event.getEntity().getY() < 0) {
					ServerLevel serverlevel = ((ServerLevel) event.getEntity().level()).getServer().overworld();
                    ServerPlayer sPlayer = (ServerPlayer) event.getEntity();

                    WorldData worldData = WorldData.get(serverlevel.getServer());

                    BlockPos pos = new BlockPos((ModConfigs.coEntranceChunkX * 16) + 16, worldData.getMiniCOY()+3,(ModConfigs.coEntranceChunkZ * 16) + 25);
                    sPlayer.changeDimension(new DimensionTransition(serverlevel, new Vec3(pos.getX()+0.5F, pos.getY(), pos.getZ()+0.5F), Vec3.ZERO, event.getEntity().getYRot(), event.getEntity().getXRot(), entity -> {}));
                    sPlayer.fallDistance = 0;
                }
			}
		}
	}
}
