package com.shpp.p2p.cs.ldebryniuk.calc10_try_to_refactor;

import java.util.HashMap;

/**
 * The following class is comprises all the logic in regard to the variable handling
 */
class VariablesHandler {

    private final HashMap<String, String> variableToVariableValue = new HashMap<>();

    /**
     * The following method adds variables to the hash map
     */
    void parseVars(String[] args) {
        if (args.length > 1) { // if there is at least one variable
            for (int i = 1; i < args.length; i++) {
                // example of variable -> "a=5"
                String variable = args[i];

                int equalSignIndex = variable.indexOf('=');
                String varName = variable.substring(0, equalSignIndex);
                String varValue = variable.substring(equalSignIndex + 1);

                variableToVariableValue.put(varName, varValue);
            }
        }
    }

    /**
     * The following method will search the variable the specified name
     *
     * @param varName can be: a || -a || fsdfsdf
     */
    String getVariable(String varName) {
        return variableToVariableValue.get(varName);
    }

}
