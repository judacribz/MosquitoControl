package ca.judacribz.mosquitomanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;
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
    private static final String D_NAME = "mosquito_manager";
    private static final String F_NAME = "catchBasin_%s.csv";

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
        setInitView(this, R.layout.activity_catch_basin_data, R.string.recorded_catch_basins, true);

        dataHelper = new DataHelper(this);

        dates = dataHelper.getAllDates();
        setSpinnerWithArray(this, dates, sprDate);

        cbData = new HashMap<>();

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

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )) {

                        Toast.makeText(this, R.string.err_ext_storage_permission, Toast.LENGTH_SHORT).show();
                    } else {
                        // Request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_WRITE_EXT_STORAGE);
                    }
                } else {
                    File rootPath = new File(Environment.getExternalStorageDirectory(), D_NAME);
                    if(!rootPath.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        rootPath.mkdirs();
                    }

                    desFile = new File(rootPath, String.format(F_NAME, currDate));

                    //noinspection ResultOfMethodCallIgnored
                    desFile.delete();
                    writeDataToCSV(desFile);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Write to csv file */
    private void writeDataToCSV(File desFile) {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(),
                new NotNull(),
                new NotNull(),
                new NotNull()
        };
        ICsvMapWriter mapWriter = null;

        try {
            mapWriter = new CsvMapWriter(new FileWriter(desFile, true), CsvPreference.EXCEL_PREFERENCE);
            mapWriter.writeHeader(CATCH_BASIN_HEADERS);

            for (CB cb : cbs) {
                cbData.put(CATCH_BASIN_HEADERS[0], cb.getCommunity());
                cbData.put(CATCH_BASIN_HEADERS[1], cb.getCbAddress());
                cbData.put(CATCH_BASIN_HEADERS[2], cb.getNumberOfLarvae());
                cbData.put(CATCH_BASIN_HEADERS[3], cb.getStageOfDevelopment());

                mapWriter.write(cbData, CATCH_BASIN_HEADERS, processors);
            }

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnItemSelected(R.id.spr_date)
    void onItemSelected(int position) {
        currDate = dates.get(position);
        cbs = dataHelper.getCatchBasinsForDate(currDate);
        listView.setAdapter(new CatchBasinAdapter(this, cbs));
    }
}
