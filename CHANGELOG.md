## Added:
* Animated keychains simulation! A proof of concept applied to the following keyblades: 
  * Bond of Flame, Bond of the Blaze, Braveheart, Dawn Till Dusk, Dead of Night, Destiny's Embrace, Earthshaker, Ends of the Earth, Fenrir, Frolic Flame, Grand Chef, Hidden Dragon, Incomplete χ-blade, χ-blade, Kingdom Key, Kingdom Key D, Kingdom Key Nightmare, Long Night, Lost Memory, Master's Defender, Midnight Blue, Mirage Split, Combined Keyblade, The Gazing Eye, No Name (BBS), Oathkeeper, Oblivion, Phantom Green, Rainfell, Retribution, Star Cluster, Star Seeker, Stormfall, Two Become One, Ultima Weapon BBS/DDD/KH1,KH2,KH3, Void Gear, Void Gear Remnant, Way to the Dawn, Wayward Wind, Young Xehanort's Keyblade.
* Keyblade hit particles! Most of them inherit the generic effect from Kingdom Key, but others have their own: 
  * JungleKing, Oathkeeper, Oblivion, Ultima Weapon BBS / DDD / KH1 / KH2 / KH3.
* Minimap icons for Air Soldier, Defender, Neoshadow and Novashadow (Thanks to WillabyNeko)
* Level up item giving tracker so further exp readjustments won't give them again.
* Keychains bag.
* Test Riku idle stance.
* Guard event which fires both, on guard start and on an actual block.
* [EFM] Added antiform idle, walking and running animations.

## Changed:
* [EFM] Combat style screen got slightly reworked.
* Pyramid gummi hitbox got slightly lowered in the lowest corner.
* Sonic Blade-like shotlocks movement got adjusted (Thanks to Xephiro).
* Removed Drive Form Visibility config option, it was added a long time ago when their render was buggy, now it's been fine for a long time :)

## Fixed:
* Grinding on rails not showing the flowmotion trail to other players.
* Antiform revert sound still being the old unsummon one.
* Keychain equipment screen button width overlapping the scroll bar.
* [EFM] Keyblades in dual wield boxing animation, removed their keychain requirement, now they will work as long as the held items are keyblades.
* [EFM] Idle animations on some drive forms.
* [EFM] Limit form boxing instead of attacking with the keyblade.
* [EFM] Valor form walking animation.
* [EFM] Drive Form poses not resetting when they are reverted until a weapon is resummoned.
* [EFM] Antiform rendering it's head in first person.
* [EFM] Crash when trying to cast any mob as a Player.
* [EFM] Crash when EFM is not present in the modpack.