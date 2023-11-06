package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainFrame extends JFrame implements MouseMotionListener {


    MainPanel mainPanel;

    public MainFrame(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.add(mainPanel);

        mainPanel.setFocusable(true);
        mainPanel.requestFocus();


        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.addMouseMotionListener(this);
        this.setVisible(true);
    }


    private void createCustomCursor() {
        java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("/pobrane.png");
        Cursor a = toolkit.createCustomCursor(image, new Point(this.getX(), this.getY()), "");
        this.setCursor(a);
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        {
            this.setLocation(this.getX() + e.getX(), this.getY() + e.getY());

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
