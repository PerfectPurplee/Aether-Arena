package main;

import java.awt.*;

import static scenes.AllScenes.*;

public class GameEngine extends Thread {

    MainFrame mainFrame;
    MainPanel mainPanel;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public GameEngine() {

        mainPanel = new MainPanel();
        mainFrame = new MainFrame(mainPanel);

        this.start();
    }

    private void update() {

    }

    public static void render(Graphics g) {

        switch (Current_Scene) {

            case PLAYING -> {
            }
            case MENU -> {
            }
            case PAUSE -> {
            }
        }


    }

    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                mainPanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }

    }

}
