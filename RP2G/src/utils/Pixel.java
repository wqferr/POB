package utils;

import java.awt.Color;

public class Pixel {
	private Color color;
	
	public Pixel(int color) {
		this.color = new Color(color);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Retorna a cor do pixel.
	 * @return A cor
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Retorna a componente vermelha deste pixel.
	 * @return A componente vermelha.
	 */
	public int red() {
		return Pixel.red(this.color);
	}
	
	/**
	 * Retorna a componente verde deste pixel.
	 * @return A componente verde deste pixel.
	 */
	public int green() {
		return Pixel.green(this.color);
	}
	
	/**
	 * Retorna a componente azul deste pixel.
	 * @return A componente azul deste pixel.
	 */
	public int blue() {
		return Pixel.blue(this.color);
	}
	
	/**
	 * Retorna a componente alpha deste pixel.
	 * @return A componente alpha deste pixel.
	 */
	public int alpha() {
		return Pixel.alpha(this.color);
	}
	
	/**
	 * Verifica se o pixel é preto.
	 * @return Se o pixel é preto.
	 */
	public boolean isBlack() {
		return Pixel.isBlack(this.color);
	}
	
	/**
	 * Verifica se o pixel é branco.
	 * @return Se o pixel é branco.
	 */
	public boolean isWhite() {
		return Pixel.isWhite(this.color);
	}
	
	/**
	 * Verifica se o pixel é vermelho.
	 * @return Se o pixel é vermelho.
	 */
	public boolean isRed() {
		return Pixel.isRed(this.color);
	}
	
	/**
	 * Verifica se o pixel é verde.
	 * @return Se o pixel é verde.
	 */
	public boolean isGreen() {
		return Pixel.isGreen(this.color);
	}
	
	/**
	 * Verifica se o pixel é azul.
	 * @return Se o pixel é azul.
	 */
	public boolean isBlue() {
		return Pixel.isBlue(this.color);
	}
	
	/**
	 * Retorna a componente vermelha deste int RGBA.
	 * @param color O int RGBA.
	 * @return A componente vermelha.
	 */
	public static int red(int color) {
		return Pixel.red(new Color(color));
	}
	
	/**
	 * Retorna a componente verde deste int RGBA.
	 * @param color O int RGBA.
	 * @return A componente verde.
	 */
	public static int green(int color) {
		return Pixel.green(new Color(color));
	}
	
	/**
	 * Retorna a componente azul deste int RGBA.
	 * @param color O int RGBA.
	 * @return A componente azul.
	 */
	public static int blue(int color) {
		return Pixel.blue(new Color(color));
	}
	
	/**
	 * Retorna a componente alpha deste int RGBA.
	 * @param color O int RGBA.
	 * @return A componente alpha.
	 */
	public static int alpha(int color) {
		return Pixel.alpha(new Color(color));
	}
	
	/**
	 * Verifica se o int RGBA é preto.
	 * @param color O int RGBA.
	 * @return Se a cor é preta.
	 */
	public static boolean isBlack(int color) {
		return Pixel.isBlack(new Color(color));
	}
	
	/**
	 * Verifica se o int RGBA é branco.
	 * @param color O int RGBA.
	 * @return Se a cor é branca.
	 */
	public static boolean isWhite(int color) {
		return isWhite(Pixel.green(new Color(color)));
	}
	
	/**
	 * Verifica se o int RGBA é vermelho.
	 * @param color O int RGBA.
	 * @return Se a cor é vermelha.
	 */
	public static boolean isRed(int color) {
		return Pixel.isRed(new Color(color));
	}
	
	/**
	 * Verifica se o int RGBA é verde.
	 * @param color O int RGBA.
	 * @return Se a cor é verde.
	 */
	public static boolean isGreen(int color) {
		return Pixel.isGreen(new Color(color));
	}
	
	/**
	 * Verifica se o int RGBA é azul.
	 * @param color O int RGBA.
	 * @return Se a cor é azul.
	 */
	public static boolean isBlue(int color) {
		return Pixel.isBlue(new Color(color));
	}
	
	/**
	 * Retorna a componente vermelha da cor.
	 * @param color A cor cuja componente é desejada.
	 * @return A componente vermelha.
	 */
	public static int red(Color color) {
		return color.getRed();
	}
	
	/**
	 * Retorna a componente verde da cor.
	 * @param color A cor cuja componente é desejada.
	 * @return A componente verde.
	 */
	public static int green(Color color) {
		return color.getGreen();
	}
	
	/**
	 * Retorna a componente azul da cor.
	 * @param color A cor cuja componente é desejada.
	 * @return A componente azul.
	 */
	public static int blue(Color color) {
		return color.getBlue();
	}
	
	/**
	 * Retorna a componente alpha da cor.
	 * @param color A cor cuja componente é desejada.
	 * @return A componente alpha.
	 */
	public static int alpha(Color color) {
		return color.getAlpha();
	}
	
	/**
	 * Verifica se a cor é preta.
	 * @param color Cor 
	 * @return Se a cor é preta
	 */
	public static boolean isBlack(Color color) {
		return (Pixel.red(color) | Pixel.green(color) | Pixel.blue(color))==0x00;
	}
	
	/**
	 * Verifica se a cor é branca.
	 * @param color Cor 
	 * @return Se a cor é branca
	 */
	public static boolean isWhite(Color color) {
		return (Pixel.red(color) & Pixel.green(color) & Pixel.blue(color))==0xff;
	}
	
	/**
	 * Verifica se a cor é vermelha.
	 * @param color Cor 
	 * @return Se a cor é vermelha
	 */
	public static boolean isRed(Color color) {
		return (Pixel.red(color) & ~Pixel.green(color) & ~Pixel.blue(color))==0xff;
	}
	
	/**
	 * Verifica se a cor é verde.
	 * @param color Cor 
	 * @return Se a cor é verde
	 */
	public static boolean isGreen(Color color) {
		return (~Pixel.red(color) & Pixel.green(color) & ~Pixel.blue(color))==0xff;
	}

	/**
	 * Verifica se a cor é azul.
	 * @param color Cor 
	 * @return Se a cor é azul
	 */
	public static boolean isBlue(Color color) {
		return (~Pixel.red(color) & ~Pixel.green(color) & Pixel.blue(color))==0xff;
	}
}
