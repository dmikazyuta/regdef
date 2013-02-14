package uralchem.kckk.uit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity implements SurfaceHolder.Callback
{
	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	LayoutInflater controlInflater = null;
	public static int GALLERY = 2;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) 
	    {
		 	super.onCreate(savedInstanceState);
		 	
		 	// Начальные настройки		 	          	        
	        setContentView(R.layout.surface);	        
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	        
	        getWindow().setFormat(PixelFormat.UNKNOWN);
	        
	        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
	        surfaceHolder = surfaceView.getHolder();
	        surfaceHolder.addCallback((Callback) this);
	        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	        
	        controlInflater = LayoutInflater.from(getBaseContext());
	        View viewControl = controlInflater.inflate(R.layout.camera, null);
	        LayoutParams layoutParamsControl 
	        	= new LayoutParams(LayoutParams.FILL_PARENT, 
	        	LayoutParams.FILL_PARENT);
	        this.addContentView(viewControl, layoutParamsControl);	        	        	       
	        	 
	        // Кнопки
	        final ImageButton shotButton = (ImageButton) findViewById(R.id.ImageButtonShot);
	        final ImageButton galleryButton = (ImageButton) findViewById(R.id.ImageButtonGallery);
	        ImageButton closeButton = (ImageButton) findViewById(R.id.ImageButtonCloseCamera);
	        final ImageButton flash = (ImageButton) findViewById(R.id.ImageButtonFlashlight);
	        
	        // Счетчик фото в UI
	        final TextView photoNumTextView = (TextView) findViewById(R.id.TextViewPhotoNum);
	        final DBAdapter adap = new DBAdapter(CameraActivity.this);
	        
	        final Handler myHandler = new Handler();
	        
	        new Thread(new Runnable() {
	   	        public void run() {	 	   	        	
	   	        	adap.open();
	   	        	String def_id = adap.getNextId("defects");
	   	        	if (def_id == null) def_id = "1";
	   	        	adap.close();

	   	        	final String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id;
		 	    	File dir = new File(link);
		 	    	
		 	    	galleryButton.setEnabled(false);
		 	    	final Context context = getApplicationContext();
		 	    	// Проверим наличие фанарика
		 	    	PackageManager pm = context.getPackageManager();
		 	    	boolean hasFlash = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		 	    	flash.setEnabled(hasFlash);
		 	    	
		 	    	if (dir.exists()) 
		 	    	{
		 	    		final String[] list = dir.list();
		 	    		// Для UI		 	    	
		 	    		myHandler.post(new Runnable() { 
		 	    			public void run() 
		 	    			{	    					
		 	    				//galleryButton.setEnabled(false);	    					
		 	    				// TextView		    		   					
		 	    				photoNumTextView.setText(Integer.toString(list.length));
		 	    				if (list.length >= 1) 
		 	    					{
		 	    					galleryButton.setEnabled(true);
			 		   	        	File imgFile = new  File(link+"/"+list[list.length-1]);
			 		   	        	Log.d("small img", link+"/"+list[list.length-1]);		 		   	        			 		   	        
			 		   	        	if (imgFile.exists())
			 		   	        	{
			 		   	        	    final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	   	        	    
			 		                   	 new Thread(new Runnable() {
			 		             	   	        public void run() {	             	            	 
			 		             	   	        	runOnUiThread(new Runnable() 
			 		             	            	 { public void run() 
			 		             	            	 	            {
			 		             	            		 				Bitmap resizeBitmap = getResizedBitmap(myBitmap, 82, 94);
			 		             	            		 				galleryButton.setImageBitmap(resizeBitmap);
			 		             	            	 	            }
			 		             	            	 	        });}  }).start(); 
			 		   	        	}
			 		   	        			 		   	        
		 	    					}
		 	    				if (list.length >= 5) 
		 	    					{
		 	    						galleryButton.setEnabled(true);
		 	    						photoNumTextView.setTextColor(Color.RED); 
		 	    						shotButton.setEnabled(false);
		 	    					
		 	    						File imgFile = new  File(link+"/"+list[list.length-1]);
		 	    						Log.d("small img", link+"/"+list[list.length-1]);		 		   	        			 		   	        
		 	    						if (imgFile.exists())
		 	    						{
		 	    							final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	   	        	    
			 		                   	 	new Thread(new Runnable() {
			 		             	   	        public void run() {	             	            	 
			 		             	   	        	runOnUiThread(new Runnable() 
			 		             	            	 { public void run() 
			 		             	            	 	            {
			 		             	            		 				Bitmap resizeBitmap = getResizedBitmap(myBitmap, 82, 94);
			 		             	            		 				galleryButton.setImageBitmap(resizeBitmap);
			 		             	            	 	            }
			 		             	            	 	        });}  }).start(); 
		 	    						}
		 	    					}
		 	    							 		   	        	
		 	    			}
		 	    		});		 	    		
		 	    	}
		 	    	else {
		 	    			// Для UI		 	    	
		       	        	myHandler.post(new Runnable() { 
		       	        		public void run() 
		       	        		{	    					
		       	        			galleryButton.setEnabled(false);	    					
		       	        			// TextView		    		   					
		       	        			photoNumTextView.setText("0");		    		 	     				 	    		    		 			
		       	        		}
		       	        	});	
		 	    		}
		 	    	
	   	     }  }).start();
	        
	        
	        // Повесим Листенер
	        closeButton.setOnClickListener(buttonListener);
	        shotButton.setOnClickListener(buttonListener);
	        galleryButton.setOnClickListener(buttonListener);
	        flash.setOnClickListener(buttonListener);
	    }	 
	
	// Поведение камеры в разных состояниях    
 	
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
   	 
		// TODO Auto-generated method stub
		if(previewing){
			camera.stopPreview();
			previewing = false;
			
		}
		
		if (camera != null){
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera = Camera.open();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}
    
	// Листенер кнопок UI
	
     private OnClickListener buttonListener = new OnClickListener()  
     { 
 		public void onClick(View v)  
         {  
 			final Context context = getApplicationContext();
 			Handler myHandler = new Handler();
 			DBAdapter adap = new DBAdapter(CameraActivity.this);
 			final ImageButton flash = (ImageButton) findViewById(R.id.ImageButtonFlashlight);
 			
 			switch(v.getId())  
            {  
 			
         		case R.id.ImageButtonCloseCamera:
         			         			
         			if (flash.isSelected())
         			{
         				try 
         				{
         					Parameters p = camera.getParameters();
         					p.setFlashMode(Parameters.FLASH_MODE_OFF);
         					camera.setParameters(p); 
         					flash.setSelected(false);
         				} 
         				catch (Exception ex) 
    					{    						
    						Toast toast1 = Toast.makeText(context, "Ошибка доступа к подсветке", Toast.LENGTH_LONG);
    						toast1.show();
    					}
        			}
         			
         			adap.open();
         			String def_id = adap.getNextId("defects");
         			if (def_id == null) def_id = "1";
         	    	adap.close();
		 	    	String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id;
		 	    	File dir = new File(link);
		 	    	Intent intent=new Intent();
		 	    	if (dir.exists())
		 	    	{
		 	    	String[] list = dir.list();
		 	    	int number = list.length;		 	   
         			// Отправка данных родителю         			
         		    intent.putExtra("images", Integer.toString(number));
		 	    	}
		 	    	else {
		 	    		intent.putExtra("images", "0");
			    	}
         		    CameraActivity.this.setResult(RESULT_OK, intent);
         			// Назад к родительскому Активити
         			CameraActivity.this.finish();         			
         		break;
         		
         		case R.id.ImageButtonShot:         			
         			// Сделаем снимок
         			camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
         			
         		break;     
         		
         		case R.id.ImageButtonGallery:         			
         			
         			
         			if (flash.isSelected())
         			{
         				try 
         				{
         					Parameters p = camera.getParameters();
         					p.setFlashMode(Parameters.FLASH_MODE_OFF);
         					camera.setParameters(p);
         					flash.setSelected(false);
         				} 
         				catch (Exception ex) 
    					{    						
    						Toast toast1 = Toast.makeText(context, "Ошибка доступа к подсветке", Toast.LENGTH_LONG);
    						toast1.show();
    					}
        			}
         			
         			adap.open();
        			String def_id_m = adap.getNextId("defects"); 
        			if (def_id_m == null) def_id_m = "1";
        			try {
							MediaScanner ms = new MediaScanner();
							ms.startScan(CameraActivity.this, def_id_m, myHandler, "camera");																				
					}
					catch (Exception ex) 
					{    						
						Toast toast1 = Toast.makeText(context, "Ошибка доступа к фотографии\n"+ex.getMessage(), Toast.LENGTH_LONG);
						toast1.setGravity(Gravity.CENTER, 0, 0);
						toast1.show();
					}
         		break;  
         		
         		case R.id.ImageButtonFlashlight:
         			    
         			if (flash.isSelected())
         			{
         				try 
         				{
         					Parameters p = camera.getParameters();
         					p.setFlashMode(Parameters.FLASH_MODE_OFF);
         					camera.setParameters(p);
         					flash.setSelected(false);
         				} 
         				catch (Exception ex) 
    					{    						
    						Toast toast1 = Toast.makeText(context, "Ошибка доступа к подсветке", Toast.LENGTH_LONG);
    						toast1.show();
    					}
        			}
         			else
         			{
         				try 
         				{
         					Parameters p = camera.getParameters();
         					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
         					camera.setParameters(p);
         					flash.setSelected(true);
         				} 
         				catch (Exception ex) 
         				{    						
         					Toast toast1 = Toast.makeText(context, "Ошибка доступа к подсветке", Toast.LENGTH_LONG);
         					toast1.show();
         				}
         			}
					
         		break;
            }
        }
     };
     
     // Автофокус
     /*
     AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

    	  public void onAutoFocus(boolean arg0, Camera arg1) {

    	  }};
    */
     
  	// Делаем фотографию
  	
     ShutterCallback myShutterCallback = new ShutterCallback(){

 		public void onShutter() {
 			// TODO Auto-generated method stub
 			
 		}};
 		
 	PictureCallback myPictureCallback_RAW = new PictureCallback(){

 		public void onPictureTaken(byte[] arg0, Camera arg1) {
 			// TODO Auto-generated method stub
 			
 		}};
  	
 	PictureCallback myPictureCallback_JPG = new PictureCallback(){

 		public void onPictureTaken(byte[] arg0, Camera arg1) {
 			// TODO Auto-generated method stub
 			/*Bitmap bitmapPicture 
 				= BitmapFactory.decodeByteArray(arg0, 0, arg0.length);	*/
 			ImageButton shotButton = (ImageButton) findViewById(R.id.ImageButtonShot);
 	        final ImageButton galleryButton = (ImageButton) findViewById(R.id.ImageButtonGallery);
 			final DBAdapter adap = new DBAdapter(CameraActivity.this);
 			
 			// Присваиваем имя 			
 			adap.open();
 			// Следующие id
 			String _id = adap.getNextId("images");
 			if (_id == null) _id = "1";
 			String def_id = adap.getNextId("defects");
 			if (def_id == null) def_id = "1";
 			
 			// Текущее время
 			long msTime = System.currentTimeMillis();  
 			Date curDateTime = new Date(msTime);
 			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_kkmm");  
 			String formattedDateString = formatter.format(curDateTime); 
 			
 			// Получаем имя для фотографии
 			String filename = "REGDEF_"+formattedDateString+"_"+_id+".jpg";
 			
 			// Полная ссылка на файл
 			File sdCard = Environment.getExternalStorageDirectory();
 			//String link = sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id+"/"+filename;
 			String dirlink = sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id;
 			final String link = sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id+"/"+filename;
 			
 			// Сохраняем запись о фото со статусом "Временный"
 			// Ссылка на директорию
 			adap.SaveImageString(dirlink, formattedDateString, "temp", def_id);			 			 			
 			OutputStream imageFileOS;

 			try 
 			{				
 	            File dir = new File (sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id); 
 				// Сохранение			
 				dir.mkdir();

 				imageFileOS = new FileOutputStream(String.format(link));
 				imageFileOS.write(arg0);
 				imageFileOS.flush();
 				imageFileOS.close();
 				
 				addPicToGallery(link);
 				
 				// Привязка по имени 				
 				Toast.makeText(CameraActivity.this, "Изображение сохранено сюда\n" + link, Toast.LENGTH_LONG).show();
 				
 			} catch (FileNotFoundException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			camera.startPreview();
 			
	        new Thread(new Runnable() {
	   	        public void run() 
	   	        {		   	        	
	   	        	File imgFile = new  File(link);
	   	        	if (imgFile.exists())
	   	        	{

	   	        	    final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	   	        	    
	                   	 new Thread(new Runnable() {
	             	   	        public void run() {	             	            	 
	             	   	        	runOnUiThread(new Runnable() 
	             	            	 { public void run() 
	             	            	 	            {
	             	            		 				Bitmap resizeBitmap = getResizedBitmap(myBitmap, 82, 94);
	             	            		 				galleryButton.setImageBitmap(resizeBitmap);
	             	            	 	            }
	             	            	 	        });}  }).start(); 
	   	        	}
	   	        	
	   	        }  }).start();
	        
 			//final Handler myHandler = new Handler();
 			final TextView photoNumTextView = (TextView) findViewById(R.id.TextViewPhotoNum);
 			
 			File dir = new File(dirlink);
 	    	String[] list = dir.list();
 	    	final int number = list.length;
   					
 	    	photoNumTextView.setText(Integer.toString(number));
 	    	if (number >= 1) galleryButton.setEnabled(true); 	    	
 	    	if (number >= 5) { photoNumTextView.setTextColor(Color.RED); shotButton.setEnabled(false);
 	    	}
		 	    	
 			adap.close();

 		}};
 		
 		 @Override
 	    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
 		 {
 	        // запишем в лог значения requestCode и resultCode
 	        Log.d("getMyDataBack", "requestCode = " + requestCode + ", resultCode = " + resultCode);
 	        
 	        final TextView photoNumTextView = (TextView) findViewById(R.id.TextViewPhotoNum);
	        final ImageButton shotButton = (ImageButton) findViewById(R.id.ImageButtonShot);
	        final ImageButton galleryButton = (ImageButton) findViewById(R.id.ImageButtonGallery);
	        
	        new Thread(new Runnable() {
 	   	        public void run() {	             	            	 
    		 
 	        // Количество временных фото
			DBAdapter adap = new DBAdapter(CameraActivity.this);
			adap.open();
			String def_id_2 = adap.getNextId("defects");
			if (def_id_2 == null) def_id_2 = "1";
			
			final String link = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/regdef_"+def_id_2;
			File dir = new File(link);
			final String[] list = dir.list();
			final int number = list.length;
			adap.close();
					
	        runOnUiThread(new Runnable() 
            	 { public void run() 
            	 	            {            		 
            		 				photoNumTextView.setText(Integer.toString(number));	
            		 				
            		 				if ((number < 5) && (number > 0)) 
            		 				{    	   	        			 	    	  	    	
				
            		 					File imgFile = new  File(link+"/"+list[list.length-1]);
            		 					Log.d("small img", link+"/"+list[list.length-1]);
	   	        	
            		 					if (imgFile.exists())
            		 					{
            		 						Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());	   	        	    
            		 						Bitmap resizeBitmap = getResizedBitmap(myBitmap, 82, 94);
	             	            		 	galleryButton.setImageBitmap(resizeBitmap);	             	            	 	            
            		 					}
	   	        	
            		 					photoNumTextView.setTextColor(Color.parseColor("#0054A8")); 
            		 					shotButton.setEnabled(true);
	   	        	
            		 				}
            		 				
            		 				else if (number == 0) 
            		 				{  
            		 					galleryButton.setEnabled(false);
            		 					shotButton.setEnabled(true);
            		 					photoNumTextView.setTextColor(Color.parseColor("#0054A8"));					
            		 					galleryButton.setImageResource(android.R.color.transparent);
            		 				}
 	            	 	       }
            	 });
	        
 	   	        }  }).start(); 
			
 	      }
          
 		private void addPicToGallery(String dirlink) {
 		    Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

 		    File f = new File(dirlink);
 		    Uri contentUri = Uri.fromFile(f);
 		    media.setData(contentUri);
 		    this.sendBroadcast(media);
 		}
 		
 		public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
 			
 			int width = bm.getWidth();
 			int height = bm.getHeight();
 			float scaleWidth = ((float) newWidth) / width;
 			float scaleHeight = ((float) newHeight) / height;

 			// create a matrix for the manipulation
 			Matrix matrix = new Matrix();

 			// resize the bit map
 			matrix.postScale(scaleWidth, scaleHeight);

 			// recreate the new Bitmap
 			Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

 			return resizedBitmap;
 			}
 	
 	    public void startGallery(Uri uri)
 	    {
 	    	Log.d("Gallery","try start Gallery! "+uri);
 	    	
 	    	if (uri != null) 
 			{
 	    		Intent intent = new Intent(Intent.ACTION_VIEW);				
 	    		intent.setDataAndType(uri, "image/*");
 				CameraActivity.this.startActivityForResult(intent, GALLERY); 				
 			}
 	    }
}
