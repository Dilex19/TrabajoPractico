package juego;

import java.awt.Color;
import entorno.Entorno;

public class Enemigo{
	private int x,y;
	private int velocidad;
	
	public Enemigo(int x,int y) {
		this.x = x;
		this.y = y;
		this.velocidad = 2;
	}
	
	public void moverHaciaJugador(Jugador jugador) {
		if(jugador.getX() > this.x) {
			this.x += velocidad;
		} else {
			this.x -= velocidad;
		}
		if(jugador.getY() > this.y) {
			this.y += velocidad;
		} else {
			this.y -= velocidad;
		}
		
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x,y, 30, 30, 0, Color.red);
	}
}