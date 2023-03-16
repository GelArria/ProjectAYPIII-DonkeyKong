package AYPIII_Proyect_Beta.game;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.main.Camera;
import AYPIII_Proyect_Beta.cosas.GameObject;
import AYPIII_Proyect_Beta.components.Spritesheet;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.cosas.Escenario;
import AYPIII_Proyect_Beta.cosas.Scoreboard;
import AYPIII_Proyect_Beta.main.Sonido;
import AYPIII_Proyect_Beta.main.Transform;
import AYPIII_Proyect_Beta.main.Window;
import static AYPIII_Proyect_Beta.main.Window.Players;
import AYPIII_Proyect_Beta.renderer.Renderer;
import AYPIII_Proyect_Beta.util.AssetPool;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;

public abstract class Scene {

    //Init la variables del spritesheet
    protected Spritesheet spritesB, spritesFuego;
    protected Camera camera = new Camera();
    public Renderer renderer = new Renderer();
    private boolean isRunning = false;
    
    public Entidad CurrentPlayer;
    
    //Init a la variable donde se guardaran todos los gameObjects. Despues de que una escena termine este se vacia o se reinicia
    protected List<GameObject> gameObjects = new ArrayList<>();
    
    protected static String SpritesFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\";
    protected static String ShadersFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\shaders\\";
    protected static String PersonajeFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\Personaje\\";
    protected static String SonidosFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\sounds\\";
    protected static String EnemiesFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\Sprite\\Fuego\\";
            
    //El fondo
    protected Escenario[][] Fondo = (Escenario[][])new Escenario[8][13];
    protected Escenario[][] FondoO = (Escenario[][])new Escenario[8][13]; 
    
    protected static Escenario[][] FondoGameOver = (Escenario[][])new Escenario[8][13];
    protected Escenario GameOverTitle;
    
    //Current Player que se esta jugando localmente
    protected static Entidad currentPlayer;
    
    //Pauline y Donkey Kong
    protected Escenario DK;
    protected Escenario Pauline;
    
    //End
    public static boolean S_Activados = true;
    protected boolean Over = false;
    protected Sonido Rip;
    
    //Endtile
    protected Escenario EndTile;
    public Sonido Win;
    
    protected static Escenario[][] FondoGameWin = (Escenario[][])new Escenario[8][13];
    protected Escenario YouWin;
    
    //Tile cuando el Jugador se cae al vacio
    protected Escenario BottomOfDeath;
    
    //Pantalla de GameOver
    protected Escenario GameOverFondo;
    protected Escenario GameOverLabel;    
    
    public boolean dedVacio = false;
    public boolean dedBarril = false;
    public boolean dedBarra = false;
    public static boolean dedPlayer = false;
    
    //Interfaz
    protected Escenario Panel;
    protected Scoreboard WorldTitle;
    protected Scoreboard FondoScoreBoard;
    
    //La barra que durara antes que se acabe
    protected Scoreboard TimerLevel;
    
    //Sprites de Vidas
    protected static Scoreboard[][] VidasSprite = (Scoreboard[][])new Scoreboard[2][5];
    public static Sonido Sound1Up;
    
    //Score para obtener vidas extra
    protected static Scoreboard[][] ToExtraLiveCounter = (Scoreboard[][])new Scoreboard[2][9];
   
    //Booleano para saber si el nivel se acabo
    protected boolean isLevelFinished = false;
    
    //Minutos y Segundos
    protected float Minutos;
    protected float Segundos;
    
    protected float minus;
    
    public Scene() {
    }

    public void init() {
        
    }

    public void start() {
        for (GameObject go : gameObjects){
            go.start();
            this.renderer.add(go);
        }
           
        isRunning = true;
    }

    /**
     *
     * @param go
     */
    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public void loadResources() {
        String Asset1 = "spritesheet.png";
        String Asset2 = "Fire 2.png";
        AssetPool.getShader(ShadersFolder+"default.glsl");

        AssetPool.addSpritesheet(SpritesFolder+Asset1,new Spritesheet(AssetPool.getTexture(SpritesFolder+Asset1),
        16, 16, 26, 0));
        
        AssetPool.addSpritesheet(EnemiesFolder+Asset2, new Spritesheet(AssetPool.getTexture(EnemiesFolder+Asset2),
        34, 56, 66, 0));
        
        AssetPool.addSound(SonidosFolder+"stage_clear.ogg", false);
        AssetPool.addSound(SonidosFolder+"jump-small.ogg", false);
        AssetPool.addSound(SonidosFolder+"gameover.ogg", false);
        AssetPool.addSound(SonidosFolder+"bump.ogg", false);
        AssetPool.addSound(SonidosFolder+"1-up.ogg", false);
        
        spritesB = AssetPool.getSpritesheet(SpritesFolder+Asset1);
        spritesFuego = AssetPool.getSpritesheet(EnemiesFolder+Asset2);
    }
    
    public abstract void update(float dt);
    

    public Camera camera(){
        return this.camera;
    }
   
    /**
     *
     * @param n
     */
    protected void generarHUD(int n){

        Rip = AssetPool.getSound(SonidosFolder+"gameover.ogg");
        Sound1Up = AssetPool.getSound(SonidosFolder+"1-up.ogg");
        Win = AssetPool.getSound(SonidosFolder+"stage_clear.ogg");
        
        Panel = new Escenario("Panel", new Transform(new Vector2f(970,10),new Vector2f(300, 670)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Panel"+n+".png"))),true);
        this.addGameObjectToScene(Panel);
        
        WorldTitle = new Scoreboard("Titulo", new Transform(new Vector2f(995,590),new Vector2f(270, 60)),6, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TWorld"+n+".png"))));
        this.addGameObjectToScene(WorldTitle);
        
        TimerLevel = new Scoreboard("Timer", new Transform(new Vector2f(0,0),new Vector2f(970, 20)),6, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage3.png"))));
        this.addGameObjectToScene(TimerLevel);
        
        BottomOfDeath = new Escenario("Bottom", new Transform(new Vector2f(-300,-90),new Vector2f(1500,60)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))),false);
        this.addGameObjectToScene(BottomOfDeath);
    }
    
    /**
     *
     * @param n
     */
    protected void generarFondo(int n) {
       
        float Acarreo = 0;
        
        for(int i = 0; i < 8 ; i++){
            Acarreo += 100;
            for(int j = 0; j < 13 ; j++){
                
                //Añadimos las texturas del fondo
                Fondo[i][j] = new Escenario("fondo", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 1, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Fondo"+n+".png"))),false);
                //Añade el objecto a la Escena
                this.addGameObjectToScene(Fondo[i][j]);
                
                
                //Ahora Añadimos el filtro oscuro de fondo
                FondoO[i][j] = new Escenario("fondoO", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 2, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage1.png"))),false);
                //Añade el objecto a la Escena
                this.addGameObjectToScene(FondoO[i][j]);
                
            }
        }
         
    }
    
    /**
     *
     * @param PlayerNumber
     */
    protected void generarScorePlayer(int PlayerNumber){
             
        FondoScoreBoard = new Scoreboard("Stats", new Transform(new Vector2f(990,600-(PlayerNumber*280)),new Vector2f(260, 260)),6, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"PanelPlayer.png"))));
        this.addGameObjectToScene(FondoScoreBoard); 
        
        for(int i = 0 ; i < 9 ; i++){
            
            if(i < CounterTo1Up){
                ToExtraLiveCounter[PlayerNumber-1][i] = new Scoreboard("Stats", new Transform(new Vector2f(1007+25*i,616-(PlayerNumber*280)),new Vector2f(24, 145)),6, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Shinee.png"))));
            }else{
                ToExtraLiveCounter[PlayerNumber-1][i] = new Scoreboard("Stats", new Transform(new Vector2f(1007+25*i,616-(PlayerNumber*280)),new Vector2f(24, 145)),6, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))));
            }
            this.addGameObjectToScene(ToExtraLiveCounter[PlayerNumber-1][i]);
        }
    
        for(int i = 0 ; i < Players[0].Vidas ; i++){
            
            VidasSprite[PlayerNumber-1][i] = new Scoreboard("Stats", new Transform(new Vector2f(1038+32*i,785-(PlayerNumber*280)),new Vector2f(32, 32)),6, false ,new SpriteRenderer(new Sprite(Players[0].Modelo.sprite.getTexture())));
        
            this.addGameObjectToScene(VidasSprite[PlayerNumber-1][i]);
        }
    
    }
    
    protected static int cont = 0;
    protected static int CounterTo1Up = 0;
    
    public static void AumentaScore(){
        
        cont++;
        if(cont % 5 == 0){
           
           Players[0].Score += 500;
           System.out.println(Players[0].Score);
           
           ToExtraLiveCounter[0][CounterTo1Up].Modelo.sprite.setTexture(AssetPool.getTexture(SpritesFolder+"Shinee.png"));
           Renderer.updateRender(ToExtraLiveCounter[0][CounterTo1Up].Modelo);
           
           CounterTo1Up++;
        }
        
        if(CounterTo1Up > 8){
            
            for(int i = 0 ; i < 9 ; i++){
                ToExtraLiveCounter[0][i].Modelo.sprite.setTexture(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"));
                
                Renderer.updateRender(ToExtraLiveCounter[0][i].Modelo);               
            }
            
            if(Players[0].Vidas < 5){
                
                if(Scene.checkSonidos()){    
                    Sound1Up.play();
                }  
                
                AumentaVida(Players[0].Vidas);
                
                Players[0].Vidas++;
          
            }
            
            CounterTo1Up = 0;
        }
    }
    
    /**
     *
     * @param V
     */
    public static void AumentaVida(int V){
        VidasSprite[0][V].Modelo.setSprite(new Sprite(AssetPool.getTexture(PersonajeFolder+"PD.png")));
        Renderer.updateRender(VidasSprite[0][V].Modelo);
    }
    
    public static void toggleSonidos(){
        if(S_Activados){
           S_Activados = false;
        }else{
           S_Activados = true;
        }
    }   
    
    public static boolean checkSonidos(){
        if(S_Activados){
            return true;
        }else{
            return false;
        }
    }
    
    public static void disableDed(){
        if(dedPlayer){
           dedPlayer = false;
        }
    }
    
    public void checkDed() {
        if(this.dedPlayer || this.dedVacio || this.dedBarril || this.dedBarra){       
            Ded();
        }
        if(Players[0].Vidas < 1){
            SceneFinalized(1);
        }
    }
     
    protected int Par = 0;
   
    public void Ded(){
        Par++;
        if( (Par % 2) == 0 || this.dedBarra){
            
            //Checkea si el juego esta muteado o no
            if(Scene.checkSonidos()){
                Players[0].OuchSound.play();
            }
            
            if(Players[0].Vidas == 1){
                SceneFinalized(1);
                
                //Quita la ultima Vida
                Players[0].Vidas--;
                VidasSprite[0][Players[0].Vidas].Modelo.setSprite(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png")));
                renderer.updateRender(this.VidasSprite[0][Players[0].Vidas].Modelo);
                
            }else{
                
                Players[0].ResetPosition();
                Players[0].Vidas--;
                VidasSprite[0][Players[0].Vidas].Modelo.setSprite(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png")));
                renderer.updateRender(this.VidasSprite[0][Players[0].Vidas].Modelo);
            }
            this.dedPlayer = true;
        }
    }
    
    //Esta funcion checkea si el nivel ha finalizado

    /**
     *
     * @param whatHappened
     */
    public void SceneFinalized(int whatHappened) {
        
        if(whatHappened == 1 && !Over){
                Over = true;
                this.isLevelFinished = true;
                
                if(Scene.checkSonidos()){
                    Rip.play();
                }
                
                EndingGameOver();
        }else{
            if(whatHappened == 2){
                EndingNextLevel();
                CambiaEscena();
            }else{
                
               if(whatHappened == 3 && !Over){
                   Over = true;
                   this.isLevelFinished = true;
                   
                   if(Scene.checkSonidos()){
                      Win.play();
                   }
                   
                   EpicWin();
               }
            }
            
        }
    }
    
    public void EpicWin(){
        float Acarreo = 0;
        
        for(int i = 0; i < 8 ; i++){
            Acarreo += 100;
            for(int j = 0; j < 13 ; j++){
           
                //Ahora Añadimos el filtro oscuro de fondo
                FondoGameWin[i][j] = new Escenario("fondoW", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 8, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage3.png"))),false);
                //Añade el objecto a la Escena
                this.addGameObjectToScene(FondoGameWin[i][j]);
                
            }
        }
        
        YouWin = new Escenario("You win", new Transform(new Vector2f(380, 330),new Vector2f(400, 80)), 8, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Win.png"))),false);
       
        //Añade el objecto a la Escena
        this.addGameObjectToScene(YouWin);
    }
    
    public void EndingGameOver(){
        float Acarreo = 0;
        
        for(int i = 0; i < 8 ; i++){
            Acarreo += 100;
            for(int j = 0; j < 13 ; j++){
           
                //Ahora Añadimos el filtro oscuro de fondo
                FondoGameOver[i][j] = new Escenario("fondoO", new Transform(new Vector2f(j*100, 720-Acarreo),new Vector2f(100, 100)), 8, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"blendImage1.png"))),false);
                //Añade el objecto a la Escena
                this.addGameObjectToScene(FondoGameOver[i][j]);
                
            }
        }
        
        GameOverTitle = new Escenario("Its fucking over", new Transform(new Vector2f(380, 330),new Vector2f(400, 80)), 8, false ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"GameOver.png"))),false);
        //Añade el objecto a la Escena
        this.addGameObjectToScene(GameOverTitle);
        
    }
    
    public static void EndingNextLevel(){
         System.out.println("NEW LEVEL");
    }
    
    //Ahora Cambia la Scena
    public static void CambiaEscena(){
        Window.changeScene();
    } 

    public abstract void addPlayers();
    
    
}
