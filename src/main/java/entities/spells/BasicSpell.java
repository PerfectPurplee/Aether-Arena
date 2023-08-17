package entities.spells;

import main.GameEngine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicSpell {

    BufferedImage[] spellSprites;
    BufferedImage BasicSpellsSpriteSheet = GameEngine.BasicSpellsSpriteSheet;
    int xSpriteStartPos, ySpriteStartPos, spriteSize;

    public static List<BasicSpell> AllActiveSpells = new ArrayList<>();

    public BasicSpell(int xSpriteStartPos, int ySpriteStartPos, int spriteSize) {
        this.xSpriteStartPos = xSpriteStartPos;
        this.ySpriteStartPos = ySpriteStartPos;
        this.spriteSize = spriteSize;

        AllActiveSpells.add(this);


    }

    public void getSpellSprites(int numberOfSpritesForAnimation) {

        for (int i = 0; i < numberOfSpritesForAnimation; i++) {
            spellSprites[i] = BasicSpellsSpriteSheet.
                    getSubimage(xSpriteStartPos, ySpriteStartPos, spriteSize, spriteSize);
        }
    }
    public void animationController(int numberOfSpritesForAnimation) {

    }
}
