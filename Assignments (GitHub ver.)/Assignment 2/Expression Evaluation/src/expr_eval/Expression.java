package expr_eval;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structure.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Populates the scalars list with simple scalar variables
	 * Scalar characters in the expression 
	 */
	ArrayList<ScalarVariable> scalars;   
	
	/**
	 * Populates the arrays list with simple array variables
	 * Array characters in the expression
	 */
	ArrayList<ArrayVariable> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }

    /**
     * Populates the scalars and arrays arraylists with characters for scalar and array
     * variables in the expression. For every variable, a SINGLE variable is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     */
    public void buildVariable() {
    		/** COMPLETE THIS METHOD **/
    		/** DO NOT create new scalars and arrays **/
    	scalars = new ArrayList<ScalarVariable>();
    	arrays = new ArrayList<ArrayVariable>();
    	
    	String myDelims = " \t*+-/()[]0123456789";
    	
    	StringTokenizer tokens = new StringTokenizer(expr, myDelims);
    	int numOfTokens = tokens.countTokens();
    	
    	for(int i = 0; i < numOfTokens; i++) {
    		
    		String token = tokens.nextToken();
    		int indexAfterLastLetterOfToken = expr.indexOf(token) + token.length();
    		char characterAfterLastLetterOfToken;
    		
    		if(indexAfterLastLetterOfToken >= expr.length()) {
    			scalars.add( new ScalarVariable(token) );
    		}else {
    			
    			characterAfterLastLetterOfToken = expr.charAt(indexAfterLastLetterOfToken);
    			
    			if(indexAfterLastLetterOfToken-2 < 0) {
    				
    				while(Character.isLetter(characterAfterLastLetterOfToken) && expr.substring(indexAfterLastLetterOfToken-1, indexAfterLastLetterOfToken).equals(token)) {
    					indexAfterLastLetterOfToken = expr.indexOf(token, indexAfterLastLetterOfToken);
        				characterAfterLastLetterOfToken = expr.charAt(indexAfterLastLetterOfToken);
        				
        				if(indexAfterLastLetterOfToken >= expr.length()) { 
        	    			scalars.add( new ScalarVariable(token) );
        	    			break;
        				}
    				}
    			}else {
	    			while((Character.isLetter(characterAfterLastLetterOfToken) || Character.isLetter(expr.charAt(indexAfterLastLetterOfToken-2))) && expr.substring(indexAfterLastLetterOfToken-1, indexAfterLastLetterOfToken).equals(token)) {
	    				indexAfterLastLetterOfToken = expr.indexOf(token, indexAfterLastLetterOfToken);
	    				characterAfterLastLetterOfToken = expr.charAt(indexAfterLastLetterOfToken);
	    				
	    				if(indexAfterLastLetterOfToken >= expr.length()) { 
	    	    			scalars.add( new ScalarVariable(token) );
	    	    			break;
	    				}
	    			}
    			}
    			//System.out.println(characterAfterLastLetterOfToken);
    			
    			if(characterAfterLastLetterOfToken == '[') {
    				arrays.add(new ArrayVariable(token));
    			}else scalars.add(new ScalarVariable(token));   		
    		}
    	}
    	
    	for(int i=0; i < scalars.size(); i++) {
    		for(int j=i+1; j < scalars.size(); j++) {
    			if(scalars.get(i).name.equals(scalars.get(j).name)) {
    				scalars.remove(j);
    			}
    				
    		}
    	}
    	for(int i=0; i < scalars.size(); i++) {
    		for(int j=i+1; j < scalars.size(); j++) {
    			if(scalars.get(i).name.equals(scalars.get(j).name)) {
    				scalars.remove(j);
    			}
    				
    		}
    	}
    	for(int i=0; i < arrays.size(); i++) {
    		for(int j=i+1; j < arrays.size(); j++) {
    			if(arrays.get(i).name.equals(arrays.get(j).name)) {
    				arrays.remove(j);
    			}
    		}
    	}
    	for(int i=0; i < arrays.size(); i++) {
    		for(int j=i+1; j < arrays.size(); j++) {
    			if(arrays.get(i).name.equals(arrays.get(j).name)) {
    				arrays.remove(j);
    			}
    		}
    	}
    	
    	//System.out.println("Scalars: \n"); printScalars();
    	//System.out.println("Arrays: \n"); printArrays();
    	
    }
    
    
    /**
     * Loads values for scalars and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadVariableValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String varl = st.nextToken();
            ScalarVariable scal = new ScalarVariable(varl);
            ArrayVariable arr = new ArrayVariable(varl);
            int scali = scalars.indexOf(scal);
            int arri = arrays.indexOf(arr);
            if (scali == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar character
                scalars.get(scali).value = num;
            } else { // array character
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,value) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    
    /**
     * Evaluates the expression, and can use RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @param scalars The scalar array list, with values for all scalar items
     * @param arrays The array array list, with values for all array items
     * 
     * @return Result of evaluation
     */

    public double evaluate() {
    		/** COMPLETE THIS METHOD **/
    		// following line just a placeholder for compilation
    		
    		return evaluateHelper(expr);
    }
    private double evaluateHelper(String str) {
    	
    	if(isNumber(str))
    		return Double.parseDouble(str);
    	if(isScalarVariable(str)) 
    		return scalars.get(0).value;
    	if(isSingleArrayVariable(str)) {
    		if(scalars.size()==1)
    			return arrays.get(0).values[scalars.get(0).value];
    		else return arrays.get(0).values[ Integer.parseInt(str.substring(str.indexOf('[')+1,str.indexOf(']'))) ];
    	}
    	
    	int pointer = 0;
    	String operand = "";
    	int openBracketCounter = 0;
    	int closeBracketCounter = 0;
    	int openParenCounter = 0;
    	int closeParenCounter = 0;
    	Stack<String> operators = new Stack<String>();
    	Stack<String> operands = new Stack<String>();
    	while(pointer<str.length()) {
    		
    		if(isOperator(str.charAt(pointer))) {
    			operators.push(Character.toString(str.charAt(pointer)));

    			operands.push(operand);
    			operand = "";
    		}
    			
    		else{
    			operand+=str.charAt(pointer);
    			
    			if(str.charAt(pointer)=='(') {
    				openParenCounter++;
    				
    				while(openParenCounter!=closeParenCounter) {
    					pointer++;
        				operand += str.charAt(pointer);
            			if(str.charAt(pointer)=='(') 
                			openParenCounter++;
                		
            			if(str.charAt(pointer)==')') 
            				closeParenCounter++;
            			
            			if(pointer>=str.length()-1)
            				break;
        			}
    			}
    			
    			else if(str.charAt(pointer)=='[') {
        			openBracketCounter++;
        			
        			while(openBracketCounter!=closeBracketCounter) {
        				pointer++;
            			operand += str.charAt(pointer);
            			if(str.charAt(pointer)=='[') 
                			openBracketCounter++;
                		
            			if(str.charAt(pointer)==']') 
            				closeBracketCounter++;
            			
            			if(pointer>=str.length()-1) {
            				break;
            			}
            		}
    			}
    		}
    		
    		evaluateStacks(operands, operators);
    		
    		if(pointer == str.length()-1) {
    			operands.push(operand);    			
    			evaluateStacks(operands, operators);
    			break;
    		}pointer++;
    		
    	}
    	//printStack(operators);
    	//printStack(operands);
    	operands.push(Double.toString(evaluateArrayOperand(operands.pop())));
    	while(!operators.isEmpty()) {
    		evaluateStacks(operands, operators);
    	}
    	System.out.println(operands.pop());
    	return 0;
    	/*if(operands.size()==1)
    		
    	System.out.println(operands.size());
    	*/
    	//return 0;
    }
    private void evaluateStacks(Stack<String> operands, Stack<String> operators) {
    	if(!operators.isEmpty()) {
			operands.push(removeOuterParenthesis(operands.pop()));
			if(operands.peek().indexOf('[')<operands.peek().indexOf('(')) {
				operands.push(Double.toString(evaluateArrayOperand(operands.pop())));
			}
			if(operators.peek().equals("*") && operands.size()>operators.size()) {
				double secondValue = evaluateHelper(operands.pop());
				double firstValue = evaluateHelper(operands.pop());
				String result = Double.toString(firstValue * secondValue);
				operands.push(result);
				operators.pop();
			}
			else if(operators.peek().equals("/") && operands.size()>operators.size()) {
				double secondValue = evaluateHelper(operands.pop());
				double firstValue = evaluateHelper(operands.pop());
				String result = Double.toString(firstValue / secondValue);
				operands.push(result);
				operators.pop();
			}else if(operators.peek().equals("+") && operands.size()>operators.size()) {
				String result = Double.toString(evaluateHelper(operands.pop()) + evaluateHelper(operands.pop()));
				operands.push(result);
				operators.pop();
			}else if(operators.peek().equals("-") && operands.size()>operators.size()) {
				double secondValue = evaluateHelper(operands.pop());
				double firstValue = evaluateHelper(operands.pop());
				String result = Double.toString(firstValue - secondValue);
				operands.push(result);
				operators.pop();
			}
		}
    }
    private String removeOuterParenthesis(String str) {
    	String output = str;
    	if(str.charAt(0)=='(') {
    		output = str.substring(1, str.length()-1);
    	}
    	return output;
    }
    private double evaluateArrayOperand(String str) {
    
    	double output = searchAndReturnArray(str.substring(0, str.indexOf('['))).values[(int) evaluateHelper(str.substring(str.indexOf('['), str.length()-1))];
    	return output;
    }
    
    private void printStack(Stack<String> stack) {
    	while(stack.isEmpty()==false) {
    		System.out.println(stack.pop());
    	}
    	System.out.println();
    }
    
    
    private boolean isNumber(String s) {
    	boolean output = false;
  
    	for(int i=0; i<s.length(); i++) {
    		if( (s.charAt(i)>='0' && s.charAt(i)<='9') )
    			output = true;
    		else {
    			output = false;
    			break;
    		}
    	}
    	return output;
    }
    
    private boolean isScalarVariable(String s) {
    	boolean output = false;
    	
    	for(int i=0; i<s.length(); i++) {
    		if( (s.charAt(i)>='a' && s.charAt(i)<='z') || (s.charAt(i)>='A' && s.charAt(i)<='Z'))
    			output = true;
    		else {
    			output = false;
    			break;
    		}
    	}
    	
    	return output;
    }
    
    private boolean isSingleArrayVariable(String s) {
    
    	if(arrays.size()==1 && (isNumber(expr.substring( expr.indexOf('[')+1, expr.indexOf(']') )) || isScalarVariable(expr.substring( expr.indexOf('[')+1, expr.indexOf(']') ))))
    		return true;
    	else return false;
    }
    
    private double searchAndReturnScalar(String s) {
    	
    	for(int i=0; i < scalars.size(); i++) {
    		if(s.equals(scalars.get(i).name))
    			return scalars.get(0).value;
    	}
    	return -1;
    }
    private ArrayVariable searchAndReturnArray(String s) {
    	for(int i=0; i < arrays.size(); i++) {
    		if(s.equals(arrays.get(i).name))
    			return arrays.get(i);
    	}return null;
    }
    
    private boolean isOperator(char c) {
    	return (c == '+' || c == '-' || c == '*' || c == '/');
    }
    
    private String removeSpaces(String str) {
    	StringTokenizer st = new StringTokenizer(" \t+");
    	int numTokens = st.countTokens();
    	String output = "";
    	
    	for(int i=0; i < numTokens; i++) {
    		output += st.nextToken();
    	}
    	return output;
    }

    /**
     * Utility method, prints the characters in the scalars list
     */
    public void printScalars() {
        for (ScalarVariable ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the characters in the arrays list
     */
    public void printArrays() {
    		for (ArrayVariable as: arrays) {
    			System.out.println(as);
    		}
    }

}

