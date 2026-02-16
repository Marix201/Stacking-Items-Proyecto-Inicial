import shapes.*;

/**
 * Simulador de torre de tazas
 * 
 * @author Hernandez-Tovar
 * @version 1- Ciclo 1
 */
public class Cup { //Modela la taza 
    private int cupNumber; //Índice de la taza
    private int heightInCm; //Altura de la taza en Cm
    private String cupColor; //Color de la taza
    private int posX; //Posición x
    private int posY; //Posición en y
    private Rectangle shape; //Representación gráfica
    private boolean isShowing; //Visible o invisible
    
    /**
     * Constructor de Cup
     */
    public Cup(int number, String color) { //Constructor
        this.cupNumber = number; //Asigna el número
        this.cupColor = color; //Asigna el color
        this.posX = 0; //Posicion x
        this.posY = 0; //Posición y
        this.isShowing = false;
        
        // Calcular altura usando método diferente
        this.heightInCm = calculateHeight(number);
        
        // Crear forma visual
        createShape();
    }
    
    /**
     * Crear la forma visual de la taza
     */
    private void createShape() { // Crea el rectángulo visual
        shape = new Rectangle();
        shape.changeColor(cupColor);
        
        // Fórmula diferente para el ancho
        int w = 25 + (cupNumber * 12);
        int h = heightInCm * 10; //Multiplica por 10 para que 1 cm lógico = 10 píxeles visuales.
        
        shape.changeSize(h, w);
    }
    
    /**
     * Calcular altura: 2^(n-1) con loop en lugar de Math.pow
     */
    private int calculateHeight(int n) {
        int h = 1;
        int exponent = n - 1;
        
        for (int i = 0; i < exponent; i++) {
            h = h * 2;
        }
        
        return h;
    }
    
    /**
     * Mostrar la taza
     */
    public void makeVisible() {
        if (isShowing) { //Verifica
            return;
        }
        shape.makeVisible();
        isShowing = true;
        //Mantiene la coherencia entre modelo y vista
    }
    
    /**
     * Ocultar la taza
     */
    public void makeInvisible() {
        if (!isShowing) {
            return;
        }
        shape.makeInvisible();
        isShowing = false;
    }
    
    /**
     * Mover a una posición específica
     */
    public void moveTo(int x, int y) {
        if (isShowing) {
            // Calcular cuánto mover desde la posición actual
            int deltaX = x - posX;
            int deltaY = y - posY;
            // Movimiento de la taza
            shape.moveHorizontal(deltaX);
            shape.moveVertical(deltaY);
        }
        
        // Actualizar posición guardada
        this.posX = x;
        this.posY = y;
    }
    
    /**
     * Obtener altura en cm
     */
    public int getHeight() {
        return heightInCm; //Devuelve la altura en cm
    }
    
    /**
     * Obtener número de la taza
     */
    public int getIndex() {
        return cupNumber;
    }
    
    /**
     * Obtener color
     */
    public String getColor() {
        return cupColor;
    }
    
    /**
     * Verificar si está visible
     */
    public boolean isVisible() {
        return isShowing;
    }
}