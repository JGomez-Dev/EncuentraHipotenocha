package com.example.encuentrahipotenocha;

public class Item {
    private int mImageResource;

    //Contructor de la clase
    public Item(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    //metodo Set de la clase
    //Establece una imagen
    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    //metodo Get de la clase
    //metodo encargado de delvolve la imagen al Main
    public int getImageResource() {
        return mImageResource;
    }
}
