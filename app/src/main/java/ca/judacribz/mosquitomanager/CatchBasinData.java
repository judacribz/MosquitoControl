package ca.judacribz.mosquitomanager;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
import ca.judacribz.mosquitomanager.models.CB;
import ca.judacribz.mosquitomanager.models.DataHelper;

import static ca.judacribz.mosquitomanager.util.UI.setInitView;

public class CatchBasinData extends AppCompatActivity {

    ArrayList<CB> cbs;
    DataHelper dataHelper;

    private HashMap<String, Object> cbData;
    boolean fileExists = false;
    private static final String CATCH_BASIN_HEADERS[] = {"Community", "CB Address", "Number of Larvae",
    "Stage of Development"};
    private File desFile = null;

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


        cbData = new HashMap<>();
//        File main = new File(Environment.getExternalStorageDirectory() + "/mosquito");
//
//        main.mkdir();

        desFile = new File(Environment.getExternalStorageDirectory() + "/catchBasin.csv");
        if (desFile.delete()) {
        }


        dataHelper = new DataHelper(this);


        cbs = dataHelper.getAllCatchBasins();
        listView.setAdapter(new CatchBasinAdapter(this, cbs));
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
                CB cb;
                for (int i = 0; i < cbs.size(); i++) {
                    cb  = cbs.get(i);
                    cbData.put(CATCH_BASIN_HEADERS[0], cb.getCommunity());
                    cbData.put(CATCH_BASIN_HEADERS[1], cb.getCbAddress());
                    cbData.put(CATCH_BASIN_HEADERS[2], cb.getNumberOfLarvae());
                    cbData.put(CATCH_BASIN_HEADERS[3], cb.getStageOfDevelopment());

                    writeDataToCSV();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeDataToCSV() {
        ICsvMapWriter mapWriter = null;

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
}
