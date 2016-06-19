package utils;

public abstract class Pixel {
	private int color;
	
	public Pixel(int color){
		this.color = color;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public int getColor(){
		return this.color;
	}
	
	public byte red(){
		return Pixel.red(this.color);
	}
	
	public byte green(){
		return Pixel.green(this.color);
	}
	
	public byte blue(){
		return Pixel.blue(this.color);
	}
	
	public byte alpha(){
		return Pixel.alpha(this.color);
	}
	
	public static byte red(int color){
		return (byte)(color & 0x00ff0000);
	}
	
	public static byte green(int color){
		return (byte)(color & 0x0000ff00);
	}
	
	public static byte blue(int color){
		return (byte)(color & 0x000000ff);
	}
	
	public static byte alpha(int color){
		return (byte)(color & 0xff000000);
	}
}
