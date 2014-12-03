package vn.edu.hust.student.dynamicpool.bll.model;


public class TrajectoryFactory {
	public static LineTrajectory createLineTrajectory(Point location) {
		return new LineTrajectory();
	}
	
	public static SinTrajectory createSinTrajectory(Point location) {
		return new SinTrajectory(location);
	}
	
	public static CycleTrajectory createCycleTrajectory(Point location) {
		return new CycleTrajectory(location);
	}
}
