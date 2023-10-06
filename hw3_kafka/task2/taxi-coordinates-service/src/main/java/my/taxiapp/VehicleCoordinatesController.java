package my.taxiapp;

import my.taxiapp.model.VehicleCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleCoordinatesController {

    @Autowired
    private VehicleCoordinatesService vehicleCoordinatesService;

    @PostMapping("/send_coordinates")
    public String dispatchCoordinates(@RequestBody VehicleCoordinates vehicleCoordinates) {
        vehicleCoordinatesService.processVehicleCoordinates(vehicleCoordinates);

        return "OK";
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleUserError(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
