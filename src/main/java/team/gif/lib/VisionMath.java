package team.gif.lib;

public class VisionMath {

    public double getPlanarDistance(double heightDiff, double theta) {
        return heightDiff / Math.tan(theta);
    }

    public double getCorrectedDistance(double heightDiff, double xTheta, double yTheta) {
        return getPlanarDistance(heightDiff, yTheta) / Math.cos(xTheta);
    }

    public double toDegrees(double pixels, double totalPixels, double fov) {
        return Math.tan(fov/2.0) * (pixels / totalPixels);
    }

    public double[] getRelativeSkew(double heightDiff, double xTheta, double yTheta, double pixelLength, double targetMaxWidth) {
        double dTheta = toDegrees(pixelLength, 320, 59.5);
        double correctedDistance = getCorrectedDistance(heightDiff, xTheta, yTheta);
        double phi = Math.asin((correctedDistance * Math.sin(dTheta)) / (targetMaxWidth/2));
        double[] result = {Math.toRadians(180.0) - dTheta - phi, phi - dTheta};
        return result;
    }
}
