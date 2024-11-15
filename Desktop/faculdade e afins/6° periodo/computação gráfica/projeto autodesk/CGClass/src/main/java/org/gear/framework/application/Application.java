package org.gear.framework.application;

import org.gear.framework.core.design_patterns.Singleton;
import org.gear.framework.core.service.event.EventAPI;
import org.gear.framework.core.service.event.EventFlag;
import org.gear.framework.core.service.event.reactive.ReactiveEvent;
import java.util.Random;

public abstract class Application {

    private static boolean running = true;
    private static Singleton<Application> instance;
    private static final EventAPI eventAPI = new EventAPI();

    private float rotationAngle = 0.0f;
    private float[] color = { 1.0f, 1.0f, 1.0f };
    private final Random random = new Random();

    public static void launch(Application application) {
        instance = new Singleton<>(application);

        eventAPI.init();
        eventAPI.registerApplicationCallbackFlag(EventFlag.APPLICATION_SHUTDOWN, instance.get()::isShutdown);
        eventAPI.subscribe(instance.get());

        instance.get().start();

        while (!eventAPI.getFlag(EventFlag.APPLICATION_SHUTDOWN)) {
            eventAPI.update();
            instance.get().update();
        }

        instance.get().shutdown();
    }

    public abstract void start();

    public abstract void update();

    public final void shutdown() {
        running = false;
    }

    public final boolean isShutdown() {
        return !running;
    }

    public static void dispatchEvent(ReactiveEvent event) {
        eventAPI.dispatchEvent(event);
    }

    public void handleKeyPress(String key) {
        switch (key) {
            case "A":
                rotationAngle -= 5.0f;
                break;
            case "D":
                rotationAngle += 5.0f;
                break;
            case "W":
                color = generateRandomColor();
                break;
            default:
                break;
        }
    }

    private float[] generateRandomColor() {
        return new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat() };
    }

    public void render() {
        drawTriangle(rotationAngle, color);
        drawTriangle(rotationAngle, new float[] { 0.5f, 0.5f, 0.5f });
        drawSquare(rotationAngle);
    }

    private void drawTriangle(float rotation, float[] color) {
    }

    private void drawSquare(float rotation) {
    }
}