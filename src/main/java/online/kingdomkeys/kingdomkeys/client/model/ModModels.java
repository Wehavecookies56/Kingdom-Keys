package online.kingdomkeys.kingdomkeys.client.model;


import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.client.render.RenderValor;

public class ModModels {

	public static RenderValor renderValor;
	/*public static RenderRayman renderRayman;	
	public static RenderGlobox renderGlobox;
	public static RenderHoodlum renderHoodlum;*/
	
	public static void register() {
		renderValor = new RenderValor(Minecraft.getInstance().getRenderManager(), new ModelValor(1F), 1);
		/*renderRayman = new RenderRayman(Minecraft.getInstance().getRenderManager(), new ModelRayman(1), 1);
		renderGlobox = new RenderGlobox(Minecraft.getInstance().getRenderManager(), new ModelGlobox(1), 1);
		renderHoodlum = new RenderHoodlum(Minecraft.getInstance().getRenderManager(), new ModelHoodlum(1), 1);*/
	}
}
