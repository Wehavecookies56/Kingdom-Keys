package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.worldmap.WorldMarkerEntity;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.world.worldmap.WorldMap;
import org.lwjgl.glfw.GLFW;

// "Land in Hollow Bastion" / "Maybe later...": the two buttons the Gummi Ship asks with before landing or taking off
public class GummiTravelConfirmScreen extends Screen {
	private static final int BUTTON_HEIGHT = 20, BOTTOM = 40, CONFIRM = 0, LATER = 1;

	private final ResourceLocation reaction;
	private final Runnable confirm;
	private final MenuButton[] buttons = new MenuButton[2];
	private int selected = CONFIRM;

	public GummiTravelConfirmScreen(ResourceLocation reaction, Runnable confirm) {
		super(Component.empty());
		this.reaction = reaction;
		this.confirm = confirm;
		play(ModSounds.menu_in.get());
	}

	private Component question() {
		Player player = Minecraft.getInstance().player;
		if (reaction.equals(ModReactionCommands.LAND.location())) {
			WorldMarkerEntity marker = player == null ? null : WorldMap.nearestMarker(player);
			String world = marker == null || marker.getWorld() == null ? "?" : ClientUtils.worldName(marker.getWorld().dimension().location());
			return Component.translatable("kingdomkeys.worldmap.confirm.land", world);
		}
		// Take off
		String world = player == null ? "?" : ClientUtils.worldName(player.level().dimension().location());
		return Component.translatable("kingdomkeys.worldmap.confirm.launch", world);
	}

	@Override
	protected void init() {
		int width = Math.min(260, this.width - 80);
		Component[] labels = {question(), Component.translatable("kingdomkeys.worldmap.confirm.later")};

		for (int i = 0; i < buttons.length; i++) {
			int option = i;
			int y = height - BOTTOM - (buttons.length - i) * BUTTON_HEIGHT;
			buttons[i] = addRenderableWidget(new MenuButton((this.width - width) / 2, y, width - 22, "", MenuButton.ButtonType.ROUNDBUTTON, b -> choose(option)).setCenterText());
			buttons[i].setMessage(labels[i]);
			buttons[i].setSelected(i == selected);
		}
	}

	@Override
	public void tick() {
		// Flown out of reach, or the command went away some other way: nothing left to ask about
		if (minecraft.player == null || !PlayerData.get(minecraft.player).getReactionCommands().containsKey(reaction)) {
			onClose();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	// The world stays in view behind the question, unblurred
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		// The mouse picks whatever it rests on; with it elsewhere the keys keep their choice
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isMouseOver(mouseX, mouseY)) {
				select(i);
			}
		}

		super.render(guiGraphics, mouseX, mouseY, partialTick);

		// A button only draws the glove when hovered: chosen with the keys, it gets one all the same.
		// The buttons turn blending off once drawn, and the dot's glow needs it
		MenuButton chosen = buttons[selected];
		if (!chosen.isHovered()) {
			RenderSystem.enableBlend();
			ClientUtils.drawGloveAndDot(guiGraphics, chosen.getX(), chosen.getY(), chosen.getWidth());
			RenderSystem.disableBlend();
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		select(selected - (int) Math.signum(scrollY));
		return true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || InputHandler.Keybinds.BACK.getKeybind().matches(keyCode, scanCode)) {
			choose(LATER);
		} else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE
				|| InputHandler.Keybinds.ENTER.getKeybind().matches(keyCode, scanCode)) {
			buttons[selected].playDownSound(minecraft.getSoundManager());
			choose(selected);
		} else if (keyCode == GLFW.GLFW_KEY_UP || keyCode == GLFW.GLFW_KEY_W || InputHandler.Keybinds.SCROLL_UP.getKeybind().matches(keyCode, scanCode)) {
			select(selected - 1);
		} else if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_S || InputHandler.Keybinds.SCROLL_DOWN.getKeybind().matches(keyCode, scanCode)) {
			select(selected + 1);
		} else {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
		return true;
	}

	private void select(int option) {
		option = Mth.clamp(option, 0, buttons.length - 1);
		if (option == selected) {
			return;
		}
		selected = option;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setSelected(i == selected);
		}
		play(ModSounds.menu_move.get());
	}

	private void choose(int option) {
		onClose();
		if (option == CONFIRM) {
			confirm.run();
		} else {
			play(ModSounds.menu_back.get());
		}
	}

	private static void play(SoundEvent sound) {
		Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, 1F));
	}
}
