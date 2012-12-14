package net.sim.classes;

import java.util.Random;

import net.sim.interfaces.BotKeyboardListener;
import net.sim.interfaces.BotMouseListener;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Intended as the presenter/controller mechanism for bots. Each new bot is
 * constructed with this as a parameter.
 * 
 * @author mrh2
 * 
 */
public class BotController implements BotMouseListener, BotKeyboardListener {

	private static final int INITIAL_BOT_POPULATION = 3;
	
	private Random mRandom;
	private int xMax, yMax, thetaMax;
	
	//Variables for structured game programming
	private GameBoard mGameBoard;
	private BotRegister mBotRegister;

	public BotController() {
		mGameBoard = new GameBoard(this, this);
		
		mRandom = new Random();
		xMax = Display.getWidth();
		yMax = Display.getHeight();
		thetaMax = 180;

		mBotRegister = new BotRegister();
		for (int i = 0; i < INITIAL_BOT_POPULATION; i++) {
			new Bot(this, mRandom.nextInt(xMax), mRandom.nextInt(yMax), Math.toRadians(mRandom.nextInt(thetaMax)));
		}
		mBotRegister.selectBot(0);
	}
	
	public void start(){
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			mBotRegister.update();
			mGameBoard.update();
		}
	}

	public void register(Bot bot) {
		mBotRegister.registerBot(bot);
	}

	// TODO remove
	// Temporary method for moving a single bot.
	public void move(int x, int y) {
		//mBotRegister.getBot(0).move(x,y);
	}

	@Override
	public void keyPressed(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mBotRegister.setSelectedMovingForward(true);
			break;
		case Keyboard.KEY_RIGHT:
			mBotRegister.setSelectedRotatingClockwise(true);
			break;
		case Keyboard.KEY_LEFT:
			mBotRegister.setSelectedRotatingAntiClockwise(true);
			break;
		}
//		System.out.printf("Key pressed: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyReleased(int key) {
		switch (key) {
		case Keyboard.KEY_UP:
			mBotRegister.setSelectedMovingForward(false);
			break;
		case Keyboard.KEY_RIGHT:
			mBotRegister.setSelectedRotatingClockwise(false);
			break;
		case Keyboard.KEY_LEFT:
			mBotRegister.setSelectedRotatingAntiClockwise(false);
			break;
		}
//		System.out.printf("Key released: %s\n", Keyboard.getKeyName(key));
	}

	@Override
	public void keyTyped(int key) {
		// No op
	}

	@Override
	public void leftButtonClicked(int x, int y) {
//		System.out.printf("Left mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void leftButtonReleased(int x, int y) {
//		System.out.printf("Left mouse button released at (%d,%d)\n", x,y);
	}

	@Override
	public void rightButtonClicked(int x, int y) {
//		System.out.printf("Right mouse button clicked at (%d,%d)\n", x,y);
	}

	@Override
	public void rightButtonReleased(int x, int y) {
//		System.out.printf("Right mouse button released at (%d,%d)\n", x,y);
		
	}

	@Override
	public void leftDragged(int x, int y) {
//		System.out.printf("Left mouse button dragged to (%d,%d)\n", x,y);
	}

	@Override
	public void rightDragged(int x, int y) {
//		System.out.printf("Right mouse button dragged to (%d,%d)\n", x,y);
	}
	
	public static void main(String[] args) {
		BotController botController = new BotController();
		botController.start();
	}

}
