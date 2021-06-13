package nsu.oop.explorer.frontend.visual;

import java.util.Stack;

public class ScreenManager {
    private Application app;
    private Stack<AbstractScreen> screenStack;

    public ScreenManager(Application app) {
        this.app = app;
        screenStack = new Stack<>();
    }

    public void pop() {
        screenStack.pop();
        app.setScreen(screenStack.peek());
    }

    public void push(AbstractScreen screen) {
        screenStack.push(screen);
        app.setScreen(screenStack.peek());
    }

    public void dispose() {
        for (AbstractScreen screen : screenStack) {
            screen.dispose();
        }

        screenStack.clear();
    }

    public final Application app() {
        return app;
    }
}
