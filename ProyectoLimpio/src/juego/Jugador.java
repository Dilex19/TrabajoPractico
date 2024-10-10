package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;

public class Jugador{
	private double x,y;
	private int velocidad;
	private int vida;
	private boolean direccion;
	private double ancho;
	private double alto;
	private double escala;
	Image Izq;
	Image Der;
	Entorno e;
	boolean apoyado;
	
	public Jugador(double x, double y, Entorno e) {
		this.x = x;
		this.y = y;
		this.e = e;
		this.direccion = false;
		this.velocidad = 2;
		this.vida = 100;
		this.escala =0.2;
		this.Izq = entorno.Herramientas.cargarImagen("azul.png");
		this.Der = entorno.Herramientas.cargarImagen("azul.png");
		this.apoyado = false;
		this.alto = this.Izq.getHeight(null) * this.escala;
		this.ancho = this.Izq.getWidth(null) * this.escala;
	}
	
	
	public void moverIzquierda() {
		if(getBorderIzquierdo() <0) { //si se choca con el borde para
			this.x = this.ancho/2-1;
		} else {
			this.x -= velocidad; 
		}
	}
	public void moverDerecha() {//si se choca con el borde para
		if(getBorderDerecho() > this.e.ancho()) {
			this.x = (this.e.ancho() - this.ancho/2)+2;
		} else {
			this.x += velocidad; 
		}
	}
	
	
	public void gravedad() {
		if(!apoyado) {
			this.y+=2;
		}
	}
//	public void moverArriba() {this.y -=velocidad;}
//	public void moverAbajo() {this.y +=velocidad;}
	
	//Dibujar Jugador
	public void dibujar() {
		if(this.direccion == true) {
			this.e.dibujarImagen(this.Izq, getX(), getY(), 0, escala);
		} else {
			this.e.dibujarImagen(this.Der, getX(), getY(), 0, escala);
		}
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
	public int getVida() {return vida;}
	
	public void recibirDaño(int daño) {this.vida -=daño;}
}
