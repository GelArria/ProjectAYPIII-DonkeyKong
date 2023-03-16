
package AYPIII_Proyect_Beta.game;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.cosas.Escenario;
import AYPIII_Proyect_Beta.main.Transform;
import static AYPIII_Proyect_Beta.main.Window.Players;
import AYPIII_Proyect_Beta.util.AssetPool;
import org.joml.Vector2f;


public class W4 extends Scene{

    //El escenario + Jugador
    private final Escenario[][] Piso = (Escenario[][])new Escenario[7][24];
    private final Escenario[][] Escaleras = (Escenario[][])new Escenario[13][6];
    
    //Tornillos
    private final Escenario[] Tornillos = (Escenario[])new Escenario[8];
    public int checkTornillo = 0;
    
    //Fire
        int i;
        private int numFire = 6;
        private Entidad[] Fuego = new Entidad[numFire];
       
    public W4() {
    }

    @Override
    public void init(){
       loadResources();

       this.camera.init(new Vector2f(0,0));
       
       Fuego[0] = new Entidad("Fuego", new Transform(new Vector2f(440,150),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,15,1.5f);
       this.addGameObjectToScene(Fuego[0]);
       
       Fuego[1] = new Entidad("Fuego", new Transform(new Vector2f(220,320),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,25,0.37f);
       this.addGameObjectToScene(Fuego[1]);
       
       Fuego[2] = new Entidad("Fuego", new Transform(new Vector2f(680,320),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),false,25,0.37f);
       this.addGameObjectToScene(Fuego[2]);
       
       Fuego[3] = new Entidad("Fuego", new Transform(new Vector2f(500,420),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,15,1.56f);
       this.addGameObjectToScene(Fuego[3]);
       
       Fuego[4] = new Entidad("Fuego", new Transform(new Vector2f(230,523),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),true,15,0.3f);
       this.addGameObjectToScene(Fuego[4]);
       
       Fuego[5] = new Entidad("Fuego", new Transform(new Vector2f(640,523),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(spritesFuego.getSprite(0)),false,15,0.3f);
       this.addGameObjectToScene(Fuego[5]);

       EndTile = new Escenario("EndTile", new Transform(new Vector2f(500,663),new Vector2f(33,33)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png"))),false);
       this.addGameObjectToScene(EndTile);
        
       //Se inicializa los elementos que contiene el nivel: HUD, Fondo, y todo el escenario
       this.generarHUD(4);
       this.generarFondo(4);
       this.generarEscenario();
       
       //En mundo 4 hay tornillos
       this.generarTornillos();
       
       //Inicializamos la cantidad de tiempo durara en el nivel
       this.Minutos = 0;
       this.Segundos = 45;
        
       //Despues de probar y calibrar esto, ahora esta formula representa el tiempo de manera real pero transformado a la barra
       this.minus = (float) (16.381 / (this.Segundos + (this.Minutos*60)));
       
    }
    
    @Override
    public void update(float dt) {
      
        
    if(!this.isLevelFinished){    
        
       this.dedBarra = this.TimerLevel.update(minus,this.dedPlayer);
       
       for (i=0 ; i<numFire ; i++)
                if(Players[0].updateHazard(this.Fuego[i])){
                    Ded();
                } 
        
       this.dedVacio = Players[0].update(dt,this.BottomOfDeath, renderer);
       
       checkDed(); 
        
       for(int j = 0 ; j < 21 ; j++){
          Players[0].update(dt,this.Piso[0][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[0][j], renderer);
       }
       
       for(int j = 0 ; j < 14 ; j++){
          Players[0].update(dt,this.Piso[1][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[1][j], renderer);
       }
       for(int j = 0 ; j < 12 ; j++){
          Players[0].update(dt,this.Piso[2][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[2][j], renderer);
       }
       for(int j = 0 ; j < 11 ; j++){
          Players[0].update(dt,this.Piso[3][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[3][j], renderer);
       }
       for(int j = 0 ; j < 11 ; j++){
          Players[0].update(dt,this.Piso[4][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[4][j], renderer);
       }
       for(int j = 0 ; j < 5 ; j++){
          Players[0].update(dt,this.Piso[5][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Piso[5][j], renderer);
       }
       
       //Escaleras
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[0][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[0][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[1][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[1][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[2][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[2][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[3][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[3][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[4][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[4][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[5][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[5][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[6][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[6][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[7][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[7][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[8][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[8][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[9][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[9][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[10][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[10][j], renderer);
       }
       for(int j = 0 ; j < 4 ; j++){
          Players[0].update(dt,this.Escaleras[11][j],renderer);
          for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Escaleras[11][j], renderer);
       }
      
       //Tornillos
       for(int j = 0 ; j < 8 ; j++){
          Players[0].update(dt,this.Tornillos[j],renderer);
       
       checkTornillo = 0;   
          
       for(int i = 0 ; i < 8 ; i++){
           if(!this.Tornillos[i].IsSolid1){
               checkTornillo++;
           }
       }
      
       if(checkTornillo == 8){
           SceneFinalized(3);
       }
          
       for (i=0; i<numFire; i++)
                    Fuego[i].update(dt, this.Tornillos[j], renderer);
       }   
       
       
    }
    
       this.renderer.render();
    }
    
    @Override
    public void addPlayers() {
       //Inicializamos el jugador
       Players[0].transform.ChangeSpritePosition(new Vector2f(80,40));
       this.addGameObjectToScene(Players[0]);
              
       this.generarScorePlayer(1);
    }

            
    private void generarTornillos(){
        
        Tornillos[0] = new Escenario("Tornillo", new Transform(new Vector2f(280,135),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[0]);
        Tornillos[1] = new Escenario("Tornillo", new Transform(new Vector2f(600,135),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[1]);
        
        Tornillos[2] = new Escenario("Tornillo", new Transform(new Vector2f(280,255),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[2]);
        Tornillos[3] = new Escenario("Tornillo", new Transform(new Vector2f(600,255),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[3]);
        
        Tornillos[4] = new Escenario("Tornillo", new Transform(new Vector2f(280,370),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[4]);
        Tornillos[5] = new Escenario("Tornillo", new Transform(new Vector2f(600,370),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[5]);
        
        Tornillos[6] = new Escenario("Tornillo", new Transform(new Vector2f(280,485),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[6]);
        Tornillos[7] = new Escenario("Tornillo", new Transform(new Vector2f(600,485),new Vector2f(40, 35)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaTornillo4.png"))),true);
        this.addGameObjectToScene(Tornillos[7]);
    }
    
    private void generarEscenario() {
       
      int ExtraE = 0;
      int ExtraP = 0;
      
      //Empieza a contar
      int count = 0;  
        
      Pauline = new Escenario("Pauline", new Transform(new Vector2f(452,628),new Vector2f(23, 30)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Pauline.png"))),true);
      this.addGameObjectToScene(Pauline);
      
      for(int i = 0 ; i < 21 ; i++){
        Piso[0][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+40,15),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
        this.addGameObjectToScene(Piso[0][i]);
      }
      
      for(int i = 0 ; i < 14 ; i++){
        
        if(i == 4 || i == 10 || i == 7){
           ExtraE += 40;
           ExtraP += 40;
        }  
           
        if(i == 1 || i == 8){
            
            for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),135-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+(i*40+ExtraE),135-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            
            
        } 
        

        Piso[1][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+120+ExtraP,135),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
        this.addGameObjectToScene(Piso[1][i]);
      }
      
      for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800,135-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(800,135-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++;
      
      
      ExtraE = 0;
      ExtraP = 0;
      for(int i = 0 ; i < 12 ; i++){
          
        if(i == 3 || i == 9){
           ExtraE += 40;
           ExtraP += 40;
        }  
        
        if(i == 1 || i == 6 || i == 7){
            for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+40+(i*40+ExtraE),255-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+40+(i*40+ExtraE),255-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
            if(i == 5 || i == 7 ){
                ExtraP +=40;
            }
        }  
        if(i == 5){
            Piso[2][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+200+ExtraP,255),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
            this.addGameObjectToScene(Piso[2][i]);
        }else{
        
        Piso[2][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+160+ExtraP,255),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
        this.addGameObjectToScene(Piso[2][i]);
        }
      }
      
      for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(760,255-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(760,255-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
      }
      count++;
      
    ExtraE = 0;
    ExtraP = 0;  
      
    for(int j = 0; j < 4 ; j++){
                 
        if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(160+(0*40+ExtraE),370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
            }else{
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(160+(0*40+ExtraE),370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
        }
                
        this.addGameObjectToScene(Escaleras[count][j]);
    }
    count++;
    
    for(int i = 0 ; i < 11 ; i++){
         
         if(i == 2 || i == 9){
            ExtraE += 40;
            ExtraP +=40;
         }
        
         if(i == 6){
            for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(160+(i*40+ExtraE),370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(160+(i*40+ExtraE),370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
            }
            count++;
            ExtraE += 40;
           
        }  
        
        if(i == 5){
            Piso[3][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+240+ExtraP,370),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
            this.addGameObjectToScene(Piso[3][i]);  
        }else{
            Piso[3][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+200+ExtraP,370),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
            this.addGameObjectToScene(Piso[3][i]);
        }
    }
      
    for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(720,370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(720,370-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
    }
    count++;
    
    for(int j = 0; j < 4 ; j++){
                 
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+160,485-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(40+160,485-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
    }
    count++;
    
    for(int i = 0 ; i < 11 ; i++){
        
        if(i == 1 || i == 9){
            Piso[4][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+240+40,485),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
            this.addGameObjectToScene(Piso[4][i]);
        }else{
            Piso[4][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+240,485),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
            this.addGameObjectToScene(Piso[4][i]);
        }
    }
      
    for(int j = 0; j < 4 ; j++){
              
                if(j == 0){
                Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(680,485-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"VigaEscalera4.png"))),false);
                }else{
                    Escaleras[count][j] = new Escenario("Escaleras", new Transform(new Vector2f(680,485-(j*23)),new Vector2f(40, 23)),4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Escalera4.png"))),false);
                }
                
                this.addGameObjectToScene(Escaleras[count][j]);
    }
    count++;
    
      for(int i = 0 ; i < 5 ; i++){
        Piso[5][i] = new Escenario("Bloque", new Transform(new Vector2f(i*40+360,605),new Vector2f(40, 23)), 4, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"Viga4.png"))),true);
        this.addGameObjectToScene(Piso[5][i]);
      }
      
    DK = new Escenario("DK", new Transform(new Vector2f(425,510),new Vector2f(85, 85)),5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(SpritesFolder+"DK.png"))),false);
    this.addGameObjectToScene(DK);  
      
    }

}
