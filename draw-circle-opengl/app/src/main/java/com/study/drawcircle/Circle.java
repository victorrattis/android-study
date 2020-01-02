package com.study.drawcircle;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Circle {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    private static final int COORDS_PER_VERTEX = 3;
    private int app = -1;
    private int mColorHandle, vertexStride = 0;

    // basically a circle is a linestring so we need its centre
    // radius and how many segments it will consist of
    public Circle(float cx, float cy, float radius, int segments) {
        CalculatePoints(cx, cy, radius, segments);
        if (app == -1) {
            int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            // create empty OpenGL ES Program
            app = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(app, vertexShader);

            // add the fragment shader to program
            GLES20.glAttachShader(app, fragmentShader);

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(app);
        }
    }

    // actuall openGL drawing
    public void draw() {
        Log.d("devlog", "draw");
        int vertexCount = vertexBuffer.remaining() / COORDS_PER_VERTEX;

        // Add program to the environment
        GLES20.glUseProgram(app);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(app, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(app, "vColor");

        // Draw the triangle, using triangle fan is the easiest way
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        // Set color of the shape (circle)
        GLES20.glUniform4fv(mColorHandle, 1, new float[]{0.5f, 0.3f, 0.1f, 1f}, 0);
    }

    public void CalculatePoints(float cx, float cy, float radius, int segments) {
        Log.d("devlog", "CalculatePoints: " + cx + ", " + cy);
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();

        float[] coordinates = new float[segments * COORDS_PER_VERTEX];

        for (int i = 0; i < segments * 3; i += 3) {
            float percent = (i / (segments - 1f));
            float rad = percent * 2f * (float) Math.PI;

            //Vertex position
            float xi = cx + radius * (float) Math.cos(rad);
            float yi = cy + radius * (float) Math.sin(rad);

            coordinates[i] = xi;
            coordinates[i + 1] = yi / (((float) dm.heightPixels / (float) dm.widthPixels));
            coordinates[i + 2] = 0.0f;
        }

        // initialise vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coordinates);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
    }

}