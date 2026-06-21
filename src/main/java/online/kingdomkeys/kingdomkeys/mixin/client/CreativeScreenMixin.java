package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.ItemButton;
import online.kingdomkeys.kingdomkeys.creativetab.CreativeFilter;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
	@Unique
	private final List<Button> kkButtons = new ArrayList<>();

	public CreativeScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, net.minecraft.world.entity.player.Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void initButtons(CallbackInfo ci) {
		int spacing = (int)(21 * ItemButton.SCALE);
		int y = topPos + 4;

		int x = leftPos + imageWidth - 128;

		addCategoryButton(x += spacing, y, new ItemStack(ModItems.kingdomKey.get()),  ICreativeTab.Tab.KEYBLADES);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.kingdomKeyChain.get()), ICreativeTab.Tab.KEYCHAINS);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.eternalFlames.get()), ICreativeTab.Tab.ORGANIZATION);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.terra_Chestplate.get()), ICreativeTab.Tab.ARMORS);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.abilityRing.get()), ICreativeTab.Tab.EQUIPABLES);
		addCategoryButton(x += spacing, y, new ItemStack(ModBlocks.gummiHangar.get()), ICreativeTab.Tab.GUMMI);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.blazing_crystal.get()), ICreativeTab.Tab.MATS);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.tranquilDarkness.get()), ICreativeTab.Tab.CARDS);
		addCategoryButton(x += spacing, y, new ItemStack(ModItems.iceCream.get()), ICreativeTab.Tab.MISC);
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void updateButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		boolean show = CreativeModeInventoryScreen.selectedTab == KingdomKeys.kingdomKeysTab.get();
		kkButtons.forEach(b -> {
			b.visible = show;
			b.active = show;
		});
	}

	private void addCategoryButton(int x, int y, ItemStack icon, ICreativeTab.Tab tab) {
		kkButtons.add(this.addRenderableWidget(new ItemButton(x, y, tab.name(), icon, () -> CreativeFilter.currentCategory == tab, b -> {
			if (CreativeFilter.currentCategory == tab) {
				CreativeFilter.currentCategory = null;
			} else {
				CreativeFilter.currentCategory = tab;
			}
			CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) Minecraft.getInstance().screen;

			KingdomKeys.kingdomKeysTab.get().buildContents(
					new CreativeModeTab.ItemDisplayParameters(
							Minecraft.getInstance().player.connection.enabledFeatures(),
							Minecraft.getInstance().options.operatorItemsTab().get(),
							Minecraft.getInstance().level.registryAccess()
					)
			);

			((CreativeModeInventoryScreenInvoker) screen).kk$selectTab(KingdomKeys.kingdomKeysTab.get());
		})));
	}
}