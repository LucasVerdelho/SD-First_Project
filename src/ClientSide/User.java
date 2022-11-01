package ClientSide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class User {

    public static void main(String[] args) throws UnknownHostException, IOException {
        
        Socket socket = new Socket("localhost", 8888);

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));


        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));


        //Autenticação
        int ret = autenticarUtilizador(in,out,systemIn);
        

        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
       
        switch (ret) {
            
            case 0:
                
                menuAdmin(in, out, systemIn);
            
                break;
            
            case 1:
                
                menuCliente(in, out, systemIn);

                break;
            
            
            default:

                break;
        }

        out.close();
        in.close();
        socket.close();

    }



    private static void menuAdmin(DataInputStream in,DataOutputStream out,BufferedReader systemIn) throws IOException{

        while(true){

            System.out.println("\nMenu Principal\n\n1) Inserir Voos\n2) Encerrar dia\n3) Consultar Voos\n\n\n0) Sair");
            
            
            String userInput=systemIn.readLine();
            int option = Integer.parseInt(userInput);
            
            out.writeInt(option);
            out.flush();


            switch (option) {
                case 1:
                    
                    //Inserir a origem do novo Voo
                    System.out.println(in.readUTF());
                    userInput=systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();

                    //Inserir o destino do novo Voo
                    System.out.println(in.readUTF());
                    userInput=systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();

                    //Adiciona o voo aos registos (se ainda não existir)
                    System.out.println(in.readUTF());

                    break;
            
                case 2:

                    System.out.println(in.readUTF());
                    userInput=systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();
                    System.out.println(in.readUTF());


                    break;

                case 3:

                    System.out.println(in.readUTF());
                    
                    break;

                case 0:
                    
                    return;
            }



        }




    }




    
    private static int menuCliente(DataInputStream in,DataOutputStream out,BufferedReader systemIn) throws IOException{


        while(true){

            System.out.println("\nMenu Principal\n\n1) Ver destinos\n2) Reservar Viagem\n3) Consultar Viagens\n4) Cancelar Viagem\n5) Ver voos\n\n0) Sair\n");
            
            
            String userInput=systemIn.readLine();
            int option = Integer.parseInt(userInput);
            
            out.writeInt(option);
            out.flush();
            switch (option) {
                case 1:
                
                    boolean consultado = false;
                    while(!consultado){

                        
                        //Escolher o aeroporto de partida:
                        String response = in.readUTF();
                        System.out.println(response);
                        userInput = systemIn.readLine();
                        out.writeUTF(userInput);
                        out.flush();
                        
                        
                        
                        //Response do Server(lista dos destinos possíveis):
                        System.out.println(in.readUTF());
                        boolean origemValida = in.readBoolean();
                        if(origemValida){
                            consultado = true;
                        }

                    }


                    break;
                
                case 2:

                    boolean terminado = false;
                    boolean escala = false;
                    while(!terminado){

                        if(!escala){
                            //Escolher a origem 
                            System.out.println(in.readUTF());
                            userInput = systemIn.readLine();
                            out.writeUTF(userInput);
                            out.flush();
                        }

                        //Escolher o destino
                        System.out.println(in.readUTF());
                        userInput = systemIn.readLine();
                        out.writeUTF(userInput);
                        out.flush();

                        //se o voo for de escala, não é necessário inserir data visto que é consederado que uma viagem com vários voos, ocorrem todos no mesmo dia
                        if(!escala){
                            //Receber a Lista de datas disponíveis e selecionar a data desejada
                            System.out.println(in.readUTF());
                            //Se não houver datas disponíveis para o voo que o passageiro quer, volta ao menu inicial
                            if(!in.readBoolean()) break;
                            System.out.println(in.readUTF());
                            userInput = systemIn.readLine();
                            out.writeUTF(userInput);
                            out.flush();
                        }

                        //Receber a confirmação da reserva
                        //Mesmo que o voo seja de escala, é necessário confirmar se o voo não está lotado
                        boolean disponivel = in.readBoolean();
                        System.out.println(in.readUTF());

                        //se a reserva for feita com sucesso, pergunta se quer fazer escala (ainda não implementado)
                        if(disponivel){

                            //Confirmar se quer escala ou não
                            System.out.println(in.readUTF());
                            userInput = systemIn.readLine();
                            escala = userInput.equals("n") ? false : true;
                            out.writeBoolean(escala);;
                            out.flush();


                            if(!escala) terminado=true;


                            System.out.println(in.readUTF());

                        }
                                        

                    }

                    break;
                
                case 3:
                    
                    System.out.println(in.readUTF());
                
                    break;

                case 4:
                
                    boolean cancelada=false;
                    while(!cancelada){
                        //Indicar o voo a viagem a ser cancelada
                        System.out.println(in.readUTF());
                        userInput = systemIn.readLine();
                        out.writeUTF(userInput);
                        out.flush();

                        //Resposta
                        System.out.println(in.readUTF());
                        cancelada=in.readBoolean();

                    }

                    break;

                case 5:

                    System.out.println(in.readUTF());

                    break;

                case 0:

                    return -1;
            }
        }


    }


    private static int autenticarUtilizador(DataInputStream in,DataOutputStream out,BufferedReader systemIn) throws IOException{

                
        System.out.println("\n1) Iniciar Sessão\n2) Criar Conta\n\n0) Sair");
        String userInput = systemIn.readLine();
        
           
        int option = Integer.parseInt(userInput);
        out.writeInt(option);
        out.flush();

        int r=1;
        
        
        switch (option) {
            case 1://iniciar sessão

                boolean autenticado=false;
                while(!autenticado){

                    
                    //UserName:
                    System.out.println(in.readUTF());
                    userInput = systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();

                    //Pass:
                    System.out.println(in.readUTF());
                    userInput = systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();

                    //Response:
                    System.out.println(in.readUTF());

                    //Se foi autenticado, devolve o tipo de user que é (Admin/Passageiro)
                    autenticado = in.readBoolean();
                    if(autenticado) r=in.readInt();


                }


                break;
        
            case 2://criar nova conta,apenas passageiros criam contas

                boolean criado=false;
                while(!criado){

                    //UserName:
                    System.out.println(in.readUTF());
                    userInput = systemIn.readLine();
                    out.writeUTF(userInput);
                    out.flush();

                    //Disponível:
                    boolean existeUser = in.readBoolean();
                    if(!existeUser){
                        
                        //Password:
                        System.out.println(in.readUTF());
                        userInput = systemIn.readLine();
                        out.writeUTF(userInput);
                        out.flush();
                        criado = true;

                    }

                    //Response:
                    System.out.println(in.readUTF());
                    
                }

                r=2;
                
                
                break;
            
            case 0:

                r=-1;
                
                break;
        }
        
        return r;

    }




}
