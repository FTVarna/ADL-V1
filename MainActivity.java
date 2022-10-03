import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends Activity implements OnClickListener {
	
	private Toast toast;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set up click listeners for all the buttons
		View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener(this);
		View newButton = findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;
	}
	
	public void onClick(View button) {
		switch (button.getId()) {
		
		case R.id.continue_button:
			if(Configuration.isSaveStatus()){
				Intent intend = new Intent(this, GameActivity.class);
				intend.putExtra("saveStatus", "saveStatus");
				startActivity(intend);
			
			}else if(!Configuration.isSaveStatus()){
				displayMessage("No Saved Game Found");
			}
			
			break;
		
		case R.id.about_button:
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
			break;

		case R.id.new_button:
			openNewGame();
			break;

		case R.id.exit_button:
	
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        	displayMessage("Saving...");
			        	Configuration config = new Configuration(GameActivity.getGuess(), GameActivity.getScore(),GameActivity.getLevel(),GameActivity.getTime(), GameActivity.getCycle(), GameActivity.getAttempt());
			        	config.saveGame();
			        	displayMessage("Game Saved");
			        	finish();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			    		finish();
			            break;
			        }
			    }
			    
			};
			
			/*if a game is played, 
			 * when user click exit button, 
			 * player will be asked to save the game
			 * that has been started.
			 */
			if(GameActivity.isPlayed()){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Save The Current Game?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
			
			/* if no game is played, 
			 * then there is nothing to save, 
			 * so player will be able to exit without 
			 * asked to save the game
			 */
			}else if(!GameActivity.isPlayed()){
				finish();
			}
			
			break;
		}
	}
	

	private void openNewGame() {
		new AlertDialog.Builder(this).setTitle(R.string.difficulty_title)
				.setItems(R.array.difficulty,

				/* -
				 * new click listener for the items (level of difficulties)
				 */
				new DialogInterface.OnClickListener() {

					/* -
					 * when a level of difficulty is clicked, startGame method
					 * is invoked with a parameter of i. The parameter i is the
					 * index of the string of arrays (difficulties).
					 */
					public void onClick(DialogInterface dialoginterface, int i) {
						startGame(i);
					}
				}).show();
	}

	private void startGame(int level) {
		Intent gameActivity = new Intent(this, GameActivity.class);
		gameActivity.putExtra("level", level);
		startActivity(gameActivity);
	}
	
	private void displayMessage(String message) {
		toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}
}