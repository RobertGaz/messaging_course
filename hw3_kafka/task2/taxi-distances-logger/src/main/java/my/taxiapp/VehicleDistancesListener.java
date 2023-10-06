package my.taxiapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class VehicleDistancesListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleDistancesListener.class);
    private static final String VEHICLE_DISTANCES_TOPIC = "vehicle_distances_topic";

    @KafkaListener(topics = VEHICLE_DISTANCES_TOPIC)
    public void getVehicleDistance(Double vehicleDistance,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String vehicleId) {
        LOGGER.info("I received info that vehicle {} overall destination is {}.", vehicleId, vehicleDistance);
    }

}
