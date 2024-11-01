package juego;


import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private enum EstadoJuego { MENU, JUGANDO, PERDIDO, GANADO }
	private EstadoJuego estadoActual;
	private Image fondoMenu;
	private int tiempoMenu;
	private Image menuEnter;
	private Image MensajeInicial;
	private Entorno entor;
	private Jugador jugador;
	private Tortuga[] tortugas;
	private Isla[] islas;
	private int momentoDeSalto;
	private Ataque ataque;
	private Bomba[] bombas;
	private Image fondoCielo;
	private Image fondoPerdiste;
	private Image fondoGanar;
	private Escudo escudo;
	private Gnomo[] gnomo;
	private Image casaImagen;
	private int[] posiciones;
	private int perdidos;
	private int salvados;
	private int eliminados;
	private int salvadosNecesarios; //La cantidad necesaria de gnomos rescatados para ganar
	private int gnomosPerdidosMaximos; //la cantidad maxima de gnomos que pueden "morir"
	private int tiempoJuegoTerminado;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.estadoActual = EstadoJuego.MENU;
		this.entor = new Entorno(this, "Proyecto para TP", 800, 600);
		this.jugador = new Jugador(270.0,455.0, entor,3);
		this.islas = new Isla[15];
		int k = 1;
		for(int i = 1; i<6;i++) {
			if(i==1) {
				islas[0] = new Isla((1)*this.entor.ancho()/(1+1), 100*1, entor,0.34); //Especificamos a la primera isla para que no quede tan grande a comparacion de las otras
			} else {
				for( int j =1; j <= i;j++) {
				islas[k++] = new Isla((j)*this.entor.ancho()/(i+1), 100*i, entor,1.3/(i+2));
					}
				}
		}
		this.posiciones = new int[4];
		for(int i = 0; i<4;i++) {
			this.posiciones[i] = 200*i;
		}
		this.gnomo = new Gnomo[4];
		this.bombas = new Bomba[1];
		this.tortugas = new Tortuga[10];
		this.menuEnter = entorno.Herramientas.cargarImagen("menuEnter.png");
		this.fondoMenu = entorno.Herramientas.cargarImagen("menuImagen.png");
		this.fondoCielo = entorno.Herramientas.cargarImagen("cieloPrueba.png");
		this.fondoPerdiste = entorno.Herramientas.cargarImagen("fondoPerdiste.png");
		this.casaImagen = entorno.Herramientas.cargarImagen("CasaGnomo.png");
		this.MensajeInicial = entorno.Herramientas.cargarImagen("MensajeInicial.png");
		this.fondoGanar = entorno.Herramientas.cargarImagen("fondoGanar.jpg");
		this.escudo = new Escudo(this.entor,this.jugador,3);
		this.perdidos = 0;
		this.salvados = 0;
		this.eliminados = 0;
		this.salvadosNecesarios = 5;
		this.gnomosPerdidosMaximos = 10;
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
		if (estadoActual == EstadoJuego.MENU) {
	        // Dibuja la imagen del menú en el centro de la pantalla
			
	        this.entor.dibujarImagen(fondoMenu, this.entor.ancho() / 2, this.entor.alto() / 2, 0, 0.8);
	        
	        this.entor.dibujarImagen(MensajeInicial, this.entor.ancho() / 2, this.entor.alto() / 4, 0, 1.1);
	        
	        // Mensaje de inicio
	        this.entor.dibujarImagen(menuEnter, this.entor.ancho() / 2, this.entor.alto() / 1.5, 0, 0.8);
	        
	        // Si se presiona ENTER, cambia el estado a JUGANDO
	        if (entor.sePresiono(entor.TECLA_ENTER)) {
	           this.tiempoMenu = this.entor.tiempo();
	        	estadoActual = EstadoJuego.JUGANDO;
	        }
	        return; // Salir del método para no continuar al siguiente estado
	    }
		
		//si el jugador es null se tomara como que murió
		if(jugador.getVida()==0 || (this.perdidos== this.gnomosPerdidosMaximos)) { //si el jugador es null o se perdieron la contidad maxima de gnomos perdidos.
			if(estadoActual == EstadoJuego.JUGANDO) {
				this.tiempoJuegoTerminado = this.entor.tiempo();
				estadoActual = EstadoJuego.PERDIDO;
			}
			this.entor.dibujarImagen(fondoPerdiste, 420, 280, 0, 1.5);
			this.entor.cambiarFont("italy", 20, Color.white);
			String xSalvados = " "+ this.salvados;
			this.entor.escribirTexto("SALVADOS:" + xSalvados, 330, 425);
			tiempo(330,500,this.tiempoJuegoTerminado, Color.white);
		//	tiempo(330,500,Color.WHITE);
		} else {
			
		if(jugador != null && this.salvados !=this.salvadosNecesarios) {
			this.entor.dibujarImagen(this.fondoCielo, 0, 150, 0, 1); //dibuja el fondo de cielo azul
			for(Isla is: this.islas) { //dibuja las islas en pantalla
				is.dibujar();
			}
			
			this.entor.dibujarImagen(casaImagen, 395, 40, 0, 0.2);
		
			nuevaTortuga(tortugas); // genera una nueva tortuga hasta que llegue a 10 tortugas vivas
			for(Tortuga tor: this.tortugas) { // recorre la lista de tortugas
				if(tor != null) { //mientras la tortuga no sea nula, la dibuja y hace que caiga
					tor.dibujar();
					tor.gravedad();
					double random =  Math.random();
					boolean tortugaApoyadoEnIsla = estaApoyado(tor, islas);
					if(tortugaApoyadoEnIsla && (tor.apoyado == false)) { //con un numero random, se hace un 50/50 para que cuando toque una isla, elija aleatoriamente entre izquierda o derecha como direcciòn.
						tor.apoyado = true;
						if(random > 0.5) {//si el numero es mayor a 0,5, la tortuga gira hacia la izquierda, si no, hacia la derecha
							tor.direccion = true;
						} else {
							tor.direccion = false;
						}
						} 
					if(!tortugaApoyadoEnIsla) { //Se pregunta si la tortuga se encuentra en una isla
							tor.apoyado = false;
						}
					
					Isla islaDeLaTortuga = islaDeLaTortuga(tor, this.islas); // guarda la isla en la que esta la tortuga
					
					tortugasRebote(tor,islaDeLaTortuga); //si la tortuga llega a un borde, cambia de dirreccion
					
					if(random >= 0.998 && tortugaApoyadoEnIsla) { //si el numero random es mayor o igual a 0.998 y esta apoyado en una isla. Esta otruga tira un elemento bomba hacia la dirección donde está mirando.
						tirarBomba(bombas,tor);
					}
	
					if(tortugaApoyadoEnIsla) { // si esta apoyado camina hacia la dirección donde mira.
						tor.movimientoX();
						}
				}
			}
			
			nuevoGnomo(gnomo); //genera un nuevo elemento gnomo
            for(Gnomo gno: this.gnomo) { //recorre el arreglo de gnomos
                if(gno != null) { // mientra el elemento no sea nulo, dibuja en pantalla al gnomo y hace que caiga
                    gno.dibujar();
                    gno.gravedad();
                    double random =  Math.random();
                    boolean gnomoApoyadoEnIsla = estaApoyadoGnomo(gno, islas);
                    if(gnomoApoyadoEnIsla && (gno.apoyado == false)) { //Se genera un numero random, y si el gnomo se encuentra apoyado en una isla, se aleatorisa si gira hacia la derecha o hacia la izquierda.
                        gno.apoyado = true;
                        if(random > 0.5) { //si el numero es mayor a 0,5, el gnomo gira hacia la izquierda, si no, hacia la derecha
                            gno.direccion = true;
                        } else {
                            gno.direccion = false;
                        }
                        } 
                    if(!gnomoApoyadoEnIsla) {
                            gno.apoyado = false;
                        }

                    if(gnomoApoyadoEnIsla) { //Si el gnomo esta apoyado en una isla este se mueve hacia donde mira.
                        gno.movimientoX();
                        }
                }
                
            }
			 
			for (Bomba bom: this.bombas) { //mueve el ataque al lado para el que fue lanzado
				if (bom != null) {
					bom.movimientoX();					
					}
			}
			
			tortugaMuere(tortugas,ataque); //si una bola de fuego choca con una tortuga, estos desaparecen, osea, se vuelven null
			
			ataqueFueraDePantalla(); //si una bola de fuego sale de la pantalla esta se vuelve null
			
			bombaFueraDePantalla(bombas); //si una  sale de la pantalla esta se vuelve null
			
			if(entor.sePresiono(entor.TECLA_ESPACIO)) { // tecla para tirar un ataque
				nuevoAtaque();
			}
							
			if (ataque != null) {	//mueve el ataque al lado para el que fue lanzado
				ataque.movimientoX();					
				}
			
			jugador.dibujar(); //dibuja en pantalla al jugador
			if(entor.estaPresionada(entor.TECLA_IZQUIERDA)) { //si se preciona la flecha izquierda, se mueve hacia la izquierda
				jugador.moverIzquierda();
			}
			if(entor.estaPresionada(entor.TECLA_DERECHA)) { //Si se preciona la flecha derecha, se mueve hacia la derecha.
				jugador.moverDerecha();
			}
			
			if(entor.estaPresionada(entor.TECLA_ABAJO) && escudo.getUsos() !=0) { //si se preciona la flecha abajo, aparece el escudo que proteje al jugador una cantidad limitada de veces de bombas
				escudo.proteccionEscudo(jugador.getDireccion(), jugador);
				escudo.dibujar();
				proteccionDelEscudo(escudo, bombas);
			} 
			
			//recorre la lista de gnomos.
			for(int i =0; i<gnomo.length;i++) { 
				//Si el gnomo no es nulo y se sale de la pantalla, o colisiona con una tortuga o colisiona con una bomba, este gnomo se vuelve nulo y se suma uno al contador de gnomos perdidos
                if(gnomo[i]!=null &&(gnomo[i].seCayoGnomo() || tortugaColicionGnomo(tortugas,gnomo[i])  || colisionBombaGnomo(bombas,gnomo[i]))) {
                    gnomo[i] = null;
                    this.perdidos +=1;
                } else { //sino, si colisiona con el jugador Y está por debajo de la tercera isla, el gnomo se vuelve nulo, suma 1 al contador de SALVADOS
                	  if(gnomo[i]!=null && (jugadorColicionGnomo(jugador,gnomo[i]) && gnomo[i].getY()>315)) {
                          gnomo[i]=null;
                          this.salvados +=1;
                          double random =Math.random();
                          if (random <0.2) { //De manera "random" al rescatar al gnomo se puede dar al jugador mas escudo o mas vida o no darle nada.
                        	  jugador.masVida();
                          }
                          if(random>0.7) {
                        	  escudo.masEscudo();
                          }
                      }
                }
            }
			
			salto(); //funcion que toma en cuenta cuando el jugador quiere dar un salto
			if(jugador.saltando) { //si el jugador esta saltando subira por cierto periodo de tiempo
				jugador.saltando(momentoDeSalto, entor.tiempo());
			}
			
			//Caida del jugador
			jugador.gravedad();
			
			//
			if(estaApoyado(jugador, islas)) { // si el jugador se encuentra una isla, la variable apoyado del jugador cambia a true, si no se mantiene en flase
				this.jugador.apoyado = true;
			} else {
				this.jugador.apoyado = false;
			}
			
			colisionFuegoBomba(ataque,bombas); //Si el ataque colisiona con la bomba, se eliminan mutuamente
			
			if(jugador.seCayoJugador() || tortugaColicionJugador(tortugas,jugador) || colisionBombaJugador(bombas,jugador)) { //Si el jugador colisiona con una toruga, o con una bomba o se cae, se le resta una vida y reaparece.
				jugador.daño();
				jugador.setX(270.0);
				jugador.setY(455.0);
				
			} 
			
			tiempo(this.posiciones[0], 25,this.entor.tiempo()-this.tiempoMenu, Color.black); //Imprime el tiempo en la parte superior de la pantalla
			gnomosPerdidos(this.posiciones[1]); //Dibuja la cantidad de gnomos perdidos en la parte superior de la pantalla.
			gnomosSalvados(this.posiciones[2]); //Dibuja la cantidad de gnomos salvados en la parte superior de la pantalla.
			tortugasEliminadas(this.posiciones[3]); //Dibuja la cantidad de tortugas eliminadas en la parte superior de la pantalla
			mostrarUsosEscudo(); //Dibuja la cantidad de usos que le quedan al escudo en la parte inferior de la pantalla.
			mostrarVida(); //Dibuja la cantidad de vida que tiene el jugador en la parte inferior de la pantalla.
			
			
		} else {
			if(this.salvados>=this.salvadosNecesarios) {	 // si se llega a la cantidad necesaria de gnomos salvados, el jugador gana.
				this.entor.dibujarImagen(fondoGanar, 400, 300, 0, 1); // Dibuja el fondo de ganar
				if(estadoActual == EstadoJuego.JUGANDO) { //Se guarda el tiempo en el que el jugador ganó
					this.tiempoJuegoTerminado = this.entor.tiempo();
					estadoActual = EstadoJuego.GANADO;
				}
				this.entor.cambiarFont("italy", 20, Color.white); 
				tiempo(330,500,this.tiempoJuegoTerminado, Color.white);// Se imprime el tiempo en el que el jugador ganó
				}
			}
		}
	}
	

	
	private void nuevoAtaque() { //Genera un nuevo ataque en direccion a donde el jugador esta mirando.
			if(this.ataque == null) {
				this.ataque = new Ataque(jugador,entor);
				return;
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
	
	
	// esta funcion sirve si y solo si la tortuga esta apoyada en una isla
	public Isla islaDeLaTortuga(Tortuga t, Isla[] i) { 
		Isla is = islas[1];
		for(Isla islas: i) {
			if(estaEnLaIsla(t,islas)) {
				is =  islas;
			}
		}
		return is;
	}
	
	//Dada una tortuga y una isla, si los borden de la torutga estan dentro de la isla la funcion retorna true. En caso contrario, retorna false
	private boolean estaEnLaIsla(Tortuga t, Isla i) {
		if(Math.abs(t.getBorderInferior()-i.getBorderSuperior())<1 && (t.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(t.getBorderDerecho()>i.getBorderIzquierdo())) {
			return true;
		}
		return false;
	}

	//Dada una tortuga y una isla, retorna true si el borde derecho de la tortuga es mayor al borde izquied. False en caso contrario.
	private boolean estaAlBordeDerecho(Tortuga t, Isla islas2) {
		return (t.getBorderDerecho() >islas2.getBorderDerecho());
	}
	
	//dada una tortuga y una isla, retorna true si el borde izquierdo de la tortuga es menor al borde izquierdo de la isla. False en caso contrario.
	private boolean estaAlBordeIzquierdo(Tortuga t, Isla islas2) {
		return (t.getBorderIzquierdo() <islas2.getBorderIzquierdo());
	}
	
	//Dada una tortuga y la isla en la que se encuentra la tortuga, cuando la tortuga llegue al borde, esta cambiará de dirección.
	private void tortugasRebote(Tortuga tor, Isla islaDeLaTortuga) {
		if(estaAlBordeDerecho(tor, islaDeLaTortuga)) { // si esta al borde derecho da la vuelta a la izquierda
			tor.direccion = false;
		}
		if(estaAlBordeIzquierdo(tor, islaDeLaTortuga)) { // si esta al borde izquierdo da la vuelta a la derecha
			tor.direccion = true;
		}
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
		return Math.abs(j.getBorderInferior()-i.getBorderSuperior())<4 && (j.getBorderIzquierdo()<i.getBorderDerecho()) && 
				(j.getBorderDerecho()>i.getBorderIzquierdo());
	}
	

	
	private void nuevoGnomo(Gnomo[] gnom) { //rellena los espaciocios nulos del arreglo de tortugas para crear una nueva tortuga
        if(entor.numeroDeTick()%300== 0 || entor.numeroDeTick() == 100) {
            for(int i= 0; i<gnom.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
                if(gnom[i] == null) {
                    gnom[i] = new Gnomo(entor);
                    return;
                }
            }
        }
    }
	
	//Dado una lista de bombas y un gnomo. La funcion recorre la lista de bombas y mientras no sean nulos, si la bomba colisiona con un gnomo retorna true. En caso contrario false
	public boolean colisionBombaGnomo(Bomba[] bomba, Gnomo gnomo) {
		for(Bomba b:bombas) {
			if((gnomo!= null && b !=null) && ( (gnomo.getBorderDerecho() > b.getBorderIzquierdo() && gnomo.getBorderIzquierdo() <b.getBorderIzquierdo() ) 
	                || (gnomo.getBorderIzquierdo() <b.getBorderDerecho() &&
	                        gnomo.getBorderDerecho() > b.getBorderDerecho())) && 
	                        gnomo.getBorderInferior() >b.getBorderSuperior() && gnomo.getBorderSuperior()<b.getBorderInferior() ){
	                    return true;
	                }
	        }
		 return false;
	}

	//Dada una lista de tortugas y un objeto gnomo, se recorre la lista de tortugas y si un objeto tortuga choca con el gnomo, retorna true, sino false.
    public boolean tortugaColicionGnomo(Tortuga[] tortugas, Gnomo gnomo) {
		for(Tortuga t:tortugas) {
			if((gnomo!= null && t !=null) && ( (gnomo.getBorderDerecho() > t.getBorderIzquierdo() && gnomo.getBorderIzquierdo() <t.getBorderIzquierdo() ) 
	                || (gnomo.getBorderIzquierdo() <t.getBorderDerecho() &&
	                        gnomo.getBorderDerecho() > t.getBorderDerecho())) && 
	                        gnomo.getBorderInferior() >t.getBorderSuperior() && gnomo.getBorderSuperior()<t.getBorderInferior() ){
	                    return true;
	                }
	        }
		 return false;
	}
    
    //Dado un objeto jugador y un objeto gnomo, si el jugador y el gnomo colisionan la funcion devuelve true, sino retorna false
	public boolean jugadorColicionGnomo(Jugador jugador, Gnomo gnomo) {
        if((gnomo != null && jugador != null) && ( (gnomo.getBorderDerecho() > jugador.getBorderIzquierdo() && gnomo.getBorderIzquierdo() <jugador.getBorderIzquierdo() ) 
                || (gnomo.getBorderIzquierdo() <jugador.getBorderDerecho() &&
                gnomo.getBorderDerecho() > jugador.getBorderDerecho())) && 
                gnomo.getBorderInferior() >jugador.getBorderSuperior() && gnomo.getBorderSuperior()<jugador.getBorderInferior()){
            return true;
        }

    return false;
}
    //retorna true si la gnomo esta pisando la isla
        public boolean estaApoyado(Gnomo g, Isla i) {
            return Math.abs(g.getBorderInferior()-i.getBorderSuperior())<1 && (g.getBorderIzquierdo()<i.getBorderDerecho()) && 
                    (g.getBorderDerecho()>i.getBorderIzquierdo());
        }

        //retorna true si el gnomo se encuentra pisando una isla del arreglo, retorna false de lo contrario
        public boolean estaApoyadoGnomo(Gnomo n, Isla[] i) {
            for(Isla islas: i) {
                if(estaApoyado(n,islas)) { 
                    return true;
                }
            }
            return false;
        }
	
        
     // recorre un array de objetos bomba, y agrega una bomba en la posicion de la torutuga
	private void tirarBomba(Bomba[] bom, Tortuga tor) { 
		for(int i= 0; i<bom.length;i++) { 
			if(bom[i] == null) {
				bom[i] = new Bomba(tor,entor);
				return;
			}
		}
	}
	
	//Recorre un array de bombas no nulas, si el escudo colisiona con una bomba del array de bombas, se resta un uso al escudo y la bomba se vuelve null
	private void proteccionDelEscudo(Escudo es, Bomba[] bom) { 
		if(escudo.getUsos() != 0) {
			for(int i = 0; i< bom.length; i++) {
				if (bom[i] != null) {
					if(((escudo.getBorderDerecho() > bom[i].getBorderIzquierdo() && escudo.getBorderIzquierdo() < bom[i].getBorderIzquierdo()) || 
							(escudo.getBorderIzquierdo() < bom[i].getBorderDerecho() && escudo.getBorderDerecho() > bom[i].getBorderDerecho())) && escudo.getBorderInferior() >bom[i].getBorderSuperior() && escudo.getBorderSuperior()<bom[i].getBorderInferior()) {
						bom[i] = null;
						escudo.escudoUsado();
						return;
					}
				}
			}
		}
	}

	//Recorre un array de bombas y cuando el ataque choca con un objeto bomba de un array de bombas, el objeto bomba en particular y el ataque se vuelven null.
	private void colisionFuegoBomba(Ataque a, Bomba[] b) { 
		if(a != null) {
			for (int j = 0; j<b.length;j++) {
				if(b[j] !=null) {
					if(((a.getBorderDerecho() > b[j].getBorderIzquierdo() && a.getBorderIzquierdo() <b[j].getBorderIzquierdo() ) 
							|| (a.getBorderIzquierdo() <b[j].getBorderDerecho() &&
							a.getBorderDerecho() > b[j].getBorderDerecho())) && a.getBorderInferior() >b[j].getBorderSuperior() && a.getBorderSuperior()<b[j].getBorderInferior()){
						this.ataque = null;
						b[j] = null;
						return;
						}
					}
				}
			}
	}

	
	//Recorre un array de bombas y cuando un objeto bomba de un array de bombas colisiona con el jugador devuelve true, si no, false
	private boolean colisionBombaJugador(Bomba[] bomba, Jugador jugador) { 		
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

	//recorre un array de tortugas, si el jugador colisiona con una tortuga retorna true, si no false
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


	// Cuando una de las bombas de un array de bombas sale de la pantalla este objeto bomba se vuelve null
	private void bombaFueraDePantalla(Bomba[] bom) { 
		for(int i= 0; i<bom.length;i++) { 
			if(bom[i] != null && (bom[i].getBorderIzquierdo()> this.entor.ancho() || bom[i].getBorderDerecho() <0)) {
				bom[i] = null;
			} 
		}
	}
		
	//Cuando un ataque sale de la pantalla este se vuelve null
	private void ataqueFueraDePantalla() { 
		if(this.ataque != null && (this.ataque.getBorderIzquierdo()> this.entor.ancho() || this.ataque.getBorderDerecho() <0)) {
			this.ataque = null;
			} 
	}
		
	//si una bola de fuego choca con una tortuga, estos desaparecen, osea, se vuelven null
	private void tortugaMuere(Tortuga[] t, Ataque ata) { 
		for(int j = 0; j<t.length;j++) {
				if(t[j] !=null) {
					if(ata != null && ( (ata.getBorderDerecho() > t[j].getBorderIzquierdo() && ata.getBorderIzquierdo() <t[j].getBorderIzquierdo() ) 
							|| (ata.getBorderIzquierdo() <t[j].getBorderDerecho() &&
							ata.getBorderDerecho() > t[j].getBorderDerecho())) && 
							ata.getBorderInferior() >=t[j].getBorderSuperior() && ata.getBorderSuperior()<=t[j].getBorderInferior() ) {
						this.ataque= null;
						t[j]=null;
						this.eliminados +=1;
						return;
				}
			}
		}
	}
	
	//A partir de la posicion de x e y imprime el timepo pasado como parametros en minutos y segundos con el color dado
	public void tiempo(int posicionX, int posicionY, int tiempo, Color col) { 
		this.entor.cambiarFont("italy", 20, col);
		int tiempoSegundos = (tiempo/1000)%60;
		int tiempoMinutos = (tiempo/60000);
		String xTiempo = " "+ tiempoMinutos + ":" + tiempoSegundos;
		this.entor.escribirTexto("TIEMPO:" + xTiempo, posicionX + 10, posicionY);
	}
	
	private void gnomosPerdidos(int posicionX) { //A partir de la posicion X como parametro, imprime en pantalla los gnomos perdidos 
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.perdidos;
		this.entor.escribirTexto("PERDIDOS:" + xTiempo, posicionX + 10, 25);
	}

	private void gnomosSalvados(int posicionX) { //A partir de la posicion X como parametro, imprime en pantalla los gnomos salvados 
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.salvados;
		this.entor.escribirTexto("SALVADOS:" + xTiempo, posicionX + 10, 25);
	}
	
	private void tortugasEliminadas(int posicionX) { //A partir de la posicion X como parametro, imprime en pantalla la cantidad de tortugas eliminadas
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.eliminados;
		this.entor.escribirTexto("ELIMINADOS:" + xTiempo, posicionX + 10, 25);
	}
	
	private void mostrarUsosEscudo() { //imprime en pantalla la cantidad de escudos que quedan por usar
		this.entor.cambiarFont("italy", 20, Color.black);
		this.entor.escribirTexto("ESCUDOS: " + this.escudo.getUsos(), 10, 575);
	}
	private void mostrarVida() { //Imprime en pantalla la cantidad de vidas que tiene el jugador
		this.entor.cambiarFont("italy", 20, Color.black);
		this.entor.escribirTexto("VIDAS: " + this.jugador.getVida(), 210, 575);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}


	
}
