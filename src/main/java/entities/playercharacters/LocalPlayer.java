package entities.playercharacters;

import entities.Healthbar;
import entities.spells.basicspells.Spell01;
import inputs.PlayerKeyboardInputs;
import main.EnumContainer;
import main.EnumContainer.ServerClientConnectionCopyObjects;
import networking.Client;
import networking.PacketManager;
import scenes.playing.Camera;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalPlayer {

    public BufferedImage allLocalPlayerSprites;

    public BufferedImage[] playerSpriteIDLE_UP = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_DOWN = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[1];

    public BufferedImage[] playerSpriteIDLE_UP_LEFT = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_UP_RIGHT = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_DOWN_LEFT = new BufferedImage[1];
    public BufferedImage[] playerSpriteIDLE_DOWN_RIGHT = new BufferedImage[1];


    public BufferedImage[] playerSpriteUP = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN = new BufferedImage[8];
    public BufferedImage[] playerSpriteLEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteRIGHT = new BufferedImage[8];

    public BufferedImage[] playerSpriteUP_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteUP_RIGHT = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN_RIGHT = new BufferedImage[8];

    public BufferedImage[] currentPlayerSprite;

    public EnumContainer.AllPlayerStates Current_Player_State;
    public EnumContainer.AllPlayableChampions localPlayerChampion;

    public Healthbar healthbar;
    public LocalPlayerHitbox localPlayerHitbox;

    public static float playerPosXWorld;
    public static float playerPosYWorld;
    public static float playerPosXScreen, playerPosYScreen;
    public int playerFeetX, playerFeetY;
    public int mouseClickXPos;
    public int mouseClickYPos;
    public float normalizedVectorX;
    public float normalizedVectorY;
    int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;

    private int animationTick;
    private final int animationSpeed = 15;
    public int animationIndexMoving, animationIndexIdle;

    //    This player Spells
    public int counterOfThisPlayerQSpells;

    public List<Spell01> listOfAllActive_Q_Spells = new ArrayList<>();

    private long lastQSpellCastTime;


    public LocalPlayer() {
        this.Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN;
        currentPlayerSprite = playerSpriteController();
//        getPlayerSprites4Directional("/Player1.png");


    }

    private void setPlayerHealthBar() {
        switch (localPlayerChampion) {

            case DON_OHL -> {
                healthbar = new Healthbar(4000, playerPosXScreen, playerPosYScreen);
            }
            case BIG_HAIRY_SWEATY_DUDE -> {
                healthbar = new Healthbar(200, playerPosXScreen, playerPosYScreen);

            }
        }
    }

    public void setPlayerChampion(EnumContainer.AllPlayableChampions champion) {
        localPlayerChampion = champion;
        getPlayerSprites8Directional(localPlayerChampion);
        setPLayerFeetPos();
        setPlayerHealthBar();
        localPlayerHitbox = new LocalPlayerHitbox();
    }

    public void setPLayerFeetPos() {
        if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.DON_OHL)) {
            playerFeetX = 36;
            playerFeetY = 68;
        } else if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE)) {
            playerFeetX = 64;
            playerFeetY = 115;
        }
    }

    public void getVectorForPlayerMovement(MouseEvent e) {
        setPlayerMovementStartingPosition(LocalPlayer.playerPosXWorld, LocalPlayer.playerPosYWorld);
        mouseClickXPos = e.getX() + Camera.cameraPosX;
        mouseClickYPos = e.getY() + Camera.cameraPosY;

        float vectorX = mouseClickXPos - (LocalPlayer.playerPosXWorld + playerFeetX);
        float vectorY = mouseClickYPos - (LocalPlayer.playerPosYWorld + playerFeetY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        distanceToTravel = magnitude;


        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    public void moveController() {
//
        if (distanceToTravel > 0) {

            playerPosXWorld += (playerMovespeed * normalizedVectorX);
            playerPosYWorld += (playerMovespeed * normalizedVectorY);
            distanceToTravel -= (float) (playerMovespeed * Math.sqrt(normalizedVectorX * normalizedVectorX + normalizedVectorY * normalizedVectorY));
            isPlayerMoving = true;
            setCurrent_Player_State();
        } else {
            isPlayerMoving = false;
            setCurrent_Player_State();
        }

    }

    public void setCurrent_Player_State() {
// double angle = atan2(y2 - y1, x2 - x1) * 180 / PI;".
        double movementAngle = Math.atan2(mouseClickYPos - playerMovementStartingPosY, mouseClickXPos - playerMovementStartingPosX);
        double angleDegrees = Math.toDegrees(movementAngle);
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }
        if (isPlayerMoving) {
            if (angleDegrees >= 22.5 && angleDegrees < 67.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN_RIGHT;

            } else if (angleDegrees >= 67.5 && angleDegrees < 112.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN;

            } else if (angleDegrees >= 112.5 && angleDegrees < 157.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_DOWN_LEFT;

            } else if (angleDegrees >= 157.5 && angleDegrees < 202.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_LEFT;

            } else if (angleDegrees >= 202.5 && angleDegrees < 247.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP_LEFT;

            } else if (angleDegrees >= 247.5 && angleDegrees < 292.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP;

            } else if (angleDegrees >= 292.5 && angleDegrees < 337.5) {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_UP_RIGHT;

            } else {
                Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
            }
        } else {
            switch (Current_Player_State) {

                case MOVING_UP -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP;
                }
                case MOVING_DOWN -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN;
                }
                case MOVING_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_LEFT;
                }
                case MOVING_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
                }
                case MOVING_UP_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP_LEFT;
                }
                case MOVING_UP_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_UP_RIGHT;
                }
                case MOVING_DOWN_LEFT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN_LEFT;
                }
                case MOVING_DOWN_RIGHT -> {
                    Current_Player_State = EnumContainer.AllPlayerStates.IDLE_DOWN_RIGHT;
                }
            }

        }

    }

    public void setPlayerMovementStartingPosition(float playerPosXWorld, float playerPosYWorld) {
        this.playerMovementStartingPosX = playerPosXWorld + playerFeetX;
        this.playerMovementStartingPosY = playerPosYWorld + playerFeetY;
    }

    public void updatePlayerPositionOnScreenAndPlayerHitbox() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;
        updatePlayerHitboxWorldAndPosOnScreen();

    }

    public void getPlayerSprites8Directional(EnumContainer.AllPlayableChampions localPlayerChampion) {

        int spriteSize = 0;
        int spriteXpos = 0;
        int numberOfSpritesInRow = 0;
        if (localPlayerChampion == EnumContainer.AllPlayableChampions.DON_OHL) {
            InputStream inputStream = getClass().getResourceAsStream("/DON_OHL.png");
            try {
                allLocalPlayerSprites = ImageIO.read(Objects.requireNonNull(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            spriteSize = 72;
            numberOfSpritesInRow = 4;


//        Assigning moving sprites for all directions
            for (int i = 0; i < numberOfSpritesInRow; i++) {
                playerSpriteUP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
                playerSpriteUP_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
                playerSpriteUP_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
                playerSpriteLEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
                playerSpriteRIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
                playerSpriteDOWN_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
                playerSpriteDOWN_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
                playerSpriteDOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);

                spriteXpos += spriteSize;
            }

            spriteXpos = 0;
//       Assinging idle sprites for all directions
            for (int i = 0; i < 1; i++) {
                playerSpriteIDLE_UP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
                playerSpriteIDLE_UP_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
                playerSpriteIDLE_UP_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
                playerSpriteIDLE_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
                playerSpriteIDLE_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
                playerSpriteIDLE_DOWN_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
                playerSpriteIDLE_DOWN_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
                playerSpriteIDLE_DOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);


                spriteXpos += spriteSize;
            }

        } else if (localPlayerChampion == EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE) {
            InputStream inputStream = getClass().getResourceAsStream("/WIKING_RUN.png");
            try {
                allLocalPlayerSprites = ImageIO.read(Objects.requireNonNull(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            spriteSize = 128;
            numberOfSpritesInRow = 8;
//        Assigning moving sprites for all directions
            for (int i = 0; i < numberOfSpritesInRow; i++) {
                playerSpriteDOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
                playerSpriteLEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
                playerSpriteRIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
                playerSpriteUP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
                playerSpriteDOWN_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
                playerSpriteDOWN_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
                playerSpriteUP_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
                playerSpriteUP_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);

                spriteXpos += spriteSize;
            }

            spriteXpos = 0;
//       Assinging idle sprites for all directions
            for (int i = 0; i < 1; i++) {
                playerSpriteIDLE_DOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
                playerSpriteIDLE_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
                playerSpriteIDLE_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
                playerSpriteIDLE_UP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
                playerSpriteIDLE_DOWN_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
                playerSpriteIDLE_DOWN_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
                playerSpriteIDLE_UP_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
                playerSpriteIDLE_UP_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);

                spriteXpos += spriteSize;
            }
        }
    }

    public void getPlayerSprites4Directional(String classpath) {
        InputStream inputStream = getClass().getResourceAsStream(classpath);
        try {
            allLocalPlayerSprites = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int spriteSize = 72;
        int spriteXpos = 0;

//        Assigning moving sprites for all directions
        for (int i = 0; i < 4; i++) {
            playerSpriteDOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteLEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 72, spriteSize, spriteSize);
            playerSpriteRIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 144, spriteSize, spriteSize);
            playerSpriteUP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 216, spriteSize, spriteSize);
            spriteXpos += 72;
        }

        spriteXpos = 0;
//       Assinging idle sprites for all directions
        for (int i = 0; i < 2; i++) {
            playerSpriteIDLE_DOWN[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteIDLE_LEFT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 72, spriteSize, spriteSize);
            playerSpriteIDLE_RIGHT[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 144, spriteSize, spriteSize);
            playerSpriteIDLE_UP[i] = allLocalPlayerSprites.getSubimage(spriteXpos, 216, spriteSize, spriteSize);

            spriteXpos += 144;
        }


    }

    public BufferedImage[] playerSpriteController() {
        switch (Current_Player_State) {
            case IDLE_UP -> {
                return playerSpriteIDLE_UP;
            }
            case IDLE_DOWN -> {
                return playerSpriteIDLE_DOWN;
            }
            case IDLE_LEFT -> {
                return playerSpriteIDLE_LEFT;
            }
            case IDLE_RIGHT -> {
                return playerSpriteIDLE_RIGHT;
            }
            case IDLE_UP_LEFT -> {
                return playerSpriteIDLE_UP_LEFT;
            }
            case IDLE_UP_RIGHT -> {
                return playerSpriteIDLE_UP_RIGHT;
            }
            case IDLE_DOWN_LEFT -> {
                return playerSpriteIDLE_DOWN_LEFT;
            }
            case IDLE_DOWN_RIGHT -> {
                return playerSpriteIDLE_DOWN_RIGHT;
            }
            case MOVING_UP -> {
                return playerSpriteUP;
            }
            case MOVING_DOWN -> {
                return playerSpriteDOWN;
            }
            case MOVING_LEFT -> {
                return playerSpriteLEFT;
            }
            case MOVING_RIGHT -> {
                return playerSpriteRIGHT;
            }
            case MOVING_UP_LEFT -> {
                return playerSpriteUP_LEFT;
            }
            case MOVING_UP_RIGHT -> {
                return playerSpriteUP_RIGHT;
            }
            case MOVING_DOWN_LEFT -> {
                return playerSpriteDOWN_LEFT;
            }
            case MOVING_DOWN_RIGHT -> {
                return playerSpriteDOWN_RIGHT;
            }
            default -> {
                return null;
            }
        }
    }

    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerSpriteController() == playerSpriteIDLE_UP |
                    playerSpriteController() == playerSpriteIDLE_DOWN |
                    playerSpriteController() == playerSpriteIDLE_LEFT |
                    playerSpriteController() == playerSpriteIDLE_RIGHT |
                    playerSpriteController() == playerSpriteIDLE_UP_LEFT |
                    playerSpriteController() == playerSpriteIDLE_UP_RIGHT |
                    playerSpriteController() == playerSpriteIDLE_DOWN_LEFT |
                    playerSpriteController() == playerSpriteIDLE_DOWN_RIGHT) {

                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (playerSpriteController() == playerSpriteUP |
                    playerSpriteController() == playerSpriteDOWN |
                    playerSpriteController() == playerSpriteLEFT |
                    playerSpriteController() == playerSpriteRIGHT |
                    playerSpriteController() == playerSpriteUP_LEFT |
                    playerSpriteController() == playerSpriteUP_RIGHT |
                    playerSpriteController() == playerSpriteDOWN_LEFT |
                    playerSpriteController() == playerSpriteDOWN_RIGHT) {
                if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.DON_OHL)) {
                    if (animationIndexMoving < 3)
                        animationIndexMoving++;
                    else animationIndexMoving = 0;
                } else if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE)) {
                    if (animationIndexMoving < 7)
                        animationIndexMoving++;
                    else animationIndexMoving = 0;
                }
            }
            animationTick = 0;
        }
    }

    public void spellCastController() {

        ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[0] = PlayerKeyboardInputs.Q_Pressed;
        ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[1] = PlayerKeyboardInputs.W_Pressed;
        ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[2] = PlayerKeyboardInputs.E_Pressed;
        ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[3] = PlayerKeyboardInputs.R_Pressed;

        boolean shouldWeSendPacketToServer = false;

        if (ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[0] && isSpellQoffCooldown()) {
            new Spell01(this);
            Spell01.QSpellCreatedOnThisMousePress = true;
            lastQSpellCastTime = System.currentTimeMillis();
            shouldWeSendPacketToServer = true;
        }
        if (ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[1]) {
            new Spell01(this);
            Spell01.QSpellCreatedOnThisMousePress = true;
            shouldWeSendPacketToServer = true;
        }
        if (ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[2]) {
            new Spell01(this);
            Spell01.QSpellCreatedOnThisMousePress = true;
            shouldWeSendPacketToServer = true;
        }
        if (ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests[3]) {
            new Spell01(this);
            Spell01.QSpellCreatedOnThisMousePress = true;
            shouldWeSendPacketToServer = true;
        }
        if (shouldWeSendPacketToServer) {
            try {
                Client.socket.send(PacketManager.spellRequestPacket());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isSpellQoffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastQSpellCastTime >= Spell01.SPELL01COOLDOWN;

    }

    public void updatePlayerHitboxWorldAndPosOnScreen() {
        localPlayerHitbox.x = playerPosXWorld;
        localPlayerHitbox.y = playerPosYWorld;
        localPlayerHitbox.playerHitboxPosXScreen = playerPosXScreen;
        localPlayerHitbox.playerHitboxPosYScreen = playerPosYScreen;

    }

    public class LocalPlayerHitbox extends Rectangle2D.Float {

        public float playerHitboxPosXScreen, playerHitboxPosYScreen;
        LocalPlayerHitbox() {
            super( playerPosXWorld,  playerPosYWorld,
                    playerSpriteDOWN[0].getWidth(),playerSpriteDOWN[0].getHeight());
        }

    }

}

