package DefinitiveAYPIII_Proyect_ver1.jade;

import DefinitiveAYPIII_Proyect_ver1.colisiones.AABB;
import DefinitiveAYPIII_Proyect_ver1.components.SpriteRenderer;
import org.joml.Vector2f;

public class GameObject {

    public final String name;
    public SpriteRenderer Modelo = null;
    public Transform transform;
    private int zIndex;
    public Boolean isSolid;
    public AABB boundingBox;
    

    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex,Boolean isSolid, SpriteRenderer Sprite) {
        this.name = name;
        this.zIndex = zIndex;
        this.transform = transform;
        this.isSolid = isSolid;
        this.Modelo = Sprite;
    }

    public void update(float dt) {   
        if(this.name == "Player"){
            Vector2f movement = null;
            
            float SPEED = 150;
       
            //Inputs de 1 sola tecla
            //DERECHA D
            if(KeyListener.isKeyPressed(68) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){
                
                movement = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //IZQUIERDA A
            if(KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){                
                movement = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //ARRIBA W
            if(KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                movement = new Vector2f(this.transform.position.x,this.transform.position.y += dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //ABAJO S
            if(KeyListener.isKeyPressed(83) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                movement = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //Inputs si se presionan 2 teclas
            
            //DIAGONAL HACIA ARRIBA DERECHA D+W
            if(KeyListener.isKeyPressed(68) && KeyListener.isKeyPressed(87)){
                movement = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y += dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //DIAGONAL HACIA ARRIBA IZQUIERDA D+A
            if(KeyListener.isKeyPressed(65) && KeyListener.isKeyPressed(87)){
                movement = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y += dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //DIAGONAL HACIA ABAJO DERECHA D+S
            if(KeyListener.isKeyPressed(68) && KeyListener.isKeyPressed(83)){
                movement = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y -= dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
            
            //DIAGONAL HACIA ABAJO IZQUIERDA A+S
            if(KeyListener.isKeyPressed(65) && KeyListener.isKeyPressed(83)){
                movement = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y -= dt * SPEED);
                this.transform.ChangeSpritePosition(movement);
            }
          
        }
        
        if(this.name == "Escenario"){
            
            
        }
        
        //Finalmente Updatea la posicion
        this.Modelo.update(dt);
    }

    public void start(){
        this.Modelo.gameObject = this;
        this.Modelo.start();
    }

    public int zIndex() {
        return this.zIndex;
    }

}
