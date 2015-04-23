package razvan.gheorghe.taskmanagement.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.utils.DBConnector;

public class ExportFragment extends Fragment {

    Typeface tf;
    DBConnector dbConnector;
    TextView txtExport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_export, container, false);
        dbConnector = new DBConnector(getActivity());
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Marker Felt.ttf");

        txtExport = (TextView) rootView.findViewById(R.id.txtExport);
        txtExport.setTypeface(Typeface.create(tf, Typeface.BOLD));

        return rootView;
    }
}
