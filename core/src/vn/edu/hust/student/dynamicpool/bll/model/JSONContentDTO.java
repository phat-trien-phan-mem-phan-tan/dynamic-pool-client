package vn.edu.hust.student.dynamicpool.bll.model;

import java.util.ArrayList;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JSONContentDTO {
	private String jsonContent = null;

	public JSONContentDTO() {

	}

	public String getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}
	
	public boolean equals(JSONContentDTO dto) {
		return this.jsonContent.equals(dto.jsonContent);
	}
	
	@JSON(include=false)
	public String toJSONString() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}
	
	@JSON(include=false)
	public static JSONContentDTO createDTOfromJSONString(String jsonString) throws Exception {
		return new JSONDeserializer<JSONContentDTO>()
		.deserialize(jsonString, JSONContentDTO.class);
	}

	public static JSONContentDTO fromFish(Fish fish) {
		JSONContentDTO dto = new JSONContentDTO();
		String fishJson = new JSONSerializer().serialize(fish);
		dto.setJsonContent(fishJson);
		return dto;
	}

	public static Fish toFish(JSONContentDTO dto) throws Exception {
		try {
			String fishJson = dto.getJsonContent();
			return new JSONDeserializer<Fish>().deserialize(fishJson);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("cannot deserialize fish");
		}
	}
	
	public static JSONContentDTO fromFishPackage(FishPackage fishPackage) {
		JSONContentDTO dto = new JSONContentDTO();
		String jsonString = new JSONSerializer().serialize(fishPackage);
		dto.setJsonContent(jsonString);
		return dto;
	}
	
	public static FishPackage toFishPackage(JSONContentDTO dto) throws Exception {
		try {
			String jsonString = dto.getJsonContent();
			return new JSONDeserializer<FishPackage>().deserialize(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("cannot deserialize fish package");
		}
	}
	
	public static JSONContentDTO fromClientSetting(ClientSetting clientSetting) {
		JSONContentDTO dto = new JSONContentDTO();
		String jsonString = new JSONSerializer().include("segments").serialize(clientSetting);
		dto.setJsonContent(jsonString);
		return dto;
	}
	
	public static ClientSetting toClientSetting(JSONContentDTO dto) throws Exception {
		try {
			String jsonString = dto.getJsonContent();
			ClientSetting clientSetting = new JSONDeserializer<ClientSetting>().deserialize(jsonString);
			if (clientSetting.getSegments() == null) {
				clientSetting.setSegments(new ArrayList<Segment>());
			}
			return clientSetting;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("cannot deserialize Setting");
		}
	}
}
