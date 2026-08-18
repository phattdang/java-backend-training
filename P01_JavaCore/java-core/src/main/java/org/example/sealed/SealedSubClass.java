package org.example.sealed;

public sealed class SealedSubClass extends SealedClass permits SecondFinalClass{
    // This class can extend SealedClass because it is permitted
}
