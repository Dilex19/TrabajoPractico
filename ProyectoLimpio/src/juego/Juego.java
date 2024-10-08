package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Jugador jugador;
	private Enemigo enemigo;
	private Isla[] islas;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.jugador = new Jugador(300.0,100.0, entorno);
//		this.enemigo = new Enemigo(100,100);
		this.islas = new Isla[15];
		int k = 0;
		for(int i = 1; i<6;i++) {
			for( int j =1; j <= i;j++) {
				islas[k++] = new Isla((j)*this.entorno.ancho()/(i+1), 100*i, entorno,1.0/(i+2));
			}
		}
		
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		for(Isla is: this.islas) {
			is.dibujar();
		}
		
		if(jugador != null) {

			jugador.dibujar();
			if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
				jugador.moverIzquierda();
			}
			if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
				jugador.moverDerecha();
			}
			
			
			//Caida
			jugador.gravedad();
			if(estaApoyado(jugador, islas)) {
				this.jugador.apoyado = true;
			} else {
				this.jugador.apoyado = false;
			}
			
//			if(entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
//				jugador.moverArriba();
//			}
//			if(entorno.estaPresionada(entorno.TECLA_ABAJO)) {
//				jugador.moverAbajo();
//			}
			//Movimiento del enemigo hacia el jugador
//			enemigo.moverHaciaJugador(jugador);
//			//Dibujar
//			enemigo.dibujar(entorno);
//			if(colision(jugador, enemigo,30)){
//				jugador=null;
//				System.out.println("colision!!!!");
//			}
		}
		
		if(jugador== null) {
			entorno.escribirTexto("El jugador se destruyo!!" , 500, 100);	
		}
		
		if(jugador==null && entorno.estaPresionada(entorno.TECLA_ESPACIO)){
			jugador=new Jugador(500.0,500.0, entorno);
			enemigo = new Enemigo(200,100);
		}
	
		
	
		
	}
	
	
	public boolean estaApoyado(Jugador j, Isla[] i) {
		for(Isla islas: i) {
			if(estaApoyado(j,islas)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean estaApoyado(Jugador j, Isla i) {
		return Math.abs(j.getBorderInferior()-i.getBorderSuperior())<2 && (j.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(j.getBorderDerecho()>i.getBorderIzquierdo());
	}
	
	public boolean colision(Jugador n, Enemigo e, double d) {
		return (n.getX()-e.getX())*(n.getX()-e.getX())+(n.getY()-e.getY())*(n.getY()-e.getY())<d*d;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
