package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.organization.ArrowgunItem;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//TODO cleanup
public class CommandMenuGui extends OverlayBase {

	public static final CommandMenuGui INSTANCE = new CommandMenuGui();
	public static final int TOP = 5, ATTACK = 4, MAGIC = 3, ITEMS = 2, DRIVE = 1;

	public static Map<ResourceLocation, CommandMenuSubMenu> commandMenuElements;

	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;

	public static int reactionSelected = 0;

	public final ResourceLocation
			root = new ResourceLocation(KingdomKeys.MODID, "root"),
			attack = new ResourceLocation(KingdomKeys.MODID, "attack"),
			magic = new ResourceLocation(KingdomKeys.MODID, "magic"),
			items = new ResourceLocation(KingdomKeys.MODID, "items"),
			drive = new ResourceLocation(KingdomKeys.MODID, "drive"),
			portals = new ResourceLocation(KingdomKeys.MODID, "portals"),
			target = new ResourceLocation(KingdomKeys.MODID, "limit"),
			limit = new ResourceLocation(KingdomKeys.MODID, "target"),
			revert = new ResourceLocation(KingdomKeys.MODID, "revert");

	public ResourceLocation currentSubmenu;

	public void initCommandMenu() {
		currentSubmenu = root;
		commandMenuElements = new HashMap<>();
		CommandMenuSubMenu rootSubmenu = new CommandMenuSubMenu.Builder(root, Component.translatable(Strings.Gui_CommandMenu_Command))
				.position(ModConfigs.cmXPos, Minecraft.getInstance().getWindow().getGuiScaledHeight())
				.openByDefault()
				.changesColour()
				.colour(new Color(10, 51, 255))
				.onUpdate((subMenu, guiGraphics) -> subMenu.updatePosition(ModConfigs.cmXPos, Minecraft.getInstance().getWindow().getGuiScaledHeight()))
				.withChildren(
						new CommandMenuItem.Builder(attack, Component.translatable(Strings.Gui_CommandMenu_Attack), null).onUpdate((item, guiGraphics) -> updateRootItem(item, null, guiGraphics)).iconUV(170, 18),
						new CommandMenuItem.Builder(portals, Component.translatable(Strings.Gui_CommandMenu_Portal), opensSubmenu(portals, true)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, portals, guiGraphics)).iconUV(180, 18),
						new CommandMenuItem.Builder(magic, Component.translatable(Strings.Gui_CommandMenu_Magic), opensSubmenu(magic, false)).onUpdate((item, guiGraphics) -> updateRootItem(item, magic, guiGraphics)).iconUV(160, 18),
						new CommandMenuItem.Builder(items, Component.translatable(Strings.Gui_CommandMenu_Items), opensSubmenu(items, true)).onUpdate((item, guiGraphics) -> updateRootItem(item, items, guiGraphics)).iconUV(150, 18),
						new CommandMenuItem.Builder(drive, Component.translatable(Strings.Gui_CommandMenu_Drive), opensSubmenu(drive, false)).onUpdate((item, guiGraphics) -> updateRootItem(item, drive, guiGraphics)).iconUV(140, 18),
						new CommandMenuItem.Builder(revert, Component.translatable(Strings.Gui_CommandMenu_Drive_Revert), item -> {
							IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
							if (playerData.getActiveDriveForm().equals(Strings.Form_Anti) && !playerData.isAbilityEquipped(Strings.darkDomination) && EntityEvents.isHostiles) {
								playErrorSound();
							} else {
								PacketHandler.sendToServer(new CSUseDriveFormPacket(DriveForm.NONE.toString()));
								playSound(ModSounds.unsummon.get());
							}
						}).invisibleByDefault().onUpdate((item, guiGraphics) -> {
							if (item.isVisible()) {
								if (ModCapabilities.getPlayer(minecraft.player).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
									item.setVisible(false);
									item.getParent().getChild(drive).setVisible(true);
								}
							}
						}).iconUV(140, 18),
						new CommandMenuItem.Builder(limit, Component.translatable(Strings.Gui_CommandMenu_Limit), opensSubmenu(limit, false)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, limit, guiGraphics)).iconUV(140, 18)
				)
				.build();
		CommandMenuSubMenu magicSubmenu = new CommandMenuSubMenu.Builder(magic, Component.translatable(Strings.Gui_CommandMenu_Magic_Title))
				.colour(new Color(102, 0, 255))
				.onUpdate(updateMagic())
				.withChildren(createMagicFromRegistry())
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu itemsSubmenu = new CommandMenuSubMenu.Builder(items, Component.translatable(Strings.Gui_CommandMenu_Items_Title))
				.colour(new Color(77, 255, 77))
				.onOpen(this::createItems)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu targetSubmenu =  new CommandMenuSubMenu.Builder(target, Component.translatable(Strings.Gui_CommandMenu_Target))
				.colour(new Color(10, 51, 255))
				.onOpen(this::createTargets)
				.autoResizes()
				.build();
		CommandMenuSubMenu portalsSubmenu = new CommandMenuSubMenu.Builder(portals, Component.translatable(Strings.Gui_CommandMenu_Portals_Title))
				.colour(new Color(204, 204, 204))
				.onOpen(this::createPortals)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu limitSubmenu = new CommandMenuSubMenu.Builder(limit, Component.translatable(Strings.Gui_CommandMenu_Limit_Title))
				.colour(new Color(255, 255, 0))
				.onUpdate(updateLimits())
				.withChildren(createLimitsFromRegistry())
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu driveSubmenu = new CommandMenuSubMenu.Builder(drive, Component.translatable(Strings.Gui_CommandMenu_Drive_Title))
				.colour(new Color(0, 255, 255))
				.onUpdate(updateDriveForms())
				.withChildren(createDriveFormsFromRegistry())
				.autoResizes()
				.buildWithParent(rootSubmenu);
	}

	private CommandMenuGui() {
		super();

	}

	private CommandMenuItem.OnEnter opensSubmenu(ResourceLocation subMenu, boolean ignoreMemory) {
		return (item -> {
			changeSubmenu(subMenu, ignoreMemory || !ModConfigs.cmCursorMemory);
			playInSound();
		});
	}

	public CommandMenuSubMenu.OnUpdate updateMagic() {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<String, Integer> magicList = new HashMap<>();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			ModConfigs.magicDisplayedInCommandMenu.stream().filter(m -> playerData.getMagicsMap().containsKey(m)).toList().forEach(s -> {
				magicList.put(s, i.getAndIncrement());
			});
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (magicList.containsKey(item.getId().toString())) {
					item.setSorting(magicList.get(item.getId().toString()));
					item.setMessage(Component.translatable(ModMagic.registry.get().getValue(item.getId()).getTranslationKey(playerData.getMagicLevel(item.getId()))));
					item.setVisible(true);
				} else {
					item.setVisible(false);
				}
			});
		};
	}

	public CommandMenuSubMenu.OnUpdate updateLimits() {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<String, Integer> limits = new HashMap<>();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			Utils.getSortedLimits(Utils.getPlayerLimitAttacks(minecraft.player)).forEach(limit -> {
				limits.put(limit.getRegistryName().toString(), i.getAndIncrement());
			});
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (limits.containsKey(item.getId().toString())) {
					item.setSorting(limits.get(item.getId().toString()));
					item.setVisible(true);
				} else {
					item.setVisible(false);
				}
			});
		};
	}

	public CommandMenuSubMenu.OnUpdate updateDriveForms() {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<String, Integer> formList = new HashMap<>();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			playerData.getDriveFormMap().forEach((s, ints) -> {
				formList.put(s, ModDriveForms.registry.get().getValue(new ResourceLocation(s)).getOrder());
			});
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (formList.containsKey(item.getId().toString())) {
					item.setSorting(formList.get(item.getId().toString()));
					item.setVisible(true);
				} else {
					item.setVisible(false);
				}
			});
		};
	}

	public CommandMenuItem.Builder[] createMagicFromRegistry() {
		List<CommandMenuItem.Builder> magic = new ArrayList<>();
		ModMagic.registry.get().forEach(magicRegistryObject -> magic.add(new CommandMenuItem.Builder(magicRegistryObject.getRegistryName(), Component.translatable(magicRegistryObject.getTranslationKey()), item -> {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
			int[] mag = playerData.getMagicsMap().get(magicRegistryObject.getRegistryName().toString());
			double cost = magicRegistryObject.getCost(mag[0], minecraft.player);

			if (playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
				playErrorSound();
				changeSubmenu(root, true);
			} else {
				if (worldData.getPartyFromMember(minecraft.player.getUUID()) != null && ModMagic.registry.get().getValue(magicRegistryObject.getRegistryName()).getHasToSelect()) { //Open party target selector
					if (currentSubmenu.equals(target) && commandMenuElements.get(currentSubmenu).getSelected() != null) {
						String target = commandMenuElements.get(currentSubmenu).getSelected().getId().getPath();
						int level = playerData.getMagicLevel(magicRegistryObject.getRegistryName());
						PacketHandler.sendToServer(new CSUseMagicPacket(magicRegistryObject.getRegistryName().toString(), target, level));
						changeSubmenu(root, true);
					} else {
						changeSubmenu(target, true);
						playInSound();
						return;
					}
				} else { //Cast Magic
					int level = playerData.getMagicLevel(magicRegistryObject.getRegistryName());
					PacketHandler.sendToServer(new CSUseMagicPacket(magicRegistryObject.getRegistryName().toString(), level, InputHandler.lockOn));
					changeSubmenu(root, true);
				}
				playSelectSound();
			}
		})
				.onUpdate((item, guiGraphics) -> {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
					Magic magicInst = ModMagic.registry.get().getValue(item.getId());
					if (playerData.getMP() > 0 && !playerData.getRecharge()) {
						item.setActive(true);
						item.setTextColour(Color.WHITE);
						if (playerData.getMP() <= magicInst.getCost(playerData.getMagicLevel(item.getId()), Minecraft.getInstance().player)) {
							item.setTextColour(Color.ORANGE);
						}
					} else {
						item.setTextColour(Color.WHITE);
						item.setActive(false);
					}
				}).iconUV(160, 18)));
		return magic.toArray(new CommandMenuItem.Builder[0]);
	}

	public CommandMenuItem.Builder[] createDriveFormsFromRegistry() {
		List<CommandMenuItem.Builder> forms = new ArrayList<>();
		ModDriveForms.registry.get().getEntries().stream().filter(driveFormRegistryObject -> driveFormRegistryObject.getValue().displayInCommandMenu(minecraft.player)).forEach(driveFormEntry -> {
			DriveForm driveFormRegistryObject = driveFormEntry.getValue();
			forms.add(new CommandMenuItem.Builder(driveFormRegistryObject.getRegistryName(), Component.translatable(driveFormRegistryObject.getTranslationKey()), item -> {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
				if (playerData.getDP() >= driveFormRegistryObject.getDriveCost()) {
					if (!antiFormCheck(playerData, driveFormRegistryObject)) {
						PacketHandler.sendToServer(new CSUseDriveFormPacket(driveFormRegistryObject.getRegistryName().toString()));
					}

					changeSubmenu(root, true);
					playInSound();
				} else {
					playErrorSound();
				}
			}).onUpdate((item, guiGraphics) -> {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
				DriveForm form = ModDriveForms.registry.get().getValue(item.getId());
				if (playerData.getDP() >= form.getDriveCost()) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}).iconUV(140, 18));
		});
		return forms.toArray(new CommandMenuItem.Builder[0]);
	}

	public CommandMenuItem.Builder[] createLimitsFromRegistry() {
		List<CommandMenuItem.Builder> limits = new ArrayList<>();
		ModLimits.registry.get().forEach(limitRegistryObject -> limits.add(new CommandMenuItem.Builder(limitRegistryObject.getRegistryName(), Component.translatable(limitRegistryObject.getTranslationKey()), item -> {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			if (playerData.getDP() < limitRegistryObject.getCost()) {
				playErrorSound();
			} else {
				if (InputHandler.lockOn != null)
					PacketHandler.sendToServer(new CSUseLimitPacket(InputHandler.lockOn, limitRegistryObject.getRegistryName()));
				else
					PacketHandler.sendToServer(new CSUseLimitPacket(limitRegistryObject.getRegistryName()));
				changeSubmenu(root, true);
				playInSound();
			}
		}).onUpdate((item, guiGraphics) -> {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			Limit limit = ModLimits.registry.get().getValue(item.getId());
			item.setMessage(Component.literal(Component.translatable(limit.getTranslationKey()).getString() + ":  "));
			if (playerData.getDP() >= limit.getCost()) {
				item.setActive(true);
			} else {
				item.setActive(false);
			}
			if (item.getParent().isVisible()) {
				drawString(guiGraphics, font, String.valueOf(ModLimits.registry.get().getValue(item.getId()).getCost() / 100), item.getX() + font.width(item.getMessage().getString()), item.getY() + 4, item.isActive() ? new Color(0, 255, 255).getRGB() : new Color(0, 255, 255).darker().darker().getRGB());
			}
		}).iconUV(140, 18)));
		return limits.toArray(new CommandMenuItem.Builder[0]);
	}

	public boolean isOrgMode() {
		return ModCapabilities.getPlayer(minecraft.player).getAlignment() != OrgMember.NONE;
	}

	public void updateRootItem(CommandMenuItem item, ResourceLocation submenu, GuiGraphics guiGraphics) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (item.getId().equals(portals) && isOrgMode() && item.getParent().getSelected().equals(item)) {
			if (minecraft.player.getMainHandItem() != null && minecraft.player.getMainHandItem().getItem() instanceof ArrowgunItem) {
				ItemStack weapon = minecraft.player.getMainHandItem();
				if (weapon.hasTag() && weapon.getTag().contains("ammo")) {
					int ammo = weapon.getTag().getInt("ammo");
					drawString(guiGraphics, minecraft.font, ammo + "", item.getX() + 8 + (int) (item.getParent().getWidth() * (ModConfigs.cmXScale / 100D)), item.getY() + 4, 0xFFFFFF);
				}
			}
		}
		if (item.getId().equals(attack) || item.getId().equals(drive)) {
			item.setVisible(!isOrgMode());
		} else if (item.getId().equals(portals) || item.getId().equals(limit)) {
			item.setVisible(isOrgMode());
		}
		if (submenu == null) {
			item.setActive(true);
			return;
		}
		if (item.getId().equals(drive) && !playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
			item.setVisible(false);
			item.getParent().getChild(revert).setVisible(true);
		}
		if (commandMenuElements.containsKey(submenu)) {
			if (submenu.equals(items)) {
				item.setActive(false);
				playerData.getEquippedItems().forEach((integer, stack) -> {
					if (!stack.isEmpty()) {
						item.setActive(true);
					}
				});
				return;
			} else if (submenu.equals(portals)) {
				item.setActive(!ModCapabilities.getWorld(minecraft.level).getAllPortalsFromOwnerID(minecraft.player.getUUID()).isEmpty());
				return;
			} else if (!commandMenuElements.get(submenu).getVisibleChildren().isEmpty()) {
				item.setActive(true);
				return;
			}
		}
		item.setActive(false);
	}

	public void createTargets(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
		if (worldData.getPartyFromMember(minecraft.player.getUUID()) != null) {
			List<Party.Member> targets = worldData.getPartyFromMember(minecraft.player.getUUID()).getMembers();
			targets.forEach(member -> {
				subMenu.addChild(new CommandMenuItem.Builder(
						new ResourceLocation(KingdomKeys.MODID, member.getUsername().toLowerCase()),
						Component.translatable(member.getUsername()),
						item -> subMenu.getParent().getSelected().onEnter()
				).build(subMenu));
			});
		}
	}

	public void createPortals(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
		worldData.getAllPortalsFromOwnerID(minecraft.player.getUUID()).forEach(uuid -> {
			PortalData portalData = worldData.getPortalFromUUID(uuid);
			String rlUUID = uuid.toString().replaceAll("-", "_");
			subMenu.addChild(new CommandMenuItem.Builder(new ResourceLocation(rlUUID), Component.translatable(portalData.getName()), item -> {
				PortalData portal = worldData.getPortalFromUUID(UUID.fromString(item.getId().getPath().replaceAll("_", "-")));
				if (!portal.getPos().equals(new BlockPos(0,0,0))) { //If the portal is not default coords
					summonPortal(portal);
				} else {
					minecraft.player.sendSystemMessage(Component.translatable(ChatFormatting.RED + "You don't have any portal destinations"));
				}
				changeSubmenu(root, true);
				playInSound();
			}).iconUV(180, 18).build(subMenu));
		});
	}

	public void summonPortal(PortalData coords) {
		BlockPos destination = coords.getPos();

		if (minecraft.player.isShiftKeyDown()) {
			PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(minecraft.player.blockPosition(), destination, coords.getDimID()));
		} else {
			HitResult rtr = InputHandler.getMouseOverExtended(100);
			if (rtr != null) {
				if(rtr instanceof BlockHitResult) {
					BlockHitResult brtr = (BlockHitResult)rtr;
					double distanceSq = minecraft.player.distanceToSqr(brtr.getBlockPos().getX(), brtr.getBlockPos().getY(), brtr.getBlockPos().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(brtr.getBlockPos().above(), destination, coords.getDimID()));
					}
				} else if(rtr instanceof EntityHitResult) {
					EntityHitResult ertr = (EntityHitResult)rtr;
					double distanceSq = minecraft.player.distanceToSqr(ertr.getEntity().getX(), ertr.getEntity().getY(), ertr.getEntity().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(ertr.getEntity().blockPosition(), destination, coords.getDimID()));
					}
				}
			}
		}
	}

	public void createItems(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
		playerData.getEquippedItems().forEach((integer, stack) -> {
				if (!stack.isEmpty()) {
					subMenu.addChild(
					new CommandMenuItem.Builder(
							new ResourceLocation(KingdomKeys.MODID, integer.toString()),
							Component.literal(stack.getDisplayName().getString().substring(1, stack.getDisplayName().getString().length()-1)),
							item -> {
								if(stack.getItem() instanceof KKPotionItem) {
									KKPotionItem potion = (KKPotionItem) stack.getItem();
									//potion.potionEffect(player);
									Party party = worldData.getPartyFromMember(minecraft.player.getUUID());

									if(potion.isGlobal() || party == null) {
										PacketHandler.sendToServer(new CSUseItemPacket(integer));
									} else {
										//Target selector
										if (currentSubmenu.equals(target) && commandMenuElements.get(currentSubmenu).getSelected() != null) {
											String target = commandMenuElements.get(currentSubmenu).getSelected().getId().getPath();
											PacketHandler.sendToServer(new CSUseItemPacket(integer, target));
										} else {
											changeSubmenu(target, true);
											playInSound();
											return;
										}
									}
									changeSubmenu(root, true);
									playSelectSound();
								} else {
									playErrorSound();
								}
							}).iconUV(150, 18)
							.build(subMenu));
		}});
	}

	public void playSelectSound() {
		playSound(ModSounds.menu_select.get());
	}

	public void playMoveSound() {
		playSound(ModSounds.menu_move.get());
	}

	public void playBackSound() {
		playSound(ModSounds.menu_back.get());
	}

	public void playInSound() {
		playSound(ModSounds.menu_in.get());
	}

	public void playErrorSound() {
		playSound(ModSounds.error.get());
	}

	public void playSound(SoundEvent sound) {
		Player player = Minecraft.getInstance().player;
		Minecraft.getInstance().level.playSound(player, player.position().x(),player.position().y(),player.position().z(), sound, SoundSource.MASTER, 1.0f, 1.0f);
	}

	public void changeSubmenu(ResourceLocation submenu, boolean resetSelected) {
		commandMenuElements.forEach((resourceLocation, subMenu) -> {
			subMenu.setActive(false);
		});
		CommandMenuSubMenu newSubmenu = commandMenuElements.get(submenu);
		CommandMenuSubMenu currentSubMenu = commandMenuElements.get(currentSubmenu);
		if (submenu.equals(root)) {
			commandMenuElements.forEach((resourceLocation, subMenu) -> {
				if (!resourceLocation.equals(root)) {
					subMenu.setVisible(false);
				}
			});
		}
		if (submenu.equals(target)) {
			commandMenuElements.get(submenu).setParent(commandMenuElements.get(currentSubmenu));
		}
		if (currentSubMenu.getParent() != null) {
			if (currentSubMenu.getParent().getId().equals(submenu)) {
				currentSubMenu.setVisible(false);
			}
		}
		if (newSubmenu != null) {
			newSubmenu.close();
			newSubmenu.setActive(true);
			newSubmenu.onOpen();
			if (newSubmenu.visibleSize() > 0) {
				if (!currentSubmenu.equals(root)) {
					newSubmenu.setVisible(false);
				}
				if (resetSelected) {
					newSubmenu.setSelected(newSubmenu.getFirst());
				}
				newSubmenu.setVisible(true);
				currentSubmenu = submenu;
			} else {
				newSubmenu.setActive(false);
			}
		}
	}

	public boolean antiFormCheck(IPlayerCapabilities playerData, DriveForm driveForm) { //Only checks if form is not final
		if(!driveForm.canGoAnti()) {
			return false;
		}
		if(playerData.isAbilityEquipped(Strings.darkDomination)) {
			return false;
		}

		if(playerData.isAbilityEquipped(Strings.lightAndDarkness)) { // Will always be true
			PacketHandler.sendToServer(new CSSummonKeyblade(true));
			PacketHandler.sendToServer(new CSUseDriveFormPacket(Strings.Form_Anti));
			playSound(ModSounds.antidrive.get());

			changeSubmenu(root, true);
			playSelectSound();
			return true;
		}

		double random = Math.random();
		int ap = playerData.getAntiPoints();

		int prob = 0;
		if (ap > 0 && ap <= 4)
			prob = 0;
		else if (ap > 4 && ap <= 9)
			prob = 10;
		else if (ap >= 10)
			prob = 25;

		if (random * 100 < prob) {
			PacketHandler.sendToServer(new CSUseDriveFormPacket(Strings.Form_Anti));
			playSound(ModSounds.antidrive.get());

			changeSubmenu(root, true);
			playSelectSound();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		//textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

		if (minecraft.player != null && ModCapabilities.getPlayer(minecraft.player) != null) {
			drawReactionCommands(guiGraphics, width, height);

			List<CommandMenuSubMenu> submenus = commandMenuElements.values().stream().sorted(Comparator.comparingInt(CommandMenuSubMenu::getZ)).toList();
			submenus.forEach(submenu -> {
				submenu.render(guiGraphics, width, height, partialTick);
				submenu.onUpdate(guiGraphics);
			});
		}

	}

	public static void down() {
		INSTANCE.playMoveSound();
		commandMenuElements.get(INSTANCE.currentSubmenu).next();
	}
	public static void up() {
		INSTANCE.playMoveSound();
		commandMenuElements.get(INSTANCE.currentSubmenu).prev();
	}

	public static void enter() {
		commandMenuElements.get(INSTANCE.currentSubmenu).getSelected().onEnter();
	}

	public static void cancel() {
		commandMenuElements.get(INSTANCE.currentSubmenu).getSelected().onCancel();
	}

	public void drawReactionCommands(GuiGraphics gui, int width, int height) {
		float alpha = 1F;
		float scale = 1.05f;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		List<String> list = playerData.getReactionCommands();

		for(int i = 0; i < list.size(); i++) {
			gui.pose().pushPose();
			{
				float shade = i == reactionSelected ? 1F : 0.4F;
				RenderSystem.setShaderColor(shade,shade,shade, alpha);
				gui.pose().translate(0, commandMenuElements.get(currentSubmenu).getY()-20, 0.5F);
				gui.pose().scale(scale, scale, scale);
				gui.pose().pushPose();
				{
					ReactionCommand command = ModReactionCommands.registry.get().getValue(new ResourceLocation(list.get(i)));
					drawString(gui, minecraft.font, Utils.translateToLocal(command.getTranslationKey()), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);

					gui.pose().scale(ModConfigs.cmXScale / 75F, 1, 1);
					blit(gui, commandMenuElements.get(currentSubmenu).getTexture(), 0, 0, 0, 15, TOP_WIDTH, TOP_HEIGHT);
				}
				gui.pose().popPose();

			}
			gui.pose().popPose();
		}
	}
}
