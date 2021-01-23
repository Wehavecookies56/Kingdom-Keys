package online.kingdomkeys.kingdomkeys.block;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class OrgPortalBlock extends BaseBlock {

	public OrgPortalBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModEntities.TYPE_ORG_PORTAL_TE.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			byte index = -1;

			if (ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
				if (worldIn.getTileEntity(pos) instanceof OrgPortalTileEntity) {
					OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);

					if (te.getUUID() == null) { // Player clicks new portal
						for (byte i = 0; i < 3; i++) {
							UUID uuid = ModCapabilities.getPlayer(player).getPortalUUIDFromIndex(i);
							if (uuid.equals(new UUID(0,0))) {
								index = i;
								break;
							}
						}

						if (index != -1) {
							UUID portalUUID = UUID.randomUUID();
							
							worldData.addPortal(portalUUID, new PortalData(portalUUID, index+1+"", pos.getX(), pos.getY(), pos.getZ(), player.dimension.getId(), player.getUniqueID()));
							//PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
							PacketHandler.sendToAll(new SCSyncWorldCapability(worldData), player);
							
							player.sendStatusMessage(new TranslationTextComponent(TextFormatting.GREEN + "This is now your portal " + (index + 1)), true);
							
							IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
							playerData.setPortalCoordsUUID((byte) index, portalUUID);
							PacketHandler.syncToAllAround(player, ModCapabilities.getPlayer(player));
							te.setUUID(portalUUID);
							te.markDirty();
						} else {
							player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"), true);
						}
						return ActionResultType.SUCCESS;

					} else if (worldData.getOwnerIDFromUUID(te.getUUID()).equals(player.getUniqueID())) { // Player clicks his portal
						for (byte i = 0; i < 3; i++) {
							UUID uuid = ModCapabilities.getPlayer(player).getPortalUUIDFromIndex(i);
							if(uuid.equals(te.getUUID())) {
								index = i;
								break;
							}	
						}
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.YELLOW + "This is your portal " + (index+1)), true);
					} else {
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "This portal belongs to " + worldIn.getPlayerByUuid(worldData.getOwnerIDFromUUID(te.getUUID())).getDisplayName().getFormattedText()), true);
						return ActionResultType.SUCCESS;
					}

				}
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!worldIn.isRemote) {
			if (worldIn.getTileEntity(pos) instanceof OrgPortalTileEntity) {
				OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
				if (te.getUUID() != null) {
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
					UUID ownerUUID = worldData.getOwnerIDFromUUID(te.getUUID());
					PlayerEntity player = worldIn.getPlayerByUuid(ownerUUID);

					byte index = -1;
					for (byte i = 0; i < 3; i++) {
						UUID uuid = ModCapabilities.getPlayer(player).getPortalUUIDFromIndex(i);
						if(uuid.equals(te.getUUID())) {
							index = i;
							break;
						}
					}
					System.out.println("R: " + index);
					if (index != -1) {
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "Portal destination disappeared"), true);
						UUID uuid = ModCapabilities.getPlayer(player).getPortalUUIDFromIndex(index);
						ModCapabilities.getWorld(player.world).removePortal(uuid);
						ModCapabilities.getPlayer(player).setPortalCoordsUUID((byte) index, new UUID(0,0));
					} else {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"));
					}
					
					PacketHandler.sendToAll(new SCSyncWorldCapability(ModCapabilities.getWorld(player.world)), player);
					PacketHandler.syncToAllAround(player, ModCapabilities.getPlayer(player));

				}
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
}
