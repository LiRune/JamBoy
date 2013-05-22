package com.holycow.database;

import com.holycow.manager.ResourcesManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * En esta clase se crea y gestiona la base de datos que guardará información acerca de los niveles
 * 
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

public class DataBase extends SQLiteOpenHelper 
{
	// Nombre de la base de datos, tabla y columnas
	static final String nombreBD = "JamBoyDB";	
	static final String tablaNiveles = "Niveles";
	static final String IDNivel = "Numero";
	static final String unlocked = "Desbloqueado";
	static final String beat = "Superado";
	static final String score = "Puntuacion";
	static final String stars = "Estrellas";

	public DataBase(Context context)
	{
		super(context, nombreBD, null, 1);
	}

	/**
	 * Crea las tablas y su contenido
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{

		// Crea la base de datos si no existe
		db.execSQL("CREATE TABLE IF NOT EXISTS "+tablaNiveles+" (" +
				IDNivel + " INTEGER PRIMARY KEY , " +
				unlocked + " TEXT, " +
				beat + " TEXT, " +
				score + " TEXT, " +
				stars + " INTEGER" +
				")");
		
		// Insertamos todos los niveles
		db.execSQL("INSERT INTO Niveles VALUES(1, 'true', 'false', 0, 0)");
		db.execSQL("INSERT INTO Niveles VALUES(2, 'false', 'false', 0, 0)");
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		//db.execSQL("DROP TABLE IF EXISTS "+tablaNiveles);
		onCreate(db);
	}


	/**
	 * Hace una consulta a la base de datos para saber si el nivel está desbloqueado
	 * @param id
	 * @return true/false
	 */
	public static boolean isUnlocked(int id)
	{
		String des;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());
		SQLiteDatabase db = myDB.getReadableDatabase();
		
		// Hacemos la consulta del nivel especificado
		Cursor c = db.rawQuery(" SELECT Desbloqueado FROM Niveles WHERE Numero ="+id, null);
		c.moveToFirst();
		des = c.getString(0);

		// Cerramos la base de datos
		db.close();

		if(des.equals("true"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	/**
	 * Hace una consulta a la base de datos para saber si el nivel ha sido superado
	 * @param id
	 * @return true/false
	 */
	public static boolean isBeat(int id)
	{
		String sup;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());
		SQLiteDatabase db = myDB.getReadableDatabase();

		// Hacemos la consulta del nivel especificado
		Cursor c = db.rawQuery(" SELECT Superado FROM Niveles WHERE Numero ="+id, null);
		c.moveToFirst();
		sup = c.getString(0);

		// Cerramos la base de datos
		db.close();

		if(sup.equals("true"))
		{
			return true;
		}
		else
		{
			return false;
		}		
	}

	
	/**
	 * Hace una consulta de la puntuación para saber si la puntuacion de la base de datos es mayor a la obtenida
	 * @param id
	 * @param puntos
	 * @return true/false
	 */
	public static boolean compareScore(int id, int puntos)
	{
		int puntuacion;
		DataBase myDB = new DataBase(ResourcesManager.getActivity());
		SQLiteDatabase db = myDB.getReadableDatabase();

		// Hacemos la consulta del nivel especificado
		Cursor c = db.rawQuery(" SELECT Puntuacion FROM Niveles WHERE Numero ="+id, null);
		c.moveToFirst();
		puntuacion = Integer.parseInt(c.getString(0));
		
		// Cerramos la base de datos
		db.close();
		
		if(puntos > puntuacion)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}