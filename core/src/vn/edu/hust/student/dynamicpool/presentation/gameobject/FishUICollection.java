package vn.edu.hust.student.dynamicpool.presentation.gameobject;

import java.util.HashMap;
import java.util.Map;

import vn.edu.hust.student.dynamicpool.bll.model.Fish;

public class FishUICollection {
	private Map<String, FishUI> dictionary = new HashMap<String, FishUI>();
	public FishUICollection() {
		
	}
	public FishUI getFishUI(Fish fish) {
		String fishId = fish.getFishId();
		FishUI fishUI = dictionary.get(fishId);
		return fishUI == null ? FishUIFactory.createFishUI(fish) : fishUI;
	}
}
