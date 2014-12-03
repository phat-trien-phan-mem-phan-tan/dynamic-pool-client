package vn.edu.hust.student.dynamicpool.presentation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hust.student.dynamicpool.GameCenter;
import vn.edu.hust.student.dynamicpool.bll.ClientBusinessLogicLayerImpl;
import vn.edu.hust.student.dynamicpool.bll.model.DeviceInfo;
import vn.edu.hust.student.dynamicpool.bll.model.ETrajectoryType;
import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.bll.model.FishType;
import vn.edu.hust.student.dynamicpool.events.EventDestination;
import vn.edu.hust.student.dynamicpool.events.EventType;
import vn.edu.hust.student.dynamicpool.presentation.assets.Assets;
import vn.edu.hust.student.dynamicpool.presentation.gameobject.FishUICollection;
import vn.edu.hust.student.dynamicpool.presentation.gameobject.FishUIFactory;
import vn.edu.hust.student.dynamicpool.presentation.screen.DeviceInfoScreen;
import vn.edu.hust.student.dynamicpool.presentation.screen.ErrorInputProcessor;
import vn.edu.hust.student.dynamicpool.presentation.screen.ErrorScreen;
import vn.edu.hust.student.dynamicpool.presentation.screen.GameScreen;
import vn.edu.hust.student.dynamicpool.presentation.screen.LoadingScreen;
import vn.edu.hust.student.dynamicpool.presentation.screen.MainMenuScreen;
import vn.edu.hust.student.dynamicpool.presentation.screen.SplashScreen;
import vn.edu.hust.student.dynamicpool.utils.AppConst;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.eposi.eventdriven.Event;
import com.eposi.eventdriven.implementors.BaseEventListener;

public class WorldController {
	private Timer timer = new Timer();
	private GameCenter game = null;
	private ClientBusinessLogicLayerImpl businessLogicLayer = null;
	private SplashScreen splashScreen = null;
	private MainMenuScreen mainMenuScreen = null;
	private LoadingScreen loadingScreen = null;
	private GameScreen gameScreen = null;
	private DeviceInfoScreen deviceInfoScreen;
	private ErrorScreen errorScreen = null;
	private FishUICollection fishUICollection = new FishUICollection();
	private int addingFishStep = 0;
	private FishType selectedFishType = FishType.FISH1;
	private float size;
	private Logger logger = LoggerFactory.getLogger(WorldController.class);

	public WorldController(GameCenter game) {
		this.game = game;
		this.registerEvents();
	}

	private void registerEvents() {
		EventDestination.getInstance().addEventListener(EventType.APP_ERROR,
				new BaseEventListener(this, "onErrorCallbackHander"));
	}

	public void init() {
		showSplashScreen();
	}

	public void showSplashScreen() {
		WorldRenderer worldRenderer = game.getWorldRenderer();
		splashScreen = new SplashScreen(worldRenderer);
		game.setScreen(splashScreen);
		waitFewSecondsAndShowMenus();
	}

	private void waitFewSecondsAndShowMenus() {
		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				showMainMenuScreen();
			}
		}, AppConst.DELAY_TIME);
		loadMainMenuScreenResources();
		loadLoadingResources();
	}

	private void loadMainMenuScreenResources() {
		Assets.instance.initMainMenuAssets();
		WorldRenderer worldRenderer = game.getWorldRenderer();
		mainMenuScreen = new MainMenuScreen(worldRenderer, this);
	}

	public void showMainMenuScreen() {
		game.setScreen(mainMenuScreen);
	}

	private void loadLoadingResources() {
		Assets.instance.initLoadingAssets();
		WorldRenderer worldRenderer = game.getWorldRenderer();
		loadingScreen = new LoadingScreen(worldRenderer);
	}

	public void joinHost(String key) {
		creatClientBusinessLogicLayer();
		EventDestination.getInstance().addEventListener(
				EventType.BLL_JOIN_HOST_WHITH_A_KEY,
				new BaseEventListener(this, "onJoinHostCallbackHander"));
		showLoadingScreen();
		this.businessLogicLayer.joinHost(key);
	}

	private void creatClientBusinessLogicLayer() {
		this.businessLogicLayer = new ClientBusinessLogicLayerImpl();
	}

	@Deprecated
	public void onJoinHostCallbackHander(Event event) {
		if (EventDestination.parseEventToBoolean(event)) {
			loadDeviceInfoScreenResource();
			showDeviceInforScreen();
			loadGameResources();
		} else {
			logError("Cannot join host");
			EventDestination.getInstance().dispatchSuccessEventWithObject(
					EventType.APP_ERROR, "cannot join host");
		}
	}

	private void showGameScreen() {
		game.setScreen(gameScreen);
	}

	private void showLoadingScreen() {
		game.setScreen(loadingScreen);
	}

	private void showFullScreen() {
		// DisplayMode desktopDisplayMode =
		// Gdx.graphics.getDesktopDisplayMode();
		// Gdx.graphics.setDisplayMode(desktopDisplayMode.width,
		// desktopDisplayMode.height, true);
	}

	private void loadGameResources() {
		Assets.instance.initGameAssets();
		WorldRenderer worldRenderer = game.getWorldRenderer();
		gameScreen = new GameScreen(worldRenderer, this);
	}

	private void logError(String errorMessage) {
		logger.error(errorMessage);
	}

	private void loadDeviceInfoScreenResource() {
		WorldRenderer worldRenderer = game.getWorldRenderer();
		deviceInfoScreen = new DeviceInfoScreen(worldRenderer, this);
	}

	private void showDeviceInforScreen() {
		game.setScreen(deviceInfoScreen);
	}

	public boolean saveScreenSizeByInch(String screenSize) {
		if (isValidScreenSize(screenSize)) {
			sendDeviceInfoToServer();
			showFullScreen();
			// showLoadingScreen();
			return true;
		}
		return false;
	}

	private void sendDeviceInfoToServer() {
		// DisplayMode desktopDisplayMode =
		// Gdx.graphics.getDesktopDisplayMode();
		// DeviceInfo deviceInfo = new DeviceInfo(desktopDisplayMode.width,
		// desktopDisplayMode.height, this.size);
		DeviceInfo deviceInfo = new DeviceInfo(AppConst.width, AppConst.height,
				this.size);
		EventDestination.getInstance().addEventListener(
				EventType.BLL_ADD_DEVICE,
				new BaseEventListener(this, "onAddDeviceCallbackHander"));
		businessLogicLayer.addDevide(deviceInfo);
	}

	public void onAddDeviceCallbackHander(Event event) {
		game.getWorldRenderer().updateViewPort();
		showGameScreen();
	}

	private boolean isValidScreenSize(String screenSize) {
		try {
			size = Float.parseFloat(screenSize);
			if (size <= 0 || size > 30)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public FishUICollection getFishUICollection() {
		return fishUICollection;
	}

	public List<Fish> getFishs() {
		List<Fish> fishs = businessLogicLayer.getFishes();
		return fishs;
	}

	public void updateFishsCordinate(float deltaTime) {
		businessLogicLayer.update(deltaTime + 0.05f);
	}

	public void exit() {
		businessLogicLayer.exit();
		Gdx.app.exit();
	}

	public void addFishButtonClick() {
		if (this.addingFishStep == 0) {
			this.addingFishStep = 1;
		} else {
			this.addingFishStep = 0;
		}
		InputProcessor selectFishInputProcessor = gameScreen
				.getSelectFishInputProcessor();
		this.setGameInputProcessor(selectFishInputProcessor);
	}

	public boolean isShowSelectFishButtons() {
		return addingFishStep == 1;
	}

	public void setGameInputProcessor(InputProcessor inputProcessor) {
		Gdx.input.setInputProcessor(inputProcessor);
	}

	public void cancelAddFish() {
		this.addingFishStep = 0;
		this.selectedFishType = FishType.FISH1;
	}

	public void selectFish(FishType fishType) {
		this.selectedFishType = fishType;
		this.addingFishStep = 2;
		InputProcessor selectTrajectoryInputProcessor = gameScreen
				.getSelectTrajectoryInputProcessor();
		this.setGameInputProcessor(selectTrajectoryInputProcessor);
	}

	public boolean isShowSelectTrajectoryButtons() {
		return addingFishStep == 2;
	}

	public void selectTrajectory(ETrajectoryType trajectoryType) {
		createFish(selectedFishType, trajectoryType);
		cancelAddFish();
	}

	private void createFish(FishType fishType, ETrajectoryType trajectoryType) {
		businessLogicLayer.createFish(fishType, trajectoryType,
				FishUIFactory.getWith(fishType),
				FishUIFactory.getHeight(fishType));
	}

	@Deprecated
	public void onErrorCallbackHander(Event event) {
		if (EventDestination.parseEventToBoolean(event)) {
			Object object = EventDestination.parseEventToTargetObject(event);
			String errorMessage = object == null ? "App error" : object
					.toString();
			showErrorScreen(errorMessage);
		}
	}

	private void showErrorScreen(String errorMessage) {
		if (this.errorScreen == null) {
			loadErrorScreen();
		}
		errorScreen.showError(errorMessage);
		game.setScreen(errorScreen);
		setGameInputProcessor(new ErrorInputProcessor(this));
	}

	private void loadErrorScreen() {
		errorScreen = new ErrorScreen(game.getWorldRenderer());
	}
}