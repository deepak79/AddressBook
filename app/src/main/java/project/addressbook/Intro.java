package project.addressbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by deepakgavkar on 18/12/16.
 */
public class Intro extends AppCompatActivity {

    ImageView imgShow;
    Button btnNext;
    TextView tvInfo;
    int count = 0;

    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.intro);

        imgShow = (ImageView) findViewById(R.id.imgShow);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvInfo = (TextView) findViewById(R.id.tvInfo);

        imgShow.setImageDrawable(getResources().getDrawable(R.drawable.first));
        tvInfo.setText("This is the landing page, All added contacts will appear here!");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 3) {
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putBoolean("activity_executed", true);
                    ed.apply();

                    Intent i = new Intent(Intro.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                } else {
                    if (count == 0) {
                        imgShow.setImageDrawable(getResources().getDrawable(R.drawable.second));
                        tvInfo.setText("Long tap on contact will give you these options to perform!");
                    } else if (count == 1) {
                        imgShow.setImageDrawable(getResources().getDrawable(R.drawable.third));
                        tvInfo.setText("This is add contact screen!");
                    } else if (count == 2) {
                        imgShow.setImageDrawable(getResources().getDrawable(R.drawable.fourth));
                        tvInfo.setText("Single tap on contact will get you on this screen!");
                    } else if (count == 3) {
                        imgShow.setImageDrawable(getResources().getDrawable(R.drawable.fifth));
                        tvInfo.setText("You can delete all of your contacts from here!");
                    }
                }
                count++;
            }
        });
    }
}
