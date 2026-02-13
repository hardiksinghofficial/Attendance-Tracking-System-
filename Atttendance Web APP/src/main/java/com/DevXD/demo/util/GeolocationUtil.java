package com.DevXD.demo.util;

public class GeolocationUtil {

    private static final double EARTH_RADIUS = 6371000; // meters

    /**
     * Calculate distance between two GPS coordinates using Haversine formula
     * @return distance in meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Check if location is within geofence
     */
    public static boolean isWithinGeofence(double userLat, double userLon,
                                           double officeLat, double officeLon,
                                           double radiusMeters) {
        double distance = calculateDistance(userLat, userLon, officeLat, officeLon);
        return distance <= radiusMeters;
    }
}