package dmitr.tutor.engine.graphics.model;

import dmitr.tutor.engine.util.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception {
        this(TextureLoader.loadTexture(fileName));
    }

    public Texture(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

}
