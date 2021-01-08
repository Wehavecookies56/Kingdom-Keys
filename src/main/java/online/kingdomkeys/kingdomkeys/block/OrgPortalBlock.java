package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
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

					if (te.getOwner() == null) {
						te.setOwner(player);
						te.markDirty();

						for (byte i = 0; i < 3; i++) {
							PortalData coords = ModCapabilities.getPlayer(player).getPortalCoords(i);
							if (coords.getX() == 0.0D && coords.getY() == 0.0D && coords.getZ() == 0.0D) {
								index = i;
								break;
							}
						}

						if (index != -1) {
							player.sendStatusMessage(new TranslationTextComponent(TextFormatting.GREEN + "This is now your portal " + (index + 1)), true);
							ModCapabilities.getPlayer(player).setPortalCoords((byte) index, new PortalData((byte) index, pos.getX(), pos.getY(), pos.getZ(), player.world.getDimensionKey()));
							PacketHandler.syncToAllAround(player, ModCapabilities.getPlayer(player));
							te.setOwner(player);
						} else {
							player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"), true);
						}
						return ActionResultType.SUCCESS;

					} else if (te.getOwner().equals(player.getUniqueID())) {
						for (byte i = 0; i < 3; i++) {
							PortalData coords = ModCapabilities.getPlayer(player).getPortalCoords(i);
							if (coords.getX() == 0.0D && coords.getY() == 0.0D && coords.getZ() == 0.0D) {
								index = i;
								break;
							}
						}
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.YELLOW + "This is your portal " + index), true);
					} else {
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "This portal belongs to " + worldIn.getPlayerByUuid(te.getOwner()).getDisplayName().getString()), true);
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
			// System.out.println(world.getTileEntity(pos));

			if (worldIn.getTileEntity(pos) instanceof OrgPortalTileEntity) {
				OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
				if (te.getOwner() != null) {
					PlayerEntity player = worldIn.getPlayerByUuid(te.getOwner());

					byte index = -1;
					for (byte i = 0; i < 3; i++) {
						PortalData coords = ModCapabilities.getPlayer(player).getPortalCoords(i);
						if (coords.getX() == pos.getX() && coords.getY() == pos.getY() && coords.getZ() == pos.getZ()) {
							index = i;
							break;
						}
					}
					System.out.println("R: " + index);
					if (index != -1) {
						player.sendStatusMessage(new TranslationTextComponent(TextFormatting.RED + "Portal destination disappeared"), true);
						ModCapabilities.getPlayer(player).setPortalCoords((byte) index, new PortalData((byte) index, 0, 0, 0, player.world.getDimensionKey()));
					} else {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"), Util.DUMMY_UUID);
					}

					PacketHandler.syncToAllAround(player, ModCapabilities.getPlayer(player));
				}
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
}
