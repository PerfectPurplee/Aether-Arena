package main;

import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;

import javax.swing.*;
import java.awt.*;

import static main.GameEngine.*;

public class MainPanel extends JPanel {

//    sposob na pobranie rozmiaru glownego ekranu.
//    Trzeba napisac metode skalujaca, tak zeby kod dzialal na wqhd.
//    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;

    public MainPanel(PlayerKeyboardInputs playerKeyboardInputs,
                     PlayerMouseInputs playerMouseInputs) {
        this.playerKeyboardInputs = playerKeyboardInputs;
        this.playerMouseInputs = playerMouseInputs;

        this.addKeyListener(playerKeyboardInputs);
        this.addMouseListener(playerMouseInputs);
        this.setFocusable(true);
        this.requestFocus();

        this.setBackground(Color.white);
        this.setPreferredSize(new Dimension(1800, 900));
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }
}
