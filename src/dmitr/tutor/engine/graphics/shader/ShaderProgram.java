package dmitr.tutor.engine.graphics.shader;

import dmitr.tutor.engine.graphics.light.PointLight;
import dmitr.tutor.engine.graphics.model.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programID;

    private int vertexShaderID;

    private int fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() throws Exception {
        programID = glCreateProgram();

        if (programID == 0) {
            throw new Exception("Error creating Shader!");
        }

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programID, uniformName);

        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform: " + uniformName);
        }

        uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".attenuation.constant");
        createUniform(uniformName + ".attenuation.linear");
        createUniform(uniformName + ".attenuation.exponent");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation attenuation = pointLight.getAttenuation();
        setUniform(uniformName + ".attenuation.constant", attenuation.getConstant());
        setUniform(uniformName + ".attenuation.linear", attenuation.getLinear());
        setUniform(uniformName + ".attenuation.exponent", attenuation.getExponent());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderID = glCreateShader(shaderType);

        if (shaderID == 0) {
            throw new Exception("Error creating Shader type: " + shaderType);
        }

        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderID, 1024));
        }

        glAttachShader(programID, shaderID);

        return shaderID;
    }

    public void link() throws Exception {
        glLinkProgram(programID);

        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID, 1024));
        }

        if (vertexShaderID != 0) {
            glDetachShader(programID, vertexShaderID);
        }

        if (fragmentShaderID != 0) {
            glDetachShader(programID, fragmentShaderID);
        }

        glValidateProgram(programID);

        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();

        if (programID != 0) {
            glDeleteProgram(programID);
        }
    }

}
