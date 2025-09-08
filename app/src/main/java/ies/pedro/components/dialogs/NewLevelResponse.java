package ies.pedro.components.dialogs;

import ies.pedro.utils.Size;

public class NewLevelResponse {

    private String name;
    private Size size;
   public NewLevelResponse(){
        this.name="";
        this.size= new Size();
    }
    public NewLevelResponse(String name, Size size){
        this.name=name;
        this.size=size;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Size getSize() {
        return size;
    }
    public void setSize(Size size) {
        this.size = size;
    }

    
}
