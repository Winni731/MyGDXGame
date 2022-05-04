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
import com.badlogic.gdx.math.Vector2;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;	// Sprite Drawer
	Texture targetTexture;	// image texture
	private float x;	// target position x
	private float y;	// target position y
	private Rectangle targetRect;
	private Random rng;
	private Timer gameTimer;
	private int secondsLeft;
	private final int seconds = 10;
	private boolean gameOver = false;
	private int score;
	private BitmapFont font;

	@Override
	public void create() {	// game initilization 
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(8);
		batch = new SpriteBatch();
		targetTexture = new Texture("target.png");
		targetRect = new Rectangle((Gdx.graphics.getWidth()/2)-targetTexture.getWidth()*2,
				(Gdx.graphics.getHeight()/2)-targetTexture.getHeight()*2,
				targetTexture.getWidth()*2,
				targetTexture.getHeight()*2);
		rng = new Random();
		gameTimer = new Timer();
		startGame();
		startTimer();
	}

	@Override
	public void render () {	// capture movements during game
		if (secondsLeft <=0 ) {
			gameOver = true;
		}
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		if (!gameOver) {
			batch.draw(targetTexture, targetRect.x, targetRect.y,
					targetRect.width, targetRect.height);
			font.draw(batch, "Seconds Lefts "+secondsLeft, 150,
					Gdx.graphics.getHeight()-150);
			font.draw(batch, "Your Score: "+score, 150, 150);
			//	startTimer();
			if (Gdx.input.justTouched()) {
				Vector2 touchPosition = new Vector2(Gdx.input.getX(),
						Gdx.graphics.getHeight()-Gdx.input.getY());
				Rectangle touchedRect = new Rectangle(touchPosition.x,
						touchPosition.y, 1, 1);
				if (Intersector.overlaps(touchedRect, targetRect)) {
					changeTargetPosition();
					score++;
				}
			}
		}
		else {
			font.draw(batch, "	Game Over! \nYour Score was: "+score, 150,
					Gdx.graphics.getHeight()-150);
			if (Gdx.input.justTouched()) {
				startGame();
			}
		}

		batch.end();
	}

//	private void stopGame() {
//	}

	private void changeTargetPosition() {
		targetRect.setPosition(rng.nextInt(Gdx.graphics.getWidth()-
				(int)targetRect.width/2)+targetRect.height,
		rng.nextInt(Gdx.graphics.getHeight()-(int)targetRect.height/2)+
				targetRect.height);
	}

	private void startTimer() {
		score = 0;
		secondsLeft = seconds;
		gameTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!gameOver) {
					secondsLeft--;
				}
			}
		}, 0, 1000);
	}

	private void startGame() {
		gameOver = false;
		secondsLeft = seconds;
		targetRect = new Rectangle((Gdx.graphics.getWidth()/2)-
				targetTexture.getWidth()*2,
				(Gdx.graphics.getHeight()/2)-
				targetTexture.getHeight()*2,
				targetTexture.getWidth()*2,
				targetTexture.getHeight()*2);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
