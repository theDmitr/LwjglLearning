package dmitr.tutor.customapp;

import dmitr.tutor.engine.graphics.Camera;
import dmitr.tutor.engine.graphics.Transformation;
import dmitr.tutor.engine.graphics.light.PointLight;
import dmitr.tutor.engine.graphics.model.Mesh;
import dmitr.tutor.engine.graphics.model.item.Item;
import dmitr.tutor.engine.graphics.shader.ShaderProgram;
import dmitr.tutor.engine.util.Utils;
import dmitr.tutor.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    private float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10.0f;
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vert"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.frag"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, Iterable<Item> items,
                       Vector3f ambientLight, PointLight pointLight) {
        clear();

        if (window.windowProperties.isResized()) {
            glViewport(0, 0, window.windowProperties.getWidth(), window.windowProperties.getHeight());
            window.windowProperties.setResized(false);
        }

        shaderProgram.bind();

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(
                FOV,
                window.windowProperties.getWidth(), window.windowProperties.getHeight(),
                Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("ambientLight", ambientLight);
        shaderProgram.setUniform("specularPower", specularPower);

        PointLight currentPointLight = new PointLight(pointLight);
        Vector3f lightPos = currentPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform("pointLight", currentPointLight);

        shaderProgram.setUniform("texture_sampler", 0);

        for (Item item : items) {
            Mesh mesh = item.getMesh();
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(item, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("material", item.getMesh().getMaterial());
            item.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

}
