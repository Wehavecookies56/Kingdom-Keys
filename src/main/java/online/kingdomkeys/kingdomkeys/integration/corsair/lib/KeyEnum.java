 package online.kingdomkeys.kingdomkeys.corsair.lib;
 
 public enum KeyEnum {
   KEY_INVALD("INVALID"),
   KEY_ESC("Esc"), 
   KEY_F1("F1"), 
   KEY_F2("F2"), 
   KEY_F3("F3"), 
   KEY_F4("F4"), 
   KEY_F5("F5"), 
   KEY_F6("F6"), 
   KEY_F7("F7"), 
   KEY_F8("F8"), 
   KEY_F9("F9"), 
   KEY_F10("F10"), 
   KEY_F11("F11"), 
   KEY_TILDE("`"), 
   KEY_1("1"), 
   KEY_2("2"), 
   KEY_3("3"), 
   KEY_4("4"), 
   KEY_5("5"), 
   KEY_6("6"), 
   KEY_7("7"), 
   KEY_8("8"), 
   KEY_9("9"), 
   KEY_0("0"), 
   KEY_MINUS("-"), 
   KEY_TAB("Tab"), 
   KEY_Q("Q"), 
   KEY_W("W"), 
   KEY_E("E"), 
   KEY_R("R"), 
   KEY_T("T"), 
   KEY_Y("Y"), 
   KEY_U("U"), 
   KEY_I("I"), 
   KEY_O("O"), 
   KEY_P("P"), 
   KEY_BRACKET_LEFT("["), 
   KEY_LOCK_CAPS("Caps"), 
   KEY_A("A"), 
   KEY_S("S"), 
   KEY_D("D"), 
   KEY_F("F"), 
   KEY_G("G"), 
   KEY_H("H"), 
   KEY_J("J"), 
   KEY_K("K"), 
   KEY_L("L"), 
   KEY_SEMICOLON(";"), //eñe
   KEY_DOUBLE_QUOTE("\""), // Tilde cerrada
   KEY_LEFT_SHIFT("Shift"), 
   KEY_OTHER_BACKSLASH("\\"), 
   KEY_Z("Z"), 
   KEY_X("X"), 
   KEY_C("C"), 
   KEY_V("V"), 
   KEY_B("B"), 
   KEY_N("N"), 
   KEY_M("M"), 
   KEY_COMMA(","), 
   KEY_PERIOD("."), 
   KEY_FORWARD_SLASH("/"), 
   KEY_LEFT_CTRL("Ctrl"), 
   KEY_LEFT_SUPER("Sup"), 
   KEY_LEFT_ALT("Alt"), 
   KEY_LANG_2("L2"), 
   KEY_SPACE("Space"),
   KEY_LANG_1("L1"), 
   KEY_INTERNAT_2("I2"), 
   KEY_RIGHT_ALT("Alt"), 
   KEY_RIGHT_SUPER("Sup"), 
   KEY_APPLICATION("App"), 
   KEY_LED_PROGRAMMING("LED"), 
   KEY_BRIGHTNESS("Brt"), 
   KEY_F12("F12"), 
   KEY_PRINT_SCREEN("Prt"), 
   KEY_SCROLL_LOCK("Scr"), 
   KEY_PAUSE("Pause"), 
   KEY_INSERT("Ins"), 
   KEY_HOME("Hme"), 
   KEY_PAGE_UP("PU"), 
   KEY_RIGHT_BRACKET("]"), 
   KEY_BACKSLASH("\\"), 
   KEY_OTHER_TILDE("~"), // c ç
   KEY_ENTER("Enter"), 
   KEY_INTERNATIONALL("Int"), 
   KEY_EQUALS("="),  // apertura ?!
   KEY_INTERNATIONAL3("I3"), 
   KEY_BACKSPACE("BS"), 
   KEY_DELETE("Del"), 
   KEY_END("End"), 
   KEY_PAGE_DOWN("PD"), 
   KEY_RIGHT_SHIFT("Shift"), 
   KEY_RIGHT_CTRL("Ctrl"), 
   KEY_ARROW_UP("Up"),
   KEY_ARROW_LEFT("Left"),
   KEY_ARROW_DOWN("Down"),
   KEY_ARROW_RIGHT("Right"),
   KEY_WIN_LOCK("WL"), 
   KEY_MUTE("Mute"),
   KEY_STOP("Stop"),
   KEY_PREVIOUS("<"), 
   KEY_PLAY("Play"),
   KEY_NEXT("Next"),
   KEY_NUM_LOCK("NL"), 
   KEY_NUMPAD_SLASH("/"), 
   KEY_NUMPAD_ASTERISK("*"), 
   KEY_NUMPAD_MINUS("-"), 
   KEY_NUMPAD_PLUS("+"), 
   KEY_NUMPAD_ENTER("Enter"),
   KEY_NUMPAD_7("7"), 
   KEY_NUMPAD_8("8"), 
   KEY_NUMPAD_9("9"), 
   KEY_NUMPAD_COMMA(","), 
   KEY_NUMPAD_4("4"), 
   KEY_NUMPAD_5("5"), 
   KEY_NUMPAD_6("6"), 
   KEY_NUMPAD_1("1"), 
   KEY_NUMPAD_2("2"), 
   KEY_NUMPAD_3("3"), 
   KEY_NUMPAD_0("0"), 
   KEY_NUMPAD_PERIOD("."), 
   KEY_G1("G1"), 
   KEY_G2("G2"), 
   KEY_G3("G3"), 
   KEY_G4("G4"), 
   KEY_G5("G5"), 
   KEY_G6("G6"), 
   KEY_G7("G7"), 
   KEY_G8("G8"), 
   KEY_G9("G9"), 
   KEY_G10("G10"), 
   KEY_VolumeUp("VolumeUp"),
   KEY_VolumeDown("VolumeDown"),
   KEY_MR("MR"), 
   KEY_M1("M1"), 
   KEY_M2("M2"), 
   KEY_M3("M3"), 
   KEY_G11("G11"), 
   KEY_G12("G12"), 
   KEY_G13("G13"), 
   KEY_G14("G14"), 
   KEY_G15("G15"), 
   KEY_G16("G16"), 
   KEY_G17("G17"), 
   KEY_G18("G18"), 
   KEY_INTERNATIONAL_5("I5"), 
   KEY_INTERNATIONAL_4("I4"), 
   KEY_FN("Fn"), 
   CLM_1("1"), 
   CLM_2("2"), 
   CLM_3("3"), 
   CLM_4("4"), 
   CLH_LEFT_LOGO("LL"), 
   CLH_RIGHT_LOGO("RL"), 
   KEY_LOGO("Logo");
   
	private final String glyph;
	private int[] lastColor = new int[] { -1, -1, -1 };
	private boolean shouldUpdate = false;

	private KeyEnum(String glyph) {
		this.glyph = glyph;
	}

	public String getGlyph() {
		return this.glyph;
	}

	public int[] getLastColor() {
		return this.lastColor;
	}

	public void setLastColor(int[] lastColor) {
		this.lastColor = lastColor;
	}
	
	public boolean getShouldUpdate() {
		return shouldUpdate;
	}
	
	public void setShouldUpdate(boolean shouldUpdate) {
		this.shouldUpdate = shouldUpdate;
	}

}