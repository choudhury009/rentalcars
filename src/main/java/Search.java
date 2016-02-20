import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jannatul on 19/02/16.
 */
public class Search {
    private List<VehicleList> vehicleList;

    public Search(){

    }

    public Search(List<VehicleList> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public List<VehicleList> getVehicleList() {
        if (vehicleList == null) {
            return new ArrayList<VehicleList>();
        }

        return vehicleList;
    }

    public void setVehicleList(List<VehicleList> vehicleList) {
        this.vehicleList = vehicleList;
    }



}
