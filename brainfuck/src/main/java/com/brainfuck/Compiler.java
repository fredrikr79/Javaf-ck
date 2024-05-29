package com.brainfuck;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class Compiler {
    private byte[] memory = new byte[30000];
    private int memoryPointer = 0;
    private final char[] instructions;
    private int instructionPointer = 0;
    private Stack<Integer> pointerLoopRecall = new Stack<>();

    public Compiler(File file) throws IOException {
        Optional<String> parsed = Parser.parseFile(file);
        if (parsed.isEmpty())
            throw new IOException("Failed to parse file.");
        this.instructions = parsed.get().toCharArray();
    }

    public Compiler(String instructions) {
        this.instructions = instructions.toCharArray();
    }

    public void runInstructions() throws IOException {
        this.instructionPointer = 0;
        this.memoryPointer = 0;
        while (instructionPointer < instructions.length) {
            char currentInstruction = this.instructions[this.instructionPointer];
            switch (currentInstruction) {
                case '+' -> this.incrementValue();
                case '-' -> this.decrementValue();
                case '>' -> this.incrementPointer();
                case '<' -> this.decrementPointer();
                case '[' -> this.enterLoop();
                case ']' -> this.conditionLoop();
                case '.' -> this.printMemoryBlock();
                case ',' -> this.readInputToMemoryBlock();
            }
            this.instructionPointer++;
        }
    }

    private void conditionLoop() {
        if (this.memory[this.memoryPointer] != 0)
            this.instructionPointer = this.pointerLoopRecall.pop() - 1;
    }

    private void enterLoop() {
        if (this.memory[this.memoryPointer] != 0) {
            this.pointerLoopRecall.add(this.instructionPointer);
        } else {
            for (int j = this.memoryPointer; j < instructions.length; j++) {
                if (instructions[j] == ']') {
                    this.instructionPointer = j;
                    return;
                }
            }
        }
    }

    private void incrementPointer() {
        this.memoryPointer++;
    }

    private void decrementPointer() {
        this.memoryPointer--;
    }

    private void incrementValue() {
        this.memory[this.memoryPointer]++;
    }

    private void decrementValue() {
        this.memory[this.memoryPointer]--;
    }

    private void printMemoryBlock() {
        System.out.print((char) this.memory[this.memoryPointer]);
    }

    private void readInputToMemoryBlock() throws IOException {
        this.memory[this.memoryPointer] = (byte) System.in.read();
    }
}
