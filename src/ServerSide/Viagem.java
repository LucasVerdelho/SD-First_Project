package ServerSide;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viagem implements Serializable {
    
    private String id;
    private List<Voo> viagem;


    public Viagem(String id) {
        this.id = id;
        this.viagem = new ArrayList<>();

    }

    public Viagem(String id, List<Voo> viagem) {
        this.id = id;
        this.viagem = viagem;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Voo> getViagem() {
        return this.viagem;
    }

    public void setViagem(List<Voo> viagem) {
        this.viagem = viagem;
    }

    public void addVoo(Voo v){
        this.viagem.add(v);
    }





    @Override
    public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n----------Viagem----------\n");
            sb.append("----Identificador da Viagem : ").append(getId()).append("\n");
            List<Voo> temp = this.getViagem();
            for (Voo v : temp) {
                sb.append(v.toString());
            }
            sb.append("--------------------------\n");
            return sb.toString();
        }
    }



