package co.edu.umanizales.appmusic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MultimediaContent {
    protected String id;
    protected String title;
    protected double duration;

    // constructor para inicializar los datos básicos
    public MultimediaContent(String id, String title, double duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    //Metodo abstracto que sera implementado por las subclases
    public abstract void play();
}
