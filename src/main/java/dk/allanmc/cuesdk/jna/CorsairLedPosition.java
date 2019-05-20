package dk.allanmc.cuesdk.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CorsairLedPosition extends Structure {
	public int ledId;
	public double top;
	public double left;
	public double height;
	public double width;

	public CorsairLedPosition() {
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "ledId", "top", "left", "height", "width" });
	}

	public CorsairLedPosition(int ledId, double top, double left, double height, double width) {
		this.ledId = ledId;
		this.top = top;
		this.left = left;
		this.height = height;
		this.width = width;
	}

	public CorsairLedPosition(Pointer peer) {
		super(peer);
	}

	public static class ByValue extends CorsairLedPosition implements Structure.ByValue {
	}

	public static class ByReference extends CorsairLedPosition implements Structure.ByReference {
	}
}