package juego;

import java.awt.Color;
import entorno.Entorno;

public class Jugador{
	private int x,y;
	private int velocidad;
	private int vida;
	
	public Jugador(int x, int y) {
		this.x = x;
		this.y = y;
		this.velocidad = 5;
		this.vida = 100;
	}
	
	public void moverIzquierda() {this.x -=velocidad;}
	public void moverDerecha() {this.x +=velocidad;}
	public void moverArriba() {this.y -=velocidad;}
	public void moverAbajo() {this.y +=velocidad;}
	
	//Dibujar Jugador
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, 30,30, 0, Color.blue);
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getVida() {return vida;}
	
	public void recibirDaño(int daño) {this.vida -=daño;}
}
