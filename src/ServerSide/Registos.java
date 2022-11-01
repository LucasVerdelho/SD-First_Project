package ServerSide;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Registos {


    private ReentrantReadWriteLock lockUsers;
    private ReentrantReadWriteLock lockVoos;
    private ReentrantReadWriteLock lockDatasEncerradas;
    private ReentrantReadWriteLock lockVoosComReservas;
    private Map<String,User> users;
    private Set<LocalDate> datasEncerradas;
    private Map<String,List<String>> voos; // Lista permanente dos voos que acontecem todos os dias
    private Map<LocalDate,List<Voo>> voosComReservas;

    public Registos(){
        
        this.lockUsers = new ReentrantReadWriteLock();
        this.lockVoos = new ReentrantReadWriteLock();
        this.lockDatasEncerradas = new ReentrantReadWriteLock();
        this.lockVoosComReservas = new ReentrantReadWriteLock();
        this.users = new HashMap<>();
        this.datasEncerradas = new HashSet<>();
        this.voos = new HashMap<>();
        this.voosComReservas = new HashMap<>();
    }

    public Set<LocalDate> getDatasEncerradas(){
        
        try{
            this.lockDatasEncerradas.readLock().lock();
            return this.datasEncerradas;
        }
        finally{
            this.lockDatasEncerradas.readLock().unlock();
        }
    }

    public Map<String,User> getUsers() {
        try{
            this.lockUsers.readLock().lock();
            return this.users;
        }finally{
            this.lockUsers.readLock().unlock();
        }
    }

    public void setUsers(Map<String,User> users) {
        this.lockUsers.readLock().lock();
        this.users = users;
        this.lockUsers.readLock().unlock();
    }


    public List<String> getDestinos(String origem){
        this.lockVoos.readLock().lock();
        List<String> listaVoos = this.voos.get(origem);
        this.lockVoos.readLock().unlock();
        if(listaVoos == null) 
            return null;

        List<String> possiveisDestinos = new ArrayList<>();
    
        for (String destino : listaVoos) {
           possiveisDestinos.add(destino);
        }

       return possiveisDestinos;
    }

    // Devolve as datas dos voos disponiveis
    public List<String> confirmaDatasDisp(String origem, String destino){
        
        // Cria lista para as possiveis datas
        List<LocalDate> datasValidas = datasValidas();
        this.lockVoosComReservas.readLock().lock();
        for(LocalDate l : datasValidas){
            //Para cada dia válido é verificado se existe voo reservado
            if(this.voosComReservas.containsKey(l)){
                
                List<Voo> voos = this.voosComReservas.get(l);
                for (Voo v : voos) {

                    //se existir um voo com a origem e destino desejado que já tenha reservas e !Full
                    if (v.getDestino().equals(destino)&&v.getOrigem().equals(origem)&&(v.isFull())){
                        datasValidas.remove(l);
                    }
                }

            }

        }
        this.lockVoosComReservas.readLock().unlock();

        //Passar as datas para Strings
        List<String> datas = new ArrayList<>();
        for (LocalDate l : datasValidas) {
            datas.add(l.toString());
        }

        //devolve null se todas as datas estiverem ocupadas
        return(datas.size()==0) ? null : datas;
    }

    
    //Lista dos dias válidos para reserva de voos
    public List<LocalDate> datasValidas(){

        LocalDate hoje = LocalDate.now();
	  
	    List<LocalDate> l = new ArrayList<>();
        //adicionar o dia de hoje
        
        LocalDate dia = hoje;
        this.lockDatasEncerradas.readLock().lock();
        for(int i=0;i<7;i++){
            
            //aqui são excluidos os dias que o admin encerrou
            if(!this.datasEncerradas.contains(dia)){
                l.add(dia);
            }
            dia = dia.plusDays(1);
        }
        this.lockDatasEncerradas.readLock().unlock();

        return l;

    }

    public boolean addVoosListaDiaria(Voo v){
        String origem = v.getOrigem();
        String destino = v.getDestino();
        this.lockVoos.writeLock().lock();
        if(this.voos.containsKey(origem)){
            var l = this.voos.get(origem);
            for(String str : l){

                //verificar se o voo já existe
                if(str.equals(destino)) return false;
            }
            l.add(destino);
        }

        else {
            List<String> novaLista = new ArrayList<>();
            novaLista.add(destino);
            this.voos.put(origem, novaLista);
        }
        this.lockVoos.writeLock().unlock();
        return true;
    }
    


    public void encerraDia(LocalDate data){

        this.lockDatasEncerradas.writeLock().lock();
        this.datasEncerradas.add(data);
        this.lockVoosComReservas.writeLock().lock();
        this.lockDatasEncerradas.writeLock().unlock();
        this.voosComReservas.remove(data);
        this.lockVoosComReservas.writeLock().unlock();
        this.lockUsers.readLock().lock();
        for(var v : this.users.entrySet()){

            //Se o user for um passageiro é removida a viagem marcada na data indicada
            if(v.getValue() instanceof Passageiro){
                Passageiro p = (Passageiro) v.getValue();
                p.getReservas().remove(data);
            }


        }
        this.lockUsers.readLock().unlock();
    }
    
    //Fazer a reserva do voo
    public boolean reservaVoo(String origem, String destino, LocalDate data, String idPassageiro){
        
        this.lockVoosComReservas.writeLock().lock();
        //o voo já tem reservas registadas
        if(this.voosComReservas.containsKey(data)){
            List<Voo> voos = this.voosComReservas.get(data);
            Voo voo;
            
            for(Voo v:voos){

                //encontrar o voo correto e reservá-lo(se existir)
                if(v.getDestino().equals(destino)&&v.getOrigem().equals(origem)){
                    if(!v.isFull()){
                        //aqui encontramos o voo desejado
                        
                        //Registar o passageiro no voo
                        voo = v;
                        voo.adicionaPassageiro(idPassageiro);
                        
                        //Registar o voo na viagem do passageiro
                        adicionaViagemAoPassageiro(idPassageiro, voo);
                        
                        return true;
                    }

                    //voo cheio
                    else return false;
                }

            }
                String idVoo = origem + '-' + destino;

                // Adiciona o passageiro a lista desse voo
                List<String> idPassageiros = new ArrayList<>();
                idPassageiros.add(idPassageiro);
                voo = new Voo(idVoo, origem, destino, data, idPassageiros);

                // Adiciona o voo ao mapa dos VoosComReservas
                this.voosComReservas.get(data).add(voo);
                adicionaViagemAoPassageiro(idPassageiro, voo);
            }
            
            
            
            //o voo não tem reservas registadas
            else{
                // Cria o novo id do Voo
                String idVoo = origem + '-' + destino;
                
                // Adiciona o passageiro a lista desse voo
                List<String> idPassageiros = new ArrayList<>();
                idPassageiros.add(idPassageiro);
                
                // Constroi o novo voo com os parametros criados
                Voo novoVoo = new Voo(idVoo, origem, destino, data, idPassageiros);
                List<Voo> voosDia = new ArrayList<>();
                voosDia.add(novoVoo);
                
                // Adiciona o voo ao mapa dos VoosComReservas
                this.voosComReservas.put(data,voosDia);
                
                // Adiciona a viagem ao Passageiro
                adicionaViagemAoPassageiro(idPassageiro, novoVoo);
            }
            
            this.lockVoosComReservas.writeLock().unlock();

        return true;

    }



    // Adiciona um voo aos VoosComReservas
    public void adicionaVooRegisto(Voo v){

        this.lockVoosComReservas.writeLock().lock();

        if(this.voosComReservas.containsKey(v.getData())){
            this.voosComReservas.get(v.getData()).add(v);
            
        }

        else{
            List<Voo> temp = new ArrayList<>();
            temp.add(v);
            this.voosComReservas.put(v.getData(),temp);
        }
        
        this.lockVoosComReservas.writeLock().unlock();
        
    }


    //Adiciona voo a uma viagem do passageiro e se o mesmo não tiver, cria uma nova
    public void adicionaViagemAoPassageiro(String idPassageiro,Voo voo){
        this.lockUsers.writeLock().lock();

        Passageiro p = (Passageiro) this.getUsers().get(idPassageiro);
        p.adicionaViagem(voo, idPassageiro);

        this.lockUsers.writeLock().unlock();
    }



    public List<String> consultaViagensUtilizador(String username){
        
        List<String> viagensUser = new ArrayList<>();
        this.lockUsers.writeLock().lock();

        // Verifica se o username existe na base de dados e caso nao exista entao o user nao tem viagens reservadas
        if(!this.getUsers().containsKey(username)){
            viagensUser.add("Utilizador nao tem viagens reservadas.\n");
            return viagensUser;
        }

        // Encontra o utilizador e acessa as suas viagens
        Passageiro p = (Passageiro) this.getUsers().get(username);
        Map<LocalDate,Viagem> mapViagensUser = p.getReservas();

        Viagem temp = null;
        StringBuilder sb = new StringBuilder();

        // Para cada data no mapa, adiciona a lista de viagensUser a viagem correspondente
        for (LocalDate d : mapViagensUser.keySet()) {
            sb.append("----Data : " + d.toString() + "\n");
            temp = mapViagensUser.get(d);
            sb.append(temp.toString());
            viagensUser.add(sb.toString());
            sb.setLength(0);
        }
        
        this.lockUsers.writeLock().unlock();

        return viagensUser;
    }

    public Viagem removeViagemUser(String username, String idViagem){

        try{    
            this.lockUsers.writeLock().lock();
            Passageiro p = (Passageiro) this.users.get(username);
            return p.removeViagem(idViagem);
        }finally{
            this.lockUsers.writeLock().unlock();
        }
    }

    public void adicionaInfoPassageiro(Passageiro p){

        this.lockUsers.writeLock().lock();
        this.getUsers().put(p.getUserName(), p);
        this.lockUsers.writeLock().unlock();
    }

    
    public void adicionaInfoAdmin(Admin a){

        this.lockUsers.writeLock().lock();
        this.getUsers().put(a.getUserName(), a);
        this.lockUsers.writeLock().unlock();
    }


    public boolean existeUser(String userName){

        try{
            this.lockUsers.readLock().lock();
            return this.getUsers().containsKey(userName);
        }finally{
            this.lockUsers.readLock().unlock();
        }
    }

    public boolean confirmaPass(String userName,String password){

        try{
            this.lockUsers.readLock().lock();
            User u = this.getUsers().get(userName);
            return u.getPassword().equals(password);
        }finally{
            this.lockUsers.readLock().unlock();
        }
    }

    public int getType(String userName){

        try{
            this.lockUsers.readLock().lock();
            return this.getUsers().get(userName).getType();
        }finally{
            this.lockUsers.readLock().unlock();
        }
    }

    public User getUser(String userName){

        try{
            this.lockUsers.readLock().lock();
            return this.users.get(userName);
        }finally{
            this.lockUsers.readLock().unlock();
        }
    }

    public List<List<String>> getListaVoos(){

        List<List<String>> listaPares = new ArrayList<>();
        this.lockVoos.readLock().lock();

        //Percorrer todas as chaves do mapa (origens)
        for(var e : this.voos.entrySet()){

            String origem = e.getKey();
            
            //Percorrer o Value de cada chave e adicionar a uma nova lista 
            //(encontrar o destino para formar a lista de pares origem-destino)
            for(String destino : e.getValue()){
                
                List<String> par = new ArrayList<>();
                par.add(origem);
                par.add(destino);
                listaPares.add(par);

            }

        }
        this.lockVoos.readLock().unlock();


        return listaPares;

    }

    


}
