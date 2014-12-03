package vn.edu.hust.student.dynamicpool.presentation.gameobject;

import vn.edu.hust.student.dynamicpool.bll.model.Fish;
import vn.edu.hust.student.dynamicpool.presentation.assets.AssetGameScreen;
import vn.edu.hust.student.dynamicpool.presentation.assets.Assets;

public class FishThreeUI extends FishUIWithTwoAnimation {
	public FishThreeUI(Fish fish) {
		super(fish);
	}

	@Override
	public void initFishAsset() {
		AssetGameScreen gameScreen = Assets.instance.gameScreen;
		fishAsset = gameScreen.getFish3Asset();
	}
}
