package AYPIII_Proyect_Beta.renderer;

import AYPIII_Proyect_Beta.components.Sprite;
import AYPIII_Proyect_Beta.components.SpriteRenderer;
import AYPIII_Proyect_Beta.cosas.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1200;
    private static List<RenderBatch> batches;

    //La cantidad maxima de batches se limita a 1000 objetos por scena
    public Renderer() {
        this.batches = new ArrayList<>();
    }

    /**
     *
     * @param go
     */
    public void add(GameObject go) {
        SpriteRenderer spr = go.Modelo;
        if (spr != null) {
            add(spr);
        }
    }

    /**
     *
     * @param sprite
     */
    public void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.zIndex()) {
                Texture tex = sprite.getTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }
    
    /**
     *
     * @param sprite
     */
    public static void updateRender(SpriteRenderer sprite) {
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.zIndex()) {
                Texture tex = sprite.getTexture();
                if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.UpdateSprite(sprite);
                    break;
                }
            }
        }
    }
    
    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
