package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

public enum SoAState {
    NONE((byte)0), CHOICE((byte)1), SACRIFICE((byte)2), CONFIRM((byte)3), COMPLETE((byte)4), WARRIOR((byte)5), GUARDIAN((byte)6), MYSTIC((byte)7);

    private final byte b;
    SoAState(byte b) {
        this.b = b;
    }
    public byte get() {
        return b;
    }

    private boolean Compare(byte b) { return this.b == b; }

    public static SoAState fromByte(byte b) {
        SoAState[] values = SoAState.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].Compare(b)) {
                return values[i];
            }
        }
        return NONE;
    }

    //TODO make choices more substantial
    public static void applyStatsForChoices(Player player, IPlayerCapabilities playerData, boolean remove) {
        if (playerData.getSoAState() == COMPLETE) {
            SoAState choice = !remove ? playerData.getChosen() : playerData.getSacrificed();
            SoAState sacrifice = !remove ? playerData.getSacrificed() : playerData.getChosen(); //If removing sacrifice is the old choice
            
            if (remove) {
    			System.out.println("Removing old choice? "+sacrifice);
    			removeNonStatsData(ModLevels.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID+":"+ sacrifice.toString().toLowerCase())), playerData);
        		System.out.println(playerData.getAbilityMap());
            	
                playerData.setSoAState(NONE);
            } else {
	            Level levelData = ModLevels.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID+":"+ choice.toString().toLowerCase()));
	        	if (levelData.getStr(1) > 0) {
	        		playerData.addStrength(levelData.getStr(1));
	    		}
	    		if (levelData.getMag(1) > 0) {
	    			playerData.addMagic(levelData.getMag(1));
	    		}
	    		if (levelData.getDef(1) > 0) {
	    			playerData.addDefense(levelData.getDef(1));
	    		}
	    		if (levelData.getAP(1) > 0) {
	    			playerData.addMaxAP(levelData.getAP(1));
	    		}
	    		if (levelData.getMaxHp(1) > 0) {
	    			playerData.addMaxHP(levelData.getMaxHp(1));
	    		}
	    		if (levelData.getMaxMp(1) > 0) {
	    			playerData.addMaxMP(levelData.getMaxMp(1));
	    		}
	    		if (levelData.getAbilities(1).length > 0) {
	    			for (String ability : levelData.getAbilities(1)) {
	    				if (ability != null) {
	    					Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(ability));
	    					if (a != null) {
	    						playerData.addAbility(ability, true);
	    					}
	    				}
	    			}
	    		}    		
	    		if (levelData.getShotlocks(1).length > 0) {
	    			for (String shotlock : levelData.getShotlocks(1)) {
	    				if (shotlock != null) {
	    					Shotlock a = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
	    					if (a != null) {
	    						playerData.addShotlockToList(shotlock, true);
	    					}
	    				}
	    			}
	    		}
	    		if (levelData.getSpells(1).length > 0) {
	    			for (String magic : levelData.getSpells(1)) {
	    				if (magic != null) {
	    					Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
	    					if (magicInstance != null) {
	    						if (playerData != null && playerData.getMagicsMap() != null) {
	    							if (!playerData.getMagicsMap().containsKey(magic)) {
	    								playerData.setMagicLevel(magic, playerData.getMagicLevel(magic), true);
	    							} else {
	    								playerData.setMagicLevel(magic, playerData.getMagicLevel(magic)+1, true);
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
	    		
	    		//Sacrifice will invert the given stats
	    		Level sacrificeData = ModLevels.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID+":"+ sacrifice.toString().toLowerCase()));
	        	if (sacrificeData.getStr(1) > 0) {
	        		playerData.setStrength(playerData.getStrength(false)-sacrificeData.getStr(1));
	    		}
	    		if (sacrificeData.getMag(1) > 0) {
	    			playerData.setMagic(playerData.getMagic(false)-sacrificeData.getMag(1));
	    		}
	    		if (sacrificeData.getDef(1) > 0) {
	    			playerData.setDefense(playerData.getDefense(false)-sacrificeData.getDef(1));
	    		}
	    		if (sacrificeData.getAP(1) > 0) {
	    			playerData.setMaxAP(playerData.getMaxAP(false)-sacrificeData.getAP(1));
	    		}
	    		if (sacrificeData.getMaxHp(1) > 0) {
	    			playerData.setMaxHP(playerData.getMaxHP()-sacrificeData.getMaxHp(1));
	    		}
	    		if (sacrificeData.getMaxMp(1) > 0) {
	    			playerData.setMaxMP(playerData.getMaxMP()-sacrificeData.getMaxMp(1));
	    		}
	    		System.out.println("REMOTE: "+player.level.isClientSide);
	    		//Remove non choice lvl 1 abilities and stuff
	    		
            }
        }
    }
    
    public static void removeNonStatsData(Level levelData, IPlayerCapabilities playerData) {
    	if (levelData.getAbilities(1).length > 0) {
			for (String ability : levelData.getAbilities(1)) {
				if (ability != null) {
					Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(ability));
					System.out.println("  Found ability "+ability);
					if (a != null) {
						playerData.removeAbility(ability);
					}
				}
			}
		}    		
		if (levelData.getShotlocks(1).length > 0) {
			for (String shotlock : levelData.getShotlocks(1)) {
				if (shotlock != null) {
					Shotlock a = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
					if (a != null) {
						playerData.removeShotlockFromList(shotlock);
					}
				}
			}
		}
		if (levelData.getSpells(1).length > 0) {
			for (String magic : levelData.getSpells(1)) {
				if (magic != null) {
					Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
					if (magicInstance != null) {
						if (playerData != null && playerData.getMagicsMap() != null) {
							if (playerData.getMagicsMap().containsKey(magic)) {
								playerData.setMagicLevel(magic, playerData.getMagicLevel(magic)-1, true);
							}
						}
					}
				}
			}
		}
		
    }
}
