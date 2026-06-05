# 2.7.13b:
# Added:
* New Magics:
  * Faith (reworked from ReMind).
  * Triple Plasma: Meldable with Thundaga Shot + Thundaga Shot
# Fixed:
* Status menu overlapping Form level and choice.
* Magics not being able to be used again if MP Recharge was completed while the submenu was open the first time.
* Drive forms menu being accessible in addons before learning any visible form (crashing the game).

# 2.7.13a:
# Added:
* Config option to revert the magic cost system change to what it was before.
* A box in the Status screen to display the SOA choice.
* A new button to the Melding screen to filter only available melding combinations.
* Now magics increase their damage as they level up.
* New Magics:
  * Deep Freeze: Meldable with Blizzaga + Triple Blizzaga.
  * Glacier: Meldable with Blizzaga + Deep Freeze / Triple Blizzaga + Deep Freeze.
  * Ported over from ReMind (Thanks to Xephiro)
    * Balloon: Melded with Water + Gravity.
    * Balloonra: Melded with Watera + Gravira / Balloon + Balloon.
    * Balloonga: Melded with Waterga + Graviga / Balloonra + Balloonra.
    * Spark: Melded with Thunder + Magnet.
    * Sparkra: Melded with Thundara + Magnera / Spark + Spark.
    * Sparkga: Melded with Thundaga + Magnega / Sparkra + Sparkra.
    * Mine Shield: Meldable with Fira + Zero Gravity.
    * Mine Square: Meldable with Fira + Stop.
    * Seeker Mine: Meldable with Mine Shield + Magnega / Mine Square + Magnega / Mine Shield + Mine Square.
    * Warp: Meldable with the same recipes as Zero Gravira (10%) and Zero Gravira (20%).
    * Esuna: Bought in the shop at tier C onwards.
* New buy sound.
* Basic magics to the shop at tiers B and A.
* New crafting recipe to combine 8 recipes skipping 2 tiers (8 D-Tier into an A-Tier recipe).
* Simulated magic cursor memory for command menu.

# Changed:
* Magics can now be casted even if your Max MP is lower.
* Normal and Warp Savepoints will now register the destination point as player respawn instead of the source one. 
* Recipes will now stack up to 64.

# Fixed:
* Some entities using the new fire texture.
* Fire texture flickering and rendering badly when there's water or clouds behind it.
* Patchouli Journal saying you lose upgrades when breaking savepoints (Used to be the case, not anymore). 
* Lock on will now lock off if the target's HP reaches 0 (helps on servers with CNPC where mobs still exist).
* Magics in the Screen to hide them will display their proper level (Fire/fira/Firaga).
* Scroll jumping when sleecting something on the melding screen.