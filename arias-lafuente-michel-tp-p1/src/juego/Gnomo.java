package juego;

import java.awt.Image;

import entorno.Entorno;

public class Gnomo {
	 	Entorno e;
	    private Gnomo gnomos;
	    double x,y;
		private double velocidad;
		private double ancho;
		private double alto;
		private double escala;
		boolean direccion;
		Image Izq;
		Image Der;
		boolean apoyado;
		boolean direccionAleatoria;
	    
	    public Gnomo(Entorno e) {	
			this.x = 400;
			this.y = 60;
			this.e = e;
			this.direccion = false;
			this.direccionAleatoria = true;
			this.velocidad = 0.8;
			this.escala =0.3;
			this.Izq = entorno.Herramientas.cargarImagen("Gnomo.png");
			this.Der = entorno.Herramientas.cargarImagen("Gnomo2.png");
			this.apoyado = false;
			this.alto = this.Izq.getHeight(null) * this.escala;
			this.ancho = this.Izq.getWidth(null) * this.escala;
	    }

	 
	    public void dibujar() {
			if(this.direccion == true) {
				this.e.dibujarImagen(this.Izq, getX(), getY(), 0, escala);
			} else {
				this.e.dibujarImagen(this.Der, getX(), getY(), 0, escala);
			}
		}

	    public void gravedad() {
			if(!apoyado) {
				this.y +=2;
			}
		}

	    
		public void movimientoX() {
			if (this.direccion) {
				this.x +=velocidad;
			} else {
				this.x -=velocidad;
			}
		}
		
		public boolean seCayoGnomo() {
			if(getBorderSuperior() > e.alto()+20) {
				return true;
			}
			return false;
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
