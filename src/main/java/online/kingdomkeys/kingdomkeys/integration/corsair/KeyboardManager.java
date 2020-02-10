package online.kingdomkeys.kingdomkeys.corsair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dk.allanmc.cuesdk.CueSDK;
import dk.allanmc.cuesdk.DeviceInfo;
import dk.allanmc.cuesdk.enums.DeviceType;
import dk.allanmc.cuesdk.jna.CorsairLedColor;
import dk.allanmc.cuesdk.jna.CorsairLedPosition;
import dk.allanmc.cuesdk.jna.CorsairLedPositions;
import dk.allanmc.cuesdk.jna.CueSDKLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import online.kingdomkeys.kingdomkeys.corsair.functions.KeyFunction;
import online.kingdomkeys.kingdomkeys.corsair.lib.CorsairUtils;
import online.kingdomkeys.kingdomkeys.corsair.lib.KeyEnum;

public class KeyboardManager {
	private final Thread updateThread = new Thread(this::updateKeys);
	private CueSDK cue = new CueSDK(false);
	private CueSDKLibrary cueSDK;
	public static boolean corsairKeyboard = false;

	public KeyboardManager() {
		for (int i = 0; i < cue.getDeviceCount(); i++) {
			DeviceInfo info = cue.getDeviceInfo(i);
			System.out.println(info.getType());
			if (info.getType() == DeviceType.CDT_Keyboard) {
				corsairKeyboard = true;
			}
		}

		if (corsairKeyboard) {
			this.updateThread.setDaemon(true);
			this.updateThread.setPriority(3);
			this.updateThread.start();
			try {
				Field cueSDKField = CueSDK.class.getDeclaredField("instance");
				cueSDKField.setAccessible(true);
				this.cueSDK = (CueSDKLibrary) cueSDKField.get(this.cue);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException("Failed to reflect CueSDK", e);
			}
		}
	}

	public void resetKeyboard() {
		if (!corsairKeyboard)
			return;
		
		for (KeyEnum key : getKeyEnums()) {
			key.setShouldUpdate(true);
			CorsairLedColor color = new CorsairLedColor();
			color.ledId = key.ordinal();
			color.r = CorsairUtils.defaultRGB[0];
			color.g = CorsairUtils.defaultRGB[1];
			color.b = CorsairUtils.defaultRGB[2];
			this.cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
		}
	}

	public void setDefaultColor(int[] colors) {
		if (!corsairKeyboard)
			return;
		
		if (!CorsairUtils.isEqualsArray(CorsairUtils.defaultRGB, colors)) {
			// System.out.println("Setting default color");
			CorsairUtils.defaultRGB = colors;
			resetKeyboard();
		}
	}

	/*
	 * public void setDefaultColor(int[] colors, boolean trans) { if
	 * (!CorsairUtils.isEqualsArray(CorsairUtils.defaultRGB, colors)) {
	 * System.out.println("Setting default color"); if (trans) { Thread t = new
	 * Thread(new Runnable() { public void run() { int
	 * r=CorsairUtils.defaultRGB[0],g=CorsairUtils.defaultRGB[1],b=CorsairUtils.
	 * defaultRGB[2]; while (!CorsairUtils.isEqualsArray(CorsairUtils.defaultRGB,
	 * colors)) { if (CorsairUtils.defaultRGB[0] < colors[0]) { r++; } else if
	 * (CorsairUtils.defaultRGB[0] > colors[0]) { r--; }
	 * 
	 * if (CorsairUtils.defaultRGB[1] < colors[1]) { g++; } else if
	 * (CorsairUtils.defaultRGB[1] > colors[1]) { g--; }
	 * 
	 * if (CorsairUtils.defaultRGB[2] < colors[2]) { b++; } else if
	 * (CorsairUtils.defaultRGB[2] > colors[2]) { b--; }
	 * 
	 * CorsairUtils.defaultRGB = new int[] {r,g,b}; resetKeyboard(); } }; });
	 * t.start(); } else { CorsairUtils.defaultRGB = colors; resetKeyboard(); } } }
	 */

	public void showLogo() {
		if (!corsairKeyboard)
			return;

		for (KeyEnum key : getKeyEnums()) {
			CorsairLedColor color = new CorsairLedColor();
			if (CorsairUtils.orange.contains(key)) {
				color.r = 255;
				color.g = 150;
				color.b = 0;
			} else if (CorsairUtils.brown.contains(key)) {
				color.r = 100;
				color.g = 40;
				color.b = 0;
			} else if (CorsairUtils.gray.contains(key)) {
				color.r = 160;
				color.g = 160;
				color.b = 160;
			} else if (CorsairUtils.blue.contains(key)) {
				color.r = 0;
				color.g = 100;
				color.b = 255;
			} else if (CorsairUtils.yellow.contains(key)) {
				color.r = 255;
				color.g = 215;
				color.b = 0;
			}
			color.ledId = key.ordinal();
			this.cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
		}
		/*
		 * CorsairLedColor color = new CorsairLedColor();
		 * 
		 * color.ledId = KeyEnum.KEY_EQUALS.ordinal(); color.r = 255; color.g = 255;
		 * color.b = 255; this.cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
		 */
	}

	int test = 0;
	boolean fadingOut = false;

	void updateKeys() {
		if (!corsairKeyboard)
			return;
		final ClientPlayerEntity player = Minecraft.getInstance().player;

		/*
		 * if (player != null) { if (player.motionY > 0) { lightKeys(new KeyEnum[] {
		 * KeyEnum.KEY_A, KeyEnum.KEY_3 }, 255, 0, 0); } else if(player.motionY < 0){
		 * lightKeys(new KeyEnum[] { KeyEnum.KEY_A, KeyEnum.KEY_3 }, Utils.defaultRGB);
		 * } }
		 */

		/*
		 * lightFunction(KeyFunction.SLOT_DAMAGE_1, KeyEnum.KEY_1);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_2, KeyEnum.KEY_2);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_3, KeyEnum.KEY_3);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_4, KeyEnum.KEY_4);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_5, KeyEnum.KEY_5);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_6, KeyEnum.KEY_6);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_7, KeyEnum.KEY_7);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_8, KeyEnum.KEY_8);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_9, KeyEnum.KEY_9);
		 * lightFunction(KeyFunction.SLOT_DAMAGE_OFF, KeyEnum.KEY_0);
		 */

		/*
		 * lightFunction(KeyFunction.HEALTH_0, KeyEnum.KEY_Q);
		 * lightFunction(KeyFunction.HEALTH_1, KeyEnum.KEY_W);
		 * lightFunction(KeyFunction.HEALTH_2, KeyEnum.KEY_E);
		 * lightFunction(KeyFunction.HEALTH_3, KeyEnum.KEY_R);
		 * lightFunction(KeyFunction.HEALTH_4, KeyEnum.KEY_T);
		 * lightFunction(KeyFunction.HEALTH_5, KeyEnum.KEY_Y);
		 * lightFunction(KeyFunction.HEALTH_6, KeyEnum.KEY_U);
		 * lightFunction(KeyFunction.HEALTH_7, KeyEnum.KEY_I);
		 * lightFunction(KeyFunction.HEALTH_8, KeyEnum.KEY_O);
		 * lightFunction(KeyFunction.HEALTH_9, KeyEnum.KEY_P);
		 * 
		 * lightFunction(KeyFunction.AIR, KeyEnum.KEY_Z);
		 * lightFunction(KeyFunction.FOOD, KeyEnum.KEY_X);
		 */

		// lightFunction(KeyFunction.RESET,KeyEnum.KEY_Z);

		/*
		 * final CorsairLedColor color = new CorsairLedColor(); color.ledId =
		 * KeyEnum.KEY_G.ordinal();
		 * 
		 * int speed = 1; if (fadingOut) { test -= speed; if (test <= 0) { test = 0;
		 * fadingOut = false; } } else { test += speed; if (test >= 255) { test = 255;
		 * fadingOut = true; } } color.r = test; color.g = 0; color.b = 0;
		 * this.cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
		 */
	}

	/**
	 * Light keys from a keyfunction
	 * 
	 * @param keyFunction
	 * @param key
	 */
	private void lightFunction(KeyFunction keyFunction, KeyEnum key) {
		if (!corsairKeyboard)
			return;
		final Minecraft minecraft = Minecraft.getInstance();

		final int[] rgbValue = keyFunction.getCallback().invoke(key, minecraft, minecraft.world, minecraft.player);
		final int[] lastColor = key.getLastColor();
		// System.out.println(key+" "+hsvColorValue);

		if (key.getShouldUpdate() || !CorsairUtils.isEqualsArray(lastColor, rgbValue)) {
			key.setShouldUpdate(false);
			final CorsairLedColor color = new CorsairLedColor();
			key.setLastColor(rgbValue);
			color.ledId = key.ordinal();
			color.r = rgbValue[0];
			color.g = rgbValue[1];
			color.b = rgbValue[2];
			this.cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
		}
	}

	private void lightKey(KeyEnum key, int[] colors) {
		if (!corsairKeyboard)
			return;
		lightKey(key, colors[0], colors[1], colors[2]);
	}

	private void lightKey(KeyEnum key, int r, int g, int b) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				CorsairLedColor color = new CorsairLedColor();
				color.ledId = key.ordinal();
				color.r = r;
				color.g = g;
				color.b = b;
				cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
			};
		});
		t.start();
	}

	private void lightKeys(KeyEnum[] keys, int[] colors) {
		if (!corsairKeyboard)
			return;
		lightKeys(keys, colors[0], colors[1], colors[2]);
	}

	private void lightKeys(KeyEnum[] keys, int r, int g, int b) {
		if (!corsairKeyboard)
			return;
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (KeyEnum key : keys) {
					CorsairLedColor color;
					color = new CorsairLedColor();
					color.ledId = key.ordinal();
					color.r = r;
					color.g = g;
					color.b = b;
					cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
				}
			};
		});
		t.start();
	}

	private void lightKeyAndReturn(KeyEnum key, int r, int g, int b, int time) {
		if (!corsairKeyboard)
			return;
		Thread t = new Thread(new Runnable() {
			public void run() {
				CorsairLedColor color = new CorsairLedColor();
				color.ledId = key.ordinal();
				color.r = r;
				color.g = g;
				color.b = b;
				cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					color = new CorsairLedColor();
					color.ledId = key.ordinal();
					color.r = CorsairUtils.defaultRGB[0];
					color.g = CorsairUtils.defaultRGB[1];
					color.b = CorsairUtils.defaultRGB[2];
					cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
				}
			};
		});
		t.start();
	}

	private void lightKeysAndReturn(KeyEnum[] keys, int r, int g, int b, int time) {
		if (!corsairKeyboard)
			return;
		Thread t = new Thread(new Runnable() {
			public void run() {
				CorsairLedColor color;
				for (KeyEnum key : keys) {
					color = new CorsairLedColor();
					color.ledId = key.ordinal();
					color.r = r;
					color.g = g;
					color.b = b;
					cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
				}

				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					for (KeyEnum key : keys) {
						color = new CorsairLedColor();
						color.ledId = key.ordinal();
						color.r = CorsairUtils.defaultRGB[0];
						color.g = CorsairUtils.defaultRGB[1];
						color.b = CorsairUtils.defaultRGB[2];
						cueSDK.CorsairSetLedsColorsAsync(1, color, null, null);
					}
				}
			};
		});
		t.start();
	}

	/*
	 * private void fadeKey(KeyEnum key, int r, int g, int b) { Thread t = new
	 * Thread(new Runnable() { public void run() { CorsairLedColor color = new
	 * CorsairLedColor(); color.ledId = key.ordinal(); color.r = r; color.g = g;
	 * color.b = b; cueSDK.CorsairSetLedsColorsAsync(1, color, null, null); }; });
	 * t.start(); }
	 */

	List<KeyEnum> getKeyEnums() {
		return this.getKeys().stream().map(key -> KeyEnum.values()[key.id]).collect(Collectors.toList());
	}

	public List<Key> getKeys() {
		CorsairLedPositions keyPositions = this.cueSDK.CorsairGetLedPositions();
		int count = keyPositions.numberOfLed;
		CorsairLedPosition[] positionArray = (CorsairLedPosition[]) keyPositions.pLedPosition.toArray(count);
		ArrayList<Key> keys = new ArrayList<Key>();
		for (CorsairLedPosition position : positionArray) {
			keys.add(new Key(position.ledId, position.top, position.left, position.height, position.width));
		}
		return keys;
	}

	public static class Key {
		private final int id;
		private final double top;
		private final double left;
		private final double height;
		private final double width;
		private final KeyEnum keyEnum;

		Key(int id, double top, double left, double height, double width) {
			this.id = id;
			this.top = top;
			this.left = left;
			this.height = height;
			this.width = width;
			this.keyEnum = KeyEnum.values()[id];
		}

		public int getId() {
			return this.id;
		}

		public double getTop() {
			return this.top;
		}

		public double getLeft() {
			return this.left;
		}

		public double getHeight() {
			return this.height;
		}

		public double getWidth() {
			return this.width;
		}

		public KeyEnum getKeyEnum() {
			return this.keyEnum;
		}
	}

}