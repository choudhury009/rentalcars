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
        // read and parse the json file
        String json = readUrl("http://www.rentalcars.com/js/vehicles.json");
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("Search");
        JsonArray jarray = jobject.getAsJsonArray("VehicleList");
        // list vehicles in price order
        listPrice(jarray);
        // table spec and breakdown tasks
        specAndBreakdown(jarray);
        // highest rated supplier
        highestRatedSupplier(jarray);

        getHighestRating(jarray);

        listCarByScore(jarray);
    }

    private static List<VehicleList> getCarList(JsonArray jsonArray)
    {
        // save a list of all the cars
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
        // retrieve all the cars
        List<VehicleList> cars = getCarList(carList);
        // sort the cars price order
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

        System.out.println("A list of all cars in ascending price order");
        for (int i = 0; i< cars.size(); i++) {
            System.out.println(i+1 + ". " + cars.get(i).getName() + " - " + cars.get(i).getPrice());
        }
    }


    public static void specAndBreakdown(JsonArray carList) {
        List<VehicleList> cars = getCarList(carList);

        System.out.println("\nSIPP specification of all cars");
        for (int i = 0; i< cars.size(); i++) {
            String details = getCarDetail(cars.get(i).getSipp());
            System.out.println(i + 1 + ". " + cars.get(i).getName() + " - " + details);
        }
    }

    public static void highestRatedSupplier(JsonArray carList)
    {
        List<VehicleList> cars = getCarList(carList);
        List<String> carType = new ArrayList<String>();
        Map<String, String> allCarTypes = new HashMap<String, String>();
        Map<String, String> breakdownRating = new HashMap<String, String>();

        System.out.println("\nHighest rated supplier per car type, descending order");
        for (int i = 0; i< cars.size(); i++) {
            char char0 = cars.get(i).getSipp().charAt(0);
            char char1 = cars.get(i).getSipp().charAt(1);
            String str = new StringBuilder().append(char0).toString();
            String defineType = getCarDetail(str);

            String value = allCarTypes.get(defineType);
            String value2 = allCarTypes.get("Special");

            if (value == null) {
                allCarTypes.put(defineType, cars.get(i).getName() + " - " + cars.get(i).getSupplier() + " - " + cars.get(i).getRating());
            }
//
            if (value2 == null) {
                if (char1 == 'X')
                    allCarTypes.put("Special", cars.get(i).getName() + " - " + cars.get(i).getSupplier() + " - " + cars.get(i).getRating());
            }
//
//            String rating = breakdownRating.get(cars.get(i).getName());
//            if (rating == null) {
//                Double total = score + cars.get(i).getRating();
//                breakdownRating.put(cars.get(i).getName(), score + " - " + cars.get(i).getRating() + " - " + total);
//            }
        }

        Iterator it = allCarTypes.entrySet().iterator();
        int index = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            index++;
            System.out.println(index + ". " + pair.getKey() + " - " + pair.getValue());
        }
//
//        System.out.println("\nlist of vehicles, ordered by the sum of the scores in descending order");
//        Iterator iter = breakdownRating.entrySet().iterator();
//        int ratingIndex = 0;
//
//        while (iter.hasNext()) {
//            Map.Entry pair = (Map.Entry)iter.next();
//            ratingIndex++;
//            System.out.println(ratingIndex + ". " + pair.getKey() + " - " + pair.getValue());
//        }
    }

    public static void supplierRating(JsonArray carList) {

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

    public static String getCarDetail(String sipp)
    {
        char[] charArray = sipp.toCharArray();
        String carDetail = "";
        switch (charArray[0])
        {
            case 'M':
                carDetail = "Mini";
                break;
            case 'E':
                carDetail = "Economy";
                break;
            case 'C':
                carDetail = "Compact";
                break;
            case 'I':
                carDetail = "Intermediate";
                break;
            case 'S':
                carDetail = "Standard";
                break;
            case 'F':
                carDetail = "Full size";
                break;
            case 'P':
                carDetail = "Premium";
                break;
            case 'L':
                carDetail = "Luxury";
                break;
            case 'X':
                carDetail = "Special";
                break;
            default:
                break;
        }

        if (charArray.length > 1) {
            switch (charArray[1])
            {
                case 'B':
                    carDetail += " - 2 doors";
                    break;
                case 'C':
                    carDetail += " - 4 doors";
                    break;
                case 'D':
                    carDetail += " - 5 doors";
                    break;
                case 'W':
                    carDetail += " - Estate";
                    break;
                case 'T':
                    carDetail += " - Convertible";
                    break;
                case 'F':
                    carDetail += " - SUV";
                    break;
                case 'P':
                    carDetail += " - Pick up";
                    break;
                case 'V':
                    carDetail += " - passenger van";
                    break;
                case 'X':
                    carDetail += " - Special";
                    break;
                default:
                    break;
            }
        }

        if (charArray.length > 2) {
            switch (charArray[2]) {
                case 'M':
                    carDetail += " - Manual";
                    break;
                case 'A':
                    carDetail += " - Automatic";
                    break;
                default:
                    break;
            }
        }

        if (charArray.length > 3) {
            switch (charArray[3]) {
                case 'N':
                    carDetail += " - petrol - no air conditioning";
                    break;
                case 'R':
                    carDetail += " - petrol - air conditioning";
                    break;
                default:
                    break;
            }
        }

        return carDetail;
    }

    public static void getHighestRating(JsonArray carList) {
        List<VehicleList> cars = getCarList(carList);
        // sort the cars price order
        Collections.sort(cars, new Comparator<VehicleList>() {
            @Override
            public int compare(VehicleList v1, VehicleList v2) {
                if((v1.getRating() - v2.getRating()) > 0) {
                    // ascending order
                    return -1;
                }
                else if ((v1.getRating() - v2.getRating()) < 0) {
                    // descending order
                    return 1;
                } else {
                    return 0;
                }
            }

        });

        List<String> existedType = new ArrayList<String>();
        int index = 0;
        for (VehicleList car: cars) {
            String carType = getCarDetail(car.getSipp()).split(" - ")[0];
            if (!existedType.contains(carType)) {
                existedType.add(carType);
                index++;
                System.out.println(index + ". " + car.getName() + " - " + carType + " - " + car.getSupplier() + " - " + car.getRating());
            }
        }
    }

    public static void listCarByScore(JsonArray carList) {
        List<VehicleList> cars = getCarList(carList);
        Map<String, Double> allCarTypes = new HashMap<String, Double>();
        int index = 0;
        System.out.println("\nlist of vehicles with sum of scores");
        for (VehicleList car: cars) {
            String transmission = getCarDetail(car.getSipp()).split(" - ")[2];
            String aircon = getCarDetail(car.getSipp()).split(" - ")[4];
            int transScore = getPoint(transmission);
            int airconScore = getPoint(aircon);
            int totalBreakdownScore = airconScore + transScore;
            index++;
            Double totalScore = getTotalScore(car.getRating(), totalBreakdownScore);
            allCarTypes.put(car.getName() + " - " + totalBreakdownScore + " - " + car.getRating() + " - ", totalScore);
        }

        Map sortedMap = SortByValue(allCarTypes);
        Iterator iter = sortedMap.entrySet().iterator();
        int ratingIndex = 0;

        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry)iter.next();
            ratingIndex++;
            System.out.println(ratingIndex + ". " + pair.getKey() + " - " + pair.getValue());
        }

    }

    public static Map SortByValue(Map unsortedMap) {
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public static Double getTotalScore(Double rating, int score) {
        return rating + score;
    }

    public static int getPoint(String breakdown)
    {
        int score = 0;
        if (breakdown.equals("Manual")) {
            score += 1;
        } else if (breakdown.equals("Automatic")) {
            score += 5;
        } else if (breakdown.equals("air conditioning")) {
            score += 2;
        }

        return score;

    }
}
