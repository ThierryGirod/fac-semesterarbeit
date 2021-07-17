package interpreter;

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

public interface Visitor {

    Instruction visit(final Program script, final Context context);

    Instruction visit(final InstructionList block, final Context context);

    Instruction visit(final AssignInstruction assignInstruction, final Context context);

    Instruction visit(final LoopInstruction loopInstruction, final Context context);

    Instruction visit(final ArithmeticInstruction mathInstruction, final Context context);

    Instruction visit(final StringWrapper stringWrapper, final Context context);

    Instruction visit(final IfThenElseInstruction ternaryInstruction, final Context context);

    Instruction visit(final GetVariableInstruction getVariableInstruction, final Context context);

    Instruction visit(final NumberWrapper numberWrapper, final Context context);

    Instruction visit(final BooleanInstruction booleanInstruction, final Context context);

    Instruction visit(final ArgumentList argumentList, final Context context);

    Instruction visit(final ParameterList parameterList, final Context context);

    Instruction visit(final ReturnInstruction returnInstruction, final Context context);

    Instruction visit(final FunctionCallInstruction functionCallInstruction, final Context context);

    Instruction visit(final FunctionDefinitionInstruction functionDefinitionInstruction, final Context context);

    Instruction visit(final BooleanWrapper booleanWrapper, final Context context);

    Instruction visit(final UndefinedWrapper undefinedWrapper, final Context context);

    Instruction visit(PrintInstruction printInstruction, Context context);
}
