package ies.pedro.utils;
import com.google.gson.annotations.Expose;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Point implements Cloneable, Comparable<Point>, Serializable {
    @Expose
    private int x;
    @Expose
    private int y;

    public Point(){
    }

    public Point(int x,int y){
        this.x=x;
        this.y=y;
    }
    public Object clone() throws CloneNotSupportedException{
        return new Point(this.x, this.y);
    }
    @Override
    public boolean equals(Object o){
        if(! (o instanceof Point)){
            return false;
        }
        if (this.getX()==((Point)(o)).getX() && this.getY()==((Point)(o)).getY()){
            return true;
        }else{
            return false;
        }

    }
    @Override
    public int compareTo(Point o) {
        if(this.getX()==o.getX() && this.getY()==o.getY())
            return 0;
        if(this.getX()<o.getX())
            return -1;
        else
            return 1;
    }
    public String toString(){
        return "X:"+this.x+" Y:"+this.y;
    }
    /**
     * @return the width
     */
    public int getX() {
        return x;
    }

    /**
     * @return the height
     */
    public int getY() {
        return y;
    }

    /**
     * @param width the width to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param height the height to set
     */
    public void setY(int y) {
        this.y = y;
    }


}
