package com.matimdev.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.matimdev.manager.ResourcesManager;
import com.matimdev.manager.SceneManager.SceneType;

public abstract class BaseScene extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	protected Engine engine;
	protected static Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;

	public VertexBufferObjectManager getVbom() {
		return vbom;
	}

	public void setVbom(VertexBufferObjectManager vbom) {
		this.vbom = vbom;
	}

	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public void setResourcesManager(ResourcesManager resourcesManager) {
		this.resourcesManager = resourcesManager;
	}

	public static Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------



	public BaseScene()
	{
		this.setResourcesManager(ResourcesManager.getInstance());
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.setVbom(resourcesManager.vbom);
		this.camera = resourcesManager.camera;
		createScene();
	}

	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract void onHomeKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();

}