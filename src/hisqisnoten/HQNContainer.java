package hisqisnoten;

public class HQNContainer {

    private String fach;
    private String semester;
    private String note;
    private String bestanden;

    public HQNContainer(String fach, String semester, String note, String bestanden) {
        this.fach = fach;
        this.semester = semester;
        this.note = note;
        this.bestanden = bestanden;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getBestanden() {
        return bestanden;
    }

    public void setBestanden(String bestanden) {
        this.bestanden = bestanden;
    }
}
