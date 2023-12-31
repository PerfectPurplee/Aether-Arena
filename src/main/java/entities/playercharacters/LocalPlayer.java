package entities.playercharacters;

import entities.Healthbar;
import entities.spells.basicspells.QSpell;
import entities.spells.basicspells.Ultimate;
import inputs.PlayerKeyboardInputs;
import main.AssetLoader;
import main.EnumContainer;
import main.GameEngine;
import main.UserInterface;
import networking.Client;
import networking.PacketManager;
import scenes.playing.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LocalPlayer {

    public BufferedImage[] currentPlayerSprite;

    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[6];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[6];

    public BufferedImage[] playerSpriteMOVE_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteMOVE_RIGHT = new BufferedImage[8];

    public BufferedImage[] playerSpriteDEATH_RIGHT;
    public BufferedImage[] playerSpriteDEATH_LEFT = new BufferedImage[10];

    public BufferedImage[] playerSpriteTAKE_DMG_RIGHT = new BufferedImage[3];
    public BufferedImage[] playerSpriteTAKE_DMG_LEFT = new BufferedImage[3];

    public BufferedImage[] playerSpriteROLL_RIGHT = new BufferedImage[5];
    public BufferedImage[] playerSpriteROLL_LEFT = new BufferedImage[5];

    public BufferedImage[] playerSpriteCAST_SPELL_LEFT = new BufferedImage[5];
    public BufferedImage[] playerSpriteCAST_SPELL_RIGHT = new BufferedImage[5];

    public EnumContainer.AllPlayerStates Current_Player_State;
    public EnumContainer.AllPlayableChampions localPlayerChampion;

    public Healthbar healthbar;
    public LocalPlayerHitbox localPlayerHitbox;
    public UserInterface userInterface;

    public static float playerPosXWorld;
    public static float playerPosYWorld;
    public static float playerPosXScreen, playerPosYScreen;

    public int playerFeetX, playerFeetY;
    public int mouseClickXPos;
    public int mouseClickYPos;
    public float normalizedVectorX;
    public float normalizedVectorY;

    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;
    public boolean isPlayerStateLocked;


    private Graphics2D g2d;
    private AssetLoader assetLoader;
    private int playerMoveSpeed = 2;

    private int animationTick;
    private final int animationSpeed = 15;
    public int animationIndexIdle, animationIndexMoving, animationIndexCasting, animationIndexDashing;

    //    This player Spells
    private final long DASH_COOLDOWN = 3000;   //in milliseconds
    public int counterOfThisPlayerQSpells;
    private long lastQSpellCastTime;
    private long lastWSpellCastTime;
    private long lastESpellCastTime;
    private long lastRSpellCastTime;
    private long lastDashCastTime;

    public long QCurrentCooldown;
    public long WCurrentCooldown;
    public long ECurrentCooldown;
    public long RCurrentCooldown;
    public int DashCurrentCooldown;


    public LocalPlayer(AssetLoader assetLoader) {
        isPlayerStateLocked = false;
        this.assetLoader = assetLoader;
        this.Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
        currentPlayerSprite = setCurrentPlayerSprite();

    }

    private void setPlayerHealthBar() {

        healthbar = new Healthbar(
                4000, localPlayerHitbox.playerHitboxPosXScreen,
                localPlayerHitbox.playerHitboxPosYScreen);

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

    public void setPlayerChampion(EnumContainer.AllPlayableChampions champion) {
        localPlayerChampion = champion;
        getPlayerSprites2Directional(localPlayerChampion);
        setPLayerFeetPos();
        localPlayerHitbox = new LocalPlayerHitbox();
        setPlayerHealthBar();

    }

    public void setPLayerFeetPos() {
        playerFeetX = 128;
        playerFeetY = 220;
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
        if (distanceToTravel > 2) {

            playerPosXWorld += (playerMoveSpeed * normalizedVectorX);
            playerPosYWorld += (playerMoveSpeed * normalizedVectorY);
            distanceToTravel -= (float) (playerMoveSpeed * Math.sqrt
                    (normalizedVectorX * normalizedVectorX + normalizedVectorY * normalizedVectorY));
            isPlayerMoving = true;
            setCurrent_Player_State();
        } else {
            isPlayerMoving = false;
            setCurrent_Player_State();
        }

    }

    public void setCurrent_Player_State() {

        if (!isPlayerStateLocked) {
            if (isPlayerMoving) {
                if (mouseClickXPos < playerPosXWorld + playerFeetX) {
                    Current_Player_State = EnumContainer.AllPlayerStates.MOVING_LEFT;
                } else {
                    Current_Player_State = EnumContainer.AllPlayerStates.MOVING_RIGHT;
                }
            } else {
                switch (Current_Player_State) {

                    case MOVING_LEFT, CASTING_SPELL_LEFT, DASHING_LEFT -> {
                        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_LEFT;
                    }
                    case MOVING_RIGHT, CASTING_SPELL_RIGHT, DASHING_RIGHT -> {
                        Current_Player_State = EnumContainer.AllPlayerStates.IDLE_RIGHT;
                    }

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


    private void getPlayerSprites2Directional(EnumContainer.AllPlayableChampions localPlayerChampion) {

        int indexOFChampionInAssetLoader;

        switch (localPlayerChampion) {
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

    private BufferedImage scaleImage(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            double scale = 0.125;
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);

            AffineTransformOp transform = new AffineTransformOp(
                    AffineTransform.getScaleInstance(scale, scale),
                    AffineTransformOp.TYPE_BICUBIC);
            return transform.filter(
                    originalImage,
                    new BufferedImage(newWidth, newHeight, originalImage.getType()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private BufferedImage flipImageHorizontally(File imageFile) {
        BufferedImage image = scaleImage(imageFile);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }


    public BufferedImage[] setCurrentPlayerSprite() {
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
            default -> {
                return null;
            }
        }
    }

    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (currentPlayerSprite == playerSpriteIDLE_LEFT || currentPlayerSprite == playerSpriteIDLE_RIGHT) {
                if (animationIndexIdle < 5) animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (currentPlayerSprite == playerSpriteMOVE_LEFT || currentPlayerSprite == playerSpriteMOVE_RIGHT) {
                if (animationIndexMoving < 7) animationIndexMoving++;
                else animationIndexMoving = 0;
            } else if (currentPlayerSprite == playerSpriteCAST_SPELL_RIGHT || currentPlayerSprite == playerSpriteCAST_SPELL_LEFT) {
                if (animationIndexCasting < 4) animationIndexCasting++;
                else {
                    isPlayerStateLocked = false;
                    setCurrent_Player_State();
                    animationIndexCasting = 0;
                }
            } else if (currentPlayerSprite == playerSpriteROLL_RIGHT || currentPlayerSprite == playerSpriteROLL_LEFT) {
                if (animationIndexDashing < 3) animationIndexDashing++;
                else {
                    playerMoveSpeed = 2;
                    isPlayerStateLocked = false;
                    setCurrent_Player_State();
                    animationIndexDashing = 0;
                }
            }
            animationTick = 0;
        }
    }

    public int currentIndexerForAnimation() {
        if (currentPlayerSprite == playerSpriteIDLE_LEFT || currentPlayerSprite == playerSpriteIDLE_RIGHT)
            return animationIndexIdle;
        else if (currentPlayerSprite == playerSpriteMOVE_LEFT || currentPlayerSprite == playerSpriteMOVE_RIGHT)
            return animationIndexMoving;
        else if (currentPlayerSprite == playerSpriteCAST_SPELL_RIGHT || currentPlayerSprite == playerSpriteCAST_SPELL_LEFT)
            return animationIndexCasting;
        else if (currentPlayerSprite == playerSpriteROLL_LEFT || currentPlayerSprite == playerSpriteROLL_RIGHT)
            return animationIndexDashing;
        else return 0;
    }

    public void spellCastController() {


        boolean shouldCreateQSpell = PlayerKeyboardInputs.Q_Pressed && isSpellQOffCooldown() && GameEngine.isOffGCD();
        boolean shouldCreateWSpell = PlayerKeyboardInputs.W_Pressed && isSpellWOffCooldown() && GameEngine.isOffGCD();
        boolean shouldCreateESpell = PlayerKeyboardInputs.E_Pressed && isSpellEOffCooldown() && GameEngine.isOffGCD();
        boolean shouldCreateRSpell = PlayerKeyboardInputs.R_Pressed && isSpellROffCooldown() && GameEngine.isOffGCD();
        boolean shouldDash = PlayerKeyboardInputs.SHIFT_Pressed && isDashOffCooldown() && GameEngine.isOffGCD();

        try {
            if (shouldCreateQSpell) {
                QSpell QSpell = new QSpell(this);
                lastQSpellCastTime = System.currentTimeMillis();
                Client.socket.send(PacketManager.spellRequestPacket('Q', QSpell.spellID, QSpell.spriteAngle));
            }
            if (shouldCreateWSpell) {
                QSpell QSpell = new QSpell(this);
                lastWSpellCastTime = System.currentTimeMillis();
                Client.socket.send(PacketManager.spellRequestPacket('W', QSpell.spellID, QSpell.spriteAngle));
            }
            if (shouldCreateESpell) {
                QSpell QSpell = new QSpell(this);
                lastESpellCastTime = System.currentTimeMillis();
                Client.socket.send(PacketManager.spellRequestPacket('E', QSpell.spellID, QSpell.spriteAngle));
            }
            if (shouldCreateRSpell) {
                Ultimate UltimateSpell = new Ultimate(this);
                lastRSpellCastTime = System.currentTimeMillis();
                Client.socket.send(PacketManager.spellRequestPacket('R', UltimateSpell.spellID, UltimateSpell.spriteAngle));
            }
            if (shouldDash) {
                isPlayerStateLocked = true;
                Current_Player_State = setDashStateForAnimation();
                playerMoveSpeed = 4;
                lastDashCastTime = System.currentTimeMillis();
                Client.socket.send(PacketManager.spellRequestPacket('D', 404, 404));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        if (shouldWeSendPacketToServer) {
//            try {
//                Client.socket.send(PacketManager.spellRequestPacket(shouldCreateQSpell, shouldCreateWSpell,
//                        shouldCreateESpell, shouldCreateRSpell));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public void updateCooldownsForDrawing() {
        QCurrentCooldown = (QSpell.SPELLQCOOLDOWN - (System.currentTimeMillis() - lastQSpellCastTime));
        WCurrentCooldown = (QSpell.SPELLQCOOLDOWN - (System.currentTimeMillis() - lastWSpellCastTime));
        ECurrentCooldown = (QSpell.SPELLQCOOLDOWN - (System.currentTimeMillis() - lastESpellCastTime));
        RCurrentCooldown = (Ultimate.ULTIMATESPELLCOOLDOWN - (System.currentTimeMillis() - lastRSpellCastTime));
    }

    private boolean isDashOffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastDashCastTime >= DASH_COOLDOWN;
    }

    private boolean isSpellQOffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastQSpellCastTime >= QSpell.SPELLQCOOLDOWN;

    }

    private boolean isSpellWOffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastWSpellCastTime >= QSpell.SPELLQCOOLDOWN;

    }

    private boolean isSpellEOffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastESpellCastTime >= QSpell.SPELLQCOOLDOWN;

    }

    private boolean isSpellROffCooldown() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastRSpellCastTime >= Ultimate.ULTIMATESPELLCOOLDOWN;

    }

    private EnumContainer.AllPlayerStates setDashStateForAnimation() {
        switch (Current_Player_State) {

            case IDLE_LEFT, MOVING_LEFT, CASTING_SPELL_LEFT -> {
                return EnumContainer.AllPlayerStates.DASHING_LEFT;
            }
            case IDLE_RIGHT, MOVING_RIGHT, CASTING_SPELL_RIGHT -> {
                return EnumContainer.AllPlayerStates.DASHING_RIGHT;
            }
            default -> {
                return EnumContainer.AllPlayerStates.DASHING_RIGHT;
            }
        }
    }


    public void updatePlayerHitboxWorldAndPosOnScreen() {
        localPlayerHitbox.x = playerPosXWorld + hitboxOffsetX;
        localPlayerHitbox.y = playerPosYWorld + hitboxOffsetYAbovePlayerSprite;
        localPlayerHitbox.playerHitboxPosXScreen = playerPosXScreen + hitboxOffsetX;
        localPlayerHitbox.playerHitboxPosYScreen = playerPosYScreen + hitboxOffsetYAbovePlayerSprite;

    }

    public void updateHealthBarCurrentHealthAndPositionOnScreen() {
        healthbar.currentHealthToDraw = healthbar.setSizeOfCurrentHealthToDraw();
        healthbar.healthbarPositionOnScreenX = (int) (localPlayerHitbox.playerHitboxPosXScreen);
        healthbar.healthbarPositionOnScreenY = (int) (localPlayerHitbox.playerHitboxPosYScreen - healthbar.offsetY);
    }


    private final int hitboxOffsetX = 90;
    private final int hitboxOffsetYAbovePlayerSprite = 130;

    public class LocalPlayerHitbox extends Rectangle2D.Float {

        public float playerHitboxPosXScreen, playerHitboxPosYScreen;

        LocalPlayerHitbox() {
            super(
                    playerPosXWorld + hitboxOffsetX,
                    playerPosYWorld + hitboxOffsetYAbovePlayerSprite,
                    (playerSpriteMOVE_RIGHT[0].getWidth() - hitboxOffsetX * 2),
                    playerSpriteMOVE_RIGHT[0].getHeight() - (hitboxOffsetYAbovePlayerSprite + 40));
        }

    }

}

