package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSON;


public class Pool {
	private DeviceInfo deviceInfo = new DeviceInfo();
	public Boundary boundary = new Boundary();
	private List<Segment> segments = new ArrayList<Segment>();
	private float scale = 1;
	@JSON(include=false)
	private Logger logger = LoggerFactory.getLogger(Pool.class);

	public Pool() {

	}

	public Pool(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
		this.boundary.setWidth(deviceInfo.getScreenWidth());
		this.boundary.setHeight(deviceInfo.getScreenHeight());
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary rectangle) {
		Boundary oldRectangle = this.boundary;
		oldRectangle.setHeight(rectangle.getHeight());
		oldRectangle.setWidth(rectangle.getWidth());
		oldRectangle.setLocation(rectangle.getLocation());
	}

	public List<Segment> getSegments() {
		if (segments == null)
			segments = new ArrayList<>();
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Segment detectCollisionLeftSegments(Boundary fishBoundary) {
		for (Segment segment : segments) {
			if (segment.getSegmentDirection() == EDirection.LEFT
					&& segment.getBeginPoint() <= fishBoundary.getMinY()
					&& fishBoundary.getMaxY() <= segment.getEndPoint()) {
				return segment;
			}
		}
		return null;
	}

	public Segment detectCollisionRightSegments(Boundary fishBoundary) {
		for (Segment segment : segments) {
			if (segment.getSegmentDirection() == EDirection.RIGHT
					&& segment.getBeginPoint() <= fishBoundary.getMinY()
					&& fishBoundary.getMaxY() <= segment.getEndPoint()) {
				return segment;
			}
		}
		return null;
	}

	public Segment detectCollisionTopSegments(Boundary fishBoundary) {
		for (Segment segment : segments) {
			if (segment.getSegmentDirection() == EDirection.TOP
					&& segment.getBeginPoint() <= fishBoundary.getMinX()
					&& fishBoundary.getMaxX() <= segment.getEndPoint()) {
				return segment;
			}
		}
		return null;
	}

	public Segment detectCollisionBottomSegments(Boundary fishBoundary) {
		for (Segment segment : segments) {
			if (segment.getSegmentDirection() == EDirection.BOTTOM
					&& segment.getBeginPoint() <= fishBoundary.getMinX()
					&& fishBoundary.getMaxX() <= segment.getEndPoint()) {
				return segment;
			}
		}
		return null;
	}

	
	
	@Override
	public Pool clone() {
		Pool clone = new Pool();
		clone.setBoundary(this.boundary);
		clone.setDeviceInfo(this.deviceInfo);
		clone.setScale(this.scale);
		return clone;
	}
}