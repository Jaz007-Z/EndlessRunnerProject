/*package com.mygdx.game.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Endless;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.Sprites.Player;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(Endless.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Player player) {
        Endless.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }

}
*/