package DefinitiveAYPIII_Proyect_ver1.jade;

import DefinitiveAYPIII_Proyect_ver1.colisiones.AABB;
import DefinitiveAYPIII_Proyect_ver1.components.Sprite;
import DefinitiveAYPIII_Proyect_ver1.components.SpriteRenderer;
import DefinitiveAYPIII_Proyect_ver1.components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import DefinitiveAYPIII_Proyect_ver1.util.AssetPool;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    //Los objetos del crean el escenario
    private GameObject[][] Fondo = (GameObject[][])new GameObject[9][14];
    private GameObject[][] FondoO = (GameObject[][])new GameObject[9][14]; 
    private GameObject[] Piso1 = (GameObject[])new GameObject[30]; 
    //
    
    
    
    private Spritesheet sprites;
    private String SpritesFolder = "C:\\Users\\Miguel\\Documents\\NetBeansProjects\\DefinitiveAYPIII_Proyect_ver1\\src\\main\\java\\DefinitiveAYPIII_Proyect_ver1\\assets\\images\\";
    private String ShadersFolder = "C:\\Users\\Miguel\\Documents\\NetBeansProjects\\DefinitiveAYPIII_Proyect_ver1\\src\\main\\java\\DefinitiveAYPIII_Proyect_ver1\\assets\\shaders\\";
    
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

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

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
    
    private void generarFondo() {
        
        sprites = AssetPool.getSpritesheet(SpritesFolder+"spritesheet.png");

        float Acarreo = -100;
        for(int i = 0; i < 9 ; i++){
            Acarreo += 100;
            for(int j = 0; j < 14 ; j++){
                
                //Añadimos las texturas del fondo
                Fondo[i][j] = new GameObject("Object "+"["+i+"]["+j+"]", new Transform(new Vector2f(-250+(j*100), 570-Acarreo),new Vector2f(100, 100)), 1, false);
                //Añade el componente del sprite
                Fondo[i][j].addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Fondo1.png"))));
                this.addGameObjectToScene(Fondo[i][j]);
                
                //Ahora Añadimos el filtro oscuro de fondo
                FondoO[i][j] = new GameObject("ObjectO "+"["+i+"]["+j+"]", new Transform(new Vector2f(-250+(j*100), 570-Acarreo),new Vector2f(100, 100)), 2, false);
                //Añade el componente del sprite
                FondoO[i][j].addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage1.png"))));
                this.addGameObjectToScene(FondoO[i][j]);
                
            }
        }
        //Piso
        for(int i = 0; i < 30 ; i++){
                Piso1[i] = new GameObject("Piso1 "+"["+i+"]", new Transform(new Vector2f(-200+(i*40), 100),new Vector2f(40, 32)), 6, false);
                //Añade el componente del sprite
                Piso1[i].addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))));
                this.addGameObjectToScene(Piso1[i]);
        }
        
        AABB Caja1 = new AABB(new Vector2f(0,0),new Vector2f(1,1));
        AABB Caja2 = new AABB(new Vector2f(1,0),new Vector2f(1,1));
                
    }
}
