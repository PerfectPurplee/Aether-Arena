package entities.spells.basicspells;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import main.AssetLoader;
import main.EnumContainer;
import networking.Client;
import main.Camera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static main.EnumContainer.ServerClientConnectionCopyObjects;


public class QSpell {

    LocalPlayer localPlayer;
    Optional<OnlinePlayer> onlinePlayer;
    public BufferedImage[] spellSprites_START = new BufferedImage[NUMBER_OF_SPRITES];
    public BufferedImage[] spellSprites_FLYING = new BufferedImage[NUMBER_OF_SPRITES];
    public BufferedImage[] spellSprites_END = new BufferedImage[NUMBER_OF_SPRITES];

    public BufferedImage[] currentSpellSprites;
    private EnumContainer.AllQspellStates current_Spell_State;

    public static final int NUMBER_OF_SPRITES = 5;
    private static final int SPEED = 4;
    public static long SPELLQCOOLDOWN = 1000; // 1 seconds in milliseconds
    private final int RANGE = 1000;
    private float distanceTraveled = 0;
    public double spriteAngle;
    //  object starting position on screen. Character pos + (vector * int)
    public float spellPosXWorld, spellPosYWorld;
    public float spellPosXScreen, spellPosYScreen;

    public int xSpriteStartPos, ySpriteStartPos, spriteSize;
    public int animationTick, animationSpeed = 15, animationIndex;
    public float normalizedVectorX;
    public float normalizedVectorY;
    public int mousePosXWorld, mousePosYWorld;

    public final int spellCasterClientID;
    public final int spellID;
    //    If true, object will be soon removed from active spell list;
    public boolean playerGotHit;
    private boolean flagForRemoval;
    public Spell01Hitbox spell01Hitbox;

    public static CopyOnWriteArrayList<QSpell> listOfActiveQSpells = new CopyOnWriteArrayList<>();
    public static boolean QSpellCreatedOnThisMousePress = false;

    public static long LastLocalSpellCreationTime;

    public QSpell(LocalPlayer playerCastingThisSpell) {

        localPlayer = playerCastingThisSpell;
        getVector();
        localPlayer.isPlayerStateLocked = true;
        setPlayerCastingThisSpellStateLocalPlayer();
        getSpellSprites(localPlayer);
        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_START;
        setCurrent_Spell_Sprite();
        setInitialProjectilePosition();

        spellCasterClientID = Client.ClientID;
        spellID = localPlayer.counterOfThisPlayerQSpells;
        localPlayer.counterOfThisPlayerQSpells++;

        spell01Hitbox = new Spell01Hitbox();
        playerGotHit = false;
        flagForRemoval = false;

        LastLocalSpellCreationTime = System.currentTimeMillis();

            listOfActiveQSpells.add(this);

    }

    public QSpell(Spell01DTO spell01DTO) {

        spellCasterClientID = spell01DTO.spellCasterClientID;
        spriteAngle = spell01DTO.spriteAngle;
        setOnlinePlayerCastingThisSpell();
        getSpellSprites(onlinePlayer);
        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_START;
        setCurrent_Spell_Sprite();
        normalizedVectorX = spell01DTO.normalizedVectorX;
        normalizedVectorY = spell01DTO.normalizedVectorY;
        spellPosXWorld = spell01DTO.spellPosXWorld;
        spellPosYWorld = spell01DTO.spellPosYWorld;
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
        onlinePlayer.ifPresent(player -> player.isPlayerStateLocked = true);
        setPlayerCastingThisSpellStateOnlinePlayer();

        spellID = spell01DTO.spellID;

        spell01Hitbox = new Spell01Hitbox();
        playerGotHit = false;
        flagForRemoval = false;

            listOfActiveQSpells.add(this);

    }

    protected void setInitialProjectilePosition() {
        spellPosXWorld = ((localPlayer.localPlayerHitbox.x +
                (localPlayer.localPlayerHitbox.width / 2 - ((float) spellSprites_FLYING[0].getWidth() / 2)) + (normalizedVectorX * 10)));
        spellPosYWorld = ((localPlayer.localPlayerHitbox.y +
                localPlayer.localPlayerHitbox.height / 2 - ((float) spellSprites_FLYING[0].getHeight() / 2)) + (normalizedVectorY * 10));
        spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
        spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
//        this is for debugging, and for server to scale
//        System.out.println("Width: " + (float) spellSprites_FLYING[0].getHeight() / 2 + " Height:  " + (float) (spellSprites_FLYING[0].getWidth() / 2));
    }

    private void setPlayerCastingThisSpellStateLocalPlayer() {
        if (localPlayer != null) {
            if (mousePosXWorld <= LocalPlayer.playerPosXWorld + localPlayer.playerFeetX)
                localPlayer.Current_Player_State = EnumContainer.AllPlayerStates.CASTING_SPELL_LEFT;
            else
                localPlayer.Current_Player_State = EnumContainer.AllPlayerStates.CASTING_SPELL_RIGHT;
        }
    }

    private void setPlayerCastingThisSpellStateOnlinePlayer() {
        if (onlinePlayer.isPresent()
                && (onlinePlayer.get().Current_Player_State_Online_Player != EnumContainer.AllPlayerStates.CASTING_SPELL_LEFT
                || onlinePlayer.get().Current_Player_State_Online_Player != EnumContainer.AllPlayerStates.CASTING_SPELL_RIGHT)) {
            if (spellPosXWorld <= onlinePlayer.get().playerPosXWorld + onlinePlayer.get().onlinePlayerHitbox.getWidth() / 2)
                onlinePlayer.get().Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.CASTING_SPELL_LEFT;
            else
                onlinePlayer.get().Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.CASTING_SPELL_RIGHT;
        }
    }

    private void setOnlinePlayerCastingThisSpell() {
        onlinePlayer = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
                .filter(onlineplayer -> onlineplayer.onlinePlayerID == spellCasterClientID).findFirst();
        if (onlinePlayer.isEmpty()) {
            System.out.println("NO ONLINE PLAYER FOR SPELL FOUND");
        }
    }

    public void setCurrent_Spell_Sprite() {

        switch (current_Spell_State) {

            case Q_SPELL_START -> {
                currentSpellSprites = spellSprites_START;
            }
            case Q_SPELL_FLYING -> {
                currentSpellSprites = spellSprites_FLYING;
            }
            case Q_SPELL_END -> {
                currentSpellSprites = spellSprites_END;
            }
        }
    }

    private void calculateDistanceTraveled(float x1, float x2, float y1, float y2) {
        distanceTraveled += (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void getSpellSpritesDefault() {
        BufferedImage[] spellAssets;
        for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
            switch (element) {
                case Q_SPELL_START -> spellAssets = AssetLoader.QSpellFireBallCastStart;
                case Q_SPELL_FLYING -> spellAssets = AssetLoader.QSpellFireballCastFlying;
                case Q_SPELL_END -> spellAssets = AssetLoader.QSpellFireBallCastEnd;
                default -> spellAssets = AssetLoader.QSpellViolet;
            }

            BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
            double angle = Math.atan2(normalizedVectorY, normalizedVectorX);
            Graphics2D g2d;
            for (int i = 0; i < spellAssets.length; i++) {
                BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                g2d = spellAsset.createGraphics();

                AffineTransform transform = new AffineTransform();
                transform.rotate(angle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                g2d.setTransform(transform);
                g2d.drawImage(spellAssets[i], 0, 0, null);
                spellAssetsRotated[i] = spellAsset;
            }
            if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
            else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING)
                spellSprites_FLYING = spellAssetsRotated;
            else spellSprites_END = spellAssetsRotated;
        }
    }

    protected void getSpellSprites(Optional<OnlinePlayer> onlinePlayer) {
        BufferedImage[] spellAssets;
        if (onlinePlayer.isPresent()) {
            System.out.println(onlinePlayer.get().onlinePlayerChampion.name());
            for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
                switch (element) {
                    case Q_SPELL_START -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.QSpellWaterBallCastStart;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.QSpellFireBallCastStart;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.QSpellRockBallCastStart;
                        else
                            spellAssets = AssetLoader.QSpellWindBallCastStart;
                    }
                    case Q_SPELL_FLYING -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.QSpellWaterBallCastFlying;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.QSpellFireballCastFlying;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.QSpellRockBallCastFlying;
                        else
                            spellAssets = AssetLoader.QSpellWindBallCastFlying;
                    }
                    case Q_SPELL_END -> {
                        if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                            spellAssets = AssetLoader.QSpellWaterBallCastEnd;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                            spellAssets = AssetLoader.QSpellFireBallCastEnd;
                        else if (onlinePlayer.get().onlinePlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                            spellAssets = AssetLoader.QSpellRockBallCastEnd;
                        else
                            spellAssets = AssetLoader.QSpellWindBallCastEnd;
                    }
                    default -> spellAssets = AssetLoader.QSpellViolet;
                }

                BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
                Graphics2D g2d;
                for (int i = 0; i < spellAssets.length; i++) {
                    BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                    g2d = spellAsset.createGraphics();

                    AffineTransform transform = new AffineTransform();
                    transform.rotate(spriteAngle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                    g2d.setTransform(transform);
                    g2d.drawImage(spellAssets[i], 0, 0, null);
                    spellAssetsRotated[i] = spellAsset;
                }
                if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
                else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING)
                    spellSprites_FLYING = spellAssetsRotated;
                else spellSprites_END = spellAssetsRotated;
            }
        } else {
            System.out.println("Online player not present, cant get Sprites");
            getSpellSpritesDefault();
        }
    }

    protected void getSpellSprites(LocalPlayer localPlayer) {
        BufferedImage[] spellAssets;
        for (EnumContainer.AllQspellStates element : EnumContainer.AllQspellStates.values()) {
            switch (element) {
                case Q_SPELL_START -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.QSpellWaterBallCastStart;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.QSpellFireBallCastStart;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.QSpellRockBallCastStart;
                    else
                        spellAssets = AssetLoader.QSpellWindBallCastStart;
                }
                case Q_SPELL_FLYING -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.QSpellWaterBallCastFlying;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.QSpellFireballCastFlying;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.QSpellRockBallCastFlying;
                    else
                        spellAssets = AssetLoader.QSpellWindBallCastFlying;
                }
                case Q_SPELL_END -> {
                    if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE)
                        spellAssets = AssetLoader.QSpellWaterBallCastEnd;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL)
                        spellAssets = AssetLoader.QSpellFireBallCastEnd;
                    else if (localPlayer.localPlayerChampion == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE)
                        spellAssets = AssetLoader.QSpellRockBallCastEnd;
                    else
                        spellAssets = AssetLoader.QSpellWindBallCastEnd;
                }
                default -> spellAssets = AssetLoader.QSpellViolet;
            }

            BufferedImage[] spellAssetsRotated = new BufferedImage[NUMBER_OF_SPRITES];
            spriteAngle = Math.atan2(normalizedVectorY, normalizedVectorX);
            Graphics2D g2d;
            for (int i = 0; i < spellAssets.length; i++) {
                BufferedImage spellAsset = new BufferedImage(spellAssets[i].getWidth(), spellAssets[i].getHeight(), BufferedImage.TYPE_INT_ARGB);
                g2d = spellAsset.createGraphics();

                AffineTransform transform = new AffineTransform();
                transform.rotate(spriteAngle, (double) spellAsset.getWidth() / 2, (double) spellAsset.getHeight() / 2);
                g2d.setTransform(transform);
                g2d.drawImage(spellAssets[i], 0, 0, null);
                spellAssetsRotated[i] = spellAsset;
            }
            if (element == EnumContainer.AllQspellStates.Q_SPELL_START) spellSprites_START = spellAssetsRotated;
            else if (element == EnumContainer.AllQspellStates.Q_SPELL_FLYING) spellSprites_FLYING = spellAssetsRotated;
            else spellSprites_END = spellAssetsRotated;
        }
    }

    private void getVector() {
        mousePosXWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getX());
        mousePosYWorld = (int) (ServerClientConnectionCopyObjects.currentMousePosition.getY());
        float vectorX = (float) (mousePosXWorld - (localPlayer.localPlayerHitbox.x + localPlayer.localPlayerHitbox.width / 2));
        float vectorY = (float) (mousePosYWorld - (localPlayer.localPlayerHitbox.y + localPlayer.localPlayerHitbox.height / 2));
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        normalizedVectorX = (vectorX / magnitude);
        normalizedVectorY = (vectorY / magnitude);
    }

    private void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            switch (current_Spell_State) {
                case Q_SPELL_START -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else {
                        animationIndex = 0;
                        current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_FLYING;
                    }
                    animationTick = 0;
                }
                case Q_SPELL_FLYING -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else animationIndex = 0;
                    animationTick = 0;
                }
                case Q_SPELL_END -> {
                    if (animationIndex < NUMBER_OF_SPRITES - 1) animationIndex++;
                    else {
                        flagForRemoval = true;
                    }
                    animationTick = 0;
                }
            }

        }
    }

    private void spellPositionUpdate() {
        calculateDistanceTraveled(spellPosXWorld, (spellPosXWorld + normalizedVectorX * SPEED), spellPosYWorld, (spellPosYWorld + normalizedVectorY * SPEED));
        if (distanceTraveled <= RANGE && !playerGotHit) {
            spellPosXWorld += (normalizedVectorX * SPEED);
            spellPosYWorld += (normalizedVectorY * SPEED);
            spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
            spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
        } else {
            spellPosXScreen = spellPosXWorld - Camera.cameraPosX;
            spellPosYScreen = spellPosYWorld - Camera.cameraPosY;
            if ((current_Spell_State != EnumContainer.AllQspellStates.Q_SPELL_END)) {
                current_Spell_State = EnumContainer.AllQspellStates.Q_SPELL_END;
                animationIndex = 0;
                spell01Hitbox = null;
            }
        }
    }

    public static void updateAllSpells01() {

        listOfActiveQSpells.removeIf(spell01 ->
                (spell01.spellPosXWorld < -64 ||
                        spell01.spellPosYWorld < -64 ||
                        spell01.spellPosXWorld > Camera.WHOLE_MAP.getWidth() + 64 ||
                        spell01.spellPosYWorld > Camera.WHOLE_MAP.getHeight() + 64 ||
                        spell01.flagForRemoval)
        );


            listOfActiveQSpells.forEach(spell01 -> {
                spell01.setCurrent_Spell_Sprite();
                spell01.spellPositionUpdate();
                spell01.animationController();
                spell01.updateSpellHitboxWorldAndPosOnScreen();
//                System.out.println("Caster:  " + spell01.spellCasterClientID +  "SpellID: " + spell01.spellID + "Pos X: "
//                        + spell01.spellPosXWorld + "Pos Y" + spell01.spellPosYWorld
            });

    }

    public void updateSpellHitboxWorldAndPosOnScreen() {
        if (Objects.nonNull(spell01Hitbox)) {
            spell01Hitbox.x = spellPosXWorld;
            spell01Hitbox.y = spellPosYWorld;
            spell01Hitbox.spell01HitboxPosXScreen = spellPosXScreen;
            spell01Hitbox.spell01HitboxPosYScreen = spellPosYScreen;
        }
    }

    private final int HitBoxOffset = 40;

    public class Spell01Hitbox extends Rectangle2D.Float {

        public float spell01HitboxPosXScreen, spell01HitboxPosYScreen;

        Spell01Hitbox() {
            super(spellPosXWorld, spellPosYWorld, spellSprites_FLYING[0].getWidth(), spellSprites_FLYING[0].getHeight());
        }

    }
}
