package juego;

import java.awt.Color;
import java.awt.Image;
import juego.Juego;
import entorno.Entorno;

public class Jugador{
	private double x,y;
	private int velocidad;
	private boolean direccion;
	private double ancho;
	private double alto;
	private double escala;
	boolean saltando;
	private int vida;
	Image Izq;
	Image Der;
	Entorno e;
	boolean apoyado;
	Juego j;
	
	public Jugador(double x, double y, Entorno e, int vida) {
		this.x = x;
		this.y = y;
		this.e = e;
		this.vida=vida;
		this.direccion = false;
		this.velocidad = 2;
		this.escala =0.35;
		this.Izq = entorno.Herramientas.cargarImagen("Personaje2.png");
		this.Der = entorno.Herramientas.cargarImagen("Personaje.png");
		this.apoyado = false;
		this.alto = this.Izq.getHeight(null) * this.escala;
		this.ancho = this.Izq.getWidth(null) * this.escala;
	}
	
	
	//mueve el jugador hacia la izquierda
	public void moverIzquierda() {
		this.direccion = true;
		if(getBorderIzquierdo() <0) { //si se choca con el borde para
			this.x = this.ancho/2-1;
		} else {
			this.x -= velocidad; 
		}
	}
	
	//mueve al jugador hacia la derecha
	public void moverDerecha() {//si se choca con el borde para
		this.direccion = false;
		if(getBorderDerecho() > this.e.ancho()) {
			this.x = (this.e.ancho() - this.ancho/2)+2;
		} else {
			this.x += velocidad; 
		}
	}
	
	//el jugador caera simulando gravedad
	public void gravedad() {
		if(!apoyado) {
			this.y+=5;
		}
	}

	//Dibujar Jugador
	public void dibujar() {
		if(this.direccion == true) {
			this.e.dibujarImagen(this.Izq, getX(), getY(), 0, escala);
		} else {
			this.e.dibujarImagen(this.Der, getX(), getY(), 0, escala);
		}
	}
	
	
	
	//El jugador subira por 300 milisegundos que dura el salto
	public void saltando(int momentoDeSalto, int i) {
		if(i- momentoDeSalto < 300) {
			this.y-=12;
		} else {
			this.saltando = false;
		}
	}
	
	
	//Resta una vida al jugador
	public void daño() {
		this.vida -= 1;	
	}
	
	//Suma una vida al jugador
	public void masVida() {
		this.vida+=1; 
	}
	
	
	//Si el jugador cae fuera de la pantalla retorna true, en caso contrario false.
	public boolean seCayoJugador() {
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
	
	public double getX() {return x;}
	public double getY() {return y;}
	public void setY(double y) {
		this.y = y;
	}
	public void setX(double x ) {
		this.x=x;
	}
	
	public boolean getDireccion() {
		return direccion;
	}

	public int getVida(){
		return this.vida;
		
	}

}
