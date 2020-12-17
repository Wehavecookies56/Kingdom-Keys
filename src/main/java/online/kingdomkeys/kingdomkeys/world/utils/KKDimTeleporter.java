package online.kingdomkeys.kingdomkeys.world.utils;

import java.util.function.Function;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

/**
 * Created by Toby on 01/08/2016.
 */
public class KKDimTeleporter extends Teleporter {

	BlockPos pos;
	String dimension;

	public KKDimTeleporter(ServerWorld worldIn, BlockPos pos, String dimension) {
		super(worldIn);
		this.pos = pos;
		this.dimension = dimension;

	}

	/*public void teleport(PlayerEntity player, BlockPos pos, int dimension) {
		ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
		playerMP.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		playerMP.setMotion(0, 0, 0);
		if (player.dimension.getId() != dimension)
			playerMP.changeDimension(DimensionType.getById(dimension));
		playerMP.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
	}*/

	@Override
	public boolean placeInPortal(Entity entityIn, float rotationYaw) {
		return false;
	}

	@Override
	public boolean makePortal(Entity entityIn) {
		return false;
	}

	@Override
	public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		if (entity instanceof PlayerEntity) {
			ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
			playerMP.setMotion(0,0,0);

			/*
			//WorldSavedDataKingdomKeys data = WorldSavedDataKingdomKeys.get(world);
			if (dimension == ModDimensions.destinyIslandsDimension.get() || dimension.equals(ModDimensions.traverseTownDimension.get().getRegistryName())) {
				//if (!data.isGenerated()) {
					entity.sendMessage(new TranslationTextComponent("Generating world, this will take a while..."));
					entity.sendMessage(new TranslationTextComponent("This only happens the first time you visit the world"));
					WorldLoader loader = new WorldLoader();
					loader.processAndGenerateStructureFile(dimension, destWorld, new BlockPos(0,0,0));//genData.offset);

					entity.sendMessage(new TranslationTextComponent("World generated completed, please wait while chunks load..."));
					entity.sendMessage(new TranslationTextComponent("Expect a large performance drop while this happens"));
				//	data.setGenerated(true);
				//}
			} else if (dimension == ModDimensions.diveToTheHeartID) {
				generateSOA(world);
			}


			 */


			if (pos == null) { // TODO spawn point for overworld
				pos = playerMP.getBedLocation();
				if (pos == null) {
					// FirstTimeJoinCapability.IFirstTimeJoin originalPos =
					// playerMP.getCapability(ModCapabilities.FIRST_TIME_JOIN, null);
					// pos = new
					// BlockPos(originalPos.getPosX(),originalPos.getPosY(),originalPos.getPosZ());
				}
			}
			Vec3d destination = getSpawnPoint(pos, world);
			playerMP.connection.setPlayerLocation(destination.x, destination.y, destination.z, playerMP.rotationYaw, playerMP.rotationPitch);
		}
		return entity;
	}
	
	public Vec3d getSpawnPoint(BlockPos pos, World world) {
		Vec3d centre = new Vec3d((pos.getX()) + 0.5, (pos.getY()) + 0.5, (pos.getZ()) + 0.5);
		return centre;
	}
	
	private void generateSOA(World world) {
		double dx = -1;
        double dy = 64;
        double dz = -1;
        
		int radius = 15;
        int barrierRadius = 16;
        int barrierHeight = 5;
        
        for(int i = 0; i<barrierHeight; i++) {
            for(float j = 0; j < 2 * Math.PI * barrierRadius; j += 0.5) {
                world.setBlockState(new BlockPos((int)Math.floor(dx + Math.sin(j) * barrierRadius), dy +i, (int)Math.floor(dz + Math.cos(j) * barrierRadius)), Blocks.BARRIER.getDefaultState());
            }
        }
        
        for(float i = 0; i < radius+2; i += 0.5) {
            for(float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                if(i > radius) {
                    world.setBlockState(new BlockPos((int)Math.floor(dx + Math.sin(j) * i), dy, (int)Math.floor(dz + Math.cos(j) * i)), Blocks.BARRIER.getDefaultState());
                }else {
                    world.setBlockState(new BlockPos((int)Math.floor(dx + Math.sin(j) * i), dy, (int)Math.floor(dz + Math.cos(j) * i)), Blocks.GLOWSTONE.getDefaultState());
                }
            }
        }
        
        /*world.setBlockState(new BlockPos(dx+(-12), dy, dz+(-13)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 0));
        world.setBlockState(new BlockPos(dx+(-4), dy, dz+(-13)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 1));
        world.setBlockState(new BlockPos(dx+(4), dy, dz+(-13)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 2));
        world.setBlockState(new BlockPos(dx+(12), dy, dz+(-13)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 3));

        world.setBlockState(new BlockPos(dx+(-12), dy, dz+(-5)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 4));
        world.setBlockState(new BlockPos(dx+(-4), dy, dz+(-5)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 5));
        world.setBlockState(new BlockPos(dx+(4), dy, dz+(-5)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 6));
        world.setBlockState(new BlockPos(dx+(12), dy, dz+(-5)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 7));

        world.setBlockState(new BlockPos(dx+(-12), dy, dz+(3)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 8));
        world.setBlockState(new BlockPos(dx+(-4), dy, dz+(3)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 9));
        world.setBlockState(new BlockPos(dx+(4), dy, dz+(3)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 10));
        world.setBlockState(new BlockPos(dx+(12), dy, dz+(3)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 11));

        world.setBlockState(new BlockPos(dx+(-12), dy, dz+(11)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 12));
        world.setBlockState(new BlockPos(dx+(-4), dy, dz+(11)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 13));
        world.setBlockState(new BlockPos(dx+(4), dy, dz+(11)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 14));
        world.setBlockState(new BlockPos(dx+(12), dy, dz+(11)), ModBlocks.StationOfAwakening.getDefaultState().withProperty(BlockStationOfAwakening.VARIANT, 15));

        TileEntityPedestal shieldPedestal = new TileEntityPedestal();
        shieldPedestal.setKeyblade(new ItemStack(ModItems.DreamShield));
        shieldPedestal.itemStacks.setStackInSlot(0, new ItemStack(ModItems.DreamShield));
        shieldPedestal.rotation = 3;
        world.setBlockState(new BlockPos(dx-12, dy+1, dz), ModBlocks.Pedestal.getDefaultState()); //Shield
        world.setTileEntity(new BlockPos(dx-12, dy+1, dz), shieldPedestal);
        
        TileEntityPedestal staffPedestal = new TileEntityPedestal();
        staffPedestal.setKeyblade(new ItemStack(ModItems.DreamStaff));
        staffPedestal.itemStacks.setStackInSlot(0, new ItemStack(ModItems.DreamStaff));
        staffPedestal.rotation=1;
        world.setBlockState(new BlockPos(dx+12, dy+1, dz), ModBlocks.Pedestal.getDefaultState()); //Staff
        world.setTileEntity(new BlockPos(dx+12, dy+1, dz), staffPedestal);

        TileEntityPedestal swordPedestal = new TileEntityPedestal();
        swordPedestal.setKeyblade(new ItemStack(ModItems.DreamSword));
        swordPedestal.itemStacks.setStackInSlot(0, new ItemStack(ModItems.DreamSword));
        swordPedestal.rotation=1;
        world.setBlockState(new BlockPos(dx, dy+1, dz-12), ModBlocks.Pedestal.getDefaultState());  //Sword
        world.setTileEntity(new BlockPos(dx, dy+1, dz-12), swordPedestal);

        world.setBlockState(new BlockPos(dx, dy+1, dz+12), ModBlocks.StationOfAwakeningDoor.getDefaultState());*/
	}


}
