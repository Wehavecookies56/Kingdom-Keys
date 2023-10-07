package online.kingdomkeys.kingdomkeys.handler;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollingEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.mob.SpawningOrbEntity;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.lib.*;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class InputHandler {

    List<UUID> portalCommands;
    Map<String, int[]> driveFormsMap;
    List<Member> targetsList;
    List<Limit> limitsList;
    List<String> magicList;
    Map<Integer, ItemStack> itemsList;
    List<String> reactionList = new ArrayList<String>();
    
    public static LivingEntity lockOn = null;
    public static int qrCooldown = 40;

    @SubscribeEvent
    public void renderOverlays(RenderGuiOverlayEvent event) {
        switch (ModConfigs.showGuiToggle) {
            case HIDE:
                event.setCanceled(
                        event.getOverlay() == ClientSetup.COMMAND_MENU ||
                        event.getOverlay() == ClientSetup.PLAYER_PORTRAIT ||
                        event.getOverlay() == ClientSetup.HP_BAR ||
                        event.getOverlay() == ClientSetup.MP_BAR ||
                        event.getOverlay() == ClientSetup.DRIVE_BAR ||
                        event.getOverlay() == ClientSetup.SHOTLOCK
                );
                break;
        }
    }

    public boolean antiFormCheck() { //Only checks if form is not final
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        Level world = mc.level;

		if(playerData.isAbilityEquipped(Strings.darkDomination)) {
			return false;
		}

        if(playerData.isAbilityEquipped(Strings.lightAndDarkness)) {
        	PacketHandler.sendToServer(new CSSummonKeyblade(true));
            PacketHandler.sendToServer(new CSUseDriveFormPacket(Strings.Form_Anti));
    		player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.antidrive.get(), SoundSource.MASTER, 1.0f, 1.0f);

            CommandMenuGui.selected = CommandMenuGui.ATTACK;
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_select.get(), SoundSource.MASTER, 1.0f, 1.0f);
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
    		player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.antidrive.get(), SoundSource.MASTER, 1.0f, 1.0f);

            CommandMenuGui.selected = CommandMenuGui.ATTACK;
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_select.get(), SoundSource.MASTER, 1.0f, 1.0f);
            return true;
        } else {
            return false;
        }
    }

    public void commandUp() {
        Minecraft mc = Minecraft.getInstance();
        mc.level.playSound(mc.player, mc.player.position().x(),mc.player.position().y(),mc.player.position().z(), ModSounds.menu_move.get(), SoundSource.MASTER, 1.0f, 1.0f);

        loadLists();

        // Mainmenu
        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
            if (CommandMenuGui.selected == CommandMenuGui.ATTACK)
                CommandMenuGui.selected = CommandMenuGui.DRIVE;
            else
                CommandMenuGui.selected++;
        }
        // InsideMagic
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (CommandMenuGui.magicSelected > 0) {
                CommandMenuGui.magicSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
            } else if (CommandMenuGui.magicSelected <= 1)
                CommandMenuGui.magicSelected = this.magicList.size() - 1;
        }
        // InsideItems
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            if (CommandMenuGui.itemSelected > 0) {
                CommandMenuGui.itemSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
            } else if (CommandMenuGui.itemSelected <= 1) {
                CommandMenuGui.itemSelected = this.itemsList.size() - 1;
            }
        }
        // InsideDrive
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (CommandMenuGui.driveSelected > 0) {
                CommandMenuGui.driveSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
            } else if (CommandMenuGui.driveSelected <= 1) {
                CommandMenuGui.driveSelected = this.driveFormsMap.size() - 1;
            }
        }
        // InsidePortal
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            if (CommandMenuGui.portalSelected > 0) {
                CommandMenuGui.portalSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
            } else if (CommandMenuGui.portalSelected <= 1) {
                CommandMenuGui.portalSelected = this.portalCommands.size() - 1;
            }
        }
        // InsideAttacks
        /*else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            if (CommandMenuGui.attackSelected > 0) {
                CommandMenuGui.attackSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
            } else if (CommandMenuGui.attackSelected <= 1) {
                CommandMenuGui.attackSelected = this.attackCommands.size() - 1;
            }
        }*/
        //InsideTargetSelector
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (CommandMenuGui.targetSelected > 0) {
                CommandMenuGui.targetSelected--;
                CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
            } else if (CommandMenuGui.attackSelected <= 1) {
                CommandMenuGui.targetSelected = this.targetsList.size() - 1;
            }
        }        
        //InsideLimits
        else if(CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
        	 if (CommandMenuGui.limitSelected > 0) {
                 CommandMenuGui.limitSelected--;
                 CommandMenuGui.submenu = CommandMenuGui.SUB_LIMIT;
             } else if (CommandMenuGui.attackSelected <= 1) {
                 CommandMenuGui.limitSelected = this.limitsList.size() - 1;
             }
        }
    }

    public void commandDown() {
        Minecraft mc = Minecraft.getInstance();
        mc.level.playSound(mc.player, mc.player.position().x(),mc.player.position().y(),mc.player.position().z(), ModSounds.menu_move.get(), SoundSource.MASTER, 1.0f, 1.0f);
        loadLists();

        // Mainmenu
        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
            if (CommandMenuGui.selected == CommandMenuGui.DRIVE)
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
            else
                CommandMenuGui.selected--;
        }
        // InsideMagic
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (CommandMenuGui.magicSelected < this.magicList.size() - 1) {
                CommandMenuGui.magicSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
            } else if (CommandMenuGui.magicSelected >= this.magicList.size() - 1)
                CommandMenuGui.magicSelected = 0;
        }
        // InsideItems
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            if (CommandMenuGui.itemSelected < this.itemsList.size() - 1) {
                CommandMenuGui.itemSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
            } else {
                if (CommandMenuGui.itemSelected >= this.itemsList.size() - 1)
                    CommandMenuGui.itemSelected = 0;
            }
        }
        // InsideDrive
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (CommandMenuGui.driveSelected < this.driveFormsMap.size() - 1) {
                CommandMenuGui.driveSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
            } else {
                if (CommandMenuGui.driveSelected >= this.driveFormsMap.size() - 1)
                    CommandMenuGui.driveSelected = 0;
            }
        }
        // InsidePortal
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            if (CommandMenuGui.portalSelected < this.portalCommands.size() - 1) {
                CommandMenuGui.portalSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
            } else {
                if (CommandMenuGui.portalSelected >= this.portalCommands.size() - 1)
                    CommandMenuGui.portalSelected = 0;
            }
        }
        // InsideAttack
        /*else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            if (CommandMenuGui.attackSelected < this.attackCommands.size() - 1) {
                CommandMenuGui.attackSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
            } else {
                if (CommandMenuGui.attackSelected >= this.attackCommands.size() - 1)
                    CommandMenuGui.attackSelected = 0;
            }
        }*/
        //InsideTargetSelector
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (CommandMenuGui.targetSelected < this.targetsList.size() - 1) {
                CommandMenuGui.targetSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
            } else {
                if (CommandMenuGui.targetSelected >= this.targetsList.size() - 1)
                    CommandMenuGui.targetSelected = 0;
            }
        }
        //InsideLimits
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
            if (CommandMenuGui.limitSelected < this.limitsList.size() - 1) {
                CommandMenuGui.limitSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_LIMIT;
            } else {
                if (CommandMenuGui.limitSelected >= this.limitsList.size() - 1)
                    CommandMenuGui.limitSelected = 0;
            }
        }
    }

    public void commandEnter() {
    	Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level world = mc.level;
        loadLists();

        //ExtendedWorldData worldData = ExtendedWorldData.get(world);
        IWorldCapabilities worldData = ModCapabilities.getWorld(world);
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        if(playerData == null || worldData == null)
        	return;
        switch (CommandMenuGui.selected) {
            case CommandMenuGui.ATTACK: //Accessing ATTACK / PORTAL submenu
                if (playerData.getAlignment() != Utils.OrgMember.NONE) {
                    // Submenu of the portals
                    if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                        if (!this.portalCommands.isEmpty() && !playerData.getRecharge()) {
                            CommandMenuGui.submenu = CommandMenuGui.SUB_PORTALS;
                            CommandMenuGui.portalSelected = 0;
                            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        } else {
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        }
                        return;
                    }
              } /* else {
                    // Attacks Submenu
                    if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                        if (!this.attackCommands.isEmpty() && !STATS.getRecharge()) {
                            CommandMenuGui.submenu = CommandMenuGui.SUB_ATTACKS;
                            CommandMenuGui.attackSelected = 0;
                            world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                        } else {
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                            world.playSound(player, player.getPosition(), ModSounds.error, SoundCategory.MASTER, 1.0f, 1.0f);
                        }
                        return;
                    }
                }

                if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
                    PacketDispatcher.sendToServer(new MagicWisdomShot());
                }*/
                break;
            case CommandMenuGui.MAGIC: //Accessing MAGIC submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    if (!playerData.getRecharge() && playerData.getMagicCooldownTicks() <= 0 && playerData.getMaxMP() > 0 && (!this.magicList.isEmpty() && !playerData.getMagicsMap().isEmpty() && (!playerData.getActiveDriveForm().equals(Strings.Form_Valor) && !playerData.getActiveDriveForm().equals(Strings.Form_Anti)))) {
                        //CommandMenuGui.magicSelected = 0;
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
                        mc.level.playSound(mc.player, mc.player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        return;
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                    }
                }
                break;

            case CommandMenuGui.ITEMS: //Accessing ITEMS submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    if (!this.itemsList.isEmpty()) {
                        CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
                        CommandMenuGui.itemSelected = 0;
                        world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_select.get(), SoundSource.MASTER, 1.0f, 1.0f);
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                    }
                    return;
                }
                break;

            case CommandMenuGui.DRIVE: //Accessing DRIVE submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                	if(playerData.getAlignment() == OrgMember.NONE) {
	                	if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {//DRIVE
	                        
	                        if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {// && !player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) {//If is in antiform
	                        	
	                        } else { //If is in a drive form other than antiform
	                        	if(!driveFormsMap.isEmpty() && playerData.getDP() >= Utils.getMinimumDPForDrive(playerData)) {
	                                CommandMenuGui.driveSelected = 0;
	                                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
	                                mc.level.playSound(mc.player, mc.player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                                return;
	                        	} else {
	                                CommandMenuGui.selected = CommandMenuGui.ATTACK;
	                                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                        	}
	                        }
	                	} else {//REVERT
	                		
	                		if(playerData.getActiveDriveForm().equals(Strings.Form_Anti) && !playerData.isAbilityEquipped(Strings.darkDomination) && EntityEvents.isHostiles) {
	                			player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                		} else {
			                	PacketHandler.sendToServer(new CSUseDriveFormPacket(DriveForm.NONE.toString()));
			            		player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                		}
						}
					} else { // Org member Limits
						// Accessing Limits Submenu
                		if(!limitsList.isEmpty() && playerData.getLimitCooldownTicks() <= 0 && playerData.getDP() >= Utils.getMinimumDPForLimit(player)) {
							CommandMenuGui.limitSelected = 0;
							CommandMenuGui.submenu = CommandMenuGui.SUB_LIMIT;
							mc.level.playSound(mc.player, mc.player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
							return;
						} else {
	                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                			player.level.playSound(player, player.blockPosition(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}

					}

                }
                break;
        }
        // Attacks Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ATTACK && CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            /*if (this.attackCommands.isEmpty()) {
            } else {
                // ModDriveForms.getDriveForm(player, world, (String)
                // this.driveCommands.get(CommandMenuGui.driveselected));
                if (!player.getCapability(ModCapabilities.PLAYER_STATS, null).getRecharge()) {
                    Ability ability = this.attackCommands.get((byte) CommandMenuGui.attackSelected);
                    // UseAbility
                    useAttack(player, ability);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.getPosition(), ModSounds.select, SoundCategory.MASTER, 1.0f, 1.0f);
                }
            }*/
        }

        // Portal Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ATTACK && CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            if (this.portalCommands.isEmpty()) {
            } else {
                // ModDriveForms.getDriveForm(player, world, (String)
                // this.driveCommands.get(CommandMenuGui.driveselected));
                if (!ModCapabilities.getPlayer(player).getRecharge()) {
                    UUID portalUUID = this.portalCommands.get((byte) CommandMenuGui.portalSelected);
                    PortalData coords = worldData.getPortalFromUUID(portalUUID); 
                    if (!coords.getPos().equals(new BlockPos(0,0,0))) { //If the portal is not default coords
                        summonPortal(player, coords);
                    } else {
                        player.sendSystemMessage(Component.translatable(ChatFormatting.RED + "You don't have any portal destination"));
                    }

                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
                }
            }
        }

       
        
     // Limits Submenu
        if (CommandMenuGui.selected == CommandMenuGui.DRIVE && CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
			if (this.limitsList.isEmpty()) {
                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
			} else {
				
				if(playerData.getDP() < limitsList.get(CommandMenuGui.limitSelected).getCost()) {
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
				} else {
					if(lockOn != null)
						PacketHandler.sendToServer(new CSUseLimitPacket(lockOn, CommandMenuGui.limitSelected));
					else
						PacketHandler.sendToServer(new CSUseLimitPacket(CommandMenuGui.limitSelected));
					CommandMenuGui.selected = CommandMenuGui.ATTACK;
					CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
					world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			}
		}

        //Items Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ITEMS && CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            if (this.itemsList.isEmpty()) {
            } else if (!this.itemsList.isEmpty()) {
            	int slot = -1;
            	int i = 0;
            	for(Map.Entry<Integer, ItemStack> entry : itemsList.entrySet()) {
            		if(CommandMenuGui.itemSelected == i) {
            			slot = entry.getKey();
            		}
            		i++;
            	}

            	if(itemsList.get(slot) != null && itemsList.get(slot).getItem() instanceof KKPotionItem) {
            		KKPotionItem potion = (KKPotionItem) itemsList.get(slot).getItem();
            		//potion.potionEffect(player);
        			Party party = worldData.getPartyFromMember(player.getUUID());

            		if(potion.isGlobal() || party == null) {
            			PacketHandler.sendToServer(new CSUseItemPacket(slot));
            		} else {
            			//Target selector
            			CommandMenuGui.targetSelected = party.getMemberIndex(player.getUUID());
                        CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
    	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        return;
            		}
            		CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_select.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	} else {
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	}
               
            }
        }
        
        //Drive Submenu
        if (CommandMenuGui.selected == CommandMenuGui.DRIVE && CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (!this.driveFormsMap.isEmpty() && playerData.getAlignment() == OrgMember.NONE) {
            	String formName = (String) driveFormsMap.keySet().toArray()[CommandMenuGui.driveSelected];
            	DriveForm driveForm = ModDriveForms.registry.get().getValue(new ResourceLocation(formName));
            	if (playerData.getDP() >= driveForm.getDriveCost()) {
	                if (formName.equals(Strings.Form_Final)) {
	                    //driveForm.initDrive(player);
	                	PacketHandler.sendToServer(new CSUseDriveFormPacket(formName));
	            		player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.drive.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                } else {
	                    if (!antiFormCheck()) {
		                	PacketHandler.sendToServer(new CSUseDriveFormPacket(formName));
		            		player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.drive.get(), SoundSource.MASTER, 1.0f, 1.0f);
	                    }
	                }
	                CommandMenuGui.selected = CommandMenuGui.ATTACK;
	                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	 } else {
 	                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                     CommandMenuGui.selected = CommandMenuGui.ATTACK;
                     world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	}
            }
        }
        
      //Magic Target Selector Submenu
        if (CommandMenuGui.selected == CommandMenuGui.MAGIC && CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (this.targetsList.isEmpty()) {
            } else {
            	Member member = targetsList.get(CommandMenuGui.targetSelected);
            	if(world.getPlayerByUUID(member.getUUID()) != null && player.distanceTo(world.getPlayerByUUID(member.getUUID())) < ModConfigs.partyRangeLimit) {
            		String magicName = this.magicList.get(CommandMenuGui.magicSelected);
            		int level = playerData.getMagicLevel(new ResourceLocation(magicName));
            		PacketHandler.sendToServer(new CSUseMagicPacket(magicName, member.getUsername(), level));
                	CommandMenuGui.selected = CommandMenuGui.ATTACK;
                	CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	} else {
	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	}

            }
        }
        
      //Items Target Selector Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ITEMS && CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (this.targetsList.isEmpty()) {
            } else {
            	Member member = targetsList.get(CommandMenuGui.targetSelected);
            	if(world.getPlayerByUUID(member.getUUID()) != null && player.distanceTo(world.getPlayerByUUID(member.getUUID())) < ModConfigs.partyRangeLimit) {
            		int slot = -1;
                	int i = 0;
                	for(Map.Entry<Integer, ItemStack> entry : itemsList.entrySet()) {
                		if(CommandMenuGui.itemSelected == i) {
                			slot = entry.getKey();
                		}
                		i++;
                	}

                	if(itemsList.get(slot) != null && itemsList.get(slot).getItem() instanceof KKPotionItem) {
                		KKPotionItem potion = (KKPotionItem) itemsList.get(slot).getItem();
                		PacketHandler.sendToServer(new CSUseItemPacket(slot, member.getUsername()));
                	}
            		CommandMenuGui.selected = CommandMenuGui.ATTACK;
                	CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	} else {
	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
            	}

            }
        }
        
        // Magic Submenu
        if (CommandMenuGui.selected == CommandMenuGui.MAGIC && CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (this.magicList.isEmpty()) {
            } else {
				String magic = this.magicList.get(CommandMenuGui.magicSelected);
				int[] mag = playerData.getMagicsMap().get(magic);
				double cost = ModMagic.registry.get().getValue(new ResourceLocation(magic)).getCost(mag[0], player);

            	if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.error.get(), SoundSource.MASTER, 1.0f, 1.0f);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            	} else {
            		if(worldData.getPartyFromMember(player.getUUID()) != null && ModMagic.registry.get().getValue(new ResourceLocation(magic)).getHasToSelect()) { //Open party target selector
            			Party party = worldData.getPartyFromMember(player.getUUID());
                        CommandMenuGui.targetSelected = party.getMemberIndex(player.getUUID());
                        CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
    	                world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
                        return;
            		} else { //Cast Magic
                		String magicName = this.magicList.get(CommandMenuGui.magicSelected);
                		int level = playerData.getMagicLevel(new ResourceLocation(magicName));
            			PacketHandler.sendToServer(new CSUseMagicPacket(magicName, level, lockOn));
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            		}
                    world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_select.get(), SoundSource.MASTER, 1.0f, 1.0f);
    			}
            }
        }
    }

    private void summonPortal(Player player, PortalData coords) {
		BlockPos destination = coords.getPos();

		if (player.isShiftKeyDown()) {
			PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(player.blockPosition(), destination, coords.getDimID()));
		} else {
			HitResult rtr = getMouseOverExtended(100);
			if (rtr != null) {
				if(rtr instanceof BlockHitResult) {
					BlockHitResult brtr = (BlockHitResult)rtr;
					double distanceSq = player.distanceToSqr(brtr.getBlockPos().getX(), brtr.getBlockPos().getY(), brtr.getBlockPos().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(brtr.getBlockPos().above(), destination, coords.getDimID()));
					}
				} else if(rtr instanceof EntityHitResult) {
					EntityHitResult ertr = (EntityHitResult)rtr;
					double distanceSq = player.distanceToSqr(ertr.getEntity().getX(), ertr.getEntity().getY(), ertr.getEntity().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(ertr.getEntity().blockPosition(), destination, coords.getDimID()));
					} 
				}
			}
		}
	}

	public void commandBack() {
    	Minecraft mc = Minecraft.getInstance();
    	mc.level.playSound(mc.player, mc.player.position().x(),mc.player.position().y(),mc.player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        Player player = mc.player;
        Level world = mc.level;

        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN)
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            CommandMenuGui.submenu = CommandMenuGui.selected == CommandMenuGui.MAGIC ? CommandMenuGui.SUB_MAGIC : CommandMenuGui.ITEMS;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
        }
        //CommandMenuGui.magicSelected = 0;
        CommandMenuGui.driveSelected = 0;

        // GuiHelper.openTutorial(Tutorials.TUTORIAL_SOA_1);

    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

		Keybinds key = getPressedKey();
        if (player != null) {
            if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 320 && event.getKey() < 330) {
                IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    			IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
    			if (globalData != null && globalData.getStoppedTicks() <= 0) {
    				if (playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
                        PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 321, InputHandler.lockOn));
                    }    		
    			}                
            }

            if (KeyboardHelper.isScrollActivatorDown() && event.getKey() > 48 && event.getKey() < 58) {
                IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    			IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
    			if (globalData != null && globalData.getStoppedTicks() <= 0) {
	                if (playerData.getMagicCooldownTicks() <= 0 && !playerData.getRecharge() && !playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
	                    PacketHandler.sendToServer(new CSUseShortcutPacket(event.getKey() - 49, InputHandler.lockOn));
	                }
    			}
                return;
            }

            if (key != null) {
                switch (key) {
                    case OPENMENU:
                        PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
                        if (ModCapabilities.getPlayer(player).getSoAState() != SoAState.COMPLETE) {
                            if (player.level.dimension() != ModDimensions.DIVE_TO_THE_HEART) {
                                mc.setScreen(new NoChoiceMenuPopup());
                            }
                        } else {
                            GuiHelper.openMenu();
                        }
                        break;

                    case SHOW_GUI:
                        ModConfigs.toggleGui();
                        player.displayClientMessage(Component.translatable("message.kingdomkeys.gui_toggle", ModConfigs.showGuiToggle.toString()), true);
                        break;

                    case SCROLL_UP:
                        // if (!MainConfig.displayGUI())
                        // break;
                        if (mc.screen == null)
                            commandUp();
                        break;

                    case SCROLL_DOWN:
                        // if (!MainConfig.displayGUI())
                        // break;
                        if (mc.screen == null)
                            commandDown();
                        break;

                    case ENTER:
                        /*
                         * if (!MainConfig.displayGUI()) break;
                         */
                        if (mc.screen == null)
                            commandEnter();

                        break;

                    case BACK:
                        // if (!MainConfig.displayGUI())
                        // break;
                        if (mc.screen == null)
                            commandBack();

                        break;

                    case SUMMON_KEYBLADE:
                        if (ModCapabilities.getPlayer(player).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
                            PacketHandler.sendToServer(new CSSummonKeyblade());
                        } else {
                            PacketHandler.sendToServer(new CSSummonKeyblade(new ResourceLocation(ModCapabilities.getPlayer(player).getActiveDriveForm())));
                        }
                        
                        if(ModConfigs.summonTogether)
                            PacketHandler.sendToServer(new CSSummonArmor());

                        break;
                        
                    case SUMMON_ARMOR:
                        PacketHandler.sendToServer(new CSSummonArmor());
                        break;
                        
                    case ACTION:
                        commandAction();
                        break;

                    case LOCK_ON:
                        if (lockOn == null) {
                            int reach = 35;
                            HitResult rtr = getMouseOverExtended(reach);
                            if (rtr != null && rtr instanceof EntityHitResult) {
                                EntityHitResult ertr = (EntityHitResult) rtr;
                                if (ertr.getEntity() != null) {
                                    double distance = player.distanceTo(ertr.getEntity());

                                    if (reach >= distance) {
                                        if (ertr.getEntity() instanceof LivingEntity && !(ertr.getEntity() instanceof SpawningOrbEntity)) {
                                            lockOn = (LivingEntity) ertr.getEntity();
                                            player.level.playSound((Player) player, player.position().x(),player.position().y(),player.position().z(), ModSounds.lockon.get(), SoundSource.MASTER, 1.0f, 1.0f);
                                        }
                                    }
                                }
                            }
                        } else {
                            lockOn = null;
                        }
                        break;

                    case REACTION_COMMAND:
                        reactionCommand();
                        break;
                }
            }
        }
	}

	private void commandAction() {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		
    	if (qrCooldown <= 0 && (player.getDeltaMovement().x != 0 && player.getDeltaMovement().z != 0)) { // If player is moving do dodge roll / quick run
			if (player.isSprinting()) { //If player is sprinting do quick run
				if (playerData.isAbilityEquipped(Strings.quickRun) || playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
					float yaw = player.getYRot();
					float motionX = -Mth.sin(yaw / 180.0f * (float) Math.PI);
					float motionZ = Mth.cos(yaw / 180.0f * (float) Math.PI);

					int wisdomLevel = playerData.getDriveFormLevel(Strings.Form_Wisdom);

					double power = 0;
					// Wisdom Form
					if (playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						power = Constants.WISDOM_QR[wisdomLevel];
						if (!player.isOnGround()) {
							player.push(motionX * power / 2, 0, motionZ * power / 2);
							qrCooldown = 20;
						}
					} else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) { //Base
						if (wisdomLevel > 2) {
							power = Constants.WISDOM_QR[wisdomLevel - 2];
						}
					}

					if (player.isOnGround()) {
						player.push(motionX * power, 0, motionZ * power);
						qrCooldown = 20;
					}
				}
			} else { //If player is moving without sprinting do dodge roll
				if (playerData.isAbilityEquipped(Strings.dodgeRoll) || playerData.getActiveDriveForm().equals(Strings.Form_Limit)) {
					int limitLevel = playerData.getDriveFormLevel(Strings.Form_Limit);
					double power = 0;
					if (playerData.getActiveDriveForm().equals(Strings.Form_Limit)) {
						power = Constants.LIMIT_DR[limitLevel];
					} else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {//Base
						if (limitLevel > 2) {
							power = Constants.LIMIT_DR[limitLevel - 2];
						}
					}

					if (player.isOnGround()) {
						player.push(player.getDeltaMovement().x * power, 0, player.getDeltaMovement().z * power);
						qrCooldown = 20;
						//PacketDispatcher.sendToServer(new InvinciblePacket(20));
					}
				}
			}
		} else { // If player is not moving do guard
			/*if (ABILITIES.getEquippedAbility(ModAbilities.guard)) {
				if (player.getHeldItemMainhand() != null) {
					// If the player holds a weapon
					if (player.getHeldItemMainhand().getItem() instanceof ItemKeyblade || player.getHeldItemMainhand().getItem() instanceof IOrgWeapon) {
						PacketDispatcher.sendToServer(new InvinciblePacket(20));
					}
				}
			}*/
		}
    }

	private Keybinds getPressedKey() {
        for (Keybinds key : Keybinds.values())
            if (key.isPressed())
                return key;
        return null;
    }

	
    @SubscribeEvent
    public void handleMouseInputEvent(InputEvent.MouseButton.Pre event) {
    	Minecraft mc = Minecraft.getInstance();
    	if(mc.level != null){
	        if (event.getButton() == Constants.LEFT_MOUSE && event.getAction() == 1) {
	        	if(KeyboardHelper.isScrollActivatorDown()) {
	        		commandEnter();
		            event.setCanceled(true);
	        	} else if(mc.screen == null){
	        		Player thePlayer = mc.player;
					if (thePlayer != null) {
						ItemStack itemstack = thePlayer.getMainHandItem();
						if (itemstack != null) {
							IExtendedReach ieri = itemstack.getItem() instanceof IExtendedReach ? (IExtendedReach) itemstack.getItem() : null; 
							if (ieri != null) {
								float reach = ieri.getReach();
								HitResult rtr = getMouseOverExtended(reach);
								if (rtr != null) {
									if (rtr instanceof EntityHitResult) {
										EntityHitResult ertr = (EntityHitResult) rtr;
										if (ertr.getEntity() != null && ertr.getEntity().invulnerableTime == 0) {
											if (ertr.getEntity() != thePlayer) {
												if(!ertr.getEntity().getPassengers().contains(thePlayer)) {
													PacketHandler.sendToServer(new CSExtendedReach(ertr.getEntity().getId()));
												}
											}
										}
									}
								}
							}
						}
					}
	        	}
	        }
	        
	        if (event.getButton() == Constants.MIDDLE_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.getAction() == 1) {
	            commandSwapReaction();
	            event.setCanceled(true);
	        }
	
	        if (event.getButton() == Constants.RIGHT_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.getAction() == 1) {
	            commandBack();
	            event.setCanceled(true);
	        }
		}
	}

	private void commandSwapReaction() {
		loadLists();
		if (this.reactionList != null && !this.reactionList.isEmpty()) {
			if (CommandMenuGui.reactionSelected < this.reactionList.size() - 1) {
				CommandMenuGui.reactionSelected++;
			} else {
				if (CommandMenuGui.reactionSelected >= this.reactionList.size() - 1)
					CommandMenuGui.reactionSelected = 0;
			}
		}
	}
    
    private void reactionCommand() {
    	loadLists();
    	if(!reactionList.isEmpty()) {
    		Minecraft mc = Minecraft.getInstance();
    		Player player = mc.player;
			PacketHandler.sendToServer(new CSUseReactionCommandPacket(CommandMenuGui.reactionSelected, InputHandler.lockOn));
			CommandMenuGui.reactionSelected = 0;
			player.level.playSound(player, player.position().x(),player.position().y(),player.position().z(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
		}
	}

	@SubscribeEvent
    public void OnMouseWheelScroll(MouseScrollingEvent event) {
    	Minecraft mc = Minecraft.getInstance();
        if (mc.isWindowActive() && KeyboardHelper.isScrollActivatorDown()) {
        	event.setCanceled(true);
        	if(event.getScrollDelta() == Constants.WHEEL_DOWN) {
                commandDown();
        	}else if(event.getScrollDelta() == Constants.WHEEL_UP) {
                commandUp();
        	}
        	return;
        }
    }

    public enum Keybinds {
        OPENMENU("key.kingdomkeys.openmenu", GLFW.GLFW_KEY_M),
        SCROLL_UP("key.kingdomkeys.scrollup",GLFW.GLFW_KEY_UP),
        SCROLL_DOWN("key.kingdomkeys.scrolldown", GLFW.GLFW_KEY_DOWN),
        ENTER("key.kingdomkeys.enter",GLFW.GLFW_KEY_RIGHT),
        BACK("key.kingdomkeys.back", GLFW.GLFW_KEY_LEFT),
        SCROLL_ACTIVATOR("key.kingdomkeys.scrollactivator",GLFW.GLFW_KEY_LEFT_ALT),
        SUMMON_KEYBLADE("key.kingdomkeys.summonkeyblade", GLFW.GLFW_KEY_G),
        LOCK_ON("key.kingdomkeys.lockon",GLFW.GLFW_KEY_Z),
        SHOW_GUI("key.kingdomkeys.showgui", GLFW.GLFW_KEY_O),
        ACTION("key.kingdomkeys.action",GLFW.GLFW_KEY_X),
        SUMMON_ARMOR("key.kingdomkeys.summonarmor",GLFW.GLFW_KEY_H),
    	REACTION_COMMAND("key.kingdomkeys.reactioncommand", GLFW.GLFW_KEY_R);

        private final KeyMapping keybinding;
        public final String translationKey;
        Keybinds(String name, int defaultKey) {
            keybinding = new KeyMapping(name, defaultKey, "key.categories.kingdomkeys");
            translationKey = name;
        }

        public KeyMapping getKeybind() {
            return keybinding;
        }

        public boolean isPressed() {
            return keybinding.consumeClick();
        }
    }
    
    public static HitResult getMouseOverExtended(float dist) {
		Minecraft mc = Minecraft.getInstance();
		Entity theRenderViewEntity = mc.getCameraEntity();
		AABB theViewBoundingBox = new AABB(theRenderViewEntity.getX() - 0.5D, theRenderViewEntity.getY() - 0.0D, theRenderViewEntity.getZ() - 0.5D, theRenderViewEntity.getX() + 0.5D, theRenderViewEntity.getY() + 1.5D, theRenderViewEntity.getZ() + 0.5D);
		HitResult returnMOP = null;
		if (mc.level != null) {
			double var2 = dist;
			returnMOP = theRenderViewEntity.pick(var2, 0, false);
			double calcdist = var2;
			Vec3 pos = theRenderViewEntity.getEyePosition(0);
			var2 = calcdist;
			if (returnMOP != null) {
				calcdist = returnMOP.getLocation().distanceTo(pos);
			}

			Vec3 lookvec = theRenderViewEntity.getViewVector(0);
			Vec3 var8 = pos.add(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
			Entity pointedEntity = null;
			float var9 = 1.0F;

			List<Entity> list = mc.level.getEntities(theRenderViewEntity, theViewBoundingBox.inflate(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).inflate(var9, var9, var9));
			double d = calcdist;

			for (Entity entity : list) {
				if (entity.isPickable()) {
					float bordersize = entity.getPickRadius();
					AABB aabb = new AABB(entity.getX() - entity.getBbWidth() / 2, entity.getY(), entity.getZ() - entity.getBbWidth() / 2, entity.getX() + entity.getBbWidth() / 2, entity.getY() + entity.getBbHeight(), entity.getZ() + entity.getBbWidth() / 2);
					aabb.inflate(bordersize, bordersize, bordersize);
					Optional<Vec3> mop0 = aabb.clip(pos, var8);

					if (aabb.contains(pos)) {
						if (0.0D < d || d == 0.0D) {
							pointedEntity = entity;
							d = 0.0D;
						}
					} else if (mop0 != null && mop0.isPresent()) {
						double d1 = pos.distanceTo(mop0.get());

						if (d1 < d || d == 0.0D) {
							pointedEntity = entity;
							d = d1;
						}
					}
				}
			}

			if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
				returnMOP = new EntityHitResult(pointedEntity);
			}
		}
		return returnMOP;
	}


    public void loadLists() {
        Minecraft mc = Minecraft.getInstance();
        IWorldCapabilities worldData = ModCapabilities.getWorld(mc.level);
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);

        if(playerData != null && worldData != null) {
	        this.driveFormsMap = Utils.getSortedDriveForms(playerData.getDriveFormMap());
	        if(!playerData.isAbilityEquipped(Strings.darkDomination)) {
	        	this.driveFormsMap.remove(Strings.Form_Anti);
			}
	        this.driveFormsMap.remove(DriveForm.NONE.toString());
	        this.driveFormsMap.remove(DriveForm.SYNCH_BLADE.toString());
	        //this.magicsMap = Utils.getSortedMagics(playerData.getMagicsMap());
	        this.portalCommands = worldData.getAllPortalsFromOwnerID(mc.player.getUUID());
	        this.magicList = ModConfigs.magicDisplayedInCommandMenu.stream().filter(magic -> playerData.getMagicsMap().containsKey(magic)).toList();
			this.limitsList = Utils.getSortedLimits(Utils.getPlayerLimitAttacks(mc.player));
			
	        if(ModCapabilities.getWorld(mc.level).getPartyFromMember(mc.player.getUUID()) != null) {
	        	this.targetsList = ModCapabilities.getWorld(mc.level).getPartyFromMember(mc.player.getUUID()).getMembers();
	        }
	        this.itemsList = Utils.getEquippedItems(playerData.getEquippedItems());
	        
	        this.reactionList = playerData.getReactionCommands();
        }
    }
}
