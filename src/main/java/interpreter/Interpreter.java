package interpreter;

import java.util.Objects;

import context.Context;
import runtime.Instruction;
import runtime.Program;
import runtime.arithmetic.ArithmeticInstruction;
import runtime.conditional.BooleanInstruction;
import runtime.conditional.BooleanWrapper;
import runtime.conditional.IfThenElseInstruction;
import runtime.function.ArgumentList;
import runtime.function.FunctionCallInstruction;
import runtime.function.FunctionDefinitionInstruction;
import runtime.function.ParameterList;
import runtime.function.PrintInstruction;
import runtime.function.ReturnInstruction;
import runtime.loop.LoopInstruction;
import runtime.main.AssignInstruction;
import runtime.main.GetVariableInstruction;
import runtime.main.InstructionList;
import runtime.wrapper.NumberWrapper;
import runtime.wrapper.StringWrapper;
import runtime.wrapper.UndefinedWrapper;

/**
 * Interpreter nach Visitor-Pattern umgesetzt.
 *
 * @version 1.0.0
 * @author Thierry Girod
 */
public class Interpreter implements Visitor {

    /**
     * Besucht das Root-Element "Script".
     *
     * @return evaluierte {@link InstructionList}
     */
    @Override
    public Instruction visit(final Program script, final Context context) {
        return script.getBlock().accept(this, context);
    }

    /**
     * Besucht und evaluiert alle Instruktionen, wobei eine
     * {@link ReturnInstruction} die Evaluation abbricht und den Rückgabewert zurück
     * gibt. Befindet sich keine {@link ReturnInstruction} in der Liste wird eine
     * leere Instruction ( {@link UndefinedWrapper} ) zurückgegeben.
     *
     * @return evaluierte {@link ReturnInstruction} oder {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final InstructionList block, final Context context) {
        for (Instruction i : block.getInstructions()) {
            if (i instanceof ReturnInstruction) {
                return i.accept(this, context);
            }
            i.accept(this, context);
        }
        return new UndefinedWrapper();
    }

    /**
     * Definiert (oder überschreibt) eine Variable.
     *
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final AssignInstruction assignInstruction, final Context context) {
        context.defineVariable(assignInstruction.getIdentifier(),
                assignInstruction.getExpression().accept(this, context));
        return new UndefinedWrapper();
    }

    /**
     * Führt die Instruktionen mehrmals auf, wobei vor jedem Durchlauf die Bedingung
     * geprüft wird.
     *
     * Jeder Durchlauf erhält einen separaten Kontext.
     *
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final LoopInstruction loopInstruction, final Context context) {
        while (evaluateCondition(loopInstruction, context).isTrue()) {
            loopInstruction.getBlock().accept(this, new Context(context));
        }
        return new UndefinedWrapper();
    }

    /**
     * Hilfsfunktion, um die Bedingung einer Schlaufe zu prüfen.
     *
     * @return conditionInstruction
     * @throws IllegalArgumentException
     *             Die Bedingung muss in einen {@link BooleanWrapper} resultieren.
     */
    private BooleanWrapper evaluateCondition(final LoopInstruction loopInstruction, final Context context) {
        final var conditionInstruction = loopInstruction.getCondition().accept(this, context);
        if (!(conditionInstruction instanceof BooleanWrapper)) {
            throw new IllegalArgumentException(conditionInstruction.toString()
                    + " needs to evaluate to a boolean value to use as a condition for a loop!");
        }
        return (BooleanWrapper) conditionInstruction;
    }

    /**
     * Führt mathematische Instruktionen durch.
     *
     * @return {@link NumberWrapper}
     * @throws IllegalArgumentException
     *             Die Instruktionen `left` und `right` müssen in
     *             {@link NumberWrapper} resultieren.
     */
    @Override
    public Instruction visit(final ArithmeticInstruction mathInstruction, final Context context) {
        final var leftInstruction = mathInstruction.getLeft().accept(this, context);
        final var rightInstruction = mathInstruction.getRight().accept(this, context);
        if (!(leftInstruction instanceof NumberWrapper)) {
            throw new IllegalArgumentException(
                    leftInstruction.toString() + " needs to evaluate to a number to use in a math function ("
                            + mathInstruction.getOperator().toString() + ")!");
        }
        if (!(rightInstruction instanceof NumberWrapper)) {
            throw new IllegalArgumentException(
                    rightInstruction.toString() + " needs to evaluate to a number to use in a math function ("
                            + mathInstruction.getOperator().toString() + ")!");
        }

        final var left = (NumberWrapper) leftInstruction;
        final var right = (NumberWrapper) rightInstruction;

        switch (mathInstruction.getOperator()) {
        case ADDITION:
            return new NumberWrapper(left.getValue() + right.getValue());
        case SUBTRACTION:
            return new NumberWrapper(left.getValue() - right.getValue());
        case MULTIPLICATION:
            return new NumberWrapper(left.getValue() * right.getValue());
        case DIVISION:
            return new NumberWrapper(left.getValue() / right.getValue());
        default:
            return new UndefinedWrapper();
        }
    }

    /**
     * @return {@link StringWrapper}
     */
    @Override
    public Instruction visit(final StringWrapper stringWrapper, final Context context) {
        return stringWrapper;
    }

    /**
     * Prüft die Bedingung und führt entweder den linken oder rechten Block aus. Es
     * werden nie beide ausgeführt. Der ausgeführte Block wird in einem separaten
     * Kontext ausgeführt.
     *
     * @return Entweder den evaluierten linken (wenn die Bedingung wahr ist),
     *         ansonsten der evaluierte rechte Block zurück.
     * @throws IllegalArgumentException
     *             Die Bedingung muss in einem {@link BooleanWrapper} resultieren.
     */
    @Override
    public Instruction visit(final IfThenElseInstruction ternaryInstruction, final Context context) {
        final var conditionInstruction = ternaryInstruction.getCondition().accept(this, context);
        if (!(conditionInstruction instanceof BooleanWrapper)) {
            throw new IllegalArgumentException(
                    conditionInstruction.toString() + " needs to evaluate to a boolean value to use in a ternary!");
        }
        final var condition = (BooleanWrapper) conditionInstruction;
        if (condition.isTrue()) {
            return ternaryInstruction.getTrueBlock().accept(this, new Context(context));
        } else {
            return ternaryInstruction.getFalseBlock().accept(this, new Context(context));
        }
    }

    /**
     * @return Gibt den Wert einer bestimmten Variable im aktuellen Kontext zurück.
     */
    @Override
    public Instruction visit(final GetVariableInstruction getVariableInstruction, final Context context) {
        return context.getVariable(getVariableInstruction.getIdentifier());
    }

    /**
     * @return {@link NumberWrapper}
     */
    @Override
    public Instruction visit(final NumberWrapper numberWrapper, final Context context) {
        return numberWrapper;
    }

    /**
     * Führt Vergleiche von Instruktionen durch.
     *
     * @return {@link BooleanWrapper}
     */
    @Override
    public Instruction visit(final BooleanInstruction booleanInstruction, final Context context) {
        final var left = booleanInstruction.getLeft().accept(this, context);
        final var right = booleanInstruction.getRight().accept(this, context);

        switch (booleanInstruction.getOperator()) {
        case EQUAL:
            return new BooleanWrapper(Objects.equals(left, right));
        case NOT_EQUAL:
            return new BooleanWrapper(!Objects.equals(left, right));
        case GREATER:
            var lfloat1 = Float.parseFloat(left.toString());
            var rfloat1 = Float.parseFloat(right.toString());
            return new BooleanWrapper(lfloat1 > rfloat1);
        case SMALLER:
            var lfloat2 = Float.parseFloat(left.toString());
            var rfloat2 = Float.parseFloat(right.toString());
            return new BooleanWrapper(lfloat2 < rfloat2);
        case GREATER_EQUAL:
            var lfloat3 = Float.parseFloat(left.toString());
            var rfloat3 = Float.parseFloat(right.toString());
            return new BooleanWrapper(lfloat3 >= rfloat3);
        case SMALLER_EQUAL:
            var lfloat4 = Float.parseFloat(left.toString());
            var rfloat4 = Float.parseFloat(right.toString());
            return new BooleanWrapper(lfloat4 <= rfloat4);
        default:
            return new UndefinedWrapper();
        }
    }

    /**
     * Wird nie besucht.
     *
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final ArgumentList argumentList, final Context context) {
        return new UndefinedWrapper();
    }

    /**
     * Wird nie besucht.
     *
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final ParameterList parameterList, final Context context) {
        return new UndefinedWrapper();
    }

    /**
     * @return Evaluiert den Ausdruck und gibt dessen Rückgabewert zurück.
     */
    @Override
    public Instruction visit(final ReturnInstruction returnInstruction, final Context context) {
        return returnInstruction.getExpression().accept(this, context);
    }

    /**
     * Entnimmt die Funktion aus dem Kontext, initialisiert die Parameter mit den
     * definierten Argumenten und führt die Funktion aus. Die Funktion wird in einem
     * eigenen Kontext ausgeführt.
     *
     * @return Führt die Funktion aus und gibt dessen Rückgabewert aus.
     */
    @Override
    public Instruction visit(final FunctionCallInstruction functionCallInstruction, final Context context) {
        var functionEntry = context.getFunctionEntry(functionCallInstruction.getIdentifier(),
                functionCallInstruction.getArguments().size());

        final var innerContext = new Context(context);

        final var parameters = functionEntry.getParameters();
        final var arguments = functionCallInstruction.getArguments().getArguments();

        for (var i = 0; i < parameters.size(); i++) {
            innerContext.defineVariable(parameters.get(i), arguments.get(i).accept(this, context));
        }

        return functionEntry.getFunctionBody().accept(this, innerContext);
    }

    /**
     * Definiert (oder überschreibt) eine Funktion.
     *
     * @return {@link UndefinedWrapper}
     */

    @Override
    public Instruction visit(final FunctionDefinitionInstruction functionDefinitionInstruction, final Context context) {
        context.defineFunction(functionDefinitionInstruction.getIdentifier(),
                functionDefinitionInstruction.getParameters(), functionDefinitionInstruction.getBlock());
        return new UndefinedWrapper();
    }

    /**
     * @return {@link BooleanWrapper}
     */
    @Override
    public Instruction visit(final BooleanWrapper booleanWrapper, final Context context) {
        return booleanWrapper;
    }

    /**
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(final UndefinedWrapper undefinedWrapper, final Context context) {
        return undefinedWrapper;
    }

    /**
     * Print Instruction um etwas auszugeben.
     * @return {@link UndefinedWrapper}
     */
    @Override
    public Instruction visit(PrintInstruction printInstruction, Context context) {
        for(Instruction i : printInstruction.getArguments().getArguments()) {
            final var value = i.accept(this, context);
            System.out.println(value);
        }
        
        return new UndefinedWrapper();
    }
}
