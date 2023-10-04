package dmitr.tutor.customapp;

import dmitr.tutor.engine.graphics.Camera;
import dmitr.tutor.engine.graphics.PositionableObject;
import dmitr.tutor.engine.graphics.light.PointLight;
import dmitr.tutor.engine.graphics.model.Material;
import dmitr.tutor.engine.graphics.model.Mesh;
import dmitr.tutor.engine.graphics.model.Texture;
import dmitr.tutor.engine.graphics.model.item.Item;
import dmitr.tutor.engine.input.MouseInput;
import dmitr.tutor.engine.kernel.IAppLogic;
import dmitr.tutor.engine.util.OBJLoader;
import dmitr.tutor.window.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class CustomGame implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private static final float MOVING_POS_STEP = 0.05f;

    private final Vector3f moveInc;

    private PositionableObject bindedMovingObject;

    private final Renderer renderer;

    private final Camera camera;

    private List<Item> items;

    private Vector3f ambientLight;

    private PointLight pointLight;

    public CustomGame() {
        renderer = new Renderer();
        camera = new Camera();
        moveInc = new Vector3f();
        bindedMovingObject = camera;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1.0f;

        items = new ArrayList<>();

        Mesh mesh1 = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture1 = new Texture("textures/grassblock.png");
        Material material1 = new Material(texture1, reflectance);
        mesh1.setMaterial(material1);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Item item1 = new Item(mesh1);
                item1.setScale(0.5f);
                item1.setPosition(1.0f * i, 0.0f, -1.0f * j - 3.0f);
                items.add(item1);
            }
        }

        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);
        Vector3f lightPosition = new Vector3f(0.0f, 0.0f, 1.0f);
        float lightIntensity = 10.0f;
        pointLight = new PointLight(lightColor, lightPosition, lightIntensity);
        PointLight.Attenuation attenuation = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(attenuation);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        moveInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            moveInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            moveInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            moveInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            moveInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            moveInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            moveInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT_CONTROL)) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        }

        if (window.isKeyPressed(GLFW_KEY_C)) {
            bindedMovingObject = camera;
        } else if (window.isKeyPressed(GLFW_KEY_V)) {
            bindedMovingObject = pointLight;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        if (bindedMovingObject != null)
            bindedMovingObject.movePosition(
                    moveInc.x * MOVING_POS_STEP,
                    moveInc.y * MOVING_POS_STEP,
                    moveInc.z * MOVING_POS_STEP);

        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotationVector = mouseInput.getDisplayVector();
            camera.moveRotation(
                    rotationVector.x * MOUSE_SENSITIVITY,
                    rotationVector.y * MOUSE_SENSITIVITY,
                    0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, items, ambientLight, pointLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (Item item : items) {
            item.getMesh().cleanup();
        }
    }

}
