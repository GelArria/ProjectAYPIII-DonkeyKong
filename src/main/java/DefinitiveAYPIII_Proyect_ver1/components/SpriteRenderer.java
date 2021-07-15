package DefinitiveAYPIII_Proyect_ver1.components;

import DefinitiveAYPIII_Proyect_ver1.colisiones.AABB;
import DefinitiveAYPIII_Proyect_ver1.jade.GameObject;
import DefinitiveAYPIII_Proyect_ver1.jade.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import DefinitiveAYPIII_Proyect_ver1.renderer.Texture;


public class SpriteRenderer{
    
    public GameObject gameObject = null;
    private Vector4f color;
    private Sprite sprite;
    private Transform lastTransform;
    private boolean isDirty = false;
    
    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.isDirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        this.isDirty = true;
    }

    public void start() {
        
        this.lastTransform = this.gameObject.transform.copy();
        Vector2f pos = this.lastTransform.position;
        
        AABB newBoundingBox;
        if(this.gameObject.name == "Player"){
            newBoundingBox = new AABB(pos, this.lastTransform.scale.x, this.lastTransform.scale.y);
        }else{
            //Hice el offset de la bounding box manual porque las coordenadas de la AABB no son la misma proporcion que el renderizado
            pos.x = pos.x+5.5f;
            pos.y = pos.y+(this.lastTransform.scale.y/2)+10f;
            newBoundingBox = new AABB(pos, this.lastTransform.scale.x-10, this.lastTransform.scale.y-1);
        }
        this.gameObject.posEsquina1 = newBoundingBox.Esquina1;
        this.gameObject.posEsquina2 = newBoundingBox.Esquina2;
        
        
        //Si el objeto es solido entonces se le asigna la nueva bounding box
        if(this.gameObject.isSolid == true){
           
           this.gameObject.boundingBox = newBoundingBox;
           
        }else{
           
           this.gameObject.boundingBox = null;  
           
        }
        
    }

    public void update(float dt) {   
        
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            //Ademas de Updatear la projeccion del transform, ahora tambien se actualiza la posicion de la bounding box
            if(this.gameObject.isSolid == true)
                updateBoundingBox(this.gameObject.transform);
            
            isDirty = true;
        } 
    }
       
    public void updateBoundingBox(Transform UpdatedTransform){
        
        
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

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

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
