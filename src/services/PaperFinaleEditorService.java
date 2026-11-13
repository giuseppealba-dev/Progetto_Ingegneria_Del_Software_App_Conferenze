package services;

import java.util.List;

import data.dao.PaperFinaleEditorDAO;
import models.Articolo;
import models.PaperFinaleEditor;

public class PaperFinaleEditorService {
    

    private final PaperFinaleEditorDAO dao = new PaperFinaleEditorDAO();


    public List<Articolo> getPapersFinaliByEditor(int editorId) {
        return dao.getPapersFinaliByEditor(editorId);
    }

    public void inviaPaperFinale(int idArticolo, int idEditor) {
        dao.inserisciPaperFinale(idArticolo, idEditor);
    }

    public void rimuoviPaperFinale(int articoloId, int editorId) {
        dao.rimuoviPaperFinale(articoloId, editorId);
    }

}
