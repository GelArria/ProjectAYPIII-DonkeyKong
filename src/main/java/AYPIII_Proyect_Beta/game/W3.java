package AYPIII_Proyect_Beta.game;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.main.KeyListener;
import AYPIII_Proyect_Beta.cosas.Escenario;
import AYPIII_Proyect_Beta.main.Transform;
import static AYPIII_Proyect_Beta.main.Window.Players;
import AYPIII_Proyect_Beta.util.AssetPool;
import org.joml.Vector2f;


public class W3 extends Scene{
    
    //Time Setters
    private long startTime = System.currentTimeMillis();
    private long startTime2 = System.currentTimeMillis();
    private int timer;
       
    //El escenario + Jugador
    private final Escenario[][] Piso = (Escenario[][])new Escenario[4][24];
    private final Escenario[][] Plataforma = (Escenario[][])new Escenario[17][4];
    
    private final Escenario[][] Escaleras = (Escenario[][])new Escenario[12][13];
   
    //Ascensor
    private final Escenario[] Ascensor = (Escenario[])new Escenario[6];
    
    //Enemies
            //Barrels
            private int numBarrels = 5;
            private Entidad[] Barriles = new Entidad[numBarrels];
            private Boolean[] ActBarrels = new Boolean[numBarrels];
            private int[][] SpawnPoints = new int[numBarrels][2];
            private int k = 0;
            private int i;
            private boolean[] sideCheck = new boolean[numBarrels];
            boolean completeCicle = false;
            private boolean newStack = true;

            //Fire
            private int numFire = 1;
            private Entidad[] Fuego = new Entidad[numFire];
      
    public W3() {
    }

    @Override
    public void init(){
       loadResources();
       
       for (i=0 ;i<numBarrels; i++){
           ActBarrels[i] = false;
           SpawnPoints[i][0] = 3;
           sideCheck[i] = true;
           SpawnPoints[i][1] = 540; 
       }
       
       Fuego[0] = new Entidad("Fuego", new Transform(new Vector2f(500,523),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,25,0.3f);
       this.addGameObjectToScene(Fuego[0]);

       this.camera.init(new Vector2f(0,0));
       
       EndTile = new Escenario("EndTile", new Transform(new Vector2f(470,663),new Vector2f(33,33)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))),false);
       this.addGameObjectToScene(EndTile);
        
       //Se inicializa los elementos que contiene el nivel: HUD, Fondo, y todo el escenario
       this.generarHUD(3);
       this.generarFondo(3);
       this.generarEscenario();

       //Inicializamos la cantidad de tiempo durara en el nivel
       this.Minutos = 0;
       this.Segundos = 55;
        
       //Despues de probar y calibrar esto, ahora esta formula representa el tiempo de manera real pero transformado a la barra
       this.minus = (float) (16.381 / (this.Segundos + (this.Minutos*60)));
    }
    
    @Override
    public void update(float dt) {
      
    if(!this.isLevelFinished){
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 100; //Centisegundos
        long secondsDisplay = elapsedSeconds % 60;
        
        long elapsedTime2 = System.currentTimeMillis() - startTime2;
        long elapsedSeconds2 = elapsedTime / 100; //Centisegundos
        long secondsDisplay2 = elapsedSeconds % 60;
        
        //TOGGLE SOUNDS
        if(KeyListener.isKeyPressed(77)){
            Scene.toggleSonidos();
        }
        
        if (secondsDisplay == timer && k<numBarrels){
                Barriles[k] = new Entidad("Barril", new Transform(new Vector2f(SpawnPoints[k][0],SpawnPoints[k][1]),new Vector2f(20, 20)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Barril.png") )),sideCheck[k],true,0);
                this.addGameObjectToScene(Barriles[k]);
                ActBarrels[k] = true;
                startTime = System.currentTimeMillis(); //Esto hace que se reinicie el contador de segundos
                k++;
                if (k == numBarrels){
                    completeCicle = true;
                }
                if (k == 0)
                    timer = 25;
                 else
                    timer = 20;
        } 
            
            this.dedBarra = this.TimerLevel.update(minus,this.dedPlayer);
                    
            //Esto va a checkear si el jugador choca con un barril
            for (i=0 ; i<numBarrels ; i++)
                if (ActBarrels[i])
                    if(Players[0].updateHazard(this.Barriles[i])){
                        Ded();
                    }
            //Esto va a checkear si el jugador toca fuego
            
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

      
        
       this.dedPlayer = this.TimerLevel.update(minus,this.dedPlayer);
        
       this.dedVacio = Players[0].update(dt,this.BottomOfDeath, renderer);
       
       checkDed(); 
        
       updateAscensor(dt);
       
       
       for(int j = 0 ; j < 22 ; j++){
          Players[0].update(dt,this.Piso[0][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Piso[0][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Piso[0][j],renderer);
            }

       for(int j = 0 ; j < 15 ; j++){
          Players[0].update(dt,this.Piso[1][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Piso[1][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Piso[1][j],renderer);
            }

       for(int j = 0 ; j < 6 ; j++){
          Players[0].update(dt,this.Piso[2][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Piso[2][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Piso[2][j],renderer);
            }

       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Piso[3][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Piso[3][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Piso[3][j],renderer);
        }
       
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Plataforma[0][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[0][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[0][j],renderer);
            }
       for(int j = 0 ; j < 3 ; j++){
          Players[0].update(dt,this.Plataforma[1][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[1][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[1][j],renderer);
            }

       for(int j = 0 ; j < 3 ; j++){
          Players[0].update(dt,this.Plataforma[2][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[2][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[2][j],renderer);
            } 
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[3][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[3][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[3][j],renderer);
            }

       for(int j = 0 ; j < 3 ; j++){
          Players[0].update(dt,this.Plataforma[4][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[4][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[4][j],renderer);
            }
       for(int j = 0 ; j < 3 ; j++){
          Players[0].update(dt,this.Plataforma[5][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[5][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[5][j],renderer);
            }
       
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[6][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[6][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[6][j],renderer);
            }
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[7][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[7][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[7][j],renderer);
            }
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[8][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[8][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[8][j],renderer);
       } 
          Players[0].update(dt,this.Plataforma[9][0],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[9][0], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[9][0],renderer);
          
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[10][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[10][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[10][j],renderer);
            }
       
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[11][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[11][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[11][j],renderer);
       }
          Players[0].update(dt,this.Plataforma[12][0],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[12][0], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[12][0],renderer);
          
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[13][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[13][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[13][j],renderer);
            }
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Plataforma[14][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Plataforma[14][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Plataforma[14][j],renderer);
            }

       //Escaleras
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[0][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[0][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[0][j],renderer);
            }
       
       for(int j = 0 ; j < 5; j++){
          Players[0].update(dt,this.Escaleras[1][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[1][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[1][j],renderer);
            }
       for(int j = 0 ; j < 11 ; j++){
          Players[0].update(dt,this.Escaleras[2][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[2][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[2][j],renderer);
            }
       for(int j = 0 ; j < 3 ; j++){
          Players[0].update(dt,this.Escaleras[3][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[4][j],renderer);
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[4][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[4][j],renderer);
            }
       for(int j = 0 ; j < 2 ; j++){
          Players[0].update(dt,this.Escaleras[5][j],renderer); 
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[5][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[5][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[6][j],renderer); 
          for (i=0; i<numFire; i++)
                Fuego[i].update(dt, this.Escaleras[6][j], renderer);
            for (i=0;i<numBarrels;i++)
                if (ActBarrels[i])
                    Barriles[i].update(dt, this.Escaleras[6][j],renderer);
            }
  
       //Ascensor
       for(int j = 0 ; j < 6 ; j++){
          Players[0].update(dt,this.Ascensor[j],renderer);
       } 
       
       if(Players[0].update(dt,this.EndTile,renderer)){
                SceneFinalized(2);
       }
       
    }
       
       this.renderer.render();
    }
    
    private void updateAscensor(float dt){
       for(int j = 0 ; j < 6 ; j++)
          this.Ascensor[j].update(dt,renderer);
    }
    
    @Override
    public void addPlayers() {
       //Inicializamos el jugador
       Players[0].transform.ChangeSpritePosition(new Vector2f(80,70));
       this.addGameObjectToScene(Players[0]);
       
       this.generarScorePlayer(1);
    }

    
    private void generarEscenario() {
       
      Pauline = new Escenario("Pauline", new Transform(new Vector2f(340,642),new Vector2f(23, 30)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Pauline.png"))),true);
      this.addGameObjectToScene(Pauline);   
        
      int ExtraP = 0;
      
      //Empieza a contar
      int count = 0;
        
      for(int i = 0 ; i < 22 ; i++){
        Piso[0][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,15),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Piso[0][i]);
      }
      
      //Plataformas del principio
      for(int i = 0 ; i < 4 ; i++){
        Plataforma[0][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,55),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[0][i]);
      }
      
      for(int i = 0 ; i < 3 ; i++){
          
        Plataforma[1][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,195),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[1][i]);
      }
      //Escalera
      for(int j = 0; j < 5 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+40,195-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(120+40,195-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
       }
       count++;
      
      //Escalera
      for(int j = 0; j < 6 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40,355-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40,355-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
       }
       count++;
       
       
      for(int i = 0 ; i < 3 ; i++){
        Plataforma[2][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+80,355),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[2][i]);
      }
      
      //Plataforma DK
      for(int i = 0 ; i < 15 ; i++){
        Piso[1][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,500),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Piso[1][i]);
      }
      
      //Plataformas del medio
      for(int i = 0 ; i < 2 ; i++){
          
        if(i == 1){
            for(int j = 0; j < 12 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(340,355-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(340,355-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            ExtraP += 40;
      
        }
          
        Plataforma[3][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+300+ExtraP,355),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[3][i]);
      }
      count++; 
      for(int i = 0 ; i < 3 ; i++){
        Plataforma[4][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+300,65),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[4][i]);
      }
      
      //Plataformas de la Derecha / Primera escalera
      
      for(int i = 0 ; i < 3 ; i++){
        Plataforma[5][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+515,70),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[5][i]);
      }
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[6][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+665,70),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[6][i]);
      }
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[7][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+775,90),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[7][i]);
      }
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[8][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+885,110),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[8][i]);
      }
      
      //Plataforma de la Derecha / Segunda Escalera
      
      
      Plataforma[9][0] = new Escenario("Bloque", new Transform(new Vector2f(885,210),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Plataforma[9][0]);
      
      for(int j = 0; j < 3 ; j++){
                 
        if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(885+40,210-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
           }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(885+40,210-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
           }
                
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++;
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[10][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+775,230),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[10][i]);
      }
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[11][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+665,250),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[11][i]);
      }
      
      //Plataforma de la Derecha / Tercera Escalera
      
      for(int j = 0; j < 4 ; j++){
                 
        if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(665,370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
           }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(665,370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
           }
                
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++; 
      
      for(int i = 0 ; i < 6 ; i++){
        Piso[2][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+705,370),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Piso[2][i]);
      }
      
      
      
      Plataforma[12][0] = new Escenario("Bloque", new Transform(new Vector2f(865,440),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Plataforma[12][0]);
      
      for(int j = 0; j < 2 ; j++){
                 
        if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(865+40,440-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
           }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(865+40,440-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
           }
                
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++; 
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[13][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+765,460),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[13][i]);
      }
      
      for(int i = 0 ; i < 2 ; i++){
        Plataforma[14][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+665,480),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Plataforma[14][i]);
      }
      
      //Plataforma Pauline
      
      for(int i = 0 ; i < 4 ; i++){
        Piso[3][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+305,620),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
        this.addGameObjectToScene(Piso[3][i]);
      }
      
      for(int j = 0; j < 4 ; j++){
                 
        if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(425+40,620-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera3.png"))),false);
           }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(425+40,620-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);
           }
                
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++; 
      
      //Escaleras DK
      for(int j = 0; j < 7 ; j++){
     
        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(210,680-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);     
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++; 
      for(int j = 0; j < 7 ; j++){
                 
        Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(270,680-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera3.png"))),false);   
        this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++; 
      
      //Plataformas del Ascensor
      Ascensor[0]= new Escenario("AscensorA", new Transform(new Vector2f(235,430),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[0]);
      Ascensor[1]= new Escenario("AscensorA", new Transform(new Vector2f(235,260),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[1]);
      Ascensor[2]= new Escenario("AscensorA", new Transform(new Vector2f(235,110),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[2]);
      
      //Hacia Abajo
      Ascensor[3]= new Escenario("AscensorD", new Transform(new Vector2f(450,430),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[3]);
      Ascensor[4]= new Escenario("AscensorD", new Transform(new Vector2f(450,260),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[4]);
      Ascensor[5]= new Escenario("AscensorD", new Transform(new Vector2f(450,110),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga3.png"))),true);
      this.addGameObjectToScene(Ascensor[5]);
      
      
      DK = new Escenario("DK", new Transform(new Vector2f(130,525),new Vector2f(85, 85)),5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"DK.png"))),false);
      this.addGameObjectToScene(DK);    
    }

}
