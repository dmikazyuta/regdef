package uralchem.kckk.uit;

import java.io.File;



import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class MediaScanner implements MediaScannerConnectionClient {
	
	private String SCAN_PATH ;
	private String ACTIVITY;
	private static final String FILE_TYPE = "images/*";
	private MediaScannerConnection conn;
	private Context context;
	public String[] list;
	private Handler myHandler;
	
	public void startScan(Context context, String def_id, Handler handler, String activity) 
	{
		Log.d("startScan", "yes");
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File (sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id); 
		Log.d("dir", sdCard.getAbsolutePath()+"/DCIM/regdef_"+def_id);
	    list = dir.list();
	    for(int i=0;i<list.length;i++)
	    {
	        Log.d("all file path"+i, list[i]+list.length);
	    }
	    
	    SCAN_PATH = sdCard.toString()+"/DCIM/regdef_"+def_id+"/"+list[list.length-1];
		Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
	    
		if(conn!=null)
		{
			conn.disconnect();
		}
		this.context = context;
		this.myHandler = handler;
		ACTIVITY = activity; 
		conn = new MediaScannerConnection(context, this);
		conn.connect();		
	}
	
	public void onMediaScannerConnected() 
	{
	    Log.d("onMediaScannerConnected","success"+conn);
	    conn.scanFile(SCAN_PATH, FILE_TYPE); 
	}
	
	public void onScanCompleted(String path, final Uri uri) 
	{
		
			/*
			Log.d("onScanCompleted",uri + "success"+conn);
			if (uri != null) 
			{
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(uri);							
				((FirstDBActivity)context).startActivityForResult(intent, FirstDBActivity.GALLERY);
				//FirstDBActivity.this.startActivity(intent);
			}
			*/
			Log.d("onScanCompleted", "file://"+uri.getPath() + "success"+conn);
			
			myHandler.post(new Runnable() { 
				public void run() {				            											
		 				try {        
		 						if (ACTIVITY.equals("first")) ((FirstDBActivity)context).startGallery(uri);
		 						else if (ACTIVITY.equals("camera")) ((CameraActivity)context).startGallery(uri);
		 					}
		 				finally 
		 				{
		 					conn.disconnect();
		 					conn = null;
		 					Thread.currentThread().isInterrupted();
		 				}  	
				}
			});
            		 				
  	            	 	      
			//((FirstDBActivity)context).startGallery(uri);		
	}
	
}
