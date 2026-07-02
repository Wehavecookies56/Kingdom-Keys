package online.kingdomkeys.kingdomkeys.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @ModifyArg(method = "calculateBlockTint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ColorResolver;getColor(Lnet/minecraft/world/level/biome/Biome;DD)I"), index = 0)
    private Biome changeBiomeColours(Biome biome, @Local(argsOnly = true) BlockPos pos) {
        //not using the "this" cast trick since we can just get the level this way and not deal with the warnings
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            if (CastleOblivionHandler.isInterior(level.dimension())) {
                return CastleOblivionData.InteriorData.getClient(level).map(interiorData -> {
                    Floor currentFloor = interiorData.getFloorAtPos(pos);
                    if (currentFloor != null) {
                        return currentFloor.getType().getFloorColour().value();
                    } else {
                        return biome;
                    }
                }).orElse(biome);
            }
        }
        return biome;
    }
}
