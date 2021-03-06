package org.firstinspires.ftc.teamcode.Blobs.blobdetect;

import org.firstinspires.ftc.teamcode.Blobs.image.Pixel;

public class Blob {
    public static int currentId = 0;
    public int width, height;
    public int x, y;
    public Pixel color;
    public int id;
    public boolean seen;

    public Blob(int width, int height, int x, int y, Pixel color) {
        set(width, height, x, y, color);
    }

    public void set(int width, int height, int x, int y, Pixel color) {
        set(width, height, x, y, color, currentId++);
    }

    //size, coordinates and color of blob
    public void set(int width, int height, int x, int y, Pixel color, int id) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        this.id = id;
    }
}
