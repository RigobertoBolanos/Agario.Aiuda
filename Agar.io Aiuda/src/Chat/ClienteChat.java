package Chat;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import Chat.ClienteChat;
import Chat.ClienteConexionServidorChat;
import connection.Client;

public class ClienteChat extends JPanel{

	private Logger log = Logger.getLogger(ClienteChat.class);
    private JTextArea mensajesChat;
    private Socket socket;
    
    private int puerto;
    private String host;
    private String usuario;
    
    public ClienteChat(Client cliente) {
    	PropertyConfigurator.configure("log4j.properties"); 
    	// Elementos de la ventana
        mensajesChat = new JTextArea();
        mensajesChat.setEnabled(false); // El area de mensajes del chat no se debe de poder editar
        mensajesChat.setLineWrap(true); // Las lineas se parten al llegar al ancho del textArea
        mensajesChat.setWrapStyleWord(true); // Las lineas se parten entre palabras (por los espacios blancos)
        JScrollPane scrollMensajesChat = new JScrollPane(mensajesChat);
        JTextField tfMensaje = new JTextField("");
        JButton btEnviar = new JButton("Enviar");
        
        
        setLayout(new BorderLayout());
        add(scrollMensajesChat, BorderLayout.CENTER);
        
        JPanel jp1 = new JPanel();
        jp1.setLayout(new GridLayout(1,2));
        jp1.add(tfMensaje);
        jp1.add(btEnviar);
        
        add(jp1, BorderLayout.SOUTH);
     
     // configuracion inicial
        
         host = cliente.getServerIp();
         puerto = ServidorChat.elPuerto;
         usuario = cliente.getNick();
         
        log.info("Quieres conectarte a " + host + " en el puerto " + puerto + " con el nombre de ususario: " + usuario + ".");
        
        // Se crea el socket para conectar con el Sevidor del Chat
        try {
            socket = new Socket(host, puerto);
        } catch (UnknownHostException ex) {
            log.error("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        } catch (IOException ex) {
            log.error("No se ha podido conectar con el servidor (" + ex.getMessage() + ").");
        }
        
        // Accion para el boton enviar
        btEnviar.addActionListener(new ClienteConexionServidorChat(socket, tfMensaje, usuario));
    }
    
    /**
     * Recibe los mensajes del chat reenviados por el servidor
     */
    public void recibirMensajesServidor(){
        // Obtiene el flujo de entrada del socket
        DataInputStream entradaDatos = null;
        String mensaje;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            log.error("Error al crear el stream de entrada: " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("El socket no se creo correctamente. ");
        }
        
        // Bucle infinito que recibe mensajes del servidor
        boolean conectado = true;
        while (conectado) {
            try {
                mensaje = entradaDatos.readUTF();
                mensajesChat.append(mensaje + System.lineSeparator());
            } catch (IOException ex) {
                log.error("Error al leer del stream de entrada: " + ex.getMessage());
                conectado = false;
            } catch (NullPointerException ex) {
                log.error("El socket no se creo correctamente. ");
                conectado = false;
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    //TODO  
//    public static void main(String[] args) {
//        // Carga el archivo de configuracion de log4J
//        PropertyConfigurator.configure("log4j.properties");        
//              
//        ClienteChat c = new ClienteChat("juan manuel");
//        
//        JFrame pan = new JFrame();
//        pan.add(c);
//        pan.setVisible(true);
//        
//        c.recibirMensajesServidor();
//    }
    
}
