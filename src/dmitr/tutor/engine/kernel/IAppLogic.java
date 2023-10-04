package dmitr.tutor.engine.kernel;

import dmitr.tutor.engine.input.MouseInput;
import dmitr.tutor.window.Window;

public interface IAppLogic {

    void init(Window window) throws Exception;

    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);

    void render(Window window);

    void cleanup();

}
