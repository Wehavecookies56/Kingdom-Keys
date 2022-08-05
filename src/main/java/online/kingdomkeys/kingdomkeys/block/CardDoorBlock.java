package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;

import javax.annotation.Nullable;

public class CardDoorBlock extends BaseBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    //If block was placed by player: false
    public static final BooleanProperty GENERATED = BooleanProperty.create("generated");
    //false = room; true = world
    public static final BooleanProperty TYPE = BooleanProperty.create("type");

    public CardDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(GENERATED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(GENERATED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(GENERATED);
        builder.add(TYPE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (state.getValue(GENERATED)) {
                if (state.getValue(TYPE)) {
                    //world card selection gui
                    //set world for floor
                    //create first room from lobby
                    //transport into room
                } else {
                    //open room synthesis gui
                    //create room
                    //transport to room
                    CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
                    if (cap != null) {
                        CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(pos);
                        if (te != null) {
                            if (player.getMainHandItem().getItem() instanceof MapCardItem card) {
                                RoomType type = card.getRoomType();
                                Room currentRoom = cap.getRoomAtPos(pos);
                                RoomData data = te.getParentRoom().getParentFloor(level).getAdjacentRoom(te.getParentRoom(), RoomUtils.Direction.opposite(te.getDirection())).getFirst();
                                //generate
                                RoomGenerator.INSTANCE.generateRoom(data, type, player, currentRoom, RoomUtils.Direction.opposite(te.getDirection()), false);
                                //set door here destination to new door
                                //set new door destination to door here
                                //transport
                            }
                        }
                    }
                }
            } else {
                //open card gui
                //set card?
                //transport to door with same card?
                //maybe just link 2 doors together somehow
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModEntities.TYPE_CARD_DOOR.get().create(pPos, pState);
    }
}
