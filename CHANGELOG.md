## Added:
* Unions system, now the SoA choice will be preceded by an Union choice, just like the path it's not changeable once done.
  * They are all equal to themselves, if a player joins Organization XIII they won't have access to their Union privileges.
  * Foretellers will be able to train you, either by summoning rounds of enemies or by sparring with them.
  * Lux is used to claim rewards from the Foretellers. Foretellers from other unions will charge you extra Lux for theirs.
* Daybreak Town, full of keyblade wielders who you can spar with.
* Physics for Starlight keychain.
* World Markers in the Ocean Between, rendered while you're in a gummi ship.
* Lux when defeating enemies and training with your Foreteller.
* Orb of Light and it's shaded variant as enemies, not spawning on their own, only used by the foretellers as training to their pupils.
* Command to set a player Lux amount.
* Command to set a player union.
  * Players *with no unions* (returning players with a choice made) can use it to get their union, OPs can use it with a player parameter to enforce one.
* Ability group parameter.
  * Prevents a player from equipping multiple abilities that belong in the same group.
* Item slot unlocks through levelup, by default they get one every X levels: Guardian 4, Warrior 5, Mystic 6. Up to 6 slots.
* Warning / Confirmation screens on:
  * When trying to unequip an accessory and max AP would decrease below the total used AP.
  * When selling items.
  * When kicking, leaving or disbanding party.
  * When interacting with gummi build/edit and it errors.
  * When setting the struggle coords further away than the max range.
* World json parameters:
  * "unlocked_by_default": Makes so a world is always accessible, if false it needs to be unlocked.
  * "marker_colour": Color of the world markers in the Ocean Between. 
* HUDElements are now usable by addons.
* Neoshadows and Novashadows spawn to the Realm of Darkness.
* Translations for all the Spanish variants (Argentina, Chile, Ecuador, México, Uruguay and Venezuela).

## Changed:
* Abilities are now data driven.
* Updated version for the leveling json files, next time a player joins will get their level adjusted and abilities re-given.
* Magic spells are now usable by entities.

## Fixed:
* Reversal RC being available even when the Dusk was out of reach.
* Dark Firaga being a normal Firaga.
* Bind and Mini not adjusting their time properly based on what was configured.
* Shotlocks leaving players airborne if they get somehow interrupted.
* Org portal not being visible in the destination if it was casted from another dimension.
* Org portal not ticking in the source after it was crossed and chunk was unloaded.
* Keychains not getting an UUID assigned when synthesised directly into the Keychains Bag, allowing for dupes.
* Selling using the old item get sound.
* Gula's hat not having a back texture.
* Aligned moogle level to the right of the synthesis screen.
* Keychain physics not being isolated per player.
* Interpolated the glove movement in selected button.
* Some localization issues.
* Removed a couple of warnings when loading the mod.
* Some crashes when needed json files are not present.
* Rare Large Body model crash.