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
import online.kingdomkeys.kingdomkeys.api.event.client.TargetSelectorEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ClientConfig;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandMenuGui extends OverlayBase {

	public static final CommandMenuGui INSTANCE = new CommandMenuGui();
	public static Map<ResourceLocation, CommandMenuSubMenu> commandMenuElements;

	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;
	int MENU_WIDTH = 71;
	int MENU_HEIGHT = 15;
	int iconWidth = 10;
	int textX = 0;

	public static final int NONE = 0;
	public static int reactionSelected = 0;

	public final ResourceLocation root, attack, magic, items, drive, portals, target, limit, revert;

	public ResourceLocation currentSubmenu;

	private CommandMenuGui() {
		super();
		root = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "root");
		attack = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "attack");
		magic = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magic");
		items = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "items");
		drive = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "drive");
		portals = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "portals");
		limit = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "limit");
		target = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "target");
		revert = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "revert");
		currentSubmenu = root;
		commandMenuElements = new HashMap<>();
		CommandMenuSubMenu rootSubmenu = new CommandMenuSubMenu.Builder(this.root, Component.translatable(Strings.Gui_CommandMenu_Command))
				.position(ModConfigs.cmXPos, Minecraft.getInstance().getWindow().getGuiScaledHeight())
				.openByDefault()
				.changesColour()
				.fixedHeader()
				.colour(new Color(10, 51, 255))
				.onUpdate((subMenu, guiGraphics) -> {
					subMenu.updatePosition(ModConfigs.cmXPos, Minecraft.getInstance().getWindow().getGuiScaledHeight());
				})
				.withChildren(
						new CommandMenuItem.Builder(attack, Component.translatable(Strings.Gui_CommandMenu_Attack), null).onUpdate((item, guiGraphics) -> updateRootItem(item, null, guiGraphics)).iconUV(30, 60),
						new CommandMenuItem.Builder(portals, Component.translatable(Strings.Gui_CommandMenu_Portal), opensSubmenu(portals)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, portals, guiGraphics)).iconUV(40, 60),
						new CommandMenuItem.Builder(magic, Component.translatable(Strings.Gui_CommandMenu_Magic), opensSubmenu(magic)).onUpdate((item, guiGraphics) -> updateRootItem(item, magic, guiGraphics)).iconUV(20, 60),
						new CommandMenuItem.Builder(items, Component.translatable(Strings.Gui_CommandMenu_Items), opensSubmenu(items)).onUpdate((item, guiGraphics) -> updateRootItem(item, items, guiGraphics)).iconUV(10, 60),
						new CommandMenuItem.Builder(drive, Component.translatable(Strings.Gui_CommandMenu_Drive), opensSubmenu(drive)).onUpdate((item, guiGraphics) -> updateRootItem(item, drive, guiGraphics)).iconUV(0, 60),
						new CommandMenuItem.Builder(revert, Component.translatable(Strings.Gui_CommandMenu_Drive_Revert), item -> {
							PlayerData playerData = PlayerData.get(minecraft.player);
							if (playerData.getActiveDriveForm().equals(Strings.Form_Anti) && !playerData.isAbilityEquipped(Strings.darkDomination) && EntityEvents.isHostiles) {
								playErrorSound();
							} else {
								PacketHandler.sendToServer(new CSUseDriveFormPacket(DriveForm.NONE.toString()));
								playSound(ModSounds.unsummon.get());
							}
						}).invisibleByDefault().onUpdate((item, guiGraphics) -> {
							if (item.isVisible()) {
								if (PlayerData.get(minecraft.player).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
									item.setVisible(false);
									item.getParent().getChild(drive).setVisible(true);
								}
							}
						}).iconUV(0, 60),
						new CommandMenuItem.Builder(limit, Component.translatable(Strings.Gui_CommandMenu_Limit), opensSubmenu(limit)).invisibleByDefault().onUpdate((item, guiGraphics) -> updateRootItem(item, limit, guiGraphics)).iconUV(0, 60)
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

	private CommandMenuItem.OnEnter opensSubmenu(ResourceLocation subMenu) {
		return (item -> {
			changeSubmenu(subMenu, false);
			playInSound();
		});
	}

	public CommandMenuSubMenu.OnUpdate updateMagic() {
		return (subMenu, guiGraphics) -> {
			AtomicInteger i = new AtomicInteger(0);
			Map<String, Integer> magicList = new HashMap<>();
			PlayerData playerData = PlayerData.get(minecraft.player);
			ModConfigs.magicDisplayedInCommandMenu.stream().filter(m -> playerData.getMagicsMap().containsKey(m)).toList().forEach(s -> {
				magicList.put(s, i.getAndIncrement());
			});
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (magicList.containsKey(item.getId().toString())) {
					item.setSorting(magicList.get(item.getId().toString()));
					item.setMessage(Component.translatable(ModMagic.registry.get(item.getId()).getTranslationKey(playerData.getMagicLevel(item.getId()))));
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
			PlayerData playerData = PlayerData.get(minecraft.player);
			playerData.getDriveFormMap().forEach((s, ints) -> {
				formList.put(s, ModDriveForms.registry.get(ResourceLocation.parse(s)).getOrder());
			});
			subMenu.getChildren().forEach(item -> {
				item.setSorting(0);
				if (formList.containsKey(item.getId().toString())) {
					item.setSorting(formList.get(item.getId().toString()));
                    item.setVisible(ModDriveForms.registry.get(item.getId()).displayInCommandMenu(minecraft.player));
				} else {
					item.setVisible(false);
				}
			});
		};
	}

	public CommandMenuItem.Builder[] createMagicFromRegistry() {
		List<CommandMenuItem.Builder> magic = new ArrayList<>();
		ModMagic.registry.forEach(magicRegistryObject -> magic.add(new CommandMenuItem.Builder(magicRegistryObject.getRegistryName(), Component.translatable(magicRegistryObject.getTranslationKey()), item -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
			WorldData worldData = WorldData.getClient();
			int[] mag = playerData.getMagicsMap().get(magicRegistryObject.getRegistryName().toString());
			double cost = magicRegistryObject.getCost(mag[0], minecraft.player);

			if (playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
				playErrorSound();
				changeSubmenu(root, true);
			} else {
				if (worldData.getPartyFromMember(minecraft.player.getUUID()) != null && ModMagic.registry.get(magicRegistryObject.getRegistryName()).getHasToSelect()) { //Open party target selector
					if (currentSubmenu.equals(target) && commandMenuElements.get(currentSubmenu).getSelected() != null) {
                        int level = playerData.getMagicLevel(magicRegistryObject.getRegistryName());
                        String data = commandMenuElements.get(currentSubmenu).getSelected().getData();
                        int targetID = Integer.parseInt(data);
                        PacketHandler.sendToServer(new CSUseMagicPacket(magicRegistryObject.getRegistryName().toString(), targetID, level));

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
				PlayerData playerData = PlayerData.get(minecraft.player);
				Magic magicInst = ModMagic.registry.get(item.getId());
				if (playerData.getMP() > 0 && !playerData.getRecharge()) {
					item.setActive(true);
					item.setTextColour(Color.WHITE);
					double magCost = magicInst.getCost(playerData.getMagicLevel(item.getId()), Minecraft.getInstance().player);
					if (playerData.getMP() <= magCost) {
						if(playerData.getMaxMP() < magCost && magCost < 300){ //Cure case using all
							item.setTextColour(Color.GRAY);
						} else {
							//Extra Cast
							if(playerData.isAbilityEquipped(Strings.extraCast)){
								if(magCost >= playerData.getMaxMP()){
									item.setTextColour(Color.ORANGE);
								} else {// if it's a normal magic
									if(playerData.getMP() > 1 && playerData.getMP() - magCost < 1) { // If the player has more than 1MP and the magic would consume it all
										item.setTextColour(Color.WHITE);
									} else { //If the player has 1MP already orange
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
			}).iconUV(160, 60)));
		return magic.toArray(new CommandMenuItem.Builder[0]);
	}

	public CommandMenuItem.Builder[] createDriveFormsFromRegistry() {
		List<CommandMenuItem.Builder> forms = new ArrayList<>();
		ModDriveForms.registry.stream().forEach(driveFormRegistryObject -> forms.add(new CommandMenuItem.Builder(driveFormRegistryObject.getRegistryName(), Component.translatable(driveFormRegistryObject.getTranslationKey()), item -> {
			PlayerData playerData = PlayerData.get(minecraft.player);
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
				drawString(guiGraphics, font, cost, item.getX() +item.getWidth()- font.width(cost)-16, item.getY() + 4, item.isActive() ? new Color(0, 255, 255).getRGB() : new Color(0, 255, 255).darker().darker().getRGB());
			}
		}).iconUV(0, 60)));
		return limits.toArray(new CommandMenuItem.Builder[0]);
	}

	public boolean isOrgMode() {
		return PlayerData.get(minecraft.player).getAlignment() != OrgMember.NONE;
	}

	public void updateRootItem(CommandMenuItem item, ResourceLocation submenu, GuiGraphics guiGraphics) {
		PlayerData playerData = PlayerData.get(minecraft.player);
		if (item.getId().equals(portals) && isOrgMode() && item.getParent().getSelected().equals(item)) {
			if (minecraft.player.getMainHandItem() != null && minecraft.player.getMainHandItem().getItem() instanceof ArrowgunItem) {
				ItemStack weapon = minecraft.player.getMainHandItem();
				if (weapon.has(ModComponents.ARROWGUN_AMMO)) {
					int ammo = weapon.getOrDefault(ModComponents.ARROWGUN_AMMO, 0);
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
		if(item.getId().equals(magic)) {
			item.setTextColour(Color.WHITE);

			//This first part of the condition is to avoid the code below from making it even darker
			if(!playerData.getMagicsMap().isEmpty() && (playerData.getRecharge() || (playerData.getMaxMP() < Utils.getCheapestMagicCost(playerData.getMagicsMap(),minecraft.player)) && (Utils.getCheapestMagicCost(playerData.getMagicsMap(),minecraft.player)) < 300) && playerData.getMagicCooldownTicks() <= 0) { //Small hack to avoid gray and dark gray flicker when using last magic and going on recharge
				item.setTextColour(Color.GRAY); //Still allows to open submenu
			}

			DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
			if(playerData.getMagicCooldownTicks() > 0 || !form.canUseMagic()){
				item.setActive(false); //Doesn't allow opening submenu while a magic is being casted and on CD
				return;
			}

			if(playerData.getMagicsMap().isEmpty()){
				item.setActive(false);
				item.setMessage(Component.literal("???"));
			} else {
				item.setActive(true);
				item.setMessage(Component.translatable(Strings.Gui_CommandMenu_Magic));
			}

		}

		if (item.getId().equals(drive)){
			if(playerData.getDriveFormMap().size() < 4){
				item.setActive(false);
				item.setMessage(Component.literal("???"));
			} else {
				item.setActive(true);
				Color color = playerData.getDP() >= Utils.getCheapestDriveCost(playerData,Utils.getVisibleDriveForms(minecraft.player)) ? Color.WHITE : Color.GRAY;
				item.setTextColour(color);
				item.setMessage(Component.translatable(Strings.Gui_CommandMenu_Drive));
			}
			if(!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
				item.setVisible(false);
				item.getParent().getChild(revert).setVisible(true);
			}
		}

		if(item.getId().equals(limit)){
			if (playerData.getLimitCooldownTicks() > 0) {
				item.setActive(false);
				return;
			}
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
				item.setActive(!WorldData.getClient().getAllPortalsFromOwnerID(minecraft.player.getUUID()).isEmpty());
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

        ArrayList<CommandMenuItem> targets = new ArrayList<>();
        WorldData worldData = WorldData.getClient();

        if (worldData.getPartyFromMember(minecraft.player.getUUID()) != null) {
            targets.add(new CommandMenuItem.Builder(
                    ResourceLocation.fromNamespaceAndPath(
                            KingdomKeys.MODID,
                            minecraft.player.getDisplayName().getString().toLowerCase()
                    ),
                    Component.literal(minecraft.player.getDisplayName().getString()),
                    item -> subMenu.getParent().getSelected().onEnter()
            ).setData(minecraft.player.getId()+"").build(subMenu));

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
                                ResourceLocation.fromNamespaceAndPath(
                                        KingdomKeys.MODID,
                                        playerAlly.getDisplayName().getString().toLowerCase()
                                ),
                                Component.literal(playerAlly.getDisplayName().getString()),
                                item -> subMenu.getParent().getSelected().onEnter()
                        ).setData(playerAlly.getId()+"").build(subMenu));
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
			subMenu.addChild(new CommandMenuItem.Builder(ResourceLocation.parse(rlUUID), Component.translatable(portalData.getName()), item -> {
				PortalData portal = worldData.getPortalFromUUID(UUID.fromString(item.getId().getPath().replaceAll("_", "-")));
				if (!portal.getPos().equals(new BlockPos(0,0,0))) { //If the portal is not default coords
					summonPortal(portal);
				} else {
					minecraft.player.sendSystemMessage(Component.translatable(ChatFormatting.RED + "You don't have any portal destinations"));
				}
				changeSubmenu(root, true);
				playInSound();
			}).iconUV(40, 60).build(subMenu));
		});
	}

	public void summonPortal(PortalData coords) {
		BlockPos destination = coords.getPos();

		if (minecraft.player.isShiftKeyDown()) {
			PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(minecraft.player.blockPosition(), destination, coords.getDimID()));
		} else {
			HitResult rtr = InputHandler.getMouseOverExtended(100);
			if (rtr != null) {
				if(rtr instanceof BlockHitResult brtr) {
                    double distanceSq = minecraft.player.distanceToSqr(brtr.getBlockPos().getX(), brtr.getBlockPos().getY(), brtr.getBlockPos().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(brtr.getBlockPos().above(), destination, coords.getDimID()));
					}
				} else if(rtr instanceof EntityHitResult ertr) {
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
		PlayerData playerData = PlayerData.get(minecraft.player);
		WorldData worldData = WorldData.getClient();
		playerData.getEquippedItems().forEach((integer, stack) -> {
				if (!stack.isEmpty()) {
					subMenu.addChild(
					new CommandMenuItem.Builder(
							ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, integer.toString()),
							Component.literal(stack.getDisplayName().getString().substring(1, stack.getDisplayName().getString().length()-1)),
							item -> {
								if(stack.getItem() instanceof KKPotionItem potion) {
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
							}).iconUV(10, 60)
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

	public boolean antiFormCheck(PlayerData playerData, DriveForm driveForm) { //Only checks if form is not final
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
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		//textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
		if (minecraft.player != null) {
			drawReactionCommands(guiGraphics, deltaTracker);

			List<CommandMenuSubMenu> submenus = commandMenuElements.values().stream().sorted(Comparator.comparingInt(CommandMenuSubMenu::getZ)).toList();
			submenus.forEach(submenu -> {
				submenu.render(guiGraphics, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), deltaTracker.getGameTimeDeltaPartialTick(true));
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

	public void drawReactionCommands(GuiGraphics gui, DeltaTracker deltaTracker) {
		float alpha = 1F;
		float scale = 1.05f;
		PlayerData playerData = PlayerData.get(minecraft.player);
		List<String> list = playerData.getReactionCommands();
        if(list.isEmpty())
            return;

		for(int i = 0; i < list.size(); i++) {
			gui.pose().pushPose();
			{
				float shade = i == reactionSelected ? 1F : 0.4F;
				RenderSystem.setShaderColor(shade,shade,shade, alpha);
				gui.pose().translate(0, commandMenuElements.get(currentSubmenu).getY()-20-(16*i), 0.5F);
				gui.pose().scale(scale, scale, scale);
				gui.pose().pushPose();
				{
					ReactionCommand command = ModReactionCommands.registry.get(ResourceLocation.parse(list.get(i)));
					drawString(gui, minecraft.font, Utils.translateToLocal(command.getTranslationKey()), (int) (5 * ModConfigs.cmXScale / 100D) + (ModConfigs.cmTextXOffset+5), 4, 0xFFFFFF);

					gui.pose().scale(ModConfigs.cmXScale / 75F, 1, 1);
					RenderSystem.enableBlend();
					blit(gui, commandMenuElements.get(currentSubmenu).getTexture(), 0, 0, 0, 45, ModConfigs.cmReactionEndLWidth, TOP_HEIGHT);
					blit(gui, commandMenuElements.get(currentSubmenu).getTexture(), ModConfigs.cmReactionEndLWidth, 0, TOP_WIDTH - (ModConfigs.cmReactionEndLWidth + ModConfigs.cmReactionEndRWidth), TOP_HEIGHT, ModConfigs.cmReactionEndLWidth + 1, 45, 1, TOP_HEIGHT, 256, 256);
					blit(gui, commandMenuElements.get(currentSubmenu).getTexture(), TOP_WIDTH - ModConfigs.cmReactionEndRWidth, 0, ModConfigs.cmReactionEndLWidth + 3, 45, ModConfigs.cmReactionEndRWidth, TOP_HEIGHT);
					RenderSystem.disableBlend();

				}
				gui.pose().popPose();

			}
			gui.pose().popPose();
		}

	}
}
