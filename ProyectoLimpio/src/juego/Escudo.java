package juego;

import java.awt.Image;

import entorno.Entorno;

public class Escudo {
	private double x;
	private double y;
	private int usos;
	private boolean visible;
	private double ancho;
	private double alto;
	private double escala;
	private boolean direccion;
	Image Izq;
	Image Der;
	Entorno e;
	Jugador j;
	
	public Escudo(Entorno e, Jugador j, int usos) {
		this.j = j;
		this.x = j.getX();
		this.y = j.getY();
		this.visible = false;
		this.usos = usos;
		this.escala = 0.05;
		this.direccion = false;
		this.e = e;
		this.Izq = entorno.Herramientas.cargarImagen("escudoPrueba.png");
		this.Der = entorno.Herramientas.cargarImagen("escudoPrueba.png");
		this.ancho = this.Izq.getWidth(null) * this.escala;
		this.alto = this.Der.getHeight(null) * this.escala;
	}
	
	
	public void proteccionEscudo(boolean direc, Jugador j) {
		if(!direc) {
			this.direccion = true;
			this.x = j.getX() + 15;
			this.y = j.getY();
			} else {
			this.direccion = false;
			this.x = j.getX() - 15;
			this.y = j.getY();
		}
	}
	
	public void dibujar() {
		if(this.direccion) {
			this.e.dibujarImagen(this.Izq, getX(), getY(),0, escala);
		}
		this.e.dibujarImagen(this.Der, getX(), getY(), 0,escala);
	}
	
	public void escudoUsado() {
		this.usos -=1;
	}
	
	
	public double getY() {
		return this.y;
	}

	public double getX() {
		return this.x;
	}
	
	public int getUsos() {
		return this.usos;
	}
	
	public double getBorderSuperior() { // retorna el borde de la parte superior de la imagen
		return this.y - this.alto/2;
	}
	public double getBorderInferior() {// retorna el borde de la parte inferior de la imagen
		return this.y + this.alto/2;
	}
	public double getBorderIzquierdo() { // retorna el borde de la parte izquierda de la imagen
		return this.x - this.ancho/2;
	}
	public double getBorderDerecho() { // retorna el borde de la parte izquierda de la imagen
		return this.x + this.ancho/2;
	}
}
