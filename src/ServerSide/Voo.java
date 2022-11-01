package ServerSide;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Voo implements Serializable {
    
    private String id;
    private String origem;
    private String destino;
    private int capacidade;
    private LocalDate data;
    private List<String> idPassageiros;


    public Voo(String origem, String destino){
        this.origem = origem;
        this.destino = destino;
    }

    public Voo(String id, String origem, String destino, LocalDate data, List<String> idPassageiros) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.capacidade = 350;
        this.data = data;
        this.idPassageiros = idPassageiros;
    }

    public String getOrigem() {
        return this.origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return this.destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public List<String> getIdPassageiros() {
        return this.idPassageiros;
    }

    public void adicionaPassageiro(String id){
        this.idPassageiros.add(id);
    }

    public void setIdPassageiros(List<String> idPassageiros) {
        this.idPassageiros = idPassageiros;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFull(){
        return (this.capacidade == this.idPassageiros.size());
    }
    
    public LocalDate getData() {
        return this.data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return  "-------------------------------------------------\n" +
                "----Voo : " + getId() + " ----\n" +
                "----Aeroporto de Partida : " + getOrigem() +  "\n" +
                "----Aeroporto de Chegada : " + getDestino() +  "\n" +
                "----Lotacao : " + (getIdPassageiros().size()) + " / " + getCapacidade()  + "\n" +
                "----Data de Partida : " + getData() + "\n";
                

    }


}