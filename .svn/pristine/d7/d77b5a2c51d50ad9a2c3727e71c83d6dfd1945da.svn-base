package com.augurit.agmobile.mapengine.common.agmobilelayer.util;


import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.agmobilelayer.model.CapabilitiesBean;
import com.esri.core.geometry.Point;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luob on 2016/1/14.
 */
public final class PullParseXmlUtil {
    private static ByteArrayInputStream tInputStringStream = null;

    private PullParseXmlUtil() {
    }

    public static CapabilitiesBean pullParseXML(String xml) {
        CapabilitiesBean poi = null;
        List<Double> scales = null;
        int i = 0;
        int j = 0;
        //        String lastNode = "";
        int min = 0;
        int max = 0;
        if (xml == null || xml.trim().equals("")) {
            return null;
        }
        try {
            tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            //获取XmlPullParser的实例
            XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
            //设置输入流  xml文件
            xmlPullParser.setInput(tInputStringStream, "UTF-8");

            //开始
            int eventType = xmlPullParser.getEventType();
            boolean hasTileMatrixSet = false;
            String parentTag = "";
            try {
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        //文档开始
                        case XmlPullParser.START_DOCUMENT:
                            poi = new CapabilitiesBean();
                            scales = new ArrayList<>();
                            break;
                        //开始节点
                        case XmlPullParser.START_TAG:
                            //无父节点
                            if (nodeName.equalsIgnoreCase("ows:ServiceTypeVersion")) {
                                if (TextUtils.isEmpty(poi.getVersion())) {
                                    poi.setVersion(xmlPullParser.nextText());
                                }
                            } else if (nodeName.equalsIgnoreCase("Format")) {
                                if (TextUtils.isEmpty(poi.getFormat())) {
                                    poi.setFormat(xmlPullParser.nextText());
                                }
                            } else if (nodeName.equalsIgnoreCase("ows:Value")) {
                                if (TextUtils.isEmpty(poi.getServiceMode())) {
                                    poi.setServiceMode(xmlPullParser.nextText());
                                }
                            } else if (nodeName.equalsIgnoreCase("TopLeftCorner")) {
                                if (poi.getTopLeftCorner() == null) {
                                    poi.setTopLeftCorner( getPoint(xmlPullParser.nextText()));
                                }
                            }

                            //父节点
                            if (nodeName.equalsIgnoreCase("Layer")) {
                                parentTag = "Layer";
                            } else if (nodeName.equalsIgnoreCase("Style")) {
                                parentTag = "Style";
                            } else if (nodeName.equalsIgnoreCase("ows:BoundingBox")) {
                                parentTag = "ows:BoundingBox";
                            } else if (nodeName.equalsIgnoreCase("TileMatrix")) {
                                parentTag = "TileMatrix";
                            } else if (nodeName.equalsIgnoreCase("TileMatrixSet")) {
                                parentTag = "TileMatrixSet";
                            }

                            //子节点
                            if (parentTag.equalsIgnoreCase("Layer")
                                    && nodeName.equalsIgnoreCase("ows:Identifier")) {//"Layer"子节点
                                if (TextUtils.isEmpty(poi.getLayer())) {
                                    poi.setLayer(xmlPullParser.nextText());
                                }
                            } else if (parentTag.equalsIgnoreCase("Style")
                                    && nodeName.equalsIgnoreCase("ows:Identifier")) {//"Style"子节点
                                if (TextUtils.isEmpty(poi.getStyle())) {
                                    poi.setStyle(xmlPullParser.nextText());
                                }
                            } else if (parentTag.equalsIgnoreCase("ows:BoundingBox")) {//"ows:BoundingBox"子节点
                                if (nodeName.equalsIgnoreCase("ows:UpperCorner")) {
                                    if (poi.getUpperCornerPoint() == null) {
                                        poi.setUpperCornerPoint(getPoint(xmlPullParser.nextText()));
                                    }
                                } else if (nodeName.equalsIgnoreCase("ows:LowerCorner")) {
                                    if (poi.getLowerCornerPoint() == null) {
                                        poi.setLowerCornerPoint(getPoint(xmlPullParser.nextText()));
                                    }
                                }
                            } else if (parentTag.equalsIgnoreCase("TileMatrix")) {//"TileMatrix"子节点
                                if (nodeName.equalsIgnoreCase("ows:Identifier") & !hasTileMatrixSet) {
                                    int value = Integer.valueOf(xmlPullParser.nextText());
                                    if (min == 0 && max == 0) {
                                        min = value;
                                        max = value;
                                    } else if (min > value) {
                                        min = value;
                                    } else if (max < value) {
                                        max = value;
                                    }
                                } else if (nodeName.equalsIgnoreCase("ScaleDenominator") & !hasTileMatrixSet) {
                                    scales.add(Double.valueOf(xmlPullParser.nextText()));
                                }
                            } else if (parentTag.equalsIgnoreCase("TileMatrixSet")) {//"TileMatrixSet"子节点
                                if (nodeName.equalsIgnoreCase("ows:Identifier")) {
                                    if (!TextUtils.isEmpty(poi.getTileMatrixSet())) {
                                        hasTileMatrixSet = true;
                                        break;
                                    }
                                    poi.setTileMatrixSet(xmlPullParser.nextText());
                                } else if (nodeName.equalsIgnoreCase("ows:SupportedCRS") & !hasTileMatrixSet) {
                                    if (TextUtils.isEmpty(poi.getSpatialReference())) {
                                        poi.setSpatialReference(getDigit(xmlPullParser.nextText()));
                                    }
                                }
                            }


                            /*//判断如果其实节点为Poi
                            if ("Layer".equals(lastNode) && !"ows:Title".equals(nodeName)) {
                                break;
                            }
                            if ("Style".equals(lastNode) && !"ows:Identifier".equals(nodeName)) {
                                break;
                            }
                            if ("Layer".equals(lastNode) && "ows:Title".equals(nodeName)) {
                                poi.setLayer(xmlPullParser.nextText());
                            } else if (!"Format".equals(lastNode) && "Format".equals(nodeName)) {
                                poi.setFormat(xmlPullParser.nextText());
                            } else if ("TileMatrixSet".equals(lastNode) && "ows:Identifier".equals(nodeName)) {
                                if (!TextUtils.isEmpty(poi.getTileMatrixSet())) {
                                    finishTileMatrixSet = true;
                                }
                                if (!finishTileMatrixSet) {
                                    poi.setTileMatrixSet(xmlPullParser.nextText());
                                }
                            } else if (lastNode.contains("ows:BoundingBox") && "ows:LowerCorner".equals(nodeName)) {
                                poi.setLowerCornerPoint(getPoint(xmlPullParser.nextText()));
                                break;
                            } else if (lastNode.contains("ows:BoundingBox") && "ows:UpperCorner".equals(nodeName)) {
                                poi.setUpperCornerPoint(getPoint(xmlPullParser.nextText()));
                            } else if ("ows:AllowedValues".equals(lastNode) && "ows:Value".equals(nodeName)) {
                                poi.setServiceMode(xmlPullParser.nextText());
                            } else if ("Style".equals(lastNode) && "ows:Identifier".equals(nodeName)) {
                                poi.setStyle(xmlPullParser.nextText());
                            } else if ("ows:SupportedCRS".equals(nodeName)) {
                                if (!finishTileMatrixSet) {
                                    poi.setSpatialReference(getDigit(xmlPullParser.nextText()));
                                }
                            } else if ("TileMatrix".equals(lastNode) && "ows:Identifier".equals(nodeName)) {
                                if (!finishTileMatrixSet) {
                                    int value = Integer.valueOf(xmlPullParser.nextText());
                                    if (min == 0 && max == 0) {
                                        min = value;
                                        max = value;
                                    } else if (min > value) {
                                        min = value;
                                    } else if (max < value) {
                                        max = value;
                                    }
                                }
                            } else if ("ScaleDenominator".equals(nodeName)) {
                                if (!finishTileMatrixSet) {
                                    scales.add(Double.valueOf(xmlPullParser.nextText()));
                                }
                            } else if ("ScaleDenominator".equals(lastNode) && "TopLeftCorner".equals(nodeName)) {
                                if (!finishTileMatrixSet && poi.getTopLeftCorner() == null) {
                                    Point readValue = getPoint(xmlPullParser.nextText());
                                    poi.setTopLeftCorner(new Point(readValue.getY(), readValue.getX()));
                                }
                            } else if ("ows:ServiceTypeVersion".equals(nodeName)) {
                                poi.setVersion(xmlPullParser.nextText());
                            }

                            lastNode = nodeName;*/

                            break;
                        //结束节点
                        case XmlPullParser.END_TAG:
                            break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                poi.setMinZoomLevel(min);
                poi.setMaxZoomLevel(max);
                poi.setScales(scales);
                tInputStringStream.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if (poi.getLayer() == null && poi.getLowerCornerPoint() == null) {
            return null;
        }
        return poi;
    }

    public static Point getPoint(String strPoint) {
        String[] points = strPoint.split(" ");
        if (points == null || points.length == 0) {
            return null;
        } else if (points.length == 2) {
            return new Point(Double.valueOf(points[0]), Double.valueOf(points[1]));
        }
        return null;
    }

    public static String getDigit(String str) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }
}
