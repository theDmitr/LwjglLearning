package dmitr.tutor.window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private long windowHandle;
    public final WindowProperties windowProperties;

    public Window(WindowProperties windowProperties) {
        this.windowProperties = windowProperties;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        windowHandle = glfwCreateWindow(
                windowProperties.getWidth(),
                windowProperties.getHeight(),
                windowProperties.getTitle(),
                NULL, NULL);

        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window!");

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            windowProperties.setWidth(width);
            windowProperties.setHeight(height);
            windowProperties.setResized(true);
        });

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        glfwSetWindowPos(windowHandle,
                (vidMode.width() - windowProperties.getWidth()) / 2,
                (vidMode.height() - windowProperties.getHeight()) / 2);

        glfwMakeContextCurrent(windowHandle);

        if (windowProperties.isvSync())
            glfwSwapInterval(1);

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

}
