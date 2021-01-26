package com.shpp.p2p.cs.ldebryniuk.assignment11;

import java.util.HashMap;

/**
 * The following class is comprises all the logic in regard to the variable handling
 */
public class VarHandler {

    // contains variables <a, -555.333>
    private final HashMap<String, String> vars = new HashMap<>();

    public static VarHandler instance = null;

    private VarHandler() {}

    public static VarHandler getInstance() {
        if (instance == null) {
            instance = new VarHandler();
        }

        return instance;
    }

    /**
     * The following method adds variables to the hash map
     */
    public void parseVars(String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                String var = args[i]; // example of var -> "a=5"
                int equalInd = var.indexOf('=');
                String varName = var.substring(0, equalInd);
                String varValue = var.substring(equalInd + 1);

                vars.put(varName, varValue);
            }
        }
    }

    /**
     * The following method will search the variable the specified name
     *
     * @param varName can be: a || -a || fsdfsdf
     */
    public String findVar(String varName) {
        if (varName.charAt(0) == '-') {
            varName = varName.substring(1);
        }

        return vars.get(varName);
    }

}
