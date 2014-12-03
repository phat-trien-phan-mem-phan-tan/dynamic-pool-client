package vn.edu.hust.student.dynamicpool.dal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.edu.hust.student.dynamicpool.bll.model.DeviceInfo;
import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.bll.model.JSONContentDTO;
import vn.edu.hust.student.dynamicpool.dal.controller.ClientMainController;
import vn.edu.hust.student.dynamicpool.dal.statics.Field;
import vn.edu.hust.student.dynamicpool.events.EventDestination;
import vn.edu.hust.student.dynamicpool.events.EventType;
import vn.edu.hust.student.dynamicpool.exception.DALException;

public class ClientDataAccessLayerImpl {
	private String clientName;
	private Logger logger = LoggerFactory
			.getLogger(ClientDataAccessLayerImpl.class);

	public ClientDataAccessLayerImpl() {
		logger.debug("Contruct");
		setClientName(UUID.randomUUID().toString());
	}

	public String getClientName() {
		return clientName;
	}

	private void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void joinHost(String key) {
		logger.debug("join host");
		try {
			ClientMainController.getInstance().start(key);
			logger.info("join host success");
			EventDestination.getInstance().dispatchSuccessEvent(
					EventType.DAL_JOIN_HOST);
		} catch (DALException e) {
			logger.error("cannot join host");
			EventDestination.getInstance().dispatchFailEventWithExeption(
					EventType.DAL_JOIN_HOST,
					new DALException("cannot join host", e));
		}

	}

	public void addDevice(DeviceInfo deviceInfo) {
		logger.debug("add device");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Field.COMMAND, Field.ADD_DEVICE);
		map.put(Field.DEVICE, deviceInfo);
		deviceInfo.setClientName(this.getClientName());
		logger.info("send add device: client name {}", deviceInfo.getClientName());
		ClientMainController.getInstance().getClientSocketController()
				.sendMessage(map);
	}

	public void requestCreateFish(Fish fish) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Field.COMMAND, Field.CREATE_FISH);
		JSONContentDTO jsonContentDTO = JSONContentDTO.fromFish(fish);
		data.put(Field.JSON_CONTENT, jsonContentDTO.toJSONString());
		data.put(Field.CLIENT_NAME, this.getClientName());
		ClientMainController.getInstance().getClientSocketController()
				.sendMessage(data);
	}

	public void exit() {

	}
}
