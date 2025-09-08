package ies.pedro.utils;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import javafx.geometry.Point2D;

public class Point2DAdapterXML extends XmlAdapter<String, Point2D> {

    // Convierte el XML (String) a un objeto Point2D
    @Override
    public Point2D unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) {
            return null;
        }
        String[] parts = v.split(",");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        return new Point2D(x, y);
    }

    // Convierte un Point2D a String para ser serializado a XML
    @Override
    public String marshal(Point2D v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.getX() + "," + v.getY(); // Formato "x,y"
    }
}