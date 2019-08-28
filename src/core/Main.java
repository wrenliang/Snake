package core;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	public static final double WINDOW_WIDTH = 800;
	public static final double WINDOW_HEIGHT = 800;
	
	public boolean gameState = false; //false is paused, true is playing
	public boolean lostGame = false; //to check if user has lost already and is trying to press start again
	public KeyCode snakeDirection = KeyCode.D; //D represents going to the right of screen
	
	//ArrayList to store the JavaFX rectangles that make up snake body
	List<Rectangle> rectList= new ArrayList<>();
	
	public static final int SNAKE_SIZE = 10;
	public static final int APPLE_SIZE = 12;
	public static final int MAX_LENGTH = 999;
	public int snakeLength = 5;
	
	//to store the new coordinates of the first segment of the snake after a key is pressed
	public double newX;
	public double newY;
	
	//stores the coordinates of the apple
	public double[] appleCoords = new double[2];

	//2D array storing the x y coordinates of each segment
	//(same indexing as the ArrayList
	public double[][] locationArr = new double[MAX_LENGTH][2];
	
	
	
	public static void main(String args[]) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//random number generator
		Random rand = new Random();
		
		//GUI elements
		primaryStage.setTitle("Snake Game");
		Label label = new Label("Welcome to Snake!");
		
		Label score = new Label("Snake Length: " + snakeLength);
		score.setLayoutX(WINDOW_WIDTH - 200);
		
		//first segment of snake starts in the middle of the screen
		locationArr[0][0] = WINDOW_WIDTH/2;
		locationArr[0][1] = WINDOW_HEIGHT/2;
		
		//used in move method
		newX = locationArr[0][0];
		newY = locationArr[0][1];
		
		//initializes positions of other snake segments
		for (int i = 1; i < snakeLength; i++) {
			locationArr[i][0] = locationArr[i-1][0] - SNAKE_SIZE;
			locationArr[i][1] = locationArr[0][1];
		}
		
		//adds GUI rectangles for each segment
		for(int i = 0; i < snakeLength; i++) {
			rectList.add(new Rectangle(locationArr[i][0],locationArr[i][1], SNAKE_SIZE, SNAKE_SIZE));
			rectList.get(i).setFill(Color.GREEN);
		}
		
		//initial apple generation
		appleCoords[0] = rand.nextDouble() * WINDOW_WIDTH;
		appleCoords[1] = rand.nextDouble() * WINDOW_HEIGHT;
		
		System.out.println(appleCoords[0] + " " + appleCoords[1]); //debugging
		
		Rectangle apple = new Rectangle(appleCoords[0], appleCoords[1], APPLE_SIZE, APPLE_SIZE);
		apple.setFill(Color.RED);
		
		Button startButton =  new Button("Start");
		startButton.setLayoutX(300f);
		
		Button pauseButton =  new Button("Pause");
		pauseButton.setLayoutX(400f);
		
		//handles start button being pressed
		EventHandler<MouseEvent> startEventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("startButton pressed");
				
				if (lostGame == false) {
					gameState = true;
				}
			}
		};
		
		//handles pause button being pressed
		EventHandler<MouseEvent> pauseEventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("pauseButton pressed");
				gameState = false;
			}
		};
		
		startButton.addEventFilter(MouseEvent.MOUSE_CLICKED, startEventHandler);
		pauseButton.addEventFilter(MouseEvent.MOUSE_CLICKED, pauseEventHandler);
		
		//initializes the root group for all the children to be added to
		Group root = new Group();
		root.getChildren().add(label);
		root.getChildren().add(score);
		root.getChildren().add(startButton);
		root.getChildren().add(pauseButton);
		root.getChildren().add(apple);
		
		for (int i = 0; i < rectList.size(); i++) {
			root.getChildren().add(rectList.get(i));
		}
		
		//initializes new scene with the root object containing all children
		Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		//handles key press
		EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				KeyCode keyPressed = event.getCode();
				System.out.println(keyPressed);
				
				//this block checks if keyPressed is 1. a valid key, and 2. not the reverse direction of the last key
				try {
					if (KeyHelper.validKey(keyPressed)) {
						if (keyPressed != KeyHelper.reverseKey(snakeDirection)) {
							snakeDirection = keyPressed;
						}
					} else {
						System.out.println("Invalid key press ignored");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
			
		};
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEventHandler);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);
		
		KeyFrame moveSnake = new KeyFrame(Duration.seconds(.05), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if (gameState) {
					switch (snakeDirection) {
					case W:
						newY = locationArr[0][1] - SNAKE_SIZE;
						break;
					case A:
						newX = locationArr[0][0] - SNAKE_SIZE;
						break;
					case S:
						newY = locationArr[0][1] + SNAKE_SIZE;
						break;
					case D:
						newX = locationArr[0][0] + SNAKE_SIZE;
						break;
						
					default:
						System.out.println("Unrecognized key");
						break;
						
					}
					
					
					
					for(int i = rectList.size() - 1; i >= 1; i--) {
						locationArr[i][0] = locationArr[i-1][0];
						locationArr[i][1] = locationArr[i-1][1];
						rectList.get(i).relocate(locationArr[i][0],locationArr[i][1]);
					}
					
					locationArr[0][0] = newX;
					locationArr[0][1] = newY;
					
					
					try {
						rectList.get(0).relocate(locationArr[0][0], locationArr[0][1]);
						
						//this block executes if snake eats an apple
						if (collisionCheck(locationArr,appleCoords)) {
							System.out.println("Got apple!");
							rectList.add(new Rectangle(locationArr[snakeLength][0] - SNAKE_SIZE, locationArr[snakeLength][1], SNAKE_SIZE, SNAKE_SIZE));
							rectList.get(snakeLength).setFill(Color.GREEN);
							root.getChildren().add(rectList.get(snakeLength));
							snakeLength++;
							System.out.println("Snake length: " + snakeLength);
							appleCoords[0] = rand.nextDouble() * WINDOW_WIDTH;
							appleCoords[1] = rand.nextDouble() * WINDOW_HEIGHT;
							apple.relocate(appleCoords[0], appleCoords[1]);
							
							score.setText("Snake Length: " + snakeLength);
						}
						
						
					} catch (Exception e) {
						gameState = false;
						System.out.println(e.toString());
						System.out.println("Game over!");
						lostGame = true;
						
						
						//displays "game over" on screen
						Label gameOver = new Label("Game Over! Start application again to play");
						gameOver.setLayoutX(WINDOW_WIDTH/4);
						gameOver.setLayoutY(WINDOW_HEIGHT/2);
						
						Label collisionReason = new Label(e.toString());
						collisionReason.setLayoutX(WINDOW_WIDTH/4);
						collisionReason.setLayoutY(gameOver.getLayoutY() + 30);
						
						gameOver.setFont(new Font("Arial", 20));
						collisionReason.setFont(new Font("Arial", 15));
						
						root.getChildren().add(gameOver);
						root.getChildren().add(collisionReason);
					}
					
				}

				
			}

			
		});
		
		timeline.getKeyFrames().add(moveSnake);
		timeline.play();

	}
	
	//checks collision with apple
	public boolean collisionCheck(double[][] locationArr, double[] appleCoords) throws Exception{
		boolean collision = false;
		
		//side edge
		if (locationArr[0][0] >= WINDOW_WIDTH || locationArr[0][0] < 0) {
			throw new Exception("Side border collision");
		}
		
		if (locationArr[0][1] >= WINDOW_HEIGHT || locationArr[0][1] < 0) {
			throw new Exception("Top/bottom border collision");
		}
		
		//self-collision check
		for (int i = 2; i < locationArr.length; i++) {
			if (locationArr[0][0] == locationArr[i][0] && locationArr[0][1] == locationArr[i][1]) {
				throw new Exception("Self-collision! " + "with " + i + "th segment");
			}
		}
		
		if (Math.abs(locationArr[0][0] - appleCoords[0]) < 10 && Math.abs(locationArr[0][1] - appleCoords[1]) < 10) {
			collision = true;
			System.out.println("Collided!");
		}
		
		return collision;
	}
	


}
