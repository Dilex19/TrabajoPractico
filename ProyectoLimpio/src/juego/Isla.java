package juego;

import java.awt.Image;

import entorno.Entorno;

public class Isla {
	private double x,y;
	private double ancho;
	private double alto;
	private double escala;
	Image isla;
	Entorno e;
	
	public Isla(double x, double y, Entorno e, double escala) {
		this.x = x;
		this.y = y;
		this.e = e;
		this.escala = escala+0.2;
		this.isla = entorno.Herramientas.cargarImagen("islaVerde.png");
		this.alto = this.isla.getHeight(null) * this.escala;
		this.ancho = this.isla.getWidth(null) * this.escala;
	}


	public void dibujar() {
			this.e.dibujarImagen(this.isla, getX(), getY(), 0, escala);
	}
	
	
	public double getBorderSuperior() {
		return this.y - this.alto/2;
	}
	public double getBorderInferior() {
		return this.y + this.alto/2;
	}
	public double getBorderIzquierdo() {
		return this.x - this.ancho/2;
	}
	public double getBorderDerecho() {
		return this.x + this.ancho/2;
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
}
