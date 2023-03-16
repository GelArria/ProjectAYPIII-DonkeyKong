package AYPIII_Proyect_Beta.cosas;

import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.main.Transform;
import AYPIII_Proyect_Beta.renderer.Renderer;
import org.joml.Vector2f;

public class Escenario extends GameObject{
    
    public boolean IsSolid1;
    public boolean IsSolid2;
    
    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param HasHitBox
     * @param Sprite
     * @param isSolid
     */
    public Escenario(String name, Transform transform, int zIndex, Boolean HasHitBox, SpriteRenderer Sprite,Boolean isSolid){
        super(name, transform, zIndex, HasHitBox, Sprite);
        this.IsSolid1 = isSolid;
        this.IsSolid2 = false;
    }
    
    /**
     *
     * @param dt
     * @param render
     */
    public void update(float dt,Renderer render){     
        
        if(this.name == "AscensorA"){
           Movimiento(dt,1);
        }
        
        if(this.name == "AscensorD"){
           Movimiento(dt,2);
        }
         
        //Checkea si una plataforma del ascensor llego al tope
        if(this.name == "AscensorA"){
            if(this.posEsquina2.y > 475){
              
                this.transform.position.y = 18;
                this.posEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y);
                this.posEsquina2 = new Vector2f(this.transform.position.x,10-this.transform.scale.y);
            }
        }
        //Checkea si una plataforma descendiente del ascensor llego al piso
        if(this.name == "AscensorD"){
            if(this.posEsquina1.y < 18){
                
                this.transform.position.y = 475;
                this.posEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y);
                this.posEsquina2 = new Vector2f(this.transform.position.x,500-this.transform.scale.y);
            }
        }
        
        //Actualiza la posiciones del objeto
        this.boundingBox.Corner1 = this.posEsquina1;
        this.boundingBox.Corner2 = this.posEsquina2;
        
        this.Modelo.update(dt);
    }
    
    /**
     *
     * @param dt
     * @param Direccion
     */
    public void Movimiento(float dt,int Direccion){
        Vector2f newposEsquina1;
        Vector2f newposEsquina2;
        
        if(Direccion == 1){
            newposEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y += dt * 30);
            newposEsquina2 = new Vector2f(newposEsquina1.x+this.transform.scale.x,newposEsquina1.y-this.transform.scale.y);
            
            this.posEsquina1 = newposEsquina1;
            this.posEsquina2 = newposEsquina2;
            
        }else{
            newposEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * 30);
            newposEsquina2 = new Vector2f(newposEsquina1.x+this.transform.scale.x,newposEsquina1.y-this.transform.scale.y);
            
            this.posEsquina1 = newposEsquina1;
            this.posEsquina2 = newposEsquina2;  
            
        }
        
        
        
    }
    
    
}
