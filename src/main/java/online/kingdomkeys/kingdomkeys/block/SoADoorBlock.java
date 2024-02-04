package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.struggle.MenuStruggle;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;

public class SoADoorBlock extends BaseBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

	private static final VoxelShape collisionShapeEW = Block.box(5.0D, 0.0D, -8.0D, 11.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeNS = Block.box(-8.0D, 0.0D, 5.0D, 24.0D, 32.0D, 11.0D);

	public SoADoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}
	
	@Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
	
	@Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        //builder.add(FACING, BIG);
        builder.add(FACING);
    }

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getShape(state,world,pos,context);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		// Tried to make animation here but random tick f*cks it all
		super.animateTick(state, world, pos, random);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		
		IWorldCapabilities worldData = ModCapabilities.getWorld(worldIn);
		
		if(!worldIn.isClientSide) {
			if(worldData != null) {
				System.out.println(worldData.getStruggles().size());
				if(worldData.getStruggles().isEmpty()) {
					Struggle struggle = new Struggle(pos, "Struggle", player.getUUID(), player.getDisplayName().getString(), false, (byte)8);
					worldData.addStruggle(struggle);
					PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
				} else {
					worldData.removeStruggle(worldData.getStruggles().get(0));
				}
			}
		} else {
			Minecraft.getInstance().setScreen(new MenuStruggle(pos));
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}
	
	@Deprecated
	   public RenderShape getRenderShape(BlockState state) {
	      return RenderShape.MODEL;
	}
	
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    	if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
    }
}
