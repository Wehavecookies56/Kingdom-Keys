package online.kingdomkeys.kingdomkeys.integration.curios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class CuriosUtils {
	public static List<ItemStack> find(Player player, Item item) {
		return CuriosApi.getCuriosInventory(player).map(curios -> curios.findCurios(item).stream().map(SlotResult::stack).toList()).orElse(List.of());
	}
}
