package com.matimdev.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.matimdev.GameActivity;
import com.matimdev.base.BaseScene;
import com.matimdev.manager.ResourcesManager;
import com.matimdev.manager.SceneManager;
import com.matimdev.scene.GameScene;

public abstract class Player extends AnimatedSprite
{
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	private Body body;

    private boolean canRun = false;
    public GameScene g;
    public SceneManager s;
    private BaseScene juego;
    private ResourcesManager r;
    private int footContacts = 0;
    private PhysicsWorld physicsWorld;
    public Player p;

    private Body mBulletBody;
	
	
	
	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
	{
		super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
	
	
	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	public Body getBody() {
		return body;
	}




	public void setBody(Body body) {
		this.body = body;
	}




	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
	{		
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));

		body.setUserData("player");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
	        public void onUpdate(float pSecondsElapsed)
	        {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				
				if (getY() <= 0)
				{					
					onDie();
				}
				
				/*if (canRun)
				{	
					body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y)); 
				}*/
	        }
		});
	}
	
	public void setRunning()
	{
		canRun = false;
		
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
		
		animate(PLAYER_ANIMATE, 0, 2, true);
	}
	
	public void runLeft(){
		if (!isFlippedHorizontal()) 
		{ 
		setFlippedHorizontal(true); 
		}
		body.setLinearVelocity(new Vector2(-5, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
		
		animate(100);
	}
	
	public void runRight(){
		if (isFlippedHorizontal()) 
		{ 
		setFlippedHorizontal(false); 
		}
		body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] {100,100,100 };
		
		animate(100);
		
	}
	
	public void stop(){
		if (isFlippedHorizontal()) 
		{ 
		setFlippedHorizontal(false); 
		}
		body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] {0,0,0 };
		
		animate(PLAYER_ANIMATE, 0, 2, false);
		
	}
	
	public void stop2(){
		if (isFlippedHorizontal()) 
		{ 
		setFlippedHorizontal(true); 
		}
		body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y)); 
		final long[] PLAYER_ANIMATE = new long[] {0,0,0 };
		
		animate(PLAYER_ANIMATE, 0, 2, false);
		
	}
	
	
	
	public void jump()
	{
		
		if (footContacts < 1) 
		{
			return; 
		}
		else{
			GameActivity.salto.play();
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12)); 
	}
	
	
	
	public void increaseFootContacts()
	{
		footContacts++;
	}
	
	public void decreaseFootContacts()
	{
		footContacts--;
	}
	
	public abstract void onDie();
	
	public void fire() {
	   	 float startBulletX=p.getX();
	   	 float startBulletY=p.getY();
	   	 Sprite bullet=new Sprite(startBulletX,startBulletY,r.bala_region,g.getVbom());
	   	 
	   	 
	   	 final float xComp =200;
	   	 final float yComp =p.getY();
	   	 final Vector2 velocity = Vector2Pool.obtain(xComp * 10, yComp * 10);
	   	 physicsWorld= g.getPhysicsWorld();

	   	 //bullet.setRotation(this.player.getRotation());

	   	 final FixtureDef bulletFixtureDef1 = PhysicsFactory.createFixtureDef(0, 0, 0);
	   	 mBulletBody = PhysicsFactory.createBoxBody(this.physicsWorld, bullet, BodyType.KinematicBody, bulletFixtureDef1);
	   	 mBulletBody.setLinearVelocity(velocity);
	   	 Vector2Pool.recycle(velocity);
	   	 physicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet, this.mBulletBody, true, false));
	   	 //this.getScene().attachChild(bullet);
	}
}