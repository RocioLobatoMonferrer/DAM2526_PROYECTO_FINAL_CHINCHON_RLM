package dominio;

/**
 * Clase que se encarga de crear las entidades
 */

public class FactoryEntity {

	/**
	 * Método que crea a las entidades dependiendo de su tipo
	 * 
	 * @param type     Tipo de la entidad. Tenemos: CPU y Entidad
	 * @param nickname Apodo de la entidad
	 * @return Instancia de una Entidad o una CPU
	 */

	public Entity createEntity(boolean type, String nickname) {
		if (type) {
			return new Entity(nickname);
		} else {
			return new Cpu(nickname);
		}
	}
}
