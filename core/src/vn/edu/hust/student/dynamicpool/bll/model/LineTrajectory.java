package vn.edu.hust.student.dynamicpool.bll.model;

import flexjson.JSON;

public class LineTrajectory extends Trajectory {
	private float dx = 1, dy = 1;
	private static final int A = 20;

	public LineTrajectory() {
		super();
	}

	@flexjson.JSON(include = false)
	public void init(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	@Override
	public void changeDirection(Point location, EDirection hitTo) {
		switch (hitTo) {
		case TOP:
		case BOTTOM:
			dy = -dy;
			break;
		case LEFT:
		case RIGHT:
			dx = -dx;
			break;
		default:
			break;
		}
	}

	@Override
	@JSON(include=false)
	public ETrajectoryType getTrajectoryType() {
		return ETrajectoryType.LINE;
	}

	@Override
	public Point updateLocation(Point location, float deltaTime) {
		increaseTimeState(deltaTime);
		float x = (float) (location.getX() + A * dx * deltaTime);
		float y = (float) (location.getY() + A * dy * deltaTime);
		location.setLocation(x, y);
		return location;
	}

	@Override
	@JSON(include=false)
	public EDirection getHorizontalDirection() {
		return dx < 0 ? EDirection.LEFT : EDirection.RIGHT;
	}

	@Override
	public Trajectory clone() {
		LineTrajectory t = new LineTrajectory();
		t.init(dx, dy);
		t.setTimeState(getTimeState());
		return t;
	}
	
	public boolean equals(LineTrajectory line) {
		return this.dx == line.dx && this.dy == line.dy && super.equals(line); 
	}
}
