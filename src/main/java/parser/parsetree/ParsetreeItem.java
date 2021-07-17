package parser.parsetree;

import java.util.List;
import java.util.stream.Collectors;

import runtime.Instruction;

public class ParsetreeItem {

    private static final int INDENTATION_PER_LEVEL = 2;

    private final String text;
    private final int level;

    public ParsetreeItem(final String text, final int level) {
        this.text = text;
        this.level = level;
    }

    public ParsetreeItem(final Instruction instruction, final int level) {
        this.text = instruction.getClass().getSimpleName();
        this.level = level;
    }

    @Override
    public String toString() {
        String prefix = " ".repeat(INDENTATION_PER_LEVEL) + "-";
        return " ".repeat(level * INDENTATION_PER_LEVEL) + prefix + " " + text;
    }

    public static void visualizeParsetree(final List<ParsetreeItem> list) {
        print("");
        print("Tree visualization:");
        print("");
        print(list.stream().map(ParsetreeItem::toString).collect(Collectors.joining("\n")));
        print("");
    }

    private static void print(final String text) {
        System.out.println(text);
    }
}
