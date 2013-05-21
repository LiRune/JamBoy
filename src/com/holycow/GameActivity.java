package com.holycow;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
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
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

import com.holycow.base.BaseScene;
import com.holycow.database.DataBase;
import com.holycow.manager.ResourcesManager;
import com.holycow.manager.SceneManager;

/** Actividad principal i única
 * En esta clase cargamos el engine y los recursos
 * 
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

public class GameActivity extends BaseGameActivity
{
	private BoundCamera camera;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	/**
	 * En este metodo se especifica el tamano de la camara, la orientacion horizontal y habilita el multi touch
	 */
	public EngineOptions onCreateEngineOptions()
	{
		camera = new BoundCamera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		return engineOptions;
	}
	
	/**
	 * Le decimos en que clase cargamos las texturas, sonidos...
	 */
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	/**
	 * La primera escena que aparece al abrir la aplicacion, en este caso el splash
	 */
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	{
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	
	/**
	 * Despues de unos segundos, desaparece el splash y carga la clase SceneManager
	 */
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				try {
					SceneManager.getInstance().createMenuScene();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	/**
	 * Accion que se realiza al pulsar el boton atras del movil
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		else if(keyCode == KeyEvent.KEYCODE_HOME)
		{
			SceneManager.getInstance().getCurrentScene().onHomeKeyPressed();
		}
		return false; 
	}

	/**
	 * Pausa el sonido
	 */
	@Override
	public void onPause()
	{
		super.onPause();
		if (this.isGameLoaded())
		{
			SceneManager.getInstance().getCurrentScene();
			BaseScene.getResourcesManager().music.pause();
		}
	}

	/**
	 * Reanuda el sonido
	 */
	@Override
	public synchronized void onResume()
	{
		super.onResume();
		if (this.isGameLoaded())
		{
			SceneManager.getInstance().getCurrentScene();
			BaseScene.getResourcesManager().music.resume();
		}
	}
	
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
