package ies.pedro.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import javafx.geometry.Point2D;

import java.io.IOException;

public class Point2DAdapterJSON extends TypeAdapter<Point2D> {
    public Point2D read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String xy = reader.nextString();
        String[] parts = xy.split(",");
        Float x = Float.parseFloat(parts[0]);
        Float y = Float.parseFloat(parts[1]);
        return new Point2D(x, y);
    }
    public void write(JsonWriter writer, Point2D value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String xy = value.getX() + "," + value.getY();
        writer.value(xy);
    }
}