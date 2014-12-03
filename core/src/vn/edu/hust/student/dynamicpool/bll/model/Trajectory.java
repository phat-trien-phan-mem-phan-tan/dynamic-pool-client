package vn.edu.hust.student.dynamicpool.bll.model;

import flexjson.JSON;

public abstract class Trajectory {
	private double timeState = 0f;

	public Trajectory() {

	}

	public double getTimeState() {
		return timeState;
	}

	@JSON(include=false)
	public void increaseTimeState(double d) {
		this.timeState += d;
	}

	public void setTimeState(double timeState) {
		this.timeState = timeState;
	}
	
	@JSON(include=false)
	public void passingUpdate(Point location, float dx, float dy) {
		
	}
	
	@JSON(include=false)
	public boolean equals(Trajectory t) {
		return this.timeState == t.timeState;
	}

	@JSON(include=false)
	public abstract void changeDirection(Point location, EDirection hitTo);

	@JSON(include=false)
	public abstract Point updateLocation(Point location, float deltaTime);

	@JSON(include=false)
	public abstract ETrajectoryType getTrajectoryType();

	@JSON(include=false)
	public abstract EDirection getHorizontalDirection();

	@JSON(include=false)
	public abstract Trajectory clone();

	@JSON(include=false)
	public void increaseLocation(Point locationBefore, float dx, float dy) {
		
	}
}
