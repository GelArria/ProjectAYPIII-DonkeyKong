package DefinitiveAYPIII_Proyect_ver1.colisiones;

import org.joml.Vector2f;

public class AABB {
    public Vector2f Esquina1,Esquina2;
    boolean isColliding = false;
    
	public AABB(Vector2f pos, float width , float height) {
		
                this.Esquina1 = pos;
                this.Esquina2 = new Vector2f(pos.x+width,pos.y-height);
               
                this.isColliding = false;
	}
	
        public boolean getCollision(AABB Box2){
            if(Box2.Esquina1.x < this.Esquina2.x && 
               Box2.Esquina2.x > this.Esquina1.x &&
               Box2.Esquina1.y > this.Esquina2.y && 
               Box2.Esquina2.y < this.Esquina1.y){
               
                return true;
            }else{
                return false;
            }
           
        }
        
        public void correctPosition(AABB Box2){
            
        }
}
