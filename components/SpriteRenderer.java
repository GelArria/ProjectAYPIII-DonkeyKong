package DefinitiveAYPIII_Proyect_ver1.components;
import DefinitiveAYPIII_Proyect_ver1.colisiones.AABB;
import DefinitiveAYPIII_Proyect_ver1.jade.GameObject;
import DefinitiveAYPIII_Proyect_ver1.jade.KeyListener;
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
        if(this.gameObject.isSolid == true){
           System.out.println(this.gameObject.name+" Objeto Solido es Creado");
           
           Vector2f Center = new Vector2f(lastTransform.position.x+lastTransform.scale.x/2 , lastTransform.position.y - lastTransform.scale.y/2);
           Vector2f halfExtent = new Vector2f(lastTransform.position.x,lastTransform.position.y);
           this.gameObject.boundingBox = new AABB(Center,halfExtent);
        }else{
           this.gameObject.boundingBox = null;       
        }
    }

    public void update(float dt) {   
        
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
