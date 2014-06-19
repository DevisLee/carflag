package blank.lee.actb_car.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import blank.lee.actb_car.entity.CarFlag;

//数据库 管理类
public class DBManager {
	// private DBHelper helper;
	private SQLiteDatabase db;
	private final static String DATABASE_NAME = "carflags.db";
	private static final String TABLE_NAME = "carflags";

	public DBManager(Context context) {
		// helper = new DBHelper(context);
		// //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		// db = helper.getWritableDatabase();

		// 获取管理对象，因为数据库需要通过管理对象才能够获取
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
		// 通过管理对象获取数据库
		db = mg.getDatabase(DATABASE_NAME);
		if (db != null) {
			Log.i("TTTT", "db != null");
		}
	}
	//查询所有数据
	public List<CarFlag> queryData(){
		List<CarFlag> carFlags = new ArrayList<CarFlag>();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // 获取游标      + " ORDER BY name"
			while (c.moveToNext()) {
				CarFlag carFlag = new CarFlag();
				carFlag.setName(c.getString(c.getColumnIndex("name")));
				carFlag.setEname(c.getString(c.getColumnIndex("ename")));
				carFlag.setImage(c.getString(c.getColumnIndex("image")));
				carFlag.setIsfamous(c.getString(c.getColumnIndex("isfamous")));
				carFlag.setCountry(c.getString(c.getColumnIndex("country")));
				carFlag.setType(c.getString(c.getColumnIndex("type")));
				carFlag.setDescription(c.getString(c.getColumnIndex("description")));
				carFlags.add(carFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return carFlags;
	}
	//查询类别数据
	public List<CarFlag> queryDataWithType(String type){
		List<CarFlag> carFlags = new ArrayList<CarFlag>();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE type='"+type+"'" , null); // 获取游标  
			while (c.moveToNext()) {
				CarFlag carFlag = new CarFlag();
				carFlag.setName(c.getString(c.getColumnIndex("name")));
				carFlag.setEname(c.getString(c.getColumnIndex("ename")));
				carFlag.setImage(c.getString(c.getColumnIndex("image")));
				carFlag.setIsfamous(c.getString(c.getColumnIndex("isfamous")));
				carFlag.setCountry(c.getString(c.getColumnIndex("country")));
				carFlag.setType(c.getString(c.getColumnIndex("type")));
				carFlag.setDescription(c.getString(c.getColumnIndex("description")));
				carFlags.add(carFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return carFlags;
	}
	
	//查询热度数据
	public List<CarFlag> queryDataWithHot(){
		List<CarFlag> carFlags = new ArrayList<CarFlag>();
		Cursor c = null;
		try {
			c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE isfamous='yes'" , null); // 获取游标  
			while (c.moveToNext()) {
				CarFlag carFlag = new CarFlag();
				carFlag.setName(c.getString(c.getColumnIndex("name")));
				carFlag.setEname(c.getString(c.getColumnIndex("ename")));
				carFlag.setImage(c.getString(c.getColumnIndex("image")));
				carFlag.setIsfamous(c.getString(c.getColumnIndex("isfamous")));
				carFlag.setCountry(c.getString(c.getColumnIndex("country")));
				carFlag.setType(c.getString(c.getColumnIndex("type")));
				carFlag.setDescription(c.getString(c.getColumnIndex("description")));
				carFlags.add(carFlag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return carFlags;
	}



	/**
	 * close database
	 */
	public void closeDB() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

//	/**
//	 * @Method:copyDB2SD
//	 * @Description: 复制数据库到SD卡
//	 * @Author:Devis
//	 * @Date:2014-5-7
//	 */
//	public static void copyDB2SD(Context context) {
//		String fileName = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
//
//		File file = new File("/data/data/" + context.getPackageName());
//		for (String path : file.list()) {
//			Log.e("Database2SD", path);
//		}
//		File databaseFile = new File(fileName);
//		try {
//			FileInputStream fis = new FileInputStream(databaseFile);
//			FileChannel inChannel = fis.getChannel();
//			File dirFile = new File("sdcard/");
//			if (!dirFile.exists()) {
//				dirFile.mkdirs();
//			}
//			FileOutputStream fos = new FileOutputStream(new File("sdcard/" + DATABASE_NAME));
//			FileChannel outChannel = fos.getChannel();
//			outChannel.transferFrom(inChannel, 0, inChannel.size());
//			fis.close();
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}

}