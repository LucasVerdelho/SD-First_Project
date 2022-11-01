package ServerSide;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiObjectSerialization {

    private String file = "voos.txt";
    private String file2 = "users.txt";
    private String file3 = "voosDiarios.txt";
    private ObjectOutputStream os;
    private ObjectInputStream is;
    

    

    public MultiObjectSerialization(){}

    public void writeall(Registos r) throws IOException, ClassNotFoundException {

        //------- PRIMEIRA LISTA PASSAGEIROS -------\\
        List<String> list1 = new ArrayList<>();

        list1.add("JoaozinhoGamer69");
        list1.add("ronaldo7");
        list1.add("Romario43");
        list1.add("123czx");
        list1.add("gatinh0gord0");
        list1.add("jorgeGTA");
        list1.add("Jacinto");
        list1.add("mariaJoana98");
        list1.add("jp1988");
        list1.add("username");
        //-------------------------------------------VOOS----------------------------------------------\\

        Voo v1 = new Voo("Lisboa-Paris","Lisboa", "Paris", LocalDate.of(2022,2,15), list1);
        Voo v2 = new Voo("Lisboa-Madrid","Lisboa", "Madrid",LocalDate.of(2022,2,15),  list1);
        Voo v3 = new Voo("Lisboa-Londres","Lisboa", "Londres",LocalDate.of(2022,2,15), list1);
        Voo v4 = new Voo("Paris-Roma","Paris", "Roma",LocalDate.of(2022,2,15), list1);
        Voo v5 = new Voo("Paris-Atenas","Paris", "Atenas",LocalDate.of(2022,2,15), list1);
        Voo v6 = new Voo("Paris-Lisboa","Paris", "Lisboa",LocalDate.of(2022,2,15), list1);
        Voo v7 = new Voo("Paris-Madrid","Paris", "Madrid",LocalDate.of(2022,2,15), list1);
        Voo v8 = new Voo("Paris-Zagreb","Paris", "Zagreb",LocalDate.of(2022,2,15), list1);
        Voo v9 = new Voo("Paris-Dublin","Paris", "Dublin",LocalDate.of(2022,2,15), list1);
        Voo v10 = new Voo("Londres-Lisboa","Londres", "Lisboa",LocalDate.of(2022,2,15), list1);
        Voo v11 = new Voo("Londres-Paris","Londres", "Paris",LocalDate.of(2022,2,15), list1);
        Voo v12 = new Voo("Londres-Madrid","Londres", "Madrid",LocalDate.of(2022,2,15), list1);
        Voo v13 = new Voo("Londres-Varsovia","Londres", "Varsovia",LocalDate.of(2022,2,15), list1);
        Voo v14 = new Voo("Roma-Berlin","Roma", "Berlin",LocalDate.of(2022,2,15), list1);
        Voo v15 = new Voo("Roma-Kiev","Roma", "Kiev",LocalDate.of(2022,2,15), list1);
        Voo v16 = new Voo("Roma-Zagreb","Roma", "Zagreb",LocalDate.of(2022,2,15), list1);
        Voo v17 = new Voo("Roma-Atenas","Roma", "Atenas",LocalDate.of(2022,2,15), list1);
        Voo v18 = new Voo("Dublin-Oslo","Dublin", "Oslo",LocalDate.of(2022,2,15), list1);
        Voo v19 = new Voo("Monaco-Bucareste","Monaco", "Bucareste",LocalDate.of(2022,2,15), list1);
        Voo v20 = new Voo("Bruxelas-Lisboa","Bruxelas", "Lisboa",LocalDate.of(2022,2,15), list1);
        Voo v21 = new Voo("Atenas-Paris","Atenas", "Paris",LocalDate.of(2022,2,15), list1);
        Voo v22 = new Voo("Kiev-Moscovo","Kiev", "Moscovo",LocalDate.of(2022,2,15),  list1);
        Voo v23 = new Voo("Moscovo-Zagreb","Moscovo", "Zagreb",LocalDate.of(2022,2,15),  list1);
        Voo v24 = new Voo("Zagreb-Viena","Zagreb", "Viena",LocalDate.of(2022,2,15),  list1);
        Voo v25 = new Voo("Estocolmo-Lisboa","Estocolmo", "Lisboa",LocalDate.of(2022,2,15), list1);

        List<Object> objectList = new ArrayList<>();

        objectList.add(v1);
        objectList.add(v2);
        objectList.add(v3);
        objectList.add(v4);
        objectList.add(v5);
        objectList.add(v6);
        objectList.add(v7);
        objectList.add(v8);
        objectList.add(v9);
        objectList.add(v10);
        objectList.add(v11);
        objectList.add(v12);
        objectList.add(v13);
        objectList.add(v14);
        objectList.add(v15);
        objectList.add(v16);
        objectList.add(v17);
        objectList.add(v18);
        objectList.add(v19);
        objectList.add(v20);
        objectList.add(v21);
        objectList.add(v22);
        objectList.add(v23);
        objectList.add(v24);
        objectList.add(v25);

        writeToFileVoos(objectList);
        readFileVoos(r);

    //----------------------------------------------Voos Diarios-------------------------------------------------\\


        Voo vd1 = new Voo("Lisboa" ,"Paris");
        Voo vd2 = new Voo("Lisboa" , "Madrid");
        Voo vd3 = new Voo("Lisboa" , "Londres");
        Voo vd4 = new Voo("Paris" , "Roma");
        Voo vd5 = new Voo("Paris" , "Atenas");
        Voo vd6 = new Voo("Paris" , "Lisboa");
        Voo vd7 = new Voo("Paris" , "Madrid");
        Voo vd8 = new Voo("Paris" , "Zagreb");
        Voo vd9 = new Voo("Paris" , "Dublin");
        Voo vd10 = new Voo("Londres" , "Lisboa");
        Voo vd11 = new Voo("Londres" , "Paris");
        Voo vd12 = new Voo("Londres" , "Madrid");
        Voo vd13 = new Voo("Londres" , "Varsovia");
        Voo vd14 = new Voo("Roma" , "Berlin");
        Voo vd15 = new Voo("Roma" , "Kiev");
        Voo vd16 = new Voo("Roma" , "Zagreb");
        Voo vd17 = new Voo("Roma" , "Atenas");
        Voo vd18 = new Voo("Dublin" , "Oslo");
        Voo vd19 = new Voo("Monaco" , "Bucareste");
        Voo vd20 = new Voo("Bruxelas" , "Lisboa");
        Voo vd21 = new Voo("Atenas" , "Paris");
        Voo vd22 = new Voo("Kiev" , "Moscovo");
        Voo vd23 = new Voo("Moscovo" , "Zagreb");
        Voo vd24 = new Voo("Zagreb" , "Viena");
        Voo vd25 = new Voo("Estocolmo" , "Lisboa");

        List<Object> voosDiariosList = new ArrayList<>();

        voosDiariosList.add(vd1);
        voosDiariosList.add(vd2);
        voosDiariosList.add(vd3);
        voosDiariosList.add(vd4);
        voosDiariosList.add(vd5);
        voosDiariosList.add(vd6);
        voosDiariosList.add(vd7);
        voosDiariosList.add(vd8);
        voosDiariosList.add(vd9);
        voosDiariosList.add(vd10);
        voosDiariosList.add(vd11);
        voosDiariosList.add(vd12);
        voosDiariosList.add(vd13);
        voosDiariosList.add(vd14);
        voosDiariosList.add(vd15);
        voosDiariosList.add(vd16);
        voosDiariosList.add(vd17);
        voosDiariosList.add(vd18);
        voosDiariosList.add(vd19);
        voosDiariosList.add(vd20);
        voosDiariosList.add(vd21);
        voosDiariosList.add(vd22);
        voosDiariosList.add(vd23);
        voosDiariosList.add(vd24);
        voosDiariosList.add(vd25);

        writeToFileVoosDiarios(voosDiariosList);
        readFileVoosDiarios(r);

        //-----------------------------------------------USERS-------------------------------------------------\\

        //---1º Passageiro---\\
        Map<LocalDate,Viagem> reservas1 = new HashMap<>();
        List<Voo> trip1 = new ArrayList<>();
        trip1.add(v1);
        Viagem viagem1 = new Viagem("JoaozinhoGamer69-2022-2-15",trip1);
        reservas1.put(LocalDate.of(2022,2,15),viagem1);
        Passageiro p1 = new Passageiro("JoaozinhoGamer69","fortnite2005",reservas1);

        //---2º Passageiro---\\
        Map<LocalDate,Viagem> reservas2 = new HashMap<>();
        List<Voo> trip2 = new ArrayList<>();
        trip2.add(v4);
        Viagem viagem2 = new Viagem("jorgeGTA-2022-2-15",trip2);
        reservas2.put(LocalDate.of(2022,2,15),viagem2);
        Passageiro p2 = new Passageiro("jorgeGTA","mamboNR1",reservas2);

        //---3º Passageiro---\\
        Map<LocalDate,Viagem> reservas3 = new HashMap<>();
        List<Voo> trip3 = new ArrayList<>();
        trip3.add(v22);
        trip3.add(v23);
        trip3.add(v24);
        Viagem viagem3 = new Viagem("Jacinto-2022-2-15",trip3);
        reservas3.put(LocalDate.of(2022,2,15),viagem3);
        Passageiro p3 = new Passageiro("Jacinto","otnicaJ",reservas3);

        //---ADMIN---\\
        Admin a1 = new Admin("Fausto", "123abc");
        Admin a2 = new Admin("Bond", "007");

        List<Object> UsersList = new ArrayList<>();
        UsersList.add(p1);
        UsersList.add(p2);
        UsersList.add(p3);

        UsersList.add(a1);
        UsersList.add(a2);

        writeToFileUsers(UsersList);
        readFileUsers(r);


    }
    


    //****************************************************************************************************\\
    //-----------------------------------------LEITURA E ESCRITA------------------------------------------\\
    //****************************************************************************************************\\

    //Escrita para ficheiro "voos.txt"
    public void writeToFileVoos(List<Object> objectList) throws IOException {
        os = new ObjectOutputStream(new FileOutputStream(file));
        os.writeObject(objectList);
        os.close();
    }

    //Leitura do ficheiro "voos.txt" e mapeamento dos Registos
    public void readFileVoos(Registos r) throws ClassNotFoundException, IOException {
        is = new ObjectInputStream(new FileInputStream(file));
        List<Object> input = (List<Object>) is.readObject();
        for (Object v : input) {
            if (v instanceof Voo) {
                Voo voo = (Voo) v;
                r.adicionaVooRegisto(voo);
                
            }
        }
        is.close();
    }

    //-------------------------------------------------------------------------------------------------\\

    //Escrita para ficheiro "users.txt"
    public void writeToFileUsers(List<Object> UsersList) throws IOException {
        os = new ObjectOutputStream(new FileOutputStream(file2));
        os.writeObject(UsersList);
        os.close();
    }

    //Leitura do ficheiro "users.txt" e mapeamento dos Registos
    public void readFileUsers(Registos r) throws IOException, ClassNotFoundException {
        is = new ObjectInputStream(new FileInputStream(file2));
        List<Object> input = (List<Object>) is.readObject();
        for (Object u : input) {
            if (u instanceof Passageiro) {
                Passageiro passageiro = (Passageiro) u;
                r.adicionaInfoPassageiro(passageiro);
                
            }
            else if (u instanceof Admin) {
                Admin admin = (Admin) u;
                r.adicionaInfoAdmin(admin);
                
            }
        }
        is.close();
    }

    //-------------------------------------------------------------------------------------------------\\

    //Escrita para ficheiro "voosDiarios.txt"
    public void writeToFileVoosDiarios(List<Object> voosDiariosList) throws IOException {
        os = new ObjectOutputStream(new FileOutputStream(file3));
        os.writeObject(voosDiariosList);
        os.close();
    }

    //Leitura do ficheiro "voosDiarios.txt" e mapeamento dos Registos
    public void readFileVoosDiarios(Registos r) throws ClassNotFoundException, IOException {
        is = new ObjectInputStream(new FileInputStream(file3));
        List<Object> input = (List<Object>) is.readObject();
        for (Object v : input) {
            if (v instanceof Voo) {
                Voo voo = (Voo) v;
                r.addVoosListaDiaria(voo);
                
            }
        }
        is.close();
    }

}
