package dk.allanmc.cuesdk.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CorsairLedPositions extends Structure {
	public int numberOfLed;
	public CorsairLedPosition.ByReference pLedPosition;

	public CorsairLedPositions() {
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "numberOfLed", "pLedPosition" });
	}

	public CorsairLedPositions(int numberOfLed, CorsairLedPosition.ByReference pLedPosition) {
		this.numberOfLed = numberOfLed;
		this.pLedPosition = pLedPosition;
	}

	public CorsairLedPositions(Pointer peer) {
		super(peer);
	}

	public static class ByValue extends CorsairLedPositions implements Structure.ByValue {
	}

	public static class ByReference extends CorsairLedPositions implements Structure.ByReference {
	}
}
