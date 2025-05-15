package nutricion;

import java.util.List;

public class MenuDieta {
    private final List<String> desayuno;
    private final List<String> comida;
    private final List<String> merienda;
    private final int kcal;

    public MenuDieta(List<String> desayuno, List<String> comida, List<String> merienda, int kcal) {
        this.desayuno = desayuno;
        this.comida = comida;
        this.merienda = merienda;
        this.kcal = kcal;
    }

    public List<String> getDesayuno()
    {
        return desayuno;
    }

    public List<String> getComida()
    {
        return comida;
    }
    
    public List<String> getMerienda()
    {
        return merienda;
    }
    
    @Override
    public String toString() {
        return "Desayuno: " + desayuno + "\n" +
               "Comida: " + comida + "\n" +
               "Merienda: " + merienda + "\n" +
               "Total Kcalorías: " + kcal;
    }
}