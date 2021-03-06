package vn.edu.hust.student.dynamicpool.presentation.screen;

import java.util.List;

import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.presentation.WorldController;
import vn.edu.hust.student.dynamicpool.presentation.WorldRenderer;
import vn.edu.hust.student.dynamicpool.presentation.assets.AssetGameScreen;
import vn.edu.hust.student.dynamicpool.presentation.assets.Assets;
import vn.edu.hust.student.dynamicpool.presentation.gameobject.FishUI;
import vn.edu.hust.student.dynamicpool.presentation.gameobject.FishUICollection;
import vn.edu.hust.student.dynamicpool.utils.AppConst;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

	protected WorldRenderer worldRenderer = null;
	protected WorldController worldController = null;
	protected SpriteBatch batch = null;
	private FishUICollection fishUICollection = null;
	private Texture exitButtonTexture;
	private Texture addFishButtonTexture;
	private Texture selectFishButtonsTexture;
	private Texture selectTrajectoryButtonTexture;
	protected InputProcessor defaultInputProcessor;
	protected InputProcessor selectFishInputProcessor;
	protected InputProcessor selectTrajectoryInputProcessor;

	public GameScreen(WorldRenderer worldRenderer,
			WorldController worldController) {
		this.worldRenderer = worldRenderer;
		this.worldController = worldController;
		this.batch = worldRenderer.getBatch();
		this.fishUICollection = worldController.getFishUICollection();
	}

	@Override
	public void render(float delta) {
		worldRenderer.beginRender();
		renderFishsUIAndUpdate(delta);
		renderHubControl();
		worldRenderer.endRender();
	}

	protected void renderFishsUIAndUpdate(float deltaTime) {
		List<Fish> fishs = worldController.getFishs();
		for (Fish fish : fishs) {
			renderAFishUI(fish, deltaTime);
		}
		worldController.updateFishsCordinate(deltaTime);
	}

	private void renderAFishUI(Fish fish, float deltaTime) {
		FishUI fishUI = fishUICollection.getFishUI(fish);
		fishUI.render(batch);
		fishUI.update(deltaTime);
	}

	protected void renderHubControl() {
		batch.draw(exitButtonTexture, 0, 0);
		batch.draw(addFishButtonTexture, 64, 0);
		if (worldController.isShowSelectFishButtons())
			batch.draw(selectFishButtonsTexture, 0, AppConst.height - 100, 480,
					100);
		if (worldController.isShowSelectTrajectoryButtons())
			batch.draw(selectTrajectoryButtonTexture, 0,
					AppConst.height - 100,
					selectTrajectoryButtonTexture.getWidth(),
					selectTrajectoryButtonTexture.getHeight());
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		AssetGameScreen gameAssets = Assets.instance.gameScreen;
		this.exitButtonTexture = gameAssets.getExitButtonTexture();
		this.addFishButtonTexture = gameAssets.getAddFishButtonTexture();
		this.selectFishButtonsTexture = gameAssets
				.getSelectFishButtonsTexture();
		this.selectTrajectoryButtonTexture = gameAssets.getSelectTrajectoryButtonTexture();
		createInputprocessors();
		setDefaultInputProcessor();
	}

	protected void createInputprocessors() {
		this.defaultInputProcessor = new GSDefaultInputProcessor(
				worldController);
		this.selectFishInputProcessor = new GSSelectFishInputProcessor(
				worldController);
		this.selectTrajectoryInputProcessor = new GSSelectTrajectoryInputProcessor(
				worldController);
	}

	private void setDefaultInputProcessor() {
		worldController.setGameInputProcessor(defaultInputProcessor);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	public InputProcessor getDefaultInputProcessor() {
		return defaultInputProcessor;
	}

	public InputProcessor getSelectFishInputProcessor() {
		return selectFishInputProcessor;
	}

	public InputProcessor getSelectTrajectoryInputProcessor() {
		return selectTrajectoryInputProcessor;
	}
}
