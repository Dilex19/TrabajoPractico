package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Jugador jugador;
//	private Enemigo enemigo;
	private Tortuga tortuga;
	private Isla[] islas;
	private int momentoDeSalto;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.jugador = new Jugador(300.0,100.0, entorno);
		this.tortuga = new Tortuga(entorno);
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
		
		if(jugador != null) {

			for(Isla is: this.islas) {
				is.dibujar();
			}
			tortuga.dibujar();
			tortuga.gravedad();
			
			if(!tortuga.direccionAleatoria) {
				if(estaApoyado(tortuga, islas)) {
					this.tortuga.apoyado = true;
					double x =  Math.random();
					if(x > 0.5) {
						tortuga.direccion = true;
					} else {
						tortuga.direccion = false;
					}
					tortuga.direccionAleatoria = true;
				} else {
					this.tortuga.apoyado = false;
				}
			}
			
			Isla islaDeLaTortuga = islaDeLaTortuga(this.tortuga, this.islas);
			if(estaAlBordeDerecho(tortuga, islaDeLaTortuga)) {
				tortuga.direccion = false;
			}
			if(estaAlBordeIzquierdo(tortuga, islaDeLaTortuga)) {
				tortuga.direccion = true;
			}
			if(estaApoyado(tortuga, islas)) {
				tortuga.movimientoX();
				}
			
			
			jugador.dibujar();
			if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
				jugador.moverIzquierda();
			}
			if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
				jugador.moverDerecha();
			}
			
			salto(); //funcion que toma en cuenta cuando el jugador quiere dar un salto
			if(jugador.saltando) { //si el jugador esta saltando subira por cierto periodo de tiempo
				jugador.saltando(momentoDeSalto, entorno.tiempo());
			}
			
			//Caida del jugador
			jugador.gravedad();
			
			//
			if(estaApoyado(jugador, islas)) {
				this.jugador.apoyado = true;
			} else {
				this.jugador.apoyado = false;
			}
			
			if(jugador.seCayoJugador()) {
				jugador = null;
			}
			
			//Movimiento del enemigo hacia el jugador
//			enemigo.moverHaciaJugador(jugador);
//			//Dibujar
//			enemigo.dibujar(entorno);
//			if(colision(jugador, enemigo,30)){
//				jugador=null;
//				System.out.println("colision!!!!");
//			}
		}
		
		
		//si el jugador es null se tomara como que muerió
		if(jugador== null) {
			entorno.cambiarFont("Arial", 30, Color.WHITE);
			entorno.escribirTexto("MORISTE" , 330, 200);
			entorno.escribirTexto("Apreta espacio para reaparecer" , 200, 300);
		}
		
		//crea de nuevo al jugador para no tener que reiniciar el juego
		if(jugador==null && entorno.estaPresionada(entorno.TECLA_ESPACIO)){
			jugador=new Jugador(300.0,100.0, entorno);
		}
	
		
	
		
	}
	
	
	public Isla islaDeLaTortuga(Tortuga t, Isla[] i) { // esta funcion sirve si y solo si la tortuga esta apoyada en una isla
		Isla is = islas[1];
		for(Isla islas: i) {
			if(estaEnLaIsla(t,islas)) {
				is =  islas;
			}
		}
		return is;
	}
	
	private boolean estaEnLaIsla(Tortuga t, Isla i) {
		if(Math.abs(t.getBorderInferior()-i.getBorderSuperior())<1 && (t.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(t.getBorderDerecho()>i.getBorderIzquierdo())) {
			return true;
		}
		return false;
	}


	private boolean estaAlBordeDerecho(Tortuga t, Isla islas2) {
		return (t.getBorderDerecho() >islas2.getBorderDerecho());
	}

	private boolean estaAlBordeIzquierdo(Tortuga t, Isla islas2) {
		return (t.getBorderIzquierdo() <islas2.getBorderIzquierdo());
	}
	// si el jugador toca la arriba y esta tocando el piso se guarda el momento en el que lo hizo
	private void salto() { 
		if(this.entorno.sePresiono(entorno.TECLA_ARRIBA) && estaApoyado(jugador, islas)) {
			this.momentoDeSalto = entorno.tiempo();
			jugador.saltando = true;
		}
		
	}

	//retorna true si el jugador se encuentra pisando una isla del arreglo, retorna false de lo contrario
	public boolean estaApoyado(Jugador j, Isla[] i) {
		for(Isla islas: i) {
			if(estaApoyado(j,islas)) {
				return true;
			}
		}
		return false;
	}
	
	
	//retorna true si el jugador esta pisando la isla
	public boolean estaApoyado(Jugador j, Isla i) {
		return Math.abs(j.getBorderInferior()-i.getBorderSuperior())<2 && (j.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(j.getBorderDerecho()>i.getBorderIzquierdo());
	}
	
	//retorna true si la tortuga esta pisando la isla
	public boolean estaApoyado(Tortuga t, Isla i) {
		return Math.abs(t.getBorderInferior()-i.getBorderSuperior())<1 && (t.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(t.getBorderDerecho()>i.getBorderIzquierdo());
	}
	
	//retorna true si la Totuga se encuentra pisando una isla del arreglo, retorna false de lo contrario
	public boolean estaApoyado(Tortuga j, Isla[] i) {
		for(Isla islas: i) {
			if(estaApoyado(j,islas)) {
				return true;
			}
		}
		return false;
	}

	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}


	
}
