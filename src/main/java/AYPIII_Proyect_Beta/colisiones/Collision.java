package AYPIII_Proyect_Beta.colisiones;

import org.joml.Vector2f;

public class Collision {
	public Vector2f distanceC1,distanceC2;
	public boolean isIntersecting;
	
    /**
     *
     * @param distanceE1
     * @param distanceE2
     * @param intersects
     */
    public Collision(Vector2f distanceE1,Vector2f distanceE2, boolean intersects) {
		this.distanceC1 = distanceE1;
                this.distanceC2 = distanceE2;
		this.isIntersecting = intersects;
	}
}
