package dmitr.tutor.engine.graphics.model;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoID;

    private final List<Integer> vboIdList;

    private final int vertexCount;

    private Material material;

    public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        FloatBuffer posBuffer = null;
        FloatBuffer textureCoordsBuffer = null;
        IntBuffer indicesBuffer = null;
        FloatBuffer vectorNormalsBuffer = null;

        try {
            vertexCount = indices.length;
            vboIdList = new ArrayList<>();

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            int vboID = glGenBuffers();
            vboIdList.add(vboID);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            vboID = glGenBuffers();
            vboIdList.add(vboID);
            textureCoordsBuffer = MemoryUtil.memAllocFloat(textureCoords.length);
            textureCoordsBuffer.put(textureCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, textureCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            vboID = glGenBuffers();
            vboIdList.add(vboID);
            vectorNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vectorNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, vectorNormalsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            vboID = glGenBuffers();
            vboIdList.add(vboID);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textureCoordsBuffer != null) {
                MemoryUtil.memFree(textureCoordsBuffer);
            }
            if (vectorNormalsBuffer != null) {
                MemoryUtil.memFree(vectorNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void render() {
        if (material.isTextured()) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, material.getTexture().getId());
        }

        glBindVertexArray(getVaoID());
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        for (int vboID : vboIdList) {
            glDeleteBuffers(vboID);
        }

        if (material.isTextured()) {
            material.getTexture().cleanup();
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }

}
