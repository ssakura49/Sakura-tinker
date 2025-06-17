package com.ssakura49.sakuratinker.utils;

public enum CommonRGBUtil {
    lightRed(0xff7e75),
    red(0xff0000),
    orange(0xffaa7f),
    yellow(0xffff00),
    lightYellow(0xffff7f),
    pink(0xffaaff),
    darkPurple(0x3e4289);
    private final int RGB;
    CommonRGBUtil(int RGB){
        this.RGB=RGB;
    }
    public int getRGB(){
        return this.RGB;
    }
}
