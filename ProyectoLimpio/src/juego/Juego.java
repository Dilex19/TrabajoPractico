package juego;


import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entor;
	private Jugador jugador;
//	private Enemigo enemigo;
	private Tortuga[] tortugas;
	private Isla[] islas;
	private int momentoDeSalto;
	private Ataque[] ataques;
	private Bomba[] bombas;
	private Image fondoCielo;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entor = new Entorno(this, "Proyecto para TP", 800, 600);
		this.jugador = new Jugador(270.0,455.0, entor);
		this.islas = new Isla[15];
		int k = 1;
		for(int i = 1; i<6;i++) {
			if(i==1) {
				islas[0] = new Isla((1)*this.entor.ancho()/(1+1), 100*1, entor,0.34);
			} else {
				for( int j =1; j <= i;j++) {
				islas[k++] = new Isla((j)*this.entor.ancho()/(i+1), 100*i, entor,1.3/(i+2));
					}
				}
		}
		
		this.bombas = new Bomba[100];
		this.tortugas = new Tortuga[10];
		this.ataques = new Ataque[10];
		this.fondoCielo = entorno.Herramientas.cargarImagen("cieloPrueba.png");
		
		// Inicializar lo que haga falta para el juego
		// ...

		// Inicia el juego!
		this.entor.iniciar();
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
			
			this.entor.dibujarImagen(this.fondoCielo, 0, 150, 0, 1);

			for(Isla is: this.islas) {
				is.dibujar();
			}
			
			nuevaTortuga(tortugas);
			for(Tortuga tor: this.tortugas) {
				if(tor != null) {
					tor.dibujar();
					tor.gravedad();
					
					double random =  Math.random();
					
					boolean tortugaApoyadoEnIsla = estaApoyado(tor, islas);
					
					if(tortugaApoyadoEnIsla && (tor.apoyado == false)) {
						tor.apoyado = true;
						if(random > 0.5) {
							tor.direccion = true;
						} else {
							tor.direccion = false;
						}
						} 
					if(!tortugaApoyadoEnIsla) {
							tor.apoyado = false;
						}
					
					Isla islaDeLaTortuga = islaDeLaTortuga(tor, this.islas); // guarda la isla en la que esta la tortuga
					
					
					tortugasRebote(tor,islaDeLaTortuga); //si la tortuga llega a un borde, cambia de dirreccion
					
					
					if(random >= 0.998 && tortugaApoyadoEnIsla) {
						tirarBomba(bombas,tor);
					}
					
					
					if(tortugaApoyadoEnIsla) {
						tor.movimientoX();
						}
				}
			}
			
			for (Bomba bom: this.bombas) { //mueve el ataque al lado para el que fue lanzado
				if (bom != null) {
					bom.movimientoX();					
					}
			}
			
			tortugaMuere(tortugas,ataques); //si una bola de fuego choca con una tortuga, estos desaparecen, osea, se vuelven null
			
			
			ataqueFueraDePantalla(ataques); //si una bola de fuego sale de la pantalla esta se vuelve null
			
			
			
			
			
			if(entor.sePresiono(entor.TECLA_ESPACIO)) { // tecla para tirar un ataque
				nuevoAtaque(ataques);
			}
			for (int i =0; i<ataques.length;i++) { //mueve el ataque al lado para el que fue lanzado
				if (ataques[i] != null) {
					ataques[i].movimientoX();					}
			}
			
			jugador.dibujar();
			if(entor.estaPresionada(entor.TECLA_IZQUIERDA)) {
				jugador.moverIzquierda();
			}
			if(entor.estaPresionada(entor.TECLA_DERECHA)) {
				jugador.moverDerecha();
			}
			
			
			salto(); //funcion que toma en cuenta cuando el jugador quiere dar un salto
			if(jugador.saltando) { //si el jugador esta saltando subira por cierto periodo de tiempo
				jugador.saltando(momentoDeSalto, entor.tiempo());
			}
			
			//Caida del jugador
			jugador.gravedad();
			
			//
			if(estaApoyado(jugador, islas)) {
				this.jugador.apoyado = true;
			} else {
				this.jugador.apoyado = false;
			}
			
			
			
			if(jugador.seCayoJugador() || tortugaColicionJugador(tortugas,jugador) || colisionBombaJugador(bombas,jugador)) {
				jugador = null;
			} else {
				if(tortugaColicionJugador(tortugas,jugador)) {
						jugador=null;
				}
			}
			
		}
		
		
		//si el jugador es null se tomara como que muerió
		if(jugador== null) {
			entor.cambiarFont("Arial", 30, Color.WHITE);
			entor.escribirTexto("MORISTE" , 330, 200);
			entor.escribirTexto("Apreta espacio para reaparecer" , 200, 300);
		}
		
		//crea de nuevo al jugador para no tener que reiniciar el juego
		if(jugador==null && entor.estaPresionada(entor.TECLA_ESPACIO)){
			jugador=new Jugador(270.0,455.0, entor);
		}
	
		
	
		
	}
	
	
	
	private boolean colisionBombaJugador(Bomba[] bomba, Jugador jugador2) {
		for(Bomba b: bombas) {
			if((jugador != null && b != null) && ( (jugador.getBorderDerecho() > b.getBorderIzquierdo() && jugador.getBorderIzquierdo() <b.getBorderIzquierdo() ) 
					|| (jugador.getBorderIzquierdo() <b.getBorderDerecho() &&
					jugador.getBorderDerecho() > b.getBorderDerecho())) && 
					jugador.getBorderInferior() >b.getBorderSuperior() && jugador.getBorderSuperior()<b.getBorderInferior() ) {
				return true;
			}
		}
		return false;
	}

	private boolean tortugaColicionJugador(Tortuga[] tortugas, Jugador jugador) {
		for(Tortuga t: this.tortugas) {
			if((jugador != null && t != null) && ( (jugador.getBorderDerecho() > t.getBorderIzquierdo() && jugador.getBorderIzquierdo() <t.getBorderIzquierdo() ) 
					|| (jugador.getBorderIzquierdo() <t.getBorderDerecho() &&
					jugador.getBorderDerecho() > t.getBorderDerecho())) && 
					jugador.getBorderInferior() >t.getBorderSuperior() && jugador.getBorderSuperior()<t.getBorderInferior() ) {
				return true;
			}
		}
		return false;
	}

	private void tortugasRebote(Tortuga tor, Isla islaDeLaTortuga) {
		if(estaAlBordeDerecho(tor, islaDeLaTortuga)) { // si esta al borde derecho da la vuelta a la izquierda
			tor.direccion = false;
		}
		if(estaAlBordeIzquierdo(tor, islaDeLaTortuga)) { // si esta al borde izquierdo da la vuelta a la derecha
			tor.direccion = true;
		}
	}

	private void ataqueFueraDePantalla(Ataque[] ata) {
		for(int i= 0; i<ata.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
			if(ata[i] != null && (ata[i].getBorderDerecho()> this.entor.ancho() || ata[i].getBorderIzquierdo() <0)) {
				ata[i] = null;
			} 
		}
		
	}

	private void tortugaMuere(Tortuga[] t, Ataque[] ata) { //si una bola de fuego choca con una tortuga, estos desaparecen, osea, se vuelven null
		for(int j = 0; j<t.length;j++) {
			for(int i= 0; i<ata.length;i++) { 
				if(t[j] !=null) {
					if(ata[i] != null && ( (ata[i].getBorderDerecho() > t[j].getBorderIzquierdo() && ata[i].getBorderIzquierdo() <t[j].getBorderIzquierdo() ) 
							|| (ata[i].getBorderIzquierdo() <t[j].getBorderDerecho() &&
							ata[i].getBorderDerecho() > t[j].getBorderDerecho())) && 
							ata[i].getBorderInferior() >=t[j].getBorderSuperior() && ata[i].getBorderSuperior()<=t[j].getBorderInferior() ) {
						ata[i]= null;
						t[j]=null;
					}
				}
			}
		}
	}
	
	
	
	private void nuevoAtaque(Ataque[] ata) {
		for(int i= 0; i<ata.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
			if(ata[i] == null) {
				ata[i] = new Ataque(jugador,entor);
				return;
			}
		}
	}
	
	
	
	private void nuevaTortuga(Tortuga[] tortu) { //rellena los espaciocios nulos del arreglo de tortugas para crear una nueva tortuga
		if(entor.numeroDeTick()%300== 0 || entor.numeroDeTick() == 0) {
			for(int i= 0; i<tortu.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
				if(tortu[i] == null) {
					tortu[i] = new Tortuga(entor);
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
		if(this.entor.sePresiono(entor.TECLA_ARRIBA) && estaApoyado(jugador, islas)) {
			this.momentoDeSalto = entor.tiempo();
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
	
	
	private void tirarBomba(Bomba[] bom, Tortuga tor) {
		for(int i= 0; i<bom.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
			if(bom[i] == null) {
				bom[i] = new Bomba(tor,entor);
				return;
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}


	
}
