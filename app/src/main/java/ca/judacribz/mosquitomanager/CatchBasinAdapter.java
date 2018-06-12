package ca.judacribz.mosquitomanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import ca.judacribz.mosquitomanager.models.CB;

public class CatchBasinAdapter extends ArrayAdapter<CB> {
    private ArrayList<CB> cbs;
    private Context context;


    CatchBasinAdapter(Context context, ArrayList<CB> cbs) {
        super(context, R.layout.item_catch_basin, cbs);
        this.context = context;
        this.cbs = cbs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View reusableView, @NonNull ViewGroup parent) {
        CB cb = cbs.get(position);

        if (reusableView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            reusableView = inflater.inflate(R.layout.item_catch_basin,
                    parent,
                    false);
        }

        TextView id = reusableView.findViewById(R.id.tvId);
        id.setText(String.valueOf(cb.getId()));
        TextView community = reusableView.findViewById(R.id.tvCommunity);
        community.setText(cb.getCommunity());
        TextView cbAddress = reusableView.findViewById(R.id.tvCBAddress);
        cbAddress.setText(cb.getCbAddress());
        TextView numLarvae = reusableView.findViewById(R.id.tvNumLarvae);
        numLarvae.setText(String.valueOf(cb.getNumberOfLarvae()));
        TextView stageOfDev = reusableView.findViewById(R.id.tvStageOfDev);
        stageOfDev.setText(cb.getStageOfDevelopment());


        return reusableView;
    }
}
