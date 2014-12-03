package vn.edu.hust.student.dynamicpool.bll.model;

import vn.edu.hust.student.dynamicpool.utils.AppConst;


public class FishFactory {
	private FishFactory() {
		
	}
	public static Fish createFishWithTrajectoryType(final FishType fishType,
			final ETrajectoryType trajectoryType, final int width,
			final int height) {
		switch (trajectoryType) {
		case SIN:
			return createFishWithSinTrajectory(width, height, fishType);
		case CYCLE:
			return createFishWithCycleTrajectory(width, height, fishType);
		case LINE:
		case NONE:
		default:
			return createFishWithLineTrajectory(width, height, fishType);
		}
	}
	public static Fish createFishWithLineTrajectory(float fishWidth,
			float fishHeight, FishType fishType) {
		Point location = new Point(AppConst.width / 2, AppConst.height / 2);
		Boundary fishBoundary = new Boundary(location, fishWidth, fishHeight);
		LineTrajectory lineTrajectory = TrajectoryFactory.createLineTrajectory(location);
		return new Fish(fishType, lineTrajectory, fishBoundary);
	}

	public static Fish createFishWithSinTrajectory(float fishWidth,
			float fishHeight, FishType fishType) {
		Point location = new Point(AppConst.width / 2, AppConst.height / 2);
		Boundary fishBoundary = new Boundary(location, fishWidth, fishHeight);
		SinTrajectory sinTrajectory = TrajectoryFactory.createSinTrajectory(location);
		return new Fish(fishType, sinTrajectory, fishBoundary);
	}

	public static Fish createFishWithCycleTrajectory(float fishWidth,
			float fishHeight, FishType fishType) {
		Point location = new Point(AppConst.width / 2, AppConst.height / 2);
		Boundary fishBoundary = new Boundary(location, fishWidth, fishHeight);
		CycleTrajectory cycleTrajectory = TrajectoryFactory.createCycleTrajectory(location);
		return new Fish(fishType, cycleTrajectory, fishBoundary);
	}
}
