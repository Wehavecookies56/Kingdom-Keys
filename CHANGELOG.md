**Added:**
* A training scarecrow (training dummy) which shows the damage dealt and the magic it deals (through color).
* Desertor heartless, these ones are slightly smaller but very fast, watch out! 
* Commander heartless, these are big and have a larger health pool, consider them a threat.
* An example datapack template for custom materials and synthesis recipes.

**Changed:**
* Heartless and Antiform eyes will now glow in the darkness.
* Font and color used in the XP obtained from an enemy.
* Assassins will now yield a bit more exp.
* The way Kingdom Keys complains about malformed synthesis materials in a custom datapack.
* Orb entities (Munny, HP...) logic to hopefully improve performance.
* Extended Reach packet got replaced by a mixin, as it was the culprit for double attacking based on the distance.

**Fixed:**
* Bomb enemies sizes not varying.
* Event for driveform ability unequip on revert.
* Crash regarding water spells owner not being a player.
* Glide stuttering while in final form if the glide ability was not equipped for the base form.
* Reverting antiform causing issues with the ability event.
* Antiform revert being displayed as active despite conditions not being met.
