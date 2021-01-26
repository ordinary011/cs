package com.shpp.p2p.cs.ldebryniuk.assignment11;

import java.util.Stack;

/**
 * The following class is comprises all the calculation logic
 */
public class Kernel {

    // contains numbers as Strings. Can contain "-33.55555"
    private final Stack<String> nums = new Stack<>();
    // contains operators like ^ * / + - (
    private final Stack<String> operators = new Stack<>();

    private final VarHandler vh = VarHandler.getInstance();


    /**
     * The following method calculates everything that is left in the stack.
     *
     * @return result of the whole formula
     */
    public String calcRestOfStack() {
        String n2 = nums.pop();
        while (!operators.empty()) {
            String n1 = nums.pop();
            String operation = operators.pop();

            n2 = calcOperation(n1, n2, operation);
        }

        return n2;
    }

    /**
     * The following method parses the whole formula and performs calculations
     */
    public void parseFormula(String formula) throws Exception {
        int numInd = ifParenthesesAtStart(formula.charAt(0));

        for (int i = 1; i < formula.length(); i++) {
            char ch = formula.charAt(i);
            if (isOperation(ch)) {
                if (isOperation(formula.charAt(i - 1))) continue; // e.g. 10*-3. Here "i" points to "-" after "*"
                numInd = whenChIsOp(formula, numInd, i);
            } else if (ch == '(') {
                if (i > 2 && isFunc(formula.substring(numInd, i))) { // parentheses after function e.g. sin(3+3)
                    int chNumTillClosing = parseFunction(formula, numInd, i);
                    i += chNumTillClosing;
                    numInd = i + 1;
                } else { // regular parentheses without a function e.g. (3+3)
                    operators.push("(");
                    numInd = i + 1; // i points to the "(". next num will have an index of i + 1;
                    i++; // skip ch after "(" because numInd will point to that number or to that sign anyway
                }
            } else if (ch == ')') {
                numInd = calcTillOpeningParentheses(formula, numInd, i);
            }
        }

        // push the last num to the stack
        pushToNumsStack(formula, numInd, formula.length());
    }

    /**
     * The following method calculates everything that is within the brackets of the functions and then calculates func
     * e.g. cos(3+3) -> cos(6) -> 0.000000
     *
     * @return number of characters from the opening to the closing of parentheses
     */
    private int parseFunction(String formula, int numInd, int i) throws Exception {
        int chNumTillClosing = findCharNumTillClosing(formula.substring(i));

        String withinParantheses = formula.substring(i, (i + chNumTillClosing + 1));
        parseFormula(withinParantheses);
        // after parseFormula at the top of the stack we will find the result of calculations within brackets
        String res = nums.pop();
        res = calcFunc(formula.substring(numInd, i), res); // e.g. cos(3+3). 6 is at top of the stack
        nums.push(res);

        return chNumTillClosing;
    }

    /**
     * The following method will perform calculate the stack until "(" occurs
     */
    private int calcTillOpeningParentheses(String formula, int numInd, int i) throws Exception {
        pushToNumsStack(formula, numInd, i); // push last num before closing parentheses

        String operation = operators.pop();
        while (!operation.equals("(")) {
            operation = doStackIteration(operation);
        }

        return i + 1; // i points to the ")". next num will have an index of i + 1;
    }

    /**
     * The following method will extract 2 nums from numbers stack then it will perform the operation on them
     * and will pop and return the next operation from operation stack
     */
    private String doStackIteration(String operation) {
        String n2 = nums.pop();
        String n1 = nums.pop();

        String res = calcOperation(n1, n2, operation);
        nums.push(res);

        return operators.pop();
    }

    /**
     * The following method comprises a logic of actions to do when the current char is an operator
     */
    private int whenChIsOp(String formula, int numInd, int i) throws Exception {
        pushToNumsStack(formula, numInd, i);

        String operationFromFormula = formula.substring(i, i + 1);
        if (!operators.empty()) {
            int formulaOperationPriority = getPriority(operationFromFormula);
            String topOfStackOperation = operators.peek();
            int topOfStackOperationPriority = getPriority(topOfStackOperation);

            while (formulaOperationPriority <= topOfStackOperationPriority) {
                doStackIteration(topOfStackOperation);

                if (!operators.empty()) {
                    topOfStackOperation = operators.peek();
                    topOfStackOperationPriority = getPriority(topOfStackOperation);
                } else break; // if stack is empty we break
            }
        }
        operators.push(operationFromFormula);

        numInd = i + 1; // i points to the сurrent operator e.g. "*". next num will have an index of i + 1;
        return numInd;
    }

    /**
     * The following method checks if the first char is an opening parentheses
     *
     * @return index of the first number in the formula (or index of "-" if first num is negative)
     */
    private int ifParenthesesAtStart(char firstChInFormula) {
        if (firstChInFormula == '(') {
            operators.push("(");
            return 1; // numInd points to the "(". num after "(" will have an index of 1;
        }

        return 0; // if no parentheses at the beginning numInd is to be set to 0
    }

    /**
     * The following method will push a number before operator or before parentheses to the stack.
     * If there was no number but a variable it will search for it and will push to the stack it's value
     */
    private void pushToNumsStack(String formula, int numInd, int endIndOfNum) throws Exception {
        if (numInd < endIndOfNum) { // if numInd == endIndOfNum -> numInd points to something else e.g. "*"
            String num = formula.substring(numInd, endIndOfNum); // num can be: 6 || -6 || a || -a

            if (isVar(num)) {
                String varValue = vh.findVar(num);

                if (varValue != null) {
                    // cases to consider 5-a || 5^-a || -a+5
                    if (varValue.charAt(0) == '-' && // if args var is negative e.g. a=-2
                            num.charAt(0) == '-' // if var in formula is negative
                    ) num = varValue.substring(1); // var -2 -> 2

                    else if (num.charAt(0) == '-')  // if var in formula is negative 10/-a
                        num = "-" + varValue;
                    else  // sign of var is already correct
                        num = varValue;

                } else {
                    System.out.println("could not find the variable with the following name: " + num);
                    throw new Exception();
                }
            }

            nums.push(num);
        }
    }



    /**
     * The following method determines if str is a function
     */
    private boolean isFunc(String str) {
        return str.matches("sin|cos|tan|atan|log10|log2|sqrt");
    }

    /**
     * The following method determines if ch is an operation
     */
    private boolean isOperation(char ch) {
        return String.valueOf(ch).matches("[\\^*/+-]");
    }

    /**
     * The following method determines if str is a variable
     *
     * @param str can be -a || a || aba
     */
    private boolean isVar(String str) {
        return str.matches(".?[a-zA-Z]+");
    }

    /**
     * The following method determines priorities of the operations
     */
    private int getPriority(String operation) {
        switch (operation) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                System.out.println("unknown operation please check your formula");
                return -1;
        }
    }

    /**
     * The following method performs operation
     */
    private String calcOperation(String firstN, String secondN, String operation) {
        double res = -1;

        double firstNum = Double.parseDouble(firstN);
        double secondNum = Double.parseDouble(secondN);

        switch (operation) {
            case "+":
                res = firstNum + secondNum;
                break;
            case "-":
                res = firstNum - secondNum;
                break;
            case "*":
                res = firstNum * secondNum;
                break;
            case "/":
                res = firstNum / secondNum;
                break;
            case "^":
                res = Math.pow(firstNum, secondNum);
                break;
            default:
                System.out.println("unknown operation check your formula");
        }

        return String.valueOf(res);
    }

    /**
     * The following method calculates functions like "sin", "cos", "tan", "atan", "log10", "log2", "sqrt"
     */
    private String calcFunc(String func, String value) {
        double val = Double.parseDouble(value);

        switch (func) {
            case "sin":
                return String.valueOf(Math.sin(val));
            case "cos":
                return String.valueOf(Math.cos(val));
            case "tan":
                return String.valueOf(Math.tan(val));
            case "atan":
                return String.valueOf(Math.atan(val));
            case "sqrt":
                return String.valueOf(Math.sqrt(val));
            case "log2":
                return String.valueOf(Math.log(val));
            case "log10":
                return String.valueOf(Math.log10(val));
            default:
                System.out.println("unknown function please check your formula");
                return value;
        }
    }

    /**
     * The following method searches for the amount of characters from opening to the closing parentheses
     *
     * @return amount of characters from opening to the closing parentheses
     */
    private int findCharNumTillClosing(String parentheses) { // cos(2+3+cos(2+3)*2) -> (2+3+cos(2+3)*2)
        int needToClose = 0;

        for (int i = 0; i < parentheses.length(); i++) {
            char ch = parentheses.charAt(i);
            if (ch == '(') needToClose++;
            else if (ch == ')') needToClose--;

            if (needToClose == 0) return i; // amount of characters from opening to the closing parentheses
        }

        // if no index was found
        System.out.println("There is a mistake in the formula. You forgot to close your parentheses");
        System.exit(0);

        return -1;
    }
}
