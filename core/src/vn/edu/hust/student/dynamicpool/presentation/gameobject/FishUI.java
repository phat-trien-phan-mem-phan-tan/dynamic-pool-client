package vn.edu.hust.student.dynamicpool.presentation.gameobject;

import vn.edu.hust.student.dynamicpool.bll.model.EDirection;
import vn.edu.hust.student.dynamicpool.bll.model.Fish;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class FishUI {
	private Fish fishBLL;
	private float time;

	public FishUI(Fish fish) {
		this.fishBLL = fish;
	}

	public float getX() {
		return fishBLL.getBoundary().getMinX();
	}

	public float getY() {
		return fishBLL.getBoundary().getMinY();
	}

	public EDirection getDirection() {
		return fishBLL.getTrajectory().getHorizontalDirection();
	}
	
	public float getScale() {
		return 1f;
	}
	public float getTime() {
		return time;
	}

	public abstract void initFishAsset();
	public abstract void render(SpriteBatch batch);

	public void update(float deltaTime) {
		time += deltaTime;
		fishBLL.updateLocation(deltaTime);
	}
}
