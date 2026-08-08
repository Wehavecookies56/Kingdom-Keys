## Added:
* Realm of Darkness arches.
* World Map dimension where gummi ships won't use any fuel.
* Air Soldier, Defender, Neoshadow and Novashadow.
* Gummi shots can destroy meteors.
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

## Changed:
* Lowered meteorite frequency.
* Removed old blizzardChangeBlocks Common Config since the new system obsoletes it.

## Fixed:
* Red tint leak from low HP.
* Melding ingredients allowing to select a copy of the first selected item even if it's not a real recipe.
* Gummi ship losing it's fuel when moving it in edit mode with the arrow buttons from the hangar.
* Air gets ignored in Gummi Ship structure serialization, saving tons of space.
* Items given with the new system (window or notification) now stack if possible in the stock menu.
* Castle Oblivion door being breakable.