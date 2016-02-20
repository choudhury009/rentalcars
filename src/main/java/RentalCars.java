import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Created by jannatul on 19/02/16.
 */
public class RentalCars {
    public static void main(String[] args)  throws Exception {
        String json = readUrl("http://www.rentalcars.com/js/vehicles.json");
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("Search");
        JsonArray jarray = jobject.getAsJsonArray("VehicleList");
        System.out.println("A list of all cars in ascending price order");
        listPrice(jarray);
        System.out.println("\nSIPP specification of all cars");
        sippSpecification(jarray);
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
                    // ascending order
                    return 1;
                }
                else if ((v1.getPrice() - v2.getPrice()) < 0) {
                    // descending order
                    return -1;
                } else {
                    return 0;
                }
            }

        });

        for (int i = 0; i< cars.size(); i++) {
            System.out.println(i+1 + ". " + cars.get(i).getName() + " - " + cars.get(i).getPrice());
        }
    }

    public static void sippSpecification(JsonArray carList) {
        List<VehicleList> cars = getCarList(carList);
        Map<String, Double> allCarTypes = new HashMap<String, Double>();

        for (int i = 0; i< cars.size(); i++) {
            char firstLetter = cars.get(i).getSipp().charAt(0);
            char secondLetter = cars.get(i).getSipp().charAt(1);
            char thirdLetter = cars.get(i).getSipp().charAt(2);
            char fourthLetter = cars.get(i).getSipp().charAt(3);
            String carType, doorsAndType, transmission, aircon = "";
            String fuel = "Petrol";
            if (firstLetter == 'M') {
                carType = "Mini";
            } else if (firstLetter == 'E') {
                carType = "Economy";
            } else if (firstLetter == 'C') {
                carType = "Compact";
            } else if (firstLetter == 'I') {
                carType = "Intermediate";
            } else if (firstLetter == 'S') {
                carType = "Standard";
            } else if (firstLetter == 'F') {
                carType = "Full size";
            } else if (firstLetter == 'P') {
                carType = "Premium";
            } else if (firstLetter == 'L') {
                carType = "Luxury";
            } else if (firstLetter == 'X') {
                carType = "Special";
            } else {
                carType = "undefined car type";
            }

            if (secondLetter == 'B') {
                doorsAndType = "2 doors";
            } else if (secondLetter == 'C') {
                doorsAndType = "4 doors";
            } else if (secondLetter == 'D') {
                doorsAndType = "5 doors";
            } else if (secondLetter == 'W') {
                doorsAndType = "Estate";
            } else if (secondLetter == 'T') {
                doorsAndType = "Convertible";
            } else if (secondLetter == 'F') {
                doorsAndType = "SUV";
            } else if (secondLetter == 'P') {
                doorsAndType = "Pick up";
            } else if (secondLetter == 'V') {
                doorsAndType = "Passenger Van";
            } else {
                if (secondLetter == 'X') {
                    doorsAndType = "Special";
                } else {
                    doorsAndType  = "undefined doors/car type";
                }
            }

            if (thirdLetter == 'M') {
                transmission = "Manual";
            } else if (thirdLetter == 'A') {
                transmission = "Automatic";
            } else {
                transmission = "undefined transmission";
            }

            if (fourthLetter == 'N') {
                aircon = "no air conditioning";
            } else if (fourthLetter == 'R') {
                aircon = "air conditioning";
            } else {
                aircon = "aircon not defined";
            }

            System.out.println(i + 1 + ". " + cars.get(i).getName() + " - " + cars.get(i).getSipp()
                    + " - " + carType + " - " + doorsAndType + " - "
                    + transmission + " - " + fuel + " - " + aircon);

            Double value = allCarTypes.get(carType);
            Double value2 = allCarTypes.get(doorsAndType);
            if (value == null) {
                allCarTypes.put(carType, cars.get(i).getRating());
            }
            if (value2 == null) {
                if (doorsAndType != "2 doors" && doorsAndType != "4 doors" && doorsAndType != "5 doors" && doorsAndType != "Passenger Van")
                allCarTypes.put(doorsAndType, cars.get(i).getRating());
            }
        }
        System.out.println("\nhighest rated supplier per car type, descending order");
        System.out.println(allCarTypes);
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
}
