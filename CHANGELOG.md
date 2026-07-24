## Added:
* Struggle! Craft the board and play around with your friends! (Multiplayer only)
  * Firstly you need to specify the corners of the arena, where the 1 vs 1 matches will use as spawn points.
  * Includes 3 modes, the winner needs to either collect all the enemy orbs (and their own) in the match or end with the most orbs when the timer ends:
    * Duel: 1v1, can be repeated.
    * Tournament: Up to 8 players, bracket style tournament, if two players tie there's an overtime.
    * Free for All: Up to 8 players, all vs all.
  * You can specify the amount of initial orbs and timer,
* A bunch of limits:
  * Xaldin: Lance Storm and Fallen Spear.
  * Vexen: Icy Pillars.
  * Lexaeus: Powerup and Rocky Pillars.
  * Zexion: Illusory Meteor.
  * Saïx: Berserker Claymore.
  * Axel: Ring of Flames and Flame Wall.
  * Demyx: Water Trail and Water Wall.
  * Luxord: Card Wall.
  * Marluxia: Scythe Dash and Petal Void.
  * Roxas: Light Barrage.
* New ServerConfig option to limit Organization limit attacks to their member only.
* A bunch of new advancements.
* A bunch of new Shotlocks:
  * Volley style: Meteor Shower, Flame Salvo, Chaos Snake. 
  * Dashing style: Absolute Zero, Photon Charge, Lightning Ray. 
  * Circular style: Bubble Blaster, Bio Barrage, Thunderstorm, Pulse Bomb.
* Toggle Gummi Ship flight mode with pick block key (by default middle mouse click).
  * In that flight mode the ship follows the camera, similar to elytra.
* Recipe to craft the dev heads.
* Treasure chest block, this is a special type of chest that has no GUI and will give the player items when right-clicked, these are intended for Castle Oblivion room generation and cannot be created.
* Calm Bounty, False Bounty, Guarded Trove map cards implemented.
* Castle Oblivion Room Types have a treasure property to specify the loot and trapped chests.
* Encounter rooms spawn a treasure chest on completion with the rewards.
* Obtained item display can be dismissed by pressing a mouse button or key.
* Item Overflow, items given to the player such as the Keycards will go into your overflow inventory if you have no inventory space. Overflow inventory can be accessed in the Stock menu.
* Dropped cards will now bounce similar to how they do in Chain of Memories.

## Changed:
* Entry to Castle Oblivion is denied if you're on peaceful difficulty.
* Stock now displays overflow instead of the player's inventory.
* Regenerating rooms in Castle Oblivion clears all entities so it no longer leaves dropped items everywhere.
* Opening card packs will deposit the cards in the Cards bag if it has some space.
* Recipes for Keyblade Forge level ups are now different for each Keyblade.
* Slightly optimized some parts of the code.
* Updated Keyblade weapon to match new EFM declarations.
* White Mushroom rewards are now data driven, by default the loot table is composed by orichalcum, orichalcum plus, illusory crystal, evanescent crystal, manifest illusion and lost illusion, and they drop from 1 up to 2 or 3 of one material.
* Datagen'd Org limits.

## Fixed:
* Castle Oblivion door criteria values match COM's mechanics, the doors within a generated room will be +1 the value of the card until it reaches 9 then will be 0.
* 0 doors now need 0 rather than any value.
* All For One scaling when on the ground.
* Made some sound effects mono fixing the sound attenuation.
* Button rendering method, improving performance while in the M menu.
* Being able to use air dash while in a vehicle.
* Removed ship fall damage for non player entities.
* Fixed Gummi Hangar hologram not rendering.
* Orientation of thrown lances and cards.