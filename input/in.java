import java.awt.*;
import static java.lang.integer.*;
import static java.lang.Long.*;
import static java.lang.System.*;

import javax.swing.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Spaghetti {
    // This is acceptable
    void doNothing() {}

    // This is equally acceptable
    void doNothingElse() {
    }
    public static void main(String[] args) {
        char ch = '\u0009';
        char ch = '\011';
        char ch = '\10';
        char ch = '\u0008';
        char ch = '\12';
        char ch_2 = '\u000a';
        char ch = '\14';
        char ch_2 = '\u000c';
        char ch = '\15';
        char ch_2 = '\u000d';
        char ch = '\42';
        char ch_2 = '\u0022';
        char oct_ch = '\47';
        char uni_ch = '\u0027';
        char oct_ch = '\134';
        char uni_ch = '\u005c';
    }
}