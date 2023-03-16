package AYPIII_Proyect_Beta.util;

import AYPIII_Proyect_Beta.components.Spritesheet;
import AYPIII_Proyect_Beta.main.Sonido;
import AYPIII_Proyect_Beta.renderer.Shader;
import AYPIII_Proyect_Beta.renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sonido> sounds = new HashMap<>();
   
    /**
     *
     * @param resourceName
     * @return
     */
    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    /**
     *
     * @param resourceName
     * @return
     */
    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (AssetPool.textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    /**
     *
     * @param resourceName
     * @param spritesheet
     */
    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    /**
     *
     * @param resourceName
     * @return
     */
    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool.";
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
    
    public static Collection<Sonido> getAllSounds(){
        return sounds.values();
    }    
    
    /**
     *
     * @param soundFile
     * @return
     */
    public static Sonido getSound(String soundFile){
        File file = new File(soundFile);
        if(sounds.containsKey(file.getAbsolutePath())){
            return sounds.get(file.getAbsolutePath());
        }else {
            assert false : "Sound file no añadido" + soundFile + "";
        }
        
        return null;
    }
    
    /**
     *
     * @param soundFile
     * @param loops
     * @return
     */
    public static Sonido addSound(String soundFile, boolean loops){
        File file = new File(soundFile);
        if(sounds.containsKey(file.getAbsolutePath())){
            return sounds.get(file.getAbsolutePath());
        }else {
            Sonido sound =  new Sonido(file.getAbsolutePath(), loops);
            AssetPool.sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }
    
}
