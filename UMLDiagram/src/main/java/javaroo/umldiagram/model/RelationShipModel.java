package javaroo.umldiagram.model;

public class RelationShipModel {

    public String from;
    public String to;
    public String relationType;

    @Override
    public String toString() {
        return "RelationShipModel{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", relationType='" + relationType + '\'' +
                '}';
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public RelationShipModel(String from, String to, String relationType) {
        this.from = from;
        this.to = to;
        this.relationType = relationType;
    }

    public RelationShipModel() {
    }
}
