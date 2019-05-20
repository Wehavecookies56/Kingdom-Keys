package dk.allanmc.cuesdk.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CorsairLedColor extends Structure {
	public int ledId;
	public int r;
	public int g;
	public int b;

	public CorsairLedColor() {
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "ledId", "r", "g", "b" });
	}

	public CorsairLedColor(int ledId, int r, int g, int b) {
		this.ledId = ledId;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public CorsairLedColor(Pointer peer) {
		super(peer);
	}

	public static class ByValue extends CorsairLedColor implements Structure.ByValue {
	}

	public static class ByReference extends CorsairLedColor implements Structure.ByReference {
	}
}