package dk.allanmc.cuesdk.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CorsairDeviceInfo extends Structure {
	public int type;
	public Pointer model;
	public int physicalLayout;
	public int logicalLayout;
	public int capsMask;

	public CorsairDeviceInfo() {
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "type", "model", "physicalLayout", "logicalLayout", "capsMask" });
	}

	public CorsairDeviceInfo(int type, Pointer model, int physicalLayout, int logicalLayout, int capsMask) {
		this.type = type;
		this.model = model;
		this.physicalLayout = physicalLayout;
		this.logicalLayout = logicalLayout;
		this.capsMask = capsMask;
	}

	public CorsairDeviceInfo(Pointer peer) {
		super(peer);
	}

	public static class ByValue extends CorsairDeviceInfo implements Structure.ByValue {
	}

	public static class ByReference extends CorsairDeviceInfo implements Structure.ByReference {
	}
}