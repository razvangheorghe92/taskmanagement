package razvan.gheorghe.taskmanagement.adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import razvan.gheorghe.taskmanagement.R;
import razvan.gheorghe.taskmanagement.objects.Product;

public class CustomArrayAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    ArrayList<Product> products = new ArrayList<Product>();

    public CustomArrayAdapter(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int pos) {
        return products.get(pos);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.product_row, null);
        }

//        CustomNumberPicker nPCant = (CustomNumberPicker) view.findViewById(R.id.nPCant);
        Spinner spUnit = (Spinner) view.findViewById(R.id.spUnitate);
        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        EditText edtCant = (EditText ) view.findViewById(R.id.edtCant);

        Product tempProduct = new Product();
        tempProduct = products.get(position);

        txtName.setText("    " + tempProduct.getName());
        edtCant.setText(tempProduct.getQuantity());
        spUnit.setSelection(getIndex(spUnit, tempProduct.getUnit()));

            if (products.size() > 0) {
                if (position % 2 == 0)
                    view.setBackgroundResource(R.drawable.marcaj_event_albastru);
                else
                    view.setBackgroundResource(R.drawable.marcaj_event_portocaliu);

            }
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Marker Felt.ttf");




        return view;
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }


}