package client;
public class Rule {
	Checkerboard checkerboard ;
	
	public Rule(Checkerboard checkerboard){
		this.checkerboard = checkerboard;
	}
	
	public boolean getRule(Piece piece, int x, int y) {
		boolean isCanStep = false;

		int startX = 0,startY = 0;
		int endX = 0, endY = 0;
		
		if(piece.getX() > x) {
			startX = x; endX = piece.getX();
		}else{
			startX = piece.getX(); endX = x;
		}
		
		if(piece.getY() > y) {
			startY = y; endY = piece.getY();
		}else{
			startY = piece.getY(); endY = y;
		}
		
		if (piece.getName() == "܇") {
			isCanStep = isVehicle(startX, startY, endX, endY);
		}
		
		if (piece.getName() == "�R") {
			isCanStep = isHorse(piece.getX(), piece.getY(), x, y);
		}
		
		return isCanStep;
	}

	/**
	 * ܇�����߹���
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public boolean isVehicle(int startX, int startY, int endX, int endY) {

		if (endX == startX) {// �ж��Ƿ���һ����������

			if (endY >= startY && endY <= 9) {
				for (int i = startY + 1; i < endY; i++) {
					
					if(checkerboard.pieceList[startX][i] != null){
						return false;
					}
				}
				
				return true;
			}

		}else if (endY == startY) {// �ж��Ƿ���һ��������
			if (endX >= startX && endX <= 9) {
				for (int i = startX + 1; i < endX; i++) {
					if (checkerboard.pieceList[i][startY] != null) {
						return false;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * ������߹���
	 */
	public boolean isHorse(int startX, int startY, int endX, int endY) {
		// ��������
		if (endX < startX && endY < startY) {
			// �ߺ���
			if (startX - 2 >= 0) {
				if (checkerboard.pieceList[startX - 1][startY] == null) {// �ж��Ƿ������
					if (endX == (startX - 2) && endY == (startY - 1)) {// ����ж����ӵ��Ƿ����
						return true;
					}
				}
			}

			// ������
			if (startY - 2 >= 0) {
				if (checkerboard.pieceList[startX][startY - 1] == null) {
					if (endX == (startX - 1) && endY == (startY - 2)) {
						return true;
					}
				}
			}
		}

		// ������
		if (endX > startX && endY < startY) {

			if (startX + 2 <= 8) {
				if (checkerboard.pieceList[startX + 1][startY] == null) {
					if (endX == (startX + 2) && endY == (startY - 1)) {
						return true;
					}
				}
			}

			if (startY - 2 >= 0) {
				if (checkerboard.pieceList[startX][startY - 1] == null) {
					if (endX == (startX + 1) && endY == (startY - 2)) {
						return true;
					}
				}
			}
		}

		// ������
		if (endX < startX && endY > startY) {

			if (startX - 2 >= 0) {
				if (checkerboard.pieceList[startX - 1][startY] == null) {
					if (endX == (startX - 2) && endY == (startY + 1)) {
						return true;
					}
				}
			}

			if (startY + 2 <= 9) {
				if (checkerboard.pieceList[startX][startY + 1] == null) {
					if (endX == (startX - 1) && endY == (startY + 2)) {
						return true;
					}
				}
			}

		}

		// ������
		if (endX > startX && endY > startY) {
			if (startX + 2 <= 8) {
				if (checkerboard.pieceList[startX + 1][startY] == null) {
					if (endX == (startX + 2) && endY == (startY + 1)) {
						return true;
					}
				}
			}

			if (startY + 2 <= 9) {
				if (checkerboard.pieceList[startX][startY + 1] == null) {
					if (endX == (startX + 1) && endY == (startY + 2)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	

}
