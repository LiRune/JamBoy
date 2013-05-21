package com.holycow.pool;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.GenericPool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.holycow.manager.ResourcesManager;
import com.holycow.manager.SceneManager;
import com.holycow.object.Bala;
import com.holycow.object.Player;
import com.holycow.scene.GameScene;

/**Clase que gestiona el pool de Bala
 * @author Samir El Aoufi
 * @author Juan José Cillero
 * @author Rubén Díaz
 *
 */

public class BalasPool extends GenericPool<Bala> {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	
	private float balaX;
	private Player player;
	private VertexBufferObjectManager vbom;
	private BoundCamera camera;
	private PhysicsWorld physicsWorld;

	// ---------------------------------------------
	// CONSTRUCTOR
	// ---------------------------------------------
	
	public BalasPool(VertexBufferObjectManager vbom, BoundCamera camera, PhysicsWorld physicsWorld, Player player) {
		this.vbom = vbom;
		this.camera = camera;
		this.physicsWorld = physicsWorld;
		this.player = player;
	}

	/**
	 * Llamado cuando Bala es requerida pero no hay en el pool
	 */
	@Override
	protected Bala onAllocatePoolItem() {
		if(player.isFlippedHorizontal())
		{
			this.balaX = player.getX() - 30;
		}
		else
		{
			this.balaX = player.getX() + 30;
		}
		
		return new Bala(posicionBalaX(), player.getY(), vbom, camera, physicsWorld, this);
	}

	/**
	 * Llamada cuando Bala se envia a pool
	 */
	@Override
	protected void onHandleRecycleItem(final Bala pBullet) {
		pBullet.setVisible(false);
		pBullet.detachSelf();
		pBullet.clearEntityModifiers();
		pBullet.clearUpdateHandlers();
		pBullet.getBody().setActive(false);
	}

	/**
	 * Llama antes de que Bala sea devuelta
	 * 
	 */
	@Override
	protected void onHandleObtainItem(final Bala pBullet) {
		pBullet.reset();
		pBullet.getBody().setActive(true);		
		pBullet.getBody().setTransform(new Vector2((posicionBalaX()+15) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (player.getY()+5) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),0);
	}
	
	/**
	 * Posicion de Bala
	 * @return
	 */
	public float posicionBalaX()
	{
		if(player.isFlippedHorizontal())
		{
			return player.getX() - 30;
		}
		else
		{
			return player.getX() + 30;
		}
	}
}
