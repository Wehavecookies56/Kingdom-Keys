# 2.8.0c:
## Added:
* Shop datapack option to require having enough materials deposited or having obtained a magic spell (so it works like in KH2 and KH BBS respectively)
  * By default, to be able to buy shards you need to have obtained a total of 30, stones 25, gems 20 and crystals 15 (Taking materials out substract the total).

## Changed:
* Room modifiers are now customizable through JSON.

## Fixed:
* Shop not displaying the new items if you had just leveled up the synthesis tier, where a relog was needed.
* Positioned Card door and Mysterious door in the inventory and in hand.
* Warp point having green trail.
* Player losing Castle Oblivion data when relogging or switching dimensions.

# 2.8.0b:
## Added:
* Glove icon on button hovering.
* Magic spells elemental texture to identify them easily. 
* Small introduction to Castle Oblivion
* Trails to the Savepoint.
* Sound when drive level goes up (Thanks to Xephiro).
* Proper revert sound (Thanks to Xephiro again).
## Changed:
* HP, MP, Drive and Munny textures.
* Creative tab submenus icons will now cycle randomly.
* Sorted magic spells in the creative menu.
## Fixed:
* Potential crash sometimes when the command menu would load before the config.
* Key rooms and exit didn't get darker when not opened yet. 
* Ignite spell item not translated in Spanish.

# 2.8.0a:
## Added:
* New spells:
  * Confuse spell: Bought in the shop at tier B onwards.
  * Mini spell: Meldable with Magnera + Warp / Magnega + Magnega / Magnega + Bind.
  * Slow spell: Bought in the shop at tier C onwards.
* Command Menu Attack submenu, for spells that are defined as physical (only for addons right now).
* New melding recipe for Crawling Fire / Firaga Burst: Firaga + Slow.
* Castle Oblivion system is now officially added an no longer in super WIP mode it is however, not complete:
  * A single entrance structure generates in the overworld to open the door you need a World Card currently the only one obtainable is the Plains one which is crafted by a Biome Memory + Empty Card.
  * Biome Memories are a rare drop from mobs in the specific biome for that memory so to get a Plains Biome Memory you need to kill mobs in a Plains biome.
  * Once you enter the castle through the front entrance you can use the Plains Card on the door at the end of the Entrance Hall to generate the first floor. Currently the Plains floor is the only one implemented more are to come.
  * Opening doors requires Map Cards which you can get from killing mobs spawned within the castle. Rooms have a limited number of spawns and some rooms do not spawn mobs.
  * When you use a card to open a door it will generate a new room with different properties depending on the card you use. Note not all cards have been implemented yet.
  * You can change the type of already generated rooms by using another card on the open door which will completely regenerate the room with the new type
  * Every floor has 3 key rooms which have multiple criteria and require a Keycard. These rooms have encounters with waves of heartless spawns after defeating all of them the doors will be unlocked and the next Keycard is granted and in the final one, Room of Truth, the door leading to Conqueror's Respite opens to then take you to the next floor as there is only one World Card implemented as of now this is as far as you can progress
  * The system is highly customisable through data packs
  * The majority of Plains rooms were built by the talented Ventusjs absolute legend and lifesaver.
## Changed:
* Creative menu tabs all merged back into one with sub categories.
* Magnet visuals reworked with a ring of electricity similar to it's BBS style.
* Shadows, Mega Shadows and Gigas Shadows will now transition in and out the ground smoothly.
* Moogles now bob up and down giving a bit of life to them.