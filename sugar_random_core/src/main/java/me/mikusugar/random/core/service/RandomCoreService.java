package me.mikusugar.random.core.service;


import me.mikusugar.random.core.utils.RandomUtilInterface;

public class RandomCoreService<T> {

    private final String input;

    private final RandomUtilInterface<T> randomUtilInterface;

    public RandomCoreService(String input, RandomUtilInterface<T> randomUtilInterface) {
        this.input = input;
        this.randomUtilInterface = randomUtilInterface;
    }

    public String getInput() {
        return input;
    }

    public RandomUtilInterface<T> getRandomUtilInterface() {
        return randomUtilInterface;
    }

}
