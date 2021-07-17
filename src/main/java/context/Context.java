package context;

import java.util.HashMap;
import java.util.Map;

import runtime.Instruction;
import runtime.function.FunctionEntry;
import runtime.function.ParameterList;
import runtime.main.InstructionList;

/**
 * Kontext-Klasse, speichert die kontextspezifischen Daten
 *
 * @version 1.0.0
 * @author Thierry Girod
 */
public class Context {

    private final Context parent;

    private final int level;

    private final Map<String, Instruction> variableMap = new HashMap<>();
    private final Map<String, Map<Integer, FunctionEntry>> functionMap = new HashMap<>();

    /**
     * Root-Kontext.
     */
    public Context() {
        
        this.parent = null;
        this.level = 0;
    }

    /**
     * Weitere Kontexte (z.B. Loops, Funktionen, Ternaries).
     *
     * @param parent
     *            Eltern-Kontext
     */
    public Context(final Context parent) {
        this.parent = parent;
        this.level = parent.level + 1;
    }

    /**
     * Existiert die Variable bereits, wird sie überschrieben (im jeweiligen
     * Kontext). Existiert die Variable noch nicht, wird sie erstellt im aktuellen
     * Kontext.
     *
     * @param identifier
     *            Name der Variable.
     * @param instruction
     *            Wert der Variable.
     */
    public void defineVariable(final String identifier, final Instruction instruction) {
        if (parent != null && !isVariableDefinedLocal(identifier) && parent.isVariableDefined(identifier)) {
            parent.defineVariable(identifier, instruction);
        } else {
            variableMap.put(identifier, instruction);
        }
    }

    /**
     * Erhalte den Wert einer Variable zurück. Falls vorhanden, entnehme den
     * Variablen-Wert aus dem lokalen Kontext, ansonsten aus dem Eltern-Kontext.
     *
     * @param identifier
     *            Name der Variable.
     * @return Variablen-Wert
     * @throws IllegalArgumentException
     *             Wenn die Variable noch nicht definiert wurde.
     */
    public Instruction getVariable(final String identifier) {
        if (!isVariableDefined(identifier)) {
            throw new IllegalArgumentException("Variable " + identifier + " is not defined!");
        }
        return isVariableDefinedLocal(identifier) ? variableMap.get(identifier) : parent.getVariable(identifier);
    }

    /**
     * Üerprüft, ob eine Variable im lokalen oder Eltern-Kontext bereits definiert
     * wurde.
     *
     * @param identifier
     *            Name der Variable.
     */
    private boolean isVariableDefined(final String identifier) {
        if (!isVariableDefinedLocal(identifier)) {
            if (parent == null) {
                return false;
            } else {
                return parent.isVariableDefined(identifier);
            }
        }
        return true;
    }

    /**
     * Üerprüft, ob eine Variable im lokalen Kontext bereits definiert wurde.
     *
     * @param identifier
     *            Name der Variable.
     */
    private boolean isVariableDefinedLocal(final String identifier) {
        return variableMap.containsKey(identifier);
    }

    /**
     * Existiert die Funktion bereits mit der exakt selben Anzahl an Parametern,
     * wird sie im jeweiligen Kontext überschrieben. Existiert die Funktion
     * bereits, jedoch nicht mit der selben Anzahl an Parametern, wird sie erstellt
     * im aktuellen Kontext. Existiert die Funktion noch nicht, wird sie erstellt im
     * aktuellen Kontext.
     *
     * @param identifier
     *            Name der Funktion.
     * @param parameters
     *            Liste der Parameternamen.
     * @param block
     *            Funktionskörper.
     */
    public void defineFunction(final String identifier, final ParameterList parameters, final InstructionList block) {
        if (parent != null && !isFunctionDefinedLocal(identifier, parameters.size())
                && parent.isFunctionDefined(identifier, parameters.size())) {
            parent.defineFunction(identifier, parameters, block);
        } else {
            functionMap.computeIfAbsent(identifier, s -> new HashMap<>());

            functionMap.get(identifier).put(parameters.size(), new FunctionEntry(parameters.getParameters(), block));
        }
    }

    /**
     * Üerprüft, ob eine Funktion mit der exakt selben Anzahl an Parametern im
     * lokalen oder Eltern-Kontext bereits definiert wurde.
     *
     * @param identifier
     *            Name der Funktion.
     * @param parameterCount
     *            Anzahl an Parametern.
     */
    private boolean isFunctionDefined(final String identifier, final int parameterCount) {
        if (!isFunctionDefinedLocal(identifier, parameterCount)) {
            if (parent == null) {
                return false;
            } else {
                return parent.isFunctionDefined(identifier, parameterCount);
            }
        }
        return true;
    }

    /**
     * Üerprüft, ob eine Funktion mit der exakt selben Anzanl an Parametern im
     * lokalen Kontext bereits definiert wurde.
     *
     * @param identifier
     *            Name der Funktion.
     * @param parameterCount
     *            Anzahl an Parametern.
     */
    private boolean isFunctionDefinedLocal(final String identifier, final int parameterCount) {
        return functionMap.containsKey(identifier) && functionMap.get(identifier).containsKey(parameterCount);
    }

    /**
     * Erhalte den Funktionskörper und die Paramter zurück. Falls vorhanden,
     * entnehme aus dem lokalen Kontext, ansonsten aus dem Eltern-Kontext.
     *
     * @param identifier
     *            Name der Funktion.
     * @param parameterCount
     *            Anzahl an Parametern.
     * @return Funktionskörper und -parameter.
     * @throws IllegalArgumentException
     *             Wenn die Funktion mit der Anzanl an Parametern noch nicht
     *             definiert wurde.
     */
    public FunctionEntry getFunctionEntry(final String identifier, final int parameterCount) {
        if (!isFunctionDefined(identifier, parameterCount)) {
            throw new IllegalArgumentException(
                    "Function " + identifier + " with " + parameterCount + " parameters is not defined!");
        }
        if (isFunctionDefinedLocal(identifier, parameterCount)) {
            // Running function with arguments
            return functionMap.get(identifier).get(parameterCount);
        }
        return parent.getFunctionEntry(identifier, parameterCount);
    }
}
