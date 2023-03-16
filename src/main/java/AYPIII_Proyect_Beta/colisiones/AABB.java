package AYPIII_Proyect_Beta.colisiones;

import AYPIII_Proyect_Beta.cosas.Entidad;
import org.joml.Vector2f;

public class AABB {
    
        public boolean touchinground;
        public static boolean touchingroof;
        
        public Vector2f Corner1,Corner2;
        public Collision CollisionData; 
        public float width;
        public float height;
        
    /**
     *
     * @param pos
     * @param width
     * @param height
     */
    public AABB(Vector2f pos, float width , float height) {
		this.width = width;
                this.height = height;
                this.Corner1 = pos;
                this.Corner2 = new Vector2f(pos.x+width,pos.y-height);
	}
	
    /**
     *
     * @param Box2
     * @return
     */
    public Collision getCollision(AABB Box2){
            
            Vector2f distanceE1 = Box2.Corner1.sub(Corner1, new Vector2f());
            Vector2f distanceE2 = Box2.Corner2.sub(Corner2, new Vector2f());
            
            distanceE1.x = Math.abs(distanceE1.x);
            distanceE1.y = Math.abs(distanceE1.y);
            
            distanceE2.x = Math.abs(distanceE2.x);
            distanceE2.y = Math.abs(distanceE2.y);
     
            distanceE1.sub(new Vector2f(Box2.width,Box2.height));
            distanceE2.sub(new Vector2f(Box2.width,Box2.height));
            
          //  System.out.println("E1 TO BOX2 E2  "+distanceE1.x+" , "+distanceE1.y);
          //  System.out.println("E2 TO BOX2 E1  "+distanceE2.x+" , "+distanceE2.y);
        
              if(distanceE1.x < 0 && 
                 distanceE1.y < 0 &&
                 distanceE2.x < 0 && 
                 distanceE2.y < 0 ){
                  return new Collision(distanceE1,distanceE2,true);
              }else{
                  return new Collision(distanceE1,distanceE2,false);
              }
                 
        }
       
    /**
     *
     * @param Box2
     * @param data
     * @param O
     */
    public void correctPosition(AABB Box2, Collision data,Entidad O) {
            
                //ENTERING FROM THE RIGHT
                if (data.distanceC2.x > data.distanceC1.x && data.distanceC2.x > data.distanceC2.y && data.distanceC2.x > data.distanceC1.y) {
                    O.transform.position.add(-Math.abs(data.distanceC2.x),0);
                    O.boundingBox.updateBoundingBox(this,O.transform.position);
		}else{
                    //ENTERING FROM THE LEFT
                    if (data.distanceC2.x < data.distanceC1.x && data.distanceC2.y < data.distanceC1.x && data.distanceC1.y < data.distanceC1.x){
                        O.transform.position.add(Math.abs(data.distanceC1.x),0);
                        O.boundingBox.updateBoundingBox(this,O.transform.position);
                    }else{
                        //ENTERING FROM ABOVE
                        if  (data.distanceC2.y > data.distanceC1.y && data.distanceC2.y > data.distanceC2.x && data.distanceC2.y > data.distanceC1.x){
                            O.isJumping = false;
                            
                            O.JumpingSound.isPlaying = false;
                            
                            O.transform.position.y = Box2.Corner1.y+O.boundingBox.height;
                            O.boundingBox.updateBoundingBox(this,O.transform.position);
                            touchinground = true;
                        }else{ 
                            //FROM BELOW
                            if  (data.distanceC2.y < data.distanceC1.y && data.distanceC1.x < data.distanceC1.y && data.distanceC2.x < data.distanceC1.y){
                                O.transform.position.y = Box2.Corner2.y;
                                O.boundingBox.updateBoundingBox(this,O.transform.position);
                                touchingroof = true;
                            }
                        }
                    }
                }
        }
	    
    //This is a function that puts the same coords from the transform position to the boundingBoxes Corners positions
    /**
     *
     * @param Box
     * @param n
     */
        public void updateBoundingBox(AABB Box,Vector2f n){
           Box.Corner1 = n;
           Box.Corner2 = new Vector2f(n.x+width,n.y-height);
        }      
        
}
