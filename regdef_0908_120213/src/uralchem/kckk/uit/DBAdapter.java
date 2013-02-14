package uralchem.kckk.uit;

import java.util.ArrayList;

import android.content.ContentValues;  
import android.content.Context;  
import android.database.Cursor;  
import android.database.SQLException;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.util.Log;  
import android.widget.Toast;

public class DBAdapter 
{
	int id = 0;  
    public static final String KEY_ROWID = "_id";  
    //public static final String KEY_QUOTES = "NAME";  
    private static final String TAG = "DBAdapter";  
  
    public static final String DATABASE_NAME = "TechWatch";  
   // private static final String DATABASE_TABLE = "obj";  
    private static final int DATABASE_VERSION = 1; 
    
    private ArrayList<String> arrayKeys = null;
    private ArrayList<String> arrayValues = null;
    private ArrayList<String> databaseKeys = null;
    private ArrayList<String> databaseKeyOptions = null;

    // �������
    private static final String create_table_sector =  
            "create table sector (_id integer primary key autoincrement, "  
            					+ "NAME text)"; 
    
    private static final String create_table_aggr =  
            "create table aggr (_id integer primary key autoincrement, "  
            				 + "NAME text, " 
            				 + "SECTOR_ID integer)";
    
    private static final String create_table_obj =  
            "create table obj (_id integer primary key autoincrement, "  
            				 + "NAME text, " 
            				 + "AGGR_ID integer)";
    
    private static final String create_table_defects =  
            "create table defects (_id integer primary key autoincrement, "  	 // id
            				 + "REG_DATE datetime not null, " 			 // ���� �����������            				 
            				 + "ZEH text not null, "					 // ��� 
            				 + "SECTOR_ID int not null, "				 // �������
            				 + "AGGR_ID int not null, "					 // �������
            				 + "POSIT_ID int not null, "				 // �������
            				 + "SMENA text not null,"					 // �����
            				 + "OBJ_ID int,"					 // ������ �������
            				 + "PART text,"						 // ����������� �����
            				 + "EQUIP text,"					 // ������������
            				 + "CAUSE text,"				     // �������
            				 + "STOP boolean not null,"			 // ���������
            				 + "DESCR text,"					 // ��������
            				 + "SEND boolean not null,"			 // ���� �������� � ����������� ����
            				 + "IMAGE_EXIST boolean not null)";	 // ���� ������� ���������� ��� ������� �������		 		 
    
    private static final String create_table_images =  
            "create table images (_id integer primary key autoincrement, "  
            				 + "NAME text, " 
            				 + "DATE datetime, "
            				 + "STATUS text, " 
            				 + "DEF_ID integer)";
    
    // ��������� ������ ���������
    private static final String create_table_options =     
    "create table options "+
    "as select 'server' as NAME, '' as PARAM union "+
    "select 'username' as NAME, '' as PARAM union "+
    "select 'password' as NAME, '' as PARAM union " +
    "select 'ZEH' as NAME, '' as PARAM union " +
    "select 'SECTOR' as NAME, '' as PARAM union " +
 //   "select 'VERSION' as NAME, '0' as PARAM union " +
 //   "select 'VERSIONNAME' as NAME, '' as PARAM union " +
    "select 'LASTUPDATE' as NAME, '' as PARAM ";
    
    // ��� � �������
    private static final String create_table_zehsector =
    "create table zehsector "+
    "(_id integer primary key, " +
    "parent int, " +
    "level int, " +
    "name text, " +
    "is_leaf int)";
    
    // ������� ������� 
    private static final String create_table_objects =
    "create table objects "+
    "(_id integer primary key, " +
    "parent int, " +
    "level int, " +
    "name text, " +
    "is_leaf int)";
    
    // ������ � ������ �������� �������
    private static final String create_idx_on_objects =
    "CREATE INDEX [parent_objects_indx] ON [objects]([parent])";
    
    // �������. ����������� �����    
    private static final String create_table_d_part =
    "create table d_part "+
    "(_id integer primary key, " +
    "parent int, " +
    "level int, " +
    "name text, " +
    "is_leaf int)";
    
    // �������. ������������
    private static final String create_table_d_equip =
    "create table d_equip"+
    "(_id integer primary key, " +
    "parent int, " +
    "level int, " +
    "name text, " +
    "is_leaf int)";
    
    // ������ � ������
    private static final String create_idx_on_d_equip =
    "CREATE INDEX [parent_d_equip_indx] ON [d_equip]([parent])";
    
    // �������. ������� �������������    
    private static final String create_table_d_cause =
    "create table d_cause "+
    "(_id integer primary key, " +
    "parent int, " +
    "level int, " +
    "name text, " +
    "is_leaf int)";
    
    // ������ � ������
    private static final String create_idx_on_d_cause=
    "CREATE INDEX [parent_d_cause_indx] ON [d_cause]([parent])";
    
    // �������� ������ � �������
    private static final String create_table_fsearch =
    	    "create table fsearch "+
    	    "(parent integer primary key, " +
    	    "level int)";
 
    private static final String drop_table_sector =  
            "DROP TABLE IF EXISTS sector";
    private static final String drop_table_obj =  
            "DROP TABLE IF EXISTS obj";
    private static final String drop_table_defects =  
            "DROP TABLE IF EXISTS defects";
    private static final String drop_table_options =  
            "DROP TABLE IF EXISTS options";
    
    private final Context context;  
    private DatabaseHelper DBHelper;  
    private SQLiteDatabase db;
    public DBAdapter(Context ctx)  
	{  
    	this.context = ctx;  
    	DBHelper = new DatabaseHelper(context);  
	}  

    private static class DatabaseHelper extends SQLiteOpenHelper  
	{  
    	DatabaseHelper(Context context)  
    	{  
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
    	}  
  
    	@Override  
    	public void onCreate(SQLiteDatabase db)  
    	{  
    		db.execSQL(create_table_defects);
    		db.execSQL(create_table_options);
    		db.execSQL(create_table_zehsector);
    		db.execSQL(create_table_images);
    		// �������
    		db.execSQL(create_table_objects);
    		db.execSQL(create_idx_on_objects);
    		db.execSQL(create_table_fsearch);
    		// �������� �������
    		db.execSQL(create_table_d_part);
    		db.execSQL(create_table_d_equip);
    		db.execSQL(create_table_d_cause);
    		db.execSQL(create_idx_on_d_equip);
    		db.execSQL(create_idx_on_d_cause); 
    	}  
    	
      
    	@Override  
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  
    	{  
    		Log.w(TAG, "Upgrading database from version " + oldVersion  
    			  + " to "  
    			  + newVersion + ", which will destroy all old data");  
        db.execSQL("DROP TABLE IF EXISTS drop_table_aggr");
        db.execSQL("DROP TABLE IF EXISTS drop_table_sector");
        db.execSQL("DROP TABLE IF EXISTS drop_table_obj");
        db.execSQL("DROP TABLE IF EXISTS drop_table_defects");        
        onCreate(db);  
    	} 
    	
	}
 
    public DBAdapter open() throws SQLException  
    {  
    	db = DBHelper.getWritableDatabase();  
    	return this;  
    }  
    
    public void close()  
    {  
    	DBHelper.close();  
    }
    
    public void insertValue(String txt1, String txt2)  
    {  
        ContentValues initialValues = new ContentValues();  
        initialValues.put("NAME", txt1);
       // initialValues.put("", txt2);
        
        db.insert("sector", null, initialValues);  
    }
    
    public long getCount()  
    {  
        Cursor cursor = db.rawQuery(  
                    "SELECT COUNT(*) FROM sector", null);  
                if(cursor.moveToFirst()) {  
                	return cursor.getLong(0);  
                }  
                return cursor.getLong(0);
                
    }     
    //* ������, �����, ������ ��� �������    
    // ���������� � ������� ��������    
    public void saveConnectParams(String server, String login, String password)
    {
    	try {
    		db.execSQL(create_table_options);
        	db.execSQL("update options set PARAM = '"+server+"' where NAME = 'server'");
        	db.execSQL("update options set PARAM = '"+login+"' where NAME = 'login'");
        	db.execSQL("update options set PARAM = '"+password+"' where NAME = 'password'");
    		}
    	catch (Exception ex) {
    		db.execSQL("update options set PARAM = '"+server+"' where NAME = 'server'");
    		db.execSQL("update options set PARAM = '"+login+"' where NAME = 'username'");
    		db.execSQL("update options set PARAM = '"+password+"' where NAME = 'password'");
    						 }
    }
    // ���������� ��������� ���� � �������
    public void saveZehSectorParams(String zeh, String sector)
    {    		
        	db.execSQL("update options set PARAM = '"+zeh+"' where NAME = 'ZEH'");
        	db.execSQL("update options set PARAM = '"+sector+"' where NAME = 'SECTOR'");        	
    }

    //* ����� ����������� ����������
    // server, username, password
    public String getConnectParam(String param)  
    {  
       String value = "0";       
    		Cursor cursor = db.rawQuery(      				
    				"SELECT PARAM from options where NAME = '"+param+"'", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
    			if(!(cursor.getString(0).equals(null)))
					{
						return cursor.getString(0);
					}
    			else	{
							return value;
						}
            }
    	}
       return value;	
    }
    
    //* ������� Zehsector
    // ������� ������ ������� ����� ������� ���������� ����������
    public void truncZehSector()
    {
    	db.execSQL("delete from zehsector;");
    }
    // ���������� ���������� �� ��������� ���� ������ ��� ����-�������
    public void insertZehSector(String _id, String parent, String level, String name, String is_leaf)
    {
    		db.execSQL("insert into zehsector values("+_id+", "+parent+", "+level+", '"+name+"', "+is_leaf+")");

    }
    // ������ �� ������� ZehSector ��� ������ ����
    public Cursor getZehCursor()  
    {  
    	Cursor cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
								"from zehsector where parent = 3 order by name", null);    	
    	return cursor;
    }    
    // ������ �� ������� ZehSector ��� ������ ����
    public String getZehSectorName(String _id)  
    {  
       String value = " "; 
       try {
    		Cursor cursor = db.rawQuery(      				
    				"SELECT name from zehsector where _id = "+_id+"", null); 
    		if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
    			if(!(cursor.getString(0).equals(null)))
					{
						return cursor.getString(0);
					}
    			else	{
							return value;
						}
            }
    	}    
       }
   	catch  (Exception ex) {
   		return value;
	}
       return value;	
    }
 // ������ �� ������� ZehSector ��� ������ ����
    public Cursor getSectorByZeh(String zeh)  
    {  
    	Cursor cursor;
    	try {
    	cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
				"from zehsector where parent = "+zeh+" order by name", null);
    	}
       	catch  (Exception ex) {
        	cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
    				"from zehsector where parent = 0 order by name", null);
    	}
    	return cursor;
    }

    //* ����������� ������ ������ ��������
    public Cursor getAggrByParent(String parent)  
    {  
    	Cursor cursor;
    	try {
    	cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
				"from objects where parent = "+parent+" and is_leaf <> 1 order by name", null);  
    	}
    	catch  (Exception ex) {
        	cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
    				"from objects where parent = 0 and is_leaf <> 1 order by name", null); 
    	}
    	return cursor;
    }
    public Cursor getPositByParent(String parent)  
    {  
    	Cursor cursor;
    	cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
				"from objects where parent = "+parent+" and is_leaf = 1 order by name", null);  
    	return cursor;
    }
    
    public String getParentById(String parent)  
    {  
    	String value = null;       
    	Cursor cursor = db.rawQuery("select parent " +
				"from objects where _id = "+parent+"", null);  
 		if (cursor != null) 
 		{
 			if(cursor.moveToFirst()) {  
 				if(!(cursor.getString(0).equals(null)))
						{
							return cursor.getString(0);
						}
 				else	{
							return "false";
						}
 									 }
 		}
 		return "false";
    	
    }
    public String getParentNameById(String _id)  
    {  
    	String value = null;       
    	Cursor cursor = db.rawQuery("select name " +
				"from objects where _id = "+_id+"", null);  
 		if (cursor != null) 
 		{
 			if(cursor.moveToFirst()) {  
 				if(!(cursor.getString(0).equals(null)))
						{
							return cursor.getString(0);
						}
 				else	{
							return "false";
						}
 									 }
 		}
 		return "false";
    	
    }
    
    public int getLevelById(String _id)  
    {        
    	Cursor cursor = db.rawQuery("select level " +
				"from objects where _id = "+_id+"", null);  
 		if (cursor != null) 
 		{
 			if(cursor.moveToFirst()) {  
 				if(!(cursor.getString(0).equals(null)))
						{
							return cursor.getInt(0);
						}
 				else	{
							return -1;
						}
 									 }
 		}
 		return -1;
    	
    }
    
    //* ������ � ��������� ����������� ��������
    // ������ �� ������� "����������� �����"
    public Cursor getPartCursor()  
    {  
    	Cursor cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
								"from d_part order by name", null);    	
    	return cursor;
    }  
    // ������ �� ������� "������������"
    public Cursor getEquipByPart(String part)  
    {  
    	Cursor cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
								"from d_equip where parent = "+part+" order by name", null);    	
    	return cursor;
    }  
    // ������ �� ������� "�������"
    public Cursor getCauseByEquip(String part)  
    {  
    	Cursor cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
								"from d_cause where parent = "+part+" order by name", null);    	
    	return cursor;
    }  
    
    // ������� ������ ������� ����� ������� ���������� ����������
    public void truncObjects()
    {
    	db.execSQL("delete from objects");
    }
    public void truncPart()
    {
    	db.execSQL("delete from d_part");
    }
    public void truncEquip()
    {
    	db.execSQL("delete from d_equip");
    }
    public void truncCause()
    {
    	db.execSQL("delete from d_cause");
    }
    // ���������� ���������� �� ��������� ���� ������ ��� ����-�������
    public void insertObjects(String _id, String parent, String level, String name, String is_leaf)
    {
    	db.execSQL("insert into objects values("+_id+", "+parent+", "+level+", '"+name+"', "+is_leaf+")");
    }
    //* ���������� ���� ��� ��������� ������ ������� �� ������ 
    public void insertFSearch()
    {
    	db.execSQL("delete from fsearch");
    	db.execSQL("insert into fsearch(parent, level) " +
    			   "select distinct parent, null from objects group by parent having MIN(is_leaf) = 1");
    }
    // ����� � ������� ����������� ���������, ������������� � ������
    public String getFSearch(String parent)
    {
    	String value = null;       
 		Cursor cursor = db.rawQuery(      				
 				"SELECT '1' from fsearch where parent = "+parent+"", null); 
 		if (cursor != null) 
 		{
 			if(cursor.moveToFirst()) {  
 				if(!(cursor.getString(0).equals(null)))
						{
							return "true";
						}
 				else	{
							return "false";
						}
 									 }
 		}
 		return "false";
    }
    
    //* ���������� ������������ �� ��������
    // ������� "����������� �����"
    public void insertPart(String _id, String parent, String level, String name, String is_leaf)
    {
    	db.execSQL("insert into d_part values("+_id+", "+parent+", "+level+", '"+name+"', "+is_leaf+")");
    }
    // ������� "������������"
    public void insertEquip(String _id, String parent, String level, String name, String is_leaf)
    {
    	db.execSQL("insert into d_equip values("+_id+", "+parent+", "+level+", '"+name+"', "+is_leaf+")");
    }
    // ������� "�������"
    public void insertCause(String _id, String parent, String level, String name, String is_leaf)
    {
    	db.execSQL("insert into d_cause values("+_id+", "+parent+", "+level+", '"+name+"', "+is_leaf+")");
    }
    
    /*    
    public void setFullDadabase()
    {
    	//insert into sector table
    	db.execSQL("insert into sector(NAME) values ('1')");
    	db.execSQL("insert into sector(NAME) values ('2')");
    	db.execSQL("insert into sector(NAME) values ('3')");
    	db.execSQL("insert into sector(NAME) values ('4')");
    	//insert into aggr table
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���1_1',1)");
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���1_2',1)");
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���1_3',1)");
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���2_1',2)");
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���2_2',2)");
    	db.execSQL("insert into aggr(NAME, SECTOR_ID) values ('���3_1',3)");
    	//insert into obj table
    	db.execSQL("insert into obj(_id, NAME, AGGR_ID) values (0,' ',0);");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_1_�',1)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_1_B',1)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_1_C',1)");
    	
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_2_�',2)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_2_B',2)");
    	
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_3_A',3)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_3_B',3)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������1_3_C',3)");
    	
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������2_1_A',4)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������2_1_B',4)");
    	
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������2_2_A',5)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������2_2_B',5)");
    	
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������3_1_A',6)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������3_1_B',6)");
    	db.execSQL("insert into obj(NAME, AGGR_ID) values ('������3_1_C',6)");
    }
    */
    
    //* ����������� ����������������� ����������
    // ��� �� ������� ������
    public String getCurrentZehName()  
    {  
       String value = null;       
    		Cursor cursor = db.rawQuery(      				
    				"SELECT name from zehsector where _id = (select cast(PARAM as number) from options where NAME = 'ZEH')", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
    			if(!(cursor.getString(0).equals(null)))
					{
						return cursor.getString(0);
					}
    			else	{
							return "������!";
						}
            }
    	}
       return value;	
    }
    
    // ������� �� ������� ������
    public String getCurrentSectorName()  
    {  
       String value = null;       
    		Cursor cursor = db.rawQuery(      				
    				"SELECT name from zehsector where _id = (select cast(PARAM as number) from options where NAME = 'SECTOR')", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
    			if(!(cursor.getString(0).equals(null)))
					{
						return cursor.getString(0);
					}
    			else	{
							return "������!";
						}
            }
    	}
       return value;	
    }
    
    //* �������� �� ������� ������
    // �������
    public Cursor getAggrBySector(String sector)  
    {  
    	Cursor cursor = db.rawQuery("select _id, parent, level, name, is_leaf " +
				"from zehsector where parent = "+sector+" order by name", null);    	
    	return cursor;
    }
    
// spinSector
    
    public String getValues(int i)  
    {  // int i = 0;
       String val = null;       
    		Cursor cursor = db.rawQuery(      				
    			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
    				"SELECT NAME as INFO FROM sector where _id = "+i, null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
                return cursor.getString(0);  
            }
    	}
       return val;	
    }
    
    //* ��������� ������ ���������
    // ������
    /*
    public String getVersion()  
    {               
    		Cursor cursor = db.rawQuery("SELECT PARAM FROM options where NAME = 'VERSION'", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
                return cursor.getString(0);  
            }
    	}
       return "��� ������";	
    }
    
    // ���������
    public void updateVersion(String version)  
    {
    //	db.execSQL("update options set PARAM = '"+zeh+"' where NAME = 'ZEH'");
    	db.execSQL("update OPTIONS set PARAM = '"+version+"' where NAME = 'VERSION'");
    }
    
    // �������� ������
    public String getVersionName()  
    {               
    		Cursor cursor = db.rawQuery("select PARAM from OPTIONS where NAME = 'VERSIONNAME'", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
                return cursor.getString(0);  
            }
    	}
       return "��� ������";	
    }
    
    // ���������
    public void updateVersionName(String versionname)  
    {
    	db.execSQL("update OPTIONS set PARAM = '"+versionname+"' where NAME = 'VERSIONNAME'");
    }
    */
   
    // ���� ���������� ����������
    public String getLastUpdate()  
    {               
    		Cursor cursor = db.rawQuery("select PARAM from OPTIONS where NAME = 'LASTUPDATE'", null); 
    if (cursor != null) 
    	{
    		if(cursor.moveToFirst()) {  
                return cursor.getString(0);  
            }
    	}
       return "��� ������";	
    }
    
    // ���������
    public void updateLastUpdate(String lastupdate)  
    {
    	db.execSQL("update OPTIONS set PARAM = '"+lastupdate+"' where NAME = 'LASTUPDATE'");
    }
    
    //Delete old Defects
    public Cursor selectOldDefects(int rownum) 
    {
    	Cursor cursor = db.rawQuery(	"select a._id as _id from (" +
    									"select (" +
    									"select COUNT(0)" +
    									" from (select _id from defects where send = 1) t1" +
    									" where t1._id <= t2._id" +
    									") as rownum," +
    									" t2._id as _id" +
    									" from (select _id from defects where send = 1) t2" +
    									") a where a.rownum <= (select count(*) from defects where send = 1) - "+rownum, null);
    	return cursor;
    }
    
    public void deleteOldDefects(int rownum) 
    {
    	db.execSQL("delete from defects where _id in " +
    									"(select a._id as _id from (" +
    									"select (" +
    									"select COUNT(0)" +
    									" from (select _id from defects where send = 1) t1" +
    									" where t1._id <= t2._id" +
    									") as rownum," +
    									" t2._id as _id" +
    									" from (select _id from defects where send = 1) t2" +
    									") a where a.rownum <= (select count(*) from defects where send = 1) - "+rownum+" )");
    }
    
    
    
// SpinAggr
    
    // spinAggr by spinSector
    
    public Integer getSpinAggrId(int i)  
    {  
    Integer j = null ;
    
    Cursor cursor = db.rawQuery(      				
			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
				"SELECT _id FROM aggr where sector_id = "+i+" LIMIT 1", null); 
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}
		return j;
		
    }
    
    public Integer getSpinObjId(int i)  
    {  
    Integer j = null ;
    
    Cursor cursor = db.rawQuery(      				
			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
				"SELECT _id FROM obj where aggr_id = "+i+" LIMIT 1", null); 
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}
		return j;
		
    }
    
    public String getSpinAggrValues(int j)  
    {  
      // String wrong_result = BuildWord.charAt('0');
       String val = null;       
    		Cursor cursor = db.rawQuery(      				
    			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
    				"SELECT NAME FROM aggr where _id = "+j, null); 
    		if (cursor != null) 
    		{
        		if(cursor.moveToFirst()) 
        		{  
        			return cursor.getString(0);  
        		}
    		}
       return val;	
    }
    
    public String getSpinObjValues(int j)  
    {  
      // String wrong_result = BuildWord.charAt('0');
       String val = null;       
    		Cursor cursor = db.rawQuery(      				
    			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
    				"SELECT NAME FROM obj where _id = "+j, null); 
    		if (cursor != null) 
    		{
        		if(cursor.moveToFirst()) 
        		{  
        			return cursor.getString(0);  
        		}
    		}
       return val;	
    }
    
    public long getAggrCount(int i)  
    {  
        Cursor cursor = db.rawQuery(  
                    "SELECT COUNT(*) FROM aggr where sector_id = "+i, null);  
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getLong(0);  
    		}
		}
                return cursor.getLong(0);
                
    }    
    
    public Integer getAggrId(int sector_id, int rownum) 
    {
    	Integer result = null;
    	Cursor cursor = db.rawQuery("select _id from (" +
    								" select (" +
    									" select COUNT(0)" +
    									" from (select _id, NAME, sector_id from aggr where sector_id = "+sector_id+") t1" +
    									" where t1._id <= t2._id" +
    									" ) as rownum," +
    									" t2._id as _id" +
    									" from (select _id, NAME, sector_id from aggr where sector_id = "+sector_id+") t2" +
    									" 	 )" +
    									" where rownum = "+rownum,null);
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}
		
		return result;
		
    }
    
//	spinObj   
    
    public long getObjCount(int i)  
    {  
    	Long j = null;
        Cursor cursor = db.rawQuery(  
                    "SELECT COUNT(*) FROM obj where aggr_id = "+i, null);  
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getLong(0);  
    		}
		}
        return j;
                
    }  
    
    public Integer getObjId(int rownum) 
    {
    	Integer result = null;
    	Cursor cursor = db.rawQuery("select _id from (" +
    								" select (" +
    									" select COUNT(0)" +
    									" from (select _id from defects order by _id desc) t1" +
    									" where t1._id <= t2._id" +
    									" ) as rownum," +
    									" t2._id as _id" +
    									" from (select _id from defects order by _id desc) t2" +
    									" 	 )" +
    									" where rownum = "+rownum,null);
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}
		
		return result;
		
    }

    public Integer getDefectsId(int menuItem, int rownum) 
    {
    	Integer result = null;
    	Cursor cursor = db.rawQuery("select _id from (" +
    								" select (" +
    									" select COUNT(0)" +
    									" from (select _id from defects where send="+menuItem+") t1" +
    									" where t1._id <= t2._id" +
    									" order by 1 desc"+
    									" ) as rownum," +
    									" t2._id as _id" +
    									" from (select _id from defects where send="+menuItem+") t2" +
    									" 	 )" +
    									" where rownum = "+rownum,null);
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}		
		return result;		
    }
    
    public String getNextId(String name)  
    {  
       String val = null;       
    		Cursor cursor = db.rawQuery(      				
    				"select seq + 1 from sqlite_sequence where name = '"+name+"'", null); 
    		if (cursor != null) 
    		{
        		if(cursor.moveToFirst()) 
        		{  
        			return cursor.getString(0);  
        		}
    		}
       return val;	
    }
    
    public String getImageLink()  
    {  
       String val = null;       
    		Cursor cursor = db.rawQuery(      				
    				"select distinct name from images where def_id = ( select seq + 1 from sqlite_sequence where name = 'temp' )", null); 
    		if (cursor != null) 
    		{
        		if(cursor.moveToFirst()) 
        		{  
        			return cursor.getString(0);  
        		}
    		}
       return val;	
    }
    

    public void DeleteAllFromDef()  
    {
    	db.execSQL("delete from defects");
    }
    
    public void DeleteFromDef(String _id)  
    {
    	db.execSQL("delete from defects where _id = "+_id+"");
    }
    public String getDefName(int _id)  
    {
        // String wrong_result = BuildWord.charAt('0');
        String val = null;
    
     		Cursor cursor = db.rawQuery(      				
     			//	 "SELECT CAST(_ID as text)||' '||NAME as INFO FROM sector where _id="+i, null); 
     				"select REG_DATE from defects where _id = "+_id, null); 
     		if (cursor != null) 
     		{
         		if(cursor.moveToFirst()) 
         		{  
         			return cursor.getString(0);  
         		}
     		}
        return val;	
    	
    }
// SaveButton
    
    public void SaveReg(String regdate, String zeh, String sector, String smena, String aggr, String posit, String obj, String part, String equip, String cause, String descr, String image_exist)  
    {
    	try {
    	db.execSQL("INSERT INTO defects (REG_DATE, ZEH, SECTOR_ID, AGGR_ID, POSIT_ID, SMENA, OBJ_ID, PART, EQUIP, CAUSE, STOP, DESCR, SEND, IMAGE_EXIST) " +
    						   " VALUES ('"+regdate+"', '"+zeh+"', "+sector+", "+aggr+", "+posit+", '"+smena+"',  '"+obj+"', '"+part+"', '"+equip+"', '"+cause+"', 0, '"+descr+"', 0, "+image_exist+")");
    		}
    	catch (Exception ex) 
    		{
    			db.execSQL("ALTER TABLE defects ADD IMAGE_EXIST boolean DEFAULT 0 not null");
    			db.execSQL("INSERT INTO defects (REG_DATE, ZEH, SECTOR_ID, AGGR_ID, POSIT_ID, SMENA, OBJ_ID, PART, EQUIP, CAUSE, STOP, DESCR, SEND, IMAGE_EXIST) " +
						   " VALUES ('"+regdate+"', '"+zeh+"', "+sector+", "+aggr+", "+posit+", '"+smena+"',  '"+obj+"', '"+part+"', '"+equip+"', '"+cause+"', 0, '"+descr+"', 0, "+image_exist+")");
    		}
    }

// ����� ���������� ������� ������� ������
    
    public void AfterSaveDefects(String defect)  
    {
    	db.execSQL("update defects set send = 1 where _id = "+defect+"");
    }    
    
// ������ ������ ������ �� ������� ��������
    
    public void DeleteOldDefects(String date)  
    {
    	db.execSQL("delete from defects where send = 1 and REG_DATE >= '"+date+"' + 7");
    }  
    
// ������ ������ ������ �� ������� ��������
    
    public void deleteImages(String _id)  
    {
    	db.execSQL("delete from images where _id = "+_id);
    }  
    
// ��������� ������ � ����������
    
    public void SaveImageString(String name, String date, String status, String def_id)  
    {
    	try {    	
    			db.execSQL(create_table_images);
    			Log.d("images", "table images created" );
        		db.execSQL("INSERT INTO IMAGES (NAME, DATE, STATUS, DEF_ID) " +
        							" VALUES ('"+name+"', '"+date+"', '"+status+"', "+def_id+")");
    		}
    	catch (Exception ex)
    	{    		
    		db.execSQL("INSERT INTO IMAGES (NAME, DATE, STATUS, DEF_ID) " +
    						   " VALUES ('"+name+"', '"+date+"', '"+status+"', "+def_id+")");
    		Log.d("images", "data into table inserted" );
    	}
    }
    
    public void ChangeImageStatus(String newstatus)  
    {
    	db.execSQL("update images set STATUS='"+newstatus+"' where STATUS = 'temp'");
    }
    
// ������ ������ ����������    
    
    
// get Defects for Http

    public Cursor getDefects(int sendstatus)  
    {  
    Cursor cursor = null;
    	try {
    		
    			cursor = db.rawQuery("select _id, REG_DATE as regdate, " +
    					"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2) as regdate_mod, " +
    					"Substr([REG_DATE], 12, 6)||':00' as regtime_mod, " +
    					"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2)||' '||Substr([REG_DATE], 12, 6)||':00' as regdatetime_mod, " +
    					"zeh, " +
    					"sector_id as sector, " +
    					"AGGR_ID as aggr, " +
    					"POSIT_ID as posit, " +
    					"smena, " +
    					"OBJ_ID as obj, " +
    					"part, " +
    					"equip, " +
    					"cause, " +
    					"descr, " +
    					"send ," +
    					"image_exist " +
    					"from defects " +
    					"where send = "+sendstatus+" order by _id desc", null); 
    	}
    	catch (Exception ex) 
    	{
    		db.execSQL("ALTER TABLE defects ADD IMAGE_EXIST boolean DEFAULT 0 not null");    	
    		cursor = db.rawQuery("select _id, REG_DATE as regdate, " +
    				"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2) as regdate_mod, " +
    				"Substr([REG_DATE], 12, 6)||':00' as regtime_mod, " +
    				"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2)||' '||Substr([REG_DATE], 12, 6)||':00' as regdatetime_mod, " +
    				"zeh, " +
    				"sector_id as sector, " +
    				"AGGR_ID as aggr, " +
    				"POSIT_ID as posit, " +
    				"smena, " +
    				"OBJ_ID as obj, " +
    				"part, " +
    				"equip, " +
    				"cause, " +
    				"descr, " +
    				"send ," +
    				"image_exist " +
    				"from defects " +
    				"where send = "+sendstatus+" order by _id desc", null); 
    	}
   	return cursor;
    }  
      
    public Cursor getDefectsHttpInsert(int sendstatus)  
    {  
	Cursor cursor = db.rawQuery("select _id, REG_DATE as regdate, " +
				"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2) as regdate_mod, " +
				"Substr([REG_DATE], 13, 6) as regtime_mod, " +
				"Substr([REG_DATE], 8, 4)||'-'||Substr([REG_DATE], 5, 2)||'-'||Substr([REG_DATE], 2, 2)||' '||Substr([REG_DATE], 12, 6)||':00' as regdatetime_mod, " +
				"zeh, " +
				"sector_id as sector, " +
				"AGGR_ID as aggr, " +
				"POSIT_ID as posit, " +
				"smena, " +
				"OBJ_ID as obj, " +
				"part, " +
				"equip, " +
				"cause, " +
				"descr, " +
				"Substr([REG_DATE], 2, 2)||'.'||Substr([REG_DATE], 5, 2)||'.'||Substr([REG_DATE], 8, 4)||' '||Substr([REG_DATE], 13, 6)||':00' as regdatetime_mod_2, " +
				"send, " +
				"IMAGE_EXIST "+
				"from defects " +
				"where send = "+sendstatus+" order by _id", null);    	
   	return cursor;
    } 
    
    public Cursor getImagesByStatus(String status)  
    {
    	Cursor cursor;
    	try {
    	cursor = db.rawQuery("select _id, name, date, status, def_id from images where status ='"+status+"'", null);
    	}
    	catch (Exception ex)
    	{    		
    		db.execSQL(create_table_images);
    		cursor = db.rawQuery("select _id, name, date, status, def_id from images where status ='"+status+"'", null);
    	}
    	return cursor;
    }
    
    public Cursor getImagesByDef(String def_id)  
    {
    	Cursor cursor = db.rawQuery("select _id, name, date, status, def_id from images where def_id = "+def_id, null); 
    	return cursor;
    }
    
    public Cursor getImagesWithNotExistDef()  
    {
    	Cursor cursor;
    	try {
    		cursor = db.rawQuery("select _id, name, date, status, def_id from images where def_id not in (select _id from defects) ", null);
    	}
    	catch (Exception ex)
    	{
    		db.execSQL(create_table_images);
    		cursor = db.rawQuery("select _id, name, date, status, def_id from images where def_id not in (select _id from defects) ", null);
    	}
    	return cursor;
    	
    }
    
   // � ���� �� �������������� ������� ?
    public Integer getNoSendDefectsCount(int sendstatus) 
    {
    	Integer result = 0;
    	Cursor cursor = db.rawQuery("select 1 from defects where send = "+sendstatus+"",null);
		if (cursor != null) 
		{
    		if(cursor.moveToFirst()) 
    		{  
    			return cursor.getInt(0);  
    		}
		}		
		return result;		
    }       

 // get Zeh list from table Zehsector
    
    public Cursor getZeh()  
    {  
	Cursor cursor = db.rawQuery("select Substr([REG_DATE], 7, 4)||'-'||Substr([REG_DATE], 4, 2)||'-'||Substr([REG_DATE], 1, 2)||'-'||Substr([REG_DATE], 12, 5)||':00' as reg_date, " +
				"zeh as zeh, sector_id as sector_id, AGGR_ID as AGGR_ID, smena as smena, OBJ_ID as OBJ_ID, PART as PART, EQUIP as EQUIP, cause as cause, descr as descr, send as send, " +
				"_id as id from defects " +
				"where send = 0", null);    	
   	return cursor;
    }
    
}
