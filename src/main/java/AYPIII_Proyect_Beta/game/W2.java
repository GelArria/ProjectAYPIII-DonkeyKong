package AYPIII_Proyect_Beta.game;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.cosas.Escenario;
import AYPIII_Proyect_Beta.main.KeyListener;
import AYPIII_Proyect_Beta.main.Transform;
import static AYPIII_Proyect_Beta.main.Window.Players;
import AYPIII_Proyect_Beta.util.AssetPool;
import org.joml.Vector2f;

public class W2 extends Scene {
    
    //Time Setters
    private long startTime = System.currentTimeMillis();
    private int timer;
    
    //El escenario + Jugador
    private final Escenario[][] Piso = (Escenario[][])new Escenario[7][24];
    private final Escenario[][] Escaleras = (Escenario[][])new Escenario[15][6];
    
    //Enemies
            //Barrels
            private int numBarrels = 14;
            private Entidad[] Barriles = new Entidad[numBarrels];
            private Boolean[] ActBarrels = new Boolean[numBarrels];
            private int[][] SpawnPoints = new int[numBarrels][2];
            private int k = 0;
            private int i;
            private boolean[] sideCheck = new boolean[numBarrels];
            boolean completeCicle = false;

            //Fire
            private int numFire = 4;
            private Entidad[] Fuego = new Entidad[numFire];
            
    
    public W2() {
        
    }

    @Override
    public void init(){
       loadResources();
       
       for (i=0 ;i<8; i++){

           ActBarrels[i] = false;
           if (i%2 == 0){
               SpawnPoints[i][0] = 3;
               sideCheck[i] = true; 
           }
           else{
               SpawnPoints[i][0] = 920;
               sideCheck[i] = false;
           }
           SpawnPoints[i][1] = 300;
        }
       
       for (;i<numBarrels;i++){
           ActBarrels[i] = false;
            if (i % 2 == 0){
                SpawnPoints[i][0] = 620;
                sideCheck[i] = true;
            }
            else{
                SpawnPoints[i][0] = 420;
                sideCheck[i] = false;
            }
            SpawnPoints[i][1] = 680;
       }
        
       
        timer = 25;
        Fuego[0] = new Entidad("Fuego", new Transform(new Vector2f(600,530),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,25,0.7f);
        this.addGameObjectToScene(Fuego[0]);
        
        Fuego[1] = new Entidad("Fuego", new Transform(new Vector2f(320,410),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,20,0.5f);
        this.addGameObjectToScene(Fuego[1]);
        
        Fuego[2] = new Entidad("Fuego", new Transform(new Vector2f(460,410),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,20,0.5f);
        this.addGameObjectToScene(Fuego[2]);
        
        Fuego[3] = new Entidad("Fuego", new Transform(new Vector2f(370,290),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,27,0.5f);
        this.addGameObjectToScene(Fuego[3]);
        
        
       
      

        this.camera.init(new Vector2f(0,0));
       
        EndTile = new Escenario("EndTile", new Transform(new Vector2f(560,663),new Vector2f(33,33)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))),false);
        this.addGameObjectToScene(EndTile);
        
       //Se inicializa los elementos que contiene el nivel: HUD, Fondo, y todo el escenario
        this.generarHUD(2);
        this.generarFondo(2);
        this.generarEscenario();

       //Inicializamos la cantidad de tiempo durara en el nivel
        this.Minutos = 0;
        this.Segundos = 40;
        
       //Despues de probar y calibrar esto, ahora esta formula representa el tiempo de manera real pero transformado a la barra
        this.minus = (float) (16.381 / (this.Segundos + (this.Minutos*60)));
    }
    
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
        if (secondsDisplay == timer){
            
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
            
            //Esto va a checkear si el jugador toca fuego
            for (i=0 ; i<numFire ; i++)
                if(Players[0].updateHazard(this.Fuego[i])){
                    Ded();
                }
             
            this.dedVacio = Players[0].update(dt,this.BottomOfDeath, renderer);
            
            if (this.dedVacio || this.dedPlayer){
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
       for(int j = 0 ; j < 16 ; j++){
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
       
       for(int j = 0 ; j < 20 ; j++){
          Players[0].update(dt,this.Piso[4][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[4][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[4][j],renderer);
            }
       for(int j = 0 ; j < 5 ; j++){
          Players[0].update(dt,this.Piso[5][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[5][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Piso[5][j],renderer);
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
       for(int j = 0 ; j < 4; j++){
          Players[0].update(dt,this.Escaleras[1][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[1][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[1][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[2][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[2][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[2][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[3][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[3][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[3][j],renderer);
            }
       
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[4][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[4][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[4][j],renderer);
            }
       
       for(int j = 0 ; j < 4 ; j++){
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
       
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[7][j],renderer); 
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[7][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[7][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[8][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[8][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[8][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[9][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[9][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[9][j],renderer);
            }
       
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[10][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[10][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[10][j],renderer);
            }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[11][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[11][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[11][j],renderer);
            }
       
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[12][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[12][j], renderer);
                for (i=0;i<numBarrels;i++)
                    if (ActBarrels[i])
                        Barriles[i].update(dt, this.Escaleras[12][j],renderer);
            }

       if(Players[0].update(dt,this.EndTile,renderer)){
            SceneFinalized(2);
       }
    } 
       
       this.renderer.render();
    }
    
    @Override
    public void addPlayers() {
       //Inicializamos el jugador en la escena
       
       Players[0].transform.ChangeSpritePosition(new Vector2f(80,40));
       this.addGameObjectToScene(Players[0]);
       
       this.generarScorePlayer(1);
    }

      
    private void generarEscenario() {
      
        
      Pauline = new Escenario("Pauline", new Transform(new Vector2f(415,645),new Vector2f(23, 30)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Pauline.png"))),true);
      this.addGameObjectToScene(Pauline);  
        
      for(int i = 0 ; i < 22 ; i++){
        Piso[0][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,5),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[0][i]);
      }
      
      int ExtraE = 0;
      int ExtraP = 0;
      
      //Empieza a contar
      int count = 0;
      
      for(int i = 0 ; i < 18 ; i++){
          
        if(i == 3 || i == 7 || i == 11 || i == 15){
            for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),120-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera2.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),120-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
            ExtraP += 40;
        }
                    
        Piso[1][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40+ExtraP,120),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[1][i]);
      }
      
      ExtraE = 0;
      ExtraP = 0;
      int Offset = 0;
      for(int i = 0 ; i < 16 ; i++){
         
        if(i == 5){
            Offset += 80;
        }
            
        if(i == 5 || i == 11){
            for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE+Offset),245-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera2.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE+Offset),245-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
            ExtraP += 40;
        }  
        
        if(i == 11){
            Offset += 80;
        }
          
          
        Piso[2][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40+ExtraP+Offset,245),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[2][i]);
      }
      
      ExtraE = 0;
      ExtraP = 0;
      
      for(int i = 0 ; i < 18 ; i++){
          
        if(i == 3 || i == 7 || i == 11 || i == 15){
            for(int j = 0; j < 5 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),380-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera2.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),380-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
            ExtraP += 40;
        }  
          
        Piso[3][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40+ExtraP,380),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[3][i]);
      }
      
      ExtraE = 0;
      ExtraP = 0;
      
      for(int i = 0 ; i < 20 ; i++){
          
        if(i == 1 || i == 19){
            for(int j = 0; j < 5 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),515-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera2.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),515-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
            ExtraP += 40;
        }  
          
        Piso[4][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40+ExtraP,515),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[4][i]);
      }
      
      for(int i = 0 ; i < 5 ; i++){
          
        Piso[5][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+360,625),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga2.png"))),true);
        this.addGameObjectToScene(Piso[5][i]);
      }
      
      for(int j = 0; j < 4 ; j++){
           
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(360+(5*40),625-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera2.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(360+(5*40),625-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                }
                this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++;
      
      for(int j = 0; j < 6 ; j++){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(125+(5*40),670-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                
                this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++;
      
      for(int j = 0; j < 6 ; j++){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(65+(5*40),670-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera2.png"))),false);
                
                this.addGameObjectToScene(Escaleras[count][j]);
                
                
      DK = new Escenario("DK", new Transform(new Vector2f(180,540),new Vector2f(85, 85)),5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"DK.png"))),false);
      this.addGameObjectToScene(DK);          
                
      }
    }
}
