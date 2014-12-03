package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.Random;

import vn.edu.hust.student.dynamicpool.utils.AppConst;
import flexjson.JSON;

public class CycleTrajectory extends Trajectory {

	private float centerX;
	private float centerY;

	private float a = 80;
	private float d = 0.2f;
	
	public CycleTrajectory() {
		super();
	}

	public CycleTrajectory(Point location) {
		super();
		this.centerX = location.getX();
		int randomInt = Math.abs(new Random().nextInt() % AppConst.height);
		this.centerY = location.getY() + randomInt;
		increaseTimeState(-Math.PI / 2);
		a = Math.abs(this.centerY - location.getY());
	}
	
	@flexjson.JSON(include=false)
	public void init(float centerX, float centerY, float a, float d) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.a = a;
		this.d = d;
	}

	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}

	@Override
	@JSON(include=false)
	public ETrajectoryType getTrajectoryType() {
		return ETrajectoryType.CYCLE;
	}

	@Override
	public Point updateLocation(Point location, float deltaTime) {
		increaseTimeState(deltaTime * d);
		float x = (float) (centerX + a * Math.cos(getTimeState()));
		float y = (float) (centerY + a * Math.sin(getTimeState()));
		location.setLocation(x, y);
		return location;
	}
	
	@Override
	public void passingUpdate(Point location, float dx, float dy) {
		centerX += dx;
		centerY += dy;
	}

	@Override
	public void changeDirection(Point location, EDirection direction) {
		switch (direction) {
		case LEFT:
		case RIGHT:
			centerY = 2 * location.getY() - centerY;
			setTimeState(-getTimeState());
			break;
		case TOP:
		case BOTTOM:
			centerX = 2 * location.getX() - centerX;
			setTimeState(Math.PI - getTimeState());
			break;
		default:
			break;
		}
	}

	@JSON(include=false)
	public EDirection getHorizontalDirection() {
		return Math.sin(getTimeState()) > 0 ? EDirection.LEFT
				: EDirection.RIGHT;
	}
	
	@Override
	public void increaseLocation(Point locationBefore, float dx, float dy) {
		centerX += dx;
		centerY += dy;
	}

	public Trajectory clone() {
		CycleTrajectory t = new CycleTrajectory();
		t.init(centerX, centerY, a, d);
		t.setTimeState(this.getTimeState());
		return t;
	}
	
	public boolean equals(CycleTrajectory t) {
		return this.centerX == t.centerX && this.centerY == t.centerY && this.a == t.a && this.d == d && super.equals(t);
	}
}
