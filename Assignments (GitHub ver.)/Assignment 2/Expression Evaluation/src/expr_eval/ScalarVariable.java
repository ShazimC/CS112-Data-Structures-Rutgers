package expr_eval;

/**
 * This class holds a (name, integer value) pair for a scalar (non-array) variable. 
 * The variable name is a sequence of one or more letters.
 * 
 * @author ru-nb-cs112
 *
 */
public class ScalarVariable {
	
	/**
	 * Name, sequence of letters
	 */
	public String name;
	
	/**
	 * Integer value
	 */
	public int value;

	/**
	 * Initializes this scalar with given name, and zero value
	 * 
	 * @param name Scalar Variable name
	 */
	public ScalarVariable(String name) {
		this.name = name; 
		value = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + "=" + value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ScalarVariable)) {
			return false;
		}
		ScalarVariable ss = (ScalarVariable)o;
		return name.equals(ss.name);
	}
}


