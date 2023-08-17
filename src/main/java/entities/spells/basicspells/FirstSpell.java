package entities.spells.basicspells;

import entities.playercharacters.PlayerClass;
import entities.spells.BasicSpell;

public class FirstSpell extends BasicSpell {

    private final int NUMBER_OF_SPRITES = 4;

    private final int SPEED = 1;

    //  object starting position on screen. Character pos + (vector * int)
    public float spellStartingPosX, spellStartingPosY;

    public FirstSpell() {
        super.xSpriteStartPos = 214;
        super.ySpriteStartPos = 349;
        super.spriteSize = 52;
//        super.getSpellSprites(NUMBER_OF_SPRITES);
        spellStartingPosX = PlayerClass.playerPosX + (super.normalizedVectorX * SPEED);
        spellStartingPosY = PlayerClass.playerPosY + (super.normalizedVectorY * SPEED);

        AllActiveSpells.add(this);
    }
}
