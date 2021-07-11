package DefinitiveAYPIII_Proyect_ver1.components;

import DefinitiveAYPIII_Proyect_ver1.jade.Component;

import java.awt.font.FontRenderContext;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
