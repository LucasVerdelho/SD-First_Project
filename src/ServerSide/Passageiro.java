package ServerSide;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Passageiro extends User implements Serializable {
    
    
    private Map<LocalDate,Viagem> reservas;
    

    public Passageiro(String userName, String password) {
        super(userName, password);
        this.reservas = new HashMap<>();
    }

    public Passageiro(String userName, String password, Map<LocalDate,Viagem> reservas){
        super(userName,password);
        this.reservas = reservas;
    }

    public Map<LocalDate,Viagem> getReservas() {
        return this.reservas;
    }

    public void adicionaViagem(Voo voo,String idPassageiro){
        
        Viagem v = this.reservas.get(voo.getData());
        if(v!=null){
            v.addVoo(voo);
        }
        
        else{

            //cria a viagem no Passageiro e adiciona o primeiro voo
            Viagem viagem = new Viagem(idPassageiro+'-'+voo.getData());
            viagem.addVoo(voo);
            this.reservas.put(voo.getData(),viagem);
        }

    }

    public Viagem removeViagem(String idViagem){
        
        //O id da viagem é composto por: UserName + data (Em String)
        //Ex:jorgeGTA-2022-01-15
        String[] str = idViagem.split("-");
        LocalDate key = LocalDate.of(Integer.parseInt(str[1]),Integer.parseInt(str[2]),Integer.parseInt(str[3]));
        return this.reservas.remove(key);
        
    }


    public void setReservas(Map<LocalDate,Viagem> reservas) {
        this.reservas = reservas;
    }

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("Utilizador : " + super.getUserName() + "\n");
        for (LocalDate d : this.reservas.keySet()) {
            sb.append("Data da Viagem : " + d.toString()+ "\n");
            sb.append(this.reservas.get(d).toString());
        }
        return sb.toString();
    }
}
