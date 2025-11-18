package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GummiCockpitBlock extends GummiBlockBase {

    List<Vec3> seats;

    public GummiCockpitBlock(GummiBlockProperties gummiProperties, List<Vec3> seats) {
        super(gummiProperties);
        this.seats = seats;
    }

    public List<Vec3> getSeats(){
        return seats;
    }

    public Vec3 getSeat(int i){
        return seats.get(i);
    }

    public int getMaxSeats(){
        return seats.size();
    }

}
