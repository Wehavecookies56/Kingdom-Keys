**Added:**
* New data version check, will automatically run the exp fix command if the playerdata is older.
* Keyblade swapping by cancelling the ATTACK menu (opposite of opening the submenu).
* Situation commands (timed reaction commands).
* Some abilities to some elemental Keyblades.
* New ability Superjump, increases vertical impulse off a flowmotion jump.
  * Obtained at Valor form levels 4 and 6.
* New ability Superslide, increases horizontal impulse off a flowmotion airdash.
  * Obtained at Master form levels 4 and 6, and at level 42 for warrior, 46 for mystic and 39 for guardian.

**Changed:**
* Command to add ability and shotlocks now require a boolean for permanency, these will prevail after a level reset or an experience fix.
  * /kingdomkeys ability give <ability> <permanent> [player]
  * /kingdomkeys shotlock give <shotlock> <permanent> [player]
  * The command to take them away will remove them both temporary and permanent.
* Flowmotion renamed to Wall Kick, equip more to allow more bounces.
* Flowmotion-related abilities are now considered growth abilities.
* Wall kick obtained at levels 7 and 51 for Warrior, 9 and 62 for Mystic, 14 and 43 for Guardian.
* Air dash obtained at levels 19 and 40 for Warrior, 29 and 45 for Mystic, 24 and 45 for Guardian.

**Fixed:**
* Random crash when opening Minecraft related to configs being accessed too early.