package com.matimdev.database;

import com.matimdev.base.BaseScene;
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
	static final String unlocked = "Bloqueado";
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

	public String isLevelUnLocked(int ID) {
		// THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO READ A VALUE FROM THE DATABASE                 
		SQLiteDatabase myDB = this.getReadableDatabase();
		String[] mySearch = new String[]{String.valueOf(ID)};
		Cursor myCursor = myDB.rawQuery("SELECT "+ unlocked +" FROM "+ tablaNiveles +" WHERE "+ IDNivel +"=?",mySearch);
		myCursor.moveToFirst();
		int index = myCursor.getColumnIndex(unlocked);
		String myAnswer = myCursor.getString(index);
		myCursor.close();
		return myAnswer;
	}

	public int unLockLevel(int ID, String isUnLocked)
	{
		// THIS METHOD IS CALLED BY YOUR MAIN ACTIVITY TO WRITE A VALUE TO THE DATABASE          
		SQLiteDatabase myDB = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(unlocked, isUnLocked);
		int numRowsAffected = myDB.update(tablaNiveles, cv, IDNivel+"=?", new String []{String.valueOf(ID)});
		return numRowsAffected;
	}


	/*       
	 * MORE ADVANCED EXAMPLES OF USAGE
	 *
         void AddEmployee(String _name, int _age, int _dept) {
                SQLiteDatabase db= this.getWritableDatabase();
                ContentValues cv=new ContentValues();
                        cv.put(colName, _name);
                        cv.put(colAge, _age);
                        cv.put(colDept, _dept);
                        //cv.put(colDept,2);
                db.insert(employeeTable, colName, cv);
                db.close();
        }

         int getEmployeeCount()
         {
                SQLiteDatabase db=this.getWritableDatabase();
                Cursor cur= db.rawQuery("Select * from "+employeeTable, null);
                int x= cur.getCount();
                cur.close();
                return x;
         }

         Cursor getAllEmployees()
         {
                 SQLiteDatabase db=this.getWritableDatabase();
                 //Cursor cur= db.rawQuery("Select "+colID+" as _id , "+colName+", "+colAge+" from "+employeeTable, new String [] {});
                 Cursor cur= db.rawQuery("SELECT * FROM "+viewEmps,null);
                 return cur;
         }

         public int GetDeptID(String Dept)
         {
                 SQLiteDatabase db=this.getReadableDatabase();
                 Cursor c=db.query(deptTable, new String[]{colDeptID+" as _id",colDeptName},colDeptName+"=?", new String[]{Dept}, null, null, null);
                 //Cursor c=db.rawQuery("SELECT "+colDeptID+" as _id FROM "+deptTable+" WHERE "+colDeptName+"=?", new String []{Dept});
                 c.moveToFirst();
                 return c.getInt(c.getColumnIndex("_id"));
         }

         public String GetDept(int ID)
         {
                 SQLiteDatabase db=this.getReadableDatabase();
                 String[] params=new String[]{String.valueOf(ID)};
                 Cursor c=db.rawQuery("SELECT "+colDeptName+" FROM"+ deptTable+" WHERE "+colDeptID+"=?",params);
                 c.moveToFirst();
                 int index= c.getColumnIndex(colDeptName);
                 return c.getString(index);
         }

         public Cursor getEmpByDept(String Dept)
         {
                 SQLiteDatabase db=this.getReadableDatabase();
                 String [] columns=new String[]{"_id",colName,colAge,colDeptName};
                 Cursor c=db.query(viewEmps, columns, colDeptName+"=?", new String[]{Dept}, null, null, null);
                 return c;
         }

         public int UpdateEmp(String _name, int _age, int _dept, int _eid)
         {
                 SQLiteDatabase db=this.getWritableDatabase();
                 ContentValues cv=new ContentValues();
                 cv.put(colName, _name);
                 cv.put(colAge, _age);
                 cv.put(colDept, _dept);
                 return db.update(employeeTable, cv, colID+"=?", new String []{String.valueOf(_eid)});
         }

         public void DeleteEmp(String _name, int _age, int _dept, int _eid)
         {
                 SQLiteDatabase db=this.getWritableDatabase();
                 db.delete(employeeTable,colID+"=?", new String [] {String.valueOf(_eid)});
                 db.close();           
         }
	 */       

}