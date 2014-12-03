package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.UUID;

import flexjson.JSON;

public class Fish {
	private String fishId = UUID.randomUUID().toString();
	private FishType fishType = FishType.FISH1;
	private Trajectory trajectory = new NoneTrajectory();
	private Boundary boundary = new Boundary();
	private FishState fishState = FishState.INSIDE;
	private EDirection passingDirection = EDirection.UNKNOWN;
	private Pool pool = null;
	public static final float dx = 20f;
	public static final float dy = 20f;

	public Fish() {

	}

	public Fish(FishType fishType, Trajectory fishTrajectory,
			Boundary fishBoundary) {
		this.fishType = fishType;
		this.trajectory = fishTrajectory;
		this.boundary = fishBoundary;
	}

	public String getFishId() {
		return fishId;
	}

	public void setFishId(String fishId) {
		this.fishId = fishId;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public Trajectory getTrajectory() {
		if (trajectory == null)
			return new NoneTrajectory();
		return trajectory;
	}

	public void setTrajectory(Trajectory trajectory) {
		this.trajectory = trajectory;
	}

	public FishType getFishType() {
		return fishType;
	}

	public void setFishType(FishType fishType) {
		this.fishType = fishType;
	}

	public FishState getFishState() {
		return fishState;
	}

	public void setFishState(FishState fishState) {
		this.fishState = fishState;
	}

	public EDirection getPassingDirection() {
		return passingDirection;
	}

	public void setPassingDirection(EDirection passingDirection) {
		this.passingDirection = passingDirection;
	}

	@JSON(include = false)
	public Pool getPool() {
		return pool;
	}

	@JSON(include = false)
	public void setPool(Pool pool) {
		this.pool = pool;
	}

	@JSON(include = false)
	public void updateLocation(float deltaTime) {
		Point location = boundary.getLocation();
		float mx = 0f, my = 0f;
		switch (passingDirection) {
		case LEFT:
			mx = -dx * deltaTime;
			location.setLocation(location.getX() + mx, location.getY());
			break;
		case RIGHT:
			mx = dx * deltaTime;
			location.setLocation(location.getX() + mx, location.getY());
			break;
		case TOP:
			my = dy * deltaTime;
			location.setLocation(location.getX(), location.getY() + my);
			break;
		case BOTTOM:
			my = -dy * deltaTime;
			location.setLocation(location.getX(), location.getY() + my);
			break;
		case UNKNOWN:
		default:
			Point updateLocation = getTrajectory().updateLocation(location,
					deltaTime);
			location.setLocation(updateLocation.getX(), updateLocation.getY());
			return;
		}
		trajectory.passingUpdate(location, mx, my);
	}

	@JSON(include = false)
	public Fish clone() {
		FishType fishType = this.fishType;
		Trajectory trajectory = this.getTrajectory().clone();
		Boundary boundary = this.getBoundary().clone();
		Fish fish = new Fish(fishType, trajectory, boundary);
		fish.setFishId(fishId);
		fish.setFishState(fishState);
		fish.setPassingDirection(passingDirection);
		fish.setPool(pool);
		return fish;
	}

	@JSON(include = false)
	public boolean equals(Fish f) {
		return this.fishId.equals(f.getFishId())
				&& this.fishType == f.getFishType()
				&& this.fishState == f.getFishState()
				&& this.passingDirection == f.getPassingDirection()
				&& this.trajectory.equals(f.getTrajectory())
				&& this.boundary.equals(f.getBoundary());
	}

	@JSON(include = false)
	public void increaseLocation(float dx, float dy) {
		this.trajectory.increaseLocation(boundary.getLocation(), dx, dy);
		this.boundary.setLocation(boundary.getMinX() + dx, boundary.getMinY()
				+ dy);
	}

	@JSON(include = false)
	public boolean isInside() {
		if (pool == null)
			return false;
		return getBoundary().isInside(pool.getBoundary());
	}

	@JSON(include = false)
	public void changeDirection(EDirection direction) {
		trajectory.changeDirection(this.boundary.getLocation(), direction);
	}
}
