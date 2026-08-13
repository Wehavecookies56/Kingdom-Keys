## Added:
* Some new advancements to make the early game mod progression a little bit clearer.
* Kingdom Key recipe in the Common Config for startingRecipes (won't affect if you are in an existing world).
* Flowmotion Rails, which you can ride once you have the Wall Kick ability.
  * They can be crafted in all 16 colors, different colored rails won't interconnect (unless it's a straight line).
  * Sneaking or flying in creative avoids you grinding on them.
  * Rail jumping power is boosted by the ability superjump.
  * Redstone can be used in intersections just like with vanilla rails.
* World Map dimension where gummi ships won't use any fuel, filled with meteors.
  * Daybreak Town added as a world test, if kept, it might be reworked in the future.
  * Mod worlds will have idle and battle tracks which will fade in and out once the player gets in a fight.
* Option to auto build Gummi Ships based on the blueprint, place items in a chest right next to the hangar and if you have energy stored it will start to build it.
  * Server Config to enable or disable that option.
* Creative blueprint which builds the ship rather than displaying the hologram when clicking import.
* Option to export gummi ship blueprints into files to be able to load them as blueprints in other worlds or servers, or even send them to your friends.
* Gummi shots can now destroy meteors.
* Gummi inner corner shape.
* Sound when placing and breaking gummi blocks.
* New Abilities: Guard, Counterguard, Aerial Recovery and Once more.
* New Heartless: Air Soldier, Defender, Neoshadow and Novashadow.
* Realm of Darkness arches.
* Staff crowns: for devs, for people who helped to develop Addons and people who contributed to the mod somehow.
* New sounds for: heartless spawning, defeating heartless, defeating nobodies, ko loop, opening a treasure chest and for picking up synthesis materials into a bag. 
* Data driven property for magic data where you can specify what the projectile does to various blocks:
  * light_lightable: Allows the projectile to turn on campfires, candles...
  * turn_off_lightable: Allows the projectile to turn them off.
  * light_portal: Allows the projectile to light up a nether portal.
  * light_tnt: Allows the projectile to light a TNT block.
  * extinguish_tnt: Allows the projectile to extinguish the TNT fuse, placing the block back again.
  * extinguish_fire: Allows the projectile to extinguish fire blocks.
  * dry_sponge: Allows the projectile dry a wet sponge.
  * wet_sponge: Allows the projectile turn a sponge into a wet sponge.
  * freeze_water: Allows the projectile turn water into ice.
  * freeze_lava: Allows the projectile turn lava into obsidian.
* /kk shortcut for /kingdomkeys

## Changed:
* Config files have now been moved into their own subfolder, if you modified any, move them there AND RENAME to keep your existing settings.
  * New files don't have the "kingdomkeys-" prefix, therefore the "kingdomkeys-common.toml" file now goes into the "kingdomkeys" folder under the name "common.toml".
* Hangar screen is no longer that cluttered with information, it was moved into a list box on the left.
* Gummi ships built in lvl 1 hangar which then were stored in lvl 2 hangar, would increase their size internally, which wouldn't allow them to be stored in the lvl 1 hangar again, now they can.
* Collision boxes now fit better on shaped gummi blocks, mini helm and gummi vernier.
* Nobody banner icon.
* Lowered meteorite frequency.
* Hostility detection (for command menu color) is now fully client side and has a cooldown of 5 ticks, should hopefully avoid the flickering that sometimes still happened.
  * Now in order to be in combat you need to have line of sight with the enemy, so in caves it should be less frequent.
* Removed old blizzardChangeBlocks Common Config since the new system obsoletes it.

## Fixed:
* Gummi ships can now transport chests and other tile entities.
* Heartless portal tp to Realm of Darkness crash.
* Even ships offset when being built.
* Red tint leak from low HP.
* Melding ingredients allowing to select a copy of the first selected item even if it's not a real recipe.
  * You were able to select Waterga and still see another Waterga able to be selected while there's no recipe that use both.
* Gummi ship losing it's fuel when moving it in edit mode with the arrow buttons from the hangar.
* Gummi hangar hologram blocks not disappearing once they were correct on hangars facing north, west or east.
* Gummi aero being invisible when faced the wrong way right next to a solid block.
* Air gets ignored in Gummi Ship structure serialization, saving tons of space.
* Items given with the new system (window or notification) now stack if possible in the stock menu.
* Castle Oblivion door being breakable.
* You will no longer get a recipe dropped everytime you get a biome memory drop.
* **/kingdomkeys ability give** command not working unless specifying true / false, now true is implied if ignored.
