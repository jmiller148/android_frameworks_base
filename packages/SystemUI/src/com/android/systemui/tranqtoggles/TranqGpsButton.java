package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;




public class TranqGpsButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private View mDivider;
	Handler mHandler = new Handler();
	final GpsObserver mGpsObserver = new GpsObserver(mHandler) ;

	
	// Gps settings observer
	class GpsObserver extends ContentObserver{
		
		public GpsObserver(Handler handler) {
			super(handler);
		}

	    @Override
	    public void onChange(boolean selfChange){

	    	updateResources();
	    }
	}



	public TranqGpsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_2);
		mIcon = (View) getRootView().findViewById(R.id.gps_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_2);

	
		
		getContext().getContentResolver().registerContentObserver(
                Settings.Secure.getUriFor(Settings.Secure.LOCATION_PROVIDERS_ALLOWED), true,
                mGpsObserver);
		
		
		updateResources();
	}

	
	protected void onDetachedFromWindow(){

		getContext().getContentResolver().unregisterContentObserver(mGpsObserver);
	}

	


	@Override
	protected boolean getStatusOn(){
	
		return Settings.Secure.isLocationProviderEnabled(getContext().getContentResolver(), LocationManager.GPS_PROVIDER);
	}

	@Override
	void updateResources() {
		
		ContentResolver contentResolver = getContext().getContentResolver();
	    boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
	    if(gpsStatus){
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_gps_on);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);
	    }else{
	    	mIcon.setBackgroundResource(R.drawable.tranqtoggle_gps_off);
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);

	    }
		
		mDivider.setBackgroundColor(TranqToggleViewTop.mToggleDivColor);
	}



	@Override
	void toggleOn() {

		ContentResolver contentResolver = getContext().getContentResolver();
	    Settings.Secure.setLocationProviderEnabled(contentResolver,"gps" ,true);
	   
		updateResources();
	}


	@Override
	void toggleOff() {
		ContentResolver contentResolver = getContext().getContentResolver();
	     Settings.Secure.setLocationProviderEnabled(contentResolver,"gps" ,false);		
		
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
