package com.example.sensorratingscale;

public class Star {

    private int data;
    private String time_text;

    /*constructor*/
    public Star(int value,String time)
    {
        this.data = value;
        this.time_text=time;
    }

    public int getData()
    {
        return data;
    }

    public String getTime()
    {
        return time_text;
    }

    public void setData(int value)
    {
        this.data = value;
    }
    public void setTime(String time)
    {
        this.time_text = time;
    }

}