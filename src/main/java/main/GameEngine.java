package main;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.QSpell;
import inputs.ActionListener;
import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;
import networking.Client;
import networking.PacketManager;
import scenes.ChampionSelect;
import scenes.Menu;
import scenes.Pause;
import scenes.Playing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    Pause pause;


    private final int FPS_SET = 120;
    private final int UPS_SET = 128;

    //    Global cooldown after using each spell given in milliseconds;
    public static final int GCD = 200;

    public static BufferedImage BasicSpellsSpriteSheet;

    public GameEngine() {


        assetLoader = new AssetLoader();
        mapGenerator = new MapGenerator(assetLoader);

        localPlayer = new LocalPlayer(assetLoader);
        championSelect = new ChampionSelect(assetLoader);
        OnlinePlayer.assetLoader = assetLoader;

        playerKeyboardInputs = new PlayerKeyboardInputs(localPlayer, this);
        playerMouseInputs = new PlayerMouseInputs(localPlayer, championSelect, this);
        mainPanel = new MainPanel(playerKeyboardInputs, playerMouseInputs);
        actionListener = new ActionListener(mainPanel, this);
        menu = new Menu(mainPanel, playerMouseInputs);
        camera = new Camera(mapGenerator);
        pause = new Pause(playerMouseInputs, playerKeyboardInputs, mainPanel);
        playing = new Playing(localPlayer, camera, pause);
        mainFrame = new MainFrame(mainPanel);


        championSelect.mainPanel = mainPanel;
        playerMouseInputs.mainFrame = mainFrame;
        playerMouseInputs.pause = pause;

        this.start();
    }

    private void update() {

        switch (Current_Scene) {
            case PLAYING, PAUSE -> playing.update();
            case CHAMPION_SELECT -> championSelect.update();
        }
    }

    public static void render(Graphics g) {

        switch (Current_Scene) {

            case PLAYING, PAUSE -> {
                playing.draw(g);
            }
            case MENU -> {
                menu.draw(g);
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
//        SCENE WE WANT CHANGE TO
        if (Current_Scene != scene) {
            mainPanel.removeAll();

            switch (scene) {
                case MENU -> {
                    if (Current_Scene == EnumContainer.AllScenes.PAUSE) {
                        client = null;
                        try {
                            Client.socket.send(PacketManager.disconnectPacket(localPlayer));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    menu.addComponentsToMainPanel();
                }
                case CHAMPION_SELECT -> {
                    championSelect.addComponentsToMainPanel();
                }
                case PLAYING -> {
                    if (this.client == null) {
                        this.createClient(this.getHostIpAddress());
                    } else {
                        try {
                            Client.socket.send(PacketManager.ChampionChangedPacket(localPlayer));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    this.localPlayer.userInterface = new UserInterface(localPlayer);
                }
                case PAUSE -> {
                    pause.initNewPanel();
                }
                case SETTINGS -> {
                }
                case MAP_EDITOR -> {
                }
            }
            EnumContainer.AllScenes.Current_Scene = scene;
        }
    }

    public static boolean isOffGCD() {
        return System.currentTimeMillis() - QSpell.LastLocalSpellCreationTime >= GCD;
    }

}
