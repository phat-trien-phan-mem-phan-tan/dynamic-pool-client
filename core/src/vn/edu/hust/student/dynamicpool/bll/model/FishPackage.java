package vn.edu.hust.student.dynamicpool.bll.model;

public class FishPackage {
	private String clientName;
	private Fish fish;

	public FishPackage() {

	}

	public FishPackage(String clientName, Fish fish) {
		this.clientName = clientName;
		this.fish = fish;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Fish getFish() {
		return fish;
	}

	public void setFish(Fish fish) {
		this.fish = fish;
	}

	public boolean equals(FishPackage f) {
		return ((clientName == null && f.clientName == null) || (this.clientName != null
				&& f.clientName != null && this.clientName.equals(f.clientName)))
				&& fish.equals(f);
	}
}
