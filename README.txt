|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

 Validador de convenciones de código en proyectos de Google para el lenguaje java       
                                                                                    
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

Realizado por:

----------------------------------------------------------------------------------------------------------------

- Andrés Felipe Sánchez Sánchez
- Jaime Eduardo Estupiñan Bedoya
- Juan Pablo Betancourt Maldonado

----------------------------------------------------------------------------------------------------------------

-------------------
1 - Pre requisitos:
-------------------

Sólo si aún no cumple con los pre-requisitos, debe instalar y configurar las siguientes herramientas:

- Java JDK: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

- Entorno de desarrollo (IDE) IntelliJ IDEA: https://www.jetbrains.com/idea/download/ 	

- ANTLR: https://www.antlr.org/

------------------------
2 - Abrir la Aplicación:
------------------------

Una vez descargado el zip con el proyecto lo que se tiene que hacer es desde IntelliJ IDEA se tiene que abrir 
la carpeta llamada proyeto_lenguajes y se tiene que abrir como proyecto de IntelliJ IDEA para que el funcionamiento 
sea el correcto.

-----------------------------------
3 - Instalación del plugin de ANTLR
-----------------------------------

En el menú de archivo -> configuración, seleccione plugins, y en el buscador escriba ANTLR.

Instale el plugin “ANTLR v4 grammar plugin”

-------------------------------------------
4 - Configuración inicial de la aplicación:
-------------------------------------------

A continuación haga clic derecho sobre el nombre del proyecto y vaya a “Open module settings”.

En la sección Dependencies, haga click en el símbolo más (+) ubicado al lado derecho, seleccione la opción “JARs or directories”, 
y finalmente, busque el archivo de antlr-4.7.X-complete.jar que descargó previamente de la página web de ANTLR. Por último, haga clic en Apply. 
De esta manera se incluyen las librerías necesarias para el funcionamiento de ANTLR. Click en OK.

Nuevamente haga clic derecho sobre el nombre del proyecto y vaya a “Open module settings”, 
vaya a la sección sources. Aquí debe seleccionar la carpeta gen y luego hacer clic en el botón sources de la zona “Mark as:” 
en la parte superior. De esta manera, estamos agregando esta carpeta como una ruta de archivos de código fuente para Java.

-------------------------------
5 - Ejecución de la Aplicación:
-------------------------------

Para poder ejecutar la aplicación lo primero que se tiene que hacer es crear archivo llamado in.java en el directorio input que está en el proyecto. 
Este archivo va a ser al que se le realice la validación de convenciones de código en proyectos de Google.

Para que IntelliJ use este archivo para la validación, debemos configurarlo como un parámetro en la configuración. Para esto, vaya al menú “Run” -> “Edit configurations…”. 
En la opción “Program arguments:” escriba input/in.java.

Después de correr la aplicación se va a generar un archivo llamado output.txt. Si no hubo ninguna violacion en las convenciones de Google este archivo simplemente 
se genera pero su contenido es vacío, en el caso de que si se hubieran violado convenciones de Google en este archivo se va a mostrar cada regla que se incumple de la siguiente manera:

<linea:1> violacion la regla 4.8.5, se tienen varias anotaciones en la misma linea

--------------------
6 - Ejemplos de uso:
--------------------

Los ejemplos de uso se puede encontrar dentro de la siguiente direccioninput\Ejemplos de uso
En esta se encuentra clasificados los ejemplos con la regla que mas prevalece en cada uno de ellos en los archivos .java, y su correspondiente salida se encuenta en los archivos con el mismo nombre pero con extension .txt, eg la regla 45.java tiene como salida 45 salida.txt