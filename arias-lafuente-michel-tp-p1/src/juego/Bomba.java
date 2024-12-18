package juego;

import java.awt.Image;

import entorno.Entorno;

public class Bomba {
	private double x,y;
	private double ancho;
	private double alto;
	private double escala;
	private Image imagen;
	Entorno e;
	Tortuga t;
	private boolean direccion;
	
	public Bomba(Tortuga t, Entorno e) {
		this.x = t.getX();
		this.y = t.getY()+5;
		this.t = t;
		this.escala = 0.08;
		this.e = e;
		this.direccion = t.getDireccion();
		this.imagen = entorno.Herramientas.cargarImagen("bombaPrueba.png");
		this.alto = this.imagen.getHeight(null) * this.escala;
		this.ancho = this.imagen.getWidth(null) * this.escala;
		
	}
	
	public void movimientoX() {
		if(this.direccion) {
			this.x += 4;
		} else {
			this.x -=4;
		}
		this.e.dibujarImagen(this.imagen, getX(), getY(), 0, escala);
	}
	


	private double getY() {
		return this.y;
	}

	private double getX() {
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
