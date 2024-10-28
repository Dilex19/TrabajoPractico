package juego;


import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private enum EstadoJuego { MENU, JUGANDO, PERDIDO, GANADO }
	private EstadoJuego estadoActual = EstadoJuego.MENU;
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
	private int salvadosNecesarios;
	private int gnomosPerdidosMaximos;
	// Variables y métodos propios de cada grupo
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
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
			this.entor.dibujarImagen(fondoPerdiste, 420, 280, 0, 1.5);
			this.entor.cambiarFont("italy", 20, Color.white);
			String xTiempo = " "+ this.salvados;
			this.entor.escribirTexto("SALVADOS:" + xTiempo, 330, 425);
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
			
			for(int i =0; i<gnomo.length;i++) { 
				//Si el gnomo no es nulo y se sale de la pantalla, o colisiona con una tortuga o colisiona con una bomba, este gnomo se vuelve nulo y se suma uno al contador de gnomos perdidos
                if(gnomo[i]!=null &&(gnomo[i].seCayoGnomo() || tortugaColicionGnomo(tortugas,gnomo[i])  || colisionBombaGnomo(bombas,gnomo[i]))) {
                    gnomo[i] = null;
                    this.perdidos +=1;
                } else { //sino, si colisiona con el jugador Y está por debajo de la tercera isla, el gnomo se vuelve nulo, suma 1 al contador de SALVADOS
                	  if(gnomo[i]!=null && (jugadorColicionGnomo(jugador,gnomo[i]) && gnomo[i].getY()>350)) {
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
			if(estaApoyado(jugador, islas)) {
				this.jugador.apoyado = true;
			} else {
				this.jugador.apoyado = false;
			}
			
			colisionFuegoBomba(ataque,bombas);
			
			if(jugador.seCayoJugador() || tortugaColicionJugador(tortugas,jugador) || colisionBombaJugador(bombas,jugador)) {
				jugador.daño();
				jugador.setX(270.0);
				jugador.setY(455.0);
				
			} 
			
			tiempo(this.posiciones[0], 25,this.entor.tiempo()-this.tiempoMenu, Color.black);
			gnomosPerdidos(this.posiciones[1]);
			gnomosSalvados(this.posiciones[2]);
			tortugasEliminadas(this.posiciones[3]);
			mostrarUsosEscudo();
			mostrarVida();
			
			
		} else {
			if(this.salvados>=this.salvadosNecesarios) {	
				this.entor.dibujarImagen(fondoGanar, 400, 300, 0, 1);
				}
			}
		}
		
//		//crea de nuevo al jugador para no tener que reiniciar el juego
//		if(jugador==null && entor.estaPresionada(entor.TECLA_ESPACIO)){
//			jugador=new Jugador(270.0,455.0, entor);
//		}
//	
	}
	
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

	private boolean tortugaColicionJugador(Tortuga[] tortugas, Jugador jugador) { // si el jugador choca con una tortuga retorna true 
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

	private void bombaFueraDePantalla(Bomba[] bom) {
		for(int i= 0; i<bom.length;i++) { //Tratar de usar el otro tipo de FOR (no me salio)
			if(bom[i] != null && (bom[i].getBorderIzquierdo()> this.entor.ancho() || bom[i].getBorderDerecho() <0)) {
				bom[i] = null;
			} 
		}
	}
		
	private void ataqueFueraDePantalla() {
		//Tratar de usar el otro tipo de FOR (no me salio)
		if(this.ataque != null && (this.ataque.getBorderIzquierdo()> this.entor.ancho() || this.ataque.getBorderDerecho() <0)) {
			this.ataque = null;
			} 
	}
		
	private void tortugaMuere(Tortuga[] t, Ataque ata) { //si una bola de fuego choca con una tortuga, estos desaparecen, osea, se vuelven null
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
	
	private void nuevoAtaque() {
		//Tratar de usar el otro tipo de FOR (no me salio)
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
		return Math.abs(j.getBorderInferior()-i.getBorderSuperior())<4 && (j.getBorderIzquierdo()<i.getBorderDerecho()) && 
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
	public boolean jugadorColicionGnomo(Jugador jugador, Gnomo gnomo) {
        if((gnomo != null && jugador != null) && ( (gnomo.getBorderDerecho() > jugador.getBorderIzquierdo() && gnomo.getBorderIzquierdo() <jugador.getBorderIzquierdo() ) 
                || (gnomo.getBorderIzquierdo() <jugador.getBorderDerecho() &&
                gnomo.getBorderDerecho() > jugador.getBorderDerecho())) && 
                gnomo.getBorderInferior() >jugador.getBorderSuperior() && gnomo.getBorderSuperior()<jugador.getBorderInferior() ){
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

        public Isla islaDelGnomo(Gnomo g, Isla[] i) { // esta funcion sirve si y solo si el gnomo esta apoyada en una isla
            Isla is = islas[1];
            for(Isla islas: i) {
                if(estaEnLaIsla(g,islas)) {
                    is =  islas;
                }
            }
            return is;
        }

        private boolean estaEnLaIsla(Gnomo g, Isla i) {
            if(Math.abs(g.getBorderInferior()-i.getBorderSuperior())<1 && (g.getBorderIzquierdo()<i.getBorderDerecho()) && 
                    (g.getBorderDerecho()>i.getBorderIzquierdo())) {
                return true;
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
	
	public void tiempo(int posicionX, int i, int tiempo, Color col) {
		this.entor.cambiarFont("italy", 20, col);
		int tiempoSegundos = (tiempo/1000)%60;
		int tiempoMinutos = (tiempo/60000);
		String xTiempo = " "+ tiempoMinutos + ":" + tiempoSegundos;
		this.entor.escribirTexto("TIEMPO:" + xTiempo, posicionX + 10, i);
	}
	
	private void gnomosPerdidos(int posicionX) {
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.perdidos;
		this.entor.escribirTexto("PERDIDOS:" + xTiempo, posicionX + 10, 25);
	}

	private void gnomosSalvados(int posicionX) {
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.salvados;
		this.entor.escribirTexto("SALVADOS:" + xTiempo, posicionX + 10, 25);
	}
	
	private void tortugasEliminadas(int posicionX) {
		this.entor.cambiarFont("italy", 20, Color.black);
		String xTiempo = " "+ this.eliminados;
		this.entor.escribirTexto("ELIMINADOS:" + xTiempo, posicionX + 10, 25);
	}
	
	private void mostrarUsosEscudo() {
		this.entor.cambiarFont("italy", 20, Color.black);
		this.entor.escribirTexto("ESCUDOS: " + this.escudo.getUsos(), 10, 575);
	}
	private void mostrarVida() {
		this.entor.cambiarFont("italy", 20, Color.black);
		this.entor.escribirTexto("VIDAS: " + this.jugador.getVida(), 210, 575);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}


	
}
