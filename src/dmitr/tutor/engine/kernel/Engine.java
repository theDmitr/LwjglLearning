package dmitr.tutor.engine.kernel;

import dmitr.tutor.engine.input.MouseInput;
import dmitr.tutor.engine.util.Timer;
import dmitr.tutor.log.LogCore;
import dmitr.tutor.log.LogType;
import dmitr.tutor.window.Window;

import static java.lang.Thread.sleep;

public class Engine implements Runnable {

    private final Window window;
    private final Timer timer;
    private final IAppLogic appLogic;
    private final MouseInput mouseInput;

    private final EngineProperties engineProperties;

    public Engine(Window window, EngineProperties engineProperties, IAppLogic appLogic) {
        this.window = window;
        this.timer = new Timer();
        this.engineProperties = engineProperties;
        this.appLogic = appLogic;
        this.mouseInput = new MouseInput();
    }

    @Override
    public void run() {

        try {
            init();
            loop();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    protected void init() throws Exception {
        LogCore.log("Engine initialization has begun!", LogType.INFO);
        window.init();
        timer.init();
        appLogic.init(window);
        mouseInput.init(window);
        LogCore.log("Engine initialization is complete!", LogType.INFO);
    }

    protected void loop() {
        float elapsedTime;
        float accumulator = 0.0f;
        float interval = 1.0f / engineProperties.UPS;

        boolean running = true;

        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.windowProperties.isvSync())
                sync();
        }
    }

    private void sync() {
        float loopSlot = 1f / engineProperties.FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected void input() {
        mouseInput.input(window);
        appLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        appLogic.update(interval, mouseInput);
    }

    protected void render() {
        appLogic.render(window);
        window.update();
    }

}
