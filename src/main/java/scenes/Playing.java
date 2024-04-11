package scenes;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.QSpell;
import inputs.PlayerMouseInputs;
import main.Camera;
import main.EnumContainer;
import main.MainPanel;
import networking.Client;
import networking.PacketManager;

import java.awt.*;
import java.io.IOException;

public class Playing implements SceneEssentials {

    private final Pause pause;
    LocalPlayer localPlayer;
    Camera camera;
    public Client client;


    public Playing(LocalPlayer localPlayer, Camera camera, Pause pause) {
        this.localPlayer = localPlayer;
        this.pause = pause;

        this.camera = camera;


    }

    public void update() {

//        Camera Updates
        camera.updateEverythingForCamera();

//        Local player and spells update

//        IF PLAYER NOT DEAD
        if (!localPlayer.isPlayerDead) {
            localPlayer.moveController();
            localPlayer.currentPlayerSprite = localPlayer.setCurrentPlayerSprite();
            localPlayer.animationController();
            localPlayer.updatePlayerPositionOnScreen();
            localPlayer.updatePlayerHitboxWorldAndPosOnScreen();
            localPlayer.updateHealthBarCurrentHealthAndPositionOnScreen();
//      spellCastController creates spells, but also sends data to server
            localPlayer.spellCastController();
            localPlayer.updateCooldownsForDrawing();

            localPlayer.checkIsPlayerDead();

//          PLAYER DEAD
        } else {
            localPlayer.updatePlayerPositionOnScreen();
            localPlayer.updateReviveCooldownAndRespawnPlayer();
            localPlayer.updateHealthBarCurrentHealthAndPositionOnScreen();
            localPlayer.animationController();

        }


        QSpell.updateAllSpells01();


//        Online player update
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {

            onlinePlayer.currentPlayerSpriteOnlinePlayer = onlinePlayer.setCurrentOnlinePlayerSprite();
            onlinePlayer.animationController();
            onlinePlayer.updatePlayerPositionOnScreenAndHitbox();
            onlinePlayer.updateHealthBarCurrentHealthAndPositionOnScreen();
            onlinePlayer.updatePlayerHitboxWorldAndPosOnScreen();
            onlinePlayer.checkIsPlayerDead();

        });

        checkIfAnyPlayerGotHit();

//        Send data to server
//        Movement request packet while mouse is dragging
        if (!localPlayer.isPlayerDead)
            sendMouseDraggedMovementPacket();

    }

    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


//        DRAWING MAP AT CURRENT CAMERA POSITION
        g2d.drawImage(camera.currentCameraPosition, 0, 0, null);


//        DRAWING ONLINEPLAYERS
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            if (!onlinePlayer.deathAnimationFinished) {
                g2d.drawImage(onlinePlayer.currentPlayerSpriteOnlinePlayer[onlinePlayer.currentIndexerForAnimation()],
                        (int) onlinePlayer.playerPosXScreen, (int) onlinePlayer.playerPosYScreen, null);
            }
        });

//        DRAWING LOCALPLAYER
        if (!localPlayer.deathAnimationFinished) {
            g2d.drawImage(localPlayer.currentPlayerSprite[localPlayer.currentIndexerForAnimation()],
                    (int) LocalPlayer.playerPosXScreen, (int) LocalPlayer.playerPosYScreen, null);
        }

//        DRAWING SPELLS
        QSpell.listOfActiveQSpells.forEach(spell01 -> {
            g2d.drawImage(spell01.currentSpellSprites[spell01.animationIndex], (int) spell01.spellPosXScreen,
                    (int) spell01.spellPosYScreen, null);
        });


//        DRAWING HEALTHBAR ONLINEPLAYERS
        OnlinePlayer.listOfAllConnectedOnlinePLayers.forEach(onlinePlayer -> {
            if (!onlinePlayer.deathAnimationFinished) {
                g2d.setColor(Color.black);
                g2d.fillRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                        onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
                g2d.setColor(Color.RED);
                g2d.fillRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                        onlinePlayer.healthbar.currentHealthToDraw, onlinePlayer.healthbar.healthbarHeight);
                g2d.setColor(Color.YELLOW);
                g2d.drawRect(onlinePlayer.healthbar.healthbarPositionOnScreenX, onlinePlayer.healthbar.healthbarPositionOnScreenY,
                        onlinePlayer.healthbar.healthbarWidth, onlinePlayer.healthbar.healthbarHeight);
            }
        });

//        DRAWING HEALTHBARLOCALPLAYER
        if (!localPlayer.deathAnimationFinished) {
            g2d.setColor(Color.black);
            g2d.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                    localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);
            g2d.setColor(Color.GREEN);
            g2d.fillRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                    localPlayer.healthbar.currentHealthToDraw, localPlayer.healthbar.healthbarHeight);
            g2d.setColor(Color.YELLOW);
            g2d.drawRect(localPlayer.healthbar.healthbarPositionOnScreenX, localPlayer.healthbar.healthbarPositionOnScreenY,
                    localPlayer.healthbar.healthbarWidth, localPlayer.healthbar.healthbarHeight);
        }

//        USER INTERFACE

        float scaleFactor = 1.5F;  // Set the scale factor for icons and cooldown text
// Draw the scaled Icons
        g2d.drawImage(localPlayer.userInterface.dashICON,
                MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 4),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor),
                null);

        g2d.drawImage(localPlayer.userInterface.currentQSpellICON,
                MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor),
                null);

        g2d.drawImage(localPlayer.userInterface.currentQSpellICON,
                MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor),
                null);

        g2d.drawImage(localPlayer.userInterface.currentESpellICON,
                MainPanel.gameSize.width / 2,
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentESpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentESpellICON.getHeight() * scaleFactor),
                null);

        g2d.drawImage(localPlayer.userInterface.currentRSpellICON,
                MainPanel.gameSize.width / 2 + (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                MainPanel.gameSize.height - (int) (60 * scaleFactor),
                (int) (localPlayer.userInterface.currentRSpellICON.getWidth() * scaleFactor),
                (int) (localPlayer.userInterface.currentRSpellICON.getHeight() * scaleFactor),
                null);


        g2d.setColor(new Color(0, 0, 0, 223));
        g2d.setFont(Menu.googleExo2.deriveFont(30F * scaleFactor));

// Draw Grey color on spell Icons indicating spell is on cooldown and the cooldown text
        if (localPlayer.DashCurrentCooldown > 0) {
            g2d.setColor(new Color(210, 210, 210, 50));
            g2d.fillRect(
                    MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 4),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor)
            );

            g2d.setColor(new Color(0, 0, 0, 192));
            g2d.drawString(String.valueOf(localPlayer.DashCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 - localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 4 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }

        if (localPlayer.QCurrentCooldown > 0) {
            g2d.setColor(new Color(210, 210, 210, 50));
            g2d.fillRect(
                    MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor));

            g2d.setColor(new Color(0, 0, 0, 192));
            g2d.drawString(String.valueOf(localPlayer.QCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 - localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor * 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.WCurrentCooldown > 0) {
            g2d.setColor(new Color(210, 210, 210, 50));
            g2d.fillRect(
                    MainPanel.gameSize.width / 2 - (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentQSpellICON.getHeight() * scaleFactor));

            g2d.setColor(new Color(0, 0, 0, 192));
            g2d.drawString(String.valueOf(localPlayer.WCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 - localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.ECurrentCooldown > 0) {
            g2d.setColor(new Color(210, 210, 210, 50));
            g2d.fillRect(
                    MainPanel.gameSize.width / 2,
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentESpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentESpellICON.getHeight() * scaleFactor));

            g2d.setColor(new Color(0, 0, 0, 192));
            g2d.drawString(String.valueOf(localPlayer.ECurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }
        if (localPlayer.RCurrentCooldown > 0) {
            g2d.setColor(new Color(210, 210, 210, 50));
            g2d.fillRect(
                    MainPanel.gameSize.width / 2 + (int) (localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor),
                    MainPanel.gameSize.height - (int) (60 * scaleFactor),
                    (int) (localPlayer.userInterface.currentRSpellICON.getWidth() * scaleFactor),
                    (int) (localPlayer.userInterface.currentRSpellICON.getHeight() * scaleFactor));

            g2d.setColor(new Color(0, 0, 0, 190));
            g2d.drawString(String.valueOf(localPlayer.RCurrentCooldown / 1000 + 1),
                    (int) (MainPanel.gameSize.width / 2 +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor +
                            localPlayer.userInterface.currentQSpellICON.getWidth() * scaleFactor / 2 - 8 * scaleFactor),
                    MainPanel.gameSize.height - (int) (25 * scaleFactor));
        }

//        SCOREBOARD

        g2d.drawImage(localPlayer.scoreboardICON, -70, -100, 192, 192, null);
        g2d.setFont(Menu.googleExo2.deriveFont(25F * scaleFactor));
        g2d.drawString(String.valueOf(localPlayer.scoreboardKills),75,  45);
        int i = 1;
        for (OnlinePlayer onlinePlayer : OnlinePlayer.listOfAllConnectedOnlinePLayers) {
            g2d.drawImage(onlinePlayer.scoreboardICON, -70, 100 * i - 100, 192, 192, null);
            g2d.setFont(Menu.googleExo2.deriveFont(25F * scaleFactor));
            g2d.drawString(String.valueOf(onlinePlayer.scoreboardKills),75,  100 * i + 45);
            i++;
        }


//        WHEN PLAYER IS DEAD
        if (localPlayer.isPlayerDead) {
            String playerDead = "Respawn in: " + (localPlayer.RespawnCurrentCooldown / 1000 + 1);
            g2d.setFont(Menu.googleExo2.deriveFont(45F * scaleFactor));
            java.awt.FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(playerDead);
            int textHeight = fontMetrics.getHeight();
            int x = (MainPanel.gameSize.width - textWidth) / 2;
            int y = (MainPanel.gameSize.height - textHeight) / 2 + fontMetrics.getAscent();
            g2d.setColor(new Color(255, 0, 0, 42));
            g2d.fillRect(0, 0, MainPanel.gameSize.width, MainPanel.gameSize.height);
            g2d.setColor(new Color(0, 0, 0, 242));
            g2d.drawString(playerDead, x, y);

        }


//        PAUSE

        if (EnumContainer.AllScenes.Current_Scene == EnumContainer.AllScenes.PAUSE) {

            g2d.setColor(new Color(231, 231, 231, 216));
            g2d.fillRoundRect(pause.pausePanel.getX() - 35, pause.pausePanel.getY() - 35,
                    pause.pausePanel.getWidth() + 70, pause.pausePanel.getHeight() + 70, 10, 10);
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
                if (!localPlayer.isPlayerDead && localPlayer.localPlayerHitbox.intersects(spell.spell01Hitbox)
                        && Client.ClientID != spell.spellCasterClientID) {
                    if (localPlayer.healthbar.currentHealth > 0) {
                        localPlayer.healthbar.currentHealth = localPlayer.healthbar.currentHealth - 50;
                        if (localPlayer.healthbar.currentHealth <= 0) {
                            OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
                                    .filter(onlinePlayer -> onlinePlayer.onlinePlayerID == spell.spellCasterClientID)
                                    .findFirst()
                                    .ifPresent(onlinePlayer -> onlinePlayer.scoreboardKills++);
                        }
                    }
                    spell.playerGotHit = true;
                }

//                    Online player
                OnlinePlayer.listOfAllConnectedOnlinePLayers
                        .stream()
                        .filter(onlinePlayer -> !onlinePlayer.isPlayerDead &&
                                onlinePlayer.onlinePlayerHitbox.intersects(spell.spell01Hitbox) && onlinePlayer.onlinePlayerID != spell.spellCasterClientID)
                        .forEach(onlinePlayerFiltered -> {
                            if (onlinePlayerFiltered.healthbar.currentHealth > 0) {
                                onlinePlayerFiltered.healthbar.currentHealth = onlinePlayerFiltered.healthbar.currentHealth - 50;
                                if (onlinePlayerFiltered.healthbar.currentHealth <= 0) {
                                    if (spell.spellCasterClientID == Client.ClientID) {
                                        localPlayer.scoreboardKills++;
                                    } else {
                                        OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
                                                .filter(onlinePlayer -> onlinePlayer.onlinePlayerID == spell.spellCasterClientID)
                                                .findFirst()
                                                .ifPresent(onlinePlayer -> onlinePlayer.scoreboardKills++);
                                    }
                                }
                            }
                            spell.playerGotHit = true;
                        });


            }
        });


    }

}

