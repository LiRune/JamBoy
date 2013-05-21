package com.holycow.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.holycow.manager.ResourcesManager;
import com.holycow.pool.BalasPool;

/**
 * @author Holy Cow
 *
 */

public class Bala extends Sprite{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;
	private Bala bala;
	private BalasPool balasPool;
	private boolean colisionEnemigo = false;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Bala(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld, BalasPool balasPool)
	{
		super(pX+15, pY+5, ResourcesManager.getInstance().bala_region, vbo);
		this.balasPool = balasPool;
		createPhysics(camera, physicsWorld);
	}

	// ---------------------------------------------
	// GETTERS & SETTERS
	// ---------------------------------------------

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public boolean isColisionEnemigo() {
		return colisionEnemigo;
	}

	public void setColisionEnemigo(boolean colisionEnemigo) {
		this.colisionEnemigo = colisionEnemigo;
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	
	/**
	 * Crea las fisicas de la bala
	 * @param camera
	 * @param physicsWorld
	 */
	public void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
	{
		bala = this;
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f));
		body.setUserData("bala");
		body.setFixedRotation(true);
		body.setBullet(true);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				if ((!camera.isEntityVisible(bala) && bala.isVisible()) || colisionEnemigo)
				{
					balasPool.recyclePoolItem(bala);
					if(colisionEnemigo)
					{
						colisionEnemigo = false;
					}
				}
			}
		});
	}
}
