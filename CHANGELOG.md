# 2.7.12b:
# Added:
* Config option to revert the magic cost system change to what it was before.
* A box in the Status screen to display the SOA choice.
* Now magics increase their damage as they level up.
* New Magics:
  * Deep Freeze: Meldable with Blizzaga + Triple Blizzaga.
  * Glacier: Meldable with Blizzaga + Deep Freeze / Triple Blizzaga + Deep Freeze.
* New buy sound.
* Basic magics to the shop at tiers B and A.
* New crafting recipe to combine 8 recipes skipping 2 tiers (8 D-Tier into an A-Tier recipe).

# Changed:
* Magics can now be casted even if your Max MP is lower.
* Normal and Warp Savepoints will now register the destination point as player respawn instead of the source one. 
* Recipes will now stack up to x64.

# Fixed:
* Some entities using the new fire texture.
* Patchouli Journal saying you lose upgrades when breaking savepoints (Used to be the case, not anymore). 
* Lock on will now lock off if the target's HP reaches 0 (helps on servers with CNPC where mobs still exist).


# 2.7.12a
### Added:
* New magics:
  * Dark Firaga: Meldable with Firaga + Blackout.
  * Triple Firaga: Meldable with Firaga + Firaga / Firaga + Fira.
  * Crawling Firaga: Meldable with Firaga + Stopra.
  * Fission Firaga: Meldable with Firaga + Aerora / Fira + Aeroga.
  * Firaga Burst: Meldable with Firaga + Aeroga (20%) / Firaga + Stopga (20%).
  * Triple Blizzaga: Meldable with Blizzaga + Blizzaga / Blizzaga + Blizzara.
  * Zero Gravity: Available in shop for 800 munny after unlocking Synthesis Rank B. 
  * Zero Gravira: Meldable with Zero Gravira + Zero Gravira / Magnet + Aero / Thunder + Zero Gravity.
  * Zero Graviga: Meldable with Zero Graviga + Zero Graviga / Zero Gravira + Thundara.
  * Blackout: Meldable with Zero Gravira + Poison.
  * Poison: Available in shop for 500 munny after unlocking Synthesis Rank C.
* *Item get* windows when synthesizing and melding items.

### Changed:
* Firaga no longer has an AOE attack, Fission Firaga takes that.
* JEI melding category to display rare recipes.
  
### Fixed:
* Selling would not work if the inventory was full.
* Blizzard not ignoring iframes.
* Moogle shop displaying Cost::
* Crash related to Large Body attacker being null. 