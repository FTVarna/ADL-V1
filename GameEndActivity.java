package uk.ac.westminster.ecwm511.cw1;

import uk.ac.westminster.ecwm511.support.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GameEndActivity extends Activity implements OnClickListener{
	
	private TextView scoreTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_end);
		scoreTextView = (TextView) findViewById(R.id.player_score_text);
		View backButton = findViewById(R.id.main_menu_button);
		backButton.setOnClickListener(this);
		
		Bundle scoreInfo = getIntent().getExtras();

		if (scoreInfo != null) {

			int score = scoreInfo.getInt("score");
			
			scoreTextView.setText("Your Score:" + score);
		}
		
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}

	public TextView getScoreTextView() {
		return scoreTextView;
	}

	public void setScoreTextView(TextView scoreTextView) {
		this.scoreTextView = scoreTextView;
	}

}