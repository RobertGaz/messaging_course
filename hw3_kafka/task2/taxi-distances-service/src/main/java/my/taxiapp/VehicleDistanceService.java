package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VehicleDistanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleDistanceService.class);
    private static final String VEHICLE_DISTANCES_TOPIC = "vehicle_distances_topic";

    private final ConcurrentHashMap<String, Double> vehicleDistances = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, VehicleCoordinates> vehicleLastCoordinates = new ConcurrentHashMap<>();

    @Autowired
    private KafkaTemplate<String, Double> kafkaTemplate;

    public void processVehicleCoordinates(VehicleCoordinates vehicleCoordinates) {
        updateVehicleDistanceAndLastCoordinates(vehicleCoordinates);

        String vehicleId = vehicleCoordinates.getVehicleId();
        Double vehicleDistance = vehicleDistances.get(vehicleId);
        kafkaTemplate.send(VEHICLE_DISTANCES_TOPIC, vehicleId, vehicleDistance);

        LOGGER.info("Sent latest distance info ({}) for vehicle {} to {} topic.",
                vehicleDistance, vehicleId, VEHICLE_DISTANCES_TOPIC);
    }

    private void updateVehicleDistanceAndLastCoordinates(VehicleCoordinates vehicleCoordinates) {
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
    }
}
