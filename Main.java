import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
public class Main {
    static int ident=10000;
    static int Id_Candidato=1;

    public static class UrnaElectoral{ //Clase de complejidad (9n + 34) = O(n)
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

        public UrnaElectoral(){                       //O(1)
            this.historialVotos = new Stack<>();       //1
            this.votosReportados = new ArrayDeque<>(); //1
            this.idCounter=0;                          //1
            lista=null;                                //1
        }

        public Boolean verificarVotante(Votante votante){ //O(1)
            return votante.getYaVoto();                    //1
        }

        public Boolean registrarVoto(Votante votante, int candidatoId){ //O(1)
            if(verificarVotante(votante)){                              //1
                System.out.println("No puede votar de nuevo.");         //1
                return false;                                           //1
            }
            else{                                                       //1
                Voto voto = new Voto(votante.getId(), candidatoId);     //1
                historialVotos.push(voto);                              //1
                Candidato candidato=buscarCandidato(candidatoId);       //1
                if(candidato!=null){                                    //1
                    candidato.AgregarVoto(voto);                        //1
                }
                votante.marcarComoVotado();                             //1
                System.out.println("Se ha registrado el voto.");        //1
                return true;                                            //1
            }
        }
        public void agregarCandidato(Candidato candidato){             //(2n + 6)
            listaCandidatos nuevoNodo = new listaCandidatos(candidato); //1
            if(lista==null){                                            //1
                lista=nuevoNodo;                                        //1
            }
            else{                                                       //1
                listaCandidatos actual=lista;                           //1
                while (actual.siguiente!=null){                         //n
                    actual=actual.siguiente;                            //n
                }
                actual.siguiente=nuevoNodo;                             //1
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
        public void Print(){                                             //(3n + 1)
            listaCandidatos actual=lista;                                 //1
            while(actual!=null){                                          //n
                System.out.println("ID: " + actual.candidato.getId() +    //n
                        ")\nNombre: " + actual.candidato.getNombre() +
                        " | Partido: " + actual.candidato.getPartido());
                actual=actual.siguiente;                                  //n
            }
        }
    }
    public static class Candidato{ //clase de complejidad (19) = O(1)
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
        Boolean T=true;
        System.out.println("Ajustes de la votacion");
        while(true){
            System.out.print("\nSelecciona una opcion para la proxima votacion:" +
                    "\n1) Añadir Candidato." +
                    "\n2) Eliminar Candidato." +
                    "\n3) Ver Lista de Candidatos" +
                    "\n4) Iniciar Votacion" +
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
                    System.out.print("Ingrese el ID del candidato que desea eliminar: ");
                    sc.nextLine();
                    int idEliminar=sc.nextInt();
                    UrnaElectoral.listaCandidatos actual=Urna.lista;
                    UrnaElectoral.listaCandidatos anterior=null;
                    while(actual!=null){
                        if(actual.candidato.getId()==idEliminar){
                            if(anterior==null){
                                Urna.lista=actual.siguiente;
                            }
                            else{
                                anterior.siguiente=actual.siguiente;
                            }
                            System.out.println("Candidato eliminado con éxito.");
                            break;
                        }
                        anterior=actual;
                        actual=actual.siguiente;
                    }

                    if(actual==null){
                        System.out.println("No se encontró un candidato con ese ID.");
                    }
                    break;

                case 3: //Print Lista de Candidatos
                    System.out.println("\nLista de candidatos:");
                    Urna.Print();
                    break;

                case 4: //Iniciar la Votacion
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
                        sc.nextLine();
                        int z=sc.nextInt();
                        switch(z){
                            case 1: //Ver Lista de Candidatos
                                System.out.println("\nLista de candidatos:");
                                Urna.Print();
                                break;

                            case 2: //Registrar Voto
                                Votante votante = new Votante();

                                System.out.print("Ingrese nombre del votante: ");
                                sc.nextLine();
                                String nombreVotante=sc.nextLine();
                                sc.nextLine();
                                votante.setNombre(nombreVotante);

                                System.out.print("Ingrese rut del votante (sin puntos ni guión): ");
                                int idVotante=sc.nextInt();
                                sc.nextLine();
                                votante.setId(idVotante);

                                System.out.print("Ingrese número del candidato a votar: ");
                                int idCandidato=sc.nextInt();
                                sc.nextLine();
                                Urna.registrarVoto(votante, idCandidato);
                                break;

                            case 3: //Finalizar Votacion
                                T=false;
                                System.out.println("\nResultados de la votación:");
                                UrnaElectoral.listaCandidatos current=Urna.lista;
                                while(current!=null){
                                    System.out.println("Candidato: " + current.candidato.getNombre() +
                                            "\n| Partido: " + current.candidato.getPartido() +
                                            "\n| Votos: " + current.candidato.getCantidadVotos() + "\n");
                                    current=current.siguiente;
                                }
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
