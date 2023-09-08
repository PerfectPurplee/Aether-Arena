package scenes.playing;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.FirstSpell;

import java.awt.*;

public class Playing {

    LocalPlayer localPlayer;
    Camera camera;


    public Playing(LocalPlayer localPlayer, Camera camera) {
        this.localPlayer = localPlayer;

        this.camera = camera;


    }

    public void update() {

//        Local player and spells update
        localPlayer.moveController();
        localPlayer.playerSpriteController();
        localPlayer.animationController();
        FirstSpell.updateFirstSpells();
        camera.updateCameraPosition();
        localPlayer.updatePlayerPositionOnScreen();

//        Online player update
//        onlinePlayer.updatePlayerPositionOnScreen();
    }

    public void draw(Graphics g) {

//        RYSOWANIE LOKALNYCH OBIEKTOW

//        Rysowanie Kamery
        g.drawImage(camera.WHOLE_MAP.getSubimage(Camera.cameraPosX, Camera.cameraPosY, camera.Camera_Width,
                camera.Camera_Height), 0, 0, null);


//        Rysowanie postaci
        if (localPlayer.checkIsCharacterMoving()) {
            g.drawImage(localPlayer.playerSpriteController()[localPlayer.animationIndexMoving],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen,  null);
        } else if (!localPlayer.checkIsCharacterMoving()) {
            g.drawImage(localPlayer.playerSpriteController()[localPlayer.animationIndexIdle],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen,  null);

        }

 //       Rysowanie ONLINE postaci

//        g.drawImage(onlinePlayer.playerSpriteDOWN[onlinePlayer.animationIndexMoving],
//                (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen,  null);

//        Rysowanie Zaklec
        FirstSpell.ListOfActiveFirstSpells.forEach(firstSpell -> {
            g.drawImage(firstSpell.spellSprites[firstSpell.animationIndex], (int) firstSpell.spellPosXScreen,
                    (int) firstSpell.spellPosYScreen, 32,32, null);
        });


    }

}
