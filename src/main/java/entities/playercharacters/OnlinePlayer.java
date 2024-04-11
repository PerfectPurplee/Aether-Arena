package entities.playercharacters;

import entities.Healthbar;
import main.AssetLoader;
import main.Camera;
import main.EnumContainer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnlinePlayer {


    public BufferedImage[] currentPlayerSpriteOnlinePlayer;
    public BufferedImage scoreboardICON;


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

    public BufferedImage[] playerSpriteCAST_SPELL_LEFT = new BufferedImage[5];
    public BufferedImage[] playerSpriteCAST_SPELL_RIGHT = new BufferedImage[5];



    public EnumContainer.AllPlayerStates Current_Player_State_Online_Player;
    public EnumContainer.AllPlayableChampions onlinePlayerChampion;

    public Healthbar healthbar;
    public OnlinePlayerHitbox onlinePlayerHitbox;

    public float playerPosXWorld, playerPosYWorld;
    public float playerPosXScreen, playerPosYScreen;

    private Graphics2D g2d;
    private int animationTick, animationSpeed = 15;
    public int animationIndexMoving, animationIndexIdle, animationIndexCasting, animationIndexDashing, animationIndexDeath;

    public final int onlinePlayerID;
    public boolean isPlayerMoving;
    public boolean isPlayerStateLocked;
    public boolean deathAnimationFinished;

    public static CopyOnWriteArrayList<OnlinePlayer> listOfAllConnectedOnlinePLayers = new CopyOnWriteArrayList<>();
    //    Assigned in a GameEngine
    public static AssetLoader assetLoader;

    private final long RESPAWN_COOLDOWN = 5000;  // in milliseconds
    public boolean isPlayerDead;

    public int scoreboardKills;

    public OnlinePlayer(int onlinePlayerID) {
        scoreboardKills = 0;
        isPlayerDead = false;
        isPlayerStateLocked = false;
        onlinePlayerChampion = EnumContainer.ServerClientConnectionCopyObjects.PLayer_Champion_Shared;
        getPlayerSprites2Directional(onlinePlayerChampion);
        this.onlinePlayerID = onlinePlayerID;
        this.Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.MOVING_RIGHT;
        currentPlayerSpriteOnlinePlayer = setCurrentOnlinePlayerSprite();
        onlinePlayerHitbox = new OnlinePlayerHitbox();
        setPlayerHealthBar();
        setScoreboardICON();


        listOfAllConnectedOnlinePLayers.add(this);

    }

    public void setScoreboardICON() {
        switch (onlinePlayerChampion) {

            case BLUE_HAIR_DUDE -> scoreboardICON = assetLoader.scoreboardICONS[0];
            case PINK_HAIR_GIRL -> scoreboardICON = assetLoader.scoreboardICONS[1];
            case BLOND_MOHAWK_DUDE -> scoreboardICON = assetLoader.scoreboardICONS[2];
            case CAPE_BALDY_DUDE -> scoreboardICON = assetLoader.scoreboardICONS[3];
        }
    }

    private void setPlayerHealthBar() {

        healthbar = new Healthbar(500, onlinePlayerHitbox.playerHitboxPosXScreen, onlinePlayerHitbox.playerHitboxPosYScreen);

//        PROTOTYPE CODE IF YOU WANT DIFFERENT HEALTH CAPACITY FOR DIFFERENT CHAMPIONS
//        switch (localPlayerChampion) {
//
//            case BLUE_HAIR_DUDE -> {
//            }
//            case PINK_HAIR_GIRL -> {
//                healthbar = new Healthbar(3000, playerPosXScreen, playerPosYScreen);
//
//            }
//        }

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

    public void getPlayerSprites2Directional(EnumContainer.AllPlayableChampions onlinePlayerChampion) {
        int indexOFChampionInAssetLoader;

        switch (onlinePlayerChampion) {
            case BLUE_HAIR_DUDE -> indexOFChampionInAssetLoader = 0;
            case PINK_HAIR_GIRL -> indexOFChampionInAssetLoader = 1;
            case BLOND_MOHAWK_DUDE -> indexOFChampionInAssetLoader = 2;
            case CAPE_BALDY_DUDE -> indexOFChampionInAssetLoader = 3;
            default -> indexOFChampionInAssetLoader = 99;
        }
        playerSpriteDEATH_RIGHT = assetLoader.playerSpriteDEATH_RIGHT[indexOFChampionInAssetLoader];
        playerSpriteDEATH_LEFT = assetLoader.playerSpriteDEATH_LEFT[indexOFChampionInAssetLoader];

        playerSpriteIDLE_RIGHT = assetLoader.playerSpriteIDLE_RIGHT[indexOFChampionInAssetLoader];
        playerSpriteIDLE_LEFT = assetLoader.playerSpriteIDLE_LEFT[indexOFChampionInAssetLoader];

        playerSpriteROLL_RIGHT = assetLoader.playerSpriteROLL_RIGHT[indexOFChampionInAssetLoader];
        playerSpriteROLL_LEFT = assetLoader.playerSpriteROLL_LEFT[indexOFChampionInAssetLoader];

        playerSpriteMOVE_RIGHT = assetLoader.playerSpriteMOVE_RIGHT[indexOFChampionInAssetLoader];
        playerSpriteMOVE_LEFT = assetLoader.playerSpriteMOVE_LEFT[indexOFChampionInAssetLoader];

        playerSpriteTAKE_DMG_RIGHT = assetLoader.playerSpriteTAKE_DMG_RIGHT[indexOFChampionInAssetLoader];
        playerSpriteTAKE_DMG_LEFT = assetLoader.playerSpriteTAKE_DMG_LEFT[indexOFChampionInAssetLoader];

        playerSpriteCAST_SPELL_LEFT = assetLoader.playerSpriteCAST_SPELL_LEFT[indexOFChampionInAssetLoader];
        playerSpriteCAST_SPELL_RIGHT = assetLoader.playerSpriteCAST_SPELL_RIGHT[indexOFChampionInAssetLoader];
    }

    public void updatePlayerPositionOnScreenAndHitbox() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;

    }


    public BufferedImage[] setCurrentOnlinePlayerSprite() {
        switch (Current_Player_State_Online_Player) {
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
            case CASTING_SPELL_LEFT -> {
                return playerSpriteCAST_SPELL_LEFT;
            }
            case CASTING_SPELL_RIGHT -> {
                return playerSpriteCAST_SPELL_RIGHT;
            }
            case DASHING_LEFT -> {
                return playerSpriteROLL_LEFT;
            }
            case DASHING_RIGHT -> {
                return playerSpriteROLL_RIGHT;
            }

            case DEATH_LEFT -> {
                return playerSpriteDEATH_LEFT;
            }
            case DEATH_RIGHT -> {
                return playerSpriteDEATH_RIGHT;
            }
            default -> {
                return null;
            }
        }
    }

    //    Used only for predicting info from server
    private void setCurrentPlayerStateOnlinePlayer() {
        switch (Current_Player_State_Online_Player) {

            case CASTING_SPELL_LEFT -> {
                Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.IDLE_LEFT;

            }
            case CASTING_SPELL_RIGHT -> {
                Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.IDLE_RIGHT;
            }

        }
    }


    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_RIGHT) {
                if (animationIndexIdle < 5) animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_RIGHT) {
                if (animationIndexMoving < 7) animationIndexMoving++;
                else animationIndexMoving = 0;
            } else if (currentPlayerSpriteOnlinePlayer == playerSpriteCAST_SPELL_RIGHT || currentPlayerSpriteOnlinePlayer == playerSpriteCAST_SPELL_LEFT) {
                if (animationIndexCasting < 4) animationIndexCasting++;
                else {
                    isPlayerStateLocked = false;
                    setCurrentPlayerStateOnlinePlayer();
                    animationIndexCasting = 0;
                }
            } else if (currentPlayerSpriteOnlinePlayer == playerSpriteROLL_RIGHT || currentPlayerSpriteOnlinePlayer == playerSpriteROLL_LEFT) {
                if (animationIndexDashing < 3) animationIndexDashing++;
                else {
                    isPlayerStateLocked = false;
                    animationIndexDashing = 0;
                }
            } else if ((currentPlayerSpriteOnlinePlayer == playerSpriteDEATH_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteDEATH_RIGHT) && !deathAnimationFinished) {
                if (animationIndexDeath < 8) animationIndexDeath++;
                else {
                    animationIndexDeath = 0;
                    deathAnimationFinished = true;

                }

            }
            animationTick = 0;
        }
    }

    // On death and on revive
    public void checkIsPlayerDead() {
        if (healthbar.currentHealth <= 0) {
            isPlayerDead = true;
        }
        if (healthbar.currentHealth > 0)
            isPlayerDead = false;
    }

    public int currentIndexerForAnimation() {
        if (currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_RIGHT)
            return animationIndexIdle;
        else if (currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_RIGHT)
            return animationIndexMoving;
        else if (currentPlayerSpriteOnlinePlayer == playerSpriteCAST_SPELL_RIGHT || currentPlayerSpriteOnlinePlayer == playerSpriteCAST_SPELL_LEFT)
            return animationIndexCasting;
        else if (currentPlayerSpriteOnlinePlayer == playerSpriteROLL_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteROLL_RIGHT)
            return animationIndexDashing;
        else if (currentPlayerSpriteOnlinePlayer == playerSpriteDEATH_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteDEATH_RIGHT)
            return animationIndexDeath;
        else return 0;
    }

    public void updatePlayerHitboxWorldAndPosOnScreen() {
        onlinePlayerHitbox.x = playerPosXWorld + hitboxOffsetX;
        onlinePlayerHitbox.y = playerPosYWorld + hitboxOffsetYAbovePlayerSprite;
        onlinePlayerHitbox.playerHitboxPosXScreen = playerPosXScreen + hitboxOffsetX;
        onlinePlayerHitbox.playerHitboxPosYScreen = playerPosYScreen + hitboxOffsetYAbovePlayerSprite;
    }

    public void updateHealthBarCurrentHealthAndPositionOnScreen() {
        healthbar.currentHealthToDraw = healthbar.setSizeOfCurrentHealthToDraw();
        healthbar.healthbarPositionOnScreenX = (int) (onlinePlayerHitbox.playerHitboxPosXScreen);
        healthbar.healthbarPositionOnScreenY = (int) (onlinePlayerHitbox.playerHitboxPosYScreen - healthbar.offsetY);
    }

    private final int hitboxOffsetX = 90;
    private final int hitboxOffsetYAbovePlayerSprite = 130;

    public class OnlinePlayerHitbox extends Rectangle2D.Float {

        public float playerHitboxPosXScreen, playerHitboxPosYScreen;

        OnlinePlayerHitbox() {
            super(playerPosXWorld + hitboxOffsetX, playerPosYWorld + hitboxOffsetYAbovePlayerSprite, (playerSpriteMOVE_RIGHT[0].getWidth() - hitboxOffsetX * 2), playerSpriteMOVE_RIGHT[0].getHeight() - (hitboxOffsetYAbovePlayerSprite + 25));
        }

    }
}
