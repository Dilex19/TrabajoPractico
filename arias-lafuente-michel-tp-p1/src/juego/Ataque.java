package juego;

import java.awt.Image;

import entorno.Entorno;

public class Ataque {
	private double x,y;
	private double ancho;
	private double alto;
	private double escala;
	Image Izq;
	Image Der;
	Entorno e;
	Jugador j;
	private boolean direccion;
	
	public Ataque(Jugador j, Entorno e) {
		this.x = j.getX();
		this.y = j.getY()+5;
		this.j = j;
		this.escala = 0.03;
		this.e = e;
		this.direccion = j.getDireccion();
		this.Izq = entorno.Herramientas.cargarImagen("fuegoPrueba.png");
		this.Der = entorno.Herramientas.cargarImagen("fuegoPrueba2.png");
		this.alto = this.Izq.getHeight(null) * this.escala;
		this.ancho = this.Izq.getWidth(null) * this.escala;
		
	}
	
	
	public void movimientoX() { //mueve al ataque en direccion izquierda si direccion es igual a true, en direccion derecha en caso contrario.
		if(this.direccion) {
			this.x -= 4;
			this.e.dibujarImagen(this.Der, getX(), getY(), 0, escala);
		} else {
			this.x +=4;
			this.e.dibujarImagen(this.Izq, getX(), getY(), 0, escala);
		}
	}
	


	private double getY() { //retorna y
		return this.y;
	}

	private double getX() { // retorna X
		return this.x;
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
