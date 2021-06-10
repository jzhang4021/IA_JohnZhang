package com.example.ia_johnzhang;


// solution from https://www.youtube.com/watch?v=lY9zSr6cxko
public class addVideo {
    public String name;
    public String url;

    public addVideo(){
    }

    public addVideo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
