package my.taxiapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleCoordinatesController {

    @Autowired
    private VehicleCoordinatesService vehicleCoordinatesService;

    @PostMapping("/send_coordinates")
    public String addCoordinates(@RequestBody VehicleCoordinates vehicleCoordinates) {
        vehicleCoordinatesService.processVehicleCoordinates(vehicleCoordinates);

        return "OK";
    }
}
