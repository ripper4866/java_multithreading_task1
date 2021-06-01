package ru.pflb.learning;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        long startTime, endTime;
        String elapsedTimeString;
        int threadsNum = Integer.parseInt(args[0]);
        int minVal, arrSize = 100000, elementsByThread = arrSize / threadsNum;
        int[] inputArr = new int[arrSize];
        int[] outputArr = new int[threadsNum];
        Thread[] threads = new Thread[threadsNum];


        System.out.println(Thread.currentThread().getName() + " started");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < inputArr.length; i++) {
            inputArr[i] = getRandomNumber(-1000000, 1000000);
        }
        endTime = System.currentTimeMillis();
        elapsedTimeString = String.valueOf((double) (endTime - startTime) / 1000.0);
        System.out.println("Initializing array took " + elapsedTimeString + " seconds");

//        // Calculating minimal value with one thread
//        startTime = System.currentTimeMillis();
//        minVal = inputArr[0];
//        for (int i = 1; i < inputArr.length; i++) {
//            if (inputArr[i] < minVal) minVal = inputArr[i];
//        }
//        endTime = System.currentTimeMillis();
//        elapsedTimeString = String.valueOf((double) (endTime - startTime) / 1000.0);
//        System.out.println("Minimal value is " + String.valueOf(minVal));
//        System.out.println("Calculating minimal value with one thread took " + elapsedTimeString + " seconds");
//        // Calculating minimal value with one thread END

        startTime = System.currentTimeMillis();
        for (int i = 0; i < threadsNum; i++) {
            int leftBoundary = elementsByThread * i;
            int rightBoundary = elementsByThread * (i + 1) - 1;
            threads[i] = new Thread(new ValueFinder(inputArr, outputArr, i, leftBoundary, rightBoundary));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        minVal = outputArr[0];
        for (int i = 1; i < outputArr.length; i++) {
            if (outputArr[i] < minVal) minVal = outputArr[i];
        }
        endTime = System.currentTimeMillis();
        elapsedTimeString = String.valueOf((double) (endTime - startTime) / 1000.0);
        System.out.println("Minimal value is " + String.valueOf(minVal));
        System.out.println("Calculating minimal value with multiple threads took " + elapsedTimeString + " seconds");
        System.out.println(Thread.currentThread().getName() + " finished");
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

class ValueFinder implements Runnable {

    int[] inputArr, outputArr;
    int leftBoundary, rightBoundary, threadNum;

    public ValueFinder(int[] inputArr, int[] outputArr, int threadNum, int leftBoundary, int rightBoundary) {
        this.inputArr = inputArr;
        this.outputArr = outputArr;
        this.threadNum = threadNum;
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        outputArr[threadNum] = Arrays.stream(this.inputArr, this.leftBoundary, this.rightBoundary).min().getAsInt();
        System.out.println(Thread.currentThread().getName() + " finished");
    }
}
