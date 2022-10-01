package uk.ac.westminster.ecwm511.cw1;

import uk.ac.westminster.ecwm511.support.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener {
	
	//static variables are used to store the game status to save it
	private static boolean isPlayed = false;
	private static String guess;
	private static int score;
	private static int level;
	private static int time;
	private static int cycle;
	private static int attempt;
	
	private Toast toast;
	private int buttonId;
	private int expressionLength;

	private Engine engine;
	private Player player;
	
	private TextView timerTextView;
	private TextView messageTextView;
	private EditText expressionEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isPlayed = true;

		setContentView(R.layout.activity_game);

		expressionEditText = (EditText) findViewById(R.id.guess_input);

		View oneButton = findViewById(R.id.one_button);
		oneButton.setOnClickListener(this);

		View twoButton = findViewById(R.id.two_button);
		twoButton.setOnClickListener(this);

		View threeButton = findViewById(R.id.three_button);
		threeButton.setOnClickListener(this);

		View fourButton = findViewById(R.id.four_button);
		fourButton.setOnClickListener(this);

		View fiveButton = findViewById(R.id.five_button);
		fiveButton.setOnClickListener(this);

		View sixButton = findViewById(R.id.six_button);
		sixButton.setOnClickListener(this);

		View sevenButton = findViewById(R.id.seven_button);
		sevenButton.setOnClickListener(this);

		View eightButton = findViewById(R.id.eight_button);
		eightButton.setOnClickListener(this);

		View nineButton = findViewById(R.id.nine_button);
		nineButton.setOnClickListener(this);

		View zeroButton = findViewById(R.id.zero_button);
		zeroButton.setOnClickListener(this);

		View minusButton = findViewById(R.id.minus_button);
		minusButton.setOnClickListener(this);

		View delButton = findViewById(R.id.del_button);
		delButton.setOnClickListener(this);

		View hashButton = findViewById(R.id.hash_button);
		hashButton.setOnClickListener(this);

		timerTextView = (TextView) findViewById(R.id.timer_text);
		messageTextView = (TextView) findViewById(R.id.game_message);

		Configuration.sharedpreferences = getSharedPreferences(Configuration.MyPREFERENCES, Context.MODE_PRIVATE);
		
		// get the extra information that was passed
		Bundle extras = getIntent().getExtras();

		if (extras != null) {

			// load saved game
			if (extras.getString("saveStatus") != null) {

				engine = new Engine(0, 0, 0);
				player = new Player(0, 0);
				
				engine.setGame(this);
				engine.setPlayer(player);
				
				player.setGameActivityContext(this);
				player.setEngine(engine);

				expressionEditText.setText(Configuration.sharedpreferences.getString(Configuration.STATE_GUESS, ""));
				player.setScore(Configuration.sharedpreferences.getInt(Configuration.STATE_SCORE, 0));
				engine.setLevel(Configuration.sharedpreferences.getInt(Configuration.STATE_LEVEL, 0));
				engine.countDown(Configuration.sharedpreferences.getInt(Configuration.STATE_TIMER, 0));
				engine.setCycle(Configuration.sharedpreferences.getInt(Configuration.STATE_CYCLE, 0));
				
				if (SettingsActivity.hintsEanabled(this)) {
					player.setAttempt(Configuration.sharedpreferences.getInt(Configuration.STATE_ATTEMPT, 0));
				}
				
				this.setExpressionLength(expressionEditText.length());

			} else {
				
				// new game creation
				int level = extras.getInt("level");

				engine = new Engine(level, 0, 0);
				
				player = new Player(0, 0);
				
				player.setGameActivityContext(this);
				player.setEngine(engine);
				
				engine.setGame(this);
				engine.setupGame(engine.getLevel());
				engine.setPlayer(player);

				engine.countDown(10);
			}
		}
	}



	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//when activity is closed assign on screen values to static variables to be saved
		guess = expressionEditText.getText().toString();
		level = engine.getLevel();
		score = player.getScore();
		time = engine.getCurrentSecond();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		engine.countDown(engine.getCurrentSecond());//when focus is regained, starts the timer from where it has left.
	}

	@Override
	protected void onPause() {
		super.onPause();
		engine.getTimer().cancel();//if focus is lost, stops the timer.
	}

	@Override
	public void onClick(View view) {
		/* checks if button is pressed other than DEL and # and 
		 * checks if the expression ends with ?
		 * then removes the ? and passes the buttonId to pressButton() method of 
		 * the player object.
		 * */
		buttonId = view.getId();
		
		if (buttonId != R.id.del_button && buttonId != R.id.hash_button) {

			if (expressionEditText.getText().toString().endsWith("?")) {
				expressionEditText.setText(expressionEditText.getText().toString().substring(0,expressionEditText.getText().length() - 1));
			}

		}

		player.pressButton(buttonId);
	}

	/**
	 * After expression is generated, this method is called 
	 * and user interface is updated
	 * to display the new arithmetic expression.
	 * */
	public void updateGUI() {
		for (int i = 0; i < Engine.getGeneratedNumbers().length; i++) {

			expressionEditText.setText(expressionEditText.getText() + ""
					+ Integer.toString(Engine.getGeneratedNumbers()[i]));

			if (i < Engine.getOperations().length) {

				expressionEditText.setText(expressionEditText.getText() + ""
						+ Engine.getOperations()[i] + "");
			}
		}

		expressionEditText.setText(expressionEditText.getText() + "=?");
		
		this.setExpressionLength(expressionEditText.length());
	}
	
	/**
	 * Displays the given parameter as hint message to the player.
	 * @param	message 	String that consists the message
	 * */
	public void displayHint(String message) {
		toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * Updates the text colour of the messageTextView according
	 * to the status of the game.
	 * @param	hasWon	winning status of the player
	 * */
	public void changeColour(boolean hasWon) {
		if (hasWon) {
			messageTextView.setTextColor(Color.parseColor("#00FF00"));
			messageTextView.setText("CORRECT!");

		} else if (!hasWon) {
			messageTextView.setTextColor(Color.parseColor("#FF0000"));
			messageTextView.setText("WRONG!!");
		}
	}

	/**
	 * Shows the game over screen. This method is called
	 * when player reaches MAX_CYCLE in the Engine class.
	 * */
	public void openGameEnd() {
		Intent gameEnd = new Intent(this, GameEndActivity.class);
		gameEnd.putExtra("score", player.getScore());
		startActivity(gameEnd);
		finish();// finish current activity
	}

	public TextView getTimerTextView() {
		return timerTextView;
	}

	public void setTimerTextView(TextView timerTextView) {
		this.timerTextView = timerTextView;
	}

	public int getExpressionLength() {
		return expressionLength;
	}

	public void setExpressionLength(int expressionLength) {
		this.expressionLength = expressionLength;
	}

	public TextView getExpressionEditText() {
		return expressionEditText;
	}

	public void setExpressionEditText(EditText expressionEditText) {
		this.expressionEditText = expressionEditText;
	}

	public TextView getMessage() {
		return messageTextView;
	}

	public void setMessage(TextView messageTextView) {
		this.messageTextView = messageTextView;
	}

	public static String getGuess() {
		return guess;
	}

	public static void setGuess(String guess) {
		GameActivity.guess = guess;
	}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		GameActivity.score = score;
	}

	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		GameActivity.level = level;
	}

	public static boolean isPlayed() {
		return isPlayed;
	}
	
	public static void setIsPlayed(boolean isPlayed) {
		GameActivity.isPlayed = isPlayed;
	}

	public static int getCycle() {
		return cycle;
	}

	public static void setCycle(int cycle) {
		GameActivity.cycle = cycle;
	}

	public static int getAttempt() {
		return attempt;
	}

	public static void setAttempt(int attempt) {
		GameActivity.attempt = attempt;
	}

	public static void setPlayed(boolean isPlayed) {
		GameActivity.isPlayed = isPlayed;
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		GameActivity.time = time;
	}
}