**Added:**
* New data version check, will automatically run the exp fix command if the playerdata is older.
* Keyblade swapping by cancelling the ATTACK menu (opposite of opening the submenu).
* Situation commands (timed reaction commands).
* Small icon representing the keybind next to the selected Reaction Command.
* New ability Superjump, increases vertical impulse off a flowmotion jump.
  * Obtained at Valor form levels 4 and 6.
* New ability Superslide, increases horizontal impulse off a flowmotion airdash.
  * Obtained at Master form levels 4 and 6, and at level 42 for warrior, 46 for mystic and 39 for guardian.
* Some abilities to some Keyblades.
  * Firaza: Twilight Blaze.
  * Waterza: Wheel of Fate.
  * Fire Boost: Frolic Flame.
  * Blizzard Boost: Crystal Snow.
  * Water Boost: Abyssal Tide, Leviathan.
  * Light and Darkness: Incomplete X-blade.
  * Lucky Strike: Lady Luck.
  * Protectga: Master's Defender.
  * Wizard's Ruse: Mirage Split, Nightmare's End, Combined Keyblade.
  * Jackpot: Treasure Trove.

**Changed:**
* Command to add ability and shotlocks now require a boolean for permanency, these will prevail after a level reset or an experience fix.
  * /kingdomkeys ability give <ability> <permanent> [player]
  * /kingdomkeys shotlock give <shotlock> <permanent> [player]
  * The command to take them away will remove them both temporary and permanent.
* Flowmotion renamed to Wall Kick, equip more to allow more bounces.
* Flowmotion-related abilities are now considered growth abilities.
* Wall kick obtained at levels 7 and 51 for Warrior, 9 and 62 for Mystic, 14 and 43 for Guardian.
* Air dash obtained at levels 19 and 40 for Warrior, 29 and 45 for Mystic, 24 and 45 for Guardian.
* Minor adjustments in a couple of keyblades' reach.
* Scroll bars will always be visible.
* Button tips color changed from orange to light blue.

**Fixed:**
* Random crash when opening Minecraft related to configs being accessed too early.
* Incomplete X-blade range being way too long.
* Some water spells not dealing water damage type.
* When having more than 1 RC if the selected one disappeared you could still attempt to cast it, crashing.
* A couple of configs in the config screen not being localized.
* Menu bottom bars gap was too large.