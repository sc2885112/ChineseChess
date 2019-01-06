package client;
/**
 * 棋子类
 * 获取棋子坐标
 * 存活状态，阵容等
 * @author lenovo
 */
public class Piece {

	private int x ;
	private int y;
	private boolean camp ;//阵营 true是红棋,false是黑棋
	private String name; //棋子名称
	private boolean Selection; //棋子的选中状态 true 选中  , false 未选中
	
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
