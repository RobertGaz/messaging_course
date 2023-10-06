package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class VehicleCoordinatesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleCoordinatesService.class);
    private static final String VEHICLE_COORDINATES_TOPIC = "vehicle_coordinates_topic";

    @Autowired
    private KafkaTemplate<String, VehicleCoordinates> kafkaTemplate;

    public void processVehicleCoordinates(VehicleCoordinates vehicleCoordinates) {
        validateVehicleCoordinates(vehicleCoordinates);

        CompletableFuture<SendResult<String, VehicleCoordinates>> future = kafkaTemplate.send(VEHICLE_COORDINATES_TOPIC, vehicleCoordinates.getVehicleId(), vehicleCoordinates);
        SendResult<String, VehicleCoordinates> sendResult;
        try {
            sendResult = future.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        LOGGER.info("Sent {} to kafka. Topic: {}, partition: {}, offset: {}.",
                vehicleCoordinates, VEHICLE_COORDINATES_TOPIC, sendResult.getRecordMetadata().partition(), sendResult.getRecordMetadata().offset());
    }

    private void validateVehicleCoordinates(VehicleCoordinates vehicleCoordinates) {
        if (vehicleCoordinates.getVehicleId().contains("!")) {
            throw new IllegalArgumentException("Invalid vehicle id");
        }

        if (Math.abs(vehicleCoordinates.getX()) > 1000000 || Math.abs(vehicleCoordinates.getY()) > 1000000) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }
}
