package com.example.ia_johnzhang;

import java.io.Serializable;

public class Exercise implements Serializable {
    String name;
    String RPE;
    String resourcePath;

    public Exercise(){
        name = "name";
        RPE = "N/A";
    }

    public Exercise(String name, String RPE){
        this.name = name;
        this.RPE = RPE;
        resourcePath = "";
    }

    public Exercise(String name, String RPE, String resourcePath) {
        this.name = name;
        this.RPE = RPE;
        this.resourcePath = resourcePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRPE() {
        return RPE;
    }

    public void setRPE(String RPE) {
        this.RPE = RPE;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
