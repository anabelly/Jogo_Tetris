package TETRIS;

// Importações necessárias para funcionar a classe
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

// Importação dos tipos de desenhos para a peça da classe Desenho
import TETRIS.Desenho.TetrisShape;


/*
 *
 * AUTOR: Luana de Souza Bianchini
 * DATA: 26/02/2015
 * CLASSE: QUADRO
 * OBJETIVO: PAINEL DO JOGO
 *
 */

public class Quadro extends JPanel implements ActionListener
{

    // Tamanho do Quadro a ser gerado
    // Em uma tela de 250 x 400, usa-se 5% dos valores
    final int QuadroWidth  = 12;
    final int QuadroHeight = 20;

    // Define o tempo de execução no jogo
    Timer tempo;

    // Define os estados do jogo
    // QUANDO TERMINA DE CAIR
    boolean isCaindoAcabou = false;
    // QUANDO ESTÁ JOGANDO
    boolean isIniciado     = false;
    // QUANDO ESTÁ PAUSADO
    boolean isPausado      = false;

    // Define quantas linhas foram removidas
    int LinhasRemovidas = 0;

    // Define o (x,y) para a peça atual
    int atualX = 0;
    int atualY = 0;

    // Define o status do jogo
    JLabel status;

    // Define o desenho da peça atual
    Desenho atualPeça;

    // Define os tipos de quadros a serem desenhados
    TetrisShape[] quadro;

    // Método construtor da classe, recebendo JFrame principal
    // como parâmetro
    public Quadro (Tetris pai)
    {

        // Define o focus para este JPanel
        setFocusable(true);
        // Instância um novo desenho para a peça atual
        atualPeça = new Desenho();

        // Cria um novo tempo com velocidade 400, para este JPanel
        tempo = new Timer(400, this);
        tempo.start();

        // Obtém o status do jogo com o JFrame de parâmetro
        status = pai.getStatus();

        // Define quantas peças existirão no ambiente
        quadro = new TetrisShape[QuadroWidth * QuadroHeight];

        // Cria um novo KeyListener
        addKeyListener(new TAdapter());

        // Limpa o JPanel
        limparQuadro();
    }

    // Define o padrão da queda da peça no jogo
    public void actionPerformed (ActionEvent e)
    {
        // Se ela acabou de cair então define que ela ainda não acabou de cair
        // e cria um novo desenho, se não, ele desce uma linha a peça
        if (isCaindoAcabou)
        {
            isCaindoAcabou = false;
            novaPeça();
        }
        else
        {
            descerUmaLinha();
        }
    }

    // Define o tamanho de cada quadrado em Largura x Altura
    int quadradoWidth() { return (int) getSize().getWidth() / QuadroWidth; }
    int quadradoHeight() { return (int) getSize().getHeight() / QuadroHeight; }
    // Define em que posição (x,y) a peça será desenhada
    TetrisShape desenharEm(int x, int y) { return quadro[(y * QuadroWidth) + x]; }

    // Começa os eventos do jogo
    public void start()
    {
        // Se o jogo está pausado, impede de iniciar
        if (isPausado)
        { return; }

        // Define que o jogo iniciou, que a peça não acabou de cair
        // e que não existe ainda nenhum linha removida
        isIniciado = true;
        isCaindoAcabou = false;
        LinhasRemovidas = 0;

        // Limpa o JPanel
        limparQuadro();
        // Cria uma nova peça
        novaPeça();

        // Começa o tempo de execução do jogo
        tempo.start();
    }

    // Pausa os eventos do jogo
    public void pause()
    {
        // Se o jogo está pausado impede de continuar
        if (!isIniciado)
        { return; }

        // Define o pausado como inverso dele
        isPausado = !isPausado;

        // Se continua pausado então para a execução do tempo, e informa
        // que o jogo está pausado, se não, continua a execução do tempo
        // e resgada o status do jogo
        if (isPausado)
        {
            tempo.stop();
            status.setText("Pausa!");
        }
        else
        {
            tempo.start();
            status.setText("Pontuação " + String.valueOf(LinhasRemovidas));
        }

        // Redesenha a tela
        repaint();
    }

    // Desenha a tela
    @Override
    public void paint (Graphics g)
    {
        // Define o método como Graphics
        super.paint(g);

        // Obtém o tamanho do JPanel
        Dimension tamanho = getSize();

        // Define o topo do JPanel
        int quadroTopo = (int) tamanho.getHeight() - QuadroHeight * quadradoHeight();

        // Cria um quadrado Tetris no top do JPanel
        for (int h = 0; h < QuadroHeight; h++)
        {
            for (int w = 0; w < QuadroWidth; w++)
            {
                // Informa onde será desenhado em (w,h)
                TetrisShape desenho = desenharEm(w, QuadroHeight - h - 1);

                // Se existir algum desenho então desenha o quadrado Tetris
                // na tela
                if (desenho != TetrisShape.SemDesenho)
                {
                    desenharQuadrado(g, 0 + w * quadradoWidth(),
                               quadroTopo + h * quadradoHeight(), desenho);
                }
            }
        }

        // Se existir desenho na peça atual
        if (atualPeça.getDesenho() != TetrisShape.SemDesenho)
        {
            // Para cada quadro, define a posição em (x,y) e desenha na tela
            for (int i = 0; i < 4; i++)
            {
                int x = atualX + atualPeça.x(i);
                int y = atualY - atualPeça.y(i);

                desenharQuadrado(g, 0 + x * quadradoWidth(),
                           quadroTopo + (QuadroHeight - y - 1) * quadradoHeight(),
                           atualPeça.getDesenho());
            }
        }
    }

    // Pular tela inteira
    private void pularParaBaixo()
    {
        // Define um novo Y como o Y da peça atual
        int newY = atualY;

        // Enquanto o novo Y for maior que 0
        while (newY > 0)
        {
            // Se ele não conseguir mover a peça pra baixo, então para
            if (!tentarMover(atualPeça, atualX, newY - 1))
            { break; }

            // Decrementa o novo Y
            --newY;
        }

        // Corta a peça
        cortarPeça();
    }

    // Descer uma linha no JPanel
    private void descerUmaLinha()
    {
        // Senão conseguir mover, corta a peça
        if (!tentarMover(atualPeça, atualX, atualY - 1))
        { cortarPeça(); }
    }

    // Limpa o JPanel
    private void limparQuadro()
    {
        // Para cada pixel na tela, define sem desenho
        for (int i = 0; i < QuadroHeight * QuadroWidth; i++)
        {
            quadro[i] = TetrisShape.SemDesenho;
        }
    }

    // Corta a peça
    private void cortarPeça()
    {
        // Verifica a posição de cada quadro do quadrado Tetris
        for (int i = 0; i < 4; ++i)
        {
            int x = atualX + atualPeça.x(i);
            int y = atualY - atualPeça.y(i);

            quadro[(y * QuadroWidth) + x] = atualPeça.getDesenho();
        }

        // Remove a linha completa
        removerLinhaCheia();

        // Se uma peça terminou de cair, cria uma nova peça
        if (!isCaindoAcabou)
        { novaPeça(); }
    }

    // Cria uma nova peça
    private void novaPeça()
    {
        // Seleciona um desenho aleatório
        atualPeça.setDesenhoAleatório();

        // Define o (x,y) da peça
        atualX = QuadroWidth / 2 + 1;
        atualY = QuadroHeight - 1 + atualPeça.minY();

        // Se não conseguir mover a peça, remove o desenho, para o tempo
        // cancela a execução, informa que é fim de jogo
        if (!tentarMover(atualPeça, atualX, atualY))
        {
            atualPeça.setDesenho(TetrisShape.SemDesenho);

            tempo.stop();
            isIniciado = false;

            status.setText("FIM DE JOGO!");
        }

    }

    // Tenta mover a peça para baixo
    private boolean tentarMover(Desenho novaPeça, int newX, int newY)
    {

        // Com base nos valores (x,y) da peça, desenha a peça em (x,y)
        // do JPanel
        for (int i = 0; i < 4; ++i)
        {
            int x = newX + novaPeça.x(i);
            int y = newY - novaPeça.y(i);

            // Retorna que foi impossivel mover
            if (x < 0 || x >= QuadroWidth || y < 0 || y >= QuadroHeight)
            { return false; }
            if (desenharEm(x, y) != TetrisShape.SemDesenho)
            { return false; }
        }

        // Define a peça atual, e o (x,y) atual da peça
        atualPeça = novaPeça;
        atualX    = newX;
        atualY    = newY;

        // Redesenha a peça na tela
        repaint();

        // Retorna que foi possivel mover
        return true;
    }

    // Remove uma linha cheia da tela
    private void removerLinhaCheia()
    {
        // Define linhas completas como zero
        int LinhasCompletas = 0;

        // Percorre para descobrir se a linha está cheia
        for (int i = QuadroHeight - 1; i >= 0; --i)
        {
            boolean linhaEstaCheia = true;

            for (int j = 0; j < QuadroWidth; ++j)
            {
                if (desenharEm(j, i) == TetrisShape.SemDesenho)
                {
                    linhaEstaCheia = false;
                    break;
                }
            }

            if (linhaEstaCheia)
            {
                ++LinhasCompletas;

                for (int k = i; k < QuadroHeight - 1; ++k)
                {
                    for (int j = 0; j < QuadroWidth; ++j)
                    {
                         quadro[(k * QuadroWidth) + j] = desenharEm(j, k + 1);
                    }
                }
            }
        }

        // Verifica quantas linhas foram removidas
        if (LinhasCompletas > 0)
        {
            LinhasRemovidas += LinhasCompletas;

            status.setText("Pontuação " + String.valueOf(LinhasRemovidas));

            isCaindoAcabou = true;
            atualPeça.setDesenho(TetrisShape.SemDesenho);
          
            repaint();
        }
    }

    // Desenha uma peça na tela
    private void desenharQuadrado(Graphics g, int x, int y, TetrisShape desenho)
    {
        Color colors[] = {
            new Color(0, 0, 0), new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = colors[desenho.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, quadradoWidth() - 2, quadradoHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + quadradoHeight() - 1, x, y);
        g.drawLine(x, y, x + quadradoWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + quadradoHeight() - 1,
                         x + quadradoWidth() - 1, y + quadradoHeight() - 1);
        g.drawLine(x + quadradoWidth() - 1, y + quadradoHeight() - 1,
                         x + quadradoWidth() - 1, y + 1);

    }

    // Controla os eventos do teclado
    public class TAdapter extends KeyAdapter
    {
        @Override
         public void keyPressed (KeyEvent e)
         {

             // Se o jogo parar, ou não existir mais desenhos, dá um retorno
             if (!isIniciado || atualPeça.getDesenho() == TetrisShape.SemDesenho)
             { return; }

             // Obtém o código do teclado
             int keycode = e.getKeyCode();

             // Se for P, pausa o jogo e retorna
             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }

             // Se estiver pausado, retorna
             if (isPausado)
             { return; }

             switch (keycode)
             {
                 case KeyEvent.VK_LEFT:
                     tentarMover(atualPeça, atualX - 1, atualY);
                     break;
                 case KeyEvent.VK_RIGHT:
                     tentarMover(atualPeça, atualX + 1, atualY);
                     break;
                 case KeyEvent.VK_DOWN:
                     descerUmaLinha();
                     break;
                 case KeyEvent.VK_C:
                     tentarMover(atualPeça.girarDireita(), atualX, atualY);
                     break;
                 case KeyEvent.VK_Z:
                     tentarMover(atualPeça.girarEsquerda(), atualX, atualY);
                     break;
                 case KeyEvent.VK_SPACE:
                     pularParaBaixo();
                     break;
             }

         }
     }
  
}
