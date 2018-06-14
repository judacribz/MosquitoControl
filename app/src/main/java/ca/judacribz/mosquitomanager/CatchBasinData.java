package ca.judacribz.mosquitomanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnItemSelected;
import ca.judacribz.mosquitomanager.models.CB;
import ca.judacribz.mosquitomanager.models.DataHelper;

import static ca.judacribz.mosquitomanager.util.UI.setInitView;
import static ca.judacribz.mosquitomanager.util.UI.setSpinnerWithArray;

public class CatchBasinData extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE_EXT_STORAGE = 10101;
    ArrayList<CB> cbs;
    ArrayList<String> dates;
    DataHelper dataHelper;
    String currDate;

    private HashMap<String, Object> cbData;
    boolean fileExists = false;
    private static final String CATCH_BASIN_HEADERS[] = {"Community", "CB Address", "Number of Larvae",
    "Stage of Development"};
    private File desFile = null;

    @BindView(R.id.lvCatchBasins) ListView listView;
    @BindView(R.id.spr_date)
    Spinner sprDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInitView(this, R.layout.activity_catch_basin_data, R.string.recorded_catch_basins,  false);

        dataHelper = new DataHelper(this);

        dates = dataHelper.getAllDates();
        setSpinnerWithArray(this, dates, sprDate);

        View view  = findViewById(R.id.list_header);
        ((TextView)view.findViewById(R.id.tvId)).setText("id");
        ((TextView)view.findViewById(R.id.tvCommunity)).setText("Community");
        ((TextView)view.findViewById(R.id.tvCBAddress)).setText("CB Address");
        ((TextView)view.findViewById(R.id.tvNumLarvae)).setText("# Larvae");
        ((TextView)view.findViewById(R.id.tvStageOfDev)).setText("Stage of Dev");

        cbData = new HashMap<>();
//        File main = new File(Environment.getExternalStorageDirectory() + "/mosquito");
//
//        main.mkdir();

        sprDate.setSelection(dates.size()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu mainMenu) {
        getMenuInflater().inflate(R.menu.menu_catch_basin_data, mainMenu);

        return super.onCreateOptionsMenu(mainMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.act_create_spreadsheet:

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                        Toast.makeText(this, "Need storage permission to create CSV data file", Toast.LENGTH_SHORT).show();
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_WRITE_EXT_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    for (CB cb : cbs) {
                        cbData.put(CATCH_BASIN_HEADERS[0], cb.getCommunity());
                        cbData.put(CATCH_BASIN_HEADERS[1], cb.getCbAddress());
                        cbData.put(CATCH_BASIN_HEADERS[2], cb.getNumberOfLarvae());
                        cbData.put(CATCH_BASIN_HEADERS[3], cb.getStageOfDevelopment());

                        writeDataToCSV();
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeDataToCSV() {
        ICsvMapWriter mapWriter = null;

        desFile = new File(Environment.getExternalStorageDirectory() + "/catchBasin_" + currDate + ".csv");
        if (desFile.delete()) {
        }

        try {
            mapWriter = new CsvMapWriter(new FileWriter(desFile, true), CsvPreference.EXCEL_PREFERENCE);

            final CellProcessor[] processors = getProcessors();

            if (!fileExists) {
                mapWriter.writeHeader(CATCH_BASIN_HEADERS);
                fileExists = true;
            }

            mapWriter.write(cbData, CATCH_BASIN_HEADERS, processors);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapWriter != null) {
                try {
                    mapWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[] { new NotNull(), new NotNull(), new NotNull(), new NotNull()};
    }

    @OnItemSelected(R.id.spr_date)
    void onItemSelected(int position) {
        currDate = dates.get(position);
        cbs = dataHelper.getCatchBasinsForDate(currDate);
        listView.setAdapter(new CatchBasinAdapter(this, cbs));
    }
}
