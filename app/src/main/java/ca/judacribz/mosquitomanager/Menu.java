package ca.judacribz.mosquitomanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

import static ca.judacribz.mosquitomanager.util.UI.setInitView;

public class Menu extends AppCompatActivity {

    @BindView(R.id.btn_catch_basin) Button btnCatchBasin;
    @BindView(R.id.btn_water_treatment) Button btnWaterTreatment;
    @BindView(R.id.btn_enter_hours) Button btnEnterHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitView(this, R.layout.activity_menu, R.string.app_name,  false);
    }

    @OnClick(R.id.btn_catch_basin)
    public void startCatchBasin() {
        startActivity(new Intent(this,CatchBasin.class));
    }
}
