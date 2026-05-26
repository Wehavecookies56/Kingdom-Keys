### Added:
* Command melding system, similar to how it works in Birth by Sleep.
* Experience and levels system for magic spells:
  * They level up by defeating enemies, by default 40% of the exp a mob gives will be going towards magics.
  * They all get the same exp, as long as they are equipped.
  * Once they reach the max exp it displays it will level up one level.
  * When they reach max level they will be available to be used in melding.
  * Experience boost ability increases their exp gains.
* Magics bag, to store all your magic spells without cluttering your inventory, 
  * Works with the magic selection and melding menus.
* Master form EFM animation.
* Common config option for gummi fuel consumption factor, defaulting to half of what it was before.
* New achievement for when obtaining a winner stick.
* New config option to limit shop inventory based on their tier to players which already meet it.
* New config option to set the amount of exp that magics get from mobs (separate from player exp).
* JEI recipe category for savepoint upgrades.
* JEI recipe category for command melding.
* Added a screen to hide spells from the command menu.

### Changed:
* Moogle Model thanks to Xephiro.
* Savepoint configs have been moved to be data-driven for a cleaner look.
* Savepoints will no longer restore any stat by default unless an item is used on them.
* Magics are no longer stacked in the equipment menu due to them being all different now because of their experience.

### Fixed:
* Sell price for netherite ingot adjusted.
* Magic Target Block didn't have a recipe, it is now crafted with a Target block and a Redstone block.
* Culled magnet blox trails based on distance, increasing performance when they are far from the player.
* Made Full MP Blast stackable with diminishing returns since the description mentioned it.
* Synthesis and Keyblade Forge screens item description being misaligned.
* Obtained EXP will now ensure a minimum of 1xp, just like the visual indicator.
* Experience boost not really increasing the exp obtained.