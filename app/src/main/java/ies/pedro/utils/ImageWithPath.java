package ies.pedro.utils;

import javafx.scene.image.Image;
import java.io.File;
import java.net.URL;

public class ImageWithPath {
    private final Image image;
    private final String path;  // la ruta en String (puede ser absoluta, relativa o URI)

    // Cargar desde un File
    public ImageWithPath(File file) {
        this.path = file.getAbsolutePath();
        this.image = new Image(file.toURI().toString());
    }

    // Cargar desde un String (puede ser ruta local o URL)
    public ImageWithPath(String path) {
        this.path = path;
        if (new File(path).exists()) {
            this.image = new Image(new File(path).toURI().toString());
        } else {
            this.image = new Image(path); // asume que es URI o recurso
        }
    }

    // Cargar desde recurso en el classpath
    public ImageWithPath(URL resourceUrl) {
        this.path = resourceUrl.toString();
        this.image = new Image(resourceUrl.toExternalForm());
    }

    public Image getImage() {
        return image;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ImageWithPath[path=" + path + "]";
    }
}