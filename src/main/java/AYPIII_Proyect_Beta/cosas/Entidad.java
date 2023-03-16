package AYPIII_Proyect_Beta.cosas;

import AYPIII_Proyect_Beta.colisiones.Collision;
import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.game.Scene;
import AYPIII_Proyect_Beta.main.KeyListener;
import AYPIII_Proyect_Beta.main.Sonido;
import AYPIII_Proyect_Beta.main.Transform;
import AYPIII_Proyect_Beta.renderer.Renderer;
import AYPIII_Proyect_Beta.util.AssetPool;
import org.joml.Vector2f;

public class Entidad extends GameObject{
    
    private static int SpriteChanger = 13;
    
    //Caracteristicas de Movimiento
    public float MovSPEED;
    public float JumpFORCE;
    public float Peso;
    public static float gAccel;
    
    //Booleanos de control de Gravedad
    private boolean[] check = new boolean[2];
    private boolean pressedK;
    private boolean alive;
    
    //Variables para cambiar el sprite
    private float ChangingSprite;
    private int lookAtDirection;
    
    //Esta Saltando?
    public boolean isJumping;
    
    //Sonido check
    public Sonido OuchSound;
    
    public Sonido JumpingSound;
    public boolean jumpingSoundON = true;
    
    //Stats del jugador en el Juego
    public int Score;
    public int Vidas;    
    
    //Movimiento del Barril
    public float SawSpeed;
    public float SawGravity;
    public float SawAccel;
    public boolean side;
    public boolean notSolidB = false;
    public boolean sideCheck = true;
    
    //Movimiento Fuego
    public float fireSpeed;
    public boolean FireSide = true;
    
    //Cual de los jugadores es
    public int playerNumber;
    
    //Controlador de tiempo
    private long startTime;
    private boolean timeCheck = true;
    private int changeSeconds;
       
    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param isSolid
     * @param Sprite
     * @param side
     * @param alive
     * @param currentPlayer
     */
    public Entidad(String name, Transform transform, int zIndex, Boolean isSolid, SpriteRenderer Sprite, boolean side, boolean alive,int currentPlayer) {
        super(name, transform, zIndex, isSolid, Sprite);
        
        this.JumpingSound = AssetPool.getSound(SoundsFolder+"jump-small.ogg");
        this.OuchSound = AssetPool.getSound(SoundsFolder+"bump.ogg");
        
        if(this.name == "Player"){
            this.playerNumber = currentPlayer;
        }
        
        this.ChangingSprite = 0;
        this.Peso = 1.00036f;
        this.side = side;
        this.alive = alive;
    }
    
    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param isSolid
     * @param Sprite
     */
    public Entidad(String name, Transform transform, int zIndex, Boolean isSolid, SpriteRenderer Sprite) {
        super(name, transform, zIndex, isSolid, Sprite);
        
        this.JumpingSound = AssetPool.getSound(SoundsFolder+"jump-small.ogg");
        this.OuchSound = AssetPool.getSound(SoundsFolder+"bump.ogg");
        
        this.ChangingSprite = 0;
        this.Peso = 1.00036f;
    }
    
    /**
     *
     * @param name
     * @param transform
     * @param zIndex
     * @param isSolid
     * @param Sprite
     * @param side
     * @param changeSeconds
     * @param fireSpeed
     */
    public Entidad(String name, Transform transform, int zIndex, Boolean isSolid, SpriteRenderer Sprite,Boolean side, int changeSeconds,float fireSpeed) {
        super(name, transform, zIndex, isSolid, Sprite);
        
        this.JumpingSound = AssetPool.getSound(SoundsFolder+"jump-small.ogg");
        this.OuchSound = AssetPool.getSound(SoundsFolder+"bump.ogg");
        
        this.ChangingSprite = 0;
        this.Peso = 1.00036f;
        this.startTime = System.currentTimeMillis();
        this.FireSide = side;
        this.changeSeconds = changeSeconds;
        this.fireSpeed = fireSpeed;
    }
    
    //Esto es exclusivo para los barriles

    /**
     *
     * @param alive
     */
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    
    /**
     *
     * @param dt
     * @param Block
     * @param render
     * @return
     */
    public boolean update(float dt,Escenario Block, Renderer render) {
        
        //Este booleano te dice si está tocando algo que se pueda atravesar.
        
        if(this.name == "Player"){
            updatePlayer(dt,Block,render);
        }
        
        if (this.name == "Barril"){
            updateBarril(dt,notSolidB);
        }
        
        if (this.name == "Fuego"){
            updateFuego(dt);
        }
        
        //Acutaliza la posiciones ya cargadas del jugador
        this.boundingBox.Corner1 = this.posEsquina1;
        this.boundingBox.Corner2 = this.posEsquina2;
        
        if(this.name == "Fuego" || this.name == "Barril"){
            
            this.boundingBox2.Corner1.x = this.posEsquina1.x+2f;
            this.boundingBox2.Corner1.y = this.posEsquina1.y+20;
            
            this.boundingBox2.Corner2.x = this.posEsquina2.x-2f;
            this.boundingBox2.Corner2.y = this.posEsquina2.y+10;  
            
        }
        
        //Aqui checkea si cada actualizacion del personaje colisiona con cualquier cosa
        Collision Interseccion = this.boundingBox.getCollision(Block.boundingBox);
        
        if (Interseccion.isIntersecting){
            
            if (this.name == "Fuego"){
                this.boundingBox.correctPosition(Block.boundingBox,Interseccion,this);
                this.SawGravity = 0;
            }
            
            if(Block.name == "EndTile"){
                return true;
            }
            
            if(Block.name == "Bottom"){
                return true;
            }
        }
        
        if(Interseccion.isIntersecting && Block.IsSolid1 && this.name != "Fuego"){            
            this.boundingBox.correctPosition(Block.boundingBox,Interseccion,this);
            if (this.name == "Barril"){
                notSolidB = sideCheck = false; //Esto siempre es falso si está tocando el suelo.
                startTime = System.currentTimeMillis(); //El cronometro se reinicia cada que toca el suelo
                this.SawGravity = 0;
            }
            if (this.name == "Player" && !this.boundingBox.touchingroof)
                jumpingSoundON = true;
        }
        
        // Verifica si el barril está tocando una escalera
        if (Interseccion.isIntersecting && !Block.IsSolid1 && this.name == "Barril"){
            notSolidB = true;
            if (timeCheck){
                startTime = System.currentTimeMillis(); //Empieza a contar cuantos segundos ha pasado desde que toco la escalera
                timeCheck = false;
            }
            
            //==================Calculos de tiempo====================\\
            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 100; //Centisegundos
            long secondsDisplay = elapsedSeconds % 60;
            //=========================================================\\
            
            
            /* Verifica si no ha tocado el suelo luego de haber tocado una
               escalera. Es importante porque si se hace true quiere decir que
               puede que este tocando una escalera y el suelo al mismo tiempo
            */
            if (secondsDisplay > 2 && !sideCheck){ //0.2 segundos//
                sideCheck = true;
                side = !side;
            }
        }
               
        if(Block.name == "Tornillo" && this.name == "Player"){
                       
            Collision Interseccion2 = this.boundingBox.getCollision(Block.boundingBox2);
            
            if(Interseccion2.isIntersecting && Block.IsSolid2 == false){
                QuitaTornillo(Block,render);
            }
            
        }
        
        updateAnimation(dt,render);
        
        //Finalmente Updatea la posicion
        this.Modelo.update(dt);
        return false;
    }
    
    //

    /**
     *
     * @param Hazard
     * @return
     */
    public boolean updateHazard(Entidad Hazard) {
        
      //Aqui checkea si cada actualizacion del personaje colisiona con cualquier cosa
      //  Hazard.boundingBox.Corner1
        
        Collision Interseccion = this.boundingBox.getCollision(Hazard.boundingBox);
        
        if (Interseccion.isIntersecting){   
            return true;
        }
       
        Collision InterseccionT = this.boundingBox.getCollision(Hazard.boundingBox2);
        
        if (InterseccionT.isIntersecting){   
            Scene.AumentaScore();
        }
        
        return false;
    }
     
    /**
     *
     * @param dt
     * @param Block
     * @param render
     */
    public void updatePlayer(float dt,Escenario Block, Renderer render){
        Vector2f[] newposEsquina = new Vector2f[2];
        
        boolean notSolid = false;
        
        this.MovSPEED = 1.0f;
        this.JumpFORCE = 1.75f;
        
        if (this.boundingBox.getCollision(Block.boundingBox).isIntersecting && !Block.IsSolid1){
            notSolid = this.boundingBox.getCollision(Block.boundingBox).isIntersecting && !Block.IsSolid1;
            this.boundingBox.touchingroof = false;
        }
           
        //Movimiento donde actualiza y carga la posicion del jugador
        newposEsquina = Movimiento(dt,this.boundingBox.touchingroof,notSolid);
            
        if (this.boundingBox.touchinground){
            this.boundingBox.touchinground = this.boundingBox.touchingroof = false;
            pressedK = true;
            check[0] = check[1] = false;
            gAccel = 0;
            
        }
         
        posEsquina1 = newposEsquina[0];
        posEsquina2 = newposEsquina[1];
        
        //Se aplica gravedad a todo tiempo
        Gravedad(dt);
                
    } 
    
    /**
     *
     * @param dt
     * @param stairs
     */
    public void updateBarril(float dt,boolean stairs){

      Vector2f[] newposBEsquina = new Vector2f[2];
    
      newposBEsquina = BarrelMove(dt,stairs);
      
      this.posEsquina1 = newposBEsquina[0];
      this.posEsquina2 = newposBEsquina[1];
      
      if (stairs)
        this.SawGravity = 0;
      else
        GravedadB(dt);
      
    }
    
    /**
     *
     * @param dt
     */
    public void updateFuego(float dt){
        Vector2f[] newposFEsquina = new Vector2f[2];
        
        newposFEsquina = FuegoMove(dt);
        
        this.posEsquina1 = newposFEsquina[0];
        this.posEsquina2 = newposFEsquina[1];
        
        GravedadB(dt);
    }
    
    /**
     *
     * @param dt
     * @return
     */
    public Vector2f[] FuegoMove(float dt){
        Vector2f[] newposFEsquina = new Vector2f[2];
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 100; //segundos
        long secondsDisplay = elapsedSeconds % 60;
        
        if (secondsDisplay == changeSeconds){
            startTime = System.currentTimeMillis();
            FireSide = !FireSide;
        }
        
        if (FireSide){
            newposFEsquina[0] = new Vector2f(this.transform.position.x += dt * fireSpeed,this.transform.position.y);
            newposFEsquina[1] = new Vector2f(newposFEsquina[0].x+this.transform.scale.x,newposFEsquina[0].y-this.transform.scale.y);
         }
         else{
            newposFEsquina[0] = new Vector2f(this.transform.position.x -= dt * fireSpeed,this.transform.position.y);
            newposFEsquina[1] = new Vector2f(newposFEsquina[0].x+this.transform.scale.x,newposFEsquina[0].y-this.transform.scale.y);
        }
        
        return newposFEsquina;
    }
    
        
       
    
    //Movimiento del Barril

    /**
     *
     * @param dt
     * @param stairs
     * @return
     */
    public Vector2f[] BarrelMove(float dt, boolean stairs){
        
        //Factor de velocidad progresivo.
        Vector2f[] newposBEsquina = new Vector2f[2];
        if (alive){
            if (!stairs){
        
                if (side){
                newposBEsquina[0] = new Vector2f(this.transform.position.x += dt *1.5f,this.transform.position.y);
                newposBEsquina[1] = new Vector2f(newposBEsquina[0].x+this.transform.scale.x,newposBEsquina[0].y-this.transform.scale.y);
                }

                else{
                    newposBEsquina[0] = new Vector2f(this.transform.position.x -= dt * 1.5f,this.transform.position.y);
                    newposBEsquina[1] = new Vector2f(newposBEsquina[0].x+this.transform.scale.x,newposBEsquina[0].y-this.transform.scale.y);
                }
            
            }
        
            else{
                this.SawAccel = 1;
                newposBEsquina[0] = new Vector2f(this.transform.position.x,this.transform.position.y -= dt);
                newposBEsquina[1] = new Vector2f(newposBEsquina[0].x+this.transform.scale.x,newposBEsquina[0].y-this.transform.scale.y);
               
            }
        
        }
        else{
            newposBEsquina[0] = new Vector2f(this.transform.position.x += dt * 2000,this.transform.position.y);
            newposBEsquina[1] = new Vector2f(newposBEsquina[0].x+this.transform.scale.x,newposBEsquina[0].y-this.transform.scale.y);
        }
        return newposBEsquina;

    }
    
    //Gravedad del Barril

    /**
     *
     * @param dt
     */
    public void GravedadB(float dt){
        
        this.SawGravity += Math.log(this.Peso);
        
        Vector2f newposBEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * SawGravity);
        Vector2f newposBEsquina2 = new Vector2f(newposBEsquina1.x+this.transform.scale.x,newposBEsquina1.y-this.transform.scale.y);
        
        this.posEsquina1 = newposBEsquina1;
        this.posEsquina2 = newposBEsquina2;
    }

    /**
     *
     */
    public void ResetPosition(){
       this.posEsquina1 = new Vector2f(80,60);
       this.transform.position = this.posEsquina1;
    }
    
    /**
     *
     * @param dt
     * @param roof
     * @param traspasable
     * @return
     */
    public Vector2f[] Movimiento(float dt,boolean roof, boolean traspasable){  
            Vector2f[] newposEsquina = new Vector2f[2];

            //inputs de 1 sola tecla
            //DERECHA D
            if(KeyListener.isKeyPressed(68) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){
                                
                //Variables para hacer cambios de Sprite
                this.ChangingSprite += dt/SpriteChanger;
                
                //Mirando a la derecha
                this.lookAtDirection = 1;
                
                newposEsquina[0] = new Vector2f(this.transform.position.x += dt *  MovSPEED,this.transform.position.y);
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y-this.transform.scale.y);
                if (check[0]){
                    check [1] = true;
                }
                return newposEsquina;
            }
            
            //IZQUIERDA A
            if(KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(83)){        
                
                //Variables para hacer cambios de Sprite
                this.ChangingSprite += dt/SpriteChanger;
                
                //Mirando a la Izquierda
                this.lookAtDirection = 2;
                
                newposEsquina[0] = new Vector2f(this.transform.position.x -= dt *  MovSPEED,this.transform.position.y);
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y-this.transform.scale.y);
                
                if (check[0]){
                    check [1] = true;
                }
                return newposEsquina;
            }
            
            //ABAJO S
            if(KeyListener.isKeyPressed(83) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                newposEsquina[0] = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * (JumpFORCE - gAccel));
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y-this.transform.scale.y);
                return newposEsquina;
            }
            
            //ABAJO S + D
            if(KeyListener.isKeyPressed(83) && !KeyListener.isKeyPressed(65)){
                
                newposEsquina[0] = new Vector2f(this.transform.position.x += dt * MovSPEED,this.transform.position.y -= dt * (JumpFORCE - gAccel));
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y-this.transform.scale.y);
                return newposEsquina;
            }
            
            //ABAJO S + A
            if(KeyListener.isKeyPressed(83) && !KeyListener.isKeyPressed(68)){
                newposEsquina[0] = new Vector2f(this.transform.position.x -= dt * MovSPEED,this.transform.position.y -= dt * (JumpFORCE - gAccel));
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y-this.transform.scale.y);
                return newposEsquina;
            }
            //ARRIBA W
            
            if(KeyListener.isKeyPressed(87) && !KeyListener.isKeyPressed(65) && !KeyListener.isKeyPressed(68)){
                
                //Variables para hacer cambios de Sprite
                this.ChangingSprite = 0;
                this.isJumping = true;
               
                //Variables para hacer cambios de Sprite
                
                if (traspasable){
                    pressedK = true;
                    check[0] = check[1] = false;
                    gAccel = 0;
                }
                
                //
                if (pressedK && !roof){
                    newposEsquina[0] = new Vector2f(this.transform.position.x,this.transform.position.y += dt * (JumpFORCE - gAccel));
                    newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y+this.transform.scale.y);
                    if(gAccel > 0 && jumpingSoundON && Scene.checkSonidos()){
                        this.JumpingSound.play();
                        jumpingSoundON = false;
                    }
                    return newposEsquina;
                }
            }
            
            
           
            //DIAGONAL HACIA ARRIBA DERECHA D+W
            if(KeyListener.isKeyPressed(68) && KeyListener.isKeyPressed(87)){
                     
                //Variables para hacer cambios de Sprite
                this.ChangingSprite = 0;
                
                this.isJumping = true;
                
                this.ChangingSprite += dt/SpriteChanger;
                this.lookAtDirection = 1;
                //Variables para hacer cambios de Sprite
                                             
                if (traspasable){
                    pressedK = true;
                    check[0] = check[1] = false;
                    gAccel = 0;
                }
                
                
                if (!check[1] && pressedK && !roof){
                    newposEsquina[0] = new Vector2f(this.transform.position.x += dt *  MovSPEED,this.transform.position.y += dt * (JumpFORCE - gAccel));
                    if(gAccel > 0 && jumpingSoundON && Scene.checkSonidos()){
                        this.JumpingSound.play();
                        jumpingSoundON = false;
                    }
                }
                else
                    newposEsquina[0] = new Vector2f(this.transform.position.x += dt *  MovSPEED,this.transform.position.y);
                
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y+this.transform.scale.y);
                check[0] = true;
                return newposEsquina;
            }
            
            //DIAGONAL HACIA ARRIBA IZQUIERDA W+A
            if(KeyListener.isKeyPressed(65) && KeyListener.isKeyPressed(87)){
                                      
                //Variables para hacer cambios de Sprite
                this.ChangingSprite = 0;
                this.isJumping = true;
                this.ChangingSprite += dt/SpriteChanger;
                this.lookAtDirection = 2;
                //Variables para hacer cambios de Sprite
                
                if (traspasable){
                    pressedK = true;
                    check[0] = check[1] = false;
                    gAccel = 0;
                }
                
                if (!check[1] && pressedK && !roof){
                    newposEsquina[0] = new Vector2f(this.transform.position.x -= dt *  MovSPEED,this.transform.position.y += dt * (JumpFORCE-gAccel));
                    //Sonido de saltar
                    if(gAccel > 0 && jumpingSoundON && Scene.checkSonidos()){
                        this.JumpingSound.play();
                        jumpingSoundON = false;
                    }
                }
                else{
                    newposEsquina[0] = new Vector2f(this.transform.position.x -= dt *  MovSPEED,this.transform.position.y);
                }
                newposEsquina[1] = new Vector2f(newposEsquina[0].x+this.transform.scale.x,newposEsquina[0].y+this.transform.scale.y);
                check[0] = true;
                return newposEsquina;
            }
            
            //Si se llega al return de abajo, quiere decir que se ha soltado una tecla presionada
            pressedK = false;
            return newposEsquina;
    }
    
    /**
     *
     * @param dt
     */
    public void Gravedad(float dt){
                //Aqui puse el gAccel por estaba en una parte que era exclusiva para el player
                gAccel += ((Math.log(Peso)));
        
                Vector2f newposEsquina1 = new Vector2f(this.transform.position.x,this.transform.position.y -= dt * gAccel);
                Vector2f newposEsquina2 = new Vector2f(newposEsquina1.x+this.transform.scale.x,newposEsquina1.y-this.transform.scale.y);
                
                this.posEsquina1 = newposEsquina1;
                this.posEsquina2 = newposEsquina2;
            
                //Esto siginifica que si hay gravedad entonces el personaje tendra que saltar
    }
    
    /**
     *
     * @param Block
     * @param render
     */
    public void QuitaTornillo(Escenario Block,Renderer render){
       Block.IsSolid1 = false;
       Block.Modelo.setSprite(new Sprite(AssetPool.getTexture(SpritesFolder+"TriggerBox.png")));
       
       //Despues de Settearle el Sprite ahora le updatea UP TO 6 SPRITES, me da error porque son 11 o 12 sprites en total
       UpdateTextureFromSprite(render);
    }
    
    /**
     *
     * @param dt
     * @param render
     */
    public void updateAnimation(float dt,Renderer render){
        
        int r;
        r = (int) (this.ChangingSprite % 2);
        
        //Con r vamos a cambiar los sprites dependiendo el numero que resulte y que no este saltando ofc
        if(this.isJumping == true){
            ChangeSprite(render,this.lookAtDirection,5);
        }else{
            if(r != 0){
                ChangeSprite(render,this.lookAtDirection,r);
            }else{
                ChangeSprite(render,this.lookAtDirection,0);
            }
        }
        
    }
    
    /**
     *
     * @param render
     * @param Direction
     * @param CurrentState
     */
    public void ChangeSprite(Renderer render,int Direction,int CurrentState){
        String c = String.valueOf(CurrentState);
        
        if(CurrentState == 5){
            if(Direction == 1){
                this.Modelo.setTexture(AssetPool.getTexture(PersonajeFolder+"PD"+c+".png"));
            }
        }else{
            if(Direction == 1){           
                this.Modelo.setTexture(AssetPool.getTexture(PersonajeFolder+"PD.png"));
            }
                     
        }   
        //Despues de Settearle el Sprite ahora le updatea UP TO 6 SPRITES, me da error porque son 11 o 12 sprites en total
        UpdateTextureFromSprite(render);
    }
    
}
