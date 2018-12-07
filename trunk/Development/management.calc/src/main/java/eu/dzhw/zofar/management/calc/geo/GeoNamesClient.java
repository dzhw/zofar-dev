/*
 * 
 */
package eu.dzhw.zofar.management.calc.geo;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GeoNamesClient extends AbstractGeoClient implements Serializable {

	private static final long serialVersionUID = 5580442445809957803L;
	
	private final Map<String, Map<String, List<Map<String, String>>>> postCodeIndex;
	private final Map<String, Map<String, List<Map<String, String>>>> nameIndex;

	public GeoNamesClient(final List<Map<String, String>> database) {
		super();
		this.postCodeIndex = new TreeMap<String, Map<String, List<Map<String, String>>>>();
		this.nameIndex = new TreeMap<String, Map<String, List<Map<String, String>>>>();
		this.indexing(database);

	}

//	public LocalGeoClient(final String username) {
//		super();
//		this.asService = true;
////		this.username = username;
//		this.postCodeIndex = null;
//		this.nameIndex = null;
//	}

	private void indexing(final List<Map<String, String>> database) {
		if (database == null)
			return;
		if (database.isEmpty())
			return;
		for (final Map<String, String> row : database) {
			final String country = row.get("countrycode").toUpperCase();
			final String placename = row.get("placename").toUpperCase();
			final String postalcode = row.get("postalcode");
			final String state = row.get("adminname1").toUpperCase();

			Map<String, List<Map<String, String>>> nationalCodeIndex = this.postCodeIndex.get(country);
			if (nationalCodeIndex == null)nationalCodeIndex = new TreeMap<String, List<Map<String, String>>>();
			
			List<Map<String, String>> rowList = nationalCodeIndex.get(postalcode);
			if (rowList == null)rowList = new ArrayList<Map<String, String>>();
			rowList.add(row);
			nationalCodeIndex.put(postalcode, rowList);

			this.postCodeIndex.put(country, nationalCodeIndex);

			Map<String, List<Map<String, String>>> nationalNameIndex = this.nameIndex.get(country);
			if (nationalNameIndex == null)
				nationalNameIndex = new TreeMap<String, List<Map<String, String>>>();

			List<Map<String, String>> postCodesForName = nationalNameIndex.get(placename);
			if (postCodesForName == null) postCodesForName = new ArrayList<Map<String, String>>();
			postCodesForName.add(row);

			nationalNameIndex.put(placename, postCodesForName);
			this.nameIndex.put(country, nationalNameIndex);
		}
	}

	public Map<String, List<Map<String, String>>> getPlacesByName(final String pattern, final String countrycode) throws Exception {
			return this.getPlacesByNameFromLocal(pattern, countrycode);
	}
	
	private Map<String, List<Map<String, String>>> getPlacesByNameFromLocal(final String pattern, final String countrycode) {
		final List<String> useCountryCodes = new ArrayList<String>();
		if (countrycode == null) {
			useCountryCodes.addAll(this.nameIndex.keySet());
		} else {
			useCountryCodes.add(countrycode);
		}

		final String key = pattern.toUpperCase();

		final Map<String, List<Map<String, String>>> back = new TreeMap<String, List<Map<String, String>>>();

		for (final String code : useCountryCodes) {
			final String country = code.toUpperCase();
			
			List<Map<String, String>> tmp = back.get(code);
			if(tmp == null)tmp = new ArrayList<Map<String, String>>();
			
			final Map<String, List<Map<String, String>>> nationalNameIndex = this.nameIndex.get(country);
			if (nationalNameIndex != null) {
				for (final String name : nationalNameIndex.keySet()) {
					if (name.matches(key)) {
						final List<Map<String, String>> found = nationalNameIndex.get(name);
						for (final Map<String, String> item : found) {
							tmp.add(item);
						}
					}
				}
			}
			back.put(code, tmp);
		}
		return back;
	}

	private Map<String, List<Map<String, String>>> getPlacesByNameFromService(final String city) throws Exception {
		return null;
	}

	public Map<String, List<Map<String, String>>> getPlacesByPostCode(final String plz, final String countrycode) throws Exception {
			return this.getPlacesByPostCodeFromLocal(plz, countrycode);
	}

	private Map<String, List<Map<String, String>>> getPlacesByPostCodeFromLocal(final String plz, final String countrycode) {
		final List<String> useCountryCodes = new ArrayList<String>();
		if (countrycode == null) {
			// useCountryCodes.addAll(this.postCodeIndex.keySet());
		} else {
			useCountryCodes.add(countrycode);

		}
		final Map<String, List<Map<String, String>>> back = new TreeMap<String, List<Map<String, String>>>();

		for (final String code : useCountryCodes) {
			final Map<String, List<Map<String, String>>> nationalCodeIndex = this.postCodeIndex.get(code);

			if (nationalCodeIndex != null) {

				final List<Map<String, String>> item = nationalCodeIndex.get(plz);
				if (item != null) {
					back.put(code, item);
				}
			}
		}
		return back;
	}
	
	// Get Meta Informations
	
	public Point2D getLocation(final Object data) throws Exception{
		if(data == null)return null;
		final Double latitude = Double.parseDouble(((Map<String, String>)data).get("latitude"));
		final Double longitude = Double.parseDouble(((Map<String, String>)data).get("longitude"));
		return new Point2D.Double(latitude,longitude);
	}


}
