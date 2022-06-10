package br.com.app_android_ecommerce.utils;

public class CalcularDistancia {

    public double distancia(double lat1, double lon1, double lat2, double lon2, char unid) {
        double theta = lon1 - lon2;

        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) + Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.cos(dist);
            dist = rad2deg(dist);

            dist = dist * 60 * 1.1515;

            if (unid == 'k') {
                dist = dist * 1.609344;
            } else if (unid == 'N') {
                dist = dist * 0.8684;
            }
            return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
