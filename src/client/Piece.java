package client;
/**
 * ������
 * ��ȡ��������
 * ���״̬�����ݵ�
 * @author lenovo
 */
public class Piece {

	private int x ;
	private int y;
	private boolean camp ;//��Ӫ true�Ǻ���,false�Ǻ���
	private String name; //��������
	private boolean Selection; //���ӵ�ѡ��״̬ true ѡ��  , false δѡ��
	
	public Piece(int x, int y, boolean camp,String name) {
		super();
		this.x = x;
		this.y = y;
		this.camp = camp;
		this.name = name;
		this.Selection = false;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean getCamp() {
		return camp;
	}
	public void setCamp(boolean camp) {
		this.camp = camp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getSelection() {
		return Selection;
	}
	public void setSelection(boolean selection) {
		Selection = selection;
	}
}
