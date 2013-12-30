package uralchem.kckk.uit;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class JournalAdapter extends BaseAdapter {
	
	public CheckBox uncheck;
    private ArrayList<HashMap<String, String>> data;
  //  private static LayoutInflater inflater=null;
    private LayoutInflater inflater;
    ViewHolder holder;
    private Context context;
    
    public JournalAdapter(Context context, ArrayList<HashMap<String, String>> d) {
        data = d;
     //   inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(context); 
        this.context = context;
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
  
    public View getView(final int position, View convertView, ViewGroup parent) {

    	View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.listrowjournal, null);
      
        holder = new ViewHolder();
        holder.title = (TextView)vi.findViewById(R.id.rowTextView);
        holder.check = (CheckBox)vi.findViewById(R.id.rowCheckBox);
        holder.image = (ImageButton)vi.findViewById(R.id.rowImageButton);
        
        holder.title.setFocusable(true);        
        holder.check.setFocusable(false);
        holder.image.setFocusable(false);

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
            	        	 Log.d("check", "check click");
            	             // По нажатию, изменяется доступность кнопки "Удаление"  
            	        	 ((FirstDBActivity)context).setListViewItems(position, isChecked);  

            		}
        });
        
        holder.image.setOnClickListener(new OnClickListener() {
              public void onClick(View v) {
            	  //Toast.makeText(context, "Image -" + String.valueOf(position), Toast.LENGTH_SHORT).show(); 
            	  Log.d("image listener", String.valueOf(position));
            	  ((FirstDBActivity)context).getDefIdForImage(position); 
            }
          });
        
        HashMap<String, String> hm = new HashMap<String, String>();
        hm = data.get(position);
         
        // Выставление значений
        holder.title.setText(hm.get(FirstDBActivity.TEXT));
        
        // Иконка для изображений
        String image_exist = hm.get(FirstDBActivity.IMAGE);       
        if (image_exist.equals("0")) holder.image.setVisibility(View.GONE);        
        
        return vi;
    }
    
    static class ViewHolder {
        TextView title;
        CheckBox check;
        ImageButton image;
        
      }
}
