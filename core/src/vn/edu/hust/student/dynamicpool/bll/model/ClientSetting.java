package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.ArrayList;
import java.util.List;

public class ClientSetting {
	private String clientName;
	private float width = 800;
	private float height = 480;
	private float scale = 1f;
	private List<Segment> segments = new ArrayList<Segment>();

	public ClientSetting() {

	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public boolean equals(ClientSetting s) {
		return ((this.clientName == null && s.clientName == null) || (this.clientName != null
				&& s.clientName != null && this.clientName.equals(s.clientName)))
				&& this.width == s.width
				&& this.height == s.height
				&& this.scale == s.scale
						&& checkSegments(s.segments);
	}

	private boolean checkSegments(List<Segment> newSegments) {
		if (this.segments == null || newSegments == null) return false;
		if (this.segments.size() != newSegments.size()) return false;
		for (int i = 0; i < this.segments.size(); i++) {
			if (!this.segments.get(i).equals(newSegments.get(i)));
		}
		return true;
	}
}
