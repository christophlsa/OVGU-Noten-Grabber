package hisqisnoten;

public class HQNContainer {

    String fach;
    String semester;
    String note;

    public HQNContainer(String fach, String semester, String note) {
        this.fach = fach;
        this.semester = semester;
        this.note = note;
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
}
