package uralchem.kckk.uit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
//import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uralchem.kckk.uit.R.color;
import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsoluteLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.jibble.simpleftp.*;

@SuppressWarnings({ "unused", "deprecation" })
public class FirstDBActivity extends Activity 
{	
    private static String progressText = null;
    private static String parentId = "empty";
    private static String aggrId = "empty";
	
	public static String TEXT = "text";
	public static String IMAGE = "image";
	public static String CHECK = "check";
	
	public static int CAMERA = 1;
	public static int GALLERY = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState); 
        final Context context = getApplicationContext();        
        setContentView(R.layout.main);  
        
        //** Form Widgets       
        // Buttons             
        final Button saveButton = (Button)findViewById(R.id.SaveButton);
        final Button syncButton = (Button)findViewById(R.id.SyncButton);
        final ToggleButton tglButton1 = (ToggleButton)findViewById(R.id.toggleButton1);
        final ToggleButton tglButton2 = (ToggleButton)findViewById(R.id.toggleButton2); 
        final Button saveButton2 = (Button)findViewById(R.id.SaveButton2);
        final Button syncButton2 = (Button)findViewById(R.id.SyncButton2);       
        final Button saveOptionButton2 = (Button)findViewById(R.id.SaveOptionButton2);
        final Button testOptionButton1 = (Button)findViewById(R.id.TestOptionButton1);
        final Button deleteDefButton = (Button)findViewById(R.id.DeleteDefButton);
        final Button checkNewVersionButton = (Button)findViewById(R.id.CheckNewVersionButton);
        final ImageButton cameraButton = (ImageButton)findViewById(R.id.imageButton1);
        final ImageButton photoButton = (ImageButton)findViewById(R.id.imageButton2);
        // Изменить Цех и Участок
        final Button updateButton1 = (Button)findViewById(R.id.UpdateButton1);
        final Button saveOptionButton1 = (Button)findViewById(R.id.SaveOptionButton1);
        final Button updateZehSectorButton = (Button)findViewById(R.id.UpdateZehSectorButton);
        // TextView     
        final TextView textViewJourData = (TextView) findViewById(R.id.TextViewJourData);
        final TextView textViewTestOptionResult = (TextView) findViewById(R.id.TextViewTestOptionResult);
        final TextView textViewDateValue = (TextView) findViewById(R.id.TextViewDateValue);
        final TextView textViewZehValue = (TextView) findViewById(R.id.TextViewZehValue);
        final TextView textViewSectorValue = (TextView) findViewById(R.id.TextViewSectorValue);
        final TextView textViewSmenaValue = (TextView) findViewById(R.id.TextViewSmenaValue);
        final TextView textViewObj = (TextView) findViewById(R.id.TextViewObj);
        final TextView textViewVersionValue = (TextView) findViewById(R.id.TextViewVersionValue);
        final TextView textViewVersionDateValue = (TextView) findViewById(R.id.TextViewVersionDateValue);
        final TextView textViewChosenAggr = (TextView) findViewById(R.id.TextViewChosenAggr);
        final TextView textViewChosenAggrValue = (TextView) findViewById(R.id.TextViewChosenAggrValue);
        // Checkbox
        // final CheckBox rowCheckBox = (CheckBox)findViewById(R.id.rowCheckBox);
        // ListView
        ListView outputList = (ListView) findViewById(R.id.listViewMenu); 
        final ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);        
        final ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
        final ListView listViewOption = (ListView) findViewById(R.id.ListViewOptions);
        // frame layouts
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayoutOptions);       
   		// Tabs layout   		
   		final TabHost tabs=(TabHost)findViewById(R.id.tabhost);       
        tabs.setup();        
        // spiners        
        final Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
        final Spinner spinObj = (Spinner) findViewById(R.id.SpinObj);
        final Spinner spinPosit = (Spinner) findViewById(R.id.SpinPosit);        
        final Spinner spinPart = (Spinner) findViewById(R.id.SpinPart);
        final Spinner spinEquip = (Spinner) findViewById(R.id.SpinEquip);
        final Spinner spinCause = (Spinner) findViewById(R.id.SpinCause);        
        final Spinner spinOptionZeh = (Spinner) findViewById(R.id.SpinOptionZeh);
		final Spinner spinOptionSector = (Spinner) findViewById(R.id.SpinOptionSector);		
        // edit text
        final EditText editTextOptionServer = (EditText) findViewById(R.id.EditTextOptionServer);
        final EditText editTextOptionUsername = (EditText) findViewById(R.id.EditTextOptionUsername);
        final EditText editTextOptionPassword = (EditText) findViewById(R.id.EditTextOptionPassword);        
        // variable
        String optionServer = editTextOptionServer.getText().toString();  
        // tab host
        // tab 1
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.tabPage1);  
        TextView tab1 = null;       
        tab1 = new TextView(this);
        tab1.setText("Регистрация дефектов");
        tab1.setGravity(android.view.Gravity.CENTER);
        tab1.setTextSize(22.0f);
        tab1.setTextColor(this.getResources().getColorStateList(R.drawable.tab_text_color));
        spec.setIndicator(tab1); 
        tabs.addTab(spec);      
        // tab 3
        TabHost.TabSpec spec3=tabs.newTabSpec("tag3");               
        spec3.setContent(R.id.tabPage3);        
        TextView tab3 = null; 
        tab3 = new TextView(this);
        tab3.setText("Журнал");     
        tab3.setGravity(android.view.Gravity.CENTER);
        tab3.setTextSize(22.0f);
        tab3.setTextColor(this.getResources().getColorStateList(R.drawable.tab_text_color));        
        spec3.setIndicator(tab3);        
        tabs.addTab(spec3);
        // tab 2
        TabHost.TabSpec spec2=tabs.newTabSpec("tag2");               
        spec2.setContent(R.id.tabPage2);
        TextView tab2 = null; 
        tab2 = new TextView(this);
        tab2.setText("Настройки");     
        tab2.setGravity(android.view.Gravity.CENTER);
        tab2.setTextSize(22.0f);
        tab2.setTextColor(this.getResources().getColorStateList(R.drawable.tab_text_color));        
        spec2.setIndicator(tab2);        
        tabs.addTab(spec2);  
        // initiate tabs
        tabs.setCurrentTab(0);        
        initTabsAppearance(tabs.getTabWidget());   
        // wifi        
   		this.registerReceiver(this.WifiStateChangedReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));   	   		  
   		try {
   				@SuppressWarnings("static-access")
				WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
   				wm.setWifiEnabled(true);   				
   			}
		catch  (Exception ex) {     
									CharSequence text = ex.getMessage();  
									int duration = Toast.LENGTH_LONG;
									Toast toast = Toast.makeText(context, "Ошибка модуля wi-fi\n"+text, duration);  
									toast.show();
   						 	  }
   		
   		// main UI Thread handler 
   		final Handler myHandler = new Handler();
   		// Установки перед использованием
   		new Thread(new Runnable() {
   	        public void run() {
       	        myHandler.post(new Runnable() { 
    				public void run() {
    					// spinners on tab1 lock   					
		   	    	 	spinPosit.setEnabled(false);
		   	    	 	spinObj.setEnabled(false);
		   	    	 	// spinners on tab1 invisible
		   	    	 	spinObj.setVisibility(View.GONE);
		   	    	 	textViewObj.setVisibility(View.GONE);
		   	    	 	// other   	    	 	
		   	    	 	setEnableInterfaces(false);   	    	 	
		   	    	 	listViewJour.setVisibility(View.GONE);
		   	    	 	textViewJourData.setVisibility(View.GONE);
		   	    	 	setViewListMenu(FirstDBActivity.this, listViewMenu);   	    	 	
		   	    	 	setViewOptionList(FirstDBActivity.this, listViewOption, frameLayout);	
		   	    	 	// кнопки камеры
		   	    	// 	cameraButton.setVisibility(View.GONE);
		   	    	// 	photoButton.setVisibility(View.GONE);
    								}
				});	
   	    	 	//* Настройки
   	    	 	final DBAdapter adap = new DBAdapter(context);   	    	 	
   	    	 	adap.open();
   	    	 	// Предустановка параметров Соединение
        		final String server = adap.getConnectParam("server");
        		final String username = adap.getConnectParam("username");
        		final String cryptpassword = adap.getConnectParam("password");
        		final String password = Decrypt(cryptpassword);        		
        		// Предустановка параметров Цех и Участок        		
        		final String zehid = adap.getConnectParam("ZEH");
        		final String sectorid = adap.getConnectParam("SECTOR");    
        		if (zehid == "" || sectorid == "") adap.saveZehSectorParams("0", "0");
        			Log.d("preconf zehid", zehid );
        			Log.d("preconf sectorid", sectorid );   
        			
        		final String zeh = adap.getZehSectorName(zehid);
        		final String sector = adap.getZehSectorName(sectorid);
        		final int noSendDefectsCount = adap.getNoSendDefectsCount(0);
        			Log.d("preconf zehname", zeh );
        			Log.d("preconf sectorname", sector );
        			
       	        // Предустановка спиннера Агрегат
        		String startwith = adap.getConnectParam("SECTOR");
   	    	    setAggrSpinner(adap, myHandler, startwith);
   	    	    
   	    	    // Очистка временных фотографий
   	    	    try {
   	    	    deleteImages(context, adap);
   	    	    }   	    	    
   	    	    catch (Exception ex)
   	    	    {
   	    	    	Toast.makeText(context, "Фотографий для удаления не найдено", 
   	    				Toast.LENGTH_LONG).show();
   	    	    	Log.d("images_link", ex.getMessage());
   	    	    }
       	        // Выставление значений в spinner
        		ArrayList<String> arrayListZeh = new ArrayList<String>();
   	    	    arrayListZeh.add(zeh);
        		ArrayList<String> arrayListSector = new ArrayList<String>();
   	    	    arrayListSector.add(sector);   	    	    
   	    	    ArrayAdapter<String> arrayAdapterZeh = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListZeh);
	    	 	arrayAdapterZeh.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
   	    	    ArrayAdapter<String> arrayAdapterSector = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListSector);
	    	 	arrayAdapterSector.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
	    	 	spinOptionZeh.setAdapter(arrayAdapterZeh);
	    	 	spinOptionSector.setAdapter(arrayAdapterSector);	    	 	   	 	   	    	 	
   	    	 	
   	    	 	// Номер и дата обновления
   	    	 	String[] appfields = GetCurrentVersion();
				final String currentVersionCode = appfields[0].toString(); 																		
				final String currentVersionName = appfields[1].toString();
				final String lastUpdate = adap.getLastUpdate();
				adap.close();
        		// Выставление значений
       	        myHandler.post(new Runnable() { 
    				public void run() 
    				{
    						// Шапка вкладки. Информация о цехе, участке, смене и времени регистрации
    						// tab1 - Дата регистрации       	
    		   	        	textViewDateValue.setText(" "+getCurrDate());
    		   	        	// tab1 - Смена
    		   	        	try {textViewSmenaValue.setText(getShift());} catch (java.text.ParseException e) {}
    		        		// tab1 - Вывод Цеха и Участка в красивом виде
    		        		String zeh2 = zeh.substring(zeh.lastIndexOf("х ")+1);
    		        		String sector2 = sector.substring(sector.lastIndexOf("к ")+1);
    		   	        	textViewZehValue.setText(zeh2);
    		   	        	textViewSectorValue.setText(sector2); 
    		   	        	textViewSmenaValue.setText(" "+"A");
    		   	        	// tab1 - Совет по выбору
    		   	        	getAggrTip(context, true);
    		   	        	// tab2 - Кнопка удаления из журнала исчезает
    		   	        	deleteDefButton.setVisibility(View.GONE);
    		   	        	// tab3 - Сервер, Логин, Пароль 
    		        		editTextOptionServer.setText(server);
    		        		editTextOptionUsername.setText(username);
    		        		editTextOptionPassword.setText(password);    		        		
    		   	        	// Кнопка настроек "Обновить"
    		   	        	if (zehid.equals("") || sectorid.equals("")) updateZehSectorButton.setEnabled(false);
    		   	        	// Кнопки "Синхронизации" блокируются при отсутствии неотправленных значений
    		   	        	if ( !(noSendDefectsCount > 0) ) 
    		   	        		{
    		   	        			syncButton.setEnabled(false);
    		   	        		}
    		   	        	// Номер и название обновлений
    		   	        	textViewVersionValue.setText(currentVersionName+" ("+currentVersionCode+")");
    		   	        	textViewVersionValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); 
    		   	        	textViewVersionDateValue.setText(lastUpdate);
    		   	        	textViewVersionDateValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    				}
    			});	
   	    	 	
   	    	 	// Выставление значений
       	        myHandler.post(new Runnable() { 
    				public void run() {       	    	 		
    	   	    	 	// tab 3 - spinner
    	   	    	 	spinOptionZeh.setEnabled(false);
    	   	    	 	spinOptionSector.setEnabled(false);
    	   	    	 	spinEquip.setEnabled(false);
    				    spinCause.setEnabled(false);    				    
    	   	    	 	// tab 3 - кнопки
    	            	saveOptionButton2.setEnabled(false);				
    					saveOptionButton1.setEnabled(false);
    					saveButton.setEnabled(false);
    				}
				});
       	        
   	        			}  }).start();
   		
        // Listeners        
        tglButton1.setOnClickListener(mAddListener);
        saveButton.setOnClickListener(mAddListener);
        syncButton.setOnClickListener(mAddListener);          
        tglButton2.setOnClickListener(mAddListener);
        saveButton2.setOnClickListener(mAddListener);
        syncButton2.setOnClickListener(mAddListener);
        // Кнопки настройки для работы с сервером, логином и паролем
        saveOptionButton1.setOnClickListener(mAddListener);
        saveOptionButton2.setOnClickListener(mAddListener);
        // Кнопки настройки для работы с Цехом-Участком
        testOptionButton1.setOnClickListener(mAddListener);
        updateButton1.setOnClickListener(mAddListener);
        updateZehSectorButton.setOnClickListener(mAddListener);
        deleteDefButton.setOnClickListener(mAddListener);
        // Обновление версии программы
        checkNewVersionButton.setOnClickListener(mAddListener);
        // Камера
        cameraButton.setOnClickListener(mAddListener);
        photoButton.setOnClickListener(mAddListener);
        
        // Toggle Button 1 Listener
    	tglButton1.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) 
            {
            	final WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            	// Текст диалога
            	final TextView view1 = new TextView(context);            			
    		    view1.setText("Вы собираетесь отключить WIFI модуль. \nСинхронизация с базой данных будет не возможна. " +
				"\n" +
				"\nПродолжить?" +
				"\n");
    			view1.setGravity(Gravity.CENTER);
    			view1.setTextColor(Color.WHITE);
    			view1.setTextSize(18);
    			
            	if (!tglButton1.isChecked())
            	{
            		AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstDBActivity.this);
        			builder1.setMessage("Внимание!")
        			.setCancelable(false)
        			.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
        			{
        				@SuppressWarnings("unchecked")
						public void onClick(DialogInterface dialog, int id) 
							{  				 	
   				 				wm.setWifiEnabled(false);  
   				 				syncButton.setEnabled(false);
   				 				tglButton2.setChecked(false);
							}         
        				})   
        			.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
        			{             
        				public void onClick(DialogInterface dialog, int id) 
						{                 
        					tglButton1.setChecked(true);
						}         
        			})	
        			.setView(view1).create().show();
            	}
            	else 
            	{
           		 new Thread(new Runnable() {
     	   	        public void run() {	                               
     	   	        	myHandler.post(new Runnable()
                    	 { public void run() 
                    	 	            {
                    		 				try 
                    		 					{
                    		 						// Toggle Button
                    		 						wm.setWifiEnabled(true);
                    		 						tglButton2.setChecked(true);
                    		 						// Sync Button
                    		 						DBAdapter adap = new DBAdapter(context);
                    		 						adap.open();   
                    		 						int noSendDefectsCount = adap.getNoSendDefectsCount(0);
                    		 						adap.close();
                    		 						if (noSendDefectsCount > 0) syncButton.setEnabled(true);                    		 						
                    		 					} catch (Exception e) {
                    		 							Log.e("error in thread", e.getMessage() );
                    		 						}
                    	 	            }
                    	 });  
     	   	        				}  }).start();   
            	}
            } 
        }); 

        // Toggle Button 2 Listener
    	tglButton2.setOnClickListener(new OnClickListener() { 
            public void onClick(View v) { 
            	final WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            	// Текст диалога
            	final TextView view1 = new TextView(context);            			
    		    view1.setText("Вы собираетесь отключить WIFI модуль. \nСинхронизация с базой данных будет не возможна. " +
				"\n" +
				"\nПродолжить?" +
				"\n");
    			view1.setGravity(Gravity.CENTER);
    			view1.setTextColor(Color.WHITE);
    			view1.setTextSize(18);
    			
            	if (!tglButton2.isChecked())
            	{
            		AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstDBActivity.this);
        			builder1.setMessage("Внимание!")
        			.setCancelable(false)
        			.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
        			{
        			@SuppressWarnings("unchecked")
					public void onClick(DialogInterface dialog, int id) 
					{ 
        				wm.setWifiEnabled(false);  
        				syncButton2.setEnabled(false);
        				tglButton1.setChecked(false);
					}         
    				})   
        			.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
    				{             
    				public void onClick(DialogInterface dialog, int id) 
    						{                 
    							tglButton2.setChecked(true);
    						}         
    				})	
    				.setView(view1).create().show();
            	}
            	else
            	{
            		 new Thread(new Runnable() {
            	   	        public void run() {	                               
            	   	        	myHandler.post(new Runnable()
                           	 { public void run() 
                           	 	            {
                           		 				try 
                           		 					{
                           		 						// Toggle Button
                           		 						wm.setWifiEnabled(true); 
                           		 						tglButton1.setChecked(true);
                           		 						// Sync Button
                           		 						DBAdapter adap = new DBAdapter(context);
                           		 						adap.open();   
                           		 						int noSendDefectsCount = adap.getNoSendDefectsCount(0);
                           		 						adap.close();
                           		 						if (noSendDefectsCount > 0) syncButton2.setEnabled(true);                           		 						
                           		 					} catch (Exception e) {
                           		 							Log.e("error in thread", e.getMessage() );
                           		 						}
                           	 	            }
                           	 });  
            	   	        				}  }).start();   
            	}
            } 
        }); 
        
        // TabHost Listener
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
        	public void onTabChanged(String tabId) {
        	int i = tabs.getCurrentTab();
        	final DBAdapter adap = new DBAdapter(context);
        	     	
        	    if (i == 0) 
        	    {
                 	 new Thread(new Runnable() {
           	   	        public void run() {	                               
           	   	        	myHandler.post(new Runnable()
                          	 { public void run() 
                          	 	            {
                          		 				try 
                          		 					{
                          		 						adap.open();   
                          		 						final int noSendDefectsCount = adap.getNoSendDefectsCount(0);
                          		 						adap.close();
                          		 						// tab 1 - Проверка состояния кнопок
                          		 						if (!(tglButton1.isChecked()) || !(noSendDefectsCount > 0)) 
                          		 						{        	           	    
                          		 							syncButton.setEnabled(false); 
                          		 						}
                           		 						else {
                       		 									syncButton.setEnabled(true);
                       		 								 }                             			
                          		 					} catch (Exception e) {
                          		 											Log.e("error in thread", e.getMessage() );
                          							  					  }
                          	 	            }
                          	 });  
                              }  }).start();      	    	
        	    }
  
        	    else if (i == 1) 
        	    {
                	 new Thread(new Runnable() {
            	   	        public void run() {	                               
            	   	        	myHandler.post(new Runnable()
                           	 { public void run() 
                           	 	            {
                           		 				try 
                           		 					{
                           		 						adap.open();   
                           		 						final int noSendDefectsCount = adap.getNoSendDefectsCount(0);
                           		 						adap.close();
                           		 						// tab 1 - Проверка состояния кнопок
                           		 						if (!(tglButton2.isChecked()) || !(noSendDefectsCount > 0)) 
                           		 						{        	           	    
                           		 							syncButton2.setEnabled(false);
                           		 							//syncButton2.setEnabled(true);
                           		 							// Переключение пункта меню        				               	     				
                           		 						}
                           		 						else {
                           		 								syncButton2.setEnabled(true);
                           		 								listViewMenu.performItemClick(listViewMenu.getAdapter().getView(0, null, null), 0, 0);
                           		 							 }
                           		 						
                           		 					} catch (Exception e) {
                           		 											Log.e("error in thread", e.getMessage() );
                           							  					  }
                           	 	            }
                           	 });  
                               }  }).start();   
        	    }
        	    else if (i == 2) 
        	    {
        	    	new Thread(new Runnable() {
           	   	        public void run() {	                               
           	   	        	myHandler.post(new Runnable()
                          	 { public void run() 
                          	 	            {
                          		
                          		 				try 
                          		 					{
                          		 						//adap.open();   
                          		 						//final int noSendDefectsCount = adap.getNoSendDefectsCount(0);
                          		 						//adap.close();
                          		 						// tab 1 - Проверка состояния кнопок
                          		 						if ( !(tglButton1.isChecked()) ) 
                          		 						{   
                          		 							// Цех и Участок
                          		 							updateZehSectorButton.setEnabled(false);
                          		 							updateButton1.setEnabled(false);
                          		 							//updateButton1.setEnabled(true);
                          		 							saveOptionButton1.setEnabled(false);                          		 							
                          		 							// Подключение
                          		 							testOptionButton1.setEnabled(false);
                          		 							saveOptionButton2.setEnabled(false);
                          		 							// Обновления
                          		 							checkNewVersionButton.setEnabled(false);
                          		 						}
                           		 						else {
                           		 								// Цех и Участок
                           		 								updateZehSectorButton.setEnabled(true);
                           		 								updateButton1.setEnabled(true);
                           		 								if ( (spinOptionSector.isEnabled()) )saveOptionButton1.setEnabled(true);
                       		 									// Обновления
                              		 							checkNewVersionButton.setEnabled(true);
                              		 							
                           		 								// Подключение
                           		 								if (!editTextOptionServer.getText().equals(null) && !editTextOptionUsername.getText().equals(null) && !editTextOptionPassword.getText().equals(null))
                           		 								{
                           		 									saveOptionButton2.setEnabled(true);
                           		 									testOptionButton1.setEnabled(true);
                           		 									textViewTestOptionResult.setText("");
                           		 								}
                           		 								
                       		 								 }                             			
                          		 					} catch (Exception e) {
                          		 											Log.e("error in thread", e.getMessage() );
                          							  					  }
                          		 				
                          	 	            }
                          	 });  
                              }  }).start(); 
        	    }
        	
        	  }
        	});

        
        
        editTextOptionServer.addTextChangedListener(new TextWatcher() 
    	{
            public void afterTextChanged(Editable s) {
            	if (!editTextOptionServer.getText().equals(null) && !editTextOptionUsername.getText().equals(null) && !editTextOptionPassword.getText().equals(null))
            	   {
            			saveOptionButton2.setEnabled(true);
            			if ( (tglButton1.isChecked()) ) 
	 						{ 
            					testOptionButton1.setEnabled(true);
            					textViewTestOptionResult.setText("");
	 						}
            	   }
            	if (editTextOptionServer.getText().toString().equals("") || editTextOptionUsername.getText().toString().equals("")|| editTextOptionPassword.getText().toString().equals(""))
        		{
    				saveOptionButton2.setEnabled(false);
    				testOptionButton1.setEnabled(false);
    				textViewTestOptionResult.setText("");
    		 	}            	
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
          		
            }
    	}); 
    	editTextOptionUsername.addTextChangedListener(new TextWatcher() 
    	{
            public void afterTextChanged(Editable s) {
            	if (!editTextOptionServer.getText().equals(null) && !editTextOptionUsername.getText().equals(null) && !editTextOptionPassword.getText().equals(null))
            	{
        			saveOptionButton2.setEnabled(true);
        			if ( (tglButton1.isChecked()) ) 
 						{ 
        					testOptionButton1.setEnabled(true);
        					textViewTestOptionResult.setText("");
 						}
            	}
            	if (editTextOptionServer.getText().toString().equals("") || editTextOptionUsername.getText().toString().equals("")|| editTextOptionPassword.getText().toString().equals(""))
        		{
    				saveOptionButton2.setEnabled(false);
    				testOptionButton1.setEnabled(false);
    				textViewTestOptionResult.setText("");
    		 	}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            		
            }
    	}); 
    	editTextOptionPassword.addTextChangedListener(new TextWatcher() 
    	{
            public void afterTextChanged(Editable s) {
            	if (!editTextOptionServer.getText().equals(null) && !editTextOptionUsername.getText().equals(null) && !editTextOptionPassword.getText().equals(null))
            	 {
        			saveOptionButton2.setEnabled(true);
        			if ( (tglButton1.isChecked()) ) 
 						{ 
        			testOptionButton1.setEnabled(true);
        			textViewTestOptionResult.setText("");
 						}
            	 }
            	if (editTextOptionServer.getText().toString().equals("") || editTextOptionUsername.getText().toString().equals("")|| editTextOptionPassword.getText().toString().equals(""))
            		{
    					saveOptionButton2.setEnabled(false);
    					testOptionButton1.setEnabled(false);
        				textViewTestOptionResult.setText("");
        		 	}
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            	
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            		
            }
    	});     	     
   
    	// Листенер Агрегата
    	spinAggr.setOnItemSelectedListener(new OnItemSelectedListener()  
    	{ 
    		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
    			{    	
					myHandler.post(new Runnable() 
					{ 
						public void run() 
						{
							textViewDateValue.setText(" "+getCurrDate());    		   	        	
							try {
									textViewSmenaValue.setText(getShift());
								} catch (java.text.ParseException e) {}
						}
					});
    			if (!spinAggr.getSelectedItem().toString().substring(0, 1).equals("[") && !spinAggr.getSelectedItem().toString().equals(" ") && !spinAggr.getSelectedItem().toString().equals("< назад"))    				
    			{   
					final DBAdapter adap = new DBAdapter(context);
					adap.open();
					// Начальный момент времени. Корень "дерева".
					if (parentId.equals("empty")) 
						{
							// Найдем id выбранного в спиннере пункта, используя данные базы о выбранном Участке 
							String startwith = adap.getConnectParam("SECTOR");
							Cursor cursorAggrId = adap.getAggrByParent(startwith);							
							int spinAggr_posit = spinAggr.getSelectedItemPosition();
							cursorAggrId.move(spinAggr_posit-1); 
							String aggrIdValue = cursorAggrId.getString(0);														
							final String aggrNameParentValue = cursorAggrId.getString(3);
							
							String level = cursorAggrId.getString(2);
							int lvl = Integer.parseInt(level);
							// Выставим агрегат на основе выбранного уровня
							Log.e("lvl","level = "+lvl);
							if (lvl < 1)  
								{
									aggrId = "empty";
									myHandler.post(new Runnable() { 
    									public void run() { 															
															textViewChosenAggr.setText("");															
															textViewChosenAggrValue.setText("");															 
														  }
													});
								}
							else if (lvl == 1)
								{	
									aggrId = aggrIdValue;
									String spingAggrValue = spinAggr.getSelectedItem().toString();
									if (spingAggrValue.equals("[ "+aggrNameParentValue+" ]")) 
									myHandler.post(new Runnable() { 
    									public void run() { 
															// Пометка о выбранном Агрегате
															textViewChosenAggr.setText(" выбран агрегат:");
															textViewChosenAggr.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
															// Пометка выбранного Агрегата
															textViewChosenAggrValue.setText(aggrNameParentValue);
															textViewChosenAggrValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
														  }
													});
									
								}
							
							// Присвоим выбранный id как parent для следующего шага
							parentId = aggrIdValue;							
						   	ArrayList<String> arrayList = new ArrayList<String>();
						   	// Проверим является ли выбранный пункт предпоследним объектом в структуре "дерева".
						   	String answer = adap.getFSearch(parentId);
						   	Cursor cursorAggrName = adap.getAggrByParent(aggrIdValue);
						   	// Нет - не является предпоследним
						   	if (!answer.equals("true")) 
						   		{
						   			// Текущий родитель списка
						   			arrayList.add("[ "+aggrNameParentValue+" ]");
						   			arrayList.add("< назад");
						   			// Вывод списка в спиннер Агрегатов
							   		cursorAggrName.move(-1); 
							   		while (cursorAggrName.moveToNext()) 
							   		{
							   			arrayList.add(cursorAggrName.getString(3));
							   		}
    							   	final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
    								arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);	
								    // Вывод списка позиций в зависимости, от выбранного Агрегата
    								ArrayList<String> arrayListPosit = new ArrayList<String>();
					   				Cursor cursorPositName = adap.getPositByParent(parentId);
					   				arrayListPosit.add(" ");
						   			cursorPositName.move(-1);
						   			while (cursorPositName.moveToNext()) 
							   		{
							   			arrayListPosit.add(cursorPositName.getString(3));
							   		}
							   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
							   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
    								// Вывод значений в UI
    								myHandler.post(new Runnable() { 
    									public void run() {   										
    										spinPosit.setAdapter(arrayAdapterPosit);
    										spinAggr.setAdapter(arrayAdapter);
    										spinPosit.setEnabled(true);
    										spinAggr.performClick();        								   	    
    										Toast toast = Toast.makeText(context, " Продолжайте выбирать категорию позиции ", Toast.LENGTH_SHORT);  
    										toast.show();
											}
										});
						   		}
						   	// Да - является предпоследним
						   	else {	
						   		
					   			ArrayList<String> arrayListPosit = new ArrayList<String>();
					   			Cursor cursorPositName = adap.getPositByParent(parentId);
					   			arrayListPosit.add(" ");
					   			cursorPositName.move(-1);
					   			while (cursorPositName.moveToNext()) 
						   		{
						   			arrayListPosit.add(cursorPositName.getString(3));
						   		}
							   	final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
							   	arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
								// Вывод значений в UI
					   			myHandler.post(new Runnable() { 
					   				public void run() {
										
					   					spinPosit.setAdapter(arrayAdapterPosit);
					   			    	spinPosit.setEnabled(true);							   	    
					   					Toast toast = Toast.makeText(context, " Выставлена конечная категория ", Toast.LENGTH_SHORT);  
					   				//	toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
					   					toast.show();
					   					}
					   				});
					   			// Подготовим возвращение, выставим parent выбранного 
					   			String aggrIdValue2 = adap.getParentById(parentId);
					   			parentId = aggrIdValue2;
					   			getAggrTip(context, false);
					   			
						   		 	}
							cursorAggrName.close();
							// Меняем совет пользователю на панели регистрации
							//setSpinnerTips(context, false);
						}
					// Продолжаем подниматься от корня дерева к листьям
					else {
							// Найдем id выбранного в спиннере пункта на основе ранее выбранного parent
							Cursor cursorAggrId = adap.getAggrByParent(parentId);
							int spinAggr_posit = spinAggr.getSelectedItemPosition();
							cursorAggrId.move(spinAggr_posit-1); 
							String aggrIdValue = cursorAggrId.getString(0);
							final String aggrNameParentValue = cursorAggrId.getString(3);
							
							String level = cursorAggrId.getString(2);
							int lvl = Integer.parseInt(level);
							// Выставим агрегат на основе выбранного уровня
							Log.e("lvl","level = "+lvl);
							if (lvl < 1)  
								{
									aggrId = "empty";
									myHandler.post(new Runnable() { 
    									public void run() { 															
															textViewChosenAggr.setText("");															
															textViewChosenAggrValue.setText("");															 
														  }
													});
								}
							else if (lvl == 1)
								{	
									aggrId = aggrIdValue;
									String spingAggrValue = spinAggr.getSelectedItem().toString();
									if (spingAggrValue.equals("[ "+aggrNameParentValue+" ]")) 
									myHandler.post(new Runnable() { 
    									public void run() { 
															// Пометка о выбранном Агрегате
															textViewChosenAggr.setText(" выбран агрегат:");
															textViewChosenAggr.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
															// Пометка выбранного Агрегата
															textViewChosenAggrValue.setText(aggrNameParentValue);
															textViewChosenAggrValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
														  }
													});
								}
							
							// Присвоим выбранный id как parent для следующего шага
							parentId = aggrIdValue;							
							ArrayList<String> arrayList = new ArrayList<String>();
							// Проверим является ли выбранный пункт предпоследним объектом в структуре "дерева".
							String answer = adap.getFSearch(parentId);
							Cursor cursorAggrName = adap.getAggrByParent(aggrIdValue);
							// Нет - не является предпоследним
							if (!answer.equals("true")) 
							   		{
										// // Текущий родитель списка
										arrayList.add("[ "+aggrNameParentValue+" ]");
    							   		arrayList.add("< назад");
    							   		// Вывод списка в спиннер Агрегатов
    							   		cursorAggrName.move(-1); 
    							   		while (cursorAggrName.moveToNext()) 
    							   		{
    							   			arrayList.add(cursorAggrName.getString(3));
    							   		}
        							   	final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
        								arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);	
        								// Вывод списка в спиннер Позиций   								  
        								ArrayList<String> arrayListPosit = new ArrayList<String>();
						   				Cursor cursorPositName = adap.getPositByParent(parentId);
						   				arrayListPosit.add(" ");
							   			cursorPositName.move(-1);
							   			while (cursorPositName.moveToNext()) 
    							   		{
    							   			arrayListPosit.add(cursorPositName.getString(3));
    							   		}
    							   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
    							   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);	
    							   		// Вывод значений в UI
        								myHandler.post(new Runnable() { 
        									public void run() {
        										spinPosit.setAdapter(arrayAdapterPosit);
        										spinPosit.setEnabled(true);
        										spinAggr.setAdapter(arrayAdapter);
        										spinAggr.performClick();        								   	    
        										Toast toast = Toast.makeText(context, " Продолжайте выбирать категорию позиции ", Toast.LENGTH_SHORT);  
        										toast.show();
        										getAggrTip(context, false);
    											}
    										});
							   		}
								// Да - является предпоследним
							   	else {		
							   			// Просто позволим спиннеру выбрать пункт
							   			ArrayList<String> arrayListPosit = new ArrayList<String>();
							   			Cursor cursorPositName = adap.getPositByParent(parentId);
							   			arrayListPosit.add(" ");
							   			cursorPositName.move(-1);
							   			while (cursorPositName.moveToNext()) 
    							   		{
    							   			arrayListPosit.add(cursorPositName.getString(3));
    							   		}
        							   	final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
        							   	arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
        								// Вывод значений в UI
							   			myHandler.post(new Runnable() { 
							   				public void run() {
							   					spinPosit.setAdapter(arrayAdapterPosit);
							   			    	spinPosit.setEnabled(true);							   	    
							   					Toast toast = Toast.makeText(context, " Выставлена конечная категория ", Toast.LENGTH_SHORT);  
							   			//		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
							   					toast.show();
							   					getAggrTip(context, false);
							   					}
							   				});
							   			// Подготовим возвращение вниз по дереву, выставим parent выбранного 
							   			String aggrIdValue2 = adap.getParentById(parentId);
							   			parentId = aggrIdValue2;
							   		//	setSpinnerTips(context, false);
							   		}
								cursorAggrName.close();
						 }
					adap.close();
				}
    	// Для возвращения вниз по дереву, к более ранним пунктам меню
		else if (spinAggr.getSelectedItem().toString().equals("< назад")) 
		{
			final DBAdapter adap = new DBAdapter(context);
			adap.open();
			String startwith = adap.getConnectParam("SECTOR"); 
			String aggrIdValue = adap.getParentById(parentId);
			final String aggrNameParentValue = adap.getParentNameById(aggrIdValue);	
			
			int lvl = adap.getLevelById(aggrIdValue);
			Log.e("lvl для <назад ","level = "+Integer.toString(lvl));
			if (lvl < 1)  
				{
					aggrId = "empty";
					myHandler.post(new Runnable() { 
						public void run() { 															
											textViewChosenAggr.setText("");															
											textViewChosenAggrValue.setText("");															 
										  }
									});
				}
			else if (lvl == 1)
				{	
					aggrId = aggrIdValue;
					String spingAggrValue = spinAggr.getSelectedItem().toString();
					if (spingAggrValue.equals("[ "+aggrNameParentValue+" ]")) 
					myHandler.post(new Runnable() { 
						public void run() { 
											// Пометка о выбранном Агрегате
											textViewChosenAggr.setText(" выбран агрегат:");									
											// Пометка выбранного Агрегата
											textViewChosenAggrValue.setText(aggrNameParentValue);
											textViewChosenAggrValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
										  }
									});
				}
			
			parentId = aggrIdValue;
		   	// Проверим не оказались ли мы у корней
			// Нет, не оказались - можно двигаться дальше
			if (!startwith.equals(aggrIdValue))
				{
					// Вывод списка в спиннер Агрегатов
					ArrayList<String> arrayList = new ArrayList<String>();
					Cursor cursorAggrName = adap.getAggrByParent(aggrIdValue);															
					arrayList.add("[ "+aggrNameParentValue+" ]");
					arrayList.add("< назад");	    							   	            							   		
					cursorAggrName.move(-1); 
					while (cursorAggrName.moveToNext()) 
					{
	   					arrayList.add(cursorAggrName.getString(3));
					}
					final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
					arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);	
					// Вывод списка в спиннер Позиций  
					ArrayList<String> arrayListPosit = new ArrayList<String>();
	   				Cursor cursorPositName = adap.getPositByParent(parentId);
	   				arrayListPosit.add(" ");
		   			cursorPositName.move(-1);
		   			while (cursorPositName.moveToNext()) 
			   		{
			   			arrayListPosit.add(cursorPositName.getString(3));
			   		}
			   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
			   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
			   		cursorPositName.close();
			   		cursorAggrName.close();	
			   		// Вывод значений в UI
					myHandler.post(new Runnable() { 
						public void run() {
							spinPosit.setAdapter(arrayAdapterPosit);
							spinPosit.setEnabled(true);
							spinAggr.setAdapter(arrayAdapter);
							spinAggr.performClick();        								   	    
							Toast toast = Toast.makeText(context, " Продолжайте выбирать категорию позиции ", Toast.LENGTH_SHORT);  
							toast.show();
						//	setSpinnerTips(context, false);
							}
						});
					
				}
			// Да, мы у корней - перестроим список спиннера под изначальный вид
			else 
				{				
					setAggrSpinner(adap, myHandler, startwith); 
		   			// Выставление значений
		   			myHandler.post(new Runnable() { 
		   				public void run() {
		   					spinAggr.performClick(); 
		   					Toast toast = Toast.makeText(context, " Вернулись в начало ", Toast.LENGTH_SHORT);  
		   					toast.show();
		   					}
		   				});
		   			// Вернем parent к изначальному состоянию
		   			parentId = "empty";
				}
			adap.close();
		}
		else if (spinAggr.getSelectedItem().toString().equals(" ")) 
		{			
			aggrId = "empty";
			myHandler.post(new Runnable() { 
				public void run() { 															
									textViewChosenAggr.setText("");															
									textViewChosenAggrValue.setText("");															 
								  }
							});
			
			// Вывод списка в спиннер Позиций  
			ArrayList<String> arrayListPosit = new ArrayList<String>();
			arrayListPosit.add(" ");
	   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
	   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
	   		// Вывод значений в UI
			myHandler.post(new Runnable() { 
				public void run() {
					
						getAggrTip(context, true);
						spinPosit.setAdapter(arrayAdapterPosit);
						spinPosit.setEnabled(false);
						
				}
			});
			
		}
		else if (spinAggr.getSelectedItem().toString().substring(0, 1).equals("[")) 
		{			
	   		// Вывод значений в UI
			myHandler.post(new Runnable() { 
				public void run() {
					final DBAdapter adap = new DBAdapter(context);
					adap.open();
					
					if (parentId.equals("empty"))
					{
						
						aggrId = "empty";
						myHandler.post(new Runnable() { 
							public void run() { 															
												textViewChosenAggr.setText("");															
												textViewChosenAggrValue.setText("");															 
											  }
										});
						
						// Найдем id выбранного в спиннере пункта, используя данные базы о выбранном Участке 
						String startwith = adap.getConnectParam("SECTOR");					
						// Вывод списка в спиннер Позиций   								  
						ArrayList<String> arrayListPosit = new ArrayList<String>();
		   				Cursor cursorPositName = adap.getPositByParent(startwith);
		   				arrayListPosit.add(" ");
			   			cursorPositName.move(-1);
			   			while (cursorPositName.moveToNext()) 
				   		{
				   			arrayListPosit.add(cursorPositName.getString(3));
				   		}
				   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
				   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
				   		
				   		// Вывод значений в UI
						myHandler.post(new Runnable() { 
							public void run() {
									spinPosit.setAdapter(arrayAdapterPosit);
									getAggrTip(context, false);
									spinPosit.setAdapter(arrayAdapterPosit);
									spinPosit.setEnabled(true);
							}
						});
						cursorPositName.close();
						
					}
					else
					{
						
						int lvl = adap.getLevelById(parentId);
						Log.e("lvl для [ ","level = "+Integer.toString(lvl));
						if (lvl < 1)  
							{
								aggrId = "empty";
								myHandler.post(new Runnable() { 
									public void run() { 															
														textViewChosenAggr.setText("");															
														textViewChosenAggrValue.setText("");															 
													  }
												});
							}
						else if (lvl == 1)
							{	
								aggrId = parentId;
								final String aggrNameParentValue = adap.getParentNameById(parentId);
								String spingAggrValue = spinAggr.getSelectedItem().toString();
								if (spingAggrValue.equals("[ "+aggrNameParentValue+" ]")) 
								myHandler.post(new Runnable() { 
									public void run() { 
														// Пометка о выбранном Агрегате
														textViewChosenAggr.setText(" выбран агрегат:");
														textViewChosenAggr.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
														// Пометка выбранного Агрегата
														textViewChosenAggrValue.setText(aggrNameParentValue);
														textViewChosenAggrValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT); 
													  }
												});
							}
						
						// Вывод списка в спиннер Позиций   								  
						ArrayList<String> arrayListPosit = new ArrayList<String>();
		   				Cursor cursorPositName = adap.getPositByParent(parentId);
		   				arrayListPosit.add(" ");
			   			cursorPositName.move(-1);
			   			while (cursorPositName.moveToNext()) 
				   		{
				   			arrayListPosit.add(cursorPositName.getString(3));
				   		}
				   		final ArrayAdapter<String> arrayAdapterPosit = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPosit);
				   		arrayAdapterPosit.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
				   		
				   		// Вывод значений в UI
						myHandler.post(new Runnable() { 
							public void run() {
									spinPosit.setAdapter(arrayAdapterPosit);
									getAggrTip(context, false);
									spinPosit.setAdapter(arrayAdapterPosit);
									spinPosit.setEnabled(true);
							}
						});
						cursorPositName.close();
					}

						adap.close();		
				}
			});
			
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) 
		{
		
		};
	
   });

   	
	// Выбор Цеха и Участка
	spinOptionZeh.setOnItemSelectedListener(new OnItemSelectedListener()  
   { 
		
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{    			
			if (!spinOptionZeh.getSelectedItem().toString().equals(" ") && spinOptionZeh.getCount()>1)
					{    						
							int spinOptionZeh_posit = spinOptionZeh.getSelectedItemPosition();  
							setSectorSpinner(context, spinOptionZeh_posit);
							spinOptionSector.setEnabled(true);    		    										
					}
			else if(spinOptionZeh.getCount()>1) {
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(" ");
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
		   		arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
				spinOptionSector.setAdapter(arrayAdapter);     				
				spinOptionSector.setEnabled(false);
				saveOptionButton1.setEnabled(false);
				//setEmptySpinner(context, spinObj);
				//setEnableInterfaces(false);
				 }
		}
		public void onNothingSelected(AdapterView<?> arg0) 
		{
			
		};
   });
   
   // События при работе со спиннером "Позиция"
   spinPosit.setOnItemSelectedListener(new OnItemSelectedListener()  
   { 

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{   
			myHandler.post(new Runnable() { 
			public void run() {
				textViewDateValue.setText(" "+getCurrDate());    		   	        	
				try {
					textViewSmenaValue.setText(getShift());
						} catch (java.text.ParseException e) {}
							}
					});
			if (!spinPosit.getSelectedItem().toString().equals(" ") && spinPosit.getCount()>1)
					{    						
					// Заполнение спиннера "Неисправная часть"
					// Вывод списка позиций в зависимости, от выбранной категории
					DBAdapter adap = new DBAdapter(context);
					adap.open();
					ArrayList<String> arrayListPart = new ArrayList<String>();
	   				Cursor cursorPart = adap.getPartCursor();
	   				arrayListPart.add(" ");
	   				cursorPart.move(-1);
		   			while (cursorPart.moveToNext()) 
			   		{
		   				arrayListPart.add(cursorPart.getString(3));
			   		}
			   		final ArrayAdapter<String> arrayAdapterPart = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPart);
			   		arrayAdapterPart.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
					// Вывод значений в UI
					myHandler.post(new Runnable() { 
						public void run() {
							spinPart.setAdapter(arrayAdapterPart);
						//	cameraButton.setVisibility(View.VISIBLE);
			   	    	// 	photoButton.setVisibility(View.VISIBLE);
							setEnableInterfaces(true);
							spinPart.setEnabled(true);
							// Камера
							TextView imagesNumber = (TextView) findViewById(R.id.textView2);
							imagesNumber.setText("0");
							imagesNumber.setTextColor(Color.parseColor("#0054A8"));
							cameraButton.setEnabled(true);
							photoButton.setEnabled(false);
							
							}
						});
					
					// Очистка временных фотографий
					try {
						int delnum = deleteImages(context, adap);						
						if (delnum > 0)
			    	    Toast.makeText(context, 
			    	    		" Временные фото были удалены. Количество: "+Integer.toString(delnum),Toast.LENGTH_LONG).show();
		   	    	    }
		   	    	    catch (Exception ex)
		   	    	    {
		   	    	    	Log.d("images_link", ex.getMessage());
		   	    	    }
					
					adap.close();
					cursorPart.close();
					}
			else { 
				
   	    	    // Очистка временных фотографий
				DBAdapter adap = new DBAdapter(context);				
				adap.open();
   	    	    try {
   	    	    	int delnum = deleteImages(context, adap);   	    	    	
   	    	    	if (delnum > 0)
   	    	    		Toast.makeText(context, 
	    	    		" Временные фото были удалены. Количество: "+Integer.toString(delnum),Toast.LENGTH_LONG).show();
   	    	    }
   	    	    catch (Exception ex)
   	    	    {
   	    	    	Log.d("images_link", ex.getMessage());
   	    	    }
   	    	    adap.close();
				
				myHandler.post(new Runnable() { 
				public void run() {
					textViewDateValue.setText(" "+getCurrDate());    		   	        	
					try {
						photoButton.setEnabled(false);
						textViewSmenaValue.setText(getShift());						
						} catch (java.text.ParseException e) {}
								}
						});
				//saveOptionButton1.setEnabled(false);
			//	getAggrTip(context, false);
				spinPart.setEnabled(false);
				setEnableInterfaces(false);
				
				ArrayList<String> arrayListPart = new ArrayList<String>();
   				arrayListPart.add(" ");
		   		final ArrayAdapter<String> arrayAdapterPart = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPart);
		   		arrayAdapterPart.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
				// Вывод значений в UI
				myHandler.post(new Runnable() { 
					public void run() {
						spinPart.setAdapter(arrayAdapterPart);
						saveButton.setEnabled(false);
					//	saveButton2.setEnabled(false);
						}
					});				
				
				 }
		}
		public void onNothingSelected(AdapterView<?> arg0) 
		{			
		};
   });	
   
   // События при работе со спиннером "Неисправная часть"
   spinPart.setOnItemSelectedListener(new OnItemSelectedListener()  
   { 

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{    			
			if (!spinPart.getSelectedItem().toString().equals(" ") && spinPart.getCount()>1)
					{
					myHandler.post(new Runnable() { 
						public void run() {
							textViewDateValue.setText(" "+getCurrDate());    		   	        	
							try {
								textViewSmenaValue.setText(getShift());
							} catch (java.text.ParseException e) {}
								}
						});
					//	saveOptionButton1.setEnabled(true);

					// Заполнение спиннера "Неисправная часть"
					// Вывод списка позиций в зависимости, от выбранного Агрегата
					final DBAdapter adap = new DBAdapter(context);
					adap.open();
	   				Cursor cursorPart = adap.getPartCursor();
	   					int spinPart_posit = spinPart.getSelectedItemPosition();	   				
	   					cursorPart.move(spinPart_posit); 
	   					String partIdValue = cursorPart.getString(0);
	   					Log.d("partIdValue value", partIdValue ); 
					cursorPart.close();
					
					ArrayList<String> arrayListEquip = new ArrayList<String>();
					//String aggrNameParentValue = cursorAggrId.getString(3);
					Cursor cursorEquip = adap.getEquipByPart(partIdValue);
						arrayListEquip.add(" ");
						cursorEquip.move(-1);
						while (cursorEquip.moveToNext()) 
						{
							arrayListEquip.add(cursorEquip.getString(3));
							Log.d("cursorEquip3 value", cursorEquip.getString(3) );
						}
						final ArrayAdapter<String> arrayAdapterEquip = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListEquip);
						arrayAdapterEquip.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
					// Вывод значений в UI
						myHandler.post(new Runnable() { 
							public void run() {
								spinEquip.setAdapter(arrayAdapterEquip);
								spinEquip.setEnabled(true);
								}
							});
					adap.close();
					cursorEquip.close();
					}
			else { 
				myHandler.post(new Runnable() { 
				public void run() {
					textViewDateValue.setText(" "+getCurrDate());    		   	        	
					try {
						textViewSmenaValue.setText(getShift());
							} catch (java.text.ParseException e) {}
								}
						});
       			
				ArrayList<String> arrayListEquip = new ArrayList<String>();
   				arrayListEquip.add(" ");
		   		final ArrayAdapter<String> arrayAdapterEquip = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListEquip);
		   		arrayAdapterEquip.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
				// Вывод значений в UI
				myHandler.post(new Runnable() { 
					public void run() {
						spinEquip.setAdapter(arrayAdapterEquip);
						spinEquip.setEnabled(false);
						saveButton.setEnabled(false);
					//	saveButton2.setEnabled(false);
						}
					});
				
				 }
		}
		public void onNothingSelected(AdapterView<?> arg0) 
		{			
		};
   });	
   

   // События при работе со спиннером "Оборудование"
   spinEquip.setOnItemSelectedListener(new OnItemSelectedListener()  
   { 

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{    			
			if (!spinEquip.getSelectedItem().toString().equals(" ") && spinEquip.getCount()>1)
					{
					myHandler.post(new Runnable() { 
					public void run() {
						textViewDateValue.setText(" "+getCurrDate());    		   	        	
						try {
							textViewSmenaValue.setText(getShift());
								} catch (java.text.ParseException e) {}
									}
							});
					//	saveOptionButton1.setEnabled(true);

					// Заполнение спиннера "Неисправная часть"
					// Вывод списка позиций в зависимости, от выбранного Агрегата
					final DBAdapter adap = new DBAdapter(context);
					adap.open();
	   				Cursor cursorPart = adap.getPartCursor();
	   					int spinPart_posit = spinPart.getSelectedItemPosition();	   				
	   					cursorPart.move(spinPart_posit); 
	   					String partIdValue = cursorPart.getString(0);
	   					Log.d("partIdValue", partIdValue ); 
					cursorPart.close();
					Cursor cursorEquip = adap.getEquipByPart(partIdValue);
						int spinEquip_posit = spinEquip.getSelectedItemPosition();	   				
						cursorEquip.move(spinEquip_posit); 
						String equipIdValue = cursorEquip.getString(0);
						Log.d("equipIdValue", equipIdValue );
					cursorEquip.close();
						
					ArrayList<String> arrayListCause = new ArrayList<String>();
					Cursor cursorCause = adap.getCauseByEquip(equipIdValue);
					arrayListCause.add(" ");
					cursorCause.move(-1);
					while (cursorCause.moveToNext()) 
						{
							arrayListCause.add(cursorCause.getString(3));
							//arrayListCause.add(cursorCause.getString(3));
							Log.d("cursorCause(3) value", cursorCause.getString(0)+" "+cursorCause.getString(3) );
						}
					final ArrayAdapter<String> arrayAdapterCause = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListCause);
					arrayAdapterCause.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
					// Вывод значений в UI
					myHandler.post(new Runnable() { 
						public void run() {
							spinCause.setAdapter(arrayAdapterCause);
							spinCause.setEnabled(true);								
							}
							});
					adap.close();
					cursorCause.close();
					}
			else {
       			textViewDateValue.setText(" "+getCurrDate());    		   	        	
       			try {
       				textViewSmenaValue.setText(getShift());
       			} catch (java.text.ParseException e) {}
				ArrayList<String> arrayListCause = new ArrayList<String>();
				arrayListCause.add(" ");
		   		final ArrayAdapter<String> arrayAdapterCause = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListCause);
		   		arrayAdapterCause.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
				// Вывод значений в UI
				myHandler.post(new Runnable() { 
					public void run() {
						spinCause.setAdapter(arrayAdapterCause);
						spinCause.setEnabled(false);
						saveButton.setEnabled(false);
				//		saveButton2.setEnabled(false);
						}
					});
				
				 }
		}
		public void onNothingSelected(AdapterView<?> arg0) 
		{			
		};
   });	
   
   // События при работе со спиннером "Причина"
   spinCause.setOnItemSelectedListener(new OnItemSelectedListener()  
   { 
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{   
			myHandler.post(new Runnable() { 
				public void run() {
   			textViewDateValue.setText(" "+getCurrDate());    		   	        	
   			try {
   				textViewSmenaValue.setText(getShift());
   			} catch (java.text.ParseException e) {}
				}
			});
   			// Если выбрана причина
			if (!spinCause.getSelectedItem().toString().equals(" ") && spinCause.getCount()>1)
					{   						
						saveButton.setEnabled(true);
						saveButton2.setEnabled(true);
					}
			else 
				{    				
					saveButton.setEnabled(false);
					saveButton2.setEnabled(false);					
	
				 }
		}
		public void onNothingSelected(AdapterView<?> arg0) 
		{			
		};
   });	
   
  // Выбор Цеха и Участка
  spinOptionSector.setOnItemSelectedListener(new OnItemSelectedListener()  
        { 

    		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
    		{    			
    			if (!spinOptionSector.getSelectedItem().toString().equals(" ") && spinOptionSector.getCount()>1)
    					{    						
    						saveOptionButton1.setEnabled(true);
    					}
    			else {    				
    				saveOptionButton1.setEnabled(false);
    				 }
    		}
    		public void onNothingSelected(AdapterView<?> arg0) 
    		{    			
    		};
        });	
  /*
  	rowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
  		public void onCheckedChanged(CompoundButton buttonView,
  				boolean isChecked) 
  				{
      				if (isChecked) deleteDefButton.setVisibility(View.VISIBLE);
      				else deleteDefButton.setVisibility(View.GONE);
                }
  	});
  	*/
/*
	listViewJour.setOnItemClickListener(new OnItemClickListener() 
	{   			
	    public void onItemClick(AdapterView<?> parent, View view,
  	        int position, long id) 
	    	{
	    		if (listViewJour.getCheckedItemCount()>0) 
	    		{
	    			//if(listViewJour.isItemChecked(position)) 	    		
	    				deleteDefButton.setVisibility(View.VISIBLE);
	    		}
	    		else 
	    		{
	    			deleteDefButton.setVisibility(View.GONE);
	    		}    	    		
	    	}
	});
	*/
	try {
			new Thread(new Runnable() {
	   	    	public void run() {		
	   	    							try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {}
	   	        						DBAdapter adap = new DBAdapter(context);
	   	        						adap.open();
	   	        						Thread loopThread = new LoopingThread();	
	   	        						CheckVersion(myHandler, adap, context, loopThread);
	   	        						adap.close();
	   	        					
	   	     						}  }).start();
		}
		catch (Exception e)
		{
			Log.e("check on start", e.getMessage());
		}
	
    }
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // запишем в лог значения requestCode и resultCode
        Log.d("getMyDataBack", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        
       	
       	
        if (requestCode == CAMERA)
        {
        	// если пришло ОК
        	if (resultCode == RESULT_OK) 
        	{        	
        		String extraData = data.getStringExtra("images");
        		ImageButton cameraButton = (ImageButton)findViewById(R.id.imageButton1);
        		ImageButton photoButton = (ImageButton)findViewById(R.id.imageButton2);
        		TextView imagesNum = (TextView)findViewById(R.id.textView2);
        		
        		if (Integer.parseInt(extraData) > 0)
        		{        		
        			imagesNum.setText(extraData);
        			photoButton.setEnabled(true);
        			
        			if (Integer.parseInt(extraData) >= 5) 
        				{ imagesNum.setTextColor(Color.RED); cameraButton.setEnabled(false); }
        		}
        		else if (Integer.parseInt(extraData) == 0)
        		{
        			imagesNum.setText(extraData);
        			photoButton.setEnabled(false);
        			
        		}
        	
        	
        	} 
        	else {
    				//Toast.makeText(this, "Произошла ошибка в окне камеры!", Toast.LENGTH_SHORT).show();
    				DBAdapter adap = new DBAdapter(FirstDBActivity.this);
    				TextView imagesNumber = (TextView) findViewById(R.id.textView2);
					adap.open();
		 	    	String def_id = adap.getNextId("defects");
		 	    	if (def_id == null) def_id = "1";
		 	    	
		 	    	String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id;
		 	    	File dir = new File(link);
		 	    	if (dir.exists())
		 	    	{
		 	    	String[] list = dir.list();
		 	    	int number = list.length;		 	    	
		 	    	imagesNumber.setText(Integer.toString(number));
		 	    	}
		 	    	else {imagesNumber.setText("0");}
		 	    	adap.close();
		 	    	
		 	    	
        		 }
        }
        else if (requestCode == GALLERY)        	
        		{        	
        			//Toast.makeText(this, "Вернулись в активити", Toast.LENGTH_SHORT).show();
        			
        	        ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);        
        	        ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
        	        
    				TabHost tabs=(TabHost)findViewById(R.id.tabhost);       
    				tabs.setup();  
    				int i = tabs.getCurrentTab();    				
    				if (i == 0) 
    				        	   {
       									// Количество временных фото
    									DBAdapter adap = new DBAdapter(FirstDBActivity.this);
    	   	        					adap.open();
    	   	        					
    	   	        					String def_id_2 = adap.getNextId("defects");
    	   	        					if (def_id_2 == null) def_id_2 = "1";
    	   	        					
    	   	        					String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id_2;
    	   	        					File dir = new File(link);
    	   	        					String[] list = dir.list();
    	   	        					final int number = list.length;
    	   	        					adap.close();
    	   	        					   	   	        					
    	   	        					TextView imagesNumber = (TextView) findViewById(R.id.textView2);
    	   	        					ImageButton cameraButton = (ImageButton) findViewById(R.id.imageButton1);
    	   	        					ImageButton photoButton = (ImageButton) findViewById(R.id.imageButton2);
    	   	        					
    	   	        					imagesNumber.setText(Integer.toString(number));
    	   	        					
    	   	        					if ((number < 5) && (number > 0)) 
	   	        						{    	   	        			 	    	  	    	
    	   	        						imagesNumber.setTextColor(Color.parseColor("#0054A8")); 
    	   	        						cameraButton.setEnabled(true);
	   	        			 	    	}
    	   	        					else if (number == 0) 
   	        							{  
    	   	        						imagesNumber.setTextColor(Color.parseColor("#0054A8"));
    	   	        						cameraButton.setEnabled(true);
    	   	        						photoButton.setEnabled(false); 
   	        							}
    				        	   }
    				if (i == 1) 
    				        	   {
    								//	Toast.makeText(this, "Вернулись в активити", Toast.LENGTH_SHORT).show();
    					
    					 				int menuPosition = listViewMenu.getCheckedItemPosition();
    									// Вывод значений в UI
    									listViewMenu.performItemClick(listViewMenu.getAdapter().getView(menuPosition, null, null), 0, 0);
    				        	   }

    				      
        		}
        
      }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	super.onPrepareDialog(id, dialog);

    	Date currentTime = new Date();
    	dialog.setTitle("Внимание!");
    }
    
    protected Dialog onCreateDialog(int id) {
    //	setTitle("loading!");
    	return DialogFactory.getDialogById(id, progressText, this, onClickWIFIListener);
    }    
    private void initTabsAppearance(TabWidget tab_widget) 
    {
        // Цвета панели вкладок
        for(int i=0; i<tab_widget.getChildCount(); i++)
            tab_widget.getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
        // Высота панели вкладок
        //	tab_widget.getChildAt(0).getLayoutParams().height =62;
        //	tab_widget.setDividerDrawable(R.drawable.divider);
    }   
    
   
    // 
    private BroadcastReceiver WifiStateChangedReceiver = new BroadcastReceiver()
    {
    	public void onReceive(Context context, Intent intent) 
    	{		
    		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);	
    		ToggleButton tglButton1 = (ToggleButton)findViewById(R.id.toggleButton1);
    		ToggleButton tglButton2 = (ToggleButton)findViewById(R.id.toggleButton2);
    		
    		int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
    			     WifiManager.WIFI_STATE_UNKNOWN);
    			   
    			   switch(extraWifiState)
    			   {
    			   		case WifiManager.WIFI_STATE_DISABLED:
    			   			tglButton1.setChecked(false);
    			   			tglButton2.setChecked(false);
    			   			break;
    			   		case WifiManager.WIFI_STATE_DISABLING:
    			   			tglButton1.setChecked(false);
    			   			tglButton2.setChecked(false);
    			   			break;
    			   		case WifiManager.WIFI_STATE_ENABLED:
    			   			tglButton1.setChecked(true);
    			   			tglButton2.setChecked(true);
    			   			break;
    			   		case WifiManager.WIFI_STATE_ENABLING:
    			   			tglButton1.setChecked(true);
    			   			tglButton2.setChecked(true);
    			   			break;
    			   		case WifiManager.WIFI_STATE_UNKNOWN:
    			   			tglButton1.setChecked(false);
    			   			tglButton2.setChecked(false);
    			   			break;
    			   }
    	}	
    };
    
    // ВКЛ / ВЫКЛ Wi-Fi модуль
	DialogInterface.OnClickListener onClickWIFIListener = new DialogInterface.OnClickListener() 
	{		
		public void onClick(DialogInterface dialogArg, int which) 
		{
		Context context = getApplicationContext();
		Button syncButton = (Button)findViewById(R.id.SyncButton);
		Button syncButton2 = (Button)findViewById(R.id.SyncButton2);
		Button updateButton1 = (Button)findViewById(R.id.UpdateButton1);
		Button updateZehSectorButton = (Button)findViewById(R.id.UpdateZehSectorButton);		
		final DBAdapter adap = new DBAdapter(context);   	    	 	
   	 	adap.open();
   	 	ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);
   	 	int noSendDefectsCount = adap.getNoSendDefectsCount(0);
		// если выкл
			if (which == -1)
			{
				 WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				 wm.setWifiEnabled(false);  
			}
		// если вкл
			else
			{
				ToggleButton tglButton1 = (ToggleButton)findViewById(R.id.toggleButton1);
				ToggleButton tglButton2 = (ToggleButton)findViewById(R.id.toggleButton2);
				tglButton1.setChecked(true);
				tglButton2.setChecked(true);
				dialogArg.dismiss();
				// кнопка "Синхронизация"
   	        	if ( (noSendDefectsCount > 0) ) 
	        		{
   						if ( syncButton.isEnabled() ) syncButton.setEnabled(true);
   						if ( syncButton2.isEnabled() ) syncButton2.setEnabled(true);
	        		}

			}	
		adap.close();
		}		
	}; 

	private String getCurrDate()
	{		
		long msTime = System.currentTimeMillis();  
		Date curDateTime = new Date(msTime);
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm");  
		String formattedDateString = formatter.format(curDateTime); 
		return formattedDateString;
	}
	
	private void setSpinners(DBAdapter adap, Handler myHandler, final Spinner spinAggr) 
	{	

	};
	
	private void setAggrSpinner(DBAdapter adap, Handler myHandler, String startwith)
	{
		
		final Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);	
   	    ArrayList<String> arrayList = new ArrayList<String>();
   	    
		String zehid = adap.getConnectParam("ZEH");
		String sectorid = adap.getConnectParam("SECTOR");   
		final String zeh = adap.getZehSectorName(zehid);
		final String sector = adap.getZehSectorName(sectorid);
		
   	    arrayList.add(" ");
   	    arrayList.add("[ "+zeh+" "+sector+" ]");
   	    Cursor cursorAggrName = adap.getAggrByParent(startwith);
   	    cursorAggrName.move(-1); 
   	    while (cursorAggrName.moveToNext()) 
   	    {
   	       	arrayList.add(cursorAggrName.getString(3));
   	    }       	        
   	    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
	   	//arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);	    		   		    		  
   	    arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
		// Выставление значений
	        myHandler.post(new Runnable() { 
			public void run() {
	   	        	spinAggr.setAdapter(arrayAdapter);	        		
				}
			});		
	}
	
	private void getAggrTip(Context context, Boolean aggr)
	{
		final TextView textViewSpinnerTip0 = (TextView) findViewById(R.id.TextViewSpinnerTip0);
		final TextView textViewSpinnerTip2 = (TextView) findViewById(R.id.TextViewSpinnerTip2);
		
		if (aggr)
		{
			textViewSpinnerTip0.setText(" 1. ");
			textViewSpinnerTip2.setText("Агрегат");
		}
		else 
		{
			textViewSpinnerTip0.setText(" 2. ");
			textViewSpinnerTip2.setText("Позицию");
		}
	}
	
	/*
	private void setObjSpinner(Context context, int spinSector_posit, int spinAggr_posit)
	{
		final DBAdapter adap = new DBAdapter(context);
		final Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
		final Spinner spinObj = (Spinner) findViewById(R.id.SpinObj);
		
		adap.open();
		ArrayList<String> arrayList = new ArrayList<String>();
   	    arrayList.add(" ");
   	    int spinSector_id = 0;
   	    int aggr_id = 0;
   	    int obj_id = 0;
   	    try 
   	    {
   	    	spinSector_id = adap.getSectorId(spinSector_posit);		
   	    	aggr_id = adap.getAggrId(spinSector_id, spinAggr_posit);
   	    	obj_id = adap.getSpinObjId(aggr_id);
   	    	int i = adap.getSpinObjId(aggr_id);
	    
   	    	while (i<obj_id+adap.getObjCount(aggr_id))
   	    		{
   	    		arrayList.add(adap.getSpinObjValues(i));            	   			
   	    		i++;
   	    		}    	   	
   	    }
   	    catch  (Exception ex) {
   	 	Toast toast_spinid = Toast.makeText(context, "Для данного агрегата объект ремонта не найден", Toast.LENGTH_LONG);  
   	 	toast_spinid.show();
	    }	   		
	        
   	    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
   		arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);   		
  			    					   	 
		spinObj.setAdapter(arrayAdapter);
			
		adap.close();
			
	}
	*/
	
	//* Меню Настройка
	// Вывод данных Участок
	private void setSectorSpinner(Context context, int spinOptionZeh_posit)
	{
		final DBAdapter adap = new DBAdapter(context);
		final Spinner spinOptionSector = (Spinner) findViewById(R.id.SpinOptionSector);
		adap.open();
		final String server = adap.getConnectParam("server");
		final String username = adap.getConnectParam("username");
		String cryptpassword = adap.getConnectParam("password");
		final String password = Decrypt(cryptpassword);
		final Handler myHandler = new Handler();		
		Cursor cursorid = adap.getZehCursor();
		cursorid.move(spinOptionZeh_posit);
		Log.d("value", cursorid.getString(0) );
		Cursor cursor = adap.getSectorByZeh(cursorid.getString(0));    											
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(" ");
		cursor.move(-1); 
		while (cursor.moveToNext()) 
		    {
				arrayList.add(cursor.getString(3));	
		    }  
   		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
   		arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
		spinOptionSector.setAdapter(arrayAdapter);  
		adap.close();
		
	}
	
	private void setEmptySpinner(Context context, Spinner spinner)
	{
		
		ArrayList<String> arrayList = new ArrayList<String>();
//   	    arrayList.add(" ");
   	    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
		arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
		spinner.setAdapter(arrayAdapter);
	}
	
    public void setListViewItems(final int pos, final boolean say)
    {
    	Log.d("list", "hello! my pos = "+pos);
    	final ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
    	final Button deleteDefButton = (Button) findViewById(R.id.DeleteDefButton);
       	 new Thread(new Runnable() {
	   	        public void run() {	
             runOnUiThread(new Runnable() 
              	 { public void run() 
              	 	            {
              		 
                	 listViewJour.setItemChecked(pos, say);
                	 int num = listViewJour.getCheckedItemCount();
                	 
                	 if (num > 0) 
			    		{   		
			    			deleteDefButton.setVisibility(View.VISIBLE);				    			
			    		}
			    		else 
			    		{
			    			deleteDefButton.setVisibility(View.GONE);
			    		}  
                	 
                	 Log.d("list", "hello! my pos = "+pos+" my count = "+num);
                	 
              	 	            }
              	 	        });  
                  }  }).start();

    }
	
	private void setEnableInterfaces(Boolean say)
	{
		Spinner spinPart = (Spinner) findViewById(R.id.SpinPart);
		Spinner spinEquip = (Spinner) findViewById(R.id.SpinEquip);
		Spinner spinCause = (Spinner) findViewById(R.id.SpinCause);
		EditText editTextDescr = (EditText) findViewById(R.id.editTextDescr);
		
		TextView textViewPart = (TextView) findViewById(R.id.TextViewPart);
		TextView textViewEquip = (TextView) findViewById(R.id.TextViewEquip);
		TextView textViewCause = (TextView) findViewById(R.id.TextViewCause);
		TextView textViewDescr = (TextView) findViewById(R.id.TextViewDescr);
		
		TextView textViewSpinnerTip0 = (TextView) findViewById(R.id.TextViewSpinnerTip0);
		TextView textViewSpinnerTip1 = (TextView) findViewById(R.id.TextViewSpinnerTip1);
		TextView textViewSpinnerTip2 = (TextView) findViewById(R.id.TextViewSpinnerTip2);
		
	   ImageButton cameraButton = (ImageButton) findViewById(R.id.imageButton1);
	   ImageButton photoButton = (ImageButton) findViewById(R.id.imageButton2);
	   LinearLayout layoutGallery = (LinearLayout) findViewById(R.id.LinearLayoutGallery);
		
		spinPart.setEnabled(say);
		editTextDescr.setEnabled(say);
		
		
		if (say)
			{
				textViewSpinnerTip0.setVisibility(View.GONE);
				textViewSpinnerTip1.setVisibility(View.GONE);
				textViewSpinnerTip2.setVisibility(View.GONE);
				
				spinPart.setVisibility(View.VISIBLE);
				spinEquip.setVisibility(View.VISIBLE);
				spinCause.setVisibility(View.VISIBLE);
				editTextDescr.setVisibility(View.VISIBLE);
				
				textViewPart.setVisibility(View.VISIBLE);
				textViewEquip.setVisibility(View.VISIBLE);
				textViewCause.setVisibility(View.VISIBLE);
				textViewDescr.setVisibility(View.VISIBLE);
				
				cameraButton.setVisibility(View.VISIBLE);
				layoutGallery.setVisibility(View.VISIBLE);
   	    	 	
   	    	 	cameraButton.setEnabled(false);
	    	 	photoButton.setEnabled(false);  	   
   	    	 	 	 	
			}
		else
			{
				spinPart.setVisibility(View.GONE);
				spinEquip.setVisibility(View.GONE);
				spinCause.setVisibility(View.GONE);
				editTextDescr.setVisibility(View.GONE);
				
				textViewPart.setVisibility(View.GONE);
				textViewEquip.setVisibility(View.GONE);
				textViewCause.setVisibility(View.GONE);
				textViewDescr.setVisibility(View.GONE);
				
				textViewSpinnerTip0.setVisibility(View.VISIBLE);
				textViewSpinnerTip1.setVisibility(View.VISIBLE);
				textViewSpinnerTip2.setVisibility(View.VISIBLE);
				
				cameraButton.setVisibility(View.GONE);
			//	cameraButton.setEnabled(true);
				layoutGallery.setVisibility(View.GONE);

			}
		
	}
	
	private void setCleanAfterSave(Context context)
	{
		EditText editTextDescr = (EditText) findViewById(R.id.editTextDescr);
		TextView textViewPart = (TextView) findViewById(R.id.TextViewPart);
		TextView textViewEquip = (TextView) findViewById(R.id.TextViewEquip);
		TextView textViewCause = (TextView) findViewById(R.id.TextViewCause);
		TextView textViewDescr = (TextView) findViewById(R.id.TextViewDescr);
		
		TextView textViewSpinnerTip0 = (TextView) findViewById(R.id.TextViewSpinnerTip0);
		TextView textViewSpinnerTip1 = (TextView) findViewById(R.id.TextViewSpinnerTip1);
		TextView textViewSpinnerTip2 = (TextView) findViewById(R.id.TextViewSpinnerTip2);
        
        Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
        Spinner spinObj = (Spinner) findViewById(R.id.SpinObj);
		
		editTextDescr.setVisibility(View.GONE);
		
		textViewPart.setVisibility(View.GONE);
		textViewEquip.setVisibility(View.GONE);
		textViewCause.setVisibility(View.GONE);
		textViewDescr.setVisibility(View.GONE);
		
		textViewSpinnerTip0.setVisibility(View.VISIBLE);
		textViewSpinnerTip1.setVisibility(View.VISIBLE);
		textViewSpinnerTip2.setVisibility(View.VISIBLE);

		editTextDescr.setText(" ");
	}
	
	
	
	private void setViewListMenu(Context context,ListView listViewMenu)
	{
		final ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
		final Button deleteDefButton = (Button)findViewById(R.id.DeleteDefButton);	
		ArrayList<String> arrayList = new ArrayList<String>();
   	    arrayList.add("			для отправки");
   	    arrayList.add("			отправленные");
		
   	    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, layout.simple_list_item_1, arrayList);
   	//	arrayAdapter.setDropDownViewResource(R.layout.);   		   		
   		listViewMenu.setAdapter(arrayAdapter);
   		listViewMenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
   		//listViewMenu.setSelection(0);
   		
   		listViewMenu.setOnItemClickListener(new OnItemClickListener() {   			
    	    public void onItemClick(AdapterView<?> parent, View view,
    	        int position, long id) 
    	    {   	          	   
    	    	int j = 0;    	    		  
	    		while (j <= listViewJour.getCount())
	    		  {
	    			listViewJour.setItemChecked(j, false);
	    			j++;
	    		  }
	    	// Выделить только одну строчку
    	      int i = 0;
    	      while (i<=1)
    	      {
    	    	  if (i == position) 
    	    		  {
    	    		  	parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.Blue));
    	    	   		TextView textViewJourData = (TextView) findViewById(R.id.TextViewJourData);
    	    	   		textViewJourData.setVisibility(View.VISIBLE);
    	    	   		listViewJour.setVisibility(View.VISIBLE);
    	    	   		setViewListJour(FirstDBActivity.this, listViewJour, position);   	    	   		
    	    	   		i++;
    	    		  }
    	    	  else {
    	    		  	parent.getChildAt(i).setBackgroundResource(R.layout.listviewmenu);
    	    		  	deleteDefButton.setVisibility(View.GONE);
    	    		  	i++;
    	    	  	   }
    	      }
    	     
    	    }
    	  });



	};
	
    public void startGallery(Uri uri)
    {
    	Log.d("Gallery","try start Gallery! "+uri);
    	
    	if (uri != null) 
		{
    		Intent intent = new Intent(Intent.ACTION_VIEW);				
    		intent.setDataAndType(uri, "image/*");
			FirstDBActivity.this.startActivityForResult(intent, GALLERY);
			
			
		}
    }
    
    public void getDefIdForImage(int position)
    {
        ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);        
        ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
        
        Handler myHandler = new Handler();
    	Context context = FirstDBActivity.this ;
    	DBAdapter adap = new DBAdapter(context);
    	
    	adap.open();
    	int menuItem = listViewMenu.getCheckedItemPosition ();
		int jourCount = listViewJour.getCount();
		int def_id = adap.getDefectsId(menuItem, (jourCount)-position);
		adap.close();
		
		Log.d("def_id for image", "menuItem="+menuItem+"; jourCount="+jourCount+"; _id="+def_id);
		try {
				MediaScanner ms = new MediaScanner();
				ms.startScan(context, Integer.toString(def_id), myHandler, "first");
		}
		catch (Exception ex) 
			{			
				Toast toast = Toast.makeText(context, "Ошибка доступа к фотографии\n"+ex.getMessage(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				
				TabHost tabs=(TabHost)findViewById(R.id.tabhost);       
				tabs.setup();  
				int i = tabs.getCurrentTab();
				if (i == 0) 
				        	   {
   									// Количество временных фото			
	   	        					TextView imagesNumber = (TextView) findViewById(R.id.textView2);
	   	        					imagesNumber.setText("0");
				        	   }
				if (i == 1) 
				        	   {
					 				int menuPosition = listViewMenu.getCheckedItemPosition();
									// Вывод значений в UI
									listViewMenu.performItemClick(listViewMenu.getAdapter().getView(menuPosition, null, null), 0, 0);
				        	   }


			}
		
    }
    
	
	private void setViewListJour(Context context, final ListView listViewJour, int listViewJour_posit)
	{
		final Button deleteDefButton = (Button)findViewById(R.id.DeleteDefButton);		
		DBAdapter adap = new DBAdapter(context);
		adap.open();
		
		Cursor cursorDefect = adap.getDefects(listViewJour_posit);
		int num = cursorDefect.getCount();	
		
		if (num > 0)
			{
				ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
				
				cursorDefect.move(-1);
				while (cursorDefect.moveToNext())
				{	
					HashMap<String, String> hm = new HashMap<String, String>();	
					String result = cursorDefect.getString(1)+ "  Смена"
									+cursorDefect.getString(9)+ "  Агр. "
									+adap.getParentNameById(cursorDefect.getString(7))+ "  Поз. "
									+adap.getParentNameById(cursorDefect.getString(8));	
					String def_id = cursorDefect.getString(cursorDefect.getColumnIndex("_id"));
					//String image_exist = cursorDefect.getString(16);
					String image_exist = "0";
					
					String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id;
		 	    	File dir = new File(link);
		 	    	
		 	    	if (dir.exists()) 
		 	    		{
		 	    		String[] list = dir.list();
		 	    		int number = list.length;
		 	    		if (number > 0) image_exist = "1";
		 	    		}
		 	    			 	    			 	    		    			 	    	
					hm.put(TEXT, result);
					hm.put(IMAGE, image_exist);
					arrayList.add(hm);
				}
				
				final JournalAdapter adapter = new JournalAdapter(this, arrayList);
				listViewJour.setItemsCanFocus(true);
				listViewJour.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);				
		   		// Вывод значений в UI
				listViewJour.setAdapter(adapter);
				/*			
				listViewJour.setOnItemClickListener(new OnItemClickListener() 
				{   			
				    public void onItemClick(AdapterView<?> parent, View view,
			  	        int position, long id) 
				    	{
				    		if (listViewJour.getCheckedItemCount()>0) 
				    		{
				    			//if(listViewJour.isItemChecked(position)) 	    		
				    			deleteDefButton.setVisibility(View.VISIBLE);				    			
				    		}
				    		else 
				    		{
				    			deleteDefButton.setVisibility(View.GONE);
				    		}   
				    		
				    		Log.d("listViewJour", "gotcha! "+Integer.toString(position));
				    		//adapter.setCheck(position, listViewJour);
				    		//listViewJour.getAdapter().getItem(position);
				    	}
				});
				*/
			}
		else 
			{	
				ArrayList arrayList = new ArrayList();
				arrayList.add("                                                                 ничего");
				listViewJour.setChoiceMode(ListView.CHOICE_MODE_NONE);	
				// Создаем адаптер
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, layout.simple_list_item_1, arrayList);						   		    								
				// Вывод значений в UI
				listViewJour.setAdapter(arrayAdapter);
			}					
   		adap.close();
   		cursorDefect.close();
	}
	
	private void setViewOptionList(Context context,ListView listViewOptionList, final FrameLayout frameLayout)
	{
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("			Цех и участок");
		arrayList.add("	   Сервер обновлений");
		final AbsoluteLayout absLayout1 = (AbsoluteLayout) findViewById(R.id.AbsLayoutOption1);
		final AbsoluteLayout absLayout2 = (AbsoluteLayout) findViewById(R.id.AbsLayoutOption2);
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, layout.simple_list_item_1, arrayList);  		   		
		listViewOptionList.setAdapter(arrayAdapter);
		listViewOptionList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);		
	
		listViewOptionList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Выделить только одну строчку      
				int i = 0;
				while (i<=1)
				{
					if (i == position) 
					{
						parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.Blue));
						frameLayout.removeAllViews();
						if (i == 0) frameLayout.addView(absLayout1);
						if (i == 1) frameLayout.addView(absLayout2);	   		
						i++;
					}
					else {
						parent.getChildAt(i).setBackgroundResource(R.layout.listviewmenu);
						i++;
						 }
			    }
										}
																		});
	};
	
    private OnClickListener mAddListener = new OnClickListener()  
    { 
    	private ListView outputList;

		public void onClick(View v)  
        {  
			
	    	final Context context = getApplicationContext();
	    try{	
    		final DBAdapter adap = new DBAdapter(context);
    		// buttons
			final ToggleButton tglButton1 = (ToggleButton)findViewById(R.id.toggleButton1);
			final Button syncButton = (Button)findViewById(R.id.SyncButton);
			final Button syncButton2 = (Button)findViewById(R.id.SyncButton2);   	   		
   	   		final Button updateZehSectorButton = (Button)findViewById(R.id.UpdateZehSectorButton);
   	   		final Button deleteDefButton = (Button)findViewById(R.id.DeleteDefButton);
    		//spinners
    		final Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
    		final Spinner spinPosit = (Spinner) findViewById(R.id.SpinPosit);
    		final Spinner spinPart = (Spinner) findViewById(R.id.SpinPart);
    		final Spinner spinEquip = (Spinner) findViewById(R.id.SpinEquip);
    		Spinner spinCause = (Spinner) findViewById(R.id.SpinCause);
    		Spinner spinObj = (Spinner) findViewById(R.id.SpinObj);      		
    		final Spinner spinOptionZeh = (Spinner) findViewById(R.id.SpinOptionZeh);
    		final Spinner spinOptionSector = (Spinner) findViewById(R.id.SpinOptionSector);
    		// EditText
    		EditText editTextDescr = (EditText) findViewById(R.id.editTextDescr);    		
    		EditText editTextOptionServer = (EditText) findViewById(R.id.EditTextOptionServer);
    	   	EditText editTextOptionLogin = (EditText) findViewById(R.id.EditTextOptionUsername);              	   		
    	   	EditText editTextOptionPassw = (EditText) findViewById(R.id.EditTextOptionPassword);
    	   	// TextView
    	   	final TextView textViewDateValue = (TextView) findViewById(R.id.TextViewDateValue);
            final TextView textViewZehValue = (TextView) findViewById(R.id.TextViewZehValue);
            final TextView textViewSectorValue = (TextView) findViewById(R.id.TextViewSectorValue);
            final TextView textViewSmenaValue = (TextView) findViewById(R.id.TextViewSmenaValue);
    	   	final TextView textViewTestOptionResult = (TextView) findViewById(R.id.TextViewTestOptionResult);
    	   	final TextView textViewVersionValue = (TextView) findViewById(R.id.TextViewVersionValue);
            final TextView textViewVersionDateValue = (TextView) findViewById(R.id.TextViewVersionDateValue);
    	   	// Постоянный поток для выполнения различных операций
    	    final Thread loopThread = new LoopingThread();
    		final Handler myHandler = new Handler();
            final ListView listViewMenu = (ListView) findViewById(R.id.listViewMenu);
   	  		final ListView listViewJour = (ListView) findViewById(R.id.ListViewJour);
   	   		TextView textViewJourData = (TextView) findViewById(R.id.TextViewJourData);
   	   		// Переменные
   	   		final int numOldToDelete = 5 ;
   	   		
   	   	//	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
   	   		
    		int dialogId = 0;    		
    		switch(v.getId())  
            {    		   
            		// Кнопка "WIFI 1"
            		case R.id.toggleButton1:

            			@SuppressWarnings("static-access")
						WifiManager wm = (WifiManager) context.getSystemService(context.WIFI_SERVICE);	
            				try {
            						//syncButton.setEnabled(false);
            						if (tglButton1.isChecked() & !wm.isWifiEnabled())
            							{
            								wm.setWifiEnabled(true);
            								if ( syncButton.isEnabled() ) syncButton.setEnabled(true);
            								if ( syncButton2.isEnabled() ) syncButton2.setEnabled(true);
            							}
            						if (!tglButton1.isChecked() & wm.isWifiEnabled())
            							{
            								//wm.setWifiEnabled(false);            							
       		        	   					dialogId = DialogFactory.DIALOG_ALERT;       		        	   				
       		        	   					showDialog(dialogId);       		        	   				       		        	   					
            							}
            					}
            			
            				catch  (Exception ex) {
            					//Context context = getApplicationContext();  
            					CharSequence text = ex.getMessage();  
            					int duration = Toast.LENGTH_LONG;
            					Toast toast = Toast.makeText(context, text, duration);  
            					toast.show();
        		   						 	  }
            		break;
            		/*
            		case R.id.TestBlockButton:
            			 			TabHost tabHost =  (TabHost) findViewById(R.id.tabhost);
                              		tabHost.setCurrentTab(2);
                              		//tabHost.setCurrentTab(0);
                 					ListView listViewOption = (ListView) findViewById(R.id.ListViewOptions);
                         			listViewOption.performItemClick(listViewOption.getAdapter().getView(0, null, null), 0, 0);
                        			
          				 
            		//	updateZehSectorButton.setEnabled(false);	            				
            		break;
            		*/
            		// Кнопка "WIFI 2"	
            		case R.id.toggleButton2:
            			ToggleButton tglButton11 = (ToggleButton)findViewById(R.id.toggleButton2);
            			ToggleButton tglButton2 = (ToggleButton)findViewById(R.id.toggleButton2);
            			Button syncButton1 = (Button)findViewById(R.id.SyncButton);
            			Button syncButton21 = (Button)findViewById(R.id.SyncButton2);
            			@SuppressWarnings("static-access")
						WifiManager wm1 = (WifiManager) context.getSystemService(context.WIFI_SERVICE);	
            				try {
            						//syncButton.setEnabled(false);
            						if (tglButton11.isChecked() & !wm1.isWifiEnabled())
            							{
            								wm1.setWifiEnabled(true);
            								if ( syncButton1.isEnabled() ) syncButton1.setEnabled(true);
            								if ( syncButton21.isEnabled() ) syncButton21.setEnabled(true);
            							}
            						if (!tglButton11.isChecked() & wm1.isWifiEnabled())
            							{
       		        	   					dialogId = DialogFactory.DIALOG_ALERT;       		        	   				
       		        	   					showDialog(dialogId);       		        	   				       		        	   					
            							}
            					}
            			
            				catch  (Exception ex) {
            					//Context context = getApplicationContext();  
            					CharSequence text = ex.getMessage();  
            					int duration = Toast.LENGTH_LONG;
            					Toast toast = Toast.makeText(context, text, duration);  
            					toast.show();
        		   						 	  }
            		break;
            		
            		case R.id.SaveButton:
            			
            			// Получаем данные для сохранения
            			adap.open();
            			 Handler myHandlerSave = new Handler();
            			// Время и Смена
            			String regdate = textViewDateValue.getText().toString();
            			String smena = textViewSmenaValue.getText().toString();
            			
            			// Цех и Участок
            			String zeh = adap.getConnectParam("ZEH");
            			String sector = adap.getConnectParam("SECTOR");
            			
            			// Найдем id выбранного Агрегата
            			String aggrIdValue;            			
            			// На случай, если не заполнен parentid            			
            			if (parentId.equals("empty")) 
						{
							String startwith = adap.getConnectParam("SECTOR");
							Cursor cursorAggrId = adap.getAggrByParent(startwith);							
								int spinAggr_posit = spinAggr.getSelectedItemPosition();
								cursorAggrId.move(spinAggr_posit-1);								
								aggrIdValue = cursorAggrId.getString(0);								
							    Log.d("savefor aggrIdValue", aggrIdValue + " "+cursorAggrId.getString(3));
							cursorAggrId.close();
						}
            			else if (spinAggr.getSelectedItem().toString().substring(0, 1).equals("[")) 
            			{
            				Log.d("savefor parent", parentId); 
            				aggrIdValue = parentId;
            			}
            			else 
            			{
            				Log.d("savefor parent", parentId);
            				Cursor cursorAggrId = adap.getAggrByParent(parentId);
            					int spinAggr_posit = spinAggr.getSelectedItemPosition();
            					cursorAggrId.move(spinAggr_posit-1); 
            					aggrIdValue = cursorAggrId.getString(0);
            					
            					Log.d("savefor aggrIdValue", aggrIdValue + " "+ cursorAggrId.getString(3));
            				cursorAggrId.close();
            			}
            			
            			// Найдем id выбранной Позиции
            			String positIdValue;
            			Cursor cursorPositId = adap.getPositByParent(aggrIdValue);
							int spinOptionPosit_posit = spinPosit.getSelectedItemPosition();
							cursorPositId.move(spinOptionPosit_posit);								
							positIdValue = cursorPositId.getString(0);
							Log.d("savefor positIdValue", positIdValue + " "+ cursorPositId.getString(3));
						cursorPositId.close();
            			
						// Найдем id выбранной Неисправной части
            			String partIdValue;
            			Cursor cursorPartId = adap.getPartCursor();
							int spinPart_posit = spinPart.getSelectedItemPosition();
							cursorPartId.move(spinPart_posit);								
							partIdValue = cursorPartId.getString(0);
							Log.d("savefor partIdValue", partIdValue + " "+ cursorPartId.getString(3));
						cursorPartId.close();
						
						// Найдем id выбранного Оборудования
            			String equipIdValue;
            			Cursor cursorEquipId = adap.getEquipByPart(partIdValue);
							int spinEquip_posit = spinEquip.getSelectedItemPosition();
							cursorEquipId.move(spinEquip_posit);								
							equipIdValue = cursorEquipId.getString(0);
							Log.d("savefor equipIdValue", equipIdValue + " "+ cursorEquipId.getString(3));
						cursorEquipId.close();
						
						// Найдем id выбранной Причины
						String causeIdValue;						
						Cursor cursorCauseId = adap.getCauseByEquip(equipIdValue);
							int spinCause_posit = spinCause.getSelectedItemPosition();
							cursorCauseId.move(spinCause_posit);								
							causeIdValue = cursorCauseId.getString(0);
							Log.d("savefor causeIdValue", causeIdValue + " "+ cursorCauseId.getString(3));
						cursorCauseId.close();
						
						// Получим значение Остановки
						String stop = "0";
						
						// Получим значение Описания
						String descr = editTextDescr.getText().toString();
						
						// Привяжем дефект к фотографиям
						String image_exist = "0";
						String def_id = adap.getNextId("defects");	
						if (def_id == null) def_id = "1";
						
			 	    	String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id;
			 	    	File dir = new File(link);
			 	    	
			 	    	if (dir.exists())
			 	    	{
			 	    		String[] list = dir.list();
			 	    		final int number = list.length;			 	    	
			 	    	    	
			 	    		if (number > 0)
			 	    			{
			 	    				// Изменим статус
			 	    				adap.ChangeImageStatus("tosend");
			 	    				// Пометим дефект как имеющий фотографии
			 	    				image_exist = "1";
			 	    			}
			 	    	}	
			 	    	
						// Сохраним полученные значения
						// Вместо конечной категории позиции aggrIdValue вставляем aggrId
						adap.SaveReg(regdate, zeh, sector, smena, aggrId, positIdValue, "", partIdValue, equipIdValue, causeIdValue, descr, image_exist);
						// Удалить старые записи, если количество превышает 5
						try {
							adap.deleteOldDefects(numOldToDelete);
							// Зачистить фотографии
							try {
									Cursor curImages = adap.getImagesWithNotExistDef();
									curImages.move(-1);
									while (curImages.moveToNext()) 
									{
										String def_id_1 = curImages.getString(curImages.getColumnIndex("_id"));
										deleteImagesByDef(adap, def_id_1); 
									} 
									curImages.close();
								}
							catch (Exception ex) {}
							
						} catch (Exception e) 
						{
							Toast toast = Toast.makeText(context, "Ошибка удаления старых записей\n"+e.getMessage(), Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
	            			toast.show();
						}
            			Toast savetoast2 = Toast.makeText(context, "Сохранение записи от "+regdate+" выполнено успешно! "/*+tab.toString()*/, Toast.LENGTH_LONG);
            			savetoast2.show();
            			
  	     				// Очистка временных фотографий
  	     				try {
  	     						int delnum = deleteImages(context, adap);						
  	     						if (delnum > 0)
  	     								Toast.makeText(context, 
  	     										" Временные фото были удалены. Количество: "+Integer.toString(delnum),Toast.LENGTH_LONG).show();
  			   	    	    }
  			   	    	catch (Exception ex)
  			   	    	    {
  			   	    	    	Log.d("images_link", ex.getMessage());
  			   	    	    }
  	     					
  	     				// Иконку и счетчик в исходное состояние
  	     				TextView imagesNumber = (TextView) findViewById(R.id.textView2);
  	     				ImageButton cameraButton = (ImageButton)findViewById(R.id.imageButton1);
  	     				ImageButton photoButton = (ImageButton)findViewById(R.id.imageButton2);
  	     				imagesNumber.setText("0");
  	     				imagesNumber.setTextColor(Color.parseColor("#0054A8"));
  	     				cameraButton.setEnabled(true);
  	     					photoButton.setEnabled(false);
  	     					
            			
    					adap.open();
    					ArrayList<String> arrayListPart = new ArrayList<String>();
    	   				Cursor cursorPart = adap.getPartCursor();
    	   				arrayListPart.add(" ");
    	   				cursorPart.move(-1);
    		   			while (cursorPart.moveToNext()) 
    			   		{
    		   				arrayListPart.add(cursorPart.getString(3));
    			   		}          				
        		   		final ArrayAdapter<String> arrayAdapterPart = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayListPart);
        		   		arrayAdapterPart.setDropDownViewResource(R.layout.spinner_1_dropdown_item);								   		    								
        				// Вывод значений в UI
        		   		myHandlerSave.post(new Runnable() { 
        					public void run() {
        						spinPart.setAdapter(arrayAdapterPart);
        						spinPart.setEnabled(true);
        						}
        					});     		   		
        		   		
        		   		editTextDescr.setText("");
            			Button syncButton11 = (Button) findViewById(R.id.SyncButton);
            			if (tglButton1.isChecked()) syncButton11.setEnabled(true); 
            			//syncButton11.setEnabled(true);
            			adap.close(); 
            		break;
            		
            		case R.id.SaveButton2:            			
                   	 new Thread(new Runnable() {
          	   	        public void run() {	
                              final TabHost tabHost =  (TabHost) findViewById(R.id.tabhost);
                          	 runOnUiThread(new Runnable() 
                         	 { public void run() 
                         	 	            {
                         		 tabHost.setCurrentTab(0);                          	
                         	 	            }
                         	 	        });  
                             }  }).start();
                   	 
            			
            		break;         
            		
            		
            		case R.id.SyncButton:            		            		  
            			            			
            			 new Thread(new Runnable() {
           				  public void run() {	
           				      runOnUiThread(new Runnable() 
           				         { public void run() 
           				             	 	{
           				        	 			int dialogId = DialogFactory.DIALOG_PROGRESS_1;
           				        	 			showDialog(dialogId);
           				             	 	}
           				          });
           				        	   	        	      
           				              try {            		             		 
           				             		adap.open();
         		            		            		
           				             		Cursor cursor = adap.getDefectsHttpInsert(0);             		
           				             			String server = adap.getConnectParam("server");
           				             			String username = adap.getConnectParam("username");
           				             			String cryptpassword = adap.getConnectParam("password");
           				             			String password = Decrypt(cryptpassword);	
           				             			final  String answer = HttpInsert(adap, cursor, FirstDBActivity.this, server, username, password);            				             		
           				             		cursor.close();         				             		
           				               	     		runOnUiThread(new Runnable() 
           				               	     		{                
           				               	     			public void run() 
           				               	     			{
           				               	     				int dialogId = DialogFactory.DIALOG_PROGRESS_1;
           				               	     				dismissDialog(dialogId);
           				               	     				Button syncButton = (Button)findViewById(R.id.SyncButton);
           				               	     				Button syncButton2 = (Button)findViewById(R.id.SyncButton2);           				               	     				
           				               	     				syncButton.setEnabled(false);
           				               	     				syncButton2.setEnabled(false);
           				               	     				if (answer == "ok")
           				               	     				{
           				               	     					Toast toast = Toast.makeText(FirstDBActivity.this, " Данные успешно отправлены в базу ", Toast.LENGTH_LONG);
           				               	     					toast.setGravity(Gravity.CENTER, 0, 0);
           				               	     					toast.show();           				               	     					
           				               	     					
           				               	     				}
           				               	     				else 
           				               	     				{
           				               	     					Toast toast = Toast.makeText(FirstDBActivity.this, " Отправка не выполнена \n"+answer, Toast.LENGTH_LONG);             				               	     				
           				               	     					toast.show();
           				               	     				}
           				               	     			}
           				               	     		});
           				               	     		
           				               	     	try 
           				               	     	{
           				               	     		Cursor cursorFile = adap.selectOldDefects(numOldToDelete);
           				               	     		cursorFile.move(-1);
													while (cursorFile.moveToNext()) 
													{
														String def_id_im = cursorFile.getString(0);
														File sdCard = Environment.getExternalStorageDirectory();
														File dir = new File (sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id_im);
														if (dir.exists())
														{
															 String[] children = dir.list();
												   		        for (int i = 0; i < children.length; i++) {
												   		            new File(dir, children[i]).delete();   		        
												   		        }
												   		     dir.delete();
														}
														sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(dir)));
													}
													cursorFile.close();
													
           				               	     		adap.deleteOldDefects(numOldToDelete);
           											// Зачистить фотографии
           											try {
           													Cursor curImages = adap.getImagesWithNotExistDef();
           													curImages.move(-1);
           													while (curImages.moveToNext()) 
           													{
           														String def_id_1 = curImages.getString(curImages.getColumnIndex("_id"));
           														deleteImagesByDef(adap, def_id_1); 
           													} 
           													curImages.close();
           												}
           											catch (Exception ex) {}
           				 						} 
           				               	     	catch (Exception e) 
           				               	     	{
           				 							Toast toast = Toast.makeText(context, "Ошибка удаления старых записей\n"+e.getMessage(), Toast.LENGTH_LONG);
           				 							toast.setGravity(Gravity.CENTER, 0, 0);
           				 	            			toast.show();
           				 						}
           				             	}
           				              catch (Exception e) 
           				             	 	 {
           				             		 Log.e("log_tag", "Error in http connection "+e.toString());
           				                	     	runOnUiThread(new Runnable() 
           				                	     	{                
           				                	     		public void run() 
           				                	     		{
           				                	     			int dialogId = DialogFactory.DIALOG_PROGRESS_1;
           				                	     			dismissDialog(dialogId);          				                	     			
           				                	     			Toast toast = Toast.makeText(FirstDBActivity.this, "В ходе отправки данных произошла ошибка!\nПроверьте доступ к сети или обратитесь к администратору", Toast.LENGTH_LONG);
           				                	     			toast.setGravity(Gravity.CENTER, 0, 0);
           				                	     			toast.show();
           				                	     		
           				                	     		}
           				                	     	});
           				             	 	 }
           				             	
           				             	 	adap.close();
           				             	 	
           				             	   	     }  }).start();
           				             	   	     
            				             	  
                    	break;
                    	
                 		case R.id.SyncButton2:
                 			
              			  new Thread(new Runnable() {
            				  public void run() {	
            				      runOnUiThread(new Runnable() 
            				         { public void run() 
            				             	 	{
            				        	 			int dialogId = DialogFactory.DIALOG_PROGRESS_1;
            				        	 			showDialog(dialogId);
            				             	 	}
            				          });
            				             	 try{
            				             		 java.lang.Thread.sleep(100);
            				                  	} catch (Exception e) {
            				                  }
            				        	   	        	      
            				              try {            		             		 
            				             		adap.open();          		            		            		
            				             		Cursor cursor = adap.getDefectsHttpInsert(0);             		
            				             			String server = adap.getConnectParam("server");
            				             			String username = adap.getConnectParam("username");
            				             			String cryptpassword = adap.getConnectParam("password");
            				             			String password = Decrypt(cryptpassword);	
            				             			HttpInsert(adap, cursor, FirstDBActivity.this, server, username, password);            				             		
            				             		cursor.close();
            				               	     		runOnUiThread(new Runnable() 
            				               	     		{                
            				               	     			public void run() 
            				               	     			{
            				               	     				int dialogId = DialogFactory.DIALOG_PROGRESS_1;
            				               	     				dismissDialog(dialogId);
            				               	     				Button syncButton = (Button)findViewById(R.id.SyncButton);
            				               	     				Button syncButton2 = (Button)findViewById(R.id.SyncButton2);
            				               	     				// Изменение статуса кнопки
            				               	     				syncButton.setEnabled(false);
            				               	     				syncButton2.setEnabled(false);            				               	     				
            				               	     				//listViewMenu.performItemClick(listViewMenu, 0, 0);
            				               	     				
            				               	     				// Переключение пункта меню
            				               	     				listViewMenu.performItemClick(listViewMenu.getAdapter().getView(0, null, null), 0, 0);
            				               	     				
            				               	     				Toast toast = Toast.makeText(FirstDBActivity.this, " Данные успешно отправлены в базу ", Toast.LENGTH_LONG);
            				               	     				toast.setGravity(Gravity.CENTER, 0, 0);
            				               	     				toast.show();
            				               	     			}
            				               	     		});
            				               	     		
            				               	     	try {
            				               	     		
            				               	     		Cursor cursorFile = adap.selectOldDefects(numOldToDelete);
           				               	     			cursorFile.move(-1);
           				               	     			while (cursorFile.moveToNext()) 
														{
															String def_id_im = cursorFile.getString(0);
															File sdCard = Environment.getExternalStorageDirectory();
															File dir = new File (sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id_im);
															if (dir.exists())
															{
															 	String[] children = dir.list();
												   		        	for (int i = 0; i < children.length; i++) {
												   		            	new File(dir, children[i]).delete();   		        
												   		        	}
												   		        dir.delete();
															}
															sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(dir)));
														}
														cursorFile.close();
            				               	     		
            				    						adap.deleteOldDefects(numOldToDelete);
            											// Зачистить фотографии
            											try {
            													Cursor curImages = adap.getImagesWithNotExistDef();
            													curImages.move(-1);
            													while (curImages.moveToNext()) 
            													{
            														String def_id_1 = curImages.getString(curImages.getColumnIndex("_id"));
            														deleteImagesByDef(adap, def_id_1); 
            													} 
            													curImages.close();
            												}
            											
            											catch (Exception ex) {}
            											
            				    						} catch (Exception e) 
            				    						{
            				    							Toast toast = Toast.makeText(context, "Ошибка удаления старых записей\n"+e.getMessage(), Toast.LENGTH_LONG);
            				    							toast.setGravity(Gravity.CENTER, 0, 0);
            				    	            			toast.show();
            				    						}
            				             	}
            				              catch (Exception e) 
            				             	 	 {
            				             		 Log.e("log_tag", "Error in http connection "+e.toString());
            				                	     	runOnUiThread(new Runnable() 
            				                	     	{                
            				                	     		public void run() 
            				                	     		{
            				                	     			int dialogId = DialogFactory.DIALOG_PROGRESS_1;
            				                	     			dismissDialog(dialogId);
            				                	     			//Button syncButton = (Button)findViewById(R.id.SyncButton);
            				                	     			//Button syncButton2 = (Button)findViewById(R.id.SyncButton2);
            				                	     			//syncButton.setEnabled(false);
            				                	     			//syncButton2.setEnabled(false);
            				                	     			//adap.close();
            				                	     			
            				                	     			Toast toast = Toast.makeText(FirstDBActivity.this, "В ходе отправки данных произошла ошибка!\nПроверьте доступ к сети или обратитесь к администратору", Toast.LENGTH_LONG);
            				                	     			toast.setGravity(Gravity.CENTER, 0, 0);
            				                	     			toast.show();
            				                	     		
            				                	     		}
            				                	     	});
            				             	 	 }
            				             	
            				             	 	adap.close();
            				             	 	
            				             	   	     }  }).start();
                        break;
                        
                 		case R.id.DeleteDefButton:
                 			
                			TextView view1 = new TextView(context);            			
                		    view1.setText(" Будут удалены выбранные записи о дефектах " +
                					"\n" +
                					"\nПродолжить?" +
                					"\n");
                			view1.setGravity(Gravity.CENTER);
                			view1.setTextColor(Color.WHITE);
                			view1.setTextSize(18);
                			
                			AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstDBActivity.this);
                			builder1.setMessage("Внимание!")        
                			.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
                			{             
                				@SuppressWarnings("unchecked")
								public void onClick(DialogInterface dialog, int id) 
                					{ 
                					
                					
                					 new Thread(new Runnable() {
                          	   	        public void run() {	             	            	 
                          	   	        	runOnUiThread(new Runnable() 
                          	            	 { public void run() 
                          	            	 	            {
                          	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                          	   		        	   				showDialog(dialogId);                          	   		        	   				
                          	            	 	            }
                          	            	 	        });
                          	            	 try{
                          	            		 java.lang.Thread.sleep(100);
                          	                 	} catch (Exception e) { }  
                          	            	 
                          	            	 
                              				
                          	            try {            						
             									((LoopingThread) loopThread).getHandler().post(new Runnable() 
             									{
             										public void run() 
             										{		
             											
             										}
             									});																										
                         					} catch (InterruptedException e) {
             									Log.e("log_tag", "Ошибка в работе потока "+e.getMessage());
             								}   
                         				
                          	            	runOnUiThread(new Runnable() 
                                    	     	{                
                                    	     		public void run() 
                                    	     		{
                                    	     			adap.open();
             		                         			int menuItem = listViewMenu.getCheckedItemPosition ();         					
             		                 					String delresult = " Записи успешно удалены "; 
             		                 					final Button syncButton2 = (Button)findViewById(R.id.SyncButton2);
             		                         			int jourCount = listViewJour.getCount();
             		                         			@SuppressWarnings("rawtypes")
             											ArrayList list = new ArrayList();
             		                         			//Cursor cursor = adap.getDefects(menuItem);
             		                         			Log.d("count", "count= "+Integer.toString(jourCount));
             		                         			
             		                         			for (int i = jourCount-1; i >= 0; i--)
             		                         				{                 					
             		                         					Log.d("i", "i= "+Integer.toString(i)+" "+listViewJour.isItemChecked(i)+" item "+ listViewJour.getAdapter().getItem(i).toString()+" menuitem= "+menuItem);
             		                         					if(listViewJour.isItemChecked(i)) 
             		                         					{                   					
             		                         						try 
             		        										{  
             		                         							int _id = adap.getDefectsId(menuItem, (jourCount)-i);
             		                         							// Собираем выделенные записи в отдельный массив
             		                         							list.add(_id);
             		                         							String regdate = adap.getDefName(_id);
             		                         							Log.d( "_id by position", " _id= "+_id+" regdate= "+regdate );
             		        										 }
             		        										catch(Exception e){        											
             		        											delresult = "Во время удаления произошла ошибка!\n"+e.getMessage();
             		        										}                 					
             		                         					}	
             		                         				}
             		                         			selectNone(listViewJour);
             		                         			
             		                         			// Удаляем собранные записи
             		                         			for (int j=0; j < list.size(); j++)
             		                         			{
             		                         				String _id = list.get(j).toString();
             		                         				Log.d( "restore _id =", " _id= "+list.get(j) );
             		                         				adap.DeleteFromDef(_id);
             		                         				// Удаляем фотографии следом
             		                         				deleteImagesByDef(adap, _id);
             		                         				
             		                         				//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
             		                         			}     
             		     		 						final int noSendDefectsCount = adap.getNoSendDefectsCount(0);
             		     		 						 myHandler.post(new Runnable() { 
             		     		 		    				public void run() {
             		     		 		    					if (!(noSendDefectsCount > 0)) syncButton2.setEnabled(false);
             		     		 							}
             		     		 		    			});	
             		    		 						    		 						
             		                         			adap.close(); 
             		                         			
             		                            		//	cursor.close();
             		                         			deleteDefButton.setVisibility(View.GONE);
             		                         			listViewMenu.performItemClick(listViewMenu, menuItem, menuItem);              			
             		                         			Toast deltoast = Toast.makeText(context, delresult, Toast.LENGTH_LONG);  
             		               	     				deltoast.show();
                                    	     			
                                    	     			
                                    	     			int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                                       	     			dismissDialog(dialogId); 
                                    	     		}
                                    	     	});       
                          	            	 							
                                             }  }).start();
                					 
                					                					                         		
                					}         
            				})        
            				
            				.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
            				{             
            				public void onClick(DialogInterface dialog, int id) 
            						{                 
            					 		
            						}         
            				})	
            				.setView(view1).create().show();                		
       	     				
                        break;
                        
                	//** Кнопки управления настройками
                	//* Кнопки управления логином, паролем, сервером, на котором расположены php страницы
                	// Проверить имя пользователя
            		case R.id.TestOptionButton1:
            			
          try {			
            	//		final Handler myHandler = new Handler();
				final String server_test =  editTextOptionServer.getText().toString();
				final String username_test = editTextOptionLogin.getText().toString();
				final String password_test = editTextOptionPassw.getText().toString();
        				
        				
             		//		final Thread loopThread1 = new LoopingThread();
                   	 new Thread(new Runnable() {
             	   	        public void run() {	             	            	 
             	   	        	runOnUiThread(new Runnable() 
             	            	 { public void run() 
             	            	 	            {
             	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_1;
             	   		        	   				showDialog(dialogId);
             	   		        	   				
             	            	 	            }
             	            	 	        });
             	            	 try{
             	            		 java.lang.Thread.sleep(100);
             	                 	} catch (Exception e) { }        
                 				
            			try {            						
									((LoopingThread) loopThread).getHandler().post(new Runnable() 
									{
										public void run() 
										{		
											try 
											{
												final String answer = HttpSelectPwd(server_test, username_test, password_test); 
												if (!(answer == "error"))
												{
													myHandler.post(new Runnable() { 
														public void run() {
															textViewTestOptionResult.setTextColor(getResources().getColor(R.color.Green)); 
															textViewTestOptionResult.setText(answer);	
															//saveOptionButton2.
														}
													});
												} 
												else 
												{
													myHandler.post(new Runnable() { 
														public void run() {
															textViewTestOptionResult.setText("Ошибка! Проверьте параметры");
															textViewTestOptionResult.setTextColor(Color.RED); 
														}
													});
												}
											
								    	     }
											catch(Exception e){ Log.e("log_tag", "Error in http connection"+e.toString());
								    	     		myHandler.post(new Runnable() { 
								    	     			public void run() {
								    	     				textViewTestOptionResult.setText("Ошибка! Проверьте параметры");
								    	     				textViewTestOptionResult.setTextColor(Color.RED); 
								    	     			}
								    	     		});
								    	    }
										}
									});																										
            					} catch (InterruptedException e) {
									Log.e("log_tag", "Ошибка в работе потока "+e.getMessage());
								}   
            					
             	            	runOnUiThread(new Runnable() 
                       	     	{                
                       	     		public void run() 
                       	     		{
                       	     		//	textViewTestOptionResult.setText(answer);
                       	     			int dialogId = DialogFactory.DIALOG_PROGRESS_1;
                       	     			dismissDialog(dialogId); 
                       	     		}
                       	     	});       
             	            	 							
                                }  }).start();                    
     			   }
              		  catch  (Exception ex) {
              									CharSequence text = ex.getMessage();  
              									int duration = Toast.LENGTH_LONG;
              									Toast toast = Toast.makeText(context, "В ходе соединения произошла ошибка!\nПроверьте параметры доступа или обратитесь к администратору!\n"+text, duration);
              									toast.setGravity(Gravity.CENTER, 0, 0);
              									toast.show();    			
              			   				    }  
          			break;         
               		
            		// Сохранение имени сервера, логина и пароля для доступа к udp            		
            		case R.id.SaveOptionButton2:			
              try {
            			adap.open();            			               
            			String server_save = editTextOptionServer.getText().toString();
            			String username_save = editTextOptionLogin.getText().toString();
            			String password_save = editTextOptionPassw.getText().toString();            			
						String cryptpassword_save = Encrypt(password_save);															
	            		//	String cleartext = simpleCrypto.decrypt("GodBlessMechanical", crypto);									            																						
            			adap.saveConnectParams(server_save, username_save, cryptpassword_save);
            			CharSequence text = "  Настройки подключения сохранены  ";  
            			int duration = Toast.LENGTH_SHORT;
            			Toast toast = Toast.makeText(context, text, duration);  
            		    toast.show();            		    
            			adap.close();
    			   }
    		  catch  (Exception ex) {
    									CharSequence text = ex.getMessage();  
    									int duration = Toast.LENGTH_LONG;
    									Toast toast = Toast.makeText(context, text, duration);  
    									toast.show();    			
    			   				    }
               		adap.close();  
               		break;
               		
               		//* Кнопки управления выбором цеха и отдела. Загрузка данных из удаленной базы
               		
               		// updateZehSectorButton
            		case R.id.UpdateZehSectorButton:            			 
            			
            			TextView view2 = new TextView(context);            			
            		    view2.setText("Будут обновлены объекты для выбранного Цеха и Участка.\n\nУбедитесь, что находитесь в зоне стабильного WIFI. " +
            					"\n" +
            					"\nПродолжить?" +
            					"\n");
            			view2.setGravity(Gravity.CENTER);
            			view2.setTextColor(Color.WHITE);
            			view2.setTextSize(18);
            			
            			AlertDialog.Builder builder2 = new AlertDialog.Builder(FirstDBActivity.this);             			
            			builder2.setMessage("Внимание!")
            			.setCancelable(false)
            			.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
            			{             
            				public void onClick(DialogInterface dialog, int id) 
            					{   					
            					// Обновление объектов
            	   				adap.open();  
           						final String server_u = adap.getConnectParam("server");
           						final String username_u = adap.getConnectParam("username");
           						String cryptpassword_u = adap.getConnectParam("password");       						
           						final String password_u = Decrypt(cryptpassword_u);
           						final String startwith_u = adap.getConnectParam("SECTOR");
                			//	final Handler myHandler = new Handler();
                					Log.d("selectObjects params", server_u+" "+username_u+" "+password_u+" "+startwith_u); 
        						
        							
                              	 new Thread(new Runnable() {
                      	   	        public void run() {	   
                      	   	        	// Запуск диалога загрузки                  	   	        	
        								myHandler.post(new Runnable() { 
        									public void run() {
          	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
          	   		        	   				showDialog(dialogId);      	   		        	   			
        									}
        								});																			
                       					try {                   					
        									((LoopingThread) loopThread).getHandler().post(new Runnable() 
        									{
        										public void run() 
        										{	
        											// Получение данных из удаленной базы, запись в локальную    											
        											String answer1 = HttpSelectObjects(adap, server_u, username_u, password_u, startwith_u);
        							        		String zehid = adap.getConnectParam("ZEH");
        							        		String sectorid = adap.getConnectParam("SECTOR");		
        							        		final String zeh = adap.getZehSectorName(zehid);
        							        		final String sector = adap.getZehSectorName(sectorid);
        							        		// Заполнение таблицы для ускорения поиска Позиций
        							        		adap.insertFSearch();
        							        		//String answer2 = adap.getFSearch("5168");
        							        		//	Log.d("empty parentId step", answer2);
        							       	        // Обновление главной панели
        							       	        myHandler.post(new Runnable() { 
        							    				public void run() {
        							    						// Registration Date   	        	
        							    						textViewDateValue.setText(" "+getCurrDate());    		   	        	
        							    						try {
    																	textViewSmenaValue.setText(getShift());
    																} catch (java.text.ParseException e) {}							    		        		
        							    		        		String zeh2 = zeh.substring(zeh.lastIndexOf("х ")+1);
        							    		        		String sector2 = sector.substring(sector.lastIndexOf("к ")+1);
        							    		   	        	textViewZehValue.setText(" "+zeh2);
        							    		   	        	textViewSectorValue.setText(" "+sector2); 
        							    		   	        	textViewSmenaValue.setText(" "+"A");
        							    		   	        	
        	                               	     				Toast toast = Toast.makeText(context, " Объекты получены успешно ", Toast.LENGTH_LONG);  
        	                               	     				toast.show();
        							    					}
        							    				});
        							       	        String answer2 = HttpSelectPart(adap, server_u, username_u, password_u);
        							       	        String answer3 = HttpSelectEquip(adap, server_u, username_u, password_u);
        							       	        // Извещаем о прогрессе
        							       	        myHandler.post(new Runnable() { 
        							    				public void run() {
        							    						Toast toast = Toast.makeText(context, " Дефекты получены успешно (1/2)", Toast.LENGTH_LONG);  
        	                               	     				toast.show();
        							    					}
        							    				});
        							       	        String answer4 = HttpSelectCause(adap, server_u, username_u, password_u);
        							       	        // Извещаем о прогрессе
        							       	        myHandler.post(new Runnable() { 
        							    				public void run() {
        							    						Toast toast = Toast.makeText(context, " Дефекты получены успешно (2/2)", Toast.LENGTH_LONG);  
        	                               	     				toast.show();
        	                               	     				
        							    					}
        							    				});
        							       	        
        											if (answer1 == "ok" && answer2 == "ok" && answer3 == "ok" && answer4 == "ok") 
        											{
        												String startwith = adap.getConnectParam("SECTOR");
        												setAggrSpinner(adap, myHandler, startwith);
        	                   							myHandler.post(new Runnable() { 
        	        										public void run() {
        	        											Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
        	        											Spinner spinOptionZeh = (Spinner) findViewById(R.id.SpinOptionZeh);
        	        											Spinner spinOptionSector = (Spinner) findViewById(R.id.SpinOptionSector);
        	        											Button saveOptionButton1 = (Button)findViewById(R.id.SaveOptionButton1);
        	        											spinOptionZeh.setEnabled(false);
        	        											spinOptionSector.setEnabled(false);
        	        											saveOptionButton1.setEnabled(false);
        	        											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
        	                               	     				dismissDialog(dialogId);  
        	                               	     				Toast toast = Toast.makeText(context, "Все данные получены успешно ", Toast.LENGTH_LONG);
        	                               	     				toast.setGravity(Gravity.CENTER, 0, 0);
        	                               	     				toast.show();
        	        										}
        	        									});
        											} else 
        											{
        												myHandler.post(new Runnable() { 
                    										public void run() {
                    											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                                           	     				dismissDialog(dialogId);
                                           	     				Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения ", Toast.LENGTH_LONG);
                                           	     				toast.setGravity(Gravity.CENTER, 0, 0);
                                           	     				toast.show();
                    										}
                    									});
        											}
        											    											
        											adap.close();
        										}
        									});			    									
                       						} catch (InterruptedException e) {                   							 
                       						//	Log.e("log_tag", "Ошибка в работе потока "+e.getMessage());
        										final String emessage = e.toString();
                       							myHandler.post(new Runnable() { 
            										public void run() {
            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                                   	     				dismissDialog(dialogId);
                                   	     				try{
                                   	     					java.lang.Thread.sleep(100);
                                   	     					} catch (Exception e) { }   
                                   	     				Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения \n"+emessage.toString(), Toast.LENGTH_LONG);
                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
                                   	     				toast.show();
            										}
            									});

                       						}                   						            						

                      	   	        	}  }).start();
                              	 
                              	 
            					}         
            				})        
            				
            			.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
            				{             
            				public void onClick(DialogInterface dialog, int id) 
            					{                 
            					 
            					}         
            				})	
            			.setView(view2).create().show();
            			
            		break;	
               		
            		case R.id.UpdateButton1:
            			TextView view3 = new TextView(context); 
            			//Button syncButton = (Button)findViewById(R.id.SyncButton);
            		    view3.setText("Будут изменены выбранные Цех и Участок.\nВсе неотправленные записи автоматически будут сохранены в центральную базу. Затем Журнал будет очищен.\n\nУбедитесь, что находитесь в зоне стабильного WIFI" +
            					"\n" +
            					"\nПродолжить?" +
            					"\n");
            			view3.setGravity(Gravity.CENTER);
            			view3.setTextColor(Color.WHITE);
            			view3.setTextSize(18);
            			
            			AlertDialog.Builder builder3 = new AlertDialog.Builder(FirstDBActivity.this);             			
            			builder3.setMessage("Внимание!")
            			.setCancelable(false)
            			.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
            			{             
            				public void onClick(DialogInterface dialog, int id) 
            					{    			
            					// Отправка в базу
            					/*try {
            					Button syncButton = (Button)findViewById(R.id.SyncButton);
            					syncButton.performClick();
            						}
            					catch (Exception e) {
            						Log.e("after move", e.getMessage());
            					}*/
            					
            					// Отправим те что есть записи
            					if ( tglButton1.isChecked() ) syncButton.performClick();

            					//if ( syncButton2.isEnabled() ) Log.d("sync but", "sync2 yes");
            					
            					// Смена Цеха и Участка
            					try {
            							updateZehSectorButton.setEnabled(false);
            							spinOptionZeh.setEnabled(true);
            							adap.open();
            							 
            							final String server_ub1 = adap.getConnectParam("server");
            							final String username_ub1 = adap.getConnectParam("username");
            							String cryptpassword_ub1 = adap.getConnectParam("password");       						
            							final String password_ub1 = Decrypt(cryptpassword_ub1);
       						
            							new Thread(new Runnable() {
            								public void run() {	   
            									// Запуск диалога загрузки                  	   	        	
            									myHandler.post(new Runnable() { 
            										public void run() {
            			            					try{
            												java.lang.Thread.sleep(100);
            											   } catch (Exception e) { }
            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
            											showDialog(dialogId); 
            											}
            											});																			
            									try {                   					
            										((LoopingThread) loopThread).getHandler().post(new Runnable() 
            										{
            											public void run() 
            											{	
            												// Получение данных из удаленной базы, запись в локальную    											
            												String answer = HttpSelectZehSector(adap, server_ub1, username_ub1, password_ub1);
            												// Вывод на спиннер
            												Cursor cursor = adap.getZehCursor();     											
            												ArrayList<String> arrayList = new ArrayList<String>();
            												arrayList.add(" ");
            												ArrayList<String> arrayList2 = new ArrayList<String>();
            												arrayList2.add(" ");
            												cursor.move(-1); 
            												while (cursor.moveToNext()) 
            												{
            													arrayList.add(cursor.getString(3));	
            												}  
            												final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList);
            												arrayAdapter.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
            												final ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(FirstDBActivity.this, R.layout.spinner_1, arrayList2);
            												arrayAdapter2.setDropDownViewResource(R.layout.spinner_1_dropdown_item);
            												if (answer == "ok") 
            												{
            													myHandler.post(new Runnable() { 
            														public void run() {
            															spinOptionZeh.setAdapter(arrayAdapter);
            															spinOptionSector.setAdapter(arrayAdapter2);
            															int dialogId = DialogFactory.DIALOG_PROGRESS_4;
            															dismissDialog(dialogId);
            															updateZehSectorButton.setEnabled(false);
            															Toast toast = Toast.makeText(context, " Данные для Цеха и Участка получены успешно ", Toast.LENGTH_LONG);  
            															toast.show();
            														}
            													});
            												} else 
            												{
            													myHandler.post(new Runnable() { 
            														public void run() {
            															int dialogId = DialogFactory.DIALOG_PROGRESS_4;
            															dismissDialog(dialogId);
            															try{
            																java.lang.Thread.sleep(100);
            															} catch (Exception e) { } 
            															updateZehSectorButton.setEnabled(true);
            															Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения ", Toast.LENGTH_LONG);
            															toast.setGravity(Gravity.CENTER, 0, 0);
            															toast.show();
            														}
            													});
            												}
            												adap.DeleteAllFromDef(); 
            												adap.close();
            											}
            										});			    									
            							} catch (InterruptedException e) {                   							 
                   						//	Log.e("log_tag", "Ошибка в работе потока "+e.getMessage());
											final String emessage = e.toString();
                   							myHandler.post(new Runnable() { 
        										public void run() {
        										int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                               	     			dismissDialog(dialogId);
                               	     			try{
                               	     				java.lang.Thread.sleep(100);
                               	     				updateZehSectorButton.setEnabled(true);
                               	     				} catch (Exception e) { }   
                               	     			Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения \n"+emessage.toString(), Toast.LENGTH_LONG);
                               	     			toast.setGravity(Gravity.CENTER, 0, 0);
                               	     			toast.show();
        										}
        									});
                   						}                   						            						
                  	   	    }  }).start();
            				
            }
            catch  (Exception ex) {															
								Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения \n "+ex, Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();    			
		   				    } 	
      	 
        	 
            					}         
            				})        
            				
            			.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
            				{             
            				public void onClick(DialogInterface dialog, int id) 
            					{                 
            						updateZehSectorButton.setEnabled(true);
            					}         
            				})	
            			.setView(view3).create().show();
            		break;	
            		
            		case R.id.CheckNewVersionButton:
            			try 
            			{	
								 new Thread(new Runnable() {
			              	   	        public void run() {				              	   	        							
			              	   	        	
			              	   	        	
			              	   	        	// Запуск диалога загрузки                  	   	        	
											myHandler.post(new Runnable() { 
												public void run() {
			  	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
			  	   		        	   				showDialog(dialogId);      	   		        	   			
												}
											});	
											
			              	   	        					try {
																((LoopingThread) loopThread).getHandler().post(new Runnable() 
																{
																	public void run() 
																	{
																		adap.open();
																		final String server = adap.getConnectParam("server");
																		adap.close();
																		// Получаем версию программы на сервере	
																		
																		String[] serverfields = GetVersionFromServer(server);	
																		if (serverfields[0].toString() == "error")
																		{
																			myHandler.post(new Runnable() { 
							            										public void run() {				            											
							            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
							                                   	     				dismissDialog(dialogId);
							                                   	     				Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
							                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
							                                   	     				toast.show();
							            										}
							            									});
																		}
																		else
																		{																			
																		final int versionCode = Integer.parseInt(serverfields[0].toString()); 																			
																		final String versionName = serverfields[1].toString(); 
																		
																		// Получаем версию нашего приложения
																		String[] appfields = GetCurrentVersion();
																		final int currentVersionCode = Integer.parseInt(appfields[0].toString()); 																		
																		final String currentVersionName = appfields[1].toString(); 
																		
																		Log.d("txt versionCode",serverfields[0].toString());
																		Log.d("current versionCode",appfields[0].toString());
																		
																		Log.d("txt versionName",versionName);																																																				
																		Log.d("current versionName",currentVersionName);

																		if (!(serverfields.equals(null)))
																				{
																					// Остановка диалога загрузки
																					myHandler.post(new Runnable() { 
																					public void run() {				            											
						            												int dialogId = DialogFactory.DIALOG_PROGRESS_4;
						                                   	     					dismissDialog(dialogId);				                                   	     				
						                                   	     					Toast toast = Toast.makeText(context, " Поиск новой версии завершен ", Toast.LENGTH_SHORT);  
						                                   	     					toast.show();
																										}
																					});
																																										
																					// Если на сервере более новая версия ?
																					if (versionCode > currentVersionCode)
																					{
																						myHandler.post(new Runnable() { 
																							public void run() 
																							{
																								// Текст диалога
																								final TextView view1 = new TextView(context);            			
																		    		    		view1.setText("Найдена новая версия программы \n Перед обновлением лучше завершить все операции  " +
																		    		    			"\n" +
																									"\n Вы готовы ?" +
																		    		    			"\n");
																		    					view1.setGravity(Gravity.CENTER);
																		    					view1.setTextColor(Color.WHITE);
																		    					view1.setTextSize(18);
																	            				AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstDBActivity.this);
																	        					builder1.setMessage("Внимание!")
																	        					.setCancelable(false)
																	        					.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
																	        					{
																	        					@SuppressWarnings("unchecked")
																								public void onClick(DialogInterface dialog, int id) 
																								{ 	
																	        						// Скачиваем файл программы
																									 new Thread(new Runnable() {
																				              	   	        public void run() {	
																				              	   	        	
																				              	   	        	
																				              	   	        	// Запуск диалога загрузки                  	   	        	
																												myHandler.post(new Runnable() { 
																													public void run() {
																				  	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
																				  	   		        	   				showDialog(dialogId);      	   		        	   			
																													}
																												});	
																												
																				              	   	        					try {
																																	((LoopingThread) loopThread).getHandler().post(new Runnable() 
																																	{
																																		public void run() 
																																		{
																																			// Качаем новый apk																																			
																																			adap.open();							
																											              	   	        	final String server = adap.getConnectParam("server");
																											              	   	        	
																											              	   	        	// Забираем файл с сервера
																											              	   	        	try {
																											              	   	        		
																											              	   	        	downloadFile(server);
																											              	   	        	
																											              	   	        	} catch (Exception e) {
																																				myHandler.post(new Runnable() { 
																								            										public void run() {				            											
																								            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
																								                                   	     				dismissDialog(dialogId);
																								                                   	     				Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
																								                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
																								                                   	     				toast.show();
																								            										}
																								            									});
																																			}
																																			myHandler.post(new Runnable() { 
																																				public void run() {
																																									int dialogId = DialogFactory.DIALOG_PROGRESS_4;
																																										dismissDialog(dialogId); 
																																										Toast toast = Toast.makeText(context, " Обновления получены. Для продолжения нажмите \"ОК\" ", Toast.LENGTH_LONG);  
																																										toast.show();
																																								   }
																																								});
																																			adap.updateLastUpdate(getCurrDate());
																																			final String lastUpdate = adap.getLastUpdate();
																																			myHandler.post(new Runnable() { 
																																					public void run() {
																																										textViewVersionValue.setText(currentVersionName+" ("+currentVersionCode+")");
																																										textViewVersionValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); 
																																										textViewVersionDateValue.setText(lastUpdate);
																																										textViewVersionDateValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
																																									  }
																																								});	
																																			
																																			adap.close();
																																			 // Запуск установщика программы																																			
																																			 InstallApplication();
																																		 																																			 																																			 																	
																																		}
																																	});
																																} catch (InterruptedException e) {
																																	myHandler.post(new Runnable() { 
																					            										public void run() {				            											
																					            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
																					                                   	     				dismissDialog(dialogId);
																					                                   	     				Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
																					                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
																					                                   	     				toast.show();
																					            										}
																					            									});
																																}
																				              	   	        				 }  }).start();	
																	        						
																								}         
																	    						})   
																	    						.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
																	        					{             
																	        						public void onClick(DialogInterface dialog, int id) 
																									{     																	        							
																									}         
																	        					})	
																	        					.setView(view1).create().show();
																							}
																						});	
																					}
																					else
																					{
																						myHandler.post(new Runnable() { 
									            											public void run() {				            											
									            												int dialogId = DialogFactory.DIALOG_PROGRESS_4;
									                                   	     					dismissDialog(dialogId);
									                                   	     					Toast toast = Toast.makeText(context, " У вас установлена самая свежая версия программы ", Toast.LENGTH_LONG);
									                                   	     					//toast.setGravity(Gravity.CENTER, 0, 0); 
									                                   	     					toast.show();
									            											}
									            										});
																					}
																					

																				}
																		// Не удалось вернуть версию
																		else {
																				myHandler.post(new Runnable() { 
							            											public void run() {				            											
							            												int dialogId = DialogFactory.DIALOG_PROGRESS_4;
							                                   	     					dismissDialog(dialogId);
							                                   	     					Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
							                                   	     					//toast.setGravity(Gravity.CENTER, 0, 0);
							                                   	     					toast.show();
							            											}
							            										});
																			 }
																	}
																	}
																});
															} catch (InterruptedException e) {
																myHandler.post(new Runnable() { 
				            										public void run() {				            											
				            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
				                                   	     				dismissDialog(dialogId);
				                                   	     				Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
				                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
				                                   	     				toast.show();
				            										}
				            									});
															}
			              	   	        				 }  }).start();
            			} catch (Exception e) 
            					{
            						Log.e("download",e.getMessage());
            						Toast toast = Toast.makeText(context, "Ошибка закачки\n"+e.getMessage(), Toast.LENGTH_LONG);
            						toast.setGravity(Gravity.CENTER, 0, 0);
        							toast.show();
            					}
            			
            		break;
            		
            		case R.id.SaveOptionButton1:
            			adap.open();   
                		int spinOptionZeh_posit = spinOptionZeh.getSelectedItemPosition(); 
                		int spinOptionSector_posit = spinOptionSector.getSelectedItemPosition();
                		// Найдем id для выбранного цеха
                		Cursor cursorZehId1 = adap.getZehCursor();
                		cursorZehId1.move(spinOptionZeh_posit);
                		String zehIdValue = cursorZehId1.getString(0);
                			Log.d("zehId find value", zehIdValue );                		
                		// Найдем id для выбранного участка
                		Cursor cursorSectorId1 = adap.getSectorByZeh(zehIdValue);
                		cursorSectorId1.move(spinOptionSector_posit);
                		String sectorIdValue = cursorSectorId1.getString(0);
                			Log.d("sectorId find value", sectorIdValue);                		
                		// Сохраним результаты
                		adap.saveZehSectorParams(zehIdValue, sectorIdValue);  
            			CharSequence text = " Выбранные Цех и Участок сохранены ";  
            			int duration = Toast.LENGTH_SHORT;
            			Toast toast = Toast.makeText(context, text, duration);  
            		    toast.show();            		                			
    	   				// Обновление объектов
   						final String server_sob1 = adap.getConnectParam("server");
            		    final String username_sob1 = adap.getConnectParam("username");
   						String cryptpassword_sob1 = adap.getConnectParam("password");       						
   						final String password_sob1 = Decrypt(cryptpassword_sob1);
   						final String startwith_sob1 = adap.getConnectParam("SECTOR");
        			//	final Handler myHandler = new Handler();
        					Log.d("selectObjects params", server_sob1+" "+username_sob1+" "+password_sob1+" "+startwith_sob1); 

                      	 new Thread(new Runnable() {
              	   	        public void run() {	   
              	   	        	// Запуск диалога загрузки                  	   	        	
								myHandler.post(new Runnable() { 
									public void run() {
  	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
  	   		        	   				showDialog(dialogId);      	   		        	   			
									}
								});																			
               					try {                   					
									((LoopingThread) loopThread).getHandler().post(new Runnable() 
									{
										public void run() 
										{	
											// Получение данных из удаленной базы, запись в локальную    											
											String answer1 = HttpSelectObjects(adap, server_sob1, username_sob1, password_sob1, startwith_sob1);
							        		String zehid = adap.getConnectParam("ZEH");
							        		String sectorid = adap.getConnectParam("SECTOR");		
							        		final String zeh = adap.getZehSectorName(zehid);
							        		final String sector = adap.getZehSectorName(sectorid);
							        		// Заполнение таблицы для ускорения поиска Позиций
							        		adap.insertFSearch();
							        		//String answer2 = adap.getFSearch("5168");
							        		//	Log.d("empty parentId step", answer2);
							       	        // Обновление главной панели
							       	        myHandler.post(new Runnable() { 
							    				public void run() {
							    						// Registration Date   	        	
														textViewDateValue.setText(" "+getCurrDate());    		   	        	
														try {
																textViewSmenaValue.setText(getShift());
															} catch (java.text.ParseException e) {}						    		        		
							    		        		String zeh2 = zeh.substring(zeh.lastIndexOf("х ")+1);
							    		        		String sector2 = sector.substring(sector.lastIndexOf("к ")+1);
							    		   	        	textViewZehValue.setText(" "+zeh2);
							    		   	        	textViewSectorValue.setText(" "+sector2); 
							    		   	        	textViewSmenaValue.setText(" "+"A");
							    		   	        	
	                               	     				Toast toast = Toast.makeText(context, " Объекты получены успешно ", Toast.LENGTH_LONG);  
	                               	     				toast.show();
							    					}
							    				});
							       	        String answer2 = HttpSelectPart(adap, server_sob1, username_sob1, password_sob1);
							       	        String answer3 = HttpSelectEquip(adap, server_sob1, username_sob1, password_sob1);
							       	        // Извещаем о прогрессе
							       	        myHandler.post(new Runnable() { 
							    				public void run() {
							    						Toast toast = Toast.makeText(context, " Дефекты получены успешно (1/2)", Toast.LENGTH_LONG);  
	                               	     				toast.show();
							    					}
							    				});
							       	        String answer4 = HttpSelectCause(adap, server_sob1, username_sob1, password_sob1);
							       	        // Извещаем о прогрессе
							       	        myHandler.post(new Runnable() { 
							    				public void run() {
							    						Toast toast = Toast.makeText(context, " Дефекты получены успешно (2/2)", Toast.LENGTH_LONG);  
	                               	     				toast.show();
	                               	     				
							    					}
							    				});
							       	        
							       	        // Глобальные переменные в исходное состояние
							       	        parentId = "empty";
							       	     	aggrId = "empty";
							       	    
											if (answer1 == "ok" && answer2 == "ok" && answer3 == "ok" && answer4 == "ok") 
											{
												String startwith = adap.getConnectParam("SECTOR");
												setAggrSpinner(adap, myHandler, startwith);
	                   							myHandler.post(new Runnable() { 
	        										public void run() {
	        											Spinner spinAggr = (Spinner) findViewById(R.id.SpinAggr);
	        											Spinner spinOptionZeh = (Spinner) findViewById(R.id.SpinOptionZeh);
	        											Spinner spinOptionSector = (Spinner) findViewById(R.id.SpinOptionSector);
	        											Button saveOptionButton1 = (Button)findViewById(R.id.SaveOptionButton1);
	        											spinOptionZeh.setEnabled(false);
	        											spinOptionSector.setEnabled(false);
	        											saveOptionButton1.setEnabled(false);
	        											updateZehSectorButton.setEnabled(true);
	        											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
	                               	     				dismissDialog(dialogId);  
	                               	     				updateZehSectorButton.setEnabled(true);
	                               	     				Toast toast = Toast.makeText(context, "Все данные получены успешно ", Toast.LENGTH_LONG);
	                               	     				toast.setGravity(Gravity.CENTER, 0, 0);
	                               	     				toast.show();
	        										}
	        									});
											} else 
											{
												myHandler.post(new Runnable() { 
            										public void run() {
            											updateZehSectorButton.setEnabled(false);
            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                                   	     				dismissDialog(dialogId);
                                   	     				updateZehSectorButton.setEnabled(true);
                                   	     				Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения ", Toast.LENGTH_LONG);
                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
                                   	     				toast.show();
            										}
            									});
											}
											    											
											adap.close();
										}
									});			    									
               						} catch (InterruptedException e) {                   							 
               						//	Log.e("log_tag", "Ошибка в работе потока "+e.getMessage());
										final String emessage = e.toString();
               							myHandler.post(new Runnable() { 
    										public void run() {
    											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
                           	     				dismissDialog(dialogId);
                           	     				try{
                           	     					java.lang.Thread.sleep(100);
                           	     					updateZehSectorButton.setEnabled(true);
                           	     					} catch (Exception e) { }   
                           	     				Toast toast = Toast.makeText(context, " В ходе загрузки произошла ошибка! \n Проверьте параметры подключения \n"+emessage.toString(), Toast.LENGTH_LONG);
                           	     				toast.setGravity(Gravity.CENTER, 0, 0);
                           	     				toast.show();
    										}
    									});

               						}                   						            						

              	   	        	}  }).start();
            			
            		break;
            		
            		case R.id.imageButton1:
            			//	Активити с камерой
                 		Intent CameraActivity = new Intent(FirstDBActivity.this, CameraActivity.class);
                 		FirstDBActivity.this.startActivityForResult(CameraActivity, CAMERA);                 		          			    
            		break;	
            		
            		case R.id.imageButton2:
            			// галерея
            			/*
            			// рабочий вариант
            			Intent intent = new Intent();
            			intent.setAction(Intent.ACTION_VIEW);
            			intent.setDataAndType(Uri.parse("file://" + "/sdcard/DCIM/regdef_18/REGDEF_16082012_1845_209.jpg"), "image/*");
            			FirstDBActivity.this.startActivity(intent);
            			
            			// тоже рабочий
            			Intent intent = new Intent();
            			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            			intent.setType("image/*");
            			FirstDBActivity.this.startActivity(intent); 
            			             			
            			*/
            			adap.open();
            			String def_id_m = adap.getNextId("defects");
            			if (def_id_m == null) def_id_m = "1";
            			adap.close();
            			try {
								MediaScanner ms = new MediaScanner();
								//if (!def_id_m.equals("null")) 
								ms.startScan(FirstDBActivity.this, def_id_m, myHandler, "first");
									//else ms.startScan(FirstDBActivity.this, "null", myHandler, "first");														
    					}
    					catch (Exception ex) 
    					{    						
    						Toast toast1 = Toast.makeText(context, "Ошибка доступа к фотографии\n"+ex.getMessage(), Toast.LENGTH_LONG);
    						toast1.setGravity(Gravity.CENTER, 0, 0);
    						toast1.show();
    					}
            			
                	break;
            }
		}
		catch  (Exception ex) {
			CharSequence text = ex.getMessage();  
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, "Ошибка работы приложения!\n"+text, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
	 	  }
        }; 
    
    };

    public String HttpInsert(DBAdapter adap, Cursor cursor, Context context, String server, String username, String password) throws java.text.ParseException
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    	
    	// Проверим, есть что отправлять
    	int nosend = adap.getNoSendDefectsCount(0);
    	String answer = null;
    	if (nosend == 1) answer = "ok";
    	else answer = " Записей для отправки не обнаружено ";
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    	cursor.move(-1); 
    	while (cursor.moveToNext()) 
    	{    
    		// Время регистрации
    		String regdate_mod = cursor.getString(2);
    		String regtime_mod = cursor.getString(3);
    		String regdatetime_mod = cursor.getString(4);
    		String regdatetime_mod_2 = cursor.getString(15);
    		// Цех - Участок - Агрегат - Позиция
    		String zeh = cursor.getString(5);
    		String sector = cursor.getString(6);
    		String aggr = cursor.getString(7);
    		String posit = cursor.getString(8);
    		// Смена    		
    		String smena = cursor.getString(9);  
    		// Часть - Оборудование - Причина - Описание
    		String part = cursor.getString(11); 
    		String equip = cursor.getString(12); 
    		String cause = cursor.getString(13); 
    		String descr = cursor.getString(14);
     		// Получим id записи о дефекте
     		String defect = cursor.getString(0);
     		
     		     		
    		// Ввод логина и пароля
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password)); 
            // Время регистрации
        	nameValuePairs.add(new BasicNameValuePair("regdate_mod", regdate_mod ));
        	nameValuePairs.add(new BasicNameValuePair("regtime_mod", regtime_mod ));
        	nameValuePairs.add(new BasicNameValuePair("regdatetime_mod", regdatetime_mod ));
        	nameValuePairs.add(new BasicNameValuePair("regdatetime_mod_2", regdatetime_mod_2 ));
        	// Цех - Участок - Агрегат - Позиция
        	nameValuePairs.add(new BasicNameValuePair("zeh", zeh));
        	nameValuePairs.add(new BasicNameValuePair("sector", sector)); 
        	nameValuePairs.add(new BasicNameValuePair("aggr", aggr)); 
        	nameValuePairs.add(new BasicNameValuePair("posit", posit)); 
        	// Смена
        	nameValuePairs.add(new BasicNameValuePair("smena", cursor.getString(5) ));        	
        	nameValuePairs.add(new BasicNameValuePair("part", part ));
        	nameValuePairs.add(new BasicNameValuePair("equip", equip ));
        	nameValuePairs.add(new BasicNameValuePair("cause", cause ));
        	nameValuePairs.add(new BasicNameValuePair("descr", descr ));        	
        	
        	Log.d("insert_defects", " regdate_mod "+regdate_mod+" regtime_mod "+regtime_mod+" regdatetime_mod "+regdatetime_mod+" regdatetime_mod_2"+regdatetime_mod_2
        		  +" zeh "+zeh+" sector "+sector+" aggr "+aggr+" posit "+posit+" part "+part+" equip "+equip+" cause "+cause+" descr "+descr+" defect id "+defect);  
        	
        	String image_exist = cursor.getString(cursor.getColumnIndex("IMAGE_EXIST"));
        	Log.d("image_exist", "image_exist= "+image_exist);
        	
        	if ( image_exist.equals("1"))
        	{
        	// Загрузка фотографий
 	    	
 	    	String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+defect; 	    	
 	    	Log.d("dirlink string", link);
 	    	
 	    	File dir = new File(link);
 	    	if (dir.exists())
 	    		{
 	    			try { 	    					 	    				
 	    					SimpleFTP ftp = new SimpleFTP();
 	    					ftp.connect(server, 21, username, password); 
 	    					Log.d("connect string", server + " / " + username + " / " + password);
 	    				    ftp.bin();
 	    				   /* 
 	    				    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 	    				    Date date = formatter.parse(cursor.getString(cursor.getColumnIndex("regdate_mod")));
 	    				    int year = date.getYear() + 1900;
 	    				    int month =  date.getMonth() + 1;
 	    				    String tag = "0";
 	    				    if (month >= 10) tag = ""; 	    				    	
 	    				    Log.d("ftp dir", Integer.toString(year) + "/"+ tag + Integer.toString(month));
 	    				   */ 
 	    				    ftp.cwd("temp");
	    				    
 	    					String[] list = dir.list();
 	    					JSONArray mJSONArray = new JSONArray(Arrays.asList(list));
 	    					
 	    					nameValuePairs.add(new BasicNameValuePair("image_count", Integer.toString(list.length) ));	
 	    					nameValuePairs.add(new BasicNameValuePair("json", mJSONArray.toString() ));
 	    					
 	    					for(int i=0;i<list.length;i++)
 	    					{ 	    						 	    						
 	    						String image = link + "/" + list[i];
 	    						Log.d("image string", image);
 	    						ftp.stor(new File(image));
 	    					}
 	    					
 	    					ftp.disconnect(); 	    					
 	    				} 	    			
 	    			catch (IOException e) {
 	    				
 	    				 answer = "connection error";
 	        	         Log.e("log_tag", "Error in ftp connection"+e.toString());     
 	    			}
 	    			
 	    		}
        	} else nameValuePairs.add(new BasicNameValuePair("image_count", "0" ));
 	    	
 	    	
        	try{        		         		
        	     HttpClient httpclient = new DefaultHttpClient();
        	     HttpPost httppost = new HttpPost("http://"+server+"/android/insert_defects.php");
        	     
        	     //HttpPost httppost = new HttpPost("http://"+server+"/android/insert_defects_copy.php");
        	     
        	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        	     HttpResponse response = httpclient.execute(httppost);
        	     HttpEntity entity = response.getEntity();
        	     is = entity.getContent();
        	     // Пометим, как отправленный
        	     adap.AfterSaveDefects(defect); 
        	     answer = "ok";
        	     
        	   }
        	catch(Exception e)
        	   		{    
        		     answer = "connection error";
        	         Log.e("log_tag", "Error in http connection"+e.toString());           	           	    
        	   		}    
        	
    	}
    	
    	return answer;
     }    
    
    public String HttpSelect()
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    	
    	 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	//http post
    	try{
    	     HttpClient httpclient = new DefaultHttpClient();
    	//     HttpPost httppost = new HttpPost("http://mon.hq.kcck/regdef/select_curdate.php");
    	     HttpPost httppost = new HttpPost("http://udp.zmu/android/select_curdate.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();
    	     }catch(Exception e){
    	         Log.e("log_tag", "Error in http connection"+e.toString());
    	    }
    	//convert response to string
    	try{
    	      BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");

    	       String line="0";
    	       while ((line = reader.readLine()) != null) {
    	                      sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	        }catch(Exception e){
    	              Log.e("log_tag", "Error converting result "+e.toString());
    	        }
    	//paring data
    	int ct_id;
    	String ct_name = null;
    	try{
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      for(int i=0;i<jArray.length();i++){
    	             json_data = jArray.getJSONObject(i);
    	          //   ct_id=json_data.getInt("CITY_ID");
    	             ct_name=json_data.getString("now");
    	         }
    	      }
    	      catch(JSONException e1){
    	    	  Toast.makeText(getBaseContext(), "No City Found" ,Toast.LENGTH_LONG).show();
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}
    	return ct_name;
    	
    }
    
 // Запрос структуры Цех-Участок из удаленной базы
    private String HttpSelectObjects(DBAdapter adap, String server, String username, String password, String startwith)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString();
    	String d = startwith.toString();
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));   
        nameValuePairs.add(new BasicNameValuePair("startwith", d));  
    	//http post
    	try {
    	     HttpClient httpclient = new DefaultHttpClient();
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/select_objects.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();

    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) 
    	        {
    	          sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	   } 
    	catch (Exception e) {
    	          Log.e("log_tag", "Error converting result "+e.toString());
    	                    }
    	//parsing data
    	String _id = null; 
    	String parent = null;
    	String level = null;
    	String name = null;
    	String is_leaf = null;
    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      // Очистим таблицу устаревших данных
    	      adap.truncObjects();
    	      for(int i=0;i<jArray.length();i++)
    	         {
    	             json_data = jArray.getJSONObject(i);
    	             // Получаем данные
    	             _id=json_data.getString("_id");
    	             parent=json_data.getString("parent");
    	             level=json_data.getString("level");
    	             name=json_data.getString("name");
    	             is_leaf=json_data.getString("is_leaf");
    	             // Вставляем в локальную таблицу
    	             adap.insertObjects(_id, parent, level, name, is_leaf);
    	             Log.d("value", _id+" "+parent+" "+level+" "+name+" "+is_leaf );   
    	         }
    	   }
    	      catch(JSONException e1){
    	    	  Log.e("log_tag", "DB connect error "+server+username+password+"   "+e1.toString());    	    	
    	    	  return "error";
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}

	     }
	catch(Exception e) {
		  	Log.e("log_tag", "DB connect error "+server+username+password+"   "+e.toString());    	    	
	         return "error";
	    			   }
    	return "ok";
    }
   
    // Запрос "Неисправная часть"
    private String HttpSelectPart(DBAdapter adap, String server, String username, String password)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString();
    //	String d = startwith.toString();
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    //    nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));   
    //    nameValuePairs.add(new BasicNameValuePair("startwith", d));  
    	//http post
    	try {
    	     HttpClient httpclient = new DefaultHttpClient();
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/select_d_part.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();

    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) 
    	        {
    	          sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	   } 
    	catch (Exception e) {
    	          Log.e("log_tag", "Error converting result "+e.toString());
    	                    }
    	//parsing data
    	String _id = null; 
    	String parent = null;
    	String level = null;
    	String name = null;
    	String is_leaf = null;
    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      // Очистим таблицу устаревших данных
    	      adap.truncPart();
    	      for(int i=0;i<jArray.length();i++)
    	         {
    	             json_data = jArray.getJSONObject(i);
    	             // Получаем данные
    	             _id=json_data.getString("_id");
    	             parent=json_data.getString("parent");
    	             level=json_data.getString("level");
    	             name=json_data.getString("name");
    	             is_leaf=json_data.getString("is_leaf");
    	             // Вставляем в локальную таблицу
    	             adap.insertPart(_id, parent, level, name, is_leaf);
    	             Log.d("d_part value", _id+" "+parent+" "+level+" "+name+" "+is_leaf );   
    	         }
    	   }
    	      catch(JSONException e1){
    	    	  Log.e("log_tag", "DB connect error "+server+username+password+"   "+e1.toString());    	    	
    	    	  return "error";
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}

	     }
	catch(Exception e) {
		  	Log.e("log_tag", "DB connect error "+server+username+password+"   "+e.toString());    	    	
	         return "error";
	    			   }
    	return "ok";
    }
    
    // Запрос "Оборудование"
    private String HttpSelectEquip(DBAdapter adap, String server, String username, String password)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    //	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString();
    //	String d = startwith.toString();
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    //    nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));   
   //     nameValuePairs.add(new BasicNameValuePair("startwith", d));  
    	//http post
    	try {
    	     HttpClient httpclient = new DefaultHttpClient();
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/select_d_equip.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();

    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) 
    	        {
    	          sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	   } 
    	catch (Exception e) {
    	          Log.e("log_tag", "Error converting result "+e.toString());
    	                    }
    	//parsing data
    	String _id = null; 
    	String parent = null;
    	String level = null;
    	String name = null;
    	String is_leaf = null;
    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      // Очистим таблицу устаревших данных
    	      adap.truncEquip();
    	      for(int i=0;i<jArray.length();i++)
    	         {
    	             json_data = jArray.getJSONObject(i);
    	             // Получаем данные
    	             _id=json_data.getString("_id");
    	             parent=json_data.getString("parent");
    	             level=json_data.getString("level");
    	             name=json_data.getString("name");
    	             is_leaf=json_data.getString("is_leaf");
    	             // Вставляем в локальную таблицу
    	             adap.insertEquip(_id, parent, level, name, is_leaf);
    	             Log.d("d_equip value", _id+" "+parent+" "+level+" "+name+" "+is_leaf );   
    	         }
    	   }
    	      catch(JSONException e1){
    	    	  Log.e("log_tag", "DB connect error "+server+username+password+"   "+e1.toString());    	    	
    	    	  return "error";
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}

	     }
	catch(Exception e) {
		  	Log.e("log_tag", "DB connect error "+server+username+password+"   "+e.toString());    	    	
	         return "error";
	    			   }
    	return "ok";
    }
    
    // Запрос "Причина"
    private String HttpSelectCause(DBAdapter adap, String server, String username, String password)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
   // 	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString();
    //	String d = startwith.toString();
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    //    nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));   
    //    nameValuePairs.add(new BasicNameValuePair("startwith", d));  
    	//http post
    	try {
    	     HttpClient httpclient = new DefaultHttpClient();
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/select_d_cause.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();

    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) 
    	        {
    	          sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	   } 
    	catch (Exception e) {
    	          Log.e("log_tag", "Error converting result "+e.toString());
    	                    }
    	//parsing data
    	String _id = null; 
    	String parent = null;
    	String level = null;
    	String name = null;
    	String is_leaf = null;
    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      // Очистим таблицу устаревших данных
    	      adap.truncCause();
    	      for(int i=0;i<jArray.length();i++)
    	         {
    	             json_data = jArray.getJSONObject(i);
    	             // Получаем данные
    	             _id=json_data.getString("_id");
    	             parent=json_data.getString("parent");
    	             level=json_data.getString("level");
    	             name=json_data.getString("name");
    	             is_leaf=json_data.getString("is_leaf");
    	             // Вставляем в локальную таблицу
    	             adap.insertCause(_id, parent, level, name, is_leaf);
    	             Log.d("d_cause value", _id+" "+parent+" "+level+" "+name+" "+is_leaf );   
    	         }
    	   }
    	      catch(JSONException e1){
    	    	  Log.e("log_tag", "DB connect error "+server+username+password+"   "+e1.toString());    	    	
    	    	  return "error";
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}

	     }
	catch(Exception e) {
		  	Log.e("log_tag", "DB connect error "+server+username+password+"   "+e.toString());    	    	
	         return "error";
	    			   }
    	return "ok";
    }
    
    // Запрос структуры Цех-Участок из удаленной базы
    private String HttpSelectZehSector(DBAdapter adap, String server, String username, String password)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;
    	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString();     	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));        
    	//http post
    	try {
    	     HttpClient httpclient = new DefaultHttpClient();
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/select_zehsector.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();

    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) 
    	        {
    	          sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	   } 
    	catch (Exception e) {
    	          Log.e("log_tag", "Error converting result "+e.toString());
    	                    }
    	//parsing data
    	String _id = null; 
    	String parent = null;
    	String level = null;
    	String name = null;
    	String is_leaf = null;
    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      // Очистим таблицу устаревших данных
    	      adap.truncZehSector();
    	      for(int i=0;i<jArray.length();i++)
    	         {
    	             json_data = jArray.getJSONObject(i);
    	             // Получаем данные
    	             _id=json_data.getString("_id");
    	             parent=json_data.getString("parent");
    	             level=json_data.getString("level");
    	             name=json_data.getString("name");
    	             is_leaf=json_data.getString("is_leaf");
    	             // Вставляем в локальную таблицу
    	             adap.insertZehSector(_id, parent, level, name, is_leaf);
    	             Log.d("value", _id+" "+parent+" "+level+" "+name+" "+is_leaf );   
    	         }
    	   }
    	      catch(JSONException e1){
    	    	  Log.e("log_tag", "DB connect error "+server+username+password+"   "+e1.toString());    	    	
    	    	  return "error";
    	      } catch (ParseException e1) {
    				e1.printStackTrace();
    		}

	     }
	catch(Exception e) {
		  	Log.e("log_tag", "DB connect error "+server+username+password+"   "+e.toString());    	    	
	         return "error";
	    			   }
    	return "ok";
    }
    
    // Проверка логина, пароля, сервера
    private String HttpSelectPwd(String server, String username, String password)
    {
    	JSONArray jArray;
    	String result = null;
    	InputStream is = null;
    	StringBuilder sb=null;

    	String a = server.toString();
    	String b = username.toString();
    	String c = password.toString(); 
    	
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    //    nameValuePairs.add(new BasicNameValuePair("server", a));
        nameValuePairs.add(new BasicNameValuePair("username", b));
        nameValuePairs.add(new BasicNameValuePair("password", c));
        
    	//http post
    	try{
    	     HttpClient httpclient = new DefaultHttpClient();
    	//     HttpPost httppost = new HttpPost("http://"+server+"/android/test_connect.php");
    	     HttpPost httppost = new HttpPost("http://"+server+"/android/test_connect.php");
    	     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	     HttpResponse response = httpclient.execute(httppost);
    	     HttpEntity entity = response.getEntity();
    	     is = entity.getContent();
    	     }catch(Exception e){
    	         Log.e("log_tag", "Error in http connection"+e.toString());
    	    }
    	//convert response to string
    	try{
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
    	       sb = new StringBuilder();
    	       sb.append(reader.readLine() + "\n");
    	       String line="0";
    	       while ((line = reader.readLine()) != null) {
    	                      sb.append(line + "\n");
    	        }
    	        is.close();
    	        result=sb.toString();
    	        }
    	catch(Exception e){
    	              Log.e("log_tag", "Error converting result "+e.toString());
    	        }
    	//parsing data
    	int ct_id;
    	String ct_name = "error";    	
    	try{    		
    	      jArray = new JSONArray(result);
    	      JSONObject json_data=null;
    	      for(int i=0;i<jArray.length();i++){
    	             json_data = jArray.getJSONObject(i);
    	             ct_name=json_data.getString("now");
    	         }
    	   }
    	catch(JSONException e1){
    	    	  Log.e("log_tag", e1.toString());
    	      } 
    	catch (ParseException e1) {
    				e1.printStackTrace();
    		}
    	return ct_name;    	
    }
         
    //*Шифрование пароля
    // Шифруем пароль   
    private String Encrypt(String cleartext)
    {
    	try {
    		SimpleCrypto simpleCrypto = new SimpleCrypto();
			@SuppressWarnings("static-access")
			String cryptpassword = simpleCrypto.encrypt("GodBlessMechanical", cleartext);		
			return cryptpassword;
			} catch (Exception e) {
				e.printStackTrace();
				}    	
    	return "";
    }
    // Рассшифровываем пароль
    private String Decrypt(String cryptpassword)
    {
    	try {
    		SimpleCrypto simpleCrypto = new SimpleCrypto();
			@SuppressWarnings("static-access")
			String cleartext = simpleCrypto.decrypt("GodBlessMechanical", cryptpassword);		
			return cleartext;
			} catch (Exception e) {
				e.printStackTrace();
				}
    	
    	return "";
    }
    // Выставляем СМЕНУ
    private String getShift() throws java.text.ParseException
    {
    	// Данные о времени
		long msTime = System.currentTimeMillis();  
		Date curDateTime = new Date(msTime);
		int hour = curDateTime.getHours();
		// Формат представления даты для парсинга
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm");  				
		// Значения СМЕНЫ
		String shift_code_1 = " А ";
		String shift_code_2 = " Б ";
		String shift_code_3 = " В ";
		String shift_code_4 = " Г ";		
	
		// СМЕНА в пределах 8-20
		if (hour >= 8 && hour <= 19)
		{
			if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("06.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0 
					|| 
				 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("07.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_1;				
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("02.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("03.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				
				return shift_code_2;
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("31.01.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("01.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_3;
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("04.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("05.02.2012 08:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_4;
			}
		}
		else 
		{
			if (hour >= 0 && hour <= 7)
			{
				// Добавляем 20 часов, вычитаем сутки
				curDateTime = new Date( (msTime + (20 * 60 * 60 * 1000))-(24 * 60 * 60 * 1000) );
			}
			
			if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("01.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0 
					|| 
				 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("02.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_1;				
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("05.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					 ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("06.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_2;
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("03.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					((int) Math.floor(  (curDateTime.getTime() - formatter.parse("04.02.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_3;
			}
			else 
				if ( ((int) Math.floor(  (curDateTime.getTime() - formatter.parse("30.01.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0 
						|| 
					((int) Math.floor(  (curDateTime.getTime() - formatter.parse("31.01.2012 20:00").getTime())/(1000*60*60*24) )) % 8 == 0  )
			{
				return shift_code_4;
			}
			
			
		}
		return "";
    }
    
    public void downloadFile(String server) 
    {
    	
    try { 
        	
        	File sdCard = Environment.getExternalStorageDirectory();                         
            File dir = new File (sdCard.getAbsolutePath()+"/Download/regdef"); 
            dir.mkdirs(); 
            Log.d("Downloader", sdCard.getAbsolutePath()+"\n"+dir); 
            
            URL u = new URL("http://"+server+"/android/regdef.apk"); 
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET"); 
            c.setDoOutput(true);        
          
            c.connect();            
            
            File file = new File(dir, "regdef.apk"); 
            FileOutputStream f = new FileOutputStream(file); 
  
            InputStream in = c.getInputStream(); 
 
            byte[] buffer = new byte[1024]; 
            int len1 = 0; 
            while ((len1 = in.read(buffer)) > 0) { 
                f.write(buffer, 0, len1); 
            } 
            
            f.close(); 
            
        } catch (Exception e) { 
            Log.e("Downloader", e.getMessage()); 
        } 

    } 
    
    public String[] GetVersionFromServer(String server) 
    { 
    	ArrayList strings = new ArrayList();
    	strings.add("error");
    	String[] fields = (String[]) strings.toArray(new String[strings.size()]);
    	
      try { 
            URL u = new URL("http://"+server+"/android/Version.txt"); 
 
            HttpURLConnection c = (HttpURLConnection) u.openConnection();            
            c.setRequestMethod("GET"); 
            c.setDoOutput(true); 
            try {
            		c.connect(); 
            InputStream in = c.getInputStream(); 
 
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
 
            byte[] buffer = new byte[1024];
            int len1 = 0; 
            while ( (len1 = in.read(buffer)) != -1 )  
            {                
                baos.write(buffer,0, len1); 
            } 
 
            String temp = "";      
            String s = baos.toString();
 
            for (int i = 0; i < s.length(); i++) 
            {                
                i = s.indexOf("=") + 1;  
                while (s.charAt(i) == ' ') 
                { 
                    i++; // Move to Next. 
                } 
                while (s.charAt(i) != ';' /*&& (s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == 'a')*/) 
                { 
                    temp = temp.toString().concat(Character.toString(s.charAt(i))) ; 
                    i++; 
                } 
                // 
                s = s.substring(i); 
                temp = temp + " "; 
            } 
            fields = temp.split(" "); 
            baos.close(); 
            } 
            catch (Exception e)
            {	
            	Log.e("connect",e.getMessage()); 
            	
            }
        } 
        catch (MalformedURLException e) { 
            Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_SHORT).show(); 
            e.printStackTrace(); 
        } catch (IOException e) {            
            e.printStackTrace(); 
            Toast.makeText(getApplicationContext(), "Error." + e.getMessage(), Toast.LENGTH_SHORT).show(); 
        }
      
      	return fields;
    }
    
    public String[] GetCurrentVersion()
    {
    	PackageInfo info = null;
    	ArrayList strings = new ArrayList(); 
    	PackageManager manager = this.getPackageManager();
    	try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e("package",e.getMessage());
		}
    	String versionCode = Integer.toString(info.versionCode);
    	String versionName = info.versionName;
    	
    	strings.add(versionCode);
    	strings.add(versionName);
    	
    	String[] fields = (String[]) strings.toArray(new String[strings.size()]); 
    	return fields;
    }
    
    public void CheckVersion(final Handler myHandler, final DBAdapter adap,  final Context context, final Thread loopThread) 
    
    {
    
    final TextView textViewVersionValue = (TextView) findViewById(R.id.TextViewVersionValue);
    final TextView textViewVersionDateValue = (TextView) findViewById(R.id.TextViewVersionDateValue);
    
    adap.open();
	final String server = adap.getConnectParam("server");
	adap.close();
	// Получаем версию программы на сервере
	try {
	String[] serverfields = GetVersionFromServer(server);	
	
	if (serverfields[0].toString() == "error")
	{
		
		myHandler.post(new Runnable() { 
			public void run() {				            											
    				Toast toast = Toast.makeText(context, " В ходе проверки обновлений произошла ошибка ", Toast.LENGTH_LONG);    			
    				toast.show();
			}
		});
		
	}
	else
	{

	final int versionCode = Integer.parseInt(serverfields[0].toString()); 																		
	final String versionName = serverfields[1].toString(); 
	// Получаем версию нашего приложения
	String[] appfields = GetCurrentVersion();
	final int currentVersionCode = Integer.parseInt(appfields[0].toString()); 																		
	final String currentVersionName = appfields[1].toString(); 
		
	Log.d("txt versionCode",serverfields[0].toString());
	Log.d("current versionCode",appfields[0].toString());
	
	Log.d("txt versionName",versionName);																																																				
	Log.d("current versionName",currentVersionName);	
    	
	if (versionCode > currentVersionCode)
	{
		Log.d("check","1.");	
		myHandler.post(new Runnable() { 
			public void run() 
			{
				// Текст диалога
				final TextView view1 = new TextView(context);            			
	    		view1.setText("Найдена новая версия программы \n Перед обновлением лучше завершить все операции  " +
	    			"\n" +
					"\n Вы готовы ?" +
	    			"\n");
				view1.setGravity(Gravity.CENTER);
				view1.setTextColor(Color.WHITE);
				view1.setTextSize(18);
				AlertDialog.Builder builder1 = new AlertDialog.Builder(FirstDBActivity.this);
				builder1.setMessage("Внимание!")
				.setCancelable(false)
				.setPositiveButton(" Да ", new DialogInterface.OnClickListener() 
				{
				@SuppressWarnings("unchecked")
				public void onClick(DialogInterface dialog, int id) 
				{ 	
					// Скачиваем файл программы
					 new Thread(new Runnable() {
              	   	        public void run() {	
              	   	        	
              	   	        	
              	   	        	// Запуск диалога загрузки                  	   	        	
								myHandler.post(new Runnable() { 
									public void run() {
  	            		 				int dialogId = DialogFactory.DIALOG_PROGRESS_4;
  	   		        	   				showDialog(dialogId);      	   		        	   			
									}
								});	
								
              	   	        					try {
													((LoopingThread) loopThread).getHandler().post(new Runnable() 
													{
														public void run() 
														{
															// Качаем новый apk																																			
															adap.open();							
							              	   	        	final String server = adap.getConnectParam("server");
							              	   	        	
							              	   	        	
							              	   	        	downloadFile(server);
							              	   	        	
															myHandler.post(new Runnable() { 
																public void run() {
																					int dialogId = DialogFactory.DIALOG_PROGRESS_4;
																						dismissDialog(dialogId); 
																						Toast toast = Toast.makeText(context, " Обновления получены. Для продолжения нажмите \"ОК\" ", Toast.LENGTH_LONG);  
																						toast.show();
																				   }
																				});
															
															 adap.updateLastUpdate(getCurrDate());															 
															 final String lastUpdate = adap.getLastUpdate();
															 myHandler.post(new Runnable() { 
																	public void run() {
																						textViewVersionValue.setText(currentVersionName+" ("+currentVersionCode+")");
																						textViewVersionValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); 
																						textViewVersionDateValue.setText(lastUpdate);
																						textViewVersionDateValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
																					  }
																				});	
									    		   	         
															 adap.close();
															 // Запуск установщика программы																																			
															 InstallApplication();
															 																																			 
															 
															 //adap.close();
														 																																			 																																			 																	
														}
													});
												} catch (InterruptedException e) {
													myHandler.post(new Runnable() { 
	            										public void run() {				            											
	            											int dialogId = DialogFactory.DIALOG_PROGRESS_4;
	                                   	     				dismissDialog(dialogId);
	                                   	     				Toast toast = Toast.makeText(context, " В ходе обновления программы произошла ошибка ", Toast.LENGTH_LONG);
	                                   	     				toast.setGravity(Gravity.CENTER, 0, 0);
	                                   	     				toast.show();
	            										}
	            									});
												}
              	   	        				 }  }).start();	
					
				}         
				})   
				.setNegativeButton(" Нет ", new DialogInterface.OnClickListener() 
				{             
					public void onClick(DialogInterface dialog, int id) 
					{     																	        							
					}         
				})	
				.setView(view1).create().show();
			}
		});	
	}
	
	}
	
	}
	catch (Exception ex)
	{
		myHandler.post(new Runnable() { 
			public void run() {				            											
    				Toast toast = Toast.makeText(context, " В ходе проверки обновлений произошла ошибка ", Toast.LENGTH_LONG);    			
    				toast.show();
			}
		});
	}
    }
    
    public void InstallApplication() 
    {    
    	File sdCard = Environment.getExternalStorageDirectory();                         
        File dir = new File (sdCard.getAbsolutePath()+"/Download/regdef"); 
        String apkName = "/regdef.apk";
        /*
        Uri packageURI = Uri.parse("regdef"); 
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, packageURI); 
		*/
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Log.d("install", dir+apkName);
        intent.setDataAndType 
        (Uri.fromFile(new File(dir+apkName) ), "application/vnd.android.package-archive"); 

        startActivity(intent);   
    }
      
    /*
    private int checked(SparseBooleanArray vArray) 
    { int vCounter = 0;     
    for(int i=0;i<vArray.size(); i++)        
    	if(vArray.valueAt(i))             
    		vCounter++;     
    return vCounter;  } 
   */
    
    private void selectNone(ListView lv) 
    {
       // ListView lv = getListView;
        for (int i = 0; i < lv.getCount(); i++) {
            lv.setItemChecked(i, false);
        }
    }
    
    private int deleteImages(Context context, DBAdapter adap) 
    {
   		Cursor cursor = adap.getImagesByStatus("temp");
   		cursor.move(-1);
    	int number = cursor.getCount(); 
    	
   		while (cursor.moveToNext())
   		{
   			String dirlink = cursor.getString(cursor.getColumnIndex("NAME"));
   			String _id = cursor.getString(cursor.getColumnIndex("_id"));
   			
   			Log.d("images_link", dirlink);
   			File dir = new File(dirlink);   			
   			// Удаляем директорию
   			if (dir.isDirectory()) 
   			{
   		        String[] children = dir.list();
   		        for (int i = 0; i < children.length; i++) {
   		            new File(dir, children[i]).delete();
   		        }
   		     dir.delete();   		     
   		    }
   			// сканируем чтобы не осталось в кэше
   			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(dir)));
   			adap.deleteImages(_id);
   			/*
   			File image = new File(link);
   			image.delete();
   			*/   			   			
   		}
   		
   		cursor.close(); 		   		
   		return number;
    }
    
    private int deleteImagesByDef(DBAdapter adap, String def_id) 
    {
   		Cursor cursor = adap.getImagesByDef(def_id);
   		cursor.move(-1);
    	int number = cursor.getCount(); 
    	
   		while (cursor.moveToNext())
   		{
   			//String link = cursor.getString(cursor.getColumnIndex("NAME"));
   			String dirlink = cursor.getString(cursor.getColumnIndex("NAME"));
   			String _id = cursor.getString(cursor.getColumnIndex("_id"));
   			
   			Log.d("images_link", dirlink);
   			File dir = new File(dirlink);   			
   			// Удаляем директорию
   			if (dir.isDirectory()) 
   			{
   		        String[] children = dir.list();
   		        for (int i = 0; i < children.length; i++) {
   		            new File(dir, children[i]).delete();   		        
   		        }
   		     dir.delete();   		    
   		    }  
   			// сканируем чтобы не осталось в кэше
   			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(dir)));
   			adap.deleteImages(_id);
	
   		}
   		
   		cursor.close(); 		   		
   		return number;
    }
    

         
}
