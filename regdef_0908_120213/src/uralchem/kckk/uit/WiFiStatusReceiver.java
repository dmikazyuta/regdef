package uralchem.kckk.uit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;


public class WiFiStatusReceiver extends BroadcastReceiver
{
	private static Context mContext;
	
	public void onReceive(Context context, Intent intent) 
	{
		if (mContext == null) mContext = context;
		WifiManager wm = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);	
		
		if (intent.getBooleanExtra(wm.SUPPLICANT_CONNECTION_CHANGE_ACTION, true)) 
		{
			if (intent.getBooleanExtra(wm.EXTRA_SUPPLICANT_CONNECTED, false))
				{					
					Toast.makeText(context, " WIFI модуль успешно подключен ", Toast.LENGTH_LONG).show();
				}
			else {					
					Toast.makeText(context, " WIFI модуль НЕ подключен ", Toast.LENGTH_LONG).show();
				 }
		}						
    }
	
 
	
}
