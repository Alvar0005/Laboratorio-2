import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
public class Main{
    static int ident=10000;    //1
    static int Id_Candidato=1; //1

    public static class UrnaElectoral{ //Clase de complejidad (3n^2 + 35n + 5m + 51) = O(n^2)
        public listaCandidatos lista; //1
        public static class listaCandidatos{ //(4)
            public Candidato candidato;       //1
            public listaCandidatos siguiente; //1

            public listaCandidatos(Candidato candidato){ //(2)
                this.candidato=candidato;                 //1
                this.siguiente=null;                      //1
            }
        }

        private Stack<Voto> historialVotos;  //1
        private Queue<Voto> votosReportados; //1
        private int idCounter;               //1

        public UrnaElectoral(){                       //(4)
            this.historialVotos = new Stack<>();       //1
            this.votosReportados = new ArrayDeque<>(); //1
            this.idCounter=0;                          //1
            lista=null;                                //1
        }
        public Stack<Voto> getHistorialVotos(){  //(1)
            return historialVotos;
        }
        public Queue<Voto> getVotosReportados(){  //(1)
            return votosReportados;
        }
        public Boolean verificarVotante(Votante votante){ //(1)
            return votante.getYaVoto();                    //1
        }
        public Boolean registrarVoto(Votante votante, int candidatoId){    //(5n + 5m + 15)
            Stack<Voto> ActualHistorial=historialVotos;                     //1
            Queue<Voto> ActualHisReports=votosReportados;                   //1
            while(!ActualHistorial.isEmpty() || !ActualHisReports.isEmpty()){         //n+m
                if(!ActualHistorial.isEmpty() && ActualHistorial.peek().getVotanteId()==votante.getId()){ //n+m
                    System.out.println("No puede votar de nuevo.");                                       //1
                    return false;                                                                         //1
                }
                else if(!ActualHisReports.isEmpty() && ActualHisReports.peek().getVotanteId()==votante.getId()){ //n+m
                    System.out.println("No puede votar de nuevo.");                                              //1
                    return false;                                                                                //1
                }
                else{
                    ActualHisReports.poll();                                //n+m
                    ActualHistorial.pop();                                  //n+m
                }
            }
            Voto voto = new Voto(votante.getId(), candidatoId);     //1
            Candidato candidato=buscarCandidato(candidatoId);       //1
            if(candidato!=null){                                    //1
                candidato.AgregarVoto(voto);                        //1
                historialVotos.push(voto);                          //1
            }
            else{
                votosReportados.add(voto);                          //1
            }
            votante.marcarComoVotado();                             //1
            System.out.println("Se ha registrado el voto.");        //1
            return true;                                            //1
        }
        public void agregarCandidato(Candidato candidato){             //(2n + 5)
            listaCandidatos nuevoNodo = new listaCandidatos(candidato); //1
            if(lista==null){                                            //1
                lista=nuevoNodo;                                        //1
            }
            else{
                listaCandidatos actual=lista;                           //1
                while (actual.siguiente!=null){                         //n
                    actual=actual.siguiente;                            //n
                }
                actual.siguiente=nuevoNodo;                             //1
            }
        }
        public void eliminarCandidato(int idCandidato){             //(3n^2 +12n +8)
            listaCandidatos actual=lista;                                   //1
            listaCandidatos anterior=null;                                  //1
            boolean encontrado=false;                                       //1
            while(actual!=null){                                            //n
                if(actual.candidato.getId()==idCandidato){                  //n
                    while(!actual.candidato.votosRecibidos.isEmpty()){      //n*n
                        Voto voto=actual.candidato.votosRecibidos.poll();   //n*n
                        votosReportados.add(voto);                          //n*n
                    }
                    if(anterior==null){                                     //n
                        lista=actual.siguiente;                             //n
                    }
                    else{
                        anterior.siguiente=actual.siguiente;                //n
                    }
                    encontrado=true;                                        //n
                    break;                                                  //n
                }
                anterior=actual;                                            //n
                actual=actual.siguiente;                                    //n
            }
            if(encontrado){                                                                     //1
                listaCandidatos siguiente=(anterior==null) ? lista : anterior.siguiente;        //1
                while(siguiente!=null){                                                         //n
                    siguiente.candidato.decrementarId();                                        //n
                    siguiente=siguiente.siguiente;                                              //n
                }
                Id_Candidato--;                                                                 //1
                System.out.println("Candidato eliminado, votos movidos y IDs reajustados.");    //1
            }
            else{
                System.out.println("No se encontró un candidato con ese ID.");                  //1
            }
        }
        public Candidato buscarCandidato(int id){ //(4n + 2)
            listaCandidatos actual=lista;          //1
            while (actual!=null){                  //n
                if(actual.candidato.getId()==id){  //n
                    return actual.candidato;       //n
                }
                actual=actual.siguiente;           //n
            }
            return null;                           //1
        }
        public void PrintListaCandidatos(){                                     //(3n + 1)
            listaCandidatos actual=lista;                                         //1
            while(actual!=null){                                                  //n
                System.out.println("ID: " + actual.candidato.getId() +            //n
                                 ")\nNombre: " + actual.candidato.getNombre() + 
                                 " | Partido: " + actual.candidato.getPartido());
                actual=actual.siguiente;                                          //n
            }
        }
        public void PrintVotos(){                                                   //(6n + 4)
            Stack<Voto> actualPila=historialVotos;                                   //1
            System.out.println("Lista de votos válidos:\n------------------------"); //1
            while(!actualPila.isEmpty()){                                            //n
                Voto voto=actualPila.pop();                                          //n
                System.out.println("Voto: " + voto.getId() +                         //n
                                 "\n|Candidato id: " + voto.getCandidatoId() + 
                                 "\n|Votante id: " + voto.getVotanteId() + 
                                 "\n|Hora: " + voto.getTimeStamp() + "\n--------");
            }
            Queue<Voto> actualCola=votosReportados;                                  //1
            System.out.println("\nLista de votos nulos:\n---------------------");    //1
            while(!actualCola.isEmpty()){                                            //n
                Voto voto=actualCola.poll();                                         //n
                System.out.println("Voto: " + voto.getId() +                         //n
                                 "\n|Candidato id: " + voto.getCandidatoId() + 
                                 "\n|Votante id: " + voto.getVotanteId() + 
                                 "\n|Hora: " + voto.getTimeStamp() + "\n--------");
            }
        }
        public void PrintResultados(){                                             //(3n + 1)
            listaCandidatos actual=lista;                                            //1
            while(actual!=null){                                                     //n
                System.out.println("ID: " + actual.candidato.getId() +               //n
                                ")\nCandidato: " + actual.candidato.getNombre() + 
                                " | Partido: " + actual.candidato.getPartido() +
                                " | Votos: " + actual.candidato.getCantidadVotos());
                actual=actual.siguiente;                                             //n
            }
        }
    }
    public static class Candidato{ //clase de complejidad (20) = O(1)
        private int id;                      //1
        private String nombre;               //1
        private String partido;              //1
        private Queue<Voto> votosRecibidos;  //1

        public Candidato(){                      //(3)
            votosRecibidos = new ArrayDeque<>();  //1
            this.id=Id_Candidato;                 //1
            Id_Candidato++;                       //1
        }
        public Candidato(String nombre, String partido){ //(5)
            votosRecibidos = new ArrayDeque<>();          //1
            this.nombre=nombre;                           //1
            this.partido=partido;                         //1
            this.id=Id_Candidato;                         //1
            Id_Candidato++;                               //1
        }
        public int getId(){ //(1)
            return id;       //1
        }
        public void setNombre(String nombre){ //(1)
            this.nombre=nombre;                //1
        }
        public String getNombre(){ //(1)
            return nombre;          //1
        }
        public void setPartido(String partido){ //(1)
            this.partido=partido;                //1
        }
        public String getPartido(){ //(1)
            return partido;          //1
        }
        public void AgregarVoto(Voto v){ //(1)
            votosRecibidos.add(v);        //1
        }
        public int getCantidadVotos(){    //(1)
            return votosRecibidos.size();  //1
        }
        public void decrementarId(){ //(1)
            this.id--;                //1
        }
    }
    public static class Votante{ //clase de complejidad (11) = O(1)
        private int id;         //1
        private String nombre;  //1
        private Boolean yaVoto; //1

        public Votante(){ //(1)
            yaVoto=false;  //1
        }
        public void setId(int id){ //(1)
            this.id=id;  //1
        }
        public int getId(){ //(1)
            return id;  //1
        }
        public void setNombre(String nombre){ //(1)
            this.nombre=nombre;  //1
        }
        public String getNombre(){ //(1)
            return nombre;  //1
        }
        public void marcarComoVotado(){ //(1)
            this.yaVoto=true;  //1
        }
        public Boolean getYaVoto(){ //(1)
            return yaVoto;  //1
        }
        public void Print(){ //(1)
            System.out.println("Nombre: " + getNombre() +    //1
                            "\nId: " + getId() +
                            "\nYa Votó: " + getYaVoto());
        }
    }
    public static class Voto{ //clase de complejidad (21) = O(1)
        private int id;            //1
        private int votanteId;     //1
        private int candidatoId;   //1
        private String timeStamp;  //1

        public Voto(){     //(3)
            this.id=ident;  //1
            ident++;        //1
            setTimeStamp(); //1
        }
        public Voto(int votanteId, int candidatoId){ //(5)
            this.id=ident;                  //1
            this.votanteId=votanteId;       //1
            this.candidatoId=candidatoId;   //1
            ident++;                        //1
            setTimeStamp();                 //1
        }
        public int getId(){ //(1)
            return id;       //1
        }
        public void setVotanteId(int votanteId){ //(1)
            this.votanteId=votanteId;             //1
        }
        public int getVotanteId(){ //(1)
            return votanteId;       //1
        }
        public void setCandidatoId(int candidatoId){ //(1)
            this.candidatoId=candidatoId;             //1
        }
        public int getCandidatoId(){ //(1)
            return candidatoId;       //1
        }
        public void setTimeStamp(){ //(3)
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss"); //1
            LocalTime horaActual = LocalTime.now();                                  //1
            this.timeStamp = horaActual.format(formatoHora);                         //1
        }
        public String getTimeStamp(){ //(1)
            return timeStamp;          //1
        }
    }
    public static void main(String[] args){
        UrnaElectoral Urna = new UrnaElectoral();
        Scanner sc = new Scanner(System.in);
        int x;
        System.out.println("Ajustes de la votacion");
        while(true){
            System.out.print("\nSelecciona una opcion para la proxima votacion:" +
                        "\n1) Añadir Candidato." +
                        "\n2) Eliminar Candidato." +
                        "\n3) Ver Lista de Candidatos." +
                        "\n4) Ver Votos." +
                        "\n5) Iniciar Votacion." +
                        "\n0) Cerrar Programa..." +
                        "\nOpción: ");
            x=sc.nextInt();
            switch(x){
                case 1: //Añadir Candidato
                    System.out.print("Ingrese nombre del candidato: ");
                    sc.nextLine();
                    String nombre=sc.nextLine();
                    System.out.print("Ingrese partido del candidato: ");
                    String partido=sc.nextLine();
                    Candidato nuevoCandidato = new Candidato(nombre, partido);
                    Urna.agregarCandidato(nuevoCandidato);
                    System.out.println("Candidato agregado con éxito.");
                    break;
                    
                case 2: //Borrar Candidato
                    if(Urna.lista==null){
                        System.out.println("No hay candidatos registrados.");
                        break;
                    }
                    System.out.print("Ingrese el ID del candidato que desea eliminar: ");
                    int idEliminar=sc.nextInt();
                    Urna.eliminarCandidato(idEliminar);
                    break;
                    
                case 3: //Print Lista de Candidatos
                    System.out.println("\nLista de candidatos:");
                    Urna.PrintListaCandidatos();
                    break;
                    
                case 4: //Ver Votos
                    if(Urna.getHistorialVotos()!=null || Urna.getVotosReportados()!=null){
                        Urna.PrintVotos();
                    }
                    else{
                        System.out.println("No hay votos registrados.");
                    }
                    break;
                    
                case 5: //Iniciar la Votacion
                    Boolean T=false;
                    if(Urna.lista!=null){
                        T=true;
                    }
                    else{
                        System.out.println("\nNo se registraron candidatos para la votacion.");
                        break;
                    }
                    while(T){
                        System.out.print("\nComienza la votacion, seleccione una opcion:" + 
                                      "\n1) Ver Lista de Candidatos." +
                                      "\n2) Registrar Voto." +
                                      "\n3) Finalizar Votacion." +
                                      "\nOpción: ");
                        int z=sc.nextInt();
                        sc.nextLine();
                        switch (z) {
                            case 1: //Ver Lista de Candidatos
                                System.out.println("\nLista de candidatos:");
                                Urna.PrintListaCandidatos();
                                break;
                                
                            case 2: //Registrar Voto
                                Votante votante = new Votante();
                                
                                System.out.print("Ingrese nombre del votante: ");
                                sc.nextLine();
                                String nombreVotante=sc.nextLine();
                                votante.setNombre(nombreVotante);
                                
                                System.out.print("Ingrese rut del votante (sin puntos ni guión): ");
                                int idVotante=sc.nextInt();
                                votante.setId(idVotante);
                                
                                System.out.print("Ingrese número del candidato a votar: ");
                                int idCandidato=sc.nextInt();
                                Urna.registrarVoto(votante, idCandidato);
                                break;
                                
                            case 3: //Finalizar Votacion
                                T=false;
                                System.out.println("\nResultados de la votación:");
                                Urna.PrintResultados();
                                break;
                        }
                    }
                    break;
                    
                default:
                    System.out.println("\n|Programa finalizado|");
                    sc.close();
                    return;
            }
        }
    }
}