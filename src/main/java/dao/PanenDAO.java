package dao;

import model.Panen;
import java.util.ArrayList;
import java.util.List;

public class PanenDAO {
    private static List<Panen> panenList = new ArrayList<>();

    public List<Panen> getAllPanen() {
        return panenList;
    }

    public boolean tambahPanen(Panen panen) {
        if (panen != null) {
            panenList.add(panen);
            return true;
        }
        return false;
    }

    public boolean updatePanen(String idPanen, Panen panenBaru) {
        for (int i = 0; i < panenList.size(); i++) {
            if (panenList.get(i).getIdPanen().equalsIgnoreCase(idPanen)) {
                panenList.set(i, panenBaru);
                return true;
            }
        }
        return false;
    }

    public boolean hapusPanen(String idPanen) {
        return panenList.removeIf(p -> p.getIdPanen().equalsIgnoreCase(idPanen));
    }

    public Panen cariPanenById(String idPanen) {
        for (Panen p : panenList) {
            if (p.getIdPanen().equalsIgnoreCase(idPanen)) {
                return p;
            }
        }
        return null;
    }
}