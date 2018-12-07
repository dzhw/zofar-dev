package eu.dzhw.zofar.management.calc.geo;

import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class AbstractGeoClient implements Serializable {
	
	/**
	 * mean radius = 6372.0
	 * 
	 * The Earth's equatorial radius = 6335.437 km.
	 * 
	 * The Earth's polar radius = 6399.592 km.
	 * 
	 * 
	 */
	private static final double EARTH_RADIUS_KM = 6372.0;

	/**
	 * statute miles
	 */
	private static final double EARTH_RADIUS_MILES = 3963.0;

	public AbstractGeoClient() {
		super();
	}
	
	public abstract Point2D getLocation(final Object location) throws Exception;
	
	// Calculate Distance and Bearing

	/**
	 * http://mathworld.wolfram.com/GreatCircle.html
	 * 
	 * and
	 * 
	 * http://www.mathforum.com/library/drmath/view/51711.html
	 * 
	 * @return
	 */
	private double distance(final double lat1, final double lng1, final double lat2, final double lng2, final char unit, final int numberOfDigits) {
		final double a1 = Math.toRadians(lat1);
		final double b1 = Math.toRadians(lng1);
		final double a2 = Math.toRadians(lat2);
		final double b2 = Math.toRadians(lng2);
		final double d = Math.acos(Math.cos(a1) * Math.cos(b1) * Math.cos(a2) * Math.cos(b2) + Math.cos(a1) * Math.sin(b1) * Math.cos(a2) * Math.sin(b2) + Math.sin(a1) * Math.sin(a2));

		double dist = 0;
		if (unit == 'M') {
			dist = d * EARTH_RADIUS_MILES;
		} else {
			dist = d * EARTH_RADIUS_KM;
		}

		if (Double.isNaN(dist)) {
			// use pytagoras for very small distances,
			dist = Math.sqrt(Math.pow(Math.abs(lat1 - lat2), 2) + Math.pow(Math.abs(lng1 - lng2), 2));
			// as rule of thumb multiply with 110km =1 degree
			if (unit == 'M') {
				dist *= 69;
			} else {
				dist *= 110;
			}
		}

		if (numberOfDigits == 0) {
			dist = (int) dist;
		} else if (numberOfDigits > 0) {
			final double factor = Math.pow(10, numberOfDigits);
			dist = Math.floor(dist * factor) / factor;
		}
		return dist;
	}
	
	public double distanceKM(final Point2D point1, final Point2D point2) {
		if(point1 == null)return -1D;
		if(point2 == null)return -1D;
		return this.distance(point1.getX(), point1.getY(), point2.getX(), point2.getY(), 'K', 3);
	}

	public double distanceKM(final double lat1, final double lng1, final double lat2, final double lng2) {
		return this.distance(lat1, lng1, lat2, lng2, 'K', 3);
	}
	
	public double distanceMiles(final Point2D point1, final Point2D point2) {
		return this.distance(point1.getX(), point1.getY(), point2.getX(), point2.getY(), 'M', 3);
	}

	public double distanceMiles(final double lat1, final double lng1, final double lat2, final double lng2) {
		return this.distance(lat1, lng1, lat2, lng2, 'M', 3);
	}
	
	public double bearing(final Point2D point1, final Point2D point2) {
		return this.bearing(point1.getX(), point1.getY(), point2.getX(), point2.getY());
	}

	public double bearing(double lat1, final double lng1, double lat2, final double lng2) {
		final double dLon = Math.toRadians(lng2 - lng1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		final double y = Math.sin(dLon) * Math.cos(lat2);
		final double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
		final double brng = Math.toDegrees(Math.atan2(y, x));
		return (brng + 360) % 360;
	}

}
