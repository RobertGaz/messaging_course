package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VehicleDistanceService {

    private final ConcurrentHashMap<String, Double> vehicleDistances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, VehicleCoordinates> vehicleLastCoordinates = new ConcurrentHashMap<>();

    public Double calculateVehicleDistance(VehicleCoordinates vehicleCoordinates) {
        String vehicleId = vehicleCoordinates.getVehicleId();
        if (vehicleLastCoordinates.get(vehicleId) != null) {
            double xIncrement = vehicleCoordinates.getX() - vehicleLastCoordinates.get(vehicleId).getX();
            double yIncrement = vehicleCoordinates.getY() - vehicleLastCoordinates.get(vehicleId).getY();
            double distanceIncrement = Math.sqrt(xIncrement * xIncrement + yIncrement * yIncrement);
            vehicleDistances.put(vehicleId, vehicleDistances.get(vehicleId) + distanceIncrement);
        } else {
            vehicleDistances.put(vehicleId, (double) 0);
        }
        vehicleLastCoordinates.put(vehicleId, vehicleCoordinates);

        return vehicleDistances.get(vehicleId);
    }

}
