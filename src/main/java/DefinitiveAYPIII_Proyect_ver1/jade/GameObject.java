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
    public AABB boundingBox = null;
    
    public Vector2f posEsquina1 = null;
    public Vector2f posEsquina2 = null;
    
    
    public GameObject(String name) {
        this.name = name;
        this.transform = new Transform();
        this.zIndex = 0;
        this.isSolid = false;
        this.Modelo = null;
    }

    public GameObject(String name, Transform transform, int zIndex,Boolean isSolid, SpriteRenderer Sprite) {
        this.name = name;
        this.zIndex = zIndex;
        this.transform = transform;
        this.isSolid = isSolid;
        this.Modelo = Sprite;
    }

    public void update(float dt, AABB Box2) {   
        
        if(this.name == "Player"){
            
            float SPEED = 150;
       
            //Inputs de 1 sola tecla
            //DERECHA D
            if(KeyListener.isKeyPressed(68) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){
                
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
                
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //IZQUIERDA A
            if(KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){                
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
              
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //ARRIBA W
            if(KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y += dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
            
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //ABAJO S
            if(KeyListener.isKeyPressed(83) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
           
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //Inputs si se presionan 2 teclas
            
            //DIAGONAL HACIA ARRIBA DERECHA D+W
            if(KeyListener.isKeyPressed(68) && KeyListener.isKeyPressed(87)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y += dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);

                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //DIAGONAL HACIA ARRIBA IZQUIERDA D+A
            if(KeyListener.isKeyPressed(65) && KeyListener.isKeyPressed(87)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y += dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
       
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //DIAGONAL HACIA ABAJO DERECHA D+S
            if(KeyListener.isKeyPressed(68) && KeyListener.isKeyPressed(83)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x += dt * SPEED,this.transform.position.y -= dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
           
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            //DIAGONAL HACIA ABAJO IZQUIERDA A+S
            if(KeyListener.isKeyPressed(65) && KeyListener.isKeyPressed(83)){
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x -= dt * SPEED,this.transform.position.y -= dt * SPEED);
                Vector2f newposEsquina2 = new Vector2f(posEsquina1.x+this.transform.scale.x,posEsquina1.y-this.transform.scale.y);
                
                posEsquina1 = newposEsquina1;
                posEsquina2 = newposEsquina2;
            }
            
            this.transform.ChangeSpritePosition(posEsquina1);
            //System.out.println("Esquina1: <"+this.boundingBox.Esquina1.x+","+this.boundingBox.Esquina1.y+"> Esquina2: <"+this.boundingBox.Esquina2.x+","+this.boundingBox.Esquina2.y+">");
            
            //Updatea la hitbox de ambos objetos
            this.boundingBox.Esquina1 = posEsquina1; 
            this.boundingBox.Esquina2 = posEsquina2;
            
            
            
            if(this.boundingBox.getCollision(Box2)){
               System.out.println("COLISION");
            }
           
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
