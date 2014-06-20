package server_gcm_rest;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Eurismar Pires Borges
 */
public class Server_GCM_Rest extends JFrame {

    private static final String API_KEY = "AIzaSyAuMLK2n1TQto8xmnSgMtX1ldBvGjdC4X0";
    JButton btnEnviar = new JButton("Enviar mensagem");
    JTextField tfDiretorio;
    JLabel lblNomeDiretorio = new JLabel("Informe o caminho de onde está o(s) arquivo(s) gerado(s) no emulador.\n Exemplo C:\\teste\\gcm_config_seuemail@gmail.com.txt");
    JLabel lblAssunto = new JLabel("Digite o Assunto da Mensagem");
    JLabel lblMensagem = new JLabel("Digite a Mensagem");
    JLabel lblAutor = new JLabel("Autor");
    
    JTextField tfAssunto = new JTextField("");
    JTextField tfAutor = new JTextField("");    
    JTextField tfMensagem = new JTextField("");
    
    FlowLayout layout;    
    Container container;
    JPanel painelPrincipal = painelPrincipal = new JPanel();
    
    public void adicionaComponentes(){
        painelPrincipal.add(lblNomeDiretorio);
        painelPrincipal.add(tfDiretorio);  
        painelPrincipal.add(lblAutor);
        painelPrincipal.add(tfAutor);        
        painelPrincipal.add(lblAssunto);
        painelPrincipal.add(tfAssunto);                
        painelPrincipal.add(lblMensagem);
        painelPrincipal.add(tfMensagem);        
        painelPrincipal.add(btnEnviar);   
    };
    public Server_GCM_Rest() {        
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));        
        layout = new FlowLayout();
        container = getContentPane();                
        setLayout(layout);                     
        setLocation(50, 50);
        setTitle("Aplicativo para envio de mensagens via GCM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tfDiretorio = new JTextField();
        add(painelPrincipal);
        adicionaComponentes();
        btnEnviar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                 String id = lerTxt();
                 System.out.println("tripa|" + id + "|");
                try {                
                    enviarMensagemGCM(id);
                } catch (IOException ex) {                    
                     JOptionPane.showMessageDialog(null, "Erro:" + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        Server_GCM_Rest frame = new Server_GCM_Rest();
        frame.setVisible(true);
        frame.setSize(800, 400);
    }

    private void enviarMensagemGCM(String idDispositivoGCM) throws IOException {        
            String request = "https://android.googleapis.com/gcm/send";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "key=AIzaSyAuMLK2n1TQto8xmnSgMtX1ldBvGjdC4X0");
            OutputStream out = conn.getOutputStream();
                      
            String assunto = tfAssunto.getText();
            assunto = tfAssunto.getText();
            
            String autor = tfAutor.getText();            
            String mensagem = tfMensagem.getText();

            String dados = "data.assunto=" + assunto + "&data.mensagem=" + mensagem + "&data.autor=" + autor;

            String registration_id = "registration_id=" + idDispositivoGCM;
            String body = dados + "&" + registration_id;
            System.out.println(body);
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            writer.write(body);

            writer.close();
            out.close();

            if (conn.getResponseCode() != 200) {
                System.out.println(conn.getResponseCode() + "-" + conn.getResponseMessage());
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            conn.disconnect();
            JOptionPane.showMessageDialog(null, sb.toString(), "Dados enviados", JOptionPane.INFORMATION_MESSAGE);
        
    }

    public String lerTxt() {                
        String nomeArq = tfDiretorio.getText();
        String linha = "";
        File arq = new File(nomeArq);

        if (arq.exists()) {
            try {
                FileReader reader = new FileReader(nomeArq);
                BufferedReader leitor = new BufferedReader(reader);
                linha = leitor.readLine();
            } catch (Exception erro) {
            }
        } else {
           JOptionPane.showMessageDialog(null,"Arquivo não encontrado" , "Dados enviados", JOptionPane.ERROR_MESSAGE);
        }
        return linha;

    }

    public static void lerDiretorio(String dir, String usuario) {
        File arquivos[];
        File diretorio = new File(dir);
        arquivos = diretorio.listFiles();
        for (int i = 0; i < arquivos.length; i++) {
            String arquivo = arquivos[i].toString();
            if (arquivo.contains("gcm_config")) {
                if (arquivo.contains(usuario)) {
                    System.out.println(arquivo);
                } else {
                    System.out.println(arquivo);
                }
            }
        }
    }
}
