## Added:
* Treasure chest block, this is a special type of chest that has no GUI and will give the player items when right clicked, these are intended for Castle Oblivion room generation and cannot be created.
* Calm Bounty, False Bounty, Guarded Trove map cards implemented.
* Castle Oblivion Room Types have a treasure property to specify the loot and trapped chests.
* Encounter rooms spawn a treasure chest on completion with the rewards.
* Obtained item display can be dismissed by pressing a mouse button or key
* Item Overflow, items given to the player such as the Keycards will go into your overflow inventory if you have no inventory space. Overflow inventory can be accessed in the Stock menu.

## Changed:
* Entry to Castle Oblivion is denied if you're on peaceful difficulty
* Stock now displays overflow instead of the player's inventory

## Fixed:
* Castle Oblivion door criteria values match COM's mechanics, the doors within a generated room will be +1 the value of the card until it reaches 9 then will be 0. 
* 0 doors now need 0 rather than any value.