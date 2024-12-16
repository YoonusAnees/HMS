package com.example.luxe;

public class ServiceReservation {
    String serviceName;
    String reservationDate;

    public ServiceReservation(String serviceName, String reservationDate) {
        this.serviceName = serviceName;
        this.reservationDate = reservationDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }
}
