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
		this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));//设置宽高
		initPiece(false);//初始化棋子
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;  //g是Graphics对象
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);//打开抗锯齿
		
		g.setColor(new Color(255, 255, 255)); //设置棋盘的颜色
		g.fillRect(0, 0, 630,700); // 填充棋盘
		
		g.setStroke(new BasicStroke(2.0f));//设置粗度
		g.setColor(Color.red); // 设为红色
		//画两边竖线
		g.drawLine(DIAMETER / 2, DIAMETER / 2, DIAMETER / 2,DIAMETER * 9 + DIAMETER / 2);
		g.drawLine(DIAMETER / 2 + DIAMETER * 8, DIAMETER / 2, DIAMETER / 2 + DIAMETER * 8,DIAMETER * 9 + DIAMETER / 2);
		
		// 画竖线
		for (int i = DIAMETER / 2; i <= GAME_WIDTH - (DIAMETER/2) ; i+=DIAMETER) {
			g.drawLine(i, DIAMETER / 2, i,(DIAMETER * 4 + DIAMETER / 2));
			g.drawLine(i,(DIAMETER * 5 + DIAMETER / 2),i,GAME_HEIGHT - DIAMETER / 2);
		}
		
		// 画横线
		for (int i = DIAMETER / 2; i <= GAME_HEIGHT - (DIAMETER/2) ; i+=DIAMETER) {
			g.drawLine(DIAMETER / 2,i,GAME_WIDTH - DIAMETER / 2,i);
		}
		
		//画楚河汉界
		g.setFont(new Font("楷体",Font.BOLD,50));
		g.drawString("楚河",120,365);//绘制楚河汉界
		g.drawString("汉界",400,365);
		
		
		//画小斜线
		g.drawLine(245, 35, 385, 175);
		g.drawLine(385, 35, 245, 175);
		g.drawLine(385, 525, 245, 665);
		g.drawLine(245, 525, 385, 665);
		
		
		//画小卒子和炮标记
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
		
		//画外围粗线
		g.setStroke(new BasicStroke(8.0f));	
		g.drawLine(25, 25, GAME_WIDTH - 25,25);
		g.drawLine(25, 25, 25,GAME_HEIGHT -25);
		g.drawLine(GAME_WIDTH - 25, 25, GAME_WIDTH - 25,GAME_HEIGHT -25);
		g.drawLine(25, GAME_HEIGHT -25, GAME_WIDTH - 25,GAME_HEIGHT -25);
				
		//画棋子
		
		g.setColor(Color.black);// 设置画笔颜色
		Piece piece;
		int x, y;
		for (int i = 0; i < pieceList.length; i++) {
			for (int j = 0; j < pieceList[i].length; j++) {

				// 如果棋子存活
				if ( pieceList[i][j] != null) {
					piece = pieceList[i][j];
					x = piece.getX(); y = piece.getY();
					
					if(x == i && y == j){
						if (piece.getSelection())
							g.setColor(Color.white);
						else
							g.setColor(new Color(200, 100, 50)); // 设为桔黄色
						
						g.fillOval(DIAMETER * x, DIAMETER * y, DIAMETER, DIAMETER);
						
						g.setStroke(new BasicStroke(3.0f));
						g.setColor(Color.black);
						g.drawArc(DIAMETER * x, DIAMETER * y, 70, 70, 0, 360);
						g.setStroke(new BasicStroke(1.0f));
						g.drawArc(DIAMETER * x + 4, DIAMETER * y + 4, 62, 62, 0, 360);
						
						// 判断阵营
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
	 * 初始化棋子
	 */
	public void initPiece(boolean turnAround) {
		pieceList = new Piece[9][10];
		if(turnAround){
			//黑棋
			pieceList[0][9] = new Piece(0, 9,  false , "車");
			pieceList[1][9] = new Piece(1, 9,  false , "馬");
			pieceList[2][9] = new Piece(2, 9,  false , "象");
			pieceList[3][9] = new Piece(3, 9,  false , "士");
			pieceList[4][9] = new Piece(4, 9,  false , "帥");
			pieceList[5][9] = new Piece(5, 9,  false , "士");
			pieceList[6][9] = new Piece(6, 9,  false , "象");
			pieceList[7][9] = new Piece(7, 9,  false , "馬");
			pieceList[8][9] = new Piece(8, 9,  false , "車");
			pieceList[1][7] = new Piece(1, 7,  false , "砲");
			pieceList[7][7] = new Piece(7, 7,  false , "砲");
			pieceList[0][6] = new Piece(0, 6,  false , "卒");
			pieceList[2][6] = new Piece(2, 6,  false , "卒");
			pieceList[4][6] = new Piece(4, 6,  false , "卒");
			pieceList[6][6] = new Piece(6, 6,  false , "卒");
			pieceList[8][6] = new Piece(8, 6,  false , "卒");
			
			//红棋
			pieceList[0][0] = new Piece(0, 0,  true , "車");
			pieceList[1][0] = new Piece(1, 0,  true , "馬");
			pieceList[2][0] = new Piece(2, 0,  true , "相");
			pieceList[3][0] = new Piece(3, 0,  true , "仕");
			pieceList[4][0] = new Piece(4, 0,  true , "将");
			pieceList[5][0] = new Piece(5, 0,  true , "仕");
			pieceList[6][0] = new Piece(6, 0,  true , "相");
			pieceList[7][0] = new Piece(7, 0,  true , "馬");
			pieceList[8][0] = new Piece(8, 0,  true , "車");
			pieceList[1][2] = new Piece(1, 2,  true , "炮");
			pieceList[7][2] = new Piece(7, 2,  true , "炮");
			pieceList[0][3] = new Piece(0, 3,  true , "兵");
			pieceList[2][3] = new Piece(2, 3,  true , "兵");
			pieceList[4][3] = new Piece(4, 3,  true , "兵");
			pieceList[6][3] = new Piece(6, 3,  true , "兵");
			pieceList[8][3] = new Piece(8, 3,  true , "兵");

		}else{
			//黑棋
			pieceList[0][0] = new Piece(0, 0,  false , "車");
			pieceList[1][0] = new Piece(1, 0,  false , "馬");
			pieceList[2][0] = new Piece(2, 0,  false , "象");
			pieceList[3][0] = new Piece(3, 0,  false , "士");
			pieceList[4][0] = new Piece(4, 0,  false , "帥");
			pieceList[5][0] = new Piece(5, 0,  false , "士");
			pieceList[6][0] = new Piece(6, 0,  false , "象");
			pieceList[7][0] = new Piece(7, 0,  false , "馬");
			pieceList[8][0] = new Piece(8, 0,  false , "車");
			pieceList[1][2] = new Piece(1, 2,  false , "砲");
			pieceList[7][2] = new Piece(7, 2,  false , "砲");
			pieceList[0][3] = new Piece(0, 3,  false , "卒");
			pieceList[2][3] = new Piece(2, 3,  false , "卒");
			pieceList[4][3] = new Piece(4, 3,  false , "卒");
			pieceList[6][3] = new Piece(6, 3,  false , "卒");
			pieceList[8][3] = new Piece(8, 3,  false , "卒");
			
			//红棋
			pieceList[0][9] = new Piece(0, 9,  true , "車");
			pieceList[1][9] = new Piece(1, 9,  true , "馬");
			pieceList[2][9] = new Piece(2, 9,  true , "相");
			pieceList[3][9] = new Piece(3, 9,  true , "仕");
			pieceList[4][9] = new Piece(4, 9,  true , "将");
			pieceList[5][9] = new Piece(5, 9,  true , "仕");
			pieceList[6][9] = new Piece(6, 9,  true , "相");
			pieceList[7][9] = new Piece(7, 9,  true , "馬");
			pieceList[8][9] = new Piece(8, 9,  true , "車");
			pieceList[1][7] = new Piece(1, 7,  true , "炮");
			pieceList[7][7] = new Piece(7, 7,  true , "炮");
			pieceList[0][6] = new Piece(0, 6,  true , "兵");
			pieceList[2][6] = new Piece(2, 6,  true , "兵");
			pieceList[4][6] = new Piece(4, 6,  true , "兵");
			pieceList[6][6] = new Piece(6, 6,  true , "兵");
			pieceList[8][6] = new Piece(8, 6,  true , "兵");
			
		}
		
		
		this.repaint();
	}
	
	/**
	 * 画卒和炮旁边的短线
	 */
	public void drawSmallLine(Graphics g, int i, int j) {
		int x = i * DIAMETER + 35;
		int y = j * DIAMETER + 35;

		//左边
		if(i > 0){
			//左上
			g.drawLine(x - 16, y - 6, x - 6, y - 6);
			g.drawLine(x - 6, y - 6, x - 6, y - 16);
			//左下
			g.drawLine(x - 16, y + 6, x - 6, y + 6);
			g.drawLine(x - 6, y + 6, x - 6, y + 16);
		}
		//右边
		if(i < 8){
			//右上
			g.drawLine(x + 16, y - 6, x + 6, y - 6);
			g.drawLine(x + 6, y - 16, x + 6, y - 6);
			//右下
			g.drawLine(x + 16, y + 6, x + 6, y + 6);
			g.drawLine(x + 6, y + 6, x + 6, y + 16);
		}

	}

	
}
