package AYPIII_Proyect_Beta.main;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.Entidad;
import AYPIII_Proyect_Beta.game.W1;
import AYPIII_Proyect_Beta.game.W2;
import AYPIII_Proyect_Beta.game.Scene;
import AYPIII_Proyect_Beta.game.W3;
import AYPIII_Proyect_Beta.game.W4;
import AYPIII_Proyect_Beta.util.AssetPool;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.ALC10.*;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    
    //Dimensiones de la Ventana
    private int width, height;
    
    //Titulo
    private String title;
    
    //Direccion de memoria que se usara para crear la ventana. La version LWJGL de C++ se usa el Ampersand de esta variable solamente
    private long glfwWindow;
    
    public float r, g, b, a;
    
    //Carpetas de jugadores
    private static String PersonajeFolder = System.getProperty("user.dir") + "\\src\\main\\java\\AYPIII_Proyect_Beta\\assets\\images\\Personaje\\";
  
    
    //Variable para crear la ventana donde estara todo.
    private static Window window = null;

    //Variable de las mas Importantes donde se guardara la Escena que se esta corriendo actualmente
    private static Scene currentScene;
    
    //Variable que cuenta las Escenas y a cual le corresponde
    private static int SceneNumber;

    private long AudioDevice;
    private long AudioContext;
    
    //Jugadores
    public static Entidad[] Players  = (Entidad[])new Entidad[4];
    
    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "Donkey Kong";
        SceneNumber = 0;
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    //Esta es la cambia Escena, Cada Vez que se activa. Cambia de Escena aumentando el numero correspondiente de Escena
    public static void changeScene() {
        SceneNumber++;
        switch (SceneNumber) {
            case 1:
                currentScene = new W1();
                currentScene.init();
                currentScene.start();
                initPlayerSystem();
                currentScene.addPlayers();
                //currentScene.startPinging();
                break;
            case 2:
                currentScene = new W2();
                currentScene.init();
                currentScene.start();
                currentScene.addPlayers();
                //currentScene.startPinging();
                break;
            case 3:
                currentScene = new W3();
                currentScene.init();
                currentScene.start();
                currentScene.addPlayers();
                //currentScene.startPinging();
                break;
            case 4:
                currentScene = new W4();
                currentScene.init();
                currentScene.start();
                currentScene.addPlayers();
                //currentScene.startPinging();
                break;
            default:
                //Si no encuentra que numero de escena, entonces hace Assert false
                assert false : "Escena Desconocida '" + SceneNumber + "'";
                break;
        }
    }

    //Getter que recolecta toda la ventana para ser usado en clases mas metidas adentro
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    //Getter de recolectar escena
    public static Scene getScene() {
        return get().currentScene;
    }
    
    //Empieza el programa ya despues de haber pasado por el constructor

    /**
     *
     */
    public void run() {
       
        //Inicializa la Ventana y sus parametros
        init();
        
        //Inicializa el loop del Juego. Aqui se queda hasta que termine el juego
        loop();

        // Destroy the audio Context
        alcDestroyContext(AudioContext);
        alcCloseDevice(AudioDevice);
        
        // Free the memory despues de terminar el juego o simplemente terminar el procedimiento de loop
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    //Inicializa Ventana y parametrizacion de inputs
    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // initialize the audio device
        String defaultDeviceName = alcGetString(0,ALC_DEFAULT_DEVICE_SPECIFIER);
        AudioDevice = alcOpenDevice(defaultDeviceName);
        
        int[] atributes = {0};
        AudioContext = alcCreateContext(AudioDevice, atributes);
        alcMakeContextCurrent(AudioContext);
        
        
        ALCCapabilities alcCapabilities = ALC.createCapabilities(AudioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        
        if(!alCapabilities.OpenAL10){
            assert false : "Audio library not supported";
        }
        
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        
        //Cambia de Escena a para empezar el juego
        Window.changeScene();
    }

    //Init el sistema de Jugadores y relacion con informacion mandada con el websocket
    public static void initPlayerSystem(){
        Players[0] = new Entidad("Player", new Transform(new Vector2f(80,40),new Vector2f(20, 20)), 5, true ,new SpriteRenderer(new Sprite(AssetPool.getTexture(PersonajeFolder+"PD.png"))),true,true,1);          
        Players[0].Vidas = 5;
    }
    
    //Procedimiento que crea e inicializa el Delta Time y empezar el loopeo del juego.
    public void loop() {
        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;
        
        //Aqui empieza el Loop del Juego
        while (!glfwWindowShouldClose(glfwWindow)) {
            
            // Poll events del Juego obligatorio
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            //ACTUALIZACION DE ESCENA
            currentScene.update(dt);
            
            glfwSwapBuffers(glfwWindow);
 
            //FPS
            //System.out.println("FPS: "+ 1.0f / dt);
            
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
