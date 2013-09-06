package com.teamdev.imagecreator;

public class Vector3D {
    public float xCoordinate;
    public float yCoordinate;
    public float zCoordinate;

    public Vector3D(float xCoordinate, float yCoordinate, float zCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
    }

    //Find [a, b], where a and b are vectors
    public Vector3D multiply(Vector3D vector){
        return new Vector3D(yCoordinate*vector.zCoordinate
                - vector.yCoordinate*zCoordinate,
                zCoordinate*vector.xCoordinate
                - xCoordinate*vector.zCoordinate,
                xCoordinate*vector.yCoordinate
                - vector.xCoordinate*yCoordinate);
    }
    //Find a*N, where a is vector, N is float number
    public Vector3D multiply(float multiplier){
        return new Vector3D(xCoordinate*multiplier,
                yCoordinate*multiplier,
                zCoordinate*multiplier);
    }
    //Find (a, b), where a and b are vectors
    public float scalarMultiply(Vector3D vector){
        return xCoordinate*vector.xCoordinate
                + yCoordinate*vector.yCoordinate
                + zCoordinate*vector.zCoordinate;
    }

    public Vector3D subtraction(Vector3D vector){
        return this.addition(vector.multiply(-1));
    }
    //Find a+b, where a and b are vectors
    public Vector3D addition(Vector3D vector){
        return new Vector3D(xCoordinate+vector.xCoordinate,
                yCoordinate+vector.yCoordinate,
                zCoordinate+vector.zCoordinate);
    }
    //Find length of vector
    public float length(){
        return (float)Math.sqrt(Math.pow(xCoordinate, 2)
                + Math.pow(yCoordinate, 2) + Math.pow(zCoordinate, 2));
    }
}
