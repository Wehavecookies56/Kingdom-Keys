package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.datagen.provider.KKLanguageProvider;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

import static online.kingdomkeys.kingdomkeys.KingdomKeys.MODID;
import static online.kingdomkeys.kingdomkeys.lib.Strings.*;

public class LanguageESES extends KKLanguageProvider {

    public LanguageESES(DataGenerator gen) {
        super(gen, "es_es");
    }

    @SuppressWarnings("all")
    @Override
    protected void addTranslations() {
        //Config category keys
        //CLIENT
        add(KingdomKeys.MODID + ".configuration.gui", "Ajustes de interfaz");
        add(KingdomKeys.MODID + ".configuration.hud_data", "Ajustes de HUD, NO los edites desde aquí");
        add(KingdomKeys.MODID + ".configuration.command_menu", "Ajustes de Menú de Comandos");
        add(KingdomKeys.MODID + ".configuration.hp_bar", "Ajustes de Indicador de Vida");
        add(KingdomKeys.MODID + ".configuration.mp_bar", "Ajustes de Indicador de Magia");
        add(KingdomKeys.MODID + ".configuration.dp_bar", "Ajustes de Indicador de Fusión");
        add(KingdomKeys.MODID + ".configuration.player_skin", "Ajustes de indicador del jugador");
        add(KingdomKeys.MODID + ".configuration.lock_on", "Ajustes de Fijar Enemigo");
        add(KingdomKeys.MODID + ".configuration.party", "Ajustes de Grupo");
        add(KingdomKeys.MODID + ".configuration.focus", "Ajustes de Indicador de Tino");

        //COMMON
        add(KingdomKeys.MODID + ".configuration.general", "Ajustes generales");
        add(KingdomKeys.MODID + ".configuration.gummi", "Gummi Ship settings");
        add(KingdomKeys.MODID + ".configuration.spawning", "Ajustes de spawneo");
        add(KingdomKeys.MODID + ".configuration.drops", "Ajustes de drops");
        add(KingdomKeys.MODID + ".configuration.shotlock", "Ajustes de Tiro Certero");
        add(KingdomKeys.MODID + ".configuration.synthesis", "Ajustes de síntesis");
        add(KingdomKeys.MODID + ".configuration.savepoint", "Ajustes de Punto de Guardado");

        //SERVER
        add(KingdomKeys.MODID + ".configuration.leveling", "Ajustes de niveles");


        //CLIENT
        addConfigKey(ModConfigs.getClientConfig().summonTogether, "Invoca simultáneamente arma y armadura");
        addConfigKey(ModConfigs.getClientConfig().auto3rdPersonShip, "3a persona automática en Nave Gummi");
        addConfigKey(ModConfigs.getClientConfig().showGuiToggle, "Cambiar visibilidad del HUD");
        addConfigKey(ModConfigs.getClientConfig().showDriveForms, "Visibilidad de Formas de Fusión");
        addConfigKey(ModConfigs.getClientConfig().hiddenMagic, "Magias ocultas en el Menú de Comandos");
        addConfigKey(ModConfigs.getClientConfig().cmTextXOffset, "Menú de Comandos offset del texto X");
        addConfigKey(ModConfigs.getClientConfig().cmHeaderTextVisible, "Mostrar cabecera del Menú de Comandos");
        addConfigKey(ModConfigs.getClientConfig().customFont, "Muestra la fuente personalizada");
        addConfigKey(ModConfigs.getClientConfig().cmClassicColors, "Colores del Menú de Comandos clasicos");
        addConfigKey(ModConfigs.getClientConfig().cmSelectedXOffset, "Menú de Comandos offset X seleccionado");
        addConfigKey(ModConfigs.getClientConfig().cmSubXOffset, "Menú de Comandos submenú X offset (%)");
        addConfigKey(ModConfigs.getClientConfig().cmEndLWidth, "Menú de Comandos anchura segmento izquierda");
        addConfigKey(ModConfigs.getClientConfig().cmEndRWidth, "Menú de Comandos anchura segmento derecha");
        addConfigKey(ModConfigs.getClientConfig().cmHeaderEndLWidth, "Menú de Comandos anchura cabecera izquierda");
        addConfigKey(ModConfigs.getClientConfig().cmHeaderEndRWidth, "Menú de Comandos anchura cabecera derecha");
        addConfigKey(ModConfigs.getClientConfig().cmReactionEndLWidth, "Comando de Reacción anchura izquierda");
        addConfigKey(ModConfigs.getClientConfig().cmReactionEndRWidth, "Comando de Reacción anchura derecha");
        addConfigKey(ModConfigs.getClientConfig().hpShowHearts, "Mostrar corazones de vida");
        addConfigKey(ModConfigs.getClientConfig().hpAlarm, "Volumen de alarma de vida baja");
        addConfigKey(ModConfigs.getClientConfig().lockOnIconScale, "Ícono de fijación escala (%)");
        addConfigKey(ModConfigs.getClientConfig().lockOnIconRotation, "Ícono de fijación vel. rot.");
        addConfigKey(ModConfigs.getClientConfig().lockOnHpPerBar, "Vida de objetivo hp por barra");
        addConfigKey(ModConfigs.getClientConfig().partyYDistance, "HUD Grupo Y offset");

        //COMMON
        addConfigKey(ModConfigs.getCommonConfig().recipeDropChance, "Probabilidad de soltar recetas");
        addConfigKey(ModConfigs.getCommonConfig().bombExplodeWithFire, "Sincorazón bomba explotan con fuego");
        addConfigKey(ModConfigs.getCommonConfig().blizzardChangeBlocks, "Hielo altera bloques");
        addConfigKey(ModConfigs.getCommonConfig().keybladeOpenDoors, "Llave espada abre puertas de hierro");
        addConfigKey(ModConfigs.getCommonConfig().driveHeal, "Cura de Fusión");
        addConfigKey(ModConfigs.getCommonConfig().drivePointsMultiplier, "Multiplicador de puntos de Fusión");
        addConfigKey(ModConfigs.getCommonConfig().focusPointsMultiplier, "Multiplicador de puntos de Tino");
        addConfigKey(ModConfigs.getCommonConfig().critMult, "Multiplicador de golpe crítico");
        addConfigKey(ModConfigs.getCommonConfig().needKeybladeForHeartless, "Llave espada necessaria para dañar Sincorazón");
        addConfigKey(ModConfigs.getCommonConfig().allowBlocksInHangarArea, "Permitir bloques en la zona del hangar");
        addConfigKey(ModConfigs.getCommonConfig().gummiBlocksDropPercent, "Bloques Gummi soltados");
        addConfigKey(ModConfigs.getCommonConfig().heartlessSpawningMode, "Modo de aparición de Sincorazón");
        addConfigKey(ModConfigs.getCommonConfig().mobSpawnRate, "Probabilidad de aparición por tipo");
        addConfigKey(ModConfigs.getCommonConfig().playerSpawnHeartless, "Spawnear Sincorazón e Incorpóreo de jugador");
        addConfigKey(ModConfigs.getCommonConfig().mobLevelingUp, "Enemigos suben de nivel");
        addConfigKey(ModConfigs.getCommonConfig().mobLevelName, "Nivel del mob en el nombre");
        addConfigKey(ModConfigs.getCommonConfig().rodHeartlessLevelScale, "Escala de nivel de Sincorazón en RoD");
        addConfigKey(ModConfigs.getCommonConfig().rodHeartlessMaxLevel, "Máximo nivel de Sincorazón en RoD");
        addConfigKey(ModConfigs.getCommonConfig().playerSpawnHeartlessData, "Estadísticas de Sincorazón e Incorpóreo de jugador");
        addConfigKey(ModConfigs.getCommonConfig().respawnROD, "Forzar respawn en RoD");
        addConfigKey(ModConfigs.getCommonConfig().mobLevelStats, "Escala de estadísticas por nivel para mobs");
        addConfigKey(ModConfigs.getCommonConfig().bossDespawnIfNoTarget, "Jefe desaparece si pierde objetivo");
        addConfigKey(ModConfigs.getCommonConfig().hpDropProbability, "Probabilidad de soltar VT");
        addConfigKey(ModConfigs.getCommonConfig().mpDropProbability, "Probabilidad de soltar MP");
        addConfigKey(ModConfigs.getCommonConfig().munnyDropProbability, "Probabilidad de soltar Platines");
        addConfigKey(ModConfigs.getCommonConfig().driveDropProbability, "Probabilidad de soltar puntos de Fusión");
        addConfigKey(ModConfigs.getCommonConfig().focusDropProbability, "Probabilidad de soltar puntos de tino");
        addConfigKey(ModConfigs.getCommonConfig().shotlockMult, "Multiplicador de daño del Tiro Certero");
        addConfigKey(ModConfigs.getCommonConfig().startingRecipes, "Recetas iniciales");

        //SERVER
        addConfigKey(ModConfigs.getServerConfig().gummiShipFuelSystem, "Gummi fuel system");
        addConfigKey(ModConfigs.getServerConfig().partyRangeLimit, "Límite de rango de grupo");
        addConfigKey(ModConfigs.getServerConfig().partyMembersLimit, "Límite de miembros de grupo");
        addConfigKey(ModConfigs.getServerConfig().requireSynthTier, "Requerir nivel de síntesis");
        addConfigKey(ModConfigs.getServerConfig().projectorHasShop, "Proyector Moguri contiene tienda");
        addConfigKey(ModConfigs.getServerConfig().getExpFromShop, "Obtener exp. de la tienda");
        addConfigKey(ModConfigs.getServerConfig().orgEnabled, "Sistema de la Organización XIII");
        addConfigKey(ModConfigs.getServerConfig().allowBoosts, "Impulsos de estadísticas");
        addConfigKey(ModConfigs.getServerConfig().allowPartyKO, "Sistema de KO en grupo");
        addConfigKey(ModConfigs.getServerConfig().wayfinderParty, "Restringir Siemprejuntos a grupo");
        addConfigKey(ModConfigs.getServerConfig().dragonLevel, "Permite que el Dragón del End suba de nivel");
        addConfigKey(ModConfigs.getServerConfig().hostileMobsLevel, "Mobs hostiles suben de nivel (no enemigos de KK)");
        addConfigKey(ModConfigs.getServerConfig().shotlockMaxDist, "Distancia máxima de Tiro Certero");
        addConfigKey(ModConfigs.getServerConfig().xpMultiplier, "Multiplicador de XP");
        addConfigKey(ModConfigs.getServerConfig().heartMultiplier, "Multiplicador de Corazones");
        addConfigKey(ModConfigs.getServerConfig().partyXPShare, "Compartir XP en grupo");
        addConfigKey(ModConfigs.getServerConfig().driveFormXPMultiplier, "Multiplicador de XP para Formas de Fusión");
        addConfigKey(ModConfigs.getServerConfig().statsMultiplier, "Multiplicador de Estadísticas");

        //Advancements
        addAdvancement("root", "\u00a1Bienvenido a Kingdom Keys!", "Instala Kingdom Keys");
        addAdvancement("to_soa", "Es esto... \u00bfmi coraz\u00f3n?!", "Descende a tu coraz\u00f3n");
        addAdvancement("choice", "\u00bfUna espada, bast\u00f3n y escudo?", "Haz una elecci\u00f3n");
        addAdvancement("obtain_recipe", "Mejor se lo llevo a un Moguri", "Obt\u00e9n una receta");
        addAdvancement("obtain_projector", "El sacrificio era inevitable", "Obt\u00e9n un proyector");
        addAdvancement("summon_keyblade", "\u00bfSoy el elegido?", "Invoca la llave espada");
        addAdvancement("upgrade_keyblade", "Todav\u00eda puede ser m\u00e1s fuerte", "Mejora tu llave espada usando la Forja");
        addAdvancement("levelup1", "\u00a1Wow!", "Sube de nivel");
        addAdvancement("levelup50", "A medio camino", "Alcanza el nivel 50");
        addAdvancement("levelup100", "\u00a1En la cima!", "Alcanza el nivel 100");
        addAdvancement("obtain_drive", "\u00bfUna fusi\u00f3n?", "Obt\u00e9n tu primera forma de fusi\u00f3n");
        addAdvancement("obtain_keychain", "Estos moguris saben como trabajar", "Obt\u00e9n tu primer llavero");
        addAdvancement("obtain_kiblade", "\u00a1Una llave para gobernarlos a todos!", "Obt\u00e9n la legendaria Llave Espada \u03c7");
        addAdvancement("obtain_magic", "\u00a1Como un hechicero!", "Obt\u00e9n tu primera magia");
        addAdvancement("obtain_org", "Nadie visti\u00f3 esto", "Obt\u00e9n la ropa de la Organizaci\u00f3n");
        addAdvancement("to_rod", "La oscuridad te consumir\u00e1", "S\u00e9 absorbido al Reino de la Oscuridad");
        addAdvancement("obtain_winner_stick","Supongo que mis vacaciones de verano...", "Obtén un palito de ganador");
        addAdvancement("munny_millionaire","¡Platinario!", "Acumula 1.000.000 de munny");
        addAdvancement("dual_wield_oblivion_oathkeeper","Lazos de luz y oscuridad", "Blande Oblivion y Oathkeeper al mismo tiempo");
        addAdvancement("get_stick","Eso es un palo", "Fabrica un palo de madera");
        addAdvancement("get_struggle_weapon","Empezando con dificultades", "Fabrica un bate de Struggle");
        addAdvancement("get_pauldron","Armadura a voluntad", "Obtén una hombrera");
        addAdvancement("play_music_disc","Sonando ahora", "Obtén uno de los discos de música del mod");
        addAdvancement("open_menu","Descubriendo el menú", "Abre el menú principal");
        addAdvancement("obtain_all_drive_forms","Maestro de las Formas", "Obtén todas las Formas de Fusión");
        addAdvancement("max_keyblade_level","Forjada a fuego", "Sube una llave espada hasta su nivel máximo");
        addAdvancement("reach_castle_oblivion","Solo quedan recuerdos", "Pon un pie en el Castillo del Olvido");
        addAdvancement("craft_estelleste_skull","La mente maestra", "Fabrica una calavera tributo a Estelleste");
        addAdvancement("craft_abelatox_skull","El culpado", "Fabrica una calavera tributo a Abelatox");
        addAdvancement("craft_wyndftw_skull","El olvidado", "Fabrica una calavera tributo a wyndftw");
        addAdvancement("craft_stel1034_skull","El escultor", "Fabrica una calavera tributo a stel312");
        addAdvancement("craft_xephirovt_skull","La mano amiga", "Fabrica una calavera tributo a XephiroVT");
        addAdvancement("all_dev_skulls","Salón de los creadores", "Fabrica las calaveras tributo de todos los creadores");

        //Keybinds
        add("key.categories.kingdomkeys", "Kingdom Keys");
        add(InputHandler.Keybinds.ACTION, "Tecla de Acci\u00f3n");
        add(InputHandler.Keybinds.BACK, "Men\u00fa de Comandos: Atr\u00e1s");
        add(InputHandler.Keybinds.ENTER, "Men\u00fa de Comandos: Acceder");
        add(InputHandler.Keybinds.SCROLL_ACTIVATOR, "Controlar Men\u00fa de Comandos con Rat\u00f3n");
        add(InputHandler.Keybinds.SCROLL_UP, "Men\u00fa de Comandos: Subir");
        add(InputHandler.Keybinds.SCROLL_DOWN, "Men\u00fa de Comandos: Bajar");
        add(InputHandler.Keybinds.SUMMON_KEYBLADE, "Invocar Llave Espada");
        add(InputHandler.Keybinds.SUMMON_ARMOR, "Invocar Armadura");
        add(InputHandler.Keybinds.REACTION_COMMAND, "Comando de Reacci\u00f3n");
        add(InputHandler.Keybinds.LOCK_ON, "Fijar");
        add(InputHandler.Keybinds.OPENMENU, "Abrir Men\u00fa");
        add(InputHandler.Keybinds.SHOW_GUI, "Mostrar HUD");
        add(InputHandler.Keybinds.LOCK_ON_SWAP, "Cambiar objetivo fijado");


        //Tabs
        add("itemGroup.kingdomkeys", "Kingdom Keys");
        add("itemGroup.kingdomkeys_keyblades", "Kingdom Keys: Llaves Espada");
        add("itemGroup.kingdomkeys_keychains", "Kingdom Keys: Llaveros");
        add("itemGroup.kingdomkeys_organization", "Kingdom Keys: Organización");
        add("itemGroup.kingdomkeys_armors", "Kingdom Keys: Armaduras");
        add("itemGroup.kingdomkeys_equipables", "Kingdom Keys: Equipables");
        add("itemGroup.kingdomkeys_gummi", "Kingdom Keys: Bloques Gummi");
        add("itemGroup.kingdomkeys_mats", "Kingdom Keys: Materiales de Síntesis");
        add("itemGroup.kingdomkeys_cards", "Kingdom Keys: Cartas");
        add("itemGroup.kingdomkeys_misc", "Kingdom Keys: Misc");

        //Death messages
        add("keybladedamage.death", "%s fue asesinado por %s");
        add("death.attack.air", "%1$s fue aireado por %2$s");
        add("death.attack.air.item", "%1$s fue aireado por %2$s usando %3$s");
        add("death.attack.darkness", "%2$s llev\u00f3 a %1$s a la oscuridad");
        add("death.attack.darkness.item", "%2$s llev\u00f3 a %1$s a la oscuridad usando %3$s");
        add("death.attack.fire", "%1$s sinti\u00f3 el calor de %2$s");
        add("death.attack.fire.item", "%1$s sinti\u00f3 el calor de %2$s usando %3$s");
        add("death.attack.ice", "%1$s fue congelado por %2$s");
        add("death.attack.ice.item", "%1$s fue congelado por %2$s usando %3$s");
        add("death.attack.light", "%2$s le ense\u00f1o a %1$s que Kingdom Hearts es luz");
        add("death.attack.light.item", "%2$s le ense\u00f1o a %1$s que Kingdom Hearts es luz usando %3$s");
        add("death.attack.lightning", "%2$s peg\u00f3 calambrazo a %1$s, \u00a1electrizante!");
        add("death.attack.lightning.item", "%2$s peg\u00f3 calambrazo a %1$s usando %3$s, \u00a1electrizante!");
        add("death.attack.offhand", "%1$s fue asesinado por %2$s");
        add("death.attack.offhand.item", "%1$s fue asesinado por %2$s usando %3$s");
        add("death.attack.stop", "%2$s detuvo la vida de %1$s");
        add("death.attack.stop.item", "%2$s detuvo la vida de %1$s usando %3$s");
        add("death.attack.water", "%1$s intent\u00f3 bailar con el agua de %2$s");
        add("death.attack.water.item", "%1$s intent\u00f3 bailar con el agua de %2$s usando %3$s");

        //Containers(Menus)
        add("container.magical_chest", "Cofre M\u00e1gico");
        add("container.pedestal", "Pedestal");
        add("container.gummi_hangar", "Hangar Gummi");

        add("container.gummi_hangar.hasbannedblocks", "La estructura contiene bloques prohibidos: ");
        add("container.gummi_hangar.doesntcontaincore", "La estructura no tiene un núcleo");
        add("container.gummi_hangar.singlecore", "La estructura solo puede tener un núcle, detectados ");
        add("container.gummi_hangar.shiptoobig","Esta Nave Gummi es demasiado grande");

        add("container.gummi_hangar.gummifound","Ya hay una Nave Gummi en la zona");
        add("container.gummi_hangar.gummitoobig","Hay una Nave Gummi demasiado grande para el hangar en la zona");
        add("container.gummi_hangar.noname","Necesitas nombrar tu Nave Gummi");
        add("container.gummi_hangar.moveshipfw","Mover nave hacia delante");
        add("container.gummi_hangar.moveshipbw","Mover nave hacia atrás");
        add("container.gummi_hangar.moveshipleft","Mover nave hacia la izquierda");
        add("container.gummi_hangar.moveshipright","Mover nave hacia la derecha");
        add("container.gummi_hangar.moveshiphigher","Mover nave hacia arriba");
        add("container.gummi_hangar.moveshiplower","Mover nave hacia abajo");

        add("container.gummi_hangar.blueprinttoobig","Los planos son demasiado grandes para este hangar");
        add("container.gummi_hangar.noblueprintimp","Debes meter unos planos para importarlos");
        add("container.gummi_hangar.noblueprintname","Debes especificar un nombre para los planos");
        add("container.gummi_hangar.noblueprintexp","Debes meter unos planos para exportarlos");

        add("container.gummi_hangar.build","Ensamblar");
        add("container.gummi_hangar.edit","Modificar");
        add("container.gummi_hangar.import","Importar");
        add("container.gummi_hangar.export","Exportar");

        add("container.gummi_hangar.power","Pot. motora");
        add("container.gummi_hangar.firepower","Pot. ofensiva");
        add("container.gummi_hangar.weight","Peso");
        add("container.gummi_hangar.armor","Coraza");
        add("container.gummi_hangar.effectivespeed","Vel. Efectiva");
        add("container.gummi_hangar.seats","Asientos");
        add("container.gummi_hangar.mobility","Movilidad");

        //Gummi Ship HUD
        add("container.gummi_ship.forward", "Acelerar");
        add("container.gummi_ship.backwards", "Marcha atrás");
        add("container.gummi_ship.left", "Izquierda");
        add("container.gummi_ship.right", "Derecha");
        add("container.gummi_ship.up", "Subir");
        add("container.gummi_ship.down", "Bajar");
        add("container.gummi_ship.boost", "Turbo");
        add("container.gummi_ship.3d_flight", "Vuelo 3D");

        add("container.gummi_ship.coords", "Coords");
        add("container.gummi_ship.facing", "Orientación");

        add("container.gummi_ship.fuel", "Combustible");
        add("container.gummi_ship.speed", "Velocidad");
        add("container.gummi_ship.eng_power", "Potencia motora");
        add("container.gummi_ship.armor", "Coraza");
        add("container.gummi_ship.numofweapons", "Núm. de armas");

        add("container.gummi_ship.ready", "Listo");
        add("container.gummi_ship.not_ready", "No listo");

        //GUI
        //Main Menu
        add(Gui_Menu_Back, "Atr\u00e1s");
        add(Gui_Menu_Back + ".desc", "Vuelve al men\u00fa anterior.");
        add(Gui_Menu_Accept, "Aceptar");
        add(Gui_Menu_Main_Title, "Men\u00fa");

        //Main Menu Buttons
        add(Gui_Menu_Main_Button_Items, "Objetos");
        add(Gui_Menu_Main_Button_Items + ".desc", "Accede a los equipables (arma, pociones...) y a tu inventario.");
        add(Gui_Menu_Main_Button_Abilities, "Habilidades");
        add(Gui_Menu_Main_Button_Abilities + ".desc", "Equipa o desequipa tus habilidades.");
        add(Gui_Menu_Main_Button_Customize, "Configuraci\u00f3n");
        add(Gui_Menu_Main_Button_Customize + ".desc", "Configura los atajos de magia.");
        add(Gui_Menu_Main_Button_Party, "Grupo");
        add(Gui_Menu_Main_Button_Party + ".desc", "Crea y gestiona tu grupo.");
        add(Gui_Menu_Main_Button_Status, "Condici\u00f3n");
        add(Gui_Menu_Main_Button_Status + ".desc", "Revisa tus estad\u00edsticas.");
        add(Gui_Menu_Main_Button_Journal, "Diario");
        add(Gui_Menu_Main_Button_Journal + ".desc", "");
        add(Gui_Menu_Main_Button_Config, "Preferencias");
        add(Gui_Menu_Main_Button_Config + ".desc", "Configura varios aspectos gr\u00e1ficos de los elementos en pantalla.");
        add(Gui_Menu_Main_Button_Style, "Estilo de combate");
        add(Gui_Menu_Main_Button_Style + ".desc", "Establece tu estilo de combate con Epic Fight");

        //Main Menu Status Bar
        add(Gui_Menu_Main_Synthesis_Tier, "S\u00edntesis");
        add(Gui_Menu_Main_Munny, "Platines");
        add(Gui_Menu_Main_Hearts, "Corazones");
        add(Gui_Menu_Main_Time, "Hora del Mundo");
        add(Gui_Menu_Main_Time_Spent, "Tiempo total");

        //Items Sub-menu
        add(Gui_Menu_Items, "Objetos");
        add(Gui_Menu_Items_Equipment, "Equipamiento");
        add(Gui_Menu_Items_Equipment + ".desc", "Equipa tu arma y varios equipables.");
        add(Gui_Menu_Items_Melding, "Síntesis");
        add(Gui_Menu_Items_Melding + ".desc", "Combine distintos hechizos para crear de nuevos.");
        add(Gui_Menu_Items_Melding_Meld, "Sintetizar");
        add(Gui_Menu_Items_Melding_Meldables, "Filtrar");
        add(Gui_Menu_Items_Melding_ItemAcquired, "¡Item Obtenido!");
        add(Gui_Menu_Items_Melding_RareItemAcquired, "¡Item Especial Obtenido!");
        add(Gui_Menu_Items_Stock, "Inventario");
        add(Gui_Menu_Items_Stock + ".desc", "Revisa tu inventario.");
        add(Gui_Menu_Items_Equipment_Weapon, "Arma");
        add(Gui_Menu_Items_Equipment_Weapon_Keyblades, "Llaves Espada");
        add(Gui_Menu_Items_Equipment_Shotlock, "Tiro Certero");
        add(Gui_Menu_Items_Equipment_Accessories, "Accesorios");
        add(Gui_Menu_Items_Equipment_Armor, "Armadura");
        add(Gui_Menu_Items_Equipment_Magic, "Magias");
        add(Gui_Menu_Items_Equipment_Pauldron, "Hombrera");
        add(Gui_Menu_Items_Equipment_Items, "Objetos");

        //Customize Sub-menu
        add(Gui_Menu_Customize, "Configuración");
        add(Gui_Menu_Customize + ".shortcuts", "Atajos");
        add(Gui_Menu_Customize + ".shortcut", "Atajo");
        add(Gui_Menu_Customize + ".magic", "Visibilidad de Magias");
        add(Gui_Menu_Customize + ".unequip", "Desequipar");

        //Party Sub-menu
        add(Gui_Menu_Party, "Grupo");
        add(Gui_Menu_Party_Create, "Crear Grupo");
        add(Gui_Menu_Party_Create + ".desc", "Crea un nuevo grupo.");
        add(Gui_Menu_Party_Create_Name, "Nombre");
        add(Gui_Menu_Party_Create_Accessibility, "Accessibilidad y l\u00edmite");
        add(Gui_Menu_Party_Create_Accessibility_Public, "P\u00fablica");
        add(Gui_Menu_Party_Create_Accessibility_Private, "Privada");
        add(Gui_Menu_Party_Join, "Unirte a Grupo");
        add(Gui_Menu_Party_Join + ".desc", "\u00fanete a un grupo ya creado.");
        add(Gui_Menu_Party_Leader_Invite, "Invitar");
        add(Gui_Menu_Party_Leader_Settings, "Opciones");
        add(Gui_Menu_Party_Leader_Promote, "Promover");
        add(Gui_Menu_Party_Leader_Kick, "Echar");
        add(Gui_Menu_Party_Leader_Disband, "Disolver");
        add(Gui_Menu_Party_Member_Leave, "Abandonar");

        //Status Sub-menu
        add(Gui_Menu_Status, "Condición");
        add(Gui_Menu_Status_Choice, "Elección");
        add(Gui_Menu_Status_Level, "Nivel");
        add(Gui_Menu_Status_TotalExp, "Experiencia");
        add(Gui_Menu_Status_NextLevel, "Siguiente Niv.");
        add(Gui_Menu_Status_HP, "VT");
        add(Gui_Menu_Status_MP, "PM");
        add(Gui_Menu_Status_AP, "PH");
        add(Gui_Menu_Status_DriveGauge, "Indicador de Fusi\u00f3n");
        add(Gui_Menu_Status_Strength, "Fuerza");
        add(Gui_Menu_Status_Magic, "Magia");
        add(Gui_Menu_Status_Defense, "Defensa");
        add(Gui_Menu_Status_FireRes, "Resistencia Piro");
        add(Gui_Menu_Status_BlizzardRes, "Resistencia Hielo");
        add(Gui_Menu_Status_ThunderRes, "Resistencia Electro");
        add(Gui_Menu_Status_LightRes, "Resistencia Luz");
        add(Gui_Menu_Status_AirRes, "Resistencia Aero");
        add(Gui_Menu_Status_WaterRes, "Resistencia Aqua");
        add(Gui_Menu_Status_DarkRes, "Resistencia Oscuridad");
        add(Gui_Menu_Status_FireResShort, "Res. Piro");
        add(Gui_Menu_Status_BlizzardResShort, "Res. Hielo");
        add(Gui_Menu_Status_ThunderResShort, "Res. Elec.");
        add(Gui_Menu_Status_LightResShort, "Res. Luz");
        add(Gui_Menu_Status_AirResShort, "Res. Aero");
        add(Gui_Menu_Status_WaterResShort, "Res. Aqua");
        add(Gui_Menu_Status_DarkResShort, "Res. Osc.");
        add(Gui_Menu_Status_FormLevel, "Niv. de Forma");
        add(Gui_Menu_Status_FormGauge, "Indicador de Forma");
        add(Gui_Menu_Status_Abilities, "Habilidades");
        add(Gui_Menu_Status_Ability, "Habilidad");

        //Config Sub-menu
        add(Gui_Menu_Config, "Preferencias");
        add(Gui_Menu_Config + ".bg", "Fondo");
        add(Gui_Menu_Config + ".hud", "Ajustar HUD");
        add(Gui_Menu_Config + ".reset_defaults", "Reset por defecto");
        add(Gui_Menu_Config + ".reset_rp", "Reset de resource pack");
        add(Gui_Menu_Config + ".hud.help0", "Mantén %s para ver ayuda");
        add(Gui_Menu_Config + ".hud.help1", "Primero selecciona el punto de anclaje seleccionando el elemento y ESPACIO");
        add(Gui_Menu_Config + ".hud.help2", "CLICK IZQUIERDO y arrastra un elemento para moverlo");
        add(Gui_Menu_Config + ".hud.help3", "Usa las FLECHAS para moverlo en incrementos grandes");
        add(Gui_Menu_Config + ".hud.help4", "Mantén CTRL + FLECHAS para moverlo en incrementos pequeños");
        add(Gui_Menu_Config + ".hud.help5", "Usa RUEDA DE SCROLL para escalarlo");
        add(Gui_Menu_Config + ".hud.help6", "Mantén X + RUEDA DE SCROLL para escalarlo horizontalmente");
        add(Gui_Menu_Config + ".hud.help7", "Mantén Y + RUEDA DE SCROLL para escalarlo verticalmente");
        add(Gui_Menu_Config + ".hud.help8", "Usa SHIFT + RUEDA DE SCROLL para rotarlo");
        add(Gui_Menu_Config + ".hud.help9", "Pulsa ALT IZQUIERDO para mostrar u ocultar recuadros");
        add(Gui_Menu_Config + ".hud.help10", "CLICK DERECHO en un elemento seleccionado para resetearlo a valores de Resourcepack");
        add(Gui_Menu_Config + ".hud.help11", "SHIFT + CLICK DERECHO en un elemento seleccionado para resetearlo a valores por defecto");
        add(Gui_Menu_Config + ".hud.help12", "Datos del elemento seleccionado:");
        add(Gui_Menu_Config + ".font", "Fuente");
        add(Gui_Menu_Config + ".command_menu", "Men\u00fa Comandos");
        add(Gui_Menu_Config + ".hp", "Barra VT");
        add(Gui_Menu_Config + ".mp", "Barra PM");
        add(Gui_Menu_Config + ".dp", "Barra Fusi\u00f3n");
        add(Gui_Menu_Config + ".player_skin", "Skin Jugador");
        add(Gui_Menu_Config + ".lock_on_hp", "Fijar mob");
        add(Gui_Menu_Config + ".party", "Grupo");
        add(Gui_Menu_Config + ".focus", "Barra Tino");
        add(Gui_Menu_Config + ".custom_font", "Fuente personalizada");
        add(Gui_Menu_Config + ".classic_colors", "Colores cl\u00e1sicos");
        add(Gui_Menu_Config + ".x_scale", "Escala X");
        add(Gui_Menu_Config + ".y_scale", "Escala Y");
        add(Gui_Menu_Config + ".x_pos", "Posici\u00f3n X");
        add(Gui_Menu_Config + ".selected_x_pos", "Margen X Seleccionado");
        add(Gui_Menu_Config + ".y_pos", "Posici\u00f3n Y");
        add(Gui_Menu_Config + ".y_dist", "Distancia Y");
        add(Gui_Menu_Config + ".sub_x_offset", "Pos. X extra Submenu");
        add(Gui_Menu_Config + ".header_title", "T\u00edtulo de Cabecera");
        add(Gui_Menu_Config + ".text_x_offset", "Pos. X del Texto extra");
        add(Gui_Menu_Config + ".hp_scale", "Escala de Barra de VT");
        add(Gui_Menu_Config + ".icon_scale", "Escala del \u00edcono fijaci\u00f3n");
        add(Gui_Menu_Config + ".icon_rotation", "Velocidad de Rotaci\u00f3n del \u00edcono fijaci\u00f3n");
        add(Gui_Menu_Config + ".hp_per_bar", "VT por barra");
        add(Gui_Menu_Config + ".show_hearts", "Mostrar corazones");
        add(Gui_Menu_Config + ".hp_alarm", "Volumen de Alarma de VT Baja");
        add(Gui_Menu_Config + ".impexp", "Importar/Exportar");
        add(Gui_Menu_Config + ".impexp.import", "Importar");
        add(Gui_Menu_Config + ".impexp.export", "Exportar al portapapeles");
        add(Gui_Menu_Config + ".notif_color", "Notificaci\u00f3n");
        add(Gui_Menu_Config + ".armor.red", "Armadura roja");
        add(Gui_Menu_Config + ".armor.green", "Armadura verde");
        add(Gui_Menu_Config + ".armor.blue", "Armadura azul");
        add(Gui_Menu_Config + ".armor.glint", "Brillo de Armadura");

        //Journal
        add(Gui_Menu_Journal, "Diario");

        //Combat Style
        add(Gui_Menu_Style, "Estilo de combate");
        add(Gui_Menu_Style + ".single", "Una mano");
        add(Gui_Menu_Style + ".dual", "Dos manos");
        add(Gui_Menu_Style + ".sora", "Sora");
        add(Gui_Menu_Style + ".roxas", "Roxas");
        add(Gui_Menu_Style + ".riku", "Riku");
        add(Gui_Menu_Style + ".terra", "Terra");
        add(Gui_Menu_Style + ".aqua", "Aqua");
        add(Gui_Menu_Style + ".ventus", "Ventus");
        add(Gui_Menu_Style + ".kh2roxasdual", "Roxas (KH2)");
        add(Gui_Menu_Style + ".daysroxasdual", "Roxas (358/2 Days)");

        //Synthesis
        add(Gui_Synthesis, "Sintetizador");
        add(Gui_Synthesis_Exp, "Exp");
        add(Gui_Synthesis_Exp_MoogleLevel, "Nivel de Moguri");
        add(Gui_Synthesis_Exp_NextLevel, "Sig. nivel");
        add(Gui_Synthesis_Synthesise, "Fabricar");
        add(Gui_Synthesis_Synthesise_Title, "Fabricar");
        add(Gui_Synthesis_Synthesise_Create, "Crear");
        add(Gui_Synthesis_Forge_Upgrade, "Mejorar");
        add(Gui_Synthesis_Forge, "Forja");
        add(Gui_Synthesis_Forge_Title, "Forja");
        add(Gui_Synthesis_Materials, "Materiales");
        add(Gui_Synthesis_Materials_Deposit, "Depositar");
        add(Gui_Synthesis_Materials_Take, "Sacar");

        //Shop
        add(Gui_Shop, "Tienda");
        add(Gui_Shop_Buy, "Comprar");
        add(Gui_Shop_Sell, "Vender");
        add(Gui_Shop_Buy_Price, "Precio:");
        add(Gui_Shop_Page, "P\u00e1gina");
        add(Gui_Shop_NoSpace, "No tienes espacio");
        add(Gui_Shop_Tier, "Clase");
        add(Gui_Shop_Main_Title, "Tienda");
        add(Gui_Shop_Buy_Cost, "Coste");
        add(Gui_Synthesis_Moogle_Name, "Tienda Moguri de %s");

        //Command Menu
        add(Gui_CommandMenu_Command, "COMANDO");
        add(Gui_CommandMenu_Attack, "Atacar");
        add(Gui_CommandMenu_Portal, "Portal");
        add(Gui_CommandMenu_Magic, "Magia");
        add(Gui_CommandMenu_Items, "Objetos");
        add(Gui_CommandMenu_Drive, "Fusi\u00f3n");
        add(Gui_CommandMenu_Drive_Revert, "Revertir");
        add(Gui_CommandMenu_Limit, "L\u00edmite");
        add(Gui_CommandMenu_Target,"Objetivo");
        add(Gui_CommandMenu_Portals_Title, "PORTAL");
        add(Gui_CommandMenu_Magic_Title, "MAGIA");
        add(Gui_CommandMenu_Items_Title, "OBJETOS");
        add(Gui_CommandMenu_Drive_Title, "FORMAS");
        add(Gui_CommandMenu_Limit_Title, "LÍMITES");

        add("kingdomkeys.helmet", "Casco");
        add("kingdomkeys.chestplate", "Pechera");
        add("kingdomkeys.leggings", "Grebas");
        add("kingdomkeys.boots", "Botas");

        //Synthesis Bag
        add("gui.synthesisbag.upgrade", "Mejorar tamaño");
        add("gui.synthesisbag.munny", "Platines");
        add("gui.synthesisbag.notenoughmunny", "No tienes suf. platines");

        add("gui.statboost.increased","%s aumentada, ahora es %s");
        add("gui.statboost.tooltip", "aumenta %s en 1");

        add("gui.magicspell.equip","Equípalo en el menú para usarlo");
        add("gui.magicspell.exp","Experiencia: %s/%s");
        add("gui.magicspell.exp_short","Exp: %s/%s");
        add("gui.magicspell.lvl_short","Nv. %s");


        add("gui.driveformorb.tooltip", "Mejora Forma %s");
        add("gui.driveformorb.upgrade", "Forma %s ha obtenido %s exp");

        //Spells bag
        add("gui.spellsbag.complain","Solo puedes tener una sola bolsa de hechizos en el inventario");
        add("gui.cardssbag.complain","Solo puedes tener una sola bolsa de cartas en el inventario");

        //Proof of Heart
        add("gui.proofofheart.desc", "Úsalo para salir de la Organización XIII");
        add("gui.proofofheart.desc2", "No podrás usarlo si llevas la ropa de la Organización XIII");
        add("gui.proofofheart.notinorg", "No estás en la Organización XIII");
        add("gui.proofofheart.leftorg", "Has salido de la Organización XIII");
        add("gui.proofofheart.unequip", "Primero desequípate la ropa de la Organización XIII");

        //Organization XIII
        add("gui.org.line1", "Al llevar el Ropaje Oscuro te vuelves un miembro de la Organización XIII.");
        add("gui.org.line2", "Elige un miembro de la Organización XIII para alinearte con él.");
        add("gui.org.line3", "Tu elección determinará tu arma inicial.");
        add("gui.org.line4", "Deseas alinearte con %1$s?");
        add("gui.org.line5", "Costará de cambiar dicha decisión.");
        add("gui.org.ok", "Ok");
        add("gui.org.select", "Seleccionar");
        add("gui.org.cancel", "Cancelar");
        add("gui.org.confirm", "Confirmar");

        //Save point
        add(Gui_Save_Creation_Title, "Nombre punto de guardado");
        add(Gui_Save_Creation_Prompt, "Introduce un nombre para este punto de guardado");
        add(Gui_Save_Creation_Global, "Establecer visibilidad global");
        add(Gui_Save_Creation_Global_Desc, "Convierte este punto en accesible para todos los jugadores");
        add(Gui_Save_Creation_Accept, "Guardar");

        add(Gui_Save_Main_CurrentPosition, "Est\u00e1s aqu\u00ed");
        add(Gui_Save_Main_Sort, "Ordenar:");
        add(Gui_Save_Main_Rename, "Nombre");
        add(Gui_Save_Main_Retake, "Foto");

        add(Gui_Save_Sorting_ByRecent, "Reciente");
        add(Gui_Save_Sorting_ByName, "Nombre");
        add(Gui_Save_Sorting_ByDimension, "Dimensi\u00f3n");
        add(Gui_Save_Sorting_ByOwner, "Due\u00f1o");
        add(Gui_Save_Sorting_Ascending, "Ascendiente");
        add(Gui_Save_Sorting_Descending, "Descendiente");


        //K.O. Screen
        add(Gui_KO_Die, "Give Up");
        add(Gui_KO_Quit, "Exit");

        //Level up messages
        add(Stats_LevelUp_Str, "\u00a1Fuerza aumentada!");
        add(Stats_LevelUp_Def, "\u00a1Defensa aumentada!");
        add(Stats_LevelUp_Magic, "\u00a1Magia aumentada!");
        add(Stats_LevelUp_HP, "\u00a1VT M\u00e1xima aumentada!");
        add(Stats_LevelUp_MP, "\u00a1PM M\u00e1ximos aumentados!");
        add(Stats_LevelUp_AP, "\u00a1PH M\u00e1ximos aumentados!");
        add(Stats_LevelUp_FormGauge, "\u00a1Aumenta el indic. de Forma!");
        add(Stats_LevelUp_MaxAccessories, "\u00a1Espacio para accesorios +1!");
        add(Stats_LevelUp_MaxArmors, "\u00a1Espacio para armaduras +1!");
        add(Stats_LevelUp_MaxMagics,"\u00a1Espacio para hechizos +1!");
        add(Stats_LevelNext, "Sig. nivel");
        add(Stats_MunnyGet, "¡Platines!");

        //Abilities
        addAbilityWithDesc(ModAbilities.AUTO_VALOR, "Auto-valent\u00eda", "En casos de emergencia, si la Forma valiente est\u00e1 disponible, el comando de reacci\u00f3n cambia a Valiente.");
        addAbilityWithDesc(ModAbilities.AUTO_WISDOM, "Auto-sabia", "En casos de emergencia, si la Forma sabia est\u00e1 disponible, el comando de reacci\u00f3n cambia a Sabia.");
        addAbilityWithDesc(ModAbilities.AUTO_LIMIT, "Auto-suma", "En casos de emergencia, si la Forma suma est\u00e1 disponible, el comando de reacci\u00f3n cambia a Suma.");
        addAbilityWithDesc(ModAbilities.AUTO_MASTER, "Auto-maestra", "En casos de emergencia, si la Forma maestra est\u00e1 disponible, el comando de reacci\u00f3n cambia a Maestra.");
        addAbilityWithDesc(ModAbilities.AUTO_FINAL, "Auto-final", "En casos de emergencia, si la Forma final est\u00e1 disponible, el comando de reacci\u00f3n cambia a Final.");
        addAbilityWithDesc(ModAbilities.STRIKE_RAID, "Tiro Mort\u00edfero", "Click derecho mientras te agachas para lanzar tu Llave Espada, usa 10PM.");
        addAbilityWithDesc(ModAbilities.FLOWSTEP, "Paso fluido", "Click derecho mientras fijas un tiro certero para usar el modo ágil hacia el último enemigo fijado.");

        addGrowthAbility(ModAbilities.HIGH_JUMP, "¡Ahora saltarás más alto!", "Salto de altura 1", "Salto de altura 2", "Salto de altura 3", "Salto de altura MÁX");
        addGrowthAbility(ModAbilities.QUICK_RUN, "Si pulsas la tecla de acción mientras corres harás un sprint.", "Carrera rápida 1", "Carrera rápida 2", "Carrera rápida 3", "Carrera rápida MÁX");
        addGrowthAbility(ModAbilities.DODGE_ROLL, "Si pulsas la tecla de acción mientras caminas darás una voltereta.", "Voltereta 1", "Voltereta 2", "Voltereta 3", "Voltereta MÁX");
        addGrowthAbility(ModAbilities.AERIAL_DODGE, "En el aire puedes volver a saltar para dar un doble salto.", "Regate aéreo 1", "Regate aéreo 2", "Regate aéreo 3", "Regate aéreo MÁX.");
        addGrowthAbility(ModAbilities.GLIDE, "En el aire, mantén pulsada la tecla de salto para planear.", "Planeador 1", "Planeador 2", "Planeador 3", "Superplaneador");
        addAbilityWithDesc(ModAbilities.AIR_SLIDE, "Impulso aéreo", "Si pulsas la tecla de acción mientras estás en el aire podrás impulsarte hacia delante. Cuantos más activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.WALL_KICK,"Rebote","Usa impulso aéreo contra una pared para propulsarte con ella y entrar en el Modo acróbata. Cuantos más activas, más rebotes.");
        addAbilityWithDesc(ModAbilities.SUPERJUMP, "Supersalto", "En el Modo acróbata, pulsa el botón de salto para saltar muy, muy alto. Cuantos más activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.SUPERSLIDE, "Superdeslizamiento", "En el Modo acróbata, pulsa la tecla de acción para darte mucho impulso en esa dirección. Cuantos más activas, mayor efecto.");

        addAbilityWithDesc(ModAbilities.SCAN, "Libra", "Muestra la VT actual del enemigo fijado.");
        addAbilityWithDesc(ModAbilities.ZERO_EXP, "Experiencia 0", "No ganas experiencia al derrotar enemigos.");
        addAbilityWithDesc(ModAbilities.MP_HASTE, "Prisa PM", "Si consumes todos los PM, aumenta la velocidad de recuperaci\u00f3n de los mismos. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.MP_HASTERA, "Prisa PM+", "Si consumes todos los PM, aumenta mucho la velocidad de recuperaci\u00f3n de los mismos. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.MP_HASTEGA, "Prisa PM++", "Si consumes todos los PM, aumenta much\u00edsimo la velocidad de recuperaci\u00f3n de los mismos. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.MP_RAGE,  "Aspirar Da\u00f1o", "Recupera PM en proporci\u00f3n al da\u00f1o que recibes. Si activas m\u00e1s, el efecto aumentar\u00e1.");
        addAbilityWithDesc(ModAbilities.DAMAGE_CONTROL, "Control de Da\u00f1o", "Recibes la mitad del da\u00f1o cuando tu VT sea igual o inferior al 25%. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.DAMAGE_DRIVE, "Fusi\u00f3n por da\u00f1o", "Llena el indicador de Fusi\u00f3n cada vez que sufres da\u00f1o. La recarga es proporcional al da\u00f1o recibido.");
        addAbilityWithDesc(ModAbilities.DRIVE_BOOST, "Extrafusi\u00f3n", "Permite una recuperaci\u00f3n m\u00e1s r\u00e1pida del indicador de Fusi\u00f3n durante la Carga PM. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.FORM_BOOST, "Extraforma", "Aumenta la duraci\u00f3n de cada Forma de Fusi\u00f3n. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.FIRE_BOOST, "Extrapiro", "Aumenta el da\u00f1o de los ataques Piro. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.BLIZZARD_BOOST, "Extrahielo", "Aumenta el da\u00f1o de los ataques Hielo. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.WATER_BOOST, "Extraaqua", "Aumenta el da\u00f1o de los ataques Aqua. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.THUNDER_BOOST, "Extraelectro", "Aumenta el da\u00f1o de los ataques Electro. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.TREASURE_MAGNET, "Atracci\u00f3n", "Acerca y recoge objetos cercanos. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.EXPERIENCE_BOOST, "Extraexperiencia", "Si la VT cae a menos de la mitad, aumenta un 100% la experiencia ganada al vencer enemigos. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.ENCOUNTER_PLUS, "Más Encuentros", "Aumenta la cantidad de enemigos que aparecen a tu alrededor. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.SECOND_CHANCE, "\u00daltimo Estertor", "Asegura un punto de vitalidad si recibes da\u00f1os severos.");
        addAbilityWithDesc(ModAbilities.LIGHT_AND_DARKNESS, "Luz y Oscuridad", "Proporciona un poder tan misterioso como poderoso.");
        addAbilityWithDesc(ModAbilities.SYNCH_BLADE, "Espada Doble", "Tendr\u00e1s una arma en cada mano. Dispondr\u00e1s tambi\u00e9n de las habilidades de la mano izquierda.");
        addAbilityWithDesc(ModAbilities.MP_SAFETY, "Seguridad PM", "Impide entrar en Carga PM cuando se acaben los PM usando los atajos, excepto si se usa Cura");
        addAbilityWithDesc(ModAbilities.DRIVE_CONVERTER, "Cambia Fusi\u00f3n", "Incrementa el valor de todos los premios de Fusi\u00f3n");
        addAbilityWithDesc(ModAbilities.FOCUS_CONVERTER, "Cambia Tino", "Incrementa el valor de todos los premios Tino");
        addAbilityWithDesc(ModAbilities.FULL_MP_BLAST, "Magia Extrapotente", "Cuando tengas todos tus PM, se aumenta el poder de la primera habilidad m\u00e1gica que uses en un 50%. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.WIZARDS_RUSE, "Ardid de Hechicero", "Permite recuperar VT proporcional a los PM que consumes. Cuantas m\u00e1s activas, m\u00e1s posibilidades de que ocurra.");
        addAbilityWithDesc(ModAbilities.EXTRA_CAST, "Un hechizo m\u00e1s", "Permite usar una \u00faltima magia antes de quedarte sin PM.");
        addAbilityWithDesc(ModAbilities.MP_THRIFT, "Ahorrar PM", "Reduce el coste m\u00e1gico en un 20%. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.CRITICAL_BOOST, "Extracr\u00edticos", "Aumenta el da\u00f1o inflingido por golpes cr\u00edticos en un 10%. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.JACKPOT, "M\u00e1s Premios", "Incrementa el valor de los premios de VT, PM y Platines. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.LUCKY_STRIKE, "Buena Suerte", "Trae suerte, fortuna y saqueo al portador, aumentando la cantidad de objetos recolectados. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.ITEM_BOOST, "Extraobjetos", "Aumenta el efecto de los objetos de cura en la batalla en un 50%. Cuantos m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.FIRAZA, "Piro+++", "Permite al usuario obtener el comando de reacci\u00f3n Piro+++.");
        addAbilityWithDesc(ModAbilities.BLIZZAZA, "Hielo+++", "Permite al usuario obtener el comando de reacci\u00f3n Hielo+++.");
        addAbilityWithDesc(ModAbilities.WATERZA, "Aqua+++", "Permite al usuario obtener el comando de reacci\u00f3n Aqua+++.");
        addAbilityWithDesc(ModAbilities.THUNDAZA, "Electro+++", "Permite al usuario obtener el comando de reacci\u00f3n Electro+++.");
        addAbilityWithDesc(ModAbilities.CURAZA, "Cura+++", "Permite al usuario obtener el comando de reacci\u00f3n Cura+++.");
        addAbilityWithDesc(ModAbilities.GRAND_MAGIC_HASTE, "Gran Prisa M\u00e1gica", "Otorga al usuario una probabilidad m\u00e1s alta para invocar una magia mejorada. Cuantas m\u00e1s activas, mayor probabilidad.");
        addAbilityWithDesc(ModAbilities.GRAND_MAGIC_EXTENDER, "Gran magia extra", "Aumenta el tiempo de disponibilidad de los comandos de Gran magia. Cuantas más activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.BERSERK_CHARGE, "Carga de Locura", "Otorga al usuario +2 de Fuerza durante la Recarga de PM. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.LEAF_BRACER, "Hoja Sana", "Utilizar Cura continuar\u00e1 a\u00fan que te ataquen.");
        addAbilityWithDesc(ModAbilities.HP_GAIN, "Saqueo VT", "Recuperas VT cuando impacta un Tiro Certero. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.ENDLESS_MAGIC, "Combo ahorramagia", "Reduce el tiempo de descanso entre magia y magia. Cuantas m\u00e1s activas, mayor efecto.");
        addAbilityWithDesc(ModAbilities.DARK_DOMINATION, "Dominio Oscuro", "Permite al usuario controlar la Antiforma a voluntad.");
        addAbilityWithDesc(ModAbilities.MAGIC_LOCK_ON, "Blanco Fijo M\u00e1gico", "Permite al usuario invocar magias en la posici\u00f3n de la entidad fijada.");
        addAbilityWithDesc(ModAbilities.COMBO_PLUS, "Combo Plus", "Increases maximum combo by 1 when on the ground. Equip more to enable more combos");
        addAbilityWithDesc(ModAbilities.NEGATIVE_COMBO, "Negative Combo", "Decreases maximum combo on the ground and in midair by 1. Equip more to increase the effect.");
        addAbilityWithDesc(ModAbilities.FINISHING_PLUS, "Finishing Plus", "Unleash successive finishing moves after combos.");
        addAbilityWithDesc(ModAbilities.PROTECT, "Protect", "Absorbs 10% of the damage taken.");
        addAbilityWithDesc(ModAbilities.PROTECTRA, "Protectra", "Absorbs 20% of the damage taken.");
        addAbilityWithDesc(ModAbilities.PROTECTGA, "Protectga", "Absorbs 40% of the damage taken.");

        //Magic
        add(ModMagic.FIRE.get(), "Piro");
        add(ModMagic.FIRA.get(), "Piro+");
        add(ModMagic.FIRAGA.get(), "Piro++");
        add(ModMagic.FIRAZA.get(), "Piro+++");

        add(ModMagic.BLIZZARD.get(), "Hielo");
        add(ModMagic.BLIZZARA.get(), "Hielo+");
        add(ModMagic.BLIZZAGA.get(), "Hielo++");
        add(ModMagic.BLIZZAZA.get(), "Hielo+++");

        add(ModMagic.WATER.get(), "Aqua");
        add(ModMagic.WATERA.get(), "Aqua+");
        add(ModMagic.WATERGA.get(), "Aqua++");
        add(ModMagic.WATERZA.get(), "Aqua+++");

        add(ModMagic.THUNDER.get(), "Electro");
        add(ModMagic.THUNDARA.get(), "Electro+");
        add(ModMagic.THUNDAGA.get(), "Electro++");
        add(ModMagic.THUNDAZA.get(), "Electro+++");

        add(ModMagic.CURE.get(), "Cura");
        add(ModMagic.CURA.get(), "Cura+");
        add(ModMagic.CURAGA.get(), "Cura++");
        add(ModMagic.CURAZA.get(), "Cura+++");

        add(ModMagic.AERO.get(), "Aero");
        add(ModMagic.AERORA.get(), "Aero+");
        add(ModMagic.AEROGA.get(), "Aero++");

        add(ModMagic.MAGNET.get(), "Magneto");
        add(ModMagic.MAGNERA.get(), "Magneto+");
        add(ModMagic.MAGNEGA.get(), "Magneto++");

        add(ModMagic.REFLECT.get(), "Reflejo");
        add(ModMagic.REFLERA.get(), "Reflejo+");
        add(ModMagic.REFLEGA.get(), "Reflejo++");

        add(ModMagic.GRAVITY.get(), "Gravedad");
        add(ModMagic.GRAVIRA.get(), "Gravedad+");
        add(ModMagic.GRAVIGA.get(), "Gravedad++");

        add(ModMagic.STOP.get(), "Paro");
        add(ModMagic.STOPRA.get(), "Paro+");
        add(ModMagic.STOPGA.get(), "Paro++");

        add(ModMagic.ZERO_GRAVITY.get(), "Ingravidez");
        add(ModMagic.ZERO_GRAVIRA.get(), "Ingravidez+");
        add(ModMagic.ZERO_GRAVIGA.get(), "Ingravidez++");



        add(ModMagic.DARK_FIRAGA.get(),"Nigro Piro++");
        add(ModMagic.TRIPLE_FIRAGA.get(),"Triple Piro++");
        add(ModMagic.CRAWLING_FIRAGA.get(),"Tardo Piro++");
        add(ModMagic.FISSION_FIRAGA.get(),"Lluvia Piro++");
        add(ModMagic.FIRAGA_BURST.get(),"Descarga Ígnea");
        add(ModMagic.IGNITE.get(),"Ignición");

        add(ModMagic.TRIPLE_BLIZZAGA.get(),"Triple Hielo++");
        add(ModMagic.DEEP_FREEZE.get(),"Hipotermia");
        add(ModMagic.GLACIER.get(),"Glaciar");
        add(ModMagic.ICE_BARRAGE.get(),"Asalto Gélido");

        add(ModMagic.THUNDAGA_SHOT.get(),"Tiro Electro++");
        add(ModMagic.TRIPLE_PLASMA.get(),"Triplasma");

        add(ModMagic.BLACKOUT.get(),"Apagón");
        add(ModMagic.POISON.get(),"Toxis");

        add(ModMagic.BALLOON.get(), "Globo");
        add(ModMagic.BALLOONRA.get(), "Globo+");
        add(ModMagic.BALLOONGA.get(), "Globo++");

        add(ModMagic.SPARK.get(), "Chispa");
        add(ModMagic.SPARKRA.get(), "Chispa+");
        add(ModMagic.SPARKGA.get(), "Chispa++");

        add(ModMagic.MINE_SHIELD.get(), "Escudo de Minas");
        add(ModMagic.MINE_SQUARE.get(), "Cuadro de Minas");
        add(ModMagic.SEEKER_MINE.get(), "Mina Astuta");

        add(ModMagic.WARP.get(),"Exilio");
        add(ModMagic.FAITH.get(),"Sanctus");
        add(ModMagic.ESUNA.get(),"Esna");
        add(ModMagic.CONFUSE.get(),"Confu");
        add(ModMagic.BIND.get(),"Enlace");
        add(ModMagic.MINI.get(),"Minimalia");
        add(ModMagic.SLOW.get(),"Freno");


        //Limits
        addLimit(ModLimits.LASER_CIRCLE, "Círculo Láser");
        addLimit(ModLimits.LASER_DOME, "Cúpula Láser");
        addLimit(ModLimits.ARROW_RAIN, "Lluvia Flechada");
        addLimit(ModLimits.SLOW_THUNDER_TRAIL, "Estela Relámpago Lenta");
        addLimit(ModLimits.FAST_THUNDER_TRAIL, "Estela Relámpago Veloz");
        addLimit(ModLimits.FLAME_WHEEL, "Anillo de Llamas");
        addLimit(ModLimits.FIRE_WALL, "Jaula de Fuego");
        addLimit(ModLimits.LANCE_STORM, "Tormenta de Lanzas");
        addLimit(ModLimits.FALLING_SPEAR, "Lanza Caída");
        addLimit(ModLimits.BERSERK_CLAYMORE, "Claymore Salvaje");
        addLimit(ModLimits.POWERUP, "Potenciación");
        addLimit(ModLimits.ROCKY_PILLARS, "Pilares Rocosos");
        addLimit(ModLimits.ICE_PILLARS, "Pilares Gélidos");
        addLimit(ModLimits.WATER_TRAIL, "Estela de Agua");
        addLimit(ModLimits.WATER_WALL, "Jaula de Agua");
        addLimit(ModLimits.CARD_RING, "Jaula de Cartas");
        addLimit(ModLimits.SCYTHE_DASH, "Esprint de Guadaña");
        addLimit(ModLimits.PETAL_VOID, "Vacío de Pétalos");
        addLimit(ModLimits.LIGHT_BARRAGE, "Ráfaga de Luz");
        addLimit(ModLimits.ILLUSORY_METEOR, "Meteoro Ilusorio");

        //Shotlocks
        addShotlock(ModShotlocks.RAGNAROK, "Ragnarok");
        addShotlock(ModShotlocks.DARK_VOLLEY, "Volea Umbría");
        addShotlock(ModShotlocks.PRISM_RAIN, "Chorro Irisado");
        addShotlock(ModShotlocks.SONIC_SHADOW, "Sombra Sónica");
        addShotlock(ModShotlocks.ULTIMA_CANNON, "Cañón Artema");
        addShotlock(ModShotlocks.METEOR_SHOWER, "Lluvia Meteórica");
        addShotlock(ModShotlocks.FLAME_SALVO, "Salva Ígnea");
        addShotlock(ModShotlocks.ABSOLUTE_ZERO, "Cero Absoluto");
        addShotlock(ModShotlocks.THUNDERSTORM, "Tronada");
        addShotlock(ModShotlocks.CHAOS_SNAKE, "Súbito Caos");
        addShotlock(ModShotlocks.BUBBLE_BLASTER, "Tiro Burbuja");
        addShotlock(ModShotlocks.BIO_BARRAGE, "Biodescarga");
        addShotlock(ModShotlocks.PULSE_BOMB, "Bomba de Pulso");
        addShotlock(ModShotlocks.PHOTON_CHARGE, "Carga Fotónica");
        addShotlock(ModShotlocks.LIGHTNING_RAY, "Rayo Célere");

        //Blox
        addBlock(ModBlocks.normalBlox, "Bloque Normal");
        addBlock(ModBlocks.hardBlox, "Bloque Duro");
        addBlock(ModBlocks.metalBlox, "Bloque Met\u00e1lico");
        addBlock(ModBlocks.dangerBlox, "Bloque Peligroso");
        addBlock(ModBlocks.bounceBlox, "Bloque de Rebote");
        addBlock(ModBlocks.blastBlox, "Bloque Explosivo");
        addBlock(ModBlocks.ghostBlox, "Bloque Fantasma");
        addBlock(ModBlocks.prizeBlox, "Bloque de Premio");
        addBlock(ModBlocks.rarePrizeBlox, "Bloque de Premio Raro");
        addBlock(ModBlocks.magnetBlox, "Bloque Magn\u00e9tico");
        addBlock(ModBlocks.pairBlox, "Bloque Emparejado");
        addBlock(ModBlocks.infestedNormalBlox, "Bloque Normal Infestado");
        addBlock(ModBlocks.gummiMeteor, "Meteorito Gummi");
        addBlock(ModBlocks.magicTarget, "Diana Mágica");

        //Ores
        addBlock(ModBlocks.blazingOre, "Mena \u00edgnea");
        addBlock(ModBlocks.blazingOreN, "Mena \u00edgnea del Nether");
        addBlock(ModBlocks.blazingOreD, "Mena \u00edgnea de pizarra profunda");
        addBlock(ModBlocks.soothingOre, "Mena Vital");
        addBlock(ModBlocks.soothingOreD, "Mena Vital de pizarra profunda");
        addBlock(ModBlocks.writhingOre, "Mena Tortuosa");
        addBlock(ModBlocks.writhingOreN, "Mena Tortuosa del Nether");
        addBlock(ModBlocks.writhingOreE, "Mena Tortuosa del End");
        addBlock(ModBlocks.writhingOreD, "Mena Tortuosa de pizarra profunda");
        addBlock(ModBlocks.betwixtOre, "Mena Neutra");
        addBlock(ModBlocks.betwixtOreD, "Mena Neutra de pizarra profunda");
        addBlock(ModBlocks.betwixtOreE, "Mena Neutra del End");
        addBlock(ModBlocks.wellspringOre, "Mena de Poder");
        addBlock(ModBlocks.wellspringOreN, "Mena de Poder del Nether");
        addBlock(ModBlocks.frostOre, "Mena Fr\u00eda");
        addBlock(ModBlocks.frostOreD, "Mena Fr\u00eda de pizarra profunda");
        addBlock(ModBlocks.lucidOre, "Mena Clara");
        addBlock(ModBlocks.lightningOre, "Mena Luminosa");
        addBlock(ModBlocks.pulsingOre, "Mena de Fuerza");
        addBlock(ModBlocks.pulsingOreD, "Mena de Fuerza de pizarra profunda");
        addBlock(ModBlocks.pulsingOreE, "Mena de Fuerza del End");
        addBlock(ModBlocks.remembranceOre, "Mena Evocadora");
        addBlock(ModBlocks.hungryOre, "Mena Voraz");
        addBlock(ModBlocks.sinisterOre, "Mena Siniestra");
        addBlock(ModBlocks.sinisterOreD, "Mena Siniestra de pizarra profunda");
        addBlock(ModBlocks.stormyOre, "Mena Recia");
        addBlock(ModBlocks.stormyOreD, "Mena Recia de pizarra profunda");
        addBlock(ModBlocks.tranquilityOre, "Mena Sosegada");
        addBlock(ModBlocks.twilightOre, "Mena Crepuscular");
        addBlock(ModBlocks.twilightOreD, "Mena Crepuscular de pizarra profunda");
        addBlock(ModBlocks.twilightOreN, "Mena Crepuscular del Nether");

        //Other
        addBlock(ModBlocks.mosaic_stained_glass, "Mosaico de Cristal Tintado");
        addBlock(ModBlocks.orgPortal, "Portal de la Organizaci\u00f3n");
        addBlock(ModBlocks.moogleProjector, "Proyector de Moguri");
        addBlock(ModBlocks.struggleBoard, "Tablón de Combate");
        addBlock(ModBlocks.station_of_awakening_core, "N\u00facleo de Estaci\u00f3n del Despertar");
        addBlock(ModBlocks.magicalChest, "Cofre M\u00e1gico");
        addBlock(ModBlocks.pedestal, "Pedestal");
        addBlock(ModBlocks.savepoint, "Punto de Guardado");
        add("block." + MODID + ".linked_savepoint", "Punto de Guardado Enlazado");
        add("block." + MODID + ".warp_point", "Punto de Guardado Interdimensional");
        addBlock(ModBlocks.soADoor, "Puerta Misteriosa");
        addBlock(ModBlocks.gummiHangar, "Hangar Gummi");
        addBlock(ModBlocks.sorCore, "N\u00facleo de Estaci\u00f3n del Pesar");
        addBlock(ModBlocks.dataPortal, "Portal de Datos");
        addBlock(ModBlocks.airstepTarget, "Punto de modo \u00e1gil");

        add("savepoint.drive", "recuperaci\u00f3n de fusi\u00f3n");
        add("savepoint.feed", "alimentaci\u00f3n");
        add("savepoint.focus", "recuperaci\u00f3n de tino");
        add("savepoint.healing", "curaci\u00f3n");
        add("savepoint.magic", "recuperaci\u00f3n de magia");
        add("savepoint.max_upgrade", "\u00faltimo nivel alcanzado");
        add("savepoint.maxed", "Velocidad de %s est\u00e1 al m\u00e1ximo");
        add("savepoint.upgrade", "La velocidad de %s est\u00e1 al %s%%");
        add("savepoint.upgrade_type", "Punto de guardado mejorado a %s");
        add("savepoint.unavailable","%s no está disponible en este punto de guardado");


        //Castle Oblivion
        addBlock(ModBlocks.cardDoor, "Puerta de Carta");
        addBlock(ModBlocks.structureWall, "Pared de Estructura");
        addBlock(ModBlocks.castleOblivionWall, "Bloque del Olvido");
        addBlock(ModBlocks.castleOblivionWallChiseled, "Bloque del Olvido chiselado");
        addBlock(ModBlocks.castleOblivionWall2, "Bloque del Olvido oscuro");
        addBlock(ModBlocks.castleOblivionWall3, "Bloque del Olvido sombrío");
        addBlock(ModBlocks.castleOblivionPillar, "Pilar del Olvido");
        addBlock(ModBlocks.castleOblivionStairs, "Escaleras del Olvido");
        addBlock(ModBlocks.castleOblivionSlab, "Losa del Olvido");

        //Realm of Darkness
        addBlock(ModBlocks.rodCrackedStone, "Piedra Oscura Agrietada");
        addBlock(ModBlocks.rodSand, "Arena Oscura");
        addBlock(ModBlocks.rodStone, "Piedra Oscura");

        //Gummi
        //Angular
        addTintedBlock(ModBlocks.gummiCubes, "Gummi Angular (Cubo %s)");
        addTintedBlock(ModBlocks.gummiShellCubes, "Gummi Escudo (Cubo %s)");
        addTintedBlock(ModBlocks.gummiDispelCubes, "Gummi Antimagia (Cubo %s)");
        addTintedBlock(ModBlocks.gummiWedges, "Gummi Angular (Cuña %s)");
        addTintedBlock(ModBlocks.gummiShellWedges, "Gummi Escudo (Cuña %s)");
        addTintedBlock(ModBlocks.gummiDispelWedges, "Gummi Antimagia (Cuña %s)");
        addTintedBlock(ModBlocks.gummiPyramids, "Gummi Angular (Pirámide %s)");
        addTintedBlock(ModBlocks.gummiShellPyramids, "Gummi Escudo (Pirámide %s)");
        addTintedBlock(ModBlocks.gummiDispelPyramids, "Gummi Antimagia (Pirámide %s)");
        //Tubulares
        addTintedBlock(ModBlocks.gummiCylinders, "Gummi Tubular (Cilindro %s)");
        addTintedBlock(ModBlocks.gummiShellCylinders, "Gummi Escudo (Cilindro %s)");
        addTintedBlock(ModBlocks.gummiDispelCylinders, "Gummi Antimagia (Cilindro %s)");
        addTintedBlock(ModBlocks.gummiDomes, "Gummi Tubular (Domo %s)");
        addTintedBlock(ModBlocks.gummiShellDomes, "Gummi Escudo (Domo %s)");
        addTintedBlock(ModBlocks.gummiDispelDomes, "Gummi Antimagia (Domo %s)");
        addTintedBlock(ModBlocks.gummiCones, "Gummi Tubular (Cono %s)");
        addTintedBlock(ModBlocks.gummiShellCones, "Gummi Escudo (Cono %s)");
        addTintedBlock(ModBlocks.gummiDispelCones, "Gummi Antimagia (Cono %s)");
        //Curvos
        addTintedBlock(ModBlocks.gummiPies, "Gummi Curvo (Tarta %s)");
        addTintedBlock(ModBlocks.gummiShellPies, "Gummi Escudo (Tarta %s)");
        addTintedBlock(ModBlocks.gummiDispelPies, "Gummi Antimagia (Tarta %s)");
        addTintedBlock(ModBlocks.gummiRoundCorners, "Gummi Curvo (Corner %s)");
        addTintedBlock(ModBlocks.gummiShellRoundCorners, "Gummi Escudo (Corner %s)");
        addTintedBlock(ModBlocks.gummiDispelRoundCorners, "Gummi Antimagia (Corner %s)");
        //Cockpits
        addTintedBlock(ModBlocks.gummiBubbleHelms, "Gummi Casco curvo (%s)");
        //Weapons
        addBlock(ModBlocks.gummiFire, "Gummi Piro");
        addBlock(ModBlocks.gummiFira, "Gummi Piro+");
        addBlock(ModBlocks.gummiBlizzard, "Gummi Hielo");
        addBlock(ModBlocks.gummiBlizzara, "Gummi Hielo+");
        addBlock(ModBlocks.gummiGravity, "Gummi Gravedad");
        addBlock(ModBlocks.gummiGravira, "Gummi Gravedad+");
        addBlock(ModBlocks.gummiWater, "Gummi Aqua");
        addBlock(ModBlocks.gummiWatera, "Gummi Aqua+");

        addBlock(ModBlocks.gummiCore, "Núcleo Gummi");

        //Aeros
        addTintedBlock(ModBlocks.gummiAeroTriangles, "Gummi Aero (Triángulo %s)");
        addTintedBlock(ModBlocks.gummiAeroSquares, "Gummi Aero (Cuadrado %s)");

        //Engines
        addBlock(ModBlocks.gummiVernier, "Gummi Envión");
        addBlock(ModBlocks.gummiThruster, "Gummi Cohete");

        //Thrusters: Envión, Cohete, Impulso
        //Canons: Gummi Electro, Gummi Electro+, Gummi Electro++,
        //Lasers: Cometa, Meteo, Artema
        //Cockpits: Gummi Cura, Gummi Cura+?, Gummi Cura++, Gummi Lázaro (revive)

        // Drive Forms
        addDriveForm(ModDriveForms.VALOR, "Valiente");
        addDriveForm(ModDriveForms.WISDOM, "Sabia");
        addDriveForm(ModDriveForms.LIMIT, "Suma");
        addDriveForm(ModDriveForms.MASTER, "Maestra");
        addDriveForm(ModDriveForms.FINAL, "Final");
        addDriveForm(ModDriveForms.ANTI, "Antiforma");

        // Reaction Commands
        addReactionCommand(ModReactionCommands.AUTO_VALOR, "Auto-valiente");
        addReactionCommand(ModReactionCommands.AUTO_WISDOM, "Auto-sabia");
        addReactionCommand(ModReactionCommands.AUTO_LIMIT, "Auto-suma");
        addReactionCommand(ModReactionCommands.AUTO_MASTER, "Auto-maestra");
        addReactionCommand(ModReactionCommands.AUTO_FINAL, "Auto-final");
        addReactionCommand(ModReactionCommands.SAVE, "Guardar");

        //Items
        //Cards
        //Rings
        addItem(ModItems.abilityRing, "Anillo Diestro");
        addItem(ModItems.aquamarineRing, "Anillo de Aguamarina");
        addItem(ModItems.cosmicArts, "Arte C\u00f3smica");
        addItem(ModItems.fullBloom, "Plenaflor");
        addItem(ModItems.fullBloomPlus, "Plenaflor+");
        addItem(ModItems.shadowArchive, "C\u00f3dice Tenebroso");
        addItem(ModItems.shadowArchivePlus, "C\u00f3dice Tenebroso+");
        addItem(ModItems.drawRing, "Anillo Magn\u00e9tico");
        addItem(ModItems.executiveRing, "Anillo Supremo");
        addItem(ModItems.starCharm, "Talism\u00e1n Estelar");
        addItem(ModItems.luckyRing, "Anillo Afortunado");

        addItem(ModItems.fireBangle, "Brazal Piro");
        addItem(ModItems.blizzardArmlet, "Pulsera Hielo");
        addItem(ModItems.thunderTrinket, "Colgante Electro");
        addItem(ModItems.petiteRibbon, "Peque\u00f1a Insignia");
        addItem(ModItems.ribbon, "Insignia");
        addItem(ModItems.grandRibbon, "Insignia Ilustre");

        addItem(ModItems.abasChain, "Cadena de Abas");
        addItem(ModItems.acrisius, "Acrisius");
        addItem(ModItems.acrisiusPlus, "Acrisius+");
        addItem(ModItems.aegisChain, "Cadena de Aegis");
        addItem(ModItems.blizzaraArmlet, "Pulsera Hielo+");
        addItem(ModItems.blizzagaArmlet, "Pulsera Hielo++");
        addItem(ModItems.blizzagunArmlet, "Pulsera Hielo+++");
        addItem(ModItems.powerBand, "Banda de Poder");
        addItem(ModItems.busterBand, "Banda Inmune");
        addItem(ModItems.championBelt, "Cinto de Campe\u00f3n");
        addItem(ModItems.chaosAnklet, "Cadena del Caos");
        addItem(ModItems.cosmicBelt, "Cinto C\u00f3smico");
        addItem(ModItems.cosmicChain, "Cadena C\u00f3smica");
        addItem(ModItems.darkAnklet, "Cadena Oscura");
        addItem(ModItems.divineBandanna, "Pa\u00f1uelo Divino");
        addItem(ModItems.elvenBandanna, "Pa\u00f1uelo de Duende");
        addItem(ModItems.firaBangle, "Brazal Piro+");
        addItem(ModItems.firagaBangle, "Brazal Piro++");
        addItem(ModItems.firagunBangle, "Brazal Piro+++");
        addItem(ModItems.protectBelt, "Cinto Protector");
        addItem(ModItems.gaiaBelt, "Cinto de Gaia");
        addItem(ModItems.midnightAnklet, "Cadena Nocturna");
        addItem(ModItems.shadowAnklet, "Cadena de Sombra");
        addItem(ModItems.shockCharm, "Dije Galv\u00e1nico");
        addItem(ModItems.shockCharmPlus, "Dije Galv\u00e1nico+");
        addItem(ModItems.thundaraTrinket, "Colgante Electro+");
        addItem(ModItems.thundagaTrinket, "Colgante Electro++");
        addItem(ModItems.thundagunTrinket, "Colgante Electro+++");

        addItem(ModItems.engineersRing, "Anillo Inventor");
        addItem(ModItems.techniciansRing , "Anillo T\u00e9cnico");
        addItem(ModItems.skillRing , "Anillo H\u00e1bil");
        addItem(ModItems.skillfulRing , "Anillo Talentoso");
        addItem(ModItems.expertsRing , "Anillo de Experto");
        addItem(ModItems.mastersRing , "Anillo Maestro");
        addItem(ModItems.cosmicRing , "Anillo C\u00f3smico");
        addItem(ModItems.sardonyxRing , "Anillo de \u00f3nice");
        addItem(ModItems.goldRing , "Anillo de Oro");
        addItem(ModItems.garnetRing , "Anillo Granate");
        addItem(ModItems.diamondRing , "Anillo de Diamante");
        addItem(ModItems.silverRing , "Anillo de Plata");
        addItem(ModItems.tourmalineRing , "Anillo de Turmalina");
        addItem(ModItems.platinumRing , "Anillo de Platino");
        addItem(ModItems.mythrilRing , "Anillo de Mitrilo");
        addItem(ModItems.orichalcumRing , "Anillo de Orichalcum");
        addItem(ModItems.medal , "Medalla");
        addItem(ModItems.soldierEarring , "Aro Guerrero");
        addItem(ModItems.mageEarring , "Aro de Mago");
        addItem(ModItems.moonAmulet , "Amuleto Lunar");
        addItem(ModItems.slayerEarring , "Aro Asesino");
        addItem(ModItems.fencerEarring , "Aro Espadach\u00edn");

        //Command menu items
        add("item.kingdomkeys.potion", "Poci\u00f3n");
        add("item.kingdomkeys.hi_potion", "Ultrapoci\u00f3n");
        add("item.kingdomkeys.mega_potion", "Omnipoci\u00f3n");
        add("item.kingdomkeys.ether", "Éter");
        add("item.kingdomkeys.hi_ether", "Ultra\u00e9ter");
        add("item.kingdomkeys.mega_ether", "Omni\u00e9ter");
        add("item.kingdomkeys.elixir", "Elixir");
        add("item.kingdomkeys.mega_lixir", "Omnielixir");
        add("item.kingdomkeys.drive_recovery", "Recarga de Fusi\u00f3n");
        add("item.kingdomkeys.hi_drive_recovery", "Recarga de Fusi\u00f3n+");
        add("item.kingdomkeys.refocuser", "Carga Tino");
        add("item.kingdomkeys.hi_refocuser", "Ultracarga Tino");
        add("item.kingdomkeys.panacea", "Panacea");
        add("item.kingdomkeys.apboost", "Impulso H\u00e1bil");
        add("item.kingdomkeys.powerboost", "Impulso de Vigor");
        add("item.kingdomkeys.magicboost", "Impulso M\u00e1gico");
        add("item.kingdomkeys.defenseboost", "Impulso Defensivo");

        add("potion.desc.panacea", "Elimina todos los efectos negativos");
        add("potion.desc.hp", "\u00A7aVT\u00A7r");
        add("potion.desc.mp", "\u00A79PM\u00A7r");
        add("potion.desc.hpmp", "\u00A7aVT\u00A7r y \u00A79PM\u00A7r");
        add("potion.desc.drive", "\u00A7eFusi\u00f3n\u00A7r");
        add("potion.desc.focus", "\u00A76Tino\u00A7r");
        add("potion.desc.beginning", "Restaurar\u00e1 %s%s de %s ");
        add("potion.desc.toall", "a todos los miembros del grupo en rango");
        add("potion.desc.toone", "al miembro del grupo elegido");

        // Spell orbs
        addItem(ModItems.fireSpell, "Hechizo de Piro");
        addItem(ModItems.blizzardSpell, "Hechizo de Hielo");
        addItem(ModItems.waterSpell, "Hechizo de Aqua");
        addItem(ModItems.thunderSpell, "Hechizo de Electro");
        addItem(ModItems.cureSpell, "Hechizo de Cura");
        addItem(ModItems.aeroSpell, "Hechizo de Aero");
        addItem(ModItems.magnetSpell, "Hechizo de Magneto");
        addItem(ModItems.reflectSpell, "Hechizo de Reflejo");
        addItem(ModItems.gravitySpell, "Hechizo de Gravedad");
        addItem(ModItems.stopSpell, "Hechizo de Paro");

        addItem(ModItems.firaSpell, "Hechizo de Piro+");
        addItem(ModItems.blizzaraSpell, "Hechizo de Hielo+");
        addItem(ModItems.wateraSpell, "Hechizo de Aqua+");
        addItem(ModItems.thundaraSpell, "Hechizo de Electro+");
        addItem(ModItems.curaSpell, "Hechizo de Cura+");
        addItem(ModItems.aeroraSpell, "Hechizo de Aero+");
        addItem(ModItems.magneraSpell, "Hechizo de Magneto+");
        addItem(ModItems.refleraSpell, "Hechizo de Reflejo+");
        addItem(ModItems.graviraSpell, "Hechizo de Gravedad+");
        addItem(ModItems.stopraSpell, "Hechizo de Paro+");

        addItem(ModItems.firagaSpell, "Hechizo de Piro++");
        addItem(ModItems.blizzagaSpell, "Hechizo de Hielo++");
        addItem(ModItems.watergaSpell, "Hechizo de Aqua++");
        addItem(ModItems.thundagaSpell, "Hechizo de Electro++");
        addItem(ModItems.curagaSpell, "Hechizo de Cura++");
        addItem(ModItems.aerogaSpell, "Hechizo de Aero++");
        addItem(ModItems.magnegaSpell, "Hechizo de Magneto++");
        addItem(ModItems.reflegaSpell, "Hechizo de Reflejo++");
        addItem(ModItems.gravigaSpell, "Hechizo de Gravedad++");
        addItem(ModItems.stopgaSpell, "Hechizo de Paro++");

        addItem(ModItems.zeroGravitySpell, "Hechizo de Ingravidez");
        addItem(ModItems.zeroGraviraSpell, "Hechizo de Ingravidez+");
        addItem(ModItems.zeroGravigaSpell, "Hechizo de Ingravidez++");

        addItem(ModItems.darkFiragaSpell, "Hechizo de Nigro Piro++");
        addItem(ModItems.tripleFiragaSpell, "Hechizo de Triple Piro++");
        addItem(ModItems.crawlingFiragaSpell, "Hechizo de Tardo Piro++");
        addItem(ModItems.fissionFiragaSpell, "Hechizo de Lluvia Píro++");
        addItem(ModItems.firagaBurstSpell, "Hechizo de Descarga Ígnea");
        addItem(ModItems.igniteSpell, "Hechizo de Ignición");

        addItem(ModItems.tripleBlizzagaSpell, "Hechizo de Triple Hielo++");
        addItem(ModItems.deepFreezeSpell, "Hechizo de Hipotermia");
        addItem(ModItems.glacierSpell, "Hechizo de Glaciar");
        addItem(ModItems.iceBarrageSpell, "Hechizo de Asalto Gélido");

        addItem(ModItems.thundagaShotSpell, "Hechizo de Tiro Electro++");
        addItem(ModItems.triplePlasmaSpell, "Hechizo de Triplasma");


        addItem(ModItems.blackoutSpell, "Hechizo de Apagón");
        addItem(ModItems.poisonSpell, "Hechizo de Toxis");


        addItem(ModItems.balloonSpell, "Hechizo de Globo");
        addItem(ModItems.balloonraSpell, "Hechizo de Globo+");
        addItem(ModItems.balloongaSpell, "Hechizo de Globo++");

        addItem(ModItems.sparkSpell, "Hechizo de Chispa");
        addItem(ModItems.sparkraSpell, "Hechizo de Chispa+");
        addItem(ModItems.sparkgaSpell, "Hechizo de Chispa++");

        addItem(ModItems.mineShieldSpell, "Hechizo de Escudo de Minas");
        addItem(ModItems.mineSquareSpell, "Hechizo de Cuadro de Minas");
        addItem(ModItems.mineSeekerSpell, "Hechizo de Mina Astuta");

        addItem(ModItems.warpSpell, "Hechizo de Exilio");
        addItem(ModItems.faithSpell, "Hechizo de Sanctus");
        addItem(ModItems.esunaSpell, "Hechizo de Esna");
        addItem(ModItems.confuseSpell, "Hechizo de Confu");
        addItem(ModItems.bindSpell, "Hechizo de Enlace");
        addItem(ModItems.miniSpell, "Hechizo de Minimalia");
        addItem(ModItems.slowSpell, "Hechizo de Freno");


        // Drive form orbs
        addItem(ModItems.valorOrb, "Orbe de Forma Valiente");
        addItem(ModItems.wisdomOrb, "Orbe de Forma Sabia");
        addItem(ModItems.limitOrb, "Orbe de Forma Suma");
        addItem(ModItems.masterOrb, "Orbe de Forma Maestra");
        addItem(ModItems.finalOrb, "Orbe de Forma Final");

        // Other
        addItem(ModItems.recipe, "Receta");
        addItem(ModItems.recipeD, "Receta de clase D");
        addItem(ModItems.recipeC, "Receta de clase C");
        addItem(ModItems.recipeB, "Receta de clase B");
        addItem(ModItems.recipeA, "Receta de clase A");
        addItem(ModItems.recipeS, "Receta de clase S");
        addItem(ModItems.recipeSS, "Receta de clase SS");
        addItem(ModItems.recipeSSS, "Receta de clase SSS");
        addItem(ModItems.iceCream, "Helado de Sal Marina");
        addItem(ModItems.winnerStick, "Palito de ganador");
        addItem(ModItems.synthesisBag, "Bolsa de Síntesis");
        addItem(ModItems.magicsBag, "Bolsa de Hechizos");
        addItem(ModItems.cardsBag, "Bolsa de Cartas");
        addItem(ModItems.proofOfHeart, "Prueba de Corazón");
        addItem(ModItems.wayfinder, "Siemprejuntos");
        addItem(ModItems.trainingDummy, "Espantapájaros de entrenamiento");


        //Patchouli
        add("patchouli.kingdomkeys.journal.name", "Diario de Pepito");
        add("patchouli.kingdomkeys.journal.desc", "Dar las gracias a Naminé.");
        add("patchouli.kingdomkeys.journal.hello", "Bienvenido, portador de la Llave Espada, al mod de $(thing)Kingdom Keys$().$(p)En este libro, encontrarás toda la información que necesitas para comenzar, así como consejos útiles.");

        //Music discs
        add("disc.duration.desc", "Duraci\u00f3n");
        add("disc.durationunits.desc", "(mins:secs)");
        add("disc.composedby", "Compuesta por");
        addMusicDisc(ModItems.disc_Birth_by_Sleep_A_Link_to_the_Future, "Birth by Sleep -A Link to the Future-", "Yoko Shimomura & Kaoru Wada");
        addMusicDisc(ModItems.disc_Dream_Drop_Distance_The_Next_Awakening, "Dream Drop Distance -The Next Awakening-", "Yoko Shimomura & Kaoru Wada");
        addMusicDisc(ModItems.disc_Hikari_KINGDOM_Instrumental_Version, "Hikari -KINGDOM Instrumental Version-", "Utada Hikaru & arreglo por Kaoru Wada");
        addMusicDisc(ModItems.disc_L_Oscurita_Dell_Ignoto, "L'Oscurita Dell'Ignoto", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Musique_pour_la_tristesse_de_Xion, "Musique pour la tristesse de Xion", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_No_More_Bugs_Bug_Version, "No More Bugs!! -Bug Version-", "Yoko Shimomura & Hirosato Noda");
        addMusicDisc(ModItems.disc_Organization_XIII, "Organization XIII", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Dearly_Beloved_UX, "Dearly Beloved -Union \u03c7 Credits Version-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Passion_Instrumental, "Passion -Kingdom Orchestra Instrumental Version-", "Yoko Shimomura & arreglo por Kaoru Wada");
        addMusicDisc(ModItems.disc_Rage_Awakened, "Rage Awakened", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_The_Other_Promise, "The Other Promise", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_13th_Struggle_Luxord, "13th Struggle -Luxord-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_13th_Dilemma_Saix, "13th Dilemma -Saix-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_13th_Reflection, "13th Reflection", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Another_Side_Battle_Ver, "Another Side -Battle Ver-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Cavern_Of_Remembrance_Days, "Cavern of Remembrance -Days Version-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Forgotten_Challenge_Recoded, "Forgotten Challenge -Re:Coded Version-", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Anger_Unchained, "Anger Unchained", "Takeharu Ishimoto");
        addMusicDisc(ModItems.disc_Hunter_Of_The_Dark, "Hunter of the Dark", "Yoko Shimomura");
        addMusicDisc(ModItems.disc_Destati, "Destati", "Yoko Shimomura");

        //Armour
        add("gui.summonarmor.notenoughspace", "No tienes suficiente espacio en el inventario");

        addItem(ModItems.organizationRobe_Helmet, "Capucha de la Organizaci\u00f3n");
        addItem(ModItems.organizationRobe_Chestplate, "Abrigo de la Organizaci\u00f3n");
        addItem(ModItems.organizationRobe_Leggings, "Pantalones de la Organizaci\u00f3n");
        addItem(ModItems.organizationRobe_Boots, "Botas de la Organizaci\u00f3n");

        addItem(ModItems.terra_Helmet, "Casco de Terra");
        addItem(ModItems.terra_Chestplate, "Pechera de Terra");
        addItem(ModItems.terra_Leggings, "Grebas de Terra");
        addItem(ModItems.terra_Boots, "Botas de Terra");
        addItem(ModItems.terra_Shoulder, "Hombrera de Terra");

        addItem(ModItems.aqua_Helmet, "Casco de Aqua");
        addItem(ModItems.aqua_Chestplate, "Pechera de Aqua");
        addItem(ModItems.aqua_Leggings, "Grebas de Aqua");
        addItem(ModItems.aqua_Boots, "Botas de Aqua");
        addItem(ModItems.aqua_Shoulder, "Hombrera de Aqua");

        addItem(ModItems.ventus_Helmet, "Casco de Ventus");
        addItem(ModItems.ventus_Chestplate, "Pechera de Ventus");
        addItem(ModItems.ventus_Leggings, "Grebas de Ventus");
        addItem(ModItems.ventus_Boots, "Botas de Ventus");
        addItem(ModItems.ventus_Shoulder, "Hombrera de Ventus");

        addItem(ModItems.nightmareVentus_Helmet, "Casco de Nightmare Ventus");
        addItem(ModItems.nightmareVentus_Chestplate, "Pechera de Nightmare Ventus");
        addItem(ModItems.nightmareVentus_Leggings, "Grebas de Nightmare Ventus");
        addItem(ModItems.nightmareVentus_Boots, "Botas de Nightmare Ventus");
        addItem(ModItems.nightmareVentus_Shoulder, "Hombrera de Nightmare Ventus");

        addItem(ModItems.eraqus_Helmet, "Casco de Eraqus");
        addItem(ModItems.eraqus_Chestplate, "Pechera de Eraqus");
        addItem(ModItems.eraqus_Leggings, "Grebas de Eraqus");
        addItem(ModItems.eraqus_Boots, "Botas de Eraqus");
        addItem(ModItems.eraqus_Shoulder, "Hombrera de Eraqus");

        addItem(ModItems.xehanort_Helmet, "Casco de Xehanort");
        addItem(ModItems.xehanort_Chestplate, "Pechera de Xehanort");
        addItem(ModItems.xehanort_Leggings, "Grebas de Xehanort");
        addItem(ModItems.xehanort_Boots, "Botas de Xehanort");
        addItem(ModItems.xehanort_Shoulder, "Hombrera de Xehanort");

        addItem(ModItems.ux_Helmet, "Casco de Armadura UX");
        addItem(ModItems.ux_Chestplate, "Pechera de Armadura UX");
        addItem(ModItems.ux_Leggings, "Grebas de Armadura UX");
        addItem(ModItems.ux_Boots, "Botas de Armadura UX");
        addItem(ModItems.ux_Shoulder, "Hombrera de Armadura UX");

        addItem(ModItems.vanitas_Helmet, "Casco de Vanitas");
        addItem(ModItems.vanitas_Chestplate, "Pechera de Vanitas");
        addItem(ModItems.vanitas_Leggings, "Grebas de Vanitas");
        addItem(ModItems.vanitas_Boots, "Botas de Vanitas");

        addItem(ModItems.vanitas_Remnant_Helmet, "Casco de Vanitas Reminiscente");
        addItem(ModItems.vanitas_Remnant_Chestplate, "Pechera de Vanitas Reminiscente");
        addItem(ModItems.vanitas_Remnant_Leggings, "Grebas de Vanitas Reminiscente");
        addItem(ModItems.vanitas_Remnant_Boots, "Botas de Vanitas Reminiscente");

        addItem(ModItems.antiCoat_Helmet, "Capucha de AntiCoat");
        addItem(ModItems.antiCoat_Chestplate, "Abrigo de AntiCoat");
        addItem(ModItems.antiCoat_Leggings, "Pantalones de AntiCoat");
        addItem(ModItems.antiCoat_Boots, "Botas de AntiCoat");

        addItem(ModItems.xemnas_Helmet, "Capucha de Xemnas");
        addItem(ModItems.xemnas_Chestplate, "Abrigo de Xemnas");
        addItem(ModItems.xemnas_Leggings, "Pantalones de Xemnas");
        addItem(ModItems.xemnas_Boots, "Botas de Xemnas");

        addItem(ModItems.dark_Riku_Chestplate, "Pechera de Riku Oscuro");
        addItem(ModItems.dark_Riku_Leggings, "Grebas de Riku Oscuro");
        addItem(ModItems.dark_Riku_Boots, "Botas de Riku Oscuro");

        addItem(ModItems.aced_Helmet, "Capucha de Aced");
        addItem(ModItems.aced_Chestplate, "Abrigo de Aced");
        addItem(ModItems.aced_Leggings, "Pantalones de Aced");
        addItem(ModItems.aced_Boots, "Botas de Aced");

        addItem(ModItems.ava_Helmet, "Capucha de Ava");
        addItem(ModItems.ava_Chestplate, "Abrigo de Ava");
        addItem(ModItems.ava_Leggings, "Pantalones de Ava");
        addItem(ModItems.ava_Boots, "Botas de Ava");

        addItem(ModItems.gula_Helmet, "Capucha de Gula");
        addItem(ModItems.gula_Chestplate, "Abrigo de Gula");
        addItem(ModItems.gula_Leggings, "Pantalones de Gula");
        addItem(ModItems.gula_Boots, "Botas de Gula");

        addItem(ModItems.invi_Helmet, "Capucha de Invi");
        addItem(ModItems.invi_Chestplate, "Abrigo de Invi");
        addItem(ModItems.invi_Leggings, "Pantalones de Invi");
        addItem(ModItems.invi_Boots, "Botas de Invi");

        addItem(ModItems.ira_Helmet, "Capucha de Ira");
        addItem(ModItems.ira_Chestplate, "Abrigo de Ira");
        addItem(ModItems.ira_Leggings, "Pantalones de Ira");
        addItem(ModItems.ira_Boots, "Botas de Ira");

        //Cards
        addItem(ModItems.tranquilDarkness, "Oscuridad Tenue");
        addItem(ModItems.teemingDarkness, "Oscuridad Total");
        addItem(ModItems.feebleDarkness, "Oscuridad Lánguida");
        addItem(ModItems.almightyDarkness, "Noche Cerrada");
        addItem(ModItems.sleepingDarkness, "Oscuridad Latente");
        addItem(ModItems.loomingDarkness, "Tinieblas");
        addItem(ModItems.bottomlessDarkness, "Oscuridad Insondable");

        addItem(ModItems.martialWaking, "Aula Marcial");
        addItem(ModItems.sorcerousWaking, "Aula de Magia");
        addItem(ModItems.alchemicWaking, "Aula Alquímica");
        addItem(ModItems.stagnantSpace, "Ralentí");
        addItem(ModItems.weightlessSpace, "Espacio Liviano");

        addItem(ModItems.calmBounty, "Botín Fácil");
        addItem(ModItems.guardedTrove, "Tesoro Custodiado");
        addItem(ModItems.falseBounty, "Falso Botín");
        addItem(ModItems.momentsReprieve, "Alivio Fugaz");
        addItem(ModItems.minglingWorlds, "Multiverso");
        addItem(ModItems.moogleRoom, "Casa Moguri");
        addItem(ModItems.prosperousRepository, "Repositorio Próspero");
        addItem(ModItems.treacherousRepository, "Repositorio Traicionero");
        addItem(ModItems.reposefulGrove, "Arboleda Serena");

        addItem(ModItems.keyOfBeginnings, "Llave Prístina");
        addItem(ModItems.keyOfGuidance, "Llave Guía");
        addItem(ModItems.keyToTruth, "Llave a la Verdad");
        addItem(ModItems.keyToRewards, "Llave Recompensa");

        addItem(ModItems.redCardPack, "Pack de cartas rojas");
        addItem(ModItems.greenCardPack, "Pack de cartas verdes");
        addItem(ModItems.blueCardPack, "Pack de cartas azules");
        addItem(ModItems.randomCardPack, "Pack de cartas aleatorias");

        addItem(ModItems.emptyCard,"Carta Vacía");
        addItem(ModItems.plainsCard,"Carta de Llanuras");
        addItem(ModItems.theNetherCard,"Carta del Nether");
        addItem(ModItems.theEndCard,"Carta del Fín");
        addItem(ModItems.castleOblivionCard,"Carta del Castillo del Olvido");
        addItem(ModItems.oceanCard,"Carta del Océano");
        addItem(ModItems.desertCard,"Carta del Desierto");
        addItem(ModItems.snowyCard,"Carta de Nieve");
        addItem(ModItems.badlandsCard,"Carta de Tierras Baldías");
        addItem(ModItems.swampCard,"Carta de Pantano");
        addItem(ModItems.caveCard,"Carta de Cueva");
        addItem(ModItems.mushroomFieldsCard,"Carta de Campo de Champiñones");
        addItem(ModItems.forestCard,"Carta de Bosque");
        addItem(ModItems.jungleCard,"Carta de Jungla");

        addItem(ModItems.plainsMemory,"Evocación de Llanuras");
        addItem(ModItems.desertMemory,"Evocación de Desierto");


        //Synthesis materials
        addItem(ModItems.blazing_shard, "Guijarro \u00edgneo");
        addItem(ModItems.blazing_stone, "Roca \u00edgnea");
        addItem(ModItems.blazing_gem, "Gema \u00edgnea");
        addItem(ModItems.blazing_crystal, "Cristal \u00edgneo");

        addItem(ModItems.soothing_shard, "Guijarro Vital");
        addItem(ModItems.soothing_stone, "Roca Vital");
        addItem(ModItems.soothing_gem, "Gema Vital");
        addItem(ModItems.soothing_crystal, "Cristal Vital");

        addItem(ModItems.writhing_shard, "Guijarro Tortuoso");
        addItem(ModItems.writhing_stone, "Roca Tortuosa");
        addItem(ModItems.writhing_gem, "Gema Tortuosa");
        addItem(ModItems.writhing_crystal, "Cristal Tortuoso");

        addItem(ModItems.betwixt_shard, "Guijarro Neutro");
        addItem(ModItems.betwixt_stone, "Roca Neutra");
        addItem(ModItems.betwixt_gem, "Gema Neutra");
        addItem(ModItems.betwixt_crystal, "Cristal Neutro");

        addItem(ModItems.wellspring_shard, "Guijarro de Poder");
        addItem(ModItems.wellspring_stone, "Roca de Poder");
        addItem(ModItems.wellspring_gem, "Gema de Poder");
        addItem(ModItems.wellspring_crystal, "Cristal de Poder");

        addItem(ModItems.frost_shard, "Guijarro Fr\u00edo");
        addItem(ModItems.frost_stone, "Roca Fr\u00eda");
        addItem(ModItems.frost_gem, "Gema Fr\u00eda");
        addItem(ModItems.frost_crystal, "Cristal Fr\u00edo");

        addItem(ModItems.lightning_shard, "Guijarro Luminoso");
        addItem(ModItems.lightning_stone, "Roca Luminosa");
        addItem(ModItems.lightning_gem, "Gema Luminosa");
        addItem(ModItems.lightning_crystal, "Cristal Luminoso");

        addItem(ModItems.lucid_shard, "Guijarro Claro");
        addItem(ModItems.lucid_stone, "Roca Clara");
        addItem(ModItems.lucid_gem, "Gema Clara");
        addItem(ModItems.lucid_crystal, "Cristal Claro");

        addItem(ModItems.hungry_shard, "Guijarro Voraz");
        addItem(ModItems.hungry_stone, "Roca Voraz");
        addItem(ModItems.hungry_gem, "Gema Voraz");
        addItem(ModItems.hungry_crystal, "Cristal Voraz");

        addItem(ModItems.twilight_shard, "Guijarro Crepuscular");
        addItem(ModItems.twilight_stone, "Roca Crepuscular");
        addItem(ModItems.twilight_gem, "Gema Crepuscular");
        addItem(ModItems.twilight_crystal, "Cristal Crepuscular");

        addItem(ModItems.mythril_shard, "Guijarro de Mitrilo");
        addItem(ModItems.mythril_stone, "Roca de Mitrilo");
        addItem(ModItems.mythril_gem, "Gema de Mitrilo");
        addItem(ModItems.mythril_crystal, "Cristal de Mitrilo");

        addItem(ModItems.tranquility_shard, "Guijarro Sosegado");
        addItem(ModItems.tranquility_stone, "Roca Sosegada");
        addItem(ModItems.tranquility_gem, "Gema Sosegada");
        addItem(ModItems.tranquility_crystal, "Cristal Sosegado");

        addItem(ModItems.sinister_shard, "Guijarro Siniestro");
        addItem(ModItems.sinister_stone, "Roca Siniestra");
        addItem(ModItems.sinister_gem, "Gema Siniestra");
        addItem(ModItems.sinister_crystal, "Cristal Siniestro");

        addItem(ModItems.stormy_shard, "Guijarro Recio");
        addItem(ModItems.stormy_stone, "Roca Recia");
        addItem(ModItems.stormy_gem, "Gema Recia");
        addItem(ModItems.stormy_crystal, "Cristal Recio");

        addItem(ModItems.remembrance_shard, "Guijarro Evocador");
        addItem(ModItems.remembrance_stone, "Roca Evocadora");
        addItem(ModItems.remembrance_gem, "Gema Evocadora");
        addItem(ModItems.remembrance_crystal, "Cristal Evocador");

        addItem(ModItems.pulsing_shard, "Guijarro de Fuerza");
        addItem(ModItems.pulsing_stone, "Roca de Fuerza");
        addItem(ModItems.pulsing_gem, "Gema de Fuerza");
        addItem(ModItems.pulsing_crystal, "Cristal de Fuerza");

        addItem(ModItems.orichalcum, "Orichalcum");
        addItem(ModItems.orichalcumplus, "Orichalcum+");
        addItem(ModItems.lost_illusion, "Ilusi\u00f3n Extraviada");
        addItem(ModItems.manifest_illusion, "Ilusi\u00f3n Materializada");

        addItem(ModItems.fluorite, "Fluorita");
        addItem(ModItems.damascus, "Damasco");
        addItem(ModItems.adamantite, "Adamantita");
        addItem(ModItems.electrum, "Electrum");
        addItem(ModItems.evanescent_crystal, "Cristal Evanescente");
        addItem(ModItems.illusory_crystal, "Cristal Ilusorio");

        addItem(ModItems.gummiMeteorFragment, "Fragmento de Gummi");
        addItem(ModItems.gummiShipBlueprint, "Planos de Gummi");
        addItem(ModItems.gummiPhone, "Gumífono");

        //Keyblades
        addItem(ModItems.abaddonPlasma, "Plasma de Abad\u00f3n");
        addItem(ModItems.abyssalTide, "Marea Abisal");
        addItem(ModItems.acedsKeyblade, "Llave Espada de Aced");
        addItem(ModItems.adventRed, "Rojo Reencuentro");
        addItem(ModItems.allForOne, "Todos para Uno");
        addItem(ModItems.astralBlast, "Explosi\u00f3n Astral");
        addItem(ModItems.aubade, "Alborada");
        addItem(ModItems.avasKeyblade, "Llave Espada de Ava");
        addItem(ModItems.bondOfFlame, "Lazo de Llamas");
        addItem(ModItems.bondOfTheBlaze, "V\u00ednculo Incandescente");
        addItem(ModItems.braveheart, "Coraz\u00f3n Valiente");
        addItem(ModItems.brightcrest, "Fulgor Impasible");
        addItem(ModItems.chaosRipper, "Ca\u00f3tica");
        addItem(ModItems.circleOfLife, "C\u00edrculo de la Vida");
        addItem(ModItems.classicTone, "Tono Cl\u00e1sico");
        addItem(ModItems.counterpoint, "Contrapunto");
        addItem(ModItems.crabclaw, "Joya del Mar");
        addItem(ModItems.crownOfGuilt, "Corona Culpable");
        addItem(ModItems.crystalSnow, "Nieve Cristalina");
        addItem(ModItems.darkerThanDark, "Oscuridad Total");
        addItem(ModItems.darkgnaw, "Mella Sombr\u00eda");
        addItem(ModItems.dawnTillDusk, "Del Alba al Ocaso");
        addItem(ModItems.deadOfNight, "Oscuridad de la noche");
        addItem(ModItems.decisivePumpkin, "Calabaza Decisiva");
        addItem(ModItems.destinysEmbrace, "Abrazo del Destino");
        addItem(ModItems.diamondDust, "Polvo de Diamante");
        addItem(ModItems.divewing, "Inmersia");
        addItem(ModItems.divineRose, "La Rosa");
        addItem(ModItems.dualDisc, "Bidiscal");
        addItem(ModItems.earthshaker, "Teluria");
        addItem(ModItems.elementalEncoder, "Codificador Elemental");
        addItem(ModItems.endOfPain, "Quitapenas");
        addItem(ModItems.endsOfTheEarth, "Postrimer\u00eda");
        addItem(ModItems.everAfter, "Para Siempre");
        addItem(ModItems.fairyHarp, "Arpa de Hada");
        addItem(ModItems.fairyStars, "Lucero Fe\u00e9rico");
        addItem(ModItems.fatalCrest, "Emblema Fatal");
        addItem(ModItems.favoriteDeputy, "Ayudante del Sheriff");
        addItem(ModItems.fenrir, "Fenrir");
        addItem(ModItems.ferrisGear, "Noria Vivaz");
        addItem(ModItems.followTheWind, "Sigue al Viento");
        addItem(ModItems.frolicFlame, "Llama Crepitante");
        addItem(ModItems.glimpseOfDarkness, "Atsibo Oscuro");
        addItem(ModItems.grandChef, "Gran Chef");
        addItem(ModItems.guardianBell, "Ta\u00f1ido Guardi\u00e1n");
        addItem(ModItems.guardianSoul, "Guardian del Alma");
        addItem(ModItems.gulasKeyblade, "Llave espada de Gula");
        addItem(ModItems.gullWing, "Ala de Gaviota");
        addItem(ModItems.happyGear, "La Clave de la Risa");
        addItem(ModItems.herosCrest, "Emblema de H\u00e9roe");
        addItem(ModItems.herosOrigin, "El Origen del H\u00e9roe");
        addItem(ModItems.hiddenDragon, "Drag\u00f3n Oculto");
        addItem(ModItems.hunnySpout, "Dulce Miel");
        addItem(ModItems.hyperdrive, "Hiperpropulsor");
        addItem(ModItems.incompleteKiblade, "Llave Espada \u03c7 Incompleta");
        addItem(ModItems.invisKeyblade, "Llave Espada de Invi");
        addItem(ModItems.irasKeyblade, "Llave Espada de Ira");
        addItem(ModItems.jungleKing, "Artesana");
        addItem(ModItems.keybladeOfPeoplesHearts, "Llave Espada del Coraz\u00f3n");
        addItem(ModItems.kiblade, "Llave Espada \u03c7");
        addItem(ModItems.kingdomKey, "Cadena del Reino");
        addItem(ModItems.kingdomKeyD, "Cadena del Reino D");
        addItem(ModItems.kingdomKeyN, "Cadena del Reino Pesadilla");
        addItem(ModItems.knockoutPunch, "Nocaut");
        addItem(ModItems.ladyLuck, "\u00daltimo Recurso");
        addItem(ModItems.leviathan, "Leviat\u00e1n");
        addItem(ModItems.lionheart, "Lionheart");
        addItem(ModItems.lostMemory, "Evocaci\u00f3n");
        addItem(ModItems.lunarEclipse, "Eclipse Lunar");
        addItem(ModItems.markOfAHero, "Gal\u00f3n de H\u00e9roe");
        addItem(ModItems.mastersDefender, "Salva del Maestro");
        addItem(ModItems.maverickFlare, "Destelleante");
        addItem(ModItems.metalChocobo, "Acero Chocobo");
        addItem(ModItems.midnightBlue, "Azul Medianoche");
        addItem(ModItems.midnightRoar, "Rugido Nocturno");
        addItem(ModItems.mirageSplit, "Espejismo Dividido");
        addItem(ModItems.missingAche, "Dolor Pasado");
        addItem(ModItems.monochrome, "Monocromo");
        addItem(ModItems.moogleOGlory, "Moguri de la Gloria");
        addItem(ModItems.mysteriousAbyss, "Abismo Misterioso");
        addItem(ModItems.nanoGear, "Nanoarma");
        addItem(ModItems.nightmaresEnd, "Fin de Pesadilla");
        addItem(ModItems.nightmaresEndAndMirageSplit, "Llave Espada Combinada");
        addItem(ModItems.noName, "La Atisbadora");
        addItem(ModItems.noNameBBS, "Inn\u00f3mita");
        addItem(ModItems.oathkeeper, "Prometida");
        addItem(ModItems.oblivion, "Recuerdos Lejanos");
        addItem(ModItems.oceansRage, "Ira Abisal");
        addItem(ModItems.olympia, "Heroica");
        addItem(ModItems.omegaWeapon, "Arma Omega");
        addItem(ModItems.ominousBlight, "Plaga Omniosa");
        addItem(ModItems.oneWingedAngel, "\u00c1ngel Unialado");
        addItem(ModItems.painOfSolitude, "Dolor de Soledad");
        addItem(ModItems.phantomGreen, "Verde Fantasma");
        addItem(ModItems.photonDebugger, "Depurador Fot\u00f3nico");
        addItem(ModItems.pixiePetal, "P\u00e9talo de Hada");
        addItem(ModItems.pumpkinhead, "Calabaza Macabra");
        addItem(ModItems.rainfell, "Aguacero");
        addItem(ModItems.rejectionOfFate, "Negar del Destino");
        addItem(ModItems.royalRadiance, "Corona Eterna");
        addItem(ModItems.rumblingRose, "Rosa del Estruendo");
        addItem(ModItems.shootingStar, "Estrella fugaz");
        addItem(ModItems.signOfInnocence, "Signo de Pureza");
        addItem(ModItems.silentDirge, "Encecha Muda");
        addItem(ModItems.skullNoise, "Retumbadora");
        addItem(ModItems.sleepingLion, "Le\u00f3n Durmiente");
        addItem(ModItems.soulEater, "Devora Almas");
        addItem(ModItems.spellbinder, "Examinadora");
        addItem(ModItems.starCluster, "C\u00famulo Estelar");
        addItem(ModItems.starSeeker, "Explorador Estelar");
        addItem(ModItems.starlight, "Luz Estelar");
        addItem(ModItems.stormfall, "Argavieso");
        addItem(ModItems.strokeOfMidnight, "\u00daltimo Ta\u00f1ido");
        addItem(ModItems.sweetDreams, "Dulcesue\u00f1os");
        addItem(ModItems.sweetMemories, "Dulces Recuerdos");
        addItem(ModItems.sweetstack, "Dulce Sorpresa");
        addItem(ModItems.threeWishes, "Tres Deseos");
        addItem(ModItems.totalEclipse, "Eclipse Total");
        addItem(ModItems.treasureTrove, "Tesoro Oculto");
        addItem(ModItems.trueLightsFlight, "Vuelo a la Luz");
        addItem(ModItems.twilightBlaze, "Llamarada Ocaso");
        addItem(ModItems.twoBecomeOne, "Dos Ser\u00e1n Uno");
        addItem(ModItems.ultimaWeaponBBS, "Arma Artema (BBS)");
        addItem(ModItems.ultimaWeaponDDD, "Arma Artema (DDD)");
        addItem(ModItems.ultimaWeaponKH1, "Arma Artema (KH1)");
        addItem(ModItems.ultimaWeaponKH2, "Arma Artema (KH2)");
        addItem(ModItems.ultimaWeaponKH3, "Arma Artema (KH3)");
        addItem(ModItems.umbrella, "Paraguas");
        addItem(ModItems.unbound, "Il\u00edmite");
        addItem(ModItems.victoryLine, "L\u00ednea de Meta");
        addItem(ModItems.voidGear, "Quid Vacuo");
        addItem(ModItems.voidGearRemnant, "Quid Vacuo Reminiscente");
        addItem(ModItems.waytotheDawn, "Camino al Alba");
        addItem(ModItems.waywardWind, "Brisa Descarada");
        addItem(ModItems.wheelOfFate, "Tim\u00f3n del Destino");
        addItem(ModItems.winnersProof, "Prueba Triunfal");
        addItem(ModItems.wishingLamp, "L\u00e1mpara M\u00e1gica");
        addItem(ModItems.wishingStar, "Estrella Fugaz");
        addItem(ModItems.woodenKeyblade, "Llave Espada de Madera");
        addItem(ModItems.woodenStick, "Palo de Madera");
        addItem(ModItems.youngXehanortsKeyblade, "Llave Espada de Joven Xehanort");
        addItem(ModItems.zeroOne, "Cero y Uno");
        addItem(ModItems.dreamSword, "Espada de Ensue\u00f1o");
        addItem(ModItems.dreamStaff, "Bast\u00f3n de Ensue\u00f1o");
        addItem(ModItems.dreamShield, "Escudo de Ensue\u00f1o");
        addItem(ModItems.struggleSword, "Espada de Struggle");
        addItem(ModItems.struggleWand, "Varita de Struggle");
        addItem(ModItems.struggleHammer, "Martillo de Struggle");
        addItem(ModItems.k111, "K111");
        addItem(ModItems.retribution, "Retribuci\u00f3n");

        //Keychains
        addItem(ModItems.abaddonPlasmaChain, "Llavero de Plasma de Abad\u00f3n");
        addItem(ModItems.abyssalTideChain, "Llavero de Marea Abisal");
        addItem(ModItems.acedsKeybladeChain, "Llavero de Llave Espada de Aced");
        addItem(ModItems.adventRedChain, "Llavero de Rojo Reencuentro");
        addItem(ModItems.allForOneChain, "Llavero de Todos para Uno");
        addItem(ModItems.astralBlastChain, "Llavero de Explosi\u00f3n Astral");
        addItem(ModItems.aubadeChain, "Llavero de Alborada");
        addItem(ModItems.avasKeybladeChain, "Llavero de Llave Espada de Ava");
        addItem(ModItems.bondOfFlameChain, "Llavero de Lazo de Llamas");
        addItem(ModItems.bondOfTheBlazeChain, "Llavero de V\u00ednculo Incandescente");
        addItem(ModItems.braveheartChain, "Llavero de Coraz\u00f3n Valiente");
        addItem(ModItems.brightcrestChain, "Llavero de Fulgor Impasible");
        addItem(ModItems.chaosRipperChain, "Llavero de Ca\u00f3tica");
        addItem(ModItems.circleOfLifeChain, "Llavero de C\u00edrculo de la Vida");
        addItem(ModItems.classicToneChain, "Llavero de Tono Cl\u00e1sico");
        addItem(ModItems.counterpointChain, "Llavero de Contrapunto");
        addItem(ModItems.crabclawChain, "Llavero de Joya del Mar");
        addItem(ModItems.crownOfGuiltChain, "Llavero de Corona Culpable");
        addItem(ModItems.crystalSnowChain, "Llavero de Nieve Cristalina");
        addItem(ModItems.darkerThanDarkChain, "Llavero de Oscuridad Total");
        addItem(ModItems.darkgnawChain, "Llavero de Mella Sombr\u00eda");
        addItem(ModItems.dawnTillDuskChain, "Llavero de Del Alba al Ocaso");
        addItem(ModItems.deadOfNightChain, "Llavero de Oscuridad de la noche");
        addItem(ModItems.decisivePumpkinChain, "Llavero de Calabaza Decisiva");
        addItem(ModItems.destinysEmbraceChain, "Llavero de Abrazo del Destino");
        addItem(ModItems.diamondDustChain, "Llavero de Polvo de Diamante");
        addItem(ModItems.divewingChain, "Llavero de Inmersia");
        addItem(ModItems.divineRoseChain, "Llavero de La Rosa");
        addItem(ModItems.dualDiscChain, "Llavero de Bidiscal");
        addItem(ModItems.earthshakerChain, "Llavero de Teluria");
        addItem(ModItems.elementalEncoderChain, "Llavero de Codificador Elemental");
        addItem(ModItems.endOfPainChain, "Llavero de Quitapenas");
        addItem(ModItems.endsOfTheEarthChain, "Llavero de Postrimer\u00eda");
        addItem(ModItems.everAfterChain, "Llavero de Para Siempre");
        addItem(ModItems.fairyHarpChain, "Llavero de Arpa de Hada");
        addItem(ModItems.fairyStarsChain, "Llavero de Lucero Fe\u00e9rrico");
        addItem(ModItems.fatalCrestChain, "Llavero de Emblema Fatal");
        addItem(ModItems.favoriteDeputyChain, "Llavero de Ayudante del Sheriff");
        addItem(ModItems.fenrirChain, "Llavero de Fenrir");
        addItem(ModItems.ferrisGearChain, "Llavero de Noria Vivaz");
        addItem(ModItems.followTheWindChain, "Llavero de Sigue al Viento");
        addItem(ModItems.frolicFlameChain, "Llavero de Llama Crepitante");
        addItem(ModItems.glimpseOfDarknessChain, "Llavero de Atsibo Oscuro");
        addItem(ModItems.grandChefChain, "Llavero de Gran Chef");
        addItem(ModItems.guardianBellChain, "Llavero de Ta\u00f1ido Guardi\u00e1n");
        addItem(ModItems.guardianSoulChain, "Llavero de Guardian del Alma");
        addItem(ModItems.gulasKeybladeChain, "Llavero de Llave Espada de Gula");
        addItem(ModItems.gullWingChain, "Llavero de Ala de Gaviota");
        addItem(ModItems.happyGearChain, "Llavero de La Clave de la Risa");
        addItem(ModItems.herosCrestChain, "Llavero de Emblema de H\u00e9roe");
        addItem(ModItems.herosOriginChain, "Llavero de El Origen del H\u00e9roe");
        addItem(ModItems.hiddenDragonChain, "Llavero de Drag\u00f3n Oculto");
        addItem(ModItems.hunnySpoutChain, "Llavero de Dulce Miel");
        addItem(ModItems.hyperdriveChain, "Llavero de Hiperpropulsor");
        addItem(ModItems.incompleteKibladeChain, "Llavero de Llave Espada    Incompleta");
        addItem(ModItems.invisKeybladeChain, "Llavero de Llave Espada de Invi");
        addItem(ModItems.irasKeybladeChain, "Llavero de Llave Espada de Ira");
        addItem(ModItems.jungleKingChain, "Llavero de Artesana");
        addItem(ModItems.keybladeOfPeoplesHeartsChain, "Llavero de Llave Espada del Coraz\u00f3n");
        addItem(ModItems.kibladeChain, "Llavero de Llave Espada   ");
        addItem(ModItems.kingdomKeyChain, "Llavero de Cadena del Reino");
        addItem(ModItems.kingdomKeyDChain, "Llavero de Cadena del Reino D");
        addItem(ModItems.kingdomKeyNChain, "Llavero de Cadena del Reino Pesadilla");
        addItem(ModItems.knockoutPunchChain, "Llavero de Nocaut");
        addItem(ModItems.ladyLuckChain, "Llavero de \u00faltimo Recurso");
        addItem(ModItems.leviathanChain, "Llavero de Leviat\u00e1n");
        addItem(ModItems.lionheartChain, "Llavero de Lionheart");
        addItem(ModItems.lostMemoryChain, "Llavero de Evocaci\u00f3n");
        addItem(ModItems.lunarEclipseChain, "Llavero de Eclipse Lunar");
        addItem(ModItems.markOfAHeroChain, "Llavero de Gal\u00f3n de H\u00e9roe");
        addItem(ModItems.mastersDefenderChain, "Llavero de Salva del Maestro");
        addItem(ModItems.maverickFlareChain, "Llavero de Destelleante");
        addItem(ModItems.metalChocoboChain, "Llavero de Acero Chocobo");
        addItem(ModItems.midnightBlueChain, "Llavero de Azul Medianoche");
        addItem(ModItems.midnightRoarChain, "Llavero de Rugido Nocturno");
        addItem(ModItems.mirageSplitChain, "Llavero de Espejismo Dividido");
        addItem(ModItems.missingAcheChain, "Llavero de Dolor Pasado");
        addItem(ModItems.monochromeChain, "Llavero de Monocromo");
        addItem(ModItems.moogleOGloryChain, "Llavero de Moguri de la Gloria");
        addItem(ModItems.mysteriousAbyssChain, "Llavero de Abismo Misterioso");
        addItem(ModItems.nanoGearChain, "Llavero de Nanoarma");
        addItem(ModItems.nightmaresEndChain, "Llavero de Fin de Pesadilla");
        addItem(ModItems.nightmaresEndAndMirageSplitChain, "Llavero de Llave Espada Combinada");
        addItem(ModItems.noNameChain, "Llavero de Inn\u00f3mita");
        addItem(ModItems.noNameBBSChain, "Llavero de Inn\u00f3mita (BBS)");
        addItem(ModItems.oathkeeperChain, "Llavero de Prometida");
        addItem(ModItems.oblivionChain, "Llavero de Recuerdos Lejanos");
        addItem(ModItems.oceansRageChain, "Llavero de Ira Abisal");
        addItem(ModItems.olympiaChain, "Llavero de Heroica");
        addItem(ModItems.omegaWeaponChain, "Llavero de Arma Omega");
        addItem(ModItems.ominousBlightChain, "Llavero de Plaga Omniosa");
        addItem(ModItems.oneWingedAngelChain, "Llavero de \u00e1ngel Unialado");
        addItem(ModItems.painOfSolitudeChain, "Llavero de Dolor de Soledad");
        addItem(ModItems.phantomGreenChain, "Llavero de Verde Fantasma");
        addItem(ModItems.photonDebuggerChain, "Llavero de Depurador Fot\u00f3nico");
        addItem(ModItems.pixiePetalChain, "Llavero de P\u00e9talo de Hada");
        addItem(ModItems.pumpkinheadChain, "Llavero de Calabaza Macabra");
        addItem(ModItems.rainfellChain, "Llavero de Aguacero");
        addItem(ModItems.rejectionOfFateChain, "Llavero de Negar del Destino");
        addItem(ModItems.royalRadianceChain, "Llavero de Corona Eterna");
        addItem(ModItems.rumblingRoseChain, "Llavero de Rosa del Estruendo");
        addItem(ModItems.shootingStarChain, "Llavero de Estrella Fugaz");
        addItem(ModItems.signOfInnocenceChain, "Llavero de Signo de Pureza");
        addItem(ModItems.silentDirgeChain, "Llavero de Encecha Muda");
        addItem(ModItems.skullNoiseChain, "Llavero de Retumbadora");
        addItem(ModItems.sleepingLionChain, "Llavero de Le\u00f3n Durmiente");
        addItem(ModItems.soulEaterChain, "Llavero de Devora Almas");
        addItem(ModItems.spellbinderChain, "Llavero de Examinadora");
        addItem(ModItems.starClusterChain, "Llavero de C\u00famulo Estelar");
        addItem(ModItems.starSeekerChain, "Llavero de Explorador Estelar");
        addItem(ModItems.starlightChain, "Llavero de Luz Estelar");
        addItem(ModItems.stormfallChain, "Llavero de Argavieso");
        addItem(ModItems.strokeOfMidnightChain, "Llavero de \u00faltimo Ta\u00f1ido");
        addItem(ModItems.sweetDreamsChain, "Llavero de Dulcesue\u00f1os");
        addItem(ModItems.sweetMemoriesChain, "Llavero de Dulces Recuerdos");
        addItem(ModItems.sweetstackChain, "Llavero de Dulce Sorpresa");
        addItem(ModItems.threeWishesChain, "Llavero de Tres Deseos");
        addItem(ModItems.totalEclipseChain, "Llavero de Eclipse Total");
        addItem(ModItems.treasureTroveChain, "Llavero de Tesoro Oculto");
        addItem(ModItems.trueLightsFlightChain, "Llavero de Vuelo a la Luz");
        addItem(ModItems.twilightBlazeChain, "Llavero de Llamarada Ocaso");
        addItem(ModItems.twoBecomeOneChain, "Llavero de Dos Ser\u00e1n Uno");
        addItem(ModItems.ultimaWeaponBBSChain, "Llavero de Arma Artema (BBS)");
        addItem(ModItems.ultimaWeaponDDDChain, "Llavero de Arma Artema (DDD)");
        addItem(ModItems.ultimaWeaponKH1Chain, "Llavero de Arma Artema (KH1)");
        addItem(ModItems.ultimaWeaponKH2Chain, "Llavero de Arma Artema (KH2)");
        addItem(ModItems.ultimaWeaponKH3Chain, "Llavero de Arma Artema (KH3)");
        addItem(ModItems.umbrellaChain, "Llavero de Paraguas");
        addItem(ModItems.unboundChain, "Llavero de Il\u00edmite");
        addItem(ModItems.victoryLineChain, "Llavero de L\u00ednea de Meta");
        addItem(ModItems.voidGearChain, "Llavero de Quid Vacuo");
        addItem(ModItems.voidGearRemnantChain, "Llavero de Quid Vacuo Reminiscente");
        addItem(ModItems.waytotheDawnChain, "Llavero de Camino al Alba");
        addItem(ModItems.waywardWindChain, "Llavero de Brisa Descarada");
        addItem(ModItems.wheelOfFateChain, "Llavero de Tim\u00f3n del Destino");
        addItem(ModItems.winnersProofChain, "Llavero de Prueba Triunfal");
        addItem(ModItems.wishingLampChain, "Llavero de L\u00e1mpara M\u00e1gica");
        addItem(ModItems.wishingStarChain, "Llavero de Estrella Fugaz");
        addItem(ModItems.youngXehanortsKeybladeChain, "Llavero de Llave Espada de Joven Xehanort");
        addItem(ModItems.zeroOneChain, "Llavero de Cero y Uno");
        addItem(ModItems.k111c, "K111c");
        addItem(ModItems.retributionChain, "Llavero de Retribuci\u00f3n");

        //Keyblade Descriptions
        add("item." + MODID + "." + Strings.abaddonPlasma + ".desc", "A weapon that lets you string together faster, incredibly long ground combos.");
        add("item." + MODID + "." + Strings.abyssalTide + ".desc", "A weapon that performs very well in midair. Excellent for taking on fliers.");
        add("item." + MODID + "." + Strings.acedsKeyblade + ".desc", "The Keyblade owned by Ursus' Foreteller.");
        add("item." + MODID + "." + Strings.adventRed + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.allForOne + ".desc", "A Keyblade that triggers fewer Reality Shifts, but compensates with a boost in Magic and more frequent critical hits.");
        add("item." + MODID + "." + Strings.astralBlast + ".desc", "A weapon that lets you string together longer ground and aerial combos.");
        add("item." + MODID + "." + Strings.aubade + ".desc", "A weapon that draws forth its wielder's personality.");
        add("item." + MODID + "." + Strings.avasKeyblade + ".desc", "The Keyblade owned by Vulpeus' Foreteller.");
        add("item." + MODID + "." + Strings.bondOfFlame + ".desc", "Enhances magic to increase damage dealt by fire-based attacks.");
        add("item." + MODID + "." + Strings.bondOfTheBlaze + ".desc", "The Keyblade wielded by Lea.");
        add("item." + MODID + "." + Strings.braveheart + ".desc", "Riku's Keyblade after the Way to the Dawn was broken.");
        add("item." + MODID + "." + Strings.brightcrest + ".desc", "A Keyblade with long reach that provides an outstanding boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.chaosRipper + ".desc", "A Keyblade with long reach that does little for your Magic, but provides an outstanding boost in Strength.");
        add("item." + MODID + "." + Strings.circleOfLife + ".desc", "Has great strength, increasing MP restoration speed after MP is consumed.");
        add("item." + MODID + "." + Strings.classicTone + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.counterpoint + ".desc", "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.crabclaw + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals good physical damage.");
        add("item." + MODID + "." + Strings.crownOfGuilt + ".desc", "A weapon that boosts your Magic to give it incredible power.");
        add("item." + MODID + "." + Strings.crystalSnow + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.darkerThanDark + ".desc", "A weapon that offers high Magic and combo reach.");
        add("item." + MODID + "." + Strings.darkgnaw + ".desc", "A Keyblade that makes up for its poor reach and low critical hit ratio by providing an extra boost in Strength.");
        add("item." + MODID + "." + Strings.dawnTillDusk + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.deadOfNight + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.decisivePumpkin + ".desc", "The greater number of combos landed, the more damage is dealt, leading to a strong finishing move!");
        add("item." + MODID + "." + Strings.destinysEmbrace + ".desc", "A Keyblade that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.diamondDust + ".desc", "Greatly enhances magic and summon power. Raises max MP by 3.");
        add("item." + MODID + "." + Strings.divewing + ".desc", "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.divineRose + ".desc", "A powerful weapon that is difficult to deflect. Capable of dealing a string of critical blows.");
        add("item." + MODID + "." + Strings.dualDisc + ".desc", "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.earthshaker + ".desc", "The Keyblade Terra started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
        add("item." + MODID + "." + Strings.elementalEncoder + ".desc", "A Keyblade that courses with mystic power.");
        add("item." + MODID + "." + Strings.endOfPain + ".desc", "A Keyblade with high magical power and critical hit rate, but reduces the occurrence of Reality Shift.");
        add("item." + MODID + "." + Strings.endsOfTheEarth + ".desc", "A well-balanced Keyblade that provides an extra boost to all your stats.");
        add("item." + MODID + "." + Strings.everAfter + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.fairyHarp + ".desc", "Raises max MP by 1, and enhances magic and summon power. Sometimes deals powerful critical blows.");
        add("item." + MODID + "." + Strings.fairyStars + ".desc", "A Keyblade that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.fatalCrest + ".desc", "Increases strength during MP Charge and allows unlimited chaining of combos.");
        add("item." + MODID + "." + Strings.favoriteDeputy + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.fenrir + ".desc", "Has great range and strength, but maximum ground and midair combos are decreased by 1.");
        add("item." + MODID + "." + Strings.ferrisGear + ".desc", "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.followTheWind + ".desc", "Draws in nearby orbs.");
        add("item." + MODID + "." + Strings.frolicFlame + ".desc", "A well-balanced Keyblade that provides an extra boost to all of your stats.");
        add("item." + MODID + "." + Strings.glimpseOfDarkness + ".desc", "A weapon that possesses very high Strength. Effective against tough enemies.");
        add("item." + MODID + "." + Strings.grandChef + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.guardianBell + ".desc", "A Keyblade with long reach that provides an extra boost in Magic.");
        add("item." + MODID + "." + Strings.guardianSoul + ".desc", "Has great strength, increasing the amount of damage dealt from Reaction Commands.");
        add("item." + MODID + "." + Strings.gulasKeyblade + ".desc", "The Keyblade owned by Leopardos' Foreteller.");
        add("item." + MODID + "." + Strings.gullWing + ".desc", "Greatly increases the amount of experience gained when defeating an enemy at a critical moment.");
        add("item." + MODID + "." + Strings.happyGear + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.herosCrest + ".desc", "Increases the damage of the finishing move in the air relative to the number of hits in the combo.");
        add("item." + MODID + "." + Strings.herosOrigin + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.hiddenDragon + ".desc", "Restores MP relative to the amount of damage taken.");
        add("item." + MODID + "." + Strings.hunnySpout + ".desc", "A well-balanced Keyblade.");
        add("item." + MODID + "." + Strings.hyperdrive + ".desc", "A Keyblade with above-average reach that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.incompleteKiblade + ".desc", "An incomplete form of the legendary Keyblade, the \u03c7-blade.");
        add("item." + MODID + "." + Strings.invisKeyblade + ".desc", "The Keyblade owned by Anguis' Foreteller.");
        add("item." + MODID + "." + Strings.irasKeyblade + ".desc", "The Keyblade owned by Unicornis' Foreteller.");
        add("item." + MODID + "." + Strings.jungleKing + ".desc", "Has a long reach, but seldom deals critical blows.");
        add("item." + MODID + "." + Strings.keybladeOfPeoplesHearts + ".desc", "A keyblade with the ability to unlock a person's heart, releasing the darkness within.");
        add("item." + MODID + "." + Strings.kiblade + ".desc", "A legendary weapon, the original Keyblade which all other are imperfectly modeled after.");
        add("item." + MODID + "." + Strings.kingdomKey + ".desc", "The key chain attached draws out the Keyblade's true form and power.");
        add("item." + MODID + "." + Strings.kingdomKeyD + ".desc", "A Keyblade which mirrors the Kingdom Key from the Realm of Darkness.");
        add("item." + MODID + "." + Strings.kingdomKeyN + ".desc", "A Keyblade which stems from the negativity of the heart.");
        add("item." + MODID + "." + Strings.knockoutPunch + ".desc", "A Keyblade that lands fewer critical hits, but compensates with a Strength boost and more frequent Reality Shifts.");
        add("item." + MODID + "." + Strings.ladyLuck + ".desc", "Raises max MP by 2, and significantly enhances magic and summon power. Also inflicts good physical damage.");
        add("item." + MODID + "." + Strings.leviathan + ".desc", "A weapon that performs extremely well in midair. Outstanding for taking on fliers.");
        add("item." + MODID + "." + Strings.lionheart + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
        add("item." + MODID + "." + Strings.lostMemory + ".desc", "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.lunarEclipse + ".desc", "A weapon that boosts versatility by greatly boosting both Strength and Magic.");
        add("item." + MODID + "." + Strings.markOfAHero + ".desc", "A Keyblade that provides an extra boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.mastersDefender + ".desc", "Master Eraqus's Keyblade. All of its stats are high.");
        add("item." + MODID + "." + Strings.maverickFlare + ".desc", "A weapon that offers high Strength and ground combo speed.");
        add("item." + MODID + "." + Strings.metalChocobo + ".desc", "Possesses incredible power and reach, but reduces max MP by 1. Rarely deals critical blows.");
        add("item." + MODID + "." + Strings.midnightBlue + ".desc", "A Keyblade imbued with wondrous power.");
        add("item." + MODID + "." + Strings.midnightRoar + ".desc", "A weapon that possesses high Strength. Useful against tough enemies.");
        add("item." + MODID + "." + Strings.mirageSplit + ".desc", "A Keyblade formed from a Reality Shift in The World That Never Was.");
        add("item." + MODID + "." + Strings.missingAche + ".desc", "A weapon that lets you string together faster, longer ground combos.");
        add("item." + MODID + "." + Strings.monochrome + ".desc", "Increases the effect of restoration items used on the field.");
        add("item." + MODID + "." + Strings.moogleOGlory + ".desc", "Kupo.");
        add("item." + MODID + "." + Strings.mysteriousAbyss + ".desc", "Enhances magic to increase damage dealt by blizzard-based attacks.");
        add("item." + MODID + "." + Strings.nanoGear + ".desc", "A well-balanced Keyblade.");
        add("item." + MODID + "." + Strings.nightmaresEnd + ".desc", "A Keyblade formed from a Reality Shift in The World That Never Was.");
        add("item." + MODID + "." + Strings.nightmaresEndAndMirageSplit + ".desc", "A Keyblade formed by combining both the Mirage Split and Nightmare's End.");
        add("item." + MODID + "." + Strings.noName + ".desc", "The Keyblade that Luxu received from the Master of Masters, containing his very own eye.");
        add("item." + MODID + "." + Strings.noNameBBS + ".desc", "A Keyblade with long reach that provides an outstanding boost in Magic and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.oathkeeper + ".desc", "Enhances magic and increases the duration of a Drive Form.");
        add("item." + MODID + "." + Strings.oblivion + ".desc", "Has great strength, and allows the Drive Gauge to restore quickly during MP Charge.");
        add("item." + MODID + "." + Strings.oceansRage + ".desc", "A Keyblade that lands fewer critical hits, but compensates with a boost in Magic and more frequent Reality Shifts.");
        add("item." + MODID + "." + Strings.olympia + ".desc", "A powerful weapon that is difficult to deflect. Capable of inflicting mighty critical blows.");
        add("item." + MODID + "." + Strings.omegaWeapon + ".desc", "A formidable weapon with exceptional capabilities.");
        add("item." + MODID + "." + Strings.ominousBlight + ".desc", "A weapon that lets you string together faster, much longer ground combos.");
        add("item." + MODID + "." + Strings.oneWingedAngel + ".desc", "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
        add("item." + MODID + "." + Strings.painOfSolitude + ".desc", "A weapon that boosts your Magic to give it more power.");
        add("item." + MODID + "." + Strings.phantomGreen + ".desc", "A Keyblade imbued with wondrous power.");
        add("item." + MODID + "." + Strings.photonDebugger + ".desc", "Increases damage done by thunder-based attacks.");
        add("item." + MODID + "." + Strings.pixiePetal + ".desc", "A Keyblade that makes up for its poor reach with an extra boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.pumpkinhead + ".desc", "Has a long reach and the ability to deal a string of critical blows.");
        add("item." + MODID + "." + Strings.rainfell + ".desc", "The Keyblade Aqua started out with. What it lacks in reach it makes up for with a balanced boost to Strength and Magic.");
        add("item." + MODID + "." + Strings.rejectionOfFate + ".desc", "A weapon that enables your attacks to reach a wide area and deal immense damage.");
        add("item." + MODID + "." + Strings.royalRadiance + ".desc", "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
        add("item." + MODID + "." + Strings.rumblingRose + ".desc", "Has great strength, allowing finishing combo moves to be unleashed successively.");
        add("item." + MODID + "." + Strings.shootingStar + ".desc", "A Keyblade with an emphasis on Magic.");
        add("item." + MODID + "." + Strings.signOfInnocence + ".desc", "A weapon that boosts your Magic to give it a lot more power.");
        add("item." + MODID + "." + Strings.silentDirge + ".desc", "A weapon that provides versatility by boosting both Strength and Magic.");
        add("item." + MODID + "." + Strings.skullNoise + ".desc", "A Keyblade that provides a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.sleepingLion + ".desc", "Well-balanced with strength and magic, increasing maximum ground-based combos by 1.");
        add("item." + MODID + "." + Strings.soulEater + ".desc", "A sword that swims with darkness. Possesses high Strength.");
        add("item." + MODID + "." + Strings.spellbinder + ".desc", "Raises max MP by 2, and significantly enhances magic and summon power.");
        add("item." + MODID + "." + Strings.starCluster + ".desc", "Mickey's Keyblade, also known as Kingdom Key W.");
        add("item." + MODID + "." + Strings.starSeeker + ".desc", "Increases maximum combo by 1 when in midair.");
        add("item." + MODID + "." + Strings.starlight + ".desc", "A basic Keyblade which is associated with the force of Light.");
        add("item." + MODID + "." + Strings.stormfall + ".desc", "A well-balanced Keyblade that provides an extra boost to all your stats.");
        add("item." + MODID + "." + Strings.strokeOfMidnight + ".desc", "A Keyblade that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.sweetDreams + ".desc", "A Keyblade with long reach that provides an extra boost in Strength and makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.sweetMemories + ".desc", "Although it does not enhance attack strength, it increases the drop rate of items.");
        add("item." + MODID + "." + Strings.sweetstack + ".desc", "A Keyblade that provides an extra boost in Strength and ensures every strike is a critical hit.");
        add("item." + MODID + "." + Strings.threeWishes + ".desc", "A powerful weapon that is difficult to deflect.");
        add("item." + MODID + "." + Strings.totalEclipse + ".desc", "A weapon that possesses extreme Strength. Devastates tough enemies.");
        add("item." + MODID + "." + Strings.treasureTrove + ".desc", "A Keyblade that makes up for its poor reach with a balanced boost in Strength and Magic.");
        add("item." + MODID + "." + Strings.trueLightsFlight + ".desc", "A weapon that enables your attacks to reach a wide area and deal heavy damage.");
        add("item." + MODID + "." + Strings.twilightBlaze + ".desc", "A weapon that boasts superior Strength and ground combo speed.");
        add("item." + MODID + "." + Strings.twoBecomeOne + ".desc", "A weapon of great strength and magic that has a special effect.");
        add("item." + MODID + "." + Strings.ultimaWeaponBBS + ".desc", "The most powerful of Keyblades.");
        add("item." + MODID + "." + Strings.ultimaWeaponDDD + ".desc", "An outstanding Keyblade that boosts all stats, and makes it easy to both land critical hits and trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH1 + ".desc", "The ultimate Keyblade. Raises max MP by 2, and possesses maximum power and attributes.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH2 + ".desc", "The Keyblade above all others, holding all power and will increase MP restoration rate, once all MP has been consumed.");
        add("item." + MODID + "." + Strings.ultimaWeaponKH3 + ".desc", "The supreme Keyblade.");
        add("item." + MODID + "." + Strings.umbrella + ".desc", "This looks awfully familiar...");
        add("item." + MODID + "." + Strings.unbound + ".desc", "Keyblade perfection. It boosts all stats, while making it easy to land critical hits and even easier to trigger Reality Shifts.");
        add("item." + MODID + "." + Strings.victoryLine + ".desc", "A Keyblade with above-average reach that makes it easier to land critical hits.");
        add("item." + MODID + "." + Strings.voidGear + ".desc", "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.voidGearRemnant + ".desc", "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
        add("item." + MODID + "." + Strings.wayToTheDawn + ".desc", "Deals various attacks.");
        add("item." + MODID + "." + Strings.waywardWind + ".desc", "The Keyblade Ventus started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
        add("item." + MODID + "." + Strings.wheelOfFate + ".desc", "A Keyblade with an emphasis on Strength.");
        add("item." + MODID + "." + Strings.winnersProof + ".desc", "Has high strength and hold's an excellent magic power. When the enemies are defeated, experience points are not gained.");
        add("item." + MODID + "." + Strings.wishingLamp + ".desc", "Wishes come true by increasing the drop rate of munny, HP and MP orbs.");
        add("item." + MODID + "." + Strings.wishingStar + ".desc", "Has a short reach, but always finishes up a combo attack with a powerful critical blow.");
        add("item." + MODID + "." + Strings.youngXehanortsKeyblade + ".desc", "The Keyblade weilded by Young Xehanort.");
        add("item." + MODID + "." + Strings.zeroOne + ".desc", "A Keyblade newly wrought within the datascape. Its powers render all opponents helpless.");
        add("item." + MODID + "." + Strings.k111 + ".desc", "A keyblade from a long forgotten age, it seems to resemble something familiar...");

        //Organization weapons
        //Xemnas
        addItem(ModItems.malice, "Malicia");
        addItem(ModItems.sanction, "Sanci\u00f3n");
        addItem(ModItems.overlord, "Caudillo");
        addItem(ModItems.veneration, "Veneraci\u00f3n");
        addItem(ModItems.autocracy, "Autocracia");
        addItem(ModItems.conquest, "Conquista");
        addItem(ModItems.terminus, "Terminus");
        addItem(ModItems.judgement, "Veredicto");
        addItem(ModItems.discipline, "Disciplina");
        addItem(ModItems.aristocracy, "Aristocracia");
        addItem(ModItems.superiority, "Superioridad");
        addItem(ModItems.aggression, "Agresi\u00f3n");
        addItem(ModItems.fury, "Furia");
        addItem(ModItems.despair, "Desesperanza");
        addItem(ModItems.triumph, "Triunfo");
        addItem(ModItems.ruination, "Ruina");
        addItem(ModItems.domination, "Dominaci\u00f3n");
        addItem(ModItems.annihilation, "Aniquilaci\u00f3n");
        addItem(ModItems.tyrant, "Tirana");
        addItem(ModItems.magnificence, "Magnificencia");
        addItem(ModItems.infinity, "Infinidad");
        addItem(ModItems.interdiction, "Interdicci\u00f3n");
        addItem(ModItems.roundFan, "Paipay");
        addItem(ModItems.absolute, "Incuestionable");

        //Xigbar
        addItem(ModItems.standalone, "Independiente");
        addItem(ModItems.killerbee, "Abeja Asesina");
        addItem(ModItems.stingray, "Stingray");
        addItem(ModItems.counterweight, "Contrapeso");
        addItem(ModItems.precision, "Precisi\u00f3n");
        addItem(ModItems.dualHead, "Dos Cabezas");
        addItem(ModItems.bahamut, "Bahamut");
        addItem(ModItems.gullwing, "Ala de Gaviota");
        addItem(ModItems.blueFrame, "Marco Azul");
        addItem(ModItems.starShell, "Bengala");
        addItem(ModItems.sunrise, "Amanecer");
        addItem(ModItems.ignition, "Ignici\u00f3n");
        addItem(ModItems.armstrong, "Brazo Fuerte");
        addItem(ModItems.hardBoiledHeat, "Calor Hirviente");
        addItem(ModItems.diabloEye, "Ojo del Diablo");
        addItem(ModItems.doubleTap, "Doble Llave");
        addItem(ModItems.stardust, "Arena Estelar");
        addItem(ModItems.energyMuzzle, "Bozal de Energ\u00eda");
        addItem(ModItems.crimeAndPunishment, "Crimen y Castigo");
        addItem(ModItems.cupidsArrow, "Flecha de Cupido");
        addItem(ModItems.finalWeapon, "Arma Final");
        addItem(ModItems.sharpshooter, "Tiradora");
        addItem(ModItems.dryer, "Secador");
        addItem(ModItems.trumpet, "Trompeta");

        //Xaldin
        addItem(ModItems.zephyr, "C\u00e9firo");
        addItem(ModItems.moonglade, "Claro de la Luna");
        addItem(ModItems.aer, "Aer");
        addItem(ModItems.nescience, "Ignorancia");
        addItem(ModItems.brume, "Bruma");
        addItem(ModItems.asura, "Asura");
        addItem(ModItems.crux, "Quid");
        addItem(ModItems.paladin, "Palad\u00edn");
        addItem(ModItems.fellking, "Cay\u00f3 el Rey");
        addItem(ModItems.nightcloud, "Noche Nublada");
        addItem(ModItems.shimmer, "Brillo");
        addItem(ModItems.vortex, "V\u00f3rtice");
        addItem(ModItems.scission, "Escisi\u00f3n");
        addItem(ModItems.heavenfall, "Ca\u00edda Celestial");
        addItem(ModItems.aether, "Aether");
        addItem(ModItems.mazzaroth, "Mazzaroth");
        addItem(ModItems.hegemon, "Hegem\u00f3n");
        addItem(ModItems.foxfire, "Zorro de Fuego");
        addItem(ModItems.yaksha, "Yaksha");
        addItem(ModItems.cynosura, "Cynosura");
        addItem(ModItems.dragonreign, "Reino del Drag\u00f3n");
        addItem(ModItems.lindworm, "Sierpe");
        addItem(ModItems.broom, "Escoba");
        addItem(ModItems.wyvern, "Wyvern");

        //Vexen
        addItem(ModItems.testerZero, "Prueba Cero");
        addItem(ModItems.productOne, "Primer Producto");
        addItem(ModItems.deepFreeze, "Par\u00e1lisis Total");
        addItem(ModItems.cryoliteShield, "Escudo Criolita");
        addItem(ModItems.falseTheory, "teor\u00eda Falsa");
        addItem(ModItems.glacier, "Glaciar");
        addItem(ModItems.absoluteZero, "Cero Absoluto");
        addItem(ModItems.gunz, "Gunz");
        addItem(ModItems.mindel, "Mindel");
        addItem(ModItems.snowslide, "Alud");
        addItem(ModItems.iceberg, "Iceberg");
        addItem(ModItems.inquisition, "Inquisici\u00f3n");
        addItem(ModItems.scrutiny, "Escrutinio");
        addItem(ModItems.empiricism, "Empirismo");
        addItem(ModItems.edification, "Edificaci\u00f3n");
        addItem(ModItems.contrivance, "Artima\u00f1a");
        addItem(ModItems.wurm, "W\u00fcrm");
        addItem(ModItems.subzero, "Subcero");
        addItem(ModItems.coldBlood, "Sangre Fr\u00eda");
        addItem(ModItems.diamondShield, "Escudo Diamante");
        addItem(ModItems.aegis, "\u00e9gida");
        addItem(ModItems.frozenPride, "Orgullo Helado");
        addItem(ModItems.potLid, "Tapa de Cazuela");
        addItem(ModItems.snowman, "Mu\u00f1eco de Nieve");

        //Lexaeus
        addItem(ModItems.reticence, "Reticencia");
        addItem(ModItems.goliath, "Goliat");
        addItem(ModItems.copperRed, "Rojo Cobre");
        addItem(ModItems.daybreak, "Alba");
        addItem(ModItems.colossus, "Coloso");
        addItem(ModItems.ursaMajor, "Osa Mayor");
        addItem(ModItems.megacosm, "Macrocosmos");
        addItem(ModItems.terrene, "Terrenal");
        addItem(ModItems.fuligin, "Fuligin");
        addItem(ModItems.hardWinter, "Duro Invierno");
        addItem(ModItems.firefly, "Luci\u00e9rnaga");
        addItem(ModItems.harbinger, "Heraldo");
        addItem(ModItems.redwood, "Velintonia");
        addItem(ModItems.sequoia, "Sequoya");
        addItem(ModItems.ironBlack, "Negro Fierro");
        addItem(ModItems.earthshine, "Brillo de Tierra");
        addItem(ModItems.octiron, "Octarino");
        addItem(ModItems.hyperion, "Modelo Hyperion");
        addItem(ModItems.clarity, "Claridad");
        addItem(ModItems.oneThousandAndOneNights, "1001 Noches");
        addItem(ModItems.cardinalVirtue, "Virtud Cardinal");
        addItem(ModItems.skysplitter, "Rompecielos");
        addItem(ModItems.bleepBloopBop, "Piiitidos");
        addItem(ModItems.monolith, "Monolito");

        //Zexion
        addItem(ModItems.blackPrimer, "Castilla Negra");
        addItem(ModItems.whiteTome, "Tomo Blanco");
        addItem(ModItems.illicitResearch, "Estudio Il\u00edcito");
        addItem(ModItems.buriedSecrets, "Secretos Enterrados");
        addItem(ModItems.arcaneCompendium, "Compendio Arcano");
        addItem(ModItems.dissentersNotes, "Notas Disidentes");
        addItem(ModItems.nefariousCodex, "C\u00f3dice Nefario");
        addItem(ModItems.mysticAlbum, "\u00e1lbum M\u00edstico");
        addItem(ModItems.cursedManual, "Manual Maldito");
        addItem(ModItems.tabooText, "Texto Tab\u00fa");
        addItem(ModItems.eldritchEsoterica, "Esoterismo Negro");
        addItem(ModItems.freakishBestiary, "Bestiario Raro");
        addItem(ModItems.madmansVita, "Curr\u00edculo Loco");
        addItem(ModItems.untitledWritings, "Texto sin Nombre");
        addItem(ModItems.abandonedDogma, "Dogma Apartado");
        addItem(ModItems.atlasOfOmens, "Atlas Prof\u00e9tico");
        addItem(ModItems.revoltingScrapbook, "Recortes Sucios");
        addItem(ModItems.lostHeterodoxy, "Preterodoxia");
        addItem(ModItems.otherworldlyTales, "Anales Infernales");
        addItem(ModItems.indescribableLore, "Saber Incre\u00edble");
        addItem(ModItems.radicalTreatise, "Tratado Radical");
        addItem(ModItems.bookOfRetribution, "Libro Retribuci\u00f3n");
        addItem(ModItems.midnightSnack, "Aperitivo de Noche");
        addItem(ModItems.dearDiary, "Diario de Muerte");

        //Saix
        addItem(ModItems.newMoon, "Luna Nueva");
        addItem(ModItems.werewolf, "Hombre Lobo");
        addItem(ModItems.artemis, "Artemisa");
        addItem(ModItems.luminary, "Luminaria");
        addItem(ModItems.selene, "Selene");
        addItem(ModItems.moonrise, "Salida de Luna");
        addItem(ModItems.astrologia, "Astrolog\u00eda");
        addItem(ModItems.crater, "Cr\u00e1ter");
        addItem(ModItems.lunarPhase, "Fase Lunar");
        addItem(ModItems.crescent, "Luna Creciente");
        addItem(ModItems.gibbous, "Luna Llena");
        addItem(ModItems.berserker, "Enloquecedor");
        addItem(ModItems.twilight, "Crep\u00fasculo");
        addItem(ModItems.queenOfTheNight, "Reina Nocturna");
        addItem(ModItems.balsamicMoon, "Luna Bals\u00e1mica");
        addItem(ModItems.orbit, "\u00f3rbita");
        addItem(ModItems.lightYear, "A\u00f1o Luz");
        addItem(ModItems.kingOfTheNight, "Rey Nocturno");
        addItem(ModItems.moonset, "Puesta de Luna");
        addItem(ModItems.horoscope, "Hor\u00f3scopo");
        addItem(ModItems.dichotomy, "Dicotom\u00eda");
        addItem(ModItems.lunatic, "Lun\u00e1tica");
        addItem(ModItems.justDesserts, "Solo Postres");
        addItem(ModItems.bunnymoon, "Luna Conejera");

        //Axel
        addItem(ModItems.ashes, "Cenizas");
        addItem(ModItems.doldrums, "Estancamiento");
        addItem(ModItems.delayedAction, "Acci\u00f3n Retrasada");
        addItem(ModItems.diveBombers, "Bombarderos");
        addItem(ModItems.combustion, "Combusti\u00f3n");
        addItem(ModItems.moulinRouge, "Moulin Rouge");
        addItem(ModItems.blazeOfGlory, "Brillo Glorioso");
        addItem(ModItems.prometheus, "Prometeo");
        addItem(ModItems.ifrit, "Ifrit");
        addItem(ModItems.magmaOcean, "Oc\u00e9ano de Magma");
        addItem(ModItems.volcanics, "Volc\u00e1nicos");
        addItem(ModItems.inferno, "Infierno");
        addItem(ModItems.sizzlingEdge, "Filo Crepitante");
        addItem(ModItems.corona, "Corona");
        addItem(ModItems.ferrisWheel, "Norias");
        addItem(ModItems.burnout, "Quemaz\u00f3n");
        addItem(ModItems.omegaTrinity, "Tr\u00edo Omega");
        addItem(ModItems.outbreak, "Estallido");
        addItem(ModItems.doubleEdge, "Doble Filo");
        addItem(ModItems.wildfire, "Fuego Desbocado");
        addItem(ModItems.prominence, "Importancia");
        addItem(ModItems.eternalFlames, "Llamas Eternas");
        addItem(ModItems.pizzaCut, "Porci\u00f3n de Pizza");
        addItem(ModItems.conformers, "Conformistas");

        //Demyx
        addItem(ModItems.basicModel, "Modelo B\u00e1sico");
        addItem(ModItems.tuneUp, "Puesta a Punto");
        addItem(ModItems.quartet, "Cuarteto");
        addItem(ModItems.quintet, "Quinteto");
        addItem(ModItems.overture, "Obertura");
        addItem(ModItems.oldHand, "Vieja Mano");
        addItem(ModItems.daCapo, "Da Capo");
        addItem(ModItems.powerChord, "Acorde de Poder");
        addItem(ModItems.fermata, "Calder\u00f3n");
        addItem(ModItems.interlude, "Interludio");
        addItem(ModItems.serenade, "Serenata");
        addItem(ModItems.songbird, "P\u00e1jaro Cantor");
        addItem(ModItems.riseToFame, "Ascenso a Fama");
        addItem(ModItems.rockStar, "Estrella de Rock");
        addItem(ModItems.eightFinger, "A Ocho Dedos");
        addItem(ModItems.concerto, "Concerto");
        addItem(ModItems.harmonics, "Arm\u00f3nicos");
        addItem(ModItems.millionBucks, "Mill\u00f3n de Pavos");
        addItem(ModItems.fortissimo, "Fortissimo");
        addItem(ModItems.upToEleven, "Hasta Once");
        addItem(ModItems.sanctuary, "Santuario");
        addItem(ModItems.arpeggio, "Arpegio");
        addItem(ModItems.princeOfAwesome, "Pr\u00edncipe Imponente");
        addItem(ModItems.afterSchool, "Tras el Cole");

        //Luxord
        addItem(ModItems.theFool, "El Loco");
        addItem(ModItems.theMagician, "El Mago");
        addItem(ModItems.theStar, "La Estrella");
        addItem(ModItems.theMoon, "La Luna");
        addItem(ModItems.justice, "La Justicia");
        addItem(ModItems.theHierophant, "El Sumo Sacerdote");
        addItem(ModItems.theWorld, "El Mundo");
        addItem(ModItems.temperance, "La Templanza");
        addItem(ModItems.theHighPriestess, "La Papisa");
        addItem(ModItems.theTower, "La Torre");
        addItem(ModItems.theHangedMan, "El Colgado");
        addItem(ModItems.death, "La Muerte");
        addItem(ModItems.theHermit, "El Ermita\u00f1o");
        addItem(ModItems.strength, "La Fuerza");
        addItem(ModItems.theLovers, "Los Enamorados");
        addItem(ModItems.theChariot, "El Carro");
        addItem(ModItems.theSun, "El Sol");
        addItem(ModItems.theDevil, "El Diablo");
        addItem(ModItems.theEmpress, "La Emperatriz");
        addItem(ModItems.theEmperor, "El Emperador");
        addItem(ModItems.theJoker, "El Comod\u00edn");
        addItem(ModItems.fairGame, "Juego Limpio");
        addItem(ModItems.finestFantasy13, "Fabula Totalis 13");
        addItem(ModItems.highRollersSecret, "Secreto del Juego");

        //Marluxia
        addItem(ModItems.fickleErica, "Eric\u00e1cea Voluble");
        addItem(ModItems.jiltedAnemone, "An\u00e9mona Plantada");
        addItem(ModItems.proudAmaryllis, "Amarilis Orgulloso");
        addItem(ModItems.madSafflower, "Alazor Loco");
        addItem(ModItems.poorMelissa, "Pobre Melisa");
        addItem(ModItems.tragicAllium, "Allium Tr\u00e1gico");
        addItem(ModItems.mournfulCineria, "Cineraria Dolida");
        addItem(ModItems.pseudoSilene, "Pseudo Silene");
        addItem(ModItems.faithlessDigitalis, "Digitalis Infiel");
        addItem(ModItems.grimMuscari, "Muscaria T\u00e9trica");
        addItem(ModItems.docileVallota, "Vallota D\u00f3cil");
        addItem(ModItems.quietBelladonna, "Belladona En Paz");
        addItem(ModItems.partingIpheion, "Ipheion Saliente");
        addItem(ModItems.loftyGerbera, "Gerbera Elevada");
        addItem(ModItems.gallantAchillea, "Milenrama de Luz");
        addItem(ModItems.noblePeony, "Noible Peon\u00eda");
        addItem(ModItems.fearsomeAnise, "An\u00eds Ind\u00f3mito");
        addItem(ModItems.vindictiveThistle, "Cardo Vengativo");
        addItem(ModItems.fairHelianthus, "Helianthus Justo");
        addItem(ModItems.solemnMagnolia, "Magnolia Solemne");
        addItem(ModItems.hallowedLotus, "Loto Sagrado");
        addItem(ModItems.gracefulDahlia, "Dalia Gr\u00e1cil");
        addItem(ModItems.stirringLadle, "Cuchar\u00f3n");
        addItem(ModItems.daintyBellflowers, "Campanilla Fr\u00e1gil");

        //Larxene
        addItem(ModItems.trancheuse, "Trinchador");
        addItem(ModItems.orage, "Tormenta");
        addItem(ModItems.tourbillon, "Torbellino");
        addItem(ModItems.tempete, "Tempestad");
        addItem(ModItems.carmin, "Carm\u00edn");
        addItem(ModItems.meteore, "Meteoro");
        addItem(ModItems.etoile, "Estrella");
        addItem(ModItems.irregulier, "Irregular");
        addItem(ModItems.dissonance, "Disonancia");
        addItem(ModItems.eruption, "Erupci\u00f3n");
        addItem(ModItems.soleilCouchant, "Puesta de Sol");
        addItem(ModItems.indigo, "\u00edndigo");
        addItem(ModItems.vague, "Oleaje");
        addItem(ModItems.deluge, "Diluvio");
        addItem(ModItems.rafale, "R\u00e1faga");
        addItem(ModItems.typhon, "Tif\u00f3n");
        addItem(ModItems.extirpeur, "Extripador");
        addItem(ModItems.croixDuSud, "Cruz del Sur");
        addItem(ModItems.lumineuse, "Luminosa");
        addItem(ModItems.clairDeLune, "Claro de Luna");
        addItem(ModItems.volDeNuit, "Vuelo Nocturno");
        addItem(ModItems.foudre, "Rayo");
        addItem(ModItems.demoiselle, "Damisela");
        addItem(ModItems.ampoule, "Bombilla");

        //Entities
        addEntityType(ModEntities.TYPE_BLAST_BLOX,"Bloque Explosivo Prendido");
        addEntityType(ModEntities.TYPE_PAIR_BLOX, "Bloque Emparejado");

        addEntityType(ModEntities.TYPE_BLIZZARD, "Hielo");
        addEntityType(ModEntities.TYPE_FIRE, "Piro");
        addEntityType(ModEntities.TYPE_THUNDER, "Electro");
        addEntityType(ModEntities.TYPE_THUNDERBOLT, "Rayo Electro");
        addEntityType(ModEntities.TYPE_GRAVITY, "Gravedad");
        addEntityType(ModEntities.TYPE_MAGNET, "Magneto");
        addEntityType(ModEntities.TYPE_WATER, "Aqua");
        addEntityType(ModEntities.TYPE_KK_THROWABLE, "Chakram");
        addEntityType(ModEntities.TYPE_ORG_PORTAL, "Portal de la Organizaci\u00f3n");
        addEntityType(ModEntities.TYPE_HPORB, "Orbe de HP");
        addEntityType(ModEntities.TYPE_MPORB, "Orbe de MP");
        addEntityType(ModEntities.TYPE_DRIVEORB, "Orbe de DP");
        addEntityType(ModEntities.TYPE_MUNNY, "Platines");

        addEntityType(ModEntities.TYPE_SPAWNING_ORB, "Orbe Generador");

        addEntityType(ModEntities.TYPE_MOOGLE, "Moguri");
        addEntityType(ModEntities.TYPE_SHADOW, "Sombra");
        addEntityType(ModEntities.TYPE_MEGA_SHADOW, "MegaSombra");
        addEntityType(ModEntities.TYPE_GIGA_SHADOW, "GigaSombra");
        addEntityType(ModEntities.TYPE_DARKBALL, "Bola oscura");
        addEntityType(ModEntities.TYPE_SHADOW_GLOB, "Mole de Sombra");

        addEntityType(ModEntities.TYPE_MINUTE_BOMB, "Minuto Explosivo");
        addEntityType(ModEntities.TYPE_SKATER_BOMB, "Bomba Patinadora");
        addEntityType(ModEntities.TYPE_STORM_BOMB, "Bomba Tormentosa");
        addEntityType(ModEntities.TYPE_DETONATOR, "Detonador");

        addEntityType(ModEntities.TYPE_RED_NOCTURNE, "Nocturno Rojo");
        addEntityType(ModEntities.TYPE_BLUE_RHAPSODY, "Rapsodia Azul");
        addEntityType(ModEntities.TYPE_YELLOW_OPERA, "\u00f3pera Amarilla");
        addEntityType(ModEntities.TYPE_GREEN_REQUIEM, "R\u00e9quiem Verde");
        addEntityType(ModEntities.TYPE_EMERALD_BLUES, "Blues Esmeralda");
        addEntityType(ModEntities.TYPE_LARGE_BODY, "Grandull\u00f3n");
        addEntityType(ModEntities.TYPE_DIRE_PLANT, "Mala Hierba");
        addEntityType(ModEntities.TYPE_SOLDIER, "Soldado");
        addEntityType(ModEntities.TYPE_DESERTER, "Desertor");
        addEntityType(ModEntities.TYPE_COMMANDER, "Commander");
        addEntityType(ModEntities.TYPE_WHITE_MUSHROOM, "Seta Blanca");
        addEntityType(ModEntities.TYPE_BLACK_FUNGUS, "Hongo Negro");
        addEntityType(ModEntities.TYPE_BLOX_BUG, "Bloque de Error");

        addEntityType(ModEntities.TYPE_NOBODY_CREEPER, "Trepador");
        addEntityType(ModEntities.TYPE_DUSK, "Umbr\u00edo");
        addEntityType(ModEntities.TYPE_ASSASSIN, "Asesino");
        addEntityType(ModEntities.TYPE_DRAGOON, "Dragón");
        addEntityType(ModEntities.TYPE_MARLUXIA, "Marluxia");

        addEntityType(ModEntities.TYPE_TRAINING_DUMMY, "Espantapájaros de entrenamiento");
        addEntityType(ModEntities.TYPE_MAGIC_TARGET, "Diana Mágica");


        //Spawn eggs
        addItem(ModEntities.MOOGLE_EGG, "Generar Moguri");
        addItem(ModEntities.SHADOW_EGG, "Generar Sombra");
        addItem(ModEntities.MEGA_SHADOW_EGG, "Generar MegaSombra");
        addItem(ModEntities.GIGA_SHADOW_EGG, "Generar GigaSombra");
        addItem(ModEntities.DARKBALL_EGG, "Generar Bola oscura");
        addItem(ModEntities.SHADOW_GLOB_EGG, "Generar Mole de Sombra");

        addItem(ModEntities.MINUTE_BOMB_EGG, "Generar Minuto Explosivo");
        addItem(ModEntities.SKATER_BOMB_EGG, "Generar Bomba Patinadora");
        addItem(ModEntities.STORM_BOMB_EGG, "Generar Bomba Tormentosa");
        addItem(ModEntities.DETONATOR_EGG, "Generar Detonador");

        addItem(ModEntities.RED_NOCTURNE_EGG, "Generar Nocturno Rojo");
        addItem(ModEntities.BLUE_RHAPSODY_EGG, "Generar Rapsodia Azul");
        addItem(ModEntities.YELLOW_OPERA_EGG, "Generar Ópera Amarilla");
        addItem(ModEntities.GREEN_REQUIEM_EGG, "Generar R\u00e9quiem Verde");
        addItem(ModEntities.EMERALD_BLUES_EGG, "Generar Blues Esmeralda");
        addItem(ModEntities.LARGE_BODY_EGG, "Generar Grandull\u00f3n");
        addItem(ModEntities.DIRE_PLANT_EGG, "Generar Mala Hierba");
        addItem(ModEntities.SOLDIER_EGG, "Generar Soldado");
        addItem(ModEntities.DESERTER_EGG, "Generar Desertor");
        addItem(ModEntities.COMMANDER_EGG, "Generar Comandante");
        addItem(ModEntities.WHITE_MUSHROOM_EGG, "Generar Seta Blanca");
        addItem(ModEntities.BLACK_FUNGUS_EGG, "Generar Hongo Negro");
        addItem(ModEntities.BLOX_BUG_EGG, "Generar Bloque de Error");

        addItem(ModEntities.NOBODY_CREEPER_EGG, "Generar Trepador");
        addItem(ModEntities.DUSK_EGG, "Generar Umbr\u00edo");
        addItem(ModEntities.ASSASSIN_EGG, "Generar Asesino");
        addItem(ModEntities.DRAGOON_EGG, "Generar Drag\u00f3n");
        addItem(ModEntities.MARLUXIA_EGG, "Generar Marluxia");

        //Messages (above hotbar)
        add("message.magnet_blox.attract", "Modo Atracci\u00f3n");
        add("message.magnet_blox.repel", "Modo Repeler");
        add("message.magnet_blox.range", "El rango es: %s");
        add("message.form_unlocked", "Desbloqueado/a %s");
        add("message.chest.lock", "Usa una llave espada para bloquear el cofre");
        add("message.chest.can_be_locked", "Puede ser bloqueado con una llave espada");
        add("message.chest.locked", "Este cofre est\u00e1 bloqueado");
        add("message.chest.keyblade_set", "Has establecido tu llave espada para desbloquear este cofre");
        add("message.chest.unlocked", "El Cofre ha sido desbloqueado");
        add("message.magic_max_level", "%s ya est\u00e1 en el m\u00e1ximo nivel");
        add("message.magic_upgrade", "%s ha mejorado a %s");
        add("message.unlocked", "Desbloqueado %s");
        add("message.wayfinder.player_not_found", "Jugador %s no encontrado");
        add("message.wayfinder.in_your_party", "de tu grupo");
        add("message.wayfinder.not_in_party", "No estás en un grupo");
        add("message.wayfinder.player_not_in_party", "El jugador %s no está en tu grupo");
        add("message.wayfinder.player_not_online", "El jugador %s no está en línea");
        add("message.wayfinder.your_wayfinder", "Este es tu siemprejuntos, dáselo a otro jugador");
        add("message.wayfinder.owner","Propietario: %s");
        add("message.wayfinder.cooldown","Recarga: %s%%");
        add("message.wayfinder.calling_for_help","%s te reclama, ¡usa su siemprejuntos!");
        add("message.wayfinder.asking_other_for_help","Llamando a %s para venir aquí");
        add("message.wayfinder.player_has_no_wayfinder","%s no tiene tu siemprejuntos encima ahora mismo");
        add("message.wayfinder.tooltip1", "Click derecho para teletransportarte");
        add("message.wayfinder.tooltip2", "Shift + click derecho para llamar");

        add("message.recipe.already_learnt", "Receta ya aprendida para %s");
        add("message.recipe.cant_learn_yet", "A\u00fan no puedes aprender esa receta");
        add("message.recipe.learnt", "Aprendida la receta de %s");
        add("message.recipe.no_more_to_learn", "No quedan m\u00e1s recetas para aprender");
        add("message.kingdomkeys.gui_toggle", "Interfaz establecida en: %s");

        //Station of awakening
        add("soa.menu.1", "Antes de poder abrir el men\u00fa.");
        add("soa.menu.2", "Debes tomar una decisi\u00f3n.");
        add("soa.menu.ok", "Adelante");
        add("soa.menu.cancel", "Mejor no");
        add("soa.warrior.1", "El poder del guerrero.");
        add("soa.warrior.2", "Valent\u00eda sin igual.");
        add("soa.warrior.3", "Una espada hecha para herir al pr\u00f3jimo.");
        add("soa.guardian.1", "El poder del guardi\u00e1n.");
        add("soa.guardian.2", "Bondad para ayudar a los amigos.");
        add("soa.guardian.3", "Un escudo cobarde que todo lo rechaza.");
        add("soa.mystic.1", "El poder del hechicero.");
        add("soa.mystic.2", "Fuerza interior.");
        add("soa.mystic.3", "Un bast\u00f3n capaz de robarte el alma.");
        add("soa.choice.confirm", "\u00bfEste es el poder que buscas?");
        add("soa.sacrifice.confirm", "\u00bfRenuncias a este poder?");
        add("soa.ok", "S\u00ed");
        add("soa.cancel", "No");
        add("soa.confirm.cancel", "Tal vez no");
        add("soa.title", "Estaci\u00f3n del Despertar");
        add("soa.subtitle", "Descenso al Coraz\u00f3n");
        add("soa.choice.intro.1", "El poder est\u00e1 en ti.");
        add("soa.choice.intro.2", "Si le das forma...");
        add("soa.choice.intro.3", "te dar\u00e1 fuerza.");
        add("soa.choice.intro.4", "Elige bien.");
        add("soa.sacrifice.intro.1", "Tu camino est\u00e1 decidido.");
        add("soa.sacrifice.intro.2", "Ahora, \u00bfa qu\u00e9 renunciar\u00e1s a cambio?");
        add("soa.reset.intro.1", "Elige con cuidado.");
        add("soa.reset.intro.2", "\u00bfQu\u00e9 forma adoptar\u00e1 tu poder?");
        add("soa.confirm.1", "Has elegido el");
        add("soa.confirm.warrior", "poder del guerrero.");
        add("soa.confirm.guardian", "poder del guardi\u00e1n.");
        add("soa.confirm.mystic", "poder del hechicero.");
        add("soa.confirm.3", "Has renunciado al");
        add("soa.confirm.5", "\u00bfEs esta la forma que quieres eligir?");

        add(HeartlessIntro1, "Este mundo ha sido conectado");
        add(HeartlessIntro2, "Unido a la oscuridad...");
        add(HeartlessIntro3, "Y pronto perder\u00e1 la luz");

        //CO intro
        add(COIntro1, "Aquí, encontrar es perder");
        add(COIntro2, "y perder es encontrar.");
        add(COIntro3, "Así son las cosas en el Castillo del Olvido.");
        add(COIntroTitle, "Castillo del Olvido");

        add("co.criteria_greater", "Condición: Una carta de valor %s, superior o 0.");
        add("co.criteria_lesser", "Condición: Una carta de valor %s o inferior.");
        add("co.criteria_equal", "Condición: Una carta de valor 0.");
        add("co.criteria_total", "Condición: Cartas para sumar un totaL de %s o superior.");
        add("co.criteria_greater_no_zero", "Condición: Una carta de valor %s o superior.");

        add("co.available_cards", "Cartas disponibles");

        add("co.category", "CATEGORÍA");
        add("co.room_size", "TAMAÑO ESTANCIA");
        add("co.enemies", "ENEMIGOS");

        add("co.category.enemy", "ENEMIGO");
        add("co.category.status", "ESTATUS");
        add("co.category.bounty", "BOTÍN");
        add("co.category.encounter", "ENCUENTRO");
        add("co.category.special", "ESPECIAL");
        add("co.category.any", "CUALQUIERA");

        add("gui.cardpacks.title", "Pack de Cartas");
        add("co.card_pack.reveal_all", "Revelar todas");

        add("co.door_succeed","Domina las cartas y ábrete camino por el castillo. De ahora en adelante tendrás que apañártelas solo.");
        add("co.door_failed","Sujeta la carta ante ti. La puerta se abrirá y tras ella un nuevo mundo");

        add("co.encounter.wave", "Oleada");
        add("co.encounter.end", "Encuentro terminado");

        add("kingdomkeys.struggle.starting", "¡Empieza el Struggle...!");
        add("kingdomkeys.struggle.tournament.next_match", "Siguiente combate del torneo...");
        add("kingdomkeys.struggle.ffa.starting", "¡Empieza el todos contra todos...!");
        add("kingdomkeys.struggle.go", "¡YA!");
        add("kingdomkeys.struggle.win", "¡Has ganado!");
        add("kingdomkeys.struggle.lose", "Has perdido");
        add("kingdomkeys.struggle.tournament.bye", "¡Pasas de ronda sin combatir!");
        add("kingdomkeys.struggle.tournament.champion", "Campeón del torneo:");
        add("kingdomkeys.struggle.tournament.round_winner", "Gana la ronda:");
        add("kingdomkeys.struggle.no_hotbar_space", "¡Libera un hueco en la barra de acceso rápido!");
        add("kingdomkeys.struggle.tie.overtime", "¡Empate! ¡Muerte súbita!");
        add("kingdomkeys.struggle.draw", "¡Empate!");

        add(Strings.Gui_Menu_Struggle_Menu_Title, "Struggle");
        add(Strings.Gui_Menu_Struggle_Create_Title, "Empezar Struggle");
        add(Strings.Gui_Menu_Struggle_Join_Title, "Unirse a un Struggle");
        add(Strings.Gui_Menu_Struggle_Settings_Title, "Ajustes del Struggle");
        add(Strings.Gui_Menu_Struggle_Create_Button, "Crear partida");
        add(Strings.Gui_Menu_Struggle_Join_Button, "Unirse a partida");
        add(Strings.Gui_Menu_Struggle_Settings_Button, "Ajustes del Struggle");
        add(Strings.Gui_Menu_Struggle_Delete_Button, "Eliminar partida");
        add(Strings.Gui_Menu_Struggle_Leave_Button, "Abandonar partida");
        add(Strings.Gui_Menu_Struggle_Ready, "Listo");
        add(Strings.Gui_Menu_Struggle_Cancel_Ready, "Cancelar listo");
        add(Strings.Gui_Menu_Struggle_Name_And_Size, "Nombre y tamaño del Struggle");
        add(Strings.Gui_Menu_Struggle_Name, "Nombre del Struggle");
        add(Strings.Gui_Menu_Struggle_Damage_Mult, "Multiplicador de orbes (%)");
        add(Strings.Gui_Menu_Struggle_Round_Time, "Duración de la ronda (segundos)");
        add(Strings.Gui_Menu_Struggle_Starting_Score, "Orbes iniciales");
        add(Strings.Gui_Menu_Struggle_Spectator_Pos, "Zona de espectadores (x,y,z)");
        add(Strings.Gui_Menu_Struggle_Mode, "Modo");
        add(Strings.Gui_Menu_Struggle + ".duel", "Duelo");
        add(Strings.Gui_Menu_Struggle + ".tournament", "Torneo");
        add(Strings.Gui_Menu_Struggle + ".ffa", "Todos contra todos");

        //Biomes
        add("biome.kingdomkeys.dive_to_the_heart", "Descenso al Coraz\u00f3n");
        add("biome.kingdomkeys.realm_of_darkness", "Reino de la Oscuridad");
        add("biome.kingdomkeys.station_of_sorrow", "Estación del Pesar");
        add("biome.kingdomkeys.castle_oblivion", "Castillo del Olvido");
        add("biome.kingdomkeys.castle_oblivion_interior", "Interior del Castillo del Olvido");

        //Effects
        addKKEffect(ModMobEffects.FREEZE, "Hielo", "Ralentiza tu movimiento y termina causando quemadura de escarcha.");
        addKKEffect(ModMobEffects.AERO, "Aero", "Invoca un escudo de viento a tu alrededor, versiones superiores también dañan al contacto.");
        addKKEffect(ModMobEffects.STOP, "Paro", "Evita todo movimiento, todo el daño recibido se aplicará de golpe cuando el efecto termine.");
        addKKEffect(ModMobEffects.GRAVITY, "Gravedad", "Limita tu movimiento mientras estás aplastado.");
        addKKEffect(ModMobEffects.KO, "KO", "Permite a un jugador en tu grupo salvarte usando Cura o una Poción en tí.");
        addKKEffect(ModMobEffects.UNDERWORLD_CURSE, "Maldición del inframundo", "Prevents the use of drive forms and limits.");
        addKKEffect(ModMobEffects.ZERO_GRAVITY, "Ingravidez", "Te mantiene estacionario en el aire durante un tiempo.");
        addKKEffect(ModMobEffects.CONFUSE, "Confuse", "Invierte tu movimiento.");
        addKKEffect(ModMobEffects.MINI,"Mini","Reduce tu tamaño, vigila que no te pisen!");

        //CO Rooms
        addRoom("almighty_darkness", "Noche Cerrada");
        addRoom("bottomless_darkness", "Oscuridad Insondable");
        addRoom("feeble_darkness", "Oscuridad lánguida");
        addRoom("looming_darkness", "Tinieblas");
        addRoom("sleeping_darkness", "Oscuridad Latente");
        addRoom("teeming_darkness", "Oscuridad Total");
        addRoom("tranquil_darkness", "Oscuridad Tenue");

        addRoom("alchemic_waking", "Aula Alquímica");
        addRoom("martial_waking", "Aula Marcial");
        addRoom("sorcerous_waking", "Aula de Magia");
        addRoom("stagnant_space", "Ralentí");
        addRoom("weightless_space", "Espacio Liviano");

        addRoom("calm_bounty", "Botín Fácil");
        addRoom("false_bounty", "Falso Botín");
        addRoom("guarded_trove", "Tesoro Custodiado");
        addRoom("moments_reprieve", "Alivio Fugaz");
        addRoom("moogle_room", "Casa Moguri");
        addRoom("prosperous_repository", "Repositorio Próspero");
        addRoom("reposeful_grove", "Arboleda Serena");
        addRoom("treacherous_repository", "Repositorio Traicionero");

        addRoom("unknown_room", "Cuarto Desconocido");
        addRoom("conquerors_respite", "Reposo del Triunfador");
        addRoom("entrance_hall", "Entrada, planta %s");

        addRoom("room_of_beginnings", "Cuarto de los Comienzos");
        addRoom("room_of_guidance", "Cuarto de Guías");
        addRoom("room_of_rewards", "Cuarto de Recompensas");
        addRoom("room_of_truth", "Cuarto de la Verdad");


        //JEI
        add("jei.category.kingdomkeys.synthesis", "S\u00edntesis de Objetos");
        add("jei.category.kingdomkeys.melding", "Síntesis de Ítems");
        add("jei.category.kingdomkeys.savepoints", "Mejoras de Puntos de Guardado");
        add("jei.category.kingdomkeys.keyblade_summon", "Invocaci\u00f3n de Llave Espada");
        add("jei.category.kingdomkeys.synthesis.locked", "Receta no desbloqueada");
        add("jei.category.kingdomkeys.synthesis.unlocked", "Receta desbloqueada");
        add("jei.category.kingdomkeys.keyblade_summon.info", "Observa info para ver una gu\u00eda b\u00e1sica");
        add("jei.info.kingdomkeys.moogle_projector", "Obtenido de los Moguri cuando son aplastados con un yunque. Utilizado en la S\u00edntesis de Objetos y para subir de nivel las Llave Espada usando la Forja y depositando materiales de s\u00edntesis. Los Moguri tambi\u00e9n sirven para esto.");
        add("jei.info.kingdomkeys.organization_weapons", "Como miembro de la Organizaci\u00f3n puedes desbloquear armas desde el men\u00fa de equipamiento utilizando corazones obtenidos al derrotar enemigos, obtendr\u00e1s el doble de corazones si utilizas una arma del miembro que elegiste. Invoca las armas con la tecla de invocar.");
        add("jei.info.kingdomkeys.organization_robes", "Equ\u00edpate el set completo para unirte y seleccionar un miembro, sin importar a quien elijas puedes desbloquear cualquiera de las armas pero requiere que desbloquees las armas de los miembros adyacentes primero.");
        add("jei.info.kingdomkeys.proof_of_heart", "Obtenido al derrotar el Drag\u00f3n, util\u00edzalo para salir de la Organizaci\u00f3n.");
        add("jei.info.kingdomkeys.keychains", "Los llaveros pueden ser utilizados para invocar su Llave Espada correspondiente al equiparlo desde el men\u00fa de Kingdom Keys y pulsando la tecla de invocar.");
        add("jei.info.kingdomkeys.recipes", "Dropeada de los mobs y encontrada en las casas Moguri de las aldeas. Util\u00edzalas para desbloquear recetas de s\u00edntesis.");
        add("jei.info.kingdomkeys.ghost_blox", "Apl\u00edca una se\u00f1al de redstone para conmutar la visibilidad del Bloque Fantasma y todos los adyacentes, mientras son invisibles no tienen colisi\u00f3n.");
        add("jei.info.kingdomkeys.danger_blox", "Similar al Cactus pero hace m\u00e1s da\u00f1o, causa da\u00f1o al contacto y cuando es golpeado. A diferencia del Cactus no tiene l\u00edmite de crecimiento ni posicionamiento. Equ\u00edpate unas botas para evitar el da\u00f1o al andar en ellos.");
        add("jei.info.kingdomkeys.blast_blox", "Similar a la TNT pero con un mayor poder destructivo, se activa al contacto de cualquier cosa excepto una pluma en tu mano.");
        add("jei.info.kingdomkeys.bounce_blox", "Entidades que pisen este bloque rebotar\u00e1n, ag\u00e1chate para caer en el bloque sin rebotar.");
        add("jei.info.kingdomkeys.magnet_blox", "Atrae o empuja entidades en la direcci\u00f3n en la que mira. Aplica una se\u00f1al de Redstone para activar, click derecho para cambiar el rango y ag\u00e1chate y click derecho para cambiar entre los modos atracci\u00f3n y repeler.");
        add("jei.info.kingdomkeys.spell_orb", "Util\u00edzalo para desbloquear la magia correspondiente. Una vez desbloqueada puede ser utilizada desde el men\u00fa de comandos siempre y cuando tengas suficientes PM, hasta el nivel 5 tendr\u00e1s 0 PM. Se obtiene como recompensa de los Bloques de Premio.");
        add("jei.info.kingdomkeys.valor_orb", "Utilizado para desbloquear la Forma Valiente. Dicha forma dispone de un hueco para una segunda Llave Espada. Activarla requiere 3 Barras de Fusi\u00f3n. Obtenido como recompensa de los Bloques de Premio Raro.");
        add("jei.info.kingdomkeys.wisdom_orb", "Utilizado para desbloquear la Forma Sabia. Activarla requiere 3 Barras de Fusi\u00f3n. Obtenido como recompensa de los Bloques de Premio Raro.");
        add("jei.info.kingdomkeys.limit_orb", "Utilizado para desbloquear la Forma Suma. Activarla requiere 4 Barras de Fusi\u00f3n. Obtenido como recompensa de los Bloques de Premio Raro.");
        add("jei.info.kingdomkeys.master_orb", "Utilizado para desbloquear la Forma Maestra. Dicha forma dispone de un hueco para una segunda Llave Espada. Activarla requiere 4 Barras de Fusi\u00f3n. Obtenido como recompensa de los Bloques de Premio Raro.");
        add("jei.info.kingdomkeys.final_orb", "Utilizado para desbloquear la Forma Final. Dicha forma dispone de un hueco para una segunda Llave Espada. Activarla requiere 5 Barras de Fusi\u00f3n. Obtenido como recompensa de los Bloques de Premio Raro.");

    }
}