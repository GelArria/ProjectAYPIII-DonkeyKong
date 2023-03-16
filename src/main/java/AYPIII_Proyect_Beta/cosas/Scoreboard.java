package AYPIII_Proyect_Beta.cosas;

import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.game.Scene;
import AYPIII_Proyect_Beta.main.Transform;
import org.joml.Vector2f;

public class Scoreboard extends GameObject{

    private final Vector2f OriginalScale;
    
    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param hitbox
     * @param Sprite
     */
    public Scoreboard(String name, Transform transform, int zIndex, Boolean hitbox, SpriteRenderer Sprite) {
        super(name, transform, zIndex, hitbox, Sprite);
        OriginalScale = transform.scale;
    }
 
    /**
     *
     * @param minus
     * @param terminado
     * @return
     */
    public boolean update(float minus,boolean terminado){ 
        if(!terminado){
            this.transform.scale = new Vector2f(this.transform.scale.x-minus,this.transform.scale.y);
        }else{
            this.transform.scale = new Vector2f(this.OriginalScale.x,this.OriginalScale.y);
            Scene.disableDed();
        }
           
        if(transform.scale.x < 0){
            return true;
        }
        
        this.Modelo.update();
        return false;
    }
      
}
