package my.taxiapp;

public class VehicleCoordinates {
    private String vehicleId;
    private double x;
    private double y;

    public VehicleCoordinates(String vehicleId, double x, double y) {
        this.vehicleId = vehicleId;
        this.x = x;
        this.y = y;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "VehicleCoordinates{" +
                "vehicleId='" + vehicleId + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
