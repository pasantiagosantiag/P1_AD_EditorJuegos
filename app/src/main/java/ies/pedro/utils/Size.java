package ies.pedro.utils;
import com.google.gson.annotations.Expose;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Size implements Cloneable, Comparable<Size>, Serializable {
    @Expose
    private int width;
    @Expose
    private int height;

    public Size(){
    }

    public Size(int width,int height){
        this.width=width;
        this.height=height;
    }
    public Object clone() throws CloneNotSupportedException{
        return new Size(this.getWidth(), this.getHeight());
    }
    @Override
    public boolean equals(Object o){
        if(! (o instanceof Size)){
            return false;
        }
        if (this.getWidth()==((Size)(o)).getWidth() && this.getHeight()==((Size)(o)).getHeight()){
            return true;
        }else{
            return false;
        }

    }
    @Override
    public int compareTo(Size o) {
        if(this.getWidth()==o.getWidth() && this.getHeight()==o.getHeight())
            return 0;
        if(this.getWidth()<o.getWidth())
            return -1;
        else
            return 1;
    }
    public String toString(){
        return "W:"+this.width+" H:"+this.height;
    }
    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }


}
