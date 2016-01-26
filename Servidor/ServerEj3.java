//package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author JM_B
 */
public class ServerEj3 extends Thread{    
        Socket skCliente;
        static final int Puerto=2000;
        DataOutputStream salida;
        DataInputStream entrada;
        Map<String, String> UsuarioClaves = new HashMap<String, String>();
        String clave = null;
        String pass = null;
        Process proceso;
        
        
    public ServerEj3(Socket sCliente) throws IOException {
            skCliente=sCliente;
            salida = new DataOutputStream (skCliente.getOutputStream());
            entrada = new DataInputStream (skCliente.getInputStream());
//            UsuarioClaves.put("javier", "secreta");
//            UsuarioClaves.put("admin", "admin");
//            UsuarioClaves.put("user", "1234");
//            UsuarioClaves.put("user1", "qwerty");
    }
    
    
    @Override
    public void run() {
 
    try { 
        String user= null;
        String key = null;
        String usuarioA=null;
        
        boolean bien = true;
        boolean ok = true;
	int correcto = -1;
       
        
        
  do{       
        salida.writeUTF("Introducir usuario (admin/admin)");//envia 
        user= entrada.readUTF(); //entra de cliente
        
        if (user.equalsIgnoreCase("admin")){
            ok=true;
            salida.writeBoolean(ok); 
		
            salida.writeUTF("introducir clave");            
            key = entrada.readUTF();
            
            while(bien){
                if (key.equalsIgnoreCase("admin")){
                    ok=true;
                    salida.writeBoolean(ok); 
                    //salida.writeUTF("HOLA " + user);  
            
                        /*aki hace algo*/       
                    int estado=1;
                    String comando= null;
      

            do{     
                switch(estado){
                case 1:
                 System.out.println("TEST cliente Introduce comando (ls/get/exit)");     
                salida.writeUTF("Introduce comando (ls/get/exit)");
                comando=entrada.readUTF();

                    if(comando.equals("ls")){   
                        System.out.println("\tEl cliente quiere ver el contenido del directorio");
                     
                        String directorios =   leerComando("ls");                    
                        salida.writeUTF(directorios);
    
                        estado=1;
                        salida.writeInt(estado);
                        break;
                        }else if(comando.equals("get")){
                            System.out.println("\tTEST clientee scribe get");
                            String directorios =   leerComando("ls *.txt");
                            salida.writeUTF(directorios);    

                            estado=3;
                            salida.writeInt(estado);
                            break;
                    }else{
                            System.out.println("\tTEST cliente scribe commando mal ");
                            salida.writeUTF("Comando erróneo");
                            
                            estado=1;
                            salida.writeInt(estado);
                            break;
                    }

                case 3://voy a mostrar el archivo
                    System.out.println("\tTEST cliente scribe nombre archivo");
                    salida.writeUTF("Introduce el nombre del archivo");         
                    comando= entrada.readUTF();
                 
                    String archivoSalida = leerArchivo(comando);
                if(comando.equalsIgnoreCase("")){
                       salida.writeUTF("nombre no encontrado"); 
                 
                }    
                else{    
                   
                    System.out.println("TEST cliente "+archivoSalida);
                    salida.writeUTF(archivoSalida); 
                }                      
// Muestor el fichero /*TODO*/          
                     
                    estado=1;
                    salida.writeInt(estado);
                    break;               
                }

                if(comando.equals("exit")) {
                    estado=-1;
                    salida.writeInt(estado);
                }
            }while(estado!=-1);    
                    
                    
                    
                    
                    bien=false;               
                }
                else{
                    ok=false;
                    salida.writeBoolean(ok);    
                    salida.writeUTF("la clave No es correcta"); 
                    salida.writeUTF("Vuelve a introducir la clave");            
                    key = entrada.readUTF();

  
                    
                    
        /*sale todo bien*/            
                    bien=true;
                }
            }
             
        }
        else  {
                ok=false;
                salida.writeBoolean(ok);  
                salida.writeUTF("El usuario "+user+ " no es válido");			
			}

        
}  while(!ok); 
 
 
        skCliente.close(); 
        System.out.println("cierro el cliente ");
// Cierro cliente
           
        } catch (Exception ex) {
// Si ocurriera algún error, lo capturo e informo
        
        System.out.println("Ocurrio un error  con el cliente ");
        ex.getMessage();
    
        }
    }
    
   
    public String leerArchivo(String nombre){
        String texto="";  
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
 
      try {
         archivo = new File ( nombre);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
         String linea;// Lectura del fichero
         while((linea=br.readLine())!=null)
            //System.out.println(linea);
            texto +="\n"+linea;
      }
      catch(Exception e){
         e.printStackTrace();
         System.out.println (e);
         
      }finally{
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
            System.out.println (e2);
         }
      }
      return texto;  
   }
    
    public String leerComando(String cmd){
            String texto= null;
                try {
                    //String cmd = cmd; 
                    String line="";
                    Process proceso  = Runtime.getRuntime().exec(cmd); 
                    BufferedReader input = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                    while ((line = input.readLine()) != null) 
                        {
                            //System.out.println(line);
                            texto +="\n"+line;
                        }   
                    input.close();

                } catch (IOException ioe) {
                        System.out.println (ioe);
                }
    
                return texto;
           
    }
            
               
    public boolean end(){
        boolean finalOk= true;
        return finalOk;
    }     
    
  public static void main( String[] arg ) {
        try{
            // Inicio el servidor en el puerto
            ServerSocket skServidor = new ServerSocket(Puerto);
            System.out.println("Servidor iniciado puerto: " + Puerto );

            while(true){
                 // Se conecta un cliente
            Socket skCliente = skServidor.accept(); 
            System.out.println("Cliente conectado");
            
            
            // Atiendo al cliente mediante un thread
            new ServerEj3 (skCliente).start();
            

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
         
}

