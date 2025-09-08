/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ies.pedro.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import ies.pedro.utils.Point2DAdapterXML;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import jakarta.xml.bind.Unmarshaller;
import ies.pedro.utils.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.Serializable;


import java.io.UnsupportedEncodingException;


import java.nio.file.Files;
import java.util.Arrays;


@XmlRootElement

public class Level implements Serializable {
    @Expose
    private Size size;
    @Expose
    private Block[][] blocks;
    @Expose
    private double time;
    @Expose
    private String sound;
    @Expose
    private String name;
    @Expose
    private String backgroundImage;

    public Level(String name) {
        this.name = name;

    }
  public Level(String name, Size size) {
        this.name = name;
        this.size=size;

    }
    public Level() {

    }

    public void init() {
        this.setBlocks(new Block[this.getSize().getHeight()][this.getSize().getWidth()]);
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                if (this.getBlocks()[i][j] == null)
                    this.getBlocks()[i][j] = new Block();

            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getBlocks().length; i++) {
            Arrays.fill(this.getBlocks()[i], null);
        }
        this.backgroundImage = null;
        this.sound = null;
        this.init();

    }


    public void setBlockValue(String value, int row, int col) {

        this.getBlocks()[row][col].setValue(value);
    }

    public String getBlockValue(int row, int col) {
        if (this.getBlocks()[row][col] == null || this.getBlocks()[row][col].getValue() == null)
            return null;
        else
            return this.getBlocks()[row][col].getValue();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    //@XmlElement
  //  @XmlJavaTypeAdapter(Point2DAdapterXML.class)
    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundPosition(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        String tempo;
        cadena.append("Nivel ").append(this.getName()).append("\n");
        cadena.append("Backgroud:").append(this.backgroundImage).append("\n");
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                //cadena.append("["+i+","+j+"]"+ this.getBlocks()[i][j]!=null?this.getBlocks()[i][j].toString():""+" "+"");
                tempo = this.getBlocks()[i][j] != null ? this.getBlocks()[i][j].toString() : "";
                cadena.append("[").append(i).append(",").append(j).append("]").append(tempo).append(" ");
            }
            cadena.append("\n");
        }
        return cadena.toString();
    }
   /* public static Level load(File file) throws JAXBException, IOException, FileNotFoundException, ClassNotFoundException, Exception {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
       Level m ;
        if (extension.equals("xml")) {
            m = Level.loadXML(file);
        } else {
            if (extension.equals("json")) {
                m = Level.loadJSON(file);

            } else {
                if (extension.equals("bin")) {
                    m = Level.loadBin(file);
                } else {
                    throw new Exception("Exencsión " + extension + " no permitida");

                }
            }

        }
        return m;

    }
*/


   /* private static Level loadJSON(File file) throws FileNotFoundException, IOException {
        Gson gson = new Gson();
        Reader reader;
        reader = Files.newBufferedReader(file.toPath());
        Level m = gson.fromJson(reader, Level.class);
        reader.close();
        return m;
    }

    private static Level loadXML(File file) throws JAXBException, IOException {

           JAXBContext context = JAXBContext.newInstance(Level.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Level) unmarshaller.unmarshal(
                        file);
          
    }

    public static Level loadBin(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream os = new FileInputStream(file);
        
        ObjectInputStream oos = new ObjectInputStream(os);
        Level m = (Level) oos.readObject();
        oos.close();
        os.close();
        return m;
    }





    public static void saveBin(Level maze, File file) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(maze);
        oos.close();
        os.close();
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
