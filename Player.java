import android.content.Context;

/**
* Player Model Object.
* 
* <p> Various attributes of player and related behaviour.
* 
* <p> Class consists of methods that simulates the
* behaviour of the player.
*
* @author Fevzi Varna
*/
public class Player {

	
	private final int TIME_ALLOWED = 10;
	private final int MAX_ATTEMPT = 4;
	
	private Engine engine;
	private Context game;

	private int hashButtonCounter = 0;
	
	//instance fields
	private int attempt;
	private int score;
	
	/**
	* Constructor.
	* @param	score	total points player receives
	* @param	attempt	number of times player attempts to answer for an expression
	*/
	public Player(int score, int attempt) {
		this.score = score;
		this.attempt = attempt;
	}

	/**
	* Player interacts with the game using the buttons on the screen. 
	* Each button has different role.
	* Method updates GUI according to the button pressed.
	* @param	buttonId	id of the buttons shown on the game screen
	*/
	public void pressButton(int buttonId) {

		switch (buttonId) {
		case R.id.one_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "1");
			break;

		case R.id.two_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "2");
			break;

		case R.id.three_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "3");
			break;

		case R.id.four_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "4");
			break;

		case R.id.five_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "5");
			break;

		case R.id.six_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "6");
			break;

		case R.id.seven_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "7");
			break;

		case R.id.eight_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "8");
			break;

		case R.id.nine_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "9");
			break;

		case R.id.zero_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "0");
			break;

		case R.id.minus_button:
			((GameActivity) game).getExpressionEditText().setText(((GameActivity) game).getExpressionEditText().getText() + "-");
			break;
		
		/* -
		 * Deletes the last character of the answer.
		 * */
		case R.id.del_button:
			int currentLength = ((GameActivity) game).getExpressionEditText().length();
			int expressionLength = ((GameActivity) game).getExpressionLength()-1; //-1 is for the question mark at the end of the expression
			String textString = ((GameActivity) game).getExpressionEditText().getText().toString();

			if (currentLength != expressionLength) {
				((GameActivity) game).getExpressionEditText().setText(textString.substring(0, textString.length() - 1));
			}
			break;

		/* -
		 * Checks if player has entered an answer, if "expression" ends with
		 * "?" or "=" then it means the user has not entered anything yet. So instead
		 * of trying to validate an empty answer the player will be
		 * presented a message.
		 */
			
		case R.id.hash_button:
			String expression = ((GameActivity) game).getExpressionEditText().getText().toString().trim();
			
			if (expression.endsWith("?") || expression.endsWith("=")) {

				((GameActivity) game).displayHint("Please Enter Value");
			
			}else{

				int lengthOfField = expression.length();

				// seperates player's answer from the field using substring
				// function
				int playerAnswer = Integer.parseInt(((GameActivity) game).getExpressionEditText().getText().toString()
								.substring(
									((GameActivity) game)
										.getExpressionLength() - 1,
										  lengthOfField));
				
				// calculated result of the expression
				int result = engine.getCalculation();

				// Cycle of game if hints are enabled
				if (SettingsActivity.hintsEanabled(game)) {

					this.attempt++;// player attempt

					if (hasWon(playerAnswer, result)) {

						attempt = 0;
						
						engine.getTimer().cancel();

						((GameActivity) game).changeColour(true);// change textfield colour to green
						
						this.setScore(engine.getCurrentSecond());

						engine.getTimer().onFinish();

						// answer is wrong
					} else {

						((GameActivity) game).changeColour(false);// change textfield colour to red

						String hintMessage = engine.getHint(playerAnswer);// get hint message by passing the value entered by the user

						((GameActivity) game)
								.displayHint(hintMessage);// hint message received above is passed to displayHint to show the message
					}

					// if user has reached 4 attempts
					if (!hasAttempts()) {
						engine.getTimer().cancel();
						engine.getTimer().onFinish();
					}

					// if hints are not enabled
				} else if (!SettingsActivity.hintsEanabled(game)) {

					hashButtonCounter++;

					// if # is pressed for the first time
					if (hashButtonCounter == 1) {

						engine.getTimer().cancel();

						if (hasWon(result, playerAnswer)) {

							((GameActivity) game)
									.changeColour(true);

							this.setScore(engine.getCurrentSecond());

							// if player hasn't won the game
						} else if (!hasWon(result, playerAnswer)) {
							((GameActivity) game).changeColour(false);
						}

						// if # is pressed the second time
					} else if (hashButtonCounter == 2) {
						hashButtonCounter = 0; // reset counter
						engine.getTimer().onFinish();
					}
				}
			}

			break;
		}
		engine.checkAnswerLength();//checks if player has entered more than 9 characters
	}

	/**
	* Returns boolean which states if the player has given
	* the correct answer or not.
	* @param	result	total of the expression
	* @param	playerAnswer	value that player entered as an answer
	* @return	boolean
	*/
	public boolean hasWon(int result, int playerAnswer) {
		if (result == playerAnswer) {
			return true;
		}
		return false;
	}

	/**
	* Returns boolean which states if the player has reached
	* the maximum attempts of answer for the expression. This method
	* is only called when hints are enabled.
	* @return	boolean
	*/
	public boolean hasAttempts() {
		if (attempt == MAX_ATTEMPT) {
			attempt = 0;
			return false;
		}
		return true;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int timeTook) {
		if (timeTook == 10) {
			this.score += 100;
		} else {
			double points = TIME_ALLOWED - timeTook;
			this.score += (int) Math.round(100 / points);
		}
	}
	
	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public void resetAttempt() {
		this.attempt = 0;
	}

	public int getMAX_ATTEMPT() {
		return MAX_ATTEMPT;
	}

	public Context getGameActivityContext() {
		return game;
	}

	public void setGameActivityContext(Context game) {
		this.game = game;
	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

}
