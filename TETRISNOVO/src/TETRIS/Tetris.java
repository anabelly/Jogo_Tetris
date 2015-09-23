package TETRIS;

// Importações necessárias para funcionar a classe
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 *
 * AUTOR: Luana de Souza Bianchini
 * DATA: 26/02/2015
 * CLASSE: TETRIS
 * OBJETIVO: JFRAME PRINCIPAL DO JOGO
 *
 */

public class Tetris extends JFrame
{

    // Cria novo JLabel para o status
    JLabel status;

    // Método construtor da classe
    public Tetris ()
    {
        // Cria um novo JLabel e posiciona ao sul do JFrame
        status = new JLabel("Pontuação 0");
        // Adiciona o JLabel status ao JFrame
        add(status, BorderLayout.SOUTH);

        // Cria uma instância da classe Quadro
        Quadro jogo = new Quadro(this);
        // Define uma borda
        jogo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Adiciona o JPanel Quadro ao JFrame
        add(jogo);
        // Inicia o jogo
        jogo.start();

        // Seta o tamanho desta janela
        setSize(250, 400);
        // Seta o titulo
        setTitle("TETRIS");
        // Define método de sair da tela
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Método para obter o status de pontuação
    public JLabel getStatus ()
    {
        return status;
    }

    // Método main
    public static void main(String[] args)
    {

        // Cria uma instância desse própria classe
        Tetris jogo = new Tetris();

        // Manipula exibição na tela
        jogo.setLocationRelativeTo(null);
        jogo.setVisible(true);

    }
}