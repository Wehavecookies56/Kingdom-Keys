package online.kingdomkeys.kingdomkeys.handler;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.NoChoiceMenuPopup;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetDriveFormPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSpawnOrgPortalPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncAllClientDataPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSUseItemPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSUseLimitPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSUseMagicPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSUseReactionCommandPacket;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

public class InputHandler {

    List<UUID> portalCommands;
    Map<String, int[]> driveFormsMap;
    Map<String, Integer> magicsMap;
    List<Member> targetsList;
    List<Limit> limitsList;
    Map<Integer, ItemStack> itemsList;
    List<String> reactionList = new ArrayList<String>();
    
    public static LivingEntity lockOn = null;
    public static int qrCooldown = 40;

    public boolean antiFormCheck() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        World world = mc.world;
        double random = Math.random();
        int ap = playerData.getAntiPoints();
        //System.out.println("Antipoints: "+ap);
        int prob = 0;
        if (ap > 0 && ap <= 4)
            prob = 0;
        else if (ap > 4 && ap <= 9)
            prob = 10;
        else if (ap >= 10)
            prob = 25;

        if (random * 100 < prob) {
            PacketHandler.sendToServer(new CSSetDriveFormPacket(Strings.Form_Anti));
    		player.world.playSound(player, player.getPosition(), ModSounds.antidrive.get(), SoundCategory.MASTER, 1.0f, 1.0f);

            CommandMenuGui.selected = CommandMenuGui.ATTACK;
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_select.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            return true;
        } else
            return false;
    }

    public void commandUp() {
        Minecraft mc = Minecraft.getInstance();
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_move.get(), SoundCategory.MASTER, 1.0f, 1.0f);

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
                CommandMenuGui.magicSelected = this.magicsMap.size() - 1;
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
        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_move.get(), SoundCategory.MASTER, 1.0f, 1.0f);
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
            if (CommandMenuGui.magicSelected < this.magicsMap.size() - 1) {
                CommandMenuGui.magicSelected++;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
            } else if (CommandMenuGui.magicSelected >= this.magicsMap.size() - 1)
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
        PlayerEntity player = mc.player;
        World world = mc.world;
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
                            world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                        } else {
                            CommandMenuGui.selected = CommandMenuGui.ATTACK;
                            world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
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
                    if (!playerData.getRecharge() && playerData.getMagicCooldownTicks() <= 0 && playerData.getMaxMP() > 0 && (!this.magicsMap.isEmpty() && (!playerData.getActiveDriveForm().equals(Strings.Form_Valor) && !playerData.getActiveDriveForm().equals(Strings.Form_Anti)))) {
                        CommandMenuGui.magicSelected = 0;
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAGIC;
                        mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                        return;
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                    }
                }
                break;

            case CommandMenuGui.ITEMS: //Accessing ITEMS submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                    if (!this.itemsList.isEmpty()) {
                        CommandMenuGui.submenu = CommandMenuGui.SUB_ITEMS;
                        CommandMenuGui.itemSelected = 0;
                        world.playSound(player, player.getPosition(), ModSounds.menu_select.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                    } else {
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                    }
                    return;
                }
                break;

            case CommandMenuGui.DRIVE: //Accessing DRIVE submenu
                if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN) {
                	if(playerData.getAlignment() == OrgMember.NONE) {
	                	if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {//DRIVE
	                        //System.out.println("drive submenu");
	                        if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {// && !player.getCapability(ModCapabilities.CHEAT_MODE, null).getCheatMode()) {//If is in antiform
	                        	
	                        } else { //If is in a drive form other than antiform
	                        	if(!driveFormsMap.isEmpty()) {
	                                CommandMenuGui.driveSelected = 0;
	                                CommandMenuGui.submenu = CommandMenuGui.SUB_DRIVE;
	                                mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
	                                return;
	                        	}
	                        }
	                	} else {//REVERT
	                		//System.out.println("REVERT");
	                		if(playerData.getActiveDriveForm().equals(Strings.Form_Anti) && EntityEvents.isHostiles) {
	                			player.world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
	                		} else {
			                	PacketHandler.sendToServer(new CSSetDriveFormPacket(DriveForm.NONE.toString()));
			            		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
	                		}
						}
					} else { // Org member Limits
						// Accessing Limits Submenu
                		if(!limitsList.isEmpty() && playerData.getLimitCooldownTicks() <= 0) {
							CommandMenuGui.limitSelected = 0;
							CommandMenuGui.submenu = CommandMenuGui.SUB_LIMIT;
							mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
							return;
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
                        player.sendMessage(new TranslationTextComponent(TextFormatting.RED + "You don't have any portal destination"), Util.DUMMY_UUID);
                    }

                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                }
            }
        }

       
        
     // Limits Submenu
        if (CommandMenuGui.selected == CommandMenuGui.DRIVE && CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
			if (this.limitsList.isEmpty()) {
                world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                CommandMenuGui.selected = CommandMenuGui.ATTACK;
                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
			} else {
				//System.out.println(limitsList.get(CommandMenuGui.limitSelected));
				if(playerData.getDP() < limitsList.get(CommandMenuGui.limitSelected).getCost()) {
                    world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
				} else {
					if(lockOn != null)
						PacketHandler.sendToServer(new CSUseLimitPacket(lockOn, CommandMenuGui.limitSelected));
					else
						PacketHandler.sendToServer(new CSUseLimitPacket(CommandMenuGui.limitSelected));
					CommandMenuGui.selected = CommandMenuGui.ATTACK;
					CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
					world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
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
        			Party party = worldData.getPartyFromMember(player.getUniqueID());

            		if(potion.isGlobal() || party == null) {
            			PacketHandler.sendToServer(new CSUseItemPacket(slot));
            		} else {
            			//Target selector
            			CommandMenuGui.targetSelected = party.getMemberIndex(player.getUniqueID());
                        CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
    	                world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                        return;
            		}
            		CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                    world.playSound(player, player.getPosition(), ModSounds.menu_select.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	} else {
                    world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	}
               
            }
        }
        
        //Drive Submenu
        if (CommandMenuGui.selected == CommandMenuGui.DRIVE && CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            if (!this.driveFormsMap.isEmpty() && playerData.getAlignment() == OrgMember.NONE) {
            	String formName = (String) driveFormsMap.keySet().toArray()[CommandMenuGui.driveSelected];
            	DriveForm driveForm = ModDriveForms.registry.getValue(new ResourceLocation(formName));
            	if (playerData.getDP() >= driveForm.getDriveCost()) {
	                if (formName.equals(Strings.Form_Final)) {
	                    //driveForm.initDrive(player);
	                	PacketHandler.sendToServer(new CSSetDriveFormPacket(formName));
	            		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
	                } else {
	                    if (!antiFormCheck()) {
		                	PacketHandler.sendToServer(new CSSetDriveFormPacket(formName));
		            		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
	                    }
	                }
	                CommandMenuGui.selected = CommandMenuGui.ATTACK;
	                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
	                world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	 } else {
 	                CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
                     CommandMenuGui.selected = CommandMenuGui.ATTACK;
                     world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	}
            }
        }
        
      //Magic Target Selector Submenu
        if (CommandMenuGui.selected == CommandMenuGui.MAGIC && CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (this.targetsList.isEmpty()) {
            } else {
            	Member member = targetsList.get(CommandMenuGui.targetSelected);
            	if(world.getPlayerByUuid(member.getUUID()) != null && player.getDistance(world.getPlayerByUuid(member.getUUID())) < ModConfigs.partyRangeLimit) {
            		String magicName = (String) magicsMap.keySet().toArray()[CommandMenuGui.magicSelected];
            		int level = playerData.getMagicLevel(magicName);
            		PacketHandler.sendToServer(new CSUseMagicPacket(magicName, member.getUsername(), level));
                	CommandMenuGui.selected = CommandMenuGui.ATTACK;
                	CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
	                world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	} else {
	                world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	}

            }
        }
        
      //Items Target Selector Submenu
        if (CommandMenuGui.selected == CommandMenuGui.ITEMS && CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            if (this.targetsList.isEmpty()) {
            } else {
            	Member member = targetsList.get(CommandMenuGui.targetSelected);
            	if(world.getPlayerByUuid(member.getUUID()) != null && player.getDistance(world.getPlayerByUuid(member.getUUID())) < ModConfigs.partyRangeLimit) {
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
	                world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	} else {
	                world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
            	}

            }
        }
        
        // Magic Submenu
        if (CommandMenuGui.selected == CommandMenuGui.MAGIC && CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            if (this.magicsMap.isEmpty()) {
            } else {
				String magic = (String) magicsMap.keySet().toArray()[CommandMenuGui.magicSelected];
				int cost = ModMagic.registry.getValue(new ResourceLocation(magic)).getCost();

            	if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
                    world.playSound(player, player.getPosition(), ModSounds.error.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                    CommandMenuGui.selected = CommandMenuGui.ATTACK;
                    CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            	} else {
            		if(worldData.getPartyFromMember(player.getUniqueID()) != null && ModMagic.registry.getValue(new ResourceLocation(magic)).getHasToSelect()) { //Open party target selector
            			Party party = worldData.getPartyFromMember(player.getUniqueID());
                        CommandMenuGui.targetSelected = party.getMemberIndex(player.getUniqueID());
                        CommandMenuGui.submenu = CommandMenuGui.SUB_TARGET;
    	                world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
                        return;
            		} else {
                		String magicName = (String) magicsMap.keySet().toArray()[CommandMenuGui.magicSelected];
                		int level = playerData.getMagicLevel(magicName);
            			PacketHandler.sendToServer(new CSUseMagicPacket(magicName, level));
                        CommandMenuGui.selected = CommandMenuGui.ATTACK;
                        CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            		}
                    world.playSound(player, player.getPosition(), ModSounds.menu_select.get(), SoundCategory.MASTER, 1.0f, 1.0f);
    			}
            }
        }
    }

    private void summonPortal(PlayerEntity player, PortalData coords) {
		BlockPos destination = coords.getPos();

		if (player.isSneaking()) {
			PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(player.getPosition(), destination, coords.getDimID()));
		} else {
			RayTraceResult rtr = InputHandler.getMouseOverExtended(100);
			if (rtr != null) {
				if(rtr instanceof BlockRayTraceResult) {
					BlockRayTraceResult brtr = (BlockRayTraceResult)rtr;
					double distanceSq = player.getDistanceSq(brtr.getPos().getX(), brtr.getPos().getY(), brtr.getPos().getZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(brtr.getPos().up(), destination, coords.getDimID()));
					}
				} else if(rtr instanceof EntityRayTraceResult) {
					EntityRayTraceResult ertr = (EntityRayTraceResult)rtr;
					double distanceSq = player.getDistanceSq(ertr.getEntity().getPosX(), ertr.getEntity().getPosY(), ertr.getEntity().getPosZ());
					double reachSq = 100 * 100;
					if (reachSq >= distanceSq) {
						PacketHandler.sendToServer(new CSSpawnOrgPortalPacket(ertr.getEntity().getPosition(), destination, coords.getDimID()));
					} 
				}
			}
		}
	}

	public void commandBack() {
    	Minecraft mc = Minecraft.getInstance();
    	mc.world.playSound(mc.player, mc.player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        PlayerEntity player = mc.player;
        World world = mc.world;

        if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAIN)
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
        else if (CommandMenuGui.submenu == CommandMenuGui.SUB_MAGIC) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ITEMS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_DRIVE) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_PORTALS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_ATTACKS) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_TARGET) {
            CommandMenuGui.submenu = CommandMenuGui.selected == CommandMenuGui.MAGIC ? CommandMenuGui.SUB_MAGIC : CommandMenuGui.ITEMS;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        } else if (CommandMenuGui.submenu == CommandMenuGui.SUB_LIMIT) {
            CommandMenuGui.submenu = CommandMenuGui.SUB_MAIN;
            world.playSound(player, player.getPosition(), ModSounds.menu_back.get(), SoundCategory.MASTER, 1.0f, 1.0f);
        }
        //CommandMenuGui.magicSelected = 0;
        CommandMenuGui.driveSelected = 0;

        // GuiHelper.openTutorial(Tutorials.TUTORIAL_SOA_1);

    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;

		Keybinds key = getPressedKey();
		if (key != null) {
			switch (key) {
			case OPENMENU:
				PacketHandler.sendToServer(new CSSyncAllClientDataPacket());
				if (ModCapabilities.getPlayer(player).getSoAState() != SoAState.COMPLETE) {
					if (player.world.getDimensionKey() != ModDimensions.DIVE_TO_THE_HEART) {
						mc.displayGuiScreen(new NoChoiceMenuPopup());
					}
				} else {
					GuiHelper.openMenu();
				}
				break;

			/*
			 * case SHOW_GUI: MainConfig.toggleShowGUI(); break;
			 */

			case SCROLL_UP:
				// if (!MainConfig.displayGUI())
				// break;
				if (mc.currentScreen == null)
					commandUp();
				break;

			case SCROLL_DOWN:
				// if (!MainConfig.displayGUI())
				// break;
				if (mc.currentScreen == null)
					commandDown();
				break;

			case ENTER:
				/*
				 * if (!MainConfig.displayGUI()) break;
				 */
				if (mc.currentScreen == null)
					commandEnter();

				break;

			case BACK:
				// if (!MainConfig.displayGUI())
				// break;
				if (mc.currentScreen == null)
					commandBack();

				break;

			case SUMMON_KEYBLADE:
				if (ModCapabilities.getPlayer(player).getActiveDriveForm().equals(DriveForm.NONE.toString())) {
					PacketHandler.sendToServer(new CSSummonKeyblade());
				} else {
					PacketHandler.sendToServer(new CSSummonKeyblade(new ResourceLocation(ModCapabilities.getPlayer(player).getActiveDriveForm())));
				}
				break;/*
						 * case SCROLL_ACTIVATOR: break;
						 */
			case ACTION:
				commandAction();
				break;

			case LOCK_ON:
				if (lockOn == null) {
					int reach = 35;
					RayTraceResult rtr = getMouseOverExtended(reach);
					if (rtr != null && rtr instanceof EntityRayTraceResult) {
						EntityRayTraceResult ertr = (EntityRayTraceResult) rtr;
						if (ertr.getEntity() != null) {
							double distance = player.getDistance(ertr.getEntity());
							// System.out.println(distance);
							if (reach >= distance) {
								if (ertr.getEntity() instanceof LivingEntity) {
									lockOn = (LivingEntity) ertr.getEntity();
									player.world.playSound((PlayerEntity) player, player.getPosition(), ModSounds.lockon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
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

	private void commandAction() {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		
    	if (qrCooldown <= 0 && (player.getMotion().x != 0 && player.getMotion().z != 0)) { // If player is moving do dodge roll / quick run
			if (player.isSprinting()) { //If player is sprinting do quick run
				if (playerData.isAbilityEquipped(Strings.quickRun) || playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
					float yaw = player.rotationYaw;
					float motionX = -MathHelper.sin(yaw / 180.0f * (float) Math.PI);
					float motionZ = MathHelper.cos(yaw / 180.0f * (float) Math.PI);

					int wisdomLevel = playerData.getDriveFormLevel(Strings.Form_Wisdom);

					double power = 0;
					// Wisdom Form
					if (playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						power = Constants.WISDOM_QR[wisdomLevel];
						if (!player.isOnGround()) {
							player.addVelocity(motionX * power / 2, 0, motionZ * power / 2);
							qrCooldown = 20;
						}
					} else if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) { //Base
						if (wisdomLevel > 2) {
							power = Constants.WISDOM_QR[wisdomLevel - 2];
						}
					}

					if (player.isOnGround()) {
						player.addVelocity(motionX * power, 0, motionZ * power);
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
						player.addVelocity(player.getMotion().x * power, 0, player.getMotion().z * power);
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
    public void handleKeyInputEvent(InputEvent.RawMouseEvent event) {
        /*
         * if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
         * if (player.getCapability(ModCapabilities.DRIVE_STATE,
         * null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
         * event.setCanceled(true); } else { event.setCanceled(false); } }
         */

    	Minecraft mc = Minecraft.getInstance();
    	if(mc.world != null){
	        if (event.getButton() == Constants.LEFT_MOUSE && KeyboardHelper.isScrollActivatorDown() && event.getAction() == 1) {
	            commandEnter();
	            event.setCanceled(true);
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
    		PlayerEntity player = mc.player;
			PacketHandler.sendToServer(new CSUseReactionCommandPacket(CommandMenuGui.reactionSelected));
			CommandMenuGui.reactionSelected = 0;
			player.world.playSound(player, player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		}
	}

	@SubscribeEvent
    public void OnMouseWheelScroll(MouseScrollEvent event) {
    	Minecraft mc = Minecraft.getInstance();
        if (mc.isGameFocused() && KeyboardHelper.isScrollActivatorDown()) {
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
        //TEST("key.kingdomkeys.test",GLFW.GLFW_KEY_K);
    	REACTION_COMMAND("key.kingdomkeys.reactioncommand", GLFW.GLFW_KEY_R);

        private final KeyBinding keybinding;
        Keybinds(String name, int defaultKey) {
            keybinding = new KeyBinding(name, defaultKey, "key.categories.kingdomkeys");
        }

        public KeyBinding getKeybind() {
            return keybinding;
        }

        public boolean isPressed() {
            return keybinding.isPressed();
        }
    }
    
    public static RayTraceResult getMouseOverExtended(float dist) {
		Minecraft mc = Minecraft.getInstance();
		Entity theRenderViewEntity = mc.getRenderViewEntity();
		AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.getPosX() - 0.5D, theRenderViewEntity.getPosY() - 0.0D, theRenderViewEntity.getPosZ() - 0.5D, theRenderViewEntity.getPosX() + 0.5D, theRenderViewEntity.getPosY() + 1.5D, theRenderViewEntity.getPosZ() + 0.5D);
		RayTraceResult returnMOP = null;
		if (mc.world != null) {
			double var2 = dist;
			returnMOP = theRenderViewEntity.pick(var2, 0, false);
			double calcdist = var2;
			Vector3d pos = theRenderViewEntity.getEyePosition(0);
			var2 = calcdist;
			if (returnMOP != null) {
				calcdist = returnMOP.getHitVec().distanceTo(pos);
			}

			Vector3d lookvec = theRenderViewEntity.getLook(0);
			Vector3d var8 = pos.add(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
			Entity pointedEntity = null;
			float var9 = 1.0F;

			List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox.grow(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).grow(var9, var9, var9));
			double d = calcdist;

			for (Entity entity : list) {
				if (entity.canBeCollidedWith()) {
					float bordersize = entity.getCollisionBorderSize();
					AxisAlignedBB aabb = new AxisAlignedBB(entity.getPosX() - entity.getWidth() / 2, entity.getPosY(), entity.getPosZ() - entity.getWidth() / 2, entity.getPosX() + entity.getWidth() / 2, entity.getPosY() + entity.getHeight(), entity.getPosZ() + entity.getWidth() / 2);
					aabb.grow(bordersize, bordersize, bordersize);
					Optional<Vector3d> mop0 = aabb.rayTrace(pos, var8);

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
				returnMOP = new EntityRayTraceResult(pointedEntity);
			}
		}
		return returnMOP;
	}


    public void loadLists() {
        Minecraft mc = Minecraft.getInstance();
        IWorldCapabilities worldData = ModCapabilities.getWorld(mc.world);
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);

        if(playerData != null && worldData != null) {
	        this.driveFormsMap = Utils.getSortedDriveForms(playerData.getDriveFormMap());
	        this.driveFormsMap.remove(DriveForm.NONE.toString());
	        this.magicsMap = Utils.getSortedMAgics(playerData.getMagicsMap());
	        this.portalCommands = worldData.getAllPortalsFromOwnerID(mc.player.getUniqueID());
			this.limitsList = Utils.getPlayerLimitAttacks(mc.player);
	        if(ModCapabilities.getWorld(mc.world).getPartyFromMember(mc.player.getUniqueID()) != null) {
	        	this.targetsList = ModCapabilities.getWorld(mc.world).getPartyFromMember(mc.player.getUniqueID()).getMembers();
	        }
	        this.itemsList = Utils.getEquippedItems(playerData.getEquippedItems());
	        
	        this.reactionList = playerData.getReactionCommands();
        }
    }
}
