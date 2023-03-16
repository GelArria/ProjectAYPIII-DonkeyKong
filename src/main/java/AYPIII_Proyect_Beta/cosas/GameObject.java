package AYPIII_Proyect_Beta.cosas;

import AYPIII_Proyect_Beta.colisiones.AABB;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.main.Transform;
import AYPIII_Proyect_Beta.renderer.Renderer;
import org.joml.Vector2f;

public class GameObject {

    //Ubicacion Carpetas de Sprite //
    protected String SpritesFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\";
    protected String ShadersFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\shaders\\";
    protected String PersonajeFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\Personaje\\";
    protected String SoundsFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\sounds\\";
       
    //Generales
    public final String name;
    public SpriteRenderer Modelo = null;
    public Transform transform;
    private int zIndex;
    
    //BoundingBox
    public Boolean hasHitBox;
    public AABB boundingBox = null;
    
    public Boolean hasHitBox2;
    public AABB boundingBox2 = null;
    
    //Coordenadas de los extremos de ambas esquinas 
    public Vector2f posEsquina1 = null;
    public Vector2f posEsquina2 = null;

    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param hitbox
     * @param Sprite
     */
    public GameObject(String name, Transform transform, int zIndex,Boolean hitbox, SpriteRenderer Sprite) {
        this.name = name;
        this.zIndex = zIndex;
        this.transform = transform;
        this.hasHitBox = hitbox;
        this.hasHitBox2 = hitbox;
        this.Modelo = Sprite;
    }
      
    //Inicializa el objeto

    /**
     *
     */
    public void start(){
        this.Modelo.gameObject = this;
        this.Modelo.start();
    }

    public int zIndex(){
        return this.zIndex;
    }

    /**
     *
     * @param render
     */
    public void UpdateTextureFromSprite(Renderer render){     
        render.updateRender(this.Modelo);
    }
}
