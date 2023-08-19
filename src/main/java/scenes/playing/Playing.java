package scenes.playing;

import entities.playercharacters.PlayerClass;
import entities.spells.basicspells.FirstSpell;
import main.MainPanel;

import java.awt.*;

public class Playing {

    PlayerClass playerClass;
    Camera camera;

    int animationTick, animationSpeed = 60, animationIndexMoving, animationIndexIdle;

    public Playing(PlayerClass playerClass, Camera camera) {
        this.playerClass = playerClass;
        this.camera = camera;


    }

    public void update() {
        playerClass.moveController();
        playerClass.playerSpriteController();
        animationController();
        FirstSpell.updateFirstSpells();
        camera.updateCameraPosition();
    }

    public void draw(Graphics g) {

//        Rysowanie Kamery
//        g.drawImage(camera.WHOLE_MAP.getSubimage(camera.cameraPosX, camera.cameraPosY, camera.Camera_Width,
//                camera.Camera_Height), 0, 0, MainPanel.worldSize.width, MainPanel.worldSize.height, null);

        //  g.drawImage(MainPanel.worldMap, 0, 0, Camera.Camera_Width, Camera.Camera_Height,
//                camera.cameraPosX, camera.cameraPosY, camera.cameraPosX + Camera.Camera_Width, camera.cameraPosY + Camera.Camera_Height, null);

        g.drawImage(MainPanel.worldMap, 0, 0, null);
//        Rysowanie postaci
        if (playerClass.checkIsCharacterMoving()) {
            g.drawImage(playerClass.playerSpriteController()[animationIndexMoving],
                    (int) PlayerClass.playerPosX, (int) PlayerClass.playerPosY, 144, 144, null);
        } else if (!playerClass.checkIsCharacterMoving()) {
            g.drawImage(playerClass.playerSpriteController()[animationIndexIdle],
                    (int) PlayerClass.playerPosX, (int) PlayerClass.playerPosY, 144, 144, null);

        }
//        Rysowanie Zaklec
        FirstSpell.ListOfActiveFirstSpells.forEach(firstSpell ->
                g.drawImage(firstSpell.spellSprites[firstSpell.animationIndex], (int) firstSpell.spellStartingPosX,
                        (int) firstSpell.spellStartingPosY, 64, 64, null));


    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_UP |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_DOWN |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_LEFT |
                    playerClass.playerSpriteController() == playerClass.playerSpriteIDLE_RIGHT)
                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            else if (playerClass.playerSpriteController() == playerClass.playerSpriteUP |
                    playerClass.playerSpriteController() == playerClass.playerSpriteDOWN |
                    playerClass.playerSpriteController() == playerClass.playerSpriteLEFT |
                    playerClass.playerSpriteController() == playerClass.playerSpriteRIGHT)
                if (animationIndexMoving < 3)
                    animationIndexMoving++;
                else animationIndexMoving = 0;
            animationTick = 0;
        }
    }
}
