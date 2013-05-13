package com.matimdev;

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
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

import com.matimdev.manager.ResourcesManager;
import com.matimdev.manager.SceneManager;

public class GameActivity extends BaseGameActivity
{
	private BoundCamera camera;
	

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	
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

		public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
		{
			ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
			pOnCreateResourcesCallback.onCreateResourcesFinished();
		}

		public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
		{
			SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		}

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
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}));
			pOnPopulateSceneCallback.onPopulateSceneFinished();
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

		@Override
		public void onPauseGame()
		{
			super.onPauseGame();
			SceneManager.getInstance().getCurrentScene().getResourcesManager().music.pause();
		}

		@Override
		public void onResumeGame()
		{
			super.onResumeGame();
			if (isGamePaused ())
			{
				SceneManager.getInstance().getCurrentScene().getResourcesManager().music.resume();
			}
		}
	}