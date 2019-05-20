package dk.allanmc.cuesdk;

import dk.allanmc.cuesdk.enums.DeviceCaps;
import dk.allanmc.cuesdk.enums.DeviceType;
import dk.allanmc.cuesdk.enums.LogicalLayout;
import dk.allanmc.cuesdk.enums.PhysicalLayout;
import dk.allanmc.cuesdk.jna.CorsairDeviceInfo;

public class DeviceInfo {
	private DeviceType type;
	private String model;
	private PhysicalLayout physicalLayout;
	private LogicalLayout logicalLayout;
	private int capsMask;

	public DeviceInfo(CorsairDeviceInfo deviceInfo) {
		this.type = DeviceType.byOrdinal(deviceInfo.type);
		this.model = deviceInfo.model.getString(0L);
		this.physicalLayout = PhysicalLayout.byOrdinal(deviceInfo.physicalLayout);
		this.logicalLayout = LogicalLayout.byOrdinal(deviceInfo.logicalLayout);
	}

	public DeviceType getType() {
		return this.type;
	}

	public String getModel() {
		return this.model;
	}

	public PhysicalLayout getPhysicalLayout() {
		return this.physicalLayout;
	}

	public LogicalLayout getLogicalLayout() {
		return this.logicalLayout;
	}

	public int getCapsMask() {
		return this.capsMask;
	}

	public boolean hasCapability(DeviceCaps cap) {
		return (this.capsMask | cap.ordinal()) == cap.ordinal();
	}
}