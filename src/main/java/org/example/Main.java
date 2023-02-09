package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        var fileReaderJobExecutor = new FileReaderJobExecutor();
        fileReaderJobExecutor.run();
    }
}