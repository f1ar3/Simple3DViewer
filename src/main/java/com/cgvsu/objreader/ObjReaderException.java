package com.cgvsu.objreader;

/** @author <a href="https://github.com/arda-sketch/CG3.ObjReader">Артамонов Артём Артурович</a> */

public class ObjReaderException extends RuntimeException {
    public ObjReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
    }
}