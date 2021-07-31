package com.harshchauhan.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameover;
	Texture playAgain;
	Texture startGame;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	int flapstate=0;
	float birdY=0;
	float velocity=0;
	int gamestate=0;
	float gravity=1;
	float gap=275;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity=3;
	Circle birdCircle;

	int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
	float[] tubeOffset=new float[numberOfTubes];
	Rectangle[] topTubesRectangle;
	Rectangle[] bottomTubesRectangle;

	int score=0;
	int scoringTube=0;
	BitmapFont font;

	float distanceBetweenTubes;

	Texture toptube;
	Texture bottomtube;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background= new Texture("background-night.png");
		playAgain=new Texture("play.png");

		birds= new Texture[2];
		birds[0]=new Texture("blueup.png");
		birds[1]=new Texture("bluedown.png");

		//shapeRenderer=new ShapeRenderer();
		birdCircle= new Circle();

		startGame=new Texture("start game.png");
		gameover=new Texture("gameover.png");

		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(5);

		toptube=new Texture("toptube.png");
		bottomtube=new Texture("bottomtube.png");
		maxTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator=new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()/2;
		topTubesRectangle=new Rectangle[numberOfTubes];
		bottomTubesRectangle=new Rectangle[numberOfTubes];

		gameStart();

	}

	public void gameStart(){
		birdY=Gdx.graphics.getHeight()/2- birds[flapstate].getHeight()/2;

		for (int i=0;i<numberOfTubes;i++){

			tubeOffset[i]= (randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-toptube.getWidth()/2 + Gdx.graphics.getWidth()+i*distanceBetweenTubes;
			topTubesRectangle[i]=new Rectangle();
			bottomTubesRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate==0){

			batch.draw(startGame, Gdx.graphics.getWidth()/2- startGame.getWidth()/2, Gdx.graphics.getHeight()/3-startGame.getHeight()/3);
		}

		if(gamestate ==1){

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score: ",String.valueOf(score));

				if(scoringTube < numberOfTubes-1){
					scoringTube++;
				}
				else{
					scoringTube=0;
				}
			}

			if(Gdx.input.justTouched()){

				velocity=-17;

			}

			for (int i=0;i<numberOfTubes;i++) {

				if(tubeX[i]<-toptube.getWidth()){
					tubeOffset[i]= (randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
					tubeX[i]+=numberOfTubes*distanceBetweenTubes;
				}

				else {
					tubeX[i] -= tubeVelocity;
				}

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() - tubeOffset[i]);

				topTubesRectangle[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffset[i], toptube.getWidth(), toptube.getHeight());
				bottomTubesRectangle[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() - tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());

				font.draw(batch, String.valueOf(score), 50, 100);
				batch.draw(birds[flapstate], Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2,birdY, birds[flapstate].getWidth()+10, birds[flapstate].getHeight()+10 );
			}

			if(birdY>0) {
				velocity += gravity;
				birdY -= velocity;
			}
			else{
				gamestate=2;
			}
		}
		else if (gamestate==0){
			if(Gdx.input.justTouched())
				gamestate=1;
		}
		else if(gamestate==2){
			font.draw(batch, String.valueOf(score), 50, 100);

			batch.draw(gameover, Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2, gameover.getWidth(), gameover.getHeight());
			batch.draw(playAgain, Gdx.graphics.getWidth()/2-playAgain.getWidth()/2-30, Gdx.graphics.getHeight()/2-gap, playAgain.getWidth()+50, playAgain.getHeight()+50);

			if(Gdx.input.justTouched()) {
				gamestate = 1;
				gameStart();
				score=0;
				scoringTube=0;
				velocity=0;
			}
		}
		//Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2


		if(flapstate==0)
			flapstate=1;
		else
			flapstate=0;


		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[flapstate].getHeight()/2, birds[flapstate].getWidth()/2);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i=0;i<numberOfTubes;i++){

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOffset[i], toptube.getWidth(), toptube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() - tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubesRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubesRectangle[i])){

				gamestate=2;
			}
		}

		//shapeRenderer.end();
	}

}
