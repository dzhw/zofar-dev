package de.his.zofar.presentation.surveyengine.ui.renderer.display;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.presentation.surveyengine.ui.components.display.UIDisplayCalendar;
import de.his.zofar.presentation.surveyengine.ui.util.JsfUtility;

/**
 * @author meisner
 *
 */
@FacesRenderer(componentFamily = UIDisplayCalendar.COMPONENT_FAMILY, rendererType = CalendarRenderer.RENDERER_TYPE)
public class CalendarRenderer extends Renderer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarRenderer.class);

	public static final String RENDERER_TYPE = "org.zofar.display.calendar";

	public CalendarRenderer() {
		super();
	}

	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		if (component == null) {
			return;
		}
		if (!component.isRendered()) {
			return;
		}

		UIDisplayCalendar calendar = (UIDisplayCalendar) component;

		final Object configObj = JsfUtility.getInstance().evaluateValueExpression(
				FacesContext.getCurrentInstance(), "#{zofar.asXmlMap(\""
						+ URLDecoder.decode((String) calendar.getAttributes().get("config"), "UTF-8") + "\")}",
				Object.class);

		Map<String, List<Map>> config = null;
		if ((configObj != null) && ((Map.class).isAssignableFrom(configObj.getClass()))) {
			Map<String, Object> castedConfig = (Map<String, Object>) configObj;

			final Map configItems = (Map) ((List) castedConfig.get("childs")).get(0);
			config = new HashMap<String, List<Map>>();
			if (configItems != null) {
				if (configItems.get("type").equals("config")) {
					final List childs = (List) configItems.get("childs");
					for (final Object child : childs) {
						if ((Map.class).isAssignableFrom(child.getClass())) {
							final Map tmp = (Map) child;
							final String tmpType = (String) tmp.get("type");
							final List tmpChilds = (List) tmp.get("childs");
							List<Map> items = config.get(tmpType);
							if (items == null)
								items = new ArrayList<Map>();
							if (tmpChilds != null) {
								for (final Object tmpChild : tmpChilds) {
									if ((Map.class).isAssignableFrom(tmpChild.getClass())) {
										final Map tmpAttributes = (Map) ((Map) tmpChild).get("attributes");
										items.add(tmpAttributes);
									}
								}
							}

							config.put(tmpType, items);
						}
					}
				}
			}
		}

		final ResponseWriter writer = context.getResponseWriter();

		writer.startElement("article", component);
		writer.writeAttribute("id", component.getClientId(), null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "form-orientation form-responsive", null);
		// writer.writeAttribute("id", component.getClientId(), null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "form-scroller", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "display-calendar", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "calendar-legend row", null);

		// List calEvents = (List) calendar.getAttributes().get("events");
		List<Map> calEvents = null;
		if (config != null) {
			calEvents = config.get("events");
		}

		if (calEvents != null) {
			// Two Columns
			int eventsPerColumn = (int) Math.ceil(calEvents.size() / 2);

			writer.startElement("div", component);
			writer.writeAttribute("class", "col-md-6", null);

			int lftPerColumn = 1;
			boolean flag = false;

			for (final Map event : calEvents) {

				writer.startElement("div", component);
				writer.writeAttribute("class", "calendar-legend-item-container", null);

				writer.startElement("span", component);
				writer.writeAttribute("class", "calendar-legend-item-icon", null);
				writer.writeAttribute("data-color", event.get("color"), null);
				writer.writeAttribute("data-pattern", event.get("pattern"), null);
				writer.endElement("span");

				writer.startElement("span", component);
				writer.writeAttribute("class", "calendar-legend-item", null);
				writer.writeAttribute("data-event-id", event.get("id"), null);
				writer.writeAttribute("data-color", event.get("color"), null);
				writer.writeAttribute("data-pattern", event.get("pattern"), null);
				writer.writeAttribute("data-label", event.get("label"), null);
				writer.endElement("span");
				writer.endElement("div");

				if (!flag && (lftPerColumn > eventsPerColumn)) {
					writer.endElement("div");
					writer.startElement("div", component);
					writer.writeAttribute("class", "col-md-6", null);
					flag = true;
				}

				lftPerColumn = lftPerColumn + 1;
			}

			writer.endElement("div");
		}
		writer.endElement("div");

		writer.startElement("div", component);
		writer.writeAttribute("class", "form-inner form-3-col pt-2 pt-sm-5", null);

		writer.startElement("span", component);
		writer.writeAttribute("class", "calendar-header-title", null);
		writer.endElement("span");

		writer.startElement("div", component);
		writer.writeAttribute("class", "container", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "calendar-container", null);
	}

	@Override
	public boolean getRendersChildren() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void encodeChildren(final FacesContext context, final UIComponent component) throws IOException {
		if (component == null) {
			return;
		}
		if (!component.isRendered()) {
			return;
		}
		final UIDisplayCalendar calendar = (UIDisplayCalendar) component;

		final Object configObj = JsfUtility.getInstance().evaluateValueExpression(
				FacesContext.getCurrentInstance(), "#{zofar.asXmlMap(\""
						+ URLDecoder.decode((String) calendar.getAttributes().get("config"), "UTF-8") + "\")}",
				Object.class);

		Map<String, List<Map>> config = null;
		if ((configObj != null) && ((Map.class).isAssignableFrom(configObj.getClass()))) {
			Map<String, Object> castedConfig = (Map<String, Object>) configObj;

			final Map configItems = (Map) ((List) castedConfig.get("childs")).get(0);
			config = new HashMap<String, List<Map>>();
			if (configItems != null) {
				if (configItems.get("type").equals("config")) {
					final List childs = (List) configItems.get("childs");
					for (final Object child : childs) {
						if ((Map.class).isAssignableFrom(child.getClass())) {
							final Map tmp = (Map) child;
							final String tmpType = (String) tmp.get("type");
							final List tmpChilds = (List) tmp.get("childs");
							List<Map> items = config.get(tmpType);
							if (items == null)
								items = new ArrayList<Map>();
							if (tmpChilds != null) {
								for (final Object tmpChild : tmpChilds) {
									if ((Map.class).isAssignableFrom(tmpChild.getClass())) {
										final Map tmpAttributes = (Map) ((Map) tmpChild).get("attributes");
										items.add(tmpAttributes);
									}
								}
							}
							config.put(tmpType, items);
						}
					}
				}
			}
		}

		final ResponseWriter writer = context.getResponseWriter();

		final String clientId = component.getClientId().replace(':', '_') + "carousel";

		writer.startElement("div", component);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", "carousel slide", null);
		writer.writeAttribute("data-interval", "false", null);

		// Indicator
		writer.startElement("ol", component);
		writer.writeAttribute("class", "carousel-indicators carousel-indicators-numbers hidden-md-up", null);

		// List calRows = (List) calendar.getAttributes().get("rows");
		List<Map> calRows = null;
		if (config != null) {
			calRows = config.get("rows");
		}
		if (calRows != null) {
			int slideId = 0;
			for (final Map row : calRows) {
				writer.startElement("li", component);
				if (slideId == 0)
					writer.writeAttribute("class", "active", null);
				writer.writeAttribute("data-target", "#" + clientId, null);
				writer.writeAttribute("data-slide-to", slideId, null);
				writer.write(row.get("title") + "");
				writer.endElement("li");
				slideId = slideId + 1;
			}
		}
		writer.endElement("ol");

		writer.startElement("div", component);
		writer.writeAttribute("role", "listbox", null);
		writer.writeAttribute("class", "carousel-inner", null);

		// Header
		writer.startElement("div", component);
		writer.writeAttribute("class", "calendar-head", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "calendar-header-cell calendar-header-cell-first", null);
		writer.writeAttribute("data-full-title", "", null);
		writer.writeAttribute("aria-hidden", "true", null);
		writer.endElement("div");

		// List calColumns = (List) calendar.getAttributes().get("columns");
		List<Map> calColumns = null;
		if (config != null) {
			calColumns = config.get("columns");
		}

		if (calColumns != null) {
			for (final Map column : calColumns) {
				writer.startElement("div", component);
				writer.writeAttribute("class", "calendar-header-cell", null);
				writer.writeAttribute("data-full-title", column.get("title") + "", null);
				writer.writeAttribute("aria-hidden", "true", null);
				writer.endElement("div");

			}
		}

		writer.endElement("div");

		// calculate slots
		final List<String> slotIds = new ArrayList<String>();
		slotIds.add("slot1");
		slotIds.add("slot2");
		slotIds.add("slot3");
		slotIds.add("slot4");
		slotIds.add("slot5");
		slotIds.add("slot6");
		slotIds.add("slot7");
		slotIds.add("slot8");
		slotIds.add("slot9");
		slotIds.add("slot10");
		slotIds.add("slot11");
		slotIds.add("slot12");
		slotIds.add("slot13");
		slotIds.add("slot14");
		slotIds.add("slot15");
		slotIds.add("slot16");
		slotIds.add("slot17");
		slotIds.add("slot18");
		slotIds.add("slot19");
		slotIds.add("slot20");

		final List<String> usedSlotIds = new ArrayList<String>();

		final Map<Integer, Map<String, Integer>> slotMap = new HashMap<Integer, Map<String, Integer>>();
		final Map<Integer, Map<String, Map<String, Object>>> metaMap = new HashMap<Integer, Map<String, Map<String, Object>>>();

		final Map calData = (Map) calendar.getAttributes().get("data");

		if (calData != null) {

			Map<Integer, List<List<Integer>>> episodeData = null;
			Map<Integer, List<Map<String, String>>> episodeMeta = null;

			if (calData.containsKey("data"))
				episodeData = (Map<Integer, List<List<Integer>>>) calData.get("data");
			if (calData.containsKey("meta"))
				episodeMeta = (Map<Integer, List<Map<String, String>>>) calData.get("meta");

			if (episodeData != null) {
				int minStartIndex = Integer.MAX_VALUE;
				int maxStopIndex = Integer.MIN_VALUE;
				final Map<Integer, List<String>> blockedRowSlotIds = new HashMap<Integer, List<String>>();

				for (final Map.Entry<Integer, List<List<Integer>>> eventSet : episodeData.entrySet()) {
					final Integer eventId = eventSet.getKey();
//					System.out.println("Event ID : "+eventId);
					final List<List<Integer>> eventEpisodes = eventSet.getValue();
					List<Map<String, String>> eventMetas = null;
					if (episodeMeta != null)
						eventMetas = episodeMeta.get(eventId);

					if (eventEpisodes != null) {
						String useSlot = null;
						// final List<String> eventUsedSlots = new ArrayList<String>();

						for (final List<Integer> eventEpisode : eventEpisodes) {
							final int tmpIndex = eventEpisodes.indexOf(eventEpisode);
//							System.out.println("Episode ID : "+tmpIndex);
							Map<String, String> eventMeta = null;
							if ((eventMetas != null) && (tmpIndex < eventMetas.size()))
								eventMeta = eventMetas.get(tmpIndex);

							final int startIndex = eventEpisode.get(0);
							minStartIndex = Math.min(minStartIndex, startIndex);

							final int stopIndex = eventEpisode.get(1);
							maxStopIndex = Math.max(maxStopIndex, stopIndex);

							final int currentStartRow = startIndex / calColumns.size();
							final int currentStopRow = stopIndex / calColumns.size();

//							int startRowIndex = 0;
//							if (currentStartRow > 0)
//								startRowIndex = ((currentStartRow - 1) * calColumns.size()) + 1;
//							int stopRowIndex = calColumns.size();
//							if (currentStopRow > 0)stopRowIndex = (currentStopRow + 1) * calColumns.size();

//							System.out.println(
//									"current start Row :" + currentStartRow + " current stop Row : " + currentStopRow);
//							System.out.println(
//									"current start Index :" + startRowIndex + " current stop Index : " + stopRowIndex);

							for (int row = currentStartRow; row <= currentStopRow; row++) {
								final Integer rowObj = new Integer(row);
								List<String> blockedRowSlotIdList = null;
								if (blockedRowSlotIds.containsKey(rowObj))
									blockedRowSlotIdList = blockedRowSlotIds.get(rowObj);
								
								if(blockedRowSlotIdList == null)blockedRowSlotIdList = new ArrayList<String>();
								
								int tmpStartRowIndex = 0;
								if(row >= 1)tmpStartRowIndex = (row * calColumns.size());						

								int tmpStopRowIndex = (calColumns.size()-1);
								if (row >= 1)tmpStopRowIndex = (row + 1) * (calColumns.size()-1);
								
//								System.out.println(
//										"# row :" + row + " start Row Index: " + tmpStartRowIndex+ " stop Row Index: " + tmpStopRowIndex);


								for (int index = tmpStartRowIndex; index <= tmpStopRowIndex; index++) {
									final Integer tileId = new Integer(index);
									Map<String, Integer> tile = slotMap.get(tileId);
									if (tile != null) {
										for(String slotId : tile.keySet()) {
											final String tmp = slotId+"";
											if(!blockedRowSlotIdList.contains(tmp)) {
												blockedRowSlotIdList.add(tmp);
											}
										}
									}
								}
//								System.out.println("row : "+row+" blockedRowSlotIdList : "+blockedRowSlotIdList);
								blockedRowSlotIds.put(rowObj, blockedRowSlotIdList);
							}

							final List<String> blockedSlotIds = new ArrayList<String>();
							for (int index = startIndex; index <= stopIndex; index++) {
								final Integer tileId = new Integer(index);
								
								final int rowIndex =  index / calColumns.size();
//								System.out.println("row index : "+rowIndex+" index : "+index);
								if(blockedRowSlotIds.containsKey(rowIndex)) {
//									System.out.println("block row : "+blockedRowSlotIds.get(rowIndex));
									blockedSlotIds.addAll(blockedRowSlotIds.get(rowIndex));
								}
								
								
								Map<String, Integer> tile = slotMap.get(tileId);
								if (tile != null) {
									blockedSlotIds.addAll(tile.keySet());
								}
							}

							// blockedSlotIds.addAll(eventUsedSlots);

							for (final String slotId : slotIds) {
								if (blockedSlotIds.contains(slotId))
									continue;
								useSlot = slotId;
								break;
							}

							if (useSlot == null)
								LOGGER.error("no more slot available");
							else {
								for (int index = startIndex; index <= stopIndex; index++) {
									final Integer tileId = new Integer(index);

									Map<String, Map<String, Object>> tileMeta = metaMap.get(tileId);
									if (tileMeta == null)
										tileMeta = new HashMap<String, Map<String, Object>>();

									Map<String, Object> slotMeta = tileMeta.get(useSlot);
									if (slotMeta == null)
										slotMeta = new HashMap<String, Object>();

									slotMeta.put("data-meta-start", startIndex);
									slotMeta.put("data-meta-stop", stopIndex);
									final StringBuffer eventMetaBuffer = new StringBuffer();
									if (eventMeta != null) {
										// slotMeta.put("data-meta-info", eventMeta);

										for (Map.Entry<String, String> metaItem : eventMeta.entrySet()) {
											eventMetaBuffer
													.append(metaItem.getKey() + " = " + metaItem.getValue() + "<br/>");
											// .append(metaItem.getKey() + " = " + metaItem.getValue() + " ");
										}

									}
									slotMeta.put("data-meta-info", eventMetaBuffer.toString());
									tileMeta.put(useSlot, slotMeta);
									metaMap.put(tileId, tileMeta);

									Map<String, Integer> tile = slotMap.get(tileId);
									if (tile == null)
										tile = new HashMap<String, Integer>();
									tile.put(useSlot, eventId);
									slotMap.put(tileId, tile);
									if (!usedSlotIds.contains(useSlot))
										usedSlotIds.add(useSlot);
									// if (!eventUsedSlots.contains(useSlot))
									// eventUsedSlots.add(useSlot);
								}
							}
						}
					}
				}
			}
		}
		// LOOP ROWS
		if (calRows != null) {
			Integer totalIndex = 0;
			int rowIndex = 0;
			for (final Map<?, ?> row : calRows) {
				final String rowId = rowIndex + "";

				writer.startElement("div", component);

				String classes = "row carousel-item";
				if (rowIndex == 0)
					classes = classes + " active";
				writer.writeAttribute("class", classes, null);

				writer.startElement("div", component);
				writer.writeAttribute("class", "calendar-row-container", null);
				writer.writeAttribute("data-legend", row.get("title") + "", null);

				writer.startElement("div", component);
				writer.writeAttribute("class", "calendar-row", null);
				writer.writeAttribute("name", rowId, null);
				writer.writeAttribute("id", rowId, null);
				writer.writeAttribute("data-rowindex", rowIndex, null);

				writer.startElement("div", component);
				writer.writeAttribute("class", "calendar-cell row-title", null);
				writer.writeAttribute("data-title", row.get("title") + "", null);
				writer.writeAttribute("aria-hidden", "true", null);
				writer.endElement("div");

				// LOOP CELLS
				if (calColumns != null) {
					int cellIndex = 0;

					Integer tmptotalIndex = new Integer(totalIndex.intValue());
					final List<String> tmpSlotIds = new ArrayList<String>();
					for (final Map column : calColumns) {
						Map<String, Integer> tile = slotMap.get(tmptotalIndex);

						if (tile != null)
							tmpSlotIds.addAll(tile.keySet());

						tmptotalIndex = tmptotalIndex + 1;
					}

					for (final Map column : calColumns) {
						final String colId = rowId + "_" + cellIndex;

						writer.startElement("div", component);
						writer.writeAttribute("class", "calendar-cell", null);
						writer.writeAttribute("name", colId, null);
						writer.writeAttribute("id", colId, null);
						writer.writeAttribute("data-cellindex", totalIndex, null);

						Map<String, Integer> tile = slotMap.get(totalIndex);
						Map<String, Map<String, Object>> tileMeta = metaMap.get(totalIndex);

						for (final String slotId : slotIds) {
							if (!usedSlotIds.contains(slotId))
								continue;
							writer.startElement("div", component);
							writer.writeAttribute("id", colId + "_" + slotId, null);

							String slotClasses = "display-calendar-slot-none";
							if (tmpSlotIds.contains(slotId))
								slotClasses = "display-calendar-slot-empty display-calendar-slot-used";
							if (tile != null) {
								if (tile.containsKey(slotId)) {
									writer.writeAttribute("data-event-id", tile.get(slotId), null);
									slotClasses = "display-calendar-slot-empty";
								}
							}

							if (tileMeta != null) {
								if (tileMeta.containsKey(slotId)) {
									Map<String, Object> slotMeta = tileMeta.get(slotId);
									if (slotMeta != null) {
										for (final Map.Entry<String, Object> slotMetaItem : slotMeta.entrySet()) {
											writer.writeAttribute(slotMetaItem.getKey(), slotMetaItem.getValue() + "",
													null);
										}
									}
								}
							}

							writer.writeAttribute("class", "display-calendar-slot " + slotClasses, null);

							writer.startElement("span", component);
							writer.writeAttribute("class", "sr-only", null);
							writer.endElement("span");
							writer.endElement("div");
						}

						writer.endElement("div");
						cellIndex = cellIndex + 1;
						totalIndex = totalIndex + 1;
					}
				}
				// LOOP CELLS END
				writer.endElement("div");
				writer.endElement("div");
				writer.endElement("div");
				rowIndex = rowIndex + 1;
			}
		}
		// LOOP ROWS END
	}

	// @Override
	// public void encodeChildren(final FacesContext context, final UIComponent
	// component) throws IOException {
	// if (component == null) {
	// return;
	// }
	// if (!component.isRendered()) {
	// return;
	// }
	// final UIDisplayCalendar calendar = (UIDisplayCalendar) component;
	//
	// final Object configObj = JsfUtility.getInstance().evaluateValueExpression(
	// FacesContext.getCurrentInstance(), "#{zofar.asXmlMap(\""
	// + URLDecoder.decode((String) calendar.getAttributes().get("config"), "UTF-8")
	// + "\")}",
	// Object.class);
	//
	// Map<String, List<Map>> config = null;
	// if ((configObj != null) &&
	// ((Map.class).isAssignableFrom(configObj.getClass()))) {
	// Map<String, Object> castedConfig = (Map<String, Object>) configObj;
	//
	// final Map configItems = (Map) ((List) castedConfig.get("childs")).get(0);
	// config = new HashMap<String, List<Map>>();
	// if (configItems != null) {
	// if (configItems.get("type").equals("config")) {
	// final List childs = (List) configItems.get("childs");
	// for (final Object child : childs) {
	// if ((Map.class).isAssignableFrom(child.getClass())) {
	// final Map tmp = (Map) child;
	// final String tmpType = (String) tmp.get("type");
	// final List tmpChilds = (List) tmp.get("childs");
	// List<Map> items = config.get(tmpType);
	// if (items == null)
	// items = new ArrayList<Map>();
	// if (tmpChilds != null) {
	// for (final Object tmpChild : tmpChilds) {
	// if ((Map.class).isAssignableFrom(tmpChild.getClass())) {
	// final Map tmpAttributes = (Map) ((Map) tmpChild).get("attributes");
	// items.add(tmpAttributes);
	// }
	// }
	// }
	// config.put(tmpType, items);
	// }
	// }
	// }
	// }
	// }
	//
	// final ResponseWriter writer = context.getResponseWriter();
	//
	// final String clientId = component.getClientId().replace(':', '_') +
	// "carousel";
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("id", clientId, null);
	// writer.writeAttribute("class", "carousel slide", null);
	// writer.writeAttribute("data-interval", "false", null);
	//
	// // Indicator
	// writer.startElement("ol", component);
	// writer.writeAttribute("class", "carousel-indicators
	// carousel-indicators-numbers hidden-md-up", null);
	//
	// // List calRows = (List) calendar.getAttributes().get("rows");
	// List<Map> calRows = null;
	// if (config !=) null) {
	// calRows = config.get("rows");
	// }
	// if (calRows != null) {
	// int slideId = 0;
	// for (final Map row : calRows) {
	// writer.startElement("li", component);
	// if (slideId == 0)
	// writer.writeAttribute("class", "active", null);
	// writer.writeAttribute("data-target", "#" + clientId, null);
	// writer.writeAttribute("data-slide-to", slideId, null);
	// writer.write(row.get("title") + "");
	// writer.endElement("li");
	// slideId = slideId + 1;
	// }
	// }
	// writer.endElement("ol");
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("role", "listbox", null);
	// writer.writeAttribute("class", "carousel-inner", null);
	//
	// // Header
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-head", null);
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-header-cell
	// calendar-header-cell-first", null);
	// writer.writeAttribute("data-full-title", "", null);
	// writer.writeAttribute("aria-hidden", "true", null);
	// writer.endElement("div");
	//
	// // List calColumns = (List) calendar.getAttributes().get("columns");
	// List<Map> calColumns = null;
	// if (config != null) {
	// calColumns = config.get("columns");
	// }
	//
	// if (calColumns != null) {
	// for (final Map column : calColumns) {
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-header-cell", null);
	// writer.writeAttribute("data-full-title", column.get("title") + "", null);
	// writer.writeAttribute("aria-hidden", "true", null);
	// writer.endElement("div");
	//
	// }
	// }
	//
	// writer.endElement("div");
	//
	// // calculate slots
	// final List<String> slotIds = new ArrayList<String>();
	// slotIds.add("slot1");
	// slotIds.add("slot2");
	// slotIds.add("slot3");
	// slotIds.add("slot4");
	// slotIds.add("slot5");
	// slotIds.add("slot6");
	// slotIds.add("slot7");
	// slotIds.add("slot8");
	// slotIds.add("slot9");
	// slotIds.add("slot10");
	// slotIds.add("slot11");
	// slotIds.add("slot12");
	// slotIds.add("slot13");
	// slotIds.add("slot14");
	// slotIds.add("slot15");
	// slotIds.add("slot16");
	// slotIds.add("slot17");
	// slotIds.add("slot18");
	// slotIds.add("slot19");
	// slotIds.add("slot20");
	//
	// final List<String> usedSlotIds = new ArrayList<String>();
	//
	// final Map<Integer, Map<String, Integer>> slotMap = new HashMap<Integer,
	// Map<String, Integer>>();
	// final Map<Integer, Map<String, Map<String, Object>>> metaMap = new
	// HashMap<Integer, Map<String, Map<String, Object>>>();
	//
	// // final Map<Integer, List<List<Integer>>> calData = (Map<Integer,
	// // List<List<Integer>>>) calendar.getAttributes()
	// // .get("data");
	//
	// final Map calData = (Map) calendar.getAttributes().get("data");
	//
	// if (calData != null) {
	//
	// Map<Integer, List<List<Integer>>> episodeData = null;
	// Map<Integer, List<Map<String, String>>> episodeMeta = null;
	//
	// if (calData.containsKey("data"))
	// episodeData = (Map<Integer, List<List<Integer>>>) calData.get("data");
	// if (calData.containsKey("meta"))
	// episodeMeta = (Map<Integer, List<Map<String, String>>>) calData.get("meta");
	//
	// if (episodeData != null) {
	//
	// // final List<String> blockedSlotIds = new ArrayList<String>();
	//// List<String> rowUsedSlots = new ArrayList<String>();
	//// int currentRowID = -1;
	//// int maxTotalStopIndex = 0;
	// for (final Map.Entry<Integer, List<List<Integer>>> eventSet :
	// episodeData.entrySet()) {
	// final Integer eventId = eventSet.getKey();
	// final List<List<Integer>> eventEpisodes = eventSet.getValue();
	// List<Map<String, String>> eventMetas = null;
	// if (episodeMeta != null)
	// eventMetas = episodeMeta.get(eventId);
	//
	// if (eventEpisodes != null) {
	// int maxStopIndex = 0;
	//
	// int stopIndex = 0;
	//
	// String useSlot = null;
	// List<String> eventUsedSlots = new ArrayList<String>();
	//
	// for (final List<Integer> eventEpisode : eventEpisodes) {
	// final int tmpIndex = eventEpisodes.indexOf(eventEpisode);
	// Map<String, String> eventMeta = null;
	// if ((eventMetas != null) && (tmpIndex < eventMetas.size()))
	// eventMeta = eventMetas.get(tmpIndex);
	//
	// final int startIndex = eventEpisode.get(0);
	//
	// if (startIndex > (maxStopIndex + 1))
	// eventUsedSlots.clear();
	//
	// stopIndex = eventEpisode.get(1);
	//
	// maxStopIndex = Math.max(maxStopIndex, stopIndex);
	//// maxTotalStopIndex = Math.max(maxStopIndex, stopIndex);
	//// if (calColumns != null) {
	//// int tmpRowID = maxTotalStopIndex / calColumns.size();
	//// System.out.println("tmp row id : "+tmpRowID);
	//// if (tmpRowID > currentRowID) {
	//// rowUsedSlots.clear();
	//// currentRowID = tmpRowID;
	//// }
	//// }
	//
	// final List<String> blockedSlotIds = new ArrayList<String>();
	// for (int index = startIndex; index <= stopIndex; index++) {
	// final Integer tileId = new Integer(index);
	// Map<String, Integer> tile = slotMap.get(tileId);
	// if (tile != null) {
	// blockedSlotIds.addAll(tile.keySet());
	// }
	// }
	//
	// blockedSlotIds.addAll(eventUsedSlots);
	//// blockedSlotIds.addAll(rowUsedSlots);
	//
	// for (final String slotId : slotIds) {
	// if (blockedSlotIds.contains(slotId))
	// continue;
	// useSlot = slotId;
	// break;
	// }
	//
	// if (useSlot == null)
	// LOGGER.error("no more slot available");
	// else {
	// for (int index = startIndex; index <= stopIndex; index++) {
	// final Integer tileId = new Integer(index);
	//
	// Map<String, Map<String, Object>> tileMeta = metaMap.get(tileId);
	// if (tileMeta == null)
	// tileMeta = new HashMap<String, Map<String, Object>>();
	//
	// Map<String, Object> slotMeta = tileMeta.get(useSlot);
	// if (slotMeta == null)
	// slotMeta = new HashMap<String, Object>();
	//
	// slotMeta.put("data-meta-start", startIndex);
	// slotMeta.put("data-meta-stop", stopIndex);
	// final StringBuffer eventMetaBuffer = new StringBuffer();
	// if (eventMeta != null) {
	// // slotMeta.put("data-meta-info", eventMeta);
	//
	// for (Map.Entry<String, String> metaItem : eventMeta.entrySet()) {
	// eventMetaBuffer
	// .append(metaItem.getKey() + " = " + metaItem.getValue() + "<br/>");
	// // .append(metaItem.getKey() + " = " + metaItem.getValue() + " ");
	// }
	//
	// }
	// slotMeta.put("data-meta-info", eventMetaBuffer.toString());
	// tileMeta.put(useSlot, slotMeta);
	// metaMap.put(tileId, tileMeta);
	//
	// Map<String, Integer> tile = slotMap.get(tileId);
	// if (tile == null)
	// tile = new HashMap<String, Integer>();
	// tile.put(useSlot, eventId);
	// slotMap.put(tileId, tile);
	// if (!usedSlotIds.contains(useSlot))
	// usedSlotIds.add(useSlot);
	// if (!eventUsedSlots.contains(useSlot))
	// eventUsedSlots.add(useSlot);
	//// if (!rowUsedSlots.contains(useSlot))
	//// rowUsedSlots.add(useSlot);
	// }
	// }
	// }
	// }
	// }
	//
	// // for (final Map.Entry<Integer, List<List<Integer>>> eventSet :
	// // episodeData.entrySet()) {
	// // final Integer eventId = eventSet.getKey();
	// // final List<List<Integer>> eventEpisodes = eventSet.getValue();
	// // List<Map<String, String>> eventMetas = null;
	// // if (episodeMeta != null)
	// // eventMetas = episodeMeta.get(eventId);
	// //
	// // if (eventEpisodes != null) {
	// // int episodesBefore = 0;
	// // // int lastStopIndex = -2;
	// // int stopIndex = 0;
	// //
	// // String useSlot = null;
	// // String lastUsedSlot = null;
	// //
	// // for (final List<Integer> eventEpisode : eventEpisodes) {
	// // final int tmpIndex = eventEpisodes.indexOf(eventEpisode);
	// // Map<String, String> eventMeta = null;
	// // if ((eventMetas != null) && (tmpIndex < eventMetas.size()))
	// // eventMeta = eventMetas.get(tmpIndex);
	// //
	// // final int startIndex = eventEpisode.get(0);
	// //
	// // boolean flag = false;
	// //
	// // if (startIndex <= (stopIndex + 1)) {
	// // flag = true;
	// // }
	// //
	// // // lastStopIndex = stopIndex;
	// // stopIndex = eventEpisode.get(1);
	// // final List<String> blockedSlotIds = new ArrayList<String>();
	// // for (int index = startIndex; index <= stopIndex; index++) {
	// // final Integer tileId = new Integer(index);
	// // Map<String, Integer> tile = slotMap.get(tileId);
	// // if (tile != null) {
	// // blockedSlotIds.addAll(tile.keySet());
	// // }
	// // }
	// //
	// // for (final String slotId : slotIds) {
	// // if (blockedSlotIds.contains(slotId))
	// // continue;
	// //// if ((useSlot != null) && (lastUsedSlot != null) && flag &&
	// // (slotId.equals(lastUsedSlot))) {
	// //// blockedSlotIds.add(slotId);
	// //// continue;
	// //// }
	// // lastUsedSlot = useSlot;
	// // useSlot = slotId;
	// // break;
	// // }
	// //
	// // if (useSlot == null)
	// // LOGGER.error("no more slot available");
	// // else {
	// // if (flag && (useSlot.equals(lastUsedSlot))) {
	// // episodesBefore = episodesBefore + 1;
	// // } else {
	// // episodesBefore = 0;
	// // }
	// // for (int index = startIndex; index <= stopIndex; index++) {
	// // final Integer tileId = new Integer(index);
	// //
	// // Map<String, Map<String, Object>> tileMeta = metaMap.get(tileId);
	// // if (tileMeta == null)
	// // tileMeta = new HashMap<String, Map<String, Object>>();
	// //
	// // Map<String, Object> slotMeta = tileMeta.get(useSlot);
	// // if (slotMeta == null)
	// // slotMeta = new HashMap<String, Object>();
	// //
	// // slotMeta.put("data-meta-start", startIndex);
	// // slotMeta.put("data-meta-stop", stopIndex);
	// // slotMeta.put("data-meta-color-grade", episodesBefore);
	// //
	// // if (eventMeta != null) {
	// // eventMeta.put("data-meta-color-grade", episodesBefore + "");
	// // // slotMeta.put("data-meta-info", eventMeta);
	// // final StringBuffer eventMetaBuffer = new StringBuffer();
	// // for (Map.Entry<String, String> metaItem : eventMeta.entrySet()) {
	// // eventMetaBuffer
	// // .append(metaItem.getKey() + " = " + metaItem.getValue() + "<br/>");
	// // }
	// //
	// // slotMeta.put("data-meta-info", eventMetaBuffer.toString());
	// // }
	// //
	// // tileMeta.put(useSlot, slotMeta);
	// // metaMap.put(tileId, tileMeta);
	// //
	// // Map<String, Integer> tile = slotMap.get(tileId);
	// // if (tile == null)
	// // tile = new HashMap<String, Integer>();
	// // tile.put(useSlot, eventId);
	// // slotMap.put(tileId, tile);
	// // if (!usedSlotIds.contains(useSlot))
	// // usedSlotIds.add(useSlot);
	// // }
	// // }
	// // }
	// // }
	// // }
	// }
	//
	// }
	//
	// // LOOP ROWS
	// if (calRows != null) {
	// Integer totalIndex = 0;
	// int rowIndex = 0;
	// for (final Map row : calRows) {
	// final String rowId = rowIndex + "";
	//
	// writer.startElement("div", component);
	//
	// String classes = "row carousel-item";
	// if (rowIndex == 0)
	// classes = classes + " active";
	// writer.writeAttribute("class", classes, null);
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-row-container", null);
	// writer.writeAttribute("data-legend", row.get("title") + "", null);
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-row", null);
	// writer.writeAttribute("name", rowId, null);
	// writer.writeAttribute("id", rowId, null);
	// writer.writeAttribute("data-rowindex", rowIndex, null);
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-cell row-title", null);
	// writer.writeAttribute("data-title", row.get("title") + "", null);
	// writer.writeAttribute("aria-hidden", "true", null);
	// writer.endElement("div");
	//
	// // LOOP CELLS
	// if (calColumns != null) {
	// int cellIndex = 0;
	//
	// Integer tmptotalIndex = new Integer(totalIndex.intValue());
	// final List<String> tmpSlotIds = new ArrayList<String>();
	// for (final Map column : calColumns) {
	// Map<String, Integer> tile = slotMap.get(tmptotalIndex);
	//
	// if (tile != null)
	// tmpSlotIds.addAll(tile.keySet());
	//
	// tmptotalIndex = tmptotalIndex + 1;
	// }
	//
	// for (final Map column : calColumns) {
	// final String colId = rowId + "_" + cellIndex;
	//
	// writer.startElement("div", component);
	// writer.writeAttribute("class", "calendar-cell", null);
	// writer.writeAttribute("name", colId, null);
	// writer.writeAttribute("id", colId, null);
	// writer.writeAttribute("data-cellindex", totalIndex, null);
	//
	// Map<String, Integer> tile = slotMap.get(totalIndex);
	// Map<String, Map<String, Object>> tileMeta = metaMap.get(totalIndex);
	//
	// for (final String slotId : slotIds) {
	// if (!usedSlotIds.contains(slotId))
	// continue;
	// writer.startElement("div", component);
	// writer.writeAttribute("id", colId + "_" + slotId, null);
	//
	// String slotClasses = "display-calendar-slot-none";
	// if (tmpSlotIds.contains(slotId))
	// slotClasses = "display-calendar-slot-empty display-calendar-slot-used";
	// if (tile != null) {
	// if (tile.containsKey(slotId)) {
	// writer.writeAttribute("data-event-id", tile.get(slotId), null);
	// slotClasses = "display-calendar-slot-empty";
	// }
	// }
	//
	// if (tileMeta != null) {
	// if (tileMeta.containsKey(slotId)) {
	// Map<String, Object> slotMeta = tileMeta.get(slotId);
	// if (slotMeta != null) {
	// for (final Map.Entry<String, Object> slotMetaItem : slotMeta.entrySet()) {
	// writer.writeAttribute(slotMetaItem.getKey(), slotMetaItem.getValue() + "",
	// null);
	// }
	// }
	// }
	// }
	//
	// writer.writeAttribute("class", "display-calendar-slot " + slotClasses, null);
	//
	// writer.startElement("span", component);
	// writer.writeAttribute("class", "sr-only", null);
	// writer.endElement("span");
	// writer.endElement("div");
	// }
	//
	// writer.endElement("div");
	// cellIndex = cellIndex + 1;
	// totalIndex = totalIndex + 1;
	// }
	// }
	// // LOOP CELLS END
	//
	// writer.endElement("div");
	// writer.endElement("div");
	// writer.endElement("div");
	// rowIndex = rowIndex + 1;
	// }
	// }
	//
	// // LOOP ROWS END
	// }

	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		if (component == null) {
			return;
		}
		if (!component.isRendered()) {
			return;
		}
		final String clientId = component.getClientId().replace(':', '_') + "carousel";

		final ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");

		// writer.startElement("div", component);
		// writer.writeAttribute("class", "", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "calendar-control", null);

		writer.startElement("div", component);
		writer.writeAttribute("class", "carousel-control-outer hidden-md-up", null);

		writer.startElement("a", component);
		writer.writeAttribute("class", "btn btn-secondary carousel-control-prev", null);
		writer.writeAttribute("hRef", "#" + clientId, null);
		writer.writeAttribute("role", "button", null);
		writer.writeAttribute("data-slide", "prev", null);

		writer.startElement("span", component);
		writer.writeAttribute("class", "label", null);
		writer.endElement("span");

		writer.startElement("i", component);
		writer.writeAttribute("class", "fa fa-angle-left", null);
		writer.writeAttribute("aria-hidden", "true", null);
		writer.endElement("i");

		writer.startElement("span", component);
		writer.writeAttribute("class", "sr-only", null);
		writer.write("Previous");
		writer.endElement("span");

		writer.endElement("a");

		writer.startElement("a", component);
		writer.writeAttribute("class", "btn btn-secondary carousel-control-next", null);
		writer.writeAttribute("hRef", "#" + clientId, null);
		writer.writeAttribute("role", "button", null);
		writer.writeAttribute("data-slide", "next", null);

		writer.startElement("i", component);
		writer.writeAttribute("class", "fa fa-angle-right", null);
		writer.writeAttribute("aria-hidden", "true", null);
		writer.endElement("i");

		writer.startElement("span", component);
		writer.writeAttribute("class", "label", null);
		writer.endElement("span");

		writer.startElement("span", component);
		writer.writeAttribute("class", "sr-only", null);
		writer.write("Next");
		writer.endElement("span");

		writer.endElement("a");

		writer.endElement("div");

		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("div");
		writer.endElement("article");
	}

}
