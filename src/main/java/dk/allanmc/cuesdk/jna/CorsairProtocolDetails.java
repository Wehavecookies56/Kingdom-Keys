package dk.allanmc.cuesdk.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CorsairProtocolDetails extends Structure {
	public Pointer sdkVersion;
	public Pointer serverVersion;
	public int sdkProtocolVersion;
	public int serverProtocolVersion;
	public byte breakingChanges;

	public CorsairProtocolDetails() {
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "sdkVersion", "serverVersion", "sdkProtocolVersion",
				"serverProtocolVersion", "breakingChanges" });
	}

	public CorsairProtocolDetails(Pointer sdkVersion, Pointer serverVersion, int sdkProtocolVersion,
			int serverProtocolVersion, byte breakingChanges) {
		this.sdkVersion = sdkVersion;
		this.serverVersion = serverVersion;
		this.sdkProtocolVersion = sdkProtocolVersion;
		this.serverProtocolVersion = serverProtocolVersion;
		this.breakingChanges = breakingChanges;
	}

	public CorsairProtocolDetails(Pointer peer) {
		super(peer);
	}

	public static class ByValue extends CorsairProtocolDetails implements Structure.ByValue {
	}

	public static class ByReference extends CorsairProtocolDetails implements Structure.ByReference {
	}
}