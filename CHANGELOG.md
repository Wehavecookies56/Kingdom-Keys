## Added:
* Reworked the player Heartless and Nobody mechanic:
  * When killed by a heartless you will then become a Shadow and your heart will fly away from you, it reduces your STR, MAG and DEF stats by a %.
  * You must chase it to return to your normal self.
  * As a shadow you cannot use the Command Menu or consume items like food.
  * The next Dusk that spawns nearby will become your Nobody, defeating it will drop a "Memory from another self" item, which will allow you to restore your full power.
* Text box in the config menu to set your nobody name.
* Search bar for filtered screens.
* Amount text box to buy screen.
* ServerConfig to set the distance the heart will travel from the turning point (default 128).
* ServerConfig to set the lock on range (previously hardcoded to 35 blocks).
* ServerConfig to set the xp level curve factor, higher number the less steep is the xp increase.
* ServerConfig to set the % of lost attributes when becoming a heartless.
* ServerConfig to set the minutes that can pass when someone's nobody can be in non-loaded chunks, therefore despawning them and adding them to the queue again.
* ClientConfig to hide the second layer of the skin when wearing clothes/armor to prevent clipping or z-fighting.
* Command /kingdomkeys heartless <turn/restore> to become a Shadow or back to Human.
* MiniCO as a proper structure (/locate and /place works).
* KH1 Scan HP Bar.
* Config menu option to hide the "HP" label and enemy name.
* Some checks to ensure players can't end up in wrong parties and cleanup of empty parties.
## Changed:
* Leveling paths now can go past lvl 100 if a datapack implements them.
* Removed mobLevelName config as now they no longer use the "Custom Name" to display it.
* Keyblade Forge can now give multiple abilities per level.
* All drive forms besides limit can no longer guard.
* Robes and Organization armor are now tighter to the player's body, fixing the issue where second skin layer would clip on the legs.
* All boolean buttons in the Config Menu are now checkboxes.
* Casting magic from the Command Menu now goes through the same server checks as the shortcuts (except MP Safety).
* Unified all projectiles code.
* Damage numbers now move smoothly while they float up.
## Fixed:
* Second Chance / Once More surviving /kill or void.
* Tracking of a player level for the party screen when they are online.
* Multiblocks like Bubble Helm or Gummi weapons reporting the wrong amount on lists and hologram.
* Items in stock screen being selectable.
* Missing Spanish translation lines.
* Leveled mobs healing back to full health when their chunk reloaded.
* Leveling up also leveling up and healing other players' tamed animals nearby.
* Treasure Magnet not pulling more items after a newly dropped one was nearby.
* Airstep timing being shared between all players.
* Stop's damage being assigned to a player name rather than the player's UUID.
* Party changes (joining, leaving, promoting and settings) not being saved instantly.
* Empty parties staying in the save forever after their leader left (existing ones are discarded when the world loads).
* Players being added twice to the same party, or joining a second party while already in one.
* Heartless players being able to cast magic through shortcuts.
* Player data not being sent to the client right after respawning.
* Keyblade tooltip will no longer show the vanilla value.
* Keyblade and Organization weapon tooltips showing 0.5 more strength than they deal without sharpness.
* Various magic projectile issues:
  * Firaga's explosion not damaging anything around it.
  * Firaza's explosion being centred on the caster instead of where it landed.
  * Watera, Waterga, Waterza and Blizzaza hitting their direct target twice.
  * Ultima Cannon's explosion dealing almost no damage to anything but the target.
  * Organization weapons, shotlocks and Gummi shots hurting party members with friendly fire off.
  * Thunder, Thundara, Thundaga and Thundaza never striking the locked-on target, only those around it.
  * Water, Watera and Waterga's spinning shield hurting allies.
  * Ice Spike hurting allies.
  * Ice Barrage and the shotlock minigame aiming at party members.
  * Spell, Organization weapon and shotlock projectiles jittering while they fly.
* A spell's cooldown bar not showing in the Command Menu when the spell right above it was selected (individual cooldowns only).
* Station of Sorrow Core block having no model.
* Recipe tier pages in the journal showing recipes that do not exist.
* Removed leftover files that were being loaded and logged as errors.
* Command registration no longer logs warnings.