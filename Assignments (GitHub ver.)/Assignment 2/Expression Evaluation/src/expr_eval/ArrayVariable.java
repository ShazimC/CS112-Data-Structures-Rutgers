package expr_eval;

/**
 * This class holds a (name, array of integer values) pair for an array variable. 
 * The name is a sequence of one or more letters. 
 * 
 * @author ru-nb-cs112
 *
 */
public class ArrayVariable {
	
	/**
	 * Name, sequence of letters
	 */
	public String name;
	
	/**
	 * Array of integer values
	 */
	public int[] values;
	
	/**
	 * Initializes this array with given name, and sets values to null.
	 * 
	 * @param name Array Variable name
	 */
	public ArrayVariable(String name) {
        this.name = name;
        values = null;
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (values == null || values.length == 0) {
			return name + "=[ ]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("=[");
		sb.append(values[0]);
		for (int i=1; i < values.length; i++) {
			sb.append(',');
			sb.append(values[i]);
		}
		sb.append(']');
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ArrayVariable)) {
			return false;
		}
		ArrayVariable as = (ArrayVariable)o;
		return name.equals(as.name);
	}
}
