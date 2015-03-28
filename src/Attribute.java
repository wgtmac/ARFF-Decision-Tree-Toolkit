import java.util.HashSet;


public class Attribute {
	public enum Type { REAL, CLASS }
	public String name;
	public Type type;
	public HashSet<String> valueSet;
	
	public Attribute (String name, String type) {
		this.name = name;
		if (type.equals("real")) {
			this.type = Type.REAL;
			valueSet = null;
		}
		else {
			this.type = Type.CLASS;
			valueSet = new HashSet<String>();
			for (String str : type.substring(1, type.length() - 1).split(",")) {
				valueSet.add(str);
			}
		}
	}
	
	public static void main (String[] args) {
		Attribute a  = new Attribute("Test", "{Student,Business,Other,Doctor,Professional}");
		for (String b : a.valueSet) {
			System.out.print(b + " ");
		}
	}
}
