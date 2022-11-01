package ServerSide;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore.LoadStoreParameter;
import java.time.LocalDate;

public class ServerSlave implements Runnable{

    private Socket socket;
    private Registos r;
    private String user;

    public ServerSlave(Socket socket,Registos r) {
        this.socket = socket;
        this.r=r;
    }

    @Override
    public void run() {
        try {
            
            /*
            
            validar datas introduzidas
            validar destinos quando o user introduz
            


*/

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            /*
            Autenticação:
            -1 -> sair
            0 -> sessão iniciada como admin
            1 -> sessão iniciada como passageiro
            
            */
            
            int ret = autenticarUtilizador(in,out);
            
            
            
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));


            
            switch (ret) {
                
                case 0:
                    System.out.println("Admin \""+this.user+"\" iniciou sessão");
                    menuAdmin(in, out);

                    break;
                
                case 1:
                    System.out.println("Cliente \""+this.user+"\" iniciou sessão");
                    menuCliente(in, out);

                   break;
                
                case 2:

                    System.out.println("Utilizador \""+this.user+"\" criado");

                default:
                    
                    break;

            }
            System.out.println("Conexão terminada");
            in.close();
            out.close();
            this.socket.close();

        } catch (IOException e) {
           
            e.printStackTrace();
        }
        
        
    }

    private void menuAdmin(DataInputStream in,DataOutputStream out) throws IOException{

        while(true){

            //Admin já está autenticado:
            int option = in.readInt();

            /*
            Menu principal do cliente:
            1 -> Inserir Voos
            2 -> Encerrar dia
            3 -> Consultar todos os voos
            
            0 -> Sair
            */

            switch (option) {
                case 1:

                    out.writeUTF("Insira o Voo:\n\nInsira a origem do novo voo");
                    out.flush();
                    String origem = in.readUTF();

                    out.writeUTF("Insira o destino do novo voo");
                    out.flush();
                    String destino = in.readUTF();
                    
                    Voo v = new Voo(origem, destino);
                    boolean adicionado = r.addVoosListaDiaria(v);
                    String response = adicionado ? "Voo adicionado com sucesso" : "Voo já existente";
                    out.writeUTF(response);
                    out.flush();

                    break;

                case 2:
                    
                    out.writeUTF("Insira o dia quer deseja encerrar o aeroporto no formato AAAA-MM-DD");
                    out.flush();

                    String data = in.readUTF();
                    String[] str = data.split("-");
                    LocalDate date = LocalDate.of(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
                    r.encerraDia(date);
                    
                    out.writeUTF("Dia "+data+" encerrado com sucesso");
                    out.flush();

                    break;

                case 3:
                    
                    out.writeUTF(r.getListaVoos().toString());
                    out.flush();

                    break;


                case 0:
                    
                    return;
                default:
                    break;
            }





        }


    }





    private void menuCliente(DataInputStream in,DataOutputStream out) throws IOException{
        while(true){

            //Passageiro já está autenticado:
            int option = in.readInt();
            /*
            Menu principal do cliente:
            1 -> Ver destinos
            2 -> Reservar Viagem
            3 -> Consultar Viagens 
            4 -> Cancelar Viagem 
            5 -> Ver voos
            
            0 -> Sair
            */
            switch (option) {
                case 1:

                boolean consultado = false;
                while(!consultado){
                    
                    out.writeUTF("Escolha o aeroporto de partida:");
                    out.flush();
                    

                    //recebe a origem
                    String origem = in.readUTF();
                    var destinos = r.getDestinos(origem);
                    if(destinos==null){
                        out.writeUTF("Origem inválida");
                        out.writeBoolean(false);
                        out.flush();

                    }
                    else{
                        out.writeUTF("Destinos possíveis:\n\n"+destinos);
                        out.writeBoolean(true);
                        out.flush();
                        consultado = true;
                    }

                }

                    break;
            
                case 2:

                    boolean escala = false;
                    boolean terminado = false;
                    String origem = null;
                    String dataInput = null;
                    String[] str=null;
                    LocalDate data=null;
                    boolean disponivel=false;

                    while(!terminado){

                        // Se nao houver escala ele escolhe a origem do Voo - A
                        if(!escala){
                            out.writeUTF("\nEscolha uma origem e um destino do seu voo:\n\nOrigem:");
                            out.flush();
                            origem = in.readUTF(); 
                        }


                        out.writeUTF("\nDestino:");
                        out.flush();
                        String destino = in.readUTF();



                        //Confirma se existem datas disponíveis
                        var v =r.confirmaDatasDisp(origem, destino);
                        
                        
                        if(!escala){
                            if(v==null){

                                out.writeUTF("Nenhuma data disponível");
                                out.writeBoolean(false);
                                out.flush();
                                break;
                            }
                        
                            out.writeUTF(v.toString());
                            out.writeBoolean(true);
                            out.writeUTF("\n\nSelecione a data no formato AAAA-MM-DD:");
                            out.flush();


                            //Receber a data da reserva e tentar reservar
                            dataInput=in.readUTF();
                            str = dataInput.split("-");
                            data = LocalDate.of(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
                            
                            disponivel=false;
                            //Confirmar se a data introduzida é válida
                            for(String s : v){
                                if(dataInput.equals(s)) disponivel=true;
                            }
                        }

                        //A data é válida, agora é verificada a lotação do avião.
                        if(disponivel){
                            disponivel = r.reservaVoo(origem, destino, data, this.user);
                        }
                        
                        //Informar o Cliente se é ou não possível fazer a reserva
                        out.writeBoolean(disponivel);
                        String response = disponivel ? "Reserva efetuada com sucesso" : "Reserva Impossível, tente novamente";
                        out.writeUTF(response);
                        out.flush();


                        //Se a reserva for feita com sucesso, perguntar se quer fazer escala (ainda não implementado)
                        if(disponivel){

                            //perguntar se quer escala
                            out.writeUTF("Deseja fazer escala? (s ou n)");
                            out.flush();

                            escala = in.readBoolean();
                            if(!escala) {
                                terminado=true;
                                out.writeUTF("Viagem registada com sucesso\n");
                            }
                            else{
                                out.writeUTF("Agora pode introduzir o próximo voo\n");
                                origem = destino;
                            }
                            out.flush();

                        }

                    }

                    
                    break;
                
                case 3:
                
                    Passageiro p = (Passageiro) r.getUser(this.user);
                    String response = p.getReservas().isEmpty() ? "\nVocê não tem reservas marcadas\n" : "\nReservas:\n"+p.getReservas().values().toString();
                    out.writeUTF(response);
                    out.flush();

                    break;

                case 4:

                    boolean cancelada=false;
                    while(!cancelada){
                        out.writeUTF("Escolha a viagem a ser cancelada indicando o seu id:");
                        out.flush();

                        String idViagem = in.readUTF();
                        var viagem = this.r.removeViagemUser(this.user, idViagem);
                        if(viagem!=null){
                            cancelada=true;
                            out.writeUTF("\nViagem cancelada com sucesso\n");
                            
                        }
                        else{
                            out.writeUTF("\nViagem inexistente, tente novamente\n");
                            
                        }
                        out.writeBoolean(cancelada);
                        out.flush();
                    }
                    break;

                case 5:

                    out.writeUTF(r.getListaVoos().toString());
                    out.flush();


                    break;

                
                case 0:
                    
                    return;
            }
        }
    }



    private int autenticarUtilizador(DataInputStream in,DataOutputStream out) throws IOException{

        
        int option = in.readInt();
        
        int ret=-2;
        String userName=null;
        switch (option) {
            
            case 1://iniciar sessão
  
                boolean autenticado=false;
                while(!autenticado){

                    out.writeUTF("Introduza o seu UserName:");
                    out.flush();
                    userName = in.readUTF();

                    out.writeUTF("Introduza a sua password:");
                    out.flush();
                    String password = in.readUTF();

                    //aqui no existeUser tem que ser devolvido se o gajo é admin ou user
                    if(r.existeUser(userName)&&r.confirmaPass(userName, password)){
                        autenticado=true;
                        out.writeUTF("Bem vindo\n");
                        this.user = userName;
                    }
                    else{
                        out.writeUTF("Dados inválidos! Tente novamente.\n");
                        
                    }
                    out.writeBoolean(autenticado);
                    if(autenticado){
                        int type = r.getType(userName);
                        out.writeInt(type);
                        ret=type;
                    }
                    out.flush();
                    
                }


                break;
        
            case 2://criar nova conta,apenas passageiros criam contas

                
                boolean criado=false;
                while(!criado){

                    out.writeUTF("Introduza o seu UserName:");
                    out.flush();
                    userName = in.readUTF();

                    boolean existeUser = r.existeUser(userName);
                    out.writeBoolean(existeUser);
                    if(!existeUser){
                        out.writeUTF("Introduza a sua password:");
                        out.flush();
                        String password = in.readUTF();

                        //Criar o passageiro novo e introduzí-lo no mapa
                        Passageiro p = new Passageiro(userName, password);
                        r.adicionaInfoPassageiro(p);
                        criado = true;

                        out.writeUTF("Conta criada com sucesso!");
                        out.flush();
                    }
                    else{
                        out.writeUTF("UserName indisponível");
                        out.flush();
                    }
                }
                this.user=userName;
                ret=2;

                break;

            case 0:
                
                ret=-1;

                break;

            default:

                ret=-2;
                break;
        }

        return ret;
    
    }

}

