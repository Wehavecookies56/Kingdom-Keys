## Added:
* Treasure chest block, this is a special type of chest that has no GUI and will give the player items when right clicked, these are intended for Castle Oblivion room generation and cannot be created.
* Calm Bounty, False Bounty, Guarded Trove map cards implemented.
* Castle Oblivion Room Types have a treasure property to specify the loot and trapped chests.
* Encounter rooms spawn a treasure chest on completion with the rewards.
* Obtained item display can be dismissed by pressing a mouse button or key.
* Item Overflow, items given to the player such as the Keycards will go into your overflow inventory if you have no inventory space. Overflow inventory can be accessed in the Stock menu.
* 2 Axel-themed limits: Ring of Flames and Flame Wall.
* Dropped cards will now bounce similar to how they do in Chain of Memories.

## Changed:
* Entry to Castle Oblivion is denied if you're on peaceful difficulty.
* Stock now displays overflow instead of the player's inventory.
* Regenerating rooms in Castle Oblivion clears all entities so it no longer leaves dropped items everywhere.
* Slightly optimized some of the code.

## Fixed:
* Castle Oblivion door criteria values match COM's mechanics, the doors within a generated room will be +1 the value of the card until it reaches 9 then will be 0.
* 0 doors now need 0 rather than any value.
* All For One scaling when on the ground.
* Made some sound effects mono fixing the sound attenuation.
* Button rendering method, improving performance while in the M menu.