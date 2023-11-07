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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalPlayer {

    public BufferedImage allLocalPlayerSprites;

    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[6];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[6];

    public BufferedImage[] playerSpriteMOVE_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteMOVE_RIGHT = new BufferedImage[8];

    public BufferedImage[] playerSpriteDEATH_RIGHT = new BufferedImage[10];
    public BufferedImage[] playerSpriteDEATH_LEFT = new BufferedImage[10];


    public BufferedImage[] playerSpriteTAKE_DMG_RIGHT = new BufferedImage[3];
    public BufferedImage[] playerSpriteTAKE_DMG_LEFT = new BufferedImage[3];

    public BufferedImage[] playerSpriteROLL_RIGHT = new BufferedImage[5];
    public BufferedImage[] playerSpriteROLL_LEFT = new BufferedImage[5];


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
    int playerMoveSpeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;

    private Graphics2D g2d;
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
        getPlayerSprites2Directional(localPlayerChampion);
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

            playerPosXWorld += (playerMoveSpeed * normalizedVectorX);
            playerPosYWorld += (playerMoveSpeed * normalizedVectorY);
            distanceToTravel -= (float) (playerMoveSpeed * Math.sqrt(normalizedVectorX * normalizedVectorX + normalizedVectorY * normalizedVectorY));
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

    public void getPlayerSprites2Directional(EnumContainer.AllPlayableChampions localPlayerChampion) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource;

        if (localPlayerChampion == EnumContainer.AllPlayableChampions.DON_OHL) {

            resource = classLoader.getResource("NewAssets/FullChar/Char1/withHands");
            File folder = new File(Objects.requireNonNull(resource).getFile());
            File[] spriteImages = folder.listFiles();

            String fileNameTemp = null;
            for (int i = 0, j = 0; i < Objects.requireNonNull(spriteImages).length; i++, j++) {
                if (i != 0) {
                    if (!spriteImages[i].getName().startsWith(String.valueOf(fileNameTemp.charAt(0)))) {
                        j = 0;
                    }
                }
                fileNameTemp = spriteImages[i].getName();
                if (spriteImages[i].getName().startsWith("death")) {
                    try {
                        playerSpriteDEATH_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteDEATH_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (spriteImages[i].getName().startsWith("idle")) {
                    try {
                        playerSpriteIDLE_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteIDLE_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (spriteImages[i].getName().startsWith("roll")) {
                    try {
                        playerSpriteROLL_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteROLL_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (spriteImages[i].getName().startsWith("walk")) {
                    try {
                        playerSpriteMOVE_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteMOVE_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (spriteImages[i].getName().startsWith("hit")) {
                    try {
                        playerSpriteTAKE_DMG_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteTAKE_DMG_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

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
        }
    }

    private BufferedImage flipImageHorizontally(File imageFile) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }


    public BufferedImage[] playerSpriteController() {
        switch (Current_Player_State) {
            case IDLE_LEFT -> {
                return playerSpriteIDLE_LEFT;
            }
            case IDLE_RIGHT -> {
                return playerSpriteIDLE_RIGHT;
            }

            case MOVING_LEFT -> {
                return playerSpriteMOVE_LEFT;
            }
            case MOVING_RIGHT -> {
                return playerSpriteMOVE_RIGHT;
            }

            default -> {
                return null;
            }
        }
    }

    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerSpriteController() == playerSpriteIDLE_LEFT ||
                    playerSpriteController() == playerSpriteIDLE_RIGHT) {

                if (animationIndexIdle < 1) animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (playerSpriteController() == playerSpriteMOVE_LEFT ||
                    playerSpriteController() == playerSpriteMOVE_RIGHT) {
                if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.DON_OHL)) {
                    if (animationIndexMoving < 3) animationIndexMoving++;
                    else animationIndexMoving = 0;
                } else if (localPlayerChampion.equals(EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE)) {
                    if (animationIndexMoving < 7) animationIndexMoving++;
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
            super(playerPosXWorld, playerPosYWorld, playerSpriteMOVE_RIGHT[0].getWidth(), playerSpriteMOVE_RIGHT[0].getHeight());
        }

    }

}

