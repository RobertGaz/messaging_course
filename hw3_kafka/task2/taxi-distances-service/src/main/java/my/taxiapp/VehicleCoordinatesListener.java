package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class VehicleCoordinatesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleCoordinatesListener.class);
    private static final String VEHICLE_COORDINATES_TOPIC = "vehicle_coordinates_topic";
    private static final String VEHICLE_DISTANCES_TOPIC = "vehicle_distances_topic";

    @Autowired
    private VehicleDistanceService vehicleDistanceService;

    @KafkaListener(topics = VEHICLE_COORDINATES_TOPIC)
    @SendTo(VEHICLE_DISTANCES_TOPIC)
    public Message<Double> processCoordinates(@Payload VehicleCoordinates vehicleCoordinates,
                                             @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        LOGGER.info("Received {} from partition {} of topic '{}'", vehicleCoordinates, partition, VEHICLE_COORDINATES_TOPIC);

        String vehicleId = vehicleCoordinates.getVehicleId();
        Double vehicleDistance = vehicleDistanceService.calculateVehicleDistance(vehicleCoordinates);

        LOGGER.info("Calculated latest distance info ({}) for vehicle '{}', sending to '{}' topic...", vehicleDistance, vehicleId, VEHICLE_DISTANCES_TOPIC);

        return MessageBuilder.withPayload(vehicleDistance)
				.setHeader(KafkaHeaders.KEY, vehicleId).build();
    }
}
