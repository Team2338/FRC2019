package team.gif.lib;

public class VisionMath {

    public static double getPlanarDistance(double heightDiff, double yTheta) {
        return heightDiff / Math.tan(yTheta);
    }

    /**
     * Uses some trig to find the distance from the sensor to the center of the target.
     * Or the distance from whatever to whatever, you could plug in values from any kind of sensor in relation
     * to a target
     *
     * @param heightDiff height difference between sensor and target center in desired units
     * @param xTheta horizontal angle difference between sensor and target in radians
     * @param yTheta vertical angle difference between sensor and target in radians
     * @return absolute distance from sensor to target in heightDiff units
     */
    public static double getCorrectedDistance(double heightDiff, double xTheta, double yTheta) {
        return getPlanarDistance(heightDiff, yTheta) / Math.cos(xTheta);
    }

    public static double pixelsToDegrees(double pixels, double totalPixels, double fov) {
        return Math.atan(pixels / (totalPixels/2.0) / Math.tan(fov/2.0));
    }

    public static double nPixelToDegrees(double np, double fov) {
        return Math.atan2(1, Math.tan(fov/2) / np);
    }

    public double[] getRelativeSkew(double heightDiff, double xTheta, double yTheta, double pixelLength, double targetMaxWidth) {
        double dTheta = pixelsToDegrees(pixelLength, 320, 59.5);
        double correctedDistance = getCorrectedDistance(heightDiff, xTheta, yTheta);
        double phi = Math.asin((correctedDistance * Math.sin(dTheta)) / (targetMaxWidth/2));
        double[] result = {Math.toRadians(180.0) - dTheta - phi, phi - dTheta};
        return result;
    }
}
