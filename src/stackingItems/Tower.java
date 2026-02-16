import shapes.*;
import java.util.ArrayList;

/**
 * Simulador de torre de tazas
 * 
 * @author Hernandez- Tovar
 * @version 1- Ciclo 1
 */
public class Tower {
    private ArrayList<Cup> cupArray; //Lista separada
    private ArrayList<Lid> lidArray; //Lista separada
    private int towerWidth; // ancho de la torre
    private int maxHeightCm; // altura máxima en cm
    private boolean isDisplayed; //Torre visible o invisible               
    private boolean lastOpSuccess; //Sabemos si la última operación fue válida o no            
    private ArrayList<Rectangle> scaleLines; //Guarda las líneas de la escala visual
    private final int BASE_Y = 450; // coordenada en Y donde comienza la torre en la pantalla
    private final int CENTER_X = 220; // coordenada en X donde se centra la torre en la pantalla
    
    /**
     * Constructor
     */
    public Tower(int width, int maxHeight) {
        this.towerWidth = width;
        this.maxHeightCm = maxHeight;
        this.isDisplayed = false;
        this.lastOpSuccess = true;
        this.cupArray = new ArrayList<Cup>();
        this.lidArray = new ArrayList<Lid>();
        this.scaleLines = new ArrayList<Rectangle>();
    }
    
    /**
     * Hacer visible la torre
     */
    public void makeVisible() {
        if (isDisplayed) {
            return;
        }
        
        isDisplayed = true;
        
        for (Cup cup : cupArray) {
            cup.makeVisible();
        }
        
        for (Lid lid : lidArray) {
            lid.makeVisible();
        }
        
        buildScale();
        
        System.out.println("Torre mostrada");
    }
    
    /**
     * Hacer invisible la torre
     */
    public void makeInvisible() {
        if (!isDisplayed) {
            return;
        }
        
        isDisplayed = false;
        
        for (Cup cup : cupArray) {
            cup.makeInvisible();
        }
        
        for (Lid lid : lidArray) {
            lid.makeInvisible();
        }
        
        hideScale();
        
        System.out.println("Torre ocultada");
    }
    
    // Métodos de taza
    
    /**
     * Añadir taza
     */
    public void pushCup(int i) { //Agrega una taza
        if (i < 1) { //Validación
            lastOpSuccess = false;
            displayError("Número de taza debe ser >= 1");
            return;
        }
        
        String color = getColorForNumber(i);
        Cup newCup = new Cup(i, color); //Crea una taza
        
        int yPos = computeNextYPosition();
        newCup.moveTo(CENTER_X, yPos); //Calcula la posición
        
        cupArray.add(newCup);
        
        if (isDisplayed) {
            newCup.makeVisible();
        }
        
        lastOpSuccess = true;
    }
    
    /**
     * Quitar taza superior
     */
    public void popCup() {
        if (cupArray.isEmpty()) {
            lastOpSuccess = false;
            displayError("No hay tazas");
            return;
        }
        
        Cup removed = cupArray.remove(cupArray.size() - 1);
        removed.makeInvisible();
        
        lastOpSuccess = true;
    }
    
    /**
     * Eliminar taza en posición
     */
    public void removeCup(int index) { //Eliminar taza por posición
        if (index < 0 || index >= cupArray.size()) {
            lastOpSuccess = false;
            displayError("Índice fuera de rango: " + index);
            return;
        }
        
        Cup removed = cupArray.remove(index);
        removed.makeInvisible();
        
        repositionAll();
        
        lastOpSuccess = true;
    }
    
    // Métodos de tapa
    
    /**
     * Añadir tapa
     */
    public void pushLid(int i) {
        if (i < 1) {
            lastOpSuccess = false;
            displayError("Número debe ser >= 1");
            return;
        }
        
        String color = getColorForNumber(i);
        Lid newLid = new Lid(i, color);
        
        int yPos = computeNextYPosition();
        newLid.moveTo(CENTER_X, yPos);
        
        lidArray.add(newLid);
        
        if (isDisplayed) {
            newLid.makeVisible();
        }
        
        lastOpSuccess = true;
    }
    
    
    /**
     * Quitar tapa superior
     */
    public void popLid() {
        if (lidArray.isEmpty()) {
            lastOpSuccess = false;
            displayError("No hay tapas");
            return;
        }
        
        Lid removed = lidArray.remove(lidArray.size() - 1);
        removed.makeInvisible();
        
        lastOpSuccess = true;
    }
    
    /**
     * Eliminar tapa en posición
     */
    public void removeLid(int index) {
        if (index < 0 || index >= lidArray.size()) {
            lastOpSuccess = false;
            displayError("Índice fuera de rango: " + index);
            return;
        }
        
        Lid removed = lidArray.remove(index);
        removed.makeInvisible();
        
        repositionAll();
        
        lastOpSuccess = true;
    }
    
    /**
     * Ordenar torre de mayor a menor
     */
    public void orderTower() {
        boolean wasVisible = isDisplayed;
        //Si está visible ocultar
        //Oculta antes de organizar
        if (wasVisible) { 
            makeInvisible();
        }
        
        bubbleSortCups(); //Ordena de forma decendente por altura
        bubbleSortLids(); //Ordena de forma descendente por número
        
        repositionAll();
        
        if (wasVisible) {
            makeVisible();
        }
        
        lastOpSuccess = true;
    }
    
    /**
     * Invertir orden
     */
    public void reverseTower() {
        boolean wasVisible = isDisplayed;
        
        if (wasVisible) {
            makeInvisible();
        }
        
        java.util.Collections.reverse(cupArray);
        java.util.Collections.reverse(lidArray);
        
        repositionAll();
        
        if (wasVisible) {
            makeVisible();
        }
        
        lastOpSuccess = true;
    }
    
    /**
     * Obtener altura total
     */
    public int height() { //Calcula altura total (requisito 1cm)
        int total = 0;
        
        for (Cup cup : cupArray) {
            total += cup.getHeight();
        }
        
        total += lidArray.size();
        
        return total;
    }
    
    /**
     * Obtener tazas con tapa
     */
    public int[] lidedCups() { //Número de tazas con tapa
        int[] result = new int[lidArray.size()];
        
        for (int i = 0; i < lidArray.size(); i++) {
            result[i] = lidArray.get(i).getCupIndex();
        }
        
        return result;
    }
    
    /**
     * Obtener todos los elementos
     */
    public String[][] stackingItems() {
        int totalItems = cupArray.size() + lidArray.size();
        String[][] items = new String[totalItems][2];
        
        int idx = 0;
        
        for (Cup cup : cupArray) { //tipo y número
            items[idx][0] = "cup";
            items[idx][1] = String.valueOf(cup.getIndex());
            idx++;
        }
        
        for (Lid lid : lidArray) { //Tipo y númer
            items[idx][0] = "lid";
            items[idx][1] = String.valueOf(lid.getCupIndex());
            idx++;
        }
        
        return items;
    }
    
    /**
     * Cerrar simulador
     */
    public void exit() {
        makeInvisible();
        
        cupArray.clear();
        lidArray.clear();
        
        System.out.println("Torre cerrada");
        lastOpSuccess = true;
    }
    
    /**
     * Verificar última operación
     */
    public boolean ok() {
        return lastOpSuccess;
    }
    
    // Métodos privados
    
    /**
     * Calcular posición Y para siguiente elemento
     */
    private int computeNextYPosition() {
        int y = BASE_Y;
        
        for (Cup cup : cupArray) {
            y -= cup.getHeight() * 10;
        }
        
        for (Lid lid : lidArray) {
            y -= 10;
        }
        
        return y;
    }
    
    /**
     * Reposicionar todos los elementos
     */
    private void repositionAll() {
        int y = BASE_Y;
        
        for (Cup cup : cupArray) {
            cup.moveTo(CENTER_X, y);
            y -= cup.getHeight() * 10;
        }
        
        for (Lid lid : lidArray) {
            lid.moveTo(CENTER_X, y);
            y -= 10;
        }
    }
    
    /**
     * Bubble sort para tazas (descendente - grandes abajo, pequeñas arriba)
     */
    private void bubbleSortCups() {
        for (int i = 0; i < cupArray.size() - 1; i++) {
            for (int j = 0; j < cupArray.size() - i - 1; j++) {
                if (cupArray.get(j).getHeight() < cupArray.get(j + 1).getHeight()) {
                    Cup temp = cupArray.get(j);
                    cupArray.set(j, cupArray.get(j + 1));
                    cupArray.set(j + 1, temp);
                }
            }
        }
    }
    
    /**
     * Bubble sort para tapas (descendente por número de taza)
     */
    private void bubbleSortLids() {
        for (int i = 0; i < lidArray.size() - 1; i++) {
            for (int j = 0; j < lidArray.size() - i - 1; j++) {
                if (lidArray.get(j).getCupIndex() < lidArray.get(j + 1).getCupIndex()) {
                    Lid temp = lidArray.get(j);
                    lidArray.set(j, lidArray.get(j + 1));
                    lidArray.set(j + 1, temp);
                }
            }
        }
    }
    
    /**
     * Construir escala de medición
     */
    private void buildScale() { //Marcas cada 1cm
        hideScale();
        
        if (!isDisplayed) {
            return;
        }
        
        int maxCm = Math.min(maxHeightCm, 50);
        
        for (int cm = 0; cm <= maxCm; cm++) {
            Rectangle line = new Rectangle();
            line.changeColor("black");
            line.changeSize(2, 12);
            
            line.moveHorizontal(50);
            line.moveVertical(BASE_Y - (cm * 10) - 50);
            line.makeVisible();
            
            scaleLines.add(line);
        }
    }
    
    /**
     * Ocultar escala
     */
    private void hideScale() {
        for (Rectangle line : scaleLines) {
            line.makeInvisible();
        }
        scaleLines.clear();
    }
    
    /**
     * Obtener color según número
     */
    private String getColorForNumber(int num) { //Tazas con colores diferentes
        String[] colors = {"blue", "green", "red", "yellow", 
                          "orange", "magenta", "black", "white"};
        return colors[(num - 1) % colors.length];
    }
    
    /**
     * Mostrar error
     */
    private void displayError(String msg) { //Notificar el error
        if (isDisplayed) {
            javax.swing.JOptionPane.showMessageDialog(null, 
                "ERROR: " + msg);
        }
    }
}