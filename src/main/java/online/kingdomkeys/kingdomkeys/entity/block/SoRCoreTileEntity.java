package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SoRCoreTileEntity extends BlockEntity {
	UUID userUUID;
	static int ticks = 0;
	
	public SoRCoreTileEntity(BlockPos pos, BlockState state) {
		super(ModEntities.TYPE_SOR_CORE_TE.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.saveAdditional(pTag, provider);
		if (userUUID != null)
			pTag.putUUID("uuid", userUUID);
	}

	@Override
	public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
		super.loadAdditional(pTag, provider);
		if(pTag.hasUUID("uuid"))
			userUUID = pTag.getUUID("uuid");
	}

	public UUID getUUID() {
		return userUUID;
	}

	public void setUUID(UUID uuid) {
		this.userUUID = uuid;
		setChanged();
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
		loadAdditional(pkt.getTag(), provider);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
		return saveCustomOnly(pRegistries);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		loadAdditional(tag, lookupProvider);
	}

	public static <T> void tick(Level level, BlockPos pos, BlockState state, T blockEntity) {
		if(!level.isClientSide) {
			//System.out.println(ticks);
			if(ticks == 0) {
				//spawnSoR();
			}
			
			if(ticks > 12000) {
				//removeSoR();
			}
			ticks++;
		}
	}

		//x
		int sorWidth = 25;
	    //z
	    int sorDepth = 25;
	    
	    int colHeight = 6;

	    String sorTop =
	    		"0000000000111110000000000" +
	    		"0000000011441441100000000" +
	            "0030001114444444111000300" +
	            "0000111114444444111110000" +
	            "0001414111414141114141000" +
	            "0001141111114111111411000" +
	            "0011414114414144114141100" +
	            "0011111144444444411111100" +
	            "0111111414414144141111110" +
	            "0144114441144411444114410" +
	            "1444414441144411444144441" +
	            "1444111414414144141114441" +
	            "1144444444440444444444411" + //Middle is 0 since it is already gonna be filled by the core block previously
	            "1444111414414144141114441" +
	            "1444414441144411444144441" +
	            "0144114441144411444114410" +
	            "0111111414414144141111110" +
	            "0011111144444444411111100" +
	            "0011414114414144114141100" +
	            "0001141111114111111411000" +
	            "0001414111414141114141000" +
	            "0000111114444444111110000" +
	            "0030001114444444111000300" +
	            "0000000011441441100000000" +
	    		"0000000000111110000000000";

		/*public static void spawnSoR() {
			BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
			int startZ = worldPosition.getZ() - (sorDepth / 2);
			int startX = worldPosition.getX() - (sorWidth / 2);

			//for (int y = 0; y < baseY; ++y) {
				for (int z = startZ; z <= worldPosition.getZ() + (sorDepth / 2); ++z) {
					for (int x = startX; x <= worldPosition.getX() + (sorWidth / 2); ++x) {
						blockpos$mutable.set(x, worldPosition.getY(), z);
						int strucX = x - startX;
						int strucZ = z - startZ;
						//if (y == 1) {
							stateToPlace(sorTop.charAt(strucX + strucZ * sorWidth), blockpos$mutable);
						//}
					}
				}
			//}
			
		}*/

		private void stateToPlace(char c, BlockPos.MutableBlockPos pos) {
			switch (c) {
			case '0':
				return;
			case '1':
				level.setBlock(pos, Blocks.QUARTZ_BLOCK.defaultBlockState(), 2);
				break;
			case '2':
				level.setBlock(pos, ModBlocks.sorCore.get().defaultBlockState(), 2);
				break;
			case '3':
				/*for (int i = 0; i <= colHeight; i++) {
					world.setBlockState(pos, Blocks.QUARTZ_PILLAR.getDefaultState(), 2);
					pos.setY(pos.getY() + 1);
				}*/
				break;
			case '4':
				level.setBlock(pos, Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState(), 2);
				break;
			}
		}
		
	/*public static void removeSoR() {
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
		int startZ = this.worldPosition.getZ() - (sorDepth / 2);
		int startX = this.worldPosition.getX() - (sorWidth / 2);

		for (int z = startZ; z <= this.worldPosition.getZ() + (sorDepth / 2); ++z) {
			for (int x = startX; x <= this.worldPosition.getX() + (sorWidth / 2); ++x) {
				blockpos$mutable.set(x, this.worldPosition.getY(), z);
				level.setBlock(blockpos$mutable, Blocks.AIR.defaultBlockState(), 2);
			}
		}
	}*/
	
}