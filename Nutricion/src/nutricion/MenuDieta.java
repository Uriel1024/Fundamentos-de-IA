package nutricion;

import java.util.List;

public class MenuDieta {
    private final List<String> desayuno;
    private final List<String> almuerzo;
    private final List<String> comida;
    private final List<String> merienda;
    private final List<String> cena;
    private final int kcal;

    public MenuDieta(List<String> desayuno, List<String> comida, List<String> merienda, List<String> almuerzo, List<String> cena, int kcal) {
        this.desayuno = desayuno;
        this.almuerzo = almuerzo;
        this.comida = comida;
        this.merienda = merienda;
        this.cena = cena;
        this.kcal = kcal;
    }

    public List<String> getDesayuno()
    {
        return desayuno;
    }

    public List<String> getAlmuerzo() {
        return almuerzo;
    }

    public List<String> getComida()
    {
        return comida;
    }
    
    public List<String> getMerienda()
    {
        return merienda;
    }

    public List <String> getCena() {
        return cena;
    }
    @Override
    public String toString() {
        return "Desayuno: " + desayuno + "\n" +
                "Almuerzo: " + almuerzo + "\n" +
               "Comida: " + comida + "\n" +
               "Merienda: " + merienda + "\n" +
                "Cena: " + cena + "\n" +
               "Total Kcalorías: " + kcal;
    }
}