package DefinitiveAYPIII_Proyect_ver1.components;

import org.joml.Vector2f;
import DefinitiveAYPIII_Proyect_ver1.renderer.Texture;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCoords;
    private String SpritesFolder = "C:\\Users\\Miguel\\Documents\\NetBeansProjects\\TutorialGabe\\src\\main\\java\\assets\\images\\"; 
    
    public Sprite(Texture texture) {
        this.texture = texture;
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0), 
                new Vector2f(0, 1)
        };
        this.texCoords = texCoords;
    }

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public void setTextura(Texture texture) {
        this.texture = texture;
    }
    
    //getters
    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }
    
    public String getFolderUbication() {
        return this.SpritesFolder;
    }
}
