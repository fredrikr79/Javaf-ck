package com.brainfuck;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        File program = new File("src/main/java/com/brainfuck/helloworld.txt");
        var c = new Compiler(program);
        c.runInstructions();
    }
}
