package com.holycow.base;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import com.holycow.manager.ResourcesManager;
import com.holycow.manager.SceneManager.SceneType;

/** 
 * Esta clase será la plantilla para todas las escenas
 * 
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

public abstract class BaseScene extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	protected Engine engine;
	protected static Activity activity;
	protected static ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected static BoundCamera camera;

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
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public static Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public static ResourcesManager getResourcesManager() {
		return resourcesManager;
	}

	public void setResourcesManager(ResourcesManager resourcesManager) {
		this.resourcesManager = resourcesManager;
	}

	public VertexBufferObjectManager getVbom() {
		return vbom;
	}

	public void setVbom(VertexBufferObjectManager vbom) {
		this.vbom = vbom;
	}
	
	public BoundCamera getCamera() {
		return camera;
	}

	public void setCamera(BoundCamera camera) {
		this.camera = camera;
	}
	
	
	//---------------------------------------------
	// ABSTRACCIÓN
	//---------------------------------------------

	

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract void onHomeKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();
}