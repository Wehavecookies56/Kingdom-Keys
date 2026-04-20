**Added:**
* Grand magic extender ability which boosts grand magic Situation Command duration by 10% after each equip.
  * Obtained at level 17 and 29 for warrior, 4 and 20 for mystic, 9 and 27 for guardian.
* Magic target block, outputting a redstone signal depending on the magic type that hit it:
  * It ranges from 1-8 as follows: fire, ice, water, lightning, aero, stop (currently not working), darkness, light.
* A way for addon makers to change the color of Situation Commands timer bar.
* A common-config option to hide player names while wearing the Organization XIII coat.

**Changed:**
* Datapacks modifying the leveling progression got support for a version number, allowing for updates to apply once a player logs in.
  * It should be retro compatible with the old .json files, but we encourage you to migrate to the new system.
* Gravity is now a dark spell.
* Minimum XP you can get from defeating enemies is at least 1.
* **/kingdomkeys exp give** won't play the level up sound so it can be used in adventure maps or servers. 

**Fixed:**
* Items and Portals not displaying a selected element until scrolled.
* Stock items tooltip icon was out of place on different aspect ratios.
* XP particle not displaying the right amount if changed on the server.