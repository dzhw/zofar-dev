package de.his.zofar.presentation.surveyengine.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.faces.component.visit.FullVisitContext;

import de.his.zofar.presentation.surveyengine.AbstractAnswerBean;
import de.his.zofar.presentation.surveyengine.BooleanValueTypeBean;
import de.his.zofar.presentation.surveyengine.NumberValueTypeBean;
import de.his.zofar.presentation.surveyengine.SingleChoiceAnswerOptionTypeBean;
import de.his.zofar.presentation.surveyengine.StringValueTypeBean;
import de.his.zofar.presentation.surveyengine.controller.SessionController;
import de.his.zofar.presentation.surveyengine.ui.components.composite.calendar.UICalendar;
import de.his.zofar.presentation.surveyengine.ui.components.composite.calendar.UICalendarItem;
import de.his.zofar.presentation.surveyengine.ui.components.composite.calendar.UICalendarSheet;
import de.his.zofar.presentation.surveyengine.ui.interfaces.IAnswerBean;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;
import de.his.zofar.presentation.surveyengine.util.SystemConfiguration;
import de.his.zofar.service.surveyengine.model.Participant;

/**
 * Bean to provide Zofar specific EL - Functions
 * 
 * @author meisner
 * 
 */
@ManagedBean(name = "zofar")
@ApplicationScoped
public class FunctionProvider implements Serializable {

	private static final long serialVersionUID = -78074230548327907L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionProvider.class);

	private final static DecimalFormatSymbols customFormatSymbols = new DecimalFormatSymbols(Locale.GERMAN);

	private final Random random;

	public FunctionProvider() {
		super();
		this.random = new Random();
	}

	@PostConstruct
	private void init() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("init");
		}

		customFormatSymbols.setDecimalSeparator('.');
	}

	public synchronized String getTimeStamp() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date date = new Date();
		String dateString = simpleDateFormat.format(date);
		return dateString;
	}

	public synchronized int getMonth(final AbstractAnswerBean var) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		int month = 0;
		if (var != null && (StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			String dateString = ((StringValueTypeBean) var).getValue();

			Date date = simpleDateFormat.parse(dateString);
			simpleDateFormat.applyPattern("M");
			month = Integer.parseInt(simpleDateFormat.format(date));
		}
		return month;
	}

	public synchronized int getYear(final AbstractAnswerBean var) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		int year = 0;
		if (var != null && (StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			String dateString = ((StringValueTypeBean) var).getValue();
			Date date = simpleDateFormat.parse(dateString);
			simpleDateFormat.applyPattern("YYYY");
			year = Integer.parseInt(simpleDateFormat.format(date));
		}
		return year;
	}

	public synchronized String getMonthLabel(final AbstractAnswerBean var) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		String label = "";
		if (var != null && (StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			String dateString = ((StringValueTypeBean) var).getValue();
			Date date = simpleDateFormat.parse(dateString);
			simpleDateFormat.applyPattern("MMMMM");
			label = simpleDateFormat.format(date);
		}
		return label;
	}

	public synchronized int getCalcDate(final AbstractAnswerBean var) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		int sum = 0;
		if (var != null && (StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			String dateString = ((StringValueTypeBean) var).getValue();
			Date date = simpleDateFormat.parse(dateString);
			simpleDateFormat.applyPattern("YYYY");
			int year = Integer.parseInt(simpleDateFormat.format(date));
			simpleDateFormat.applyPattern("M");
			int month = Integer.parseInt(simpleDateFormat.format(date));
			sum = ((year * 100) + month);
		}
		return sum;
	}

	public synchronized boolean recorderEnabled() {
		final SystemConfiguration system = SystemConfiguration.getInstance();
		return system.record();
	}

	public synchronized Object valueOf(final AbstractAnswerBean var) {
		// LOGGER.info("valueOf");
		if (var != null) {
			if ((BooleanValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((BooleanValueTypeBean) var).getValue();
			}
			if ((NumberValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((NumberValueTypeBean) var).getValue();
			}
			if ((StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((StringValueTypeBean) var).getValue();
			}
			if ((SingleChoiceAnswerOptionTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((SingleChoiceAnswerOptionTypeBean) var).getValue();
			}
		}
		return null;
	}

	public synchronized String labelOf(final AbstractAnswerBean var) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("labelOf : {}", var);
		}

		if (var != null) {
			if ((BooleanValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((BooleanValueTypeBean) var).getValue() + "";
			}
			if ((NumberValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((NumberValueTypeBean) var).getValue() + "";
			}
			if ((StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((StringValueTypeBean) var).getValue();
			}
			if ((SingleChoiceAnswerOptionTypeBean.class).isAssignableFrom(var.getClass())) {
				return ((SingleChoiceAnswerOptionTypeBean) var).getLabel();
			}
		}
		return null;
	}

	public synchronized String labelOfDropDown(final SingleChoiceAnswerOptionTypeBean var) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("labelOfDropDown : {}", var);
		}

		if (var != null) {
			final Map<String, Object> labels = ((SingleChoiceAnswerOptionTypeBean) var).getLabels();
			if ((labels != null) && (!labels.isEmpty())) {
				final String uid = var.getValueId();
				if ((uid != null) && (labels.containsKey(uid)))
					return (String) labels.get(uid);
			}
		}
		return null;
	}

	private synchronized <T> List<T> mergeLists(final List<T> list1, final List<T> list2) {
		if ((list1 == null) && (list2 == null))
			return null;
		final List<T> list = new ArrayList<T>();
		if (list1 != null)
			list.addAll(list1);
		if (list2 != null)
			list.addAll(list2);
		return list;
	}

	private synchronized <T, S> Map<T, S> mergeListsToMap(final List<T> keyList, final List<S> valueList) {
		final Iterator<T> i1 = keyList.iterator();
		final Iterator<S> i2 = valueList.iterator();
		final Map<T, S> map = new HashMap<T, S>();
		while (i1.hasNext() && i2.hasNext()) {
			map.put(i1.next(), i2.next());
		}
		return map;
	}

	public synchronized List<Object> list(final Object element1) {
		return listHelper(element1);
	}

	public synchronized List<Object> list(final Object element1, final Object element2) {
		return listHelper(element1, element2);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3) {
		return listHelper(element1, element2, element3);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4) {
		return listHelper(element1, element2, element3, element4);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5) {
		return listHelper(element1, element2, element3, element4, element5);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6) {
		return listHelper(element1, element2, element3, element4, element5, element6);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9);
	}

	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10);
	}

	// 11
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11);
	}

	// 12
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12);
	}

	// 13
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13);
	}

	// 14
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14);
	}

	// 15
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15);
	}

	// 16
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16);
	}

	// 17
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17);
	}

	// 18
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18);
	}

	// 21
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21);
	}

	// 22
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22);
	}

	// 23
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23);
	}

	// 24
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24);
	}

	// 25
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25);
	}

	// 26
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26);
	}

	// 27
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26, final Object element27) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26, element27);
	}

	// 28
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26, final Object element27,
			final Object element28) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26, element27,
				element28);
	}

	// 29
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26, final Object element27,
			final Object element28, final Object element29) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26, element27,
				element28, element29);
	}

	// 30
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26, final Object element27,
			final Object element28, final Object element29, final Object element30) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26, element27,
				element28, element29, element30);
	}

	// 83
	public synchronized List<Object> list(final Object element1, final Object element2, final Object element3,
			final Object element4, final Object element5, final Object element6, final Object element7,
			final Object element8, final Object element9, final Object element10, final Object element11,
			final Object element12, final Object element13, final Object element14, final Object element15,
			final Object element16, final Object element17, final Object element18, final Object element19,
			final Object element20, final Object element21, final Object element22, final Object element23,
			final Object element24, final Object element25, final Object element26, final Object element27,
			final Object element28, final Object element29, final Object element30, final Object element31,
			final Object element32, final Object element33, final Object element34, final Object element35,
			final Object element36, final Object element37, final Object element38, final Object element39,
			final Object element40, final Object element41, final Object element42, final Object element43,
			final Object element44, final Object element45, final Object element46, final Object element47,
			final Object element48, final Object element49, final Object element50, final Object element51,
			final Object element52, final Object element53, final Object element54, final Object element55,
			final Object element56, final Object element57, final Object element58, final Object element59,
			final Object element60, final Object element61, final Object element62, final Object element63,
			final Object element64, final Object element65, final Object element66, final Object element67,
			final Object element68, final Object element69, final Object element70, final Object element71,
			final Object element72, final Object element73, final Object element74, final Object element75,
			final Object element76, final Object element77, final Object element78, final Object element79,
			final Object element80, final Object element81, final Object element82, final Object element83) {
		return listHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10, element11, element12, element13, element14, element15, element16, element17, element18,
				element19, element20, element21, element22, element23, element24, element25, element26, element27,
				element28, element29, element30, element31, element32, element33, element34, element35, element36,
				element37, element38, element39, element40, element41, element42, element43, element44, element45,
				element46, element47, element48, element49, element50, element51, element52, element53, element54,
				element55, element56, element57, element58, element59, element60, element61, element62, element63,
				element64, element65, element66, element67, element68, element69, element70, element71, element72,
				element73, element74, element75, element76, element77, element78, element79, element80, element81,
				element82, element83);
	}

	public synchronized boolean inList(final List<Object> list, final Object pattern) {
		if (list == null)
			return false;
		if (pattern == null)
			return false;
		if (list.isEmpty())
			return false;
		return list.contains(pattern);
	}

	public synchronized Map<String, String> map(final String toParse) {
		return this.map(toParse, "=", ",");
	}

	public synchronized Map<String, String> map(final String toParse, final String pairSplit, final String itemSplit) {
		if (toParse == null)
			return null;
		if (toParse.equals(""))
			return null;
		if (pairSplit == null)
			return null;
		if (pairSplit.equals(""))
			return null;
		if (itemSplit == null)
			return null;
		if (itemSplit.equals(""))
			return null;
		Map<String, String> back = new LinkedHashMap<String, String>();

		if (!toParse.contains(itemSplit)) {
			if (toParse.contains(pairSplit)) {
				String[] pair = toParse.split(Pattern.quote(pairSplit));
				if ((pair != null) && (pair.length == 2)) {
					back.put(pair[0], pair[1]);
				}
			}
		} else {
			String[] items = toParse.split(Pattern.quote(itemSplit));
			if (items != null) {
				for (final String item : items) {
					if (item.contains(pairSplit)) {
						String[] pair = item.split(Pattern.quote(pairSplit));
						if ((pair != null) && (pair.length == 2)) {
							back.put(pair[0], pair[1]);
						}
					}
				}
			}
		}

		return back;
	}

	private List<Object> listHelper(Object... objects) {
		return (List<Object>) Arrays.asList(objects);
	}

	public synchronized List<Object> explode(final String toSplit, final String splitBy) {
		return listHelper(toSplit.split(splitBy));
	}

	public synchronized List<?> reverseList(final List<?> list) {
		// LOGGER.info("reverse list {}", list);
		if (list == null)
			return null;
		final List<?> reversedValues = new ArrayList<Object>(list);
		Collections.reverse(reversedValues);
		return reversedValues;
	}

	public synchronized String concat(final String element1, final String element2) {
		return concatHelper(element1, element2);
	}

	public synchronized String concat(final String element1, final String element2, final String element3) {
		return concatHelper(element1, element2, element3);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4) {
		return concatHelper(element1, element2, element3, element4);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5) {
		return concatHelper(element1, element2, element3, element4, element5);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5, final String element6) {
		return concatHelper(element1, element2, element3, element4, element5, element6);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5, final String element6, final String element7) {
		return concatHelper(element1, element2, element3, element4, element5, element6, element7);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5, final String element6, final String element7,
			final String element8) {
		return concatHelper(element1, element2, element3, element4, element5, element6, element7, element8);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5, final String element6, final String element7,
			final String element8, final String element9) {
		return concatHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9);
	}

	public synchronized String concat(final String element1, final String element2, final String element3,
			final String element4, final String element5, final String element6, final String element7,
			final String element8, final String element9, final String element10) {
		return concatHelper(element1, element2, element3, element4, element5, element6, element7, element8, element9,
				element10);
	}

	private String concatHelper(String... objects) {
		if (objects == null)
			return null;
		return StringUtils.join(objects);
	}

	public synchronized List<?> optionsOf(final AbstractAnswerBean var) {
		// LOGGER.info("get Options");
		if (var == null)
			return null;

		if ((BooleanValueTypeBean.class).isAssignableFrom(var.getClass())) {
			return new ArrayList<Boolean>();
		}
		if ((NumberValueTypeBean.class).isAssignableFrom(var.getClass())) {
			return new ArrayList<Number>();
		}
		if ((StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			return new ArrayList<String>();
		}
		if ((SingleChoiceAnswerOptionTypeBean.class).isAssignableFrom(var.getClass())) {
			final SingleChoiceAnswerOptionTypeBean tmp = (SingleChoiceAnswerOptionTypeBean) var;
			final Map<String, Object> options = tmp.getOptionValues();
			List<Object> valueList = new ArrayList<Object>(options.values());
			valueList.removeAll(missingsOf(var));
			return valueList;
		}
		return null;
	}

	public synchronized List<?> missingsOf(final AbstractAnswerBean var) {
		// LOGGER.info("get Missings");
		if (var == null)
			return null;

		if ((BooleanValueTypeBean.class).isAssignableFrom(var.getClass())) {
			return new ArrayList<Boolean>();
		}
		if ((NumberValueTypeBean.class).isAssignableFrom(var.getClass())) {
			final List<Object> missingList = new ArrayList<Object>();
			missingList.add("");
			return missingList;
		}
		if ((StringValueTypeBean.class).isAssignableFrom(var.getClass())) {
			final List<Object> missingList = new ArrayList<Object>();
			missingList.add("");
			return missingList;
		}
		if ((SingleChoiceAnswerOptionTypeBean.class).isAssignableFrom(var.getClass())) {
			final SingleChoiceAnswerOptionTypeBean tmp = (SingleChoiceAnswerOptionTypeBean) var;
			final List<Object> missingList = new ArrayList<Object>();
			final Map<String, Object> options = tmp.getMissingValues();
			// final Map<String, Object> options = tmp.getOptionValues();
			// final Iterator<String> it = options.keySet().iterator();
			// while (it.hasNext()) {
			// final String key = it.next();
			// if (key.indexOf("missing") != -1)
			// missingList.add(options.get(key));
			// else {
			// final Object value = options.get(key);
			// LOGGER.debug("option item {} ({})",value,value.getClass());
			// if ((value != null) && ((value + "").equals("-97")))
			// missingList.add(value);
			// }
			// }
			if (options != null)
				missingList.addAll(options.values());
			missingList.add("");
			return missingList;
		}
		return null;
	}

	public synchronized Boolean isMissing(final AbstractAnswerBean var) {
		// LOGGER.info("isMissing");
		if (var != null) {
			final List<?> missingList = missingsOf(var);
			final Object value = valueOf(var);
			if ((value != null) && (missingList != null))
				return missingList.contains(value);
		}
		return false;
	}

	public synchronized Boolean isSet(final String var, final Participant participant) {
		if (var == null)
			return false;
		if (participant == null)
			return false;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("is Set {} for {}", var, participant.getToken());
		}

		if (participant.getSurveyData().containsKey(var))
			return true;
		return false;
	}

	// @Deprecated
	// public synchronized Boolean isSet(final AbstractAnswerBean var, final
	// Participant participant) {
	// if (var == null)
	// return false;
	// if (participant == null)
	// return false;
	// if (LOGGER.isDebugEnabled()) {
	// LOGGER.debug("is Set {} for {}", var.getVariableName(),
	// participant.getToken());
	// }
	//
	// if (participant.getSurveyData().containsKey(var.getVariableName()))
	// return true;
	// return false;
	// }

	public synchronized Boolean isBooleanSetOld(final AbstractAnswerBean var, final Participant participant) {
		if (var == null)
			return false;
		if (participant == null)
			return false;
		if (participant.getSurveyData().containsKey(var.getVariableName()) && var.getStringValue().equals("true")) {
			return true;
		}

		return false;
	}

	public synchronized Boolean isBooleanSet(final String var, final Participant participant) {
		if (var == null)
			return false;
		if (participant == null)
			return false;
		if (participant.getSurveyData().containsKey(var)
				&& participant.getSurveyData().get(var).getValue().equals("true")) {
			return true;
		}

		return false;
	}

	public synchronized Integer isSetCounter(final List<AbstractAnswerBean> vars, final Participant participant) {
		// LOGGER.info("missingCount");
		if (vars != null) {
			int back = 0;
			for (Object var : vars) {
				if (((AbstractAnswerBean.class).isAssignableFrom(var.getClass()))
						&& (isBooleanSetOld((AbstractAnswerBean) var, participant)))
					back = back + 1;
			}
			return back;
		}
		return 0;
	}

	public synchronized Integer missingCount(final List<AbstractAnswerBean> vars) {
		// LOGGER.info("missingCount");
		if (vars != null) {
			int back = 0;
			for (Object var : vars) {
				if (((AbstractAnswerBean.class).isAssignableFrom(var.getClass()))
						&& (isMissing((AbstractAnswerBean) var)))
					back = back + 1;
			}
			return back;
		}
		return 0;
	}

	public synchronized Object reverseValue(final AbstractAnswerBean var) {
		if (var == null)
			return null;
		final List<?> values = optionsOf(var);
		// LOGGER.debug("reverseValue list {}",values);
		Map<?, ?> map = mergeListsToMap(values, reverseList(values));
		// LOGGER.debug("reverseValue map {}",map);
		return recodeValue(var, map, valueOf(var));
	}

	public synchronized Object recodeValue(final AbstractAnswerBean var, final Map<?, ?> mapping,
			final Object alternative) {
		if (var == null)
			return null;
		if (mapping == null)
			return alternative;
		if (mapping.isEmpty())
			return alternative;
		if (mapping.containsKey(valueOf(var)))
			return mapping.get(valueOf(var));
		return alternative;
	}

	public synchronized Object chooseSetted(final SingleChoiceAnswerOptionTypeBean option1,
			final SingleChoiceAnswerOptionTypeBean option2) {
		String value1 = null;
		if (option1 != null) {
			value1 = option1.getValue();
		}
		String value2 = null;
		if (option2 != null) {
			value2 = option2.getValue();
		}
		if ((value1 == null) && (value2 == null))
			return null;
		if ((value1 != null) && (value2 == null))
			return value1;
		if ((value1 == null) && (value2 != null))
			return value2;

		value1 = value1.trim();
		value2 = value2.trim();

		if ((value1.equals("")) && (value2.equals("")))
			return "";
		if ((value1.equals("")) && (!value2.equals("")))
			return option2.getStringValue();
		if ((!value1.equals("")) && (value2.equals("")))
			return option1.getStringValue();
		if ((!value1.equals("")) && (!value2.equals("")))
			return option1.getStringValue() + option2.getStringValue();

		return "";
	}

	public synchronized Object chooseSettedDropDown(final SingleChoiceAnswerOptionTypeBean option1,
			final SingleChoiceAnswerOptionTypeBean option2) {
		String value1 = null;
		if (option1 != null) {
			value1 = option1.getValueId();
		}
		String value2 = null;
		if (option2 != null) {
			value2 = option2.getValueId();
		}

		// LOGGER.info("value1 {}",value1);
		// LOGGER.info("value2 {}",value2);

		if ((value1 == null) && (value2 == null))
			return null;
		if ((value1 != null) && (value2 == null))
			return value1;
		if ((value1 == null) && (value2 != null))
			return value2;

		if ((value1.equals("")) && (value2.equals("")))
			return null;
		if ((!value1.equals("")) && (value2.equals("")))
			return value1;
		if ((value1.equals("")) && (!value2.equals("")))
			return value2;
		if ((!value1.equals("")) && (!value2.equals(""))) {
			final String firstId = option2.getOptionValues().keySet().toArray()[0] + "";
			if (!value2.equals(firstId))
				return value2;
			else if (!value1.equals(firstId))
				return value1;
			else
				return firstId;
		}
		return null;
	}

	public synchronized Object ifthenelse(final Boolean condition, final Object thenElement, final Object elseElement) {
		if (condition)
			return thenElement;
		return elseElement;
	}

	// public Long asNumber(final Object input) {
	// if (input == null)
	// return 0L;
	// Object toConvert = input;
	// if ((AbstractAnswerBean.class).isAssignableFrom(input.getClass()))
	// toConvert = valueOf((AbstractAnswerBean) input);
	// // LOGGER.info("input {}",toConvert);
	// try {
	// return Long.parseLong(toConvert + "");
	// } catch (NumberFormatException exp) {
	// LOGGER.info("keine Zahl {}", input);
	// }
	// return 0L;
	// }

	public synchronized Double asNumber(final Object input) throws Exception {
		if (input == null)
			return 0D;
		Object toConvert = input;
		if ((AbstractAnswerBean.class).isAssignableFrom(input.getClass())) {
			toConvert = valueOf((AbstractAnswerBean) input);
			LOGGER.info("input to convert from {} =  {}", ((AbstractAnswerBean) input).getVariableName(), toConvert);
		}
		// if(toConvert.equals(""))return 0D;
		// LOGGER.info("input {}",toConvert);
		try {
			return Double.parseDouble(toConvert + "");
		} catch (NumberFormatException exp) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("keine Zahl {}", input);
			}

		}
		return 0D;
	}
	
	public synchronized Double asNumberNeu(final Object input, SessionController session) throws Exception {
		try {
			if (input == null)
				return 0D;
			Object toConvert = null;
			
			System.out.println("input ("+input.getClass()+") : "+input);
			if ((AbstractAnswerBean.class).isAssignableFrom(input.getClass())) {
//				toConvert = valueOf((AbstractAnswerBean) input);
				final FacesContext fc = FacesContext.getCurrentInstance();
				toConvert = JsfUtility.getInstance().evaluateValueExpression(fc, String.valueOf(input), String.class);
				LOGGER.info("input to convert from {} =  {}", ((AbstractAnswerBean) input).getVariableName(), toConvert);
			}
			else {
				toConvert = input;
			}
				
			
			
			System.out.println("toConvert "+toConvert);
			if(toConvert == null)return 0D;
			toConvert = toConvert+"".trim();
			if(toConvert.equals(""))return 0D;
			// LOGGER.info("input {}",toConvert);
			try {
				return Double.parseDouble(toConvert + "");
			} catch (NumberFormatException exp) {
				if (LOGGER.isDebugEnabled()) {
					System.out.println("keine Zahl "+input);
					LOGGER.debug("keine Zahl {}", input);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0D;
	}

//	public synchronized Double asNumberNeu(final Object input, SessionController session) throws Exception {
//		try {
//			if (input == null)
//				return 0D;
//			Object toConvert = "-1";
//			if ((AbstractAnswerBean.class).isAssignableFrom(input.getClass())) {
//				final String variableName = ((AbstractAnswerBean) input).getVariableName();
//				if(this.isSet(variableName, session.getParticipant())) {
//					toConvert = session.getParticipant().getSurveyData().get(variableName).getValue();
//				}
//				
//				LOGGER.info("input from variable {} =  {}",variableName, toConvert);
//				System.out.println("input from variable "+variableName+" =  "+toConvert);
//			}
//			else {
//				toConvert = input;
//				LOGGER.info("input {}", toConvert);
//				System.out.println("input "+toConvert);
//			}
//			
//			if(toConvert.equals(""))return 0D;
//			// LOGGER.info("input {}",toConvert);
//			try {
//				return Double.parseDouble(toConvert + "");
//			} catch (NumberFormatException exp) {
//				if (LOGGER.isDebugEnabled()) {
//					System.out.println("keine Zahl "+input);
//					LOGGER.debug("keine Zahl {}", input);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0D;
//	}

	public Double asNumber(final Object input, final int digits) throws Exception {
		if (digits <= 0)
			return this.asNumber(input);
		final Double back = new Double(new DecimalFormat("#." + StringUtils.repeat("#", digits), customFormatSymbols)
				.format(this.asNumber(input)));
		return back;
	}

	public Long toLong(final Object input) throws Exception {
		if (input == null)
			return 0L;
		Object toConvert = input;
		if ((Number.class).isAssignableFrom(input.getClass()))
			toConvert = ((Number) input).longValue();
		// LOGGER.info("input {}",toConvert);
		// if(toConvert.equals(""))return 0L;
		try {
			return Long.parseLong(toConvert + "");
		} catch (NumberFormatException exp) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("keine Zahl {}", input);
			}

		}
		return 0L;
	}

	public Integer toInteger(final Object input) throws Exception {
		if (input == null)
			return 0;
		Object toConvert = input;
		if ((Number.class).isAssignableFrom(input.getClass()))
			toConvert = ((Number) input).longValue();
		// LOGGER.info("input {}",toConvert);
		// if(toConvert.equals(""))return 0;
		try {
			return Integer.parseInt(toConvert + "");
		} catch (NumberFormatException exp) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("keine Zahl {}", input);
			}

		}
		return 0;
	}

	public void assign(final String varname, final Object expression) {
		// LOGGER.info("assign {} = {}",varname,expression);
		final FacesContext fc = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = fc.getExternalContext();

		Map<String, Object> requestMap = externalContext.getRequestMap();
		requestMap.put(varname, expression);
		//
		// for (String key : requestMap.keySet()) {
		// LOGGER.info("# {} = {}",key,requestMap.get(key));
		// }
	}

	public boolean xor(final boolean a, final boolean b) {
		return (a ^ b);
	}

	public synchronized Object scaleValue(final List<AbstractAnswerBean> vars, final String missing) {
		return scaleValue(vars, null, missing);
	}

	public synchronized Object scaleValue(final List<AbstractAnswerBean> vars,
			final List<AbstractAnswerBean> reverseVars, final String missing) {
		// LOGGER.debug("scaleValue");
		// if (vars == null)
		// return missing;
		final List<AbstractAnswerBean> allVars = mergeLists(vars, reverseVars);
		final Integer varCount = allVars.size();
		final Integer missingCount = missingCount(allVars);

		if ((missing != null) && (!(missing.trim()).equals("")) && (missingCount == varCount))
			return Double.parseDouble(missing);

		Double sum = 0D;
		Boolean flag = false;

		for (final AbstractAnswerBean var : allVars) {
			Object value = valueOf(var);
			if (!isMissing(var)) {
				if ((reverseVars != null) && reverseVars.contains(var))
					value = reverseValue(var);
				try {
					sum = sum + Double.parseDouble(value + "");
					flag = true;
				} catch (NumberFormatException exp) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("converting {} value {} to number failed", var.getVariableName(), value);
					}

				}
			}
		}
		if (!flag)
			return Double.parseDouble(missing);
		final Double data = sum / (varCount - missingCount);
		return data;
	}

	public synchronized Object sumValue(final List<AbstractAnswerBean> vars) {

		final List<AbstractAnswerBean> allVars = vars;
		int sum = 0;
		for (final AbstractAnswerBean var : allVars) {
			Object value = valueOf(var);
			if (value == null) {
				sum = sum + Integer.parseInt("0");
			}
			if (value != null && !value.toString().isEmpty()) {
				try {
					sum = sum + Integer.parseInt(value.toString());
				} catch (NumberFormatException exp) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("sumValue converting {} value {} to number failed", var.getVariableName(), value);
					}

				}
			}
		}
		return sum;
	}

	public synchronized Object valueCounter(final List<AbstractAnswerBean> vars) {

		final List<AbstractAnswerBean> allVars = vars;
		int counter = 0;
		for (final AbstractAnswerBean var : allVars) {
			Object value = valueOf(var);
			if (value == null) {
			}
			if (value != null && !value.toString().isEmpty()) {
				try {
					if (Integer.parseInt(value.toString()) > 0) {
						counter++;
					}
				} catch (NumberFormatException exp) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("valueCounter converting {} value {} to number failed", var.getVariableName(),
								value);
					}

				}
			}
		}
		return counter;
	}

	public synchronized String difference(final String value1, final String value2) {

		int val1 = 0;
		int val2 = 0;
		try {
			if (value1 != null && !value1.isEmpty()) {
				val1 = Integer.parseInt(value1.toString());
			} else {
				val1 = 0;
			}
			if (value2 != null && !value2.isEmpty()) {
				val2 = Integer.parseInt(value2.toString());
			} else {
				val2 = 0;
			}
		} catch (NumberFormatException exp) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("converting {} value {} to number failed");
			}

		}
		return "" + (val1 - val2);

	}

	public Object locate() {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = fc.getExternalContext();
		final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
		final String ip = httpServletRequest.getRemoteAddr();

		final String completeUrl = "http://ipinfo.io/" + ip + "/city";
		String result = callURL(completeUrl);
		result = result.replace('\n', ' ');
		result = result.replace('\"', ' ');
		result = result.trim();
		return result;
	}

	public synchronized String userAgent() {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = fc.getExternalContext();
		final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
		return httpServletRequest.getHeader("User-Agent");
	}

	public synchronized boolean isMobile() {
		final String userAgent = this.userAgent();
		if (userAgent != null)
			return userAgent.contains("Mobi");
		return false;
	}

	public String baseUrl() {
		final FacesContext fc = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = fc.getExternalContext();
		final HttpServletRequest httpServletRequest = (HttpServletRequest) externalContext.getRequest();
		// final String url = httpServletRequest.getRequestURL().toString();
		// final String uri = httpServletRequest.getRequestURI();
		final String referer = httpServletRequest.getHeader("referer");
		final String forwarded = httpServletRequest.getHeader("X-Forwarded-Host");
		// Enumeration<String> headerNames =
		// httpServletRequest.getHeaderNames();
		// while(headerNames.hasMoreElements()){
		// final String name = headerNames.nextElement();
		// final String headerValue = httpServletRequest.getHeader(name);
		// LOGGER.info("HEADER {} = {}",name,headerValue);
		// }

		// LOGGER.info("referer {}",referer);
		// LOGGER.info("Forwarded Host {}",forwarded);

		String back = referer;
		if (back == null)
			back = forwarded;
		if (back == null)
			back = "UNKOWN";
		// if(uri != null)back = back.replaceAll(Pattern.quote(uri), "");
		return back;
	}

	public boolean startwith(final String haystack, final String pattern) {
		if (haystack == null)
			return false;
		if (pattern == null)
			return false;
		if (haystack.startsWith(pattern))
			return true;
		return false;
	}

	public boolean contains(final String haystack, final String pattern) {
		if (haystack == null)
			return false;
		if (pattern == null)
			return false;
		if (haystack.contains(pattern))
			return true;
		return false;
	}

	public Object temperatur(final String city) {
		final String completeUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city
				+ "&mode=xml&units=metric";
		try {
			final SAXParserFactory spfac = SAXParserFactory.newInstance();
			final SAXParser sp = spfac.newSAXParser();

			final class ContentHandler extends DefaultHandler {

				private String temperature;

				public String getTemperatur() {
					return this.temperature;
				}

				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					super.startElement(uri, localName, qName, attributes);
					if (qName.equals("temperature")) {
						String current = attributes.getValue("value");
						String min = attributes.getValue("min");
						String max = attributes.getValue("max");
						String unit = attributes.getValue("unit");

						this.temperature = current;
					}
				}

			}
			final ContentHandler handler = new ContentHandler();
			sp.parse(completeUrl, handler);
			return handler.getTemperatur();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private synchronized String callURL(String myURL) {
		// LOGGER.info("Requested URL: {}", myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + myURL, e);
		}

		return sb.toString();
	}

	public String injectExternal(final String relativePath, final String filename, final String url) {
		final String content = callURL(url);

		final FacesContext fc = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = fc.getExternalContext();
		final String absolutePath = externalContext.getRealPath("/" + relativePath + "/" + filename);

		final File file = new File(absolutePath);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("inject {} to {}", url, absolutePath);
		}

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}

	// Component specific functions

	public synchronized UIComponent retrieveElementByID(final String id) throws Exception {
		System.out.print("retrieveElementByID... ");
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		final UIComponent[] found = new UIComponent[1];

		root.visitTree(new FullVisitContext(context), new VisitCallback() {
			@Override
			public VisitResult visit(VisitContext context, UIComponent component) {
				if (component != null && component.getId() != null && component.getId().equals(id)) {
					found[0] = component;
					return VisitResult.COMPLETE;
				}
				return VisitResult.ACCEPT;
			}
		});
		System.out.println("done : " + found[0]);
		return found[0];
	}

	public synchronized UIComponent findComponent(final String id) throws Exception {
		UIComponent result = null;
		UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
		if (root != null) {
			result = findComponent(root, id);
		}
		return result;
	}

	private UIComponent findComponent(UIComponent root, String id) throws Exception {

		UIComponent result = null;
		if (root.getId().equals(id))
			return root;

		for (UIComponent child : root.getChildren()) {
			if (child.getId().equals(id)) {
				result = child;
				break;
			}
			result = findComponent(child, id);
			if (result != null)
				break;
		}

		return result;
	}

	// calendar specific functions

	public synchronized String getEpisodes(final UIComponent calendar, final List<Object> columnList,
			final List<Object> rowList) throws Exception {
		if (calendar == null)
			return null;

		final UIComponent tmp = calendar.getFacet("javax.faces.component.COMPOSITE_FACET_NAME");
		if (tmp == null)
			return null;

		final List<UIComponent> childs = tmp.getChildren();
		System.out.print("get Episodes... ");
		for (final UIComponent child : childs) {
			if ((UICalendarSheet.class).isAssignableFrom(child.getClass())) {
				final List<UICalendarItem> back = new ArrayList<UICalendarItem>();

				final UICalendarSheet sheet = (UICalendarSheet) child;

				for (final UIComponent sheetChild : sheet.getChildren()) {
					if ((UICalendarItem.class).isAssignableFrom(sheetChild.getClass())) {
						UICalendarItem item = (UICalendarItem) sheetChild;
						back.add(item);
					}
				}

				final Map<Integer, List<List<String>>> beams = new LinkedHashMap<Integer, List<List<String>>>();

				for (int a = 1; a <= 20; a++) {
					List<List<String>> slotBeam = null;
					if (beams.containsKey(a))
						slotBeam = beams.get(a);
					if (slotBeam == null)
						slotBeam = new ArrayList<List<String>>();

					List<String> currentEpisode = new ArrayList<String>();
					int itemIndex = 1;
					for (UICalendarItem item : back) {
						final IAnswerBean variable = item.getSlot(a);
						if (variable != null) {
							final Boolean value = Boolean.valueOf(variable.getStringValue());
							if (value) {
								// currentEpisode.add(item.getId());
								currentEpisode.add(itemIndex + "");
							} else {
								if (!currentEpisode.isEmpty()) {
									slotBeam.add(currentEpisode);
									currentEpisode = new ArrayList<String>();
								}
							}
						}
						itemIndex = itemIndex + 1;
					}
					beams.put(a, slotBeam);
				}

				final Map<Integer, List<List<String>>> strippedBeams = new LinkedHashMap<Integer, List<List<String>>>();

				for (Map.Entry<Integer, List<List<String>>> slotBeam : beams.entrySet()) {

					List<List<String>> slotPairs = null;
					if (strippedBeams.containsKey(slotBeam.getKey()))
						slotPairs = strippedBeams.get(slotBeam.getKey());
					if (slotPairs == null)
						slotPairs = new ArrayList<List<String>>();

					for (List<String> episode : slotBeam.getValue()) {
						if (episode.isEmpty())
							continue;
						final String startId = episode.get(0);
						final String endId = episode.get(episode.size() - 1);
						final List<String> pair = new ArrayList<String>();
						pair.add(startId);
						pair.add(endId);
						slotPairs.add(pair);
					}

					if ((slotPairs != null) && (!slotPairs.isEmpty())) {
						strippedBeams.put(slotBeam.getKey(), slotPairs);
					}
				}
				// try {
				System.out.println("done");
				return encodeEpisodes(strippedBeams);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
		System.out.println("error");
		return null;
	}

	public synchronized String getEpisodesHTML5(final UIComponent calendar, final List<Object> columnList,
			final List<Object> rowList) {
		if (calendar == null)
			return null;

		final List<UIComponent> items = calendar.getFacet("items").getChildren();

//		System.out.print("get Episodes x... ");

		final List<UICalendarItem> back = new ArrayList<UICalendarItem>();
		for (final UIComponent item : items) {
			// LOGGER.info("cal cidl : " + item.getClass().getName());

			if ((UICalendarItem.class).isAssignableFrom(item.getClass())) {
				UICalendarItem calendarItem = (UICalendarItem) item;
				back.add(calendarItem);
			}
		}

		final Map<Integer, List<List<String>>> beams = new LinkedHashMap<Integer, List<List<String>>>();
		for (int a = 1; a <= 20; a++) {
			List<List<String>> slotBeam = null;
			if (beams.containsKey(a))
				slotBeam = beams.get(a);
			if (slotBeam == null)
				slotBeam = new ArrayList<List<String>>();

			List<String> currentEpisode = new ArrayList<String>();
			boolean dirty = false;
			int itemIndex = 1;
			for (final UICalendarItem item : back) {
				final IAnswerBean variable = item.getSlot(a);
				if (variable != null) {
					final Boolean value = Boolean.valueOf(variable.getStringValue());
//					System.out.println("variable : "+variable.getVariableName()+ " = "+value);
					if (value) {
						// currentEpisode.add(item.getId());
//						System.out.println("itemIndex : "+itemIndex);
						currentEpisode.add(itemIndex + "");
						dirty = true;
					} else {
						if (!currentEpisode.isEmpty()) {
							slotBeam.add(currentEpisode);
							currentEpisode = new ArrayList<String>();
							dirty = false;
						}
					}
				}
				itemIndex = itemIndex + 1;
			}
			if(dirty)slotBeam.add(currentEpisode);
			beams.put(a, slotBeam);
		}

		final Map<Integer, List<List<String>>> strippedBeams = new LinkedHashMap<Integer, List<List<String>>>();

		for (final Map.Entry<Integer, List<List<String>>> slotBeam : beams.entrySet()) {
//			System.out.println("slotBeam : "+slotBeam);
			List<List<String>> slotPairs = null;
			if (strippedBeams.containsKey(slotBeam.getKey()))
				slotPairs = strippedBeams.get(slotBeam.getKey());
			if (slotPairs == null)
				slotPairs = new ArrayList<List<String>>();

			for (final List<String> episode : slotBeam.getValue()) {
				if (episode.isEmpty())
					continue;
				final String startId = episode.get(0);
				final String endId = episode.get(episode.size() - 1);
				final List<String> pair = new ArrayList<String>();
				pair.add(startId);
				pair.add(endId);
				slotPairs.add(pair);
			}

			if ((slotPairs != null) && (!slotPairs.isEmpty())) {
				strippedBeams.put(slotBeam.getKey(), slotPairs);
			}
		}
		try {
//			System.out.println("done");
			return encodeEpisodes(strippedBeams);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		System.out.println("error");
		return null;

	}

	public synchronized Map<String, Object> episodeTupel(final Integer eventId, final Object start, final Object stop,
			final Object meta) throws Exception {
		if (eventId == null)
			return null;
		if (start == null)
			return null;
		if (stop == null)
			return null;
		final HashMap<String, Object> back = new HashMap<String, Object>();
		back.put("event", eventId);
		back.put("start", start);
		back.put("stop", stop);
		if (meta != null)
			back.put("meta", meta);
		return back;
	}

	public synchronized String getEpisodesFromVariables(final List<Object> variableTupels) throws Exception {
		if (variableTupels == null)
			return null;
		final Map<Integer, List<List<String>>> strippedBeams = new LinkedHashMap<Integer, List<List<String>>>();
		final Map<Integer, List<Map<String, String>>> metaBeams = new LinkedHashMap<Integer, List<Map<String, String>>>();
		for (final Object variableTupel : variableTupels) {
			if ((Map.class).isAssignableFrom(variableTupel.getClass())) {
				final Map<String, Object> tmp = (Map<String, Object>) variableTupel;
				final Integer eventId = (Integer) tmp.get("event");
				final String start = tmp.get("start") + "";
				final String stop = tmp.get("stop") + "";
				final Object meta = tmp.get("meta");

				if (eventId < 0)
					continue;
				if (Integer.parseInt(start) < 0)
					continue;
				if (Integer.parseInt(stop) < 0)
					continue;

				List<List<String>> slotPairs = null;
				if (strippedBeams.containsKey(eventId))
					slotPairs = strippedBeams.get(eventId);
				if (slotPairs == null)
					slotPairs = new ArrayList<List<String>>();

				List<Map<String, String>> slotMeta = null;
				if (metaBeams.containsKey(eventId))
					slotMeta = metaBeams.get(eventId);
				if (slotMeta == null)
					slotMeta = new ArrayList<Map<String, String>>();

				final List<String> pair = new ArrayList<String>();
				// pair.add(Integer.parseInt(start)+"");
				// pair.add(Integer.parseInt(stop)+"");
				pair.add(start);
				pair.add(stop);

				slotPairs.add(pair);

				final Map<String, String> metaMap = new LinkedHashMap<String, String>();
				if (meta != null) {
					if ((Map.class).isAssignableFrom(meta.getClass()))
						metaMap.putAll((Map) meta);

				}
				slotMeta.add(metaMap);

				strippedBeams.put(eventId, slotPairs);
				metaBeams.put(eventId, slotMeta);
			}
		}

		try {
			System.out.println("done");

			Map<String, Object> back = new HashMap<String, Object>();
			back.put("data", strippedBeams);
			back.put("meta", metaBeams);

			return encodeEpisodesWithMeta(back);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("error");
		return null;
	}

	private String encodeEpisodes(final Map<Integer, List<List<String>>> beams) throws Exception {
		if (beams == null)
			return null;
		final DecimalFormat formatter = new DecimalFormat("000");
		final StringBuffer serializedMap = new StringBuffer();
		boolean first1 = true;
		for (Map.Entry<Integer, List<List<String>>> slotBeam : beams.entrySet()) {
			if (!first1)
				serializedMap.append(";");
			serializedMap.append(slotBeam.getKey() + "#");
			final StringBuffer tmp = new StringBuffer();
			for (List<String> episode : slotBeam.getValue()) {
				for (final String item : episode) {
					tmp.append(formatter.format(Integer.parseInt(item)));
				}
				tmp.append(":");
			}
			String cleaned = tmp.toString();
			cleaned = cleaned.substring(0, cleaned.length() - 1);
			serializedMap.append(cleaned);
			serializedMap.append("");
			first1 = false;
		}
		final String toCompresss = serializedMap.toString();
		System.out.print("compress... ");
		final String compressed = compress(toCompresss);
		System.out.println("done");
		return compressed;
	}

	public synchronized Map<Integer, List<List<Integer>>> decodeEpisodes(final String serializedMap) throws Exception {
		if (serializedMap == null)
			return null;
		if (serializedMap.equals(""))
			return null;
//		System.out.print("decompress... ");
		final String decompressed = decompress(serializedMap);
//		System.out.println("done");
//		System.out.print("decode... ");
		final Map<Integer, List<List<Integer>>> back = new LinkedHashMap<Integer, List<List<Integer>>>();
		final String[] slotSets = decompressed.split(";");
		if (slotSets != null) {
			for (final String slotSet : slotSets) {
				final String[] slotPair = slotSet.split("#");
				if ((slotPair != null) && (slotPair.length == 2)) {
					final String slotIndexStr = slotPair[0];
					final Integer slotIndex = Integer.parseInt(slotIndexStr);

					final String episodes = slotPair[1];
					final String[] episodePairs = episodes.split(":");

					List<List<Integer>> slotPairs = null;
					if (back.containsKey(slotIndex))
						slotPairs = back.get(slotIndex);
					if (slotPairs == null)
						slotPairs = new ArrayList<List<Integer>>();

					if ((episodePairs != null) && (episodePairs.length > 0)) {
						for (final String pair : episodePairs) {
							final String from = pair.substring(0, 3);
							final String to = pair.substring(3);
							slotPairs.add(Arrays.asList(Integer.parseInt(from), Integer.parseInt(to)));
						}
					}
					if (!slotPairs.isEmpty())
						back.put(slotIndex, slotPairs);
				}
			}
		}
//		System.out.println("done");
		return back;
	}

	private String encodeEpisodesWithMeta(final Map<String, Object> data) throws Exception {
		if (data == null)
			return null;

		String toCompress = new String();

		if (data.containsKey("data")) {
			final Map<Integer, List<List<String>>> beams = (Map<Integer, List<List<String>>>) data.get("data");
			final DecimalFormat formatter = new DecimalFormat("000");
			final StringBuffer serializedMap = new StringBuffer();
			boolean first1 = true;

			for (Map.Entry<Integer, List<List<String>>> slotBeam : beams.entrySet()) {
				if (!first1)
					serializedMap.append(";");
				serializedMap.append(slotBeam.getKey() + "#");
				final StringBuffer tmp = new StringBuffer();
				for (List<String> episode : slotBeam.getValue()) {
					for (final String item : episode) {
						tmp.append(formatter.format(Integer.parseInt(item)));
					}
					tmp.append(":");
				}
				String cleaned = tmp.toString();
				cleaned = cleaned.substring(0, cleaned.length() - 1);
				serializedMap.append(cleaned);
				serializedMap.append("");
				first1 = false;
			}
			final String toCompresssData = serializedMap.toString();
			System.out.print("compress data ... ");
			toCompress += "DATA(" + compress(toCompresssData) + ")DATA";
			System.out.println("done");
		}
		if (data.containsKey("meta")) {
			final Map<Integer, List<Map<String, String>>> metaBeams = (Map<Integer, List<Map<String, String>>>) data
					.get("meta");
			final StringBuffer serializedMap = new StringBuffer();

			boolean first1 = true;

			for (Map.Entry<Integer, List<Map<String, String>>> metaBeam : metaBeams.entrySet()) {
				if (!first1)
					serializedMap.append(";");

				serializedMap.append(metaBeam.getKey() + "#");
				final StringBuffer tmp = new StringBuffer();
				for (Map<String, String> episodeMeta : metaBeam.getValue()) {
					boolean first2 = true;
					for (final Map.Entry<String, String> episodeMetaItem : episodeMeta.entrySet()) {
						if (!first2)
							tmp.append("^");
						tmp.append(episodeMetaItem.getKey() + "°" + episodeMetaItem.getValue());
						first2 = false;
					}
					tmp.append(":");
				}

				String cleaned = tmp.toString();
				cleaned = cleaned.substring(0, cleaned.length() - 1);
				serializedMap.append(cleaned);
				serializedMap.append("");
				first1 = false;
			}

			final String toCompresssMeta = serializedMap.toString();
			System.out.print("compress meta ... ");
			toCompress += "META(" + toCompresssMeta + ")META";
			System.out.println("done");
		}
		System.out.println("toCompress : " + toCompress);
		return compress(toCompress);
	}

	public synchronized final Map<String, Object> decodeEpisodesWithMeta(final String serializedMap) throws Exception {
		if (serializedMap == null)
			return null;
		if (serializedMap.equals(""))
			return null;
		System.out.print("decompress... ");
		final String decompressed = decompress(serializedMap);
		System.out.println("decompressed : " + decompressed);
		System.out.println("done");
		System.out.print("decode... ");
		final Map<String, Object> back = new HashMap<String, Object>();

		final int dataStart = decompressed.indexOf("DATA(");
		final int dataStop = decompressed.indexOf(")DATA");

		if ((dataStart != -1) && (dataStop != -1) && (dataStart <= dataStop)) {
			final String dataStr = decompressed.substring(dataStart + 5, dataStop);
			System.out.println("dataStr : " + dataStr);
			back.put("data", decodeEpisodes(dataStr));
		}

		final int metaStart = decompressed.indexOf("META(");
		final int metaStop = decompressed.indexOf(")META");

		if ((metaStart != -1) && (metaStop != -1) && (metaStart <= metaStop)) {
			final String metaStr = decompressed.substring(metaStart + 5, metaStop);
			System.out.println("metaStr : " + metaStr);

			final Map<Integer, List<Map<String, String>>> metaBack = new LinkedHashMap<Integer, List<Map<String, String>>>();
			final String[] eventSets = metaStr.split(";");
			if (eventSets != null) {
				for (final String eventSet : eventSets) {
					final String[] eventPair = eventSet.split(Pattern.quote("#"));
					if ((eventPair != null) && (eventPair.length == 2)) {
						final String eventIndexStr = eventPair[0];
						final Integer eventIndex = Integer.parseInt(eventIndexStr);

						final String metaListStr = eventPair[1];
						final String[] metaListPairs = metaListStr.split(Pattern.quote("^"));

						List<Map<String, String>> metaPairs = null;
						if (back.containsKey(eventIndex))
							metaPairs = metaBack.get(eventIndex);
						if (metaPairs == null)
							metaPairs = new ArrayList<Map<String, String>>();

						if ((metaListPairs != null) && (metaListPairs.length > 0)) {
							Map<String, String> metaPairMap = new LinkedHashMap<String, String>();
							for (final String metaListPair : metaListPairs) {
								final String[] metaPair = metaListPair.split(Pattern.quote("°"));
								if ((metaPair != null) && (metaPair.length == 2))
									metaPairMap.put(metaPair[0], metaPair[1]);
								else {
									System.err.println("meta split failed : " + metaListPair);
								}
							}
							metaPairs.add(metaPairMap);
						}
						if (!metaPairs.isEmpty())
							metaBack.put(eventIndex, metaPairs);
					}
				}
			}
			System.out.println("MetaBack : " + metaBack);
			back.put("meta", metaBack);
		}

		System.out.println("done");
		return back;
	}

	private synchronized String compress(final String content) throws Exception {
		byte[] compressed = Snappy.compress(content.getBytes());
		return new String(Hex.encodeHex(compressed));
	}

	private synchronized String decompress(final String content) throws Exception {
		byte[] uncompressed = Hex.decodeHex(content.toCharArray());
		return new String(Snappy.uncompress(uncompressed));
	}

	// Methods to analyse Episodes

	public synchronized boolean hasEpisodes(final Map<Integer, List<List<Integer>>> beams, final List<Long> slots)
			throws Exception {
//		System.out.print("hasEpisodes... ");
		if (beams == null)
			return false;
		if (beams.isEmpty())
			return false;
		if (slots == null)
			return false;
		if (slots.isEmpty())
			return false;
		for (final Long slot : slots) {
			if (!beams.containsKey(slot.intValue()))
				continue;
			if (!beams.get(slot.intValue()).isEmpty())
				return true;
		}
		return false;
	}

	public synchronized boolean hasEpisodesInRange(final Map<Integer, List<List<Integer>>> beams, final Integer slot,
			Integer rangeStart, Integer rangeStop) throws Exception {
		System.out.print("hasEpisodesInRange... ");
		if (beams == null)
			return false;
		if (beams.isEmpty())
			return false;
		if (slot == null)
			return false;

		if (!beams.containsKey(slot.intValue()))
			return false;
		if (!beams.get(slot.intValue()).isEmpty()) {
			List<List<Integer>> beamList = beams.get(slot.intValue());
			for (final List<Integer> beam : beamList) {
				final Integer beamStart = beam.get(0);
				final Integer beamStop = beam.get(beam.size() - 1);
				// if ((beamStart >= rangeStart) && (beamStop <= rangeStop))
				if ((beamStart >= rangeStart) && (beamStart <= rangeStop))
					return true;
			}
		}
		return false;
	}

	public synchronized int countEpisodesInRange(final Map<Integer, List<List<Integer>>> beams, final Integer slot,
			Integer rangeStart, Integer rangeStop) throws Exception {
		System.out.print("hasEpisodesInRange... ");
		if (beams == null)
			return 0;
		if (beams.isEmpty())
			return 0;
		if (slot == null)
			return 0;

		if (!beams.containsKey(slot.intValue()))
			return 0;
		if (!beams.get(slot.intValue()).isEmpty()) {
			List<List<Integer>> beamList = beams.get(slot.intValue());
			int back = 0;
			for (final List<Integer> beam : beamList) {
				final Integer beamStart = beam.get(0);
				final Integer beamStop = beam.get(beam.size() - 1);
				// if ((beamStart >= rangeStart) && (beamStop <= rangeStop))
				if ((beamStart >= rangeStart) && (beamStart <= rangeStop))
					back = back + 1;
			}
			return back;
		}
		return 0;
	}

	public synchronized List<List<Integer>> getEpisodes(final Map<Integer, List<List<Integer>>> beams,
			final Integer slot) throws Exception {
		System.out.print("getEpisodes... ");
		if (beams == null)
			return null;
		if (beams.isEmpty())
			return null;
		if (slot == null)
			return null;
		if (beams.containsKey(slot))
			return beams.get(slot);
		return null;
	}

	public synchronized Integer getTileIndex(final SingleChoiceAnswerOptionTypeBean colIndexBean,
			final SingleChoiceAnswerOptionTypeBean rowIndexBean, final Long colCount, final Long rowCount)
			throws Exception {
		System.out.println("getTileIndex... ");
		if (rowIndexBean == null)
			return -1;

		final Integer rowIndex = toInteger(asNumber(rowIndexBean));

		if (rowIndex < 0)
			return -1;

		if (colIndexBean == null)
			return -1;

		final Integer colIndex = toInteger(asNumber(colIndexBean));

		if (colIndex < 0)
			return -1;

		Integer back = -1;
		if (rowIndex == 0) {
			back = colIndex;
		} else if (rowIndex > 0) {
			back = (int) ((rowIndex * colCount) + colIndex);
		}

		System.out.println("col Index : " + colIndex + " (" + colCount + ") row Index : " + rowIndex + " (" + rowCount
				+ ") back : " + back);

		return back;
	}

	public synchronized List<String> convertEpisode(final List<Integer> episode, final List<Object> columnList,
			final List<Object> rowList) throws Exception {
		System.out.print("convertEpisode... ");
		if (episode == null)
			return null;
		if (episode.isEmpty())
			return null;
		if (columnList == null)
			return null;
		if (columnList.isEmpty())
			return null;
		if (rowList == null)
			return null;
		if (rowList.isEmpty())
			return null;

		final List<String> back = new ArrayList<String>();
		final int colCount = columnList.size();

		for (final Integer episodeItem : episode) {
			final int tileIndex = episodeItem;
			final int rowIndex = (int) Math.ceil(tileIndex / colCount);
			final int colIndex = tileIndex - (rowIndex * colCount);
			back.add(columnList.get(colIndex) + " " + rowList.get(rowIndex));
		}

		return back;
	}

	private synchronized List<UICalendarItem> getItems(final UICalendar calendar) throws Exception {
		if (calendar == null)
			return null;
		final UIComponent tmp = calendar.getFacet("javax.faces.component.COMPOSITE_FACET_NAME");
		if (tmp == null)
			return null;
		final List<UIComponent> childs = tmp.getChildren();

		for (final UIComponent child : childs) {
			if ((UICalendarSheet.class).isAssignableFrom(child.getClass())) {
				List<UICalendarItem> back = new ArrayList<UICalendarItem>();
				final UICalendarSheet sheet = (UICalendarSheet) child;
				for (final UIComponent sheetChild : sheet.getChildren()) {
					if ((UICalendarItem.class).isAssignableFrom(sheetChild.getClass())) {
						back.add((UICalendarItem) sheetChild);
					}
				}
				return back;
			}
		}
		return null;
	}

	private synchronized Map<Integer, BooleanValueTypeBean> getSlots(final UICalendarItem calendarItem)
			throws Exception {
		if (calendarItem == null)
			return null;
		final Map<String, Object> attributes = calendarItem.getAttributes();
		if (attributes != null) {
			Map<Integer, BooleanValueTypeBean> back = new HashMap<Integer, BooleanValueTypeBean>();
			for (int a = 1; a <= 20; a++) {
				final IAnswerBean variable = calendarItem.getSlot(a);
				if (variable == null)
					continue;
				if ((BooleanValueTypeBean.class).isAssignableFrom(variable.getClass()))
					back.put(new Integer(a), (BooleanValueTypeBean) variable);
			}
			return back;
		}
		return null;
	}

	public synchronized Map<Integer, Integer> calendarSlotCounts(final UIComponent calendar) throws Exception {

		if (calendar == null)
			return null;
		if (!(UICalendar.class).isAssignableFrom(calendar.getClass()))
			return null;
		final List<UICalendarItem> items = getItems((UICalendar) calendar);

		if (items != null) {
			final Map<Integer, Integer> back = new HashMap<Integer, Integer>();
			for (final UICalendarItem item : items) {
				final Map<Integer, BooleanValueTypeBean> slots = getSlots(item);
				if (slots != null) {
					for (Map.Entry<Integer, BooleanValueTypeBean> slot : slots.entrySet()) {
						Integer count = null;
						if (back.containsKey(slot.getKey()))
							count = back.get(slot.getKey());
						if (count == null)
							count = new Integer(0);
						if (slot.getValue().getValue())
							back.put(slot.getKey(), count + 1);
					}
				}
			}
			// final UICalendarLegend legend =
			// this.getLegend((UICalendar)calendar);
			// Map<Integer, String> legendLabels = this.getLegendLabels(legend);
			// for (Map.Entry<Integer, Integer> slot : back.entrySet()) {
			// String label = null;
			// if((legendLabels !=
			// null)&&legendLabels.containsKey(slot.getKey()))label =
			// legendLabels.get(slot.getKey());
			// LOGGER.info("Calendar Count {} {}", slot.getKey(), label+" =
			// "+slot.getValue());
			// }
			return back;
		}
		return null;
	}

	// random decision functions

	/**
	 * Generate a random Boolean Decision
	 * 
	 * @return random boolean
	 */
	public synchronized boolean randomBoolean() {
		return random.nextBoolean();
	}

	/**
	 * Generate a weighted random Boolean Decision
	 * 
	 * @param percent
	 *            Value between 1 and 100
	 * @return random boolean
	 */
	public synchronized boolean randomBoolean(final int percent) {
		final double weight = ((double) percent) / 100;
		final double rnd = random.nextDouble();
		final boolean back = rnd < weight;
		return back;
	}

	/**
	 * Generate a random Decision from Item set
	 * 
	 * @return random Item
	 */
	public synchronized String randomOf(final List<String> items) {
		final Random random = new SecureRandom();
		final String selected = items.get(random.nextInt(items.size()));
		return selected;
	}

	/**
	 * 
	 * Parse JSON from String
	 * 
	 */

	public synchronized Map<String, Object> asXmlMap(final String input) throws Exception {
		if (input == null)
			return null;
		// try {
		final SAXParserFactory spfac = SAXParserFactory.newInstance();
		final SAXParser sp = spfac.newSAXParser();

		final class ContentHandler extends DefaultHandler {
			Map<String, Object> map;

			private Deque<Map<String, Object>> stack = new ArrayDeque<Map<String, Object>>();

			public ContentHandler(Map<String, Object> map) {
				super();
				this.map = map;
			}

			public Map<String, Object> getMap() {
				return map;
			}

			@Override
			public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
				Map<String, Object> elemMap = new LinkedHashMap<String, Object>();
				elemMap.put("type", qName);

				int attrLen = atts.getLength();
				Map<String, Object> attrMap = new LinkedHashMap<String, Object>();
				for (int i = 0; i < attrLen; i++) {
					attrMap.put(atts.getQName(i), atts.getValue(i));
				}
				if (!attrMap.isEmpty())
					elemMap.put("attributes", attrMap);

				stack.push(elemMap);
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				super.endElement(uri, localName, qName);
				Map<String, Object> elemMap = stack.pop();

				List<Object> childs = (List<Object>) stack.peek().get("childs");
				if (childs == null)
					childs = new ArrayList<Object>();
				childs.add(elemMap);
				stack.peek().put("childs", childs);
			}

			@Override
			public void startDocument() throws SAXException {
				stack.push(this.map);
			}

			@Override
			public void endDocument() throws SAXException {
				// this.map = (Map<String, Object>) ((List<Object>)
				// this.map.get("childs")).get(0);
				// stack.peek().put("doc", stack.pop());
			}

			@Override
			public void characters(char[] ch, int start, int length) {
				String content = String.valueOf(ch).substring(start, start + length);
				if (content != null) {
					content = content.trim();
					if (!content.equals(""))
						stack.peek().put("content", content);
				}
			}

		}
		final ContentHandler handler = new ContentHandler(new HashMap<String, Object>());
		sp.parse(IOUtils.toInputStream(input, "UTF-8"), handler);
		return handler.getMap();

		// } catch (ParserConfigurationException e) {
		// e.printStackTrace();
		// } catch (SAXException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// return null;
	}

	public void log(final String message, final Participant participant) {
		LOGGER.info("[MESSAGE] ({}) {}", participant.getToken(), message);
	}

}
