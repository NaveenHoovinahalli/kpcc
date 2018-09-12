package teli.com.kpcc.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.InjectView;
import teli.com.kpcc.R;
import teli.com.kpcc.Utils.AndroidUtils;
import teli.com.kpcc.Utils.Constants;
import teli.com.kpcc.models.PollNotParticipatedFeed;
import teli.com.kpcc.views.CcTextView;

/**
 * Created by naveen on 12/1/15.
 */
public class CastVoteActivity extends BaseActivity {

    @InjectView(R.id.radio_choice_container)
    RadioGroup radioGroup;

    @InjectView(R.id.vote_button)
    Button submitVote;

    @InjectView(R.id.tv_poll_question)
    CcTextView pollQuestion;

    String pollQuestiontoSend;

    SharedPreferences sharedPreferences;

    PollNotParticipatedFeed pollNotParticipatedFeed;

    public static final String POLLNOTFEED="poll_not_feed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.poll_item);

        if(getIntent().hasExtra(POLLNOTFEED)){
           pollNotParticipatedFeed=  getIntent().getParcelableExtra(POLLNOTFEED);
           pollQuestiontoSend=getIntent().getStringExtra("POLLQUESTION");

            pollQuestion.setText(pollNotParticipatedFeed.getPollQuestion());



            for(int i=0;i<pollNotParticipatedFeed.getPollChoices().size();i++){
                pollNotParticipatedFeed.getPollChoices().get(i).getId();
                RadioButton button=new RadioButton(this);
                button.setId(i);
                button.setText(pollNotParticipatedFeed.getPollChoices().get(i).getChoice());
                button.setTextSize(20);
                radioGroup.addView(button);
            }
//            radioGroup.check(0);
        }
    }
    public void SubmitVote(View view){


        int checkedId= radioGroup.getCheckedRadioButtonId();
        if(checkedId>=0) {

            String selected = pollNotParticipatedFeed.getPollChoices().get(checkedId).getId();
            Log.d("Selected Choice", "" + selected);

            sharedPreferences = getSharedPreferences("SharedPollCount", Context.MODE_PRIVATE);
            int count = Integer.parseInt(sharedPreferences.getString("POLL_COUNT", "0"));
            if (count >= 1) {
                sharedPreferences.edit().putString("POLL_COUNT", String.valueOf(count - 1)).apply();
            }

            String url = String.format(Constants.URL_POLL_PARTICIPATE_IN_POLL, AndroidUtils.getDeviceImei(this),
                    pollNotParticipatedFeed.getId(), selected);

            Intent intent = new Intent(CastVoteActivity.this, PollResultActivity.class);
            intent.putExtra(PollResultActivity.RESULT_URL, url);
            intent.putExtra("POLLQUESTION",pollQuestiontoSend);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"Please select the option",Toast.LENGTH_SHORT).show();
        }


     }

}
