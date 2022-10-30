import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;

class MathFunction {
    PVector matToVec(float[][] matrix) {
        PVector vector = new PVector();
        vector.x = matrix[0][0];
        vector.y = matrix[1][0];
        vector.z = 0;
        if (matrix.length > 2) {
            vector.z = matrix[2][0];
        }
        return vector;
    }

    float[][] pVcToMat(PVector vector) {
        float[][] mat = new float[3][1];
        mat[0][0] = vector.x;
        mat[1][0] = vector.y;
        mat[2][0] = vector.z;

        return mat;
    }

    PVector matrixMultiply(float[][] a, float[][] b) {
        int rowA = a.length;
        int colA = a[0].length;

        int rowB = b.length;
        int colB = b[0].length;

        float[][] product = new float[rowA][colB];

        for (int i = 0; i < rowA; i++) {
            for (int j = 0; j < colB; j++) {
                float result = 0;
                for (int k = 0; k < colA; k++) {
                    result += a[i][k] * b[k][j];
                }
                product[i][j] = result;
            }
        }

        return matToVec(product);
    }
}

public class Main extends PApplet {

    float[][] projectionConstant = {
            {1, 0, 0},
            {0, 1, 0}
    };

    float theta = 0;

    PVector[] coordinates = new PVector[8];

    public void settings() {
        size(600, 600);
        coordinates[0] = new PVector(-50, -50, -50);
        coordinates[1] = new PVector(50, -50, -50);
        coordinates[2] = new PVector(50, 50, -50);
        coordinates[3] = new PVector(-50, 50, -50);

        coordinates[4] = new PVector(-50, -50, 50);
        coordinates[5] = new PVector(50, -50, 50);
        coordinates[6] = new PVector(50, 50, 50);
        coordinates[7] = new PVector(-50, 50, 50);

        MathFunction math = new MathFunction();
    }

    public void draw() {
        background(0);
        translate(width / 2, height / 2);
        stroke(255);
        strokeWeight(4);
        noFill();
        MathFunction math = new MathFunction();

        PVector[] dotSync = new PVector[8];

        float[][] rZ = {
                {cos(theta), -sin(theta), 0},
                {sin(theta), cos(theta), 0},
                {0, 0, 1}
        };

        float[][] rY = {
                {cos(theta), 0, -sin(theta)},
                {0, 1, 0},
                {sin(theta), 0, cos(theta)}
        };


        for (int i = 0; i < coordinates.length; i++) {
            float[][] coordinatesInMatrix = math.pVcToMat(coordinates[i]);
            PVector angularRotation = math.matrixMultiply(rZ, coordinatesInMatrix);
            angularRotation = math.matrixMultiply(rY, math.pVcToMat(angularRotation));

            PVector proj = math.matrixMultiply(projectionConstant, math.pVcToMat(angularRotation));
            dotSync[i] = proj;
        }
        theta += 0.01;

        for (int i = 0; i < dotSync.length; i++) {
            stroke(255);
            strokeWeight(4);
            noFill();
            point(dotSync[i].x, dotSync[i].y);
        }
        lines(0, 1, dotSync);
        lines(1, 2, dotSync);
        lines(2, 3, dotSync);
        lines(3, 0, dotSync);

        lines(4, 5, dotSync);
        lines(5, 6, dotSync);
        lines(6, 7, dotSync);
        lines(7, 4, dotSync);
        lines(0, 4, dotSync);
        lines(1, 5, dotSync);
        lines(2, 6, dotSync);
        lines(3, 7, dotSync);
    }

    public void lines(int a, int b, PVector[] pointInSpace) {
        PVector point1 = pointInSpace[a];
        PVector pointB  = pointInSpace[b];
        line(point1.x, point1.y, pointB.x, pointB.y);
    }


    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

}