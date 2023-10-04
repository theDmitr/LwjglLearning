package dmitr.tutor.engine.util;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {

    public static int loadTexture(String fileName) throws Exception {
        int width;
        int height;
        ByteBuffer buffer;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buffer = stbi_load(fileName, w, h, channels, 4);

            if (buffer == null) {
                throw new Exception("Image file " + fileName + " not loaded: " + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buffer);

        return textureID;
    }

}
