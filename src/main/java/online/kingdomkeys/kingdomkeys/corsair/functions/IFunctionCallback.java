package online.kingdomkeys.kingdomkeys.corsair.functions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.corsair.lib.KeyEnum;

@FunctionalInterface
public abstract interface IFunctionCallback {
	public abstract int[] invoke(KeyEnum keyEnum, Minecraft minecraft, World world, EntityPlayerSP entityPlayerSP);
}