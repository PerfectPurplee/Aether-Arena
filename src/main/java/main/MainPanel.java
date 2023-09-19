package main;

import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;

import javax.swing.*;
import java.awt.*;

import static main.GameEngine.*;

public class MainPanel extends JPanel {

    //    sposob na pobranie rozmiaru glownego ekranu.
//    Trzeba napisac metode skalujaca, tak zeby kod dzialal na wqhd.
    public static Dimension gameSize;
    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;


    public MainPanel(PlayerKeyboardInputs playerKeyboardInputs, PlayerMouseInputs playerMouseInputs) {
        this.setLayout(new BorderLayout());
        gameSize = Toolkit.getDefaultToolkit().getScreenSize();
//        gameSize = new Dimension(1280, 800);
        this.playerKeyboardInputs = playerKeyboardInputs;
        this.playerMouseInputs = playerMouseInputs;
        this.addKeyListener(playerKeyboardInputs);
        this.addMouseListener(playerMouseInputs);
        this.addMouseMotionListener(playerMouseInputs);
        this.setFocusable(true);
        this.requestFocus();
//        this.setBackground(Color.white);
        this.setPreferredSize(gameSize);


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }


}


