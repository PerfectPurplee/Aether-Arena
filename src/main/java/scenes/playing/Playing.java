package scenes.playing;

import entities.playercharacters.Player1;

import java.awt.*;

public class Playing {

    Player1 player1;

    public Playing() {
        player1 = new Player1();


    }

    public void draw(Graphics g) {
        g.drawImage(player1.playerSpriteRIGHT[0], 0, 0, null);
    }
}
