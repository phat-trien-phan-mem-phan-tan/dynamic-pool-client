package vn.edu.hust.student.dynamicpool.presentation.screen;

import vn.edu.hust.student.dynamicpool.events.EventDestination;
import vn.edu.hust.student.dynamicpool.events.EventType;
import vn.edu.hust.student.dynamicpool.presentation.WorldController;
import vn.edu.hust.student.dynamicpool.presentation.WorldRenderer;
import vn.edu.hust.student.dynamicpool.presentation.assets.AssetMainMenuScreen;
import vn.edu.hust.student.dynamicpool.presentation.assets.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.eposi.eventdriven.Event;
import com.eposi.eventdriven.implementors.BaseEventListener;

public class MainMenuScreen implements Screen {
	private WorldRenderer worldRenderer = null;
	private AssetMainMenuScreen mainMenuAssets = null;
	private Stage stage = new Stage();
	private Table table = new Table();
	private ImageButton joinHostButton = null;
	private WorldController worldController;

	public MainMenuScreen(WorldRenderer worldRenderer,
			WorldController worldController) {
		this.worldRenderer = worldRenderer;
		this.worldController = worldController;
	}

	@Override
	public void render(float delta) {
		worldRenderer.beginRender();
		stage.act();
		stage.draw();
		worldRenderer.endRender();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		mainMenuAssets = Assets.instance.mainMenuScreen;
		initJoinButton();
		initBackground();
		configureTableAndStage();
	}

	private void initJoinButton() {
		TextureRegionDrawable joinHostImageUp = mainMenuAssets
				.getJoinHostDrawable();
		joinHostButton = new ImageButton(joinHostImageUp);
		addJoinClickListener();
		table.add(joinHostButton).row();
	}

	private void addJoinClickListener() {
		joinHostButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				joinHostClickHander();
			}
		});
	}

	protected void joinHostClickHander() {
		EventDestination.getInstance().addEventListener(
				EventType.PRS_ENTER_KEY,
				new BaseEventListener(this, "onEnterKeyCallbackHander"));
		Gdx.input.getTextInput(new EnterKeyInputListener(),
				"Please enter key of host", "");
	}

	public void onEnterKeyCallbackHander(Event event) {
		Object keyObject = EventDestination.parseEventToTargetObject(event);
		if (EventDestination.parseEventToBoolean(event)
				&& keyObject != null) {
			joinHostAction(keyObject.toString());
		}
	}

	public void joinHostAction(String key) {
		worldController.joinHost(key);
	}

	private void initBackground() {
		TextureRegionDrawable background = mainMenuAssets
				.getMainMenuBackgroundDrawable();
		table.setBackground(background);
	}

	private void configureTableAndStage() {
		table.setFillParent(true);
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
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

}
