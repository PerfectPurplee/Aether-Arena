package main;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import inputs.ActionListener;
import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;
import networking.Client;
import scenes.championselect.ChampionSelect;
import scenes.menu.Menu;
import scenes.playing.Camera;
import scenes.playing.Playing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static main.EnumContainer.AllScenes.Current_Scene;

public class GameEngine extends Thread {

    static Playing playing;
    static Menu menu;
    static ChampionSelect championSelect;
    static MainPanel mainPanel;
    MainFrame mainFrame;
    AssetLoader assetLoader;
    LocalPlayer localPlayer;
    OnlinePlayer onlinePlayer;
    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;
    ActionListener actionListener;
    MapGenerator mapGenerator;
    Camera camera;
    //    Server server;
    Client client;


    private final int FPS_SET = 120;
    private final int UPS_SET = 128;

    public static BufferedImage BasicSpellsSpriteSheet;

    public GameEngine() {


        assetLoader = new AssetLoader();
        mapGenerator = new MapGenerator(assetLoader);

        localPlayer = new LocalPlayer(assetLoader);
        championSelect = new ChampionSelect(assetLoader);
        OnlinePlayer.assetLoader = assetLoader;

        playerKeyboardInputs = new PlayerKeyboardInputs(localPlayer);
        playerMouseInputs = new PlayerMouseInputs(localPlayer, championSelect);
        mainPanel = new MainPanel(playerKeyboardInputs, playerMouseInputs);
        actionListener = new ActionListener(mainPanel, this);
        menu = new Menu(mainPanel, playerMouseInputs);
        camera = new Camera(mapGenerator);
        playing = new Playing(localPlayer, camera);
        mainFrame = new MainFrame(mainPanel);

        championSelect.mainPanel = mainPanel;
        playerMouseInputs.mainFrame = mainFrame;
        playerMouseInputs.gameEngine = this;

        this.start();
    }

    private void update() {

        switch (Current_Scene) {
            case PLAYING -> playing.update();
            case CHAMPION_SELECT -> championSelect.update();
        }
    }

    public static void render(Graphics g) {

        switch (Current_Scene) {

            case PLAYING -> {
                playing.draw(g);
            }
            case MENU -> {
                menu.draw(g);
            }
            case PAUSE -> {
            }
            case CHAMPION_SELECT -> {
                championSelect.draw(g);
            }
            case SETTINGS -> {
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



//    public void createServer() {
//        server = new Server(onlinePlayer);
//    }

    public void createClient(String serverIPaddress) {
        client = new Client(localPlayer, serverIPaddress);
        playerMouseInputs.client = client;
        playing.client = client;
    }


    public String getHostIpAddress() {

        String serverIPaddress = JOptionPane.showInputDialog(mainPanel, "Type in your host IP address to connect: ");

        return serverIPaddress;
    }

    public void changeScene(EnumContainer.AllScenes scene) {
        mainPanel.removeAll();

//        SCENE WE WANT CHANGE TO
        switch (scene) {
            case MENU -> {
                menu.addComponentsToMainPanel();
            }
            case CHAMPION_SELECT -> {
                championSelect.addComponentsToMainPanel();
            }
            case PLAYING -> {
                this.createClient(this.getHostIpAddress());
            }
            case PAUSE -> {
            }
            case SETTINGS -> {
            }
            case MAP_EDITOR -> {
            }
        }
        EnumContainer.AllScenes.Current_Scene = scene;

    }

}
