package DefinitiveAYPIII_Proyect_ver1.jade;

import DefinitiveAYPIII_Proyect_ver1.components.Sprite;
import DefinitiveAYPIII_Proyect_ver1.components.SpriteRenderer;
import DefinitiveAYPIII_Proyect_ver1.components.Spritesheet;
import org.joml.Vector2f;
import DefinitiveAYPIII_Proyect_ver1.util.AssetPool;

public class LevelEditorScene extends Scene {

    //El fondo
    private GameObject[][] Fondo = (GameObject[][])new GameObject[8][13];
    private GameObject[][] FondoO = (GameObject[][])new GameObject[8][13]; 
   
    
    //El escenario + Jugador
    private GameObject Bloque;
    private GameObject Player;
    //
    
    public Spritesheet sprites;
    private String SpritesFolder = "C:\\Users\\Miguel\\Documents\\NetBeansProjects\\DefinitiveAYPIII_Proyect_ver1\\src\\main\\java\\DefinitiveAYPIII_Proyect_ver1\\assets\\images\\";
    private String ShadersFolder = "C:\\Users\\Miguel\\Documents\\NetBeansProjects\\DefinitiveAYPIII_Proyect_ver1\\src\\main\\java\\DefinitiveAYPIII_Proyect_ver1\\assets\\shaders\\";
    
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(0, 0));

        generarFondo();

    }

    private void loadResources() {
        AssetPool.getShader(ShadersFolder+"default.glsl");

        AssetPool.addSpritesheet(SpritesFolder+"spritesheet.png",
                new Spritesheet(AssetPool.getTexture(SpritesFolder+"spritesheet.png"),
                        16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {

        this.Player.update(dt,this.Bloque.boundingBox);
        
        this.renderer.render();
    }
    
    private void generarFondo() {
        
        sprites = AssetPool.getSpritesheet(SpritesFolder+"spritesheet.png");

        //El fondo como sera estatico entonces no se estara actualizando...
        
        float Acarreo = 0;
        for(int i = 0; i < 8 ; i++){
            Acarreo += 100;
            for(int j = 0; j < 13 ; j++){
                
                //Añadimos las texturas del fondo
                Fondo[i][j] = new GameObject("Object "+"["+i+"]["+j+"]", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 1, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Fondo1.png"))));
                //Añade el objecto a la Escena
                this.addGameObjectToScene(Fondo[i][j]);
                
                //Ahora Añadimos el filtro oscuro de fondo
                FondoO[i][j] = new GameObject("ObjectO "+"["+i+"]["+j+"]", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 2, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage1.png"))));
                //Añade el objecto a la Escena
                this.addGameObjectToScene(FondoO[i][j]);
                
            }
        }
        //bloque de prueba
        Bloque = new GameObject("Bloque", new Transform(new Vector2f(500,300),new Vector2f(100, 100)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage2.png"))));
        this.addGameObjectToScene(Bloque);
        
        Player = new GameObject("Player", new Transform(new Vector2f(100,220),new Vector2f(32, 40)), 3, true ,new SpriteRenderer(sprites.getSprite(0))); 
        this.addGameObjectToScene(Player);
        
    }
}
