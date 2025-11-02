package co.edu.umanizales.appmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public abstract class MultimediaContent {
    protected String id;
    protected String title;
    protected double duration;

    //Metodo abstracto que sera implementado por las subclases
    public abstract void play();
}
