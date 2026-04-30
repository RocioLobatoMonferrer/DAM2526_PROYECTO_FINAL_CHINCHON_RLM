package dominio;

/**
 * Clase que se encarga de la gestión de las CPUs
 */

public class Cpu extends Entity implements ICpu {

	/**
	 * Constructor de la clase que lo instancia
	 * 
	 * @param nickname Apodo de la CPU
	 */

	public Cpu(String nickname) {
		super(nickname);
	}

	// TODO: Elegir como va escoger las jugadas la IA

	/**
	 * @inheritDoc
	 */

	@Override
	public int choosePlay() {
		return 0;
	}

	/**
	 * @inheritDoc
	 */

	@Override
	public int chooseDiscard() {
		return 0;
	}

}
