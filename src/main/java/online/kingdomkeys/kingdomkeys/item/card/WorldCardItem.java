package online.kingdomkeys.kingdomkeys.item.card;

import java.util.function.Supplier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;

public class WorldCardItem extends Item {

    private final Supplier<FloorType> floorType;

    public WorldCardItem(Supplier<FloorType> floorType) {
        super(new Properties());
        this.floorType = floorType;
    }

    public FloorType getFloorType() {
        return floorType.get();
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
    	if(!pLevel.isClientSide) {
	        CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(pLevel);
	        if (cap != null) {
	            Room currentRoom = cap.getRoomAtPos(pLevel, pPlayer.blockPosition());
	            if (currentRoom == null) {
	                KingdomKeys.LOGGER.info("something is wrong player should be in the lobby room");
	            } else {
	                Floor floor = cap.getFloors().get(0);
	                if (floor != null) {
	                	//Clear the whole floor (typical mom quote)
	                	//Clear the whole path
	                	floor.setWorldCard(this);
                        RoomData data = floor.getRoom(new RoomUtils.RoomPos(0, 1));
                        Room newRoom = RoomGenerator.INSTANCE.generateRoom(data, ModRoomTypes.SLEEPING_DARKNESS.get(), pPlayer, currentRoom, RoomUtils.Direction.NORTH, false);
	                }
	            }
	        }
    	}
    	return super.use(pLevel, pPlayer, pUsedHand);
    }
}
