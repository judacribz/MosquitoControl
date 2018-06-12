package ca.judacribz.mosquitomanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import ca.judacribz.mosquitomanager.models.CB;
import ca.judacribz.mosquitomanager.models.DataHelper;

import static ca.judacribz.mosquitomanager.util.UI.setInitView;

public class CatchBasinData extends AppCompatActivity {

    ArrayList<CB> cbs;
    DataHelper dataHelper;

    @BindView(R.id.lvCatchBasins) ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitView(this, R.layout.activity_catch_basin_data, R.string.recorded_catch_basins,  false);

        View view  = findViewById(R.id.list_header);

        ((TextView)view.findViewById(R.id.tvId)).setText("id");
        ((TextView)view.findViewById(R.id.tvCommunity)).setText("Community");
        ((TextView)view.findViewById(R.id.tvCBAddress)).setText("CB Address");
        ((TextView)view.findViewById(R.id.tvNumLarvae)).setText("# Larvae");
        ((TextView)view.findViewById(R.id.tvStageOfDev)).setText("Stage of Dev");


        dataHelper = new DataHelper(this);


        cbs = dataHelper.getAllCatchBasins();
        listView.setAdapter(new CatchBasinAdapter(this, cbs));
    }
}
