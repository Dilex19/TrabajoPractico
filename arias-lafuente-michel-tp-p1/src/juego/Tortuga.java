package juego;

import java.awt.Image;

import entorno.Entorno;

public class Tortuga {
	double x,y;
	private double velocidad;
	private double ancho;
	private double alto;
	private double escala;
	boolean direccion;
	Entorno e;
	Image Izq;
	Image Der;
	boolean apoyado;
	
	public Tortuga(Entorno e) {
		this.x = spawnRandom(e);
		this.y = -20;
		this.e = e;
		this.direccion = false;
		this.velocidad = 0.5;
		this.escala =0.09;
		this.Izq = entorno.Herramientas.cargarImagen("Enemigo2.png");
		this.Der = entorno.Herramientas.cargarImagen("Enemigo.png");
		this.apoyado = false;
		this.alto = this.Izq.getHeight(null) * this.escala;
		this.ancho = this.Izq.getWidth(null) * this.escala;
	}
	
	//cada que es llamada la funcion, la tortuga cae 2 pixeles.
	public void gravedad() {
		if(!apoyado) {
			this.y +=2;
		}
	}
	
	//Dibujar Tortuga
		public void dibujar() {
			if(this.direccion == true) {
				this.e.dibujarImagen(this.Izq, getX(), getY(), 0, escala);
			} else {
				this.e.dibujarImagen(this.Der, getX(), getY(), 0, escala);
			}
		}
	
	
	
	//Elige unas coordenadas aleatorioas din contar el "medio"
	private int spawnRandom(Entorno e) {
		int x = (int) (Math.random() * e.ancho());
		while((x <510 && x >290) || x<90 || x>700) {
			x = (int) (Math.random() * e.ancho());
		}
		return x;
	}

	
	//Mueve a la tortuga hacia la derecha si direccion es true, en caso contrario a la izquierda;
	public void movimientoX() {
		if (this.direccion) {
			this.x +=velocidad;
		} else {
			this.x -=velocidad;
		}
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


	public double getX() {
		return x;
}
	public double getY() {
		return y;
	}

	public boolean getDireccion() {
		return direccion;
	}
	
}
