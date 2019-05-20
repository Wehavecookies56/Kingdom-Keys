package dk.allanmc.cuesdk;

import dk.allanmc.cuesdk.enums.LedId;
import dk.allanmc.cuesdk.jna.CorsairLedPosition;

public class LedPosition {
	private LedId ledId;
	private double top;
	private double left;
	private double height;
	private double width;

	public LedPosition(CorsairLedPosition ledPosition) {
		this.ledId = LedId.byOrdinal(ledPosition.ledId);
		this.top = ledPosition.top;
		this.left = ledPosition.left;
		this.height = ledPosition.height;
		this.width = ledPosition.width;
	}

	public LedId getLedId() {
		return this.ledId;
	}

	public void setLedId(LedId ledId) {
		this.ledId = ledId;
	}

	public double getTop() {
		return this.top;
	}

	public void setTop(double top) {
		this.top = top;
	}

	public double getLeft() {
		return this.left;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return this.width;
	}

	public void setWidth(double width) {
		this.width = width;
	}
}