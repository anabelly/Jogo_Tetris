package TETRIS;

import java.util.Random;

public class Desenho
{

    // Define tipos de desenho das peças
    enum TetrisShape { SemDesenho, ZDesenho, SDesenho, LinhaDesenho,
                TDesenho, QuadradoDesenho, LDesenho, EspelhadoLDesenho }

    // Cria uma peça
    private TetrisShape peçaDesenho;
    // Cria coordenadas do desenho
    private int coordenadas[][];
    // Cria tabela com coordenadas
    private int[][][] tabelaCoordenadas;

    // Método construtor que zera as coordenadas, e zera o desenho
    public Desenho ()
    {
        // Nova coordenada de 4 x 2, onde 4 representa quatro quadrados
        // do desenho e 2 representa o plano (x,y)
        coordenadas = new int[4][2];
        // Define como sem desenho
        setDesenho(TetrisShape.SemDesenho);
    }

    // Seta um desenho para a peça, "desenha" as coordenadas de um desenho
    public void setDesenho (TetrisShape desenho)
    {
        // Mapa de desenhos possíveis para peça, cada linha representa um desenho
        // cada coluna representa uma peça, cada cédula representa o plano (x,y)
        tabelaCoordenadas = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        // Seta as coordenadas do desenho
        for (int i = 0; i < 4 ; i++)
        {
            for (int j = 0; j < 2; ++j)
            {
                coordenadas[i][j] = tabelaCoordenadas[desenho.ordinal()][i][j];
            }
        }

        // Define a peça com o desenho
        peçaDesenho = desenho;
    }

    // Manipula o (x,y) do desenho
    private void setX(int index, int x) { coordenadas[index][0] = x; }
    private void setY(int index, int y) { coordenadas[index][1] = y; }

    // Retorna o (x,y) do desenho
    public int x(int index) { return coordenadas[index][0]; }
    public int y(int index) { return coordenadas[index][1]; }

    // Retorna o desenho
    public TetrisShape getDesenho()  { return peçaDesenho; }

    // Cria um desenho aleatório
    public void setDesenhoAleatório()
    {
        // Define um Random
        Random r = new Random();

        // Calcula valor randômico de 0 a 7
        int x = Math.abs(r.nextInt()) % 7 + 1;

        // Cria um quadro com desenhos possiveis
        TetrisShape[] valor = TetrisShape.values();
        // Seta somente o desenho gerado pelo numero randômico
        setDesenho(valor[x]);
    }

    // Retorna a coordenada minima de x
    public int minX()
    {
      int min = coordenadas[0][0];

      for (int i = 0; i < 4; i++)
      {
          min = Math.min(min, coordenadas[i][0]);
      }
     
      return min;
    }

    // Retorna a coordenada minima de y
    public int minY()
    {
      int min = coordenadas[0][1];

      for (int i = 0; i < 4; i++)
      {
          min = Math.min(min, coordenadas[i][1]);
      }

      return min;
    }

    // Gira o desenho para a esquerda
    public Desenho girarEsquerda ()
    {
        // Se a peça for quadrada não gira, e retorna
        if (peçaDesenho == TetrisShape.QuadradoDesenho)
        { return this; }

        // Cria um resultado
        Desenho resultado = new Desenho();

        // Seta o resultado com a peça
        resultado.peçaDesenho = peçaDesenho;

        // Inverte os valores das coordenadas
        for (int i = 0; i < 4; ++i)
        {
            resultado.setX(i, y(i));
            resultado.setY(i, -x(i));
        }

        // Retorna o desenho invertido
        return resultado;
    }

    // Gira o desenho para a direita
    public Desenho girarDireita ()
    {
        // Se a peça for quadrada não gira, e retorna
        if (peçaDesenho == TetrisShape.QuadradoDesenho)
        { return this; }

        // Cria um resultado
        Desenho resultado = new Desenho();

        // Seta o resultado com a peça
        resultado.peçaDesenho = peçaDesenho;

        // Inverte os valores das coordenadas
        for (int i = 0; i < 4; ++i)
        {
            resultado.setX(i, -y(i));
            resultado.setY(i, x(i));
        }

        // Retorna o desenho invertido
        return resultado;
    }

}