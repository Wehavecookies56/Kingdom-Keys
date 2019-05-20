package dk.allanmc.cuesdk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dk.allanmc.cuesdk.enums.CorsairError;
import dk.allanmc.cuesdk.jna.CorsairLedColor;
import dk.allanmc.cuesdk.jna.CorsairLedPosition;
import dk.allanmc.cuesdk.jna.CorsairLedPositions;
import dk.allanmc.cuesdk.jna.CorsairProtocolDetails;
import dk.allanmc.cuesdk.jna.CueSDKLibrary;

public class CueSDK {
	private final CueSDKLibrary instance;

	public CueSDK() {
		this(false);
	}

	public CueSDK(boolean exclusiveLightingControl) {
		this.instance = CueSDKLibrary.INSTANCE;
		CorsairProtocolDetails.ByValue protocolDetails = this.instance.CorsairPerformProtocolHandshake();

		if (protocolDetails.serverProtocolVersion == 0) {
			handleError();
		}

		if (protocolDetails.breakingChanges != 0) {
			String sdkVersion = protocolDetails.sdkVersion.getString(0L);
			String cueVersion = protocolDetails.serverVersion.getString(0L);
			throw new RuntimeException("Incompatible SDK (" + sdkVersion + ") and CUE " + cueVersion + " versions.");
		}

		if (exclusiveLightingControl) {
			byte ret = this.instance.CorsairRequestControl(0);
			if (ret != 1) {
				handleError();
			}
		}
	}

	public int getDeviceCount() {
		return this.instance.CorsairGetDeviceCount();
	}

	public DeviceInfo getDeviceInfo(int deviceIndex) {
		return new DeviceInfo(this.instance.CorsairGetDeviceInfo(deviceIndex));
	}

	public List<LedPosition> getLedPositions() {
		CorsairLedPositions corsairLedPositions = this.instance.CorsairGetLedPositions();
		ArrayList<LedPosition> ledPositions = new ArrayList();
		int count = corsairLedPositions.numberOfLed;

		if ((corsairLedPositions != null) && (count > 0)) {
			CorsairLedPosition.ByReference pLedPosition = corsairLedPositions.pLedPosition;
			CorsairLedPosition[] nativeLedPositions = (CorsairLedPosition[]) pLedPosition.toArray(new CorsairLedPosition[count]);
			ledPositions.ensureCapacity(count);
			for (CorsairLedPosition nativeLedPosition : nativeLedPositions) {
				ledPositions.add(new LedPosition(nativeLedPosition));
			}
		}

		return ledPositions;
	}

	public void setLedsColors(Collection<LedColor> ledColors) {
		if ((ledColors == null) || (ledColors.isEmpty())) {
			return;
		}
		Iterator<LedColor> iterator = ledColors.iterator();
		LedColor ledColor = (LedColor) iterator.next();
		if (ledColors.size() == 1) {
			setLedColor(ledColor);
		} else {
			CorsairLedColor[] nativeLedColors = (CorsairLedColor[]) new CorsairLedColor().toArray(ledColors.size());
			int index = 0;
			copyCorsairLedColor(ledColor, nativeLedColors[(index++)]);
			while (iterator.hasNext()) {
				ledColor = (LedColor) iterator.next();
				copyCorsairLedColor(ledColor, nativeLedColors[(index++)]);
			}
			byte ret = this.instance.CorsairSetLedsColors(nativeLedColors.length, nativeLedColors[0]);
			if (ret != 1) {
				handleError();
			}
		}
	}

	public void setLedColor(LedColor ledColor) {
		if (ledColor == null) {
			return;
		}
		CorsairLedColor nativeLedColor = new CorsairLedColor();
		copyCorsairLedColor(ledColor, nativeLedColor);
		byte ret = this.instance.CorsairSetLedsColors(1, nativeLedColor);
		if (ret != 1) {
			handleError();
		}
	}

	private void copyCorsairLedColor(LedColor src, CorsairLedColor dst) {
		dst.ledId = src.ledId;
		dst.r = src.r;
		dst.g = src.g;
		dst.b = src.b;
	}

	private void handleError() {
		int errorId = this.instance.CorsairGetLastError();
		CorsairError error = CorsairError.byOrdinal(errorId);
		if (error != CorsairError.CE_Success) {
			throw new RuntimeException(error + " - " + error.getMessage());
		}
	}
}
