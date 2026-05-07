# FACTORYENTITY

## FUNCIÓN

Clase que se encarga de crear las entidades.

## MÉTODOS

- Método que crea a las entidades dependiendo de su tipo. Podemos encontrar los siguientes parametros:

	- type: Tipo de la entidad. Tenemos: CPU y Entidad.
	- nickname: Apodo de la entidad.

	Este método devuelve la instancia de una Entidad o una CPU.

```java
public Entity createEntity(boolean type, String nickname) {
		if (type) {
			return new Entity(nickname);
		} else {
			return new Cpu(nickname);
		}
	}
```

## RELACIONES

FactoryEntity utiliza a Entity o Cpu dependiendo de lo que se desee crear y a su vez es utilizado por Game para decidir cual creará.


[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)