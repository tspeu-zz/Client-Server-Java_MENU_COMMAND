//package Cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author JM_B
 */
public class Cliente extends Thread{
    
    DataOutputStream salidaC ;
    DataInputStream entradaC;
    
     public Cliente() {
        
    }
     
    @Override
    public void run() {
        BufferedReader lector  = new BufferedReader(new InputStreamReader(System.in)); 
// Buffer que lee lo escrito por cliente
        
        
    try {
        Socket socket = new Socket("localhost",2000); 
// Se crea el socket del cliente con la conexión local y el puerto 2000
        System.out.println("Conectado con el servidor");

// Buffers de entrada y salida de datos. Necesarios para la comunicaciones entre el cliente y el servidor
        salidaC  = new DataOutputStream (socket.getOutputStream());
        entradaC = new DataInputStream (socket.getInputStream());
           

        int estado= 1;
        String sale= null;
        String user= null;
        String key =null;
        String comandoCl = null;
        boolean vieneOk= true;
        boolean okServe= true;
        boolean keyServe;
    do{            
//user            
        System.out.println(entradaC.readUTF());
        user = lector.readLine();//se lee el teclado
        salidaC.writeUTF(user); //se envia

         vieneOk= entradaC.readBoolean();//recoge el boolean

         if (vieneOk==false){

             System.out.println(entradaC.readUTF());


         }else{
            while(okServe ==true){    
                System.out.println(entradaC.readUTF());                    
                key = lector.readLine();                  
                salidaC.writeUTF(key); 

                keyServe= entradaC.readBoolean();//recoge el boolean

                if (keyServe){
//                        salidaC.writeUTF(key); 

                    do{    

                        switch(estado){   
                            
                            case 1:   
                                String linea; 
//                                boolean finLectura;                               
                                System.out.println(entradaC.readUTF());   //comando
                                comandoCl = lector.readLine(); //envia comando ls
                                salidaC.writeUTF(comandoCl);

                                 linea=entradaC.readUTF();
                                 System.out.println(linea);
                              
                                estado = entradaC.readInt();
                                //estado=1;
                                 break;
                            
                            case 3:
                                System.out.println(entradaC.readUTF());//leo respuesta del servidor
                                comandoCl = lector.readLine(); //envia comando ls
                                salidaC.writeUTF(comandoCl); //nombre del archivo
                                
                                
                               // leerArchivo(comandoCl);
                                
                                //akiwhile
                                  
                                System.out.println(entradaC.readUTF());
                              
                                
                                
                                estado = entradaC.readInt();
                                break;
                        }
                    }while(estado!=-1);                    
                        okServe=false;//para salir      
                }else{
                    System.out.println(entradaC.readUTF());             
                    okServe=true;
                }                  
            }
        }                 
    }while(!vieneOk);           
            
            System.out.println("Desconectando conexión ...");

// Se cierra el socket y los flujos
            entradaC.close();
            salidaC.close();
            socket.close();
       
    } catch (Exception ex) { 
// Si ocurriera alguna excepción, la capturo e informo
            //ex.printStackTrace();
            //System.out.println(ex.getMessage());
            System.out.println("Cerrada sesion cliente");
    }
}
    
    
    public String leerArchivo(String nombre){
        String texto=null;
        
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
 
      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File ( nombre);
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);
 
         // Lectura del fichero
         String linea;
         while((linea=br.readLine())!=null)
            System.out.println(linea);
            texto +=linea;
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
      return texto;  
   }
        
        
        
        
        
    
    
     public static void main(String[] args) {

 // Instancia que representa un cliente
        Cliente cliente = new Cliente();
// Se inicia el hilo del cliente. 
        cliente.start(); 
    }
     
    
    
}


//vieneOk= false;
            //System.out.println("aki me Test");
/*			
            
    do	{
    switch(estado){
            //System.out.println("Introducir un comando (ls/get/exit)"); 
		case 1:
			System.out.println(entradaC.readUTF());
			sale = lector.readLine();
			
			salidaC.writeUTF(sale);


           
		   if(sale.equals("ls")){
				 System.out.println("caso "+estado);
			   System.out.println(entradaC.readUTF());
			   estado=1;
			   break;
		   
		   }
		   else if (sale.equals("get")){
		   
			   System.out.println(entradaC.readUTF());
			   estado=2;
			   break;
		   }
		   else 
		   estado= 1;
		   break;
		   
		   case 2:
				System.out.println(entradaC.readUTF());
		   
				estado=1;
				break;     
 
		   }
		   //if(sale.equals("exit")) estado=-1; 
    }   while(estado == -1);        
*/	