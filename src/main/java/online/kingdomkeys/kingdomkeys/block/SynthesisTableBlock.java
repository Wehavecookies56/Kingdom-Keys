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
			//Minecraft.getInstance().displayGuiScreen(new GuiMenu_Synthesis());
		}
		return ActionResultType.SUCCESS;
	}
	
}
