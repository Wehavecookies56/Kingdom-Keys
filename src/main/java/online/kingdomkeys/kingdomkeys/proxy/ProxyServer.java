package online.kingdomkeys.kingdomkeys.proxy;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import online.kingdomkeys.kingdomkeys.worldgen.JigsawJank;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ProxyServer implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //Nothing to do here yet


    }

	@Override
	public PlayerEntity getClientPlayer() {
		return null;
	}
}
