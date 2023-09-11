package scenes.championselect;

import inputs.PlayerMouseInputs;
import main.MainPanel;
import scenes.SceneEssentials;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChampionSelect implements SceneEssentials {

    public MainPanel mainPanel;

    public JLabel championChoice1;
    public JLabel championChoice2;

    BufferedImage champion1;
    BufferedImage champion2;

    Queue<Throwable> yo = new ConcurrentLinkedQueue<>();

    public ChampionSelect() {
        initComponents();
        champion1 = getChampionImage("/DON_OHL.png");
        champion2 = getChampionImage("/WIKING_RUN.png");

    }

    public void initComponents() {
        championChoice1 = new JLabel();
        championChoice1.setBackground(Color.BLUE);
        championChoice2 = new JLabel();
        championChoice2.setBackground(Color.BLUE);


    }

    public void addComponentsToMainPanel() {
        championChoice1.addMouseListener(mainPanel.getMouseListeners()[0]);
        championChoice2.addMouseListener(mainPanel.getMouseListeners()[0]);
        championChoice2.setBounds(mainPanel.getWidth() / 2 + 20, 300, 144, 144);
        championChoice1.setBounds(mainPanel.getWidth() / 2 - 164, 300, 144, 144);
        mainPanel.add(championChoice1);
        mainPanel.add(championChoice2);

    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(champion1, mainPanel.getWidth() / 2 - 164, 300, 144, 144, null);
        g.drawImage(champion2, mainPanel.getWidth() / 2 + 20, 300, 144, 144, null);

    }


    private BufferedImage getChampionImage(String imagePath) {

        InputStream inputStream = getClass().getResourceAsStream(imagePath);
        try {
            assert inputStream != null;
            if (imagePath.equals("/DON_OHL.png")) {
                return ImageIO.read(inputStream).getSubimage(0, 72 * 7, 72, 72);
            } else if (imagePath.equals("/WIKING_RUN.png")) {
                return ImageIO.read(inputStream).getSubimage(0, 0, 128, 128);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
