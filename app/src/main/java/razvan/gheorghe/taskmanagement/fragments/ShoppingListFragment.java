package razvan.gheorghe.taskmanagement.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.adapters.CustomArrayAdapter;
import razvan.gheorghe.taskmanagement.objects.Product;
import razvan.gheorghe.taskmanagement.utils.DBConnector;

public class ShoppingListFragment extends Fragment {

    Typeface tf;
    DBConnector dbConnector;
    ListView toBuyList, boughtList;
    TextView title;
    CustomArrayAdapter lv1Adapter, lv2Adapter;
    ArrayList<Product> products;
    AutoCompleteTextView edtAddProd;
    Button btnAdd;
    ArrayList<Product> tempToBuy;
    ArrayList<Product> tempBought;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        dbConnector = new DBConnector(getActivity());
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Marker Felt.ttf");

        products = new ArrayList<Product>();
        tempToBuy = new ArrayList<Product>();
        tempBought = new ArrayList<Product>();
        products = dbConnector.findAllProducts();

        tempToBuy.addAll(products);

        lv1Adapter = new CustomArrayAdapter(getActivity(), products);
        lv2Adapter = new CustomArrayAdapter(getActivity(), tempBought);
        title = (TextView) rootView.findViewById(R.id.title);
        title.setTypeface(Typeface.create(tf, Typeface.BOLD));
        edtAddProd = (AutoCompleteTextView) rootView.findViewById(R.id.edtAddProd);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        toBuyList = (ListView) rootView.findViewById(R.id.toBuyList);
        boughtList = (ListView) rootView.findViewById(R.id.toBuyList);

        toBuyList.setAdapter(lv1Adapter);
        boughtList.setAdapter(lv2Adapter);

        toBuyList.setItemsCanFocus(false);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProdName = edtAddProd.getText().toString();
                edtAddProd.setText("");
                String nrBuc = "1";
                String unit = "buc";
                Product prod = new Product(newProdName, nrBuc, unit);
                tempToBuy.add(prod);
                lv1Adapter = new CustomArrayAdapter(getActivity(), tempToBuy);
                toBuyList.setAdapter(lv1Adapter);
            }
        });

        toBuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempBought.add(tempToBuy.get(position));
                tempToBuy.remove(position);
                lv1Adapter = new CustomArrayAdapter(getActivity(), tempToBuy);
                lv2Adapter = new CustomArrayAdapter(getActivity(), tempBought);
                toBuyList.setAdapter(lv1Adapter);
                boughtList.setAdapter(lv2Adapter);
            }
        });

        boughtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempToBuy.add(tempBought.get(position));
                tempBought.remove(position);
                lv1Adapter = new CustomArrayAdapter(getActivity(), tempToBuy);
                lv2Adapter = new CustomArrayAdapter(getActivity(), tempBought);
                toBuyList.setAdapter(lv1Adapter);
                boughtList.setAdapter(lv2Adapter);
            }
        });

        return rootView;
    }


}
