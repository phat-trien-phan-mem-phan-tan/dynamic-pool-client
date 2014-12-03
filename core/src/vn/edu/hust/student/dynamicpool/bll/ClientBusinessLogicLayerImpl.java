package vn.edu.hust.student.dynamicpool.bll;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hust.student.dynamicpool.bll.model.DeviceInfo;
import vn.edu.hust.student.dynamicpool.bll.model.ETrajectoryType;
import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.bll.model.FishFactory;
import vn.edu.hust.student.dynamicpool.bll.model.FishType;
import vn.edu.hust.student.dynamicpool.bll.model.Pool;
import vn.edu.hust.student.dynamicpool.bll.model.client.ClientPoolManager;
import vn.edu.hust.student.dynamicpool.dal.ClientDataAccessLayerImpl;
import vn.edu.hust.student.dynamicpool.events.EventDestination;
import vn.edu.hust.student.dynamicpool.events.EventType;

import com.eposi.eventdriven.Event;
import com.eposi.eventdriven.implementors.BaseEventListener;

public class ClientBusinessLogicLayerImpl {

	protected ClientDataAccessLayerImpl dataAccessLayer;
	private ClientPoolManager clientPoolManager = new ClientPoolManager();
	private Logger logger = LoggerFactory
			.getLogger(ClientBusinessLogicLayerImpl.class);

	public ClientBusinessLogicLayerImpl() {
		this.dataAccessLayer = new ClientDataAccessLayerImpl();
		registerEvents();
	}

	protected void registerEvents() {
		EventDestination.getInstance().addEventListener(
				EventType.DAL_JOIN_HOST,
				new BaseEventListener(this, "onJoinHostCallBackHander"));
		EventDestination.getInstance().addEventListener(
				EventType.DAL_UPDATE_SETTINGS_RESPONSE,
				new BaseEventListener(this, "onUpdateSettingCallbackHander"));
		EventDestination.getInstance().addEventListener(
				EventType.DAL_CREATE_FISH_RESPONSE,
				new BaseEventListener(this, "onCreateFishCallbackHander"));
		EventDestination.getInstance().addEventListener(
				EventType.DAL_RECIVICE_NEW_FISH,
				new BaseEventListener(this, "onRecieveNewFishCallbackHander"));
		EventDestination.getInstance().addEventListener(
				EventType.DAL_DISCONNECT_FROM_SERVER,
				new BaseEventListener(this,
						"onDisconnectFromServerCallbackHander"));
	}

	public void joinHost(String key) {
		this.dataAccessLayer.joinHost(key);
	}

	@Deprecated
	public void onJoinHostCallBackHander(Event event) {
		logger.debug("on join host callback hander");
		if (EventDestination.parseEventToBoolean(event)) {
			logger.info("join host success");
			EventDestination.getInstance().dispatchSuccessEvent(
					EventType.BLL_JOIN_HOST_WHITH_A_KEY);
		} else {
			logger.error("cannot join host");
			EventDestination.getInstance()
					.dispatchSuccessEventWithObject(EventType.APP_ERROR,
							"cannot join host, please check again");
		}
	}

	public void addDevide(DeviceInfo deviceInfo) {
		logger.debug("add device");
		logger.info("send add device: client name {}",
				deviceInfo.getClientName());
		dataAccessLayer.addDevice(deviceInfo);
	}

	@Deprecated
	public void onUpdateSettingCallbackHander(Event event) {
		logger.debug("on update setting callback hander");
		if (EventDestination.parseEventToBoolean(event)) {
			Object addDeviceResultObject = EventDestination
					.parseEventToTargetObject(event);
			if (Pool.class.isInstance(addDeviceResultObject)) {
				logger.debug("recive setting success");
				Pool clientPoolSetting = (Pool) addDeviceResultObject;
				logger.info("update setting: client name {}", clientPoolSetting
						.getDeviceInfo().getClientName());
				clientPoolManager.updateSetting(clientPoolSetting);
				EventDestination.getInstance().dispatchSuccessEvent(
						EventType.BLL_ADD_DEVICE);
				return;
			}
			logger.debug("cannot update setting: target object is not an instance of Pool");
		}
		logger.debug("Cannot update setting");
		EventDestination.getInstance().dispatchFailEvent(
				EventType.BLL_ADD_DEVICE);
	}

	public List<Fish> getFishes() {
		return clientPoolManager.getFishes();
	}

	public void update(float deltaTime) {
		clientPoolManager.updateLocationOfFishes(deltaTime);
	}

	public void exit() {
		this.dataAccessLayer.exit();
	}

	public void createFish(final FishType fishType,
			final ETrajectoryType trajectoryType, final int width,
			final int height) {
		logger.debug("Create fish");
		Fish fish = FishFactory.createFishWithTrajectoryType(fishType,
				trajectoryType, width, height);
		clientPoolManager.addFish(fish);
		dataAccessLayer.requestCreateFish(fish);
	}

	@Deprecated
	public void onCreateFishCallbackHander(Event event) {
		logger.debug("on create fish callback hander");
		if (EventDestination.parseEventToBoolean(event)) {
			Object targetObject = EventDestination
					.parseEventToTargetObject(event);
			if (Fish.class.isInstance(targetObject)) {
				logger.info("create fish success");
				Fish fish = (Fish) targetObject;
				clientPoolManager.addFish(fish);
				EventDestination.getInstance().dispatchSuccessEventWithObject(
						EventType.BLL_CREATE_FISH, targetObject);
				return;
			}
			logger.error("cannot create fish: target object is not instance of IFish");
		} else {
			logger.error("cannot create fish");
		}
		EventDestination.getInstance().dispatchFailEvent(
				EventType.BLL_CREATE_FISH);
	}

	@Deprecated
	public void onRecieveNewFishCallbackHander(Event event) {
		if (EventDestination.parseEventToBoolean(event)) {
			Object fishObject = EventDestination
					.parseEventToTargetObject(event);
			if (fishObject instanceof Fish) {
				this.clientPoolManager.addFish((Fish) fishObject);
			}
		}
	}
	
	@Deprecated
	public void onDisconnectFromServerCallbackHander(Event event) {
		EventDestination.getInstance().dispatchSuccessEventWithObject(EventType.APP_ERROR,"lost connection");
	}
}