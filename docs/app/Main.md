# MAIN

## FUNCIÓN

Clase que ejecutará el flujo del programa.

## MÉTODOS

- Método donde se desarrolla todo el flujo del programa.

```java
public void main() {
		IGame game = new Game();

		game.startConfiguration();

	}
```

- Método que llama al constructor del método principal.

```java
public static void main(String[] args) {
		new Main().main();
	}
```

## RELACIONES

El Main es usado por Game para todo el flujo del juego.

[Volver al Índice](../indiceClases.md)

[Regresar al README](../README.md)