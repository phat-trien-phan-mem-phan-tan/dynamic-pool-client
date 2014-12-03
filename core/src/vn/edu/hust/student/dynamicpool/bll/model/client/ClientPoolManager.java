package vn.edu.hust.student.dynamicpool.bll.model.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hust.student.dynamicpool.bll.model.Boundary;
import vn.edu.hust.student.dynamicpool.bll.model.EDirection;
import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.bll.model.FishState;
import vn.edu.hust.student.dynamicpool.bll.model.Pool;
import vn.edu.hust.student.dynamicpool.bll.model.PoolManager;
import vn.edu.hust.student.dynamicpool.bll.model.Segment;
import vn.edu.hust.student.dynamicpool.utils.AppConst;

public class ClientPoolManager implements PoolManager {
	private Logger logger = LoggerFactory.getLogger(ClientPoolManager.class);
	private Pool clientPool = new Pool();
	private List<Fish> fishes = new ArrayList<Fish>();

	public ClientPoolManager() {

	}

	@Override
	public void updateLocationOfFishes(float deltaTime) {
		detectCollision();
		for (Fish fish : fishes) {
			fish.updateLocation(deltaTime);
		}
	}

	List<Fish> removeFishes = new ArrayList<Fish>();

	private void detectCollision() {
		for (Fish fish : fishes) {
			Boundary poolBoundary = clientPool.getBoundary();
			Boundary fishBoundary = fish.getBoundary();
			switch (fish.getFishState()) {
			case INSIDE:
				detectCollisionForInsideFish(fish);
				break;
			case PASSING:
				if (fishBoundary.isOutside(poolBoundary)) {
					removeFishes.add(fish);
				}
				break;
			case OUTSIDE:
				// if (!fish.getBoundary().isOutside(clientPool.getBoundary()))
				// {
				// fish.setFishState(FishState.RETURN);
				// }
				// break;
			case RETURN:
			default:
				if (fishBoundary.isInside(poolBoundary)) {
					logger.debug("new fish {} come inside client pool",
							fish.getFishId());
					fish.setFishState(FishState.INSIDE);
					fish.setPassingDirection(EDirection.UNKNOWN);
				}
				break;
			}
		}
		fishes.removeAll(removeFishes);
	}

	private void detectCollisionForInsideFish(Fish fish) {
		Boundary fishBoundary = fish.getBoundary();
		Boundary poolBoundary = clientPool.getBoundary();
		if (fishBoundary.getMinX() <= poolBoundary.getMinX()) {
			hitLeft(fish, fishBoundary);
		} else if (fishBoundary.getMaxX() >= poolBoundary.getMaxX()) {
			hitRight(fish, fishBoundary);
		} else if (fishBoundary.getMinY() <= poolBoundary.getMinY()) {
			hitBottom(fish, fishBoundary);
		} else if (fishBoundary.getMaxY() >= poolBoundary.getMaxY()) {
			hitTop(fish, fishBoundary);
		}
	}

	private void hitLeft(Fish fish, Boundary fishBoundary) {
		Segment segment = clientPool.detectCollisionLeftSegments(fishBoundary);
		if (segment == null) {
			fish.changeDirection(EDirection.LEFT);
			fish.setFishState(FishState.RETURN);
			fish.setPassingDirection(EDirection.RIGHT);
		} else {
			fish.setFishState(FishState.PASSING);
			fish.setPassingDirection(EDirection.LEFT);
		}
	}

	private void hitRight(Fish fish, Boundary fishBoundary) {
		Segment segment = clientPool.detectCollisionRightSegments(fish
				.getBoundary());
		if (segment == null) {
			fish.changeDirection(EDirection.RIGHT);
			fish.setFishState(FishState.RETURN);
			fish.setPassingDirection(EDirection.LEFT);
		} else {
			fish.setFishState(FishState.PASSING);
			fish.setPassingDirection(EDirection.RIGHT);
		}
	}

	private void hitBottom(Fish fish, Boundary fishBoundary) {
		Segment segment = clientPool.detectCollisionBottomSegments(fish
				.getBoundary());
		if (segment == null) {
			fish.changeDirection(EDirection.BOTTOM);
			fish.setFishState(FishState.RETURN);
			fish.setPassingDirection(EDirection.TOP);
		} else {
			fish.setFishState(FishState.PASSING);
			fish.setPassingDirection(EDirection.BOTTOM);
		}
	}

	private void hitTop(Fish fish, Boundary fishBoundary) {
		Segment segment = clientPool.detectCollisionTopSegments(fishBoundary);
		if (segment == null) {
			fish.changeDirection(EDirection.TOP);
			fish.setFishState(FishState.RETURN);
			fish.setPassingDirection(EDirection.BOTTOM);
		} else {
			fish.setFishState(FishState.PASSING);
			fish.setPassingDirection(EDirection.TOP);
		}
	}

	public void updateSetting(Pool clientPoolSetting) {
		clientPool.getDeviceInfo().setClientName(
				clientPoolSetting.getDeviceInfo().getClientName());
		float width = clientPoolSetting.getBoundary().getWidth();
		float height = clientPoolSetting.getBoundary().getHeight();
		clientPool.getBoundary().setWidth(width);
		clientPool.getBoundary().setHeight(height);
		AppConst.VIEWPORT_WIDTH = width;
		AppConst.VIEWPORT_HEIGHT = height;
		updateSegments(clientPoolSetting.getSegments());
		clientPool.setScale(clientPoolSetting.getScale());
	}

	private void updateSegments(List<Segment> clientSegments) {
		List<Segment> segments = clientPool.getSegments();
		segments.clear();
		for (Segment clientSegment : clientSegments) {
			Segment segment = new Segment(clientSegment.getSegmentDirection(),
					clientSegment.getBeginPoint(), clientSegment.getEndPoint());
			segments.add(segment);
		}
	}

	public List<Fish> getFishes() {
		return fishes;
	}

	public void addFish(Fish fish) {
		fish.setPool(clientPool);
		fishes.add(fish);
	}

	public void removeFish(String fishId) {

	}
}
