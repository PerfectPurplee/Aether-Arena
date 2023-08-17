package main;

import entities.playercharacters.PlayerClass;
import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;
import scenes.playing.Playing;

import java.awt.*;

import static scenes.AllScenes.*;

public class GameEngine extends Thread {

    static Playing playing;
    MainFrame mainFrame;
    MainPanel mainPanel;
    PlayerClass playerClass;
    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public GameEngine() {

        playerClass = new PlayerClass(100, 100);
        playing = new Playing(playerClass);
        playerKeyboardInputs = new PlayerKeyboardInputs(playerClass);
        playerMouseInputs = new PlayerMouseInputs(playerClass);
        mainPanel = new MainPanel(playerKeyboardInputs, playerMouseInputs);
        mainFrame = new MainFrame(mainPanel);



        this.start();
    }

    private void update() {

        if (Current_Scene == PLAYING) {
            playing.update();
        }

    }

    public static void render(Graphics g) {

        switch (Current_Scene) {

            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> {
            }
            case PAUSE -> {
            }
            case MAP_EDITOR -> {
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
