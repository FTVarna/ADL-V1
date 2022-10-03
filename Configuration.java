import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
* Configuration Model Object.
*
* <p> Stores on screen attributes of the game temporarily and saves 
* them when asked by the player before the activity is destroyed.
*
* @author Fevzi Tugrul Varna
*/

public class Configuration {

	
	static SharedPreferences sharedpreferences;
	static boolean saveStatus = false;
	
	static final String MyPREFERENCES = "MyPrefs";
	static final String STATE_GUESS = "guessKey";
	static final String STATE_RESULT = "resultKey";
	static final String STATE_TIMER = "timerKey";
	static final String STATE_SCORE = "scoreKey";
	static final String STATE_LEVEL = "levelKey";
	static final String STATE_ATTEMPT = "playerAttemptKey";
	static final String STATE_CYCLE = "cycleKey";
	
	//instance fields
	private String guess;
	private int score;
	private int level;
	private int timeTook;
	private int cycle;
	private int attempt;
	
	/**
	* Constructor.
	* @param	guess		selected level of the game	
	* @param	score		total calculation of the expression on the game screen
	* @param	level		total number of times the game is played
	* @param	timeTook	selected level of the game	
	* @param	cycle		total calculation of the expression on the game screen
	* @param	attempt		total number of times the game is played
	*/
	public Configuration(String guess, int score, int level, int timeTook, int cycle, int attempt) {
		this.guess = guess;
		this.score = score;
		this.level = level;
		this.timeTook = timeTook;
		this.cycle = cycle;
		this.attempt = attempt;
	}
	
	/**
	* Saves the attributes of the current game that is required when
	* player wishes to continue the exited game.
	*/
	public void saveGame(){
		 Editor editor = sharedpreferences.edit();
		 editor.putInt(STATE_ATTEMPT, attempt);
		 editor.putInt(STATE_CYCLE, cycle);
	     editor.putInt(STATE_TIMER, timeTook);
	     editor.putInt(STATE_LEVEL, level);
	     editor.putInt(STATE_SCORE, score);
	     editor.putString(STATE_GUESS, guess);
	     editor.commit();
	     saveStatus = true;
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTimeTook() {
		return timeTook;
	}

	public void setTimeTook(int timeTook) {
		this.timeTook = timeTook;
	}

	public static boolean isSaveStatus() {
		return saveStatus;
	}

	public static void setSaveStatus(boolean saveStatus) {
		Configuration.saveStatus = saveStatus;
	}
}