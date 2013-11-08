package at.theengine.android.aquarinthhia;

public class RainData {

	private String mLocationName;
	private String mTemp;
	private String mTime;
	private String mRain;
	private float mLat;
	private float mLng;
	private String mImage;
	
	public String getLocationName() {
		return mLocationName;
	}
	public void setLocationName(String locationName) {
		this.mLocationName = locationName;
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
	public String getRain() {
		return mRain;
	}
	public void setRain(String rain) {
		this.mRain = rain;
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
