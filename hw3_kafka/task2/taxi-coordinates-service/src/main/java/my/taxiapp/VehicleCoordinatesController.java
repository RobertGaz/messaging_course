package my.taxiapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleCoordinatesController {

    @Autowired
    private VehicleCoordinatesDispatcher vehicleCoordinatesDispatcher;

    @PostMapping("/send_coordinates")
    public String dispatchCoordinates(@RequestBody VehicleCoordinates vehicleCoordinates) {
        vehicleCoordinatesDispatcher.dispatchVehicleCoordinates(vehicleCoordinates);

        return "OK";
    }
}
