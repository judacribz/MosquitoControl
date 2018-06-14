package ca.judacribz.mosquitomanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

import static ca.judacribz.mosquitomanager.util.UI.setInitView;

public class Menu extends AppCompatActivity {


    public static final String EXTRA_LOGOUT_USER = "ca.judacribz.mosquitomanager.EXTRA_LOGOUT_USER";

    @BindView(R.id.btn_catch_basin) Button btnCatchBasin;
    @BindView(R.id.btn_water_treatment) Button btnWaterTreatment;
    @BindView(R.id.btn_enter_hours) Button btnEnterHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitView(this, R.layout.activity_menu, R.string.app_name,  false);

        btnCatchBasin.setText(R.string.catch_basin);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu mainMenu) {
        getMenuInflater().inflate(R.menu.menu_menu, mainMenu);

        return super.onCreateOptionsMenu(mainMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.act_logout:
                Intent logoutIntent = new Intent(this, Login.class);
                logoutIntent.putExtra(EXTRA_LOGOUT_USER, true);
                startActivity(logoutIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.btn_catch_basin)
    public void startCatchBasin() {
        startActivity(new Intent(this,CatchBasin.class));
    }
}
