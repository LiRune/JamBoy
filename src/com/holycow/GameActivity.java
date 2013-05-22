package com.holycow;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

import com.holycow.base.BaseScene;
import com.holycow.manager.ResourcesManager;
import com.holycow.manager.SceneManager;

/** 
 * Actividad principal y �nica
 * En esta clase cargamos el engine, los recursos y la primera escena
 * 
 * @author Samir El Aoufi
 * @author Juan Jos� Cillero
 * @author Rub�n D�az
 *
 */

public class GameActivity extends BaseGameActivity
{
	// La c�mara y sus medidas
	private final int CAMERA_WIDTH = 800;
	private final int CAMERA_HEIGHT = 480;
	private BoundCamera camera;

	/**
	 * Primero crear� el Engine
	 */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		// Limita las actualizaciones por segundo
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	/**
	 * En este metodo se especifican algunas opciones del Engine
	 */
	public EngineOptions onCreateEngineOptions()
	{
		// Asignamos los l�mites a la c�mara
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		// Fijamos la pantalla en horizontal
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
		
		// Habilitamos m�sica y sonido
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		// Habilitamos el Multi Touch
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}
	
	/**
	 * Aqu� cargar� todos los recursos que especifiquemos
	 */
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		// Carga los recursos con la clase creada ResourcesManager
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	/**
	 * Se crea la primera escena que aparece al abrir la aplicacion, en este caso el splash
	 */
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		// Creamos la escena con la clase SceneManager
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	
	/**
	 * Ocurre despu�s de haber cargado todo. Despues de unos segundos, desaparece el splash y carga la clase SceneManager
	 */
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		// Creamos un temporizador para que la escena Splash se quede un rato en pantalla antes de cargar el Men�
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				try 
				{
					SceneManager.getInstance().createMenuScene();
				} 
				catch (IllegalStateException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	/**
	 * Se activar� al pulsar un bot�n del dispositivo
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
		// Dependiendo del bot�n ejecutar� un m�todo abstracto creado en SceneManager
		// Bot�n Atr�s
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		// Bot�n Home
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			SceneManager.getInstance().getCurrentScene().onHomeKeyPressed();
		}
		return false; 
	}

	/**
	 * M�todo que se ejecuta al ir al Home del dispositivo
	 */
	@Override
	public void onPause()
	{
		super.onPause();
		
		// Si el juego est� iniciado pausamos la m�sica
		if (this.isGameLoaded())
		{
			SceneManager.getInstance().getCurrentScene();
			BaseScene.getResourcesManager().music.pause();
		}
	}

	/**
	 * M�todo que se ejecuta al volver al juego desde el Home del dispositivo
	 */
	@Override
	public synchronized void onResume()
	{
		super.onResume();
		
		// Si el juego est� iniciado reanudaremos la m�sica
		if (this.isGameLoaded())
		{
			SceneManager.getInstance().getCurrentScene();
			BaseScene.getResourcesManager().music.resume();
		}
	}
	
	/**
	 * M�todo que se ejecuta al salir del programa
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (this.isGameLoaded())
		{
			System.exit(0);	
		}	
	}
}
