import java.util.HashSet;

/**
 * Class to hold attribute data
 * */
public class Attribute {
    public enum Type { REAL, CLASS }
    
    private String name;            // Attribute name
    private Type type;                // real number or several classes
    private HashSet<String> valueSet;    // for classes, set of possible values
    
    public String getName() { return name; }
    public Type getType() {    return type;    }
    public HashSet<String> getValueSet() {return valueSet;}

    public Attribute (String name, String type) {
        this.name = name;
        if (type.equals("real")) {
            this.type = Type.REAL;
            valueSet = null;
        }
        else {
            this.type = Type.CLASS;
            valueSet = new HashSet<String>();
            for (String s : type.substring(1, type.length() - 1).split(",")) {
                valueSet.add(s);
            }
        }
    }
    
    public boolean isRealNum () {
    	return this.type == Type.REAL;
    }
    
    @Override
    public String toString() {
        return "Attribute [name=" + name + ", type=" + type + ", valueSet=" + valueSet + "]";
    }
    
    public static void main (String[] args) {
        Attribute a  = new Attribute("Test",
                "{Student,Business,Other,Doctor,Professional}");
        for (String b : a.valueSet) {
            System.out.print(b + " ");
        }
    }
}
