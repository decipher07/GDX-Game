package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

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
	Rectangle manRectangle ;

	BitmapFont font ;

	int score =  0 ;
	int gameState = 0 ;

	Random random ;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
    Texture coin ;
	int coinCount ;

	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();


	ArrayList<Integer> bombsXs = new ArrayList<Integer>();
	ArrayList<Integer> bombsYs = new ArrayList<Integer>();
	Texture bomb ;
	int bombCount ;


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

		bomb = new Texture("bomb.png");

	    random = new Random();

	    font = new BitmapFont();
	    font.setColor(Color.WHITE);
	    font.getData().setScale(10);

	}

	public void makeCoin(){
	    float height = random.nextFloat() * Gdx.graphics.getHeight();

	    coinYs.add((int) height);

	    coinXs.add(Gdx.graphics.getWidth());
    }

	public void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();

		bombsYs.add((int) height);

		bombsXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1){
			// Game Is Live

			// Coins //
			if (coinCount < 100){
				coinCount++ ;
			} else {
				coinCount = 0 ;
				makeCoin();
			}

			coinRectangles.clear();
			for (int i = 0 ; i < coinXs.size() ; i++ ){
				batch.draw(coin, coinXs.get(i) , coinYs.get(i));
				coinXs.set(i , coinXs.get(i) - 4 );
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			// Bombs //
			if (bombCount < 100){
				bombCount++ ;
			} else {
				bombCount = 0 ;
				makeBomb();
			}
			coinRectangles.clear();
			for (int i = 0 ; i < bombsXs.size() ; i++ ){
				batch.draw(bomb, bombsXs.get(i) , bombsYs.get(i));
				bombsXs.set(i , bombsXs.get(i) - 8);
				bombRectangles.add(new Rectangle(bombsXs.get(i), bombsYs.get(i), bomb.getWidth(), bomb.getHeight()));
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


		} else if (gameState == 0){
			// Waiting to Start
			if (Gdx.input.justTouched()){
				gameState = 1 ;
			}
		} else {
			// Game Over
			if (Gdx.input.justTouched()){
				gameState = 1 ;
				manY = Gdx.graphics.getHeight() / 2 ;
				score = 0 ;
				velocity = 0 ;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;

				bombsXs.clear();
				bombsYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}



		batch.draw(man[manState], Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2, manY );

		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2, manY, man[manState].getWidth(), man[manState].getHeight());

		for (int i = 0 ; i < coinRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))){

			    score++;

			    coinRectangles.remove(i);
			    coinXs.remove(i);
			    coinYs.remove(i);
			    break ;
			}
		}


		for (int i = 0 ; i < bombRectangles.size(); i++){
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))){
				Gdx.app.log("Bomb!", "Collision");
				gameState = 2 ;
			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
