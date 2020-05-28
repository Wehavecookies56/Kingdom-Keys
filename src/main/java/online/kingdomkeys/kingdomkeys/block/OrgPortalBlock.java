package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalCoords;

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
		return ModEntities.TYPE_ORG_PORTAL.get().create();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			byte index = -1;

			//if (player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE) {
				if (worldIn.getTileEntity(pos) instanceof OrgPortalTileEntity) {

					OrgPortalTileEntity te = (OrgPortalTileEntity) worldIn.getTileEntity(pos);
					System.out.println(te.getOwner());

					if (te.getOwner() == null) {
						te.setOwner(player);
						te.markDirty();

						for (byte i = 0; i < 3; i++) {
							PortalCoords coords = ModCapabilities.get(player).getPortalCoords(i);
							// System.out.println(i+" "+coords.getX());
							if (coords.getX() == 0.0D && coords.getY() == 0.0D && coords.getZ() == 0.0D) {
								index = i;
								break;
							}
						}
						// System.out.println("A: "+index);
						if (index != -1) {
							player.sendMessage(new TranslationTextComponent(TextFormatting.GREEN + "This is now " + player.getDisplayName().getFormattedText()+ "'s portal " + (index + 1)));
							ModCapabilities.get(player).setPortalCoords((byte) index, new PortalCoords((byte) index, pos.getX(), pos.getY(), pos.getZ(), player.dimension.getId()));
							//TODO sync with the client
							//System.out.println(index + " " + player.getCapability(ModCapabilities.ORGANIZATION_XIII, null).getPortalCoords(index).getDimID());
							//PacketDispatcher.sendTo(new SyncOrgXIIIData(player.getCapability(ModCapabilities.ORGANIZATION_XIII, null)), (EntityPlayerMP) player);
						} else {
							player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"));
						}
						return ActionResultType.SUCCESS;

					} else if (te.getOwner().equals(player.getDisplayName().getFormattedText())) {
						player.sendMessage(new TranslationTextComponent(TextFormatting.YELLOW + "This is your portal " + index));
					} else {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "This portal belongs to " + worldIn.getPlayerByUuid(te.getOwner()).getDisplayName().getFormattedText()));
						return ActionResultType.FAIL;
					}

				}
			//}
		}
		return ActionResultType.FAIL;
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
						PortalCoords coords = ModCapabilities.get(player).getPortalCoords(i);
						if (coords.getX() == pos.getX() && coords.getY() == pos.getY() && coords.getZ() == pos.getZ()) {
							index = i;
							break;
						}
					}
					System.out.println("R: " + index);
					if (index != -1) {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "Portal destination disappeared"));
						ModCapabilities.get(player).setPortalCoords((byte) index, new PortalCoords((byte) index, 0, 0, 0, 0));
					} else {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"));
					}

					//PacketDispatcher.sendTo(new SyncOrgXIIIData(player.getCapability(ModCapabilities.ORGANIZATION_XIII, null)), (EntityPlayerMP) player);
				}
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
}
