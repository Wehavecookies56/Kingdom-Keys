Added:
* Tamed mobs level up with the player
* Config option to disable changing the mob name to show the level
* Config option to set the starting recipes

Fixed:
* Some messages missing text with JourneyMap (possibly other mods) installed

Changed:
* Music in KK dimensions will now play in creative mode
* Synthesis materials are now registered via a tag "kingdomkeys:synthesis/materials" so with a datapack any item can be used in a recipe so long as it is added to this tag
* Organization weapon unlocks are now data driven through tags "kingdomkeys:organization/membername" e.g. "kingdomkeys:organization/axel"
* JSON data loading has been optimised and as a result should be a bit faster 
* Castle Oblivion room types, floor types and room structures are now data driven and the next floor will generate when reaching the end of the current floor however, Castle Oblivion is still very much work in progress so please pretend it doesn't exist yet otherwise we might need to re:chain some memories...