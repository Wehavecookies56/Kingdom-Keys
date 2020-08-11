package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.synthesis.GuiMenu_Synthesis;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class SynthesisTableBlock extends BaseBlock {

	public SynthesisTableBlock(Properties properties) {
		super(properties);
	}


	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote) {
			Minecraft.getInstance().displayGuiScreen(new GuiMenu_Synthesis()); 
			return ActionResultType.SUCCESS;
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
						PortalData coords = ModCapabilities.getPlayer(player).getPortalCoords(i);
						if (coords.getX() == pos.getX() && coords.getY() == pos.getY() && coords.getZ() == pos.getZ()) {
							index = i;
							break;
						}
					}
					System.out.println("R: " + index);
					if (index != -1) {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "Portal destination disappeared"));
						ModCapabilities.getPlayer(player).setPortalCoords((byte) index, new PortalData((byte) index, 0, 0, 0, 0));
					} else {
						player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You have no empty slots for portals"));
					}

					PacketHandler.syncToAllAround(player, ModCapabilities.getPlayer(player));
				}
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
}
