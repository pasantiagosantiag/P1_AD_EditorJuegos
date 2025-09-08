package ies.pedro.utils;

import ies.pedro.model.Level;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class LevelAdapterXML extends XmlAdapter<String,Level> {

    @Override
    public String marshal(Level v) throws Exception {

        return null;
    }

    @Override
    public Level unmarshal(String v) throws Exception {
        return null;
    }
}
