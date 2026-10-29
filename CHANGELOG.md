## Added:
* Fire, Fira and Firaga Barrier spells: KH2 approach at fire magic.
* Raging Storm spell: three columns of fire slowly circle around you for a while, burning enemies they touch. You can only move until it ends.
  * Melded from Fission Firaga and Firaga Burst, and a 10% chance to get it instead when melding Firaga or Triple Firaga.
* Gummi Hangar stats shown as a hexagon style chart: HP, Offense, Power, Mobility, Seats and Speed.
* Gummi Ship barrel roll: double tap left or right while in a ship to roll to that side, dodging a bit towards it. Requires at least one Aero gummi in the ship and scales with mobility (up to 40).
* Option in M menu, config, lock on, to hide the name and level shown above mobs. Player Nobodies are still shown.
* ServerConfig heartlessChaseLight to use the old heartless system (chasing the light).
* Kingdom Keys volume slider in Music & Sounds, next to the vanilla ones. (Ignores music tracks).

## Changed:
* Save points that restore MP also make magic cooldowns go twice as fast while standing on them, shared or individual.
* Lux icon to be the star instead of the light.
* Becoming a Heartless: a Shadow can only be restored by being struck down by a Keyblade. Dying any other way brings you back as a Shadow.
  * Apprentices now spawn around Shadow players to defeat them.
  * Your Nobody gets created once you turn into a Heartless rather than when you are restored.
  * It has a new animation when turning heartless.
  * Your Nobody vanishes when you log out, and a new one can come into being once you are back.
* CommonConfig playerSpawnHeartlessData is now splitted between nobodyHealthPercent and nobodyStrengthPercent (150% by default)
* Gummi weapons offense values to match KH3. Each volley deals a tenth of it, shared between its shots.
* Gummi boost follows the ship's pitch too, not only its yaw (3D flight mostly).
* Gummi water boost shockwave effect got reworked.

## Fixed:
* M menu bottom left panel changing its width as the time ticks.
* A player's Nobody getting its health and attack scaled twice (by the player's stats and by its level). It is now a regular Dusk at the player's level, multiplied by the new config percentages.
* Upgrading a save point not updating its stored type, so a save point upgraded to Warp didn't show as a destination from other dimensions.