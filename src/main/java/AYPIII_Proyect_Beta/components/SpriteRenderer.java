package AYPIII_Proyect_Beta.components;

import AYPIII_Proyect_Beta.cosas.GameObject;
import AYPIII_Proyect_Beta.colisiones.AABB;
import AYPIII_Proyect_Beta.main.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import AYPIII_Proyect_Beta.renderer.Texture;


public class SpriteRenderer{
    
    public GameObject gameObject = null;
    private Vector4f color;
    public Sprite sprite;
    private Transform lastTransform;
    public boolean isDirty = false;

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        this.isDirty = true;
    }

    public void start() {
        
        this.lastTransform = this.gameObject.transform.copy();

        AABB newBoundingBox = null;
        if(this.gameObject.name == "Player" || this.gameObject.name == "Barril" || this.gameObject.name == "Fuego"){
            newBoundingBox = new AABB(this.lastTransform.position, this.lastTransform.scale.x, this.lastTransform.scale.y);
        }
        
        if(this.gameObject.name == "Barril" || this.gameObject.name == "Fuego"){
            Vector2f Aux3 = this.lastTransform.position; 
            Aux3.y += 10;
            this.gameObject.boundingBox2 = new AABB(Aux3, this.lastTransform.scale.x-5, this.lastTransform.scale.y+20);
        }
        
        if(this.gameObject.name == "Tornillo"){
            Vector2f Aux4 = this.lastTransform.position;
            Aux4.x -= 5;
            Aux4.y += 20;
            this.gameObject.boundingBox2 = new AABB(Aux4, this.lastTransform.scale.x-8, this.lastTransform.scale.y-10);
        }
        
        
        if(this.gameObject.name == "Panel"){
            Vector2f Aux1 = this.lastTransform.position;
            Aux1.y += 650f;
            newBoundingBox = new AABB(Aux1, this.lastTransform.scale.x, this.lastTransform.scale.y);
            
        }else{
            Vector2f Aux2 = this.lastTransform.position;
            Aux2.y += 3f;
            newBoundingBox = new AABB(Aux2, this.lastTransform.scale.x-10, this.lastTransform.scale.y);
        }
         
        this.gameObject.posEsquina1 = newBoundingBox.Corner1;
        this.gameObject.posEsquina2 = newBoundingBox.Corner2;
        
        //Si el objeto es solido entonces se le asigna la nueva bounding box
        
        if(this.gameObject.hasHitBox == true){
        
           this.gameObject.boundingBox = newBoundingBox;
           
        }else{
           
           this.gameObject.boundingBox = null;  
           
        }
        
    }

    /**
     *
     * @param dt
     */
    public void update(float dt) {   
        
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        } 
    }
    
    public void update() {   
        
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        } 
    }
    
    
    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setTexture(Texture tex){
        this.sprite.setTexture(tex);
    }
    
    /**
     *
     * @param sprite
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    /**
     *
     * @param color
     */
    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }
}
