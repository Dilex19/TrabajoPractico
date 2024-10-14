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
	private Tortuga[] tortugas;
	private Isla[] islas;
	private int momentoDeSalto;
	private Ataque[] ataques;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		this.jugador = new Jugador(300.0,100.0, entorno);
		this.islas = new Isla[15];
		int k = 0;
		for(int i = 1; i<6;i++) {
			for( int j =1; j <= i;j++) {
				islas[k++] = new Isla((j)*this.entorno.ancho()/(i+1), 100*i, entorno,1.0/(i+2));
			}
		}
		
		this.tortugas = new Tortuga[100];
		this.tortugas[0] = new Tortuga(entorno);
		this.ataques = new Ataque[50];
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
			
			nuevaTortuga(tortugas);
			for(Tortuga tor: this.tortugas) {
				if(tor != null) {
					tor.dibujar();
					tor.gravedad();
					
					if(!tor.direccionAleatoria) { // cuando la tortuga toca una isla, elige aleatoriamente si ir a la izquierda o derecha
						if(estaApoyado(tor, islas)) {
							tor.apoyado = true;
							double x =  Math.random();
							if(x > 0.5) {
								tor.direccion = true;
							} else {
								tor.direccion = false;
							}
							tor.direccionAleatoria = true;
						} else {
							tor.apoyado = false;
						}
					}
					
					Isla islaDeLaTortuga = islaDeLaTortuga(tor, this.islas); // guarda la isla en la que esta la tortuga
					if(estaAlBordeDerecho(tor, islaDeLaTortuga)) { // si esta al borde derecho da la vuelta a la izquierda
						tor.direccion = false;
					}
					if(estaAlBordeIzquierdo(tor, islaDeLaTortuga)) { // si esta al borde izquierdo da la vuelta a la derecha
						tor.direccion = true;
					}
					if(estaApoyado(tor, islas)) {
						tor.movimientoX();
						}
				}
			}
			
			if(entorno.sePresiono(entorno.TECLA_ESPACIO)) { // tecla para tirar un ataque
				nuevoAtaque(ataques);
			}
			for (int i =0; i<ataques.length;i++) { //mueve el ataque al lado para el que fue lanzado
				if (ataques[i] != null) {
					ataques[i].movimientoX();					}
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
	
	
	
	
	private void nuevoAtaque(Ataque[] ata) {
		for(int i= 0; i<ata.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
			if(ata[i] == null) {
				ata[i] = new Ataque(jugador,entorno);
				return;
			}
		}
	}
	
	
	
	private void nuevaTortuga(Tortuga[] tortu) { //rellena los espaciocios nulos del arreglo de tortugas para crear una nueva tortuga
		if(entorno.numeroDeTick()%300== 0) {
			for(int i= 0; i<tortu.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
				if(tortu[i] == null) {
					tortu[i] = new Tortuga(entorno);
					return;
				}
			}
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
