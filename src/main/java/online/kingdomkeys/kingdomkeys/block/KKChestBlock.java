package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.KKChestTileEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class KKChestBlock extends BaseBlock {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty BIG = BooleanProperty.create("big");

	private static final VoxelShape collisionShapeEW = Block.makeCuboidShape(2.0D, 0.0D, 1.0D, 14.0D, 12.0D, 15.0D);
	private static final VoxelShape collisionShapeNS = Block.makeCuboidShape(1.0D, 0.0D, 2.0D, 15.0D, 12.0D, 14.0D);

	public KKChestBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(BIG, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(BIG, false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, BIG);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return getShape(state, world, pos, context);
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		// Tried to make animation here but random tick f*cks it all
		super.animateTick(state, world, pos, random);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "fire");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "blizzard");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "water");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "thunder");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "cure");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "aero");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "magnet");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "reflect");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "gravity");
			props.addMagicToList(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "stop");
			
			props.setDriveFormLevel(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "valor", 1);
			props.setDriveFormLevel(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "wisdom", 1);
			props.setDriveFormLevel(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "limit", 1);
			props.setDriveFormLevel(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "master", 1);
			props.setDriveFormLevel(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "final", 1);
			
			//props.setMaxDP(900);
			
			PacketHandler.syncToAllAround(player, props);
			
//			setDefaultState(state.with(BIG, true));
			if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof KKChestTileEntity) {
				KKChestTileEntity te = (KKChestTileEntity) worldIn.getTileEntity(pos);
				if (te.getKeybladeName() == null || te.getKeybladeName().equals(null)) { // No lock, lock it
					te.setKeyblade(player.getHeldItemMainhand());
					te.markDirty();

					player.sendMessage(new TranslationTextComponent(TextFormatting.GREEN + "Set " + te.getKeybladeName() + " as the keyblade to unlock the chest"));

					// player.openGui(KingdomKeys.instance, GuiIDs.GUI_KKCHEST_INV, world,
					// pos.getX(), pos.getY(), pos.getZ());
					return ActionResultType.SUCCESS;
				} else if (te.getKeybladeName() != null && te.getKeybladeName().equals(player.getHeldItemMainhand().getItem().toString())) { // Locked but you have the right keyblade
					// player.openGui(KingdomKeys.instance, GuiIDs.GUI_KKCHEST_INV, world,
					// pos.getX(), pos.getY(), pos.getZ());
					return ActionResultType.SUCCESS;
				} else { // F
					player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "This chest is locked"));
					return ActionResultType.FAIL;
				}
			}
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
	}

	@Override
	public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModEntities.TYPE_KKCHEST.get().create();
	}

}
