import java.util.Random;

import android.content.Context;
import android.os.CountDownTimer;

/**
* Engine Model Object.
* 
* <p> Various attributes of game and related behaviour.
* 
* <p> Class consists of methods that are the core functionality
* of the game.
*
* @author Fevzi Tugrul Varna
*/
public class Engine{
	
	private static int[] generatedNumbers;//stores random generated numbers for the expression
	private static char[] operations;//stores random generated operations for the expression
	
	private final int MAX_CYCLES = 10;
	private final int MAX_ANSWER_LENGTH = 9;
	
	private Context game;
	private Player player;
	private CountDownTimer timer;
	private Random randomGenerator = new Random();
	private int highest;
	private int lowest;
	private int integersInvolved;
	
	//instance fields
	private int result;//result of the arithmetic expression
	private int level;//level of game
	private int cycle;//number of games played (max 10)
	
	/**
	* Constructor.
	* @param	level	selected level of the game	
	* @param	result	total calculation of the expression on the game screen
	* @param	cycle	total number of times the game is played
	*/
	public Engine(int level, int result, int cycle){
		this.level = level;
		this.result = result;
		this.cycle = cycle;
	}
	
	/**
	* Invokes the compulsory methods needed for the game to start.
	* All random generations, and updating the user interface is
	* invoked by this method.
	* @param int level	of the game
	*/
	public void setupGame(int level) {
		integersInvolved = getNumberOfIntegers(level);
		generateIntegers(integersInvolved);
		generateOperations(integersInvolved-1);
		((GameActivity) game).updateGUI();
	}

	/**
	* Returns a boolean which confirms if the game has finished or not.
	* @return	boolean
	*/
	public boolean hasEnded(){
		if(this.cycle == this.MAX_CYCLES){
			GameActivity.setIsPlayed(false);//the game has ended, so we do not need to keep track of this.
			return true;
		}
		this.cycle++;
		return false;
	}
	
	/**
	* Generates random arithmetic operations for the quantity of
	* the parameter received. Generated arithmetic operations are used for
	* the arithmetic expression.
	* @param int numberOfOperations	number of the operations that will be generated
	*/
	public void generateOperations(int numberOfOperations){
		operations = new char[numberOfOperations];
		
		int randomInteger;
		
		for(int i=0; i < numberOfOperations; i++){
			randomInteger = randomGenerator.nextInt(5 - 1) + 1;
			if(randomInteger==1){
				operations[i] = '+';
			}else if(randomInteger==2){
				operations[i] = '-';
			}else if(randomInteger==3){
				operations[i] = '*';
			}else if(randomInteger==4){
				operations[i] = '/';
			}
		}
		
	}
	
	/**
	* Returns a randomly generated integer based on the level of the game
	* the returned integer is used as the quantity of numbers that
	* expression consists.
	* @param	int level	Level of the game
	* @return	int numberOfIntegers
	*/
	public int getNumberOfIntegers(int level){
		int numberOfIntegers = 0;
		
		switch(level){
	
		//Novice level
		case 0:
			numberOfIntegers = 2;
			break;
	
		//Easy level
		case 1:
			highest = 4;
			lowest = 2;
			numberOfIntegers = randomGenerator.nextInt(highest - lowest) + lowest; //generates random number between 2 and 3
			break;
		
		//Medium level
		case 2:
			highest = 5;
			lowest = 2;
			numberOfIntegers = randomGenerator.nextInt(highest - lowest) + lowest;//generates random number between 2 and 4
			break;
		
		//Guru level
		case 3:
			highest = 7;
			lowest = 4;
			numberOfIntegers = randomGenerator.nextInt(highest - lowest) + lowest;//generates random number between 4 and 6
			break;
		}
		
		return numberOfIntegers;
	}
	
	/**
	* Generates random numbers for the quantity of
	* the parameter received. Generated numbers are used for
	* the arithmetic expression.
	* @param	int numberOfIntegers	an integer specified by the getNumberOfIntegers()
	*/
	public void generateIntegers(int numberOfIntegers){
		generatedNumbers = new int[numberOfIntegers];
		
		for(int i=0; i < generatedNumbers.length; i++){
			generatedNumbers[i] = randomGenerator.nextInt(99 - 1) + 1;
		}
	}
	
	/**
	* Returns a message of "greater" or "lower" which is used to display
	* to the player as a hint.
	* @param	int enteredValue	An integer entered by the user as an answer
	* @return	String Greater/Lower
	*/
	public String getHint(int enteredValue){
		if(enteredValue > result){
			return "Lower";
		}
		
		return "Greater";
	}
	
	/**
	* Returns a total for the current expression displayed 
	* for the screen. Used for evaluation when player enters answer.
	* @return	int result	Calculation of the expression
	*/
	public int getCalculation(){
		result = generatedNumbers[0];
		
		for (int i = 0; i < generatedNumbers.length; i++) {
		
		if (i < operations.length) {
			
				if(operations[i]=='+'){
					result+= generatedNumbers[i+1];
				}else if(operations[i]=='-'){
					result-= generatedNumbers[i+1];
				}else if(operations[i]=='*'){
					result*= generatedNumbers[i+1];
				}else if(operations[i]=='/'){
					result/= generatedNumbers[i+1];
				}
			}
		}
		
		return result;
	}
	
	
	public void countDown(int seconds){
		int milliseconds = seconds * 1000;//convert seconds to milliseconds
		
		//new timer CountDownTimer object
		timer = new CountDownTimer(milliseconds, 100) {

			public void onTick(long millisUntilFinished) {
				//show seconds on GUI
				((GameActivity) game).getTimerTextView().setText(millisUntilFinished / 1000 + " secs");
			}

			public void onFinish() {
				/*
				 * When timer reaches 0
				 * */
				
				//reset textfields
				((GameActivity) game).getExpressionEditText().setText(null);
				
				/*
				 * if hints are enabled, player's attempts are 
				 * reset to 0 when time is up.
				 * */
				if (SettingsActivity.hintsEanabled(game)) {
					player.resetAttempt();
				}
				
	
				/*
				 * if player reaches 10 cycles then game is over
				 * */
				if(hasEnded()){
		
					((GameActivity) game).openGameEnd();
				
					/*if 10 cycles has not been reaches yet*/
					}else if(!hasEnded()){
				
					setupGame(level);//run the setupGame method
				countDown(10);//start timer, from second 10
				}
			}
			
		}.start();
	}
	
	/**
	* Returns the length of the player's answer which is
	* used to keep track of number of characters entered
	* to avoid possible errors when validating the answer.
	* Is it also used to notify the player when the answer
	* exceeds 9 characters.
	* @return	int currentLength - expressionLength
	*/
	public int getAnswerLength(){
		int currentLength = ((GameActivity) game).getExpressionEditText().length();
		int expressionLength = ((GameActivity) game).getExpressionLength();
		return currentLength - expressionLength;
	}
	
	/**
	* When player attempts to enter an answer that has
	* more characters than specified, player is displayed
	* a notification message and unable to type more than 9 
	* characters.
	*/
	public void checkAnswerLength(){
		if(getAnswerLength() >= MAX_ANSWER_LENGTH){
			int currentLength = ((GameActivity) game).getExpressionEditText().length();
			String expression = ((GameActivity) game).getExpressionEditText().getText().toString();
			((GameActivity) game).getExpressionEditText().setText(expression.substring(0, currentLength-1));
			((GameActivity) game).displayHint("Please Do Not Exceed the Maximum Length of Answer");
		}
	}
	
	/**
	* Returns the current second of the time remaining. Which is
	* requested when player gives correct answer to calculate the
	* points.
	* @return	currentSecond
	*/
	public int getCurrentSecond() {
		int currentSecond = Integer.parseInt(((GameActivity) game).getTimerTextView().getText().toString().substring(0,2).trim());
		return currentSecond;
	}
	
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	public void incrementCycle(){
		this.cycle++;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	public static int[] getGeneratedNumbers() {
		return generatedNumbers;
	}

	public static void setGeneratedNumbers(int[] generatedNumbers) {
		Engine.generatedNumbers = generatedNumbers;
	}

	public static char[] getOperations() {
		return operations;
	}

	public static void setOperations(char[] operations) {
		Engine.operations = operations;
	}

	public Random getRandomGenerator() {
		return randomGenerator;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getIntegersInvolved() {
		return integersInvolved;
	}

	public void setIntegersInvolved(int integersInvolved) {
		this.integersInvolved = integersInvolved;
	}

	public CountDownTimer getTimer() {
		return timer;
	}

	public void setTimer(CountDownTimer timer) {
		this.timer = timer;
	}

	public Context getGame() {
		return game;
	}

	public void setGame(Context game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}