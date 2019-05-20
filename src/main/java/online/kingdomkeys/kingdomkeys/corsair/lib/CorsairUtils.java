package online.kingdomkeys.kingdomkeys.corsair.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CorsairUtils {
	//Colors for logo
	public static Set<KeyEnum> orange = new HashSet<KeyEnum>(Arrays.asList(KeyEnum.KEY_ESC, KeyEnum.KEY_LEFT_CTRL));
	public static Set<KeyEnum> brown = new HashSet<KeyEnum>(Arrays.asList(KeyEnum.KEY_Q, KeyEnum.KEY_W, KeyEnum.KEY_A, KeyEnum.KEY_S));
	public static Set<KeyEnum> gray = new HashSet<KeyEnum>(Arrays.asList(KeyEnum.KEY_0,KeyEnum.KEY_EQUALS,
			KeyEnum.KEY_R, KeyEnum.KEY_T, KeyEnum.KEY_Y, KeyEnum.KEY_U, KeyEnum.KEY_I,KeyEnum.KEY_O,KeyEnum.KEY_P,KeyEnum.KEY_BRACKET_LEFT,KeyEnum.KEY_RIGHT_BRACKET,
			KeyEnum.KEY_F,KeyEnum.KEY_G,KeyEnum.KEY_H,KeyEnum.KEY_J,KeyEnum.KEY_K,KeyEnum.KEY_L,KeyEnum.KEY_SEMICOLON, KeyEnum.KEY_DOUBLE_QUOTE, KeyEnum.KEY_OTHER_TILDE, KeyEnum.KEY_ENTER));
	public static Set<KeyEnum> blue = new HashSet<KeyEnum>(Arrays.asList(KeyEnum.KEY_E, KeyEnum.KEY_D));
	public static Set<KeyEnum> yellow = new HashSet<KeyEnum>(Arrays.asList(KeyEnum.KEY_F1, KeyEnum.KEY_F2,KeyEnum.KEY_TILDE,KeyEnum.KEY_TAB,KeyEnum.KEY_LOCK_CAPS,KeyEnum.KEY_LEFT_SHIFT, KeyEnum.KEY_LEFT_SUPER, KeyEnum.KEY_LEFT_ALT, KeyEnum.KEY_X, KeyEnum.KEY_3));
	
	public static int[] defaultRGB = { 255, 255, 255 };

	public static boolean isEqualsArray(int[] a1, int[] a2) {
		if(a1[0] == a2[0] && a1[1] == a2[1] && a1[2] == a2[2]) 
			return true;
		return false;
	}
}
