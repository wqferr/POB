package utils;

import java.awt.Color;

public class Pixel {
	private Color color;
	
	public Pixel(int color){
		this.color = new Color(color);
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public int red(){
		return Pixel.red(this.color);
	}
	
	public int green(){
		return Pixel.green(this.color);
	}
	
	public int blue(){
		return Pixel.blue(this.color);
	}
	
	public int alpha(){
		return Pixel.alpha(this.color);
	}
	
	public boolean isBlack(){
		return Pixel.isBlack(this.color);
	}
	
	public boolean isWhite(){
		return Pixel.isWhite(this.color);
	}
	
	public boolean isRed(){
		return Pixel.isRed(this.color);
	}
	
	public boolean isGreen(){
		return Pixel.isGreen(this.color);
	}
	
	public boolean isBlue(){
		return Pixel.isBlue(this.color);
	}
	
	public static int red(int color){
		return Pixel.red(new Color(color));
	}
	
	public static int green(int color){
		return Pixel.green(new Color(color));
	}
	
	public static int blue(int color){
		return Pixel.blue(new Color(color));
	}
	
	public static int alpha(int color){
		return Pixel.alpha(new Color(color));
	}
	
	public static boolean isBlack(int color){
		return Pixel.isBlack(new Color(color));
	}
	
	public static boolean isWhite(int color){
		return isWhite(Pixel.green(new Color(color)));
	}
	
	public static boolean isRed(int color){
		return Pixel.isRed(new Color(color));
	}
	
	public static boolean isGreen(int color){
		return Pixel.isGreen(new Color(color));
	}
	
	public static boolean isBlue(int color){
		return Pixel.isBlue(new Color(color));
	}
	
	public static int red(Color color){
		return color.getRed();
	}
	
	public static int green(Color color){
		return color.getGreen();
	}
	
	public static int blue(Color color){
		return color.getBlue();
	}
	
	public static int alpha(Color color){
		return color.getAlpha();
	}
	
	public static boolean isBlack(Color color){
		return (Pixel.red(color) | Pixel.green(color) | Pixel.blue(color))==0x00;
	}
	
	public static boolean isWhite(Color color){
		return (Pixel.red(color) & Pixel.green(color) & Pixel.blue(color))==0xff;
	}
	
	public static boolean isRed(Color color){
		return (Pixel.red(color) & ~Pixel.green(color) & ~Pixel.blue(color))==0xff;
	}
	
	public static boolean isGreen(Color color){
		return (~Pixel.red(color) & Pixel.green(color) & ~Pixel.blue(color))==0xff;
	}
	
	public static boolean isBlue(Color color){
		return (~Pixel.red(color) & ~Pixel.green(color) & Pixel.blue(color))==0xff;
	}
}
