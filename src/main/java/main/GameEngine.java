package main;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import inputs.ActionListener;
import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;
import networking.Client;
import networking.Server;
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

import static scenes.AllScenes.*;

public class GameEngine extends Thread {

    static Playing playing;
    static Menu menu;
    MainFrame mainFrame;
    MainPanel mainPanel;
    LocalPlayer localPlayer;
    OnlinePlayer onlinePlayer;
    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;
    ActionListener actionListener;
    Camera camera;
    Server server;
    Client client;


    private final int FPS_SET = 120;
    private final int UPS_SET = 128;

    public static BufferedImage BasicSpellsSpriteSheet;

    public GameEngine() {

        getAllBasicSpellsSpriteSheet();

        localPlayer = new LocalPlayer(10000, 10000);
        onlinePlayer = new OnlinePlayer(10000, 10000);

        playerKeyboardInputs = new PlayerKeyboardInputs(localPlayer);
        playerMouseInputs = new PlayerMouseInputs(localPlayer);
        mainPanel = new MainPanel(playerKeyboardInputs, playerMouseInputs);
        actionListener = new ActionListener(mainPanel, this);
        menu = new Menu(mainPanel, actionListener);
        camera = new Camera();
        playing = new Playing(localPlayer, onlinePlayer, camera);
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
                menu.draw(g);
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
//                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }

    }

    private void getAllBasicSpellsSpriteSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/AttackSprites.png");
        try {
            BasicSpellsSpriteSheet = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createServer() {
        server = new Server(onlinePlayer);
    }

    public void createClient(String serverIPaddress) {
        client = new Client(localPlayer,onlinePlayer ,serverIPaddress);
    }

    public String getHostIpAddress() {

        String serverIPaddress = JOptionPane.showInputDialog(mainPanel, "Type in your host IP address to connect: ");

        return serverIPaddress;
    }

}
