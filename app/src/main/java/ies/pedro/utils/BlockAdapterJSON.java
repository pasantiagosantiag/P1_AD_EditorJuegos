package ies.pedro.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ies.pedro.model.Block;
import javafx.geometry.Point2D;

import java.io.IOException;

public class BlockAdapterJSON extends TypeAdapter<Block> {
    public Block read(JsonReader reader) throws IOException {
        Block block = new Block();
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();

            return block;
        }
        String value = reader.nextString();
       // Block b= new Block();
        block.setValue(value);

        return block;
    }
    public void write(JsonWriter writer, Block value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String xy = value.getValue();
        writer.value(xy);
    }
}