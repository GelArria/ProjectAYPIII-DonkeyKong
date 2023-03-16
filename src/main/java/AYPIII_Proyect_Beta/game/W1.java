package AYPIII_Proyect_Beta.game;


import AYPIII_Proyect_Beta.main.Transform;
import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.Escenario;
import AYPIII_Proyect_Beta.main.KeyListener;
import static AYPIII_Proyect_Beta.main.Window.Players;
import org.joml.Vector2f;
import AYPIII_Proyect_Beta.util.AssetPool;

public class W1 extends Scene {
    
    ////Timer setter
    private long startTime = System.currentTimeMillis();
    private int timer;
    
    //El escenario + Escaleras
    private final Escenario[][] Piso = (Escenario[][])new Escenario[7][24];
    private final Escenario[][] Escaleras = (Escenario[][])new Escenario[12][6];
    
    //Enemies
            //Barrels
            private int numBarrels = 18;
            private Entidad[] Barriles = new Entidad[numBarrels];
            private Boolean[] ActBarrels = new Boolean[numBarrels];
            private int[][] SpawnPoints = new int[numBarrels][2];
            private int k = 0;
            private int i;
            private boolean[] sideCheck = new boolean[numBarrels];
            boolean completeCicle = false;
            
            //Fire
            private Entidad[] Fuego = new Entidad[3];
            private int numFire = 1;
    
    public W1(){

    }

    @Override
    public void init() {
        loadResources();
        
        for (i=0;i<numBarrels;i++){
            ActBarrels[i] = false;
            if (i == 0 || i == 2 || i == 3 || i == 7 || i == 8 || i == 13 || i == 15 || i == 16){
                SpawnPoints[i][0] = 920;
                sideCheck[i] = false;
            }
            else{
                if (i == 4 || i == 9 || i == 17)
                    SpawnPoints[i][0] = 80;
                else
                    SpawnPoints[i][0] = 3;
                sideCheck[i] = true;
            }
        }

        this.camera.init(new Vector2f(0,0));
        
        this.CurrentPlayer = Players[0];
        
        Fuego[0] = new Entidad("Fuego", new Transform(new Vector2f(600,530),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,25,0.3f);
        this.addGameObjectToScene(Fuego[0]);
        
        
        timer = 25; //2.5 segundos
        // [k][0] = x
        // [k][1] = y 
        SpawnPoints[0][1] = 120;
        SpawnPoints[1][1] = 500;
        SpawnPoints[2][1] = 120;
        SpawnPoints[3][1] = 320; 
        SpawnPoints[4][1] = 480;
        SpawnPoints[5][1] = 200;
        SpawnPoints[6][1] = 600; //
        SpawnPoints[7][1] = 500;
        SpawnPoints[8][1] = 320;
        SpawnPoints[9][1] = 480;
        SpawnPoints[10][1] = 600;
        SpawnPoints[11][1] = 600;
        SpawnPoints[12][1] = 600;
        SpawnPoints[13][1] = 320;
        SpawnPoints[14][1] = 600;
        SpawnPoints[15][1] = 140;
        SpawnPoints[16][1] = 140;
        SpawnPoints[17][1] = 480;
        
        EndTile = new Escenario("EndTile", new Transform(new Vector2f(500,663),new Vector2f(33,33)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))),false);
        this.addGameObjectToScene(EndTile);
        
        //Se inicializa los elementos que contiene el nivel: HUD, Fondo, y todo el escenario
        this.generarHUD(1);
        this.generarFondo(1);
        this.generarEscenario();

        //Inicializamos la cantidad de tiempo durara en el nivel
        this.Minutos = 0;
        this.Segundos = 30;
        
        //Despues de probar y calibrar esto, ahora esta formula representa el tiempo de manera real pero transformado a la barra
        this.minus = (float) (16.381 / (this.Segundos + (this.Minutos*60)));
        
    }

    /**
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
               
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 100; //Centisegundos
        long secondsDisplay = elapsedSeconds % 60;
        
        //TOGGLE SOUNDS
        if(KeyListener.isKeyPressed(77)){
            Scene.toggleSonidos();
        }
       
        if(!this.isLevelFinished){
            /* El primer barril aparece luego de 4 segundos de haber iniciado
              luego aparecen barriles cada 3 segundos.
            */
            
            if (secondsDisplay == timer){
                //Prueba de barril
            
                Barriles[k] = new Entidad("Barril", new Transform(new Vector2f(SpawnPoints[k][0],SpawnPoints[k][1]),new Vector2f(20, 20)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Barril.png") )),sideCheck[k],true,0);
                this.addGameObjectToScene(Barriles[k]);
                ActBarrels[k] = true;
                startTime = System.currentTimeMillis(); //Esto hace que se reinicie el contador de segundos
                k++;
                if (k == numBarrels){
                    k = 0;
                    completeCicle = true;
                }
                if (k == 0)
                    timer = 25;
                else
                    timer = 7;  
            }
            
            this.dedBarra = this.TimerLevel.update(minus,this.dedPlayer);
                    
            //Esto va a checkear si el jugador choca con un barril
            for (i=0 ; i<numBarrels ; i++)
                if (ActBarrels[i])
                    if(Players[0].updateHazard(this.Barriles[i])){
                        Ded();
                    }
            
            for (i=0 ; i<numFire ; i++)
                if(Players[0].updateHazard(this.Fuego[i])){
                    Ded();
                }
             
            this.dedVacio = Players[0].update(dt,this.BottomOfDeath, renderer);
            
            if (this.dedVacio || this.dedPlayer || this.dedBarra){
                if (completeCicle)
                    k = numBarrels;
                for (int j = 0; j<k; j++)
                    Barriles[j].setAlive(false);
                startTime = System.currentTimeMillis();
                k = 0;
                
            }
            
            checkDed();
            
            //Colision con el piso
            for(int j = 0 ; j < 22 ; j++){
                Players[0].update(dt,this.Piso[0][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[0][j], renderer);
                
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[0][j],renderer);
            }
            
            for(int j = 0 ; j < 18 ; j++){
                Players[0].update(dt,this.Piso[1][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[1][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[1][j],renderer);
            }
            
            for(int j = 0 ; j < 18 ; j++){
                Players[0].update(dt,this.Piso[2][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[2][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[2][j],renderer);
            }
            
            for(int j = 0 ; j < 18 ; j++){
                Players[0].update(dt,this.Piso[3][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[3][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[3][j],renderer);
            }
            
            for(int j = 0 ; j < 18 ; j++){
                Players[0].update(dt,this.Piso[4][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[4][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[4][j],renderer);
            }
      
            for(int j = 0 ; j < 18 ; j++){
                Players[0].update(dt,this.Piso[5][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[5][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[5][j],renderer);
            }
                
            for(int j = 0 ; j < 6 ; j++){
                Players[0].update(dt,this.Piso[6][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[6][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[6][j],renderer);
            }
            
            
            //Escaleras
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[0][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[0][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[0][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[1][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[1][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[1][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[2][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[2][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[2][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[3][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[3][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[3][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[4][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[4][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[4][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[5][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[5][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[5][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[6][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[6][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[6][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[7][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[7][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[7][j],renderer);
            }
            
            for(int j = 0 ; j < 3 ; j++){
                Players[0].update(dt,this.Escaleras[8][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[8][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[8][j],renderer);
            }
            
            //Escalera hacia Pauline
            for(int j = 0 ; j < 4 ; j++){
                Players[0].update(dt,this.Escaleras[9][j],renderer);
                for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[9][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[9][j],renderer);
            }
        
            //El jugador cuando toque el endtile, se acaba el nivel
            if(Players[0].update(dt,this.EndTile,renderer)){
                SceneFinalized(2);
            }
        }
       
        this.renderer.render();
    }
    
    @Override
    public void addPlayers() {
        //Inicializamos el jugador
        Players[0].transform.ChangeSpritePosition(new Vector2f(80,40));
        this.addGameObjectToScene(Players[0]);
        
        Players[0].Vidas = 5;
        
        this.generarScorePlayer(1);
    }
    
    private void generarEscenario() {
        
        DK = new Escenario("DK", new Transform(new Vector2f(60,540),new Vector2f(85, 85)),5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"DK.png"))),false);
        this.addGameObjectToScene(DK);
        
        Pauline = new Escenario("Pauline", new Transform(new Vector2f(290,650),new Vector2f(25, 30)),5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Pauline.png"))),false);
        this.addGameObjectToScene(Pauline);
        
        //Primer piso  
        float escalon = 0;
        for(int i = 0; i < 22 ; i++){
            escalon += 2;
            
            Piso[0][i] = new Escenario("Bloque0", new Transform(new Vector2f(i*40+40,0+escalon),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[0][i]);
        }
        
        //Segundo piso
        int ExtraE = 0;
        int ExtraP = 0;
        int count = 0;
        escalon = 0;
        for(int i = 0; i < 18 ; i++){
            escalon += 2;
           
            
            if(i == 4 || i == 13){
       
             for(int j = 0; j < 3 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800-(i*40+ExtraE),105+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800-(i*40+ExtraE),105+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
             }
             count++;
             ExtraE = 40;
             ExtraP += 40;
            }
            Piso[1][i] = new Escenario("Bloque1", new Transform(new Vector2f(800-(i*40+ExtraP),105+escalon),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[1][i]);
        }
        
        //Tercer piso
        
        ExtraE = 0;
        ExtraP = 0;
    
        escalon = 0;
        for(int i = 0; i < 18 ; i++){
            escalon += 2;
            
            if(i == 1 || i == 9){
                for(int j = 0; j < 3 ; j++){
                    if(j == 0){
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+(i*40+ExtraE),205+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                    }else{
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+(i*40+ExtraE),205+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                    }
                    
                    this.addGameObjectToScene(Escaleras[count][j]);
                    
                    
                }
                count++;
                ExtraE = 40;
                ExtraP += 40;
            }
            
            
            Piso[2][i] = new Escenario("Bloque2"+i, new Transform(new Vector2f(120+(i*40+ExtraP),205+escalon),new Vector2f(40, 23)),3, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[2][i]);
        }
        
        //Cuarto piso
         ExtraE = 0;
         ExtraP = 0;
        
        escalon = 0;
        for(int i = 0; i < 18 ; i++){
            escalon += 2;
            
            if(i == 2 || i == 14){
                for(int j = 0; j < 3 ; j++){
                    if(j == 0){
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800-(i*40+ExtraE),305+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                    }else{
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800-(i*40+ExtraE),305+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                    }
                    
                    this.addGameObjectToScene(Escaleras[count][j]);
                    
                    
                }
                count++;
                ExtraE = 40;
                ExtraP += 40;
            }
            
            Piso[3][i] = new Escenario("Bloque3"+i, new Transform(new Vector2f(800-(i*40+ExtraP),305+escalon),new Vector2f(40, 23)),3, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[3][i]);
        }
        
        ExtraE = 0;
        ExtraP = 0;
        
        //Quinto piso
        escalon = 0;
        for(int i = 0; i < 18 ; i++){
            escalon += 2;
            
            if(i == 3 || i == 11){
                for(int j = 0; j < 3 ; j++){
                    if(j == 0){
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+(i*40+ExtraE),405+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                    }else{
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+(i*40+ExtraE),405+escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                    }
                    
                    this.addGameObjectToScene(Escaleras[count][j]);
                    
                    
                }
                count++;
                ExtraE = 40;
                ExtraP += 40;
            }
            
            
            Piso[4][i] = new Escenario("Bloque3"+i, new Transform(new Vector2f(120+(i*40+ExtraP),405+escalon),new Vector2f(40, 23)),3, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[4][i]);
        }
        
        //Plataforma Dk
        escalon = 0;
        for(int i = 0; i < 18 ; i++){
            if(i>15){
                escalon += 2;
            }
            
            if(i == 17){
                for(int j = 0; j < 3 ; j++){
                
                    if(j == 0){
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40),515-escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                    }else{
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40),515-escalon-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                    }
                    
                    this.addGameObjectToScene(Escaleras[count][j]);
                    
                    
                }
                count++;
    
                ExtraP += 40;
            }
            
            
            Piso[5][i] = new Escenario("Bloque3"+i, new Transform(new Vector2f(-40+(i*40+ExtraP),515-escalon),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[5][i]);
        }
        
        //Plataforma Pauline
        escalon = 0;
        for(int i = 0; i < 6; i++){
                   
            Piso[6][i] = new Escenario("Bloque4"+i, new Transform(new Vector2f(250+(i*40),625),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga1.png"))),true);
            this.addGameObjectToScene(Piso[6][i]);
        }
        
        for(int j = 0; j < 4 ; j++){
                
                    if(j == 0){
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(250+(6*40),625-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera1.png"))),false);
                    }else{
                        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(250+(6*40),625-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
                    }
                    this.addGameObjectToScene(Escaleras[count][j]);
       
                    
        }
        count++;
        
        //Escaleras Dk
        for(int j = 0; j < 6 ; j++){
               
            Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(215,670-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
            this.addGameObjectToScene(Escaleras[count][j]);
        }
            
        count++;
        
        for(int j = 0; j < 6 ; j++){
               
            Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(155,670-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera1.png"))),false);
            this.addGameObjectToScene(Escaleras[count][j]);
        }
        
        /*
        Dk rap lyrics
           
        Here, here, here we go!
        
        So they're finally here, performing for you,
        If you know the words, you can join in too,
        Put your hands together, if you want to clap,
        As we take you through, this monkey rap!

        Huh!
        D.K., Donkey Kong!!

        He's the leader of the bunch, you know him well,
        He's finally back to kick some tail,
        His Coconut Gun can fire in spurts,
        If he shoots ya, it's gonna hurt,
        He's bigger, faster, and stronger too,
        He's the first member of the D.K. crew!

        Huh!
        D.K., Donkey Kong!
        D.K., Donkey Kong is here!

        This Kong's got style, so listen up dudes,
        She can shrink in size, to suit her mood,
        She's quick and nimble when she needs to be,
        She can float through the air and climb up trees!
        If you choose her, you'll not choose wrong,
        With a skip and a hop, she's one cool Kong!

        Huh!
        D.K., Donkey Kong!

        He has no style, he has no grace,
        Th-th-th-this Kong has a funny face,
        He can handstand when he needs to,
        And stretch his arms out, just for you,
        Inflate himself just like a balloon,
        This crazy Kong just digs this tune!

        Huh!
        D.K., Donkey Kong!
        D.K., Donkey Kong is here!

        He's back again and about time too,
        And this time he's in the mood,
        He can fly real high with his jetpack on,
        With his pistols out, he's one tough Kong!
        He'll make you smile when he plays his tune,
        But Kremlings beware 'cause he's after you!

        Huh!
        D.K., Donkey Kong!
        Huh!

        Finally, he's here for you,
        It's the last member of the D.K. crew!
        This Kong's so strong, it isn't funny,
        Can make a Kremling cry out for mummy,
        Can pick up a boulder with relative ease,
        Makes crushing rocks seem such a breeze,
        He may move slow, he can't jump high,
        But this Kong's one hell* of a guy! (*Replaced with "heck" in later versions of the song.)

        Huh!
        C'mon Cranky, take it to the fridge!

        W-w-w-walnuts. peanuts. pineapple smells.
        Grapes. melons. oranges and coconut shells!
        Ahh yeah!
        Walnuts, peanuts, pineapple smells,
        Grapes, melons, oranges and coconut shells!
        Ahh yeah!
        */
  
        
        
      }
      
}
