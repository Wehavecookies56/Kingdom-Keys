package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
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
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.client.TargetSelectorEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.MagicSpellItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.organization.ArrowgunItem;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandMenuGui extends OverlayBase {
	public static final CommandMenuGui INSTANCE = new CommandMenuGui();
	public static final int NONE = 0;
	public static Map<ResourceLocation, CommandMenuSubMenu> commandMenuElements;
	public static int reactionSelected = 0;
	public static ResourceLocation lastUsedMagic;
	public final ResourceLocation root, attack, magic, items, drive, portals, target, limit, revert;
	public ResourceLocation currentSubmenu;
	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;
	int MENU_WIDTH = 71;
	int MENU_HEIGHT = 15;
	int iconWidth = 10;
	int textX = 0;

	private CommandMenuGui() {
		super();
		root = KingdomKeys.rl("root");
		attack = KingdomKeys.rl("attack");
		magic = KingdomKeys.rl("magic");
		items = KingdomKeys.rl("items");
		drive = KingdomKeys.rl("drive");
		portals = KingdomKeys.rl("portals");
		limit = KingdomKeys.rl("limit");
		target = KingdomKeys.rl("target");
		revert = KingdomKeys.rl("revert");
		currentSubmenu = root;
		commandMenuElements = new HashMap<>();
		CommandMenuSubMenu rootSubmenu = new CommandMenuSubMenu.Builder(this.root, Component.translatable(Strings.Gui_CommandMenu_Command).withStyle(ClientUtils.KK_Font_EXP))
				.position(0, 0)
				.openByDefault()
				.changesColour()
				.fixedHeader()
				.colour(new Color(10, 51, 255))
				.onUpdate((subMenu, guiGraphics) -> {
					ClientUtils.CM_ELEMENT.height = subMenu.getHeight() * (subMenu.getVisibleChildren().size() + 1);
					ClientUtils.CM_ELEMENT.width = subMenu.getWidth();
					subMenu.setPosition(0, 0);
					subMenu.setWidth(74);
				})
				.withChildren(
						new CommandMenuItem.Builder(attack, Component.translatable(Strings.Gui_CommandMenu_Attack), opensSubmenu(attack)).onUpdate((item, guiGraphics) -> updateRootItem(item, attack, guiGraphics)).iconUV(30, 60).onCancel(item -> onCancel(item, null, guiGraphics)),
						new CommandMenuItem.Builder(portals, Component.translatable(Strings.Gui_CommandMenu_Portal), opensSubmenu(portals)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, portals, guiGraphics)).iconUV(40, 60),
						new CommandMenuItem.Builder(magic, Component.translatable(Strings.Gui_CommandMenu_Magic), opensSubmenu(magic)).onUpdate((item, guiGraphics) -> updateRootItem(item, magic, guiGraphics)).iconUV(20, 60),
						new CommandMenuItem.Builder(items, Component.translatable(Strings.Gui_CommandMenu_Items), opensSubmenu(items)).onUpdate((item, guiGraphics) -> updateRootItem(item, items, guiGraphics)).iconUV(10, 60),
						new CommandMenuItem.Builder(drive, Component.translatable(Strings.Gui_CommandMenu_Drive), opensSubmenu(drive)).onUpdate((item, guiGraphics) -> updateRootItem(item, drive, guiGraphics)).iconUV(0, 60),
						new CommandMenuItem.Builder(revert, Component.translatable(Strings.Gui_CommandMenu_Drive_Revert), item -> {
							PlayerData playerData = PlayerData.get(minecraft.player);
							if (playerData.isFormActive(ModDriveForms.ANTI) && !playerData.isAbilityEquipped(ModAbilities.DARK_DOMINATION) && EntityEvents.threatLevel == EntityEvents.ThreatLevel.HOSTILES) {
								playErrorSound();
							} else {
								PacketHandler.sendToServer(new CSUseDriveFormPacket(ModDriveForms.NONE.location()));
								playSound(ModSounds.unsummon.get());
							}
						}).invisibleByDefault().onUpdate((item, guiGraphics) -> {
							if (item.isVisible()) {
								PlayerData playerData = PlayerData.get(minecraft.player);
								item.setActive(!(playerData.isFormActive(ModDriveForms.ANTI) && !playerData.isAbilityEquipped(ModAbilities.DARK_DOMINATION) && EntityEvents.threatLevel == EntityEvents.ThreatLevel.HOSTILES));
								if (PlayerData.get(minecraft.player).noFormActive()) {
									item.setVisible(false);
									item.getParent().getChild(drive).setVisible(true);
								}
							}
						}).iconUV(0, 60),
						new CommandMenuItem.Builder(limit, Component.translatable(Strings.Gui_CommandMenu_Limit), opensSubmenu(limit)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, limit, guiGraphics)).iconUV(0, 60)
				).build();
		CommandMenuSubMenu attackSubmenu = new CommandMenuSubMenu.Builder(attack, Component.translatable(Strings.Gui_CommandMenu_Attack).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(255, 0, 51))
				.onUpdate(updatePhysical())
				.onOpen(this::createPhysicalSpells)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu magicSubmenu = new CommandMenuSubMenu.Builder(magic, Component.translatable(Strings.Gui_CommandMenu_Magic_Title).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(102, 0, 255))
				.onUpdate(updateMagic())
				.onOpen(this::createMagicSpells)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu itemsSubmenu = new CommandMenuSubMenu.Builder(items, Component.translatable(Strings.Gui_CommandMenu_Items_Title).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(77, 255, 77))
				.onOpen(this::createItems)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu targetSubmenu = new CommandMenuSubMenu.Builder(target, Component.translatable(Strings.Gui_CommandMenu_Target).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(10, 51, 255))
				.onOpen(this::createTargets)
				.autoResizes()
				.build();
		CommandMenuSubMenu portalsSubmenu = new CommandMenuSubMenu.Builder(portals, Component.translatable(Strings.Gui_CommandMenu_Portals_Title).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(204, 204, 204))
				.onOpen(this::createPortals)
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu limitSubmenu = new CommandMenuSubMenu.Builder(limit, Component.translatable(Strings.Gui_CommandMenu_Limit_Title).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(255, 255, 0))
				.onUpdate(updateLimits())
				.withChildren(createLimitsFromRegistry())
				.autoResizes()
				.buildWithParent(rootSubmenu);
		CommandMenuSubMenu driveSubmenu = new CommandMenuSubMenu.Builder(drive, Component.translatable(Strings.Gui_CommandMenu_Drive_Title).withStyle(ClientUtils.KK_Font_EXP))
				.colour(new Color(0, 255, 255))
				.onUpdate(updateDriveForms())
				.withChildren(createDriveFormsFromRegistry())
				.autoResizes()
				.buildWithParent(rootSubmenu);
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

	private CommandMenuItem.OnEnter opensSubmenu(ResourceLocation subMenu) {
		return (item -> {
			if (changeSubmenu(subMenu, false)) {
				playInSound();
			} else {
				playErrorSound();
			}
		});
	}

	public CommandMenuSubMenu.OnUpdate updateMagic() {
		return updateSpells(MagicData.SpellType.MAGIC);
	}

	public CommandMenuSubMenu.OnUpdate updatePhysical() {
		return updateSpells(MagicData.SpellType.PHYSICAL);
	}

	private CommandMenuSubMenu.OnUpdate updateSpells(MagicData.SpellType type) {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<ResourceLocation, Integer> spellList = new HashMap<>();
			PlayerData playerData = PlayerData.get(minecraft.player);

			boolean hasSpells = false;
			List<ResourceLocation> spells = Utils.getSpellsList(playerData, type);

			for (ResourceLocation s : spells) {
				Magic magic = ModMagic.registry.get(s);

				if (magic != null) {
					hasSpells = true;
					spellList.put(s, i.getAndIncrement());
				}
			}

			if (!hasSpells && subMenu.getId().equals(type == MagicData.SpellType.MAGIC ? magic : attack)) {
				changeSubmenu(root, false);
				return;
			}


			List<CommandMenuItem> children = subMenu.getChildren();
			for (CommandMenuItem item : children) {
				int slot = Utils.getMagicSlotFromNameAndLevel(playerData.getEquippedMagics(), item.getId());
				ItemStack stack = playerData.getEquippedMagics().get(slot);

				if (stack != null && stack.getItem() instanceof MagicSpellItem) {
					item.setSorting(0);

					if (spellList.containsKey(item.getId())) {
						item.setSorting(spellList.get(item.getId()));
						item.setMessage(Component.translatable(ModMagic.registry.get(item.getId()).getTranslationKey()));
						item.setVisible(true);
					} else {
						item.setVisible(false);
					}
				}
			}
		};
	}

	public CommandMenuSubMenu.OnUpdate updateLimits() {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<String, Integer> limits = new HashMap<>();
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
			Map<ResourceLocation, Integer> formList = new HashMap<>();
			PlayerData playerData = PlayerData.get(minecraft.player);
			playerData.getDriveFormMap().forEach((s, ints) -> formList.put(s, ModDriveForms.registry.get(s).getOrder()));
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (formList.containsKey(item.getId())) {
					item.setSorting(formList.get(item.getId()));
					item.setVisible(ModDriveForms.registry.get(item.getId()).displayInCommandMenu(minecraft.player));
				} else {
					item.setVisible(false);
				}
			});
		};
	}

	public CommandMenuItem.Builder[] createDriveFormsFromRegistry() {
		List<CommandMenuItem.Builder> forms = new ArrayList<>();
		ModDriveForms.registry.stream().forEach(driveFormRegistryObject -> forms.add(new CommandMenuItem.Builder(driveFormRegistryObject.getRegistryName(), Component.translatable(driveFormRegistryObject.getTranslationKey()), item -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
			if (playerData.getDP() >= driveFormRegistryObject.getDriveCost()) {
				if (!antiFormCheck(playerData, driveFormRegistryObject)) {
					PacketHandler.sendToServer(new CSUseDriveFormPacket(driveFormRegistryObject.getRegistryName()));
				}
				changeSubmenu(root, true);
				playInSound();
			} else {
				playErrorSound();
			}
		}).onUpdate((item, guiGraphics) -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
			DriveForm form = ModDriveForms.registry.get(item.getId());
			item.setActive(playerData.getDP() >= form.getDriveCost());
		}).iconUV(0, 60)));
		return forms.toArray(new CommandMenuItem.Builder[0]);
	}

	public CommandMenuItem.Builder[] createLimitsFromRegistry() {
		List<CommandMenuItem.Builder> limits = new ArrayList<>();
		ModLimits.registry.forEach(limitRegistryObject -> limits.add(new CommandMenuItem.Builder(limitRegistryObject.getRegistryName(), Component.translatable(limitRegistryObject.getTranslationKey()), item -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
			if (playerData.getDP() < limitRegistryObject.getCost()) {
				playErrorSound();
			} else {
				if (InputHandler.lockOn != null)
					PacketHandler.sendToServer(new CSUseLimitPacket(limitRegistryObject.getRegistryName(), InputHandler.lockOn.getId()));
				else
					PacketHandler.sendToServer(new CSUseLimitPacket(limitRegistryObject.getRegistryName()));
				changeSubmenu(root, true);
				playInSound();
			}
		}).onUpdate((item, guiGraphics) -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
			if (playerData.getLimitCooldownTicks() > 0) {
				item.setActive(false);
				return;
			} else {
				item.setActive(true);
			}
			Limit limit = ModLimits.registry.get(item.getId());
			item.setMessage(Component.literal(Component.translatable(limit.getTranslationKey()).getString() + "  "));
			item.setActive(playerData.getDP() >= limit.getCost());
			if (item.getParent().isVisible()) {
				String cost = String.valueOf(ModLimits.registry.get(item.getId()).getCost() / 100);
				drawString(guiGraphics, font, cost, item.getX() + item.getWidth() - font.width(cost) - 16, item.getY() + 4, item.isActive() ? new Color(0, 255, 255).getRGB() : new Color(0, 255, 255).darker().darker().getRGB());
			}
		}).iconUV(0, 60)));
		return limits.toArray(new CommandMenuItem.Builder[0]);
	}

	public boolean isOrgMode() {
		return PlayerData.get(minecraft.player).getAlignment() != OrgMember.NONE;
	}

	public void onCancel(CommandMenuItem item, ResourceLocation submenu, GuiGraphics guiGraphics) {
		if (item.getId().equals(attack)) {
			PacketHandler.sendToServer(new CSSwapKeyblade());
		}
	}

	public void updateRootItem(CommandMenuItem item, ResourceLocation submenu, GuiGraphics guiGraphics) {
		PlayerData playerData = PlayerData.get(minecraft.player);
		if (item.getId().equals(portals) && isOrgMode() && item.getParent().getSelected().equals(item)) {
			if (minecraft.player.getMainHandItem() != null && minecraft.player.getMainHandItem().getItem() instanceof ArrowgunItem) {
				ItemStack weapon = minecraft.player.getMainHandItem();
				if (weapon.has(ModComponents.ARROWGUN_AMMO)) {
					int ammo = weapon.getOrDefault(ModComponents.ARROWGUN_AMMO, 0);
					drawString(guiGraphics, minecraft.font, ammo + "", item.getX() + 8 + item.getParent().getWidth(), item.getY() + 4, 0xFFFFFF);
				}
			}
		}
		if (item.getId().equals(drive)) {
			item.setVisible(!isOrgMode());
		} else if (item.getId().equals(portals) || item.getId().equals(limit)) {
			item.setVisible(isOrgMode());
		}
		if (submenu == null) {
			item.setActive(true);
			return;
		}

		if (item.getId().equals(attack)) {
			updateSpellCategory(item, playerData, MagicData.SpellType.PHYSICAL, Component.translatable(Strings.Gui_CommandMenu_Attack));
			return;
		}

		if (item.getId().equals(magic)) {
			updateSpellCategory(item, playerData, MagicData.SpellType.MAGIC, Component.translatable(Strings.Gui_CommandMenu_Magic));
			return;
		}

		if (item.getId().equals(drive)) {
			//System.out.println(playerData.getDriveFormMap());
			if (playerData.getDriveFormMap().entrySet().stream().filter(entry -> ModDriveForms.registry.get(entry.getKey()).displayInCommandMenu(minecraft.player)).toList().size() <= 0) { //If no forms are unlocked (fake forms + anti)
				item.setActive(false);
				item.setMessage(Component.literal("???"));
				return;
			} else { //If any form is unlocked
				if (minecraft.player.hasEffect(ModMobEffects.UNDERWORLD_CURSE)) {
					item.setActive(false);
					return;
				}
				item.setActive(true);
				Color color = playerData.getDP() >= Utils.getCheapestDriveCost(playerData, Utils.getVisibleDriveForms(minecraft.player)) ? Color.WHITE : Color.GRAY;
				item.setTextColour(color);
				item.setMessage(Component.translatable(Strings.Gui_CommandMenu_Drive));
			}
			if (!playerData.noFormActive()) { //while in a drive form
				item.setVisible(false);
				item.getParent().getChild(revert).setVisible(true);
			}
		}

		if (item.getId().equals(limit)) {
			if (minecraft.player.hasEffect(ModMobEffects.UNDERWORLD_CURSE) || playerData.getLimitCooldownTicks() > 0) {
				item.setActive(false);
				return;
			}
		}

		if (commandMenuElements.containsKey(submenu)) {
			if (submenu.equals(attack) || submenu.equals(magic)) {
				item.setActive(false);
				playerData.getEquippedMagics().forEach((integer, stack) -> {
					if (!stack.isEmpty()) {
						item.setActive(true);
					}
				});
				return;
			} else if (submenu.equals(items)) {
				item.setActive(false);
				playerData.getEquippedItems().forEach((integer, stack) -> {
					if (!stack.isEmpty()) {
						item.setActive(true);
					}
				});
				return;
			} else if (submenu.equals(portals)) {
				item.setActive(!WorldData.getClient().getAllPortalsFromOwnerID(minecraft.player.getUUID()).isEmpty());
				return;
			} else if (!commandMenuElements.get(submenu).getVisibleChildren().isEmpty()) {
				item.setActive(true);
				return;
			}
		}
		item.setActive(false);
	}

	private void updateSpellCategory(CommandMenuItem item, PlayerData playerData, MagicData.SpellType type, Component title) {
		if (!ModConfigs.SERVER_SPEC.isLoaded())
			return;

		item.setTextColour(Color.WHITE);

		double cheapest = Utils.getCheapestMagicCost(playerData.getEquippedMagics(), minecraft.player, type);

		if (Utils.getSpellsList(playerData, type).isEmpty() && type == MagicData.SpellType.MAGIC) { //Only set ??? to magic
			item.setActive(false);
			item.setMessage(Component.literal("???"));
			return;
		}

		boolean allowUseMagicIfCostIsHigher = ModConfigs.SERVER.allowCastMagicIfTooExpensive.get();

		boolean insufficientMP = cheapest > playerData.getMaxMP() && cheapest < 300;

		if ((playerData.getRecharge() || (!allowUseMagicIfCostIsHigher && insufficientMP)) && playerData.getMagicCooldownTicks() <= 0) {
			item.setTextColour(Color.GRAY);
		}

		DriveForm form = ModDriveForms.registry.get(playerData.getActiveDriveForm());

		if (playerData.getMagicCooldownTicks() > 0 || !form.canUseMagic()) {
			item.setActive(false);
			return;
		}

		item.setActive(true);
		item.setMessage(title);
	}

	public void createTargets(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();

		ArrayList<CommandMenuItem> targets = new ArrayList<>();
		WorldData worldData = WorldData.getClient();

		//Self should always show in case using an addon
		targets.add(new CommandMenuItem.Builder(
				KingdomKeys.rl(minecraft.player.getGameProfile().getName().toLowerCase()),
				Component.literal(minecraft.player.getGameProfile().getName()),
				item -> subMenu.getParent().getSelected().onEnter()
		).setData(minecraft.player.getId() + "").build(subMenu));

		//Party Members
		if (worldData.getPartyFromMember(minecraft.player.getUUID()) != null) {
			List<Party.Member> members = worldData
					.getPartyFromMember(minecraft.player.getUUID())
					.getMembers();

			members.stream()
					.filter(member -> !member.getUUID().equals(minecraft.player.getUUID()))
					.map(member -> minecraft.player.level().getPlayerByUUID(member.getUUID()))
					.filter(Objects::nonNull)
					.filter(playerAlly ->
							minecraft.player.distanceTo(playerAlly)
									<= ModConfigs.SERVER.partyRangeLimit.get()
					)
					.forEach(playerAlly -> {
						targets.add(new CommandMenuItem.Builder(
								KingdomKeys.rl(playerAlly.getGameProfile().getName().toLowerCase()),
								Component.literal(playerAlly.getGameProfile().getName()),
								item -> subMenu.getParent().getSelected().onEnter()
						).setData(playerAlly.getId() + "").build(subMenu));
					});
		}

		TargetSelectorEvent event = new TargetSelectorEvent(subMenu, targets);
		NeoForge.EVENT_BUS.post(event);

		event.getTargets().forEach(subMenu::addChild);
	}

	public void createPortals(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();
		WorldData worldData = WorldData.getClient();
		worldData.getAllPortalsFromOwnerID(minecraft.player.getUUID()).forEach(uuid -> {
			PortalData portalData = worldData.getPortalFromUUID(uuid);
			String rlUUID = uuid.toString().replaceAll("-", "_");
			subMenu.addChild(new CommandMenuItem.Builder(KingdomKeys.rl(rlUUID), Component.translatable(portalData.getName()), item -> {
				PortalData portal = worldData.getPortalFromUUID(UUID.fromString(item.getId().getPath().replaceAll("_", "-")));
				if (!portal.getPos().equals(new BlockPos(0, 0, 0))) { //If the portal is not default coords
					summonPortal(portal);
				} else {
					minecraft.player.sendSystemMessage(Component.translatable(ChatFormatting.RED + "You don't have any portal destinations"));
				}
				changeSubmenu(root, true);
				playInSound();
			}).iconUV(40, 60).build(subMenu));
		});
		subMenu.setSelected(subMenu.getFirst());
	}

	public void summonPortal(PortalData coords) {
		BlockPos destination = coords.getPos();

		if (minecraft.player.isShiftKeyDown()) {
			PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(minecraft.player.blockPosition(), destination, coords.getDimID()));
		} else {
			HitResult rtr = InputHandler.getMouseOverExtendedStraight(100);
			if (rtr != null) {
				double reachSq = 100 * 100;

				if (rtr instanceof BlockHitResult brtr) {
					double distanceSq = minecraft.player.distanceToSqr(brtr.getBlockPos().getX(), brtr.getBlockPos().getY(), brtr.getBlockPos().getZ());
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(brtr.getBlockPos().above(), destination, coords.getDimID()));
					}
				} else if (rtr instanceof EntityHitResult ertr) {
					double distanceSq = minecraft.player.distanceToSqr(ertr.getEntity().getX(), ertr.getEntity().getY(), ertr.getEntity().getZ());
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(ertr.getEntity().blockPosition(), destination, coords.getDimID()));
					}
				}
			}
		}
	}

	public void createMagicSpells(CommandMenuSubMenu subMenu) {
		createMagics(subMenu, MagicData.SpellType.MAGIC);
	}

	public void createPhysicalSpells(CommandMenuSubMenu subMenu) {
		createMagics(subMenu, MagicData.SpellType.PHYSICAL);
	}

	private void createMagics(CommandMenuSubMenu subMenu, MagicData.SpellType type) {
		subMenu.getChildren().clear();

		PlayerData playerData = PlayerData.get(minecraft.player);
		WorldData worldData = WorldData.getClient();
		for (Map.Entry<Integer, ItemStack> entry : playerData.getEquippedMagics().entrySet()) {
			Integer slot = entry.getKey();

			if (ModConfigs.hiddenMagic.contains(slot))
				continue;

			if (slot >= playerData.getMaxMagics())
				break;

			ItemStack stack = entry.getValue();

			if (stack.isEmpty() || !(stack.getItem() instanceof MagicSpellItem spell))
				continue;

			ResourceLocation magicId = spell.getMagic();
			Magic magic = ModMagic.registry.get(magicId);

			if (magic == null)
				continue;

			if (magic.getSpellType() != type)
				continue;

			subMenu.addChild(
					new CommandMenuItem.Builder(
							spell.getMagic(),
							Component.translatable(magic.getTranslationKey()),
							item -> {
								double cost = magic.getCost(minecraft.player);

								boolean allowUseMagicIfCostIsHigher = ModConfigs.SERVER.allowCastMagicIfTooExpensive.get();
								boolean insufficientMP = cost > playerData.getMaxMP() && cost < 300;

								if (playerData.getMaxMP() == 0 || playerData.getRecharge() || (!allowUseMagicIfCostIsHigher && insufficientMP)) {
									playErrorSound();
									changeSubmenu(root, true);
									return;
								}

								ArrayList<CommandMenuItem> targets = new ArrayList<>();
								NeoForge.EVENT_BUS.post(new TargetSelectorEvent(commandMenuElements.get(currentSubmenu), targets));

								boolean hasParty = worldData.getPartyFromMember(minecraft.player.getUUID()) != null;
								boolean needsTarget = magic.getHasToSelect();

								if ((hasParty && needsTarget) || !targets.isEmpty()) {
									if (currentSubmenu.equals(target) && commandMenuElements.get(currentSubmenu).getSelected() != null) {
										int targetID = Integer.parseInt(commandMenuElements.get(currentSubmenu).getSelected().getData());
										PacketHandler.sendToServer(new CSUseMagicPacket(magicId.toString(), targetID));
										changeSubmenu(root, true);
									} else {
										changeSubmenu(target, true);
										playInSound();
										return;
									}
								} else {
									PacketHandler.sendToServer(new CSUseMagicPacket(magicId.toString(), InputHandler.lockOn));

									//Cursor memory
									for (int i = 0; i < subMenu.getChildren().size(); i++) {
										lastUsedMagic = magicId;
									}

									changeSubmenu(root, true);
								}

								playSelectSound();
							})
							.onUpdate((item, guiGraphics) -> {
								PlayerData playerData2 = PlayerData.get(minecraft.player);
								if (playerData2.getMP() > 0 && !playerData2.getRecharge()) {
									item.setActive(true);
									item.setTextColour(Color.WHITE);

									double magCost = magic.getCost(minecraft.player);

									if (playerData2.getMP() <= magCost) {
										boolean allowUseMagicIfCostIsHigher = ModConfigs.SERVER.allowCastMagicIfTooExpensive.get();
										boolean insufficientMP = magCost > playerData2.getMaxMP() && magCost < 300;

										if (playerData2.getMaxMP() == 0 || playerData2.getRecharge() || (!allowUseMagicIfCostIsHigher && insufficientMP)) {
											item.setTextColour(Color.GRAY);
										} else {
											if (playerData2.isAbilityEquipped(ModAbilities.EXTRA_CAST)) {
												if (magCost >= playerData2.getMaxMP()) {
													item.setTextColour(Color.ORANGE);
												} else {
													if (playerData2.getMP() > 1 && playerData2.getMP() - magCost < 1) {
														item.setTextColour(Color.WHITE);
													} else {
														item.setTextColour(Color.ORANGE);
													}
												}
											} else {
												item.setTextColour(Color.ORANGE);
											}
										}
									}
								} else {
									item.setTextColour(Color.WHITE);
									item.setActive(false);
								}
							})
							.iconUV(type == MagicData.SpellType.PHYSICAL ? 30 : 20, 60)
							.build(subMenu)
			);
		}

		subMenu.setSelected(subMenu.getFirst());

		for (CommandMenuItem child : subMenu.getChildren()) {
			if (child.getId().equals(lastUsedMagic)) {
				subMenu.setSelected(child);
				break;
			}
		}
	}

	public void createItems(CommandMenuSubMenu subMenu) {
		subMenu.getChildren().clear();
		PlayerData playerData = PlayerData.get(minecraft.player);
		WorldData worldData = WorldData.getClient();
		playerData.getEquippedItems().forEach((integer, stack) -> {
			if (!stack.isEmpty()) {
				subMenu.addChild(
						new CommandMenuItem.Builder(
								KingdomKeys.rl(integer.toString()),
								Component.literal(stack.getDisplayName().getString().substring(1, stack.getDisplayName().getString().length() - 1)),
								item -> {
									if (stack.getItem() instanceof KKPotionItem potion) {
										//potion.potionEffect(player);
										Party party = worldData.getPartyFromMember(minecraft.player.getUUID());

										if (potion.isGlobal() || party == null) {
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
								}).iconUV(10, 60)
								.build(subMenu));
			}
		});
		subMenu.setSelected(subMenu.getFirst());
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
		Minecraft.getInstance().level.playSound(player, player.position().x(), player.position().y(), player.position().z(), sound, SoundSource.MASTER, 1.0f, 1.0f);
	}

	public boolean changeSubmenu(ResourceLocation submenu, boolean resetSelected) {
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
				return true;
			} else {
				newSubmenu.setActive(false);
				currentSubMenu.setActive(true);
				return false;
			}
		}
		return false;
	}

	public boolean antiFormCheck(PlayerData playerData, DriveForm driveForm) { //Only checks if form is not final
		if (!driveForm.canGoAnti()) {
			return false;
		}
		if (playerData.isAbilityEquipped(ModAbilities.DARK_DOMINATION)) {
			return false;
		}

		if (playerData.isAbilityEquipped(ModAbilities.LIGHT_AND_DARKNESS)) { // Will always be true
			PacketHandler.sendToServer(new CSSummonKeyblade(true));
			PacketHandler.sendToServer(new CSUseDriveFormPacket(ModDriveForms.ANTI.location()));
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
			PacketHandler.sendToServer(new CSUseDriveFormPacket(ModDriveForms.ANTI.location()));
			playSound(ModSounds.antidrive.get());

			changeSubmenu(root, true);
			playSelectSound();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		guiGraphics.managed = true;
		if (minecraft.player != null) {
			ClientUtils.RC_ELEMENT.applyTransform(guiGraphics, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());

			drawReactionCommands(guiGraphics, deltaTracker);
			ClientUtils.RC_ELEMENT.endTransform(guiGraphics);

			ClientUtils.CM_ELEMENT.applyTransform(guiGraphics, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());

			List<CommandMenuSubMenu> submenus = commandMenuElements.values().stream().sorted(Comparator.comparingInt(CommandMenuSubMenu::getZ)).toList();
			submenus.forEach(submenu -> {
				submenu.render(guiGraphics, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), deltaTracker.getGameTimeDeltaPartialTick(true));
				submenu.onUpdate(guiGraphics);
			});
			ClientUtils.CM_ELEMENT.endTransform(guiGraphics);
		}
		guiGraphics.flush();

		guiGraphics.managed = false;
	}

	public void drawReactionCommands(GuiGraphics gui, DeltaTracker deltaTracker) {
		float alpha = 1F;
		float scale = 1.05f;
		PlayerData playerData = PlayerData.get(minecraft.player);
		LinkedHashMap<ResourceLocation, Integer> list = playerData.getReactionCommands();
		if (list.isEmpty())
			return;

		ResourceLocation rcTexture = commandMenuElements.get(currentSubmenu).getTexture();

		int i = 0;
		for (Map.Entry<ResourceLocation, Integer> entry : list.entrySet()) {
			gui.pose().pushPose();
			{
				float shade = i == reactionSelected ? 1F : 0.4F;
				RenderSystem.setShaderColor(shade, shade, shade, alpha);
				gui.pose().translate(0, commandMenuElements.get(currentSubmenu).getY() - (16 * i), 2F);
				gui.pose().scale(scale, scale, scale);
				gui.pose().pushPose();
				{
					ReactionCommand command = ModReactionCommands.registry.get(entry.getKey());
					String time = "";
					if (entry.getValue() > -1) {
						time = String.format("%.1f", entry.getValue() / 20.0);
						gui.pose().pushPose();
						gui.pose().scale(0.6F, 0.8F, scale);
						drawString(gui, minecraft.font, Component.literal(time).withStyle(ClientUtils.KK_Font_EXP), (int) ((TOP_WIDTH - ModConfigs.cmReactionEndRWidth) * 1.9F), 6, 0xFFFFFF);
						gui.pose().popPose();
					}
					drawString(gui, minecraft.font, Utils.translateToLocal(command.getTranslationKey()), ModConfigs.cmTextXOffset + 15, 4, 0xFFFFFF);

					gui.pose().scale(1.33F, 1, 1);
					RenderSystem.enableBlend();
					if (time.isEmpty()) {
						drawRC(gui, rcTexture);
					} else {
						drawSC(gui, rcTexture, entry);
					}

					if (i == reactionSelected) {
						gui.pose().pushPose();
						{
							gui.pose().scale(0.55F, 0.8F, 1);
							blit(gui, rcTexture, 4, 4, 52, 45, 10, 10);
							gui.drawString(minecraft.font, Component.literal(InputHandler.Keybinds.REACTION_COMMAND.getKeybind().getKey().getDisplayName().getString()).withStyle(ClientUtils.KK_Font_MENU), 6, 5, 0xFFFFFF, false);
						}
						gui.pose().popPose();
					}

					RenderSystem.disableBlend();
				}
				gui.pose().popPose();
			}
			gui.pose().popPose();
			i++;
		}

		if (reactionSelected >= list.size()) {
			reactionSelected = 0;
		}

	}

	private void drawSC(GuiGraphics gui, ResourceLocation rcTexture, Map.Entry<ResourceLocation, Integer> entry) {
		ReactionCommand command = ModReactionCommands.registry.get(entry.getKey());
		int middleWidth = TOP_WIDTH - (ModConfigs.cmReactionEndLWidth + ModConfigs.cmReactionEndRWidth) + 1;
		//Black bg bar
		blit(gui, rcTexture, ModConfigs.cmReactionEndLWidth - 2, 0, middleWidth + 4, TOP_HEIGHT, 48, 45, 1, TOP_HEIGHT, 256, 256);

		PlayerData playerData = PlayerData.get(minecraft.player);
		int maxDuration = (int) (command.getDuration() + command.getDuration() * (playerData.getNumberOfAbilitiesEquipped(ModAbilities.GRAND_MAGIC_EXTENDER) * 0.25F));
		float perc = 100F * entry.getValue() / maxDuration;
		//Purple bar
		Color color = new Color(command.getColor());
		RenderSystem.setShaderColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1);
		blit(gui, rcTexture, ModConfigs.cmReactionEndLWidth - 2, 0, (int) ((middleWidth + 4) * perc / 100F), TOP_HEIGHT, 50, 45, 1, TOP_HEIGHT, 256, 256);
		RenderSystem.setShaderColor(1, 1, 1, 1);

		//Left
		blit(gui, rcTexture, 0, 0, 24, 45, ModConfigs.cmReactionEndLWidth + 1, TOP_HEIGHT);
		//Middle
		blit(gui, rcTexture, ModConfigs.cmReactionEndLWidth, 0, middleWidth, TOP_HEIGHT, ModConfigs.cmReactionEndLWidth + 25, 45, 1, TOP_HEIGHT, 256, 256);
		//Right
		blit(gui, rcTexture, TOP_WIDTH - ModConfigs.cmReactionEndRWidth, 0, ModConfigs.cmReactionEndLWidth + 27, 45, ModConfigs.cmReactionEndRWidth, TOP_HEIGHT);

	}

	private void drawRC(GuiGraphics gui, ResourceLocation rcTexture) {
		//Left
		blit(gui, rcTexture, 0, 0, 0, 45, ModConfigs.cmReactionEndLWidth + 1, TOP_HEIGHT);

		int middleWidth = TOP_WIDTH - (ModConfigs.cmReactionEndLWidth + ModConfigs.cmReactionEndRWidth) + 1;
		//Middle
		blit(gui, rcTexture, ModConfigs.cmReactionEndLWidth, 0, middleWidth, TOP_HEIGHT, ModConfigs.cmReactionEndLWidth + 1, 45, 1, TOP_HEIGHT, 256, 256);
		//Right
		blit(gui, rcTexture, TOP_WIDTH - ModConfigs.cmReactionEndRWidth, 0, ModConfigs.cmReactionEndLWidth + 3, 45, ModConfigs.cmReactionEndRWidth, TOP_HEIGHT);

	}
}
