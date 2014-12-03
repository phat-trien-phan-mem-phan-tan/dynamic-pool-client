package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.UUID;

public class DeviceInfo {
	private String clientName = UUID.randomUUID().toString();
	private float screenWidth;
	private float screenHeight;
	private float screenSize;

	public DeviceInfo() {

	}

	public DeviceInfo(float screenWidth, float screenHeight, float screenSize) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenSize = screenSize;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public float getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(float screenWidth) {
		this.screenWidth = screenWidth;
	}

	public float getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(float screenHeight) {
		this.screenHeight = screenHeight;
	}

	public float getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(float screenSize) {
		this.screenSize = screenSize;
	}

	public boolean equals(DeviceInfo d) {
		return this.screenWidth == d.screenWidth
				&& this.screenHeight == d.screenHeight
				&& this.screenSize == this.screenSize
				&& ((this.clientName == null && d.clientName == null) || (this.clientName != null
						&& d.clientName != null && this.clientName
							.equals(d.clientName)));
	}
}
