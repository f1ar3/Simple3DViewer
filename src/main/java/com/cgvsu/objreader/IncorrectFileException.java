package com.cgvsu.objreader;

/** @author <a href="https://github.com/arda-sketch/CG3.ObjReader">Артамонов Артём Артурович</a> */

public class IncorrectFileException extends Exception{
    public IncorrectFileException(String additionString){
        super("Input file is incorrect. " + additionString);
    }
}
