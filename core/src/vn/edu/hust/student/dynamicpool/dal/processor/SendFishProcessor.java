package vn.edu.hust.student.dynamicpool.dal.processor;

import java.util.Map;

import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.bll.model.JSONContentDTO;
import vn.edu.hust.student.dynamicpool.dal.statics.Field;
import vn.edu.hust.student.dynamicpool.events.EventDestination;
import vn.edu.hust.student.dynamicpool.events.EventType;

public class SendFishProcessor extends Processor {

	@Override
	public ProcessorExecutionResponse execute(ProcessorExecutionRequest request) {
		Map<String, Object> map = request.getParameters();
		if (map.containsKey(Field.JSON_CONTENT)) {
			Object jsonContentObject = map.get(Field.JSON_CONTENT);
			if (jsonContentObject instanceof String) {
				try {
					JSONContentDTO jsonContentDTO = JSONContentDTO.createDTOfromJSONString((String)jsonContentObject);
					Fish fish = JSONContentDTO.toFish(jsonContentDTO);
					EventDestination.getInstance().dispatchSuccessEventWithObject(
								EventType.DAL_RECIVICE_NEW_FISH, fish);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		return null;
	}

}
