package online.kingdomkeys.kingdomkeys.block;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOrgPortalGUI;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class OrgPortalBlock extends BaseBlock implements EntityBlock {

	public OrgPortalBlock(Properties properties) {
		super(properties);
	}
	
	private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return collisionShape;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return collisionShape;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Block.box(0D, 0D, 0D, 16.0D, 2.0D, 16.0D);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (!worldIn.isClientSide) {
			if (placer instanceof Player) {
				Player player = (Player) placer;
				OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getBlockEntity(pos);
				IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
	
				List<UUID> portals = worldData.getAllPortalsFromOwnerID(player.getUUID());
	
				if (portals.size() < 3) {
					UUID portalUUID = UUID.randomUUID();
	
					worldData.addPortal(portalUUID, new PortalData(portalUUID, "Portal", pos.getX(), pos.getY()-1, pos.getZ(), player.level.dimension(), player.getUUID()));
					Utils.syncWorldData(worldIn, worldData);
	
					player.displayClientMessage(new TranslatableComponent(ChatFormatting.GREEN + "This is now your portal"), true);
	
					te.setUUID(portalUUID);
					te.setChanged();
					PacketHandler.sendTo(new SCShowOrgPortalGUI(te.getBlockPos()), (ServerPlayer) player);
				} else {
					player.displayClientMessage(new TranslatableComponent(ChatFormatting.RED + "You have no empty slots for portals"), true);
				}
			}
		}		
		super.setPlacedBy(worldIn, pos, state, placer, stack);
	}
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide) {
			if (ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
				if (worldIn.getBlockEntity(pos) instanceof OrgPortalTileEntity) {
					OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getBlockEntity(pos);
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);

					if (te.getUUID() == null) { // Player clicks new portal

					} else if (worldData.getOwnerIDFromUUID(te.getUUID()).equals(player.getUUID())) { // Player clicks his portal
						List<UUID> portals = worldData.getAllPortalsFromOwnerID(player.getUUID());
						byte i = 0;
						for (i = 0; i < portals.size(); i++) {
							if (portals.get(i).equals(te.getUUID())) {
								break;
							}
						}
						PacketHandler.sendTo(new SCShowOrgPortalGUI(te.getBlockPos()), (ServerPlayer)player);
						player.displayClientMessage(new TranslatableComponent(ChatFormatting.YELLOW + "This is your portal " + (i+1)+": "+worldData.getPortalFromUUID(portals.get(i)).getName()), true);
					} else {
						player.displayClientMessage(new TranslatableComponent(ChatFormatting.RED + "This portal belongs to " + worldIn.getPlayerByUUID(worldData.getOwnerIDFromUUID(te.getUUID())).getDisplayName().getString()), true);
						return InteractionResult.SUCCESS;
					}

				}
			}
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!worldIn.isClientSide) {
			if (worldIn.getBlockEntity(pos) instanceof OrgPortalTileEntity) {
				OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getBlockEntity(pos);
				UUID portalID = te.getUUID();
				te.setRemoved();
				if (portalID != null) {
					IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
					UUID ownerUUID = worldData.getOwnerIDFromUUID(portalID);
					
					ModCapabilities.getWorld(worldIn).removePortal(portalID);

					Player player = worldIn.getServer().getPlayerList().getPlayer(ownerUUID);
					if(player != null) { //Remove from player's menu
						Utils.syncWorldData(worldIn, worldData);
						player.displayClientMessage(new TranslatableComponent(ChatFormatting.RED + "Portal destination disappeared"), true);
					}
				}
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_ORG_PORTAL_TE.get().create(pPos, pState);
	}
}
