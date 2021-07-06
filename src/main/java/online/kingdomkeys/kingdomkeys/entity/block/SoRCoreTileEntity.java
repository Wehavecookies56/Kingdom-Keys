package online.kingdomkeys.kingdomkeys.entity.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SoRCoreTileEntity extends TileEntity implements ITickableTileEntity {
	UUID userUUID;
	int ticks = 0;
	
	public SoRCoreTileEntity() {
		super(ModEntities.TYPE_SOR_CORE_TE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
		super.write(parentNBTTagCompound);
		if (userUUID != null)
			parentNBTTagCompound.putUniqueId("uuid", userUUID);
		return parentNBTTagCompound;
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		if(nbt.hasUniqueId("uuid"))
			userUUID = nbt.getUniqueId("uuid");
	}

	public UUID getUUID() {
		return userUUID;
	}

	public void setUUID(UUID uuid) {
		this.userUUID = uuid;
		markDirty();
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		this.read(state, tag);
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			System.out.println(ticks);
			if(ticks == 0) {
				spawnSoR();
			}
			
			if(ticks > 12000) {
				removeSoR();
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

		public void spawnSoR() {
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
			int startZ = pos.getZ() - (sorDepth / 2);
			int startX = pos.getX() - (sorWidth / 2);

			//for (int y = 0; y < baseY; ++y) {
				for (int z = startZ; z <= pos.getZ() + (sorDepth / 2); ++z) {
					for (int x = startX; x <= pos.getX() + (sorWidth / 2); ++x) {
						blockpos$mutable.setPos(x, pos.getY(), z);
						int strucX = x - startX;
						int strucZ = z - startZ;
						//if (y == 1) {
							stateToPlace(sorTop.charAt(strucX + strucZ * sorWidth), blockpos$mutable);
						//}
					}
				}
			//}
			
		}

		private void stateToPlace(char c, BlockPos.Mutable pos) {
			switch (c) {
			case '0':
				return;
			case '1':
				world.setBlockState(pos, Blocks.QUARTZ_BLOCK.getDefaultState(), 2);
				break;
			case '2':
				world.setBlockState(pos, ModBlocks.sorCore.get().getDefaultState(), 2);
				break;
			case '3':
				/*for (int i = 0; i <= colHeight; i++) {
					world.setBlockState(pos, Blocks.QUARTZ_PILLAR.getDefaultState(), 2);
					pos.setY(pos.getY() + 1);
				}*/
				break;
			case '4':
				world.setBlockState(pos, Blocks.LIGHT_GRAY_CONCRETE.getDefaultState(), 2);
				break;
			}
		}
		
	public void removeSoR() {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int startZ = this.pos.getZ() - (sorDepth / 2);
		int startX = this.pos.getX() - (sorWidth / 2);

		for (int z = startZ; z <= this.pos.getZ() + (sorDepth / 2); ++z) {
			for (int x = startX; x <= this.pos.getX() + (sorWidth / 2); ++x) {
				blockpos$mutable.setPos(x, this.pos.getY(), z);
				world.setBlockState(blockpos$mutable, Blocks.AIR.getDefaultState(), 2);
			}
		}
	}
	
}