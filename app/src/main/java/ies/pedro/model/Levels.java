package ies.pedro.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import ies.pedro.utils.BlockAdapterJSON;
import ies.pedro.utils.LevelAdapterXML;
import ies.pedro.utils.Point2DAdapterJSON;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.geometry.Point2D;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
@XmlRootElement
public class Levels {


    private Level selected;
    @Expose()
    private final ArrayList<Level> levels;



    public Levels() {
        this.levels = new ArrayList<>();
    }
    public void reset(){
        this.levels.clear(); // = new ArrayList<>();
    }
    public static void save(Levels levels, File file) throws Exception {
       /* if (levels.sound == null || levels.sound.isEmpty()) {
            throw new Exception("Es necesario indicar el sonido del laberinto");
        }*/
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        if (extension.equals("xml")) {
            Levels.saveXML(levels, file);
        } else {
            if (extension.equals("json")) {
                Levels.saveJSON(levels, file);

            } else {
                if (extension.equals("bin")) {
                    Levels.saveBin(levels, file);
                } else {
                    throw new Exception("Exensión " + extension + " no permitida");

                }
            }

        }
    }
    public static Levels load(File file) throws JAXBException, IOException, FileNotFoundException, ClassNotFoundException, Exception {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Levels m=null ;
        if (extension.equals("xml")) {
            m = Levels.loadXML(file);
        } else {
            if (extension.equals("json")) {
                m = Levels.loadJSON(file);

            } else {
                if (extension.equals("bin")) {
                    m = Levels.loadBin(file);
                } else {
                    throw new Exception("Exencsión " + extension + " no permitida");

                }
            }

        }
        return m;

    }
    private static Levels loadJSON(File file) throws FileNotFoundException, IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Point2D.class, new Point2DAdapterJSON());
        builder.registerTypeAdapter(Block.class, new BlockAdapterJSON());
        Gson gson=builder.excludeFieldsWithoutExposeAnnotation().create();
        Reader reader;
        reader = Files.newBufferedReader(file.toPath());
        Levels m = gson.fromJson(reader, Levels.class);
        reader.close();
        return m;
    }

    private static Levels loadXML(File file) throws JAXBException, IOException {

        JAXBContext context = JAXBContext.newInstance(Levels.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Levels) unmarshaller.unmarshal(
                file);

    }

    public static Levels loadBin(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream os = new FileInputStream(file);

        ObjectInputStream oos = new ObjectInputStream(os);
        Levels m = (Levels) oos.readObject();
        oos.close();
        os.close();
        return m;
    }

    private static void saveJSON(Levels levels, File file) throws FileNotFoundException, UnsupportedEncodingException {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Point2D.class, new Point2DAdapterJSON());
        builder.registerTypeAdapter(Block.class, new BlockAdapterJSON());
        Gson gson=builder.excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(levels);
        java.io.PrintWriter pw = new PrintWriter(file, "UTF-8");
        pw.print(json);
        pw.close();
    }

    private static void saveXML(Levels levels, File file) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(levels.getClass());

        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);//(file, "UTF-8");
        marshaller.marshal(levels, fw);
        fw.close();

    }

    public static void saveBin(Levels levels, File file) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(levels);
        oos.close();
        os.close();
    }


    public void addLevel(Level level) {
        this.levels.add(level);
    }
    @XmlJavaTypeAdapter(LevelAdapterXML.class)
    public Level getSelected(){
        return this.selected;
    }
    @XmlElement
    public ArrayList<Level> getLevels(){
        return this.levels;
    }
    public void setSelected(Level selected){
        this.selected = selected;
    }
    public void setSelected(int index){
        this.selected = this.levels.get(index);
    }
    public void setSelected(String name){
        this.selected = this.levels.stream().filter( l ->
                l.getName().equals(name)).findFirst().get();
    }
    public void resetSelected(){
        this.selected.reset();
    }
    public Level getLevelByIndex(int index){
        return this.levels.get(index);
    }
    public Level getLevelByName(String name){
        return this.levels.stream().filter( l->l.getName().equals(name)).findFirst().get();
    }

    public void removeLevel(String name){
        this.levels.removeIf(level -> level.getName().equals(name));
    }
}
