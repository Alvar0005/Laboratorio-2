import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
public class Main {
    static int ident = 10000;
    static int Id_Candidato = 1;

    public static class UrnaElectoral {
        public listaCandidatos lista;

        public static class listaCandidatos {
            public Candidato candidato;
            public listaCandidatos siguiente;

            public listaCandidatos(Candidato candidato) {
                this.candidato = candidato;
                this.siguiente = null;
            }
        }

        private Stack<Voto> historialVotos;
        private Queue<Voto> votosReportados;
        private int idCounter;

        public UrnaElectoral() {
            this.historialVotos = new Stack<>();
            this.votosReportados = new ArrayDeque<>();
            this.idCounter = 0;
            lista = null;
        }

        public Boolean verificarVotante(Votante votante) {
            return votante.getYaVoto();
        }

        public Boolean registrarVoto(Votante votante, int candidatoId) {
            if (verificarVotante(votante)) {
                System.out.println("No puede votar de nuevo.");
                return false;
            } else {
                Voto voto = new Voto(votante.getId(), candidatoId);
                historialVotos.push(voto);
                Candidato candidato = buscarCandidato(candidatoId);
                if (candidato != null) {
                    candidato.AgregarVoto(voto);
                }
                votante.marcarComoVotado();
                System.out.println("Se ha registrado el voto.");
                return true;
            }
        }

        public void agregarCandidato(Candidato candidato) {
            listaCandidatos nuevoNodo = new listaCandidatos(candidato);
            if (lista == null) {
                lista = nuevoNodo;
            } else {
                listaCandidatos actual = lista;
                while (actual.siguiente != null) {
                    actual = actual.siguiente;
                }
                actual.siguiente = nuevoNodo;
            }
        }

        public Candidato buscarCandidato(int id) {
            listaCandidatos actual = lista;
            while (actual != null) {
                if (actual.candidato.getId() == id) {
                    return actual.candidato;
                }
                actual = actual.siguiente;
            }
            return null;
        }

        public void Print() {
            listaCandidatos actual = lista;
            while (actual != null) {
                System.out.println("ID: " + actual.candidato.getId() +
                        ")\nNombre: " + actual.candidato.getNombre() +
                        " | Partido: " + actual.candidato.getPartido());
                actual = actual.siguiente;
            }
        }
    }
    public static class Candidato {
        private int id;
        private String nombre;
        private String partido;
        private Queue<Voto> votosRecibidos;

        public Candidato() {
            votosRecibidos = new ArrayDeque<>();
            this.id = Id_Candidato;
            Id_Candidato++;
        }

        public Candidato(String nombre, String partido) {
            this();
            this.nombre = nombre;
            this.partido = partido;
        }

        public int getId() {
            return id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void setPartido(String partido) {
            this.partido = partido;
        }

        public String getPartido() {
            return partido;
        }

        public void AgregarVoto(Voto v) {
            votosRecibidos.add(v);
        }

        public int getCantidadVotos() {
            return votosRecibidos.size();
        }
    }
    public static class Votante {
        private int id;
        private String nombre;
        private Boolean yaVoto;

        public Votante() {
            yaVoto = false;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        public void marcarComoVotado() {
            this.yaVoto = true;
        }

        public Boolean getYaVoto() {
            return yaVoto;
        }

        public void Print() {
            System.out.println("Nombre: " + getNombre() +
                    "\nId: " + getId() +
                    "\nYa Votó: " + getYaVoto());
        }
    }
    public static class Voto {
        private int id;
        private int votanteId;
        private int candidatoId;
        private String timeStamp;

        public Voto() {
            this.id = ident;
            ident++;
            setTimeStamp();
        }

        public Voto(int votanteId, int candidatoId) {
            this();
            this.votanteId = votanteId;
            this.candidatoId = candidatoId;
        }

        public int getId() {
            return id;
        }

        public void setVotanteId(int votanteId) {
            this.votanteId = votanteId;
        }

        public int getVotanteId() {
            return votanteId;
        }

        public void setCandidatoId(int candidatoId) {
            this.candidatoId = candidatoId;
        }

        public int getCandidatoId() {
            return candidatoId;
        }

        public void setTimeStamp() {
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime horaActual = LocalTime.now();
            this.timeStamp = horaActual.format(formatoHora);
        }

        public String getTimeStamp() {
            return timeStamp;
        }
    }
    public static void main(String[] args) {
        UrnaElectoral Urna = new UrnaElectoral();
        Scanner sc = new Scanner(System.in);
        int x;
        Boolean T = true;

        System.out.println("Ajustes de la votacion");
        while (true) {
            System.out.print("\nSelecciona una opcion para la proxima votacion:" +
                    "\n1) Añadir Candidato." +
                    "\n2) Eliminar Candidato." +
                    "\n3) Ver Lista de Candidatos" +
                    "\n4) Iniciar Votacion" +
                    "\n0) Cerrar Programa..." +
                    "\nOpción: ");
            x = sc.nextInt();

            switch (x) {
                case 1: //Añadir Candidato
                    System.out.print("Ingrese nombre del candidato: ");
                    sc.nextLine();
                    String nombre = sc.nextLine();
                    System.out.print("Ingrese partido del candidato: ");
                    String partido = sc.nextLine();
                    Candidato nuevoCandidato = new Candidato(nombre, partido);
                    Urna.agregarCandidato(nuevoCandidato);
                    System.out.println("Candidato agregado con éxito.");
                    break;

                case 2: //Borrar Candidato
                    System.out.print("Ingrese el ID del candidato que desea eliminar: ");
                    sc.nextLine();
                    int idEliminar = sc.nextInt();
                    UrnaElectoral.listaCandidatos actual = Urna.lista;
                    UrnaElectoral.listaCandidatos anterior = null;

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
                    T=true;
                    while (T) {
                        System.out.print("\nComienza la votacion, seleccione una opcion:" +
                                "\n1) Ver Lista de Candidatos." +
                                "\n2) Registrar Voto." +
                                "\n3) Finalizar Votacion." +
                                "\nOpción: ");
                        sc.nextLine();
                        int z = sc.nextInt();

                        switch (z) {
                            case 1: //Ver Lista de Candidatos
                                System.out.println("\nLista de candidatos:");
                                Urna.Print();
                                break;

                            case 2: //Registrar Voto
                                Votante votante = new Votante();

                                System.out.print("Ingrese nombre del votante: ");
                                sc.nextLine();
                                String nombreVotante = sc.nextLine();
                                sc.nextLine();
                                votante.setNombre(nombreVotante);

                                System.out.print("Ingrese rut del votante (sin puntos ni guión): ");
                                int idVotante = sc.nextInt();
                                sc.nextLine();
                                votante.setId(idVotante);

                                System.out.print("Ingrese número del candidato a votar: ");
                                int idCandidato = sc.nextInt();
                                sc.nextLine();
                                Urna.registrarVoto(votante, idCandidato);
                                break;

                            case 3: //Finalizar Votacion
                                T=false;
                                System.out.println("\nResultados de la votación:");
                                UrnaElectoral.listaCandidatos current = Urna.lista;
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
