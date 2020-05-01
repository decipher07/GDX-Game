package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background ;
	Texture[] man;
	int manState = 0 ;
	int pause = 0 ;
	int manY = 0;
	float gravity = 0.5f ;
	float velocity = 0 ;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
    Texture coin ;
	int coinCount ;
	Random random ;

	@Override
	public void create () {
		batch = new SpriteBatch();

		background = new Texture("bg.png");

		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getHeight()/2 ;

		coin = new Texture("coin.png");
	    random = new Random();
	}

	public void makeCoin(){
	    float height = random.nextFloat() * Gdx.graphics.getHeight();

	    coinYs.add((int) height);

	    coinXs.add(Gdx.graphics.getWidth());
    }

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (coinCount < 100){
		    coinCount++ ;
        } else {
		    coinCount = 0 ;
		    makeCoin();
        }

		for (int i = 0 ; i < coinXs.size() ; i++ ){
		    batch.draw(coin, coinXs.get(i) , coinYs.get(i));
		    coinXs.set(i , coinXs.get(i) - 4 );
        }

		if (Gdx.input.justTouched()){
			velocity -= 10 ;
		}

		if (pause < 20){
			pause++;
		} else {
			if (manState < 3) {
				manState++;
			} else {
				manState = 0;
			}
		}

		velocity += gravity;
		manY -= velocity;

		if (manY <= 0)
			manY = 0 ;

		batch.draw(man[manState], Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2, manY );

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
