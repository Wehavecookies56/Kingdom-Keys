package online.kingdomkeys.kingdomkeys.block;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOrgPortalGUI;
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
	
	private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return Block.makeCuboidShape(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isRemote) {
			if (placer instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) placer;
				OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
				IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
	
				List<UUID> portals = worldData.getAllPortalsFromOwnerID(player.getUniqueID());
	
				if (portals.size() < 3) {
					UUID portalUUID = UUID.randomUUID();
	
					worldData.addPortal(portalUUID, new PortalData(portalUUID, "Portal", pos.getX(), pos.getY()-1, pos.getZ(), player.world.getDimensionKey(), player.getUniqueID()));
					Utils.syncWorldData(worldIn, worldData);
	
					player.sendStatusMessage(new TranslationTextComponent(TextFormatting.GREEN + "This is now your portal"), true);
	
					te.setUUID(portalUUID);
					te.markDirty();
					PacketHandler.sendTo(new SCShowOrgPortalGUI(te.getPos()), (ServerPlayerEntity) player);
				} else {
					player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"), true);
				}
			}
		}		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			if (ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
				if (worldIn.getTileEntity(pos) instanceof OrgPortalTileEntity) {
					OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);

					if (te.getUUID() == null) { // Player clicks new portal

					} else if (worldData.getOwnerIDFromUUID(te.getUUID()).equals(player.getUniqueID())) { // Player clicks his portal
						List<UUID> portals = worldData.getAllPortalsFromOwnerID(player.getUniqueID());
						byte i = 0;
						for (i = 0; i < portals.size(); i++) {
							if (portals.get(i).equals(te.getUUID())) {
								break;
							}
						}
						PacketHandler.sendTo(new SCShowOrgPortalGUI(te.getPos()), (ServerPlayerEntity)player);
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.YELLOW + "This is your portal " + (i+1)+": "+worldData.getPortalFromUUID(portals.get(i)).getName()), true);
					} else {
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "This portal belongs to " + worldIn.getPlayerByUuid(worldData.getOwnerIDFromUUID(te.getUUID())).getDisplayName().getString()), true);
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
				UUID portalID = te.getUUID();
				te.remove();
				if (portalID != null) {
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
					UUID ownerUUID = worldData.getOwnerIDFromUUID(portalID);
					
					ModCapabilities.getWorld(worldIn).removePortal(portalID);

					PlayerEntity player = worldIn.getServer().getPlayerList().getPlayerByUUID(ownerUUID);
					if(player != null) { //Remove from player's menu
						Utils.syncWorldData(worldIn, worldData);
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "Portal destination disappeared"), true);
					}
				}
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
}
