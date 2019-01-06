package client;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Checkerboard extends JPanel {
	public static final int GAME_WIDTH = 630;
	public static final int DIAMETER = GAME_WIDTH / 9;
	public static final int GAME_HEIGHT = DIAMETER * 10;
	
	public Piece[][] pieceList ;
	
	
	public Checkerboard(){
		this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));//���ÿ��
		initPiece(false);//��ʼ������
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;  //g��Graphics����
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//�򿪿����
		
		g.setColor(new Color(255, 255, 255)); //�������̵���ɫ
		g.fillRect(0, 0, 630,700); // �������
		
		g.setStroke(new BasicStroke(2.0f));//���ôֶ�
		g.setColor(Color.red); // ��Ϊ��ɫ
		//����������
		g.drawLine(DIAMETER / 2, DIAMETER / 2, DIAMETER / 2,DIAMETER * 9 + DIAMETER / 2);
		g.drawLine(DIAMETER / 2 + DIAMETER * 8, DIAMETER / 2, DIAMETER / 2 + DIAMETER * 8,DIAMETER * 9 + DIAMETER / 2);
		
		// ������
		for (int i = DIAMETER / 2; i <= GAME_WIDTH - (DIAMETER/2) ; i+=DIAMETER) {
			g.drawLine(i, DIAMETER / 2, i,(DIAMETER * 4 + DIAMETER / 2));
			g.drawLine(i,(DIAMETER * 5 + DIAMETER / 2),i,GAME_HEIGHT - DIAMETER / 2);
		}
		
		// ������
		for (int i = DIAMETER / 2; i <= GAME_HEIGHT - (DIAMETER/2) ; i+=DIAMETER) {
			g.drawLine(DIAMETER / 2,i,GAME_WIDTH - DIAMETER / 2,i);
		}
		
		//�����Ӻ���
		g.setFont(new Font("����",Font.BOLD,50));
		g.drawString("����",120,365);//���Ƴ��Ӻ���
		g.drawString("����",400,365);
		
		
		//��Сб��
		g.drawLine(245, 35, 385, 175);
		g.drawLine(385, 35, 245, 175);
		g.drawLine(385, 525, 245, 665);
		g.drawLine(245, 525, 385, 665);
		
		
		//��С���Ӻ��ڱ��
		drawSmallLine(g,0,3);
		drawSmallLine(g,2,3);
		drawSmallLine(g,4,3);
		drawSmallLine(g,6,3);
		drawSmallLine(g,8,3);
		drawSmallLine(g,0,6);
		drawSmallLine(g,2,6);
		drawSmallLine(g,4,6);
		drawSmallLine(g,6,6);
		drawSmallLine(g,8,6);
		drawSmallLine(g,1,2);
		drawSmallLine(g,7,2);
		drawSmallLine(g,1,7);
		drawSmallLine(g,7,7);
		
		//����Χ����
		g.setStroke(new BasicStroke(8.0f));	
		g.drawLine(25, 25, GAME_WIDTH - 25,25);
		g.drawLine(25, 25, 25,GAME_HEIGHT -25);
		g.drawLine(GAME_WIDTH - 25, 25, GAME_WIDTH - 25,GAME_HEIGHT -25);
		g.drawLine(25, GAME_HEIGHT -25, GAME_WIDTH - 25,GAME_HEIGHT -25);
				
		//������
		
		g.setColor(Color.black);// ���û�����ɫ
		Piece piece;
		int x, y;
		for (int i = 0; i < pieceList.length; i++) {
			for (int j = 0; j < pieceList[i].length; j++) {

				// ������Ӵ��
				if ( pieceList[i][j] != null) {
					piece = pieceList[i][j];
					x = piece.getX(); y = piece.getY();
					
					if(x == i && y == j){
						if (piece.getSelection())
							g.setColor(Color.white);
						else
							g.setColor(new Color(200, 100, 50)); // ��Ϊ�ۻ�ɫ
						
						g.fillOval(DIAMETER * x, DIAMETER * y, DIAMETER, DIAMETER);
						
						g.setStroke(new BasicStroke(3.0f));
						g.setColor(Color.black);
						g.drawArc(DIAMETER * x, DIAMETER * y, 70, 70, 0, 360);
						g.setStroke(new BasicStroke(1.0f));
						g.drawArc(DIAMETER * x + 4, DIAMETER * y + 4, 62, 62, 0, 360);
						
						// �ж���Ӫ
						if (piece.getCamp())
							g.setColor(Color.red);
						else
							g.setColor(Color.black);
						
						g.drawString(piece.getName(), DIAMETER * x + 8, DIAMETER * y + 50);
						
					}
				}
			}
		}
	}
	
	/**
	 * ��ʼ������
	 */
	public void initPiece(boolean turnAround) {
		pieceList = new Piece[9][10];
		if(turnAround){
			//����
			pieceList[0][9] = new Piece(0, 9,  false , "܇");
			pieceList[1][9] = new Piece(1, 9,  false , "�R");
			pieceList[2][9] = new Piece(2, 9,  false , "��");
			pieceList[3][9] = new Piece(3, 9,  false , "ʿ");
			pieceList[4][9] = new Piece(4, 9,  false , "��");
			pieceList[5][9] = new Piece(5, 9,  false , "ʿ");
			pieceList[6][9] = new Piece(6, 9,  false , "��");
			pieceList[7][9] = new Piece(7, 9,  false , "�R");
			pieceList[8][9] = new Piece(8, 9,  false , "܇");
			pieceList[1][7] = new Piece(1, 7,  false , "�h");
			pieceList[7][7] = new Piece(7, 7,  false , "�h");
			pieceList[0][6] = new Piece(0, 6,  false , "��");
			pieceList[2][6] = new Piece(2, 6,  false , "��");
			pieceList[4][6] = new Piece(4, 6,  false , "��");
			pieceList[6][6] = new Piece(6, 6,  false , "��");
			pieceList[8][6] = new Piece(8, 6,  false , "��");
			
			//����
			pieceList[0][0] = new Piece(0, 0,  true , "܇");
			pieceList[1][0] = new Piece(1, 0,  true , "�R");
			pieceList[2][0] = new Piece(2, 0,  true , "��");
			pieceList[3][0] = new Piece(3, 0,  true , "��");
			pieceList[4][0] = new Piece(4, 0,  true , "��");
			pieceList[5][0] = new Piece(5, 0,  true , "��");
			pieceList[6][0] = new Piece(6, 0,  true , "��");
			pieceList[7][0] = new Piece(7, 0,  true , "�R");
			pieceList[8][0] = new Piece(8, 0,  true , "܇");
			pieceList[1][2] = new Piece(1, 2,  true , "��");
			pieceList[7][2] = new Piece(7, 2,  true , "��");
			pieceList[0][3] = new Piece(0, 3,  true , "��");
			pieceList[2][3] = new Piece(2, 3,  true , "��");
			pieceList[4][3] = new Piece(4, 3,  true , "��");
			pieceList[6][3] = new Piece(6, 3,  true , "��");
			pieceList[8][3] = new Piece(8, 3,  true , "��");

		}else{
			//����
			pieceList[0][0] = new Piece(0, 0,  false , "܇");
			pieceList[1][0] = new Piece(1, 0,  false , "�R");
			pieceList[2][0] = new Piece(2, 0,  false , "��");
			pieceList[3][0] = new Piece(3, 0,  false , "ʿ");
			pieceList[4][0] = new Piece(4, 0,  false , "��");
			pieceList[5][0] = new Piece(5, 0,  false , "ʿ");
			pieceList[6][0] = new Piece(6, 0,  false , "��");
			pieceList[7][0] = new Piece(7, 0,  false , "�R");
			pieceList[8][0] = new Piece(8, 0,  false , "܇");
			pieceList[1][2] = new Piece(1, 2,  false , "�h");
			pieceList[7][2] = new Piece(7, 2,  false , "�h");
			pieceList[0][3] = new Piece(0, 3,  false , "��");
			pieceList[2][3] = new Piece(2, 3,  false , "��");
			pieceList[4][3] = new Piece(4, 3,  false , "��");
			pieceList[6][3] = new Piece(6, 3,  false , "��");
			pieceList[8][3] = new Piece(8, 3,  false , "��");
			
			//����
			pieceList[0][9] = new Piece(0, 9,  true , "܇");
			pieceList[1][9] = new Piece(1, 9,  true , "�R");
			pieceList[2][9] = new Piece(2, 9,  true , "��");
			pieceList[3][9] = new Piece(3, 9,  true , "��");
			pieceList[4][9] = new Piece(4, 9,  true , "��");
			pieceList[5][9] = new Piece(5, 9,  true , "��");
			pieceList[6][9] = new Piece(6, 9,  true , "��");
			pieceList[7][9] = new Piece(7, 9,  true , "�R");
			pieceList[8][9] = new Piece(8, 9,  true , "܇");
			pieceList[1][7] = new Piece(1, 7,  true , "��");
			pieceList[7][7] = new Piece(7, 7,  true , "��");
			pieceList[0][6] = new Piece(0, 6,  true , "��");
			pieceList[2][6] = new Piece(2, 6,  true , "��");
			pieceList[4][6] = new Piece(4, 6,  true , "��");
			pieceList[6][6] = new Piece(6, 6,  true , "��");
			pieceList[8][6] = new Piece(8, 6,  true , "��");
			
		}
		
		
		this.repaint();
	}
	
	/**
	 * ��������ԱߵĶ���
	 */
	public void drawSmallLine(Graphics g, int i, int j) {
		int x = i * DIAMETER + 35;
		int y = j * DIAMETER + 35;

		//���
		if(i > 0){
			//����
			g.drawLine(x - 16, y - 6, x - 6, y - 6);
			g.drawLine(x - 6, y - 6, x - 6, y - 16);
			//����
			g.drawLine(x - 16, y + 6, x - 6, y + 6);
			g.drawLine(x - 6, y + 6, x - 6, y + 16);
		}
		//�ұ�
		if(i < 8){
			//����
			g.drawLine(x + 16, y - 6, x + 6, y - 6);
			g.drawLine(x + 6, y - 16, x + 6, y - 6);
			//����
			g.drawLine(x + 16, y + 6, x + 6, y + 6);
			g.drawLine(x + 6, y + 6, x + 6, y + 16);
		}

	}

	
}
