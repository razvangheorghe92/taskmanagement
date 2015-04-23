package razvan.gheorghe.taskmanagement.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Product {
    String idProd;
    String name;
    String quantity;
    String unit;

    public Product() {}

    public Product(String name, String quantity, String unit) {
        this.idProd = UUID.randomUUID().toString();
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Product(String idProd, String name, String quantity, String unit) {
        this.idProd = idProd;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String toJson() {
        JSONObject jsn = new JSONObject();

        try {
            jsn.put("name",name);
            jsn.put("quantity", quantity);
            jsn.put("unit", unit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsn.toString();
    }

    public Product fromJson(JSONObject jsn) {

        try {
            this.name = jsn.getString("name");
            this.quantity = jsn.getString("quantity");
            this.unit = jsn.getString("unit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    public String getIdProd() {
        return idProd;
    }

    public void setIdProd(String idProd) {
        this.idProd = idProd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}