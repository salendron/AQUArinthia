package at.theengine.android.aquarinthhia;

public class LakeData {
	
	private String mLakeName;
	private String mTemp;
	private String mHeight;
	private String mTime;
	private float mLat;
    private float mLng;
    private String mImage;
	
	public String getLakeName() {
		return mLakeName;
	}
	public void setLakeName(String lakeName) {
		this.mLakeName = lakeName;
	}
	public String getTemp() {
		return mTemp;
	}
	public void setTemp(String temp) {
		this.mTemp = temp;
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
