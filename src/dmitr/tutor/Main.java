package dmitr.tutor;

import dmitr.tutor.customapp.CustomGame;
import dmitr.tutor.engine.kernel.Engine;
import dmitr.tutor.engine.kernel.EngineProperties;
import dmitr.tutor.engine.kernel.IAppLogic;
import dmitr.tutor.window.Window;
import dmitr.tutor.window.WindowProperties;

public class Main {

    public static void main(String[] args) {
        WindowProperties windowProperties = new WindowProperties("Game", 600, 600, true);
        EngineProperties engineProperties = new EngineProperties(60, 20);

        Window window = new Window(windowProperties);
        IAppLogic appLogic = new CustomGame();
        Engine engine = new Engine(window, engineProperties, appLogic);

        try {
            engine.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

}
