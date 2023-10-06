package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class VehicleCoordinatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleCoordinatesListener.class);
    private static final String VEHICLE_COORDINATES_TOPIC = "vehicle_coordinates_topic";

    @Autowired
    private VehicleDistanceService vehicleDistanceService;

    @KafkaListener(topics = VEHICLE_COORDINATES_TOPIC)
    public void getVehicleCoordinates(@Payload VehicleCoordinates vehicleCoordinates,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info("Received {} from partition {}", vehicleCoordinates, partition);
        vehicleDistanceService.processVehicleCoordinates(vehicleCoordinates);
    }
}
