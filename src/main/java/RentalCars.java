import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jannatul on 19/02/16.
 */
public class RentalCars {
    public static void main(String[] args)  throws Exception{
//        ObjectMapper mapper = new ObjectMapper();
//        Search obj = mapper.readValue(new URL("http://www.rentalcars.com/js/vehicles.json"), Search.class);
//        System.out.println(obj);

        String json = readUrl("http://www.rentalcars.com/js/vehicles.json");
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("Search");
        JsonArray jarray = jobject.getAsJsonArray("VehicleList");
        listPrice(jarray);
    }

    private static List<VehicleList> getCarList(JsonArray jsonArray)
    {
        Gson gson = new Gson();
        List<VehicleList> cars = new ArrayList<VehicleList>();
        for (int i = 0; i< jsonArray.size(); i++) {
            JsonObject jobject = jsonArray.get(i).getAsJsonObject();
            VehicleList vehicle = gson.fromJson(jobject.toString(), VehicleList.class);
            cars.add(vehicle);
        }
        return cars;
    }

    public static void listPrice(JsonArray carList) {
        List<VehicleList> cars = getCarList(carList);

        Collections.sort(cars, new Comparator<VehicleList>() {
            @Override
            public int compare(VehicleList v1, VehicleList v2) {
                if((v1.getPrice() - v2.getPrice()) > 0) {
                    return -1;
                }
                else if ((v1.getPrice() - v2.getPrice()) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });

        for (VehicleList car: cars) {
            System.out.println(car.getName() + " - " + car.getPrice());
        }

    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static String parse(String jsonLine) {
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("Search");
        JsonArray jarray = jobject.getAsJsonArray("VehicleList");
        jobject = jarray.get(0).getAsJsonObject();
        String result = jobject.get("sipp").toString();
        System.out.println(result);
        return result;
    }
}
