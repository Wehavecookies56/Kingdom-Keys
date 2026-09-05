## Added:
* Unions system, now the SoA choice will be preceded by an Union choice, just like the path it's not changeable once done.
  * They are all equal to themselves, if a player joins Organization XIII they won't have access to their Union privileges.
  * 
* Daybreak Town.
* World Markers in the Ocean Between, rendered while you're in a gummi ship.
* Lux when defeating enemies, used with your union's foreteller to redeem some rewards.
* Command to set a player Lux amount.
* Command to set a player union.
  * Players *with no unions* (returning players with a choice made) can use it to get their union, OPs can use it with a player parameter to enforce one.
* Ability group parameter.
  * Prevents a player from equipping multiple abilities that belong in the same group.
* Item slot unlocks through levelup, by default they get one every X levels: Guardian 4, Warrior 5, Mystic 6.
* Warning / Confirmation screens on:
  * When trying to unequip an accessory and max AP would decrease below the total used AP.
  * When selling items.
  * When kicking, leaving or disbanding party.
  * When interacting with gummi build/edit and it errors.
  * When setting the struggle coords further away than the max range.
* World json parameters:
  * "unlocked_by_default": Makes so a world is always accessible, if false it needs to be unlocked.
  * "marker_colour": Color of the world markers in the Ocean Between. 

## Changed:
* Abilities are now data driven.
* Updated version for the leveling json files.

## Fixed:
* Reversal RC being available even when the Dusk was out of reach.
* Keychains not getting an UUID assigned when synthesised directly into the Keychains Bag, allowing for dupes.
* Selling using the old item get sound.
* Gula's hat not having a back texture.
* Aligned moogle level to the right of the synthesis screen.
* Some localization issues.
* Removed a couple of warnings when loading the mod.