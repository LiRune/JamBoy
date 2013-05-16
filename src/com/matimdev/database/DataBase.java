package com.matimdev.database;

import com.matimdev.base.BaseScene;
import com.matimdev.manager.ResourcesManager;
import com.matimdev.scene.GameScene;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	static final String nombreBD = "JamBoyDB";	
	static final String tablaNiveles = "Niveles";
	static final String IDNivel = "Numero";
	static final String unlocked = "Desbloqueado";
	static final String beat = "Superado";
	static final String score = "Puntuacion";

	public DataBase(Context context) {
		// THE VALUE OF 1 ON THE NEXT LINE REPRESENTS THE VERSION NUMBER OF THE DATABASE
		// IN THE FUTURE IF YOU MAKE CHANGES TO THE DATABASE, YOU NEED TO INCREMENT THIS NUMBER
		// DOING SO WILL CAUSE THE METHOD onUpgrade() TO AUTOMATICALLY GET TRIGGERED
		super(context, nombreBD, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		// ESTABLISH NEW DATABASE TABLES IF THEY DON'T ALREADY EXIST IN THE DATABASE
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tablaNiveles+" (" +
				IDNivel + " INTEGER PRIMARY KEY , " +
				unlocked + " TEXT, " +
				beat + " TEXT, " +
				score + " TEXT" +
				")");

		db.execSQL("INSERT INTO Niveles VALUES(1," + "'true', " + "'false', 0)");
		db.execSQL("INSERT INTO Niveles VALUES(2," + "'false', " + "'false', 0)");
		//db.close();


		/*DataBase myDB2 = new DataBase(BaseScene.getActivity());
		db = myDB2.getReadableDatabase();
		Cursor c = db.rawQuery(" SELECT COUNT(*) FROM Niveles", null);
		int numNiveles = Integer.parseInt(c.getString(0));
		System.out.println("ID Niveles: " + numNiveles);
		if (numNiveles < GameScene.getNumNiveles())
		{
			int nuevosNiveles = GameScene.getNumNiveles() - numNiveles;

			for (int i = numNiveles + 1; i < numNiveles + nuevosNiveles; i++)
			{
				cv.put(IDNivel, i);
				cv.put(unlocked, "false");
				cv.put(beat, "false");
				cv.put(score, "0");
				db.insert(tablaNiveles, null, cv);
			}
		}		
		c.close();*/

		/*ContentValues cv = new ContentValues();
		cv.put(IDNivel, 1);
		cv.put(unlocked, "true");
		cv.put(beat, "false");
		cv.put(score, "0");
		db.insert(tablaNiveles, null, cv);
		cv.put(IDNivel, 2);
		cv.put(unlocked, "false");
		cv.put(beat, "false");
		cv.put(score, "0");
		db.insert(tablaNiveles, null, cv);
		cv.put(IDNivel, 3);
		cv.put(unlocked, "false");
		cv.put(beat, "false");
		cv.put(score, "0");
		db.insert(tablaNiveles, null, cv);*/

		/*             
		 * MORE ADVANCED EXAMPLES OF USAGE
		 *
                db.execSQL("CREATE TRIGGER fk_empdept_deptid " +
                                " BEFORE INSERT "+
                                " ON "+employeeTable+                          
                                " FOR EACH ROW BEGIN"+
                                " SELECT CASE WHEN ((SELECT "+colDeptID+" FROM "+deptTable+" WHERE "+colDeptID+"=new."+colDept+" ) IS NULL)"+
                                " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
                                "  END;");

                db.execSQL("CREATE VIEW "+viewEmps+
                                " AS SELECT "+employeeTable+"."+colID+" AS _id,"+
                                " "+employeeTable+"."+colName+","+
                                " "+employeeTable+"."+colAge+","+
                                " "+deptTable+"."+colDeptName+""+
                                " FROM "+employeeTable+" JOIN "+deptTable+
                                " ON "+employeeTable+"."+colDept+" ="+deptTable+"."+colDeptID
                                );
		 */                             
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// THIS METHOD DELETES THE EXISTING TABLE AND THEN CALLS THE METHOD onCreate() AGAIN TO RECREATE A NEW TABLE
		// THIS SERVES TO ESSENTIALLY RESET THE DATABASE
		// INSTEAD YOU COULD MODIFY THE EXISTING TABLES BY ADDING/REMOVING COLUMNS/ROWS/VALUES THEN NO EXISTING DATA WOULD BE LOST
		db.execSQL("DROP TABLE IF EXISTS "+tablaNiveles);
		onCreate(db);
	}


	public static boolean nivelDesbloqueado(int id){
		String desb = null;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());

		SQLiteDatabase db = myDB.getReadableDatabase();

		Cursor c = db.rawQuery(" SELECT Desbloqueado FROM Niveles WHERE Numero ="+id, null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya m�s registros
			do {
				desb = c.getString(0);
				System.out.println("NIVEL "+id+" DESBLOQUEADO: "+desb);

			} while(c.moveToNext());
		}

		db.close();

		if(desb.equals("true")){
			return true;
		}else{
			return false;
		}		
	}

	public static boolean nivelSuperado(int id){
		String sup=null;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());

		SQLiteDatabase db = myDB.getReadableDatabase();

		Cursor c = db.rawQuery(" SELECT Superado FROM Niveles WHERE Numero ="+id, null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya m�s registros
			do {
				sup = c.getString(0);
				System.out.println("NIVEL "+id+" SUPERADO: "+sup);

			} while(c.moveToNext());
		}

		db.close();

		if(sup.equals("true")){
			return true;
		}else{
			return false;
		}		
	}

	public static boolean compararPuntuacion(int id, int puntos){
		int punt = 0;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());

		SQLiteDatabase db = myDB.getReadableDatabase();

		Cursor c = db.rawQuery(" SELECT Puntuacion FROM Niveles WHERE Numero ="+id, null);
		if (c.moveToFirst()) {
			//Recorremos el cursor hasta que no haya m�s registros
			do {
				punt = Integer.parseInt(c.getString(0));
				System.out.println("PUNTUACION DE LA BD: "+punt);

			} while(c.moveToNext());
		}

		db.close();
		
		
		if(puntos>punt){
			return true;
		}else{
			return false;
		}
		

	}
}