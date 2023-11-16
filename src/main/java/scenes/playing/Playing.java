package scenes.playing;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.QSpell;
import inputs.PlayerMouseInputs;
import main.MainPanel;
import networking.Client;
import networking.PacketManager;
import scenes.SceneEssentials;
import scenes.menu.Menu;

import java.awt.*;
import java.io.IOException;

public class Playing implements SceneEssentials {

    LocalPlayer localPlayer;
    Camera camera;
    public Client client;


    public Playing(LocalPlayer localPlayer, Camera camera) {
        this.localPlayer = localPlayer;

        this.camera = camera;


    }

    public void update() {

//        Camera Updates
        camera.updateEverythingForCamera();

//        Local player and spells update
        localPlayer.moveController();
        localPlayer.currentPlayerSprite = localPlayer.setCurrentPlayerSprite();
        localPlayer.animationController();
        localPlayer.updatePlayerPositionOnScreenAndPlayerHitbox();
        localPlayer.updateHealthBarCurrentHealthAndPositionOnScreen();
//      spellCastController creates spells, but also sends data to server
        localPlayer.spellCastController();
        localPlayer.updateCooldownsForDrawing();


        QSpell.updateAllSpells01();


//        Online player update
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

            onlinePlayer.currentPlayerSpriteOnlinePlayer = onlinePlayer.setCurrentOnlinePlayerSprite();
            onlinePlayer.animationController();
            onlinePlayer.updatePlayerPositionOnScreenAndHitbox();
            onlinePlayer.updateHealthBarCurrentHealthAndPositionOnScreen();

        });

        checkIfAnyPlayerGotHit();

//        Send data to server
//        Movement request packet while mouse is dragging
        sendMouseDraggedMovementPacket();

    }

    public void draw(Graphics g) {


//        DRAWING MAP AT CURRENT CAMERA POSITION
        g.drawImage(camera.currentCameraPosition, 0, 0, null);


//        DRAWING ONLINEPLAYERS
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

            g.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[onlinePlayer.currentIndexerForAnimation()],
                    (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);

        });

//        DRAWING LOCALPLAYER
        g.drawImage(localPlayer.currentPlayerSprite[localPlayer.currentIndexerForAnimation()],
                (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, null);

//        DRAWING SPELLS
        QSpell.listOfActiveQSpells.forEach(spell01 -> {
            g.drawImage(spell01.currentSpellSprites[spell01.animationIndex], (int) spell01.spellPosXScreen,
                    (int) spell01.spellPosYScreen, null);
        });


//        DRAWING HEALTHBAR ONLINEPLAYERS
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            g.setColor(Color.black);
            g.fillRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.RED);
            g.fillRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                    onlinePlayer.healthbar.currentHealthToDraw, onlinePlayer.healthbar.healthbarHeight);
            g.setColor(Color.YELLOW);
            g.drawRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                    onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
        });

//        DRAWING HEALTHBARLOCALPLAYER
        g.setColor(Color.black);
        g.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.GREEN);
        g.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.currentHealthToDraw, localPlayer.healthbar.healthbarHeight);
        g.setColor(Color.YELLOW);
        g.drawRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);


//        USER INTERFACE

        float scaleFactor = 1.5F;  // Set the scale factor for icons and cooldown text
// Draw the scaled Icons
        g.drawImage(localPlayer.userInterface.currentQSpellICON,
                MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor),
                null);

        g.drawImage(localPlayer.userInterface.currentQSpellICON,
                MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor),
                null);

        g.drawImage(localPlayer.userInterface.currentESpellICON,
                MainPanel.gameSize.width / 2,
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentESpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentESpellICON.getHeight() * scaleFactor),
                null);

        g.drawImage(localPlayer.userInterface.currentRSpellICON,
                MainPanel.gameSize.width / 2 + (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentRSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentRSpellICON.getHeight() * scaleFactor),
                null);


        g.setColor(new Color(0, 0, 0, 223));
        g.setFont(Menu.googleExo2.deriveFont(30F * scaleFactor));

// Draw Grey color on spell Icons indicating spell is on cooldown and the cooldown text
        if (localPlayer.QCurrentCooldown > 0) {
            g.setColor(new Color(210, 210, 210, 50));
            g.fillRect(
                    MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor));

            g.setColor(new Color(0, 0, 0, 192));
            g.drawString(String.valueOf(localPlayer.QCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 - localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.WCurrentCooldown > 0) {
            g.setColor(new Color(210, 210, 210, 50));
            g.fillRect(
                    MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor));

            g.setColor(new Color(0, 0, 0, 192));
            g.drawString(String.valueOf(localPlayer.WCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 - localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.ECurrentCooldown > 0) {
            g.setColor(new Color(210, 210, 210, 50));
            g.fillRect(
                    MainPanel.gameSize.width / 2,
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentESpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentESpellICON.getHeight() * scaleFactor));

            g.setColor(new Color(0, 0, 0, 192));
            g.drawString(String.valueOf(localPlayer.ECurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.RCurrentCooldown > 0) {
            g.setColor(new Color(210, 210, 210, 50));
            g.fillRect(
                    MainPanel.gameSize.width / 2 + (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentRSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentRSpellICON.getHeight() * scaleFactor));

            g.setColor(new Color(0, 0, 0, 190));
            g.drawString(String.valueOf(localPlayer.RCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }


//        DEBUGGING

//        HITBOXES
//        g.setColor(Color.red);
//        g.drawRect((int) localPlayer.localPlayerHitbox.playerHitboxPosXScreen, (int) localPlayer.localPlayerHitbox.playerHitboxPosYScreen,
//                (int) localPlayer.localPlayerHitbox.getWidth(), (int) localPlayer.localPlayerHitbox.getHeight());
//        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer ->
//                g.drawRect((int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosXScreen, (int) onlinePlayer.onlinePlayerHitbox.playerHitboxPosYScreen,
//                        (int) onlinePlayer.onlinePlayerHitbox.getWidth(), (int) onlinePlayer.onlinePlayerHitbox.getHeight()));
//        QSpell.listOfActiveQSpells.forEach(spell01 -> {
//            if (spell01.spell01Hitbox != null) {
//                g.drawRect((int) spell01.spell01Hitbox.spell01HitboxPosXScreen, (int) spell01.spell01Hitbox.spell01HitboxPosYScreen,
//                        (int) spell01.spell01Hitbox.getWidth(), (int) spell01.spell01Hitbox.getHeight());
//            }
//        });

    }


    private void sendMouseDraggedMovementPacket() {
        if (PlayerMouseInputs.mouseDragging) {
            try {
                Client.socket.send(PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, Client.ClientID));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkIfAnyPlayerGotHit() {

//        LocalPlayer

        QSpell.listOfActiveQSpells.forEach(spell -> {
            if (spell.spell01Hitbox != null) {
                if (localPlayer.localPlayerHitbox.intersects(spell.spell01Hitbox) && Client.ClientID != spell.spellCasterClientID) {
                    if (localPlayer.healthbar.currentHealth > 0) {
                        localPlayer.healthbar.currentHealth = localPlayer.healthbar.currentHealth - 50;
                    }
                    spell.playerGotHit = true;
                }

//                    Online player
                OnlinePlayer.listOfAllConnectedOnlinePLayers
                        .stream()
                        .filter(onlinePlayer -> onlinePlayer.onlinePlayerHitbox.intersects(spell.spell01Hitbox) && onlinePlayer.onlinePlayerID != spell.spellCasterClientID)
                        .forEach(onlinePlayerFiltered -> {
                            if (onlinePlayerFiltered.healthbar.currentHealth > 0) {
                                onlinePlayerFiltered.healthbar.currentHealth = onlinePlayerFiltered.healthbar.currentHealth - 50;
                            }
                            spell.playerGotHit = true;
                        });


            }
        });


    }
    //            FOR ONLY ONE PLAYER GETTING HIT
//            Optional<OnlinePlayer> onlinePlayerInter = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
//                    .filter(onlinePlayer -> spell.spell01Hitbox.intersects(onlinePlayer.onlinePlayerHitbox.getBounds())).findFirst();
//            onlinePlayerInter.ifPresent(onlinePlayer -> onlinePlayer.healthbar.currentHealth = onlinePlayer.healthbar.currentHealth - 50);

}

