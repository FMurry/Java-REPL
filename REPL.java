import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

/**
 * PEMDAS REPL
 * @author Frederic Murry
 *
 */
public class REPL {
	private String prefix;
	private Scanner input;
	private String userInput;
	private String[] nameBuffer;
	private String greeting;
	private ArrayList<Variable> varNames;
	private Variable referenced;
	
	/**
	 * Basic Constructor
	 */
	public REPL(){
		prefix = "FM Java>";
		input = new Scanner(System.in);
		userInput = "";
		nameBuffer = new String[100];
		greeting = "Welcome to Frederic Murry's Java REPL\nPlease add a space after each operateion"
				+ "\nEx: ( 12 + 4) / 4\nystem>Type 'exit' to quit repl"
				+ "\nSystem>Type info for more information";
		varNames = new ArrayList();
		referenced = null;
	}
	
	/**
	 * Basic class to hold variable
	 * @author fredericmurry
	 *
	 */
	class Variable{
		String name;
		String type;
		
		/**
		 * Basic Constructor
		 * @param name - name of the variable 
		 * @param type - type of variable
		 */
		public Variable(String name, String type){
			this.name = name;
			this.type = type;
		}
		
		/**
		 * Get the name of variable
		 * @return name of variable
		 */
		public String getName(){
			return name;
		}
		/**
		 * Get the Type of the variable
		 * @return type of variable
		 */
		public String getType(){
			return type;
		}
	}
	
	/**
	 * enumerator for basic PEMDAS
	 * @author fredericmurry
	 *
	 */
	private enum PEMDAS
	{
		ADD(1), SUBTRACT(2), MULTIPLICATION(3), DIVIDE(4), POWER(5), EQUALS(6)
		,COS(7),SIN(8);
		
		int token;
		
		/**
		 * Basic constructor
		 * @param token the token
		 */
		PEMDAS(int token){
			this.token = token;
		}
		
		/**
		 * Return the token
		 * @return the token
		 */
		int getToken(){
			return token;
		}
	}
	
	/**
	 * Map that defines the Operations
	 */
	private static Map<String, PEMDAS> ops = new HashMap<String, PEMDAS>() {{
        put("+",PEMDAS.ADD);
        put("-",PEMDAS.SUBTRACT);
        put("*",PEMDAS.MULTIPLICATION);
        put("/",PEMDAS.DIVIDE);
        put("^",PEMDAS.POWER);
        put("=",PEMDAS.EQUALS);
        put("cos",PEMDAS.COS);
        put("sin",PEMDAS.SIN);
    }};

    /**
     * Map to hold integer variables
     */
    private static Map<String, Integer> intVars = new HashMap<String,Integer>(){{

    }};

    /**
     * Map to hold String variables
     */
    private static Map<String, String> stringVars = new HashMap<String, String>(){{

    }};

    /**
     * Map to hold double variables
     */
    private static Map<String, Double> doubleVars = new HashMap<String, Double>(){{

    }};

   
	public static boolean hasPriority(String operator, String a){
		return (ops.containsKey(a) && ops.get(a).token >= ops.get(operator).token);
	}
	
	/**
	 * Formats input into postfix output
	 * @param infix input received 
	 * @return postfix output
	 */
	public static String postfix(String infix)
    {
        StringBuilder output = new StringBuilder();
        Deque<String> stack  = new LinkedList<>();

        for (String token : infix.split("\\s")) {
            if (ops.containsKey(token)) {
                while ( ! stack.isEmpty() && hasPriority(token, stack.peek()))
                    output.append(stack.pop()).append(' ');
                stack.push(token);

            
            } else if (token.equals("(")) {
                stack.push(token);

           
            } else if (token.equals(")")) {
                while ( ! stack.peek().equals("("))
                    output.append(stack.pop()).append(' ');
                stack.pop();

            } else {
                output.append(token).append(' ');
            }
        }

        while ( ! stack.isEmpty())
            output.append(stack.pop()).append(' ');

        return output.toString();
    }
	
	/**
	 * Determines whether a variable is in memory by name
	 * @param name name of varaible
	 * @param list list of names for variable already defined
	 * @return true or not
	 */
	public boolean listContains(String name, ArrayList<Variable> list){
		boolean contains = false;
		for(int i = 0; i < list.size();i++){
			if(name.equals(list.get(i).getName())){
				contains = true;
				referenced = list.get(i);
			}
		}
		return contains;
	}
	
	/**
	 * Run the Program
	 */
	public void run(){
		System.out.println(greeting);
		while(!userInput.equals("exit")){
			System.out.print(prefix);
			userInput = input.nextLine();
			replLogic(userInput);
		}
	}
	/**
	 * Print information
	 */
	public void printInformation(){
		System.out.println("Placeholder");
	}
	/**
	 * Logic for application
	 * @param input from user
	 */
	public void replLogic(String input) {
		
		try{
		if(input.equals("info")){
			printInformation();
		}
		else if(input.contains("int")){
			try{
				Scanner line = new Scanner(input);
				line.next();
				String name = line.next();
				String op = line.next();
				int number = Integer.parseInt(line.next());
				Variable var = new Variable(name,"int");
				intVars.put(name,number);
				varNames.add(var);
			}catch(Exception e){
				System.out.println("Formatting error: declare variable like 'int a = #'");
			}

		}
		else if(input.contains("double")){
			try{
				Scanner line = new Scanner(input);
				line.next();
				String name = line.next();
				String op = line.next();
				double number = Double.parseDouble(line.next());
				Variable var = new Variable(name,"double");
				doubleVars.put(name,number);
				varNames.add(var);
			}catch(Exception e){
				System.out.println("Formatting error: declare variable like 'double a = #'");
			}

		}
		else if(input.contains("string")){
			try{
				Scanner line = new Scanner(input);
				line.next();
				String name = line.next();
				String op = line.next();
				String str = line.next();
				stringVars.put(name,str);
				Variable var = new Variable(name,"string");
				varNames.add(var);
			}catch(Exception e){
				System.out.println("Formatting error: declare variable like 'int a = #'");
			}

		}
		else if(input.contains("sin")){
			if(input.contains("sin(")){
				int rParen = input.indexOf(')');
				try{
					double radians = Double.parseDouble(input.substring(4,rParen));

				}
				catch(Exception e){
					System.out.println("Format Error: sin function needs to be sin(#)");
				}
			}
			else{
				System.out.println("Format Error: sin function needs to be sin(#)");
			}
		}
		else if(input.contains("cos")){
			if(input.contains("cos(")){
				int rParen = input.indexOf(')');
				try{
					double radians = Double.parseDouble(input.substring(4,rParen));
					
				}
				catch(Exception e){
					System.out.println("Format Error: cps function needs to be cos(#)");
				}
			}
			else{
				System.out.println("Format Error: cos function needs to be cos(#)");
			}
		}
		else if(listContains(input,varNames)){
			try{
				String type = referenced.getType();
				if(type.equals("int")){
					int var = intVars.get(input);
					System.out.println(var);
				}
				else if(type.equals("double")){
					double var = doubleVars.get(input);
					System.out.println(var);
				}
				else if(type.equals("string")){
					String var = stringVars.get(input);
					System.out.println(var);
				}
				else{
					System.out.println("No variable referenced by name of "+input);
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		else{
			String postfix = postfix(input);
			Stack<Double> stack = new Stack<>();
			Scanner scan = new Scanner(postfix);
			boolean useStack = false;
			while(scan.hasNext()){
				String s = scan.next();
				useStack = true;
				switch(s){
					case "+":
						stack.push(stack.pop() + stack.pop());
						break;
					case "-":
						double a = stack.pop();
						double b = stack.pop();
						stack.push(b - a);
						break;
					case "*":
						stack.push(stack.pop() * stack.pop());
						break;
					case "/":
						double c = stack.pop();
						double d = stack.pop();
						if(c!= 0){
							stack.push(d / c);
						}
							else{
								System.out.print("Error: Divide by zero");
								stack.push(null);
							}
							break;
					case "^":
						double e = stack.pop();
						double f = stack.pop();
						System.out.println("E: "+e+" F:"+f);
						double power = 1;
						for(int i = 0; i<e;i++){
							power*=f;

						} 
						stack.push(power);
						break;
					case "exit":
							quit();
							stack.pop();
							break;
					default:
							stack.push(Double.parseDouble(s));
							break;
					}
				
				
			}
			if(useStack){
				System.out.println("\n"+stack.pop());
				scan.close();
			}
		}
	}
	catch(Exception e){
		System.out.println("ERROR: Check your formatting on last input check README for more details on formatting");
		System.out.println("Java Error:" + e.getMessage());
	}
	}
	
	public static void main(String[] args){
		REPL repl = new REPL();
		System.out.println("Initializing...");
		repl.run();
		repl.quit();
		
	}
	
	/**
	 * Quit the application
	 */
	public void quit(){
		System.out.println("Exiting.....");
		input.close();
		System.exit(0);
	}
	
}
