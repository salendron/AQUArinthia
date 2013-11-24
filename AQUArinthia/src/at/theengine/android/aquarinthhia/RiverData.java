package at.theengine.android.aquarinthhia;

public class RiverData {
	
	private String mRiverName;
	private String mMass;
	private String mHeight;
	private String mTime;
	private float mLat;
    private float mLng;
    private String mImage;
	
	public String getRiverName() {
		return mRiverName;
	}
	public void setRiverName(String riverName) {
		this.mRiverName = riverName;
	}
	public String getMass() {
		return mMass;
	}
	public void setMass(String mass) {
		this.mMass = mass;
	}
	public String getTime() {
		return mTime;
	}
	public void setTime(String time) {
		this.mTime = time;
	}
	public String getHeight() {
		return mHeight;
	}
	public void setHeight(String height) {
		this.mHeight = height;
	}
	
	public float getLat() {
        return this.mLat;
    }

    public void setLat(float lat) {
        this.mLat = lat;
    }
    
    public float getLng() {
        return this.mLng;
    }

    public void setLng(float lng) {
        this.mLng = lng;
    }
    public String getImage() {
		return mImage;
	}
	public void setImage(String image) {
		this.mImage = image;
	}
}
