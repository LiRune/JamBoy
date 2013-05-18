package com.matimdev.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.matimdev.manager.ResourcesManager;

public class Bala extends Sprite{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private Body body;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------

	public Bala(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY+5, ResourcesManager.getInstance().bala_region, vbo);
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

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{
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
				
				if (balaFueraCamara(camera))
				{
					onDie();
				}
			}
		});
	}
	
	private boolean balaFueraCamara(final Camera camera)
	{
		if(!camera.isEntityVisible(this))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public void onDie()
	{
		this.getBody().setActive(false);
		this.detachSelf();
	}
}
