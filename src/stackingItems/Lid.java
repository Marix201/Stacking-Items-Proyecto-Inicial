import shapes.*;

/**
 * Simulador de torre de tazas
 * 
 * @author Hernandez-Tovar
 * @version 1- Ciclo 1
 */
public class Lid {
    private int associatedCupNumber; //índice de la taza ala que pertenece la tapa
    private String lidColor; //Color de la tapa(mismos de la taza)
    private int xPosition; //Posisición x actual de la tapa 
    private int yPosition; //Posisición y actual de la tapa 
    private Rectangle visualShape; // Representación visible
    private boolean showing; // Visible o invisible
    private final int LID_HEIGHT = 1;  // Constante porque las tapas miden 1cm
    
    /**
     * Constructor
     */
    public Lid(int cupNumber, String color) {
        //Creación de la tapa
        this.associatedCupNumber = cupNumber; //Asocia la tapa a una taza
        this.lidColor = color; //Asigna el color a la tapa
        this.xPosition = 0; //Posición inicial
        this.yPosition = 0; //Posición inicial
        this.showing = false; //Empieza visible por defecto
        
        initializeVisual(); //Lógica gráfiza del constructor
    }
    
    /**
     * Inicializar representación visual
     */
    private void initializeVisual() {
        //Parte gráfica
        visualShape = new Rectangle(); //Crea un ractángulo
        visualShape.changeColor(lidColor); //Asigna color
        
        // Ancho igual al de su taza
        int width = 25 + (associatedCupNumber * 12);
        
        visualShape.changeSize(10, width);
    }
    
    /**
     * Hacer visible la tapa
     */
    public void makeVisible() {
        if (showing) {
            return;
        } //Evita volverla visible solo si ya lo está
        visualShape.makeVisible();
        showing = true; //Actualiza el estado
    }
    
    /**
     * Hacer invisible la tapa
     */
    public void makeInvisible() {
        if (!showing) { //Valida el estado
            return;
        }
        visualShape.makeInvisible();
        showing = false; //Oculta y actualiza el estado
    }
    
    /**
     * Mover a posición específica
     */
    public void moveTo(int x, int y) { //Permite colocar la tapa en cierta posición
        if (showing) { //Solo la mueve si está visible
            //Calculo para saber cuánto debe moverse
            int deltaX = x - xPosition;
            int deltaY = y - yPosition;
            
            visualShape.moveHorizontal(deltaX);
            visualShape.moveVertical(deltaY);
        }
        //Seguarda la posición lógica aunque no esté visible
        this.xPosition = x;
        this.yPosition = y;
    }
    
    /**
     * Obtener altura (siempre 1 cm)
     */
    public int getHeight() {
        return LID_HEIGHT;
    }
    
    /**
     * Obtener número de taza asociada
     */
    public int getCupIndex() {
        return associatedCupNumber;
    }
    
    /**
     * Obtener color
     */
    public String getColor() {
        return lidColor;
    }
    
    /**
     * Verificar si está visible
     */
    public boolean isVisible() {
        return showing;
    }
}