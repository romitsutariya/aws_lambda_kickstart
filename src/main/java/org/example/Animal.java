package org.example;

/** Represents an animal payload handled by the Lambda function. */
public record Animal(String species, boolean canHop, boolean canSwim) {}
