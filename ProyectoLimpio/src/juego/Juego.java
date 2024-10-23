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
		this.fondoCielo = entorno.Herramientas.cargarImagen("cieloPrueba.png");
		this.fondoPerdiste = entorno.Herramientas.cargarImagen("fondoPerdiste.png");
		this.casaImagen = entorno.Herramientas.cargarImagen("el_crustaceo_cascarudo_casa.png");
		this.fondoGanar = entorno.Herramientas.cargarImagen("fondoGanar.jpg");
		this.escudo = new Escudo(this.entor,this.jugador,3);
		this.perdidos = 0;
		this.salvados = 0;
		this.eliminados = 0;
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
		
		//si el jugador es null se tomara como que muerió
		if(jugador.getVida()==0 || (this.perdidos== 10)) {
			this.entor.dibujarImagen(fondoPerdiste, 420, 280, 0, 1.5);
			this.entor.cambiarFont("italy", 20, Color.white);
			String xTiempo = " "+ this.salvados;
			this.entor.escribirTexto("SALVADOS:" + xTiempo, 330, 425);
		//	tiempo(330,500,Color.WHITE);
		} else {
			
		if(jugador != null && this.salvados !=20) {
			this.entor.dibujarImagen(this.fondoCielo, 0, 150, 0, 1); //dibuja el fondo de cielo azul
			for(Isla is: this.islas) { //dibuja las islas en pantalla
				is.dibujar();
			}
			
			this.entor.dibujarImagen(casaImagen, 395, 40, 0, 0.17);
		
			nuevaTortuga(tortugas); // genera una nueva tortuga hasta que llegue a 10 tortugas vivas
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
			
			nuevoGnomo(gnomo);
            for(Gnomo gno: this.gnomo) {
                if(gno != null) {
                    gno.dibujar();
                    gno.gravedad();
                    double random =  Math.random();
                    boolean gnomoApoyadoEnIsla = estaApoyadoGnomo(gno, islas);
                    if(gnomoApoyadoEnIsla && (gno.apoyado == false)) {
                        gno.apoyado = true;
                        if(random > 0.5) {
                            gno.direccion = true;
                        } else {
                            gno.direccion = false;
                        }
                        } 
                    if(!gnomoApoyadoEnIsla) {
                            gno.apoyado = false;
                        }

                    if(gnomoApoyadoEnIsla) {
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
			
			jugador.dibujar();
			if(entor.estaPresionada(entor.TECLA_IZQUIERDA)) {
				jugador.moverIzquierda();
			}
			if(entor.estaPresionada(entor.TECLA_DERECHA)) {
				jugador.moverDerecha();
			}
			
			if(entor.estaPresionada(entor.TECLA_ABAJO) && escudo.getUsos() !=0) {
				escudo.proteccionEscudo(jugador.getDireccion(), jugador);
				escudo.dibujar();
				proteccionDelEscudo(escudo, bombas);
			} 
			
			for(int i =0; i<gnomo.length;i++) {
                if(gnomo[i]!=null &&(gnomo[i].seCayoGnomo() || tortugaColicionGnomo(tortugas,gnomo[i])  || colisionBombaGnomo(bombas,gnomo[i]))) {
                    gnomo[i] = null;
                    this.perdidos +=1;
                } else {
                	  if(gnomo[i]!=null && (jugadorColicionGnomo(jugador,gnomo[i]) && gnomo[i].getY()>350)) {
                          gnomo[i]=null;
                          this.salvados +=1;
                          double random =Math.random();
                          if (random <0.2) {
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
			
			tiempo(this.posiciones[0], 25, Color.black);
			gnomosPerdidos(this.posiciones[1]);
			gnomosSalvados(this.posiciones[2]);
			tortugasEliminadas(this.posiciones[3]);
			mostrarUsosEscudo();
			mostrarVida();
			
			
		} else {
			if(this.salvados>=20) {	
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
	
	public void tiempo(int posicionX, int i, Color col) {
		this.entor.cambiarFont("italy", 20, col);
		int tiempoSegundos = ((this.entor.tiempo())/1000)%60;
		int tiempoMinutos = (this.entor.tiempo())/60000;
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
